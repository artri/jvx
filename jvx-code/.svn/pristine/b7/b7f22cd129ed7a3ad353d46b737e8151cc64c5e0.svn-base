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
 * 08.03.2012 - [JR] - #556: getListSeparator implemented
 */
package com.sibvisions.util.type;

import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.sibvisions.util.Internalize;

/**
 * The <code>LocaleUtil</code> is a utility class for date conversion and for formatting dates
 * as string.
 *  
 * @author Martin Handsteiner
 */
public final class LocaleUtil
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class Members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The language list for a country code. */
	private static Map<String, Set<String>> countryCodeLanguageCodes = new HashMap<String, Set<String>>(); 
		
	/** The country codes for a language. */
	private static Map<String, Set<String>> languageCodeCountryCodes = new HashMap<String, Set<String>>(); 
		
	/** The default locale. */
	private static Locale defaultLocale;
	
	/** The default thread locale. */
	private static ThreadLocal<Locale> threadDefaultLocale = new ThreadLocal<Locale>(); // no lazy init to avoid thread problems
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	static
	{
		resetCountryLanguages();
	}
	
	/**
	 * Creating a LocaleUtil is not allowed.
	 */
	private LocaleUtil()
	{
		// Utility Class
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Resets the country languages to default.
	 */
	public static void resetCountryLanguages()
	{
		countryCodeLanguageCodes.clear();
		languageCodeCountryCodes.clear();
		
		for (Locale locale : Locale.getAvailableLocales())
		{
			addCountryLanguage(locale.getCountry(), locale.getLanguage());
		}
	}

	/**
	 * Adds a language to the given country.
	 * 
	 * @param pCountryCode the country code.
	 * @param pLanguageCode the language code.
	 */
	public static void addCountryLanguage(String pCountryCode, String pLanguageCode)
	{
		Set<String> languageCodes = countryCodeLanguageCodes.get(pCountryCode);
		if (languageCodes == null)
		{
			languageCodes = new HashSet<String>();
			countryCodeLanguageCodes.put(pCountryCode, languageCodes);
		}
		languageCodes.add(pLanguageCode);
		
		Set<String> countryCodes = languageCodeCountryCodes.get(pLanguageCode);
		if (countryCodes == null)
		{
			countryCodes = new HashSet<String>();
			languageCodeCountryCodes.put(pLanguageCode, countryCodes);
		}
		countryCodes.add(pCountryCode);
	}
	
	/**
	 * Removes a language from the given country.
	 * 
	 * @param pCountryCode the country code.
	 * @param pLanguageCode the language code.
	 */
	public static void removeCountryLanguage(String pCountryCode, String pLanguageCode)
	{
		Set<String> languageCodes = countryCodeLanguageCodes.get(pCountryCode);
		if (languageCodes != null)
		{
			languageCodes.remove(pLanguageCode);
			if (languageCodes.size() == 0)
			{
				countryCodeLanguageCodes.remove(pCountryCode);
			}
		}
		Set<String> countryCodes = languageCodeCountryCodes.get(pLanguageCode);
		if (countryCodes != null)
		{
			countryCodes.remove(pCountryCode);
			if (countryCodes.size() == 0)
			{
				languageCodeCountryCodes.remove(pLanguageCode);
			}
		}
	}
	
	/**
	 * Gets the language codes for the given country code.
	 * 
	 * @param pCountryCode the country code.
	 * @return the language codes.
	 */
	public static String[] getLanguageCodesForCountryCode(String pCountryCode)
	{
		Set<String> languageCodes = countryCodeLanguageCodes.get(pCountryCode);

		if (languageCodes == null)
		{
			return new String[0];
		}
		else
		{
			return languageCodes.toArray(new String[languageCodes.size()]);
		}
	}

	/**
	 * Gets the country codes for the given language code.
	 * 
	 * @param pLanguageCode the language code. 
	 * @return the time zone ids.
	 */
	public static String[] getCountryCodesForLanguageCode(String pLanguageCode)
	{
		Set<String> countryCodes = languageCodeCountryCodes.get(pLanguageCode);
		
		if (countryCodes == null)
		{
			return new String[0];
		}
		else
		{
			return countryCodes.toArray(new String[countryCodes.size()]);
		}
	}
	

	/**
	 * Get a locale parsing language, country and variant.
	 * 
	 * @param pLanguageTag the language tag.
	 * @return the Locale.
	 */
	@SuppressWarnings("unused")
	public static Locale forLanguageTag(String pLanguageTag)
	{
		// Default Locale should be base of intern functionality.
		Locale internLocale = Internalize.intern(getDefault());
		if (pLanguageTag == null || pLanguageTag.length() == 0)
		{
			return getDefault();
		}
		else
		{
		    int hashIndex = pLanguageTag.indexOf('#');
		    if (hashIndex < 0)
		    {
		        hashIndex = pLanguageTag.length();
		    }
			int langEnd = pLanguageTag.indexOf('_');
			
			if (langEnd < 0)
			{
				return Internalize.intern(new Locale(pLanguageTag.substring(0, hashIndex)));
			}
			else
			{
				int countryEnd = pLanguageTag.indexOf('_', langEnd + 1);
				
				if (countryEnd < 0)
				{
					return Internalize.intern(new Locale(pLanguageTag.substring(0, langEnd), pLanguageTag.substring(langEnd + 1, hashIndex)));
				}
				else
				{
					return Internalize.intern(new Locale(
							pLanguageTag.substring(0, langEnd), 
							pLanguageTag.substring(langEnd + 1, countryEnd), 
							pLanguageTag.substring(countryEnd + 1, hashIndex)));
				}
			}
		}
	}
	
	/**
	 * Gets the default locale. If no locale is set, the default local is returned.
	 * 
	 * @return the default locale.
	 */
	public static Locale getDefault()
	{
		Locale locale = threadDefaultLocale.get();
		if (locale != null)
		{
			return locale;
		}
		if (defaultLocale != null)
		{
			return defaultLocale; 
		}
		return Locale.getDefault();
	}
	
	/**
	 * Sets the default locale. If no locale is set, the default locale is returned.
	 * 
	 * @param pLocale the default locale.
	 */
	public static void setDefault(Locale pLocale)
	{
		defaultLocale = pLocale;
	}
	
	/**
	 * Gets the thread default locale. If no locale is set, the thread default local is returned.
	 * 
	 * @return the thread default locale.
	 */
	public static Locale getThreadDefault()
	{
		return threadDefaultLocale.get();
	}
	
	/**
	 * Sets the default locale. If no locale is set, the default locale is returned.
	 * 
	 * @param pLocale the default locale.
	 */
	public static void setThreadDefault(Locale pLocale)
	{
		threadDefaultLocale.set(pLocale);
	}

	/**
	 * Gets the list separator for the current locale.
	 * 
	 * @return the list separator e.g. <code>;</code> in Europe or <code>,</code> in US
	 * @see #getListSeparator(Locale)
	 */
	public static String getListSeparator()
	{
		return getListSeparator(getDefault());
	}
	
	/**
	 * Gets the list separator for the given locale.
	 *
	 * @param pLocale the locale to use
	 * @return the list separator e.g. <code>;</code> in Europe or <code>,</code> in US
	 */
	public static String getListSeparator(Locale pLocale)
	{
		DecimalFormatSymbols dfs = new DecimalFormatSymbols(pLocale);
		
		
		if (dfs.getDecimalSeparator() == '.')
		{
			return ",";
		}
		else
		{
			return ";";
		}
	}
	
	/**
	 * Gets the language from a locale string, e.g. de_at, de
	 * 
	 * @param pLocale the locale
	 * @return the extracted language
	 */
	public static String getLanguage(String pLocale)
	{
		if (pLocale == null)
		{
			return null;
		}
		
		if (pLocale.length() == 2)
		{
			return pLocale;
		}
		else
		{
			int iPos = pLocale.indexOf("_");
			
			if (iPos > 0)
			{
				return pLocale.substring(0, iPos);
			}
		}
		
		return pLocale;
	}
	
	/**
	 * Gets the country from a locale string, e.g. de_at, de
	 * 
	 * @param pLocale the locale
	 * @return the extracted country
	 */
	public static String getCountry(String pLocale)
	{
		if (pLocale == null)
		{
			return null;
		}
		
		int iPos = pLocale.indexOf("_");
		
		if (iPos > 0)
		{
			return pLocale.substring(iPos + 1);
		}
		
		return null;
	}

	
}	// LocaleUtil
