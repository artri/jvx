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
 * 10.12.2008 - [JR] - moved accelerator methods to UIButton
 * 24.10.2012 - [JR] - #604: added constructor
 */
package javax.rad.genui.menu;

import javax.rad.genui.UIFactoryManager;
import javax.rad.ui.IImage;
import javax.rad.ui.event.IActionListener;
import javax.rad.ui.menu.IMenuItem;

/**
 * Platform and technology independent MenuItem.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 */
public class UIMenuItem extends AbstractUIMenuItem<IMenuItem> 
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>UIMenuItem</code>.
     *
     * @see IMenuItem
     */
	public UIMenuItem()
	{
		super(UIFactoryManager.getFactory().createMenuItem());
	}
	
    /**
     * Creates a new instance of <code>UIMenuItem</code> with the given
     * menu item.
     *
     * @param pMenuItem the menu item
     * @see IMenuItem
     */
	protected UIMenuItem(IMenuItem pMenuItem)
	{
		super(pMenuItem);
	}

	/**
     * Creates a new instance of <code>UIMenuItem</code>.
     *
     * @param pText the text.
     * @see IMenuItem
     */
	public UIMenuItem(String pText)
	{
		this();
		
		setText(pText);
	}

	/**
	 * Creates a new instance of {@link UIMenuItem}.
	 *
	 * @param pText the {@link String text}.
	 * @param pActionListener the {@link IActionListener action listener}.
	 * @see #eventAction()
	 * @see #setText(String)
	 */
	public UIMenuItem(String pText, IActionListener pActionListener)
	{
		this();
		
		setText(pText);
		eventAction().addListener(pActionListener);
	}

	/**
	 * Creates a new instance of {@link UIMenuItem}.
	 *
	 * @param pText the {@link String text}.
	 * @param pListener the {@link Object listener}.
	 * @param pMethodName the {@link String method name}.
	 * @see #eventAction()
	 * @see #setText(String)
	 */
	public UIMenuItem(String pText, Object pListener, String pMethodName)
	{
		this();
		
		setText(pText);
		eventAction().addListener(pListener, pMethodName);
	}

	/**
	 * Creates a new instance of {@link UIMenuItem}.
	 *
	 * @param pText the {@link String text}.
	 * @param pImage the {@link IImage image}.
	 * @param pActionListener the {@link IActionListener action listener}.
	 * @see #eventAction()
	 * @see #setImage(IImage)
	 * @see #setText(String)
	 */
	public UIMenuItem(String pText, IImage pImage, IActionListener pActionListener)
	{
		this();
		
		setText(pText);
		setImage(pImage);
		eventAction().addListener(pActionListener);
	}

	/**
	 * Creates a new instance of {@link UIMenuItem}.
	 *
	 * @param pText the {@link String text}.
	 * @param pImage the {@link IImage image}.
	 * @param pListener the {@link Object listener}.
	 * @param pMethodName the {@link String method name}.
	 * @see #eventAction()
	 * @see #setImage(IImage)
	 * @see #setText(String)
	 */
	public UIMenuItem(String pText, IImage pImage, Object pListener, String pMethodName)
	{
		this();
		
		setText(pText);
		setImage(pImage);
		eventAction().addListener(pListener, pMethodName);
	}
	
}	// UIMenuItem
