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
 * 20.02.2014 - [HM] - creation
 */
package javax.rad.model.ui;

import javax.rad.model.IDataBook;

/**
 * The <code>IControllable</code> to allow external control for {@link IControl}s.
 * 
 * @author Martin Handsteiner
 */
public interface IControllable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** Command for selecting the first row. */
	public static final String COMMAND_FIRST	= "COMMAND_FIRST";
	
	/** Command for selecting the last row. */
	public static final String COMMAND_LAST		= "COMMAND_LAST";
	
	/** Command for selecting the next row. */
	public static final String COMMAND_NEXT		= "COMMAND_NEXT";
	
	/** Command for selecting the previous row. */
	public static final String COMMAND_PREVIOUS = "COMMAND_PREVIOUS";
	
	/** Command for start editing. */
	public static final String COMMAND_EDIT = "COMMAND_EDIT";
	
	/** Command for inserting a row. */
	public static final String COMMAND_INSERT = "COMMAND_INSERT";
	
	/** Command for inserting a detail row. */
	public static final String COMMAND_INSERT_SUB = "COMMAND_INSERT_SUB";
	
	/** Command for restore a row. */
	public static final String COMMAND_RESTORE = "COMMAND_RESTORE";
	
	/** Command for deleting a row. */
	public static final String COMMAND_DELETE = "COMMAND_DELETE";
	
	/** Command for deleting a row. */
	public static final String COMMAND_DUPLICATE = "COMMAND_DUPLICATE";
	
	/** Command for exporting the data. */
	public static final String COMMAND_EXPORT = "COMMAND_EXPORT";
	
	/** Command for searching the data. */
	public static final String COMMAND_SEARCH = "COMMAND_SEARCH";
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the <code>IController</code> for this <code>IControllable</code>.
	 * 
	 * @return the <code>IController</code>
	 */
	public IController getController();

	/**
	 * Sets the <code>IController</code> for this <code>IControllable</code>.
	 * 
	 * @param pController the <code>IController</code>
	 */
	public void setController(IController pController);
	
	/**
	 * Gets the controllable <code>IDataBook</code> for this <code>IControllable</code>.
	 * IEditorControl should return the set data row, if it is instanceof IDataBook or null otherwise.
	 * ITableControl should return the set data book.
	 * ITreeControl should return the data book of the current selected node.
	 * The controller has to be informed, If the databook changes on a focused IControllable.
	 * eg: ITreeControl node selection of a different databook.
	 * Editor or table has the focus, and a different databook is set on editor or table. 
	 * 
	 * @return the controllable <code>IDataBook</code>
	 */
	public IDataBook getActiveDataBook();

	/**
	 * Gets true, if the command should be enabled.
	 * 
	 * @param pCommand the command
	 * @return true, if the command should be enabled.
	 */
	public boolean isCommandEnabled(String pCommand);
	
	/**
	 * Performs the command.
	 * 
	 * @param pCommand the command
	 * @throws Throwable if the command fails.
	 */
	public void doCommand(String pCommand) throws Throwable;

}	// IControllable
