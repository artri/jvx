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
package com.sibvisions.rad.ui.vaadin.ext.ui.panel.layout;

import java.util.Map;

import com.sibvisions.rad.ui.vaadin.ext.ui.client.panel.helper.Margins;

/**
 * The {@link AbstractGapLayout} is an abstract {@link AbstractLayout} extension
 * which provides everything needed for layouts which are using margins and
 * gaps.
 * 
 * @author Robert Zenz
 */
public abstract class AbstractGapLayout extends AbstractLayout
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The horizontal gap between the components. */
	protected int horizontalGap = 0;
	
	/** The margins/insets of the border. */
	protected Margins margins = new Margins(0, 0, 0, 0);
	
	/** The vertical gap between the components. */
	protected int verticalGap = 0;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link AbstractGapLayout}.
	 *
	 * @param pName the {@link String name} of the layout.
	 */
	protected AbstractGapLayout(String pName)
	{
		super(pName);
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
		data.put("horizontalGap", Integer.toString(horizontalGap));
		data.put("verticalGap", Integer.toString(verticalGap));
		data.put("margins", margins.toString());
		
		return super.getData();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the horizontal gap.
	 *
	 * @return the horizontal gap.
	 */
	public int getHorizontalGap()
	{
		return horizontalGap;
	}
	
	/**
	 * Gets the {@link Margins margins}.
	 *
	 * @return the {@link Margins margins}.
	 */
	public Margins getMargins()
	{
		return margins;
	}
	
	/**
	 * Gets the vertical gap.
	 *
	 * @return the vertical gap.
	 */
	public int getVerticalGap()
	{
		return verticalGap;
	}
	
	/**
	 * Sets the horizontal gap.
	 * 
	 * @param pHorizontalGap the horizontal gap.
	 */
	public void setHorizontalGap(int pHorizontalGap)
	{
		horizontalGap = pHorizontalGap;
		
		notifyParent();
	}
	
	/**
	 * Sets the {@link Margins margins}.
	 *
	 * @param pMargins the new {@link Margins margins}.
	 */
	public void setMargins(Margins pMargins)
	{
		if (pMargins == null)
		{
			margins = new Margins(0, 0, 0, 0);
		}
		else
		{
			margins = pMargins;
		}
		
		notifyParent();
	}
	
	/**
	 * Sets the vertical gap.
	 * 
	 * @param pVerticalGap the vertical gap.
	 */
	public void setVerticalGap(int pVerticalGap)
	{
		verticalGap = pVerticalGap;
		
		notifyParent();
	}
	
}	// AbstractGapLayout
