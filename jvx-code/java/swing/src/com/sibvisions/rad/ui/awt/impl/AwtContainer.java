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
 * 01.10.2008 - [HM] - creation
 * 23.09.2009 - [JR] - checkAdd: allow sub toolbars
 * 13.08.2013 - [JR] - #756: add implementation component before add to resource
 */
package com.sibvisions.rad.ui.awt.impl;

import java.awt.Component;
import java.awt.Container;
import java.awt.LayoutManager;
import java.util.ArrayList;
import java.util.List;

import javax.rad.ui.IComponent;
import javax.rad.ui.IContainer;
import javax.rad.ui.ILayout;
import javax.rad.ui.IResource;
import javax.rad.ui.container.IToolBar;
import javax.rad.ui.menu.IMenuBar;
import javax.swing.JComponent;

/**
 * A generic Abstract Window Toolkit(AWT) container object is a 
 * component that can contain other AWT components.
 * 
 * @author Martin Handsteiner
 * @param <C> AWT Container implementation 
 */
public class AwtContainer<C extends Container> extends AwtComponent<C> 
                                               implements IContainer
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** Holds the container layout definition. */
	private ILayout				layout			= null;

	/** List of subcomponents. */
	private List<IComponent>	components		= new ArrayList<IComponent>(4);
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>AwtContainer</code>.
	 * 
	 * @param pContainer AWT Container implementation 
	 */
	protected AwtContainer(C pContainer)
	{
		super(pContainer);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	// IContainer
	
	/**
	 * {@inheritDoc}
	 */
	public ILayout getLayout()
	{
		return layout;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setLayout(ILayout pLayout)
	{
		if (pLayout == null)
		{
			setLayoutIntern(null);
		}
		else
		{
			setLayoutIntern((LayoutManager)pLayout.getResource());
		}
		
		layout = pLayout;
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
		checkAdd(pComponent, pConstraints, pIndex);
		
		if (pComponent.getParent() != null)
		{
			pComponent.getParent().remove(pComponent);
		}
		
		Object constraints;
		if (pConstraints instanceof IResource)
		{
			constraints = ((IResource)pConstraints).getResource();
		}
		else
		{
			constraints = pConstraints;
		}
		
		//add component to internal list before add to technology, because event handling for events from the underlying technology
		//should work with our component tree as well (#756)
		if (pIndex < 0)
		{
			components.add(pComponent);
		}
		else
		{
			components.add(pIndex, pComponent);
		}

		IContainer conOldParent = pComponent.getParent();
		
		pComponent.setParent(this);

		try
		{
			addIntern((Component)pComponent.getResource(), constraints, pIndex);
		}
		catch (RuntimeException re)
		{
			removeIntern((Component)pComponent.getResource());
			
			components.remove(pComponent);
			
			pComponent.setParent(conOldParent);
			
			throw re;
		}
		catch (Error e)
		{
			removeIntern((Component)pComponent.getResource());
			
			components.remove(pComponent);
			
			pComponent.setParent(conOldParent);

			throw e;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void remove(int pIndex)
	{
		Component comp = (Component)components.get(pIndex).getResource();
		boolean visible = comp.isVisible();
		
		if (visible)
		{
			if (comp instanceof JComponent)
			{
				((JComponent)comp).putClientProperty("temporaryVisibility", Boolean.TRUE);
			}

			// Ticket 757: component remove in applets is very slow.
			comp.setVisible(false);
		}
		
		try
		{
			removeIntern(comp);

		}
		finally
		{
			if (visible)
			{
				if (!comp.isVisible()) // Tabset changed visibility, so do not touch
				{
					comp.setVisible(visible);
				}
				if (comp instanceof JComponent)
				{
					((JComponent)comp).putClientProperty("temporaryVisibility", null);
				}
			}
		}
		
		IComponent component = components.remove(pIndex);
		
		component.setParent(null);
		
		resource.repaint();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void remove(IComponent pComponent)
	{
		if (pComponent.getParent() == this)
		{
			remove(components.indexOf(pComponent));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeAll()
	{
		while (components.size() > 0)
		{
			remove(components.size() - 1);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public int getComponentCount()
	{
		return components.size();
	}

	/**
	 * {@inheritDoc}
	 */
	public IComponent getComponent(int pIndex)
	{
		return components.get(pIndex);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int indexOf(IComponent pComponent)
	{
		return components.indexOf(pComponent);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Checks if it's allowed to add a specific component to this container.
	 * 
	 * @param pComponent the component to be added
	 * @param pConstraints an object expressing layout constraints
	 * @param pIndex the position in the container's list at which to insert the IComponent; -1 means insert at the end component
	 */
	protected void checkAdd(IComponent pComponent, Object pConstraints, int pIndex)
	{
		if (!(this instanceof IToolBar) && pComponent instanceof IToolBar)
		{
			throw new IllegalArgumentException("It's not supported to 'add' an IToolBar. Use 'addToolBar'!"); 
		}
		
		if (pComponent instanceof IMenuBar)
		{
			throw new IllegalArgumentException("It's not supported to 'add' an IMenuBar. Use 'setMenuBar'!");
		}
	}

	/**
	 * Internal function, for setting the layout.
	 * 
	 * @param pLayoutManager the layout
	 */
	protected void setLayoutIntern(LayoutManager pLayoutManager)
	{
		resource.setLayout(pLayoutManager);
	}

	/**
	 * Internal function, for adding physical component and supporting layers.
	 * 
	 * @param pComponent the <code>Component</code> to be added
	 * @param pConstraints an object expressing layout contraints for this
	 * @param pIndex the position in the container's list at which to insert
	 *        the <code>Component</code>; <code>-1</code> means insert at the end
	 *        component
	 * @see #add(IComponent, Object, int)
	 */
	protected void addIntern(Component pComponent, Object pConstraints, int pIndex)
	{
		resource.add(pComponent, pConstraints, pIndex);
	}

	/**
	 * Internal function, for adding physical component and supporting layers.
	 * 
	 * @param pComponent the <code>Component</code> to be removed
	 * @see #remove(int)
	 */
	protected void removeIntern(Component pComponent)
	{
		resource.remove(pComponent);
	}
	
} 	// AwtContainer
