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

/**
 * The {@link AbstractAlignedLayout} is an abstract {@link AbstractGapLayout}
 * extension which provides everything needed for layouts which align the
 * components.
 * 
 * @author Robert Zenz
 */
public abstract class AbstractAlignedLayout extends AbstractGapLayout
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The horizontal alignment. */
	public enum HorizontalAlignment
	{
		/** The components are aligned at the center. */
		Center,
		/** The default alignment is used. */
		Default,
		/** The components are aligned to the left. */
		Left,
		/** The components are aligned to the right. */
		Right,
		/** The components are stretched over the full width. */
		Stretch
	}
	
	/** The vertical alignment. */
	public enum VerticalAlignment
	{
		/** The components are aligned to the bottom. */
		Bottom,
		/** The components are aligned at the center. */
		Center,
		/** The default alignment is used. */
		Default,
		/** The components are stretched over the full width. */
		Stretch,
		/** The components are aligned to the top. */
		Top
	}

	/** The orientation. */
	public enum Orientation
	{
		/** The layout is oriented horizontally. */
		Horizontal,
		/** The layout is oriented vertically. */
		Vertical
	}

	
	/** The {@link HorizontalAlignment horizontal alignment}. */
	protected HorizontalAlignment horizontalAlignment = HorizontalAlignment.Left;
	
	/** The {@link Orientation orientation}. */
	protected Orientation orientation = Orientation.Horizontal;
	
	/** The {@link VerticalAlignment vertical alignment}. */
	protected VerticalAlignment verticalAlignment = VerticalAlignment.Top;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link AbstractAlignedLayout}.
	 *
	 * @param pName the {@link String name} of the layout.
	 */
	protected AbstractAlignedLayout(String pName)
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
		data.put("horizontalAlignment", horizontalAlignment.name());
		data.put("verticalAlignment", verticalAlignment.name());
		data.put("orientation", orientation.name());
		
		return super.getData();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the {@link HorizontalAlignment horizontal alignment}.
	 *
	 * @return the {@link HorizontalAlignment horizontal alignment}.
	 */
	public HorizontalAlignment getHorizontalAlignment()
	{
		return horizontalAlignment;
	}
	
	/**
	 * Gets the {@link Orientation orientation}.
	 *
	 * @return the {@link Orientation orientation}.
	 */
	public Orientation getOrientation()
	{
		return orientation;
	}
	
	/**
	 * Gets the {@link VerticalAlignment vertical alignment}.
	 *
	 * @return the {@link VerticalAlignment vertical alignment}.
	 */
	public VerticalAlignment getVerticalAlignment()
	{
		return verticalAlignment;
	}
	
	/**
	 * Sets the {@link HorizontalAlignment horizontal alignment}.
	 *
	 * @param pHorizontalAlignment the new {@link HorizontalAlignment horizontal
	 *            alignment}.
	 */
	public void setHorizontalAlignment(HorizontalAlignment pHorizontalAlignment)
	{
		horizontalAlignment = pHorizontalAlignment;
		
		notifyParent();
	}
	
	/**
	 * Sets the {@link Orientation orientation}.
	 *
	 * @param pOrientation the new {@link Orientation orientation}.
	 */
	public void setOrientation(Orientation pOrientation)
	{
		orientation = pOrientation;
		
		notifyParent();
	}
	
	/**
	 * Sets the {@link VerticalAlignment vertical alignment}.
	 *
	 * @param pVerticalAlignment the new {@link VerticalAlignment vertical
	 *            alignment}.
	 */
	public void setVerticalAlignment(VerticalAlignment pVerticalAlignment)
	{
		verticalAlignment = pVerticalAlignment;
		
		notifyParent();
	}
	
}	// AbstractAlignedLayout
