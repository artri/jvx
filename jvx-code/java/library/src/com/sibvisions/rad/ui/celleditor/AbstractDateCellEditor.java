/*
 * Copyright 2015 SIB Visions GmbH
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
 * 11.11.2015 - [RZ] - creation
 */
package com.sibvisions.rad.ui.celleditor;

import java.util.Locale;
import java.util.TimeZone;

import javax.rad.ui.celleditor.IDateCellEditor;

import com.sibvisions.util.type.DateUtil;

/**
 * The {@link AbstractDateCellEditor} is an {@link IDateCellEditor}
 * implementation, which provides a base implementation.
 * 
 * @author Robert Zenz
 */
public abstract class AbstractDateCellEditor extends AbstractComboCellEditor 
                                             implements IDateCellEditor
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The date util. */
	protected transient DateUtil dateUtil = new DateUtil();
    /** The creation date format. */
    protected transient String   creationDateFormat = null;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link AbstractDateCellEditor}.
	 */
	protected AbstractDateCellEditor()
	{
		this(null);
	}
	
	/**
	 * Creates a new instance of {@link AbstractDateCellEditor}.
	 *
	 * @param pInitialFormat the {@link String initial format}.
	 */
	protected AbstractDateCellEditor(String pInitialFormat)
	{
		setDateFormat(pInitialFormat);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public String getDateFormat()
	{
		return dateUtil.getDatePattern();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setDateFormat(String pDateFormat)
	{
	    creationDateFormat = pDateFormat;
	    
		dateUtil.setDatePattern(creationDateFormat, dateUtil.getLocale(), dateUtil.getTimeZone());
	}
	
    /**
     * {@inheritDoc}
     */
    public TimeZone getTimeZone()
    {
        return dateUtil.getTimeZone();
    }

    /**
     * {@inheritDoc}
     */
    public void setTimeZone(TimeZone pTimeZone)
    {
        dateUtil.setDatePattern(creationDateFormat, dateUtil.getLocale(), pTimeZone);
    }

    /**
     * {@inheritDoc}
     */
    public Locale getLocale()
    {
        return dateUtil.getLocale();
    }

    /**
     * {@inheritDoc}
     */
    public void setLocale(Locale pLocale)
    {
        dateUtil.setDatePattern(creationDateFormat, pLocale, dateUtil.getTimeZone());
    }

}	// AbstractDateCellEditor
