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
 * 15.11.2008 - [HM] - creation
 * 24.10.2012 - [JR] - #604: added constructor
 */
package javax.rad.genui.layout;

import javax.rad.genui.UIFactoryManager;
import javax.rad.genui.UILayout;
import javax.rad.ui.layout.IBorderLayout;

/**
 * Platform and technology independent BorderLayout.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF,... .
 * 
 * @author Martin Handsteiner
 */
public class UIBorderLayout extends UILayout<IBorderLayout, String> 
                            implements IBorderLayout
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>UIBorderLayout</code>.
     *
     * @see IBorderLayout
     */
	public UIBorderLayout()
	{
		this(UIFactoryManager.getFactory().createBorderLayout());
	}

    /**
     * Creates a new instance of <code>UIBorderLayout</code> with the given
     * layout.
     *
     * @param pLayout the layout
     * @see IBorderLayout
     */
	protected UIBorderLayout(IBorderLayout pLayout)
	{
		super(pLayout);
	}
	
    /**
     * Creates a new instance of <code>UIBorderLayout</code>.
     *
     * @param pHorizontalGap the horizontal gap between components.
     * @param pVerticalGap the vertical gap between components.
     * 
     * @see IBorderLayout
     */
	public UIBorderLayout(int pHorizontalGap, int pVerticalGap)
	{
		this();
		
		setHorizontalGap(pHorizontalGap);
		setVerticalGap(pVerticalGap);
	}

}	// UIBorderLayout
