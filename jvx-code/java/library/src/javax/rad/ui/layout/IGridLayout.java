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
package javax.rad.ui.layout;

import javax.rad.ui.IInsets;
import javax.rad.ui.ILayout;
import javax.rad.ui.IResource;

/**
 * Platform and technology independent grid oriented layout definition. It is
 * designed for use with AWT, Swing, SWT, JSP, JSF,... .
 * 
 * @author Thomas Lehner
 */
public interface IGridLayout extends ILayout<IGridLayout.IGridConstraints>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
	 * Creates a constraint with the specified number of rows and columns.
	 * 
	 * @param pColumns the number of columns
	 * @param pRows the number of rows
	 * @return the constraint.
	 */
	public IGridConstraints getConstraints(int pColumns, int pRows);

	/**
	 * Creates a constraint with the specified number of rows and columns with
	 * the determined dimensions.
	 * 
	 * @param pColumns the number of columns
	 * @param pRows the number of rows 
	 * @param pWidth width of the content
	 * @param pHeight height of the content
	 * @return the constraint.
	 */
	public IGridConstraints getConstraints(int pColumns, int pRows, int pWidth, int pHeight);

	/**
	 * Creates a constraint with the specified number of rows and columns with
	 * the determined dimensions and insets.
	 * 
	 * @param pColumns the number of columns
	 * @param pRows the number of rows
	 * @param pWidth width of the content
	 * @param pHeight height of the content
	 * @param insets the determined insets
	 * @return the constraint.
	 */
	public IGridConstraints getConstraints(int pColumns, int pRows, int pWidth, int pHeight, IInsets insets);

	/**
	 * Sets the column count.
	 * 
	 * @param pColumns the column count.
	 */
	public void setColumns(int pColumns);

	/**
	 * Returns the number of columns.
	 * 
	 * @return the number of columns.
	 */
	public int getColumns();

	/**
	 * Sets the row count.
	 * 
	 * @param pRows the row count.
	 */
	public void setRows(int pRows);

	/**
	 * Returns the number of rows.
	 * 
	 * @return the number of rows.
	 */
	public int getRows();
	
	//****************************************************************
	// Subinterface definition
	//****************************************************************

	/**
	 * The Constraint stores the column and row for layouting a component.
	 * 
	 * @author Thomas Lehner
	 */
	public static interface IGridConstraints extends IResource
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    // Method definitions
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	    /**
		 * Returns the x-position on the grid.
		 *
		 * @return the x-position.
		 */
		public int getGridX();
		
		/**
		 * Sets the x-position on the GridLayout.
		 *
		 * @param pGridX the x-position to set.
		 */
		public void setGridX(int pGridX);
		
		/**
		 * Returns the y-position on the GridLayout.
		 *
		 * @return the y-position.
		 */
		public int getGridY();
		
		/**
		 * Sets the y-position on the GridLayout.
		 *
		 * @param pGridY the x-position to set.
		 */
		public void setGridY(int pGridY);
		
		/**
		 * Returns the height on the GridLayout.
		 *
		 * @return the height.
		 */
		public int getGridHeight();
		
		/**
		 * Sets the height on the GridLayout.
		 *
		 * @param pGridHeight the height to set.
		 */
		public void setGridHeight(int pGridHeight);
		
		/**
		 * Returns the width on the GridLayout.
		 *
		 * @return the width.
		 */
		public int getGridWidth();
		
		/**
		 * Sets the width on the GridLayout.
		 *
		 * @param pGridWidth the height to set.
		 */
		public void setGridWidth(int pGridWidth);
		
		/**
		 * Returns the insets on the GridLayout.
		 *
		 * @return the insets.
		 */
		public IInsets getInsets();
		
		/**
		 * Sets the width on the GridLayout.
		 *
		 * @param pInsets the insets to set.
		 */
		public void setInsets(IInsets pInsets);
		
	}	// IGridConstraints
	
}	// IGridLayout

