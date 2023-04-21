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

import javax.rad.ui.IPoint;

/**
 * Platform and technology independent point.
 * 
 * @author Martin Handsteiner
 */
public class UIPoint extends UIResource<IPoint> 
                     implements IPoint
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>UIPoint</code> with x=0 and y=0.
     *
     * @see IPoint
     */
	public UIPoint()
	{
		super(UIFactoryManager.getFactory().createPoint(0, 0));
	}

    /**
     * Creates a new instance of <code>UIPoint</code> with the given point.
     *
     * @param pPoint the point
     * @see IPoint
     */
	protected UIPoint(IPoint pPoint)
	{
		super(pPoint);
	}
	
    /**
     * Creates a new instance of <code>UIPoint</code> with the given x and y.
     *
     * @param pX the x value
     * @param pY the y value
     * @see IPoint
     */
    public UIPoint(int pX, int pY)
    {
    	super(UIFactoryManager.getFactory().createPoint(pX, pY));
    }

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
    public int getX()
    {
    	return uiResource.getX();
    }

	/**
	 * {@inheritDoc}
	 */
    public void setX(int pX)
    {
    	uiResource.setX(pX);
    }

	/**
	 * {@inheritDoc}
	 */
    public int getY()
    {
    	return uiResource.getY();
    }

	/**
	 * {@inheritDoc}
	 */
    public void setY(int pY)
    {
    	uiResource.setY(pY);
    }
    
}	// UIPoint
