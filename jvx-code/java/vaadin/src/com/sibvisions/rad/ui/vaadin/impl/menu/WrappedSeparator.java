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
 * 24.09.2013 - [JR] - creation
 */
package com.sibvisions.rad.ui.vaadin.impl.menu;

import javax.rad.ui.menu.IPopupMenu;

import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.MenuBar.MenuItem;

/**
 * The <code>WrappedSeparator</code> is an {@link AbstractComponent} that holds a
 * {@link com.vaadin.ui.MenuBar.MenuItem} or {@link IPopupMenu}.
 * 
 * @author René Jahn
 */
public class WrappedSeparator extends AbstractComponent
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class Members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** the MenuItem. */
	private MenuItem menuItem;
	
	/** whether this item is visible. */
	private boolean bVisible = true;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of <code>WrappedSeparator</code>.
	 */
	public WrappedSeparator() 
	{
		super();
    }
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isVisible()
	{
		if (menuItem != null)
		{
			return ((MenuItem)menuItem).isVisible();
		}
		
		return bVisible;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setVisible(boolean pVisible)
	{
		bVisible = pVisible;
		
		if (menuItem != null)
		{
			((MenuItem)menuItem).setVisible(pVisible);
		}
	}	
	
    /**
     * {@inheritDoc}
     */
    @Override
    public String getStyleName()
    {
        if (menuItem != null)
        {
            return menuItem.getStyleName();
        }
        
        return super.getStyleName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setStyleName(String pStyleName)
    {
        super.setStyleName(pStyleName);

        if (menuItem != null)
        {
            menuItem.setStyleName(pStyleName);
        }
    }	
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	

	/**
	 * Returns the separator tem.
	 * 
	 * @return the item
	 */
	public MenuItem getMenuItem()
	{
		return menuItem;
	}

	/**
	 * Sets the separator item.
	 * 
	 * @param pMenuItem the item
	 */
	public void setMenuItem(MenuItem pMenuItem)
	{
		menuItem = pMenuItem;
	}
   
} 	// WrappedSeparator
