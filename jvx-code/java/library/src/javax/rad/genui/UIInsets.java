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
 * 14.11.2008 - [HM] - creation
 * 24.10.2012 - [JR] - #604: added constructor
 */
package javax.rad.genui;

import javax.rad.ui.IInsets;

/**
 * Platform and technology independent Insets.
 * 
 * @author Martin Handsteiner
 */
public class UIInsets extends UIResource<IInsets> 
                      implements IInsets
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>UIInsets</code>.
     *
     * @see IInsets
     */
    public UIInsets()
	{
		super(UIFactoryManager.getFactory().createInsets(0, 0, 0, 0));
	}

    /**
     * Creates a new instance of <code>UIInsets</code> with the given
     * insets.
     *
     * @param pInsets the insets
     * @see IInsets
     */
    protected UIInsets(IInsets pInsets)
	{
		super(pInsets);
	}
    
    /**
     * Creates a new instance of <code>UIInsets</code>.
     *
     * @param pTop the top 
     * @param pLeft the left 
     * @param pBottom the bottom 
     * @param pRight the right 
     * @see IInsets
     */
    public UIInsets(int pTop, int pLeft, int pBottom, int pRight)
	{
		super(UIFactoryManager.getFactory().createInsets(pTop, pLeft, pBottom, pRight));
	}
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
    public int getLeft()
    {
    	return uiResource.getLeft();
    }

	/**
	 * {@inheritDoc}
	 */
    public void setLeft(int pLeft)
    {
    	uiResource.setLeft(pLeft);
    }
	/**
	 * {@inheritDoc}
	 */
    public int getRight()
    {
    	return uiResource.getRight();
    }

	/**
	 * {@inheritDoc}
	 */
    public void setRight(int pRight)
    {
    	uiResource.setRight(pRight);
    }

	/**
	 * {@inheritDoc}
	 */
    public int getTop()
    {
    	return uiResource.getTop();
    }

	/**
	 * {@inheritDoc}
	 */
    public void setTop(int pTop)
    {
    	uiResource.setTop(pTop);
    }

	/**
	 * {@inheritDoc}
	 */
    public int getBottom()
    {
    	return uiResource.getBottom();
    }

	/**
	 * {@inheritDoc}
	 */
    public void setBottom(int pBottom)
    {
    	uiResource.setBottom(pBottom);
    }

}	// UIInsets
