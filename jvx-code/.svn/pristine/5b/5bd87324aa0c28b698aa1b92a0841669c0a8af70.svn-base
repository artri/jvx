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
 * 22.12.2008 - [HM] - creation
 * 31.05.2016 - [JR] - #1564: style property introduced
 */
package com.sibvisions.rad.ui.swing.ext.format;

import java.awt.Color;
import java.awt.Font;

import javax.rad.ui.Style;
import javax.swing.Icon;

/**
 * The <code>CellFormat</code> holds the format information for a cell.
 *  
 * @author Martin Handsteiner
 */
public class CellFormat
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the background. */
	private Color background;
	
	/** the foreground. */
	private Color foreground;
	
	/** the font. */
	private Font font;
	
	/** the font. */
	private Icon image;
	
	/** the cell style. */
    private Style style;

    /** the left indent. */
	private int leftIndent;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Constructs a new instance of <code>CellFormat</code>.
	 */
	public CellFormat()
	{
		background = null;
		foreground = null;
		font = null;
		image = null;
		leftIndent = 0;
		style = null;
	}
	
	/**
	 * Constructs a new instance of <code>CellFormat</code>.
	 * 
	 * @param pBackground the background of the Cell.
	 */
	public CellFormat(Color pBackground)
	{
		background = pBackground;
		foreground = null;
		font = null;
		image = null;
		leftIndent = 0;
		style = null;
	}
	
	/**
	 * Constructs a new instance of <code>CellFormat</code>.
	 * 
	 * @param pBackground the background of the Cell.
	 * @param pForeground the foreground of the Cell.
	 * @param pFont  the font of the Cell.
	 */
	public CellFormat(Color pBackground, Color pForeground, Font pFont)
	{
		background = pBackground;
		foreground = pForeground;
		font = pFont;
		image = null;
		leftIndent = 0;
		style = null;
	}
	
	/**
	 * Constructs a new instance of <code>CellFormat</code>.
	 * 
	 * @param pBackground the background of the Cell.
	 * @param pForeground the foreground of the Cell.
	 * @param pFont  the font of the Cell.
	 * @param pImage the image of the Cell.
	 */
	public CellFormat(Color pBackground, Color pForeground, Font pFont, Icon pImage)
	{
		background = pBackground;
		foreground = pForeground;
		font = pFont;
		image = pImage;
		leftIndent = 0;
		style = null;
	}
	
	/**
	 * Constructs a new instance of <code>CellFormat</code>.
	 * 
	 * @param pBackground the background of the Cell.
	 * @param pForeground the foreground of the Cell.
	 * @param pFont  the font of the Cell.
	 * @param pImage the image of the Cell.
	 * @param pLeftIndent the left indent.
	 */
	public CellFormat(Color pBackground, Color pForeground, Font pFont, Icon pImage, int pLeftIndent)
	{
		background = pBackground;
		foreground = pForeground;
		font = pFont;
		image = pImage;
		leftIndent = pLeftIndent;
		style = null;
	}
	
    /**
     * Constructs a new instance of <code>CellFormat</code>.
     * 
     * @param pBackground the background of the Cell.
     * @param pForeground the foreground of the Cell.
     * @param pFont  the font of the Cell.
     * @param pImage the image of the Cell.
     * @param pStyle the style.
     * @param pLeftIndent the left indent.
     */
    public CellFormat(Color pBackground, Color pForeground, Font pFont, Icon pImage, Style pStyle, int pLeftIndent)
    {
        background = pBackground;
        foreground = pForeground;
        font = pFont;
        image = pImage;
        leftIndent = pLeftIndent;
        style = pStyle;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the background.
	 * 
	 * @return the background.
	 */
	public Color getBackground()
	{
		return background;
	}
	
	/**
	 * Gets the foreground.
	 * 
	 * @return the foreground.
	 */
	public Color getForeground()
	{
		return foreground;
	}
	
	/**
	 * Gets the font.
	 * 
	 * @return the font.
	 */
	public Font getFont()
	{
		return font;
	}
	
	/**
	 * Gets the image.
	 * 
	 * @return the image.
	 */
	public Icon getImage()
	{
		return image;
	}
	
    /**
     * Gets the style.
     * 
     * @return the style
     */
    public Style getStyle()
    {
        return style;
    }
    
    /**
	 * Gets the left indent.
	 * 
	 * @return the indent.
	 */
	public int getLeftIndent()
	{
		return leftIndent;
	}
	
}	// CellFormat
