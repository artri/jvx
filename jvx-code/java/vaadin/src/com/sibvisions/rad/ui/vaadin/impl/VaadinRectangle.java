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

import javax.rad.ui.IRectangle;

/**
 * The <code>VaadinRectangle</code> class is the the vaadin implementation of {@link IRectangle}.
 * 
 * @author Benedikt Cermak
 */
public class VaadinRectangle extends VaadinResourceBase<IRectangle>
                             implements IRectangle
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class Members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
	/** the x. */
	private int x;
	/** the y. */
	private int y;
	/** the width. */
	private int width;
	/** the height. */
	private int height;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
	/**
	 * Creates an instance of <code>VaadinRectangle</code> based on 
	 * a <code>java.awt.Rectangle</code>.
	 * 
	 * @param pRectangle the rectangle
	 * @see javax.rad.ui.IRectangle
	 */
    public VaadinRectangle(IRectangle pRectangle)
	{
    	this(pRectangle.getX(), pRectangle.getY(), pRectangle.getWidth(), pRectangle.getHeight());
	}

    /**
     * Creates a new instance of <code>VaadinRectangle</code> with the given x, y, width and height.
     *
     * @param pX the x value
     * @param pY the y value
     * @param pWidth the width 
     * @param pHeight the height 
     * @see javax.rad.ui.IRectangle
     */
    public VaadinRectangle(int pX, int pY, int pWidth, int pHeight)
	{
    	x = pX;
    	y = pY;
    	width = pWidth;
    	height = pHeight;
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
        return super.toString() + " (" + x + ", " + y + ", " + width + ", " + height + ")"; 
    }
    
}	// VaadinRectangle
