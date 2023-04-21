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
package javax.rad.ui.menu;

import javax.rad.ui.IComponent;
import javax.rad.ui.IContainer;
import javax.rad.ui.event.PopupMenuHandler;
import javax.rad.ui.event.type.popupmenu.IPopupMenuCanceledListener;
import javax.rad.ui.event.type.popupmenu.IPopupMenuWillBecomeInvisibleListener;
import javax.rad.ui.event.type.popupmenu.IPopupMenuWillBecomeVisibleListener;

/**
 * Platform and technology independent popup menu definition.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 * @see	java.awt.PopupMenu
 * @see	javax.swing.JPopupMenu
 */
public interface IPopupMenu extends IContainer
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Method definitions
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Appends a new separator at the end of the menu.
     */
	public void addSeparator();

    /**
     * Inserts a separator at the specified position.
     *
     * @param       pIndex an integer specifying the position at which to 
     *                    insert the menu separator
     * @exception   IllegalArgumentException if the value of 
     *                       <code>index</code> &lt; 0
     */
    public void addSeparator(int pIndex);
	
   /**
     * Shows the popup menu at the x, y position relative to an origin
     * component.
     * The origin component must be contained within the component
     * hierarchy of the popup menu's parent.  Both the origin and the parent 
     * must be showing on the screen for this method to be valid.
     * <p>
     * If this <code>PopupMenu</code> is being used as a <code>Menu</code>
     * (i.e., it has a non-<code>Component</code> parent),
     * then you cannot call this method on the <code>PopupMenu</code>.
     * 
     * @param pOrigin the component which defines the coordinate space
     * @param pX the x coordinate position to popup the menu
     * @param pY the y coordinate position to popup the menu
     */
    public void show(IComponent pOrigin, int pX, int pY);
	
	/**
	 * The PopupMenuHandler for the popup menu will become visible event.
	 * 
	 * @return the PopupMenuHandler for the focus gained event.
	 */
	public PopupMenuHandler<IPopupMenuWillBecomeVisibleListener> eventPopupMenuWillBecomeVisible();
	
	/**
	 * The PopupMenuHandler for the popup menu will become invisible event.
	 * 
	 * @return the PopupMenuHandler for the popup menu will become invisible event.
	 */
	public PopupMenuHandler<IPopupMenuWillBecomeInvisibleListener> eventPopupMenuWillBecomeInvisible();

	/**
	 * The PopupMenuHandler for the popup menu canceled event.
	 * 
	 * @return the PopupMenuHandler for the popup menu canceled event.
	 */
	public PopupMenuHandler<IPopupMenuCanceledListener> eventPopupMenuCanceled();
    
}	// IPopupMenu
