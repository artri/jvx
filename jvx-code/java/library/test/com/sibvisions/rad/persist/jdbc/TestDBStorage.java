/*
 * Copyright 2012 SIB Visions GmbH
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
 * 20.03.2012 - [JR] - creation
 */
package com.sibvisions.rad.persist.jdbc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

import javax.rad.io.IFileHandle;
import javax.rad.io.RemoteFileHandle;
import javax.rad.model.SortDefinition;
import javax.rad.model.condition.And;
import javax.rad.model.condition.Equals;
import javax.rad.model.condition.Not;
import javax.rad.persist.MetaData;
import javax.rad.persist.MetaData.Feature;
import javax.rad.remote.MasterConnection;
import javax.rad.remote.SubConnection;
import javax.rad.type.bean.Bean;
import javax.rad.type.bean.IBean;

import org.junit.Assert;
import org.junit.Test;

import com.sibvisions.rad.model.remote.RemoteDataBook;
import com.sibvisions.rad.model.remote.RemoteDataSource;
import com.sibvisions.rad.persist.bean.BeanConverter;
import com.sibvisions.rad.persist.event.IStorageListener;
import com.sibvisions.rad.persist.event.StorageEvent;
import com.sibvisions.rad.util.DirectObjectConnection;
import com.sibvisions.util.type.CommonUtil;
import com.sibvisions.util.type.FileUtil;
import com.sibvisions.util.type.LocaleUtil;

/**
 * Tests the functionality of {@link DBStorage}.
 * 
 * @author René Jahn
 */
public class TestDBStorage
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Tests locale specific CSV export. Details, see see
	 * http://support.sibvisions.com/index.php?do=details&task_id=564
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testCSVSeparatorBug564() throws Exception
	{
		DBAccess dba = DBAccess.getDBAccess("jdbc:hsqldb:hsql://localhost/testdb");
		
		// set connect properties		
		dba.setUsername("sa");
		dba.setPassword("");
		
		dba.open();
		
		try
		{
			dba.executeStatement("delete from DETAIL");
			dba.executeStatement("delete from TEST");
			dba.commit();
			
			DBStorage dbsTest = new DBStorage();
			dbsTest.setDBAccess(dba);
			dbsTest.setWritebackTable("TEST");
			dbsTest.open();
			
			dbsTest.insert(new Object[] { BigDecimal.valueOf(0), "First" });
			dbsTest.insert(new Object[] { BigDecimal.valueOf(1), "Second" });
			
			Locale locDefault = LocaleUtil.getDefault();
			
			try
			{
				LocaleUtil.setDefault(Locale.GERMAN);
				
				IFileHandle fh = dbsTest.createCSV(null, null, null, null);
				
				String sCSV = new String(FileUtil.getContent(fh.getInputStream()));
				
				Assert.assertEquals("\"Id\";\"Name\"\n0;\"First\"\n1;\"Second\"\n", sCSV);
				
				LocaleUtil.setDefault(Locale.ENGLISH);
				
				fh = dbsTest.createCSV(null, null, null, null);
				
				sCSV = new String(FileUtil.getContent(fh.getInputStream()));
				
				Assert.assertEquals("\"Id\",\"Name\"\n0,\"First\"\n1,\"Second\"\n", sCSV);
			}
			finally
			{
				LocaleUtil.setDefault(locDefault);
			}
		}
		finally
		{
			dba.close();
		}
	}
	
	/**
	 * Tests update with bean.
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testBeanUpdate() throws Exception
	{
		IBean referenceFirst = new Bean();
		referenceFirst.put("ID", BigDecimal.valueOf(0));
		referenceFirst.put("NAME", "First");
		
		IBean referenceSecond = new Bean();
		referenceSecond.put("ID", BigDecimal.valueOf(1));
		referenceSecond.put("NAME", "Second");
		
		DBAccess dba = DBAccess.getDBAccess("jdbc:hsqldb:hsql://localhost/testdb");
		
		// set connect properties		
		dba.setUsername("sa");
		dba.setPassword("");
		
		dba.open();
		
		try
		{
			dba.executeStatement("delete from DETAIL");
			dba.executeStatement("delete from TEST");
			dba.commit();
			
			DBStorage dbsTest = new DBStorage();
			dbsTest.setDBAccess(dba);
			dbsTest.setWritebackTable("TEST");
			dbsTest.open();
			
			dbsTest.insert(referenceFirst);
			dbsTest.insert(referenceSecond);
			
			IBean bean = new Bean();
			bean.put("ID", BigDecimal.valueOf(0));
			
			Object result = dbsTest.update(bean);
			
			Assert.assertEquals(referenceFirst, result);
			
			bean.put("NAME", null);
			
			result = dbsTest.update(bean);
			
			Assert.assertNotEquals(referenceFirst, result);
			
			bean = dbsTest.fetchBean(new Equals("ID", "1"));
			
			Assert.assertEquals(referenceSecond, bean);
			
			bean.put("NAME", null);
			
			result = dbsTest.update(bean);
			
			Assert.assertNotEquals(referenceFirst, result);
		}
		finally
		{
			dba.close();
		}
	}
	
	/**
	 * Tests the lazy fetch of BLOB columns introduced with ticket 987.
	 * 
	 * @throws Throwable if the test fails.
	 */
	@Test
	public void testLazyFetchOfBlobColumnsTicket987() throws Throwable
	{
		DBAccess dba = DBAccess.getDBAccess("jdbc:hsqldb:hsql://localhost/testdb");
		dba.setUsername("sa");
		dba.setPassword("");
		dba.setLargeObjectLimit(28L);
		dba.open();
		
		try
		{
			dba.executeStatement("drop table if exists STORAGE_LAZY");
			dba.executeStatement("create table STORAGE_LAZY ("
					+ "ID integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,"
					+ "NAME varchar(100),"
					+ "NOTE varchar(100),"
					+ "LAZYA clob,"
					+ "LAZYB blob)");
			dba.commit();
			
			DBStorage storage = new DBStorage();
			storage.setDBAccess(dba);
			storage.setWritebackTable("STORAGE_LAZY");
			storage.setLazyFetchEnabled(true);
			storage.open();
			
			try
			{
				byte[] smallBytes = initArray(new byte[10], (byte)1);
				byte[] largeBytes = initArray(new byte[30], (byte)2);
				byte[] bigBytes = initArray(new byte[60], (byte)3);
				
				storage.insert(new Object[] { BigDecimal.valueOf(1), "TestRow #1", "small", null, smallBytes });
				storage.insert(new Object[] { BigDecimal.valueOf(2), "TestRow #2", "large", "Some test string.", largeBytes });
				storage.insert(new Object[] { BigDecimal.valueOf(3), "TestRow #3", "big", " ", bigBytes });
				
				// -------------------------------------------------------------
				// Check RemoteFileHandle on server.
                // -------------------------------------------------------------
				List<IBean> serverFetch = storage.fetchBean(null, null, 0, -1);
				
                Object firstServerValue = serverFetch.get(0).get("LAZYB");
                Object secondServerValue = serverFetch.get(1).get("LAZYB");
                Object thirdServerValue = serverFetch.get(2).get("LAZYB");
                
                Assert.assertTrue(firstServerValue instanceof byte[]);
                Assert.assertTrue(secondServerValue instanceof RemoteFileHandle);
                Assert.assertTrue(thirdServerValue instanceof RemoteFileHandle);
				
                byte[] firstServerBytes = (byte[])firstServerValue;
                byte[] secondServerBytes = FileUtil.getContent(((RemoteFileHandle)secondServerValue).getInputStream());
                byte[] thirdServerBytes = FileUtil.getContent(((RemoteFileHandle)thirdServerValue).getInputStream());
                
                Assert.assertArrayEquals(smallBytes, firstServerBytes);
                Assert.assertArrayEquals(largeBytes, secondServerBytes);
                Assert.assertArrayEquals(bigBytes, thirdServerBytes);

                // -------------------------------------------------------------
                // Check RemoteFileHandle on client.
                // -------------------------------------------------------------

                DirectObjectConnection directConnection = new DirectObjectConnection();
				directConnection.put("TEST", storage);
				
				MasterConnection connection = new MasterConnection(directConnection);
				connection.setApplicationName("demo");
				connection.setUserName("rene");
				connection.setPassword("rene");
				connection.open();
				
				try
				{
					SubConnection sub = connection.createSubConnection("demo.StorageDataBookTest");
					sub.open();
					
					try
					{
						RemoteDataSource dataSource = new RemoteDataSource(sub);
						dataSource.open();
						
						RemoteDataBook dataBook = new RemoteDataBook();
						dataBook.setDataSource(dataSource);
						dataBook.setName("TEST");
						dataBook.open();
						
						Object firstValue = dataBook.getDataRow(0).getValue("LAZYB");
						Object secondValue = dataBook.getDataRow(1).getValue("LAZYB");
						Object thirdValue = dataBook.getDataRow(2).getValue("LAZYB");
						
						Assert.assertTrue(firstValue instanceof byte[]);
						Assert.assertTrue(secondValue instanceof RemoteFileHandle);
						Assert.assertTrue(thirdValue instanceof RemoteFileHandle);
						
						byte[] firstBytes = (byte[])firstValue;
						byte[] secondBytes = FileUtil.getContent(((RemoteFileHandle)secondValue).getInputStream());
						byte[] thirdBytes = FileUtil.getContent(((RemoteFileHandle)thirdValue).getInputStream());
						
						Assert.assertArrayEquals(smallBytes, firstBytes);
						Assert.assertArrayEquals(largeBytes, secondBytes);
						Assert.assertArrayEquals(bigBytes, thirdBytes);
						
						dataBook.close();
						
						dataSource.close();
					}
					finally
					{
						sub.close();
					}
				}
				finally
				{
					connection.close();
				}
			}
			finally
			{
				storage.close();
			}			
		}
		finally
		{
			dba.close();
		}
	}
	
	/**
	 * Tests the behavior if one tries to insert something into a storage that
	 * does not have a writeback table set.
	 * 
	 * @throws Exception if the test fails.
	 */
	@Test
	public void testNullPointerOnInsertWithNoWritebackTableTicket1460() throws Exception
	{
		DBAccess dba = DBAccess.getDBAccess("jdbc:hsqldb:hsql://localhost/testdb");
		dba.setUsername("sa");
		dba.setPassword("");
		dba.open();
		
		try
		{
			dba.executeStatement("drop table if exists NULL_TEST");
			dba.executeStatement("create table NULL_TEST ("
					+ "ID integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,"
					+ "VALUE varchar(100),"
					+ "NOTE varchar(100))");
			dba.commit();
			
			DBStorage storage = new DBStorage();
			storage.setDBAccess(dba);
			storage.setFromClause("NULL_TEST");
			storage.open();
			
			IBean bean = storage.createEmptyBean();
			bean.put("VALUE", BigDecimal.ONE);
			
			storage.insert(bean);
			
			storage.close();
		}
		finally
		{
			dba.close();
		}
	}
	
	/**
	 * Tests the behavior if one tries to insert something into a storage that
	 * does not have a writeback table set.
	 * 
	 * @throws Exception if the test fails.
	 */
	@Test
	public void testSetMetaDataTicket1467() throws Exception
	{
		DBStorage storage = new DBStorage();
		
		storage.setMetaData(new MetaData());
		// only allowed, if not open!
	}
	
	/**
	 * AutoLink test.
	 * 
	 * @throws Exception if it fails
	 */
	@Test
	public void testAutoLinkReference() throws Exception
	{
		DBAccess dba = DBAccess.getDBAccess("jdbc:hsqldb:hsql://localhost/testdb");
		
		// set connect properties		
		dba.setUsername("sa");
		dba.setPassword("");
		dba.open();
		
		try
		{
			dba.executeStatement("drop table if exists ELECTIONDETAILS");
			dba.executeStatement("drop table if exists ELECTION");
			dba.executeStatement("drop table if exists STATES");
			dba.executeStatement("drop table if exists PERIODS");
			
			dba.executeStatement("create table PERIODS ("
					+ "ID integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,"
					+ "PERIOD varchar(100))");
			dba.executeStatement("create table STATES ("
					+ "ID integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,"
					+ "STATE varchar(100),"
					+ "PERI_ID INTEGER, "
					+ "FOREIGN KEY (PERI_ID) REFERENCES PERIODS (ID), "
					+ "UNIQUE (PERI_ID, ID))");
			dba.executeStatement("create table ELECTION ("
					+ "ID integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,"
					+ "PERCENT varchar(100),"
					+ "PERI_ID INTEGER, "
					+ "FOREIGN KEY (PERI_ID) REFERENCES PERIODS (ID),"
					+ "STAT_ID INTEGER, "
					+ "FOREIGN KEY (STAT_ID) REFERENCES STATES (ID),"
					+ "FOREIGN KEY (PERI_ID, STAT_ID) REFERENCES STATES (PERI_ID, ID))");
			dba.commit();
			
			DBStorage detail = new DBStorage();
			detail.setDBAccess(dba);
			detail.setWritebackTable("ELECTION");
			detail.open();
			
			Assert.assertArrayEquals(new String[] { "PERI_ID", "STAT_ID", "PERI_PERIOD", "STAT_STATE" },
					detail.getMetaData().getColumnMetaData("STAT_STATE").getLinkReference().getColumnNames());
			Assert.assertArrayEquals(new String[] { "PERI_ID", "PERI_PERIOD" },
					detail.getMetaData().getColumnMetaData("PERI_PERIOD").getLinkReference().getColumnNames());
		}
		finally
		{
			dba.close();
		}
	}
	
	/**
	 * AutoLink overdefined test. FK1_ID -> TAB1 FK2_ID -> TAB2 FK1_ID,FK2_ID ->
	 * TAB3
	 * 
	 * @throws Exception if it fails
	 */
	@Test
	public void testAutoLinkOverdefined() throws Exception
	{
		DBAccess dba = DBAccess.getDBAccess("jdbc:hsqldb:hsql://localhost/testdb");
		
		// set connect properties		
		dba.setUsername("sa");
		dba.setPassword("");
		dba.open();
		
		try
		{
			dba.executeStatement("drop table if exists ELECTIONDETAILS");
			dba.executeStatement("drop table if exists ELECTION");
			dba.executeStatement("drop table if exists STATES");
			dba.executeStatement("drop table if exists PERIODS");
			
			dba.executeStatement("create table PERIODS ("
					+ "ID integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,"
					+ "PERIOD varchar(100))");
			dba.executeStatement("create table STATES ("
					+ "ID integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,"
					+ "STATE varchar(100))");
			dba.executeStatement("create table ELECTION ("
					+ "ID integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,"
					+ "PERCENT varchar(100),"
					+ "PERI_ID INTEGER, "
					+ "FOREIGN KEY (PERI_ID) REFERENCES PERIODS (ID),"
					+ "STAT_ID INTEGER, "
					+ "FOREIGN KEY (STAT_ID) REFERENCES STATES (ID),"
					+ "UNIQUE (PERI_ID, STAT_ID))");
			dba.executeStatement("create table ELECTIONDETAILS ("
					+ "ID integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,"
					+ "DETAIL varchar(100), "
					+ "PERI_ID INTEGER, "
					+ "FOREIGN KEY (PERI_ID) REFERENCES PERIODS (ID),"
					+ "STAT_ID INTEGER, "
					+ "FOREIGN KEY (STAT_ID) REFERENCES STATES (ID),"
					+ "FOREIGN KEY (PERI_ID, STAT_ID) REFERENCES ELECTION (PERI_ID, STAT_ID))");
			dba.commit();
			
			DBStorage detail = new DBStorage();
			detail.setDBAccess(dba);
			detail.setWritebackTable("ELECTIONDETAILS");
			detail.open();
			
			Assert.assertArrayEquals(new String[] { "ID", "DETAIL", "PERI_ID", "PERI_PERIOD", "STAT_ID", "STAT_STATE", "ELEC_PERCENT" },
					detail.getMetaData().getColumnNames());
			Assert.assertArrayEquals(new String[] { "STAT_ID", "STAT_STATE" },
					detail.getMetaData().getColumnMetaData("STAT_STATE").getLinkReference().getColumnNames());
			Assert.assertArrayEquals(new String[] { "PERI_ID", "PERI_PERIOD" },
					detail.getMetaData().getColumnMetaData("PERI_PERIOD").getLinkReference().getColumnNames());
			Assert.assertArrayEquals(new String[] { "PERI_ID", "STAT_ID", "PERI_PERIOD", "STAT_STATE", "ELEC_PERCENT" },
					detail.getMetaData().getColumnMetaData("ELEC_PERCENT").getLinkReference().getColumnNames());
		}
		finally
		{
			dba.close();
		}
	}
	
	/**
	 * Tests if restrict conditions are working and supported.
	 * 
	 * @throws Exception if something fails.
	 */
	@Test
	public void testStorageRestrictCondition() throws Exception
	{
		DBAccess dba = createSubStorageRestrictDBAccess();
		
		try
		{
			DBStorage detailStorage = createSubStorageRestrictDetailDBStorage(dba);
			DBStorage masterStorage = createSubStorageRestrictMasterDBStorage(dba, detailStorage);
			
			masterStorage.setRestrictCondition(new Equals("NAME", "Second"));
			
			detailStorage.open();
			masterStorage.open();
			
			List<IBean> masterFetched = masterStorage.fetchBean(null, new SortDefinition("ID"), 0, 0);
			Assert.assertEquals(1, masterFetched.size());
			assertSubStorageRestrictMasterValues(masterFetched.get(0), BigDecimal.valueOf(2), BigDecimal.valueOf(1), "A");
			
			List<IBean> detailFetched = detailStorage.fetchBean(null, null, -1, -1);
			Assert.assertEquals(2, detailFetched.size());
			Assert.assertEquals(BigDecimal.valueOf(1), detailFetched.get(0).get("ID"));
			Assert.assertEquals(BigDecimal.valueOf(2), detailFetched.get(1).get("ID"));
		}
		finally
		{
			dba.close();
		}
	}
	
	/**
	 * Tests if restrict conditions are working and supported.
	 * 
	 * @throws Exception if something fails.
	 */
	@Test
	public void testStorageRestrictConditionWithSubStorageRestrictCondition1725() throws Exception
	{
		DBAccess dba = createSubStorageRestrictDBAccess();
		
		try
		{
			DBStorage detailStorage = createSubStorageRestrictDetailDBStorage(dba);
			DBStorage masterStorage = createSubStorageRestrictMasterDBStorage(dba, detailStorage);
			
			masterStorage.setRestrictCondition(new Equals("NAME", "Second"));
			detailStorage.setRestrictCondition(new Equals("FLAG", BigDecimal.ZERO));
			
			detailStorage.open();
			masterStorage.open();
			
			List<IBean> masterFetched = masterStorage.fetchBean(null, new SortDefinition("ID"), 0, 0);
			Assert.assertEquals(1, masterFetched.size());
			assertSubStorageRestrictMasterValues(masterFetched.get(0), BigDecimal.valueOf(2), BigDecimal.valueOf(1), "A");
			
			List<IBean> detailFetched = detailStorage.fetchBean(null, null, -1, -1);
			Assert.assertEquals(1, detailFetched.size());
			Assert.assertEquals(BigDecimal.valueOf(1), detailFetched.get(0).get("ID"));
		}
		finally
		{
			dba.close();
		}
	}
	
	/**
	 * Tests if restrict conditions or sub storages are working and supported.
	 * 
	 * @throws Exception if something fails.
	 */
	@Test
	public void testSubStorageRestrictCondition1527() throws Exception
	{
		DBAccess dba = createSubStorageRestrictDBAccess();
		
		try
		{
			DBStorage detailStorage = createSubStorageRestrictDetailDBStorage(dba);
			DBStorage masterStorage = createSubStorageRestrictMasterDBStorage(dba, detailStorage);
			
			detailStorage.setRestrictCondition(new Equals("FLAG", BigDecimal.ZERO));
			
			detailStorage.open();
			masterStorage.open();
			
			List<IBean> masterFetched = masterStorage.fetchBean(null, new SortDefinition("ID"), 0, 0);
			Assert.assertEquals(3, masterFetched.size());
			assertSubStorageRestrictMasterValues(masterFetched.get(0), BigDecimal.valueOf(1), null, null);
			assertSubStorageRestrictMasterValues(masterFetched.get(1), BigDecimal.valueOf(2), BigDecimal.valueOf(1), "A");
			assertSubStorageRestrictMasterValues(masterFetched.get(2), BigDecimal.valueOf(3), BigDecimal.valueOf(2), null);
			
			List<IBean> detailFetched = detailStorage.fetchBean(null, null, -1, -1);
			Assert.assertEquals(1, detailFetched.size());
			Assert.assertEquals(BigDecimal.valueOf(1), detailFetched.get(0).get("ID"));
		}
		finally
		{
			dba.close();
		}
	}
	
	/**
	 * Tests if restrict conditions or sub storages are working and supported.
	 * 
	 * @throws Exception if something fails.
	 */
	@Test
	public void testSubStorageRestrictConditionWithNot1688() throws Exception
	{
		DBAccess dba = createSubStorageRestrictDBAccess();
		
		try
		{
			DBStorage detailStorage = createSubStorageRestrictDetailDBStorage(dba);
			DBStorage masterStorage = createSubStorageRestrictMasterDBStorage(dba, detailStorage);
			
			detailStorage.setRestrictCondition(new Not(new Equals("FLAG", BigDecimal.ZERO)));
			
			detailStorage.open();
			masterStorage.open();
			
			List<IBean> masterFetched = masterStorage.fetchBean(null, new SortDefinition("ID"), 0, 0);
			Assert.assertEquals(3, masterFetched.size());
			assertSubStorageRestrictMasterValues(masterFetched.get(0), BigDecimal.valueOf(1), null, null);
			assertSubStorageRestrictMasterValues(masterFetched.get(1), BigDecimal.valueOf(2), BigDecimal.valueOf(1), null);
			assertSubStorageRestrictMasterValues(masterFetched.get(2), BigDecimal.valueOf(3), BigDecimal.valueOf(2), "B");
			
			List<IBean> detailFetched = detailStorage.fetchBean(null, null, -1, -1);
			Assert.assertEquals(1, detailFetched.size());
			Assert.assertEquals(BigDecimal.valueOf(2), detailFetched.get(0).get("ID"));
		}
		finally
		{
			dba.close();
		}
	}
	
	/**
	 * Tests if restrict conditions or sub storages are working and supported.
	 * 
	 * @throws Exception if something fails.
	 */
	@Test
	public void testSubStorageRestrictConditionWithFromClause1527() throws Exception
	{
		DBAccess dba = createSubStorageRestrictDBAccess();
		
		try
		{
			DBStorage detailStorage = createSubStorageRestrictDetailDBStorage(dba);
			DBStorage masterStorage = createSubStorageRestrictMasterDBStorage(dba, detailStorage);
			
			detailStorage.setFromClause(detailStorage.getWritebackTable());
			detailStorage.setWritebackTable(null);
			detailStorage.setRestrictCondition(new Equals("FLAG", BigDecimal.ZERO));
			
			detailStorage.open();
			masterStorage.open();
			
			List<IBean> masterFetched = masterStorage.fetchBean(null, new SortDefinition("ID"), 0, 0);
			Assert.assertEquals(3, masterFetched.size());
			assertSubStorageRestrictMasterValues(masterFetched.get(0), BigDecimal.valueOf(1), null, null);
			assertSubStorageRestrictMasterValues(masterFetched.get(1), BigDecimal.valueOf(2), BigDecimal.valueOf(1), "A");
			assertSubStorageRestrictMasterValues(masterFetched.get(2), BigDecimal.valueOf(3), BigDecimal.valueOf(2), null);
			
			List<IBean> detailFetched = detailStorage.fetchBean(null, null, -1, -1);
			Assert.assertEquals(1, detailFetched.size());
			Assert.assertEquals(BigDecimal.valueOf(1), detailFetched.get(0).get("ID"));
		}
		finally
		{
			dba.close();
		}
	}
	
	/**
	 * Tests if restrict conditions or sub storages are working and supported.
	 * 
	 * @throws Exception if something fails.
	 */
	@Test
	public void testSubStorageRestrictConditionWithNotWithFromClause1688() throws Exception
	{
		DBAccess dba = createSubStorageRestrictDBAccess();
		
		try
		{
			DBStorage detailStorage = createSubStorageRestrictDetailDBStorage(dba);
			DBStorage masterStorage = createSubStorageRestrictMasterDBStorage(dba, detailStorage);
			
			detailStorage.setFromClause(detailStorage.getWritebackTable());
			detailStorage.setWritebackTable(null);
			detailStorage.setRestrictCondition(new Not(new Equals("FLAG", BigDecimal.ZERO)));
			
			detailStorage.open();
			masterStorage.open();
			
			List<IBean> masterFetched = masterStorage.fetchBean(null, new SortDefinition("ID"), 0, 0);
			Assert.assertEquals(3, masterFetched.size());
			assertSubStorageRestrictMasterValues(masterFetched.get(0), BigDecimal.valueOf(1), null, null);
			assertSubStorageRestrictMasterValues(masterFetched.get(1), BigDecimal.valueOf(2), BigDecimal.valueOf(1), null);
			assertSubStorageRestrictMasterValues(masterFetched.get(2), BigDecimal.valueOf(3), BigDecimal.valueOf(2), "B");
			
			List<IBean> detailFetched = detailStorage.fetchBean(null, null, -1, -1);
			Assert.assertEquals(1, detailFetched.size());
			Assert.assertEquals(BigDecimal.valueOf(2), detailFetched.get(0).get("ID"));
		}
		finally
		{
			dba.close();
		}
	}
	
	/**
	 * Tests automatic link reference with and without writeback table.
	 * 
	 * @throws Exception if something fails.
	 */
	@Test
	public void testCreateAutomaticLinkReference() throws Exception
	{
		DBAccess dba = DBAccess.getDBAccess("jdbc:hsqldb:hsql://localhost/testdb");
		dba.setUsername("sa");
		dba.setPassword("");
		dba.open();
		
		try
		{
			dba.executeStatement("drop table if exists RESTRICT_MASTER");
			dba.executeStatement("drop table if exists RESTRICT_DETAIL");
			
			dba.executeStatement("create table RESTRICT_DETAIL ("
					+ "ID integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,"
					+ "NAME varchar(100),"
					+ "FLAG integer)");
			
			dba.executeStatement("create table RESTRICT_MASTER ("
					+ "ID integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,"
					+ "NAME varchar(100),"
					+ "RESTRICT_DETAIL_ID integer,"
					+ "foreign key(RESTRICT_DETAIL_ID) references RESTRICT_DETAIL(ID))");
			
			dba.commit();
			
			DBStorage detailStorage = new DBStorage();
			detailStorage.setDBAccess(dba);
			detailStorage.setRestrictCondition(new Equals("FLAG", BigDecimal.ZERO));
			detailStorage.setWritebackTable("RESTRICT_DETAIL");
			detailStorage.open();
			
			DBStorage masterStorage = new DBStorage();
			masterStorage.setDBAccess(dba);
			masterStorage.setWritebackTable("RESTRICT_MASTER");
			masterStorage.createAutomaticLinkReference(
					new String[] { "RESTRICT_DETAIL_ID", "RESTRICT_DETAIL_NAME" },
					detailStorage,
					new String[] { "ID", "NAME" });
			
			masterStorage.open();
			
			Assert.assertNotNull(masterStorage.getMetaData().getColumnMetaData("RESTRICT_DETAIL_ID").getLinkReference());
			Assert.assertNotNull(masterStorage.getMetaData().getColumnMetaData("RESTRICT_DETAIL_NAME").getLinkReference());
			
			detailStorage = new DBStorage();
			detailStorage.setDBAccess(dba);
			detailStorage.setRestrictCondition(new Equals("FLAG", BigDecimal.ZERO));
			detailStorage.setWritebackTable("RESTRICT_DETAIL");
			detailStorage.open();
			
			masterStorage = new DBStorage();
			masterStorage.setDBAccess(dba);
			masterStorage.setFromClause("RESTRICT_MASTER");
			masterStorage.createAutomaticLinkReference(
					new String[] { "RESTRICT_DETAIL_ID" },
					detailStorage,
					new String[] { "ID" });
			
			masterStorage.open();
			
			Assert.assertNotNull(masterStorage.getMetaData().getColumnMetaData("RESTRICT_DETAIL_ID").getLinkReference());
			
			masterStorage = new DBStorage();
			masterStorage.setDBAccess(dba);
			masterStorage.setFromClause("RESTRICT_MASTER");
			masterStorage.createAutomaticLinkReference(
					new String[] { "RESTRICT_DETAIL_ID" },
					"RESTRICT_DETAIL",
					new String[] { "ID" });
			
			masterStorage.open();
			
			Assert.assertNotNull(masterStorage.getMetaData().getColumnMetaData("RESTRICT_DETAIL_ID").getLinkReference());
			
			masterStorage = new DBStorage();
			masterStorage.setDBAccess(dba);
			masterStorage.setFromClause("RESTRICT_MASTER");
			
			masterStorage.open();
			
			masterStorage.createAutomaticLinkReference(
					new String[] { "RESTRICT_DETAIL_ID" },
					"RESTRICT_DETAIL",
					new String[] { "ID" });
			
			Assert.assertNotNull(masterStorage.getMetaData().getColumnMetaData("RESTRICT_DETAIL_ID").getLinkReference());
			
			masterStorage.close();
			detailStorage.close();
		}
		finally
		{
			dba.close();
		}
	}
	
	/**
	 * Tests WriteBack feature if no writeback table is set. Details see:
	 * http://support.sibvisions.com/index.php?do=details&task_id=1676
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testWriteBackInsteadOfMetaData1676() throws Exception
	{
		DBAccess dba = DBAccess.getDBAccess("jdbc:hsqldb:hsql://localhost/testdb");
		dba.setUsername("sa");
		dba.setPassword("");
		dba.open();
		
		try
		{
			dba.executeStatement("drop table if exists METADATA");
			
			dba.executeStatement("create table METADATA ("
					+ "ID integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,"
					+ "NAME varchar(100))");
			dba.commit();
			
			DBStorage dbs = new DBStorage();
			dbs.setDBAccess(dba);
			dbs.setFromClause("METADATA");
			dbs.open();
			
			Assert.assertFalse(dbs.getMetaData().isSupported(Feature.WriteBack));
			
			dbs.eventInsteadOfInsert().addListener(new IStorageListener()
			{
				public void storageChanged(StorageEvent pStorageEvent) throws Throwable
				{
					//no code
				}
			});
			
			Assert.assertTrue(dbs.getMetaData().isSupported(Feature.WriteBack));
			
			dbs.close();
			
			dbs = new DBStorage();
			dbs.setDBAccess(dba);
			dbs.setFromClause("METADATA");
			dbs.eventInsteadOfInsert().addListener(new IStorageListener()
			{
				public void storageChanged(StorageEvent pStorageEvent) throws Throwable
				{
					//no code
				}
			});
			dbs.open();
			
			Assert.assertTrue(dbs.getMetaData().isSupported(Feature.WriteBack));
		}
		finally
		{
			dba.close();
		}
	}
	
	/**
	 * Tests the additional query columns.
	 * http://support.sibvisions.com/index.php?do=details&task_id=1696
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testAdditionalQueryColumns1696() throws Exception
	{
		DBAccess dba = DBAccess.getDBAccess("jdbc:hsqldb:hsql://localhost/testdb");
		dba.setUsername("sa");
		dba.setPassword("");
		dba.open();
		
		try
		{
			dba.executeStatement("drop table if exists ADDITIONAL");
			
			dba.executeStatement("create table ADDITIONAL ("
					+ "ID integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,"
					+ "NAME varchar(100))");
			dba.commit();
			
			DBStorage dbs = new DBStorage();
			dbs.setDBAccess(dba);
			dbs.setAdditionalQueryColumns(new String[] { "1 VALUE", "2 SECOND_VALUE" });
			dbs.setWritebackTable("ADDITIONAL");
			dbs.open();
			
			MetaData metaData = dbs.getMetaData();
			
			Assert.assertEquals(metaData.getColumnMetaDataCount(), 4);
		}
		finally
		{
			dba.close();
		}
	}
	
	/**
	 * Tests if the restrict condition is altered after a fetch with an
	 * additional condition is executed, which should not be.
	 * 
	 * @throws Exception if the test fails.
	 */
	@Test
	public void testAlteringRestrictCondition1702() throws Exception
	{
		DBAccess dba = createSubStorageRestrictDBAccess();
		
		try
		{
			DBStorage detailStorage = createSubStorageRestrictDetailDBStorage(dba);
			
			And condition = new And(new Equals("1", "1"));
			
			detailStorage.setFromClause(detailStorage.getWritebackTable());
			detailStorage.setWritebackTable(null);
			detailStorage.setRestrictCondition(condition);
			
			detailStorage.open();
			
			// Sanity check.
			Assert.assertEquals(1, condition.getConditions().length);
			
			List<IBean> detailFetched = detailStorage.fetchBean(new Equals("NAME", "B"), null, -1, -1);
			Assert.assertEquals("Odd number of beans has been fetched even though there should be only one.", 1, detailFetched.size());
			
			Assert.assertEquals("Restrict condition was altered by fetch!", 1, condition.getConditions().length);
		}
		finally
		{
			dba.close();
		}
	}
	
	/**
	 * Tests if the additional condition is ignored if a restrict condition is
	 * set, which should not be.
	 * 
	 * @throws Exception if the test fails.
	 */
	@Test
	public void testIgnoredAdditionalRestrictCondition1702() throws Exception
	{
		DBAccess dba = createSubStorageRestrictDBAccess();
		
		try
		{
			DBStorage detailStorage = createSubStorageRestrictDetailDBStorage(dba);
			
			detailStorage.setFromClause(detailStorage.getWritebackTable());
			detailStorage.setWritebackTable(null);
			detailStorage.setRestrictCondition(new Equals("1", "1"));
			
			detailStorage.open();
			
			List<IBean> detailFetched = detailStorage.fetchBean(new Equals("NAME", "B"), null, -1, -1);
			Assert.assertEquals("Odd number of beans has been fetched even though there should be only one.", 1, detailFetched.size());
		}
		finally
		{
			dba.close();
		}
	}
	
	/**
	 * Tests setting a custom bean converter.
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testCustomBeanConverter() throws Exception
	{
		DBAccess dba = DBAccess.getDBAccess("jdbc:hsqldb:hsql://localhost/testdb");
		
		// set connect properties		
		dba.setUsername("sa");
		dba.setPassword("");
		
		dba.open();
		
		try
		{
			dba.executeStatement("delete from DETAIL");
			dba.executeStatement("delete from TEST");
			dba.commit();
	
			BeanConverter bc = new BeanConverter();
			
			DBStorage dbsTest = new DBStorage();
			dbsTest.setDBAccess(dba);
			dbsTest.setWritebackTable("TEST");
			dbsTest.setBeanConverter(bc);
			dbsTest.open();
		}
		finally
		{
			dba.close();
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Asserts the values of a bean in the SubStorage-Restrict-Test.
	 * 
	 * @param pBean the bean.
	 * @param pId the ID.
	 * @param pDetailId the detail ID.
	 * @param pName the name.
	 */
	private void assertSubStorageRestrictMasterValues(IBean pBean, BigDecimal pId, BigDecimal pDetailId, String pName)
	{
		Assert.assertEquals(pId, pBean.get("ID"));
		Assert.assertEquals(pDetailId, pBean.get("RESTRICT_DETAIL_ID"));
		Assert.assertEquals(pName, pBean.get("RESTRICT_DETAIL_NAME"));
	}
	
	/**
	 * Creates a DBAccess for the SubStorage-Restrict-Test.
	 * 
	 * @return the DBAccess.
	 * @throws Exception if creation fails.
	 */
	private DBAccess createSubStorageRestrictDBAccess() throws Exception
	{
		DBAccess dba = DBAccess.getDBAccess("jdbc:hsqldb:hsql://localhost/testdb");
		dba.setUsername("sa");
		dba.setPassword("");
		dba.open();
		
		try
		{
			dba.executeStatement("drop table if exists RESTRICT_MASTER");
			dba.executeStatement("drop table if exists RESTRICT_DETAIL");
			
			dba.executeStatement("create table RESTRICT_DETAIL ("
					+ "ID integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,"
					+ "NAME varchar(100),"
					+ "FLAG integer)");
			
			dba.executeStatement("create table RESTRICT_MASTER ("
					+ "ID integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,"
					+ "NAME varchar(100),"
					+ "RESTRICT_DETAIL_ID integer,"
					+ "foreign key(RESTRICT_DETAIL_ID) references RESTRICT_DETAIL(ID))");
			
			dba.commit();
			
			dba.executeStatement("insert into RESTRICT_DETAIL values (1, 'A', 0)");
			dba.executeStatement("insert into RESTRICT_DETAIL values (2, 'B', 1)");
			
			dba.commit();
			
			dba.executeStatement("insert into RESTRICT_MASTER values (1, 'First', null)");
			dba.executeStatement("insert into RESTRICT_MASTER values (2, 'Second', 1)");
			dba.executeStatement("insert into RESTRICT_MASTER values (3, 'Third', 2)");
			
			dba.commit();
			
			return dba;
		}
		catch (Exception e)
		{
			CommonUtil.close(dba);
			
			throw e;
		}
	}
	
	/**
	 * Creates a detail DBStorage for the SubStorage-Restrict-Test.
	 * 
	 * @param pDba the DBAccess.
	 * @return the DBStorage.
	 * @throws Exception if creation fails.
	 */
	private DBStorage createSubStorageRestrictDetailDBStorage(DBAccess pDba) throws Exception
	{
		DBStorage detailStorage = new DBStorage();
		detailStorage.setDBAccess(pDba);
		detailStorage.setWritebackTable("RESTRICT_DETAIL");
		
		return detailStorage;
	}
	
	/**
	 * Creates a master DBStorage for the SubStorage-Restrict-Test.
	 * 
	 * @param pDba the DBAccess.
	 * @param pDetailStorage the detail DBStorage.
	 * @return the DBStorage.
	 * @throws Exception if creation fails.
	 */
	private DBStorage createSubStorageRestrictMasterDBStorage(DBAccess pDba, DBStorage pDetailStorage) throws Exception
	{
		DBStorage masterStorage = new DBStorage();
		masterStorage.setDBAccess(pDba);
		masterStorage.setWritebackTable("RESTRICT_MASTER");
		masterStorage.createAutomaticLinkReference(
				new String[] { "RESTRICT_DETAIL_ID", "RESTRICT_DETAIL_NAME" },
				pDetailStorage,
				new String[] { "ID", "NAME" });
		
		return masterStorage;
	}

	/**
	 * Initializes an {@code byte} array with the given value at every entry.
	 * 
	 * @param pArray the array to initialize.
	 * @param pValue the value to fill in.
	 * @return the initialized array.
	 */
	private byte[] initArray(byte[] pArray, byte pValue)
	{
		for (int idx = 0; idx < pArray.length; idx++)
		{
			pArray[idx] = pValue;
		}
		
		return pArray;
	}
	
}	// TestDBStorage
