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
 */
package com.sibvisions.rad.ui.vaadin.impl.container;

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

import com.sibvisions.rad.ui.vaadin.impl.VaadinSingleComponentContainer;
import com.vaadin.ui.SingleComponentContainer;

/**
 * The <code>AbstractVaadinWindow</code> class is the vaadin implementation of {@link IWindow}.
 * 
 * @author Benedikt Cermak
 * @param <C> an instance of {@link SingleComponentContainer}        
 */
public abstract class AbstractVaadinWindow<C extends SingleComponentContainer> extends VaadinSingleComponentContainer<C> 
															                   implements IWindow
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** EventHandler for windowOpened. */
	protected WindowHandler<IWindowOpenedListener>	    eventWindowOpened		= null;

	/** EventHandler for windowClosing. */
	protected WindowHandler<IWindowClosingListener>	    eventWindowClosing		= null;

	/** EventHandler for windowClosed. */
	protected WindowHandler<IWindowClosedListener>	    eventWindowClosed		= null;

	/** EventHandler for windowActivated. */
	protected WindowHandler<IWindowActivatedListener>	eventWindowActivated	= null;

	/** EventHandler for windowDeactivated. */
	protected WindowHandler<IWindowDeactivatedListener>	eventWindowDeactivated	= null;

	/** EventHandler for windowIconified. */
	protected WindowHandler<IWindowIconifiedListener>	eventWindowIconified	= null;

	/** EventHandler for windowDeiconified. */
	protected WindowHandler<IWindowDeiconifiedListener>	eventWindowDeiconified	= null;

	/** the translation mapping. */
	private TranslationMap translation;
	
	/** Active Flag. */
	private boolean			active					= false;
	
	/** whether this frame is disposed. */
	private boolean 		disposed				= false;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of <code>AbstractVaadinWindow</code>.
	 * 
	 * @param pContainer an instance of a {@link com.vaadin.ui.ComponentContainer}
	 */
	protected AbstractVaadinWindow(C pContainer)
	{
		super(pContainer);
		
		setVisible(false);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public void pack()
	{
		//Not supported
	}

	/**
	 * {@inheritDoc}
	 */
	public void dispose()
	{
		disposed = true;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isDisposed()
	{
		return disposed;
	}

	/**
	 * {@inheritDoc}
	 */
	public void toBack()
	{
		//Not suppported
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
	public boolean isActive()
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
		}
		return eventWindowDeactivated;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Sets window active flag.
	 * 
	 * @param pActive
	 *            active flag
	 */
	public final void setActive(boolean pActive)
	{
		if (active != pActive)
		{
			active = pActive;

			if (active)
			{
				if (eventWindowActivated != null)
				{
					getFactory().synchronizedDispatchEvent(eventWindowActivated, new UIWindowEvent(eventSource, UIWindowEvent.WINDOW_ACTIVATED, 
																		 					       System.currentTimeMillis(), 0));	
				}
			}
			else
			{
				if (eventWindowDeactivated != null)
				{
					getFactory().synchronizedDispatchEvent(eventWindowDeactivated, new UIWindowEvent(eventSource, UIWindowEvent.WINDOW_DEACTIVATED, 
																		   					         System.currentTimeMillis(), 0));
				}
			}
		}
	}
	
	/**
	 * Sends the window closing event.
	 */
	public void performWindowClosing()
	{
		if (eventWindowClosing != null)
		{
			getFactory().synchronizedDispatchEvent(eventWindowClosing, new UIWindowEvent(eventSource, UIWindowEvent.WINDOW_CLOSING,
										  										         System.currentTimeMillis(), 0));
		}		
	}

} 	// AbstractVaadinWindow
