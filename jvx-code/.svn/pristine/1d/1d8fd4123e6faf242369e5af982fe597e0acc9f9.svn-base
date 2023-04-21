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

import javax.rad.ui.IAlignmentConstants;
import javax.rad.ui.IInsets;
import javax.rad.ui.component.IActionComponent;
import javax.rad.ui.component.IFormatableButton;
import javax.rad.ui.component.ILabeledIcon;

import com.sibvisions.rad.ui.web.impl.WebInsets;

/**
 * Web server implementation of {@link ILabeledIcon}.
 * 
 * @author René Jahn
 */
public abstract class AbstractWebFormatableButton extends AbstractWebMarginActionComponent
                                                  implements IFormatableButton
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>AbstractWebFormatableButton</code>.
     *
     * @see IActionComponent
     */
	protected AbstractWebFormatableButton()
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface Implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public WebInsets getMargins()
	{
    	return getProperty("margins", new WebInsets());
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setMargins(IInsets pMargins)
	{
		setProperty("margins", pMargins);
	}

	/**
	 * {@inheritDoc}
	 */
	public int getVerticalTextPosition()
    {
    	return getProperty("verticalTextPosition", Integer.valueOf(IAlignmentConstants.ALIGN_CENTER)).intValue();
    }

	/**
	 * {@inheritDoc}
	 */
	public void setVerticalTextPosition(int pVerticalTextPosition)
    {
		setProperty("verticalTextPosition", Integer.valueOf(pVerticalTextPosition));
    }
	
	/**
	 * {@inheritDoc}
	 */
	public int getHorizontalTextPosition()
    {
    	return getProperty("horizontalTextPosition", Integer.valueOf(IAlignmentConstants.ALIGN_RIGHT)).intValue();
    }

	/**
	 * {@inheritDoc}
	 */
	public void setHorizontalTextPosition(int pHorizontalTextPosition)
    {
		setProperty("horizontalTextPosition", Integer.valueOf(pHorizontalTextPosition));
    }

	/**
	 * {@inheritDoc}
	 */
    public int getImageTextGap()
    {
    	return getProperty("imageTextGap", Integer.valueOf(5)).intValue();
    }

	/**
	 * {@inheritDoc}
	 */
    public void setImageTextGap(int pImageTextGap)
    {
    	setProperty("imageTextGap", Integer.valueOf(pImageTextGap));
    }
    
}	// AbstractWebLabeledIconComponent
