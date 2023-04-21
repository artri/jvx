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
 * 17.10.2012 - [CB] - creation
 * 24.09.2013 - [JR] - extends VaadinComponentBase
 */
package com.sibvisions.rad.ui.vaadin.impl;

import javax.rad.ui.IAlignmentConstants;
import javax.rad.ui.IComponent;
import javax.rad.ui.event.FocusHandler;
import javax.rad.ui.event.UIFocusEvent;

import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.BlurNotifier;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.FieldEvents.FocusNotifier;
import com.vaadin.ui.Component;

/**
 * The <code>VaadinComponent</code> is the default component with fixed component resource type.
 * 
 * @author René Jahn
 * @param <C> an instance of {@link Component}
 */
public class VaadinComponent<C extends Component> extends VaadinComponentBase<C, C>                   
                                                  implements IComponent, 
                                                             IAlignmentConstants,
                                                             FocusListener,
                                                             BlurListener
                                                            
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>VaadinComponent</code>.
	 * 
	 * @param pComponent an instance of {@link Component}.
	 * @see IComponent
	 */
	protected VaadinComponent(C pComponent)
	{
		super(pComponent);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	// FocusListener
	
	/**
	 * {@inheritDoc}
	 */
	public void focus(FocusEvent event) 
	{
		if (eventFocusGained != null)
		{
			dispatchFocusEvent(eventFocusGained, UIFocusEvent.FOCUS_GAINED);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void blur(BlurEvent event) 
	{
		if (eventFocusLost != null)
		{
			dispatchFocusEvent(eventFocusLost, UIFocusEvent.FOCUS_LOST);
		}
	}

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("deprecation")
    @Override
	protected void addFocusListener()
	{
		if (resource instanceof FocusNotifier)
		{
			((FocusNotifier)resource).addFocusListener(this);
		}
		else if (resource instanceof com.vaadin.v7.event.FieldEvents.FocusNotifier)
		{
			((com.vaadin.v7.event.FieldEvents.FocusNotifier)resource).addFocusListener(this);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("deprecation")
    @Override
	protected void addBlurListener()
	{
		if (resource instanceof BlurNotifier)
		{
			((BlurNotifier)resource).addBlurListener(this);
		}
		else if (resource instanceof com.vaadin.v7.event.FieldEvents.BlurNotifier)
		{
			((com.vaadin.v7.event.FieldEvents.BlurNotifier)resource).addBlurListener(this);
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Dispatches a focus event.
     * 
     * @param pHandler the mouse handler
     * @param pId the event id
     */
    protected void dispatchFocusEvent(FocusHandler pHandler, int pId)
    {
        if (pHandler != null)
        {
            getFactory().synchronizedDispatchEvent(pHandler, new UIFocusEvent(eventSource,  pId, System.currentTimeMillis(), 0));
        }
    }
    
    /**
     * Gets whether the given text is html formatted.
     * 
     * @param pText the text
     * @return <code>true</code> if text is html formatted, <code>false</code> otherwise
     */
    public static boolean isHtml(String pText)
    {
    	return pText != null && pText.length() >= 6 && "<html>".equalsIgnoreCase(pText.substring(0, 6).toLowerCase());
    }
	
} 	// VaadinComponent
