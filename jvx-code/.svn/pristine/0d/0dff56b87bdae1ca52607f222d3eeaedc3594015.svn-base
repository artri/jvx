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

import com.vaadin.ui.Component;

/**
 * The {@link BorderLayout} allows to layout the components in a borderly
 * fashion.
 * 
 * @author Robert Zenz
 */
public class BorderLayout extends AbstractGapLayout
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constants
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The constraint for the center. */
	public static final String CENTER = "Center";
	
	/** The constraint for the east. */
	public static final String EAST = "East";
	
	/** The constraint for the north. */
	public static final String NORTH = "North";
	
	/** The constraint for the south. */
	public static final String SOUTH = "South";
	
	/** The constraint for the west. */
	public static final String WEST = "West";
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link BorderLayout}.
	 */
	public BorderLayout()
	{
		super("BorderLayout");
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public String getStringConstraint(Component pComponent, Object pConstraint)
	{
		if (!(pConstraint instanceof String))
		{
			return CENTER;
		}
		
		return (String)pConstraint;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void clear(Component pComponent)
	{
	}
	
}	// BorderLayout
