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
 * 12.05.2009 - [RH] - creation
 */
package demo;

import javax.rad.persist.DataSourceException;

import com.sibvisions.rad.persist.jdbc.DBStorage;

/**
 * The <code>StorageDataBookTest</code> class contains objects for the 
 * {@link com.sibvisions.rad.model.remote.TestRemoteDataBook} unit test.
 * 
 * @author Roland Hörmann
 */
public class StorageDataBookTest extends Session
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Returns the Test defaults storage.
	 * 
	 * @return the Test defaults storage.
	 * @throws DataSourceException if the storage couldn't be opened.
	 */
	public DBStorage getTESTDEFAULTS() throws DataSourceException
	{
		DBStorage dbTest = (DBStorage)get("TESTDEFAULTS");
		
		if (dbTest == null) 
		{
			dbTest = new DBStorage();
			dbTest.setDBAccess(getTestDataSource());
			dbTest.setWritebackTable("TEST_DEFAULTS");
			dbTest.setFromClause("TEST_DEFAULTS");
			dbTest.open();
			
			put("TESTDEFAULTS", dbTest);
		}
		return dbTest;
	}
	
	/**
	 * Returns the Test storage.
	 * 
	 * @return the Test storage.
	 * @throws DataSourceException if the storage couldn't be opened.
	 */
	public DBStorage getTEST() throws DataSourceException
	{
		DBStorage dbTest = (DBStorage)get("TEST");
		
		if (dbTest == null) 
		{
			dbTest = new DBStorage();
			dbTest.setDBAccess(getTestDataSource());
			dbTest.setWritebackTable("TEST");
			dbTest.setFromClause("TEST");
			dbTest.open();
			
			put("TEST", dbTest);
		}
		return dbTest;
	}
	
	/**
	 * Returns the Detail storage.
	 * 
	 * @return the Detail storage.
	 * @throws DataSourceException if the storage couldn't be opened.
	 */
	public DBStorage getDETAIL() throws DataSourceException
	{
		DBStorage dbDetail = (DBStorage)get("DETAIL");
		
		if (dbDetail == null)
		{
			dbDetail = new DBStorage();
			dbDetail.setDBAccess(getTestDataSource());
			dbDetail.setWritebackTable("DETAIL");
			dbDetail.setFromClause("DETAIL");
			dbDetail.open();
			
			put("DETAIL", dbDetail);
		}
		return dbDetail;
	}
	
	/**
	 * Returns the Adressen storage.
	 * 
	 * @return the Adressen storage.
	 * @throws DataSourceException if the storage couldn't be opened.
	 */
	public DBStorage getADRESSEN() throws DataSourceException
	{
		DBStorage dbAdressen = (DBStorage)get("ADRESSEN");
		
		if (dbAdressen == null)
		{
			dbAdressen = new DBStorage();
			dbAdressen.setDBAccess(getDataSource());
			dbAdressen.setWritebackTable("ADRESSEN");
			dbAdressen.setFromClause("ADRESSEN");
			dbAdressen.open();
			
			put("ADRESSEN", dbAdressen);
		}
		return dbAdressen;
	}	
	
}	// StorageDataBookTest
