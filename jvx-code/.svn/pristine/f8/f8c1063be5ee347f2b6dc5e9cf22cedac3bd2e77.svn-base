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
 */
package com.sibvisions.rad.ui.swing.ext.text;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.swing.JFormattedTextField;

import com.sibvisions.util.type.DateUtil;
import com.sibvisions.util.type.LocaleUtil;
import com.sibvisions.util.type.TimeZoneUtil;

/**
 * The <code>DateFormatter</code> can handle null values and empty Strings and handle complex date formats.
 *  
 * @author Martin Handsteiner
 */
public class DateFormatter extends JFormattedTextField.AbstractFormatter
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The date util. */
	private DateUtil dateUtil = new DateUtil();
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** 
	 * Constructs a new date formatter with default date format.
	 */
	public DateFormatter()
	{
		this(null);
	}

	/** 
	 * Constructs a new date formatter that supports empty Strings and null values.
	 * @param pDateUtil the formatter that should support empty Strings and null values.
	 */
	public DateFormatter(DateUtil pDateUtil)
	{
		if (pDateUtil == null)
		{
			dateUtil = new DateUtil();
		}
		else
		{
			dateUtil = pDateUtil;
		}
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object stringToValue(String pText) throws ParseException
	{
		return dateUtil.parse(pText);
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
			return dateUtil.format((Date)pValue);
		}
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the date format.
	 * 
	 * @return the date format.
	 * @deprecated use {@link #getDatePattern()} instead
	 */
	@Deprecated
    public DateFormat getDateFormat()
	{
		return dateUtil.getDateFormat();
	}

	/**
	 * Gets the date format.
	 * 
	 * @param pDateFormat the date format.
     * @deprecated use {@link #setDatePattern(String)} instead
	 */
    @Deprecated
    public void setDateFormat(DateFormat pDateFormat)
	{
		dateUtil.setDateFormat(pDateFormat);
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
        return dateUtil.getDatePattern();
    }

    /**
     * Gets the locale that is used for creation.
     * Null means, that the {@link LocaleUtil#getDefault()} is used.
     * 
     * @return the locale or null for {@link LocaleUtil#getDefault()}.
     */
    public Locale getLocale()
    {
        return dateUtil.getLocale();
    }
    
    /**
     * Gets the timeZone that is used for creation.
     * Null means, that the {@link TimeZoneUtil#getDefault()} is used.
     * 
     * @return the time zone or null for {@link TimeZoneUtil#getDefault()}
     */
    public TimeZone getTimeZone()
    {
        return dateUtil.getTimeZone();
    }
    
	/**
	 * Gets the date format pattern.
	 * 
	 * @param pDatePattern the date format pattern.
	 */
	public void setDatePattern(String pDatePattern)
	{
		dateUtil.setDatePattern(pDatePattern);
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
        dateUtil.setDatePattern(pDatePattern, pLocale);
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
        dateUtil.setDatePattern(pDatePattern, pLocale, pTimeZone);
    }
	
}	// DateFormatter
