/*
 * Copyright 2009 SIB Visions GmbH
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
 * 01.10.2008 - [HM] - creation
 * 05.06.2009 - [JR] - set/getTranslation added
 * 03.04.2019 - [DJ] - #2000 - cell tooltip functionality added
 */
package javax.rad.ui.control;

import javax.rad.model.ColumnView;
import javax.rad.model.ui.ITableControl;
import javax.rad.ui.IComponent;
import javax.rad.ui.component.IEditable;
import javax.rad.util.TranslationMap;

/**
 * Platform and technology independent Table definition.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 * @see	javax.swing.JTable
 */
public interface ITable extends IComponent, 
                                ITableControl,
                                INavigationControl,
                                IEditable,
                                ICellFormatable,
                                ICellToolTipable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Method definitions
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Returns the ColumnView displayed by this control.
     * If null is set, the default ColumnView is shown.
     *
     * @return the ColumnView.
     * @see #setColumnView
     */
    public ColumnView getColumnView();

    /**
     * Sets the ColumnView displayed by this control.
     * If null is set, the default ColumnView is shown.
     * 
	 * @param pColumnView the ColumnView
     * @see #getColumnView
     */
    public void setColumnView(ColumnView pColumnView);
	
    /**
     * Gets true, if the ITable is in auto resize mode.
     * @return true, if the ITable is in auto resize mode.
     */
    public boolean isAutoResize();
    
    /**
     * Sets true, if the ITable is in auto resize mode.
     * @param pAutoResize true, if the ITable is in auto resize mode.
     */
    public void setAutoResize(boolean pAutoResize);

    /**
     * Gets the row height.
     * A negativ row height means automatic calculation of row height between min and max row height.
     * A positiv row height should be used as is. 
     * @return the row height.
     */
    public int getRowHeight();
    
    /**
     * Sets the row height.
     * A negativ row height means automatic calculation of row height between min and max row height.
     * A positiv row height should be used as is. 
     * @param pRowHeight the row height.
     */
    public void setRowHeight(int pRowHeight);

    /**
     * Gets the min row height for automatic calculation.
     * @return the min row height for automatic calculation.
     */
    public int getMinRowHeight();
    
    /**
     * Sets the min row height for automatic calculation.
     * @param pMinRowHeight the min row height for automatic calculation.
     */
    public void setMinRowHeight(int pMinRowHeight);

    /**
     * Gets the max row height for automatic calculation.
     * @return the max row height for automatic calculation.
     */
    public int getMaxRowHeight();
    
    /**
     * Sets the max row height for automatic calculation.
     * @param pMaxRowHeight the max row height for automatic calculation.
     */
    public void setMaxRowHeight(int pMaxRowHeight);

	/**
     * Gets the visibility of the table header.
     *
     * @return the visibility of the table header.
     */
    public boolean isTableHeaderVisible();

	/**
     * Sets the visibility of the table header.
     *
     * @param pTableHeaderVisible the visibility of the table header.
     */
    public void setTableHeaderVisible(boolean pTableHeaderVisible);

	/**
     * Gets true, if it is possible sorting the data by clicking on the header.
     *
     * @return true, if it is possible sorting the data by clicking on the header.
     */
    public boolean isSortOnHeaderEnabled();

	/**
     * Set true, if it should possible sorting the data by clicking on the header.
     *
     * @param pSortOnHeaderEnabled true, if it is possible sorting the data by clicking on the header.
     */
    public void setSortOnHeaderEnabled(boolean pSortOnHeaderEnabled);

	/**
	 * Sets the translation for this table.
	 * 
	 * @param pTranslation the translation mapping
	 */
	public void setTranslation(TranslationMap pTranslation);
	
	/**
	 * Gets the translation for this table.
	 * 
	 * @return the translation mapping
	 */
	public TranslationMap getTranslation();

	/**
	 * Gets if showing the selection or not.
	 * 
	 * @return showing the selection or not.
	 */
	public boolean isShowSelection();

	/**
	 * Sets if showing the selection or not.
	 * 
	 * @param pShowSelection showing the selection or not.
	 */
	public void setShowSelection(boolean pShowSelection);

	/**
	 * Gets if showing the focus rect or not.
	 * 
	 * @return showing the focus rect or not.
	 */
	public boolean isShowFocusRect();

	/**
	 * Sets if showing the focus rect or not.
	 * 
	 * @param pShowFocusRect showing the focus rect or not.
	 */
	public void setShowFocusRect(boolean pShowFocusRect);

	/**
	 * Gets if showing the vertical lines or not.
	 * 
	 * @return showing the vertical lines or not.
	 */
	public boolean isShowVerticalLines();

	/**
	 * Sets if showing the vertical lines or not.
	 * 
	 * @param pShowVerticalLines showing the vertical lines or not.
	 */
	public void setShowVerticalLines(boolean pShowVerticalLines);

	/**
	 * Gets if showing the vertical lines or not.
	 * 
	 * @return showing the vertical lines or not.
	 */
	public boolean isShowHorizontalLines();

	/**
	 * Sets if showing the vertical lines or not.
	 * 
	 * @param pShowHorizontalLines showing the vertical lines or not.
	 */
	public void setShowHorizontalLines(boolean pShowHorizontalLines);

	/**
	 * True, if the mouse event occured on current selected cell.
	 * 
	 * @return True, if the mouse event occured on current selected cell.
	 */
	public boolean isMouseEventOnSelectedCell();

}	// ITable
