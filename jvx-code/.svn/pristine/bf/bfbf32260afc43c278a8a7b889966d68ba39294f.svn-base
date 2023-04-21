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
 * 11.02.2011 - [JR] - added additional constructors
 */
package com.sibvisions.rad.ui.web.impl;

import java.util.Hashtable;

import javax.rad.ui.IColor;

/**
 * Web server implementation of {@link IColor}.
 * 
 * @author Martin Handsteiner
 */
public class WebColor extends WebResource 
                      implements IColor
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class Members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
	/** List of available system colors. */
	private static Hashtable<String, WebColor> systemColors = new Hashtable<String, WebColor>();

	/** the optional internal name. */
	private String sInternalName;
	
	/** the red, green, blue and alpha value. */
	private int rgba;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates an opaque sRGB color with the specified combined RGB value
     * consisting of the red component in bits 16-23, the green component
     * in bits 8-15, and the blue component in bits 0-7.  The actual color
     * used in rendering depends on finding the best match given the
     * color space available for a particular output device.  Alpha is
     * defaulted to 255.
     *
     * @param pRGBA the combined RGB components
     * @see IColor
     * @see IColor#getRed
     * @see IColor#getGreen
     * @see IColor#getBlue
     * @see IColor#getRGBA
     */
    protected WebColor(int pRGBA)
	{
    	rgba = pRGBA;
	}
    
    /**
     * Creates an opaque sRGB color with the specified red, green, 
     * and blue values in the range (0 - 255). The actual color 
     * used in rendering depends on finding the best match given 
     * the color space available for a given output device.  
     * Alpha is defaulted to 255.
     *
     * @param pR the red component
     * @param pG the green component
     * @param pB the blue component
     * @see IColor
     * @see IColor#getRed
     * @see IColor#getGreen
     * @see IColor#getBlue
     * @see IColor#getRGBA
     */
    public WebColor(int pR, int pG, int pB)
    {
    	this(0xFF000000 | ((pR & 0xFF) << 16) | ((pG & 0xFF) << 8) | (pB & 0xFF));    	
    }

    /**
     * Creates an sRGB color with the specified red, green, blue, and alpha
     * values in the range (0 - 255).
     *
     * @param pR the red component
     * @param pG the green component
     * @param pB the blue component
     * @param pA the alpha component
     * @see IColor
     * @see IColor#getRed
     * @see IColor#getGreen
     * @see IColor#getBlue
     * @see IColor#getAlpha
     * @see IColor#getRGBA
     */
    public WebColor(int pR, int pG, int pB, int pA)
	{
		this(((pA & 0xFF) << 24) | ((pR & 0xFF) << 16) | ((pG & 0xFF) << 8) | (pB & 0xFF));
	}    
    
    /**
     * Creates a new instance of <code>WebColor</code> with given value and an internal name.
     * The internal name could used as style name.
     * 
     * @param pInternalName the internal color name
     * @param pRGBA the combined RGB components
     * @see WebColor#WebColor(int)
     */
    private WebColor(String pInternalName, int pRGBA)
    {
        this(pRGBA);
        
        sInternalName = pInternalName;
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
    	return getAsString(this);
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface Implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public int getRed()
    {
		return (rgba >> 16) & 0xFF;
    }

	/**
	 * {@inheritDoc}
	 */
	public int getGreen()
    {
		return (rgba >> 8) & 0xFF;
    }

	/**
	 * {@inheritDoc}
	 */
	public int getBlue()
    {
		return rgba & 0xFF;
    }

	/**
	 * {@inheritDoc}
	 */
	public int getAlpha()
    {
        return (rgba >> 24) & 0xff;
    }
	
	/**
	 * {@inheritDoc}
	 */
	public int getRGBA()
    {
    	return rgba;
    }
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void setAsString(String pValue)
    {
        if (pValue != null)
        {
            String[] parts = pValue.split(";");
            
            rgba = Integer.parseInt(parts[0].substring(1), 16) | 0xff000000;
            
            if (parts.length == 2)
            {
                sInternalName = parts[1];
            }
        }
    }
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Gets the string representation of given color.
     * 
     * @param pColor the color
     * @return the string representation e.g. #AABBCC or #AABBCC;colorname
     */
    public static String getAsString(IColor pColor)
    {
    	String sInternalName;
    	
    	int rgba;
    	int alpha;
    	
    	
    	if (pColor instanceof WebColor)
    	{
    		rgba = ((WebColor)pColor).rgba;
    		sInternalName = ((WebColor)pColor).sInternalName;
    	}
    	else
    	{
    		rgba = pColor.getRGBA();
    		sInternalName = null;
    	}
    	
		alpha = pColor.getAlpha();
    	
    	String sValue = "#" + Integer.toString(0x1000000 | (rgba & 0xffffff), 16).substring(1).toUpperCase();
    	
    	if (alpha != 255)
    	{
    		sValue += Long.toHexString(0x100L | (alpha & 0xffL)).substring(1).toUpperCase();
    	}
    	
    	if (sInternalName != null)
    	{
    	    sValue += ";" + sInternalName.toLowerCase();
    	}
    	
     	return sValue;
    }
    
    /**
	 * Gets system color.
	 * 
	 * @param pType <code>SystemColor</code> type
	 * @return color definition
	 */
	public static WebColor getSystemColor(String pType)
	{
		return systemColors.get(pType);
	}

	/**
	 * Sets userdefined color as system color.
	 * 
	 * @param pType <code>SystemColor</code> type
	 * @param pSystemColor color definition
	 * @see java.awt.SystemColor
	 */
	public static void setSystemColor(String pType, WebColor pSystemColor)
	{
	    if (pSystemColor != null)
	    {
	        systemColors.put(pType, new WebColor(pType.replace("IControl.", "").replace(".", "_"), pSystemColor.rgba));
	    }
	    else
	    {
	        systemColors.remove(pType);
	    }
	}
    
}	// WebColor
