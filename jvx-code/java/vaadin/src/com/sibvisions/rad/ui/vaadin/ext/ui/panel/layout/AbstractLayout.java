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
 * 10.03.2016 - [RZ] - creation
 */
package com.sibvisions.rad.ui.vaadin.ext.ui.panel.layout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sibvisions.rad.ui.vaadin.ext.ui.panel.ILayout;
import com.sibvisions.rad.ui.vaadin.ext.ui.panel.ILayoutedPanel;
import com.vaadin.ui.Component;

/**
 * The {@link AbstractLayout} is an abstract implementation of {@link ILayout}
 * which provides the most basic methods and mechanics.
 * 
 * @author Robert Zenz
 */
public abstract class AbstractLayout implements ILayout
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The {@link List} of {@link Component}s form the {@link #parent}. */
	protected List<Component> components;
	
	/** The {@link Map} which is used for the data. */
	protected Map<String, String> data = new HashMap<String, String>();
	
	/** The parent {@link ILayoutedPanel}. */
	protected ILayoutedPanel parent;
	
	/** The name this layout. */
	private String name;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link AbstractLayout}.
	 *
	 * @param pName the {@link String name} of this layout.
	 */
	protected AbstractLayout(String pName)
	{
		super();
		
		name = pName;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public Map<String, String> getData()
	{
		return data;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setParent(ILayoutedPanel pParent)
	{
		parent = pParent;
		
		if (parent != null)
		{
			components = parent.getComponents();
		}
		else
		{
			components = null;
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Notifies the {@link #parent} of {@link ILayoutedPanel#notifyOfChanges()
	 * of changes} if there is a {@link #parent}.
	 */
	protected void notifyParent()
	{
		if (parent != null)
		{
			parent.notifyOfChanges();
		}
	}
	
}	// AbstractLayout
