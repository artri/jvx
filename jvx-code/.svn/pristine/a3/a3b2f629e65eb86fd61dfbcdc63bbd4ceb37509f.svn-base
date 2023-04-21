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
 * 26.04.2019 - [DJ] - #2016: workaround for uncontrolled size increase of empty panels
 */
package com.sibvisions.rad.ui.vaadin.ext.ui.client.panel.layout;

import com.sibvisions.rad.ui.vaadin.ext.ui.client.panel.helper.Size;
import com.vaadin.client.ComponentConnector;

/**
 * The {@link BorderLayout} lay outs its components in a borderly fashion.
 * 
 * @author Robert Zenz
 */
public class BorderLayout extends AbstractClientSideGapLayout
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constants
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The constraint for the center. */
	private static final String CENTER = "Center";
	
	/** The constraint for the east. */
	private static final String EAST = "East";
	
	/** The constraint for the north. */
	private static final String NORTH = "North";
	
	/** The constraint for the south. */
	private static final String SOUTH = "South";
	
	/** The constraint for the west. */
	private static final String WEST = "West";
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The {@link ComponentConnector} for the center. */
	private ComponentConnector centerConnector;
	
	/** The {@link ComponentConnector} for the east. */
	private ComponentConnector eastConnector;
	
	/** The distance from the bottom. */
	private int fromBottom = 0;
	
	/** The distance from the left. */
	private int fromLeft = 0;
	
	/** The distance from the right. */
	private int fromRight = 0;
	
	/** The distance from the top. */
	private int fromTop = 0;
	
	/** The {@link Size} of the layout itself. */
//	private Size layoutSize = null;

	/** The {@link Size} of the layout itself. */
	private int minimumWidth = 0;
	/** The {@link Size} of the layout itself. */
	private int minimumHeight = 0;

	/** The {@link Size} of the layout itself. */
	private int preferredWidth = 0;
	/** The {@link Size} of the layout itself. */
	private int preferredHeight = 0;

	/** The {@link ComponentConnector} for the north. */
	private ComponentConnector northConnector;
	
	/** The {@link ComponentConnector} for the south. */
	private ComponentConnector southConnector;
	
	/** The {@link ComponentConnector} for the west. */
	private ComponentConnector westConnector;
	
	/** The size in layout path, to check with postLayout. */
	private Size cachedSize;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link BorderLayout}.
	 */
	public BorderLayout()
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
	public void clearCaches()
	{
		super.clearCaches();
		
		northConnector = null;
		westConnector = null;
		southConnector = null;
		eastConnector = null;
		centerConnector = null;
	}
	
	/**
	 * Gets the {@link Size preferred size}.
	 *
	 * @return the {@link Size preferred size}.
	 */
	@Override
	public Size getPreferredSize()
	{
		updateFromState();
		
		findConnectors();

		calculateLayoutSizeBase();
		
		return new Size(preferredWidth, preferredHeight);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void layoutComponents(boolean pFirstRun)
	{
		super.layoutComponents();
		
		if (pFirstRun)
		{
			cachedSize = parent.getPanelSize();
		}
		else
		{
			cachedSize = parent.getCanvasSize();
		}
		
		findConnectors();
		calculateLayoutSizeBase();
		
		if (cachedSize.width < minimumWidth)
		{
			cachedSize.width = minimumWidth;
		}
		
		if (cachedSize.height < minimumHeight)
		{
			cachedSize.height = minimumHeight;
		}

		// workaround for uncontrolled size increase of empty panels
		if (parent.getChildren().size() == 0)
		{
			cachedSize.width = preferredWidth;
			cachedSize.height = preferredHeight;
		}
		else if ((isInsideScrollableContainer() && pFirstRun) || isScrollableContainer())
		{
		    Size preferredSize = limitPreferredSize(parent.getParent(), preferredWidth, preferredHeight);
			// Now we will check if we are smaller than our preferred size, if yes,
			// we will resize ourselves to our preferred size and do a second layout
			// pass afterwards to make sure that we are at least our preferred size
			// big if we can get that big.
			
			if (cachedSize.width < preferredSize.width)
			{
				cachedSize.width = preferredSize.width;
			}
			if (cachedSize.height < preferredSize.height)
			{
				cachedSize.height = preferredSize.height;
			}
		}
		
		parent.setCanvasSize(cachedSize);
		updatePreferredSizeCache(preferredWidth, preferredHeight);

		layoutNorth();
		layoutSouth();
		layoutWest();
		layoutEast();
		layoutCenter();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Calculates the size the layout requires.
	 */
	protected void calculateLayoutSizeBase()
	{
		minimumWidth = getPreferredWidth(eastConnector) + getPreferredWidth(westConnector) + margins.getHorizontal();
		minimumHeight = getPreferredHeight(northConnector) + getPreferredHeight(southConnector) + margins.getVertical();
		
		if (eastConnector != null && westConnector != null)
		{
			minimumWidth += horizontalGap;
			
			if (centerConnector != null)
			{
				minimumWidth += horizontalGap;
			}
		}
		
		if (northConnector != null && southConnector != null)
		{
			minimumHeight += verticalGap;
			
			if (centerConnector != null)
			{
				minimumHeight += verticalGap;
			}
		}

		preferredWidth = minimumWidth;
		preferredHeight = minimumHeight;
		
		if (centerConnector != null)
		{
			preferredWidth += getPreferredWidth(centerConnector);
			preferredHeight += getPreferredHeight(centerConnector);
		}

		preferredWidth = Math.max(preferredWidth, Math.max(getPreferredWidth(northConnector), getPreferredWidth(southConnector)));
		preferredHeight = Math.max(preferredHeight, Math.max(getPreferredHeight(westConnector), getPreferredHeight(eastConnector)));
	}

	/**
	 * Calculates the size the layout requires.
	 */
//	public void calculateLayoutSize()
//	{
//		layoutSize = new Size(minimumWidth, minimumHeight);
//		
//		if (cachedSize.width > minimumWidth)
//		{
//			layoutSize.setWidth(cachedSize.width);
//		}
//		
//		if (cachedSize.height > minimumHeight)
//		{
//			layoutSize.setHeight(cachedSize.height);
//		}
//	}
	
	/**
	 * Finds the connectors for the slots.
	 */
	protected void findConnectors()
	{
		for (ComponentConnector connector : parent.getChildComponents())
		{
			String constraint = parent.getConstraint(connector);
			
			if (constraint == null || CENTER.equals(constraint))
			{
				centerConnector = connector;
			}
			else if (NORTH.equals(constraint))
			{
				northConnector = connector;
			}
			else if (SOUTH.equals(constraint))
			{
				southConnector = connector;
			}
			else if (EAST.equals(constraint))
			{
				eastConnector = connector;
			}
			else if (WEST.equals(constraint))
			{
				westConnector = connector;
			}
		}
	}
	
	/**
	 * Gets the preferred height of the given {@link ComponentConnector},
	 * returns {@code 0} if the given connector is {@code null}.
	 * 
	 * @param pConnector the {@link ComponentConnector} of which to get the
	 *            preferred height.
	 * @return the preferred height of the given {@link ComponentConnector},
	 *         {@code 0} if the connector is {@code null}.
	 */
	protected int getPreferredHeight(ComponentConnector pConnector)
	{
		if (pConnector != null)
		{
			return getPreferredSize(pConnector).height;
		}
		
		return 0;
	}
	
	/**
	 * Gets the preferred width of the given {@link ComponentConnector}, returns
	 * {@code 0} if the given connector is {@code null}.
	 * 
	 * @param pConnector the {@link ComponentConnector} of which to get the
	 *            preferred width.
	 * @return the preferred width of the given {@link ComponentConnector},
	 *         {@code 0} if the connector is {@code null}.
	 */
	protected int getPreferredWidth(ComponentConnector pConnector)
	{
		if (pConnector != null)
		{
			return getPreferredSize(pConnector).width;
		}
		
		return 0;
	}
	
	/**
	 * Lays out the center.
	 */
	protected void layoutCenter()
	{
		if (centerConnector != null)
		{
			resizeRelocate(
					centerConnector,
					fromLeft,
                    fromTop,
					cachedSize.width - fromLeft - fromRight,
					cachedSize.height - fromTop - fromBottom);
		}
	}
	
	/**
	 * Lays out the east.
	 */
	protected void layoutEast()
	{
		if (eastConnector != null)
		{
			fromRight = getPreferredSize(eastConnector).width;
			
			resizeRelocate(
					eastConnector,
                    cachedSize.width - margins.right - fromRight,
					fromTop,
					fromRight,
					cachedSize.height - fromTop - fromBottom);
			
			fromRight = fromRight + margins.right;
			
			if (centerConnector != null)
			{
				fromRight = fromRight + horizontalGap;
			}
		}
		else
		{
			fromRight = margins.right;
		}
	}
	
	/**
	 * Lays out the north.
	 */
	protected void layoutNorth()
	{
		if (northConnector != null)
		{
			fromTop = getPreferredSize(northConnector).height;
			
			resizeRelocate(
					northConnector,
                    margins.left,
					margins.top,
					cachedSize.width - margins.getHorizontal(),
					fromTop);
			
			fromTop = fromTop + margins.top;
			
			if (centerConnector != null || southConnector != null)
			{
				fromTop = fromTop + verticalGap;
			}
		}
		else
		{
			fromTop = margins.top;
		}
	}
	
	/**
	 * Lays out the south.
	 */
	protected void layoutSouth()
	{
		if (southConnector != null)
		{
			fromBottom = getPreferredSize(southConnector).height;
			
			resizeRelocate(
					southConnector,
                    margins.left,
					cachedSize.height - fromBottom - margins.bottom,
					cachedSize.width - margins.getHorizontal(),
					fromBottom);
			
			fromBottom = fromBottom + margins.bottom;
			
			if (centerConnector != null)
			{
				fromBottom = fromBottom + verticalGap;
			}
		}
		else
		{
			fromBottom = margins.bottom;
		}
	}
	
	/**
	 * Lays out the west.
	 */
	protected void layoutWest()
	{
		if (westConnector != null)
		{
			fromLeft = getPreferredSize(westConnector).width;
			
			resizeRelocate(
					westConnector,
                    margins.left,
					fromTop,
					fromLeft,
					cachedSize.height - fromTop - fromBottom);
			
			fromLeft = fromLeft + margins.left;
			
			if (centerConnector != null || eastConnector != null)
			{
				fromLeft = fromLeft + horizontalGap;
			}
		}
		else
		{
			fromLeft = margins.left;
		}
	}
	
}	// BorderLayout
