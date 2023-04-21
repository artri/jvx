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
 * 17.01.2023 - [HM] - creation
 */
package com.sibvisions.rad.ui.vaadin.ext.ui;

import java.util.ArrayList;
import java.util.Iterator;

import com.vaadin.server.Resource;
import com.vaadin.shared.Registration;
import com.vaadin.ui.AbstractSingleComponentContainer;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Upload;

/**
 * The <code>MouseOverButton</code> class is the server-side implementation of an upload button that 
 * handles mouse over events.
 * 
 * @author Martin Handsteiner
 */
public class MouseOverUpload extends Upload implements ComponentContainer
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /** Components with mouse over button. **/
    protected ArrayList<Component> components = new ArrayList<Component>();
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
	
	/**
	 * Creates a new instance of <code>MouseOverUpload</code>.
	 */
	public MouseOverUpload() 
	{
		addComponent(new MouseOverButton());
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    @Override
	public String getButtonCaption()
	{
	    return getSubmitButton().getCaption();
	}
	
    /**
     * {@inheritDoc}
     */
    @Override
	public void setButtonCaption(String pCaption)
	{
        getSubmitButton().setCaption(pCaption);
	}
	
    /**
     * {@inheritDoc}
     */
    @Override
    public Resource getIcon()
    {
        return getSubmitButton().getIcon();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setIcon(Resource pResource)
    {
        getSubmitButton().setIcon(pResource);
    }
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    @Override
    public int getComponentCount()
    {
        return 1;
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
    public Registration addComponentAttachListener(ComponentAttachListener pListener) 
    {
        return addListener(ComponentAttachEvent.class, pListener, ComponentAttachListener.attachMethod);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Deprecated
    public void removeComponentAttachListener(ComponentAttachListener pListener) 
    {
        removeListener(ComponentAttachEvent.class, pListener, ComponentAttachListener.attachMethod);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Registration addComponentDetachListener(ComponentDetachListener pListener) 
    {
        return addListener(ComponentDetachEvent.class, pListener, ComponentDetachListener.detachMethod);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Deprecated
    public void removeComponentDetachListener(ComponentDetachListener pListener) 
    {
        removeListener(ComponentDetachEvent.class, pListener, ComponentDetachListener.detachMethod);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addComponent(Component pComponent)
    {
        if (isOrHasAncestor(pComponent)) 
        {
            throw new IllegalArgumentException("Component cannot be added inside it's own content");
        }

        if (pComponent.getParent() != null) 
        {
            // If the component already has a parent, try to remove it
            AbstractSingleComponentContainer.removeFromParent(pComponent);
        }

        pComponent.setParent(this);
        fireComponentAttachEvent(pComponent);
        markAsDirty();
        
        components.add(pComponent);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addComponents(Component... pComponents)
    {
        for (Component c : pComponents) 
        {
            addComponent(c);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeComponent(Component pComponent)
    {
        if (equals(pComponent.getParent())) 
        {
            pComponent.setParent(null);
            fireComponentDetachEvent(pComponent);
            markAsDirty();
            
            components.remove(pComponent);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeAllComponents()
    {
        ArrayList<Component> comps = new ArrayList<Component>();
        Iterator<Component> it = iterator();

        while (it.hasNext())
        {
            comps.add(it.next());
        }

        for (Component comp : comps) 
        {
            removeComponent(comp);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void replaceComponent(Component oldComponent, Component newComponent)
    {
        // not supported
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<Component> getComponentIterator()
    {
        return iterator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void moveComponentsFrom(ComponentContainer source)
    {
        ArrayList<Component> comps = new ArrayList<Component>();
        Iterator<Component> it = iterator();

        while (it.hasNext())
        {
            comps.add(it.next());
        }

        for (Component comp : comps) 
        {
            source.removeComponent(comp);
            addComponent(comp);
        }
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Fires the component attached event. This should be called by the
     * addComponent methods after the component have been added to this
     * container.
     *
     * @param component
     *            the component that has been added to this container.
     */
    protected void fireComponentAttachEvent(Component component) 
    {
        fireEvent(new ComponentAttachEvent(this, component));
    }

    /**
     * Fires the component detached event. This should be called by the
     * removeComponent methods after the component have been removed from this
     * container.
     *
     * @param component
     *            the component that has been removed from this container.
     */
    protected void fireComponentDetachEvent(Component component) 
    {
        fireEvent(new ComponentDetachEvent(this, component));
    }

    /**
     * Gets the internal used <code>MouseOverButton</code>.
     * 
     * @return the internal used <code>MouseOverButton</code>
     */
    public MouseOverButton getSubmitButton()
    {
        if (components.size() > 0)
        {
            return (MouseOverButton)components.get(0);
        }
        else
        {
            return null;
        }
    }
    
}	// MouseOverButton
