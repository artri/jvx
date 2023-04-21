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
 * 11.08.2009 - [JR] - used JVxCheckBoxMenuItem
 */
package com.sibvisions.rad.ui.swing.impl.menu;

import javax.rad.ui.IImage;
import javax.rad.ui.menu.ICheckBoxMenuItem;

import com.sibvisions.rad.ui.swing.ext.JVxCheckBoxMenuItem;

/**
 * The <code>SwingCheckBoxMenuItem</code> is the <code>ICheckBoxMenuItem</code>
 * implementation for swing.
 * 
 * @author Martin Handsteiner
 * @see	javax.swing.JCheckBoxMenuItem
 * @see javax.rad.ui.menu.ICheckBoxMenuItem
 */
public class SwingCheckBoxMenuItem extends SwingMenuItem<JVxCheckBoxMenuItem> 
                                   implements ICheckBoxMenuItem
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>SwingCheckBoxMenuItem</code>.
	 */
	public SwingCheckBoxMenuItem()
	{
		super(new JVxCheckBoxMenuItem());
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
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * {@inheritDoc}
     */
	@Override
	public void setPressedImage(IImage pImage)
	{
		super.setPressedImage(pImage);
		resource.setSelectedIcon(resource.getPressedIcon());
	}
    
}	// SwingCheckBoxMenuItem
