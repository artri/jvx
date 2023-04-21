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

import java.util.Map;

import com.vaadin.ui.Component;

/**
 * The {@link ILayout} is the basic interface for all layouts which are going to
 * be used in an {@link ILayoutedPanel}.
 * 
 * @author Robert Zenz
 */
public interface ILayout
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the layout data.
	 * <p>
	 * The layout data is a simple {@link Map} which holds all the data which
	 * should be send to the client. This method will always be called before
	 * data is send to the client.
	 * 
	 * @return the layout data.
	 */
	public Map<String, String> getData();
	
	/**
	 * Gets the name of the layout.
	 * <p>
	 * The name is used to instantiate the layout on the client-side.
	 * 
	 * @return the layout name.
	 */
	public String getName();
	
	/**
	 * Gets the given constraint as {@link String}.
	 * <p>
	 * The constraint is a constraint for the given {@link Component}, this
	 * method should return the {@link String} representation of the given
	 * constraint or {@code null} if there is none.
	 * 
	 * @param pComponent the {@link Component}.
	 * @param pConstraint the constraint.
	 * @return the {@link String} representation of the given constraint,
	 *         alternatively {@code null}.
	 */
	public String getStringConstraint(Component pComponent, Object pConstraint);
	
	/**
	 * Clears the cached component information, if any.
	 * <p>
	 * @param pComponent the {@link Component}. 
	 */
	public void clear(Component pComponent);
	
	/**
	 * Sets the parent {@link ILayoutedPanel}.
	 * 
	 * @param pParent the parent {@link ILayoutedPanel}, or {@code null} if the
	 *            {@link ILayout} is removed.
	 */
	public void setParent(ILayoutedPanel pParent);
	
}	// ILayout
