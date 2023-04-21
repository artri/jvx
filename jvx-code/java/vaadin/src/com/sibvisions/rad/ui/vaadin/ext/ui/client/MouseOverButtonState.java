/*
 * Copyright 2013 SIB Visions GmbH
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
 * 25.01.2013 - [SW] - creation
 */
package com.sibvisions.rad.ui.vaadin.ext.ui.client;

import com.vaadin.shared.ui.button.ButtonState;

/**
 * The <code>MouseOverButtonState</code> class is the state for {@link com.sibvisions.rad.ui.vaadin.ext.ui.MouseOverButton}.
 * 
 * @author Stefan Wurm
 */
public class MouseOverButtonState extends ButtonState
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
	
	/** true if the mouse listener should be registered. **/
	private boolean registerMouseHandler = false;
	
	/** true if the icon should be before the text. **/
	private boolean iconBeforeText = true;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
	
	/**
	 * Gets whether the mouse handler is registered.
	 * 
	 * @return <code>true</code> if the mouse handler is registered, <code>false</code> otherwise
	 */
	public boolean isRegisterMouseHandler()
	{
		return registerMouseHandler;
	}

	/**
	 * Sets wheter the mouse handler should be registered.
	 * 
	 * @param pRegisterMouseHandler <code>true</code> if the mouse handler should be registered.
	 */
	public void setRegisterMouseHandler(boolean pRegisterMouseHandler)
	{
		registerMouseHandler = pRegisterMouseHandler;
	}

	/**
	 * Gets whether the icon is before the text element.
	 * 
	 * @return <code>true</code> if the icon is before the text element.
	 */
	public boolean isIconBeforeText()
	{
		return iconBeforeText;
	}

	/**
	 * Sets whether the icon should be before the text.
	 * 
	 * @param pIconBeforeText <code>true</code> if the icon is before the text element.
	 */
	public void setIconBeforeText(boolean pIconBeforeText)
	{
		iconBeforeText = pIconBeforeText;
	}
	
} 	// MouseOverButtonState
