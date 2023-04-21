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
 * The {@link Rectangle} consists of a {@link Point position} and {@link Size
 * size}.
 * 
 * @author Robert Zenz
 */
public class Rectangle
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /** The x position. */
    public int x;
    
    /** The y position. */
    public int y;
    
    /** The height. */
    public int height;
    
    /** The width. */
    public int width;
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link Rectangle}.
	 */
	public Rectangle()
	{
		this(0, 0, 0, 0);
	}
	
	/**
	 * Creates a new instance of {@link Rectangle}.
	 *
	 * @param pPosition the {@link Point position}.
	 * @param pSize the {@link Size size}.
	 */
	public Rectangle(Point pPosition, Size pSize)
	{
	    if (pPosition != null)
	    {
	        x = pPosition.x;
	        y = pPosition.y;
	    }
        if (pSize != null)
        {
            width = pSize.width;
            height = pSize.height;
        }
	}
	
	/**
	 * Creates a new instance of {@link Rectangle}.
	 *
	 * @param pX the x value.
	 * @param pY the y value.
	 * @param pWidth the width.
	 * @param pHeight the height.
	 */
	public Rectangle(int pX, int pY, int pWidth, int pHeight)
	{
		x = pX;
		y = pY;
		width = pWidth;
		height = pHeight;
	}
	
	/**
	 * Creates a new instance of {@link Rectangle}.
	 *
	 * @param pData the {@link String data}.
	 */
	public Rectangle(String pData)
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
		return getPosition().toString() + "/" + getSize().toString();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the {@link Point position}.
	 *
	 * @return the {@link Point position}.
	 */
	public Point getPosition()
	{
		return new Point(x, y);
	}
	
	/**
	 * Gets the {@link Size size}.
	 *
	 * @return the {@link Size size}.
	 */
	public Size getSize()
	{
		return new Size(width, height);
	}
	
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
    
	/**
	 * Sets the values from the given {@link String data}. If the data is
	 * {@code null}, empty or cannot be parsed the values will be set to zero.
	 * 
	 * @param pData the {@link String data}.
	 */
	public void set(String pData)
	{
		if (pData != null)
		{
			int separatorIndex = pData.indexOf("/");
			
			if (separatorIndex >= 0)
			{
			    String position = pData.substring(0, separatorIndex);
			    int yStartIndex = position.indexOf("+", 1);
			    if (yStartIndex < 0)
			    {
			        yStartIndex = position.indexOf("-", 1);
			    }
			    if (yStartIndex >= 0)
			    {
			        x = Integer.parseInt(position.substring(0, yStartIndex));
			        y = Integer.parseInt(position.substring(yStartIndex));
			    }
			    else
			    {
			        x = 0;
			        y = 0;
			    }
			    String size = pData.substring(separatorIndex + 1);
			    int index = size.indexOf('x');
			    if (index >= 0)
			    {
			        width = Integer.parseInt(size.substring(0, index));
			        height = Integer.parseInt(size.substring(index + 1));
			    }
			    else
			    {
			        width = 0;
			        height = 0;
			    }
	        }
	        else
	        {
	            x = 0;
	            y = 0;
	            width = 0;
	            height = 0;
	        }
		}
		else
		{
            x = 0;
            y = 0;
            width = 0;
            height = 0;
		}
	}
	
}	// Rectangle
