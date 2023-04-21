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
 * 26.03.2013 - [JR] - creation
 */
package com.sibvisions.rad.ui.vaadin.impl;

import javax.rad.ui.IComponent;
import javax.rad.ui.IContainer;

/**
 * The <code>IVaadinContainer</code> class extends {@link IContainer} and adds features for adding and removing
 * components to and from vaadin.
 * 
 * @author René Jahn
 * @see IContainer
 */
public interface IVaadinContainer extends IContainer
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Method definitions
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
	
	/**
	 * Adds a component to the vaadin internal component.
	 * 
	 * @param pComponent the component
	 * @param pConstraints the constraints
	 * @param pIndex the index
	 */
	public void addToVaadin(IComponent pComponent, Object pConstraints, int pIndex);
	
	/**
	 * Removes a component from the vaadin internal component.
	 * 
	 * @param pComponent the component
	 */
	public void removeFromVaadin(IComponent pComponent);
	
	/**
	 * Forces the container layout to apply layout logic to all its child components.
	 */
	public void performLayout();
	
}	// IVaadinContainer
