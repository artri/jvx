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

import javax.rad.ui.IDimension;

import com.sibvisions.util.type.StringUtil;

/**
 * Web server implementation of {@link IDimension}.
 * 
 * @author Martin Handsteiner
 */
public class WebDimension extends WebResource 
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
     * Creates a new instance of <code>WebDimension</code>.
     *
     * @see IDimension
     */
    public WebDimension()
    {
    }

    /**
     * Creates a new instance of <code>WebDimension</code> with the given with and height.
     *
     * @param pWidth the width 
     * @param pHeight the height 
     * @see IDimension
     */
    public WebDimension(int pWidth, int pHeight)
    {
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
    	return width + "," + height; 
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

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof WebDimension)
        {
            WebDimension d = (WebDimension)obj;
            
            return width == d.width && height == d.height;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return 11 + (width * 19) ^ (height * 41);
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void setAsString(String pValue)
    {
    	int[] values = StringUtil.parseInteger(pValue, ",");
    	
    	if (values != null && values.length >= 2)
    	{
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
        return super.toString() + " (" + width + ", " + height + ")"; 
    }
    
}	// WebDimension
