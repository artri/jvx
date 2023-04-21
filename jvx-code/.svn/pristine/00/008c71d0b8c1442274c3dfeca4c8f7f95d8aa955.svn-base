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
 * 16.10.2012 - [CB] - creation
 */
package com.sibvisions.rad.ui.vaadin.impl.container;

import javax.rad.ui.IComponent;
import javax.rad.ui.IContainer;
import javax.rad.ui.container.ISplitPanel;

import com.sibvisions.rad.ui.vaadin.ext.VaadinUtil;
import com.sibvisions.rad.ui.vaadin.impl.VaadinComponentBase;
import com.sibvisions.rad.ui.vaadin.impl.VaadinContainer;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.AbstractSplitPanel;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.VerticalSplitPanel;

/**
 * The <code>VaadinSplitPanel</code> class is the vaadin implementation of
 * {@link ISplitPanel}.
 * 
 * @author Benedikt Cermak
 */
public class VaadinSplitPanel extends VaadinContainer<AbstractSplitPanel> implements ISplitPanel
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The left or top Component. */
	private IComponent firstComponent = new VaadinPanel();
	
	/** The right or bottom Component. */
	private IComponent secondComponent = new VaadinPanel();
	
	/** The current orientation. */
	private int orientation = SPLIT_LEFT_RIGHT;
	
	/** The default size of a splitpanel if it is undefined. **/
	private static final int DEFAULT_SPLITPANEL_HEIGHT = 600;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of <code>VaadinSplitPanel</code>.
	 *
	 * @see ISplitPanel
	 */
	public VaadinSplitPanel()
	{
		super(new HorizontalSplitPanel());
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public int getOrientation()
	{
		return orientation;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setOrientation(int pOrientation)
	{
		if (pOrientation != orientation)
		{
			orientation = pOrientation;
			
			AbstractSplitPanel oldResource = resource;
			
			if (orientation == SPLIT_TOP_BOTTOM)
			{
				resource = new VerticalSplitPanel();
			}
			else
			{
				resource = new HorizontalSplitPanel();
			}
			
			if (oldResource.getParent() != null)
			{
				((ComponentContainer)oldResource.getParent()).replaceComponent(oldResource, resource);
			}
			
			resource.setFirstComponent(oldResource.getFirstComponent());
			resource.setSecondComponent(oldResource.getSecondComponent());
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getDividerPosition()
	{
		if (orientation == SPLIT_TOP_BOTTOM)
		{
			return (int)(resource.getHeight() * (resource.getSplitPosition() / 100));
		}
		else
		{
			return (int)(resource.getWidth() * (resource.getSplitPosition() / 100));
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setDividerPosition(int pDividerPosition)
	{
		if (pDividerPosition <= -1)
		{
			resource.setSplitPosition(50, Unit.PERCENTAGE);
		}
		else
		{
			resource.setSplitPosition(pDividerPosition, Unit.PIXELS);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getDividerAlignment()
	{
		// not supported
		return -1;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setDividerAlignment(int pDividerAlignment)
	{
		// not supported
	}
	
	/**
	 * {@inheritDoc}
	 */
	public IComponent getFirstComponent()
	{
		return firstComponent;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setFirstComponent(IComponent pComponent)
	{
		if (pComponent == null)
		{
			if (firstComponent != null)
			{
				resource.removeComponent((Component)firstComponent.getResource());
				firstComponent = null;
			}
		}
		else
		{
			add(pComponent, FIRST_COMPONENT, 0);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public IComponent getSecondComponent()
	{
		return secondComponent;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setSecondComponent(IComponent pComponent)
	{
		if (pComponent == null)
		{
			if (secondComponent != null)
			{
				
				resource.removeComponent((Component)secondComponent.getResource());
				secondComponent = null;
			}
		}
		else
		{
			add(pComponent, SECOND_COMPONENT, -1);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void add(IComponent pComponent, Object pConstraints, int pIndex)
	{
		if (pConstraints == FIRST_COMPONENT && firstComponent != null)
		{
			remove(firstComponent);
		}
		else if (pConstraints == SECOND_COMPONENT && secondComponent != null)
		{
			remove(secondComponent);
		}
		else if (pConstraints == null)
		{
			if (firstComponent == null)
			{
				pConstraints = FIRST_COMPONENT;
			}
			else if (secondComponent == null)
			{
				pConstraints = SECOND_COMPONENT;
			}
		}
		if (pConstraints == FIRST_COMPONENT)
		{
			super.add(pComponent, FIRST_COMPONENT, 0);
			firstComponent = pComponent;
			
		}
		else if (pConstraints == SECOND_COMPONENT)
		{
			super.add(pComponent, SECOND_COMPONENT, -1);
			secondComponent = pComponent;
		}
		else
		{
			throw new IllegalArgumentException("SplitPanel can only handle first and second component!");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void remove(int pIndex)
	{
		IComponent comp = getComponent(pIndex);
		
		super.remove(pIndex);
		
		if (comp == firstComponent)
		{
			firstComponent = null;
		}
		else if (comp == secondComponent)
		{
			secondComponent = null;
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void performLayout()
	{
		setSize(firstComponent);
		setSize(secondComponent);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setHeightUndefined()
	{
		resource.setHeight(DEFAULT_SPLITPANEL_HEIGHT, Unit.PIXELS);
		
		performLayout();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setParent(IContainer pParent)
	{
		super.setParent(pParent);
		
		setSize(firstComponent);
		setSize(secondComponent);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Sets the size of given {@link IComponent} to "an appropriate value".
	 * 
	 * @param pComponent the {@link IComponent}.
	 */
	private void setSize(IComponent pComponent)
	{
		if (pComponent == null)
		{
			return;
		}
		
		if (!VaadinUtil.isWidthUndefined(resource, false))
		{
			((VaadinComponentBase<?, ?>)pComponent).setWidthFull();
		}
		else
		{
			((VaadinComponentBase<?, ?>)pComponent).setWidthUndefined();
		}
		
		if (!VaadinUtil.isHeightUndefined(resource, false))
		{
			((VaadinComponentBase<?, ?>)pComponent).setHeightFull();
		}
		else
		{
			((VaadinComponentBase<?, ?>)pComponent).setHeightUndefined();
		}
	}
	
}	// VaadinSplitPanel
