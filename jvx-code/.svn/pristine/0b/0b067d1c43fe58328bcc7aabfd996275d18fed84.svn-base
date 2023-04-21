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

import java.awt.Rectangle;

import javax.rad.ui.IRectangle;

/**
 * An <code>AwtRectangle</code> specifies an area in a coordinate space that is 
 * enclosed by the <code>AwtRectangle</code> object's top-left point 
 * (<i>x</i>,&nbsp;<i>y</i>) in the coordinate space, its width, and its height.
 * 
 * @author Martin Handsteiner
 */
public class AwtRectangle extends AwtResource<Rectangle> 
                          implements IRectangle
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates an instance of <code>AwtRectangle</code> based on 
	 * a <code>java.awt.Rectangle</code>.
	 * 
	 * @param pRectangle java.awt.Rectangle
	 * @see java.awt.Rectangle
	 */
	public AwtRectangle(Rectangle pRectangle)
	{
		super(pRectangle);
	}

	/**
	 * Constructs a new <code>AwtRectangle</code> whose top-left corner is 
     * specified as (<code>x</code>,&nbsp;<code>y</code>) and whose width and height 
     * are specified by the arguments of the same name.
     * 
	 * @param pX the specified x coordinate
	 * @param pY the specified y coordinate
	 * @param pWidth the width of the <code>AwtRectangle</code>
	 * @param pHeight the height of the <code>AwtRectangle</code>
	 */
	public AwtRectangle(int pX, int pY, int pWidth, int pHeight)
	{
		super(new Rectangle(pX, pY, pWidth, pHeight));
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public int getX()
	{
		return resource.x;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setX(int pX)
	{
		resource.x = pX;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getY()
	{
		return resource.y;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setY(int pY)
	{
		resource.y = pY;
	}

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

}	// AwtRectangle
