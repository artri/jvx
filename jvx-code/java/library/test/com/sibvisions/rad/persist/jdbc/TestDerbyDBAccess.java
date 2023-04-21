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
 * 16.05.2009 - [RH] - creation
 * 23.11.2009 - [RH] - ColumnMetaData && PrimaryKey Column is replaced with MetaData class
 */
package com.sibvisions.rad.persist.jdbc;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests all Functions of {@link DerbyDBAccess}.
 * 
 * @author Roland Hörmann
 * @see com.sibvisions.rad.persist.jdbc.DerbyDBAccess
 */
public class TestDerbyDBAccess extends TestDBAccess
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** 
	 * {@inheritDoc}
	 */
	@Before
	@Override
	public void open() throws Exception
	{
		checkConnectionError();
		
		dba = new DerbyDBAccess();
		
		// set connect properties		
		dba.setUrl("jdbc:derby://192.168.1.220:1527/testdb");
		dba.setUsername("test");
		dba.setPassword("test");

		try
		{
			// open and check
			dba.open();
			
			setConnectionError(false);
		}
		catch (Exception e)
		{
			setConnectionError(true);
			
			throw e;
		}
		Assert.assertTrue(dba.isOpen());
		
		dba.executeStatement("delete from detail");
		dba.executeStatement("delete from test");
		dba.executeStatement("delete from test_sort");

		// Test db 2
		dba2 = new DerbyDBAccess();
		dba2.setUrl("jdbc:derby://192.168.1.220:1527/test"); 
		dba2.setUsername("test");
		dba2.setPassword("test");
		
		dba2.open();
		
		/*
		// -Xmx512m ...krank und krank langsam Mysql mit innoDB
		dba2.getConnection().setAutoCommit(false);
		
    	PreparedStatement psPreparedStatement = dba2.getPreparedStatement("delete from PERSONEN", false);
    	psPreparedStatement.execute();

    	psPreparedStatement = dba2.getPreparedStatement("insert into PERSONEN (VORNAME, NACHNAME, GEBDAT) VALUES (?, ?, ?)");
    	
		//Adressen befüllen
    	for (int i = 1; i <= 500; i++)
    	{
    		psPreparedStatement.setString(1, "Vorname +" + i);
    		psPreparedStatement.setString(2, "Nachname +" + i);
    		psPreparedStatement.setDate(3, new java.sql.Date(DateUtil.getDate(1, 1, 1950 + i, 0, 0, 0).getTime()));
    		psPreparedStatement.execute();
    	}
    	
    	dba2.commit();
    	*/
	}
	
	/**
	 * Don't test!
	 */
	@Test
	@Override
	public void testBaseStatements()
	{
	}
	
    /**
     * Don't test!
     * 
     * @throws Exception never thrown
     */
    @Test
    @Override
    public void testFilterLikeReverse() throws Exception
    {
    }
	
	/**
	 * {@inheritDoc}
	 */
	@Test
	@Override
	public void testMetaData() throws Exception
	{
		DBStorage dbs = new DBStorage();
		dbs.setDBAccess(dba);
		dbs.setWritebackTable("TEST");
		dbs.setFromClause("TEST");
		dbs.open();
	}
	
} 	// TestDerbyDBAccess
