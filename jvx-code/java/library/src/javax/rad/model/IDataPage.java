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
 * 10.04.2009 - [RH] - interface review - size to getRowCount renamed
 *                                        getDataRow uses an ChangeableDataRow
 *                                        getMasterRow(), getChangedDataRows() added
 *                                        change state management is moved to ChangeableDataRow and MemDataBook     
 */
package javax.rad.model;

import javax.rad.model.condition.ICondition;

/**
 * The {@link IDataPage} is the abstract representation of multiple
 * {@link IDataRow}s, basically mimicking one set of rows or a result set.
 * <p>
 * The {@link IDataPage} is part of the of the {@link IDataBook}, as it can have
 * multiple {@link IDataPage}s. A {@link IDataRow} can also have a
 * {@link #getMasterDataRow() master} {@link IDataRow} which it is associated
 * with.
 * 
 * @see IDataRow
 * @see IDataBook
 * @see IChangeableDataRow
 * 
 * @author Roland Hörmann
 */
public interface IDataPage extends Iterable<IChangeableDataRow>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the parent {@link IDataBook}.
	 * 
	 * @return Gets the parent {@link IDataBook}.
	 */
	public IDataBook getDataBook();
	
	/**
	 * Gets the corresponding {@link IDataRow master row}.
	 * 
	 * @return the corresponding {@link IDataRow master row}.
	 * @throws ModelException if the {@link IDataRow master row} could not be
	 *             fetched for some reason.
	 */
	public IDataRow getMasterDataRow() throws ModelException;
	
	/**
	 * Gets a copy of the {@link IChangeableDataRow} at the specified index.
	 * Returns {@code null} if there is no row at the specified index.
	 * <p>
	 * If the row is currently not in memory, it will be fetched from the
	 * underlying storage.
	 * 
	 * @param pDataRowIndex the index of the {@link IChangeableDataRow} to get.
	 * @return a copy of the {@link IChangeableDataRow} at the specified index,
	 *         {@code null} if there is no row at the specified index.
	 * @throws ModelException if the {@link IChangeableDataRow} at the specified
	 *             index can no be got.
	 */
	public IChangeableDataRow getDataRow(int pDataRowIndex) throws ModelException;
	
	/**
	 * Gets the number of rows which are currently fetched.
	 * If {@link #isAllFetched()} is {@code false}, you first have to call {@link #fetchAll()},
	 * to get the whole amount of rows. 
	 * 
	 * @return the number of rows which are currently fetched.
	 * @throws ModelException if the number of rows could not be determined.
	 */
	public int getRowCount() throws ModelException;
	
	/**
	 * Fetches all {@link IDataRow}s from the storage.
	 * 
	 * @throws ModelException if there is a problem while fetching the rows.
	 */
	public void fetchAll() throws ModelException;
	
	/**
	 * Gets if all {@link IDataRow}s have been fetched from the storage, and
	 * there is nothing more to fetch.
	 * 
	 * @return {@code true} if all {@link IDataRow}s have been fetched.
	 * @throws ModelException if determining if all {@link IDataRow}s have been
	 *             fetched failed.
	 */
	public boolean isAllFetched() throws ModelException;
	
	/**
	 * Gets an int array containing the indexes of all changed {@link IDataRow}
	 * s. Returns an empty array if there are no changed {@link IDataRow}s.
	 * 
	 * @return an int array containing the indexes of all changed
	 *         {@link IDataRow}s. An empty array if there are no changes.
	 * @throws ModelException if a exception occurs during synchronize.
	 * @deprecated since 2.5, use {@link #getChangedRows()} instead. 
	 */
	@Deprecated
	public int[] getChangedDataRows() throws ModelException;
	
	/**
	 * Gets an int array containing the indexes of all changed {@link IDataRow}
	 * s. Returns an empty array if there are no changed {@link IDataRow}s.
	 * 
	 * @return an int array containing the indexes of all changed
	 *         {@link IDataRow}s. An empty array if there are no changes.
	 * @throws ModelException if a exception occurs during synchronize.
	 */
	public int[] getChangedRows() throws ModelException;
	
	/**
	 * Searches for the first occurrence of an {@link IDataRow} which matches
	 * the given {@link ICondition} and returns its index. {@code -1} is
	 * returned if there is no {@link IDataRow} that matches.
	 * <p>
	 * This method might fetch more rows from the storage as needed.
	 * 
	 * @param pCondition the {@link ICondition}.
	 * @return the index of the first matching {@link IDataRow}, {@code -1} if
	 *         there is none.
	 * @throws ModelException if searching through and/or fetching the
	 *             {@link IDataRow}s failed.
	 */
	public int searchNext(ICondition pCondition) throws ModelException;
	
	/**
	 * Searches for the next occurrence of an {@link IDataRow} which matches the
	 * given {@link ICondition} and returns its index. The search is started at
	 * the given index. {@code -1} is returned if there is no {@link IDataRow}
	 * that matches.
	 * <p>
	 * This method might fetch more rows from the storage as needed.
	 * 
	 * @param pCondition the {@link ICondition}.
	 * @param pStartIndex the index at which to start, inclusive.
	 * @return the index of the next matching {@link IDataRow}, {@code -1} if
	 *         there is none.
	 * @throws ModelException if searching through and/or fetching the
	 *             {@link IDataRow}s failed.
	 */
	public int searchNext(ICondition pCondition, int pStartIndex) throws ModelException;
	
	/**
	 * Searches for the last occurrence of an {@link IDataRow} which matches the
	 * given {@link ICondition} and returns its index. {@code -1} is returned if
	 * there is no {@link IDataRow} that matches.
	 * <p>
	 * This method might fetch more rows from the storage as needed.
	 * 
	 * @param pCondition the {@link ICondition}.
	 * @return the index of the last matching {@link IDataRow}, {@code -1} if
	 *         there is none.
	 * @throws ModelException if searching through and/or fetching the
	 *             {@link IDataRow}s failed.
	 */
	public int searchPrevious(ICondition pCondition) throws ModelException;
	
	/**
	 * Searches for the previous occurrence of an {@link IDataRow} which matches
	 * the given {@link ICondition} and returns its index. The search is started
	 * at the given index. {@code -1} is returned if there is no
	 * {@link IDataRow} that matches.
	 * <p>
	 * This method might fetch more rows from the storage as needed.
	 * 
	 * @param pCondition the {@link ICondition}.
	 * @param pStartIndex the index at which to start, exclusive.
	 * @return the index of the previous matching {@link IDataRow}, {@code -1}
	 *         if there is none.
	 * @throws ModelException if searching through and/or fetching the
	 *             {@link IDataRow}s failed.
	 */
	public int searchPrevious(ICondition pCondition, int pStartIndex) throws ModelException;
	
} 	// IDataPage
