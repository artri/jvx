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
 * 24.10.2012 - [JR] - #604: added constructor
 * 31.05.2016 - [JR] - #1564: style property support
 */
package javax.rad.genui.control;

import javax.rad.genui.UIFactoryManager;
import javax.rad.genui.UIResource;
import javax.rad.ui.IColor;
import javax.rad.ui.IFont;
import javax.rad.ui.IImage;
import javax.rad.ui.Style;
import javax.rad.ui.control.ICellFormat;

/**
 * Platform and technology independent CellFormat.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 */
public class UICellFormat extends UIResource<ICellFormat> 
                          implements ICellFormat
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Constructs a new instance of <code>UICellFormat</code>.
	 */
	public UICellFormat()
	{
		super(UIFactoryManager.getFactory().createCellFormat(null, null, null, null, null, 0));
	}

	/**
	 * Constructs a new instance of <code>UICellFormat</code> with the given
	 * cell format.
	 * 
	 * @param pCellFormat the cell format
	 */
	protected UICellFormat(ICellFormat pCellFormat)
	{
		super(pCellFormat);
	}
	
	/**
	 * Constructs a new instance of <code>UICellFormat</code>.
	 * 
	 * @param pBackground the background of the Cell.
	 */
	public UICellFormat(IColor pBackground)
	{
		super(UIFactoryManager.getFactory().createCellFormat(pBackground, null, null, null, null, 0));
	}
	
	/**
	 * Constructs a new instance of <code>UICellFormat</code>.
	 * 
	 * @param pBackground the background of the Cell.
	 * @param pForeground the foreground of the Cell.
	 */
	public UICellFormat(IColor pBackground, IColor pForeground)
	{
		super(UIFactoryManager.getFactory().createCellFormat(pBackground, pForeground, null, null, null, 0));
	}
	
	/**
	 * Constructs a new instance of <code>SwingCellFormat</code>.
	 * 
	 * @param pBackground the background of the Cell.
	 * @param pForeground the foreground of the Cell.
	 * @param pFont  the font of the Cell.
	 */
	public UICellFormat(IColor pBackground, IColor pForeground, IFont pFont)
	{
		super(UIFactoryManager.getFactory().createCellFormat(pBackground, pForeground, pFont, null, null, 0));
	}
	
	/**
	 * Constructs a new instance of <code>UICellFormat</code>.
	 * 
	 * @param pBackground the background of the Cell.
	 * @param pForeground the foreground of the Cell.
	 * @param pFont  the font of the Cell.
	 * @param pImage the image of the Cell.
	 */
	public UICellFormat(IColor pBackground, IColor pForeground, IFont pFont, IImage pImage)
	{
		super(UIFactoryManager.getFactory().createCellFormat(pBackground, pForeground, pFont, pImage, null, 0));
	}
	
	/**
	 * Constructs a new instance of <code>UICellFormat</code>.
	 * 
	 * @param pBackground the background of the Cell.
	 * @param pForeground the foreground of the Cell.
	 * @param pFont  the font of the Cell.
	 * @param pImage the image of the Cell.
	 * @param pLeftIndent the left indent.
	 */
	public UICellFormat(IColor pBackground, IColor pForeground, IFont pFont, IImage pImage, int pLeftIndent)
	{
		super(UIFactoryManager.getFactory().createCellFormat(pBackground, pForeground, pFont, pImage, null, pLeftIndent));
	}
	
    /**
     * Constructs a new instance of <code>UICellFormat</code>.
     * 
     * @param pBackground the background of the Cell.
     * @param pForeground the foreground of the Cell.
     * @param pFont  the font of the Cell.
     * @param pImage the image of the Cell.
     * @param pStyle the style definition of the Cell.
     * @param pLeftIndent the left indent.
     */
    public UICellFormat(IColor pBackground, IColor pForeground, IFont pFont, IImage pImage, Style pStyle, int pLeftIndent)
    {
        super(UIFactoryManager.getFactory().createCellFormat(pBackground, pForeground, pFont, pImage, pStyle, pLeftIndent));
    }

    /**
	 * Constructs a new instance of <code>UICellFormat</code>.
	 * 
	 * @param pImage the image of the Cell.
	 */
	public UICellFormat(IImage pImage)
	{
		super(UIFactoryManager.getFactory().createCellFormat(null, null, null, pImage, null, 0));
	}
	
    /**
	 * Constructs a new instance of <code>UICellFormat</code>.
	 * 
	 * @param pStyle the style of the Cell.
	 */
    public UICellFormat(Style pStyle)
    {
    	super(UIFactoryManager.getFactory().createCellFormat(null, null, null, null, pStyle, 0));
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public IColor getBackground()
	{
		return uiResource.getBackground();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public IColor getForeground()
	{
		return uiResource.getForeground();
	}

	/**
	 * {@inheritDoc}
	 */
	public IFont getFont()
	{
		return uiResource.getFont();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public IImage getImage()
	{
		return uiResource.getImage();
	}

    /**
     * {@inheritDoc}
     */
	public Style getStyle()
	{
	    return uiResource.getStyle();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getLeftIndent()
	{
		return uiResource.getLeftIndent();
	}
	
}	// UICellFormat
