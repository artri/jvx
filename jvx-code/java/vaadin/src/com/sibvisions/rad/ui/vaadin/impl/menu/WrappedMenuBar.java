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
 * 22.10.12 - [CB] - creation
 */

package com.sibvisions.rad.ui.vaadin.impl.menu;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.sibvisions.rad.ui.vaadin.ext.ui.MenuBar;
import com.sibvisions.rad.ui.vaadin.ext.ui.SimplePanel;
import com.vaadin.ui.Component;

/**
 * The <code>WrappedMenuBar</code> is an {@link SimplePanel} that holds a
 * {@link MenuBar}.
 * 
 * @author Benedikt Cermak
 */
public class WrappedMenuBar extends SimplePanel
{	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** the MenuBar. */
	private MenuBar menuBar = new MenuBar();
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	 /**
     * Creates a new instance of <code>WrappedMenuBar</code>.
     *
     * @see com.vaadin.ui.MenuBar
     */
	public WrappedMenuBar()
	{
		menuBar.setSizeFull();
		menuBar.addStyleName("jvxmenubar");
		
		addComponent(menuBar);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface Implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public void replaceComponent(Component pOldComponent, Component pNewComponent)
	{
		if (pNewComponent instanceof MenuBar) 
		{
			pNewComponent.setSizeFull();

			removeComponent(menuBar);
			addComponent(pNewComponent);
			
			menuBar = (MenuBar)pNewComponent;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public int getComponentCount()
	{
		return 1;
	}

	/**
	 * {@inheritDoc}
	 */
	public Iterator<Component> iterator()
	{
		return new Iterator<Component>()
		{
			int iPos = -1;
			
			public boolean hasNext()
			{
				return iPos == -1;
			}

			public Component next()
			{
				iPos++;
				
				if (iPos == 0)
				{
					return menuBar;
				}
				
				throw new NoSuchElementException(); 
			}

			public void remove()
			{
				throw new UnsupportedOperationException("Can't remove MenuBar");
			}
		};
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
		return menuBar.isVisible();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setVisible(boolean pVisible)
	{
		menuBar.setVisible(pVisible);
	}		
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setEnabled(boolean pEnable)
	{
		menuBar.setEnabled(pEnable);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEnabled()
	{
		return menuBar.isEnabled();
	}
	
    /**
     * {@inheritDoc}
     */
    @Override
	public void setId(String pId)
	{
	    super.setId(pId);
	    
	    menuBar.setId(pId);
	}

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStyleName()
    {
        return menuBar.getStyleName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setStyleName(String pStyleName)
    {
        if (menuBar == null)
        {
            super.setStyleName(pStyleName);
        }
        else
        {
            menuBar.setStyleName(pStyleName);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addStyleName(String pStyleName)
    {
        menuBar.addStyleName(pStyleName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeStyleName(String pStyleName)
    {
        menuBar.removeStyleName(pStyleName);
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
     * Get the {@link MenuBar}.
     *
     * @return the MenuBar
     * @see com.vaadin.ui.MenuBar
     */
	public MenuBar getMenuBar()
	{
		return menuBar;
	}
	
}	// WrappedMenuBar
