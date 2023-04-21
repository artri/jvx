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
 * 20.09.2016 - [RZ] - creation
 */
package com.sibvisions.rad.ui.vaadin.ext.ui.client.grid;

import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ui.VCustomComponent;
import com.vaadin.client.widgets.Grid;
import com.vaadin.client.widgets.Grid.Column;

/**
 * The {@link JVxEditor} is an {@link com.vaadin.client.widgets.Grid.Editor}
 * extension which contains JVx specific behavior.
 * 
 * @author Robert Zenz
 * @param <T> the type.
 */
public class JVxEditor<T> extends Grid.Editor<T>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link JVxEditor}.
	 */
	public JVxEditor()
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
	protected Widget getWidget(Column<?, T> pColumn)
	{
		// This is workaround for Vaadin (Upstream) #20336.
		Widget widget = super.getWidget(pColumn);
		
		if (widget instanceof VCustomComponent)
		{
			return ((VCustomComponent)widget).getWidget();
		}
		
		return widget;
	}
	
}	// JVxEditor
