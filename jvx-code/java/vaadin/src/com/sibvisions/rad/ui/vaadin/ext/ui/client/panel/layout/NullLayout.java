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
package com.sibvisions.rad.ui.vaadin.ext.ui.client.panel.layout;

import com.sibvisions.rad.ui.vaadin.ext.ui.client.panel.helper.Rectangle;
import com.vaadin.client.ComponentConnector;

/**
 * The {@link NullLayout} allows manual layout.
 * 
 * @author Robert Zenz
 */
public class NullLayout extends AbstractClientSideLayout
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link NullLayout}.
	 */
	public NullLayout()
	{
		super();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void layoutComponents(boolean pFirstRun)
	{
		super.layoutComponents();
		
		for (ComponentConnector connector : parent.getChildComponents())
		{
			String constraint = parent.getConstraint(connector);
			
			if (constraint != null)
			{
				Rectangle rectangleConstraint = new Rectangle(constraint);
				
				resizeRelocate(
						connector,
                        rectangleConstraint.x,
						rectangleConstraint.y,
						rectangleConstraint.width,
						rectangleConstraint.height);
			}
		}
	}
	
}	// NullLayout
