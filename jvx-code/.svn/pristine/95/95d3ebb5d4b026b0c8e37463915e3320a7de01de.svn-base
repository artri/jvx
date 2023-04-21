/*
 * Copyright 2012 SIB Visions GmbH
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
 * 21.10.2012 - [CB] - creation
 */
package com.sibvisions.rad.ui.vaadin.impl.menu;

import java.util.List;

import javax.rad.ui.IComponent;
import javax.rad.ui.IContainer;
import javax.rad.ui.ILayout;
import javax.rad.ui.menu.IMenu;
import javax.rad.ui.menu.IMenuBar;
import javax.rad.ui.menu.IPopupMenu;

import com.sibvisions.rad.ui.vaadin.impl.ContainerObject;
import com.sibvisions.rad.ui.vaadin.impl.IVaadinContainer;

/**
 * The <code>VaadinMenu</code> class is the vaadin implementation of {@link IMenu}.
 * 
 * @author Benedikt Cermak
 */
public class VaadinMenu extends VaadinMenuItem
                        implements IMenu,
                                   IVaadinContainer
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the container object. */
	private ContainerObject container;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>VaadinMenu</code>.
     *
     * @see javax.rad.ui.menu.IMenu
     */
	public VaadinMenu()
	{
		this(null);
	}
	
	/**
     * Creates a new instance of <code>VaadinMenuItem</code>.
     *
     * @see javax.rad.ui.menu.IMenuItem
     * @param pText the MenuItem caption
     */
	
	public VaadinMenu(String pText)
	{
		super(pText);
		
		container = new ContainerObject(this);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
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
	public void performLayout()
	{
		//Nothing to do
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
	public IComponent getComponent(int pIndex)
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

	/**
	 * {@inheritDoc}
	 */
	public void addToVaadin(IComponent pComponent, Object pConstraints, int pIndex)
    {
		if (resource.getMenuItem() != null)
		{
			IContainer parent = getParent();
			
			while (parent != null && !(parent instanceof IMenuBar))
			{
				parent = parent.getParent();
			}
			
			if (parent != null)
			{
				((VaadinMenuBar)parent).addMenuItem(this, pComponent, pIndex);
			}
		}
    }
	
	/**
	 * {@inheritDoc}
	 */
	public void removeFromVaadin(IComponent pComponent)
	{
		if (resource.getMenuItem() != null)
		{
			IContainer parent = getParent();
			
			while (parent != null)
			{
				if (parent instanceof IMenuBar)
				{
					((VaadinMenuBar)parent).removeMenuItem(pComponent);
				}
				else if (parent instanceof IPopupMenu)
				{
					((VaadinPopupMenu)parent).removeMenuItem(pComponent);
				}
				
				parent = parent.getParent();
			}
		}	
	}

	/**
	 * {@inheritDoc}
	 */
	public void addSeparator()
	{
		addSeparator(-1);
	}
    
    /**
	 * {@inheritDoc}
	 */
	public void addSeparator(int pIndex)
	{	
		add(new VaadinSeparator(), null, pIndex);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the list of added components.
	 * 
	 * @return the added components
	 */
	protected List<IComponent> getComponents()
	{
		return container.getComponents();
	}
	
}	// VaadinMenu

