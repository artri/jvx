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
 * 19.11.2009 - [HM] - creation
 * 18.03.2011 - [JR] - #314: border visibility support for TextField/Area
 */
package com.sibvisions.rad.ui.web.impl.component;

import javax.rad.ui.component.ITextArea;

/**
 * Web server implementation of {@link ITextArea}.
 * 
 * @author Martin Handsteiner
 */
public class WebTextArea extends WebTextField
                         implements ITextArea
{

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>WebTextArea</code>.
     *
     * @see javax.rad.ui.component.ITextArea
     */
	public WebTextArea()
	{
		setBorderVisible(true);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface Implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
    public int getRows()
    {
    	return getProperty("rows", Integer.valueOf(5)).intValue();
    }

	/**
	 * {@inheritDoc}
	 */
    public void setRows(int pRows)
    {
    	setProperty("rows", Integer.valueOf(pRows));
    }

	/**
	 * {@inheritDoc}
	 */
	public boolean isWordWrap()
	{
		return getProperty("wordWrap", Boolean.FALSE).booleanValue();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setWordWrap(boolean pWordWrap)
	{
		setProperty("wordWrap", Boolean.valueOf(pWordWrap));
	}

}	// WebTextArea
