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
 * 18.10.2012 - [CB] - creation
 */
package com.sibvisions.rad.ui.vaadin.impl.container;

import javax.rad.ui.IImage;
import javax.rad.ui.container.IPanel;

import com.sibvisions.rad.ui.vaadin.ext.ui.SimplePanel;
import com.sibvisions.rad.ui.vaadin.impl.VaadinImage;
import com.sibvisions.rad.ui.vaadin.impl.VaadinSingleComponentContainer;

/**
 * The <code>VaadinPanel</code> class is the vaadin implementation of {@link IPanel}.
 * 
 * @author Benedikt Cermak
 */
public class VaadinPanel extends VaadinSingleComponentContainer<SimplePanel>
                         implements IPanel
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the background image. */
	private VaadinImage backGroundImg = null;
	
	/** the overflow CSS attribute. */
	private String sOverflow;
	
	/** if scrollbars should be shown. **/
	private boolean bScrolling = false;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>VaadinPanel</code>.
     *
     * @see IPanel
     */
	public VaadinPanel()
	{
		super(new SimplePanel());
		
		resource.setSizeUndefined();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public IImage getBackgroundImage()
	{
		return backGroundImg;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setBackgroundImage(IImage pBackgroundImage)
	{
		backGroundImg = (VaadinImage)pBackgroundImage;
		
		if (backGroundImg != null)
		{
		    resource.setBackgroundImage(backGroundImg.getResource());
		}
		else
		{
			resource.setBackgroundImage(null);
		}
	}
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	

	/**
	 * Sets the overflow attribute.
	 * 
	 * @param pOverflow the value
	 */
	public void setOverflow(String pOverflow)
	{
		sOverflow = pOverflow;
		
		if (pOverflow == null)
		{
			getCssExtension().removeAttribute("overflow", true);
		}
		else
		{
			getCssExtension().addAttribute("overflow", pOverflow);
		}
	}
	
	/**
	 * Gets the overflow attribute.
	 * 
	 * @return the value
	 */
	public String getOverflow()
	{
		return sOverflow;
	}
	
	/**
	 * Sets whether scrollbars should be shown.
	 * 
	 * @param pScrolling if scrollbars should be shown.
	 */
	public void setScrolling(boolean pScrolling)
	{
		bScrolling = pScrolling;
		
		if (bScrolling)
		{
			addInternStyleName("v-scrolling");
		}
		else
		{
			removeInternStyleName("v-scrolling");
		}
	}
	
	/**
	 * Gets whether the scrollbars are shown.
	 * 
	 * @return <code>true</code> if scrollbars are shown, <code>false</code> otherwise
	 */
	public boolean isScrolling()
	{
		return bScrolling;
	}
	
}	// VaadinPanel
