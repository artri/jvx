/*
 * Copyright 2018 SIB Visions GmbH
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
 * 27.02.2018 - [RZ] - creation
 */
package com.sibvisions.rad.ui.vaadin.ext.ui.client;

import com.sibvisions.rad.ui.vaadin.ext.ui.client.panel.LayoutedPanelConnector;
import com.vaadin.client.ui.VSplitPanelVertical;
import com.vaadin.client.ui.splitpanel.VerticalSplitPanelConnector;
import com.vaadin.shared.ui.splitpanel.VerticalSplitPanelState;

/**
 * The {@link ExtendedVerticalSplitPanelConnector} is a
 * {@link VerticalSplitPanelConnector} extension which prevents the size from
 * being applied to the component when it is a child of the
 * {@link LayoutedPanelConnector}.
 * <p>
 * The background for this is as follows. The LayoutedPanel sets an absolute
 * width and height on its children. This is done during the layout phase and
 * after the states have been received from the server and applied to the
 * components. The SplitPanel is a little bit special in this regard as it
 * receives a state update when the splitter is being dragged and so the full
 * state is applied to it outside of a layout phase. So the full size (100%
 * width and height) is set on the component and the LayoutedPanel has no idea
 * that this just happened and can't undo that.
 * <p>
 * So the solution is this connector, it prevents the full width and height
 * being applied to the component if it is the child of
 * {@link LayoutedPanelConnector}.
 * 
 * @author Robert Zenz
 */
//@Connect(value = VerticalSplitPanel.class, loadStyle = LoadStyle.EAGER)
public class ExtendedVerticalSplitPanelConnector extends VerticalSplitPanelConnector
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link ExtendedVerticalSplitPanelConnector}.
	 */
	public ExtendedVerticalSplitPanelConnector()
	{
		super();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public VerticalSplitPanelState getState()
	{
		return super.getState();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public VSplitPanelVertical getWidget()
	{
		return super.getWidget();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void updateComponentSize(String pNewWidth, String pNewHeight)
	{
		// Do not apply the size if we are a child of a LayoutedPanel and
		// a relative size is being set.
		if (!(getParent() instanceof LayoutedPanelConnector && pNewWidth.endsWith("%")))
		{
			super.updateComponentSize(pNewWidth, pNewHeight);
		}
	}
	
}	// ExtendedVerticalSplitPanelConnector
