/*
 * Copyright 2016 SIB Visions GmbH
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
 * 06.06.2016 - [RZ] - creation
 */
package com.sibvisions.rad.persist.jdbc;

import java.io.File;
import java.math.BigDecimal;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the {@link SQLiteDBAccess} class.
 * 
 * @author Robert Zenz
 */
public class TestSQLiteDBAccess extends TestDBAccess
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Before
	@Override
	public void open() throws Exception
	{
		checkConnectionError();
		
		File tempDatabaseFile = File.createTempFile("jvx", "sqlite");
		tempDatabaseFile.deleteOnExit();
		
		dba = new SQLiteDBAccess();
		dba.setUrl("jdbc:sqlite:" + tempDatabaseFile.toString());
		
		dba.open();
		
		// drop test table
		dba.executeStatement("drop table if exists detail");
		dba.executeStatement("drop table if exists test");
		dba.executeStatement("drop table if exists test_defaults");
		dba.executeStatement("drop table if exists test_sort");
		
		// create test_defaults table
		dba.executeStatement(
				"CREATE TABLE test_defaults (active char(1) DEFAULT 'N' NOT NULL, " +
						"datetimeval timestamp DEFAULT '2010-01-01 12:00:00', " +
						"numberval decimal(11) DEFAULT '1234', " +
						"text varchar(50) DEFAULT 'TEXT Test', " +
						"dateval date DEFAULT '2001-01-01')");
		
		// create test table
		dba.executeStatement(
				"create table test (id integer primary key autoincrement, name varchar(100))");
		
		// create detail table
		dba.executeStatement(
				"create table detail (id integer primary key autoincrement, name varchar(100), " +
						"test_id INTEGER, FOREIGN KEY (test_id) REFERENCES test (id))");
		
		// create test sort table
		dba.executeStatement(
				"create table test_sort ("
						+ "id integer primary key autoincrement,"
						+ "name varchar(100),"
						+ "sort integer)");
		
		dba.executeStatement(
				"CREATE TABLE ADRESSEN ("
						+ "ID integer primary key autoincrement,"
						+ "POST_ID int(18) NOT NULL,"
						+ "STRA_ID int(18) NOT NULL,"
						+ "HAUSNUMMER int(18) NOT NULL,"
						+ "TUERNUMMER int(18),"
						+ "STIEGE int(18))");
		
		dba2 = new SQLiteDBAccess();
		dba2.setUrl("jdbc:sqlite:" + tempDatabaseFile.toString());
		
		dba2.open();
		Assert.assertTrue(dba2.isOpen());
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Test
	@Override
	public void testGetDefaultValue() throws Exception
	{
		// Default values in SQLite are returned "as defined", and not converted
		// to the target datatype.
		
		Map<String, Object> htDefaults = dba.getDefaultValues(null, null, "test_defaults");
		
		Assert.assertEquals("N", getDefaultValue(htDefaults, "active"));
		Assert.assertEquals(new BigDecimal("1234"), getDefaultValue(htDefaults, "numberval"));
		Assert.assertEquals("2001-01-01", getDefaultValue(htDefaults, "dateval").toString());
		Assert.assertEquals("2010-01-01 12:00:00", getDefaultValue(htDefaults, "datetimeval").toString());
		Assert.assertEquals("TEXT Test", getDefaultValue(htDefaults, "text"));
	}
	
	/**
	 * Creates test tables for new tests.
	 * 
	 * @throws Exception if the connect or the create table fails
	 */
	@Override
	protected void createTestTables() throws Exception
	{
		dba.executeStatement("create table test_unquoted_lowercase (id integer primary key autoincrement, name varchar(100))");
		
		dba.executeStatement("create table TEST_UNQUOTED_UPPERCASE (ID integer primary key autoincrement, NAME varchar(100))");
		
		dba.executeStatement("create table `Test_Quoted` (`Id` integer primary key autoincrement, `Name` varchar(100))");
		
		dba.executeStatement("create table TEST_PK_FETCH (SOME_VALUE varchar(32) not null, CREATED_AT timestamp not null default CURRENT_TIMESTAMP, primary key (SOME_VALUE))"); 
	    
        dba.executeStatement("create table TEST_COLUMN_SPECIALCHAR (ID integer not null, \"FIRST NAME\" varchar(100), \"LAST#NAME\" varchar(100), "
                + "constraint TESC_PK primary key (ID))");
	}
	
}	// TestSQLiteDBAccess
