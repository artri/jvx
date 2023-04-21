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
 * 02.05.2016 - [RZ] - creation
 * 25.04.2019 - [JD] - skip setting full size for VaadinTextArea/VaadinTextField
 */
package com.sibvisions.rad.ui.vaadin.impl.layout;

import java.util.ArrayList;
import java.util.List;

import javax.rad.ui.IComponent;
import javax.rad.ui.IDimension;
import javax.rad.ui.IInsets;

import com.sibvisions.rad.ui.vaadin.ext.ui.client.panel.helper.Margins;
import com.sibvisions.rad.ui.vaadin.ext.ui.panel.LayoutedPanel;
import com.sibvisions.rad.ui.vaadin.ext.ui.panel.layout.AbstractGapLayout;
import com.sibvisions.rad.ui.vaadin.impl.VaadinComponentBase;
import com.sibvisions.rad.ui.vaadin.impl.VaadinInsets;
import com.vaadin.ui.Component;
import com.vaadin.ui.Component.Event;
import com.vaadin.ui.Component.Listener;

/**
 * The {@link AbstractVaadinClientLayout} provides the abstract base for all
 * wrappers of client-side layouts.
 * 
 * @author Robert Zenz
 * @param <L> the type of the {@link AbstractGapLayout}.
 * @param <CO> the type of the constraints.
 */
public abstract class AbstractVaadinClientLayout<L extends AbstractGapLayout, CO> extends AbstractVaadinLayout<LayoutedPanel, CO>
		                                                                          implements javax.rad.ui.ILayout<CO>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The {@link List} of added {@link IComponent}s. */
	protected List<IComponent> components = new ArrayList<IComponent>();
	
	/** The layout. */
	protected L layout = null;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link AbstractVaadinClientLayout}.
	 *
	 * @param pLayout the layout.
	 */
	protected AbstractVaadinClientLayout(L pLayout)
	{
		super(new LayoutedPanel(pLayout));
		
		layout = pLayout;
		
		resource.addListener(new BeforeClientResponseEventListener(this));
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~		

	/**
	 * {@inheritDoc}
	 */
	public void repaintLayout(boolean pComponentsChanged)
	{
		resource.storeComponentSizes();
		
		// This is needed for a few components which would otherwise calculate
		// their contents incorrectly if they are not set to full size.
		// The TabSheet is one of these.
		for (IComponent component : components)
		{
			VaadinComponentBase<?, ?> componentBase = (VaadinComponentBase<?, ?>)component;
			
			if (componentBase.getResource().getWidth() < 0)
			{
			    componentBase.setWidthFull();
			}
            if (componentBase.getResource().getHeight() < 0)
            {
                componentBase.setHeightFull();
            }
		}
		
		resource.markAsDirty();
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public void addComponent(IComponent pComponent, Object pConstraints, int pIndex)
	{
		resource.addComponent((Component)pComponent.getResource(), pConstraints, pIndex);
		
		components.add(pComponent);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void removeComponent(IComponent pComponent)
	{
		resource.removeComponent((Component)pComponent.getResource());
		
		components.remove(pComponent);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public IInsets getMargins()
	{
		Margins margins = layout.getMargins();
		
		return new VaadinInsets(
				margins.top,
				margins.left,
				margins.bottom,
				margins.right);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setMargins(IInsets pMargins)
	{
		if (pMargins == null)
		{
			layout.setMargins(null);
		}
		else
		{
			layout.setMargins(new Margins(
					pMargins.getTop(),
					pMargins.getLeft(),
					pMargins.getBottom(),
					pMargins.getRight()));
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getHorizontalGap()
	{
		return layout.getHorizontalGap();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setHorizontalGap(int pHorizontalGap)
	{
		layout.setHorizontalGap(pHorizontalGap);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getVerticalGap()
	{
		return layout.getVerticalGap();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setVerticalGap(int pVerticalGap)
	{
		layout.setVerticalGap(pVerticalGap);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Sets the state into the state.
	 * 
	 * @param pComponent the component.
	 */
	protected void setSizes(IComponent pComponent)
	{
		if (pComponent.isMinimumSizeSet())
		{
			IDimension minimumSize = pComponent.getMinimumSize();
			
			resource.setMinimumSize(
					(Component)pComponent.getResource(),
					minimumSize.getWidth(),
					minimumSize.getHeight());
		}
		else
		{
			resource.setMinimumSize((Component)pComponent.getResource(), -1, -1);
		}
		
		if (pComponent.isMaximumSizeSet())
		{
			IDimension maximumSize = pComponent.getMaximumSize();
			
			resource.setMaximumSize(
					(Component)pComponent.getResource(),
					maximumSize.getWidth(),
					maximumSize.getHeight());
		}
		else
		{
			resource.setMaximumSize((Component)pComponent.getResource(), -1, -1);
		}
		
		if (pComponent.isPreferredSizeSet())
		{
			IDimension preferredSize = pComponent.getPreferredSize();
			
			resource.setPreferredSize(
					(Component)pComponent.getResource(),
					preferredSize.getWidth(),
					preferredSize.getHeight());
		}
		else
		{
			resource.setPreferredSize((Component)pComponent.getResource(), -1, -1);
		}
		
		IDimension size = pComponent.getSize();
		
		if (size != null && (size.getWidth() > 0 || size.getHeight() > 0))
		{
			
			resource.setSize(
					(Component)pComponent.getResource(),
					size.getWidth(),
					size.getHeight());
		}
		else
		{
			resource.setSize((Component)pComponent.getResource(), -1, -1);
		}
	}

	/**
	 * Sets the state into the state.
	 */
	protected void beforeClientResponse()
	{
		setSizes(getParentContainer());
		
		for (IComponent component : components)
		{
			setSizes(component);
		}
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * The {@link BeforeClientResponseEventListener} listens for the
	 * {@link com.sibvisions.rad.ui.vaadin.ext.ui.panel.LayoutedPanel.BeforeClientReponseEvent}
	 * and invokes the {@link AbstractVaadinClientLayout#beforeClientResponse()}
	 * method.
	 */
	protected static class BeforeClientResponseEventListener implements Listener
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/** The {@link AbstractVaadinClientLayout parent}. */
		protected AbstractVaadinClientLayout<?, ?> parent = null;
		
		/**
		 * Creates a new instance of {@link BeforeClientResponseEventListener}.
		 *
		 * @param pParent the {@link AbstractVaadinClientLayout parent}.
		 */
		public BeforeClientResponseEventListener(AbstractVaadinClientLayout<?, ?> pParent)
		{
			parent = pParent;
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Interface implementation
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * {@inheritDoc}
		 */
		public void componentEvent(Event pEvent)
		{
			if (pEvent instanceof LayoutedPanel.BeforeClientReponseEvent)
			{
				parent.beforeClientResponse();
			}
		}
		
	}	// BeforeClientResponseEventListener
	
}	// AbstractVaadinClientLayout
