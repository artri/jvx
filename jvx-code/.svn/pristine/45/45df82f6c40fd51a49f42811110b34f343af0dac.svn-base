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
 * 11.11.2008 - [RH] - DeleteCascade added, notifyMasterReferenceColumnsChanged(IDataRow) add
 * 13.11.2008 - [RH] - is/setUpdate/Delete/InsertEnabled() added; is/setReadOnly added 
 * 17.11.2008 - [RH] - Selection mode added; SetDefaultDelected removed
 *                     refresh removed(); store renamed to Save...(); other methods renamed
 * 10.04.2009 - [RH] - interface review - derived now from IChangeableDataRow
 * 16.04.2009 - [RH] - remove/add/getDetailDataBooks moved to IDataBook.    
 * 19.04.2009 - [RH] - Enum SelectionMode and WriteBackIsolationLevel          
 * 06.09.2010 - [RH] - comment corrected for getDataPage(...)   
 * 15.10.2010 - [RH] - detailChanged renamed to notifyDetailChanged 
 * 29.11.2010 - [JR] - searchNext added
 *                   - isInsertEnabled, isUpdateEnabled, isDeleteEnabled throws ModelException 
 * 13.05.2011 - [RH] - #350 - MemDataBook should remove all details in saveSelectedRow(), if a row is deleted   
 * 08.04.2013 - [RH] - saveDataPage() add (during saveAllDataRows Bug fix)    
 * 12.04.2013 - [RH] - Need isUpdate/Delete/InsertAllowed and isUpdate/Delete/InsertEnabled should only get what is set - realized!  
 */
package javax.rad.model;

import javax.rad.model.condition.ICondition;
import javax.rad.model.event.DataBookHandler;
import javax.rad.model.event.IReadOnlyChecker;
import javax.rad.model.event.IRowCalculator;
import javax.rad.model.reference.ReferenceDefinition;
import javax.rad.util.INamedObject;

/**
 * The {@link IDataBook} is a storage independent representation of table and
 * handles all the operations to load, save and change the data.
 * <p>
 * An {@link IDataBook} does have at least one {@link IDataPage} which then
 * holds all the {@link IDataRow}s.
 * <p>
 * If the {@link IDataBook} has an associated master, the current
 * {@link IDataPage} is selected according to the selection in the master
 * databook.
 * <p>
 * The {@link IDataBook} also implements the {@link IChangeableDataRow}, which
 * means that the databook is always its own currently selected {@link IDataRow}
 * .
 * 
 * @see IDataPage
 * @see IRowDefinition
 * @see IChangeableDataRow
 * @see IDataSource
 * 
 * @author Roland Hörmann
 */
public interface IDataBook extends IChangeableDataRow, 
                                   IDataPage, 
                                   INamedObject
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constants
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * The {@link SelectionMode} specifies which row should be selected after a
	 * reload or after the master row has changed.
	 */
	public enum SelectionMode
	{
		/**
		 * No row will be selected, if there is/was a selected row it will be
		 * unselected.
		 */
		DESELECTED,
		
		/**
		 * The first row will be selected.
		 */
		FIRST_ROW,
		
		/**
		 * The selection will be preserved and not changed. If that is not
		 * possible (for example the 20th row is selected, a new filter is
		 * applied which only leaves 15 rows) the first row will be selected.
		 * <p>
		 * Note that this might have a performance impact, if for example the
		 * 500th row is selected and a new filter is applied, the
		 * {@link IDataBook} will try to fetch up to the 500th row to reset the
		 * selection.
		 */
		CURRENT_ROW,
		
		/**
		 * The selection will be preserved and not changed. If that is not
		 * possible (for example the 20th row is selected, a new filter is
		 * applied which only leaves 15 rows) no row will be selected (removing
		 * the selection if needed).
		 * <p>
		 * Note that this might have a performance impact, if for example the
		 * 500th row is selected and a new filter is applied, the
		 * {@link IDataBook} will try to fetch up to the 500th row to reset the
		 * selection.
		 */
		CURRENT_ROW_DESELECTED,
		
		/**
		 * The selection will be preserved and not changed. If that is not
		 * possible (for example the 20th row is selected, a new filter is
		 * applied which only leaves 15 rows) the first row will be selected.
		 * <p>
		 * Note that this might have a performance impact, if for example the
		 * 500th row is selected and a new filter is applied, the
		 * {@link IDataBook} will try to fetch up to the 500th row to reset the
		 * selection.
		 */
		CURRENT_ROW_SETFILTER,
		
		/**
		 * The selection will be preserved and not changed. If that is not
		 * possible (for example the 20th row is selected, a new filter is
		 * applied which only leaves 15 rows) no row will be selected (removing
		 * the selection if needed).
		 * <p>
		 * Note that this might have a performance impact, if for example the
		 * 500th row is selected and a new filter is applied, the
		 * {@link IDataBook} will try to fetch up to the 500th row to reset the
		 * selection.
		 */
		CURRENT_ROW_DESELECTED_SETFILTER
	}
	
	/**
	 * The {@link WriteBackIsolationLevel} specifies when the changes in the
	 * {@link IDataBook} will be (implicitly) saved. This also includes
	 * notifications and synchronizations with the master or details.
	 */
	public enum WriteBackIsolationLevel
	{
		/**
		 * The changes are stored/saved when the current row is deselected or a
		 * different row is selected.
		 */
		DATA_ROW,
		
		/**
		 * The changes are only stored when {@link IDataBook#saveSelectedRow()},
		 * {@link IDataBook#saveAllRows()} or {@link IDataBook#saveDataPage()}
		 * is invoked.
		 */
		DATASOURCE
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Sets the {@link RowDefinition} to use.
	 * <p>
	 * The {@link IRowDefinition} contains all the information about the columns
	 * and it can only be set if the {@link IDataBook} is not already
	 * {@link #isOpen() open}.
	 * 
	 * @param pRowDefinition the {@link RowDefinition} to use.
	 * @throws ModelException if the {@link IDataBook} is already
	 *             {@link #isOpen() open}.
	 * @see #getRowDefinition()
	 * @see IRowDefinition
	 */
	public void setRowDefinition(IRowDefinition pRowDefinition) throws ModelException;
	
	/**
	 * Sets the {@link IDataSource} to use, which allows the {@link IDataBook}
	 * to retrieve and set data.
	 * <p>
	 * The {@link IDataSource} can only be set if the {@link IDataBook} is not
	 * already {@link #isOpen() open}.
	 * 
	 * @param pDataSource the {@link IDataSource} to use.
	 * @throws ModelException if the {@link IDataBook} is already
	 *             {@link #isOpen() open}.
	 * @see #getDataSource()
	 * @see IDataSource
	 */
	public void setDataSource(IDataSource pDataSource) throws ModelException;
	
	/**
	 * Gets the {@link IDataSource} that is used.
	 * 
	 * @return the {@link IDataSource} that is used.
	 * @see #setDataSource(IDataSource)
	 * @see IDataSource
	 */
	public IDataSource getDataSource();
	
	/**
	 * Sets the name to use.
	 * <p>
	 * The name can only be set if the {@link IDataBook} is not already
	 * {@link #isOpen() open}.
	 * 
	 * @param pName the name to use.
	 * @throws ModelException if the {@link IDataBook} is already
	 *             {@link #isOpen() open}.
	 * @see #getName()
	 */
	public void setName(String pName) throws ModelException;
	
	/**
	 * Gets the name that is used.
	 * 
	 * @return the name that is used.
	 * @see #setName(String)
	 */
	public String getName();
	
	/**
	 * Sets the {@link WriteBackIsolationLevel} to use.
	 * <p>
	 * The default value should be {@link WriteBackIsolationLevel#DATA_ROW}.
	 * 
	 * @param pIsolationLevel the {@link WriteBackIsolationLevel} to use.
	 * @see #getWritebackIsolationLevel()
	 * @see WriteBackIsolationLevel
	 * @throws ModelException if setting DATAROW level fails, because saveAllRows fails.  
	 */
	public void setWritebackIsolationLevel(WriteBackIsolationLevel pIsolationLevel) throws ModelException;
	
	/**
	 * Gets the {@link WriteBackIsolationLevel} that is used.
	 * <p>
	 * The default value should be {@link WriteBackIsolationLevel#DATA_ROW}.
	 * 
	 * @return the {@link WriteBackIsolationLevel} that is used.
	 * @see #setWritebackIsolationLevel(WriteBackIsolationLevel)
	 * @see WriteBackIsolationLevel
	 */
	public WriteBackIsolationLevel getWritebackIsolationLevel();
	
	/**
	 * Sets the {@link ReferenceDefinition master reference} that is going to be
	 * used.
	 * <p>
	 * The {@link ReferenceDefinition master reference} defines that this
	 * {@link IDataBook} is a detail of the defined master. If the masters
	 * selection changes, the current {@link IDataPage} will change accordingly.
	 * The master will also be notified if any change that occurs (like that a
	 * row is {@link #insert(boolean) inserted} / {@link #update() updated} /
	 * {@link #delete() deleted}.
	 * 
	 * <p>
	 * The {@link ReferenceDefinition master reference} can only be set if the
	 * {@link IDataBook} is not already {@link #isOpen() open}.
	 * 
	 * @param pReferenceDefinitionToMasterBook the {@link ReferenceDefinition
	 *            master reference} that is going to be used.
	 * @throws ModelException if the {@link IDataBook} is already
	 *             {@link #isOpen() open}.
	 * @see #getMasterReference()
	 * @see ReferenceDefinition
	 */
	public void setMasterReference(ReferenceDefinition pReferenceDefinitionToMasterBook) throws ModelException;
	
	/**
	 * Gets the {@link ReferenceDefinition master reference} that is used.
	 * <p>
	 * The {@link ReferenceDefinition master reference} defines that this
	 * {@link IDataBook} is a detail of the defined master. If the masters
	 * selection changes, the current {@link IDataPage} will change accordingly.
	 * 
	 * @return the {@link ReferenceDefinition master reference} that is used.
	 */
	public ReferenceDefinition getMasterReference();
	
	/**
	 * Gets if this {@link IDataBook} has been joined with itself.
	 * 
	 * @return {@code true} if this {@link IDataBook} has been joined with
	 *         itself.
	 */
	public boolean isSelfJoined();
	
	/**
	 * Gets the {@link IDataPage} for the given {@link TreePath}.
	 * 
	 * @param pTreePath the {@link TreePath}.
	 * @return the {@link IDataPage} for the given {@link TreePath}.
	 * @throws ModelException if the pRootRow or pTreePath don't contains the
	 *             master columns from the master
	 *             <code>ReferenceDefinition</code>
	 */
	public IDataPage getDataPage(TreePath pTreePath) throws ModelException;
	
	/**
	 * Gets the {@link IDataPage} for the given {@link TreePath} and the given
	 * {@link IDataRow root row}.
	 * 
	 * @param pRootRow the {@link IDataRow root row}.
	 * @param pTreePath the {@link TreePath}.
	 * @return the {@link IDataPage} for the given {@link TreePath} and
	 *         {@link IDataRow root row}.
	 * @throws ModelException if the pRootRow or pTreePath don't contains the
	 *             master columns from the master
	 *             <code>ReferenceDefinition</code>
	 */
	public IDataPage getDataPage(IDataRow pRootRow, TreePath pTreePath) throws ModelException;
	
	/**
	 * Returns the corresponding <code>IDataPage</code> to specified root row
	 * from the root <code>DataBook</code>. If it doesn't exists, it will be
	 * created and returned.
	 * 
	 * @param pRootRow the root <code>IDataRow</code> of the root
	 *            <code>DataBook</code>.
	 * @throws ModelException if the pRootRow don't contains the master columns
	 *             from the master <code>ReferenceDefinition</code>
	 * @return the corresponding <code>IDataPage</code> to specified master row
	 *         from the master <code>DataBook</code>.
	 */
	public IDataPage getDataPageWithRootRow(IDataRow pRootRow) throws ModelException;
	
	/**
	 * Gets the tree root reference.
	 * 
	 * @return gets the tree root reference.
	 */
	public ReferenceDefinition getRootReference();
	
	/**
	 * Gets the root data book.
	 * 
	 * @return gets the root reference.
	 */
	public IDataBook getRootDataBook();
	
	/**
	 * Sets the tree root reference.
	 * 
	 * @param pReferenceDefinition the tree root reference.
	 * @throws ModelException if the <code>IDataBook</code> is open
	 */
	public void setRootReference(ReferenceDefinition pReferenceDefinition) throws ModelException;
	
	/**
	 * Gets the tree current tree path.
	 * 
	 * @return gets the tree root refernce.
	 */
	public TreePath getTreePath();
	
	/**
	 * Sets the tree current tree path.
	 * 
	 * @param pTreePath the tree root reference.
	 * @throws ModelException if the <code>IDataBook</code> is open
	 */
	public void setTreePath(TreePath pTreePath) throws ModelException;
	
	/**
	 * Returns the corresponding <code>IDataPage</code> to specified master row
	 * from the master <code>DataBook</code>. If it doesn't exists, it will be
	 * created and returned.
	 * 
	 * @param pMasterRow the master <code>IDataRow</code> of the master
	 *            <code>DataBook</code>.
	 * @throws ModelException if the pMasterRow don't contains the master
	 *             columns from the master <code>ReferenceDefinition</code>
	 * @return the corresponding <code>IDataPage</code> to specified master row
	 *         from the master <code>DataBook</code>.
	 */
	public IDataPage getDataPage(IDataRow pMasterRow) throws ModelException;
	
	/**
	 * Returns true if an <code>IDataPage</code> to specified master row from
	 * the master <code>DataBook</code> exists.
	 * 
	 * @param pMasterDataRow the master <code>IDataRow</code> of the master
	 *            <code>DataBook</code>.
	 * @throws ModelException if the pMasterRow don't contains the master
	 *             columns from the master <code>ReferenceDefinition</code>
	 * @return true if an <code>IDataPage</code> to specified master row from
	 *         the master <code>DataBook</code> exists.
	 */
	public boolean hasDataPage(IDataRow pMasterDataRow) throws ModelException;
	
	/**
	 * Gets the {@link SelectionMode} that is used.
	 * 
	 * @return the {@link SelectionMode} that is used.
	 * @see #setSelectionMode(SelectionMode)
	 */
	public SelectionMode getSelectionMode();
	
	/**
	 * Sets the {@link SelectionMode} that should be used.
	 * 
	 * @param pSelectionMode the {@link SelectionMode} that should be used.
	 * @see #getSelectionMode()
	 */
	public void setSelectionMode(SelectionMode pSelectionMode);
	
	/**
	 * Sets the selected {@link IDataRow} to the given index. If the given index
	 * is less than zero or greater than the number of rows available, no row
	 * will be selected (if there is already a row selected, it will be
	 * unselected).
	 * <p>
	 * If the row at the given index is currently not in memory, it will be
	 * loaded from the storage.
	 * <p>
	 * Depending on the set {@link WriteBackIsolationLevel} changing the
	 * selection might save the previously selected row to the storage.
	 * <p>
	 * Before the selection is changed the
	 * {@link javax.rad.model.ui.IControl#saveEditing()} method of all
	 * registered {@link javax.rad.model.ui.IControl}s is invoked. Afterwards
	 * the {@link #eventBeforeRowSelected()} and
	 * {@link #eventAfterRowSelected()} events are fired in this order. The last
	 * action is that the {@link javax.rad.model.ui.IControl#notifyRepaint()}
	 * method of all registered {@link javax.rad.model.ui.IControl}s is invoked.
	 * 
	 * @param pDataRowIndex the index of the {@link IDataRow} to select. If it
	 *            is less than zero or greater than the number of rows
	 *            available, no row will be selected and an eventually already
	 *            selected row will be unselected.
	 * @throws ModelException if the {@link IDataBook} is not {@link #isOpen()
	 *             open} or if the {@link IDataRow} at the given index could not
	 *             be fetched from the storage or if the master
	 *             {@link IDataBook} does not have a selected row or if the
	 *             synchronization with the master failed.
	 * @see #getSelectedRow()
	 */
	public void setSelectedRow(int pDataRowIndex) throws ModelException;
	
	/**
	 * Gets the index of the selected row.
	 * <p>
	 * The index might {@code -1} if there is no row selected or if this
	 * {@link IDataBook} is not {@link #isOpen()}.
	 * 
	 * @return the index of the selected row, {@code -1} if there is none
	 *         selected or the {@link IDataBook} is not {@link #isOpen() open}.
	 * @throws ModelException if the synchronization with the master failed.
	 * @see #setSelectedRow(int)
	 */
	public int getSelectedRow() throws ModelException;
	
	/**
	 * Sets the selected column based on the given name.
	 * <p>
	 * If the given column name is {@code null} no column is selected (if there
	 * is already a row selected, it will be unselected).
	 * <p>
	 * Before the selection changes the {@link #eventBeforeColumnSelected()} is
	 * fired and afterwards the {@link #eventAfterColumnSelected()} is fired.
	 * 
	 * @param pSelectedColumn the name of the column to select, can be
	 *            {@code null} to remove the selection (if any).
	 * @throws ModelException if the {@link IDataBook} is not {@link #isOpen()
	 *             open} or if there is no column with the given name or if
	 *             synchronization with the master failed.
	 * @see #getSelectedColumn()
	 */
	public void setSelectedColumn(String pSelectedColumn) throws ModelException;
	
	/**
	 * Gets the name of the selected column.
	 * <p>
	 * The returned name might be {@code null} if there is no column selected.
	 * 
	 * @return the name of the selected column, {@code null} if there is none
	 *         selected.
	 * @throws ModelException if the synchronization with the master failed.
	 * @see #setSelectedColumn(String)
	 */
	public String getSelectedColumn() throws ModelException;
	
	/**
	 * Sets if a delete on a master {@link IDataBook} should also delete all
	 * rows associated with the master row in this {@link IDataBook}.
	 * 
	 * @param pDeleteCascade {@code true} if the rows in this {@link IDataBook}
	 *            should be deleted if the master row is deleted.
	 * @see #isDeleteCascade()
	 */
	public void setDeleteCascade(boolean pDeleteCascade);
	
	/**
	 * Gets if a delete on a master {@link IDataBook} should also delete all
	 * rows associated with the master row in this {@link IDataBook}.
	 * 
	 * @return {@code true} if the rows in this {@link IDataBook} should be
	 *         deleted if the master row is deleted.
	 * @see #setDeleteCascade(boolean)
	 */
	public boolean isDeleteCascade();
	
	/**
	 * Gets if it is possible to {@link #insert(boolean)} a new row.
	 * <p>
	 * An insert might not be possible if it is either {@link #isInsertEnabled()
	 * disabled} or if the storage does not allow an insert.
	 * 
	 * @return {@code true} if it is possible to {@link #insert(boolean)} a row.
	 * @throws ModelException if the state could not be determined.
	 * @see #isInsertEnabled()
	 * @see #setInsertEnabled(boolean)
	 */
	public boolean isInsertAllowed() throws ModelException;
	
	/**
	 * Gets if {@link #insert(boolean) inserting} is enabled.
	 * 
	 * @return {@code true} if {@link #insert(boolean) inserting} is enabled.
	 * @throws ModelException if the state could not be determined.
	 * @see #isInsertAllowed()
	 * @see #setInsertEnabled(boolean)
	 */
	public boolean isInsertEnabled() throws ModelException;
	
	/**
	 * Sets if {@link #insert(boolean) inserting} is enabled.
	 * 
	 * @param pInsertEnabled {@code true} if {@link #insert(boolean) inserting}
	 *            is enabled.
	 * @see #isInsertAllowed()
	 * @see #isInsertEnabled()
	 */
	public void setInsertEnabled(boolean pInsertEnabled);
	
	/**
	 * Gets if it is possible to {@link #setValue(String, Object) update} an
	 * already existing row.
	 * <p>
	 * An update might not be possible of the it is either
	 * {@link #isUpdateEnabled() disabled}, no {@link #getSelectedRow() no row
	 * is selected} or if the storage does not allow an update.
	 * 
	 * @return {@code true} if it is possible to
	 *         {@link #setValue(String, Object) update} a row.
	 * @throws ModelException if the state could not be determined.
	 * @see #isUpdateEnabled()
	 * @see #setUpdateEnabled(boolean)
	 */
	public boolean isUpdateAllowed() throws ModelException;
	
	/**
	 * Gets if {@link #setValue(String, Object)} updating a row is enabled.
	 * 
	 * @return {@code true} if {@link #setValue(String, Object)} updating a row
	 *         is enabled.
	 * @throws ModelException if the state could not be determined.
	 * @see #isUpdateAllowed()
	 * @see #setUpdateEnabled(boolean)
	 */
	public boolean isUpdateEnabled() throws ModelException;
	
	/**
	 * Sets if {@link #setValue(String, Object)} updating a row is enabled.
	 * 
	 * @param pUpdateEnabled {@code true} if {@link #setValue(String, Object)}
	 *            updating a row is enabled.
	 * @see #isUpdateAllowed()
	 * @see #isUpdateEnabled()
	 */
	public void setUpdateEnabled(boolean pUpdateEnabled);
	
	/**
	 * Gets if it is possible to {@link #delete() delete} an already existing
	 * row.
	 * <p>
	 * A delete might not be possible of the it is either
	 * {@link #isDeleteEnabled() disabled}, no {@link #getSelectedRow() no row
	 * is selected} or if the storage does not allow a delete.
	 * 
	 * @return {@code true} if it is possible to {@link #delete() delete} a row.
	 * @throws ModelException if the state could not be determined.
	 * @see #isDeleteEnabled()
	 * @see #setDeleteEnabled(boolean)
	 */
	public boolean isDeleteAllowed() throws ModelException;
	
	/**
	 * Gets if {@link #delete() deleting} a row is enabled.
	 * 
	 * @return {@code true} if {@link #delete() deleting} a row is enabled.
	 * @throws ModelException if the state could not be determined.
	 * @see #isDeleteAllowed()
	 * @see #setDeleteCascade(boolean)
	 */
	public boolean isDeleteEnabled() throws ModelException;
	
	/**
	 * Sets if {@link #delete() deleting} a row is enabled.
	 * 
	 * @param pDeleteEnabled {@code true} if {@link #delete() deleting} a row is
	 *            enabled.
	 * @see #isDeleteAllowed()
	 * @see #isDeleteEnabled()
	 */
	public void setDeleteEnabled(boolean pDeleteEnabled);
	
	/**
	 * Gets if this {@link IDataBook} is read only.
	 * 
	 * @return {@code true} this {@link IDataBook} is read only.
	 * @see #setReadOnly(boolean)
	 */
	public boolean isReadOnly();
	
	/**
	 * Sets if this {@link IDataBook} is read only.
	 * <p>
	 * If there are changes and the {@link IDataBook} is set to read only, the
	 * changed data is saved.
	 * 
	 * @param pReadOnlyEnabled {@code true} if this {@link IDataBook} is read
	 *            only.
	 * @throws ModelException if saving any changes failed.
	 * @see #isReadOnly()
	 */
	public void setReadOnly(boolean pReadOnlyEnabled) throws ModelException;
	
	/**
	 * Gets if this {@link IDataBook} is open.
	 * 
	 * @return {@code true} if this {@link IDataBook} is open.
	 * @see #open()
	 */
	public boolean isOpen();
	
	/**
	 * Opens this {@link IDataBook}.
	 * <p>
	 * "Open" means that all actions are performed which are needed to deliver
	 * data to the user, an {@link IDataBook} that is open can be used for
	 * querying and changing data.
	 * <p>
	 * Implementations should perform all needed set up work in this method
	 * (like reading metadata from the storage).
	 *
	 * @throws ModelException if there is no {@link #getName() name} set, or if
	 *             there is no column in the {@link #getRowDefinition() row
	 *             definition}, or if there is no {@link #getDataSource()} set.
	 */
	public void open() throws ModelException;
	
	/**
	 * Closes this {@link IDataBook}.
	 * <p>
	 * A closed {@link IDataBook} has the same state as an not
	 * {@link IDataBook#isOpen() open} {@link IDataBook}. It can't be used to
	 * query or change data, but it can be {@link #open reopened}.
	 * <p>
	 * Implementations should performs all clean up in this method and the
	 * (cached) data can be discarded at this point.
	 */
	public void close();
	
	/**
	 * Inserts a new {@link IDataRow} at the current position. If there is
	 * currently no {@link IDataRow} is selected the row will be inserted at the
	 * beginning. After this operation the new {@link IDataRow} will be
	 * selected.
	 * <p>
	 * Note that this is an {@link #setSelectedRow(int) selected row} changing
	 * operation.
	 * <p>
	 * The following events are fired in the following order:
	 * 
	 * <ol>
	 * <li>{@link javax.rad.model.ui.IControl#saveEditing()} is invoked on all
	 * registered {@link javax.rad.model.ui.IControl}s.</li>
	 * <li>{@link #eventBeforeInserting()}</li>
	 * <li>{@link #eventAfterInserting()}</li>
	 * <li>{@link #eventAfterRowSelected()}</li>
	 * <li>{@link javax.rad.model.ui.IControl#notifyRepaint()} is invoked on all
	 * registered {@link javax.rad.model.ui.IControl}s.</li>
	 * </ol>
	 * 
	 * @param pBeforeRow if the new row should be inserted before the current
	 *            selection.
	 * @return the index at which the new row has been inserted.
	 * @throws ModelException if the {@link IDataBook} is not {@link #isOpen()
	 *             open}, or if saving of already existing changes failed, or if
	 *             {@link #isInsertAllowed()} is disabled, or if saving the row
	 *             to the storage failed.
	 */
	public int insert(boolean pBeforeRow) throws ModelException;
	
	/**
	 * Sets the currently selected row as "updating". This also happens
	 * implicitly if a {@link #setValue(String, Object) value is set}. If there
	 * is no row selected a {@link ModelException} is thrown.
	 * <p>
	 * The storage might want to reload and eventually lock the row that is
	 * being updated.
	 * <p>
	 * Before the update is executed the {@link #eventBeforeUpdating()} is fired
	 * and afterwards the {@link #eventAfterUpdating()} is fired.
	 * 
	 * @throws ModelException if the {@link IDataBook} is not {@link #isOpen()
	 *             open}, or if {@link #isUpdateAllowed()} is disabled (for
	 *             example no row is selected), or if there was an error when
	 *             setting the state.
	 */
	public void update() throws ModelException;
	
	/**
	 * Deletes the currently selected row. If there is no row selected, a
	 * {@link ModelException} is thrown. It deletes the selected.
	 * <p>
	 * Before the delete is executed the {@link #eventBeforeDeleting()} is fired
	 * and afterwards the {@link #eventAfterDeleting()} is fired. As the row has
	 * been deleted at that point, the {@link #setSelectedRow(int) selected row}
	 * is changed to the next row.
	 * 
	 * @throws ModelException if the {@link IDataBook} is not {@link #isOpen()
	 *             open}, or if {@link #isDeleteAllowed()} is disabled (for
	 *             example no row is selected), or if there was an error when
	 *             setting the state.
	 */
	public void delete() throws ModelException;
	
	/**
	 * Notifies the master {@link IDataBook} that one or more detail rows have
	 * changed.
	 */
	public void notifyDetailChanged();
	
	/**
	 * Saves the currently selected row. If there is no row selected nothing
	 * happens.
	 * <p>
	 * Depending on the state of the row, either {@link #eventBeforeInserted()}/
	 * {@link #eventAfterInserted()}, {@link #eventBeforeUpdated()}/
	 * {@link #eventAfterUpdated()} or {@link #eventBeforeDeleted()}/
	 * {@link #eventAfterDeleted()} are fired. Afterwards the
	 * {@link javax.rad.model.ui.IControl#notifyRepaint()} method of all
	 * registered {@link javax.rad.model.ui.IControl}s is invoked.
	 * 
	 * @throws ModelException if the {@link IDataBook} is not {@link #isOpen()
	 *             open} or if the changes could not be saved.
	 * @see #saveAllRows()
	 */
	public void saveSelectedRow() throws ModelException;
	
	/**
	 * Restores the currently selected row to a state without changes. If there
	 * is no row selected, nothing happens.
	 * <p>
	 * "Restoring" means that all changes made by the user are reverted, if this
	 * {@link IDataBook} is backed by a database it would mean to refetch the
	 * row.
	 * <p>
	 * Before the restore the {@link #eventBeforeRestore()} is fired and
	 * afterwards the {@link #eventAfterRestore()} is fired. Afterwards the
	 * {@link javax.rad.model.ui.IControl#notifyRepaint()} method of all
	 * registered {@link javax.rad.model.ui.IControl}s is invoked.
	 * 
	 * @throws ModelException if the {@link IDataBook} is not {@link #isOpen()
	 *             open} or if the row could not be restored.
	 * @see #restoreAllRows()
	 */
	public void restoreSelectedRow() throws ModelException;
	
	/**
	 * Restores all rows to a state without changes.
	 * <p>
	 * "Restoring" means that all changes made by the user are reverted, if this
	 * {@link IDataBook} is backed by a database it would mean to refetch the
	 * row.
	 * <p>
	 * Restoring all rows does restore all rows, including those which do not
	 * match the currently set {@link #getFilter() filter}.
	 * <p>
	 * Before the restore of each row the {@link #eventBeforeRestore()} is fired
	 * and afterwards the {@link #eventAfterRestore()} is fired. Afterwards the
	 * {@link javax.rad.model.ui.IControl#notifyRepaint()} method of all
	 * registered {@link javax.rad.model.ui.IControl}s is invoked.
	 * 
	 * @throws ModelException if the {@link IDataBook} is not {@link #isOpen()
	 *             open} or if the row could not be restored.
	 * @see #restoreSelectedRow()
	 */
	public void restoreAllRows() throws ModelException;
	
    /**
     * Gets if this {@link IDataBook} has any changes in any {@link IDataPage}.
     * 
     * @return {@code true} if this {@link IDataBook} has any changes in any {@link IDataPage}.
     */
    public boolean hasChanges();
    
	/**
	 * Gets if this {@link IDataBook} is out of sync with its master.
	 * 
	 * @return {@code true} if this {@link IDataBook} is out of sync with its
	 *         master.
	 */
	public boolean isOutOfSync();
	
	/**
	 * Reloads all rows from the storage, any changes are discarded.
	 * <p>
	 * If the backing instance is a database, it means that all data has to be
	 * refetched from the database.
	 * <p>
	 * Before the reload the {@link javax.rad.model.ui.IControl#cancelEditing()}
	 * of all registered {@link javax.rad.model.ui.IControl}s is invoked. After
	 * that the {@link #eventBeforeReload()} is fired and afterwards the
	 * {@link #eventAfterReload()}. Afterwards the
	 * {@link javax.rad.model.ui.IControl#notifyRepaint()} method of all
	 * registered {@link javax.rad.model.ui.IControl}s is invoked.
	 * 
	 * @throws ModelException if the {@link IDataBook} is not {@link #isOpen()
	 *             open} or if the reload failed.
	 * @see #reload(SelectionMode)
	 */
	public void reload() throws ModelException;
	
	/**
	 * Reloads all rows from the storage (any changes are discarded) and applies
	 * the given {@link SelectionMode} to this reload operation.
	 * <p>
	 * If the backing instance is a database, it means that all data has to be
	 * refetched from the database.
	 * <p>
	 * Before the reload the {@link javax.rad.model.ui.IControl#cancelEditing()}
	 * of all registered {@link javax.rad.model.ui.IControl}s is invoked. After
	 * that the {@link #eventBeforeReload()} is fired and afterwards the
	 * {@link #eventAfterReload()}. Afterwards the
	 * {@link javax.rad.model.ui.IControl#notifyRepaint()} method of all
	 * registered {@link javax.rad.model.ui.IControl}s is invoked.
	 * 
	 * @param pSelectionMode the Selection mode to use
	 * @throws ModelException if the {@link IDataBook} is not {@link #isOpen()
	 *             open} or if the reload failed.
	 * @see #reload()
	 */
	public void reload(SelectionMode pSelectionMode) throws ModelException;
	
	/**
	 * Sets the {@link ICondition filter} that is used. If the given
	 * {@link ICondition filter} is {@code null} no filter will be applied to
	 * the data.
	 * <p>
	 * This call is schematically equal to {@link #reload()} with the exception
	 * that changes are saved before the {@link ICondition filter} is applied.
	 * 
	 * @param pFilter the {@link ICondition filter} to apply.
	 * @throws ModelException if the {@link IDataBook} is not {@link #isOpen()
	 *             open} or the data could not be reloaded.
	 * @see #getFilter()
	 * @see ICondition
	 */
	public void setFilter(ICondition pFilter) throws ModelException;
	
	/**
	 * Gets the used {@link ICondition filter}. Can be {@code null} if none is
	 * used or set.
	 * 
	 * @return the used {@link ICondition filter}, {@code null} if none used or
	 *         set.
	 * @see #setFilter(ICondition)
	 * @see ICondition
	 */
	public ICondition getFilter();
	
	/**
	 * Sets the {@link SortDefinition} that is used. The given
	 * {@link SortDefinition} can be {@code null} for no sorting.
	 * <p>
	 * This is schematically equal top {@link #setFilter(ICondition)}.
	 * 
	 * @param pSort the {@link SortDefinition} to use. Can be {@code null} for
	 *            no sorting.
	 * @throws ModelException if the changes could not be stored.
	 * @see #getSort()
	 * @see SortDefinition
	 */
	public void setSort(SortDefinition pSort) throws ModelException;
	
	/**
	 * Gets the used {@link SortDefinition}. Can be {@code null} if there is
	 * none set or used.
	 * 
	 * @return the used {@link SortDefinition}, {@code null} if there is none
	 *         set or used.
	 * @see #setSort(SortDefinition)
	 * @see SortDefinition
	 */
	public SortDefinition getSort();
	
	/**
	 * Registers the given {@link IDataBook} as a detail {@link IDataBook}.
	 * 
	 * @param pDataBook the {@link IDataBook} to register as a detail
	 *            {@link IDataBook}.
	 * @see #removeDetailDataBook(IDataBook)
	 * @see #getDetailDataBooks()
	 */
	public void addDetailDataBook(IDataBook pDataBook);
	
	/**
	 * Unregisters the given {@link IDataBook} as a detail {@link IDataBook}.
	 * 
	 * @param pDataBook the {@link IDataBook} to unregister as a detail
	 *            {@link IDataBook}.
	 * @see #addDetailDataBook(IDataBook)
	 * @see #getDetailDataBooks()
	 */
	public void removeDetailDataBook(IDataBook pDataBook);
	
	/**
	 * Gets all detail {@link IDataBook}s as array. The returned array might be
	 * empty if there are no detail {@link IDataBook}s.
	 * 
	 * @return all detail {@link IDataBook}s as array, the array might be empty
	 *         if there are none.
	 * @see #addDetailDataBook(IDataBook)
	 * @see #removeDetailDataBook(IDataBook)
	 */
	public IDataBook[] getDetailDataBooks();
	
	/**
	 * Notifies this {@link IDataBook} that its master {@link IDataBook} has
	 * changed.
	 */
	public void notifyMasterChanged();
	
	/**
	 * Gets the {@link IRowCalculator}.
	 * 
	 * @return the {@link IRowCalculator}.
	 */
	public IRowCalculator getRowCalculator();
	
	/**
	 * Sets the {@link IRowCalculator}.
	 * 
	 * @param pRowCalculator the {@link IRowCalculator}.
	 */
	public void setRowCalculator(IRowCalculator pRowCalculator);
	
	/**
	 * Gets the {@link IReadOnlyChecker}.
	 * 
	 * @return the {@link IReadOnlyChecker}.
	 */
	public IReadOnlyChecker getReadOnlyChecker();
	
	/**
	 * Sets the {@link IReadOnlyChecker}.
	 * 
	 * @param pReadOnlyChecker the {@link IReadOnlyChecker}..
	 */
	public void setReadOnlyChecker(IReadOnlyChecker pReadOnlyChecker);
	
	/**
	 * Gets the {@link DataBookHandler} for before row selected event.
	 * <p>
	 * This event is fired before the {@link #getSelectedRow() selection of the
	 * current row} changes.
	 * <p>
	 * This event fires every time the selection changes, not just if the
	 * selection is changed by the user.
	 * 
	 * @return the {@link DataBookHandler} for before row selected event.
	 */
	public DataBookHandler eventBeforeRowSelected();
	
	/**
	 * Gets the {@link DataBookHandler} for after row selected event.
	 * <p>
	 * This event is fired after the {@link #getSelectedRow() selection of the
	 * current row} changes.
	 * <p>
	 * This event fires every time the selection changes, not just if the
	 * selection is changed by the user.
	 * 
	 * @return the {@link DataBookHandler} for after row selected event.
	 */
	public DataBookHandler eventAfterRowSelected();
	
	/**
	 * Gets the {@link DataBookHandler} for before inserting event.
	 * <p>
	 * This event is fired before a {@link #insert(boolean) new row is inserted}
	 * .
	 * 
	 * @return the {@link DataBookHandler} for before inserting event.
	 */
	public DataBookHandler eventBeforeInserting();
	
	/**
	 * Gets the {@link DataBookHandler} for after inserting event.
	 * <p>
	 * This event is fired after a {@link #insert(boolean) new row is inserted}.
	 * 
	 * @return the {@link DataBookHandler} for after inserting event.
	 */
	public DataBookHandler eventAfterInserting();
	
	/**
	 * Gets the {@link DataBookHandler} for before inserted event.
	 * <p>
	 * This event is fired before an {@link #insert(boolean) inserted row} is
	 * {@link #saveSelectedRow() saved}.
	 * 
	 * @return the {@link DataBookHandler} for before inserted event.
	 */
	public DataBookHandler eventBeforeInserted();
	
	/**
	 * Gets the {@link DataBookHandler} for after inserted event.
	 * <p>
	 * This event is fired after an {@link #insert(boolean) inserted row} is
	 * {@link #saveSelectedRow() saved}.
	 * 
	 * @return the {@link DataBookHandler} for after inserted event.
	 */
	public DataBookHandler eventAfterInserted();
	
	/**
	 * Gets the {@link DataBookHandler} for before updating event.
	 * <p>
	 * This event is fired before a {@link #update() row is updated}.
	 * 
	 * @return the {@link DataBookHandler} for before updating event.
	 */
	public DataBookHandler eventBeforeUpdating();
	
	/**
	 * Gets the {@link DataBookHandler} for after updating event.
	 * <p>
	 * This event is fired after a {@link #update() row is updated}.
	 * 
	 * @return the {@link DataBookHandler} for after updating event.
	 */
	public DataBookHandler eventAfterUpdating();
	
	/**
	 * Gets the {@link DataBookHandler} for before updated event.
	 * <p>
	 * This event is fired before an {@link #update() updated row} is
	 * {@link #saveSelectedRow() saved}.
	 * 
	 * @return the {@link DataBookHandler} for before updated event.
	 */
	public DataBookHandler eventBeforeUpdated();
	
	/**
	 * Gets the {@link DataBookHandler} for after updated event.
	 * <p>
	 * This event is fired after an {@link #update() updated row} is
	 * {@link #saveSelectedRow() saved}.
	 * 
	 * @return the {@link DataBookHandler} for after updated event.
	 */
	public DataBookHandler eventAfterUpdated();
	
	/**
	 * Gets the {@link DataBookHandler} for before deleting event.
	 * <p>
	 * This event is fired before a {@link #delete() row is deleted}.
	 * 
	 * @return the {@link DataBookHandler} for before deleting event.
	 */
	public DataBookHandler eventBeforeDeleting();
	
	/**
	 * Gets the {@link DataBookHandler} for after deleting event.
	 * <p>
	 * This event is fired after a {@link #delete() row is deleted}.
	 * 
	 * @return the {@link DataBookHandler} for after deleting event.
	 */
	public DataBookHandler eventAfterDeleting();
	
	/**
	 * Gets the {@link DataBookHandler} for before deleted event.
	 * <p>
	 * This event is fired before a {@link #delete() deleted row} is
	 * {@link #saveSelectedRow() saved}.
	 * 
	 * @return the {@link DataBookHandler} for before deleted event.
	 */
	public DataBookHandler eventBeforeDeleted();
	
	/**
	 * Gets the {@link DataBookHandler} for after deleted event.
	 * <p>
	 * This event is fired after a {@link #delete() deleted row} is
	 * {@link #saveSelectedRow() saved}.
	 * 
	 * @return the {@link DataBookHandler} for after deleted event.
	 */
	public DataBookHandler eventAfterDeleted();
	
	/**
	 * Gets the {@link DataBookHandler} for before restore event.
	 * <p>
	 * This event is fired before a {@link #restoreSelectedRow() row is
	 * restored}.
	 * 
	 * @return the {@link DataBookHandler} for before restore event.
	 */
	public DataBookHandler eventBeforeRestore();
	
	/**
	 * Gets the {@link DataBookHandler} for after restore event.
	 * <p>
	 * This event is fired after a {@link #restoreSelectedRow() row is restored}
	 * .
	 * 
	 * @return the {@link DataBookHandler} for after restore event.
	 */
	public DataBookHandler eventAfterRestore();
	
	/**
	 * Gets the {@link DataBookHandler} for before reload event.
	 * <p>
	 * This event is fired before the {@link IDataBook} is {@link #reload()
	 * reloaded}.
	 * 
	 * @return the {@link DataBookHandler} for before reload event.
	 */
	public DataBookHandler eventBeforeReload();
	
	/**
	 * Gets the {@link DataBookHandler} for after reload event.
	 * <p>
	 * This event is fired after the {@link IDataBook} is {@link #reload()
	 * reloaded}.
	 * 
	 * @return the {@link DataBookHandler} for after reload event.
	 */
	public DataBookHandler eventAfterReload();
	
	/**
	 * Gets the {@link DataBookHandler} for before filter changed event.
	 * <p>
	 * This event is fired before the {@link #setFilter(ICondition) filter
	 * changes}.
	 * 
	 * @return the {@link DataBookHandler} for before filter changed event.
	 */
	public DataBookHandler eventBeforeFilterChanged();
	
	/**
	 * Gets the {@link DataBookHandler} for after filter changed event.
	 * <p>
	 * This event is fired after the {@link #setFilter(ICondition) filter
	 * changes}.
	 * 
	 * @return the {@link DataBookHandler} for after filter changed event.
	 */
	public DataBookHandler eventAfterFilterChanged();
	
	/**
	 * Gets the {@link DataBookHandler} for before sort changed event.
	 * <p>
	 * This event is fired before the {@link #setSort(SortDefinition) sort
	 * changes}.
	 * 
	 * @return the {@link DataBookHandler} for before sort changed event.
	 */
	public DataBookHandler eventBeforeSortChanged();
	
	/**
	 * Gets the {@link DataBookHandler} for after sort changed event.
	 * <p>
	 * This event is fired afterthe {@link #setSort(SortDefinition) sort
	 * changes}.
	 * 
	 * @return the {@link DataBookHandler} for after sort changed event.
	 */
	public DataBookHandler eventAfterSortChanged();
	
	/**
	 * Gets the {@link DataBookHandler} for before column selected event.
	 * <p>
	 * This event is fired before the {@link #setSelectedColumn(String) selected
	 * column} changes.
	 * 
	 * @return the {@link DataBookHandler} for before column selected event.
	 */
	public DataBookHandler eventBeforeColumnSelected();
	
	/**
	 * Gets the {@link DataBookHandler} for after column selected event.
	 * <p>
	 * This event is fired after the {@link #setSelectedColumn(String) selected
	 * column} changes.
	 * 
	 * @return the {@link DataBookHandler} for after column selected event.
	 */
	public DataBookHandler eventAfterColumnSelected();
	
    /**
     * Gets the {@link DataBookHandler} for opened event.
     * <p>
     * This event is fired after the {@link IDataBook} is open.
     * 
     * @return the {@link DataBookHandler} for opened event.
     */
    public DataBookHandler eventOpened();
    
    /**
     * Gets the {@link DataBookHandler} for closed event.
     * <p>
     * This event is fired after the {@link IDataBook} is closed.
     * Any exception in the listeners will be silently ignored.
     * 
     * @return the {@link DataBookHandler} for closed event.
     */
    public DataBookHandler eventClosed();
    
	/**
	 * Saves all {@link IDataRow}s which have been changed, it is the same as
	 * {@link #saveSelectedRow()} except that it is a mass operation on all
	 * rows.
	 * <p>
	 * Saving all rows does save all rows, including those which do not match
	 * the currently set {@link #getFilter() filter}.
	 * <p>
	 * Depending on the state of the rows, either {@link #eventBeforeInserted()}
	 * / {@link #eventAfterInserted()}, {@link #eventBeforeUpdated()}/
	 * {@link #eventAfterUpdated()} or {@link #eventBeforeDeleted()}/
	 * {@link #eventAfterDeleted()} are fired. Afterwards the
	 * {@link javax.rad.model.ui.IControl#notifyRepaint()} method of all
	 * registered {@link javax.rad.model.ui.IControl}s is invoked.
	 * <p>
	 * These events are fired for all changed rows.
	 * 
	 * @throws ModelException if the {@link IDataBook} is not {@link #isOpen()
	 *             open} or if the changes could not be saved.
	 * @see #saveSelectedRow()
	 */
	public void saveAllRows() throws ModelException;
	
	/**
	 * Deletes all {@link IDataRow}s which are currently available, it is the
	 * same as {@link #delete()} except that it is a mass operation on all rows.
	 * <p>
	 * Deleting all rows does only delete the currently available rows, rows
	 * which do not match the currently applied {@link #getFilter() filter} will
	 * not be deleted.
	 * <p>
	 * First the {@link #setSelectedRow(int) row is selected} and afterwards
	 * {@link #delete() deleted}.
	 * <p>
	 * These events are fired for all rows.
	 * 
	 * @throws ModelException if the {@link IDataBook} is not {@link #isOpen()
	 *             open} or if the changes (because of the selection change)
	 *             could not be saved.
	 * @see #delete()
	 * @deprecated since 2.5, use {@link #deleteAllRows()} instead.
	 */
	@Deprecated
	public void deleteAllDataRows() throws ModelException;
	
	/**
	 * Deletes all {@link IDataRow}s which are currently available, it is the
	 * same as {@link #delete()} except that it is a mass operation on all rows.
	 * <p>
	 * Deleting all rows does only delete the currently available rows, rows
	 * which do not match the currently applied {@link #getFilter() filter} will
	 * not be deleted.
	 * <p>
	 * First the {@link #setSelectedRow(int) row is selected} and afterwards
	 * {@link #delete() deleted}.
	 * <p>
	 * These events are fired for all rows.
	 * 
	 * @throws ModelException if the {@link IDataBook} is not {@link #isOpen()
	 *             open} or if the changes (because of the selection change)
	 *             could not be saved.
	 * @see #delete()
	 */
	public void deleteAllRows() throws ModelException;
	
	/**
	 * Removes the DataPage to the specified master DataRow or TreePath. e.g.
	 * used to delete all DataPages to the master.
	 * 
	 * @param pMasterDataRow the MasterDataRow to use.
	 * @param pTreePath the TreePath to use.
	 * @throws ModelException if the remove of the DataPage didn't worked out.
	 */
	public void removeDataPage(IDataRow pMasterDataRow, TreePath pTreePath) throws ModelException;
	
    /**
     * Restores all rows in the current DataPage.
     * 
     * @throws ModelException if restoreDataPage() fails
     */
    public void restoreDataPage() throws ModelException;
    
	/**
	 * Saves all rows in the current DataPage.
	 * 
	 * @throws ModelException if saveDataPage() fails
	 */
	public void saveDataPage() throws ModelException;
	
	/**
	 * Returns the additional data row. The additional data row is only in
	 * memory. If it is set to visible, it is shown as first row. All controls
	 * will show the content of this data row, if selected.
	 * 
	 * @return the additional data row.
	 * @throws ModelException if the data book is not open.
	 */
	public IDataRow getAdditionalDataRow() throws ModelException;
	
	/**
	 * True, if the additional data row is visible. The data row is available in
	 * row number 0.
	 * 
	 * @return true, if the additional data row is visible.
	 */
	public boolean isAdditionalDataRowVisible();
	
	/**
	 * Set true, if the additional data row is visible. The data row is
	 * available in row number 0.
	 * 
	 * @param pVisible true, if the additional data row is visible.
	 */
	public void setAdditionalDataRowVisible(boolean pVisible);
	
	/**
	 * Sets the selected row index relatively to the current
	 * <code>IDataPage</code>.<br>
	 * This should be used from the tree controls to show which one is selected.
	 * The difference is due to a visible additional row. In case is is visible,
	 * setSelectedRow(1) is equal to setSelectedRowInPage(0).
	 * 
	 * @param pDataRowIndex the selected row index relatively to the current
	 *            <code>IDataPage</code>.
	 * @throws ModelException if the row with the iDataRowIndex couldn't get
	 *             from the storage or if the <code>IDataBook</code> isn't open
	 *             or the master <code>IDataBook</code> has no selected row.
	 */
	public void setSelectedDataPageRow(int pDataRowIndex) throws ModelException;
	
	/**
	 * Returns the selected row index relatively to the current
	 * <code>IDataPage</code>.<br>
	 * This should be used from the tree controls to show which one is selected.
	 * The difference is due to a visible additional row. In case is is visible,
	 * setSelectedRow(1) is equal to setSelectedRowInPage(0).
	 * 
	 * @return the selected row index relatively to the current
	 *         <code>IDataPage</code>.
	 * @throws ModelException if a exception occurs during synchronize.
	 */
	public int getSelectedDataPageRow() throws ModelException;
	
} 	// IDataBook
