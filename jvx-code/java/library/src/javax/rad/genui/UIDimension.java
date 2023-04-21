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

import javax.rad.ui.IDimension;

/**
 * Platform and technology independent Dimension.
 * 
 * @author Martin Handsteiner
 */
public class UIDimension extends UIResource<IDimension> 
                         implements IDimension
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>UIDimension</code>.
     *
     * @see IDimension
     */
	public UIDimension()
	{
		super(UIFactoryManager.getFactory().createDimension(0, 0));
	}

    /**
     * Creates a new instance of <code>UIDimension</code> with the given
     * dimension.
     *
     * @param pDimension the dimension
     * @see IDimension
     */
	protected UIDimension(IDimension pDimension)
	{
		super(pDimension);
	}
	
    /**
     * Creates a new instance of <code>IDimension</code> with the given with and height.
     *
     * @param pWidth the width 
     * @param pHeight the height 
     * @see IDimension
     */
    public UIDimension(int pWidth, int pHeight)
    {
    	super(UIFactoryManager.getFactory().createDimension(pWidth, pHeight));
    }

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface Implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
    public int getWidth()
    {
    	return uiResource.getWidth();
    }

	/**
	 * {@inheritDoc}
	 */
    public void setWidth(int pWidth)
    {
    	uiResource.setWidth(pWidth);
    }

	/**
	 * {@inheritDoc}
	 */
    public int getHeight()
    {
    	return uiResource.getHeight();
    }

	/**
	 * {@inheritDoc}
	 */
    public void setHeight(int pHeight)
    {
    	uiResource.setHeight(pHeight);
    }

}	// UIDimension
