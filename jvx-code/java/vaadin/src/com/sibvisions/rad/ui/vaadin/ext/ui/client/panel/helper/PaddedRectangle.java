/*
 * Copyright 2016 SIB Visions GmbH
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
 * 12.05.2016 - [RZ] - creation
 */
package com.sibvisions.rad.ui.vaadin.ext.ui.client.panel.helper;

/**
 * The {@link PaddedRectangle} is a {@link Rectangle} extension which adds
 * {@link Margins}.
 * 
 * @author Robert Zenz
 */
public class PaddedRectangle extends Rectangle
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The {@link Margins}. */
	public Margins padding;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link PaddedRectangle}.
	 */
	public PaddedRectangle()
	{
		super();
		
		padding = new Margins();
	}
	
	/**
	 * Creates a new instance of {@link PaddedRectangle}.
	 *
	 * @param pX the x value.
	 * @param pY the y value.
	 * @param pWidth the width.
	 * @param pHeight the height.
	 */
	public PaddedRectangle(int pX, int pY, int pWidth, int pHeight)
	{
		super(pX, pY, pWidth, pHeight);
		
		padding = new Margins();
	}
	
	/**
	 * Creates a new instance of {@link PaddedRectangle}.
	 *
	 * @param pPosition the {@link Point position}.
	 * @param pSize the {@link Size size}.
	 * @param pPadding the {@link Margins padding}.
	 */
	public PaddedRectangle(Point pPosition, Size pSize, Margins pPadding)
	{
		super(pPosition, pSize);
		
		padding = pPadding;
	}
	
	/**
	 * Creates a new instance of {@link PaddedRectangle}.
	 *
	 * @param pData the {@link String data}.
	 */
	public PaddedRectangle(String pData)
	{
	    this();
	    
		set(pData);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return super.toString() + "&" + padding.toString();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void set(String pData)
	{
		if (pData != null)
		{
		    int index = pData.indexOf('&');
		    if (index >= 0)
		    {
		        super.set(pData.substring(0, index));
		        
		        padding.set(pData.substring(index + 1));
		    }
		    else
		    {
	            super.set(null);
	            
	            padding.bottom = 0;
	            padding.left = 0;
	            padding.right = 0;
	            padding.top = 0;
		    }
		}
		else
		{
		    super.set(null);
			
            padding.bottom = 0;
            padding.left = 0;
            padding.right = 0;
            padding.top = 0;
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the {@link Margins padding}.
	 *
	 * @return the {@link Margins padding}.
	 */
	public Margins getPadding()
	{
		return padding;
	}
	
}	// PaddedRectangle
