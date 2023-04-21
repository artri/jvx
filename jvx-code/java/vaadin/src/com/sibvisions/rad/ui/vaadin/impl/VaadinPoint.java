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

import javax.rad.ui.IPoint;

/**
 * The <code>VaadinPoint</code> class is the vaadin implementation of {@link IPoint}.
 * 
 * @author Benedikt Cermak
 */
public class VaadinPoint extends VaadinResourceBase<IPoint>
                         implements IPoint
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class Members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
	/** the x. */
	private int x;
	/** the y. */
	private int y;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
	/**
	 * Creates a new instance of <code>VaadinPoint</code> with the given
	 * point.
	 * 
	 * @param pPoint the point
	 * @see IPoint
	 */
	public VaadinPoint(IPoint pPoint)
	{
		this(pPoint.getX(), pPoint.getY());
	}
	
    /**
     * Creates a new instance of <code>VaadinPoint</code> with the given x and y positions.
     *
     * @param pX the x value
     * @param pY the y value
     * @see IPoint
     */
    public VaadinPoint(int pX, int pY)
    {
    	x = pX;
    	y = pY;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public int getX()
	{
		return x;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setX(int pX)
	{
		x = pX;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getY()
	{
		return y;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setY(int pY)
	{
		y = pY;
	}
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return super.toString() + " (" + x + ", " + y + ")"; 
    }
    
}	// VaadinPoint
