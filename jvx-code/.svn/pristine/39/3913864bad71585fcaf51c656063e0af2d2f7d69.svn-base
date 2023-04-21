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
package com.sibvisions.rad.ui.swing.impl.menu;

import java.awt.Component;

import javax.rad.ui.menu.IMenu;
import javax.swing.JMenu;

/**
 * The <code>SwingMenu</code> is the <code>IMenu</code>
 * implementation for swing.
 * 
 * @author Martin Handsteiner
 * @see	javax.swing.JMenu
 * @see javax.rad.ui.menu.IMenu
 */
public class SwingMenu extends SwingMenuItem<JMenu> 
                       implements IMenu
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>SwingMenu</code>.
	 */
	public SwingMenu()
	{
		super(new JMenu());
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
		add(new SwingSeparator(), null, pIndex);
    }
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
	 * {@inheritDoc}
	 */
	protected void addIntern(Component pComponent, Object pConstraints, int pIndex)
	{
		resource.getPopupMenu().add(pComponent, pConstraints, pIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	protected void removeIntern(Component pComponent)
	{
		resource.getPopupMenu().remove(pComponent);
	}
	
}	// SwingMenu
