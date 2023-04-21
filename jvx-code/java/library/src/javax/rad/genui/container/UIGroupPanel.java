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
 * 27.11.2008 - [HM] - creation
 * 09.10.2009 - [JR] - constructor with String parameter
 * 08.01.2011 - [JR] - #235: default constructor: UIFlowLayout with margins 5
 * 24.10.2012 - [JR] - #604: added constructor
 * 05.04.2014 - [JR] - #1001: don't change text if translation is disabled
 */
package javax.rad.genui.container;

import javax.rad.genui.UIContainer;
import javax.rad.genui.UIFactoryManager;
import javax.rad.genui.UILayout;
import javax.rad.ui.container.IGroupPanel;

/**
 * Platform and technology independent GroupPanel.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF,... .
 * 
 * @author Martin Handsteiner
 */
public class UIGroupPanel extends UIContainer<IGroupPanel> 
                          implements IGroupPanel
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the text. */
	private transient String sText;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
	/**
     * Creates a new instance of <code>UIGroupPanel</code>.
     *
     * @see IGroupPanel
     */
	public UIGroupPanel()
	{
		this(UIPanel.createDefaultLayout(), null);
	}

	/**
	 * Creates a new instance of <code>UIGroupPanel</code> with a specific layout
	 * and text.
	 * 
	 * @param pLayout the layout to use
	 * @param pText the text to display as title
	 * @see #setLayout(javax.rad.ui.ILayout)
	 * @see #setText(String)
	 */
	public UIGroupPanel(UILayout pLayout, String pText)
	{
		super(UIFactoryManager.getFactory().createGroupPanel());
		
		setLayout(pLayout);
		
		//not necessary
		if (pText != null)
		{
			setText(pText);
		}
	}

	/**
	 * Creates a new instance of <code>UIGroupPanel</code> with a specific layout.
	 * 
	 * @param pLayout the layout to use
	 * @see #setLayout(javax.rad.ui.ILayout)
	 */
	public UIGroupPanel(UILayout pLayout)
	{
		this(pLayout, null);
	}

	/**
	 * Creates a new instance of <code>UIGroupPanel</code> with a specific text.
	 * 
	 * @param pText the text to display as title
	 * @see #setText(String)
	 */
	public UIGroupPanel(String pText)
	{
		this(UIPanel.createDefaultLayout(), pText);
	}

	/**
     * Creates a new instance of <code>UIGroupPanel</code> with the given
     * group panel.
     *
     * @param pPanel the group panel
     * @see IGroupPanel
     */
	protected UIGroupPanel(IGroupPanel pPanel)
	{
		super(pPanel);
		
		setLayout(UIPanel.createDefaultLayout());
	}	

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public String getText()
	{
		return sText;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setText(String pText)
	{
		sText = pText;
		
		//maybe null if the component is not added
		uiResource.setText(translate(pText));
	}

	/**
	 * {@inheritDoc}
	 */
	public int getHorizontalAlignment()
	{
		return uiResource.getHorizontalAlignment();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setHorizontalAlignment(int pHorizontalAlignment)
	{
		uiResource.setHorizontalAlignment(pHorizontalAlignment);
	}

	/**
	 * {@inheritDoc}
	 */
	public int getVerticalAlignment()
	{
		return uiResource.getVerticalAlignment();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setVerticalAlignment(int pVerticalAlignment)
	{
		uiResource.setVerticalAlignment(pVerticalAlignment);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateTranslation()
	{
		boolean bChanged = isTranslationChanged();

		super.updateTranslation();
		
		if (bTranslate && bChanged)
		{
			uiResource.setText(translate(sText));
		}
	}

}	// UIGroupPanel
