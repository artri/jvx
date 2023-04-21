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
 * 09.03.2016 - [RZ] - creation
 */
package com.sibvisions.rad.ui.vaadin.ext.ui.client.panel.helper;

/**
 * Represents the size of a component.
 * 
 * @author Robert Zenz
 */
public class Size
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The height. */
	public int height;
	
	/** The width. */
	public int width;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link Size}.
	 */
	public Size()
	{
		this(0, 0);
	}
	
	/**
	 * Creates a new instance of {@link Size}.
	 *
	 * @param pWidth the width.
	 * @param pHeight the height.
	 */
	public Size(int pWidth, int pHeight)
	{
		width = pWidth;
		height = pHeight;
	}
	
	/**
	 * Creates a new instance of {@link Size}.
	 *
	 * @param pData the {@link String data}.
	 */
	public Size(String pData)
	{
		set(pData);
	}
	
	/**
	 * Gets the size, if data contains a size, else it gets null.
	 * 
	 * @param pData the data
	 * @return the size or null
	 */
	public static Size getSize(String pData)
	{
        if (pData != null)
        {
            int index = pData.indexOf('x');
            if (index >= 0)
            {
                return new Size(Integer.parseInt(pData.substring(0, index)), Integer.parseInt(pData.substring(index + 1)));
            }
        }
        
        return null;
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
		return toString(width, height);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Returns the {@link String} representation of the given width and height.
	 * 
	 * @param pWidth the width.
	 * @param pHeight the height.
	 * @return the string representation.
	 */
	public static String toString(int pWidth, int pHeight)
	{
		return Integer.toString(pWidth) + "x" + Integer.toString(pHeight);
	}
	
	/**
	 * Adds the given {@link Margins}.
	 *
	 * @param pMargins the {@link Margins margins}.
	 */
	public void add(Margins pMargins)
	{
		if (pMargins != null)
		{
			height = height + pMargins.getVertical();
			width = width + pMargins.getHorizontal();
		}
	}
	
	/**
	 * Gets the height.
	 *
	 * @return the height.
	 */
	public int getHeight()
	{
		return height;
	}
	
	/**
	 * Gets the width.
	 * 
	 * @return the width.
	 */
	public int getWidth()
	{
		return width;
	}
	
	/**
	 * Checks if this instance is zero.
	 *
	 * @return {@code true} if it is zero.
	 */
	public boolean isZero()
	{
		return width <= 0 && height <= 0;
	}
	
	/**
	 * Sets the width and height from the given {@link String data}. If the data
	 * is {@code null}, empty or cannot be parsed width and height will be set
	 * to zero.
	 * 
	 * @param pData the {@link String data}.
	 */
	public void set(String pData)
	{
		if (pData != null)
		{
		    int index = pData.indexOf('x');
		    if (index >= 0)
		    {
		        width = Integer.parseInt(pData.substring(0, index));
		        height = Integer.parseInt(pData.substring(index + 1));
		    }
		    else
		    {
	            width = 0;
	            height = 0;
		    }
		}
		else
		{
			width = 0;
			height = 0;
		}
	}
	
	/**
	 * Sets the height.
	 * 
	 * @param pHeight the height.
	 */
	public void setHeight(int pHeight)
	{
		height = pHeight;
	}
	
	/**
	 * Sets the width.
	 * 
	 * @param pWidth the width.
	 */
	public void setWidth(int pWidth)
	{
		width = pWidth;
	}
	
}	// Size
