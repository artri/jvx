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
 * 17.10.2012 - [CB] - creation
 */
package com.sibvisions.rad.ui.vaadin.impl.container;

import javax.rad.ui.IComponent;
import javax.rad.ui.IRectangle;

import com.sibvisions.rad.ui.vaadin.impl.layout.VaadinClientBorderLayout;
import com.vaadin.ui.Window;

/**
 * The <code>VaadinWindow</code> class is the concrete {@link AbstractVaadinWindow} for a {@link Window} resource.
 * It adds missing features like center and title support.
 * 
 * @author Benedikt Cermak
 */
public class VaadinWindow extends AbstractVaadinWindow<Window>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>VaadinWindow</code>.
     *
     * @see javax.rad.ui.container.IWindow
     */
	public VaadinWindow()
	{
		super(new Window());
		
		resource.center();
		setLayout(new VaadinClientBorderLayout());
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public void toFront()
	{
		resource.bringToFront();
	}

	/**
	 * {@inheritDoc}
	 */
	public void centerRelativeTo(IComponent pComponent)
	{
		resource.center();		
	}		

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	@Override
	public void setBounds(IRectangle pBounds)
	{
		super.setBounds(pBounds);
		
        resource.setPositionX(pBounds.getX());
        resource.setPositionY(pBounds.getY());		
	}	
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
	
	/**
	 * Gets the title of the window.
	 * 
	 * @return the window title
	 */
	public String getTitle()
    {
		return resource.getCaption();
    }

	/**
	 * Sets the title of the window.
	 * 
	 * @param pTitle the window title
	 */
	public void setTitle(String pTitle)
    {
		resource.setCaption(pTitle);
    }

}	// VaadinWindow
