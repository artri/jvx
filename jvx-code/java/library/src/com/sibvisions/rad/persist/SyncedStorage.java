/*
 * Copyright 2022 SIB Visions GmbH
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
 * 07.07.2022 - [JR] - creation
 */
package com.sibvisions.rad.persist;

import java.util.Hashtable;
import java.util.List;

import javax.rad.model.SortDefinition;
import javax.rad.model.condition.ICondition;
import javax.rad.persist.DataSourceException;
import javax.rad.persist.ICachedStorage;
import javax.rad.persist.MetaData;

import com.sibvisions.rad.server.annotation.Accessible;

/**
 * The <code>SyncedStorage</code> synchronizes the access of an {@link ICachedStorage}.
 * 
 * @author René Jahn
 */
public class SyncedStorage implements ICachedStorage
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the storage. */
	private ICachedStorage storage;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of <code>SyncedStorage</code>.
	 * 
	 * @param pStorage the storage which should be synchronized
	 */
	public SyncedStorage(ICachedStorage pStorage)
	{
		storage = pStorage;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Accessible
	public synchronized MetaData getMetaData() throws DataSourceException 
	{
		return storage.getMetaData();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Accessible
	public synchronized MetaData getMetaData(String pGroup, String pName) throws DataSourceException 
	{
		return storage.getMetaData(pGroup, pName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Accessible
	public synchronized Hashtable<String, MetaData> getMetaDataFromCache(String pGroup) 
	{
		return storage.getMetaDataFromCache(pGroup);
	}	

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Accessible
	public synchronized int getEstimatedRowCount(ICondition pFilter) throws DataSourceException 
	{
		return storage.getEstimatedRowCount(pFilter);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Accessible
	public synchronized List<Object[]> fetch(ICondition pFilter, SortDefinition pSort, int pFromRow, int pMinimumRowCount) throws DataSourceException 
	{
		return storage.fetch(pFilter, pSort, pFromRow, pMinimumRowCount);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized Object[] refetchRow(Object[] pDataRow) throws DataSourceException 
	{
		return storage.refetchRow(pDataRow);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized Object[] insert(Object[] pDataRow) throws DataSourceException 
	{
		return storage.insert(pDataRow);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Accessible
	public synchronized Object[] update(Object[] pOldDataRow, Object[] pNewDataRow) throws DataSourceException 
	{
		return storage.update(pOldDataRow, pNewDataRow);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Accessible
	public synchronized void delete(Object[] pDeleteDataRow) throws DataSourceException 
	{
		storage.delete(pDeleteDataRow);
	}

}	// SyncedStorage
