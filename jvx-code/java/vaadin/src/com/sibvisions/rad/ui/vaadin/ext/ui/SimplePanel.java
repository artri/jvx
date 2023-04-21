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
 * 10.04.2013 - [SW] - creation
 */
package com.sibvisions.rad.ui.vaadin.ext.ui;

import java.util.Collection;

import com.sibvisions.rad.ui.vaadin.ext.ui.client.SimplePanelState;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.server.Resource;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.SingleComponentContainer;

/**
 * The <code>SimplePanel</code> class is a {@link CssLayout} which implements the {@link SingleComponentContainer} interface.
 * It's a very simple "flat" panel without additional levels.
 * 
 * @author Stefan Wurm
 * @see SingleComponentContainer
 */
public class SimplePanel extends CssLayout 
                         implements SingleComponentContainer
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
	
	/**
	 * Creates a new instance of <code>SimplePanel</code>.
	 */
	public SimplePanel()
	{
		setStyleName("v-simplepanel");
	}

	/**
	 * Creates a new instance of <code>SimplePanel</code> with caption.
	 * 
	 * @param pText the caption
	 */
	public SimplePanel(String pText)
	{
		this();

		setCaption(pText);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
	
	/**
	 * {@inheritDoc}
	 */
	public void setContent(Component content)
	{
		removeAllComponents();
		
		if (content != null)
		{
			addComponent(content);
		}
		
		markAsDirty();
	}

	/**
	 * {@inheritDoc}
	 */
	public Component getContent()
	{
		if (getComponentCount() > 0)
		{
			return getComponent(0);
		}
		
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public SimplePanelState getState()
	{
	    return (SimplePanelState)super.getState();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Sets the background image.
	 * 
	 * @param pResource the image resource
	 */
	public void setBackgroundImage(Resource pResource)
	{
	    setResource("background-image", pResource);
	}
	
	/**
	 * Gets the background image.
	 * 
	 * @return the image resource or <code>null</code> if no background image was set
	 */
	public Resource getBackgroundImage()
	{
	    return getResource("background-image");
	}
	
    /**
     * Removes all click listeners.
     */
    @SuppressWarnings("deprecation")
    public void removeAllLayoutClickListeners() 
    {
        for (LayoutClickListener listener : getLayoutClickListeners())
        {
            removeLayoutClickListener(listener);
        }
    }

    /**
     * Gets all click listeners.
     * 
     * @return all click listeners.
     */
    public LayoutClickListener[] getLayoutClickListeners() 
    {
        Collection<?> listeners = getListeners(LayoutClickEvent.class);
        return listeners.toArray(new LayoutClickListener[listeners.size()]);
    }

} 	// SimplePanel
