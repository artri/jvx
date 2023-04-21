/*
 * Copyright 2012 SIB Visions GmbH
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
 * 22.01.2013 - [SW] - creation
 */
package com.sibvisions.rad.ui.vaadin.impl.menu;

import javax.rad.ui.IImage;
import javax.rad.ui.menu.ICheckBoxMenuItem;

/**
 * The <code>VaadinCheckBoxMenuItem</code> class is the vaadin implementation of {@link ICheckBoxMenuItem}.
 * 
 * @author Stefan Wurm
 */
public class VaadinCheckBoxMenuItem extends VaadinMenuItem 
                                    implements ICheckBoxMenuItem
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * new instance.
	 */
	public VaadinCheckBoxMenuItem() 
	{
		super();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~		
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isSelected()
	{
		return resource.isChecked();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setSelected(boolean pPressed)
	{
		resource.setChecked(pPressed);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setPressedImage(IImage pImage)
	{
		// not supported
	}

	/**
	 * {@inheritDoc}
	 */
	public IImage getPressedImage()
	{
		// not supported
		
		return null;
	}

} 	// VaadinCheckBoxMenuItem
