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
 * 23.10.2012 - [CB] - creation
 * 24.09.2013 - [JR] - support VaadinSeparator
 *                   - fixed resetMenuItem (recursion check didn't work)
 */
package com.sibvisions.rad.ui.vaadin.impl.menu;

import java.util.List;

import javax.rad.ui.IComponent;
import javax.rad.ui.IContainer;
import javax.rad.ui.menu.IMenu;
import javax.rad.ui.menu.IMenuBar;

import com.sibvisions.rad.ui.vaadin.impl.VaadinComponent;
import com.sibvisions.rad.ui.vaadin.impl.VaadinContainer;
import com.sibvisions.util.type.CommonUtil;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;

/**
 * The <code>VaadinMenuBar</code> class is the vaadin implementation of {@link IMenuBar}.
 * 
 * @author Benedikt Cermak
 */
public class VaadinMenuBar extends VaadinContainer<WrappedMenuBar> 
                           implements IMenuBar							
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>VaadinMenuBar</code>.
	 * 
	 * @see javax.rad.ui.menu.IMenuBar
	 */
	public VaadinMenuBar()
	{
		super(new WrappedMenuBar());
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addToVaadin(IComponent pComponent, Object pConstraints, int pIndex)
    {
		addMenuItem(this, pComponent, pIndex);
    }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeFromVaadin(IComponent pComponent)
	{
		removeMenuItem(pComponent);
	}	
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	

	/**
	 * Adds a menu item to the Menu.
	 * 
	 * @param pParent the parent Container
	 * @param pItem the item to add 
	 * @param pIndex the index
	 */
	public void addMenuItem(IContainer pParent, IComponent pItem, int pIndex)
	{
		createMenuItem(pParent, pItem, pIndex);
		
		if (pItem instanceof IMenu)
		{
			IMenu menu = (IMenu)pItem;
			
			IComponent comp;

			for (int i = 0, anz = menu.getComponentCount(); i < anz; i++)
			{
				comp = menu.getComponent(i);
				
				if (comp instanceof IMenu)
				{
					addMenuItem(menu, (IMenu)comp, i);
				}
				else
				{
					createMenuItem(menu, comp, i);
				}
			}
		}
	}
	
	/**
	 * Creates a {@link MenuItem}.
	 * 
	 * @param pParent the parent Container
	 * @param pComponent the MenuItem to add 
	 * @param pIndex the index
	 */
	private void createMenuItem(IContainer pParent, IComponent pComponent, int pIndex)
	{
		MenuItem miNew;

		if (pParent instanceof IMenuBar)
		{
			VaadinMenuItem menuItem = (VaadinMenuItem)pComponent;

			WrappedMenuItem wmiComponent = menuItem.getResource();
			
			MenuItem miComponent = wmiComponent.getMenuItem();
			
			if (miComponent == null)
			{
				int menuSize = ((VaadinMenuBar)pParent).getResource().getMenuBar().getSize();
				
				Command cmd = null;
				
				//if the item is not a menu
				if (!(menuItem instanceof VaadinMenu))
				{
					cmd = menuItem.getVaadinCommand();
				}

				if (pIndex < 0 || menuSize <= 0)
				{
					miNew = ((VaadinMenuBar)pParent).getResource().getMenuBar().addItem(CommonUtil.nvl(menuItem.getText(), ""),
							                                                            menuItem.getIcon(),						
							                                                            cmd);
				}
				else
				{
					List<MenuItem> itemList = ((VaadinMenuBar)pParent).getResource().getMenuBar().getItems();
					MenuItem itemAtIndex = itemList.get(pIndex);
					
					
					miNew = ((VaadinMenuBar)pParent).getResource().getMenuBar().addItemBefore(CommonUtil.nvl(menuItem.getText(), ""),
                            															      menuItem.getIcon(),						
                            															      cmd,
                            															      itemAtIndex);
				}
				
				//connect with vaadin
				wmiComponent.setMenuItem(miNew);
				
				resource.getMenuBar().setId(miNew, menuItem.getId());
			}
		}
		else 
		{
			MenuItem miMenu = ((WrappedMenuItem)pParent.getResource()).getMenuItem();
			
			//not "added" to Vaadin!
			if (miMenu == null)
			{
				return;
			}

			if (pComponent instanceof VaadinSeparator)
			{
				VaadinSeparator menuSeparator = (VaadinSeparator)pComponent;
				
				WrappedSeparator wmiComponent = menuSeparator.getResource();
				
				MenuItem miComponent = wmiComponent.getMenuItem();
				
				if (miComponent == null)
				{
					int menuSize = miMenu.getSize();
	
					if (pIndex < 0 || pIndex == menuSize || menuSize < 0)
					{
						miNew  = miMenu.addSeparator();
					}
					else
					{		
						miNew  = miMenu.addSeparatorBefore(miMenu.getChildren().get(pIndex));
					}
					
					wmiComponent.setMenuItem(miNew);
					
	                resource.getMenuBar().setId(miNew, menuSeparator.getId());
				}
			}
			else
			{
				VaadinMenuItem menuItem = (VaadinMenuItem)pComponent;
				
				WrappedMenuItem wmiComponent = menuItem.getResource();
				
				MenuItem miComponent = wmiComponent.getMenuItem();
				
				if (miComponent == null)
				{
					int menuSize = miMenu.getSize();
					
					if (pIndex < 0 || pIndex == menuSize || menuSize < 0)
					{
						miNew = miMenu.addItem(CommonUtil.nvl(menuItem.getText(), ""), 
								   			   menuItem.getIcon(), 
								   			   menuItem.getVaadinCommand());
					}
					else
					{
						miNew = miMenu.addItemBefore(CommonUtil.nvl(menuItem.getText(), ""), 
											   		 menuItem.getIcon(), 
											   		 menuItem.getVaadinCommand(),
											   		 miMenu.getChildren().get(pIndex));
					}
					
					if (pComponent instanceof VaadinCheckBoxMenuItem)
					{
						miNew.setCheckable(true);
						//important, otherwise we don't have the initial state!
						miNew.setChecked(((VaadinCheckBoxMenuItem)pComponent).isSelected());
					}
					
					wmiComponent.setMenuItem(miNew);
					
					resource.getMenuBar().setId(miNew, menuItem.getId());
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
		IContainer conParent = pComponent.getParent();

		MenuItem item;
		
		if (pComponent instanceof VaadinSeparator)
		{
			VaadinSeparator vsComponent = ((VaadinSeparator)pComponent); 
			
			WrappedSeparator wmiComponent = vsComponent.getResource();
			
			item = wmiComponent.getMenuItem();
		}
		else
		{
			VaadinMenuItem vmiComponent = ((VaadinMenuItem)pComponent); 
			
			WrappedMenuItem wmiComponent = vmiComponent.getResource();
			
			item = wmiComponent.getMenuItem();
		}
		
		if (item != null)
		{
			if (conParent instanceof IMenuBar)
			{
				((WrappedMenuBar)conParent.getResource()).getMenuBar().removeItem(item);
			}
			else if (conParent instanceof IMenu)
			{
				((MenuItem)((WrappedMenuItem)conParent.getResource()).getMenuItem()).removeChild(item);
			}
			
			resource.getMenuBar().setId(item, null);
		}

		//reset all "internal" items
		resetMenuItem(pComponent);
	}
	
	/**
	 * Resets all internal menu items. All {@link MenuItem}s will be set to null.
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
	 * Sets the id for the given menu item.
	 * 
	 * @param pItem the menu item
	 * @param pId the id
	 */
	public void setId(VaadinComponent pItem, String pId)
	{
	    Object oItem = pItem.getResource();
	    
	    if (oItem instanceof WrappedMenuItem)
	    {
	        oItem = ((WrappedMenuItem)oItem).getMenuItem();
	    }
	    else if (oItem instanceof WrappedSeparator)
	    {
	        oItem = ((WrappedSeparator)oItem).getMenuItem();
	    }
	    
        if (oItem instanceof MenuItem)
        {
            resource.getMenuBar().setId((MenuItem)oItem, pId);
        }
	}
	
} 	// VaadinMenuBar
