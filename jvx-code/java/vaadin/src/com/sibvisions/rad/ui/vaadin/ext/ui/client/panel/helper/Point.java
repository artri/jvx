/*
 * Copyright 2016 SIB Visions GmbH
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
 * 15.03.2016 - [RZ] - creation
 */
package com.sibvisions.rad.ui.vaadin.ext.ui.client.panel.helper;

/**
 * The {@link Point} represents a point on the screen.
 * 
 * @author Robert Zenz
 */
public class Point
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The x position. */
	public int x;
	
	/** The y position. */
	public int y;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link Point}.
	 */
	public Point()
	{
		this(0, 0);
	}
	
	/**
	 * Creates a new instance of {@link Point}.
	 *
	 * @param pX the x position.
	 * @param pY the y position.
	 */
	public Point(int pX, int pY)
	{
		x = pX;
		y = pY;
	}
	
	/**
	 * Creates a new instance of {@link Point}.
	 *
	 * @param pData the {@link String data}.
	 */
	public Point(String pData)
	{
		set(pData);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		String xAsString = Integer.toString(x);
		if (x >= 0)
		{
			xAsString = "+" + xAsString;
		}
		
		String yAsString = Integer.toString(y);
		if (y >= 0)
		{
			yAsString = "+" + yAsString;
		}
		
		return xAsString + yAsString;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the x position.
	 * 
	 * @return the x position.
	 */
	public int getX()
	{
		return x;
	}
	
	/**
	 * Gets the y position.
	 * 
	 * @return the y position.
	 */
	public int getY()
	{
		return y;
	}
	
	/**
	 * Sets the x and y values from the given {@link String data}. If the data
	 * is {@code null}, empty or cannot be parsed x and y will be set to zero.
	 * 
	 * @param pData the {@link String data}.
	 */
	public void set(String pData)
	{
		if (pData != null && pData.length() > 0)
		{
			int yStartIndex = pData.indexOf("+", 1);
			
			if (yStartIndex < 0)
			{
				yStartIndex = pData.indexOf("-", 1);
			}
			
			if (yStartIndex >= 0)
			{
    			x = Integer.parseInt(pData.substring(0, yStartIndex));
    			y = Integer.parseInt(pData.substring(yStartIndex));
			}
			else
			{
	            x = 0;
	            y = 0;
			}
		}
		else
		{
			x = 0;
			y = 0;
		}
	}
	
	/**
	 * Sets the x position.
	 * 
	 * @param pX the x position.
	 */
	public void setX(int pX)
	{
		x = pX;
	}
	
	/**
	 * Sets the y position.
	 * 
	 * @param pY the y position.
	 */
	public void setY(int pY)
	{
		y = pY;
	}
	
}
