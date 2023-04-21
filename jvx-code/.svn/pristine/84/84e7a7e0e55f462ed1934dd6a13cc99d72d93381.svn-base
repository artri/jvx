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
 * 08.12.2008 - [JR] - default button functionality implemented
 * 10.12.2008 - [JR] - moved accelerator methods from UIMenuItem
 * 24.10.2012 - [JR] - #604: added constructor
 * 22.09.2014 - [RZ] - #1103: the name is now created from the listeners or action command.
 */
package javax.rad.genui.component;

import javax.rad.genui.UIFactoryManager;
import javax.rad.ui.IImage;
import javax.rad.ui.component.IButton;
import javax.rad.ui.event.IActionListener;
import javax.rad.util.IRunnable;

/**
 * Platform and technology independent button.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 * 
 */
public class UIButton extends AbstractUIButton<IButton> 
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>UIButton</code>.
     *
     * @see IButton
     */
	public UIButton()
	{
		this(UIFactoryManager.getFactory().createButton());
	}

    /**
     * Creates a new instance of <code>UIButton</code> with the given
     * button.
     *
     * @param pButton the button
     * @see IButton
     */
	protected UIButton(IButton pButton)
	{
		super(pButton);
	}	
	
    /**
     * Creates a new instance of <code>UIButton</code>.
     *
     * @param pText the label of the button.
     * @see IButton
     */
	public UIButton(String pText)
	{
		this();
		
		setText(pText);
	}

	/**
	 * Creates a new instance of {@link UIButton}.
	 *
	 * @param pText the {@link String text}.
	 * @param pImage the {@link IImage image}.
	 * @see #setImage(IImage)
	 * @see #setText(String)
	 */
	public UIButton(String pText, IImage pImage)
	{
		this();
		
		setText(pText);
		setImage(pImage);
	}
	
	/**
	 * Creates a new instance of {@link UIButton}.
	 *
	 * @param pText the {@link String text}.
	 * @param pActionListener the {@link IActionListener action listener}.
	 * @see #eventAction()
	 * @see #setText(String)
	 */
	public UIButton(String pText, IActionListener pActionListener)
	{
		this();
		
		setText(pText);
		eventAction().addListener(pActionListener);
	}
	
    /**
     * Creates a new instance of {@link UIButton}.
     *
     * @param pText the {@link String text}.
     * @param pActionListener the {@link IRunnable action listener}.
     * @see #eventAction()
     * @see #setText(String)
     */
    public UIButton(String pText, IRunnable pActionListener)
    {
        this();
        
        setText(pText);
        eventAction().addListener(pActionListener);
    }
    
	/**
	 * Creates a new instance of {@link UIButton}.
	 *
	 * @param pText the {@link String text}.
	 * @param pListener the {@link Object listener}.
	 * @param pMethodName the {@link String method name}.
	 * @see #eventAction()
	 * @see #setText(String)
	 */
	public UIButton(String pText, Object pListener, String pMethodName)
	{
		this();
		
		setText(pText);
		eventAction().addListener(pListener, pMethodName);
	}
	
	/**
	 * Creates a new instance of {@link UIButton}.
	 *
	 * @param pText the {@link String text}.
	 * @param pImage the {@link IImage image}.
	 * @param pActionListener the {@link IActionListener action listener}.
	 * @see #eventAction()
	 * @see #setImage(IImage)
	 * @see #setText(String)
	 */
	public UIButton(String pText, IImage pImage, IActionListener pActionListener)
	{
		this();
		
		setText(pText);
		setImage(pImage);
		eventAction().addListener(pActionListener);
	}
	
    /**
     * Creates a new instance of {@link UIButton}.
     *
     * @param pText the {@link String text}.
     * @param pImage the {@link IImage image}.
     * @param pActionListener the {@link IRunnable action listener}.
     * @see #eventAction()
     * @see #setImage(IImage)
     * @see #setText(String)
     */
    public UIButton(String pText, IImage pImage, IRunnable pActionListener)
    {
        this();
        
        setText(pText);
        setImage(pImage);
        eventAction().addListener(pActionListener);
    }
    
	/**
	 * Creates a new instance of {@link UIButton}.
	 *
	 * @param pText the {@link String text}.
	 * @param pImage the {@link IImage image}.
	 * @param pListener the {@link Object listener}.
	 * @param pMethodName the {@link String method name}.
	 * @see #eventAction()
	 * @see #setImage(IImage)
	 * @see #setText(String)
	 */
	public UIButton(String pText, IImage pImage, Object pListener, String pMethodName)
	{
		this();
		
		setText(pText);
		setImage(pImage);
		eventAction().addListener(pListener, pMethodName);
	}
	
}	// UIButton
