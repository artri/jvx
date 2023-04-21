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
 */
package com.sibvisions.rad.ui.web.impl.component;

import javax.rad.ui.component.IToggleButton;

/**
 * Web server implementation of {@link IToggleButton}.
 * 
 * @author Martin Handsteiner
 */
public class WebToggleButton extends WebButton
                             implements IToggleButton
{

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>WebToggleButton</code>.
     *
     * @see javax.rad.ui.component.IToggleButton
     */
	public WebToggleButton()
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface Implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public boolean isSelected()
    {
    	return getProperty("selected", Boolean.FALSE).booleanValue();
    }

	/**
	 * {@inheritDoc}
	 */
    public void setSelected(boolean pSelected)
    {
    	setProperty("selected", Boolean.valueOf(pSelected));
    	
		setProperty("ariaPressed", pSelected ? Boolean.TRUE : Boolean.FALSE);
    }
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 
    /**
     * {@inheritDoc}
     */
    @Override
    public void click()
    {
    	if (isEnabled() && isVisible())
    	{
    		setSelected(!isSelected());
    	}
    	
    	super.click();
    }
    
}	// WebToggleButton
