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
 * 19.11.2009 - [HM] - creation
 */
package com.sibvisions.rad.ui.web.impl;

import javax.rad.ui.IRectangle;

import com.sibvisions.util.type.StringUtil;

/**
 * Web server implementation of {@link IRectangle}.
 * 
 * @author Martin Handsteiner
 */
public class WebRectangle extends WebResource
                          implements IRectangle
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class Members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
	/** the x position. */
	private int x;
	/** the y position. */
	private int y;
	/** the width. */
	private int width;
	/** the height. */
	private int height;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>WebRectangle</code>.
     *
     * @see IRectangle
     */
    public WebRectangle()
	{
	}

    /**
     * Creates a new instance of <code>WebRectangle</code> with the given x, y, width and height.
     *
     * @param pX the x value
     * @param pY the y value
     * @param pWidth the width 
     * @param pHeight the height 
     * @see IRectangle
     */
    public WebRectangle(int pX, int pY, int pWidth, int pHeight)
	{
    	x = pX;
    	y = pY;
    	width = pWidth;
    	height = pHeight;
	}
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
	 * {@inheritDoc}
	 */
    @Override
    public String getAsString()
    {
    	return x + "," + y + "," + width + "," + height; 
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

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof WebRectangle)
        {
            WebRectangle r = (WebRectangle)obj;
            
            return x == r.x && y == r.y && width == r.width && height == r.height;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return 13 + (x * 23) ^ (y * 37) ^ (width * 43) ^ (height * 47);
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void setAsString(String pValue)
    {
    	int[] values = StringUtil.parseInteger(pValue, ",");
    	
    	if (values != null && values.length >= 4)
    	{
        	x = values[0];
        	y = values[1];
        	width = values[2];
        	height = values[3];
    	}
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return super.toString() + " (" + x + ", " + y + ", " + width + ", " + height + ")"; 
    }
    
}	// WebRectangle
