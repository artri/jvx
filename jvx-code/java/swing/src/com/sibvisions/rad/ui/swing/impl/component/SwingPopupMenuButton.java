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
package com.sibvisions.rad.ui.swing.impl.component;

import java.awt.Insets;

import javax.rad.ui.IInsets;
import javax.rad.ui.component.IPopupMenuButton;
import javax.rad.ui.menu.IMenuItem;
import javax.rad.ui.menu.IPopupMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.sibvisions.rad.ui.awt.impl.AwtInsets;
import com.sibvisions.rad.ui.swing.ext.JVxButton;
import com.sibvisions.rad.ui.swing.impl.SwingFactory;

/**
 * The <code>SwingPopupMenuButton</code> is the <code>IPopupMenuButton</code>
 * implementation for swing.
 * 
 * @author René Jahn
 * @see	JVxButton
 * @see javax.rad.ui.component.IPopupMenuButton
 */
public class SwingPopupMenuButton extends SwingAbstractFormatableButton<JVxButton>
                                  implements IPopupMenuButton
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** the popup menu. */
    private IPopupMenu menu;
    
    /** the default menu item. */
    private IMenuItem miDefault;
    
    /** the margins. */
    private Insets margin;
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>SwingPopupMenuButton</code>.
	 */
	public SwingPopupMenuButton()
	{
		super(new JVxButton(), true);
		
		margin = resource.getMargin();
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
    public void setPopupMenu(IPopupMenu pMenu)
    {
        menu = pMenu;
        
        if (pMenu != null)
        {
            resource.setPopupMenu((JPopupMenu)pMenu.getResource());
        }
        else
        {
            resource.setPopupMenu(null);            
        }
    }

    /**
     * {@inheritDoc}
     */
    public IPopupMenu getPopupMenu()
    {
        return menu;
    }

    /**
     * {@inheritDoc}
     */
    public void setDefaultMenuItem(IMenuItem pItem)
    {
        miDefault = pItem;
        
        if (pItem != null)
        {
            resource.setDefaultItem((JMenuItem)pItem.getResource());
        }
        else
        {
            resource.setDefaultItem(null);
        }
    }

    /**
     * {@inheritDoc}
     */
    public IMenuItem getDefaultMenuItem()
    {
        return miDefault;
    }
    
	/**
	 * {@inheritDoc}
	 */
    public void setMargins(IInsets pMargins)
    {
    	if (pMargins != null)
    	{
        	margin = (Insets)pMargins.getResource();

        	resource.setMargin(margin);
    	}
    	else
    	{
    		resource.setMargin(null);

			margin = resource.getMargin();
    	}
    }
    
	/**
	 * {@inheritDoc}
	 */
    public IInsets getMargins()
    {
    	if (margin != null)
    	{
    		return new AwtInsets(margin);
    	}
    	else
    	{
    		return new AwtInsets(resource.getMargin());
    	}
    }
    
    /**
     * {@inheritDoc}
     */
    public void setVerticalTextPosition(int pVerticalPosition)
    {
        resource.setVerticalTextPosition(SwingFactory.getVerticalSwingAlignment(pVerticalPosition));
    }
    
    /**
     * {@inheritDoc}
     */
    public int getVerticalTextPosition()
    {
        return SwingFactory.getVerticalAlignment(resource.getVerticalTextPosition());
    }
    
    /**
     * {@inheritDoc}
     */
    public void setHorizontalTextPosition(int pHorizontalPosition)
    {
        resource.setHorizontalTextPosition(SwingFactory.getHorizontalSwingAlignment(pHorizontalPosition));
    }
    
    /**
     * {@inheritDoc}
     */
    public int getHorizontalTextPosition()
    {
        return SwingFactory.getHorizontalAlignment(resource.getHorizontalTextPosition());
    }
	
}	// SwingPopupMenuButton
