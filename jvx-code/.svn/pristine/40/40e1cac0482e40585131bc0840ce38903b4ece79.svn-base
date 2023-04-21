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
 * 16.11.2008 - [HM] - creation
 * 24.10.2012 - [JR] - #604: added constructor
 */
package javax.rad.genui.component;

import javax.rad.genui.UIFactoryManager;
import javax.rad.ui.IImage;
import javax.rad.ui.component.IToggleButton;
import javax.rad.ui.event.IActionListener;
import javax.rad.util.IRunnable;

/**
 * Platform and technology independent toggle button.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 * @see	java.awt.Button
 * @see	javax.swing.JToggleButton
 */
public class UIToggleButton extends AbstractUIToggleButton<IToggleButton> 
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>UIToggleButton</code>.
     *
     * @see IToggleButton
     */
	public UIToggleButton()
	{
		this(UIFactoryManager.getFactory().createToggleButton());
	}
	
    /**
     * Creates a new instance of <code>UIToggleButton</code> with the given
     * toggle button.
     *
     * @param pButton te toggle button
     * @see IToggleButton
     */
	protected UIToggleButton(IToggleButton pButton)
	{
		super(pButton);
	}
	
    /**
     * Creates a new instance of <code>UIToggleButton</code>.
     *
     * @param pText the label of the button.
     * @see IToggleButton
     */
	public UIToggleButton(String pText)
	{
		this();

		setText(pText);
	}

	
	/**
	 * Creates a new instance of {@link UIToggleButton}.
	 *
	 * @param pText the {@link String text}.
	 * @param pActionListener the {@link IActionListener action listener}.
	 * @see #eventAction()
	 * @see #setText(String)
	 */
	public UIToggleButton(String pText, IActionListener pActionListener)
	{
		this();
		
		setText(pText);
		eventAction().addListener(pActionListener);
	}
	
    /**
     * Creates a new instance of {@link UIToggleButton}.
     *
     * @param pText the {@link String text}.
     * @param pActionListener the {@link IRunnable action listener}.
     * @see #eventAction()
     * @see #setText(String)
     */
    public UIToggleButton(String pText, IRunnable pActionListener)
    {
        this();
        
        setText(pText);
        eventAction().addListener(pActionListener);
    }
    
	/**
	 * Creates a new instance of {@link UIToggleButton}.
	 *
	 * @param pText the {@link String text}.
	 * @param pListener the {@link Object listener}.
	 * @param pMethodName the {@link String method name}.
	 * @see #eventAction()
	 * @see #setText(String)
	 */
	public UIToggleButton(String pText, Object pListener, String pMethodName)
	{
		this();
		
		setText(pText);
		eventAction().addListener(pListener, pMethodName);
	}
	
	/**
	 * Creates a new instance of {@link UIToggleButton}.
	 *
	 * @param pText the {@link String text}.
	 * @param pImage the {@link IImage image}.
	 * @param pActionListener the {@link IActionListener action listener}.
	 * @see #eventAction()
	 * @see #setImage(IImage)
	 * @see #setText(String)
	 */
	public UIToggleButton(String pText, IImage pImage, IActionListener pActionListener)
	{
		this();
		
		setText(pText);
		setImage(pImage);
		eventAction().addListener(pActionListener);
	}
	
    /**
     * Creates a new instance of {@link UIToggleButton}.
     *
     * @param pText the {@link String text}.
     * @param pImage the {@link IImage image}.
     * @param pActionListener the {@link IRunnable action listener}.
     * @see #eventAction()
     * @see #setImage(IImage)
     * @see #setText(String)
     */
    public UIToggleButton(String pText, IImage pImage, IRunnable pActionListener)
    {
        this();
        
        setText(pText);
        setImage(pImage);
        eventAction().addListener(pActionListener);
    }
    
	/**
	 * Creates a new instance of {@link UIToggleButton}.
	 *
	 * @param pText the {@link String text}.
	 * @param pImage the {@link IImage image}.
	 * @param pListener the {@link Object listener}.
	 * @param pMethodName the {@link String method name}.
	 * @see #eventAction()
	 * @see #setImage(IImage)
	 * @see #setText(String)
	 */
	public UIToggleButton(String pText, IImage pImage, Object pListener, String pMethodName)
	{
		this();
		
		setText(pText);
		setImage(pImage);
		eventAction().addListener(pListener, pMethodName);
	}

}	// UIToggleButton
