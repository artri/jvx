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
 * 19.11.2009 - [HM] - creation
 */
package com.sibvisions.rad.ui.web.impl;

import java.awt.Insets;

import javax.rad.ui.IInsets;

import com.sibvisions.util.type.StringUtil;

/**
 * Web server implementation of {@link IInsets}.
 * 
 * @author Martin Handsteiner
 */
public class WebInsets extends WebResource
                       implements IInsets, 
                                  Cloneable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class Members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
	/** the top. */
	private int top;
	/** the left. */
	private int left;
	/** the bottom. */
	private int bottom;
	/** the right. */
	private int right;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>WebInsets</code>.
     *
     * @see IInsets
     */
    public WebInsets()
	{
	}
    
    /**
     * Creates a new instance of <code>WebInsets</code>.
     *
     * @param pTop the top 
     * @param pLeft the left 
     * @param pBottom the bottom 
     * @param pRight the right 
     * @see IInsets
     */
    public WebInsets(int pTop, int pLeft, int pBottom, int pRight)
	{
    	top = pTop;
    	left = pLeft;
    	bottom = pBottom;
    	right = pRight;
	}
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
	 * {@inheritDoc}
	 */
    public String getAsString()
    {
    	return top + "," + left + "," + bottom + "," + right; 
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

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
    public Object clone() 
    { 
    	try 
    	{ 
    		return super.clone();
    	} 
    	catch (CloneNotSupportedException e) 
    	{ 
    		// this shouldn't happen, since we are Cloneable
    		throw new InternalError();
    	}
    }
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
	 * {@inheritDoc}
	 */
    public void setAsString(String pValue)
    {
    	int[] values = StringUtil.parseInteger(pValue, ",");
    	
    	if (values != null && values.length >= 4)
    	{
    		top = values[0];
    		left = values[1];
    		bottom = values[2];
    		right = values[3];
    	}
    }
    
    /**
     * Checks whether two {@link WebInsets} objects are equal. Two instances
     * of <code>WebInsets</code> are equal if the four integer values
     * of the fields <code>top</code>, <code>left</code>,
     * <code>bottom</code>, and <code>right</code> are all equal.
     * 
     * @param pObject the object to compare
     * @return <code>true</code> if the two insets are equal, otherwise <code>false</code>.
     */
    public boolean equals(Object pObject) 
    {
        if (pObject instanceof Insets) 
        {
            Insets i = (Insets)pObject;
            
            return top == i.top && left == i.left && bottom == i.bottom && right == i.right;
        }
        
        return false;
    }

    /**
     * Returns the hash code for this insets.
     *
     * @return a hash code for this insets.
     */
    public int hashCode() 
    {
        return 17 + (top * 29) ^ (left * 31) ^ (bottom * 41) ^ (right * 47);
    }

    /**
     * Returns a string representation of this <code>WebInsets</code> object.
     * This method is intended to be used only for debugging purposes, and
     * the content and format of the returned string may vary between
     * implementations. The returned string may be empty but may not be
     * <code>null</code>.
     *
     * @return  a string representation of this <code>Insets</code> object.
     */
    public String toString() 
    {
        return getClass().getName() + "[top="  + top + ",left=" + left + ",bottom=" + bottom + ",right=" + right + "]";
    }    
    
}	// WebInsets
