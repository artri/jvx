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
 * 10.11.2008 - [HM] - creation
 * 22.04.2014 - [RZ] - #1014: added get/setDisplayReferencedColumnName
 */
package javax.rad.ui.celleditor;

import javax.rad.model.ColumnView;
import javax.rad.model.condition.ICondition;
import javax.rad.model.reference.ColumnMapping;
import javax.rad.model.reference.ReferenceDefinition;
import javax.rad.ui.IDimension;

/**
 * Platform and technology independent linked editor definition.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 */
public interface ILinkedCellEditor extends IComboCellEditor
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets reference definition that defines the link.
	 * 
	 * @return the link reference definition.
	 */
	public ReferenceDefinition getLinkReference();

	/**
	 * Sets reference definition that defines the link.
	 * 
	 * @param pReferenceDefinition the link reference definition.
	 */
	public void setLinkReference(ReferenceDefinition pReferenceDefinition);

	/**
	 * Gets the additional condition, that is combined with And.
	 * 
	 * @return the additional condition.
	 */
	public ICondition getAdditionalCondition();

	/**
	 * Sets the additional condition, that is combined with And.
	 * 
	 * @param pCondition the additional condition.
	 */
	public void setAdditionalCondition(ICondition pCondition);

	/**
	 * Gets search columns for which should be searched in the linked cell.
	 * 
	 * @return search columns for which should be searched in the linked cell.
	 */
	public ColumnMapping getSearchColumnMapping();

	/**
	 * Sets search columns for which should be searched in the linked cell.
	 * 
	 * @param pSearchColumnNames search columns for which should be searched in the linked cell.
	 */
	public void setSearchColumnMapping(ColumnMapping pSearchColumnNames);

	/**
	 * True, if the linked cell editor should sort by the column name it is editing.
	 * Default is false.
	 * 
	 * @return true, if the linked cell editor should sort by the column name it is editing.
	 */
	public boolean isSortByColumnName();

	/**
	 * True, if the linked cell editor should sort by the column name it is editing.
	 * Default is false.
	 * 
	 * @param pSortByColumnName true, if the linked cell editor should sort by the column name it is editing.
	 */
	public void setSortByColumnName(boolean pSortByColumnName);

	/**
	 * Gets whether the table in the popup readonly.
	 *
	 * @return the table in the popup readonly.
	 */
	public boolean isTableReadonly();

	/**
	 * Sets the table in the popup readonly.
	 *
	 * @param pTableReadonly sets the table in the popup readonly.
	 */
	public void setTableReadonly(boolean pTableReadonly);

    /**
     * Gets whether the table header visibility is determined automatically.
     *
     * @return whether the table header visibility is determined automatically.
     */
    public boolean isAutoTableHeaderVisibility();

	/**
	 * Gets whether the table header is visible.
	 *
	 * @return whether the table header is visible.
	 */
	public boolean isTableHeaderVisible();

	/**
	 * Sets the table header visible.
	 *
	 * @param pTableHeaderVisible the table header visible.
	 */
	public void setTableHeaderVisible(boolean pTableHeaderVisible);

	/**
	 * Gets true, if only values from the table are allowed.
	 *
	 * @return true, if only values from the table are allowed.
	 */
	public boolean isValidationEnabled();

	/**
	 * Sets true, if only values from the table are allowed.
	 *
	 * @param pValidationEnabled true, if only values from the table are allowed.
	 */
	public void setValidationEnabled(boolean pValidationEnabled);
	
	/**
	 * True, if the text is searched anywhere inside a column.
	 * Default is true.
	 * 
	 * @return True, if the text is searched anywhere inside a column.
	 */
	public boolean isSearchTextAnywhere();

	/**
	 * True, if the text is searched anywhere inside a column.
	 * Default is true.
	 * 
	 * @param pSearchTextAnywhere True, if the text is searched anywhere inside a column.
	 */
	public void setSearchTextAnywhere(boolean pSearchTextAnywhere);

	/**
	 * True, if the text is searched in all visible table columns.
	 * Default is false.
	 * 
	 * @return True, if the text is searched in all visible table columns.
	 */
	public boolean isSearchInAllTableColumns();

	/**
	 * True, if the text is searched in all visible table columns.
	 * Default is false.
	 * 
	 * @param pSearchInAllTableColumns True, if the text is searched in all visible table columns.
	 */
	public void setSearchInAllTableColumns(boolean pSearchInAllTableColumns);

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
     * Gets the size of the Popup window using a <code>Dimension</code> object.
     * Null means, that the Popup window opens with preferredSize.
     *
     * @return the Popup size of.
     */
	public IDimension getPopupSize();
	
    /**
     * Sets the size of the Popup window using a <code>Dimension</code> object.
     * Null means, that the Popup window opens with preferredSize.
     *
     * @param pPopupSize the Popup size of.
     */
	public void setPopupSize(IDimension pPopupSize);
	
	/**
	 * Gets the name of the referenced column that is used for displaying values.
	 * These values will be displayed instead of the default-shown values.
	 * Will return {@code null} if no display referenced column name is set and
	 * the editors default behavior decides what is displayed.
	 * 
	 * @return the name of the display referenced column. {@code null} if not set.
	 */
	public String getDisplayReferencedColumnName();
	
	/**
	 * Sets the name of the referenced column that is used for displaying values.
	 * These values will be displayed instead of the default-shown values.
	 * 
	 * @param pDisplayReferencedColumnName the name of the display referenced column.
	 */
	public void setDisplayReferencedColumnName(String pDisplayReferencedColumnName);
	
	/**
	 * Gets the concat mask that is used for displaying values.
     * In case of no wildcards, all visible columns shown in the popup will be concatinated with the given mask.
     * e.g. " ", " - ", ... will lead in  "AT 1220 Vienna",  "AT - 1220 - Vienna"
     * In case of wildcards, the stars are replaced in order of visible columns
     * e.g. "* (*)" will lead in "AT (1220)"
	 * 
	 * @return the concat mask that is used for displaying values. {@code null} if not set.
	 */
	public String getDisplayConcatMask();
	
	/**
	 * Gets the concat mask that is used for displaying values.
	 * In case of no wildcards, all visible columns shown in the popup will be concatinated with the given mask.
	 * e.g. " ", " - ", ... will lead in  "AT 1220 Vienna",  "AT - 1220 - Vienna"
	 * In case of wildcards, the stars are replaced in order of visible columns
	 * e.g. "* (*)" will lead in "AT (1220)"
	 * 
	 * @param pDisplayConcatMasks the concat mask that is used for displaying values. {@code null} if not set.
	 */
	public void setDisplayConcatMask(String pDisplayConcatMasks);
	
	/**
	 * Gets the columns, that should not be cleared in case validation enabled is false, 
	 * and more than one column has to be cleared.
	 * This could be the case with overlapping foreign keys, or for choosing default values with more than one column.
	 * 
	 * @return the columns, that should not be cleared in case validation enabled is false.
	 */
	public String[] getDoNotClearColumnNames();
	
	/**
	 * Sets the columns, that should not be cleared in case validation enabled is false, 
	 * and more than one column has to be cleared.
	 * This could be the case with overlapping foreign keys, or for choosing default values with more than one column.
	 * 
	 * @param pDoNotClearColumnNames the columns, that should not be cleared in case validation enabled is false.
	 */
	public void setDoNotClearColumnNames(String... pDoNotClearColumnNames);
	
	
}	// ILinkedCellEditor
