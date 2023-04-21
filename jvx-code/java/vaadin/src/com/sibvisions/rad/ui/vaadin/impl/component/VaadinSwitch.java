/*
 * Copyright 2012 SIB Visions GmbH
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
 * 07.04.2023 - [HM] - creation
 */
package com.sibvisions.rad.ui.vaadin.impl.component;

import javax.rad.ui.IImage;
import javax.rad.ui.component.ISwitch;
import javax.rad.ui.event.Key;
import javax.rad.ui.event.UIActionEvent;

import com.sibvisions.rad.ui.vaadin.ext.ui.Switch;
import com.sibvisions.rad.ui.vaadin.ext.ui.client.CssExtensionAttribute;
import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.data.HasValue.ValueChangeListener;

/**
 * The <code>VaadinSwitch</code> class is the implementation of {@link ISwitch}.
 * 
 * @author Martin Handsteiner
 */
public class VaadinSwitch extends AbstractVaadinCaptionComponent<Switch> 
							   implements ISwitch, 
									      ValueChangeListener<Boolean>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The gap between the image and text. **/
	private int imageTextGap = 0;
	
	/** whether changed event should be ignored. */
	private boolean bIgnoreChangedEvent;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Creates a new instance of <code>VaadinSwitch</code>.
     *
     * @see javax.rad.ui.component.ISwitch
     */
	public VaadinSwitch()
	{
		super(new Switch());

	    resource.addValueChangeListener(this);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface Implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
    public Key getAccelerator()
    {
    	// not supported
    	return null;
    }
    
	/**
	 * {@inheritDoc}
	 */
    public void setAccelerator(Key pAccelerator)
    {
    	// not supported
    }
	
	/**
	 * {@inheritDoc}
	 */
	public int getImageTextGap()
	{
		return imageTextGap;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setImageTextGap(int pImageTextGap)
	{
		imageTextGap = pImageTextGap; 
		
		CssExtensionAttribute cssAttribute = new CssExtensionAttribute("margin-right", imageTextGap + "px");
		cssAttribute.setElementClassName("v-icon");
		cssAttribute.setSearchDirection(CssExtensionAttribute.SEARCH_DOWN);
		
		getCssExtension().addAttribute(cssAttribute);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setVerticalTextPosition(int pVerticalPosition)
	{
		// not supported
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getVerticalTextPosition()
	{
		// not supported
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setHorizontalTextPosition(int pHorizontalPosition)
	{
		// not supported
	}

	/**
	 * {@inheritDoc}
	 */
	public int getHorizontalTextPosition()
	{
		// not supported
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isBorderOnMouseEntered()
	{
		// not supported
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setBorderOnMouseEntered(boolean pBorderOnMouseEntered)
	{
		// not supported
	}

	/**
	 * {@inheritDoc}
	 */
	public void setBorderPainted(boolean pBorderPainted)
	{
		// not supported
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isBorderPainted()
	{
		// not supported
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setMouseOverImage(IImage pImage)
	{
		// not supported
	}

	/**
	 * {@inheritDoc}
	 */
	public IImage getMouseOverImage()
	{
		// not supported
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setPressedImage(IImage pImage)
	{
		// not supported
	}

	/**
	 * {@inheritDoc}
	 */
	public IImage getPressedImage()
	{
		// not supported
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setDefaultButton(boolean pDefault)
	{
		// not supported
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isDefaultButton()
	{
		// not supported
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isSelected()
	{
		return ((Boolean)resource.getValue()).booleanValue();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setSelected(boolean pPressed)
	{
	    bIgnoreChangedEvent = true;
		
		try
		{
		    resource.setValue(Boolean.valueOf(pPressed));
		}
		finally
		{
		    bIgnoreChangedEvent = false;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void valueChange(ValueChangeEvent<Boolean> pEvent)
	{
		if (eventActionPerformed != null && !bIgnoreChangedEvent)
    	{
			getFactory().synchronizedDispatchEvent(eventActionPerformed, new UIActionEvent(eventSource, 
    															 					 	   UIActionEvent.ACTION_PERFORMED, 
    															 					 	   System.currentTimeMillis(), 
    															 					 	   0, 
    															 					 	   sActionCommand));
    	}
	}
	
} 	// VaadinSwitch
