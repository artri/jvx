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
 * 11.10.2012 - [CB] - creation
 */
package com.sibvisions.rad.ui.vaadin.impl.component;

import javax.rad.ui.IImage;
import javax.rad.ui.component.ICheckBox;
import javax.rad.ui.event.Key;
import javax.rad.ui.event.UIActionEvent;

import com.sibvisions.rad.ui.vaadin.ext.ui.AccessibilityUtil;
import com.sibvisions.rad.ui.vaadin.ext.ui.client.CssExtensionAttribute;
import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.data.HasValue.ValueChangeListener;
import com.vaadin.ui.CheckBox;

/**
 * The <code>VaadinCheckBox</code> class is the vaadin implementation of {@link ICheckBox}.
 * 
 * @author Benedikt Cermak
 */
public class VaadinCheckBox extends AbstractVaadinCaptionComponent<CheckBox>
                            implements ICheckBox,
                        		       ValueChangeListener
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** If the change event shoul be ignored. */
	private boolean ignoreChangeEvent = false;
	
	/** The gap between the Image and the Text. **/
	private int imageTextGap = 0;
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Creates a new instance of <code>VaadinCheckBox</code>.
     *
     * @see javax.rad.ui.component.ICheckBox
     */
	public VaadinCheckBox()
	{
		super(new CheckBox());

		addInternStyleName("jvxcheckbox");
		
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
    	//Not supported
    	return null;
    }
    
	/**
	 * {@inheritDoc}
	 */
    public void setAccelerator(Key pAccelerator)
    {
    	//Not supported
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
		//Not supported
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getVerticalTextPosition()
	{
		//Not supported
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setHorizontalTextPosition(int pHorizontalPosition)
	{
		//Not supported
	}

	/**
	 * {@inheritDoc}
	 */
	public int getHorizontalTextPosition()
	{
		//Not supported
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isBorderOnMouseEntered()
	{
		//Not supported
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setBorderOnMouseEntered(boolean pBorderOnMouseEntered)
	{
		//Not supported
	}

	/**
	 * {@inheritDoc}
	 */
	public void setBorderPainted(boolean pBorderPainted)
	{
		//Not supported
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isBorderPainted()
	{
		//Not supported
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setMouseOverImage(IImage pImage)
	{
		//Not supported
	}

	/**
	 * {@inheritDoc}
	 */
	public IImage getMouseOverImage()
	{
		//Not supported
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setPressedImage(IImage pImage)
	{
		//Not supported
	}

	/**
	 * {@inheritDoc}
	 */
	public IImage getPressedImage()
	{
		//Not supported
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setDefaultButton(boolean pDefault)
	{
		//Not supported
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isDefaultButton()
	{
		//Not supported
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
		ignoreChangeEvent = true;
		
		resource.setValue(Boolean.valueOf(pPressed));
		
		AccessibilityUtil.setPressed(getAttributesExtension(), pPressed);		
		
		ignoreChangeEvent = false;
	}

	/**
	 * {@inheritDoc}
	 */
	public void valueChange(ValueChangeEvent pEvent)
	{
		if (!ignoreChangeEvent)
    	{
			Boolean value = (Boolean)pEvent.getValue();
		
			AccessibilityUtil.setPressed(getAttributesExtension(), value.booleanValue());
			
			if (eventActionPerformed != null)
			{
				getFactory().synchronizedDispatchEvent(eventActionPerformed, new UIActionEvent(eventSource, 
	    															 					 	   UIActionEvent.ACTION_PERFORMED, 
	    															 					 	   System.currentTimeMillis(), 
	    															 					 	   0, 
	    															 					 	   getActionCommand()));
			}
    	}
	}
    
}	// VaadinCheckBox
