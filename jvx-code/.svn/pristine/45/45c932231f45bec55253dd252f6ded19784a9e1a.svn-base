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
 * 01.10.2008 - [RH] - creation
 * 31.10.2008 - [MH] - interface change, the ICellEditor is now responsable for setting
 *                     and getting the value, the ICellEditorListener has store and restore.                     
 */
package javax.rad.model.ui;

import javax.rad.model.IDataRow;

/**
 * The <code>ICellEditor</code> provides the generation of the physical editor component, 
 * handles correct all events, and gives standard access to edited values.
 * 
 * @author Roland Hörmann, Martin Handsteiner
 */
public interface ICellEditor
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates an <code>ICellEditorHandler</code> library dependent component.
	 * 
	 * @param pCellEditorListener the <code>ICellEditorListener</code>
	 * @param pDataRow	the <code>IDataRow</code>
	 * @param pColumnName	the column name
	 * @return the <code>ICellEditorHandler</code>.
	 */
	public ICellEditorHandler createCellEditorHandler(ICellEditorListener pCellEditorListener, 
													  IDataRow pDataRow, 
													  String pColumnName);

	/**
	 * Defines, if the editor should edit directly in complex controls.
	 * 
	 * @return the <code>true</code> if editor will edit directly, <code>false</code> otherwise.
	 */
	public boolean isDirectCellEditor();

} 	// ICellEditor
