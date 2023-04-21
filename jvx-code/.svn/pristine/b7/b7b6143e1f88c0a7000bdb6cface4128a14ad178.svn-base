/*
 * Copyright 2016 SIB Visions GmbH
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
 * 10.03.2016 - [RZ] - creation
 */
package com.sibvisions.rad.ui.vaadin.ext.ui.panel;

import java.util.List;

import com.sibvisions.rad.ui.vaadin.ext.ui.client.panel.helper.Size;
import com.vaadin.ui.Component;

/**
 * The {@link ILayoutedPanel} is the interface for every panel that wants to use
 * {@link ILayout}s.
 * 
 * @author Robert Zenz
 */
public interface ILayoutedPanel
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Adds the given {@link Component} with the given constraint.
	 * 
	 * @param pComponent the {@link Component} to add.
	 * @param pConstraint the constraint to use.
	 */
	public void addComponent(Component pComponent, Object pConstraint);
	
	/**
	 * Adds the given {@link Component} with the given constraint at the given
	 * index.
	 * 
	 * @param pComponent the {@link Component} to add.
	 * @param pConstraint the constraint to use.
	 * @param pIndex the index to insert the component at, can be -1 for
	 *            "at the end".
	 */
	public void addComponent(Component pComponent, Object pConstraint, int pIndex);
	
	/**
	 * Gets a {@link List} of {@link Component}s which are currently added to
	 * the panel.
	 * 
	 * @return a {@link List} of {@link Component}s which are currently added to
	 *         the panel.
	 */
	public List<Component> getComponents();
	
	/**
	 * Gets the constraints for the given {@link Component}.
	 * 
	 * @param pComponent the {@link Component} for which to get the constraints.
	 * @return the constraints for the given {@link Component}, returns
	 *         {@code null} if there are no constraints or if the
	 *         {@link Component} is not added.
	 */
	public Object getConstraints(Component pComponent);
	
	/**
	 * Notifies this panel of changes in the layout which, which means that the
	 * client side layout is triggered.
	 */
	public void notifyOfChanges();
	
	/**
	 * Removes the given {@link Component}.
	 * 
	 * @param pComponent the {@link Component} to remove.
	 */
	public void removeComponent(Component pComponent);
	
	/**
	 * Sets the constraints for the {@link Component}.
	 * 
	 * @param pComponent the {@link Component} for which the constraints to set.
	 * @param pConstraints the constraints to set.
	 */
	public void setConstraints(Component pComponent, Object pConstraints);
	
	/**
	 * Sets the {@link ILayout} which is to be used.
	 * 
	 * @param pLayout the {@link ILayout}.
	 */
	public void setLayout(ILayout pLayout);
	
	/**
	 * Sets the maximum {@link Size} for the given {@link Component}.
	 * <p>
	 * This is barely a hint for the layout.
	 * 
	 * @param pComponent the {@link Component}.
	 * @param pSize the maximum {@link Size}.
	 */
	public void setMaximumSize(Component pComponent, Size pSize);
	
	/**
	 * Sets the minimum {@link Size} for the given {@link Component}.
	 * <p>
	 * This is barely a hint for the layout.
	 * 
	 * @param pComponent the {@link Component}.
	 * @param pSize the minimum {@link Size}.
	 */
	public void setMinimumSize(Component pComponent, Size pSize);
	
	/**
	 * Sets the preferred {@link Size} for the given {@link Component}.
	 * <p>
	 * This is barely a hint for the layout.
	 * 
	 * @param pComponent the {@link Component}.
	 * @param pSize the preferred {@link Size}.
	 */
	public void setPreferredSize(Component pComponent, Size pSize);
	
}	// ILayoutedPanel
