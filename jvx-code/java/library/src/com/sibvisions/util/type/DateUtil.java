/*
 * Copyright 2009 SIB Visions GmbH
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 *
 * History
 *
 * 26.10.2009 - [HM] - creation
 * 13.02.2010 - [JR] - format with Date and long parameter implemented
 * 24.02.2013 - [JR] - convert timezone implemented
 */
package com.sibvisions.util.type;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import com.sibvisions.util.ImmutableTimestamp;

/**
 * The <code>DateUtil</code> is a utility class for date conversion and for formatting dates
 * as string.
 *  
 * @author Martin Handsteiner
 */
public class DateUtil implements Serializable
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /** The text separator. */
    private static final char TEXTSEPARATOR = '\'';
    
    /** The cache of {@link DateSymbols} by {@link Locale} identifier. */
    private static Map<String, DateSymbols> dateSymbolsByLocale = new HashMap<String, DateSymbols>();
    
    /** The date format. */
    private SimpleDateFormat dateFormat;
    /** The date format. */
    private transient SimpleDateFormat parseDateFormat;
    
    /** The locale of the currently used {@link #dateFormat}. */
    private Locale dateFormatLocale;
    /** The time zone of the currently used {@link #dateFormat}. */
    private TimeZone dateFormatTimeZone;
    
    /** The locale base for creation. */
    private transient Locale creationLocale = null;
    /** The time zone base for creation. */
    private transient TimeZone creationTimeZone = null;
    /** The pattern base for creation. */
    private transient String creationPattern = null;
    
    /** Performance tuning, pattern. */
    private transient List<String>[] parsedPattern = null;
    /** Performance tuning, pattern. */
    private transient boolean[] ignorePattern = null;
    /** Performance tuning, reference. */
    private transient List<String>[] parsedReference = null;

    /** True, if the format should be checked strict. */
    private boolean strictFormatCheck = false;
    /** index of hours. */
    private transient int hoursIndex;
    /** index of amPm. */
    private transient int amPmIndex;
    
    /** The currently used {@link DateSymbols}. */
    private transient DateSymbols dateSymbols = null;
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** 
     * Constructs a new instance of <code>DateUtil</code> with default date format.
     */
    public DateUtil()
    {
        setDatePattern(null, null);
    }

    /** 
     * Constructs a new instance of <code>DateUtil</code> that supports empty Strings and null values.
     * 
     * @param pDateFormat the formatter that should support empty Strings and null values
     * @deprecated since 2.7, use {@link #DateUtil(String)} or {@link #DateUtil(String, Locale)} instead.
     *             Using this function might lead to undefined behavior because of a new caching mechanism based on
     *             the Locale of the DateUtil. However, there is no way to extract the Locale that has been used from
     *             a DateFormat, so it is always treated as using {@link LocaleUtil#getDefault()}.
     */
    @Deprecated
    public DateUtil(DateFormat pDateFormat)
    {
        setDateFormat(pDateFormat);
    }

    /** 
     * Constructs a new instance of <code>DateUtil</code> that supports empty Strings and null values.
     * <p>
     * As no {@link Locale} is specified, {@link LocaleUtil#getDefault()} will always be used.
     * 
     * @param pDatePattern the pattern that should support empty Strings and null values. {@code null} to use the default.
     */
    public DateUtil(String pDatePattern)
    {
        setDatePattern(pDatePattern);
    }

    /** 
     * Constructs a new instance of <code>DateUtil</code> that supports empty Strings and null values.
     * 
     * @param pDatePattern the pattern that should support empty Strings and null values. {@code null} to use the default.
     * @param pLocale the {@link Locale} to use. {@code null} to always use {@link LocaleUtil#getDefault()}.
     */
    public DateUtil(String pDatePattern, Locale pLocale)
    {
        setDatePattern(pDatePattern, pLocale);
    }

    /** 
     * Constructs a new instance of <code>DateUtil</code> that supports empty Strings and null values.
     * 
     * @param pDatePattern the pattern that should support empty Strings and null values. {@code null} to use the default.
     * @param pLocale the {@link Locale} to use. {@code null} to always use {@link LocaleUtil#getDefault()}.
     * @param pTimeZone the {@link TimeZone} to use. {@code null} to always use {@link TimeZoneUtil#getDefault()}.
     */
    public DateUtil(String pDatePattern, Locale pLocale, TimeZone pTimeZone)
    {
        setDatePattern(pDatePattern, pLocale, pTimeZone);
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Gets the index of found name. 
     * @param pLowerPart the part to search
     * @param pNames the names
     * @param pShortNames the short names
     * @return the index.
     */
    private static int findIndex(String pLowerPart, String[] pNames, String[] pShortNames)
    {
        for (int i = 0; i < pNames.length; i++)
        {
            if (pNames[i].equals(pLowerPart))
            {
                return i;
            }
        }
        for (int i = 0; i < pNames.length; i++)
        {
            if (pNames[i].startsWith(pLowerPart))
            {
                return i;
            }
        }
        if (pShortNames != null)
        {
            for (int i = 0; i < pNames.length; i++)
            {
                String shortName = pShortNames[i].replace(".", ""); // newer jdk has Jan., Feb.
                                                                    // Jänner / Januar oder Feber / Februar
                if (pLowerPart.length() <= pNames[i].length() + 2 && shortName.length() > 0 && pLowerPart.startsWith(shortName))
                {
                    return i;
                }
            }
        }
        return -1;
    }
    
    /**
     * Gets the {@link DateSymbols} from the static cache, or creates them if
     * needed.
     * 
     * @param pLocale the {@link Locale} to be used as key.
     * @param pDateFormat the {@link SimpleDateFormat} which is used as source
     *                    for the symbols.
     * @return the {@link DateSymbols} for this locale.
     */
    private static DateSymbols getOrCreateDateSymbols(Locale pLocale, SimpleDateFormat pDateFormat) 
    {
        String localeIdentifier = pLocale.toString();
        
        synchronized(dateSymbolsByLocale)
        {
            DateSymbols dateSymbols = dateSymbolsByLocale.get(localeIdentifier);
            
            if (dateSymbols == null)
            {
                dateSymbols = new DateSymbols(pDateFormat);
                
                dateSymbolsByLocale.put(localeIdentifier, dateSymbols);
            }
            
            return dateSymbols;
        }
    }
    
    /**
     * Delivers also true for surrogate characters.
     * @param pChar the character.
     * @return Delivers also true for surrogate characters.
     */
    private static boolean isLetter(char pChar)
    {
        return pChar >= Character.MIN_SURROGATE || Character.isLetter(pChar);
    }
    
    /**
     * Delivers also true for surrogate characters.
     * @param pChar the character.
     * @return Delivers also true for surrogate characters.
     */
    private static boolean isLetterOrDigit(char pChar)
    {
        return pChar >= Character.MIN_SURROGATE || Character.isLetterOrDigit(pChar);
    }
    
    /**
     * To lower case and replaces umlauts.
     * @param pText the original month
     * @return the converted month.
     */
    private static String toLowerAndWithoutUmlauts(String pText)
    {
        StringBuilder lowerResult = new StringBuilder(pText.length());
        
        char oldCh = Character.MAX_VALUE;
        for (int i = 0, length = pText.length(); i < length; i++)
        {
            char ch = Character.toLowerCase(pText.charAt(i));
            
            switch (ch) 
            {
                case 'ä':
                case 'â':
                case 'á':
                case 'à': lowerResult.append('a'); break;
                case 'ü':
                case 'û':
                case 'ú':
                case 'ù': lowerResult.append('u'); break;
                case 'ö':
                case 'ô':
                case 'ó':
                case 'ò': lowerResult.append('o'); break;
                case 'ê':
                case 'é':
                case 'è': lowerResult.append('o'); break;
                case 'í':
                case 'ì': lowerResult.append('i'); break;
                case 'e': if (oldCh == 'a' || oldCh == 'u' || oldCh == 'o')
                          {
                              break;
                          }
                default:  lowerResult.append(ch); break;
            }
            oldCh = ch;
        }
        
        return lowerResult.toString();
    }

    /**
     * Parses the date from text.
     * 
     * @param pText the text.
     * @return the parsed date.
     * @throws ParseException if there is an error in the conversion
     */
    public Date parse(String pText) throws ParseException
    {
        if (pText == null || pText.length() == 0)
        {
            return null;
        }
        else
        {
            setDatePattern(creationPattern, creationLocale, creationTimeZone);
            
            return parseStringIntern(pText);
        }
    }

    /**
     * Formats the date to text.
     * 
     * @param pDate the date.
     * @return the formatted text.
     */
    public String format(Date pDate)
    {
        if (pDate == null)
        {
            return null;
        }
        else
        {
            setDatePattern(creationPattern, creationLocale, creationTimeZone);
            
            return dateFormat.format(pDate);
        }
    }

    /**
     * Creates a Reference Date.
     */
    private void createReferenceDate()
    {
        parsedPattern = null;
        ignorePattern = null;
        parsedReference = null;

        dateSymbols = getOrCreateDateSymbols(dateFormatLocale, dateFormat);
        
        Calendar cal = TimeZoneUtil.getCalendar(dateFormatTimeZone, dateFormatLocale);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        String pattern = dateFormat.toPattern();
        
        boolean isTextSeparator = false;
        boolean isNonFormatLetter = false;
        StringBuilder correctPattern = new StringBuilder();
        for (char ch : pattern.toCharArray())
        {
            boolean curIsNonFormatLetter = isNonFormatLetter(ch);
            if (!isTextSeparator && curIsNonFormatLetter != isNonFormatLetter)
            {
                isNonFormatLetter = curIsNonFormatLetter;
                correctPattern.append(TEXTSEPARATOR);
            }
            correctPattern.append(ch);
            if (ch == TEXTSEPARATOR)
            {
                isTextSeparator = !isTextSeparator;
            }
        }
        if (!isTextSeparator && isNonFormatLetter)
        {
            correctPattern.append(TEXTSEPARATOR);
        }
        
        pattern = correctPattern.toString();
        parsedPattern = getParsed(pattern, true);

        ignorePattern = new boolean[parsedPattern[0].size()];
        boolean containsDay = false;
        boolean containsDate = false;
        hoursIndex = -1;
        amPmIndex = -1;
        for (int i = 0; i < ignorePattern.length; i++)
        {
            char patChar = parsedPattern[0].get(i).charAt(0);
            
            switch (patChar)
            {
                case 'd':
                case 'D':
                case 'u':
                case 'F': containsDay = true;
                case 'y':
                case 'Y':
                case 'M':
                case 'E':
                case 'W':
                case 'w': containsDate = true; break;
                case 'h':
                case 'H': hoursIndex = i; break;
                case 'a': amPmIndex = i; break;
                default:
            }
        }
        
        if (!containsDate)
        {
            cal.set(Calendar.MONTH, 0);
            cal.set(Calendar.YEAR, 1970);
            cal.set(Calendar.DAY_OF_MONTH, 1);
        }
        
        boolean amPmNotDetectable = hoursIndex >= 0 && amPmIndex < 0 && parsedPattern[0].get(hoursIndex).charAt(0) == 'h';
        if (amPmNotDetectable)
        {
            StringBuilder formatPattern = new StringBuilder(48);
            formatPattern.append(parsedPattern[1].get(0));
            for (int i = 0; i < ignorePattern.length; i++)
            {
                String pat = parsedPattern[0].get(i);
                if (pat.startsWith("h"))
                {
                    pat = pat.toUpperCase();
                }
                formatPattern.append(pat);
                formatPattern.append(parsedPattern[1].get(i + 1));
            }
            boolean lenient = dateFormat.isLenient();
            dateFormat = new SimpleDateFormat(formatPattern.toString(), dateFormatLocale);
            dateFormat.setLenient(lenient);
        }
        
        StringBuilder parsePattern = new StringBuilder(48);
        parsePattern.append(parsedPattern[1].get(0));
        for (int i = 0; i < ignorePattern.length; i++)
        {
            String pat = parsedPattern[0].get(i);
            boolean ignoreWeekDay = containsDay && pat.startsWith("E");
            
            ignorePattern[i] = ignoreWeekDay;
            if (!ignoreWeekDay)
            {
                if (pat.startsWith("E"))
                {
                    cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                }
                if (amPmNotDetectable && pat.startsWith("h"))
                {
                    pat = pat.toUpperCase();
                }
                parsePattern.append(pat);
                parsePattern.append(parsedPattern[1].get(i + 1));
            }
        }
        
        parseDateFormat = new SimpleDateFormat(parsePattern.toString(), dateFormatLocale);
        parseDateFormat.setLenient(false);
        parseDateFormat.setTimeZone(dateFormatTimeZone);

        for (int i = 0, size = parsedPattern[1].size(); i < size; i++)
        {
            parsedPattern[1].set(i, eliminateSingleQuote(parsedPattern[1].get(i)));
        }
        
        String referenceDate = dateFormat.format(cal.getTime());
        parsedReference = getParsed(referenceDate, false);
    }
    
   /**
     * Gets, if the given character is a non format letter.
     * @param pCharacter the character
     * @return true, if the given character is a non format letter.
     */
    private boolean isNonFormatLetter(char pCharacter)
    {
        return isLetter(pCharacter) && "GyYMLwWDdFEuaHkKhmsSzZX".indexOf(pCharacter) < 0;
    }

    /**
     * Gets the current used {@link SimpleDateFormat}
     * This can be used, to get informations like current used date format symbols, and so on.
     * 
     * @return the current used {@link SimpleDateFormat}
     */
    public SimpleDateFormat getSimpleDateFormat()
    {
        setDatePattern(creationPattern, creationLocale, creationTimeZone); // Ensure correct date format for current locale, time zone.

        return dateFormat;
    }
    
    /**
     * Gets the date format.
     * 
     * @return the date format.
     * @deprecated DateFormat should not be used anymore, it lacks on proper get methods for used locale. Use {@link #getSimpleDateFormat()}.
     */
    @Deprecated
    public DateFormat getDateFormat()
    {
        return getSimpleDateFormat();
    }
    
    /**
     * Gets the date format.
     * 
     * @param pDateFormat the date format.
     * @deprecated since 2.7, use {@link #setDatePattern(String)}, {@link #setDatePattern(String, Locale)} or {@link #setDatePattern(String, Locale, TimeZone)} instead.
     *             Using this function might lead to undefined behavior because of a new caching mechanism based on
     *             the Locale of the DateUtil. However, there is no way to extract the Locale that has been used from
     *             a DateFormat, so it is always treated as using {@link LocaleUtil#getDefault()}.
     */
    public void setDateFormat(DateFormat pDateFormat)
    {
        if (pDateFormat == null)
        {
            setDatePattern(null, null);
        }
        else if (pDateFormat instanceof SimpleDateFormat)
        {
            dateFormat = (SimpleDateFormat)pDateFormat;
            dateFormatLocale = LocaleUtil.getDefault();
            dateFormatTimeZone = dateFormat.getTimeZone();
            
            creationLocale = null;
            creationTimeZone = dateFormatTimeZone == TimeZoneUtil.getDefault() ? null : dateFormatTimeZone;
            creationPattern = dateFormat.toPattern();
            
            createReferenceDate();
        }
        else
        {
            throw new IllegalArgumentException("Only SimpleDateFormat is supported!");
        }
    }

    /**
     * Gets the date format pattern.
     * 
     * @return the date format pattern.
     * @see #setDatePattern(String)
     * @see #setDatePattern(String, Locale)
     */
    public String getDatePattern()
    {
        return dateFormat.toPattern();
    }

    /**
     * Gets the locale that is used for creation.
     * Null means, that the {@link LocaleUtil#getDefault()} is used.
     * 
     * @return the locale or null for {@link LocaleUtil#getDefault()}.
     */
    public Locale getLocale()
    {
        return creationLocale;
    }
    
    /**
     * Gets the timeZone that is used for creation.
     * Null means, that the {@link TimeZoneUtil#getDefault()} is used.
     * 
     * @return the time zone or null for {@link TimeZoneUtil#getDefault()}
     */
    public TimeZone getTimeZone()
    {
        return creationTimeZone;
    }
    
    /**
     * Sets the date format pattern.
     * <p>
     * This function will reset the currently set {@link Locale} to {@code null},
     * which means that {@link LocaleUtil#getDefault()} is used.
     * 
     * @param pDatePattern the date format pattern.
     * @see #setDatePattern(String, Locale)
     */
    public void setDatePattern(String pDatePattern)
    {
        setDatePattern(pDatePattern, null, null);
    }
    
    /**
     * Sets the date format pattern.
     * 
     * @param pDatePattern the date format pattern.
     * @param pLocale the {@link Locale} to use. {@code null} to always use {@link LocaleUtil#getDefault()}.
     * @see #setDatePattern(String)
     */
    public void setDatePattern(String pDatePattern, Locale pLocale)
    {
        setDatePattern(pDatePattern, pLocale, null);
    }
    
    /**
     * Sets the date format pattern.
     * 
     * @param pDatePattern the date format pattern.
     * @param pLocale the {@link Locale} to use. {@code null} to always use {@link LocaleUtil#getDefault()}.
     * @param pTimeZone the {@link TimeZone} to use. {@code null} to always use {@link TimeZoneUtil#getDefault()}.
     * @see #setDatePattern(String)
     */
    public void setDatePattern(String pDatePattern, Locale pLocale, TimeZone pTimeZone)
    {
        Locale locale = pLocale;
        if (locale == null)
        {
            locale = LocaleUtil.getDefault();
        }
        
        TimeZone timeZone = pTimeZone;
        if (timeZone == null)
        {
            timeZone = TimeZoneUtil.getDefault();
        }
        
        if (StringUtil.isEmpty(pDatePattern))
        {
            if (creationPattern != null
                    || pLocale != creationLocale || locale != dateFormatLocale
                    || pTimeZone != creationTimeZone || timeZone != dateFormatTimeZone)
            {
                dateFormat = new SimpleDateFormat(getDefaultDateTimePattern(locale), locale);
                dateFormat.setTimeZone(timeZone);
                dateFormatLocale = locale;
                dateFormatTimeZone = timeZone;
                
                creationLocale = pLocale;
                creationTimeZone = pTimeZone;
                creationPattern = null;

                createReferenceDate();
            }
        }
        else
        {
            if (!pDatePattern.equals(creationPattern)
                    || pLocale != creationLocale || locale != dateFormatLocale
                    || pTimeZone != creationTimeZone || timeZone != dateFormatTimeZone)
            {
                dateFormat = new SimpleDateFormat(pDatePattern, locale);
                dateFormat.setTimeZone(timeZone);
                dateFormatLocale = locale;
                dateFormatTimeZone = timeZone;
                
                creationLocale = pLocale;
                creationTimeZone = pTimeZone;
                creationPattern = pDatePattern;

                createReferenceDate();
            }
        }
    }
    
    /**
     * Strict format check defines whether the set pattern should be exactly by format, or be more flexibel in analysing the given date.
     * This has nothing to do with lenient in SimpleDateFormat. Lenient is set to false anyway.
     * Only correct dates are allowed.
     * The following will be allowed without strict check, with locale de_AT and pattern: dd. MMMM.yyyy HH:mm
     *   01.01.2016
     *   01.Januar.2016 00:00
     *   01.Jänner.2016
     *   01.01
     *   01.01.16
     *   01.01
     *   01 01 2016 00 00
     *   010116
     *   01012016
     *   
     * The result will always be 01.Januar.2016 00:00
     *  
     * @return true, if format should be checked strict.
     */
    public boolean isStrictFormatCheck()
    {
        return strictFormatCheck;
    }
    
    /**
     * Strict format check defines whether the set pattern should be exactly by format, or be more flexibel in analysing the given date.
     * This has nothing to do with lenient in SimpleDateFormat. Lenient is set to false anyway.
     * Only correct dates are allowed.
     * The following will be allowed without strict check, with locale de_AT and pattern: dd. MMMM.yyyy HH:mm
     *   01.01.2016
     *   01.Januar.2016 00:00
     *   01.Jänner.2016
     *   01.01
     *   01.01.16
     *   01.01
     *   01 01 2016 00 00
     *   010116
     *   01012016
     *   
     * The result will always be 01.Januar.2016 00:00
     *  
     * @param pStrictFormatCheck true, if format should be checked strict.
     */
    public void setStrictFormatCheck(boolean pStrictFormatCheck)
    {
        strictFormatCheck = pStrictFormatCheck;
    }
    
    /**
     * Gets the correct spelled months depending on the pattern, ignoring umlauts, and korrekt length.
     * @param pPart the part to parse
     * @param pPattern the exact month pattern
     * @return the correct spelled months depending on the pattern, ignoring umlauts, and korrekt length.
     */
    private String getMonthPart(String pPart, String pPattern)
    {
        pPart = pPart.trim();
        
        if (!Character.isDigit(pPart.charAt(0)))
        {
            String[] lowerPart = new String[] {pPart.toLowerCase(), toLowerAndWithoutUmlauts(pPart)};
            for (int i = 0; i < 2; i++)
            {
                int mon = findIndex(lowerPart[i], dateSymbols.lowerMonths[i], dateSymbols.lowerShortMonths[i]) + 1;
                if (mon > 0)
                {
                    if (pPattern.length() == 2 && mon < 10)
                    {
                        return "0" + mon;
                    }
                    else if (pPattern.length() <= 2)
                    {
                        return String.valueOf(mon);
                    }
                    else if (pPattern.length() == 3)
                    {
                        return dateSymbols.shortMonths[mon - 1];
                    }
                    else
                    {
                        return dateSymbols.months[mon - 1];
                    }
                }
            }
        }
        return pPart;
    }
    
    /**
     * Gets the correct spelled weekday depending on the pattern, ignoring umlauts, and korrekt length.
     * @param pPart the part to parse
     * @param pPattern the exact weekday pattern
     * @return the correct spelled weekday depending on the pattern, ignoring umlauts, and korrekt length.
     */
    private String getWeekdayPart(String pPart, String pPattern)
    {
        int index = findIndex(toLowerAndWithoutUmlauts(pPart), dateSymbols.lowerWeekdays, dateSymbols.lowerShortWeekdays);
        if (index >= 0)
        {
            if (pPattern.length() <= 3)
            {
                return dateSymbols.shortWeekdays[index];
            }
            else
            {
                return dateSymbols.weekdays[index];
            }
        }
        return pPart;
    }

    /**
     * Gets the correct spelled era.
     * @param pPart the part to parse
     * @param pPattern the exact weekday pattern
     * @return the correct spelled era.
     */
    private String getErasPart(String pPart, String pPattern)
    {
        String lowerPart = toLowerAndWithoutUmlauts(pPart);
        int length = lowerPart.length();
        int eraLength = Integer.MAX_VALUE;
        int index = -1;
        
        for (int i = 0; i < dateSymbols.lowerEras.length; i++)
        {
            String lowerEra = dateSymbols.lowerEras[i];
            boolean containsAll = true;
            for (int j = 0; containsAll && j < length; j++)
            {
                containsAll = lowerEra.indexOf(lowerPart.charAt(j)) >= 0;
            }
            if (containsAll && lowerEra.length() < eraLength)
            {
                eraLength = lowerEra.length();
                index = i;
            }
        }

        if (index >= 0)
        {
            return dateSymbols.eras[index];
        }
        else
        {
            return pPart;
        }
    }

    /**
     * Gets the correct spelled am pm part.
     * @param pPart the am pm string
     * @param pPattern the pattern
     * @return the correct spelled am pm part.
     */
    private String getAmPmPart(String pPart, String pPattern)
    {
        pPart = toLowerAndWithoutUmlauts(pPart);
        int index = findIndex(pPart, dateSymbols.lowerAmPm, dateSymbols.lowerAmPm);
        if (index >= 0)
        {
            return dateSymbols.amPm[index];
        }
        else if ("am".startsWith(pPart))
        {
            return dateSymbols.amPm[Calendar.AM];
        }
        else if ("pm".startsWith(pPart))
        {
            return dateSymbols.amPm[Calendar.PM];
        }
        return pPart;
    }
    
    /**
     * Gets the best transformation of the date part for the pattern.
     * 
     * @param pPart the date part
     * @param pPattern the pattern
     * @param pReference the the reference date part
     * @return the best transformation of the date part for the pattern
     */
    private String getPart(String pPart, String pPattern, String pReference)
    {
        try
        {
            if (Character.isDigit(pPart.charAt(0)) && pPattern.startsWith("MMM"))
            {
                int number = Integer.parseInt(pPart);
                if (pPattern.length() == 3)
                {
                    return dateSymbols.shortMonths[(number - 1) % 12];
                }
                else
                {
                    return dateSymbols.months[(number - 1) % 12];
                }
            }
            else if (pPattern.toLowerCase().startsWith("yy"))
            {
                if (pReference.length() > pPart.length())
                {
                    int year = Integer.parseInt(pReference.substring(0, pReference.length() - pPart.length()) + pPart);
                    int refYear = Integer.parseInt(pReference);
                    if (pPart.length() == 2 && year >= refYear + 50)
                    {
                        year -= 100;
                    }

                    return String.valueOf(year);
                }
            }
        }
        catch (Exception ex)
        {
            // Do nothing
        }
        
        switch (pPattern.charAt(0))
        {
            case 'M':   return getMonthPart(pPart, pPattern);
            case 'E':   return getWeekdayPart(pPart, pPattern);
            case 'G':   return getErasPart(pPart, pPattern);
            default:    return pPart;
        }
    }

    /**
     * Gets the strict transformation of the date part for the pattern.
     * 
     * @param pPart the date part
     * @param pPattern the pattern
     * @param pReference the the reference date part
     * @return the best transformation of the date part for the pattern
     */
    private String getStrictPart(String pPart, String pPattern, String pReference)
    {
        if (pPattern.startsWith("MMM"))
        {
            return getMonthPart(pPart, pPattern);
        }
        else if (pPattern.startsWith("E"))
        {
            return getWeekdayPart(pPart, pPattern);
        }
        else if (pPattern.startsWith("G"))
        {
            return getErasPart(pPart, pPattern);
        }

        return pPart;
    }
    
    /**
     * Gets the character type depending on format or date parsing.
     * @param pCharacter the character
     * @param pFormat the format
     * @return the character type
     */
    private char getCharacterType(char pCharacter, boolean pFormat)
    {
        if (isLetterOrDigit(pCharacter))
        {
            if (pFormat)
            {
                return pCharacter;
            }
            else
            {
                return Character.isDigit(pCharacter) ? '0' : 'a';
            }
        }
        else if (pFormat && pCharacter == TEXTSEPARATOR)
        {
            return pCharacter;
        }
        else
        {
            return ' ';
        }
    }
    
    /**
     * Eliminates text separator characters.
     * @param pSeparator the separator string
     * @return separator string
     */
    private String eliminateSingleQuote(String pSeparator)
    {
        int index = pSeparator.indexOf(TEXTSEPARATOR);
        
        if (index >= 0)
        {
            StringBuilder result = new StringBuilder(pSeparator.length());
            int start = 0;
            while (index >= 0)
            {
                result.append(pSeparator.substring(start, index));
                
                start = index + 1;
                index = pSeparator.indexOf(TEXTSEPARATOR, start);
                if (index == start)
                {
                    start--;
                }
            }
            result.append(pSeparator.substring(start));
            
            return result.toString();
        }
        
        return pSeparator;
    }
    
    /**
     * Adds a separator and initializes the next separatorPattern. 
     * @param pSeperatorPart the separator part
     * @param pSeparator the separator
     * @param pFormat if a format is parsed.
     * @return the next text separator.
     */
    private String addSeparator(List<String> pSeperatorPart, String pSeparator, boolean pFormat)
    {
        pSeperatorPart.add(pSeparator);
        if (pFormat)
        {
            return null;
        }
        else if (pSeperatorPart.size() < parsedPattern[1].size())
        {
            return parsedPattern[1].get(pSeperatorPart.size());
        }
        else
        {
            return "";
        }
    }
    
    /**
     * Parse and check time zone info.
     * @param pText the text to parse
     * @param pTimeZonePart the result timeZonePart
     * @return the endIndex.
     */
    private int getTimeZonePart(String pText, StringBuilder pTimeZonePart)
    {
        String lowerText = toLowerAndWithoutUmlauts(pText);
     
        int pos = 0;
        if (lowerText.startsWith("gmt"))
        {
            pos = 3;
        }
        if (pos < lowerText.length())
        {
            char sign = lowerText.charAt(pos);
            if (sign == '+' || sign == '-')
            {
                pos++;
            }
            else if (pos > 0)
            {
                sign = '+';
            }
    
            if (pos == 0)
            {
                for (int i = 0; i < dateSymbols.lowerZoneStrings.length; i++)
                {
                    for (int j = 0; j < 5; j++)
                    {
                        if (lowerText.startsWith(dateSymbols.lowerZoneStrings[i][j]))
                        {
                            pTimeZonePart.append(dateSymbols.zoneStrings[i][j]);
                            
                            return pTimeZonePart.length();
                        }
                    }
                }
                if (sign == 'z') // Zulu time support.
                {
                    pTimeZonePart.append("+0000");
                    return 1;
                }
                // Try to parse any String for a Timezone.
                // This will lead in a better exception in case of eg: CETL 16:00 would otherwise lead in a Exception CET CE:16  
                // Downside is, that CET January will not be parsed in case of missing time zone, because January will be parsed as time zone.
                // So we do not use this code for now.
//                else if (isLetter(sign)) // Return the text as timezone, to get a better exception
//                {
//                    while (pos < lowerText.length() - 1 && Character.isLetter(sign))
//                    {
//                        pos++;
//                        sign = lowerText.charAt(pos);
//                    }
//                    System.out.println(pos);
//                    pTimeZonePart.append(pText.substring(0, pos));
//                    
//                    return pos;
//                }
            }
            else 
            {
                char ch = lowerText.charAt(pos);
                if (Character.isDigit(ch))
                {
                    pTimeZonePart.append(sign);
                    do
                    {
                        if (ch == ':')
                        {
                            if (pTimeZonePart.length() < 3)
                            {
                                pTimeZonePart.insert(1, '0');
                            }
                        }
                        else
                        {
                            pTimeZonePart.append(ch);
                        }
                        
                        pos++;
                        if (pos < lowerText.length())
                        {
                            ch = lowerText.charAt(pos);
                        }
                        else
                        {
                            ch = Character.MAX_VALUE;
                        }
                    }
                    while (pTimeZonePart.length() < 5 && (Character.isDigit(ch) || ch == ':'));
                    
                    if (pTimeZonePart.length() < 5)
                    {
                        if (pTimeZonePart.charAt(1) != '0')
                        {
                            pTimeZonePart.insert(1, '0');
                        }
                        while (pTimeZonePart.length() < 5)
                        {
                            pTimeZonePart.append('0');
                        }
                    }
                    
                    return pos;
                }
            }
        }
        if (pos > 0)
        {
            pTimeZonePart.append("+0000");
            return pos;
        }
        else
        {
            return -1;
        }
    }
    
    /**
     * Checks, if the given string is in the list of tags with whitespace.
     *  
     * @param pCheckString the string to check.
     * @return true, if the given string is in the list of tags with whitespace.
     */
    private boolean shouldIgnoreWhiteSpace(String pCheckString)
    {
        for (int x = 0; x < dateSymbols.ignoreSpaceCheck.length; x++)
        {
            if (dateSymbols.ignoreSpaceCheck[x].startsWith(pCheckString))
            {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Parses the date parts and the seperator parts.
     * 
     * @param pText the date
     * @param pFormat true, if it is a format
     * @return the date parts and the seperator parts
     */
    private List<String>[] getParsed(String pText, boolean pFormat)
    {
        List<String> datePart = new ArrayList<String>();
        List<String> seperatorPart = new ArrayList<String>();
        
        String datePattern = getDatePattern();
        boolean hasTimeZonePattern = datePattern.indexOf('Z') >= 0 || datePattern.indexOf('z') >= 0 || datePattern.indexOf('X') >= 0;
        
        int len = pText.length();
        int pos = 0;
        char ch = pText.charAt(0);
        boolean isLetterOrDigit = isLetterOrDigit(ch);
        char characterType = getCharacterType(ch, pFormat);
        boolean isTextSeparator = false;
        String textSeparator = pFormat ? null : parsedPattern[1].get(0);
        for (int i = 1; i < len; i++)
        {
            char newCh = pText.charAt(i);

            boolean whitespaceChecked = false; 
            if (!pFormat && characterType == 'a' && !Character.isLetter(newCh))
            {
                if (shouldIgnoreWhiteSpace(toLowerAndWithoutUmlauts(pText.substring(pos, i + 1))))
                {
                    whitespaceChecked = true;
                    newCh = 'a';
                }
            }
            
            boolean newIsLetterOrDigit = isLetterOrDigit(newCh);
            char newCharacterType = getCharacterType(newCh, pFormat);

            if (pFormat && characterType == TEXTSEPARATOR)
            {
                isTextSeparator = !isTextSeparator;
            }
            
            if (!isTextSeparator)
            {
                // Find textual separator and prevent it is parsed as date part.
                if (!pFormat && !whitespaceChecked 
                        && textSeparator.trim().length() > 0 && pText.substring(i - 1).startsWith(textSeparator))
                {
                    if (isLetterOrDigit && (characterType == '0' || characterType == 'a') && i - 1 - pos > 0)
                    {
                        datePart.add(pText.substring(pos, i - 1));
                        pos = i - 1;
                    }
                    isLetterOrDigit = false; 
                    characterType = ' ';
                    
                    i += textSeparator.length() - 1;
                    if (i < len)
                    {
                        newCh = pText.charAt(i);
                        newIsLetterOrDigit = isLetterOrDigit(newCh);
                        newCharacterType = getCharacterType(newCh, pFormat);
                    }
                    else
                    {
                        newCh = '0';
                        newIsLetterOrDigit = true;
                        newCharacterType = '0';
                    }
                }
                String part = pText.substring(pos, i);
                if (!pFormat)
                {
                    int maxPrecision = 0; // 0 means not a number, >0 means max size of the number for the corresponding parsed pattern
                    char patternChar = Character.MAX_VALUE;
                    boolean isTimeZone = false;
                    if (datePart.size() < parsedPattern[0].size())
                    {
                        patternChar = parsedPattern[0].get(datePart.size()).charAt(0);
                        switch (patternChar)
                        {
                            case 'y': maxPrecision = 4; break;
                            case 'S': maxPrecision = 3; break;
                            case 'z':
                            case 'Z':
                            case 'X': isTimeZone = true;
                            case 'E':
                            case 'a':
                            case 'G': maxPrecision = 0; break;
                            default: maxPrecision = 2;
                        }
                    }
                    
                    // Flexible TimeZone check
                    if (isTimeZone && (newIsLetterOrDigit || newCh == '+' || newCh == '-'))
                    {
                        if (!isLetterOrDigit && !"+".equals(part) && !"-".equals(part) && part.length() > 0)
                        {
                            textSeparator = addSeparator(seperatorPart, part, pFormat);
                            pos = i;
                        }
                        
                        StringBuilder timeZonePart = new StringBuilder(len);
                        
                        int endIndex = getTimeZonePart(pText.substring(pos), timeZonePart);
                        
                        if (endIndex < 0)
                        {
                            if (parsedReference != null)
                            {
                                datePart.add(parsedReference[0].get(datePart.size()));
                            }
                        }
                        else
                        {
                            part = timeZonePart.toString();
                            pos += endIndex;
                            i = pos - 1;
                            newCh = pText.charAt(i);
                            i++;
                            newIsLetterOrDigit = false;
                            newCharacterType = 'z';
                            isLetterOrDigit = true;
                        }
                    }

                    // fill in numbers from reference, if we know that there should be one.
                    if ((newCharacterType == 'a' || ((newCh == '+' || newCh == '-') && hasTimeZonePattern)) && maxPrecision > 0 && patternChar != 'M') 
                    {
                        if (isLetterOrDigit || parsedReference == null)
                        {
                            datePart.add(part);
                            isLetterOrDigit = false;
                            characterType = ' ';
                            pos = i;
                        }
                        else
                        {
                            datePart.add(parsedReference[0].get(datePart.size()));
                        }
                        
                        if (!pFormat && textSeparator.length() > 0 && pText.substring(i).startsWith(textSeparator))
                        {
                            newCh = pText.charAt(i);
                            newIsLetterOrDigit = isLetterOrDigit;
                            newCharacterType = characterType;
                        }
                        else
                        {
                            textSeparator = addSeparator(seperatorPart, "", pFormat);
                            if (!pFormat && textSeparator.length() > 0 && pText.substring(i).startsWith(textSeparator))
                            {
                                newCh = pText.charAt(i);
                                newIsLetterOrDigit = isLetterOrDigit;
                                newCharacterType = characterType;
                            }
                            else
                            {
                                i--;
                                newCh = pText.charAt(i);
                                newIsLetterOrDigit = isLetterOrDigit;
                                newCharacterType = characterType;
                            }
                        }
                    }
                    // if the weekday is missing, add a proper one.
                    else if (isLetterOrDigit 
                             && datePart.size() < parsedPattern[0].size() 
                             && parsedPattern[0].get(datePart.size()).startsWith("E") 
                             && dateSymbols.lowerWeekdays != null
                             && findIndex(toLowerAndWithoutUmlauts(part), dateSymbols.lowerWeekdays, dateSymbols.lowerShortWeekdays) < 0)
                    {
                        datePart.add(parsedReference[0].get(datePart.size())); // Use from reference date.
//                      datePart.add(getWeekdayPart(lowerWeekdays[Calendar.MONDAY], parsedPattern[0].get(datePart.size()))); // use monday.
                        textSeparator = addSeparator(seperatorPart, "", pFormat);
                    }
                    // if it is a large number, delimiters should be filled in automatically based on the max precision of the number. 
//                  if (isLetterOrDigit && newIsLetterOrDigit && characterType == '0' && newCharacterType == '0' && part.length() == maxPrecision)
                    else if (isLetterOrDigit && characterType == '0' && part.length() == maxPrecision)
                    {
                        characterType = '1';
                    }
                }
                
                if (isLetterOrDigit != newIsLetterOrDigit
                        || (isLetterOrDigit && characterType != newCharacterType))
                {
                    if (isLetterOrDigit)
                    {
                        datePart.add(part);

                        if (seperatorPart.size() == 0)
                        {
                            textSeparator = addSeparator(seperatorPart, "", pFormat);
                        }
                        if (isLetterOrDigit == newIsLetterOrDigit && characterType != newCharacterType)
                        {
                            if (pFormat || textSeparator.length() == 0 || !pText.substring(i).startsWith(textSeparator))
                            {
                                textSeparator = addSeparator(seperatorPart, "", pFormat);
                            }
                        }
                    }
                    else
                    {
                        textSeparator = addSeparator(seperatorPart, part, pFormat);
                    }
                    pos = i;
                }
            }
            isLetterOrDigit = newIsLetterOrDigit;
            characterType = newCharacterType;
            ch = newCh;
        }
        String part = pText.substring(pos);
        if (isLetterOrDigit)
        {
            datePart.add(part);
            if (seperatorPart.size() == 0)
            {
                seperatorPart.add("");
            }
            seperatorPart.add("");
        }
        else
        {
            seperatorPart.add(part);
        }

        return new List[] {datePart, seperatorPart};
    }
    
    /**
     * Parses <code>text</code> returning an Date. Some
     * formatters may return null.
     *
     * @param pText String to convert
     * @return Date representation of text
     * @throws ParseException if there is an error in the conversion
     */
    private Date parseStringIntern(String pText) throws ParseException
    {
        try
        {
            List<String>[] parsedDate = getParsed(pText, false);
            
            int datePartCount = parsedDate[0].size();
            int patternSeperatorCount = strictFormatCheck ? parsedDate[1].size() : parsedPattern[0].size();

            String amPmPart = null;
            if (!strictFormatCheck)
            {
                if (amPmIndex >= 0)
                {
                    if (amPmIndex < parsedDate[0].size())
                    {
                        amPmPart = getAmPmPart(parsedDate[0].get(amPmIndex), parsedPattern[0].get(amPmIndex));
                    }
                    else
                    {
                        amPmPart = parsedReference[0].get(amPmIndex);
                    }
                    if (hoursIndex >= 0 && hoursIndex < parsedDate[0].size())
                    {
                        try
                        {
                            int hours = Integer.parseInt(parsedDate[0].get(hoursIndex));
                            boolean h24 = parsedPattern[0].get(hoursIndex).charAt(hoursIndex) == 'H'; 
                            if (hours >= 12)
                            {
                                amPmPart = dateSymbols.amPm[Calendar.PM];
                                if (!h24)
                                {
                                    parsedDate[0].set(hoursIndex, String.valueOf(88 + hours).substring(1));
                                }
                            }
                            else if (h24)
                            {
                                amPmPart = dateSymbols.amPm[Calendar.AM];
                            }
                        }
                        catch (Exception ex)
                        {
                            // Do nothing
                        }
                    }
                }
            }
            
            StringBuilder buffer = new StringBuilder(48);
            if (strictFormatCheck)
            {
                buffer.append(parsedDate[1].get(0));
            }
            else
            {
                buffer.append(parsedPattern[1].get(0));
            }
            for (int i = 0, size = parsedPattern[0].size(); i < size; i++)
            {
                if (!ignorePattern[i])
                {
                    if (i == amPmIndex && amPmPart != null)
                    {
                        buffer.append(amPmPart);
                    }
                    else if (i < datePartCount)
                    {
                        if (strictFormatCheck)
                        {
                            buffer.append(getStrictPart(parsedDate[0].get(i), parsedPattern[0].get(i), parsedReference[0].get(i)));
                        }
                        else
                        {
                            buffer.append(getPart(parsedDate[0].get(i), parsedPattern[0].get(i), parsedReference[0].get(i)));
                        }
                    }
                    else if (!strictFormatCheck)
                    {
                        buffer.append(parsedReference[0].get(i));
                    }
                    if (i < patternSeperatorCount)
                    {
                        if (strictFormatCheck && (i + 1 >= parsedPattern[0].size() || !parsedPattern[0].get(i + 1).startsWith("S")))
                        {
                            buffer.append(parsedDate[1].get(i + 1));
                        }
                        else
                        {
                            buffer.append(parsedPattern[1].get(i + 1));
                        }
                    }
                }
            }

            return new ImmutableTimestamp(parseDateFormat.parse(buffer.toString()).getTime(), creationTimeZone);
        }
        catch (ParseException exception)
        {
            return new ImmutableTimestamp(parseDateFormat.parse(pText).getTime(), creationTimeZone);
        }
        catch (Exception exception)
        {
            throw new ParseException("Wrong Dateformat", 0);
        }
    }

    /**
     * Formats a time string.
     * 
     * @param pDate the time (in millis since 01.01.1970 00:00) value to be formatted into a time string
     * @param pFormat the format 
     * @return the formatted date/time string
     * @see SimpleDateFormat
     */
    public static String format(long pDate, String pFormat)
    {
        return format(new Date(pDate), pFormat);
    }
    
    /**
     * Formats a Date into a date/time string.
     * 
     * @param pDate the time value to be formatted into a time string
     * @param pFormat the format 
     * @return the formatted date/time string
     * @see SimpleDateFormat
     */
    public static String format(Date pDate, String pFormat)
    {
        DateUtil du = new DateUtil(pFormat);
        
        return du.format(pDate);
    }
    
    /**
     * Gets the current date.
     * 
     * @return the current date.
     */
    public static Date getCurrentDate()
    {
        return new Date();
    }
    
    /**
     * Creates a new date instance.
     * 
     * @param pDay the day of month
     * @param pMonth the month
     * @param pYear the year
     * @param pHour the hour of day
     * @param pMinutes the minutes
     * @param pSeconds the seconds
     * @return the date
     * @deprecated since 2.8.5, use {@link #getTimestamp(int, int, int, int, int, int)} instead.
     *             This function has day, month year instead of java standard year, month, day which is confusing and will cause errors.
     */
    @Deprecated
    public static Date getDate(int pDay, int pMonth, int pYear, int pHour, int pMinutes, int pSeconds)
    {
        Calendar cal = TimeZoneUtil.getDefaultCalendar();
        
        cal.set(Calendar.DAY_OF_MONTH, pDay);
        cal.set(Calendar.MONTH, pMonth - 1);
        cal.set(Calendar.YEAR, pYear);
        cal.set(Calendar.HOUR_OF_DAY, pHour);
        cal.set(Calendar.MINUTE, pMinutes);
        cal.set(Calendar.SECOND, pSeconds);
        cal.set(Calendar.MILLISECOND, 0);
        
        return cal.getTime();
    }
    
    /**
     * Gets the current date.
     * 
     * @return the current date.
     */
    public static ImmutableTimestamp getCurrentTimestamp()
    {
        return new ImmutableTimestamp(System.currentTimeMillis());
    }
    
    /**
     * Creates a new date instance.
     * 
     * @param pYear the year
     * @param pMonth the month
     * @param pDay the day of month
     * @return the date
     */
    public static ImmutableTimestamp getTimestamp(int pYear, int pMonth, int pDay)
    {
        return getTimestamp(pYear, pMonth, pDay, 0, 0, 0, 0);
    }
    
    /**
     * Creates a new date instance.
     * 
     * @param pYear the year
     * @param pMonth the month
     * @param pDay the day of month
     * @param pHour the hour of day
     * @param pMinutes the minutes
     * @return the date
     */
    public static ImmutableTimestamp getTimestamp(int pYear, int pMonth, int pDay, int pHour, int pMinutes)
    {
        return getTimestamp(pYear, pMonth, pDay, pHour, pMinutes, 0, 0);
    }
    
    /**
     * Creates a new date instance.
     * 
     * @param pYear the year
     * @param pMonth the month
     * @param pDay the day of month
     * @param pHour the hour of day
     * @param pMinutes the minutes
     * @param pSeconds the seconds
     * @return the date
     */
    public static ImmutableTimestamp getTimestamp(int pYear, int pMonth, int pDay, int pHour, int pMinutes, int pSeconds)
    {
        return getTimestamp(pYear, pMonth, pDay, pHour, pMinutes, pSeconds, 0);
    }
    
    /**
     * Creates a new date instance.
     * 
     * @param pYear the year
     * @param pMonth the month
     * @param pDay the day of month
     * @param pHour the hour of day
     * @param pMinutes the minutes
     * @param pSeconds the seconds
     * @param pMilliSeconds the milliseconds
     * @return the date
     */
    public static ImmutableTimestamp getTimestamp(int pYear, int pMonth, int pDay, int pHour, int pMinutes, int pSeconds, int pMilliSeconds)
    {
        Calendar cal = TimeZoneUtil.getDefaultCalendar();
        
        cal.set(Calendar.YEAR, pYear);
        cal.set(Calendar.MONTH, pMonth - 1);
        cal.set(Calendar.DAY_OF_MONTH, pDay);
        cal.set(Calendar.HOUR_OF_DAY, pHour);
        cal.set(Calendar.MINUTE, pMinutes);
        cal.set(Calendar.SECOND, pSeconds);
        cal.set(Calendar.MILLISECOND, pMilliSeconds);
        
        return new ImmutableTimestamp(cal.getTimeInMillis());
    }

    /**
     * Calculates the years between two dates with decimals for 12 months and 31 days.
     * 
     * @param pEndDate the end date
     * @param pBeginDate the begin date
     * @return the years between two dates with decimals for 12 months and 31 days.
     */
    public static double yearsBetween(Date pEndDate, Date pBeginDate)
    {
        return monthsBetween(pEndDate, pBeginDate) / 12d;
    }

    /**
     * Adds the years to the given date, with decimals for 12 months and 31 days.
     * 
     * @param pDate the date
     * @param pYears the months
     * @return the result date.
     */
    public static ImmutableTimestamp addYears(Date pDate, double pYears)
    {
        return addMonths(pDate, pYears * 12d);
    }
    
    /**
     * Calculates the months between two dates with decimals for 31 days.
     * 
     * @param pEndDate the end date
     * @param pBeginDate the begin date
     * @return the months between two dates with decimals for 31 days.
     */
    public static double monthsBetween(Date pEndDate, Date pBeginDate)
    {
        Calendar cal = TimeZoneUtil.getDefaultCalendar();
        cal.setTime(pEndDate);
        long endMonths = cal.get(Calendar.YEAR) * 12 + cal.get(Calendar.MONTH);
        long endDayTime = (cal.get(Calendar.DAY_OF_MONTH) - 1) * 86400000L + 
                cal.get(Calendar.HOUR_OF_DAY) * 3600000L + 
                cal.get(Calendar.MINUTE) * 60000L +
                cal.get(Calendar.SECOND) * 1000L +
                cal.get(Calendar.MILLISECOND);
        long endLastDayTime = cal.getActualMaximum(Calendar.DAY_OF_MONTH) * 86400000L;
        
        cal.setTime(pBeginDate);
        long beginMonths = cal.get(Calendar.YEAR) * 12 + cal.get(Calendar.MONTH);
        long beginDayTime = (cal.get(Calendar.DAY_OF_MONTH) - 1) * 86400000L + 
                cal.get(Calendar.HOUR_OF_DAY) * 3600000L + 
                cal.get(Calendar.MINUTE) * 60000L +
                cal.get(Calendar.SECOND) * 1000L +
                cal.get(Calendar.MILLISECOND);
        long beginLastDayTime = cal.getActualMaximum(Calendar.DAY_OF_MONTH) * 86400000L;
        
        long diffMonths = endMonths - beginMonths;
        long diffDayTime = endDayTime - beginDayTime;
        
        if (diffMonths < 0)
        {
            if (beginDayTime < endDayTime)
            {
                diffMonths++;
                diffDayTime -= endLastDayTime;
            }
        }
        else if (diffMonths > 0)
        {
            if (beginDayTime > endDayTime)
            {
                diffMonths--;
                diffDayTime += beginLastDayTime;
            }   
        }
        
        return diffMonths + diffDayTime / 2678400000d;
    }
    
    /**
     * Adds the months to the given date, with decimals for 31 days.
     * 
     * @param pDate the date
     * @param pMonths the months
     * @return the result date.
     */
    public static ImmutableTimestamp addMonths(Date pDate, double pMonths)
    {
        Calendar cal = TimeZoneUtil.getDefaultCalendar();
        cal.setTime(pDate);

        long milliSeconds = Math.round(pMonths * 2678400000d);
        long months = milliSeconds / 2678400000L;
        
        milliSeconds -= months * 2678400000L;
        
        int beginDstOffset;
        int endDstOffset;
        if (months < 0)
        {
            beginDstOffset = cal.get(Calendar.DST_OFFSET);

            cal.setTimeInMillis(cal.getTimeInMillis() + milliSeconds);

            endDstOffset = cal.get(Calendar.DST_OFFSET);

            cal.add(Calendar.MONTH, (int)months);
        }
        else
        {
            cal.add(Calendar.MONTH, (int)months);
            
            beginDstOffset = cal.get(Calendar.DST_OFFSET);

            cal.setTimeInMillis(cal.getTimeInMillis() + milliSeconds);

            endDstOffset = cal.get(Calendar.DST_OFFSET);
        }
        
        return new ImmutableTimestamp(cal.getTimeInMillis() + beginDstOffset - endDstOffset);
    }
    
    /**
     * Calculates the months between two dates with decimals for 31 days.
     * 
     * @param pEndDate the end date
     * @param pBeginDate the begin date
     * @return the months between two dates with decimals for 31 days.
     */
    public static double monthsBetweenRelativeToEnd(Date pEndDate, Date pBeginDate)
    {
        Calendar cal = TimeZoneUtil.getDefaultCalendar();
        cal.setTime(pEndDate);
        long endMonths = cal.get(Calendar.YEAR) * 12 + cal.get(Calendar.MONTH);
        long endLastDayTime = cal.getActualMaximum(Calendar.DAY_OF_MONTH) * 86400000L;
        long endDayTime = (cal.get(Calendar.DAY_OF_MONTH) - 1) * 86400000L + 
                cal.get(Calendar.HOUR_OF_DAY) * 3600000L + 
                cal.get(Calendar.MINUTE) * 60000L +
                cal.get(Calendar.SECOND) * 1000L +
                cal.get(Calendar.MILLISECOND) 
                - endLastDayTime;
        
        cal.setTime(pBeginDate);
        long beginMonths = cal.get(Calendar.YEAR) * 12 + cal.get(Calendar.MONTH);
        long beginLastDayTime = cal.getActualMaximum(Calendar.DAY_OF_MONTH) * 86400000L;
        long beginDayTime = (cal.get(Calendar.DAY_OF_MONTH) - 1) * 86400000L + 
                cal.get(Calendar.HOUR_OF_DAY) * 3600000L + 
                cal.get(Calendar.MINUTE) * 60000L +
                cal.get(Calendar.SECOND) * 1000L +
                cal.get(Calendar.MILLISECOND) 
                - beginLastDayTime;
        
        long diffMonths = endMonths - beginMonths;
        long diffDayTime = endDayTime - beginDayTime;
        
        if (diffMonths < 0)
        {
            if (beginDayTime < endDayTime)
            {
                diffMonths++;
                diffDayTime -= beginLastDayTime;
            }
        }
        else if (diffMonths > 0)
        {
            if (beginDayTime > endDayTime)
            {
                diffMonths--;
                diffDayTime += endLastDayTime;
            }   
        }
        
        return diffMonths + diffDayTime / 2678400000d;
    }
    
    /**
     * Adds the months to the given date, with decimals for 31 days.<br>
     * The result date has the same distance to the end of month as the start date.<br> 
     * eg.<br>
     * 2021-01-30 + 1 month = 2021-02-27<br> 
     * (2 days before next month will remain 2 days before next month)<br>
     * 
     * @param pDate the date
     * @param pMonths the months
     * @return the result date.
     */
    public static ImmutableTimestamp addMonthsRelativeToEnd(Date pDate, double pMonths)
    {
        Calendar cal = TimeZoneUtil.getDefaultCalendar();
        cal.setTime(pDate);

        long milliSeconds = Math.round(pMonths * 2678400000d);
        long months = milliSeconds / 2678400000L;
        
        milliSeconds -= months * 2678400000L;
        
        int beginDstOffset;
        int endDstOffset;

        long beginLastDayTime = cal.getActualMaximum(Calendar.DAY_OF_MONTH) * 86400000L;
        long beginDayTime = (cal.get(Calendar.DAY_OF_MONTH) - 1) * 86400000L + 
                cal.get(Calendar.HOUR_OF_DAY) * 3600000L + 
                cal.get(Calendar.MINUTE) * 60000L +
                cal.get(Calendar.SECOND) * 1000L +
                cal.get(Calendar.MILLISECOND) 
                - beginLastDayTime;
        cal.set(Calendar.DAY_OF_MONTH, 1); 
        cal.set(Calendar.HOUR_OF_DAY, 0); 
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        cal.add(Calendar.MONTH, (int)months + 1);
        long endLastDayTime = cal.getActualMaximum(Calendar.DAY_OF_MONTH) * 86400000L;
        
        if (beginDayTime + milliSeconds > 0)
        {
            cal.add(Calendar.MONTH, 1);
            milliSeconds -= endLastDayTime;
        }

        beginDstOffset = cal.get(Calendar.DST_OFFSET);
        
        cal.setTimeInMillis(cal.getTimeInMillis() + beginDayTime + milliSeconds);

        endDstOffset = cal.get(Calendar.DST_OFFSET);
        
        return new ImmutableTimestamp(cal.getTimeInMillis() + beginDstOffset - endDstOffset);
    }
    
    /**
     * Calculates the days between two dates with decimals for 24 hours.
     * 
     * @param pEndDate the end date
     * @param pBeginDate the begin date
     * @return the days between two dates with decimals for 24 hours.
     */
    public static double daysBetween(Date pEndDate, Date pBeginDate)
    {
        Calendar cal = TimeZoneUtil.getDefaultCalendar();
        cal.setTime(pEndDate);
        long endMillis = cal.getTimeInMillis() + cal.get(Calendar.DST_OFFSET);
        
        cal.setTime(pBeginDate);
        long beginMillis = cal.getTimeInMillis() + cal.get(Calendar.DST_OFFSET);
        
        return (endMillis - beginMillis) / 86400000d;
    }
    
    /**
     * Adds the days to the given date, with decimals for 24 hours.
     * 
     * @param pDate the date
     * @param pDays the months
     * @return the result date.
     */
    public static ImmutableTimestamp addDays(Date pDate, double pDays)
    {
        Calendar cal = TimeZoneUtil.getDefaultCalendar();
        cal.setTime(pDate);
        int beginDstOffset = cal.get(Calendar.DST_OFFSET);

        cal.setTimeInMillis(cal.getTimeInMillis() + Math.round(pDays * 86400000d));
        int endDstOffset = cal.get(Calendar.DST_OFFSET);

        return new ImmutableTimestamp(cal.getTimeInMillis() + beginDstOffset - endDstOffset);
    }
    
    /**
     * Calculates the hours between two dates.
     * 
     * @param pEndDate the end date
     * @param pBeginDate the begin date
     * @return the hours between two dates.
     */
    public static double hoursBetween(Date pEndDate, Date pBeginDate)
    {
        return (pEndDate.getTime() - pBeginDate.getTime()) / 3600000d;
    }
    
    /**
     * Adds the hours to the given date.
     * 
     * @param pDate the date
     * @param pHours the hours
     * @return the result date.
     */
    public static ImmutableTimestamp addHours(Date pDate, double pHours)
    {
        return new ImmutableTimestamp(pDate.getTime() + Math.round(pHours * 3600000d));
    }
    
    /**
     * Calculates the minutes between two dates.
     * 
     * @param pEndDate the end date
     * @param pBeginDate the begin date
     * @return the minutes between two dates.
     */
    public static double minutesBetween(Date pEndDate, Date pBeginDate)
    {
        return (pEndDate.getTime() - pBeginDate.getTime()) / 60000d;
    }
    
    /**
     * Adds the minutes to the given date.
     * 
     * @param pDate the date
     * @param pMinutes the minutes
     * @return the result date.
     */
    public static ImmutableTimestamp addMinutes(Date pDate, double pMinutes)
    {
        return new ImmutableTimestamp(pDate.getTime() + Math.round(pMinutes * 60000d));
    }
    
    /**
     * Calculates the seconds between two dates.
     * 
     * @param pEndDate the end date
     * @param pBeginDate the begin date
     * @return the seconds between two dates.
     */
    public static double secondsBetween(Date pEndDate, Date pBeginDate)
    {
        return (pEndDate.getTime() - pBeginDate.getTime()) / 1000d;
    }
    
    /**
     * Adds the seconds to the given date.
     * 
     * @param pDate the date
     * @param pSeconds the seconds
     * @return the result date.
     */
    public static ImmutableTimestamp addSeconds(Date pDate, double pSeconds)
    {
        return new ImmutableTimestamp(pDate.getTime() + Math.round(pSeconds * 1000d));
    }
    
    /**
     * Calculates the milliseconds between two dates.
     * 
     * @param pEndDate the end date
     * @param pBeginDate the begin date
     * @return the milliseconds between two dates.
     */
    public static double millisecondsBetween(Date pEndDate, Date pBeginDate)
    {
        return pEndDate.getTime() - pBeginDate.getTime();
    }
    
    /**
     * Adds the milliseconds to the given date.
     * 
     * @param pDate the date
     * @param pMilliSeconds the milliseconds
     * @return the result date.
     */
    public static ImmutableTimestamp addMilliseconds(Date pDate, double pMilliSeconds)
    {
        return new ImmutableTimestamp(pDate.getTime() + Math.round(pMilliSeconds));
    }

    /**
     * Truncates the date/time part below year.
     * @param pDate the date
     * @return the truncated date
     */
    public static ImmutableTimestamp truncYear(Date pDate)
    {
        return trunc(pDate, Calendar.YEAR);
    }

    /**
     * Truncates the date/time part below month.
     * @param pDate the date
     * @return the truncated date
     */
    public static ImmutableTimestamp truncMonth(Date pDate)
    {
        return trunc(pDate, Calendar.MONTH);
    }

    /**
     * Truncates the date/time part below day.
     * @param pDate the date
     * @return the truncated date
     */
    public static ImmutableTimestamp truncDay(Date pDate)
    {
        return trunc(pDate, Calendar.DAY_OF_MONTH);
    }

    /**
     * Truncates the date/time part below hour.
     * @param pDate the date
     * @return the truncated date
     */
    public static ImmutableTimestamp truncHour(Date pDate)
    {
        return trunc(pDate, Calendar.HOUR_OF_DAY);
    }

    /**
     * Truncates the date/time part below minute.
     * @param pDate the date
     * @return the truncated date
     */
    public static ImmutableTimestamp truncMinute(Date pDate)
    {
        return trunc(pDate, Calendar.MINUTE);
    }

    /**
     * Truncates the date/time part below second.
     * @param pDate the date
     * @return the truncated date
     */
    public static ImmutableTimestamp truncSecond(Date pDate)
    {
        return trunc(pDate, Calendar.SECOND);
    }

    /**
     * The last day of year.
     * @param pDate the date
     * @return the result date
     */
    public static ImmutableTimestamp lastDayOfYear(Date pDate)
    {
        return addDays(addYears(trunc(pDate, Calendar.YEAR), 1), -1);
    }

    /**
     * The last millisecond of year.
     * @param pDate the date
     * @return the result date
     */
    public static ImmutableTimestamp lastMillisecondOfYear(Date pDate)
    {
        return addMilliseconds(addYears(trunc(pDate, Calendar.YEAR), 1), -1);
    }

    /**
     * The last day of month.
     * @param pDate the date
     * @return the result date
     */
    public static ImmutableTimestamp lastDayOfMonth(Date pDate)
    {
        return addDays(addMonths(trunc(pDate, Calendar.MONTH), 1), -1);
    }

    /**
     * The last millisecond of month.
     * @param pDate the date
     * @return the result date
     */
    public static ImmutableTimestamp lastMillisecondOfMonth(Date pDate)
    {
        return addMilliseconds(addMonths(trunc(pDate, Calendar.MONTH), 1), -1);
    }

    /**
     * The last millisecond of day.
     * @param pDate the date
     * @return the result date
     */
    public static ImmutableTimestamp lastMillisecondOfDay(Date pDate)
    {
        return addMilliseconds(addDays(trunc(pDate, Calendar.DAY_OF_MONTH), 1), -1);
    }

    /**
     * Truncates the date/time part below the given calendar unit.
     * @param pDate the date
     * @param pCalendarUnit the calendar unit
     * @return the truncated date
     */
    public static ImmutableTimestamp trunc(Date pDate, int pCalendarUnit)
    {
        Calendar cal = TimeZoneUtil.getDefaultCalendar();
        cal.setTime(pDate);

        switch (pCalendarUnit)
        {
            case Calendar.YEAR:         cal.set(Calendar.MONTH, 0); 
            case Calendar.MONTH:        cal.set(Calendar.DAY_OF_MONTH, 1);
            case Calendar.DAY_OF_MONTH: cal.set(Calendar.HOUR_OF_DAY, 0); 
            case Calendar.HOUR:
            case Calendar.HOUR_OF_DAY:  cal.set(Calendar.MINUTE, 0); 
            case Calendar.MINUTE:       cal.set(Calendar.SECOND, 0); 
            case Calendar.SECOND:       cal.set(Calendar.MILLISECOND, 0);
                 break;
            default:
                throw new IllegalArgumentException("Not supported calendar unit!");
        }
        
        return new ImmutableTimestamp(cal.getTimeInMillis());
    }
    
    /**
     * Calculates the interval between two dates depending on the given unit.
     * 
     * @param pEndDate the end date
     * @param pBeginDate the begin date
     * @param pCalendarUnit the calendar unit
     * @return the interval between two dates with decimals for 12 months and 31 days.
     */
    public static double between(Date pEndDate, Date pBeginDate, int pCalendarUnit)
    {
        switch (pCalendarUnit)
        {
            case Calendar.YEAR:         return yearsBetween(pEndDate, pBeginDate);
            case Calendar.MONTH:        return monthsBetween(pEndDate, pBeginDate);
            case Calendar.DAY_OF_MONTH: return daysBetween(pEndDate, pBeginDate);
            case Calendar.HOUR:
            case Calendar.HOUR_OF_DAY:  return hoursBetween(pEndDate, pBeginDate);
            case Calendar.MINUTE:       return minutesBetween(pEndDate, pBeginDate);
            case Calendar.SECOND:       return secondsBetween(pEndDate, pBeginDate);
            case Calendar.MILLISECOND:  return millisecondsBetween(pEndDate, pBeginDate);
            default:
                throw new IllegalArgumentException("Not supported calendar unit!");
        }
    }

    /**
     * Adds the interval to the given date depending on the given unit.
     * 
     * @param pDate the date
     * @param pInterval the interval
     * @param pCalendarUnit the calendar unit
     * @return the result date.
     */
    public static ImmutableTimestamp add(Date pDate, double pInterval, int pCalendarUnit)
    {
        switch (pCalendarUnit)
        {
            case Calendar.YEAR:         return addYears(pDate, pInterval);
            case Calendar.MONTH:        return addMonths(pDate, pInterval);
            case Calendar.DAY_OF_MONTH: return addDays(pDate, pInterval);
            case Calendar.HOUR:
            case Calendar.HOUR_OF_DAY:  return addHours(pDate, pInterval);
            case Calendar.MINUTE:       return addMinutes(pDate, pInterval);
            case Calendar.SECOND:       return addSeconds(pDate, pInterval);
            case Calendar.MILLISECOND:  return addMilliseconds(pDate, pInterval);
            default:
                throw new IllegalArgumentException("Not supported calendar unit!");
        }
    }
    
    /**
     * Calculates a next date depending on a last date, a reference date, an interval and a calendar unit.
     * @param pCurrentDate the last date, from which to calculate
     * @param pReferenceDate the reference date
     * @param pInterval the interval
     * @param pCalendarUnit the calendar unit
     * @return the next date
     */
    public static ImmutableTimestamp getNextDate(Date pCurrentDate, Date pReferenceDate, int pInterval, int pCalendarUnit)
    {
        if (pCurrentDate == null)
        {
            pCurrentDate = new Date();
        }
        if (pReferenceDate == null)
        {
            pReferenceDate = pCurrentDate;
        }
        
        long diff = Math.round(Math.floor(between(pCurrentDate, pReferenceDate, pCalendarUnit) / pInterval));
        
        return add(pReferenceDate, (diff + 1) * pInterval, pCalendarUnit);
    }
    
    /**
     * Converts a date from one timezone to another timezone.
     * 
     * @param pDate the date
     * @param pFromTimeZone the timezone of the date
     * @param pToTimeZone the expected timezone
     * @return the date converted to the expected timezone
     */
    public static ImmutableTimestamp convertTimeZone(Date pDate, String pFromTimeZone, String pToTimeZone)
    {
        return convertTimeZone(pDate, TimeZone.getTimeZone(pFromTimeZone), TimeZone.getTimeZone(pToTimeZone));
    }

    /**
     * Converts a date from one timezone to another timezone.
     * 
     * @param pDate the date
     * @param pFromTimeZone the timezone of the date
     * @param pToTimeZone the expected timezone
     * @return the date converted to the expected timezone
     */
    public static ImmutableTimestamp convertTimeZone(Date pDate, TimeZone pFromTimeZone, TimeZone pToTimeZone)
    {
        if (pDate == null)
        {
            return null;
        }
        
        Calendar fromCal = Calendar.getInstance(pFromTimeZone);
        fromCal.setTime(pDate);
           
        Calendar toCal = Calendar.getInstance(pToTimeZone);
        toCal.setTime(pDate);

        int iFromOffset = fromCal.get(Calendar.ZONE_OFFSET) + fromCal.get(Calendar.DST_OFFSET);
        int iToOffset = toCal.get(Calendar.ZONE_OFFSET) + toCal.get(Calendar.DST_OFFSET);

        return new ImmutableTimestamp(toCal.getTimeInMillis() + iToOffset - iFromOffset);     
    }

    /**
     * Gets the default date pattern for the current <code>LocaleUtil.getDefault()</code>.
     * 
     * @return the default date pattern
     */
    public static String getDefaultDatePattern()
    {
        return getDefaultDatePattern(LocaleUtil.getDefault());
    }
    
    /**
     * Gets the default date short pattern for the current <code>LocaleUtil.getDefault()</code>.
     * 
     * @return the default date short pattern
     */
    public static String getDefaultDateShortPattern()
    {
        return getDefaultDateShortPattern(LocaleUtil.getDefault());
    }
    
    /**
     * Gets the default date time pattern for the current <code>LocaleUtil.getDefault()</code>.
     * 
     * @return the default date time pattern
     */
    public static String getDefaultDateTimePattern()
    {
        return getDefaultDateTimePattern(LocaleUtil.getDefault());
    }
    
    /**
     * Gets the default date time short pattern for the current <code>LocaleUtil.getDefault()</code>.
     * 
     * @return the default date time short pattern
     */
    public static String getDefaultDateTimeShortPattern()
    {
        return getDefaultDateTimeShortPattern(LocaleUtil.getDefault());
    }
    
    /**
     * Gets the default time pattern for the current <code>LocaleUtil.getDefault()</code>.
     * 
     * @return the default time pattern
     */
    public static String getDefaultTimePattern()
    {
        return getDefaultTimePattern(LocaleUtil.getDefault());
    }
    
    /**
     * Gets the default date pattern for the given locale.
     * 
     * @param pLocale the locale
     * @return the default date pattern
     */
    public static String getDefaultDatePattern(Locale pLocale)
    {
        return ensureFullYear(((SimpleDateFormat)SimpleDateFormat.getDateInstance(SimpleDateFormat.LONG, pLocale)).toPattern());
    }
    
    /**
     * Gets the default date short pattern for the given locale.
     * 
     * @param pLocale the locale
     * @return the default date short pattern
     */
    public static String getDefaultDateShortPattern(Locale pLocale)
    {
        return ensureFullYear(((SimpleDateFormat)SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT, pLocale)).toPattern());
    }
    
    /**
     * Gets the default date time pattern for the given locale.
     * 
     * @param pLocale the locale
     * @return the default date time pattern
     */
    public static String getDefaultDateTimePattern(Locale pLocale)
    {
        return ensureFullYear(((SimpleDateFormat)SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.LONG, SimpleDateFormat.SHORT, pLocale)).toPattern());
    }
    
    /**
     * Gets the default date time short pattern for the given locale.
     * 
     * @param pLocale the locale
     * @return the default date time short pattern
     */
    public static String getDefaultDateTimeShortPattern(Locale pLocale)
    {
        return ensureFullYear(((SimpleDateFormat)SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.SHORT, pLocale)).toPattern());
    }
    
    /**
     * Gets the default time pattern for the given locale.
     * 
     * @param pLocale the locale
     * @return the default time pattern
     */
    public static String getDefaultTimePattern(Locale pLocale)
    {
        return ((SimpleDateFormat)SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT, pLocale)).toPattern();
    }
    
    /**
     * This method prevents that the date format has only 2 digits for year (yy).
     * It will be replaced by 4 digits (yyyy).
     * 
     * @param pPattern the date pattern
     * @return a date pattern that guarantees to show all year digits.
     */
    public static String ensureFullYear(String pPattern)
    {
        if (pPattern != null)
        {
            if (pPattern.contains("yy") && !pPattern.contains("yyy"))
            {
                return pPattern.replace("yy", "yyyy");
            }
            else if (pPattern.contains("YY") && !pPattern.contains("YYY"))
            {
                return pPattern.replace("YY", "YYYY");
            }
        }
        
        return pPattern;
    }
    
    /**
     * {@link DateSymbols} contains various informations about the symbols used
     * in a date by locale.
     * <p>
     * It has been introduced to make sure that this information is not
     * duplicated every time a new DateUtil is being created.
     *  
     * @author Robert Zenz
     */
    private static final class DateSymbols 
    {
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Class members
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        
        /** Performance tuning, months. */
        private String[] months = null;
        /** Performance tuning, shortMonths. */
        private String[] shortMonths = null;
        /** Performance tuning, months. */
        private String[][] lowerMonths = null;
        /** Performance tuning, shortMonths. */
        private String[][] lowerShortMonths = null;

        /** Performance tuning, weeks. */
        private String[] weekdays = null;
        /** Performance tuning, shortWeeks. */
        private String[] shortWeekdays = null;
        /** Performance tuning, weekdays. */
        private String[] lowerWeekdays = null;
        /** Performance tuning, weekdays. */
        private String[] lowerShortWeekdays = null;

        /** Performance tuning, amPm. */
        private String[] amPm = null;
        /** Performance tuning, amPm. */
        private String[] lowerAmPm = null;

        /** Eras. */
        private String[] eras = null;
        /** Eras. */
        private String[] lowerEras = null;
        
        /** Performance tuning, time zone strings. */
        private String[][] zoneStrings = null;
        /** Performance tuning, time zone strings. */
        private String[][] lowerZoneStrings = null;
        /** Strings to check space or other seperator characters. */
        private String[] ignoreSpaceCheck = null;
        
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Initialization
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        
        /**
         * Creates a new instance of {@link DateSymbols}.
         *
         * @param dateFormat the date format.
         */
        private DateSymbols(SimpleDateFormat dateFormat)
        {
            DateFormatSymbols symbols = dateFormat.getDateFormatSymbols();
            
            months = symbols.getMonths();
            shortMonths = symbols.getShortMonths();
            lowerMonths = new String[2][months.length];
            lowerShortMonths = new String[2][months.length];
            for (int i = 0; i < months.length; i++)
            {
                lowerMonths[0][i] = months[i].toLowerCase();
                lowerShortMonths[0][i] = shortMonths[i].toLowerCase();
                lowerMonths[1][i] = toLowerAndWithoutUmlauts(months[i]);
                lowerShortMonths[1][i] = toLowerAndWithoutUmlauts(shortMonths[i]);
            }
            weekdays = symbols.getWeekdays();
            shortWeekdays = symbols.getShortWeekdays();
            lowerWeekdays = new String[weekdays.length];
            lowerShortWeekdays = new String[weekdays.length];
            for (int i = 0; i < weekdays.length; i++)
            {
                lowerWeekdays[i] = toLowerAndWithoutUmlauts(weekdays[i]);
                lowerShortWeekdays[i] = toLowerAndWithoutUmlauts(shortWeekdays[i]);
            }
            amPm = symbols.getAmPmStrings();
            lowerAmPm = new String[amPm.length];
            for (int i = 0; i < amPm.length; i++)
            {
                lowerAmPm[i] = toLowerAndWithoutUmlauts(amPm[i]);
            }
            eras = symbols.getEras();
            lowerEras = new String[eras.length];
            String[] lowerErasShort = new String[eras.length];
            String[] lowerErasShort1 = new String[eras.length];
            String[] lowerErasShort2 = new String[eras.length];
            String[] lowerErasShort3 = new String[eras.length];
            String[] lowerErasShort4 = new String[eras.length];
            String[] lowerErasShort5 = new String[eras.length];
            String[] lowerErasShort6 = new String[eras.length];
            for (int i = 0; i < amPm.length; i++)
            {
                lowerEras[i] = toLowerAndWithoutUmlauts(eras[i]);
                lowerErasShort[i] = lowerEras[i].replace(" ", "");
                lowerErasShort1[i] = lowerEras[i].replace(".", "");
                lowerErasShort2[i] = lowerErasShort[i].replace(".", "");
                lowerErasShort3[i] = lowerEras[i].replace("chr.", "c.");
                lowerErasShort4[i] = lowerErasShort3[i].replace(" ", "");
                lowerErasShort5[i] = lowerErasShort3[i].replace(".", "");
                lowerErasShort6[i] = lowerErasShort4[i].replace(".", "");
            }
            
            zoneStrings = symbols.getZoneStrings();
            lowerZoneStrings = new String[zoneStrings.length][];
            for (int i = 0; i < zoneStrings.length; i++)
            {
                lowerZoneStrings[i] = new String[5];
                for (int j = 0; j < 5; j++)
                {
                    lowerZoneStrings[i][j] = toLowerAndWithoutUmlauts(zoneStrings[i][j]);
                }
            }

            ArrayList<String> fillInList = new ArrayList<String>();
            fillInTextsThatContainsNotLetter(fillInList, lowerMonths[1]);
            fillInTextsThatContainsNotLetter(fillInList, lowerShortMonths[1]);
            fillInTextsThatContainsNotLetter(fillInList, lowerWeekdays);
            fillInTextsThatContainsNotLetter(fillInList, lowerShortWeekdays);
            fillInTextsThatContainsNotLetter(fillInList, lowerAmPm);
            fillInTextsThatContainsNotLetter(fillInList, lowerErasShort6);
            fillInTextsThatContainsNotLetter(fillInList, lowerErasShort5);
            fillInTextsThatContainsNotLetter(fillInList, lowerErasShort4);
            fillInTextsThatContainsNotLetter(fillInList, lowerErasShort3);
            fillInTextsThatContainsNotLetter(fillInList, lowerErasShort2);
            fillInTextsThatContainsNotLetter(fillInList, lowerErasShort1);
            fillInTextsThatContainsNotLetter(fillInList, lowerErasShort);
            fillInTextsThatContainsNotLetter(fillInList, lowerEras);

//            System.out.println(" -> " + fillInList.size());
            
            ignoreSpaceCheck = fillInList.toArray(new String[fillInList.size()]);
            
        }
        
        /**
         * Fills in all texts that contains a not letter.
         * @param pFillInList the fill in list
         * @param pTexts the texts
         */
        private void fillInTextsThatContainsNotLetter(List<String> pFillInList, String[] pTexts)
        {
            for (int i = 0; i < pTexts.length; i++)
            {
                String text = pTexts[i];
                if (containsNotLetter(text) && isLetter(text.charAt(0)))
                {
//                    int length = text.length();
//                    int index = 0;
//                    while (index < pFillInList.size() && length < pFillInList.get(index).length())
//                    {
//                        index++;
//                    }
//                    pFillInList.add(index, pTexts[i]);
                    pFillInList.add(pTexts[i]);
                }
            }
        }
        
        /**
         * Checks, if there is a character, that is not a letter.
         * @param pText the text
         * @return true, if there is a character, that is not a letter.
         */
        private boolean containsNotLetter(String pText)
        {
            for (int i = 0, length = pText.length(); i < length; i++)
            {
                if (!isLetter(pText.charAt(i)))
                {
                    return true;
                }
            }
            
            return false;
        }
        
    }   // DateSymbols
    
}   // DateUtil
