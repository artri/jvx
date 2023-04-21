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
 * 03.11.2008 - [HM] - creation
 */
package javax.rad.model.ui;

/**
 * The ICellEditorListener will be informed from the ICellEditor,
 * if editing is complete, or canceled.
 * 
 * @author Martin Handsteiner
 */
public interface ICellEditorListener 
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constants
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** 
	 * CellEditorComponent has lost focus. 
	 * The focus should be delivered to the opposite component. 
	 */
	public static final String FOCUS_LOST = "FOCUS_LOST";
	/** 
	 * CellEditorComponent has completed editing with action key. 
	 * The action key is control dependent defined as key which changes the value.
	 * The current cell should be still selected. 
	 */
	public static final String ACTION_KEY = "ACTION_KEY";
	/** 
	 * CellEditorComponent has completed editing with enter key. 
	 * The next cell should be selected.
	 */
	public static final String ENTER_KEY = "ENTER_KEY";
	/** 
	 * CellEditorComponent has completed editing with enter key. 
	 * The previous cell should be selected.
	 */
	public static final String SHIFT_ENTER_KEY = "SHIFT_ENTER_KEY";
	/** 
	 * CellEditorComponent has completed editing with tab key.
	 * The next cell should be selected.
	 */
	public static final String TAB_KEY = "TAB_KEY";
	/** 
	 * CellEditorComponent has completed editing with shift tab key.
	 * The previous cell should be selected.
	 */
	public static final String SHIFT_TAB_KEY = "SHIFT_TAB_KEY";
	/** 
	 * CellEditorComponent has canceled editing with escape key.
	 * The current cell should be still selected. 
	 */
	public static final String ESCAPE_KEY = "ESCAPE_KEY";
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Informs the <code>ICellEditorListener</code> that the editing has started.
	 */
	public void editingStarted();
	
	/**
	 * Informs the <code>ICellEditorListener</code> that the editing is completed.
	 * 
	 * @param pCompleteType the type of completion.
	 */
	public void editingComplete(String pCompleteType);
	
	/**
	 * Tells whether the CellEditor should save immediate.
	 * @return whether the CellEditor should save immediate.
	 */
	public boolean isSavingImmediate();
	
	/**
	 * Get's the control corresponding to this cell editor listener.
	 * @return the control corresponding to this cell editor listener.
	 */
	public IControl getControl();
	
}	// ICellEditorListener
