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
 * 16.11.2008 - [HM] - creation
 * 23.07.2009 - [JR] - checkAdd: allow sub toolbar
 * 04.10.2011 - [JR] - #477: beforeAddNotify handling    
 * 22.10.2011 - [JR] - setZOrder: used ((UIContainer)this) because jdk 1.5 <= 06 can not compile the code!
 * 13.08.2013 - [JR] - #756: changed add order                 
 */
package javax.rad.genui;

import java.util.ArrayList;
import java.util.List;

import javax.rad.genui.container.UITabsetPanel;
import javax.rad.ui.IComponent;
import javax.rad.ui.IContainer;
import javax.rad.ui.IImage;
import javax.rad.ui.IInsets;
import javax.rad.ui.ILayout;
import javax.rad.ui.IRectangle;
import javax.rad.ui.container.IToolBar;
import javax.rad.ui.menu.IMenuBar;

/**
 * Platform and technology independent Container.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF,... .
 * 
 * @author Martin Handsteiner
 * 
 * @param <C> instance of IContainer
 */
public abstract class UIContainer<C extends IContainer> extends UIComponent<C> 
                                                        implements IContainer
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The UIParent of this UIComponent. */
	private transient ILayout uiLayout = null;
	
	/** List of subcomponents. */
	protected transient List<UIComponent<?>> components = new ArrayList<UIComponent<?>>(4);
	
	/** List of subcomponents. */
	protected transient List<UIComponent<?>> allComponents = new ArrayList<UIComponent<?>>(4);
	
	/** the flag indicates that addNotify is active. */
	private transient boolean bAddNotify = false;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>UIContainer</code>.
     *
     * @param pContainer the Container.
     * @see IContainer
     */
	protected UIContainer(C pContainer)
	{
		super(pContainer);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface Implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public ILayout getLayout()
    {
    	return uiLayout;
    }

	/**
	 * {@inheritDoc}
	 */
	public void setLayout(ILayout pLayout)
    {
		if (getComponentCount() > 0)
		{
			if (pLayout != uiLayout)
			{
				//don't throw an exception to be "compatible" for some updates
				error("Can't change layout if components have already been added!");
				
				//throw new IllegalStateException("Can't change layout if components have already been added!");
			}
		}
		
        if (isNotified() && uiLayout instanceof UILayout)
        {
            // inform the uiLayout, that this container is removed from ui.
            ((UILayout)uiLayout).removeNotify(this);
        }

        // ensure that the factory gets his own resource, to prevent exception on factory internal casts!
		ILayout layout = pLayout;
		while (layout instanceof UILayout)
		{
			layout = (ILayout)((UILayout)pLayout).uiResource;
		}
    	uiResource.setLayout(layout);

		uiLayout = pLayout;
		
        if (isNotified() && uiLayout instanceof UILayout)
        {
            // inform the uiLayout, that this container is added to ui.
            ((UILayout)uiLayout).addNotify(this);
        }
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
		
		addInternal(uiResource, pIndex, (UIComponent<?>)pComponent, pConstraints, pIndex);
    }

	/**
	 * {@inheritDoc}
	 */
	public void remove(int pIndex)
    {
		removeInternal(components.get(pIndex));
    }

	/**
	 * {@inheritDoc}
	 */
	public void remove(IComponent pComponent)
	{
		int index = components.indexOf(pComponent); // For compatibility reasons, it has to call remove(int pIndex).
		
		if (index >= 0)
		{
			remove(index);
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
	public UIComponent<?> getComponent(int pIndex)
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
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateTranslation()
	{
		super.updateTranslation();
		
		//updateTranslation will be called through addNotify -> don't call it more than once!
		if (bAddNotify)
		{
			return;
		}
		
		//Update the translation for all sub components
		for (UIComponent<?> comp : allComponents.toArray(new UIComponent[allComponents.size()]))
		{
			comp.updateTranslation();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void beforeAddNotify(IComponent pParent)
	{
		if (bAddNotify)
		{
			return;
		}
		
		bAddNotify = true;
		
		try
		{
			super.beforeAddNotify(pParent);
		}
		finally
		{
			bAddNotify = false;
		}
    		
        IComponent originalParentExpected = parentExpected;
        parentExpected = pParent;
        try
        {
    		for (UIComponent<?> comp : allComponents.toArray(new UIComponent[allComponents.size()]))
    		{
    			if (!comp.isBeforeNotified())
    			{
    			    comp.parentExpected = this;
    			    try
    			    {
    			        comp.beforeAddNotify(this);
    			    }
    			    finally
    			    {
    			        comp.parentExpected = null;
    			    }
    		            
    			    if (comp.getUIComponent() != comp)
    			    {
    			        comp.getUIComponent().parentExpected = this;
    			        try
    			        {
    			            comp.getUIComponent().beforeAddNotify(this);
    			        }
    			        finally
    			        {
    			            comp.getUIComponent().parentExpected = null;
    			        }
    			    }
    			}
    		}
        }
        finally
        {
            parentExpected = originalParentExpected;
        }
	}	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addNotify()
	{
		if (bAddNotify)
		{
			return;
		}
		
		bAddNotify = true;
		
		try
		{
			super.addNotify();
		}
		finally
		{
			bAddNotify = false;
		}
		
		for (UIComponent<?> comp : allComponents.toArray(new UIComponent[allComponents.size()]))
		{
			if (!comp.isNotified())
			{
			    comp.addNotify();
	            
	            if (comp.getUIComponent() != comp)
	            {
	                comp.getUIComponent().addNotify();
	            }
			}
		}
        if (uiLayout instanceof UILayout)
        {
            // inform the uiLayout, that this container is added to ui.
            ((UILayout)uiLayout).addNotify(this);
        }
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeNotify()
	{
		if (isNotified() && uiLayout instanceof UILayout)
		{
			// inform the uiLayout, that this container is removed from ui.
			((UILayout)uiLayout).removeNotify(this);
		}
		
		super.removeNotify();

		for (UIComponent<?> comp : allComponents.toArray(new UIComponent[allComponents.size()]))
		{
			if (comp.isNotified())
			{
			    comp.removeNotify();

                if (comp.getUIComponent() != comp)
                {
                    comp.getUIComponent().removeNotify();
                }
			}
		}
	}

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setUIResourceEnabled(boolean pEnable)
    {
        super.setUIResourceEnabled(pEnable);
        
        for (IComponent component : getComponents())
        {
            if (component instanceof UIComponent)
            {
                ((UIComponent)component).setUIResourceEnabled(pEnable && component.isEnabled());
            }
        }
    }
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * This informs the layout, that the visiblilty of an added component is changed.
     * @param pComponent the component
     */
    public void componentChanged(UIComponent pComponent)
    {
    	if (uiLayout instanceof UILayout)
    	{
    		((UILayout)uiLayout).componentChanged(this, pComponent);
    	}
    }
    
	/**
	 * Gets the insets due to different componentUIResource and uiResource.
	 * 
	 * @return the insets.
	 */
	public IInsets getInsets()
	{
		IComponent componentUIResource = getComponentUIResource();
		
		IComponent tempUIResource = getUIResource();
		
		if (componentUIResource != tempUIResource && tempUIResource != null)
		{
			IRectangle bounds = tempUIResource.getBounds();
			
			int x = bounds.getX();
			int y = bounds.getY();
			int width = bounds.getWidth();
			int height = bounds.getHeight();
			
			tempUIResource = tempUIResource.getParent();
				
			while (tempUIResource != componentUIResource && tempUIResource != null)
			{
				bounds = tempUIResource.getBounds();
				x += bounds.getX();
				y += bounds.getY();
				
				tempUIResource = tempUIResource.getParent();
			}
			
			if (tempUIResource != null)
			{
				bounds = tempUIResource.getBounds();
				return uiResource.getFactory().createInsets(y, x, bounds.getHeight() - height - y, bounds.getWidth() - width - x);
			}
		}
		
		return null;
	}
	
	/**
	 * Gets the added {@link IComponent}s as array.
	 * <p>
	 * The returned array is a copy, changing the array will have no effect
	 * on the {@link UIContainer}. The returned array might be empty, but never
	 * {@code null}.
	 * 
	 * @return the added {@link IComponent}s as array.
	 * @since 2.5
	 */
	public IComponent[] getComponents()
	{
		return components.toArray(new IComponent[components.size()]);
	}
	
	/** 
	 * Gets a new UIResource. The ui resource has to be a child of component ui resource.
	 * @return the new ui resource.
	 */
	protected UIContainer<C> getUIResourceContainer()
	{
		return (UIContainer<C>)uiResource.getEventSource();
	}
	
	/** 
	 * Sets a new UIResource. The ui resource has to be a child of component ui resource.
	 * @param pUIResourceContainer the new ui resource.
	 */
	protected void setUIResourceContainer(UIContainer<C> pUIResourceContainer)
	{
	    UIComponent<?> root = getUIComponent();
	    
		IContainer parent = pUIResourceContainer;
		while (parent != null && parent != root)
		{
			parent = parent.getParent();
		}
		if (parent == null)
		{
			throw new IllegalArgumentException("The ui resource container has to be a child!");
		}
		
		components.clear();
		
		uiResource = pUIResourceContainer.getUIResource();
		
		ILayout layout = pUIResourceContainer.getLayout();
		
		setLayout(layout);
		
		while (pUIResourceContainer.getComponentCount() > 0)
		{
			UIComponent<?> comp = pUIResourceContainer.getComponent(0);
			add(comp, layout.getConstraints(comp));
		}
	}
	
	/**
	 * Gets true, if the allComponents contains the given component.
	 * @param pComponent the component to check.
	 * @return true, if the allComponents contains the given component.
	 */
	protected boolean contains(UIComponent<?> pComponent)
	{
		return allComponents.contains(pComponent);
	}
	
	/**
	 * Checks if it's allowed to add a specific component to this container.
	 * 
	 * @param pComponent the component to be added
	 * @param pConstraints an object expressing layout constraints
	 * @param pIndex the position in the container's list at which to insert the IComponent; -1 means insert at the end component
	 */
	protected void checkAdd(IComponent pComponent, Object pConstraints, int pIndex)
	{
	    if (pComponent == null)
	    {
	        throw new IllegalArgumentException("Component can't be null!");
	    }
	    
		if (!(pComponent instanceof UIComponent))
		{
			throw new IllegalArgumentException("Only UIComponents may be added to UIContainer!"); 
		}
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
	 * Internal components are stored in a separate list to ensure translation works also for
	 * this components.
	 * @param pParentUIResource the parent ui resource where to add the component
	 * @param pParentUIResourceIndex the index where to add the component.getUIResource inside the parent ui resource
	 * @param pComponent the component to add
	 * @param pConstraints the constraints
	 * @param pIndex the index where the component is added to this container.
	 */
	protected void addInternal(IContainer pParentUIResource, int pParentUIResourceIndex, UIComponent pComponent, Object pConstraints, int pIndex)
    {
		IContainer parent = pComponent.getParent();
		// Remove from UIContainer or from IContainer
		if (parent != null)
		{
			if (parent == this && pIndex > 0) // if component is already added to this container and will be readded behind, the index will be wrong after remove
			{
				int index = parent.indexOf(pComponent);
				if (index >= 0 && index < pIndex)
				{
					pIndex--;
				}
			}
			if (pParentUIResource != null && pParentUIResource == pComponent.getComponentUIResource().getParent() && pParentUIResourceIndex > 0)
			{
				int index = pParentUIResource.indexOf(pComponent.getComponentUIResource());
				
				if (index >= 0 && index < pParentUIResourceIndex)
				{
					pParentUIResourceIndex--;
				}
			}
			
			parent.remove(pComponent);
		}
		
		IComponent component = pComponent.getComponentUIResource();
		// Make sure that UIComponents are not handed to the technology implementation.
		// This is to support custom components which are wrapping another UIComponent
		// without the need for the technology implementation to be aware of this.
		while (component instanceof UIComponent<?>)
		{
			component = ((UIComponent<?>)component).getComponentUIResource();
		}

		Object constraints = pConstraints;
		while (constraints instanceof UIResource<?>)
		{
			constraints = ((UIResource<?>)constraints).getUIResource();
		}

		//send beforeAddNotify if the container is already added
		if (isNotified() && !pComponent.isBeforeNotified())
		{
		    pComponent.parentExpected = this;
		    try
		    {
		        pComponent.beforeAddNotify(this);
		    }
		    finally
		    {
	            pComponent.parentExpected = null;
		    }
			
			if (pComponent.getUIComponent() != pComponent)
			{
			    pComponent.getUIComponent().parentExpected = this;
			    try
			    {
			        pComponent.getUIComponent().beforeAddNotify(this);
			    }
			    finally
			    {
			        pComponent.getUIComponent().parentExpected = null;
			    }
			}
		}
		
		//add component to internal list before add to technology, because event handling for events from the underlying technology
		//should work with our component tree as well (#756)
		allComponents.add(pComponent);
		
		pComponent.setParent(this);
		
		if (pParentUIResource != null)
		{
			if (pIndex < 0)
			{
				components.add(pComponent);
			}
			else
			{
				components.add(pIndex, pComponent);
			}

			try
			{
				if (pParentUIResourceIndex > pParentUIResource.getComponentCount())
				{
					pParentUIResourceIndex = pParentUIResource.getComponentCount();
				}
				
				pParentUIResource.add(component, constraints, pParentUIResourceIndex);

		        if (uiLayout instanceof UILayout)
		        {
		            // inform the uiLayout, that there is a new component with constraints.
		            // This is now done after the component is notified.
		            // uiLayout.getConstraints(pComponent); // use setConstraints for now, to have a chance to get the original constraint from genUI. 
		            uiLayout.setConstraints(pComponent, pConstraints);
		        }
			}
			catch (RuntimeException re)
			{
				allComponents.remove(pComponent);
				components.remove(pComponent);
	
				pComponent.setParent(null);
				
				throw re;
			}
			catch (Error e)
			{
				allComponents.remove(pComponent);
				components.remove(pComponent);
				
				pComponent.setParent(null);
				
				throw e;
			}
		}
		if (pComponent.getUIComponent() != pComponent)
		{
			allComponents.add(pComponent.getUIComponent());
			
			pComponent.getUIComponent().setParent(this);
		}
		
		//send addNotify if the container is already added
		if (isNotified() && !pComponent.isNotified())
		{
			pComponent.addNotify();
			
			if (pComponent.getUIComponent() != pComponent)
			{
				pComponent.getUIComponent().addNotify();
			}
	        if (pParentUIResource != null)
	        {
	            componentChanged(pComponent);
	        }
		}
    }

	/**
	 * Internal components are stored in a separate list to ensure translation works also for
	 * this components.
	 * @param pComponent the component to add
	 */
	protected void removeInternal(UIComponent pComponent)
    {
		if (pComponent.getParent() == this)
		{
			int index = components.indexOf(pComponent);
			if (index >= 0)
			{
				IComponent componentUIResource = pComponent.getComponentUIResource();

				while (componentUIResource instanceof UIComponent<?>)
				{
					componentUIResource = ((UIComponent<?>)componentUIResource).getComponentUIResource();
				}
			
				uiResource.remove(componentUIResource);
				
				components.remove(index);

				if (uiLayout instanceof UILayout)
				{
					// clear the constraints from uiLayout when the component is removed.
					uiLayout.setConstraints(pComponent, null);
				}
			}
			
			if (pComponent.isNotified())
			{
				pComponent.removeNotify();

				if (pComponent.getUIComponent() != pComponent)
				{
					pComponent.getUIComponent().removeNotify();
				}
	            if (index >= 0)
	            {
	                componentChanged(pComponent);
	            }
			}

			allComponents.remove(pComponent);
			
			pComponent.setParent(null);
			
			if (pComponent.getUIComponent() != pComponent)
			{
				allComponents.remove(pComponent.getUIComponent());
				
				pComponent.getUIComponent().setParent(null);
			}
		}
    }

	/**
	 * Sets the order of the given component.
	 * @param pComponent the component
	 * @param pZOrder the zOrder.
	 */
	public void setZOrder(UIComponent pComponent, int pZOrder)
	{
		int index = indexOf(pComponent);
		if (index >= 0)
		{
			Object constraints = null;
			IImage image = null;
			if (getLayout() != null)
			{
				constraints = getLayout().getConstraints(pComponent);
			}
			else if (this instanceof UITabsetPanel)
			{
				constraints = ((UITabsetPanel)this).getTextAt(index);
				image = ((UITabsetPanel)this).getIconAt(index);
			}
			
			uiResource.remove(pComponent.getComponentUIResource());
			components.remove(index);
			if (pZOrder > components.size())
			{
				pZOrder = components.size();
			}
			components.add(pZOrder, pComponent);

			uiResource.add(pComponent.getComponentUIResource(), constraints, pZOrder);
			
			if (image != null)
			{
				((UITabsetPanel)this).setIconAt(index, image);
			}
		}
	}
	
}	// UIContainer
