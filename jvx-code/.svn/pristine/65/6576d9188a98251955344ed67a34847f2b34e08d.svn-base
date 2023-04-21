/*
 * Copyright 2018 SIB Visions GmbH
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
 * 06.07.2018 - [JR] - creation
 */
package com.sibvisions.rad.ui.swing.ext;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.border.Border;

/**
 * The <code>WrappedInsetsBorder</code> wrapps another {@link Border} and supports changing the insets and supports
 * painting the border with custom insets instead of the border insets.
 * 
 * @author René Jahn
 */
public class WrappedInsetsBorder implements Border 
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** the default insets. */
	private static final Insets INSETS_DEFAULT = new Insets(3, 3, 3, 3);
	
	/** the wrapped border. */
	private Border border;
	
	/** the border insets. */
	private Insets insets = INSETS_DEFAULT;
	
	/** the paint insets. */
	private Insets insetsPaint = insets;
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of <code>WrappedInsetsBorder</code>.
	 * 
	 * @param pBorder the wrapped border
	 */
	public WrappedInsetsBorder(Border pBorder)
	{
		this(pBorder, null);
	}
	
	/**
	 * Creates a new instance of <code>WrappedInsetsBorder</code>.
	 * 
	 * @param pBorder the wrapped border
	 * @param pInsets the border insets
	 */
	public WrappedInsetsBorder(Border pBorder, int pInsets)
	{
		this(pBorder, new Insets(pInsets, pInsets, pInsets, pInsets));
	}
	
	/**
	 * Creates a new instance of <code>WrappedInsetsBorder</code>.
	 * 
	 * @param pBorder the wrapped border
	 * @param pInsets the border insets
	 */
	public WrappedInsetsBorder(Border pBorder, Insets pInsets)
	{
		border = pBorder;
		
		if (pInsets != null)
		{
			insets = pInsets;
			insetsPaint = insets;
		}
	}
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public void paintBorder(Component pComponent, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight) 
	{
		if (border != null)
		{
			border.paintBorder(pComponent, pGraphics, 
					           pX - insetsPaint.left, 
					           pY - insetsPaint.top, 
					           pWidth + insetsPaint.left + insetsPaint.right, 
					           pHeight + insetsPaint.top + insetsPaint.bottom);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Insets getBorderInsets(Component var1) 
	{
		return insets;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isBorderOpaque() 
	{
		if (border != null)
		{
			return border.isBorderOpaque();
		}
		else
		{
			return false;
		}
	}
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Sets the paint insets.
	 * 
	 * @param pInsets the insets
	 */
	public void setPaintInsets(Insets pInsets)
	{
		insetsPaint = pInsets;
	}
	
	/**
	 * Gets the paint insets.
	 * 
	 * @return the insets
	 */
	public Insets getPaintInsets()
	{
		return insetsPaint;
	}

}	// WrappedInsetsBorder
