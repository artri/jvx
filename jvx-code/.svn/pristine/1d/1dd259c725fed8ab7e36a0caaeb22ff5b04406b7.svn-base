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

import javax.rad.ui.IComponent;
import javax.rad.ui.IRectangle;
import javax.rad.ui.event.UIComponentEvent;
import javax.rad.ui.event.UIWindowEvent;
import javax.rad.ui.event.WindowHandler;

import com.sibvisions.rad.ui.vaadin.ext.VaadinUtil;
import com.sibvisions.rad.ui.vaadin.impl.celleditor.ShortcutHandler;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;
import com.vaadin.ui.Window.ResizeEvent;
import com.vaadin.ui.Window.ResizeListener;

/**
 * The <code>VaadinFrame</code> class enables close events for frames.
 *
 * @param <C> an instance of {@link Window}
 * @author Benedikt Cermak
 */
public class VaadinFrame<C extends Window> extends AbstractVaadinFrame<C>
						 implements CloseListener, 
						            ResizeListener, 
						            FocusListener, 
						            BlurListener
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /**
     * Creates a new instance of <code>VaadinFrame</code>.
     */
	public VaadinFrame()
	{
		this(new Window());
	}

	/**
	 * Creates a new instance of <code>VaadinFrame</code> with given a pre-configured window.
	 * 
	 * @param pWindow the window
	 */
    protected VaadinFrame(Window pWindow)
    {
        super((C)pWindow);
        
        resource.setResizeLazy(true);
        
        resource.addActionHandler(new ShortcutHandler());
        resource.addResizeListener(this);
    }
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public String getTitle()
    {
		return resource.getCaption();
    }

	/**
	 * {@inheritDoc}
	 */
	public void setTitle(String pTitle)
    {
		resource.setCaption(pTitle);
    }	
	
	/**
	 * {@inheritDoc}
	 */
	public void toFront()
	{
	    try
	    {
	        resource.bringToFront();
	    }
	    catch (IllegalStateException ise)
	    {
	        if (resource.getUI() == null)
	        {
	            throw ise;
	        }
	    }
	}	
	
	/**
	 * {@inheritDoc}
	 */
	public void centerRelativeTo(IComponent pComponent)
	{
		resource.center();		
	}	
	
	// Listener
	
	/**
	 * {@inheritDoc}
	 */
	public void windowClose(CloseEvent pEvent)
	{
		if (eventWindowClosed != null)
		{
			getFactory().synchronizedDispatchEvent(eventWindowClosed, new UIWindowEvent(eventSource, UIWindowEvent.WINDOW_CLOSED,
  										    									  		System.currentTimeMillis(), 0));
		}
	}	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void blur(BlurEvent event)
	{
		if (eventWindowDeactivated != null)
		{
			getFactory().synchronizedDispatchEvent(eventWindowDeactivated, new UIWindowEvent(eventSource, UIWindowEvent.WINDOW_DEACTIVATED,
  										                                               		 System.currentTimeMillis(), 0));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void focus(FocusEvent event)
	{
		if (eventWindowActivated != null)
		{
			getFactory().synchronizedDispatchEvent(eventWindowActivated, new UIWindowEvent(eventSource, UIWindowEvent.WINDOW_ACTIVATED,
  										    										 	   System.currentTimeMillis(), 0));
		}
	}	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void windowResized(ResizeEvent e)
	{
		performLayout();
		
		if (eventComponentResized != null)
		{
			bounds.setWidth((int)e.getWindow().getWidth());
			bounds.setHeight((int)e.getWindow().getHeight());
			
			getFactory().synchronizedDispatchEvent(eventComponentResized, new UIComponentEvent(eventSource, UIComponentEvent.COMPONENT_RESIZED,
																					 	       System.currentTimeMillis(), 0));
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void performLayout()
	{
		if (VaadinUtil.isParentHeightDefined(getRootPane().getResource()))
		{
			getRootPane().setSizeFull();
		}
		else
		{
			getRootPane().setSizeUndefined();
		}
	}
	
	/**
	 * {@inheritDoc}
	 */	
	@Override
	public void setBounds(IRectangle pBounds)
	{
		super.setBounds(pBounds);

		if (pBounds != null)
		{
	        resource.setPositionX(pBounds.getX());
	        resource.setPositionY(pBounds.getY());
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
    public boolean isResizable()
    {
		return resource.isResizable();
    }

	/**
	 * {@inheritDoc}
	 */
	@Override
    public void setResizable(boolean pResizable)
    {
		resource.setResizable(pResizable);
    }		

	/**
	 * {@inheritDoc}
	 */
	@Override
	public WindowHandler eventWindowClosed()
	{
		if (eventWindowClosed == null)
		{
			resource.addCloseListener(this);
		}
			
		return super.eventWindowClosed();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public WindowHandler eventWindowActivated()
	{
		if (eventWindowActivated == null)
		{
			resource.addFocusListener(this);
		}
			
		return super.eventWindowActivated();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public WindowHandler eventWindowDeactivated()
	{
		if (eventWindowDeactivated == null)
		{
			resource.addBlurListener(this);
		}
			
		return super.eventWindowDeactivated();
	}	

}	// VaadinFrame
