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
 * 11.03.2011 - [RH] - #308 - DB specific automatic quoting implemented            
 */
package com.sibvisions.rad.persist.jdbc;

import java.math.BigDecimal;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

import javax.rad.model.datatype.BigDecimalDataType;
import javax.rad.persist.DataSourceException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests all Functions of {@link DBAccess} with a MS SQL driver.
 * 
 * @author Roland Hörmann
 * @see com.sibvisions.rad.persist.jdbc.DBAccess
 */
public class TestMSSQLDBAccess extends TestDBAccess
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
		
		DriverManager.setLoginTimeout(8);
		
		dba = createMSSQLDBAccess();
		
		// set connect properties	
		dba.setUrl(getJdbcUrl());
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
		dba2 = createMSSQLDBAccess();
		dba2.setUrl(getJdbcUrl());
		dba2.setUsername("test");
		dba2.setPassword("test");
		
		dba2.open();
		
		/* -Xmx512m ...krank und krank langsam Mysql mit innoDB
		dba2.getConnection().setAutoCommit(false);
		
    	psPreparedStatement = dba2.getPreparedStatement("delete from ADRESSEN", false);
    	dba2.executeUpdate(psPreparedStatement);

		//Adressen befüllen
    	for (int i = 1; i <= 100000; i++)
    	{
			psPreparedStatement = dba2.getPreparedStatement(
					"insert into ADRESSEN (POST_ID, STRA_ID, HAUSNUMMER, STIEGE, TUERNUMMER) " +
	                  " VALUES (" + getRandom(0, 8000 - 1) + ", " + 
	                  				getRandom(0, 5000 - 1) + ", " + 
	                  				getRandom(0, 49) + ", " +
	                  				getRandom(1, 9) + "," + 
	                  				getRandom(1, 100) + ")", 
					false);
			dba2.executeUpdate(psPreparedStatement);
    	}
    	
    	dba2.commit();*/
	}

	/**
	 * Creates the MSSQLDBAccess.
	 * @return the MSSQLDBAccess.
	 */
	protected MSSQLDBAccess createMSSQLDBAccess()
	{
	    return new MSSQLDBAccess();
	}
	
	/**
	 * Gets the jdbc url to the Test DB.
	 * @return the jdbc url to the Test DB.
	 */
	protected String getJdbcUrl()
	{
	    return "jdbc:jtds:sqlserver://192.168.1.201;databaseName=test";
	}
	
	/** 
	 * Creates test tables for new tests.
	 * 
	 * @throws Exception
	 *             if the connect or the create table fails
	 */
    @Override
	protected void createTestTables() throws Exception
	{
		dba.executeStatement("create table test_unquoted_lowercase (id numeric(18) NOT NULL IDENTITY, name varchar(100), "
				+ "CONSTRAINT tuql_pk PRIMARY KEY (id))");

		dba.executeStatement("create table TEST_UNQUOTED_UPPERCASE (ID numeric(18) NOT NULL IDENTITY, NAME varchar(100), "
				+ "CONSTRAINT tuqu_pk PRIMARY KEY (ID))");

		dba.executeStatement("create table `Test_Quoted` (`Id` numeric(18) NOT NULL IDENTITY, `Name` varchar(100), " 
				+ "CONSTRAINT tq_pk PRIMARY KEY (`Id`))");
		
		dba.executeStatement("create table TEST_PK_FETCH (SOME_VALUE varchar(32) not null, CREATED_AT date not null default CURRENT_TIMESTAMP, "
				+ "primary key (SOME_VALUE))"); 
	    
        dba.executeStatement("create table TEST_COLUMN_SPECIALCHAR (ID integer not null, [FIRST NAME] varchar(100), [LAST#NAME] varchar(100), "
                + "constraint TESC_PK primary key (ID))");
	}

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /**
     * Don't test.
     */
    @Test
    @Override
    public void testBaseStatements()
    {
    }
    
    /**
     * {@inheritDoc}
     */
    @Test
    @Override
    public void testDefaultAllowedValues() throws Exception
    {
		try
		{
			dba.executeStatement("drop table test_defaultallowed");
		}
		catch (SQLException se)
		{
			//nothing to be done
		}
		
		dba.executeStatement("CREATE TABLE test_defaultallowed (dummyname varchar(100), zahl integer DEFAULT 15, text varchar(100) DEFAULT 'Hallo')");
		
		DBStorage dbs = new DBStorage();
		dbs.setDBAccess(dba);
		dbs.setWritebackTable("test_defaultallowed");
		dbs.open();
		
		//#1751 #1750
		Assert.assertEquals(BigDecimal.valueOf(15), dbs.getMetaData().getColumnMetaData("ZAHL").getDefaultValue());
		Assert.assertEquals("Hallo", dbs.getMetaData().getColumnMetaData("TEXT").getDefaultValue());
		
    }   
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Test
    public void testGetDefaultValue() throws Exception
    {
        Map<String, Object> htDefaults = dba.getDefaultValues(null, null, "test_defaults");
        
        Assert.assertEquals("N", getDefaultValue(htDefaults, "active"));
        Assert.assertEquals(new BigDecimal("1234"), getDefaultValue(htDefaults, "numberval"));
        Assert.assertEquals("2001-01-01", getDefaultValue(htDefaults, "dateval").toString());
        Assert.assertEquals("2010-01-01 12:00:00.0", getDefaultValue(htDefaults, "datetimeval").toString());
        Assert.assertEquals("TEXT Test", getDefaultValue(htDefaults, "text"));
    }   
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Test update of PK column.
	 * 
	 * @throws Exception if not all methods work correctly
	 */
	@Test
	public void testUpdatePK() throws Exception
	{
		ServerMetaData smd = new ServerMetaData();
		ServerColumnMetaData scmd = new ServerColumnMetaData(new Name("id", dba.quote("id")));
		scmd.getColumnMetaData().setTypeIdentifier(BigDecimalDataType.TYPE_IDENTIFIER);
		scmd.getColumnMetaData().setWritable(true);
		smd.addServerColumnMetaData(scmd);

		scmd = new ServerColumnMetaData(new Name("name", dba.quote("name")));
		scmd.setWritable(true);
		smd.addServerColumnMetaData(scmd);

		smd.setAutoIncrementColumnNames(new Name[] { new Name("id", dba.quote("id"))});
		smd.setPrimaryKeyColumnNames(new Name[] { new Name("id", dba.quote("id"))});

		// insert()
		Object[] oaNew = dba.insert("test", smd, new Object[] {Integer.valueOf(1), "insert()"});

		try
		{
			// update()
			dba.update("test", smd, oaNew, new Object[] {BigDecimal.valueOf(2), "update()"});
			
			Assert.fail("PK updated but it's not allowed!");
		}
		catch (DataSourceException dse)
		{
			Assert.assertEquals("Update AnsiSQL failed! - UPDATE test SET id = ? , name = ?  WHERE id = ?", dse.getMessage());
		}
	}	

	/**
	 * Tests {@link MSSQLDBAccess#getAllowedValues(String)}.
	 * 
	 * @throws Exception if the test fails
	 */
	@Test
	public void testAllowedValues() throws Exception
	{
		Map<String, Object[]> htValues = dba.getAllowedValues(null, null, "test_defaults");

		Assert.assertArrayEquals(new Object[] {"Y", "N"}, htValues.get("active"));
	}

	/**
	 * Tests default schema detection.
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testSchemaDetection() throws Exception
	{
		//Schema detection is different for MSSQL 2000 and MSSQL 2005+
		DBStorage dbs = new DBStorage();
		dbs.setDBAccess(dba);
		dbs.setFromClause("ADRESSEN");
		dbs.open();
	}
	
	/**
	 * Tests if the synonym support works.
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testSynonyms() throws Exception
	{
		DBStorage dbsTest = new DBStorage();
		dbsTest.setDBAccess(dba);
		dbsTest.setWritebackTable("SYN_TEST");
		dbsTest.open();
		
		// SYN_TEST points to the DETAIL Table, it should find a Primary Key to make sure that the synonym is replaced with the DETAIL table.
		Assert.assertTrue(dbsTest.getMetaData().getPrimaryKeyColumnNames().length > 0);
	}
	
} 	// TestMSSQLDBAccess
