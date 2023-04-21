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
package com.sibvisions.rad.ui.vaadin.ext.ui.client.panel.layout;

/**
 * The {@link AbstractClientSideAlignedLayout} is an
 * {@link AbstractClientSideGapLayout} extension which provides everything
 * needed for layouts which support aligning the components.
 * 
 * @author Robert Zenz
 */
public abstract class AbstractClientSideAlignedLayout extends AbstractClientSideGapLayout
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The {@link HorizontalAlignment}. */
	protected HorizontalAlignment horizontalAlignment = HorizontalAlignment.LEFT;
	
	/** The {@link Orientation}. */
	protected Orientation orientation = Orientation.HORIZONTAL;
	
	/** The {@link VerticalAlignment}. */
	protected VerticalAlignment verticalAlignment = VerticalAlignment.TOP;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link AbstractClientSideAlignedLayout}.
	 */
	protected AbstractClientSideAlignedLayout()
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
	protected void updateFromState()
	{
		super.updateFromState();
		
		horizontalAlignment = HorizontalAlignment.getAlignment(parent.getLayoutData("horizontalAlignment"));
		verticalAlignment = VerticalAlignment.getAlignment(parent.getLayoutData("verticalAlignment"));
		
		orientation = Orientation.getOrientation(parent.getLayoutData("orientation"));
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * The {@link HorizontalAlignment}.
	 * 
	 * @author Robert Zenz
	 */
	public enum HorizontalAlignment
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Constants
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/** The components are aligned at the center. */
		CENTER,
		
		/** The default alignment is used. */
		DEFAULT,
		
		/** The components are aligned to the left. */
		LEFT,
		
		/** The components are aligned to the right. */
		RIGHT,
		
		/** The components are stretched over the full width. */
		STRETCH;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// User-defined methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Gets the {@link HorizontalAlignment alignment}.
		 *
		 * @param pValue the {@link String value}.
		 * @return the {@link HorizontalAlignment alignment}.
		 */
		public static HorizontalAlignment getAlignment(String pValue)
		{
			if (pValue != null && pValue.length() > 0)
			{
				for (HorizontalAlignment alignment : values())
				{
					if (alignment.name().equalsIgnoreCase(pValue))
					{
						return alignment;
					}
				}
			}
			
			return LEFT;
		}
		
	}	// HorizontalAlignment
	
	/**
	 * The {@link Orientation}.
	 * 
	 * @author Robert Zenz
	 */
	public enum Orientation
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Constants
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/** The layout is oriented horizontally. */
		HORIZONTAL,
		
		/** The layout is oriented vertically. */
		VERTICAL;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// User-defined methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Gets the {@link Orientation orientation}.
		 *
		 * @param pValue the {@link String value}.
		 * @return the {@link Orientation orientation}.
		 */
		public static Orientation getOrientation(String pValue)
		{
			if (pValue != null && pValue.length() > 0)
			{
				for (Orientation orientation : values())
				{
					if (orientation.name().equalsIgnoreCase(pValue))
					{
						return orientation;
					}
				}
			}
			
			return HORIZONTAL;
		}
		
	}	// Orientation
	
	/**
	 * The {@link VerticalAlignment}.
	 * 
	 * @author Robert Zenz
	 */
	public enum VerticalAlignment
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Constants
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/** The components are aligned to the bottom. */
		BOTTOM,
		
		/** The components are aligned at the center. */
		CENTER,
		
		/** The default alignment is used. */
		DEFAULT,
		
		/** The components are stretched over the full width. */
		STRETCH,
		
		/** The components are aligned to the top. */
		TOP;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// User-defined methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Gets the {@link VerticalAlignment alignment}.
		 *
		 * @param pValue the {@link String value}.
		 * @return the {@link VerticalAlignment alignment}.
		 */
		public static VerticalAlignment getAlignment(String pValue)
		{
			if (pValue != null && pValue.length() > 0)
			{
				for (VerticalAlignment alignment : values())
				{
					if (alignment.name().equalsIgnoreCase(pValue))
					{
						return alignment;
					}
				}
			}
			
			return TOP;
		}
		
	}	// VerticalAlignment
	
}	// AbstractClientSideAlignedLayout
