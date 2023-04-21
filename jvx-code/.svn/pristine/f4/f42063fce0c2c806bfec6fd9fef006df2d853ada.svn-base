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

import java.awt.Color;

import javax.rad.ui.IColor;

/**
 * The <code>AwtColor</code> class is used to encapsulate the access 
 * to all usable colors for AWT components. It's also used to change
 * or define new colors.
 * 
 * @author Martin Handsteiner
 */
public class AwtColor extends AwtResource<Color> 
                      implements IColor
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates an instance of <code>AwtColor</code> based on 
	 * a <code>java.awt.Color</code>.
	 * 
	 * @param pColor java.awt.Color
	 * @see java.awt.Color
	 */
	public AwtColor(Color pColor)
	{
		super(pColor);
	}

    /**
     * Creates a sRGB color with the specified combined RGBA value consisting
     * of the alpha component in bits 24-31, the red component in bits 16-23,
     * the green component in bits 8-15, and the blue component in bits 0-7.
     *     
     * @param pRGBA the combined RGBA components
     * @see java.awt.Color
     */
	public AwtColor(int pRGBA)
	{
		super(new Color(pRGBA, true));
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public int getAlpha()
	{
		return resource.getAlpha();
	}

	/**
	 * {@inheritDoc}
	 */
	public int getBlue()
	{
		return resource.getBlue();
	}

	/**
	 * {@inheritDoc}
	 */
	public int getGreen()
	{
		return resource.getGreen();
	}

	/**
	 * {@inheritDoc}
	 */
	public int getRed()
	{
		return resource.getRed();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getRGBA()
	{
		return resource.getRGB();
	}
	
}	// AwtColor
