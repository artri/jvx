/*
 * Copyright 2012 SIB Visions GmbH
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
 * 03.10.2012 - [CB] - creation
 */
package com.sibvisions.rad.ui.vaadin.impl;

import javax.rad.ui.IInsets;

/**
 * The <code>VaadinInsets</code> class is the vaadin implementation of {@link IInsets}.
 * 
 * @author Benedikt Cermak
 */
public class VaadinInsets extends VaadinResourceBase<IInsets>
                          implements IInsets, Cloneable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class Members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
	/** the x. */
	private int top;
	/** the y. */
	private int left;
	/** the width. */
	private int bottom;
	/** the height. */
	private int right;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
	/**
	 * Creates a new instance of <code>VaadinInsets</code> based with the given 
	 * insets.
	 * 
	 * @param pInsets java.awt.Insets
	 * @see IInsets
	 */
    public VaadinInsets(IInsets pInsets) 
	{
    	super(pInsets);
	}
    
    /**
     * Creates a new instance of <code>VaadinInsets</code> with the given
     * point definition.
     *
     * @param pTop the top 
     * @param pLeft the left 
     * @param pBottom the bottom 
     * @param pRight the right 
     * @see IInsets
     */
    public VaadinInsets(int pTop, int pLeft, int pBottom, int pRight)
	{
    	top = pTop;
    	left = pLeft;
    	bottom = pBottom;
    	right = pRight;
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    public int getTop()
    {
    	return top;
    }

    /**
     * {@inheritDoc}
     */
    public void setTop(int pTop)
    {
    	top = pTop;
    }
    
    /**
     * {@inheritDoc}
     */
    public int getLeft()
    {
    	return left;
    }

    /**
     * {@inheritDoc}
     */
    public void setLeft(int pLeft)
    {
    	left = pLeft;
    }
    
    /**
     * {@inheritDoc}
     */
    public int getBottom()
    {
    	return bottom;
    }

    /**
     * {@inheritDoc}
     */
    public void setBottom(int pBottom)
    {
    	bottom = pBottom;
    }
    
    /**
     * {@inheritDoc}
     */
    public int getRight()
    {
    	return right;
    }

    /**
     * {@inheritDoc}
     */
    public void setRight(int pRight)
    {
    	right = pRight;
    }

    /**
     * {@inheritDoc}
     */
    public Object clone() 
    { 
    	return new VaadinInsets(this);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return super.toString() + " (" + top + ", " + left + ", " + bottom + ", " + right + ")"; 
    }
    
}	// VaadinInsets
