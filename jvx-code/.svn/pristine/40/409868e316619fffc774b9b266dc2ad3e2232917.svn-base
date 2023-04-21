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
 * 08.12.2008 - [JR] - used JVxToggleButton instead of JToggleButton
 */
package com.sibvisions.rad.ui.swing.impl.component;

import javax.rad.ui.component.IToggleButton;
import javax.swing.JToggleButton;

import com.sibvisions.rad.ui.swing.ext.JVxToggleButton;

/**
 * The <code>SwingToggleButton</code> is the <code>IToggleButton</code>
 * implementation for swing.
 * 
 * @param <C> instance of JToggleButton
 * 
 * @author Martin Handsteiner
 * @see	javax.swing.JToggleButton
 * @see javax.rad.ui.component.IToggleButton
 */
public class SwingToggleButton<C extends JToggleButton> extends SwingAbstractButton<C> 
                                                        implements IToggleButton
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>SwingToggleButton</code>.
	 */
	public SwingToggleButton()
	{
		super((C)new JVxToggleButton(), true);
	}

	/**
	 * Creates a new instance of <code>SwingToggleButton</code>.
	 * 
	 * @param pToggleButton the instance of JToggleButton.
	 * @param pDummyImage <code>true</code> to initializes a dummy image
	 */
	protected SwingToggleButton(C pToggleButton, boolean pDummyImage)
	{
		super(pToggleButton, pDummyImage);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public boolean isSelected()
	{
		return resource.isSelected();
	}

	/**
	 * {@inheritDoc}
	 */
    public void setSelected(boolean pPressed)
    {
    	resource.setSelected(pPressed);
    }
    
}	// SwingToggleButton
