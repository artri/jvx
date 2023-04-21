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
 * 09.03.2016 - [RZ] - creation
 */
package com.sibvisions.rad.ui.vaadin.ext.ui.client.panel;

import com.sibvisions.rad.ui.vaadin.ext.ui.client.panel.helper.Size;

/**
 * The {@link IClientSideLayout} is the basic interface for all layouts which
 * are going to be used in an {@link LayoutedPanelConnector}.
 * 
 * @author Robert Zenz
 */
public interface IClientSideLayout
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Clears all caches.
	 * <p>
	 * This method is invoked when it is safe to clear and remove all cached
	 * values, most of the time when the layout pass has ended.
	 */
	public void clearCaches();
	
	/**
	 * Lays out the components as needed.
	 * <p>
	 * This is the main method for layouting the panel and components.
	 * 
	 * @param pFirstRun true, if it is in first run, false, if it is in postLayout
	 */
	public void layoutComponents(boolean pFirstRun);
	
	/**
	 * Gets the preferred {@link Size} of this layout.
	 * 
	 * @return the preferred {@link Size} of this layout. {@code null} if there
	 *         is no preferred size.
	 */
	public Size getPreferredSize();
	
	/**
	 * Sets the parent {@link LayoutedPanelConnector}.
	 * 
	 * @param pParent the parent {@link LayoutedPanelConnector}.
	 */
	public void setParent(LayoutedPanelConnector pParent);
	
}	// IClientSideLayout
