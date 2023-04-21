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

import java.awt.Font;

import javax.rad.ui.IFont;

/**
 * The <code>AwtFont</code> class represents fonts, which are used to
 * render text in a visible way.
 * 
 * @author Martin Handsteiner
 */
public class AwtFont extends AwtResource<Font> 
                     implements IFont 
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 *  Creates an instance of <code>AwtFont</code> based on 
	 *  a <code>java.awt.Font</code>.
	 *  
	 *  @param pFont java.awt.Font
	 *  @see java.awt.Font
	 */
	public AwtFont(Font pFont) 
	{
		super(pFont);
	}
	
	/**
	 * Creates an instance of <code>AwtFont</code> from the specified name, 
	 * style and point size.
     * 
	 * @param pName name the font name.  This can be a font face name or a font
     *        family name, and may represent either a logical font or a physical
     *        font found in this <code>GraphicsEnvironment</code>.
     *        The family names for logical fonts are: Dialog, DialogInput,
     *        Monospaced, Serif, or SansSerif. If <code>name</code> is
     *        <code>null</code>, the <em>logical font name</em> of the new
     *        <code>AwtFont</code> as returned by <code>getName()</code>is set to
     *        the name "Default".
	 * @param pStyle the style constant for the <code>AwtFont</code>
     *        The style argument is an integer bitmask that may
     *        be PLAIN, or a bitwise union of BOLD and/or ITALIC
     *        (for example, ITALIC or BOLD|ITALIC).
     *        If the style argument does not conform to one of the expected
     *        integer bitmasks then the style is set to PLAIN.
	 * @param pSize the point size of the <code>AwtFont</code>
	 */
	public AwtFont(String pName, int pStyle, int pSize) 
	{
		super(new Font(pName, pStyle, pSize));
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public String getFamily() 
	{
		return resource.getFamily();
	}

	/**
	 * {@inheritDoc}
	 */
	public String getFontName() 
	{
		return resource.getFontName();
	}

	/**
	 * {@inheritDoc}
	 */
	public String getName() 
	{
		return resource.getName();
	}

	/**
	 * {@inheritDoc}
	 */
	public int getSize() 
	{
		return resource.getSize();
	}

	/**
	 * {@inheritDoc}
	 */
	public int getStyle() 
	{
		return resource.getStyle();
	}

}	// AwtFont
