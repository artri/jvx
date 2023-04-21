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
 */
package com.sibvisions.util.type;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Locale;
import java.util.WeakHashMap;

/**
 * The <code>NumberUtil</code> is a utility class for number conversion and for formatting numbers
 * as string.
 *  
 * @author Martin Handsteiner
 */
public class NumberUtil implements Serializable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The default NUMBER_FORMAT. */
	private static final String DEFAULT_NUMBER_FORMAT = "#.######################################"; // (DecimalFormat)NumberFormat.getNumberInstance();
	
	/** Cached decimal format symbols. */
	private static WeakHashMap<Locale, DecimalFormatSymbols> decimalFormatSymbolsCache = new WeakHashMap<Locale, DecimalFormatSymbols>();
	
    /** The locale of the currently used {@link #numberFormat}. */
    private Locale numberFormatLocale;

	/** The locale base for creation. */
	private transient Locale creationLocale = null;
	/** The pattern base for creation. */
	private transient String creationPattern = null;
	/** The allowed characters beside numbers. */
	private transient String patternFixedText = null;
	
	/** The number format. */
	private DecimalFormat numberFormat;
	
    /** True, if the format should be checked strict. */
    private boolean strictFormatCheck = false;

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** 
	 * Constructs a new instance of <code>NumberUtil</code> with default number format.
	 */
	public NumberUtil()
	{
		setNumberFormat(null);
	}

	/** 
	 * Constructs a new instance of <code>NumberUtil</code> that supports empty Strings and null values.
	 * 
	 * @param pNumberFormat the formatter that should support empty Strings and null values
	 */
    @Deprecated
	public NumberUtil(NumberFormat pNumberFormat)
	{
		setNumberFormat(pNumberFormat);
	}

	/** 
	 * Constructs a new instance of <code>NumberUtil</code> that supports empty Strings and null values.
	 * 
	 * @param pNumberPattern the pattern that should support empty Strings and null values
	 */
	public NumberUtil(String pNumberPattern)
	{
		setNumberPattern(pNumberPattern);
	}

    /** 
     * Constructs a new instance of <code>NumberUtil</code> that supports empty Strings and null values.
     * 
     * @param pNumberPattern the pattern that should support empty Strings and null values
     * @param pLocale the {@link Locale} to use. {@code null} to always use {@link LocaleUtil#getDefault()}.
     */
    public NumberUtil(String pNumberPattern, Locale pLocale)
    {
        setNumberPattern(pNumberPattern, pLocale);
    }

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Parses the number from text.
	 * 
	 * @param pText the text.
	 * @return the parsed number.
     * @throws ParseException if there is an error in the conversion
	 */
	public Number parse(String pText) throws ParseException
	{
		if (pText == null || pText.length() == 0 || "-".equals(pText))
		{
			return null;
		}
		else
		{   
		    setNumberPattern(creationPattern, creationLocale);
		    
			numberFormat.setParseBigDecimal(true); // Ensure creation of BigDecimal
			
			BigDecimal value;
			try
			{
			    String exponentSeparator = numberFormat.getDecimalFormatSymbols().getExponentSeparator();
			    char minusSign = numberFormat.getDecimalFormatSymbols().getMinusSign();
                char percent = numberFormat.getDecimalFormatSymbols().getPercent();
                char permill = numberFormat.getDecimalFormatSymbols().getPerMill();
                char decimalSeparator = numberFormat.getDecimalFormatSymbols().getDecimalSeparator();
			    char groupingSeparator = decimalSeparator == ',' ? '.' : ',';

			    StringBuilder convertedNumber = new StringBuilder(pText.length());
                StringBuilder fixedText = new StringBuilder(pText.length());
			    
                boolean isMinus = false;
                boolean isPercent = false;
                boolean isPermill = false;
			    int decPos = -1;
			    int decCount = 0;
			    int groupPos = -1;
			    int groupCount = 0;
			    boolean lastIsExponent = false;
			    
			    for (int i = 0, length = pText.length(); i < length; i++)
			    {
			        char ch = pText.charAt(i);
			        boolean isExponent = exponentSeparator.indexOf(ch) >= 0 || ch == 'E' || ch == 'e';
			        
			        if (Character.isDigit(ch))
			        {
			            convertedNumber.append(ch);
			            fixedText.setLength(0);
			        }
			        else if ((ch == minusSign || ch == '-') && lastIsExponent)
			        {
                        convertedNumber.append(ch);
                        if (i == length - 1)
                        {
                            convertedNumber.append('0');
                        }
                        fixedText.setLength(0);
			        }
			        else if (isExponent)
			        {
			            convertedNumber.append('E');
			            if (i == length - 1)
			            {
			                convertedNumber.append('0');
	                        fixedText.setLength(0);
			            }
			            else
			            {
	                        fixedText.append(ch);
			            }
			        }
			        else if (ch == minusSign || ch == '-')
			        {
			            isMinus = true;
                        fixedText.setLength(0);
			        }
			        else if (ch == percent || ch == '%')
			        {
			            isPercent = true;
                        fixedText.setLength(0);
			        }
                    else if (ch == permill || ch == '\u2030')
                    {
                        isPermill = true;
                        fixedText.setLength(0);
                    }
			        else if (ch == decimalSeparator)
			        {
                        decPos = convertedNumber.length();
			            decCount++;
                        fixedText.setLength(0);
			        }
                    else if (ch == groupingSeparator)
                    {
                        groupPos = convertedNumber.length();
                        groupCount++;
                        fixedText.setLength(0);
                    }
                    else if (!Character.isWhitespace(ch) && !Character.isDigit(ch))
                    {
                        fixedText.append(ch);
                        if (strictFormatCheck)
                        {
                            if (!patternFixedText.contains(fixedText))
                            {
                                throw new ParseException(pText, pText.indexOf(i));
                            }
                        }
                        else if (!patternFixedText.toLowerCase().contains(fixedText.toString().toLowerCase()))
                        {
                            throw new ParseException(pText, pText.indexOf(i));
                        }
                        if (lastIsExponent)
                        {
                            convertedNumber.setLength(convertedNumber.length() - 1);
                        }
                    }
			        lastIsExponent = isExponent;
			    }

			    if (decCount == 1 && decPos > groupPos)
			    {
			        convertedNumber.insert(decPos, '.');
			    }
			    else if ((decCount >= 1 && groupCount == 1 && groupPos > decPos)
			            || (decCount == 0 && groupCount == 1 && convertedNumber.length() - groupPos != 3))
			    {
			        convertedNumber.insert(groupPos, '.');
			    }
			    else if ((decCount > 1 && groupCount == 1 && groupPos < decPos)
			             || (decCount > 1 && groupCount > 1))
			    {
			        throw new ParseException(pText, pText.indexOf(decimalSeparator));
			    }
			    if (isMinus)
			    {
			        convertedNumber.insert(0,  minusSign);
			    }
			    
				value = new BigDecimal(convertedNumber.toString());
				
				if (isPercent)
				{
				    value = value.divide(BigDecimal.valueOf(100));
				}
				else if (isPermill)
				{
                    value = value.divide(BigDecimal.valueOf(1000));
				}
			}
//			catch (ParseException ex) // Do not try the normal parser, if we decided, that the number is invalid!
//			{
//			    throw ex;
//			}
			catch (Exception ex)
			{
	            ParsePosition pos = new ParsePosition(0);
	            value = (BigDecimal)numberFormat.parse(pText, pos);
	            
	            if (pos.getIndex() < pText.length())
	            {
	                throw new ParseException(pText, pos.getIndex());
	            }
			}
			return value;
		}
	}

	/**
	 * Formats the number to text.
	 * 
	 * @param pNumber the number.
	 * @return the formatted text.
	 */
	public String format(Number pNumber)
	{
		if (pNumber == null)
		{   
			return null;
		}
		else
		{
		    setNumberPattern(creationPattern, creationLocale);
			
			return numberFormat.format(pNumber);
		}
	}

    /**
     * Gets the current used {@link DecimalFormat}.
     * This can be used, to get informations like current used decimal symbols, grouping information and so on.
     * 
     * @return the current used {@link DecimalFormat}.
     */
    public DecimalFormat getDecimalFormat()
    {
        setNumberPattern(creationPattern, creationLocale); // Ensure correct number format for current locale.
        
        return numberFormat;
    }

    /**
     * Gets the decimal format symbols for te given locale.
     * @param pLocale the locale
     * @return the decimal format symbols
     */
    private DecimalFormatSymbols getDecimalFormatSymbols(Locale pLocale)
    {
        DecimalFormatSymbols decimalFormatSymbols = decimalFormatSymbolsCache.get(pLocale);
        if (decimalFormatSymbols == null)
        {
            decimalFormatSymbols = new DecimalFormatSymbols(pLocale);
            decimalFormatSymbolsCache.put(pLocale, decimalFormatSymbols);
        }
        return decimalFormatSymbols;
    }
    
	/**
	 * Gets the number format.
	 * 
	 * @return the number format.
     * @deprecated NumberFormat should not be used anymore, it lacks on proper get methods for used locale. Use {@link #getDecimalFormat()}.
	 */
    @Deprecated
	public NumberFormat getNumberFormat()
	{
        return getDecimalFormat();
	}

	/**
	 * Gets the number format.
	 * 
	 * @param pNumberFormat the number format.
	 * @deprecated NumberFormat should not be used anymore, it lacks on proper get methods for used locale. Use {@link #setNumberPattern(String, Locale)}.
	 */
    @Deprecated
	public void setNumberFormat(NumberFormat pNumberFormat)
	{
		if (pNumberFormat == null)
		{
			setNumberPattern(null, null);
		}
		else if (pNumberFormat instanceof DecimalFormat)
		{
			numberFormat = (DecimalFormat)pNumberFormat;
			numberFormatLocale = LocaleUtil.getDefault();
			
			creationLocale = null;
			creationPattern = numberFormat.toPattern();
			
		    preparePatternFixedText();
		}
		else
		{
			throw new IllegalArgumentException("Only DecimalFormat is supported!");
		}
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
	 * Gets the number format pattern.
	 * 
	 * @return the number format pattern.
	 */
	public String getNumberPattern()
	{
		return numberFormat.toPattern();
	}

	/**
	 * Gets the number format pattern.
	 * 
	 * @param pNumberPattern the number format pattern.
	 */
	public void setNumberPattern(String pNumberPattern)
	{
	    setNumberPattern(pNumberPattern, null);
	}

    /**
     * Sets the new number format, if something was changed.
     * @param pNumberPattern the pattern
     * @param pLocale the locale
     */
    public void setNumberPattern(String pNumberPattern, Locale pLocale)
    {
        Locale locale = pLocale;
        if (locale == null)
        {
            locale = LocaleUtil.getDefault();
        }
        
        if (pNumberPattern == null)
        {
            if (creationPattern != null
                    || pLocale != creationLocale || locale != numberFormatLocale)
            {
                numberFormat = new DecimalFormat(DEFAULT_NUMBER_FORMAT, getDecimalFormatSymbols(locale));
                numberFormatLocale = locale;
                
                creationLocale = pLocale;
                creationPattern = pNumberPattern;
                preparePatternFixedText();
            }
        }
        else
        {
            if (!pNumberPattern.equals(creationPattern)
                    || pLocale != creationLocale || locale != numberFormatLocale)
            {
                numberFormat = new DecimalFormat(pNumberPattern, getDecimalFormatSymbols(locale));
                numberFormatLocale = locale;
                
                creationLocale = pLocale;
                creationPattern = pNumberPattern;
                preparePatternFixedText();
            }
        }
    }

    /**
     * Gets all allowed characters.
     */
    private void preparePatternFixedText()
    {
        String patternCharacters = "0#.-,E;%\u2030\u00A4";
        char   patternQuote = '\''; 
        char   patternCurrency = '\u00A4'; 
        
        String pattern = numberFormat.toPattern();
        StringBuilder result = new StringBuilder();
        boolean isInsideQuote = false;
        char oldCh = '-';
        boolean hasCurrency = false;
        
        for (int i = 0, length = pattern.length(); i < length; i++)
        {
            char ch = pattern.charAt(i);
            
            if (ch == patternQuote)
            {
                if (isInsideQuote && oldCh == patternQuote)
                {
                    result.append(ch);
                }
                isInsideQuote = !isInsideQuote;
            }
            else if (!Character.isWhitespace(ch) && !Character.isDigit(ch))
            {
                if (isInsideQuote || patternCharacters.indexOf(ch) < 0)
                {
                    result.append(ch);
                }
                else if (ch == patternCurrency)
                {
                    hasCurrency = true;
                    result.append(numberFormat.getDecimalFormatSymbols().getCurrencySymbol());
                    result.append(numberFormat.getDecimalFormatSymbols().getCurrency().getCurrencyCode());
                }
            }
            oldCh = ch;
        }
        
        if (!hasCurrency && numberFormat.getMinimumFractionDigits() == 2 && numberFormat.getMaximumFractionDigits() == 2)
        {
            result.append(numberFormat.getDecimalFormatSymbols().getCurrencySymbol());
            result.append(numberFormat.getDecimalFormatSymbols().getCurrency().getCurrencyCode());
        }

        patternFixedText = result.toString();
    }
    
	/**
	 * Formats a number.
	 * 
	 * @param pNumber the number
	 * @param pNumberPattern the format 
	 * @return the formatted number string
	 * @see DecimalFormat
	 */
	public static String format(Number pNumber, String pNumberPattern)
	{
		return format(pNumber, pNumberPattern, LocaleUtil.getDefault());
	}
	
	/**
	 * Formats a number with given locale settings.
	 * 
	 * @param pNumber the number
	 * @param pNumberPattern the format 
	 * @param pLocale the locale to use for deimal format symbols
	 * @return the formatted number string
	 * @see DecimalFormat
	 */
	public static String format(Number pNumber, String pNumberPattern, Locale pLocale)
	{
		NumberUtil nu = new NumberUtil(pNumberPattern, pLocale);
	
		return nu.format(pNumber);
	}	
	
    /**
     * Strict format check allows only characters, that are part of the format pattern.
     *  
     * @return true, if format should be checked strict.
     */
    public boolean isStrictFormatCheck()
    {
        return strictFormatCheck;
    }
    
    /**
     * Strict format check allows only characters, that are part of the format pattern.
     *  
     * @param pStrictFormatCheck true, if format should be checked strict.
     */
    public void setStrictFormatCheck(boolean pStrictFormatCheck)
    {
        strictFormatCheck = pStrictFormatCheck;
    }
    
    /**
     * Converts a number value into a {@link BigDecimal}.
     * 
     * @param pValue the value
     * @return the {@link BigDecimal} value
     */
    public static BigDecimal toBigDecimal(Number pValue)
    {
    	if (pValue instanceof BigDecimal)
    	{
    		return (BigDecimal)pValue;
    	}
    	else if (pValue instanceof Integer)
    	{
    		return new BigDecimal(pValue.intValue());
    	}
    	else if (pValue instanceof Long)
    	{
    		return new BigDecimal(pValue.longValue());
    	}
    	else if (pValue instanceof Float)
    	{
    		return new BigDecimal(pValue.floatValue());
    	}
    	else if (pValue instanceof Double)
    	{
    		return new BigDecimal(pValue.doubleValue());
    	}
    	else if (pValue instanceof BigInteger)
    	{
    		return new BigDecimal((BigInteger)pValue);
    	}
    	else if (pValue instanceof Short)
    	{
    		return new BigDecimal(pValue.shortValue());
    	}
    	else if (pValue instanceof Byte)
    	{
    		return new BigDecimal(pValue.byteValue());
    	}
    	
    	return new BigDecimal(pValue.toString());
    }    

}	// NumberUtil
