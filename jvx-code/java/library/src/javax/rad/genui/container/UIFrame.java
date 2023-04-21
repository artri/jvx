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
 * 09.04.2009 - [JR] - used InternalToolBarPanel as toolbar container
 * 04.06.2009 - [JR] - add/removeNotify
 * 01.09.2009 - [JR] - extends AbstractFrame
 * 24.10.2012 - [JR] - #604: added constructor
 */
package javax.rad.genui.container;

import javax.rad.genui.UIFactoryManager;
import javax.rad.genui.layout.UIBorderLayout;
import javax.rad.ui.container.IFrame;

/**
 * Platform and technology independent Frame.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF,... .
 * 
 * @author Martin Handsteiner
 */
public class UIFrame extends AbstractFrame<IFrame> 
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>UIFrame</code>.
     *
     * @see IFrame
     */
	public UIFrame()
	{
		this(UIFactoryManager.getFactory().createFrame());
	}

    /**
     * Creates a new instance of <code>UIFrame</code> with the given
     * frame.
     *
     * @param pFrame the frame
     * @see IFrame
     */
	protected UIFrame(IFrame pFrame)
	{
		super(pFrame);
		
		setLayout(new UIBorderLayout());
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setVisible(boolean pVisible)
	{
    	if (pVisible && !isNotified())
    	{
			addNotify();
    	}

    	super.setVisible(pVisible);
	}	
	
}	// UIFrame
