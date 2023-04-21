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
 * 28.03.2014 - [HM] - creation
 */
package com.sibvisions.rad.ui.web.impl.celleditor;

import javax.rad.model.IDataPage;
import javax.rad.model.IDataRow;
import javax.rad.model.ui.ICellEditorHandler;
import javax.rad.model.ui.ICellEditorListener;

import com.sibvisions.rad.ui.celleditor.AbstractCheckBoxCellEditor;
import com.sibvisions.rad.ui.web.impl.WebFactory;

/**
 * Web server implementation of {@link javax.rad.ui.celleditor.ICheckBoxCellEditor}.
 * 
 * @author Martin Handsteiner
 */
public class WebCheckBoxCellEditor extends AbstractCheckBoxCellEditor
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of <code>WebCheckBoxCellEditor</code>.
	 *
	 * @see javax.rad.ui.celleditor.ICheckBoxCellEditor
	 */
	public WebCheckBoxCellEditor()
	{
		setHorizontalAlignment(ALIGN_CENTER);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public ICellEditorHandler createCellEditorHandler(ICellEditorListener pCellEditorListener, IDataRow pDataRow, String pColumnName)
	{
		return new DefaultCellEditorHandler(this, pCellEditorListener, pDataRow, pColumnName);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Object getCellRendererComponent(Object pParentComponent,
										   IDataPage pDataPage,
										   int pRowNumber,
										   IDataRow pDataRow,
										   String pColumnName,
										   boolean pIsSelected,
										   boolean pHasFocus)
	{
		return null;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the class name.
	 * 
	 * @return the class name
	 */
	public String getClassName()
	{
		return WebFactory.getClassName(this);
	}	
	
}	// WebCheckBoxCellEditor
