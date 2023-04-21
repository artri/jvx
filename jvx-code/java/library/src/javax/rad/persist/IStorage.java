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
 * 19.11.2008 - [RH] - Parameter Array at select() and getRowCount() removed
 * 28.04.2009 - [RH] - interface reviewed, state less fetch method
 * 23.11.2009 - [RH] - ColumnMetaData with MetaData replaced
 * 25.03.2010 - [JR] - #103: getMetaData(String, String), getMetaDataFromCache(String) added
 * 27.03.2010 - [JR] - moved caching features to ICachedStorage
 * 29.09.2010 - [RH] - countRows renamed to getEstimatedRowCount()
 */
package javax.rad.persist;

import java.util.List;

import javax.rad.model.SortDefinition;
import javax.rad.model.condition.ICondition;

/**
 * The <code>IStorage</code> defines the methods to access any kind of table 
 * oriented storage: e.g. a database, XML file, ...<br>
 * <br>
 * 
 * @author Roland Hörmann
 */
public interface IStorage
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Returns the meta data for this AbstractStorage from the storage as <code>MetaData</code>.
	 * 
	 * @return the meta data for this AbstractStorage from the storage as <code>MetaData</code>.
	 * @throws DataSourceException if an <code>Exception</code> occur during getting the meta data from the storage.
	 */
	public MetaData getMetaData() throws DataSourceException;
	
	/**
	 * Returns the number of rows in this AbstractStorage from the storage.<br>
	 * It consider the specified <code>ICondition</code> to count the rows.
	 * 
	 * @param pFilter the <code>ICondition</code> to use.
	 * @return the number of rows in this AbstractStorage from the storage.
	 * @throws DataSourceException if an <code>Exception</code> occur during interacting with the storage.            
	 */		
	public int getEstimatedRowCount(ICondition pFilter) throws DataSourceException;
	
	/**
	 * Returns the requested rows as <code>List[Object[]]</code>. Optimization can also return
	 * more then the minimum row count. If possible all in a certain time.
	 * 
	 * @param pFilter the <code>ICondition</code> to use.
	 * @param pSort	the <code>SortDefinition</code> to use.
	 * @param pFromRow the from row index to request from storage.
	 * @param pMinimumRowCount the minimum row count to request, beginning from the pFromRow.
	 * @return the requested rows as <code>List[Object[]]</code>.
	 * @throws DataSourceException if an <code>Exception</code> occur during interacting with the storage.
	 */
	public List<Object[]> fetch(ICondition pFilter, SortDefinition pSort, int pFromRow, int pMinimumRowCount) throws DataSourceException;
	
	/**
	 * It refetchs the specified row and returns it as <code>Object[]</code> from the storage.<br>
	 * Before the user starts editing in the GUI, the IDataRow in the storage should be locked for updates.
	 * 
	 * @param pDataRow the specified row as <code>Object[]</code>.
	 * @return the refetched row as <code>Object[]</code>.
	 * @throws DataSourceException if an <code>Exception</code> occur during interacting with the storage.
	 */
	public Object[] refetchRow(Object[] pDataRow) throws DataSourceException;
	
	/**
	 * Returns the newly inserted row for this IStorage.<br>
	 * 
	 * @param pDataRow the new row as <code>Object[]</code> to insert.
	 * @return the newly inserted row from this IStorage.
	 * @throws DataSourceException if an <code>Exception</code> occur during insert the row to the storage
	 */
	public Object[] insert(Object[] pDataRow) throws DataSourceException;
	
	/**
	 * Return the updated row as <code>Object[]</code>.<br>
	 * 
	 * @param pOldDataRow the old row as <code>Object[]</code>
	 * @param pNewDataRow the new row as <code>Object[]</code> to update
	 * @return the updated row as <code>Object[]</code>.
	 * @throws DataSourceException if an <code>Exception</code> occur during updating the row.
	 */
	public Object[] update(Object[] pOldDataRow, Object[] pNewDataRow) throws DataSourceException;
	
	/**
	 * Deletes the specified row from the storage.
	 * 
	 * @param pDeleteDataRow the row as <code>Object[]</code> to delete.
	 * @throws DataSourceException if an <code>Exception</code> occur during deleting the row or
	 *             				   if the storage isn't opened or the PrimaryKey is wrong and more/less 
	 *                             then one row is deleted.
	 */
	public void delete(Object[] pDeleteDataRow) throws DataSourceException;
	
} 	// IStorage
