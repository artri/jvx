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
 * 07.03.2016 - [RZ] - creation
 */
package com.sibvisions.rad.ui.vaadin.ext.ui.panel.layout;

import com.sibvisions.rad.ui.vaadin.ext.ui.client.panel.helper.Rectangle;
import com.vaadin.ui.Component;

/**
 * The {@link NullLayout} allows to layout the components in an absolute manner.
 * 
 * @author Robert Zenz
 */
public class NullLayout extends AbstractLayout
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link NullLayout}.
	 */
	public NullLayout()
	{
		super("NullLayout");
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public String getStringConstraint(Component pComponent, Object pConstraint)
	{
		if (pConstraint != null)
		{
			return pConstraint.toString();
		}
		
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void clear(Component pComponent)
	{
	}	
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the constraint for the given value.
	 * 
	 * @param pX the x position.
	 * @param pY the y position.
	 * @param pWidth the width.
	 * @param pHeight the height.
	 * @return the constraint.
	 */
	public static Rectangle getConstraint(int pX, int pY, int pWidth, int pHeight)
	{
		return new Rectangle(pX, pY, pWidth, pHeight);
	}
	
}	// NullLayout
