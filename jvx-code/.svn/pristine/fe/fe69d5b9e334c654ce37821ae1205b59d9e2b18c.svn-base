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
 * 03.05.2009 - [JR] - add/removeNotify
 * 11.06.2009 - [JR] - pack: call addNotify before call pack
 * 21.07.2009 - [JR] - isDisposed implemented 
 * 01.09.2009 - [JR] - extends AbstractWindow
 * 03.10.2011 - [JR] - setVisible removed: super class handles addNotify
 */
package javax.rad.genui.container;

import javax.rad.genui.UIFactoryManager;
import javax.rad.ui.container.IWindow;

/**
 * Platform and technology independent Window.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 */
public class UIWindow extends AbstractWindow<IWindow> 
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>UIWindow</code>.
     *
     * @see IWindow
     */
	public UIWindow()
	{
		super(UIFactoryManager.getFactory().createWindow());
	}

    /**
     * Creates a new instance of <code>UIWindow</code> with the given
     * window.
     *
     * @param pWindow the window
     * @see IWindow
     */
	protected UIWindow(IWindow pWindow)
	{
		super(pWindow);
	}
	
}	// UIWindow
