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
 * 20.11.2009 - [HM] - creation
 */
package com.sibvisions.rad.ui.web.impl.control;

import javax.rad.ui.IColor;
import javax.rad.ui.IFont;
import javax.rad.ui.IImage;
import javax.rad.ui.Style;
import javax.rad.ui.control.ICellFormat;

import com.sibvisions.rad.ui.web.impl.WebColor;
import com.sibvisions.rad.ui.web.impl.WebFont;
import com.sibvisions.rad.ui.web.impl.WebImage;
import com.sibvisions.rad.ui.web.impl.WebResource;

/**
 * Web server implementation of {@link ICellFormat}.
 * 
 * @author Martin Handsteiner
 */
public class WebCellFormat extends WebResource
                           implements ICellFormat
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the background. */
	private WebColor background = null;
	/** the foreground. */
	private WebColor foreground = null;
	/** the font. */
	private WebFont font = null;
	/** the image. */
	private WebImage image = null;
	/** the style. */
	private Style style = null;
	/** the left indent. */
	private int leftIndent;
	
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
	 * @param pStyle the style of the Cell.
	 * @param pLeftIndent the left indent.
	 */
	public WebCellFormat(IColor pBackground, IColor pForeground, IFont pFont, IImage pImage, Style pStyle, int pLeftIndent)
	{
		background = (pBackground == null) ? null : (WebColor)pBackground.getResource();
		foreground = (pForeground == null) ? null : (WebColor)pForeground.getResource();
		font = (pFont == null) ? null : (WebFont)pFont.getResource();
		image = (pImage == null) ? null : (WebImage)pImage.getResource();
		leftIndent = pLeftIndent;
		style = pStyle;
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
    	return getNullableAsString(background) + ";" 
    		+ getNullableAsString(foreground) + ";" 
    		+ getNullableAsString(font) + ";"
    		+ getNullableAsString(image); 
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
	    return style;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getLeftIndent()
	{
		return leftIndent;
	}
		
}	// WebCellFormat
