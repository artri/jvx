/*
 * Copyright 2013 SIB Visions GmbH
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
 * 23.01.2013 - [SW] - creation
 * 31.05.2016 - [JR] - style property support
 */
package com.sibvisions.rad.ui.vaadin.impl.control;

import java.util.HashMap;
import java.util.Map;

import javax.rad.ui.IColor;
import javax.rad.ui.IFont;
import javax.rad.ui.IImage;
import javax.rad.ui.Style;
import javax.rad.ui.control.ICellFormat;

/**
 * The <code>VaadinCellFormat</code> class is the vaadin implementation of {@link ICellFormat}.
 * 
 * @author Stefan Wurm
 */
public class VaadinCellFormat implements ICellFormat
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the background. */
	private IColor background = null;
	
	/** the foreground. */
	private IColor foreground = null;
	
	/** the font. */
	private IFont font = null;
	
	/** the image. */
	private IImage image = null;
	
	/** the style. */
	private Style style;
	
    /** the additional style properties. */
    private Map<String, String> additionalStyles = null;	
	
	/** the left indent. */
	private int leftIndent;
		
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>VaadinCellFormat</code>.
	 * 
	 * @param pBackground the background of the Cell.
	 * @param pForeground the foreground of the Cell.
	 * @param pFont  the font of the Cell.
	 * @param pImage the image of the Cell.
	 * @param pStyle the style definition of the Cell.
	 * @param pLeftIndent the left indent.
	 */
	public VaadinCellFormat(IColor pBackground, IColor pForeground, IFont pFont, IImage pImage, Style pStyle, int pLeftIndent)
	{
		background = pBackground;
		foreground = pForeground;
		font = pFont;
		image = pImage;
		leftIndent = pLeftIndent;
		style = pStyle;
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

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /**
     * {@inheritDoc}
     */
	@Override
	public Object getResource()
	{
		return this;
	}

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Gets the additional style properties.
     * 
     * @return the additional style properties.
     */
    public Map<String, String> getAdditionalStyles()
    {
        return additionalStyles;
    }

    /**
     * Sets the additional style properties.
     * 
     * @param pAdditionalStyles the additional style properties.
     * @see #addAdditionalStyle(String, String)
     */
    public void setAdditionalStyles(Map<String, String> pAdditionalStyles)
    {
        additionalStyles = pAdditionalStyles;
    }
    
    /**
     * Adds an additional style property.
     * 
     * @param pName the style name in camel case format
     * @param pValue the style value
     */
    public void addAdditionalStyle(String pName, String pValue)
    {
        if (additionalStyles == null)
        {
            additionalStyles = new HashMap<String, String>();
        }
        
        additionalStyles.put(pName, pValue);
    }	
	
} 	// VaadinCellFormat
