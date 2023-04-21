/*
 * Copyright 2013 SIB Visions GmbH
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
 * 26.03.2013 - [TK] - creation
 */
package com.sibvisions.rad.ui.vaadin.impl;

import java.util.List;

import javax.rad.ui.IAlignmentConstants;
import javax.rad.ui.IComponent;
import javax.rad.ui.IDimension;
import javax.rad.ui.ILayout;

import com.sibvisions.rad.ui.vaadin.impl.layout.IVaadinLayout;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Component;

/**
 * The <code>VaadinContainerBase</code> class is the vaadin implementation of {@link javax.rad.ui.IContainer} 
 * and the base class for all containers. It encapsulates the component management and layout handling. 
 * 
 * @author René Jahn
 *
 * @param <CR> an instance of {@link Component}
 * @param <C> an instance of {@link Component}
 */
public abstract class VaadinContainerBase<CR extends Component, C extends Component> extends VaadinComponentBase<CR, C>
															                         implements IVaadinContainer, 
                                                                                                IAlignmentConstants
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the internal component container. */
	private ContainerObject container;
	
	/** whether {@link #performLayout()} should be ignored. */
	boolean bIgnoreLayout = false;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /**
     * Creates a new instance of <code>VaadinContainerBase</code>.
     *
     * @param pContainer a AbstractComponentContainer 
     * @see javax.rad.ui.IContainer
     */
	protected VaadinContainerBase(C pContainer)
	{
		super(pContainer);

		container = new ContainerObject(this);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface Implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("rawtypes")
	public ILayout getLayout()
    {
		return container.getLayout();
    }

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("rawtypes")
	public void setLayout(ILayout pLayout)
    {
		container.setLayout(pLayout);	
		
		performLayout();
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
		
		performLayout();
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
		
		performLayout();
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
	public void performLayout()
	{
		if (!bIgnoreLayout)
		{
		    ILayout layout = container.getLayout();
		    
			if (layout instanceof IVaadinLayout)
			{
				((IVaadinLayout<?>)layout).markDirty();
			}
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSize(IDimension pSize)
	{
		float fWidth = resource.getWidth();
		float fHeight = resource.getHeight();
		Unit uWidth = resource.getWidthUnits();
		Unit uHeight = resource.getHeightUnits();
		int iWidth = pSize.getWidth();
		int iHeight = pSize.getHeight();
				
		super.setSize(pSize);
		
		if (fWidth != resource.getWidth() || uWidth != resource.getWidthUnits()
			|| fHeight != resource.getHeight() || uHeight != resource.getHeightUnits()
			|| iWidth != bounds.getWidth() || iHeight != bounds.getHeight())
		{
			performLayout();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setWidthFull()
	{
		float fValue = resource.getWidth();
		Unit unit = resource.getWidthUnits();
		
		super.setWidthFull();
		
		if (fValue != resource.getWidth() || unit != resource.getWidthUnits())
		{
			performLayout();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setHeightFull()
	{
		float fValue = resource.getHeight();
		Unit unit = resource.getHeightUnits();

		super.setHeightFull();
		
		if (fValue != resource.getHeight() || unit != resource.getHeightUnits())
		{
			performLayout();
		}	
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setWidthUndefined()
	{
		float fValue = resource.getWidth();
		Unit unit = resource.getWidthUnits();
		
		super.setWidthUndefined();
		
		if (fValue != resource.getWidth() || unit != resource.getWidthUnits())
		{
			performLayout();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setHeightUndefined()
	{
		float fValue = resource.getHeight();
		Unit unit = resource.getHeightUnits();

		super.setHeightUndefined();
		
		if (fValue != resource.getHeight() || unit != resource.getHeightUnits())
		{
			performLayout();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSizeFull()
	{
		float fWidth = resource.getWidth();
		float fHeight = resource.getHeight();
		Unit uWidth = resource.getWidthUnits();
		Unit uHeight = resource.getHeightUnits();

		super.setWidthFull();
		super.setHeightFull();
		
		if (fWidth != resource.getWidth() || uWidth != resource.getWidthUnits()
			|| fHeight != resource.getHeight() || uHeight != resource.getHeightUnits())
		{
			performLayout();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSizeUndefined()
	{
		float fWidth = resource.getWidth();
		float fHeight = resource.getHeight();
		Unit uWidth = resource.getWidthUnits();
		Unit uHeight = resource.getHeightUnits();

		super.setWidthUndefined();
		super.setHeightUndefined();
		
		if (fWidth != resource.getWidth() || uWidth != resource.getWidthUnits()
			|| fHeight != resource.getHeight() || uHeight != resource.getHeightUnits())
		{
			performLayout();
		}
	}	
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Sets the layout without calling {@link #performLayout()}.
	 * 
	 * @param pLayout the default layout
	 */
	protected void setDefaultLayout(ILayout pLayout)
	{
		//don't execute performLayout
		container.setLayout(pLayout);
	}
	
	/**
	 * Gets the list of added components.
	 * 
	 * @return the added components
	 */
	protected List<IComponent> getComponents()
	{
		return container.getComponents();
	}
	
	/**
	 * Sets whether {@link #performLayout()} should be ignored.
	 * 
	 * @param pIgnore <code>true</code> to ignore {@link #performLayout()} and
	 *                <code>false</code> to invoke {@link #performLayout()} immediately
	 */
	public void setIgnorePerformLayout(boolean pIgnore)
	{
		bIgnoreLayout = pIgnore;

		if (!pIgnore)
		{
			performLayout();
		}
	}
	
}	// VaadinContainerBase
