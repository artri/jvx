/*
 * Copyright 2011 SIB Visions GmbH
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
 * 09.10.2011 - [JR] - creation
 */
package com.sibvisions.rad.persist.jdbc;

import java.sql.DriverManager;

import javax.rad.remote.MasterConnection;

import org.junit.Assert;
import org.junit.Test;

import com.sibvisions.rad.model.remote.RemoteDataBook;
import com.sibvisions.rad.model.remote.RemoteDataSource;
import com.sibvisions.rad.util.DirectObjectConnection;

/**
 * The <code>TestEnumSupport</code> tests the enum support for PostgreSql.
 * 
 * @author René Jahn
 */
public class TestEnumSupport
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Test methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Tests CRUD operations for a PostgreSQL table with enum.
	 * 
	 * @throws Throwable if test fails
	 */
	@Test
	public void testPostgreSqlCRUD() throws Throwable
	{
		DriverManager.setLoginTimeout(8);

		DBAccess dba = new PostgreSQLDBAccess();
		
		// set connect properties		
		dba.setUrl("jdbc:postgresql://192.168.1.232/testdb");
		dba.setUsername("test");
		dba.setPassword("test");
		//dba.setDBProperty("loglevel", "2");
		dba.open();
		
		DBStorage dbs = new DBStorage();
		dbs.setDBAccess(dba);
		dbs.setWritebackTable("person");
		dbs.open();
		
		DirectObjectConnection doc = new DirectObjectConnection();
		doc.put("dbs", dbs);
		
		MasterConnection macon = new MasterConnection(doc);
		macon.open();
		
		RemoteDataSource rds = new RemoteDataSource(macon);
		rds.open();
		
		RemoteDataBook rdb = new RemoteDataBook();
		rdb.setName("dbs");
		rdb.setDataSource(rds);
		rdb.open();
		
		rdb.fetchAll();
		
		Assert.assertArrayEquals(new Object[] {"sad", "ok", "happy"}, rdb.getRowDefinition().getColumnDefinition("CURRENT_MOOD").getAllowedValues());

		//Delete
		rdb.deleteAllRows();
		
		//Insert
		rdb.insert(false);
		rdb.setValues(new String[] {"NAME", "CURRENT_MOOD"}, new String[] {"Henry", "ok"});
		rdb.saveAllRows();
		
		rdb.reload();
		
		//Update
		rdb.setValue("CURRENT_MOOD", "sad");
		rdb.saveSelectedRow();
	}

	/**
	 * Tests CRUD operations for a MySQL table with enum and set.
	 * 
	 * @throws Throwable if test fails
	 */
	@Test
	public void testMySqlCRUD() throws Throwable
	{
		DBAccess dba = new MySQLDBAccess();
		
		// set connect properties		
		dba.setUrl("jdbc:mysql://192.168.1.201/test");
		dba.setUsername("test");
		dba.setPassword("test");
		dba.open();
		
		DBStorage dbs = new DBStorage();
		dbs.setDBAccess(dba);
		dbs.setWritebackTable("PERSON");
		dbs.open();
		
		DirectObjectConnection doc = new DirectObjectConnection();
		doc.put("dbs", dbs);
		
		MasterConnection macon = new MasterConnection(doc);
		macon.open();
		
		RemoteDataSource rds = new RemoteDataSource(macon);
		rds.open();
		
		RemoteDataBook rdb = new RemoteDataBook();
		rdb.setName("dbs");
		rdb.setDataSource(rds);
		rdb.open();
		
		rdb.fetchAll();
		
		Assert.assertArrayEquals(new Object[] {"Y", "N"}, rdb.getRowDefinition().getColumnDefinition("ACTIVE").getAllowedValues());
		Assert.assertArrayEquals(new Object[] {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"}, rdb.getRowDefinition().getColumnDefinition("WEEKDAYS").getAllowedValues());

		rdb.insert(false);
		rdb.setValues(new String[] {"ACTIVE", "WEEKDAYS"}, new String[] {"y", "Mon"});

		rdb.insert(false);
		rdb.setValues(new String[] {"ACTIVE", "WEEKDAYS"}, new String[] {"y", "Mon,Fri"});
		
		rdb.saveAllRows();
		
		rdb.deleteAllRows();
		rdb.saveAllRows();
	}
	
}	// TestEnumSupport
