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
 * 26.11.2009 - [HM] - creation
 */
package com.sibvisions.rad.ui.web.impl.menu;

import javax.rad.ui.IImage;
import javax.rad.ui.menu.ICheckBoxMenuItem;

import com.sibvisions.rad.ui.web.impl.WebImage;

/**
 * Web server implementation of {@link ICheckBoxMenuItem}.
 * 
 * @author Martin Handsteiner
 */
public class WebCheckBoxMenuItem extends WebMenuItem
                                 implements ICheckBoxMenuItem
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the selected image. */
	private WebImage pressedImage = null;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>WebCheckBoxMenuItem</code>.
     *
     * @see javax.rad.ui.menu.ICheckBoxMenuItem
     */
	public WebCheckBoxMenuItem()
	{
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
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
    }
    
	/**
	 * {@inheritDoc}
	 */
    public void setPressedImage(IImage pPressedImage)
    {
    	pressedImage = (WebImage)pPressedImage;
    }

	/**
	 * {@inheritDoc}
	 */
    public WebImage getPressedImage()
    {
    	return pressedImage;
    }

}	// WebCheckBoxMenuItem
