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
 * 26.02.2013 - [SW] - creation
 */
package com.sibvisions.rad.ui.vaadin.impl.menu;

import javax.rad.ui.IComponent;
import javax.rad.ui.IContainer;
import javax.rad.ui.event.PopupMenuHandler;
import javax.rad.ui.event.UIPopupMenuEvent;
import javax.rad.ui.event.type.popupmenu.IPopupMenuCanceledListener;
import javax.rad.ui.event.type.popupmenu.IPopupMenuWillBecomeInvisibleListener;
import javax.rad.ui.event.type.popupmenu.IPopupMenuWillBecomeVisibleListener;
import javax.rad.ui.menu.IMenu;
import javax.rad.ui.menu.IPopupMenu;
import javax.rad.util.EventHandler;

import com.sibvisions.rad.ui.vaadin.ext.ui.SimplePanel;
import com.sibvisions.rad.ui.vaadin.impl.VaadinContainerBase;
import com.vaadin.contextmenu.ContextMenu;
import com.vaadin.contextmenu.ContextMenu.ContextMenuOpenListener;
import com.vaadin.contextmenu.MenuItem;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.MenuBar;

/**
 * The <code>VaadinPopupMenu</code> class is the vaadin implementation of <code>IPopupMenu</code>.
 * It uses the Vaadin Add On ContextMenu which is an extension for components to show a hierarchical 
 * popup menu. 
 * 
 * @author Stefan Wurm
 */
public class VaadinPopupMenu extends VaadinContainerBase<SimplePanel, SimplePanel> 
                             implements IPopupMenu,
                                        ContextMenuOpenListener
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

	/** The extension for the component. **/
	private ContextMenu contextMenu;
	
	/** the internal menubar wrapper. */
	private VaadinMenuBar mbWrapper;
	
	/** the menu wrapper. */
	private VaadinMenu menWrapper;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
	
	/**
	 * Creates a new instance of <code>VaadinPopupMenu</code>.
	 */
	public VaadinPopupMenu()
	{
		super(new SimplePanel());
		
		mbWrapper = new VaadinMenuBar();
		
		MenuBar mbar = mbWrapper.getResource().getMenuBar();
		
		//THIS IS IMPORTANT, because every addItem increases the menuItem.getId(). The ID isn't a gloal unique number.
		//It's unique per menubar. If we re-use the items of this wrapped menubar in another menubar instance, we could 
		//get a problem with correct menuitem detection because e.g. in click handling. The clickId from the client is 
		//the id of the item and this id will be used to find the menu item of a menubar. If the menu item is copied from 
		//another menu bar, the wrong menu item could be detected and event handling will be wrong! 
		for (int i = 0; i < 3; i++)
		{
		    //simply increases the item id
		    MenuBar.MenuItem item = mbar.addItem("dummy", null);
		    mbar.removeItem(item);
		}
		
		menWrapper = new VaadinMenu("Option");
		
		mbWrapper.add(menWrapper);
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
		add(new VaadinSeparator(), null, pIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	public void show(IComponent pOrigin, int pX, int pY)
	{
		if (pOrigin.getResource() instanceof AbstractComponent)
		{
	    	if (eventPopupMenuWillBecomeVisible != null)
	    	{
	    		getFactory().synchronizedDispatchEvent(eventPopupMenuWillBecomeVisible, 
									    			   new UIPopupMenuEvent(eventSource, 
																 			UIPopupMenuEvent.POPUPMENU_WILLBECOMEVISIBLE, 
																 			System.currentTimeMillis(),
																 			0));
	    	}

	    	AbstractComponent component = (AbstractComponent) pOrigin.getResource();

	    	if (contextMenu == null)
	    	{
	    		contextMenu = new ContextMenu(component.getUI(), false);
	    	}

			contextMenu.removeItems();

			addSubMenuItem(null, menWrapper);
			
			contextMenu.open(pX, pY);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void add(IComponent pComponent, Object pConstraints, int pIndex)
    {
		menWrapper.add(pComponent, pConstraints, pIndex);
    }

	/**
	 * {@inheritDoc}
	 */
	public void remove(int pIndex)
    {
		menWrapper.remove(pIndex);
    }

	/**
	 * {@inheritDoc}
	 */
	public int getComponentCount()
	{
		return menWrapper.getComponentCount();
	}

	/**
	 * {@inheritDoc}
	 */
	public IComponent getComponent(int pIndex)
	{
		return menWrapper.getComponent(pIndex);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int indexOf(IComponent pComponent)
	{
		return menWrapper.indexOf(pComponent);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void addToVaadin(IComponent pComponent, Object pConstraints, int pIndex)
	{
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeFromVaadin(IComponent pComponent)
	{
	}
	
	//Listener
	
    /**
	 * {@inheritDoc}
	 */
    public PopupMenuHandler<IPopupMenuWillBecomeVisibleListener> eventPopupMenuWillBecomeVisible()
    {
		if (eventPopupMenuWillBecomeVisible == null)
		{
			eventPopupMenuWillBecomeVisible = new PopupMenuHandler<IPopupMenuWillBecomeVisibleListener>(IPopupMenuWillBecomeVisibleListener.class);
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
		}
		return eventPopupMenuCanceled;
    }
    
    /**
     * {@inheritDoc}
     */
    public void onContextMenuOpen(ContextMenuOpenEvent pEvent)
    {
        if (EventHandler.isDispatchable(eventPopupMenuWillBecomeVisible))
        {
    		getFactory().synchronizedDispatchEvent(eventPopupMenuWillBecomeVisible, 
								    			   new UIPopupMenuEvent(eventSource, 
															 			UIPopupMenuEvent.POPUPMENU_WILLBECOMEVISIBLE, 
															 			System.currentTimeMillis(),
															 			0));
        }
    }
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Adds the submenu items to the context menu.
	 * 
	 * @param pItem the top item.
	 * @param pMenu the vaadin menu with the sub items.
	 */
	private void addSubMenuItem(MenuItem pItem, IContainer pMenu)
	{
		MenuItem item = null;
		
		for (int i = 0, anz = pMenu.getComponentCount(); i < anz; i++)
		{
			IComponent component = pMenu.getComponent(i);
			
			if (component.isVisible()) // Add only if component is visible.
			{
				if (component instanceof VaadinMenu)
				{
					VaadinMenu menu = (VaadinMenu)component;
					
					if (pItem == null)
					{
						item = contextMenu.addItem(menu.getText(), menu.getContextCommand());
					}
					else 
					{
						item = pItem.addItem(menu.getText(), menu.getContextCommand());
					}
	
					((VaadinMenu)component).getResource().setMenuItem(item);
	
					addSubMenuItem(item, (VaadinMenu)component);
				}
				else if (component instanceof VaadinMenuItem)
				{
					VaadinMenuItem menuItem = (VaadinMenuItem)component;
					
					if (pItem == null)
					{
						item = contextMenu.addItem(menuItem.getText(), menuItem.getIcon(), menuItem.getContextCommand());
					}
					else
					{
						item = pItem.addItem(((VaadinMenuItem)component).getText(), ((VaadinMenuItem)component).getIcon(), menuItem.getContextCommand());
					}
	
					((VaadinMenuItem)component).getResource().setMenuItem(item);
				}
				else if (component instanceof VaadinSeparator)
				{
					if (pItem == null)
					{
						item = contextMenu.addSeparator();
					}
					else
					{
						item = pItem.addSeparator();
					}
				}
				
				if (item != null)
				{
					item.setEnabled(component.isEnabled());
				}
			}
		}
	}

	/**
	 * Removes a menu item from the menu.
	 * 
	 * @param pComponent the menu item to remove
	 */
	public void removeMenuItem(IComponent pComponent)
	{
		//reset all "internal" items
		resetMenuItem(pComponent);
	}
	
	/**
	 * Resets all internal menu items. All ???s will be set to null.
	 * 
	 * @param pComponent the menu item (can be a separator!)
	 */
	private void resetMenuItem(IComponent pComponent)
	{
		Object oResource = pComponent.getResource();
		
		if (oResource instanceof WrappedSeparator)
		{
			((WrappedSeparator)oResource).setMenuItem(null);
		}
		else
		{
			((WrappedMenuItem)oResource).setMenuItem(null);
		}
		
		if (pComponent instanceof IMenu)
		{
			IMenu menu = ((IMenu)pComponent);
			
			for (int i = 0, anz = menu.getComponentCount(); i < anz; i++)
			{
				resetMenuItem(menu.getComponent(i));
			}
		}
	}
	
	/**
	 * Gets the internal menubar wrapper. The menubar is independent of this popup but has
	 * the same component hierarchy.
	 * 
	 * @return the menu bar
	 */
	public VaadinMenuBar getMenuBar()
	{
	    return mbWrapper;
	}

}	// VaadinPopupMenu
