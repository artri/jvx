/*
 * Copyright 2011 SIB Visions GmbH
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
 * 12.09.2011 - [JR] - creation
 */
package com.sibvisions.util.type;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;

/**
 * The <code>ScreenUtil</code> contains utility methods which solves common screen problems. 
 * 
 * @author René Jahn
 */
public final class ScreenUtil
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Invisible constructor because <code>ScreenUtil</code> is a utility
	 * class.
	 */
	private ScreenUtil()
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the screen centered coordinates for the given component size.
	 * 
	 * @param pDimension a component dimension
	 * @return Point the center point
	 */
	public static Point getScreenCenterLocation(Dimension pDimension)
	{
		return new Point((Toolkit.getDefaultToolkit().getScreenSize().width - pDimension.width) / 2,
						 (Toolkit.getDefaultToolkit().getScreenSize().height - pDimension.height) / 2);
	}


	/**
	 * Centers a component on the screen.
	 * 
	 * @param pComponent the component 
	 */
	public static void center(Component pComponent)
	{
		if (pComponent == null)
		{
			return;
		}
		
		pComponent.setLocation(getScreenCenterLocation(pComponent.getSize()));
	}
	
	/**
	 * Gets the coordinates for a component to center it over another component. 
	 * 
	 * @param pComponent the component that should be centered
	 * @param pOtherComponent the reference component
	 * @return the center point
	 */
	public static Point getObjectCenterLocation(Component pComponent, Component pOtherComponent)
	{
		return new Point(pOtherComponent.getX() + ((pOtherComponent.getWidth() - pComponent.getWidth()) / 2),
		                 pOtherComponent.getY() + ((pOtherComponent.getHeight() - pComponent.getHeight()) / 2));
	}
	
	/**
	 * Centers a component over another component.
	 *
	 * @param pComponent the component that should be centered
	 * @param pOtherComponent the reference component
	 */
	public static void center(Component pComponent, Component pOtherComponent)
	{
		if (pComponent == null)
		{
			return;
		}
		
        if (pOtherComponent == null || !pOtherComponent.isShowing())
        {
            center(pComponent);
        }
        else
        {
        	pComponent.setLocation(getObjectCenterLocation(pComponent, pOtherComponent));
        }
	}

}	// ScreenUtil
