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
package javax.rad.ui.component;

import javax.rad.ui.menu.IMenuItem;
import javax.rad.ui.menu.IPopupMenu;

/**
 * Platform and technology independent popup menu button definition.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author René Jahn
 */
public interface IPopupMenuButton extends IFormatableButton
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Sets the {@link IPopupMenu} which should be shown.
     *  
     * @param pMenu the menu
     */
    public void setPopupMenu(IPopupMenu pMenu);
    
    /**
     * Gets the {@link IPopupMenu} which can be shown.
     * 
     * @return the menu
     */
    public IPopupMenu getPopupMenu();
	
    /**
     * Sets the default menu item. The default menu item will be used as action
     * for this button. The menu item should be an item of the configured menu.
     * If no default menu item is set, the full menu will be shown in case of
     * button click.
     * 
     * @param pItem the item to use as action item
     * @see #setPopupMenu(IPopupMenu)
     */
    public void setDefaultMenuItem(IMenuItem pItem);
    
    /**
     * Gets the default menu item.
     * 
     * @return the menu item
     * @see #setDefaultMenuItem(IMenuItem)
     */
    public IMenuItem getDefaultMenuItem();
    
}	// IPopupMenuButton
