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

import javax.rad.ui.IAlignmentConstants;
import javax.rad.ui.component.IIcon;

/**
 * Web server implementation of {@link IIcon}.
 * 
 * @author Martin Handsteiner
 */
public class WebIcon extends AbstractWebLabeledIconComponent
                     implements IIcon
{
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>WebIcon</code>.
     *
     * @see IIcon
     */
	public WebIcon()
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getHorizontalAlignment()
    {
    	return getProperty("horizontalAlignment", Integer.valueOf(IAlignmentConstants.ALIGN_CENTER)).intValue();
    }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getVerticalAlignment()
    {
    	return getProperty("verticalAlignment", Integer.valueOf(IAlignmentConstants.ALIGN_CENTER)).intValue();
    }
    
}	// WebIcon
