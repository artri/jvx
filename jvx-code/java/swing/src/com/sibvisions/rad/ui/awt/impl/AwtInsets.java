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
 * 07.10.2008 - [HM] - creation
 */
package com.sibvisions.rad.ui.awt.impl;

import java.awt.Insets;

import javax.rad.ui.IInsets;

/**
 * The <code>AwtInsets</code> class encapsulates the margins (in integer precision) in a single object.
 * 
 * @author Martin Handsteiner
 */
public class AwtInsets extends AwtResource<Insets> 
                       implements IInsets
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates an instance of <code>AwtInsets</code> based on 
	 * a <code>java.awt.Insets</code>.
	 * 
	 * @param pInsets java.awt.Insets
	 * @see java.awt.Insets
	 */
    public AwtInsets(Insets pInsets) 
	{
    	super(pInsets);
	}

    /**
     * Creates an instance of <code>AwtInsets</code> for specific margins.
     * 
     * @param pTop the top margin
     * @param pLeft the left margin
     * @param pBottom the bottom margin
     * @param pRight the right margin
     * @see Insets#Insets(int, int, int, int)
     */
    public AwtInsets(int pTop, int pLeft, int pBottom, int pRight)
    {
    	super(new Insets(pTop, pLeft, pBottom, pRight));
    }

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * {@inheritDoc}
     */
    public int getTop()
    {
    	return resource.top;
    }

    /**
     * {@inheritDoc}
     */
    public void setTop(int pTop)
    {
    	resource.top = pTop;
    }
    
    /**
     * {@inheritDoc}
     */
    public int getLeft()
    {
    	return resource.left;
    }

    /**
     * {@inheritDoc}
     */
    public void setLeft(int pLeft)
    {
    	resource.left = pLeft;
    }
    
    /**
     * {@inheritDoc}
     */
    public int getBottom()
    {
    	return resource.bottom;
    }

    /**
     * {@inheritDoc}
     */
    public void setBottom(int pBottom)
    {
    	resource.bottom = pBottom;
    }
    
    /**
     * {@inheritDoc}
     */
    public int getRight()
    {
    	return resource.right;
    }

    /**
     * {@inheritDoc}
     */
    public void setRight(int pRight)
    {
    	resource.right = pRight;
    }
    
}	// AwtInsets
