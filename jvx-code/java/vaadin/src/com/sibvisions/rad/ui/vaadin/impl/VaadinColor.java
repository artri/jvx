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
 * 20.09.2012 - [CB] - creation
 */
package com.sibvisions.rad.ui.vaadin.impl;

import java.util.Hashtable;

import javax.rad.ui.IColor;

/**
 * The <code>VaadinColor</code> class is the vaadin implementation of {@link IColor}.
 * 
 * @author Benedikt Cermak
 */
public class VaadinColor extends VaadinResourceBase<IColor> 
						 implements IColor
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class Members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** List of available system colors. */
	private static Hashtable<String, VaadinColor> systemColors = new Hashtable<String, VaadinColor>();
	
	/** the red value. **/
	private int red;
	
	/** the green value. **/
	private int green;
	
	/** the blue value. **/
	private int blue;
	
	/** the alpha value. **/
	private int alpha;
	
	/** the red, green, blue and alpha value. **/
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
    public VaadinColor(int pRGBA)
	{
        rgba  = pRGBA;
        
        alpha = (pRGBA >> 24) & 0xFF;
        red   = (pRGBA >> 16) & 0xFF;
        green = (pRGBA >> 8) & 0xFF;
        blue  = pRGBA & 0xFF;
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
    public VaadinColor(int pR, int pG, int pB)
    {
    	red   = pR;
    	green = pG;
    	blue  = pB;
    	alpha = 0xff;
    	
    	rgba  = (0xFF << 24)
                | ((pR & 0xFF) << 16)
                | ((pG & 0xFF) << 8)
                | ((pB & 0xFF) << 0);
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
    public VaadinColor(int pR, int pG, int pB, int pA)
	{
    	red   = pR;
    	green = pG;
    	blue  = pB;
    	alpha = pA;
    	
        rgba  = ((pA & 0xFF) << 24)
                | ((pR & 0xFF) << 16)
                | ((pG & 0xFF) << 8)
                | ((pB & 0xFF) << 0);
	}    

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface Implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public int getAlpha()
	{
		return alpha;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getBlue()
	{
		return blue;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getGreen()
	{
		return green;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getRed()
	{
		return red;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getRGBA()
	{
		return rgba;
	}
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Returns the value string for the css attribute. The alpha value will be recognized, if set.
	 * 
	 * @return the value string, e.g. rgb(128, 128, 128) or rgba(128, 128, 128, 0.5)
	 */
    public String getStyleValueRGB()
	{
        return getStyleValueRGB(this);
	}
    
    /**
     * Returns the value string  of the color for the css attribute. The alpha value will be recognized, if set.
     * 
     * @param pColor the {@link IColor}
     * @return the value string, e.g. rgb(128, 128, 128) or rgba(128, 128, 128, 0.5)
     */
    public static String getStyleValueRGB(IColor pColor)
    {
        if (pColor.getAlpha() == 0xFF)
        {
            return "rgb( " + pColor.getRed() + ", " + pColor.getGreen() + ", " + pColor.getBlue() + ")";
        }
        else
        {
            return "rgba( " + pColor.getRed() + ", " + pColor.getGreen() + ", " + pColor.getBlue() + ", " + (Math.round(pColor.getAlpha() / 255f * 100) / 100f) + ")";
        }
    }
    
    /**
	 * Gets system color.
	 * 
	 * @param pType the cystem color type
	 * @return color definition
	 */
	public static IColor getSystemColor(String pType)
	{
		return systemColors.get(pType);
	}

	/**
	 * Sets userdefined color as system color.
	 * 
	 * @param pType <code>SystemColor</code> type
	 * @param pSystemColor color definition
	 */
	public static void setSystemColor(String pType, VaadinColor pSystemColor)
	{
    	if (pSystemColor == null)
    	{
    		systemColors.remove(pType);
    	}
    	else
    	{
    		systemColors.put(pType, pSystemColor);
    	}
	}
	
}	// VaadinColor
