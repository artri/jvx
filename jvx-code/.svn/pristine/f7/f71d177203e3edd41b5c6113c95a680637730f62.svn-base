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

import javax.rad.ui.IImage;
import javax.rad.ui.component.IButton;

import com.sibvisions.rad.ui.web.impl.WebImage;

/**
 * Web server implementation of {@link IButton}.
 * 
 * @author Martin Handsteiner
 */
public class WebButton extends AbstractWebFormatableButton
                       implements IButton
{

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>WebButton</code>.
     *
     * @see javax.rad.ui.component.IButton
     */
	public WebButton()
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface Implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
    public boolean isBorderOnMouseEntered()
    {
    	return getProperty("borderOnMouseEntered", Boolean.FALSE).booleanValue();
    }

	/**
	 * {@inheritDoc}
	 */
    public void setBorderOnMouseEntered(boolean pBorderOnMouseEntered)
    {
    	setProperty("borderOnMouseEntered", Boolean.valueOf(pBorderOnMouseEntered));
    }

    /**
	 * {@inheritDoc}
	 */
    public boolean isBorderPainted()
    {
    	return getProperty("borderPainted", Boolean.TRUE).booleanValue();
    }
    
    /**
	 * {@inheritDoc}
	 */
    public void setBorderPainted(boolean pBorderPainted)
    {
    	setProperty("borderPainted", Boolean.valueOf(pBorderPainted));
    }
    
    /**
	 * {@inheritDoc}
	 */
    public WebImage getMouseOverImage()
    {
    	return getProperty("mouseOverImage", null);
    }
    
    /**
	 * {@inheritDoc}
	 */
    public void setMouseOverImage(IImage pMouseOverImage)
    {
    	setProperty("mouseOverImage", pMouseOverImage);
    }
    
    /**
	 * {@inheritDoc}
	 */
    public WebImage getPressedImage()
    {
    	return getProperty("mousePressedImage", null);
    }

    /**
	 * {@inheritDoc}
	 */
    public void setPressedImage(IImage pMousePressedImage)
    {
		setProperty("mousePressedImage", pMousePressedImage);
    }
    
	/**
	 * {@inheritDoc}
	 */
    public boolean isDefaultButton()
    {
    	return getProperty("defaultButton", Boolean.FALSE).booleanValue();
    }
    
    /**
	 * {@inheritDoc}
	 */
    public void setDefaultButton(boolean pDefaultButton)
    {
    	setProperty("defaultButton", Boolean.valueOf(pDefaultButton));
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
	 * {@inheritDoc}
	 */
	@Override
	public void setText(String pText)
	{
		super.setText(pText);
		
		updateAccessibility();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setToolTipText(String pToolTipText)
	{
		super.setToolTipText(pToolTipText);
		
		updateAccessibility();
	}
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Updates the accessibility attributes of this button.
	 */
	private void updateAccessibility()
	{
		setProperty("ariaLabel", getAriaLabel(getToolTipText(), getText()));
	}
    
}	// WebButton
