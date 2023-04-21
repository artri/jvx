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
 * 01.10.2008 - [HM] - creation
 */
package com.sibvisions.rad.ui.awt.impl;

import java.awt.Dimension;

import javax.rad.ui.IDimension;

/**
 * The <code>AwtDimension</code> class encapsulates the width and 
 * height of a component (in integer precision) in a single object.
 * 
 * @author Martin Handsteiner
 */
public class AwtDimension extends AwtResource<Dimension> 
                          implements IDimension
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates an instance of <code>AwtDimension</code> based on 
	 * a {@link Dimension}.
	 * 
	 * @param pDimension java.awt.Dimension
	 * @see java.awt.Dimension
	 */
    public AwtDimension(Dimension pDimension) 
	{
    	super(pDimension);
	}

    /**
     * Creates an instance of <code>AwtDimension</code> whose width  
     * and height are the same as for the specified dimension. 
     * 
     * @param pWidth the specified width
     * @param pHeight the specified height
     */
    public AwtDimension(int pWidth, int pHeight)
    {
    	super(new Dimension(pWidth, pHeight));
    }

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * {@inheritDoc}
     */
    public int getWidth()
    {
    	return resource.width;
    }

    /**
     * {@inheritDoc}
     */
    public void setWidth(int pWidth)
    {
    	resource.width = pWidth;
    }
    
    /**
     * {@inheritDoc}
     */
    public int getHeight()
    {
    	return resource.height;
    }

    /**
     * {@inheritDoc}
     */
    public void setHeight(int pHeight)
    {
    	resource.height = pHeight;
    }
    
}	// AwtDimension
