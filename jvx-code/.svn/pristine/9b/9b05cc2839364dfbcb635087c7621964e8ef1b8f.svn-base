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
 * 09.04.2009 - [RH] - creation
 * 18.04.2009 - [RH] - get/set/DataPage/RowIndex moved to ChangeableDataRow      
 * 31.03.2011 - [RH] - #163 - IChangeableDataRow should support isWritableColumnChanged       
 * 19.12.2012 - [RH] - code review - documentation add                     
 */
package javax.rad.model;

/**
 * The {@link IChangeableDataRow} is an {@link IDataRow} extension which adds
 * support for monitoring the state of the {@link IDataRow} (whether it is
 * currently changing or not, and what changes are occurring).
 * 
 * @see IDataRow
 * @see IDataPage
 * @see IDataBook
 * 
 * @author Roland Hörmann
 */
public interface IChangeableDataRow extends IDataRow
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the parent {@link IDataPage}.
	 * 
	 * @return the the parent {@link IDataPage}, can {@code null} if there is no
	 *         parent {@link IDataPage}.
	 */
	public IDataPage getDataPage();
	
	/**
	 * Gets the row index within the parent {@link IDataPage}.
	 * 
	 * @return the row index inside the parent {@link IDataPage}, can be
	 *         {@code -1} if there is no parent {@link IDataPage}.
	 */
	public int getRowIndex();
	
	/**
	 * Gets the internal unique identifier for this {@link IDataRow}.
	 * 
	 * @return the internal unique identifier for this {@link IDataRow}.
	 * @throws ModelException if the unique identifier could not be determined.
	 */
	public Object getUID() throws ModelException;
	
	/**
	 * Gets if this row is currently being inserted, which means that it has
	 * been created but has not yet been saved/finalized.
	 * 
	 * @return {@code true} if this row is currently being inserted.
	 * @throws ModelException if the state could not be determined.
	 */
	public boolean isInserting() throws ModelException;
	
	/**
	 * Gets if this row is currently being updated, which means that its values
	 * are changed but these changes have not yet been saved/finalized.
	 * 
	 * @return {@code true} if this row is currently being changed.
	 * @throws ModelException if the state could not be determined.
	 */
	public boolean isUpdating() throws ModelException;
	
	/**
	 * Gets if this row is currently being deleted, which means that it has been
	 * removed/deleted but this action has not yet been finalized.
	 * 
	 * @return if this row is currently being deleted.
	 * @throws ModelException if the state could not be determined.
	 */
	public boolean isDeleting() throws ModelException;
	
	/**
	 * Gets if any associated detail rows have been modified. A modified row has
	 * either been updated, deleted or was inserted.
	 * 
	 * @return {@code true} if any associated detail rows have been modified.
	 * @throws ModelException if the state of the detail rows could not be
	 *             determined.
	 */
	public boolean isDetailChanged() throws ModelException;
	
	/**
	 * Gets the original {@link IDataRow}, before any changes were performed.
	 * 
	 * @return the original {@link IDataRow}, before any changes were performed.
	 * @throws ModelException if the original {@link IDataRow} could not be got.
	 * @deprecated since 2.5, use {@link #getOriginalDataRow()}.
	 */
	@Deprecated
	public IDataRow getOriginalRow() throws ModelException;
	
	/**
	 * Gets the original {@link IDataRow}, before any changes were performed.
	 * 
	 * @return the original {@link IDataRow}, before any changes were performed.
	 * @throws ModelException if the original {@link IDataRow} could not be got.
	 */
	public IDataRow getOriginalDataRow() throws ModelException;
	
    /**
     * Gets if there was a writeable column changed. A writeable column is a
     * column which can be written to/saved.
     * 
     * @return {@code true} if there was a writeable column changed.
     * @throws ModelException if the state could not be determined.
     */
    public boolean isChanged() throws ModelException;
    
	/**
	 * Gets if there was a writeable column changed. A writeable column is a
	 * column which can be written to/saved.
	 * 
	 * @return {@code true} if there was a writeable column changed.
	 * @throws ModelException if the state could not be determined.
	 */
	public boolean isWritableColumnChanged() throws ModelException;
	
} 	// IChangeableDataRow
