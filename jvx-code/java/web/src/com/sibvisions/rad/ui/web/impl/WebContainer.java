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

import javax.rad.ui.IComponent;
import javax.rad.ui.IContainer;
import javax.rad.ui.ILayout;

/**
 * Web server implementation of {@link IContainer}.
 * 
 * @author Martin Handsteiner
 */
public abstract class WebContainer extends WebComponent
                                   implements IWebContainer
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the internal component container. */
	private ContainerObject container;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>WebContainer</code>.
     *
     * @see IContainer
     */
	protected WebContainer()
	{
		container = new ContainerObject(this);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface Implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public ILayout getLayout()
    {
		return container.getLayout();
    }

	/**
	 * {@inheritDoc}
	 */
	public void setLayout(ILayout pLayout)
    {
		container.setLayout(pLayout);
    }

	/**
	 * {@inheritDoc}
	 */
	public void add(IComponent pComponent)
	{
		add(pComponent, null, -1);
	}

	/**
	 * {@inheritDoc}
	 */
	public void add(IComponent pComponent, Object pConstraints)
	{
		add(pComponent, pConstraints, -1);
	}

	/**
	 * {@inheritDoc}
	 */
	public void add(IComponent pComponent, int pIndex)
	{
		add(pComponent, null, pIndex);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void add(IComponent pComponent, Object pConstraints, int pIndex)
    {
		container.add(pComponent, pConstraints, pIndex);
    }

	/**
	 * {@inheritDoc}
	 */
	public void remove(IComponent pComponent)
	{
		container.remove(pComponent);
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeAll()
	{
		container.removeAll();
	}

	/**
	 * {@inheritDoc}
	 */
	public void remove(int pIndex)
    {
		container.remove(pIndex);
    }

	/**
	 * {@inheritDoc}
	 */
	public int getComponentCount()
	{
		return container.getComponentCount();
	}

	/**
	 * {@inheritDoc}
	 */
	public WebComponent getComponent(int pIndex)
	{
		return container.getComponent(pIndex);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int indexOf(IComponent pComponent)
	{
		return container.indexOf(pComponent);
	}
	
}	// WebContainer
