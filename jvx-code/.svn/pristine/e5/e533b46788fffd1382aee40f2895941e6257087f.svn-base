/*
 * Copyright 2018 SIB Visions GmbH
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
 * 21.03.2018 - [JR] - creation
 */
package com.sibvisions.rad.ui.web.impl.component;

import javax.rad.ui.IImage;
import javax.rad.ui.component.IActionComponent;
import javax.rad.ui.component.ILabeledIcon;

import com.sibvisions.rad.ui.web.impl.WebImage;

/**
 * Web server implementation of {@link ILabeledIcon}.
 * 
 * @author René Jahn
 */
public abstract class AbstractWebLabeledIconComponent extends AbstractWebLabelComponent
                                                      implements ILabeledIcon 
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>AbstractWebLabeledIconComponent</code>.
     *
     * @see IActionComponent
     */
	protected AbstractWebLabeledIconComponent()
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface Implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
    public WebImage getImage()
    {
    	return getProperty("image", null);
    }
    
	/**
	 * {@inheritDoc}
	 */
    public void setImage(IImage pImage)
    {
		setProperty("image", pImage);
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean isPreserveAspectRatio()
    {
        return getProperty("preserveAspectRatio", Boolean.FALSE).booleanValue();
    }

    /**
     * {@inheritDoc}
     */
    public void setPreserveAspectRatio(boolean pPreserveAspectRatio)
    {
        setProperty("preserveAspectRatio", Boolean.valueOf(pPreserveAspectRatio));
    }    
    
}	// AbstractWebLabeledIconComponent
