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
 * 04.06.2009 - [JR] - setParent overwritten
 * 22.12.2010 - [JR] - setParent: allow null
 * 23.07.2018 - [JR] - hide resource in mac mode
 */
package com.sibvisions.rad.ui.swing.impl.menu;

import javax.rad.ui.IContainer;
import javax.rad.ui.container.IFrame;
import javax.rad.ui.menu.IMenuBar;
import javax.swing.JMenuBar;

import com.sibvisions.rad.ui.swing.impl.SwingComponent;
import com.sibvisions.rad.ui.swing.impl.SwingFactory;

/**
 * The <code>SwingMenuBar</code> is the <code>IMenuBar</code>
 * implementation for swing.
 * 
 * @author Martin Handsteiner
 * @see	javax.swing.JMenuBar
 * @see javax.rad.ui.menu.IMenuBar
 */
public class SwingMenuBar extends SwingComponent<JMenuBar> 
                          implements IMenuBar
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>SwingMenuBar</code>.
	 */
	public SwingMenuBar()
	{
		super(new JMenuBar());
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setParent(IContainer pParent)
	{
		if (pParent == null || pParent instanceof IFrame)
		{
			parent = pParent;
		}
		else
		{
			throw new IllegalArgumentException("Only 'IFrame' instances are allowed as parent of a 'SwingMenuBar'");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setVisible(boolean pVisible)
	{
		super.setVisible(pVisible);
		
		boolean bMacBar = SwingFactory.isMacLaF() && Boolean.getBoolean("apple.laf.useScreenMenuBar");
		
		if (bMacBar)
		{
			((JMenuBar)resource).setVisible(false);
		}
	}

}	// SwingMenuBar
