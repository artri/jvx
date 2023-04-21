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
import javax.rad.ui.menu.IMenuItem;

import com.sibvisions.rad.ui.web.impl.WebImage;
import com.sibvisions.rad.ui.web.impl.component.AbstractWebActionComponent;

/**
 * Web server implementation of {@link IMenuItem}.
 * 
 * @author Martin Handsteiner
 */
public class WebMenuItem extends AbstractWebActionComponent
                         implements IMenuItem
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>WebMenuItem</code>.
     *
     * @see javax.rad.ui.menu.IMenuItem
     */
	public WebMenuItem()
	{
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
    public void setImage(IImage pImage)
    {
		super.setImage(WebImage.createScaledImage((WebImage)pImage));
    }
    
}	// WebMenuItem
