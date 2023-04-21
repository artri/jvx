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
 * 07.06.2016 - [RZ] - creation
 */
package com.sibvisions.rad.persist.jdbc;

import org.junit.Assert;
import org.junit.Before;

/**
 * Tests the {@link TestH2DBAccess} class.
 * 
 * @author Robert Zenz
 */
public class TestH2DBAccess extends TestDBAccess
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
		dba = new H2DBAccess();
		dba.setUrl("jdbc:h2:mem:test");
		
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
				"create table test (id integer primary key auto_increment, name varchar(100))");
		
		// create detail table
		dba.executeStatement(
				"create table detail (id integer primary key auto_increment, name varchar(100), " +
						"test_id INTEGER, FOREIGN KEY (test_id) REFERENCES test (id))");
		
		// create test sort table
		dba.executeStatement(
				"create table test_sort ("
						+ "id integer primary key auto_increment,"
						+ "name varchar(100),"
						+ "sort integer)");
		
		dba.executeStatement(
				"CREATE TABLE ADRESSEN ("
						+ "ID integer primary key auto_increment,"
						+ "POST_ID int(18) NOT NULL,"
						+ "STRA_ID int(18) NOT NULL,"
						+ "HAUSNUMMER int(18) NOT NULL,"
						+ "TUERNUMMER int(18),"
						+ "STIEGE int(18))");

		dba2 = new H2DBAccess();
		dba2.setUrl("jdbc:h2:mem:test");
		
		dba2.open();
		Assert.assertTrue(dba2.isOpen());
	}
	
}	// TestH2DBAccess
