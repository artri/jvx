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
 * 11.10.2009 - [JR] - get/setBackgroundImage implemented
 * 08.01.2011 - [JR] - #235: default constructor: UIFlowLayout with margins 5
 * 11.01.2011 - [JR] - createDefaultLayout implemented to avoid getLayout access in constructor 
 *                     (possible problems in inherited classes)
 * 24.10.2012 - [JR] - #604: added constructor
 */
package javax.rad.genui.container;

import javax.rad.genui.UIContainer;
import javax.rad.genui.UIFactoryManager;
import javax.rad.genui.UIImage;
import javax.rad.genui.UIInsets;
import javax.rad.genui.UILayout;
import javax.rad.genui.layout.UIFlowLayout;
import javax.rad.ui.IImage;
import javax.rad.ui.container.IPanel;

/**
 * Platform and technology independent Panel.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF,... .
 * 
 * @author Martin Handsteiner
 */
public class UIPanel extends UIContainer<IPanel> 
                     implements IPanel
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the background image. */ 
	protected transient IImage imgBack = null;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>UIPanel</code>.
     *
     * @see IPanel
     */
	public UIPanel()
	{
		this(createDefaultLayout());
	}
	
    /**
     * Creates a new instance of <code>UIPanel</code> with the given 
     * panel.
     *
     * @param pPanel the panel.
     * @see IPanel
     */
	protected UIPanel(IPanel pPanel)
	{
		super(pPanel);
		
		setLayout(createDefaultLayout());
	}

	/**
     * Creates a new instance of <code>UIPanel</code>.
     *
     * @param pLayout the layout.
     * @see IPanel
     */
	public UIPanel(UILayout pLayout)
	{
		super(UIFactoryManager.getFactory().createPanel());
		
		setLayout(pLayout);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public void setBackgroundImage(IImage pImage)
	{
		// ensure that the factory gets his own resource, to prevent exception on factory internal casts!
		if (pImage instanceof UIImage)
		{
	    	uiResource.setBackgroundImage(((UIImage)pImage).getUIResource());
		}
		else
		{
	    	uiResource.setBackgroundImage(pImage);
		}
		
		imgBack = pImage;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public IImage getBackgroundImage()
	{
		return imgBack;
	}
	
	/**
	 * Creates the default layout for this panel.
	 * 
	 * @return the default layout
	 */
	public static UILayout createDefaultLayout()
	{
		UIFlowLayout fl = new UIFlowLayout();		

		//defaults
		fl.setMargins(new UIInsets(5, 5, 5, 5));
		
		return fl;
	}
	
}	// UIPanel
