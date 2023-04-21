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
 * 25.01.2009 - [HM] - creation
 * 31.05.2016 - [JR] - #1564: style property support
 */
package com.sibvisions.rad.ui.swing.impl.control;

import java.awt.Color;
import java.awt.Font;

import javax.rad.ui.IColor;
import javax.rad.ui.IFont;
import javax.rad.ui.IImage;
import javax.rad.ui.Style;
import javax.rad.ui.control.ICellFormat;
import javax.swing.ImageIcon;

import com.sibvisions.rad.ui.awt.impl.AwtResource;
import com.sibvisions.rad.ui.swing.ext.format.CellFormat;

/**
 * The <code>SwingCellFormat</code> is the <code>ICellFormat</code>
 * implementation for swing.
 * 
 * @author Martin Handsteiner
 */
public class SwingCellFormat extends AwtResource<CellFormat> 
                             implements ICellFormat
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the background. */
	private IColor background;
	
	/** the foreground. */
	private IColor foreground;
	
	/** the font. */
	private IFont font;
	
	/** the image. */
	private IImage image;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Constructs a new instance of <code>SwingCellFormat</code>.
	 * 
	 * @param pBackground the background of the Cell.
	 * @param pForeground the foreground of the Cell.
	 * @param pFont  the font of the Cell.
	 * @param pImage the image of the Cell.
	 * @param pStyle the style definition of the Cell.
	 * @param pLeftIndent the left indent.
	 */
	public SwingCellFormat(IColor pBackground, IColor pForeground, IFont pFont, IImage pImage, Style pStyle, int pLeftIndent)
	{
		super(new CellFormat((pBackground == null) ? null : (Color)pBackground.getResource(),
				             (pForeground == null) ? null : (Color)pForeground.getResource(),
						     (pFont == null)       ? null : (Font)pFont.getResource(),
						     (pImage == null)      ? null : (ImageIcon)pImage.getResource(),
						     pStyle,
						     pLeftIndent));
		background = pBackground;
		foreground = pForeground;
		font = pFont;
		image = pImage;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public IColor getBackground()
	{
		return background;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public IColor getForeground()
	{
		return foreground;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public IFont getFont()
	{
		return font;
	}
		
	/**
	 * {@inheritDoc}
	 */
	public IImage getImage()
	{
		return image;
	}
		
    /**
     * {@inheritDoc}
     */
    public Style getStyle()
    {
        return resource.getStyle();
    }
    
    /**
	 * {@inheritDoc}
	 */
	public int getLeftIndent()
	{
		return resource.getLeftIndent();
	}
		
}	// SwingCellFormat
