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
package com.sibvisions.rad.ui.vaadin.ext.ui.panel.layout;

import java.util.Map;

import com.vaadin.ui.Component;

/**
 * The {@link FlowLayout} allows to layout the components in a flow.
 * 
 * @author Robert Zenz
 */
public class FlowLayout extends AbstractAlignedLayout
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** If the layout wraps automatically. */
	private boolean autoWrap = false;
	
	/** The {@link HorizontalAlignment horizontal component alignment}. */
	protected HorizontalAlignment horizontalComponentAlignment = HorizontalAlignment.Center;
	
	/** The {@link VerticalAlignment vertical component alignment}. */
	protected VerticalAlignment verticalComponentAlignment = VerticalAlignment.Center;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link FlowLayout}.
	 */
	public FlowLayout()
	{
		super("FlowLayout");
		
		horizontalAlignment = HorizontalAlignment.Center;
		verticalAlignment = VerticalAlignment.Center;
		
		horizontalGap = 5;
		verticalGap = 5;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public String getStringConstraint(Component pComponent, Object pConstraint)
	{
		// This layout does not use constraints.
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void clear(Component pComponent)
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, String> getData()
	{
		data.put("autoWrap", Boolean.toString(autoWrap));
		data.put("horizontalComponentAlignment", horizontalComponentAlignment.name());
		data.put("verticalComponentAlignment", verticalComponentAlignment.name());
		
		return super.getData();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets if the layout should automatically wrap.
	 * 
	 * @return {@code true} if the layout should automatically wrap.
	 */
	public boolean isAutoWrap()
	{
		return autoWrap;
	}
	
	/**
	 * Sets if the layout should automatically wrap.
	 * 
	 * @param pAutoWrap {@code true} if the layout should automatically wrap.
	 */
	public void setAutoWrap(boolean pAutoWrap)
	{
		autoWrap = pAutoWrap;
		
		notifyParent();
	}
	
	/**
	 * Gets the {@link HorizontalAlignment horizontal component alignment}.
	 *
	 * @return the {@link HorizontalAlignment horizontal component alignment}.
	 */
	public HorizontalAlignment getHorizontalComponentAlignment()
	{
		return horizontalComponentAlignment;
	}
	
	/**
	 * Sets the {@link HorizontalAlignment horizontal component alignment}.
	 *
	 * @param pHorizontalComponentAlignment the new {@link HorizontalAlignment horizontal
	 *            component alignment}.
	 */
	public void setHorizontalComponentAlignment(HorizontalAlignment pHorizontalComponentAlignment)
	{
		horizontalComponentAlignment = pHorizontalComponentAlignment;
		
		notifyParent();
	}

	/**
	 * Gets the {@link VerticalAlignment vertical component alignment}.
	 *
	 * @return the {@link VerticalAlignment vertical component alignment}.
	 */
	public VerticalAlignment getVerticalComponentAlignment()
	{
		return verticalComponentAlignment;
	}
	
	/**
	 * Sets the {@link VerticalAlignment vertical component alignment}.
	 *
	 * @param pVerticalComponentAlignment the new {@link VerticalAlignment vertical
	 *            component alignment}.
	 */
	public void setVerticalComponentAlignment(VerticalAlignment pVerticalComponentAlignment)
	{
		verticalComponentAlignment = pVerticalComponentAlignment;
		
		notifyParent();
	}
	
}	// FlowLayout
