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

import javax.rad.ui.celleditor.INumberCellEditor;

import com.sibvisions.util.type.NumberUtil;

/**
 * The {@link AbstractNumberCellEditor} is an {@link INumberCellEditor}
 * implementation, which provides a base implementation.
 * 
 * @author Robert Zenz
 */
public abstract class AbstractNumberCellEditor extends AbstractInplaceCellEditor 
                                               implements INumberCellEditor
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The number util. */
	protected transient NumberUtil numberUtil = new NumberUtil();
    /** The creation number format. */
    protected transient String     creationNumberFormat = null;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link AbstractNumberCellEditor}.
	 */
	protected AbstractNumberCellEditor()
	{
		this(null);
	}
	
	/**
	 * Creates a new instance of {@link AbstractNumberCellEditor}.
	 *
	 * @param pInitialNumberFormat the {@link String initial number format}.
	 */
	protected AbstractNumberCellEditor(String pInitialNumberFormat)
	{
        setHorizontalAlignment(ALIGN_RIGHT);
        
		setNumberFormat(pInitialNumberFormat);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public String getNumberFormat()
	{
		return numberUtil.getNumberPattern();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setNumberFormat(String pNumberFormat)
	{
	    creationNumberFormat = pNumberFormat;
	    
		numberUtil.setNumberPattern(creationNumberFormat, numberUtil.getLocale());
	}
	
    /**
     * {@inheritDoc}
     */
    public Locale getLocale()
    {
        return numberUtil.getLocale();
    }

    /**
     * {@inheritDoc}
     */
    public void setLocale(Locale pLocale)
    {
        numberUtil.setNumberPattern(creationNumberFormat, numberUtil.getLocale());
    }

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isDirectCellEditor()
	{
		return false;
	}
	
}	// AbstractNumberCellEditor
