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
package com.sibvisions.rad.ui.vaadin.ext.ui.panel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.sibvisions.rad.ui.vaadin.ext.ui.client.panel.LayoutedPanelState;
import com.sibvisions.rad.ui.vaadin.ext.ui.client.panel.helper.Size;
import com.sibvisions.rad.ui.vaadin.ext.ui.panel.layout.NullLayout;
import com.vaadin.shared.Connector;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.AbstractSplitPanel;
import com.vaadin.ui.Component;
import com.vaadin.ui.HasComponents;
import com.vaadin.ui.TabSheet;

/**
 * The {@link LayoutedPanel} is the main implementation of
 * {@link ILayoutedPanel} and provided all methods.
 * 
 * @author Robert Zenz
 */
public class LayoutedPanel extends AbstractLayout implements ILayoutedPanel
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The cached instance of the {@link BeforeClientReponseEvent}. */
	private BeforeClientReponseEvent cachedBeforeClientReponseEventInstance = null;
	
	/** The {@link Component}s which have been added. */
	private List<Component> components = new ArrayList<Component>();
	
	/**
	 * The {@link Map} which maps from {@link Component}s to their constraints.
	 */
	private Map<Component, Object> componentToConstraints = new HashMap<Component, Object>();
	
	/** The {@link ILayout}. */
	private ILayout layout;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link LayoutedPanel}.
	 */
	public LayoutedPanel()
	{
		super();
		
		// Horrible hack, see beforeClientReponse(boolean) for the explanation.
		setWidth(100, Unit.PERCENTAGE);
		setHeight(Float.POSITIVE_INFINITY, Unit.PICAS);
	}
	
	/**
	 * Creates a new instance of {@link LayoutedPanel}.
	 *
	 * @param pLayout the {@link ILayout layout}.
	 */
	public LayoutedPanel(ILayout pLayout)
	{
		this();
		
		setLayout(pLayout);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public void addComponent(Component pComponent, Object pConstraint)
	{
		addComponent(pComponent, pConstraint, -1);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void addComponent(Component pComponent, Object pConstraint, int pIndex)
	{
		super.addComponent(pComponent);
		
		if (pIndex < 0)
		{
			components.add(pComponent);
		}
		else
		{
			components.add(pIndex, pComponent);
		}
		
		setConstraints(pComponent, pConstraint);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Component> getComponents()
	{
		return components;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Object getConstraints(Component pComponent)
	{
		return componentToConstraints.get(pComponent);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void notifyOfChanges()
	{
		markAsDirty();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setConstraints(Component pComponent, Object pConstraints)
	{
		componentToConstraints.put(pComponent, pConstraints);
		
		String constraint = layout.getStringConstraint(pComponent, pConstraints);
		
		getState().constraints.put(pComponent, constraint);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setLayout(ILayout pLayout)
	{
		if (layout != null)
		{
			layout.setParent(null);
		}
		
		layout = pLayout;
		
		if (layout == null)
		{
			layout = new NullLayout();
		}
		
		layout.setParent(this);
		
		getState().layoutName = layout.getName();
		
		markAsDirty();
	}
	
	/**
	 * Sets the maximum size.
	 *
	 * @param pComponent the {@link Component component}.
	 * @param pSize the {@link Size size}.
	 */
	public void setMaximumSize(Component pComponent, Size pSize)
	{
		setSize(getState().maximumSizes, pComponent, pSize);
	}
	
	/**
	 * Sets the maximum size.
	 *
	 * @param pComponent the {@link Component component}.
	 * @param pWidth the maximum width.
	 * @param pHeight the maximum height.
	 */
	public void setMaximumSize(Component pComponent, int pWidth, int pHeight)
	{
		setSize(getState().maximumSizes, pComponent, pWidth, pHeight);
	}
	
	/**
	 * Sets the minimum size.
	 *
	 * @param pComponent the {@link Component component}.
	 * @param pSize the {@link Size size}.
	 */
	public void setMinimumSize(Component pComponent, Size pSize)
	{
		setSize(getState().minimumSizes, pComponent, pSize);
	}
	
	/**
	 * Sets the minimum size.
	 *
	 * @param pComponent the {@link Component component}.
	 * @param pWidth the minimum width.
	 * @param pHeight the minimum height.
	 */
	public void setMinimumSize(Component pComponent, int pWidth, int pHeight)
	{
		setSize(getState().minimumSizes, pComponent, pWidth, pHeight);
	}
	
	/**
	 * Sets the preferred size.
	 *
	 * @param pComponent the {@link Component component}.
	 * @param pSize the {@link Size size}.
	 */
	public void setPreferredSize(Component pComponent, Size pSize)
	{
		setSize(getState().preferredSizes, pComponent, pSize);
	}
	
	/**
	 * Sets the preferred size.
	 *
	 * @param pComponent the {@link Component component}.
	 * @param pWidth the preferred width.
	 * @param pHeight the preferred height.
	 */
	public void setPreferredSize(Component pComponent, int pWidth, int pHeight)
	{
		setSize(getState().preferredSizes, pComponent, pWidth, pHeight);
	}
	
	/**
	 * Sets the size.
	 *
	 * @param pComponent the {@link Component component}.
	 * @param pSize the {@link Size size}.
	 */
	public void setSize(Component pComponent, Size pSize)
	{
		setSize(getState().sizes, pComponent, pSize);
	}
	
	/**
	 * Sets the size.
	 *
	 * @param pComponent the {@link Component component}.
	 * @param pWidth the width.
	 * @param pHeight the height.
	 */
	public void setSize(Component pComponent, int pWidth, int pHeight)
	{
		setSize(getState().sizes, pComponent, pWidth, pHeight);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getComponentCount()
	{
		return components.size();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<Component> iterator()
	{
		return components.iterator();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeComponent(Component pComponent)
	{
		if (layout != null)
		{
			layout.clear(pComponent);
		}
		
		components.remove(pComponent);
		componentToConstraints.remove(pComponent);
		
		getState().constraints.remove(pComponent);
		getState().minimumSizes.remove(pComponent);
		getState().maximumSizes.remove(pComponent);
		getState().preferredSizes.remove(pComponent);
		
		super.removeComponent(pComponent);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void replaceComponent(Component pOldComponent, Component pNewComponent)
	{
		if (pOldComponent == null || pOldComponent.getParent() != this)
		{
			return;
		}
		
		Object constraints = componentToConstraints.remove(pOldComponent);
		
		removeComponent(pOldComponent);
		addComponent(pNewComponent, constraints);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addComponent(Component pComponent)
	{
		addComponent(pComponent, -1);
	}
	
	@Override
	public void beforeClientResponse(boolean pInitial)
	{
		super.beforeClientResponse(pInitial);
		
		// TODO HACK Working around server side logic for setting 100% height.
		// There is some server side logic
		// in AbstractComponent.beforeClientResponse(boolean) which checks if it
		// makes sense that a Component has 100% set. If that check determines
		// that it does not make sense, the width will not be send to
		// the client. But this does not only affect the FormLayout itself, but
		// also its children.
		// To bypass that check we set the height to POSITIVE_INFINITY and PICAS
		// which annuls the check for us and for our children completely. For us
		// the check believes that we have an absolute size so it lets us
		// through. If our children request 100% the check sees that we have
		// an absolute size and will also let it pass. Basically a win-win
		// situation and the *least* hacky solution that I could think of.
		if (getHeight() == Float.POSITIVE_INFINITY && getHeightUnits() == Unit.PICAS)
		{
			getState().height = "100%";
		}
		
		fireBeforeClientResponseEvent();
		
		getState().layoutData = layout.getData();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected LayoutedPanelState getState()
	{
		return (LayoutedPanelState)super.getState();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected LayoutedPanelState getState(boolean markAsDirty)
	{
		return (LayoutedPanelState)super.getState(markAsDirty);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Tests if the given {@link Component} is hosted inside of a
	 * {@link LayoutedPanel}.
	 * 
	 * @param pComponent the {@link Component} to test.
	 * @return {@code true} if the given {@link Component} is hosted inside of a
	 *         {@link LayoutedPanel}. {@code false} if it is not or the given
	 *         {@link Component} was null or did not have a parent.
	 * @see Component#getParent()
	 */
	public static boolean isChildOfLayoutedPanel(Component pComponent)
	{
		if (pComponent != null && pComponent.getParent() != null)
		{
			HasComponents parent = pComponent.getParent();
			
			while (parent instanceof AbstractSplitPanel
					|| parent instanceof TabSheet)
			{
				parent = parent.getParent();
			}
			
			return parent instanceof LayoutedPanel;
		}
		
		return false;
	}
	
	/**
	 * Adds the given {@link Component} at the given index.
	 * 
	 * @param pComponent the {@link Component} to add.
	 * @param pIndex the index at which to add it, -1 for "at the end".
	 */
	public void addComponent(Component pComponent, int pIndex)
	{
		addComponent(pComponent, null, pIndex);
	}
	
	/**
	 * Gets the {@link ILayout layout}.
	 *
	 * @return the {@link ILayout layout}.
	 */
	public ILayout getLayout()
	{
		return layout;
	}
	
	/**
	 * Stores the sizes of all {@link #components}.
	 */
	public void storeComponentSizes()
	{
		for (Component component : components)
		{
			// If there is a size set, let us set it into the state to
			// preserve it, because we're going to overwrite it.
			//
			// The AbstractVaadinClientLayout (impl) will be setting
			// the components to their full size afterwards. This is needed
			// for various components which would otherwise not calculate
			// their contents on the client side correcly.
			if (component.getWidthUnits() == Unit.PIXELS
					&& component.getHeightUnits() == Unit.PIXELS)
			{
				setSize(component, (int)component.getWidth(), (int)component.getHeight());
			}
		}
	}
	
	/**
	 * Fires the {@link BeforeClientReponseEvent}.
	 */
	protected void fireBeforeClientResponseEvent()
	{
		if (cachedBeforeClientReponseEventInstance == null)
		{
			cachedBeforeClientReponseEventInstance = new BeforeClientReponseEvent(this);
		}
		
		fireEvent(cachedBeforeClientReponseEventInstance);
	}
	
	/**
	 * A simple helper method which does either set the given {@link Size}, or
	 * if it is invalid (meaning the width and height is less than zero or it is
	 * {@code null}) removes the size form the map.
	 * <p>
	 * This makes sure that not "a lot" of empty/invalid/not needed sizes are
	 * send to the server.
	 * 
	 * @param pSizeMap the {@link Map} to put into.
	 * @param pConnector the {@link Connector}.
	 * @param pSize the {@link Size}.
	 */
	protected void setSize(Map<Connector, String> pSizeMap, Connector pConnector, Size pSize)
	{
		if (pSize != null && pSize.width >= 0 && pSize.height >= 0)
		{
			pSizeMap.put(pConnector, pSize.toString());
		}
		else
		{
			pSizeMap.remove(pConnector);
		}
	}
	
	/**
	 * A simple helper method which does either set the given given, or if it is
	 * invalid (meaning the width and height is less than zero) removes the size
	 * form the map.
	 * <p>
	 * This makes sure that not "a lot" of empty/invalid/not needed sizes are
	 * send to the server.
	 * 
	 * @param pSizeMap the {@link Map} to put into.
	 * @param pConnector the {@link Connector}.
	 * @param pWidth the width.
	 * @param pHeight the height.
	 */
	protected void setSize(Map<Connector, String> pSizeMap, Connector pConnector, int pWidth, int pHeight)
	{
		if (pWidth >= 0 && pHeight >= 0)
		{
			pSizeMap.put(pConnector, Size.toString(pWidth, pHeight));
		}
		else
		{
			pSizeMap.remove(pConnector);
		}
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * The {@link BeforeClientReponseEvent} is fired after
	 * {@link com.vaadin.server.AbstractClientConnector#beforeClientResponse(boolean)}
	 * is invoked.
	 * 
	 * @author Robert Zenz
	 */
	public static class BeforeClientReponseEvent extends Event
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of {@link BeforeClientReponseEvent}.
		 *
		 * @param pSource the {@link Component source}.
		 */
		public BeforeClientReponseEvent(Component pSource)
		{
			super(pSource);
		}
		
	}	// BeforeClientReponseEvent
	
}	// LayoutedPanel
