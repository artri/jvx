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
 * 18.01.2013 - [SW] - creation
 */
package com.sibvisions.rad.ui.vaadin.impl.layout;

import javax.rad.ui.IComponent;
import javax.rad.ui.ILayout;

/**
 * The <code>IVaadinLayout</code> defines additional vaadin layouting methods. 
 * That's needed because Vaadin widgets holds it's children in the layout.
 * 
 * @author Stefan Wurm
 * @param <CO> the type of the layout constraints.
 */
public interface IVaadinLayout<CO> extends ILayout<CO>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Adds a component to the layout at the index.
	 * 
	 * @param pComponent the component
	 * @param pConstraints the constraints
	 * @param pIndex the index.
	 */
	public void addComponent(IComponent pComponent, Object pConstraints, int pIndex);
	
	/**
	 * Removes the component from the layout.
	 * 
	 * @param pComponent the component
	 */
	public void removeComponent(IComponent pComponent);
	
	/**
	 * Forces the layout to apply layout logic to all its child components.
	 */
	public void markDirty();
	
	/**
	 * Marks that the child components have changed.
	 */
	public void markComponentsChanged();
	
} 	// IVaadinLayout
