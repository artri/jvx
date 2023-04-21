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
 * 23.10.2012 - [CB] - creation
 */
package com.sibvisions.rad.ui.vaadin.impl.component;

import javax.rad.ui.component.IToggleButton;

import com.sibvisions.rad.ui.vaadin.ext.ui.AccessibilityUtil;
import com.sibvisions.rad.ui.vaadin.ext.ui.MouseOverButton;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

/**
 * The <code>VaadinToggleButton</code> is the vaadin implementation of {@link IToggleButton}.
 * 
 * @author Benedikt Cermak
 */
public class VaadinToggleButton extends AbstractVaadinButton 
							    implements IToggleButton
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class Members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** State of the ToggleButton, true if pressed or selected. */
	private boolean	selected	= false;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>VaadinToggleButton</code>.
	 * 
	 * @see javax.rad.ui.component.IToggleButton
	 */
	public VaadinToggleButton()
	{
		this(null);
	}

	/**
	 * Creates a new instance of <code>VaadinToggleButton</code>.
	 * 
	 * @see javax.rad.ui.component.IToggleButton
	 * @param pText the button text.
	 */
	public VaadinToggleButton(String pText)
	{
		super(new MouseOverButton(pText == null ? "" : pText));

		resource.addStyleName("jvxtogglebutton");
		
		resource.addClickListener(new ClickListener()
		{
			public void buttonClick(ClickEvent pEvent)
			{
			    setSelected(!isSelected());
			}
		});
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface Implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public boolean isSelected()
	{
		return selected;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setSelected(boolean pSelected)
	{
	    selected = pSelected;
	    
		if (selected)
		{
            if (!isBorderPainted())
            {
                removeInternStyleName("v-button-noborder");
            }
			addInternStyleName("pressed");
		}
		else
		{
            if (!isBorderPainted())
            {
                addInternStyleName("v-button-noborder");
            }
			removeInternStyleName("pressed");
		}
		
		AccessibilityUtil.setPressed(getAttributesExtension(), selected);
	}

} 	// VaadinToggleButton
