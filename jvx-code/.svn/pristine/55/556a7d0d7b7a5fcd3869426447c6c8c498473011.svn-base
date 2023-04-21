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
 * 05.10.2010 - [JR] - creation
 * 16.02.2011 - [JR] - #287: removed fetch method with cache information 
 */
package com.sibvisions.rad.persist.jdbc;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.rad.model.SortDefinition;
import javax.rad.model.condition.ICondition;
import javax.rad.persist.DataSourceException;
import javax.rad.persist.ICachedStorage;
import javax.rad.persist.IStorage;
import javax.rad.persist.MetaData;
import javax.rad.remote.MasterConnection;

import org.junit.Test;

import com.sibvisions.rad.model.remote.RemoteDataBook;
import com.sibvisions.rad.model.remote.RemoteDataSource;
import com.sibvisions.rad.util.DirectObjectConnection;

/**
 * Tests the caching mechanism of DBAccess.
 *  
 * @author René Jahn
 */
public class TestICachedStorage
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Tests DBAccess with {@link javax.rad.persist.ICachedStorage} methods.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testCachedStorage() throws Throwable
	{
		DBAccess dba = DBAccess.getDBAccess("jdbc:hsqldb:hsql://localhost/demodb");
		dba.setUsername("sa");
		dba.setPassword("");
		dba.open();
		
		try
		{
			CachingStorage cs = new CachingStorage(dba, "USERS");
	
			HashMap<String, Object> hmpObjects = new HashMap<String, Object>();
			hmpObjects.put("users", cs);
			
			MasterConnection macon = new MasterConnection(new DirectObjectConnection(hmpObjects));
			macon.open();
			
			RemoteDataSource rds = new RemoteDataSource(macon);
			rds.open();
			
			RemoteDataBook book = new RemoteDataBook();
			book.setDataSource(rds);
			book.setName("users");
			book.open();
		}
		finally
		{
			try
			{
				dba.close();	
			}
			catch (Exception e)
			{
				// nothing to be done
			}
		}
	}
	
	/**
	 * Tests DBAccess without {@link javax.rad.persist.ICachedStorage}.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testStorage() throws Throwable
	{
		DBAccess dba = DBAccess.getDBAccess("jdbc:hsqldb:hsql://localhost/demodb");
		dba.setUsername("sa");
		dba.setPassword("");
		dba.open();
		
		try
		{
			NoCachingStorage cs = new NoCachingStorage(dba, "USERS");
			
			HashMap<String, Object> hmpObjects = new HashMap<String, Object>();
			hmpObjects.put("users", cs);
	
			MasterConnection macon = new MasterConnection(new DirectObjectConnection(hmpObjects));
			macon.open();
			
			RemoteDataSource rds = new RemoteDataSource(macon);
			rds.open();
			
			RemoteDataBook book = new RemoteDataBook();
			book.setDataSource(rds);
			book.setName("users");
			book.open();
		}
		finally
		{
			try
			{
				dba.close();	
			}
			catch (Exception e)
			{
				// nothing to be done
			}
		}
	}
	
    //****************************************************************
    // Subclass definition
    //****************************************************************
	
	/**
	 * A simple {@link ICachedStorage} implementation. It forwards interface method calls to
	 * a {@link DBStorage}.
	 * 
	 * @author René Jahn
	 */
	protected class CachingStorage implements ICachedStorage
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/** the internal storage. */
		private DBStorage storage;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Creates a new instance of <code>CachingStorage</code> with a database connection
		 * and a specific from clause.
		 * 
		 * @param pAccess the db access
		 * @param pFrom the from clause
		 * @throws DataSourceException if the access to the db fails
		 */
		public CachingStorage(DBAccess pAccess, String pFrom) throws DataSourceException
		{
			storage = new DBStorage();
			storage.setDBAccess(pAccess);
			storage.setFromClause(pFrom);
			storage.open();
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Interface implementation
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * {@inheritDoc}
		 */
		public MetaData getMetaData(String pGroup, String pName) throws DataSourceException
		{
			return storage.getMetaData(pGroup, pName);
		}

		/**
		 * {@inheritDoc}
		 */
		public Hashtable<String, MetaData> getMetaDataFromCache(String pGroup)
		{
			return storage.getMetaDataFromCache(pGroup);
		}

		/**
		 * {@inheritDoc}
		 */
		public int getEstimatedRowCount(ICondition pFilter) throws DataSourceException
		{
			return storage.getEstimatedRowCount(pFilter);
		}

		/**
		 * {@inheritDoc}
		 */
		public void delete(Object[] pDeleteDataRow) throws DataSourceException
		{
			storage.delete(pDeleteDataRow);
		}

		/**
		 * {@inheritDoc}
		 */
		public List<Object[]> fetch(ICondition pFilter, SortDefinition pSort, int pFromRow, int pMinimumRowCount) throws DataSourceException
		{
			return storage.fetch(pFilter, pSort, pFromRow, pMinimumRowCount);
		}

		/**
		 * {@inheritDoc}
		 */
		public MetaData getMetaData() throws DataSourceException
		{
			return storage.getMetaData();
		}

		/**
		 * {@inheritDoc}
		 */
		public Object[] insert(Object[] pDataRow) throws DataSourceException
		{
			return storage.insert(pDataRow);
		}

		/**
		 * {@inheritDoc}
		 */
		public Object[] refetchRow(Object[] pDataRow) throws DataSourceException
		{
			return storage.refetchRow(pDataRow);
		}

		/**
		 * {@inheritDoc}
		 */
		public Object[] update(Object[] pOldDataRow, Object[] pNewDataRow) throws DataSourceException
		{
			return storage.update(pOldDataRow, pNewDataRow);
		}
		
	}	// CachingStorage
	
	/**
	 * A simple {@link IStorage} implementation. It forwards interface method calls to
	 * a {@link DBStorage}.
	 * 
	 * @author René Jahn
	 */
	protected class NoCachingStorage implements IStorage
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/** the internal storage. */
		private DBStorage storage;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Creates a new instance of <code>NoCachingStorage</code> with a database connection
		 * and a specific from clause.
		 * 
		 * @param pAccess the db access
		 * @param pFrom the from clause
		 * @throws DataSourceException if the access to the db fails
		 */
		public NoCachingStorage(DBAccess pAccess, String pFrom) throws DataSourceException
		{
			storage = new DBStorage();
			storage.setDBAccess(pAccess);
			storage.setFromClause(pFrom);
			storage.open();
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Interface implementation
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * {@inheritDoc}
		 */
		public int getEstimatedRowCount(ICondition pFilter) throws DataSourceException
		{
			return storage.getEstimatedRowCount(pFilter);
		}

		/**
		 * {@inheritDoc}
		 */
		public void delete(Object[] pDeleteDataRow) throws DataSourceException
		{
			storage.delete(pDeleteDataRow);
		}

		/**
		 * {@inheritDoc}
		 */
		public List<Object[]> fetch(ICondition pFilter, SortDefinition pSort, int pFromRow, int pMinimumRowCount) throws DataSourceException
		{
			return storage.fetch(pFilter, pSort, pFromRow, pMinimumRowCount);
		}

		/**
		 * {@inheritDoc}
		 */
		public MetaData getMetaData() throws DataSourceException
		{
			return storage.getMetaData();
		}

		/**
		 * {@inheritDoc}
		 */
		public Object[] insert(Object[] pDataRow) throws DataSourceException
		{
			return storage.insert(pDataRow);
		}

		/**
		 * {@inheritDoc}
		 */
		public Object[] refetchRow(Object[] pDataRow) throws DataSourceException
		{
			return storage.refetchRow(pDataRow);
		}

		/**
		 * {@inheritDoc}
		 */
		public Object[] update(Object[] pOldDataRow, Object[] pNewDataRow) throws DataSourceException
		{
			return storage.update(pOldDataRow, pNewDataRow);
		}
		
	}	// NoCachingStorage
	
}	// TestICachedStorage
