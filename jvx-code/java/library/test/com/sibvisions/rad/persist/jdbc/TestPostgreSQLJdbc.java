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
 * 01.04.2010 - [MH] - creation
 */
package com.sibvisions.rad.persist.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests acceptsUrl in Postgresql Jdbc Bug.
 * 
 * @author Martin Handsteiner
 */
public class TestPostgreSQLJdbc
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Test connection to hsql db registered also a postgresql driver.
	 * 
	 * @throws Exception if connection is wrong or could not be established
	 */	
	@Test
	public void testConnectionWithRegisteredPostgresqlDriver() throws Exception
	{
		Class.forName("org.hsqldb.jdbcDriver");
		
		Properties properties = new Properties();
		properties.setProperty("user", "sa");
		properties.setProperty("password", "");
		properties.setProperty("shutdown", Boolean.TRUE.toString());

		Connection hsqlConnection = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/testdb", properties);

		Assert.assertNotNull("No Connection created", hsqlConnection);
		Assert.assertEquals(Class.forName("org.hsqldb.jdbc.JDBCConnection"), hsqlConnection.getClass());
	}
		
} 	// TestPostgreSQLJdbc
