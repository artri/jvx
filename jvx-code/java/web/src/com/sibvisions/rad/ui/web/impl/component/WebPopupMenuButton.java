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
 * 21.03.2018 - [HM] - creation
 */
package com.sibvisions.rad.ui.web.impl.component;

import javax.rad.ui.component.IPopupMenuButton;
import javax.rad.ui.menu.IMenuItem;
import javax.rad.ui.menu.IPopupMenu;

import com.sibvisions.rad.ui.web.impl.menu.WebPopupMenu;

/**
 * Web server implementation of {@link IPopupMenuButton}.
 * 
 * @author René Jahn
 */
public class WebPopupMenuButton extends AbstractWebFormatableButton
                                implements IPopupMenuButton
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the popup menu. */
	private IPopupMenu popupMenu;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>WebPopupMenuButton</code>.
     *
     * @see javax.rad.ui.component.IButton
     */
	public WebPopupMenuButton()
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface Implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
    public void setPopupMenu(IPopupMenu pMenu)
    {
    	if (popupMenu != null)
    	{
    		removeAdditional(popupMenu);
    	}
    	
    	popupMenu = (WebPopupMenu)pMenu;

    	if (popupMenu != null)
    	{
			addAdditional(popupMenu);
    	}
    	
        setProperty("popupMenu", pMenu);
    }

    /**
     * {@inheritDoc}
     */
    public IPopupMenu getPopupMenu()
    {
    	return popupMenu;
    }

    /**
     * {@inheritDoc}
     */
    public void setDefaultMenuItem(IMenuItem pItem)
    {
        setProperty("defaultMenuItem", pItem);
    }

    /**
     * {@inheritDoc}
     */
    public IMenuItem getDefaultMenuItem()
    {
        return getProperty("defaultMenuItem", null);
    }
    
}	// WebPopupMenuButton
