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
 * 17.11.2008 - [HM] - creation
 * 10.12.2008 - [JR] - implemented navigation key methods
 * 18.12.2008 - [JR] - removed navigation key methods
 * 20.02.2009 - [JR] - added navigation key method 
 * 24.10.2012 - [JR] - #604: added constructor
 */
package javax.rad.genui.container;

import javax.rad.genui.UIContainer;
import javax.rad.genui.UIFactoryManager;
import javax.rad.genui.layout.UIBorderLayout;
import javax.rad.ui.container.IDesktopPanel;

/**
 * Platform and technology independent DesktopPanel.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF,... .
 * 
 * @author Martin Handsteiner
 */
public class UIDesktopPanel extends UIContainer<IDesktopPanel> 
                            implements IDesktopPanel
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Creates a new instance of <code>UIDesktopPanel</code>.
     *
     * @see IDesktopPanel
     */
	public UIDesktopPanel()
	{
		this(UIFactoryManager.getFactory().createDesktopPanel());
	}

    /**
     * Creates a new instance of <code>UIDesktopPanel</code> with the given
     * desktop panel.
     *
     * @param pDesktopPanel the desktop panel
     * @see IDesktopPanel
     */
	protected UIDesktopPanel(IDesktopPanel pDesktopPanel)
	{
		super(pDesktopPanel);
		
		setLayout(new UIBorderLayout());
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public void setTabMode(boolean pTabMode)
    {
    	uiResource.setTabMode(pTabMode);
    }
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isTabMode()
    {
    	return uiResource.isTabMode();
    }
	
	/**
	 * {@inheritDoc}
	 */
	public void setNavigationKeysEnabled(boolean pEnabled)
	{
		uiResource.setNavigationKeysEnabled(pEnabled);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isNavigationKeysEnabled()
	{
		return uiResource.isNavigationKeysEnabled();
	}
	
}	// UIDesktopPanel
