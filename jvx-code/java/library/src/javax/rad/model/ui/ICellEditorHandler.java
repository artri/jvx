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
 * 10.11.2008 - [RH] - creation
 */
package javax.rad.model.ui;

import javax.rad.model.IDataRow;
import javax.rad.model.ModelException;

/**
 * The <code>ICellEditorHandler</code> holds the physical editor component, 
 * handles correct all events, and stores or restores the values.
 * 
 * @param <C> Placeholder for the library dependent component type.
 * 
 * @author Martin Handsteiner
 */
public interface ICellEditorHandler<C>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Informs the handler, that the editor is or will be discarded.
	 * This is the place for removing all listeners and disabling events
	 * coming after this call (Focus Event is a funny example...).
	 */
	public void uninstallEditor();

	/**
	 * Returns the <code>ICellEditor</code> that created this handler.
	 * 
	 * @return the <code>ICellEditor</code> that created this handler.
	 */
	public ICellEditor getCellEditor();

	/**
	 * Returns the <code>CellEditorListener</code>.
	 * 
	 * @return the <code>CellEditorListener</code>.
	 */
	public ICellEditorListener getCellEditorListener();

	/**
	 * Returns the <code>IDataRow</code> that is edited.
	 * 
	 * @return the <code>IDataRow</code> that is edited.
	 */
	public IDataRow getDataRow();

	/**
	 * Returns the column name that is edited in the <code>IDataRow</code>.
	 * 
	 * @return the column name that is edited in the <code>IDataRow</code>.
	 */
	public String getColumnName();

	/**
	 * Returns the library dependent <code>CellEditorComponent</code>.
	 * 
	 * @return the library dependent <code>CellEditorComponent</code>.
	 */
	public C getCellEditorComponent();

	/**
	 * Saves the changes made from the CellEditorComponent
	 * <code>DataBook</code>.
	 * @throws ModelException if the value can not be stored.
	 */
	public void saveEditing() throws ModelException;

	/**
	 * Informs the GUI control, that the last edit should be canceled(restored)
	 * the correct value is in the <code>DataBook</code>.
	 * @throws ModelException if the value can not be restored.
	 */
	public void cancelEditing() throws ModelException;

} 	// ICellEditorHandler
