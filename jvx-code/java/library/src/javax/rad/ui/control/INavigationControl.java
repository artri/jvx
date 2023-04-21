/*
 * Copyright 2013 SIB Visions GmbH
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
 * 01.10.2013 - [HM] - creation
 */
package javax.rad.ui.control;

/**
 * The {@link INavigationControl} is an interface that allows to control the
 * behavior of the {@code ENTER} and {@code TAB} key in the implementing
 * controls.
 * <p>
 * By default the implementing control is assumed to be a row/column based
 * control.
 * 
 * @author Martin Handsteiner
 */
public interface INavigationControl
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constants
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Nothing should happened when pressing the {@code ENTER}/{@code TAB} keys.
	 */
	public static final int NAVIGATION_NONE = 0;
	
	/**
	 * When pressing the {@code ENTER}/{@code TAB} keys the focus should jump
	 * from cell to cell, without changing the row. If the last cell is reached,
	 * the focus should jump to the next control.
	 */
	public static final int NAVIGATION_CELL_AND_FOCUS = 1;
	
	/**
	 * When pressing the {@code ENTER}/{@code TAB} keys the focus should jump
	 * from cell to cell, if the last cell is reached the next row should be
	 * selected. If the last row and cell is reached, the focus should jump to
	 * the next control.
	 */
	public static final int NAVIGATION_CELL_AND_ROW_AND_FOCUS = 2;
	
	/**
	 * When pressing the {@code ENTER}/{@code TAB} keys the focus should jump
	 * from row to row. If the last row is reached, the focus should jump to the
	 * next control.
	 */
	public static final int NAVIGATION_ROW_AND_FOCUS = 3;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the navigation mode for the {@code ENTER} key.
	 * 
	 * @return the navigation mode for the {@code ENTER} key.
	 * @see #NAVIGATION_NONE
	 * @see #NAVIGATION_CELL_AND_FOCUS
	 * @see #NAVIGATION_CELL_AND_ROW_AND_FOCUS
	 * @see #NAVIGATION_ROW_AND_FOCUS
	 */
	public int getEnterNavigationMode();
	
	/**
	 * Sets the navigation mode for the {@code ENTER} key.
	 * 
	 * @param pNavigationMode the navigation mode for the {@code ENTER} key.
	 * @see #NAVIGATION_NONE
	 * @see #NAVIGATION_CELL_AND_FOCUS
	 * @see #NAVIGATION_CELL_AND_ROW_AND_FOCUS
	 * @see #NAVIGATION_ROW_AND_FOCUS
	 */
	public void setEnterNavigationMode(int pNavigationMode);
	
	/**
	 * Gets the navigation mode for the {@code TAB} key.
	 * 
	 * @return the navigation mode for the {@code TAB} key.
	 * @see #NAVIGATION_NONE
	 * @see #NAVIGATION_CELL_AND_FOCUS
	 * @see #NAVIGATION_CELL_AND_ROW_AND_FOCUS
	 * @see #NAVIGATION_ROW_AND_FOCUS
	 */
	public int getTabNavigationMode();
	
	/**
	 * Sets the navigation mode for the {@code TAB} key.
	 * 
	 * @param pNavigationMode the navigation mode for the {@code TAB} key.
	 * @see #NAVIGATION_NONE
	 * @see #NAVIGATION_CELL_AND_FOCUS
	 * @see #NAVIGATION_CELL_AND_ROW_AND_FOCUS
	 * @see #NAVIGATION_ROW_AND_FOCUS
	 */
	public void setTabNavigationMode(int pNavigationMode);
	
} 	// INavigationControl
