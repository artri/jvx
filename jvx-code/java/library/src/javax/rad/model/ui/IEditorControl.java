/*
 * Copyright 2010 SIB Visions GmbH
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
 * 14.10.2010 - [HM] - creation
 * 09.02.2012 - [JR] - #548: get/setCellEditor added
 */
package javax.rad.model.ui;

import javax.rad.model.IDataRow;
import javax.rad.model.ModelException;

/**
 * The <code>IEditorControl</code> is an IControl that displays the value of one column in an IDataRow.
 * 
 * @author Martin Handsteiner
 */
public interface IEditorControl extends IControl
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Returns the DataRow displayed by this control.
     *
     * @return the DataRow.
     * @see #setDataRow
     */
    public IDataRow getDataRow();

    /**
     * Sets the DataRow displayed by this control.
     * 
	 * @param pDataRow the DataRow
     * @throws ModelException if the column name is invalid
     * @see #getDataRow
     * @see #setColumnName(String)
     */
    public void setDataRow(IDataRow pDataRow) throws ModelException;
	
    /**
     * Returns the column name displayed by this control.
     *
     * @return the column name.
     * @see #setColumnName
     */
    public String getColumnName();

    /**
     * Sets the column name displayed by this control.
     * 
	 * @param pColumnName the column name.
     * @throws ModelException if the column name is invalid
     * @see #getColumnName
     */
    public void setColumnName(String pColumnName) throws ModelException;
	
	/**
     * Gets the CellEditor that edits the given column in the given DataRow.
     * If the CellEditor is null, the editor from the columns DataType is used to edit.
     *
     * @return the CellEditor.
     * @see #setCellEditor
     */
	public ICellEditor getCellEditor();
	
	/**
     * Sets the CellEditor that edits the given column in the given DataRow.
     * If the CellEditor is null, the editor from the columns DataType is used to edit.
     *
     * @param pCellEditor the CellEditor.
     * @throws ModelException if the column name of the editor is invalid
     * @see #getCellEditor
     */
	public void setCellEditor(ICellEditor pCellEditor) throws ModelException;
    
} 	// IEditorControl
