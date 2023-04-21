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
 * 03.10.2012 - [CB] - creation
 */
package com.sibvisions.rad.ui.vaadin.impl;

import javax.rad.ui.IDimension;

/**
 * The <code>VaadinDimension</code> class is the vaadin implementation of {@link IDimension}.
 * 
 * @author Benedikt Cermak
 */
public class VaadinDimension extends VaadinResourceBase<IDimension> 
                             implements IDimension
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class Members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
	/** the width. */
	private int width;
	/** the height. */
	private int height;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
	/**
	 * Creates a new instance of <code>VaadinDimension</code> based on 
	 * a {@link IDimension}.
	 * 
	 * @param pDimension the dimension
	 * @see javax.rad.ui.IDimension
	 */
    public VaadinDimension(IDimension pDimension)
    {
    	this(pDimension.getWidth(), pDimension.getHeight());
    }

    /**
     * Creates a new instance of <code>VaadinDimension</code> with the given with and height.
     *
     * @param pWidth the width 
     * @param pHeight the height 
     * @see javax.rad.ui.IDimension
     */
    public VaadinDimension(int pWidth, int pHeight)
    {
    	width = pWidth;
    	height = pHeight;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface Implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /**
     * {@inheritDoc}
     */
    public int getWidth()
    {
    	return width;
    }

    /**
     * {@inheritDoc}
     */
    public void setWidth(int pWidth)
    {
    	width = pWidth;
    }
    
    /**
     * {@inheritDoc}
     */
    public int getHeight()
    {
    	return height;
    }

    /**
     * {@inheritDoc}
     */
    public void setHeight(int pHeight)
    {
    	height = pHeight;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return super.toString() + " (" + width + ", " + height + ")"; 
    }
    
}	// VaadinDimension
