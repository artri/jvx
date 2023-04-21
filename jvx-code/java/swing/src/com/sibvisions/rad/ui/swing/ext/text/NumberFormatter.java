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
 * 27.11.2008 - [HM] - creation
 * 05.08.2009 - [JR] - replaceAllowed: null check 
 */
package com.sibvisions.rad.ui.swing.ext.text;

import java.text.NumberFormat;
import java.text.ParseException;

import javax.rad.model.datatype.IDataType;
import javax.swing.JFormattedTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;

import com.sibvisions.util.type.NumberUtil;

/**
 * The <code>NumberFormatter</code> can handle null values and empty Strings and handle complex number formats.
 *  
 * @author Martin Handsteiner
 */
public class NumberFormatter extends JFormattedTextField.AbstractFormatter
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The number util. */
	private NumberUtil numberUtil;

	/** The possible underlaying datatype. */
	private IDataType dataType;
	
	/** The document filter. */
	private NumberDocumentFilter documentFilter = null;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** 
	 * Constructs a new <code>NumberFormatter</code> with default number format.
	 */
	public NumberFormatter()
	{
		this(null);
	}

	/** 
	 * Constructs a new <code>NumberFormatter</code> with a specific number format.
	 * 
	 * @param pNumberUtil the number format
	 */
	public NumberFormatter(NumberUtil pNumberUtil)
	{
		if (pNumberUtil == null)
		{
			numberUtil = new NumberUtil();
		}
		else
		{
			numberUtil = pNumberUtil;
		}
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
    protected DocumentFilter getDocumentFilter() 
	{
        if (documentFilter == null) 
        {
            documentFilter = new NumberDocumentFilter();
        }
        return documentFilter;
    }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object stringToValue(String pText) throws ParseException
	{
		Object value = numberUtil.parse(pText);
		
		if (dataType != null)
		{
			try
			{
				dataType.convertAndCheckToTypeClass(value);
			}
			catch (Exception pException)
			{
				throw new ParseException(pText, 0);
			}
		}
		
		return value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String valueToString(Object pValue) throws ParseException
	{
		if (pValue == null)
		{   
			return "";
		}
		else
		{
			return numberUtil.format((Number)pValue);
		}
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the data type.
	 * 
	 * @return thedata type.
	 */
	public IDataType getDataType()
	{
		return dataType;
	}

	/**
	 * Sets the data type.
	 * 
	 * @param pDataType the data type.
	 */
	public void setDataType(IDataType pDataType)
	{
		dataType = pDataType;
	}

    /**
	 * Gets the number format.
	 * 
	 * @return the number format.
	 * @deprecated use {@link #getNumberPattern()} instead
	 */
    @Deprecated
	public NumberFormat getNumberFormat()
	{
		return numberUtil.getNumberFormat();
	}

	/**
	 * Gets the number format.
	 * 
	 * @param pNumberFormat the number format.
     * @deprecated use {@link #setNumberPattern(String)} instead
	 */
    @Deprecated
	public void setNumberFormat(NumberFormat pNumberFormat)
	{
		numberUtil.setNumberFormat(pNumberFormat);
	}

	/**
	 * Gets the number pattern.
	 * 
	 * @return the number pattern.
	 */
	public String getNumberPattern()
	{
		return numberUtil.getNumberPattern();
	}

	/**
	 * Gets the number pattern.
	 * 
	 * @param pNumberPattern the number pattern.
	 */
	public void setNumberPattern(String pNumberPattern)
	{
		numberUtil.setNumberPattern(pNumberPattern);
	}
	
	/**
     * Replaces only the allowed text.
     *
     * @param pFb Filter bypass
     * @param pOffset Location in Document
     * @param pLength Length of text to delete
     * @param pText Text to insert, null indicates no text to insert
     * @param pAttr AttributeSet indicating attributes of inserted text, null is legal.
     * @exception BadLocationException  the given insert is not a valid position within the document
     */
	public void replaceAllowed(DocumentFilter.FilterBypass pFb, int pOffset, int pLength, String pText, AttributeSet pAttr) throws BadLocationException 
	{
		Document doc = pFb.getDocument();
		String oldText = doc.getText(0, doc.getLength());
		
		String newText = oldText.substring(0, pOffset) + (pText != null ? pText : "") + oldText.substring(pOffset + pLength);
		try 
		{
		    int minusIndex = newText.lastIndexOf('-');
			if (minusIndex > 0 && !newText.substring(0, minusIndex).endsWith("E"))
			{
				if (newText.startsWith("-"))
				{
					newText = newText.substring(1, minusIndex) + newText.substring(minusIndex + 1);
				}
				else
				{
					newText = "-" + newText.substring(0, minusIndex) + newText.substring(minusIndex + 1);
				}
				stringToValue(newText);
				pFb.replace(0, oldText.length(), newText, pAttr);
			}
			else
			{
				stringToValue(newText);
				pFb.replace(pOffset, pLength, pText, pAttr);
			}
		}
		catch (ParseException pEx)
		{
			invalidEdit();
		}
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************

	/**
     * DocumentFilter implementation that calls back to the replace
     * method of DefaultFormatter.
     * 
     * @author Martin Handsteiner
     */
    private class NumberDocumentFilter extends DocumentFilter 
    {
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	// Overwritten methods
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	
    	/**
    	 * {@inheritDoc}
    	 */
    	@Override
        public void remove(FilterBypass pFb, int pOffset, int pLength) throws BadLocationException 
        {
        	replaceAllowed(pFb, pOffset, pLength, "", null);
        }

    	/**
    	 * {@inheritDoc}
    	 */
    	@Override
        public void insertString(FilterBypass pFb, int pOffset, String pText, AttributeSet pAttr) throws BadLocationException 
        {
        	replaceAllowed(pFb, pOffset, 0, pText, pAttr);
        }

    	/**
    	 * {@inheritDoc}
    	 */
    	@Override
        public void replace(FilterBypass pFb, int pOffset, int pLength, String pText, AttributeSet pAttr) throws BadLocationException 
        {
        	replaceAllowed(pFb, pOffset, pLength, pText, pAttr);
        }
    	
    }	// NumberDocumentFilter

}	// NumberFormatter
