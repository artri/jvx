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
 * 26.11.2008 - [RH] - lockAndRefetch added()
 * 04.05.2009 - [RH] - interface review, renamed to IDBAccess
 * 23.10.2009 - [RH] - the ColumnMetaData & Primary Key Columns are replaced with a MetaData object in this interface.
 * 02.03.2010 - [RH] - reorganized MetaData -> ServerMetaData, ColumnMetaData -> ServerColumnMetaData
 */
package com.sibvisions.rad.persist.jdbc;

import java.util.List;

import javax.rad.model.SortDefinition;
import javax.rad.model.condition.ICondition;
import javax.rad.persist.DataSourceException;


/**
 * The <code>IDBAccess</code> defines the methods to access any kind of table 
 * oriented storage: e.g. a database, XML file, ...<br>
 * <br>
 * It extends the IDataSource interface with the table oriented storage modification methods.
 * 
 * @see javax.rad.model.IDataPage
 * @see javax.rad.model.IDataBook
 * 
 * @author Roland Hörmann
 */
public interface IDBAccess
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
	/**
	 * Returns the List of fetched rows (as List of Object[]) for the specified query tables and 
	 * parameters. It fetch's the the rows from pFromRow row index a minimum amount of 
	 * pMinimumRowCount rows.  Implementations should fetch as much rows as possible in a 
	 * proper amount of time, to get less requests from the client model to the server 
	 * IDBAccess. Implementation can cache the select cursor and reuse it for the next fetch
	 * operation, but they should take care, that's a state less call and in a fall over case
	 * it maybe loose on the fail over system the cursor. 
	 * 
	 * @param pBeforeQueryColumns	the before query columns
	 * @param pQueryColumns			the query columns	
	 * @param pFromClause			the from clause with query tables and join definitions
	 * @param pFilter	            the filter to use
	 * @param pWhereClause			the last where condition in query
	 * @param pAfterWhereClause		the after where clause in query
	 * @param pSort		            the sort order to use
	 * @param pFromRow				the row index from to fetch
	 * @param pMinimumRowCount		the minimum count row to fetch
	 * @param pServerMetaData		the MetaDataColumn array to use.
	 * @param pAllowLazyFetch		if lazy fetch should be allowed.
	 * @return the List of fetched rows (as List of Object[]) for the specified query tables and 
	 * 		   parameters.
	 * @throws DataSourceException	if the fetch fails.
	 */
	public List<Object[]> fetch(ServerMetaData pServerMetaData, String pBeforeQueryColumns, String[] pQueryColumns, String pFromClause,
								ICondition pFilter, String pWhereClause, String pAfterWhereClause, 
								SortDefinition pSort, int pFromRow, int pMinimumRowCount,
								boolean pAllowLazyFetch) throws DataSourceException;
			
	/**
	 * It locks the specified row in the storage.<br>
	 * 
	 * @param pWritebackTable the storage unit to use
	 * @param pPKFilter 		the PrimaryKey in as an <code>ICondition</code> to identify the row to lock   
	 * @param pServerMetaData	the MetaDataColumn array to use.
	 * @throws DataSourceException if an <code>Exception</code> occur during interacting with the storage 
	 */	
	public void lockRow(String pWritebackTable, ServerMetaData pServerMetaData, ICondition pPKFilter) throws DataSourceException;
	
	/**
	 * Returns the newly inserted row from the write back table.
	 * 
	 * @param pWritebackTable	the write back table to use.
	 * @param pServerMetaData	the meta data to use.
	 * @param pNewDataRow       the new values Object[] to insert.
	 * @return the newly inserted row as Object[] from the write back table.
	 * @throws DataSourceException if an <code>Exception</code> occur during insert the row to the table
	 */
	public Object[] insert(String pWritebackTable, ServerMetaData pServerMetaData, Object[] pNewDataRow) throws DataSourceException;

	/**
	 * Return the updated row.
	 * 
	 * @param pWritebackTable	the write back table to use.
	 * @param pServerMetaData	the meta data to use.
	 * @param pOldDataRow       the old values of the row
	 * @param pNewDataRow       the new values of the row
	 * @return the updated row.
	 * @throws DataSourceException if an <code>Exception</code> occur during update.
	 */
	public Object[] update(String pWritebackTable, ServerMetaData pServerMetaData, Object[] pOldDataRow, Object[] pNewDataRow) throws DataSourceException;

	/**
	 * Deletes the specified row.
	 *  
	 * @param pWritebackTable	the write back table to use.
	 * @param pServerMetaData	the meta data to use.
	 * @param pDeleteDataRow    the row to delete.
	 * @throws DataSourceException if an <code>Exception</code> occur during delete.
	 */
	public void delete(String pWritebackTable, ServerMetaData pServerMetaData, Object[] pDeleteDataRow) throws DataSourceException;
	
} 	// IDBAccess
