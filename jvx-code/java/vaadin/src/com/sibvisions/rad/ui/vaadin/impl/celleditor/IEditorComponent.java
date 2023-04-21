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
 * 23.04.2013 - [SW] - creation
 */
package com.sibvisions.rad.ui.vaadin.impl.celleditor;

import com.vaadin.event.Action;

/**
 * The <code>IEditorComponent</code> is the interface for editor components. It adds support for action
 * handling.
 * 
 * @author Stefan Wurm
 */
public interface IEditorComponent
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Method definitions
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	

	/**
	 * Handles the actions.
	 * 
	 * @param action the action event.
	 */
	public void handleAction(Action action);
	
}	// IEditorComponent
