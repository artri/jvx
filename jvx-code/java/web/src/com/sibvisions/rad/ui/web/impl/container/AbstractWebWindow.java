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
 * 20.11.2009 - [HM] - creation
 */
package com.sibvisions.rad.ui.web.impl.container;

import javax.rad.ui.IComponent;
import javax.rad.ui.container.IWindow;
import javax.rad.ui.event.UIWindowEvent;
import javax.rad.ui.event.WindowHandler;
import javax.rad.ui.event.type.window.IWindowActivatedListener;
import javax.rad.ui.event.type.window.IWindowClosedListener;
import javax.rad.ui.event.type.window.IWindowClosingListener;
import javax.rad.ui.event.type.window.IWindowDeactivatedListener;
import javax.rad.ui.event.type.window.IWindowDeiconifiedListener;
import javax.rad.ui.event.type.window.IWindowIconifiedListener;
import javax.rad.ui.event.type.window.IWindowOpenedListener;
import javax.rad.util.TranslationMap;

/**
 * Web server implementation of {@link IWindow}.
 * 
 * @author Martin Handsteiner
 */
public abstract class AbstractWebWindow extends WebToolBarPanel
										implements IWindow
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** EventHandler for windowOpened. */
	protected WindowHandler<IWindowOpenedListener> eventWindowOpened = null;
	/** EventHandler for windowClosing. */
	protected WindowHandler<IWindowClosingListener> eventWindowClosing = null;
	/** EventHandler for windowClosed. */
	protected WindowHandler<IWindowClosedListener> eventWindowClosed = null;
	/** EventHandler for windowActivated. */
	protected WindowHandler<IWindowActivatedListener> eventWindowActivated = null;
	/** EventHandler for windowDeactivated. */
	protected WindowHandler<IWindowDeactivatedListener> eventWindowDeactivated = null;
	/** EventHandler for windowIconified. */
	protected WindowHandler<IWindowIconifiedListener> eventWindowIconified = null;
	/** EventHandler for windowDeiconified. */
	protected WindowHandler<IWindowDeiconifiedListener> eventWindowDeiconified = null;

	/** the translation mapping. */
	private TranslationMap translation;
	
	/** Active Flag. */
	private boolean active = false;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>AbstractWebWindow</code>.
     *
     * @see javax.rad.ui.container.IWindow
     */
	protected AbstractWebWindow()
	{
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public void centerRelativeTo(IComponent pComponent)
	{
		setProperty("centerRelativeTo", pComponent);
	}
	
    /**
     * {@inheritDoc}
     */
    public void setTranslation(TranslationMap pTranslation)
    {
        translation = pTranslation;
    }
    
    /**
     * {@inheritDoc}
     */
    public TranslationMap getTranslation()
    {
        return translation;
    }
	
	/**
	 * {@inheritDoc}
	 */
	public final boolean isActive()
    {
    	return active;
    }
	
	/**
	 * {@inheritDoc}
	 */
    public WindowHandler<IWindowOpenedListener> eventWindowOpened()
    {
		if (eventWindowOpened == null)
		{
			eventWindowOpened = new WindowHandler<IWindowOpenedListener>(IWindowOpenedListener.class);
			
			setProperty("windowOpened", eventWindowOpened);
		}
		return eventWindowOpened;
    }
    
	/**
	 * {@inheritDoc}
	 */
    public WindowHandler<IWindowClosingListener> eventWindowClosing()
    {
		if (eventWindowClosing == null)
		{
			eventWindowClosing = new WindowHandler<IWindowClosingListener>(IWindowClosingListener.class);
			
			setProperty("windowClosing", eventWindowClosing);
		}
		return eventWindowClosing;
    }

    /**
	 * {@inheritDoc}
	 */
    public WindowHandler<IWindowClosedListener> eventWindowClosed()
    {
		if (eventWindowClosed == null)
		{
			eventWindowClosed = new WindowHandler<IWindowClosedListener>(IWindowClosedListener.class);
			
			setProperty("windowClosed", eventWindowClosed);
		}
		return eventWindowClosed;
    }
    
    /**
	 * {@inheritDoc}
	 */
    public WindowHandler<IWindowIconifiedListener> eventWindowIconified()
    {
		if (eventWindowIconified == null)
		{
			eventWindowIconified = new WindowHandler<IWindowIconifiedListener>(IWindowIconifiedListener.class);
			
			setProperty("windowIconified", eventWindowIconified);
		}
		return eventWindowIconified;
    }

    /**
	 * {@inheritDoc}
	 */
    public WindowHandler<IWindowDeiconifiedListener> eventWindowDeiconified()
    {
		if (eventWindowDeiconified == null)
		{
			eventWindowDeiconified = new WindowHandler<IWindowDeiconifiedListener>(IWindowDeiconifiedListener.class);
			
			setProperty("windowDeiconified", eventWindowDeiconified);
		}
		return eventWindowDeiconified;
    }

    /**
	 * {@inheritDoc}
	 */
    public WindowHandler<IWindowActivatedListener> eventWindowActivated()
    {
		if (eventWindowActivated == null)
		{
			eventWindowActivated = new WindowHandler<IWindowActivatedListener>(IWindowActivatedListener.class);
			
			setProperty("windowActivated", eventWindowActivated);
		}
		return eventWindowActivated;
    }

    /**
	 * {@inheritDoc}
	 */
    public WindowHandler<IWindowDeactivatedListener> eventWindowDeactivated()
    {
		if (eventWindowDeactivated == null)
		{
			eventWindowDeactivated = new WindowHandler<IWindowDeactivatedListener>(IWindowDeactivatedListener.class);
			
			setProperty("windowDeactivated", eventWindowDeactivated);
		}
		return eventWindowDeactivated;
    }

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
	 * Sets window active flag.
	 * 
	 * @param pActive active flag
	 */
	public final void setActive(boolean pActive)
    {
		if (active != pActive)
		{
	    	active = pActive;
	    	
			if (active)
			{
				if (eventWindowActivated != null && eventWindowActivated.isDispatchable())
				{
					getFactory().synchronizedDispatchEvent(eventWindowActivated, new UIWindowEvent(getEventSource(), 
							                                                                       UIWindowEvent.WINDOW_ACTIVATED, 
							                                                                       System.currentTimeMillis(), 0));
				}
			}
			else
			{
				if (eventWindowDeactivated != null && eventWindowDeactivated.isDispatchable())
				{
					getFactory().synchronizedDispatchEvent(eventWindowDeactivated, new UIWindowEvent(getEventSource(), 
							                                                                         UIWindowEvent.WINDOW_DEACTIVATED, 
							                                                                         System.currentTimeMillis(), 0));
				}
			}
		}
    }

	
    /**
     * Dispatches the window opened event.
     */
	public void windowOpened()
	{
		if (eventWindowOpened != null && eventWindowOpened.isDispatchable())
		{
			getFactory().synchronizedDispatchEvent(eventWindowOpened, new UIWindowEvent(getEventSource(), 
					                                                                    UIWindowEvent.WINDOW_OPENED, 
					                                                                    System.currentTimeMillis(), 0));
		}
	}

    /**
     * Dispatches the window closing event.
     */
	public void windowClosing()
	{
		if (eventWindowClosing != null && eventWindowClosing.isDispatchable())
		{
			getFactory().synchronizedDispatchEvent(eventWindowClosing, new UIWindowEvent(getEventSource(), UIWindowEvent.WINDOW_CLOSING, 
					                                                                     System.currentTimeMillis(), 0));
		}
	}

    /**
     * Dispatches the window closing event.
     */
	public void windowClosed()
	{
		if (eventWindowClosed != null && eventWindowClosed.isDispatchable())
		{
			getFactory().synchronizedDispatchEvent(eventWindowClosed, new UIWindowEvent(getEventSource(), UIWindowEvent.WINDOW_CLOSED, 
					                                                                     System.currentTimeMillis(), 0));
		}
	}
    
}	// AbstractWebWindow
