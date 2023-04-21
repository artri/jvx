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
 * 11.05.2016 - [RZ] - creation
 */
package com.sibvisions.rad.ui.vaadin.ext.ui.panel.layout;

import java.util.Map;

import com.sibvisions.rad.ui.vaadin.ext.ui.client.panel.helper.PaddedRectangle;
import com.vaadin.ui.Component;

/**
 * The {@link GridLayout} allows to use layout the components in a grid.
 * 
 * @author Robert Zenz
 */
public class GridLayout extends AbstractGapLayout
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The number of columns. */
	private int columns = 1;
	
	/** The number of rows. */
	private int rows = 1;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link GridLayout}.
	 *
	 * @param pColumns the columns.
	 * @param pRows the rows.
	 */
	public GridLayout(int pColumns, int pRows)
	{
		super("GridLayout");
		
		columns = pColumns;
		rows = pRows;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the number of columns.
	 * 
	 * @return the number of columns.
	 */
	public int getColumns()
	{
		return columns;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the {@link GridConstraints} for the given column and row.
	 * 
	 * @param pColumn the column.
	 * @param pRow the row.
	 * @return the {@link GridConstraints}.
	 */
	public GridConstraints getConstraints(int pColumn, int pRow)
	{
		return getConstraints(pColumn, pRow, 1, 1);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the {@link GridConstraints} for the given column, row, width and
	 * height.
	 * 
	 * @param pColumn the column.
	 * @param pRow the row.
	 * @param pWidth the width.
	 * @param pHeight the height.
	 * @return the {@link GridConstraints}.
	 */
	public GridConstraints getConstraints(int pColumn, int pRow, int pWidth, int pHeight)
	{
		return new GridConstraints(this, pColumn, pRow, pWidth, pHeight);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public String getStringConstraint(Component pComponent, Object pConstraint)
	{
		if (pConstraint != null)
		{
			return pConstraint.toString();
		}
		
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
		data.put("columns", Integer.toString(columns));
		data.put("rows", Integer.toString(rows));
		
		return super.getData();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the number of rows.
	 * 
	 * @return the number of rows.
	 */
	public int getRows()
	{
		return rows;
	}
	
	/**
	 * Sets the number of columns.
	 *
	 * @param pColumns the new number of columns.
	 */
	public void setColumns(int pColumns)
	{
		columns = pColumns;
		
		notifyParent();
	}
	
	/**
	 * Sets the number of rows.
	 *
	 * @param pRows the new number of rows.
	 */
	public void setRows(int pRows)
	{
		rows = pRows;
		
		notifyParent();
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * The {@link GridConstraints} represents a constraint inside the grid.
	 * 
	 * @author Robert Zenz
	 */
	public static class GridConstraints extends PaddedRectangle
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/** The parent {@link GridLayout}. */
		private GridLayout parent = null;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of {@link GridConstraints}.
		 *
		 * @param pParent the {@link GridLayout parent}.
		 * @param pColumn the x.
		 * @param pRow the y.
		 * @param pWidth the width.
		 * @param pHeight the height.
		 */
		public GridConstraints(GridLayout pParent, int pColumn, int pRow, int pWidth, int pHeight)
		{
			super(pColumn, pRow, pWidth, pHeight);
			
			parent = pParent;
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// User-defined methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Sets the column.
		 * 
		 * @param pColumn the column.
		 */
		public void setColumn(int pColumn)
		{
			x = pColumn;
			
			parent.notifyParent();
		}
		
		/**
		 * Sets the height.
		 * 
		 * @param pHeight the height.
		 */
		public void setHeight(int pHeight)
		{
			height = pHeight;
			
			parent.notifyParent();
		}
		
		/**
		 * Sets the bottom padding.
		 *
		 * @param pBottom the new bottom padding.
		 */
		public void setPaddingBottom(int pBottom)
		{
			padding.bottom = pBottom;
			
			parent.notifyParent();
		}
		
		/**
		 * Sets the left padding.
		 *
		 * @param pLeft the new left padding.
		 */
		public void setPaddingLeft(int pLeft)
		{
			padding.left = pLeft;
			
			parent.notifyParent();
		}
		
		/**
		 * Sets the right padding.
		 *
		 * @param pRight the new right padding.
		 */
		public void setPaddingRight(int pRight)
		{
			padding.right = pRight;
			
			parent.notifyParent();
		}
		
		/**
		 * Sets the top padding.
		 *
		 * @param pTop the new top padding.
		 */
		public void setPaddingTop(int pTop)
		{
			padding.top = pTop;
			
			parent.notifyParent();
		}
		
		/**
		 * Sets the row.
		 * 
		 * @param pRow the row.
		 */
		public void setRow(int pRow)
		{
			y = pRow;
			
			parent.notifyParent();
		}
		
		/**
		 * Sets the width.
		 * 
		 * @param pWidth the width.
		 */
		public void setWidth(int pWidth)
		{
			width = pWidth;
			
			parent.notifyParent();
		}
		
	}	// GridConstraints
	
}	// GridLayout
