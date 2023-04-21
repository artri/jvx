/*
 * Copyright 2021 SIB Visions GmbH
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
 * 27.07.2021 - [JR] - creation
 */
package com.sibvisions.rad.ui.web.impl.celleditor;

import javax.rad.model.IDataRow;
import javax.rad.model.ModelException;
import javax.rad.model.ui.ICellEditor;
import javax.rad.model.ui.ICellEditorHandler;
import javax.rad.model.ui.ICellEditorListener;

/**
 * The <code>DefaultCellEditorHandler</code> is a default implementation of {@link ICellEditorHandler}. It
 * wraps an {@link ICellEditor} as editor component.
 * 
 * @author René Jahn
 */
public class DefaultCellEditorHandler implements ICellEditorHandler<ICellEditorHandler> 
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the cell editor. */
	private ICellEditor cellEditor;
	
	/** the cell editor listener. */
	private ICellEditorListener cellEditorListener;
	
	/** the data row. */
	private IDataRow dataRow;
	
	/** the column name. */
	private String columnName;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>DefaultCellEditorHandler</code>.
	 * 
	 * @param pEditor the cell editor
	 * @param pListener the cell editor listener
	 * @param pDataRow the data row
	 * @param pColumnName the column name
	 */
	public DefaultCellEditorHandler(ICellEditor pEditor, ICellEditorListener pListener, IDataRow pDataRow, String pColumnName)
	{
		cellEditor = pEditor;
		cellEditorListener = pListener;
		dataRow = pDataRow;
		columnName = pColumnName;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface Implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public void uninstallEditor() 
	{
	}

	/**
	 * {@inheritDoc}
	 */
	public ICellEditor getCellEditor() 
	{
		return cellEditor;
	}

	/**
	 * {@inheritDoc}
	 */
	public ICellEditorListener getCellEditorListener() 
	{
		return cellEditorListener;
	}

	/**
	 * {@inheritDoc}
	 */
	public IDataRow getDataRow() 
	{
		return dataRow;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getColumnName() 
	{
		return columnName;
	}

	/**
	 * {@inheritDoc}
	 */
	public ICellEditorHandler getCellEditorComponent() 
	{
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public void saveEditing() throws ModelException 
	{
	}

	/**
	 * {@inheritDoc}
	 */
	public void cancelEditing() throws ModelException 
	{
	    //force sync
	    dataRow.getValue(columnName);
	}
 
}	// DefaultCellEditorHandler
