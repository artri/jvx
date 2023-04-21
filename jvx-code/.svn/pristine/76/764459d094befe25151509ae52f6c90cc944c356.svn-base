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
 * 26.11.2009 - [HM] - creation
 */
package com.sibvisions.rad.ui.web.impl.menu;

import javax.rad.ui.IComponent;
import javax.rad.ui.event.PopupMenuHandler;
import javax.rad.ui.event.UIPopupMenuEvent;
import javax.rad.ui.event.type.popupmenu.IPopupMenuCanceledListener;
import javax.rad.ui.event.type.popupmenu.IPopupMenuWillBecomeInvisibleListener;
import javax.rad.ui.event.type.popupmenu.IPopupMenuWillBecomeVisibleListener;
import javax.rad.ui.menu.IPopupMenu;

import com.sibvisions.rad.ui.web.impl.WebComponent;
import com.sibvisions.rad.ui.web.impl.WebContainer;

/**
 * Web server implementation of {@link IPopupMenu}.
 * 
 * @author Martin Handsteiner
 */
public class WebPopupMenu extends WebContainer
                          implements IPopupMenu
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** EventHandler for popupMenuWIllBecomeVisible. */
	private PopupMenuHandler<IPopupMenuWillBecomeVisibleListener> eventPopupMenuWillBecomeVisible = null;
	/** EventHandler for popupMenuWIllBecomeInvisible. */
	private PopupMenuHandler<IPopupMenuWillBecomeInvisibleListener> eventPopupMenuWillBecomeInvisible = null;
	/** EventHandler for popupMenuWIllBecomeInvisible. */
	private PopupMenuHandler<IPopupMenuCanceledListener> eventPopupMenuCanceled = null;
	
	/** the opener component. */
	private WebComponent opener;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>WebPopupMenu</code>.
     *
     * @see javax.rad.ui.menu.IPopupMenu
     */
	public WebPopupMenu()
	{
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public void addSeparator()
    {
    	addSeparator(-1);
    }

	/**
	 * {@inheritDoc}
	 */
    public void addSeparator(int pIndex)
    {
    	add(new WebSeparator(), null, pIndex);
    }

	/**
	 * {@inheritDoc}
	 */
    public void show(IComponent pOrigin, int pX, int pY)
    {
    	if (eventPopupMenuWillBecomeVisible != null)
    	{
    		getFactory().synchronizedDispatchEvent(eventPopupMenuWillBecomeVisible, 
    		                                       new UIPopupMenuEvent(getEventSource(), UIPopupMenuEvent.POPUPMENU_WILLBECOMEVISIBLE, 
    				                                                    System.currentTimeMillis(), 0));
    	}

    	if (opener != null) 
    	{
    		opener.removeAdditional(this);
    		
    		opener.setProperty("popupMenu", null, true);
    	}
    	
    	opener = (WebComponent)pOrigin;
    	
    	opener.addAdditional(this);
    	
    	opener.setProperty("popupMenu", this, true);
    }
    
    /**
	 * {@inheritDoc}
	 */
    public PopupMenuHandler<IPopupMenuWillBecomeVisibleListener> eventPopupMenuWillBecomeVisible()
    {
		if (eventPopupMenuWillBecomeVisible == null)
		{
			eventPopupMenuWillBecomeVisible = new PopupMenuHandler<IPopupMenuWillBecomeVisibleListener>(IPopupMenuWillBecomeVisibleListener.class);
			
			setProperty("popupMenuWillBecomeVisible", eventPopupMenuWillBecomeVisible);
		}
		return eventPopupMenuWillBecomeVisible;
    }
	
    /**
	 * {@inheritDoc}
	 */
    public PopupMenuHandler<IPopupMenuWillBecomeInvisibleListener> eventPopupMenuWillBecomeInvisible()
    {
		if (eventPopupMenuWillBecomeInvisible == null)
		{
			eventPopupMenuWillBecomeInvisible = new PopupMenuHandler<IPopupMenuWillBecomeInvisibleListener>(IPopupMenuWillBecomeInvisibleListener.class);
			
			setProperty("popupMenuWillBecomeInvisible", eventPopupMenuWillBecomeInvisible);
		}
		return eventPopupMenuWillBecomeInvisible;
    }	
	
    /**
	 * {@inheritDoc}
	 */
    public PopupMenuHandler<IPopupMenuCanceledListener> eventPopupMenuCanceled()
    {
		if (eventPopupMenuCanceled == null)
		{
			eventPopupMenuCanceled = new PopupMenuHandler<IPopupMenuCanceledListener>(IPopupMenuCanceledListener.class);
			
			setProperty("popupMenuCanceled", eventPopupMenuCanceled);
		}
		return eventPopupMenuCanceled;
    }	
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void setVisible(boolean pVisible)
    {
    	if (!pVisible)
    	{
        	if (eventPopupMenuWillBecomeInvisible != null)
        	{
        		getFactory().synchronizedDispatchEvent(eventPopupMenuWillBecomeInvisible, 
        		                                       new UIPopupMenuEvent(getEventSource(), UIPopupMenuEvent.POPUPMENU_WILLBECOMEINVISIBLE, 
        				                                                    System.currentTimeMillis(), 0));
        	}

        	if (opener != null)
        	{
        		opener.setProperty("popupMenu", null, true);
        		
        		opener.removeAdditional(this);
        		
        		opener = null;
        	}
    	}
    	else
    	{
    		error("Use show method instead of setVisible(true)");
    	}
    }

}	// WebPopupMenu
