/*
 * Copyright 2011 SIB Visions GmbH
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
 * 03.12.2013 - [JR] - creation (used http://opencsv.sourceforge.net/ - CSVParser.java)
 */
package com.sibvisions.util.type;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The <code>StringParser</code> is a very simple text parser, that just implements 
 * splitting a single line into fields.
 *
 * @author René Jahn
 * @author Glen Smith
 * @author Rainer Pruy
 */
public class StringParser
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the separator character. */
	private final char			chSeparator;

	/** the quote character. */
	private final char			chQuote;

	/** the escape character. */
	private final char			chEscape;

	/** one "open" part of a parsed multi-line text. */
	private String				sPending;

	/** whether quote characters are a must. */
	private boolean				bMustQuote;

	/** whether to ignore leading whitespaces. */
	private boolean				bIgnoreLeadingWhiteSpace;
	
	/** whether the parse-position is in a field. */
	private boolean				bInField		= false;

	/** whether quote characters shouldn't be included in the result fields. */
	private boolean				bRemoveQuotes	= true;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>StringParser</code> with supplied separator. The default
	 * quote character <code>&amp;</code> and escape character <code>&#92;</code> will be used.
	 * 
	 * @param pSeparator the delimiter to use for separating entries
	 */
	public StringParser(char pSeparator)
	{
		this(pSeparator, '"', '\\');
	}

	/**
	 * Creates a new instance of <code>StringParser</code> with supplied separator and quote char. 
	 * The default escape character <code>&#92;</code> will be used.
	 * 
	 * @param pSeparator the delimiter to use for separating entries
	 * @param pQuote the character to use for quoted elements
	 */
	public StringParser(char pSeparator, char pQuote)
	{
		this(pSeparator, pQuote, '\\');
	}

	/**
	 * Creates a new instance of <code>StringParser</code> with supplied separator, quote and escape character.
	 * 
	 * @param pSeparator the delimiter to use for separating entries
	 * @param pQuote the character to use for quoted elements
	 * @param pEscape the character to use for escaping a separator or quote
	 */
	public StringParser(char pSeparator, char pQuote, char pEscape)
	{
		chSeparator = pSeparator;
		chQuote = pQuote;
		chEscape = pEscape;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Parse a line that contains one filed.
	 * 
	 * @param pText the text to parse
	 * @return the parsed fields
	 * @throws IOException if parsing fails
	 */
	public String[] parse(String pText) throws IOException
	{
		return parse(pText, true);
	}

	/**
	 * Parse a line with multi field support.
	 * 
	 * @param pText the text to parse
	 * @param pMulti whether the text contains more than one field
	 * @return the parsed fields
	 * @throws IOException if parsing fails
	 */
	public String[] parse(String pText, boolean pMulti) throws IOException
	{
		sPending = null;
		bInField = false;

		return parseText(pText, pMulti);
	}

	/**
	 * Parses an incoming text and returns an array of elements.
	 * 
	 * @param pText the string to parse
	 * @param pMulti whether the text contains more than one field
	 * @return the comma-tokenized list of elements, or <code>null</code> if text is <code>null</code>
	 * @throws IOException if parsing text fails
	 */
	private String[] parseText(String pText, boolean pMulti) throws IOException
	{

		if (!pMulti && sPending != null)
		{
			sPending = null;
		}

		if (pText == null)
		{
			if (sPending != null)
			{
				String s = sPending;
				
				sPending = null;
				
				return new String[] { s };
			}
			else
			{
				return null;
			}
		}

		List<String> liTokens = new ArrayList<String>();
		StringBuilder sbPart = new StringBuilder(pText.length());
		
		boolean inQuotes = false;
		
		if (sPending != null)
		{
			sbPart.append(sPending);
			
			sPending = null;
			
			inQuotes = true;
		}
		
		char ch;
		
		for (int i = 0, cnt = pText.length(); i < cnt; i++)
		{
			ch = pText.charAt(i);
			
			if (ch == this.chEscape)
			{
				if (isNextCharacterEscapable(pText, inQuotes || bInField, i))
				{
					if (!bRemoveQuotes)
					{
						sbPart.append(pText.charAt(i));
					}
					
					sbPart.append(pText.charAt(i + 1));
					
					i++;
				}
			}
			else if (ch == chQuote)
			{
				if (isNextCharacterEscapedQuote(pText, inQuotes || bInField, i))
				{
					sbPart.append(pText.charAt(i + 1));
					
					i++;
				}
				else
				{

					// the tricky case of an embedded quote in the middle: a,bc"d"ef,g
					if (!bMustQuote)
					{
						if (i > 2 // not on the beginning of the line
								&& pText.charAt(i - 1) != this.chSeparator // not at the beginning of an escape sequence
								&& pText.length() > (i + 1) && pText.charAt(i + 1) != this.chSeparator) // not at the end of an escape sequence
						{
							if (bIgnoreLeadingWhiteSpace && sbPart.length() > 0 && isAllWhiteSpace(sbPart))
							{
								sbPart.setLength(0); // discard white space leading up to quote
							}
							else
							{
								sbPart.append(ch);	// continue
							}
						}
						else if (!bRemoveQuotes)
						{
							sbPart.append(ch); //add leading and trailing quotes to the result
						}
					}

					inQuotes = !inQuotes;
				}
				
				bInField = !bInField;
			}
			else if (ch == chSeparator && !inQuotes)
			{
				liTokens.add(sbPart.toString());
				
				sbPart.setLength(0); // start work on next token
				
				bInField = false;
			}
			else
			{
				if (!bMustQuote || inQuotes)
				{
					sbPart.append(ch);
					
					bInField = true;
				}
			}
		}
		// line is done - check status
		if (inQuotes)
		{
			if (pMulti)
			{
				sbPart.append("\n");	// continuing a quoted section, re-append newline
				
				sPending = sbPart.toString();
				
				sbPart = null; // this partial content is not to be added to field list yet
			}
			else
			{
				throw new IOException("Un-terminated quoted field at end of text");
			}
		}
		if (sbPart != null)
		{
			liTokens.add(sbPart.toString());
		}
		return liTokens.toArray(new String[liTokens.size()]);

	}

	/**
	 * Gets whether parsing has open fields from last call(s).
	 * 
	 * @return true if something was left over from last call(s)
	 */
	public boolean isPending()
	{
		return sPending != null;
	}
	
	/**
	 * Gets whether the "next" character is a quote or an escape.
	 * 
	 * @param pText the current text
	 * @param pInQuotes <code>true</code> if the current context is quoted
	 * @param pIndex current index in text
	 * @return <code>true</code> if the following character is a quote
	 */
	private boolean isNextCharacterEscapedQuote(String pText, boolean pInQuotes, int pIndex)
	{
		return pInQuotes // we are in quotes, therefore there can be escaped quotes in here
			   && pText.length() > (pIndex + 1) // there is indeed anothercharacter to check
			   && pText.charAt(pIndex + 1) == chQuote;
	}

	/**
	 * Gets whether the next character is an escape.
	 * 
	 * @param pText the current text
	 * @param pInQuotes <code>true</code> if the current context is quoted
	 * @param pIndex current index in text
	 * @return <code>true</code> if the next character is a quote
	 */
	protected boolean isNextCharacterEscapable(String pText, boolean pInQuotes, int pIndex)
	{
		return pInQuotes // we are in quotes, therefore there can be escapedquotes in here
			   && pText.length() > (pIndex + 1) // there is indeed another character to check
			   && (pText.charAt(pIndex + 1) == chQuote || pText.charAt(pIndex + 1) == this.chEscape);
	}

	/**
	 * Gets whether the given text only contains whitespaces or is empty.
	 * 
	 * @param pText a sequence of characters to examine
	 * @return <code>true</code> if every character in the sequence is whitespace
	 */
	protected boolean isAllWhiteSpace(CharSequence pText)
	{
		boolean bResult = true;
		
		for (int i = 0, cnt = pText.length(); i < cnt; i++)
		{
			if (!Character.isWhitespace(pText.charAt(i)))
			{
				return false;
			}
		}
		
		return bResult;
	}

	/**
	 * Sets whether leading and trailing quotes should be removed from the fields.
	 * 
	 * @param pIgnoreQuotes <code>true</code> to remove quotes from found fields,
	 *                      <code>false</code> if found should contain leading and trailing quotes
	 */
	public void setRemoveQuotes(boolean pIgnoreQuotes)
	{
		bRemoveQuotes = pIgnoreQuotes;
	}

	/**
	 * Gets whether leading and trailing quotes will be removed from the fields.
	 * 
	 * @return <code>true</code> if leading and trailing quotes will be removed,
	 *         <code>false</code> otherwise
	 */
	public boolean isRemoveQuotes()
	{
		return bRemoveQuotes;
	}

	/**
	 * Sets whether a field must be in quotes to be a valid field.
	 * 
	 * @param pMustQuote <code>true</code> to enable strict quote mode, <code>false</code> bo
	 *                   allow mixed fields
	 */
	public void setMustQuote(boolean pMustQuote)
	{
		bMustQuote = pMustQuote;
	}
	
	/**
	 * Gets whether the strict quote mode is enabled.
	 * 
	 * @return <code>true</code> if strict quote mode is enabled, <code>false</code> otherwise
	 * @see #setMustQuote(boolean)
	 */
	public boolean isMustQuote()
	{
		return bMustQuote;
	}
	
	/**
	 * Sets whether leading whitespaces should be ignored.
	 * 
	 * @param pIgnore <code>true</code> to ignore leading whitespaces, 
	 *                <code>false</code> otherwise
	 */
	public void setIgnoreLeadingWhiteSpace(boolean pIgnore)
	{
		bIgnoreLeadingWhiteSpace = pIgnore;
	}
	
	/**
	 * Gets whether leading whitespaces should be ignored.
	 * 
	 * @return <code>true</code> if leading whitespaces will be ignored,
	 *         <code>false</code> otherwise
	 */
	public boolean isIgnoreLeadingWhiteSpace()
	{
		return bIgnoreLeadingWhiteSpace;
	}
	
} 	// StringParser

