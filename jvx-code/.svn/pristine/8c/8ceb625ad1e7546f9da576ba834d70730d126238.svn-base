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
 * 07.03.2016 - [RZ] - creation
 */
package com.sibvisions.rad.ui.vaadin.ext.ui.client.panel;

import java.util.HashMap;
import java.util.Map;

import com.vaadin.shared.Connector;
import com.vaadin.shared.ui.AbstractLayoutState;

/**
 * The {@link LayoutedPanelState} is the state for the layouted panel.
 * 
 * @author Robert Zenz
 */
public class LayoutedPanelState extends AbstractLayoutState
{
	{
		primaryStyleName = "v-jvx-panel";
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The margins of the added components. */
	public Map<Connector, String> componentMargins = new HashMap<Connector, String>();
	
	/** The constraints. */
	public Map<Connector, String> constraints = new HashMap<Connector, String>();
	
	/** The data of the current layout. */
	public Map<String, String> layoutData;
	
	/** The name of the current layout. */
	public String layoutName;
	
	/** The maximum sizes for the components. */
	public Map<Connector, String> maximumSizes = new HashMap<Connector, String>();
	
	/** The minimum sizes for the components. */
	public Map<Connector, String> minimumSizes = new HashMap<Connector, String>();
	
	/** The preferred sizes for the components. */
	public Map<Connector, String> preferredSizes = new HashMap<Connector, String>();
	
	/** The sizes for the components. */
	public Map<Connector, String> sizes = new HashMap<Connector, String>();
	
}	// LayoutedPanelState
