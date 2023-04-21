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

import javax.rad.ui.component.ITextField;

/**
 * Web server implementation of {@link ITextField}.
 * 
 * @author Martin Handsteiner
 */
public class WebTextField extends AbstractWebLabelComponent
                          implements ITextField
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** the placeholder. */
	private String sPlaceholder;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>WebTextField</code>.
     * 
     * @see	javax.rad.ui.component.ITextField
     */
	public WebTextField()
	{
		setBorderVisible(true);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface Implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
    public int getColumns()
    {
    	return getProperty("columns", Integer.valueOf(10)).intValue();
    }

	/**
	 * {@inheritDoc}
	 */
    public void setColumns(int pColumns)
    {
    	setProperty("columns", Integer.valueOf(pColumns));
    }

	/**
	 * {@inheritDoc}
	 */
    public boolean isEditable()
    {
    	return getProperty("editable", Boolean.TRUE).booleanValue();
    }

	/**
	 * {@inheritDoc}
	 */
    public void setEditable(boolean pEditable)
    {
    	setProperty("editable", Boolean.valueOf(pEditable));
    }
    
	/**
	 * {@inheritDoc}
	 */
    public void setBorderVisible(boolean pVisible)
    {
    	setProperty("border", Boolean.valueOf(pVisible));
    }
    
	/**
	 * {@inheritDoc}
	 */
    public boolean isBorderVisible()
    {
    	return getProperty("border", Boolean.TRUE).booleanValue();
    }	    
    
	/**
	 * {@inheritDoc}
	 */
    public void selectAll()
    {
    	setProperty("selectAll", Boolean.TRUE, true);
    }	
    
	/**
	 * {@inheritDoc}
	 */
    public String getPlaceholder()
    {
    	return sPlaceholder;
    }
    
	/**
	 * {@inheritDoc}
	 */
    public void setPlaceholder(String pPlaceholder)
    {
    	sPlaceholder = pPlaceholder;
    	
    	setProperty("placeholder", sPlaceholder);
    }    

}	// WebTextField
