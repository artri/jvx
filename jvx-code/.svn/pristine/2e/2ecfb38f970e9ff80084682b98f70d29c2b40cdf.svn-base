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
 * 01.10.2008 - [HM] - creation
 */
package com.sibvisions.rad.ui.swing.impl.menu;

import javax.rad.ui.IComponent;
import javax.rad.ui.event.PopupMenuHandler;
import javax.rad.ui.event.UIPopupMenuEvent;
import javax.rad.ui.event.type.popupmenu.IPopupMenuCanceledListener;
import javax.rad.ui.event.type.popupmenu.IPopupMenuWillBecomeInvisibleListener;
import javax.rad.ui.event.type.popupmenu.IPopupMenuWillBecomeVisibleListener;
import javax.rad.ui.menu.IPopupMenu;
import javax.swing.JComponent;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import com.sibvisions.rad.ui.swing.ext.JVxPopupMenu;
import com.sibvisions.rad.ui.swing.impl.SwingComponent;

/**
 * The <code>SwingPopupMenu</code> is the <code>IPopupMenu</code>
 * implementation for swing.
 * 
 * @author Martin Handsteiner
 * @see	javax.swing.JPopupMenu
 * @see javax.rad.ui.menu.IPopupMenu
 */
public class SwingPopupMenu extends SwingComponent<JVxPopupMenu> 
                            implements IPopupMenu, PopupMenuListener
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

	/** whether the popup menu listener was added. */
	protected boolean bPopupMenuListener = false;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>SwingPopupMenu</code>.
	 */
	public SwingPopupMenu()
	{
		super(new JVxPopupMenu());
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
		add(new SwingSeparator(), null, pIndex);
    }

	/**
	 * {@inheritDoc}
	 */
	public void show(IComponent pOrigin, int pX, int pY)
	{
		resource.show((JComponent)pOrigin.getResource(), pX, pY);
	}
	
    /**
	 * {@inheritDoc}
	 */
    public PopupMenuHandler<IPopupMenuWillBecomeVisibleListener> eventPopupMenuWillBecomeVisible()
    {
		if (eventPopupMenuWillBecomeVisible == null)
		{
			eventPopupMenuWillBecomeVisible = new PopupMenuHandler<IPopupMenuWillBecomeVisibleListener>(IPopupMenuWillBecomeVisibleListener.class);
			
			addPopupMenuListener();
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
			
			addPopupMenuListener();
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
			
			addPopupMenuListener();
		}
		return eventPopupMenuCanceled;
    }	
	
    /**
	 * {@inheritDoc}
	 */
	public void popupMenuWillBecomeVisible(PopupMenuEvent e)
	{
    	if (eventPopupMenuWillBecomeVisible != null)
    	{
    		eventPopupMenuWillBecomeVisible.dispatchEvent(new UIPopupMenuEvent(eventSource, 
    																 UIPopupMenuEvent.POPUPMENU_WILLBECOMEVISIBLE, 
    															     System.currentTimeMillis(),
    															     0));
    	}
	}

    /**
	 * {@inheritDoc}
	 */
	public void popupMenuWillBecomeInvisible(PopupMenuEvent e)
	{
    	if (eventPopupMenuWillBecomeInvisible != null)
    	{
    		eventPopupMenuWillBecomeInvisible.dispatchEvent(new UIPopupMenuEvent(eventSource, 
    																 UIPopupMenuEvent.POPUPMENU_WILLBECOMEINVISIBLE, 
    															     System.currentTimeMillis(),
    															     0));
    	}
	}

    /**
	 * {@inheritDoc}
	 */
	public void popupMenuCanceled(PopupMenuEvent e)
	{
    	if (eventPopupMenuCanceled != null)
    	{
    		eventPopupMenuCanceled.dispatchEvent(new UIPopupMenuEvent(eventSource, 
    																 UIPopupMenuEvent.POPUPMENU_CANCELED, 
    															     System.currentTimeMillis(),
    															     0));
    	}
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Adds a component listener for this component, if not already added.
	 */
	protected void addPopupMenuListener()
	{
		if (!bPopupMenuListener)
		{
			bPopupMenuListener = true;
			resource.addPopupMenuListener(this);
		}
	}

}	// SwingPopupMenu
