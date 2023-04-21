/*
 * Copyright 2011 SIB Visions GmbH
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
 * 10.05.2011 - [JR] - creation
 * 24.10.2012 - [JR] - #604: added constructor
 * 17.10.2015 - [JR] - added UILayout constructor
 */
package javax.rad.genui.container;

import javax.rad.genui.UIContainer;
import javax.rad.genui.UIFactoryManager;
import javax.rad.genui.UILayout;
import javax.rad.genui.layout.UIFlowLayout;
import javax.rad.ui.container.IScrollPanel;

/**
 * Platform and technology independent ScrollPanel.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF,... .
 * 
 * @author René Jahn
 */
public class UIScrollPanel extends UIContainer<IScrollPanel> 
                           implements IScrollPanel
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>UIScrollPanel</code>.
     *
     * @see IScrollPanel
     */
	public UIScrollPanel()
	{
		this(UIFactoryManager.getFactory().createScrollPanel());
	}

    /**
     * Creates a new instance of <code>UIScrollPanel</code> with the given
     * panel.
     *
     * @param pPanel the panel
     * @see IScrollPanel
     */
	protected UIScrollPanel(IScrollPanel pPanel)
	{
		super(pPanel);
		
		UIFlowLayout flDefault = new UIFlowLayout();
		flDefault.setMargins(5, 5, 5, 5);
		
		setLayout(flDefault);
	}
	
    /**
     * Creates a new instance of <code>UIScrollPanel</code>.
     *
     * @param pLayout the layout.
     * @see IScrollPanel
     */
    public UIScrollPanel(UILayout pLayout)
    {
        super(UIFactoryManager.getFactory().createScrollPanel());
        
        setLayout(pLayout);
    }	
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

}	// UIScrollPanel
