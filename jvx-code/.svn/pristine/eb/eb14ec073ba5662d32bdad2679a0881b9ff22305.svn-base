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

import java.awt.Point;

import javax.rad.ui.IPoint;

/**
 * A point representing a location in (x, y) coordinate space, specified
 * in integer precision.
 * 
 * @author Martin Handsteiner
 */
public class AwtPoint extends AwtResource<Point> 
                      implements IPoint
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates an instance of <code>AwtPoint</code> based on
	 * a <code>java.awt.Point</code>.
	 * 
	 * @param pPoint java.awt.Point
	 * @see java.awt.Point
	 */
	public AwtPoint(Point pPoint)
	{
		super(pPoint);
	}

	/**
	 * Creates an instance of <code>AwtPoint</code> at the specified
	 * (<i>x</i>,&nbsp;<i>y</i>) location in the coordinate space.
	 * 
	 * @param pX the <i>x</i> coordinate
	 * @param pY the <i>y</i> coordinate
	 */
	public AwtPoint(int pX, int pY)
	{
		super(new Point(pX, pY));
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

}	// AwtPoint
