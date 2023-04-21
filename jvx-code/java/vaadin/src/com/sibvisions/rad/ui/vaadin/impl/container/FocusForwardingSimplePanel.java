/*
 * Copyright 2018 SIB Visions GmbH
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
 * 27.12.2018 - [RZ] - Creation
 */
package com.sibvisions.rad.ui.vaadin.impl.container;

import java.util.ArrayList;
import java.util.List;

import com.sibvisions.rad.ui.vaadin.ext.events.RegistrationContainer;
import com.sibvisions.rad.ui.vaadin.ext.ui.SimplePanel;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.BlurNotifier;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.FieldEvents.FocusNotifier;
import com.vaadin.shared.Registration;
import com.vaadin.ui.Component;

/**
 * The {@link FocusForwardingSimplePanel} is a {@link SimplePanel} extension
 * which implements the {@link FocusNotifier} and {@link BlurNotifier}
 * interfaces. When a listener is being registered, it is being stored and is
 * always attached to the current {@link #getContent()}.
 * 
 * @author Robert Zenz
 */
@SuppressWarnings("deprecation")
public class FocusForwardingSimplePanel extends SimplePanel implements FocusNotifier, BlurNotifier
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The {@link BlurListener}s. */
	protected List<BlurListener> blurListeners = null;
	
	/** The {@link FocusListener}s. */
	protected List<FocusListener> focusListeners = null;
	
	/** The {@link Registration}s on the content. */
	protected RegistrationContainer registrations = null;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link FocusForwardingSimplePanel}.
	 */
	public FocusForwardingSimplePanel()
	{
		super();
	}
	
	/**
	 * Creates a new instance of {@link FocusForwardingSimplePanel}.
	 *
	 * @param pText the {@link String text}.
	 */
	public FocusForwardingSimplePanel(String pText)
	{
		super(pText);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setContent(Component pContent)
	{
		super.setContent(pContent);
		
		updateListeners();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Registration addBlurListener(BlurListener pListener)
	{
		if (pListener == null)
		{
			return null;
		}
		
		if (blurListeners == null) 
		{
			blurListeners = new ArrayList<>();
		}
		
		blurListeners.add(pListener);
		
		updateListeners();
		
		return () -> blurListeners.remove(pListener);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Registration addFocusListener(FocusListener pListener)
	{
		if (pListener == null)
		{
			return null;
		}
		
		if (focusListeners == null) 
		{
			focusListeners = new ArrayList<>();
		}
		
		focusListeners.add(pListener);
		
		updateListeners();
		
		return () -> focusListeners.remove(pListener);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Attaches the {@link #blurListeners} to the current {@link #getContent()},
	 * if any.
	 */
	protected void attachBlurListeners()
	{
		if (blurListeners != null && !blurListeners.isEmpty())
		{
			if (registrations == null)
			{
				registrations = new RegistrationContainer();
			}
			
			if (getContent() instanceof BlurNotifier)
			{
				BlurNotifier blurNotifier = (BlurNotifier)getContent();
				
				for (BlurListener blurListener : blurListeners)
				{
					registrations.add(blurNotifier.addBlurListener(blurListener));
				}
			}
			else if (getContent() instanceof com.vaadin.v7.event.FieldEvents.BlurNotifier)
			{
				com.vaadin.v7.event.FieldEvents.BlurNotifier blurNotifier = (com.vaadin.v7.event.FieldEvents.BlurNotifier)getContent();
				
				for (BlurListener blurListener : blurListeners)
				{
					blurNotifier.addBlurListener(blurListener);
					
					registrations.add(() -> blurNotifier.removeBlurListener(blurListener));
				}
			}
			else
			{
				// Nothing to do.
			}
		}
	}
	
	/**
	 * Attaches the {@link #focusListeners} to the current
	 * {@link #getContent()}, if any.
	 */
	protected void attachFocusListeners()
	{
		if (focusListeners != null && !focusListeners.isEmpty())
		{
			if (registrations == null)
			{
				registrations = new RegistrationContainer();
			}
			
			if (getContent() instanceof FocusNotifier)
			{
				FocusNotifier focusNotifier = (FocusNotifier)getContent();
				
				for (FocusListener focusListener : focusListeners)
				{
					registrations.add(focusNotifier.addFocusListener(focusListener));
				}
			}
			else if (getContent() instanceof com.vaadin.v7.event.FieldEvents.FocusNotifier)
			{
				com.vaadin.v7.event.FieldEvents.FocusNotifier focusNotifier = (com.vaadin.v7.event.FieldEvents.FocusNotifier)getContent();
				
				for (FocusListener focusListener : focusListeners)
				{
					focusNotifier.addFocusListener(focusListener);
					
					registrations.add(() -> focusNotifier.removeFocusListener(focusListener));
				}
			}
			else
			{
				// Nothing to do.
			}
		}
	}
	
	/**
	 * Updates the listeners on the old and curretn {@link #getContent()}.
	 */
	protected void updateListeners()
	{
		if (registrations != null)
		{
			registrations.removeAll();
		}
		
		if (getContent() != null)
		{
			attachFocusListeners();
			attachBlurListeners();
		}
	}
	
}	// FocusForwardingSimplePanel
