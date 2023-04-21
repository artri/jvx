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
 * 21.03.2018 - [JR] - creation
 */
package com.sibvisions.rad.ui.vaadin.impl.component;

import java.util.List;

import javax.rad.ui.event.ActionHandler;
import javax.rad.ui.event.UIActionEvent;
import javax.rad.ui.menu.IMenuItem;
import javax.rad.ui.menu.IPopupMenu;

import com.sibvisions.rad.ui.vaadin.ext.ui.MenuBar;
import com.sibvisions.rad.ui.vaadin.ext.ui.extension.CssExtension;
import com.sibvisions.rad.ui.vaadin.ext.ui.extension.IDynamicCss;
import com.sibvisions.rad.ui.vaadin.impl.VaadinFactory;
import com.sibvisions.rad.ui.vaadin.impl.menu.VaadinMenuItem;
import com.sibvisions.rad.ui.vaadin.impl.menu.VaadinPopupMenu;
import com.sibvisions.rad.ui.vaadin.impl.menu.WrappedMenuBar;
import com.sibvisions.util.type.StringUtil;
import com.vaadin.server.Resource;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;

/**
 * The <code>WrappedPopupMenuButton</code> is a layout which embedds a {@link MenuBar} to act
 * as button with a popup menu.
 * 
 * @author René Jahn
 */
public class WrappedPopupMenuButton extends CssLayout
                                    implements Command
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** the menu bar. */
    private InternalMenuBar menubar;
    
    /** the popup menu. */
    private IPopupMenu menu;
    
    /** the default item if clicked. */
    private IMenuItem miDefault;

    /** the root menu item. */
    private MenuItem itemRoot;
    
    /** the default item. */
    private MenuItem itemDefault;
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Creates a new instance of <code>WrappedPopupMenuButton</code>.
     */
    public WrappedPopupMenuButton()
    {
        setSizeFull();
        
        addStyleName("wrappedpopupmenubutton");
        
        menubar = new InternalMenuBar();
        menubar.setPopupStyleName("jvxpopupmenubuttonpopup usedefault");
        
        addComponent(menubar);
        
        menubar.addStyleName("jvxpopupbutton v-no-defaultitem v-nocaption");
        
        itemRoot = menubar.addItem("", null);
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    public void menuSelected(MenuItem selectedItem)
    {
        if (miDefault instanceof VaadinMenuItem && ((VaadinMenuItem)miDefault).hasActionEventHandler())
        {
            ActionHandler handler = miDefault.eventAction();
            
            ((VaadinFactory)miDefault.getFactory()).synchronizedDispatchEvent(handler, new UIActionEvent(miDefault.getEventSource(),
                                                                                                         UIActionEvent.ACTION_PERFORMED, 
                                                                                                         System.currentTimeMillis(), 
                                                                                                         0, 
                                                                                                         miDefault.getActionCommand()));
        }
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Sets the popup menu.
     * 
     * @param pMenu the menu
     */
    public void setPopupMenu(IPopupMenu pMenu)
    {
        if (menu != null)
        {
            MenuBar mbar = ((WrappedMenuBar)((VaadinPopupMenu)menu).getMenuBar().getResource()).getMenuBar();
            mbar.removeMarkAsDirtyConnector(menubar);
            
            menubar.clearIds();
        }
        
        menu = pMenu;

        if (menu != null)
        {
            MenuBar mbar = ((WrappedMenuBar)((VaadinPopupMenu)menu).getMenuBar().getResource()).getMenuBar();
            mbar.addMarkAsDirtyConnector(menubar);
            
            ((InternalMenuBar)menubar).button = this;
        }
    }
    
    /**
     * Gets the popup menu.
     * 
     * @return the menu
     */
    public IPopupMenu getPopupMenu()
    {
        return menu;
    }    
    
    /**
     * Sets the menu item which acts as default item.
     * 
     * @param pItem the default menu item
     */
    public void setDefaultMenuItem(IMenuItem pItem)
    {
        if (itemDefault != null)
        {
            menubar.bIgnore = true;
            
            try
            {
                menubar.removeItem(itemDefault);
                
                menubar.addStyleName("v-no-defaultitem");
                
                itemDefault = null;
                
                itemRoot.setCommand(null);
            }
            finally
            {
                menubar.bIgnore = false;
            }
        }
        
        miDefault = pItem;
        
        if (miDefault != null)
        {
            menubar.bIgnore = true;

            try
            {
                itemRoot.removeChildren();
                itemRoot.setCommand(this);
                
                itemDefault = menubar.addItem("", null);
                
                menubar.removeStyleName("v-no-defaultitem");
            }
            finally
            {
                menubar.bIgnore = false;
            }
        }
        
        menubar.markAsDirty();
    }
    
    /**
     * Gets the default menu item.
     * 
     * @return the menu item
     * @see #setDefaultMenuItem(IMenuItem)
     */
    public IMenuItem getDefaultMenuItem()
    {
        return miDefault;
    }
    
    /**
     * Gets the css extension of the menu bar.
     * 
     * @return the menu bar
     */
    public CssExtension getCssExtension()
    {
    	return menubar.getCssExtension();
    }
    
    /**
     * Sets the tab index.
     * 
     * @param pIndex the tab index
     */
    public void setTabIndex(int pIndex)
    {
        menubar.setTabIndex(pIndex);
    }
    
    /**
     * Gets the tab index.
     * 
     * @return the tab index
     */
    public int getTabIndex()
    {
        return menubar.getTabIndex();
    }    
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    @Override
    public void setIcon(Resource pIcon)
    {
        itemRoot.setIcon(pIcon);        
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Resource getIcon()
    {
        return itemRoot.getIcon();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setCaption(String pText)
    {
        itemRoot.setText(pText);

        if (StringUtil.isEmpty(pText))
        {
            menubar.addStyleName("v-nocaption");
        }
        else
        {
            menubar.removeStyleName("v-nocaption");
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getCaption()
    {
        return itemRoot.getText();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setEnabled(boolean pEnabled)
    {
        itemRoot.setEnabled(pEnabled);
        
        if (itemDefault != null)
        {
            itemDefault.setEnabled(pEnabled);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEnabled()
    {
        return itemRoot.isEnabled();
    }
    
    //****************************************************************
    // Subclass definition
    //****************************************************************

    /**
     * The <code>InternalMenuBar</code> is a {@link MenuBar} which updates the button menu after 
     * the underlying popup menu has changed.
     * 
     * @author René Jahn
     */
    public static class InternalMenuBar extends MenuBar
                                        implements IDynamicCss
    {
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Class members
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /** the referenced button. */
        private WrappedPopupMenuButton button;
        
        /** the CSS extension. */
        private CssExtension cssext;
        
        /** whether to ignore menu changes. */
        private boolean bIgnore = false;
        
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Overwritten methods
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /**
         * {@inheritDoc}
         */
        @Override
        public void markAsDirty()
        {
            if (bIgnore)
            {
                return;
            }
            
            if (button != null && button.menu != null)
            {
                bIgnore = true;

                try
                {
                    MenuBar mbar = ((WrappedMenuBar)((VaadinPopupMenu)button.menu).getMenuBar().getResource()).getMenuBar();
                    mbar.copyIds(this);
                    
                    setId(((AbstractComponent)button.menu.getResource()).getId());
                    
                    List<MenuItem> items;
                    
                    if (button.itemDefault != null)
                    {
                        items = button.itemDefault.getChildren();
                        
                        if (items == null)
                        {
                            //this call creates the children list of itemRoot
                            button.itemDefault.addItem("delete", null);
    
                            items = button.itemDefault.getChildren();
                        }
                        
                        button.itemRoot.removeChildren();
                    }
                    else
                    {
                        items = button.itemRoot.getChildren();
    
                        if (items == null)
                        {
                            button.itemRoot.addItem("delete", null);
                            
                            items = button.itemRoot.getChildren();
                        }
                    }
    
                    items.clear();
                    
                    List<MenuItem> liItems = mbar.getItems().get(0).getChildren();
                    
                    //#1976
                    if (liItems != null)
                    {
                    	items.addAll(liItems);
                    }
                }
                finally
                {
                    bIgnore = false;
                }
            }
            
            super.markAsDirty();
        }
        
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Interface implementation
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /**
         * {@inheritDoc}
         */
        public CssExtension getCssExtension()
        {
            if (cssext == null)
            {
                cssext = new CssExtension();
                cssext.extend(this);
            }
            
            return cssext;
        }        
        
    }   // InternalMenuBar  
    
}   // WrappedPopupMenuButton
