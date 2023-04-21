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
 * 11.08.2009 - [JR] - creation
 */
package com.sibvisions.rad.ui.swing.ext;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;

/**
 * A menu item that can be selected or deselected. If selected, the menu
 * item typically appears with a checkmark next to it. If unselected or
 * deselected, the menu item appears without a checkmark. Like a regular
 * menu item, a check box menu item can have either text or a graphic
 * icon associated with it, or both.
 * 
 * @author René Jahn
 * @see JCheckBoxMenuItem
 */
public class JVxCheckBoxMenuItem extends JCheckBoxMenuItem
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Creates an initially unselected check box menu item with no set text or icon.
     */
	public JVxCheckBoxMenuItem()
	{
		super();
	}
	
    /**
     * Creates a menu item whose properties are taken from the Action supplied.
     *
     * @param pAction the action of the CheckBoxMenuItem
     */
	public JVxCheckBoxMenuItem(Action pAction)
	{
		super(pAction);
	}
	
    /**
     * Creates an initially unselected check box menu item with an icon.
     *
     * @param pIcon the icon of the CheckBoxMenuItem.
     */
	public JVxCheckBoxMenuItem(Icon pIcon)
	{
		super(pIcon);
	}
	
    /**
     * Creates an initially unselected check box menu item with text.
     *
     * @param pText the text of the CheckBoxMenuItem
     */
	public JVxCheckBoxMenuItem(String pText)
	{
		super(pText);
	}
	
    /**
     * Creates a check box menu item with the specified text and selection state.
     *
     * @param pText the text of the check box menu item.
     * @param pSelected the selected state of the check box menu item
     */
	public JVxCheckBoxMenuItem(String pText, boolean pSelected)
	{
		super(pText, pSelected);
	}
	
    /**
     * Creates an initially unselected check box menu item with the specified text and icon.
     *
     * @param pText the text of the CheckBoxMenuItem
     * @param pIcon the icon of the CheckBoxMenuItem
     */
	public JVxCheckBoxMenuItem(String pText, Icon pIcon)
	{
		super(pText, pIcon);
	}
	
    /**
     * Creates a check box menu item with the specified text, icon, and selection state.
     *
     * @param pText the text of the check box menu item
     * @param pIcon the icon of the check box menu item
     * @param pSelected the selected state of the check box menu item
     */
	public JVxCheckBoxMenuItem(String pText, Icon pIcon, boolean pSelected)
	{
		super(pText, pIcon, pSelected);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Icon getIcon()
	{
		if (isSelected() && getSelectedIcon() != null)
		{
			return getSelectedIcon();
		}
		else
		{
			return super.getIcon();
		}
	}
	
}	// JVxCheckBoxMenuItem
