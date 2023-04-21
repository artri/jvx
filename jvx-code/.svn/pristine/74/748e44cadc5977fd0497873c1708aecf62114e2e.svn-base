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
 * 05.12.2008 - [HM] - creation
 * 10.06.2009 - [JR] - added a logger and removed System.out.println
 * 31.03.2011 - [JR] - #161: set/getLanguage implemented
 * 02.11.2011 - [JR] - translate: fixed instance check
 */
package javax.rad.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import com.sibvisions.util.log.ILogger;
import com.sibvisions.util.log.LoggerFactory;

/**
 * Translation is for translating labels or label keys in any language.
 * It supports wild card translation. This is supported in a very fast implementation.
 * Translation examples:
 * 	"Helo *, how are you?", "Hallo *, wie geht es Dir?"
 * translates "Helo Lisa, how are you?" to "Hallo Lisa, wie geht es Dir?".
 * 
 * @author Martin Handsteiner
 */
public class TranslationMap implements ITranslator
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the logger. */
	private ILogger log = LoggerFactory.getInstance(getClass());
	
	/** Parent TranslationMap to delegate translations. */
	private TranslationMap parent = null;
	
	/** Holds the simple translation tables. */
	private Hashtable<String, String> simpleTranslations = new Hashtable<String, String>();
	
	/** Stores the wild card translations. */
	private Hashtable<String, List<WildCardEntry>> wildcardTranslations = new Hashtable<String, List<WildCardEntry>>();
	
	/** the language code for the translations in this map (default: unset/<code>null</code>). */
	private String sLangCode = null;
	
	/** the resource path which was used for loading the translation, if known. */
	private String sResource;
	
	/** last modified time. */
	private long lLastModified = -1;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Constructs a new translation map.
	 */
	public TranslationMap()
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the Parent TranslationMap to delegate translations.
	 * 
	 * @return the Parent TranslationMap to delegate translations.
	 */
	public TranslationMap getParent()
	{
		return parent;
	}
	
	/**
	 * Sets the Parent TranslationMap to delegate translations.
	 * 
	 * @param pParent the Parent TranslationMap to delegate translations.
	 * @throws IllegalArgumentException if parent is not allowed
	 */
	public void setParent(TranslationMap pParent)
	{
		if (pParent == this)
		{
			throw new IllegalArgumentException("Can't set this translation as parent!");
		}
		parent = pParent;
	}
	
	/**
	 * Translates the text. 
	 * 
	 * @param pText the text to translate. 
	 * @return the translated text.
	 */
	public String translate(String pText)
	{
		if (pText == null)
		{
			return "";
		}
		else
		{
			String translation = simpleTranslations.get(pText);
			
			if (translation == null)
			{
				int length = pText.length();
				
				if (length >= 2)
				{
					int keyLength = getKeyLength(length);
					translation = translate(pText.substring(0, keyLength), pText);
					
					while (translation == null && keyLength > 0)
					{
						keyLength /= 2;
						translation = translate(pText.substring(0, keyLength), pText);
					}
				}
				
				if (translation == null)
				{
					translation = pText;
					// no need to search next time again. 
					simpleTranslations.put(translation, translation);
					
					log.debug("<entry key=\"", prepareCharacters(pText), "\">Translation</entry>");
				}
			}
			if (parent != null && translation == simpleTranslations.get(translation))
			{
				return parent.translate(pText);
			}
			return translation;
		}
	}
	
	/**
	 * Converts special characters for xml output.
	 * 
	 * @param sText the original text
	 * @return the text with replaced characters
	 */
	private String prepareCharacters(String sText)
	{
		StringBuilder sbNewText = new StringBuilder();
		
		char chText;
		
		
		for (int i = 0; i < sText.length(); i++)
		{
			chText = sText.charAt(i);
			
			switch (chText)
			{
				case '&': 
					sbNewText.append("&amp;"); 
					break;
				case '<': 
					sbNewText.append("&lt;"); 
					break;
				case '>': 
					sbNewText.append("&gt;"); 
					break;
				case '\'': 
					sbNewText.append("&apos;"); 
					break;
				case '\"': 
					sbNewText.append("&quot;"); 
					break;
				default: 
					sbNewText.append(chText);				
			}
		}
		
		return sbNewText.toString();
	}
	
	/**
	 * Calculates the key length to search.
	 * 
	 * @param pLength the length of the text.
	 * @return the key length.
	 */
	private int getKeyLength(int pLength)
	{
		int result = 8;
		while (result > pLength)
		{
			result /= 2;
		}
		return result;
	}
	/**
	 * Translates the text. 
	 * 
	 * @param pKey the key for wild card search. 
	 * @param pText the text to translate. 
	 * @return the translated text.
	 */
	private String translate(String pKey, String pText)
	{
		List<WildCardEntry> translations = wildcardTranslations.get(pKey);
		if (translations == null)
		{
			return null;
		}
		else
		{
			String translation = null;
			int i = translations.size() - 1;
			
			while (i >= 0 && translation == null)
			{
				translation = translateWildCard(translations.get(i), pText);
				i--;
			}
			return translation;
		}
	}

	/**
	 * Translates the text. 
	 * 
	 * @param pWildCardParts the wild card parts. 
	 * @param pText the text to translate. 
	 * @return the translated text.
	 */
	private String translateWildCard(WildCardEntry pWildCardParts, String pText)
	{
		// check first wild card part. 
		String wildCardPart = pWildCardParts.wildCardParts[0];
		if (pText.startsWith(wildCardPart))
		{
			int last = pWildCardParts.wildCardParts.length - 1;

			// check last wild card part first for performance and memory reasons.
			String lastWildCardPart = pWildCardParts.wildCardParts[last];
			if (pText.endsWith(lastWildCardPart))
			{
				int length = pText.length();
				int startPos = wildCardPart.length();
				
				List<String> dynamicParts = new ArrayList<String>();

				for (int i = 1; i < last; i++)
				{
					wildCardPart = pWildCardParts.wildCardParts[i];
					int nextPos = pText.indexOf(wildCardPart, startPos);
					
					if (nextPos < 0)
					{
						return null;
					}
					else 
					{
						dynamicParts.add(pText.substring(startPos, nextPos));
						
						startPos = nextPos + wildCardPart.length();
					}
				}
				dynamicParts.add(pText.substring(startPos, length - lastWildCardPart.length()));
				
				StringBuilder result = new StringBuilder(length * 2);

				result.append(pWildCardParts.translationParts[0]);
				
				int transPartCount = pWildCardParts.translationParts.length;
				for (int i = 1; i < transPartCount; i++)
				{
					int order = pWildCardParts.dynamicPartOrder[i - 1];
					if (order < last)
					{
						if (pWildCardParts.noSubTranslation[i - 1])
						{
							result.append(dynamicParts.get(order));
						}
						else
						{
							result.append(translate(dynamicParts.get(order)));
						}
					}
					result.append(pWildCardParts.translationParts[i]);
				}
				
				return result.toString();
			}
		}
		return null;
	}

	/**
	 * Tokenizes the text in wildCardParts. 
	 * 
	 * @param pText the text.
	 * @return the tokenized text.
	 */
	private String[] tokenize(String pText)
	{
		List<String> tokens = new ArrayList<String>();
		
		int startPos = 0; 
		int endPos = pText.indexOf('*');
		while (endPos >= 0)
		{
			tokens.add(pText.substring(startPos, endPos));
			startPos = endPos + 1;
			endPos = pText.indexOf('*', startPos);
		}
		tokens.add(pText.substring(startPos));
		return tokens.toArray(new String[tokens.size()]);
	}
	
	/**
	 * Puts a new translation.
	 * 
	 * @param pText the text to translate.
	 * @param pTranslation the translation.
	 * @return the previous translation.
	 */
	public String put(String pText, String pTranslation)
	{
		if (pText == null)
		{
			throw new IllegalArgumentException("Null Text can not be translated!");
		}
		int len = pText.length();
		
		if (len == 0)
		{
			throw new IllegalArgumentException("Empty Text can not be translated!");
		}

		// Allow to overrule translation anyway without going to parent, even if the translation is equal to text.
//		if (pText.equals(pTranslation))
//		{
//			pTranslation = pText;
//		}
		
		String previousTranslation = simpleTranslations.put(pText, pTranslation);
		
		String[] wildCardParts = tokenize(pText);
		String[] translationParts = tokenize(pTranslation);
		
		if (wildCardParts.length > 1)
		{
			String wildCartPart = wildCardParts[0];
			
			String key = wildCartPart.substring(0, getKeyLength(wildCartPart.length()));
			
			List<WildCardEntry> translations = wildcardTranslations.get(key);
			
			if (translations == null)
			{
				translations = new ArrayList<WildCardEntry>();
				
				wildcardTranslations.put(key, translations);
			}
			else if (previousTranslation != null)
			{
				for (int i = translations.size() - 1; i >= 0; i--)
				{
					if (Arrays.equals(translations.get(i).wildCardParts, wildCardParts))
					{
						translations.remove(i);
					}
				}
			}
			String translationPartBefore = translationParts[0];
			int[] dynamicPartOrder = new int[translationParts.length - 1];
			boolean[] noSubTranslation = new boolean[translationParts.length - 1];
			for (int i = 0; i < translationParts.length - 1; i++)
			{
				dynamicPartOrder[i] = i; 

				String translationPartAfter = translationParts[i + 1];
				
				if (translationPartBefore.length() > 0
						&& translationPartBefore.charAt(translationPartBefore.length() - 1) == '~')
				{
					noSubTranslation[i] = true;
					translationParts[i] = translationPartBefore.substring(0, translationPartBefore.length() - 1);
				}
				if (translationPartAfter.length() > 0)
				{
					char ch = translationPartAfter.charAt(0);
					if (ch >= '0' && ch <= '9')
					{
						dynamicPartOrder[i] = ch - '0';
						translationPartAfter = translationPartAfter.substring(1);
						translationParts[i + 1] = translationPartAfter;
					}
				}
				
				translationPartBefore = translationPartAfter;
			}
			
			int index = translations.size();
			WildCardEntry wildCardEntry = new WildCardEntry(wildCardParts, translationParts, dynamicPartOrder, noSubTranslation);
			
			while (index > 0 
					&& translations.get(index - 1).wildCardParts.length < wildCardEntry.wildCardParts.length)
			{
				index--;
			}
			
			translations.add(index, new WildCardEntry(wildCardParts, translationParts, dynamicPartOrder, noSubTranslation));
		}
		
		lLastModified = System.currentTimeMillis();
		
		return previousTranslation;
	}
	
	/**
	 * Gets all translation map texts.
	 * 
	 * @return all translation map texts.
	 */
	public Enumeration<String> keys()
	{
		return simpleTranslations.keys();
	}
	
	/**
	 * Gets the configured translation for the given text.
	 * 
	 * @param pText the text.
	 * @return the configured translation for the given text.
	 */
	public String get(String pText)
	{
		return simpleTranslations.get(pText); 
	}

	/**
	 * The size of the translation map.
	 * 
	 * @return the size of the translation map.
	 */
	public int size()
	{
		return simpleTranslations.size(); 
	}

	/**
	 * Clears all translations.
	 */
	public void clear()
	{
		simpleTranslations.clear(); 
		wildcardTranslations.clear();
		
		lLastModified = System.currentTimeMillis();
	}

	/**
	 * Gets all the untranslated texts.
	 * 
	 * @return the untranslated texts.
	 */
	public List<String> getUntranslated()
	{
		List<String> result = new ArrayList<String>(simpleTranslations.size());
		
		Enumeration<String> keys = simpleTranslations.keys();
		while (keys.hasMoreElements())
		{
			String key = keys.nextElement();
			if (key == simpleTranslations.get(key))
			{
				result.add(key);
			}
		}
		return result;
	}
	
	/**
	 * Gets this TranslationMap as Properties.
	 * 
	 * @return this TranslationMap as Properties.
	 */
	public Properties getAsProperties()
	{
		Properties result = new Properties();
		
		Enumeration<String> keys = simpleTranslations.keys();
		while (keys.hasMoreElements())
		{
			String key = keys.nextElement();
			result.setProperty(key, simpleTranslations.get(key));
		}
		
		return result;
	}
	
	/**
	 * Sets this TranslationMap as Properties.
	 * 
	 * @param pProperties the Properties.
	 */
	public void setAsProperties(Properties pProperties)
	{
		Enumeration keys = pProperties.propertyNames();
		while (keys.hasMoreElements())
		{
			Object key = keys.nextElement();
			if (key instanceof String)
			{
				put((String)key, pProperties.getProperty((String)key));
			}
		}
	}
	
	/**
	 * Gets whether this TranslationMap is empty.
	 * 
	 * @return <code>true</code> if no translations are available, <code>false</code> otherwise
	 */
	public boolean isEmpty()
	{
		return simpleTranslations.isEmpty();
	}
	
	/**
	 * Sets the language code of the translated texts.
	 *  
	 * @param pLanguage the language code e.g. "en"
	 */
	public void setLanguage(String pLanguage)
	{
		sLangCode = pLanguage;
	}
	
	/**
	 * Gets the language code of the translated texts.
	 * 
	 * @return the language code e.g. "en"
	 */
	public String getLanguage()
	{
		return sLangCode;
	}
	
	/**
	 * Sets the resource path as additional information, if known.
	 * 
	 * @param pPath the resource that contains the translation
	 */
	public void setResourcePath(String pPath)
	{
	    sResource = pPath;
	}
	
	/**
	 * Gets the resource path.
	 * 
	 * @return the resource path or <code>null</code> if path is not known
	 */
	public String getResourcePath()
	{
	    return sResource;
	}
	
	/**
	 * Gets the last modified time.
	 * 
	 * @return the last modified time in millis
	 */
	public long lastModified()
	{
		return lLastModified;
	}

	//****************************************************************
	// Subclass definition
	//****************************************************************

	/**
	 * Entry that stores the WildCard informations.
	 * 
	 * @author Martin Handsteier
	 */
	private static final class WildCardEntry
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/** the wildCardParts. */
		private String[] wildCardParts;
		
		/** the translationParts. */
		private String[] translationParts;
		
		/** the wildCardOrder. */
		private int[] 	dynamicPartOrder;
		
		/** the wildCardOrder. */
		private boolean[]	noSubTranslation;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Constructs a new instance of <code>WildCardEntry</code>.
		 * 
		 * @param pWildCardParts the wildCardParts.
		 * @param pTranslationParts the translationParts.
		 * @param pDynamicPartOrder the dynamicPartOrder.
		 * @param pNoSubTranslation the noSubTranslation.
		 */
		private WildCardEntry(String[] pWildCardParts, String[] pTranslationParts, int[] pDynamicPartOrder, boolean[] pNoSubTranslation)
		{
			wildCardParts = pWildCardParts;
			translationParts = pTranslationParts;
			dynamicPartOrder = pDynamicPartOrder;
			noSubTranslation = pNoSubTranslation;
		}
		
	}	// WildCardEntry
	
}	// TranslationMap
