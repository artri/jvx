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
 * 09.03.2016 - [RZ] - creation
 */
package com.sibvisions.rad.ui.vaadin.ext.ui.client.panel.helper;

/**
 * Holds the margins.
 * 
 * @author Robert Zenz
 */
public class Margins
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The bottom value. */
	public int bottom;
	
	/** The left value. */
	public int left;
	
	/** The right value. */
	public int right;
	
	/** The top value. */
	public int top;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link Margins}.
	 */
	public Margins()
	{
		this(0, 0, 0, 0);
	}
	
	/**
	 * Creates a new instance of {@link Margins}.
	 *
	 * @param pTop the top value.
	 * @param pLeft the left value.
	 * @param pBottom the bottom value.
	 * @param pRight the right value.
	 */
	public Margins(int pTop, int pLeft, int pBottom, int pRight)
	{
		super();
		
		top = pTop;
		left = pLeft;
		bottom = pBottom;
		right = pRight;
	}
	
	/**
	 * Creates a new instance of {@link Margins}.
	 *
	 * @param pData the {@link String data}.
	 */
	public Margins(String pData)
	{
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
		return Integer.toString(top) + ";"
				+ Integer.toString(left) + ";"
				+ Integer.toString(bottom) + ";"
				+ Integer.toString(right) + ";";
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the bottom value.
	 * 
	 * @return the bottom value.
	 */
	public int getBottom()
	{
		return bottom;
	}
	
	/**
	 * Gets the horizontal value (the sum of left and right).
	 * 
	 * @return the horizontal value.
	 */
	public int getHorizontal()
	{
		return left + right;
	}
	
	/**
	 * Gets the left value.
	 * 
	 * @return the left value.
	 */
	public int getLeft()
	{
		return left;
	}
	
	/**
	 * Gets the right value.
	 * 
	 * @return the right value.
	 */
	public int getRight()
	{
		return right;
	}
	
	/**
	 * Gets the top value.
	 * 
	 * @return the top value.
	 */
	public int getTop()
	{
		return top;
	}
	
	/**
	 * Gets the vertical value (the sum of top and bottom).
	 * 
	 * @return the vertical value.
	 */
	public int getVertical()
	{
		return top + bottom;
	}
	
	/**
	 * Sets the values from the given {@link String data}. If the data is
	 * {@code null}, empty or cannot be parsed the values will be set to zero.
	 * 
	 * @param pData the {@link String data}.
	 */
	public void set(String pData)
	{
		if (pData != null && pData.length() > 0)
		{
			String[] splittedData = pData.split(";");
			
			top = Integer.parseInt(splittedData[0]);
			left = Integer.parseInt(splittedData[1]);
			bottom = Integer.parseInt(splittedData[2]);
			right = Integer.parseInt(splittedData[3]);
		}
		else
		{
			top = 0;
			left = 0;
			right = 0;
			top = 0;
		}
	}
	
	/**
	 * Sets the bottom value.
	 * 
	 * @param pBottom the bottom value.
	 */
	public void setBottom(int pBottom)
	{
		bottom = pBottom;
	}
	
	/**
	 * Sets the left value.
	 * 
	 * @param pLeft the left value.
	 */
	public void setLeft(int pLeft)
	{
		left = pLeft;
	}
	
	/**
	 * Sets the right value.
	 * 
	 * @param pRight the right value.
	 */
	public void setRight(int pRight)
	{
		right = pRight;
	}
	
	/**
	 * Sets the top value.
	 * 
	 * @param pTop the top value.
	 */
	public void setTop(int pTop)
	{
		top = pTop;
	}
	
}	// Margins
