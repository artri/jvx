/*
 * Copyright 2014 SIB Visions GmbH
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
 * 06.03.2014 - [LT] - erstellt
 */
package com.sibvisions.rad.ui.swing.ext.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;

import com.sibvisions.rad.ui.swing.ext.JVxUtil;

/**
 * The GridLayout class is a layout manager that lays out a container's
 * components in a rectangular grid.
 * 
 * @author Thomas Lehner
 */
public class JVxGridLayout implements LayoutManager2
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** Stores all constraints. */
	private final Map<Component, CellConstraints>	constraintMap;
	
	/** the layout margins. */
	private Insets									margins	= new Insets(0, 0, 0, 0);
	
	/** The number of rows. */
	private int										rows;

	/** The number of columns. */
	private int										columns;

	/** the horizontal gap between components. */
	private int										horizontalGap = 0;

	/** the vertical gap between components. */
	private int										verticalGap = 0;
	
	/** cache for x-coordinates. */
	private int[] xPosition = null;

	/** cache for y-coordinates. */
	private int[] yPosition = null;
	

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Constructs a new GridLayout that calculates columns and rows automatically. 
	 */
	public JVxGridLayout()
	{
		this(0, 0);
	}
	
	/**
	 * Constructs a new GridLayout.
	 * 
	 * @param pColumns the column count, a value smaller than 1 means automatic calculation.
	 * @param pRows the row count, a value smaller than 1 means automatic calculation.
	 */
	public JVxGridLayout(int pColumns, int pRows)
	{
		rows = pRows;
		columns = pColumns;
		
		constraintMap = new HashMap<Component, CellConstraints>();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public void addLayoutComponent(String pName, Component c)
	{
		throw new UnsupportedOperationException("Use #addLayoutComponent(Component, Object) instead.");
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeLayoutComponent(Component pComp)
	{
		constraintMap.remove(pComp);
	}

	/**
	 * {@inheritDoc}
	 */
	public Dimension preferredLayoutSize(Container pParent)
	{
		int maxWidth = 0;
		int maxHeight = 0;

		int targetColumns = columns;
		int targetRows = rows;

		for (Map.Entry<Component, CellConstraints> entry : constraintMap.entrySet())
		{
			Component component = entry.getKey();
			
			if (component.isVisible())
			{
				CellConstraints constraints = entry.getValue();
				Insets insets = constraints.getInsets();
				
				Dimension pref = JVxUtil.getPreferredSize(component);
				int width = (pref.width + constraints.gridWidth - 1) / constraints.gridWidth + insets.left + insets.right;
				if (width > maxWidth)
				{
					maxWidth = width;
				}
				int height = (pref.height + constraints.gridHeight - 1) / constraints.gridHeight + insets.top + insets.bottom;
				if (height > maxHeight)
				{
					maxHeight = height;
				}
				
				if (columns <= 0 && constraints.gridX + constraints.gridWidth > targetColumns)
				{
					targetColumns = constraints.gridX + constraints.gridWidth;
				}
				if (rows <= 0 && constraints.gridY + constraints.gridHeight > targetRows)
				{
					targetRows = constraints.gridY + constraints.gridHeight;
				}
			}
		}
		
		Insets parentInsets = pParent.getInsets();

		return new Dimension(maxWidth * targetColumns + parentInsets.left + parentInsets.right 
				  + margins.left + margins.right + horizontalGap * (targetColumns - 1),
				  maxHeight * targetRows + parentInsets.bottom + parentInsets.top 
				   + margins.top + margins.bottom + verticalGap * (targetRows - 1));
	}

	/**
	 * {@inheritDoc}
	 */
	public Dimension minimumLayoutSize(Container parent)
	{
		return new Dimension(0, 0);
	}

	/**
	 * {@inheritDoc}
	 */
	public void layoutContainer(Container pParent)
	{
		synchronized (pParent.getTreeLock())
		{
			Dimension size = pParent.getSize();

			Insets parentInsets = pParent.getInsets();
			
			int targetColumns = columns;
			int targetRows = rows;

			if (columns <= 0 || rows <= 0)
			{
				for (Map.Entry<Component, CellConstraints> entry : constraintMap.entrySet())
				{
					Component component = entry.getKey();
					
					if (component.isVisible())
					{
						CellConstraints constraints = entry.getValue();

						if (columns <= 0 && constraints.gridX + constraints.gridWidth > targetColumns)
						{
							targetColumns = constraints.gridX + constraints.gridWidth;
						}
						if (rows <= 0 && constraints.gridY + constraints.gridHeight > targetRows)
						{
							targetRows = constraints.gridY + constraints.gridHeight;
						}
					}
				}
			}
			
			if (targetColumns > 0 && targetRows > 0)
			{
				int leftInsets = parentInsets.left + margins.left;
				int topInsets = parentInsets.top + margins.top;
				
				int totalGapsWidth = (targetColumns - 1) * horizontalGap;
				int totalGapsHeight = (targetRows - 1) * verticalGap;
				
				int totalWidth = size.width - leftInsets - parentInsets.right - margins.right - totalGapsWidth;
				int totalHeight = size.height - topInsets - parentInsets.bottom - margins.bottom - totalGapsHeight;
				
				int columnSize = totalWidth / targetColumns;
				int rowSize = totalHeight / targetRows;
	
				int widthCalcError = totalWidth - columnSize * targetColumns;
				int heightCalcError = totalHeight - rowSize * targetRows;
				int xMiddle = 0;
				if (widthCalcError > 0)
				{
					xMiddle = (targetColumns / widthCalcError + 1) / 2;
				}
				int yMiddle = 0;
				if (heightCalcError > 0)
				{
					yMiddle = (targetRows / heightCalcError + 1) / 2;
				}
				
				if (xPosition == null || xPosition.length != targetColumns + 1)
				{
					xPosition = new int[targetColumns + 1];
				}
				xPosition[0] = leftInsets;
				int corrX = 0;
				for (int i = 0; i < targetColumns; i++)
				{
					xPosition[i + 1] = xPosition[i] + columnSize + horizontalGap;
					if (widthCalcError > 0 && corrX * targetColumns / widthCalcError + xMiddle == i) 
					{
						xPosition[i + 1]++;
						corrX++;
					}
				}

				if (yPosition == null || yPosition.length != targetRows + 1)
				{
					yPosition = new int[targetRows + 1];
				}
				yPosition[0] = topInsets;
				int corrY = 0;
				for (int i = 0; i < targetRows; i++)
				{
					yPosition[i + 1] = yPosition[i] + rowSize + verticalGap;
					if (heightCalcError > 0 && corrY * targetRows / heightCalcError + yMiddle == i) 
					{
						yPosition[i + 1]++;
						corrY++;
					}
				}
				for (Map.Entry<Component, CellConstraints> entry : constraintMap.entrySet())
				{
					Component component = entry.getKey();
					
					if (component.isVisible())
					{
						CellConstraints constraints = entry.getValue();
						Insets insets = constraints.insets;
						
						int x = getPosition(xPosition, constraints.gridX, columnSize, horizontalGap) + insets.left;
						int y = getPosition(yPosition, constraints.gridY, rowSize, verticalGap) + insets.top;
		
						component.setBounds(x, y,
								getPosition(xPosition, constraints.gridX + constraints.gridWidth, columnSize, horizontalGap) - horizontalGap - x - insets.right, 
								getPosition(yPosition, constraints.gridY + constraints.gridHeight, rowSize, verticalGap) - verticalGap - y - insets.bottom);
					}
				}
			}
		}
	}
	
	/**
	 * Gets in any case an position inside the grid.
	 * @param pPositions the stored positions.
	 * @param pIndex the index
	 * @param pSize the size of column or row
	 * @param pGap the gap
	 * @return the position
	 */
	private static int getPosition(int[] pPositions, int pIndex, int pSize, int pGap)
	{
		if (pIndex < 0)
		{
			return pPositions[0] + pIndex * (pSize + pGap);
		}
		else if (pIndex >= pPositions.length)
		{
			return pPositions[pPositions.length - 1] + (pIndex - pPositions.length + 1) * (pSize + pGap);
		}
		else
		{
			return pPositions[pIndex];
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public void addLayoutComponent(Component pComp, Object pConstraints)
	{
		checkNotNull(pConstraints, "The constraints must not be null.");
		
		if (pConstraints instanceof CellConstraints)
		{
			setConstraints(pComp, (CellConstraints)pConstraints);
		}
		else
		{
			throw new IllegalArgumentException("Illegal constraint type " + pConstraints.getClass());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Dimension maximumLayoutSize(Container pTarget)
	{
		return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
	}

	/**
	 * {@inheritDoc}
	 */
	public float getLayoutAlignmentX(Container pTarget)
	{
		return 0.5f;
	}

	/**
	 * {@inheritDoc}
	 */
	public float getLayoutAlignmentY(Container pTarget)
	{
		return 0.5f;
	}

	/**
	 * {@inheritDoc}
	 */
	public void invalidateLayout(Container pTarget)
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Returns the number of columns.
	 * 
	 * @return the number of columns
	 */
	public int getColumns()
	{
		return columns;
	}
	
	/**
	 * Sets the column count.
	 * 
	 * @param pColumns the column count
	 */
	public void setColumns(int pColumns)
	{
		columns = pColumns;
	}

	/**
	 * Returns the number of rows.
	 * 
	 * @return the number of rows
	 */
	public int getRows()
	{
		return rows;
	}
	
	/**
	 * Sets the row count.
	 * 
	 * @param pRows the row count
	 */
	public void setRows(int pRows)
	{
		rows = pRows;
	}
	
	/**
	 * Gets the constraints for the specified <code>IComponent</code>.
	 *
	 * @param pComp the <code>IComponent</code> to be queried
	 * @return the constraint for the specified <code>IComponent</code>,
	 *         or null if component is null or is not present
	 *         in this layout
	 */
	public CellConstraints getConstraints(Component pComp)
	{
		return constraintMap.get(pComp);
	}
	
	/**
	 * Creates a constraint with the specified number of rows and columns.
	 * 
	 * @param pColumns the number of columns
	 * @param pRows the number of rows
	 * @return the constraint
	 */
	public CellConstraints getConstraints(int pColumns, int pRows)
	{
		return new CellConstraints(pColumns, pRows, 1, 1);
	}

	/**
	 * Creates a constraint with the specified number of rows and columns with
	 * the determined dimensions.
	 * 
	 * @param pColumns the number of columns
	 * @param pRows the number of rows 
	 * @param pWidth width of the content
	 * @param pHeight height of the content
	 * @return the constraint
	 */
	public CellConstraints getConstraints(int pColumns, int pRows, int pWidth, int pHeight)
	{
		return new CellConstraints(pColumns, pRows, pWidth, pHeight);
	}

	/**
	 * Creates a constraint with the specified number of rows and columns with
	 * the determined dimensions and insets.
	 * 
	 * @param pColumns the number of columns
	 * @param pRows the number of rows
	 * @param pWidth width of the content
	 * @param pHeight height of the content
	 * @param pInsets the determined insets
	 * @return the constraint
	 */
	public CellConstraints getConstraints(int pColumns, int pRows, int pWidth, int pHeight, Insets pInsets)
	{
		return new CellConstraints(pColumns, pRows, pWidth, pHeight, pInsets);
	}
	
	/**
	 * Puts the component and its constraints into the constraint Map.
	 * 
	 * @param pComponent the component
	 * @param pConstraints the components constraints
	 */
	public void setConstraints(Component pComponent, CellConstraints pConstraints)
	{
		checkNotNull(pComponent, "The component must not be null.");
		checkNotNull(pConstraints, "The constraints must not be null.");

        constraintMap.put(pComponent, (CellConstraints)pConstraints.clone());
        
        //#2292
        pComponent.invalidate();
        if (pComponent.getParent() instanceof JComponent)
        {
            ((JComponent)pComponent.getParent()).revalidate();
        }
	}
	
	/**
	 * Gets the margins.
	 * 
	 * @return the margins.
	 */
	public Insets getMargins()
	{
		return margins;
	}

	/**
	 * Sets the margins.
	 * 
	 * @param pMargins the margins
	 */
	public void setMargins(Insets pMargins)
	{
		if (pMargins == null)
		{
			margins = new Insets(0, 0, 0, 0);
		}
		else
		{
			margins = pMargins;
		}
	}

	/**
	 * Gets the horizontal gap.
	 * 
	 * @return the horizontal gap
	 */
	public int getHorizontalGap()
	{
		return horizontalGap;
	}

	/**
	 * Sets the horizontal gap.
	 * 
	 * @param pHorizontalGap the horizontal gap
	 */
	public void setHorizontalGap(int pHorizontalGap)
	{
		horizontalGap = pHorizontalGap;
	}

	/**
	 * Gets the vertical gap.
	 * 
	 * @return the vertical gap
	 */
	public int getVerticalGap()
	{
		return verticalGap;
	}

	/**
	 * Sets the vertical gap.
	 * 
	 * @param pVerticalGap the vertical gap
	 */
	public void setVerticalGap(int pVerticalGap)
	{
		verticalGap = pVerticalGap;
	}
	
	/**
	 * Throws a NullPointerException with the specified message if the given
	 * Object references null.
	 * 
	 * @param pReference the Object which shall be checked
	 * @param pMessage the specified message
	 */
	public static void checkNotNull(Object pReference, String pMessage)
	{
		if (pReference == null)
		{
			throw new NullPointerException(pMessage);
		}
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************

	/**
	 * The <code>CellConstraint</code> class stores the X and Y position, the Width and Height and
	 * the insets of the component.
	 * 
	 * @author Thomas Lehner
	 */
	public static class CellConstraints implements Cloneable
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/** The position on the x-axis. */
		private int					gridX;

		/** The position on the y-axis. */
		private int					gridY;

		/** The width of the component in grids. */
		private int					gridWidth;

		/** The height of the component in grids. */
		private int					gridHeight;

		/** The specified insets of the component. */
		private Insets				insets;

		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Constructs a new CellConstraint.
		 */
		public CellConstraints()
		{
			this(0, 0);
		}

		/**
		 * Constructs a new CellConstraint with the given parameters.
		 * 
		 * @param pGridX the position on the x-axis
		 * @param pGridY the position on the y-axis
		 */
		public CellConstraints(int pGridX, int pGridY)
		{
			this(pGridX, pGridY, 1, 1, null);
		}
		
		/**
		 * Constructs a new CellConstraint with the given parameters.
		 * 
		 * @param pGridX the position on the x-axis
		 * @param pGridY the position on the y-axis
		 * @param pWidth the width of the component
		 * @param pHeight the height of the component
		 */
		public CellConstraints(int pGridX, int pGridY, int pWidth, int pHeight)
		{
			this(pGridX, pGridY, pWidth, pHeight, null);
		}

		/**
		 * Constructs a new CellConstraint with the given parameters.
		 * 
		 * @param pGridX the position on the x-axis
		 * @param pGridY the position on the y-axis
		 * @param pGridWidth the width of the component
		 * @param pGridHeight the height of the component
		 * @param pInsets the specified insets
		 */
		public CellConstraints(int pGridX, int pGridY, int pGridWidth, int pGridHeight, Insets pInsets)
		{ 
			setGridX(pGridX);
			setGridY(pGridY);
			setGridWidth(pGridWidth);
			setGridHeight(pGridHeight);
			setInsets(pInsets);
		}

		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// User-defined methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Returns the x-position on the grid.
		 *
		 * @return the x-position
		 */
		public int getGridX()
		{
			return gridX;
		}

		/**
		 * Sets the x-position on the GridLayout.
		 *
		 * @param pGridX the x-position to set
		 */
		public void setGridX(int pGridX)
		{
            if (pGridX < 0)
            {
                throw new IndexOutOfBoundsException("The grid x must be a positive number.");
            }
            
			gridX = pGridX;
		}

		/**
		 * Returns the y-position on the GridLayout.
		 *
		 * @return the y-position
		 */
		public int getGridY()
		{
			return gridY;
		}

		/**
		 * Sets the y-position on the GridLayout.
		 *
		 * @param pGridY the x-position to set
		 */
		public void setGridY(int pGridY)
		{
            if (pGridY < 0)
            {
                throw new IndexOutOfBoundsException("The grid y must be a positive number.");
            }
            
			gridY = pGridY;
		}

		/**
		 * Returns the width on the GridLayout.
		 *
		 * @return the width
		 */
		public int getGridWidth()
		{
			return gridWidth;
		}

		/**
		 * Sets the width on the GridLayout.
		 *
		 * @param pGridWidth the height to set
		 */
		public void setGridWidth(int pGridWidth)
		{
            if (pGridWidth <= 0)
            {
                throw new IndexOutOfBoundsException("The grid width must be a positive number.");
            }

			gridWidth = pGridWidth;	
		}

		/**
		 * Returns the height on the GridLayout.
		 *
		 * @return the height
		 */
		public int getGridHeight()
		{
			return gridHeight;
		}

		/**
		 * Sets the height on the GridLayout.
		 *
		 * @param pGridHeight the height to set
		 */
		public void setGridHeight(int pGridHeight)
		{
            if (pGridHeight <= 0)
            {
                throw new IndexOutOfBoundsException("The grid height must be a positive number.");
            }

			gridHeight = pGridHeight;
		}

		/**
		 * Returns the insets on the GridLayout.
		 *
		 * @return the insets
		 */
		public Insets getInsets()
		{
			return insets;
		}
		
		/**
		 * Sets the width on the GridLayout.
		 *
		 * @param pInsets the insets to set
		 */
		public void setInsets(Insets pInsets)
		{
			if (pInsets == null)
			{
				insets = new Insets(0, 0, 0, 0);
			}
			else
			{
				insets = pInsets;
			}
		}

		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    // Overwritten methods
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		@Override
		public Object clone()
		{
			try
			{
				CellConstraints c = (CellConstraints)super.clone();
				c.insets = (Insets)insets.clone();
				
				return c;
			}
			catch (CloneNotSupportedException e)
			{
				// This shouldn't happen, since we are Cloneable.
				throw new InternalError();
			}
		}
		
	}	//CellConstraints
	
}	//JVxGridLayout
