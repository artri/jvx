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
 * 12.10.2008 - [JR] - setTabMode implemented
 * 20.02.2009 - [JR] - navigation keys en/disable
 */
package com.sibvisions.rad.ui.swing.impl.container;

import javax.rad.ui.container.IDesktopPanel;

import com.sibvisions.rad.ui.swing.ext.JVxDesktopPane;
import com.sibvisions.rad.ui.swing.ext.focus.TabIndexFocusTraversalPolicy;
import com.sibvisions.rad.ui.swing.impl.SwingComponent;

/**
 * The <code>SwingDesktopPanel</code> is the <code>IDesktopPanel</code>
 * implementation for swing.
 * 
 * @author Martin Handsteiner
 * @see	javax.swing.JDesktopPane
 * @see IDesktopPanel
 */
public class SwingDesktopPanel extends SwingComponent<JVxDesktopPane> 
                        	   implements IDesktopPanel 
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>SwingDesktopPanel</code>.
	 */
	public SwingDesktopPanel()
	{
		super(new JVxDesktopPane());
		
		resource.setTabsDraggable(true);
		
		resource.setFocusTraversalPolicy(new TabIndexFocusTraversalPolicy());
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public void setTabMode(boolean pTabMode)
	{
		resource.setTabMode(pTabMode);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isTabMode()
	{
		return resource.isTabMode();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setNavigationKeysEnabled(boolean pEnabled)
	{
		resource.setNavigationKeysEnabled(pEnabled);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isNavigationKeysEnabled()
	{
		return resource.isNavigationKeysEnabled();
	}
	
}	// SwingDesktopPanel
