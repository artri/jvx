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
 * 01.10.2008 - [HM] - creation
 * 11.10.2009 - [JR] - used JVxPanel instead of JPanel
 *                   - get/setBackgroundImage implemented
 */
package com.sibvisions.rad.ui.swing.impl.container;

import javax.rad.ui.IImage;
import javax.rad.ui.container.IPanel;
import javax.swing.ImageIcon;

import com.sibvisions.rad.ui.swing.ext.JVxPanel;
import com.sibvisions.rad.ui.swing.impl.SwingComponent;

/**
 * The <code>SwingPanel</code> is the <code>IPanel</code>
 * implementation for swing.
 * 
 * @author Martin Handsteiner
 * @see	javax.swing.JPanel
 * @see IPanel
 */
public class SwingPanel extends SwingComponent<JVxPanel> 
                        implements IPanel 
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the background image. */
	private IImage imgBack = null;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>SwingPanel</code>.
	 */
	public SwingPanel()
	{
		super(new JVxPanel());
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public void setBackgroundImage(IImage pImage)
	{
		imgBack = pImage;
		
		if (pImage == null)
		{
			resource.setBackgroundImage(null);
		}
		else
		{
			resource.setBackgroundImage((ImageIcon)pImage.getResource());
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public IImage getBackgroundImage()
	{
		return imgBack;
	}
	
}	// SwingPanel
