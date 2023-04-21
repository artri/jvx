/*
 * Copyright 2021 SIB Visions GmbH
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
 * 08.04.2021 - [JR] - creation
 */
package com.sibvisions.rad.ui.web.impl.component;

import javax.rad.ui.IAlignmentConstants;

import com.sibvisions.rad.ui.web.impl.WebComponent;

/**
 * Web server implementation of {@link IAlignmentConstants}.
 * 
 * @author René Jahn
 */
public abstract class AbstractWebAlignmentComponent extends WebComponent
   									                implements IAlignmentConstants
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>AbstractWebAlignmentComponent</code>.
     *
     * @see IAlignmentConstants
     */
	public AbstractWebAlignmentComponent()
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface Implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public int getHorizontalAlignment()
    {
    	return getProperty("horizontalAlignment", Integer.valueOf(IAlignmentConstants.ALIGN_LEFT)).intValue();
    }

	/**
	 * {@inheritDoc}
	 */
	public void setHorizontalAlignment(int pHorizontalAlignment)
    {
		setProperty("horizontalAlignment", Integer.valueOf(pHorizontalAlignment));
    }

	/**
	 * {@inheritDoc}
	 */
	public int getVerticalAlignment()
    {
    	return getProperty("verticalAlignment", Integer.valueOf(IAlignmentConstants.ALIGN_TOP)).intValue();
    }

	/**
	 * {@inheritDoc}
	 */
	public void setVerticalAlignment(int pVerticalAlignment)
    {
		setProperty("verticalAlignment", Integer.valueOf(pVerticalAlignment));
    }

}	// AbstractWebAlignmentComponent
