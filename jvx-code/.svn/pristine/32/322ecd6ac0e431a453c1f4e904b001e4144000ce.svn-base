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
 */
package com.sibvisions.rad.persist.jdbc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.rad.model.condition.Equals;
import javax.rad.remote.MasterConnection;
import javax.rad.type.bean.Bean;
import javax.rad.type.bean.IBean;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sibvisions.rad.model.remote.RemoteDataBook;
import com.sibvisions.rad.model.remote.RemoteDataSource;
import com.sibvisions.rad.util.DirectObjectConnection;

/**
 * Tests all Functions of {@link MySQLDBAccess}.
 * 
 * @author Roland Hörmann
 * @see com.sibvisions.rad.persist.jdbc.MySQLDBAccess
 */
public class TestMySQLDBAccess extends TestDBAccess
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** 
	 * Connect to the Test Database and create a Test Table.
	 * 
	 * @throws Exception
	 *             if the connect or the create table fails
	 */
	@Before
	@Override
	public void open() throws Exception
	{
		checkConnectionError();
		
		dba = createDBAccess();
		
		// set connect properties		
		dba.setUrl(getURLPrefix() + "//" + getHostName() + "/test");
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
		dba.executeStatement("delete from TEST");
		dba.executeStatement("delete from TEST_SORT");
		dba.executeStatement("delete from deletetest");
		dba.executeStatement("INSERT INTO deletetest (ID, CATEGORY_ID) VALUES" +
				"('1','1')," + 
				"('2','2')");
		dba.executeStatement("delete from inventry");
		dba.executeStatement("delete from hdr");

		// Test db 2
		dba2 = createDBAccess();
		dba2.setUrl(getURLPrefix() + "//" + getHostName() + "/test");
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

		dba.executeStatement("DROP TABLE `contacts`");
		dba.executeStatement(
				"CREATE TABLE `contacts` (" +
				"`id` int(6) unsigned NOT NULL AUTO_INCREMENT," +
				"`email` varchar(255) DEFAULT NULL," +
				"`vorname` varchar(255) DEFAULT NULL," +
				"`nachname` varchar(255) DEFAULT NULL," +
				"`update_zeit` datetime NOT NULL DEFAULT '0000-00-00 00:00:00'," +
				"PRIMARY KEY (`id`)," +
				"FULLTEXT KEY `fulltextidx` (`email`)" +
				") ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=latin1");

		dba.executeStatement("DROP TABLE IF EXISTS `limittest`");
		dba.executeStatement(
				"CREATE TABLE `limittest` (" +
				"`id` INT(11) NOT NULL AUTO_INCREMENT," +
				"`name` VARCHAR(50) NOT NULL DEFAULT ''," +
				"`value` DECIMAL(13,3) NOT NULL DEFAULT '0.000'," +
				"PRIMARY KEY (`id`)" +
				")");
		for (int counter = 0; counter < 35; counter++)
		{
			dba.executeStatement("INSERT INTO `limittest` VALUES (0, SUBSTRING(MD5(RAND()) FROM 1 FOR 12), TRUNCATE(RAND() * 1000, 3))");
		}
		
		dba.executeStatement(
				"INSERT INTO `contacts` (`id`, `email`, `vorname`, `nachname`, `update_zeit`) VALUES" +
				"('1','test@domain.at','test','test','0000-00-00 00:00:00')," + 
				"('2','test2@domainat','1234','1234','0000-00-00 00:00:00')");
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
		dba.executeStatement("create table test_unquoted_lowercase (id int(10) unsigned NOT NULL AUTO_INCREMENT, name varchar(100), "
				+ "PRIMARY KEY (id)) ENGINE=InnoDB DEFAULT CHARSET=latin1");

		dba.executeStatement("create table TEST_UNQUOTED_UPPERCASE (ID int(10) unsigned NOT NULL AUTO_INCREMENT, NAME varchar(100), "
				+ "PRIMARY KEY (ID)) ENGINE=InnoDB DEFAULT CHARSET=latin1");

		dba.executeStatement("create table `Test_Quoted` (`Id` int(10) unsigned NOT NULL AUTO_INCREMENT, `Name` varchar(100), " 
				+ "PRIMARY KEY (`Id`)) ENGINE=InnoDB DEFAULT CHARSET=latin1");
		
		dba.executeStatement("create table TEST_PK_FETCH (SOME_VALUE varchar(32) not null, CREATED_AT timestamp not null default CURRENT_TIMESTAMP, "
				+ "PRIMARY KEY (SOME_VALUE)) ENGINE=InnoDB DEFAULT CHARSET=latin1");

		dba.executeStatement("create table TEST_TIMESTAMP (SOME_VALUE varchar(32) not null, CREATED_AT timestamp not null default CURRENT_TIMESTAMP, "
//                + "CREATED2_AT timestamp(6) not null default CURRENT_TIMESTAMP," // 
		        + "PRIMARY KEY (SOME_VALUE)) ENGINE=InnoDB DEFAULT CHARSET=latin1");
	    
        dba.executeStatement("create table TEST_COLUMN_SPECIALCHAR (ID integer not null, `FIRST NAME` varchar(100), `LAST#NAME` varchar(100), "
                + "constraint TESC_PK primary key (ID))");
	}

    /**
     * {@inheritDoc}
     */
    @Test
    @Override
    public void testGetDefaultValue() throws Exception
    {
        Map<String, Object> htDefaults = dba.getDefaultValues("test", null, "TEST_DEFAULTS");
        
        Assert.assertEquals("N", htDefaults.get("ACTIVE"));
        Assert.assertEquals(new BigDecimal("1234"), htDefaults.get("NUMBERVAL"));
        Assert.assertEquals("2001-01-01 00:00:00.0", htDefaults.get("DATEVAL").toString());
        Assert.assertEquals("2010-01-01 12:00:00.0", htDefaults.get("DATETIMEVAL").toString());
        Assert.assertEquals("TEXT Test", htDefaults.get("TEXT"));
    }	
	
    /**
     * Tests DBAccess creation with an existing connection.
     * 
     * @throws Exception if the DBAccess creation failed
     */
    @Test
    public void testCreateWithConnection() throws Exception
    {
        // Won't test depending on MariaDB Version (>= 3) a mariadb jdbc url is returned.
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Creates a new DBAccess instance.
     * 
     * @return the database access
     */
    protected DBAccess createDBAccess()
    {
    	return new MySQLDBAccess();
    }
    
    /**
     * Gets the hostname.
     * 
     * @return the hostname or IP
     */
    protected String getHostName()
    {
        return "192.168.1.201";
    }	
    
    /**
     * Gets the URL prefix.
     * 
     * @return the URL prefix
     */
    protected String getURLPrefix()
    {
    	return "jdbc:mysql:";
    }
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
	 * Test the MySQL bug with tables with default, value that isn't used...
	 * 
	 * @throws Exception if not all methods work correctly
	 */
	@Test
	public void testBug35UseNullInsteadEmptyTimestampColumn() throws Exception
	{
		DBStorage dbsContacts = new DBStorage();
		dbsContacts.setWritebackTable("contacts");
		dbsContacts.setDBAccess(dba);
		dbsContacts.openInternal(false);
		
		List<IBean> lContacts = dbsContacts.fetchBean(null, null, 0, 1);
		
		System.out.println(lContacts);
	}

	/**
	 * Tests delete without prefix of table name and where clause columns.
	 * 
	 * @throws Exception if the test fails
	 */
	@Test
	public void testBug68() throws Exception
	{
		DBStorage dbs = new DBStorage();
		dbs.setDBAccess(dba);
		dbs.setFromClause("deletetest");
		dbs.setWritebackTable("deletetest");
		dbs.open();
		
		Bean bean = new Bean();
		bean.put("ID", Long.valueOf(1));
		
		dbs.delete(bean);
	}	
	
	/**
	 * Test duplicate columns.
	 * 
	 * @throws Exception
	 *             if the meta data aren't correct
	 */
	@Test	
	public void testBug82() throws Exception
	{
		DBStorage dbs = new DBStorage();
		dbs.setDBAccess(dba);
		dbs.setQueryColumns(new String[] { "id", "name name1", "name name2"});
		dbs.setFromClause("TEST");
		dbs.open();
	}	
	
	/**
	 * Tests refetch of PK when inserting a new row. The write back table doesn't contain
	 * the schema name.
	 * 
	 * @throws Exception if the test fails
	 */
	@Test
	public void testRefetchAfterInsertWithoutSchema() throws Exception
	{
		DBStorage dbs = new DBStorage();
		dbs.setDBAccess(dba);
		dbs.setFromClause("hdr");
		dbs.setWritebackTable("hdr");
		dbs.open();
		
		IBean bean = new Bean();
		bean.put("COMP_CODE", new BigDecimal(1));
		
		bean = dbs.insert(bean);
		
		Assert.assertNotNull("ID not set!", bean.get("ID"));
	}
	

	/**
	 * Tests refetch of PK when inserting a new row. The write back table contains
	 * the schema name.
	 * 
	 * @throws Exception if the test fails
	 */
	@Test
	public void testRefetchAfterInsertWithSchema() throws Exception
	{
		DBStorage dbs = new DBStorage();
		dbs.setDBAccess(dba);
		dbs.setFromClause("test.hdr");
		dbs.setWritebackTable("test.hdr");
		dbs.open();
		
		IBean bean = new Bean();
		bean.put("COMP_CODE", new BigDecimal(1));
		
		bean = dbs.insert(bean);
		
		Assert.assertNotNull("ID not set!", bean.get("ID"));
	}
	
    /**
     * Tests the support for limiting the results.
     *  
     * @throws Exception if the test fails.
     */
    @Test
    public void testLimit() throws Exception
    {
        dba.executeStatement("DROP TABLE IF EXISTS `limittest`");
        dba.executeStatement(
                "CREATE TABLE `limittest` (" +
                "`id` INT(11) NOT NULL AUTO_INCREMENT," +
                "`name` VARCHAR(50) NOT NULL DEFAULT ''," +
                "`value` DECIMAL(13,3) NOT NULL DEFAULT '0.000'," +
                "PRIMARY KEY (`id`)" +
                ")");
        for (int counter = 0; counter < 35; counter++)
        {
            dba.executeStatement("INSERT INTO `limittest` VALUES (0, SUBSTRING(MD5(RAND()) FROM 1 FOR 12), TRUNCATE(RAND() * 1000, 3))");
        }
        
        // Disable fetching of more data.
        dba.setMaxTime(0);
        ((MySQLDBAccess)dba).setMinimumFetchAmount(0);
        
        DBStorage dbs = new DBStorage();
        dbs.setDBAccess(dba);
        dbs.setWritebackTable("limittest");
        dbs.open();
        
        List<IBean> fetchedBean = dbs.fetchBean(null, null, 0, 10);
        
        System.out.println(fetchedBean.size());
        for (IBean bean : fetchedBean)
        {
            System.out.println(bean);
        }
        
        fetchedBean = dbs.fetchBean(null, null, 10, 10);
        
        System.out.println(fetchedBean.size());
        for (IBean bean : fetchedBean)
        {
            System.out.println(bean);
        }
        
        fetchedBean = dbs.fetchBean(null, null, 20, 10);
        
        System.out.println(fetchedBean.size());
        for (IBean bean : fetchedBean)
        {
            System.out.println(bean);
        }
        
        fetchedBean = dbs.fetchBean(null, null, 30, 10);
        
        System.out.println(fetchedBean.size());
        for (IBean bean : fetchedBean)
        {
            System.out.println(bean);
        }
        
        fetchedBean = dbs.fetchBean(null, null, 0, 100);
        
        System.out.println(fetchedBean.size());
        for (IBean bean : fetchedBean)
        {
            System.out.println(bean);
        }
        
        
        List<Object[]> fetchedData = dbs.fetch(null, null, 0, 25);
        
        Assert.assertNotNull("No data was fetched.", fetchedData);
        Assert.assertEquals("Not enough (or too much) data has been fetched.", 25, fetchedData.size());
        Assert.assertNotNull("Fetched data is null terminated, even though it should not be.", fetchedData.get(24));
        
        Assert.assertEquals(BigDecimal.valueOf(1), fetchedData.get(0)[0]);
        Assert.assertEquals(BigDecimal.valueOf(25), fetchedData.get(24)[0]);
        
        fetchedData = dbs.fetch(null, null, 25, 25);
        
        Assert.assertNotNull("No data was fetched.", fetchedData);
        Assert.assertEquals("Not enough (or too much) data has been fetched.", 11, fetchedData.size());
        Assert.assertNull("Fetched data is not null terminated, even though it should be.", fetchedData.get(10));
        
        Assert.assertEquals(BigDecimal.valueOf(26), fetchedData.get(0)[0]);
        Assert.assertEquals(BigDecimal.valueOf(35), fetchedData.get(9)[0]);

        dba.executeStatement("DROP TABLE IF EXISTS `limittest`");
    }
    
	/**
	 * Tests if more data is fetched even if there is a limit set.
	 * 
	 * @throws Exception if this test fails.
	 */
	@Test
	public void testLimitFetchMore() throws Exception
	{
		// Make sure that more data can be fetched.
		dba.setMaxTime(99999999);
		
		DBStorage dbs = new DBStorage();
		dbs.setDBAccess(dba);
		dbs.setWritebackTable("limittest");
		dbs.open();
		
		List<Object[]> fetchedData = dbs.fetch(null, null, 0, 5);
		
		Assert.assertNotNull("No data was fetched.", fetchedData);
		Assert.assertEquals("Not all data was fetched.", 36, fetchedData.size());
		Assert.assertNull("Additional fetched data is not null terminated, even though there should be no more data.", fetchedData.get(35));
		
		// Make sure that not everything can be fetched for the next test.
		dba.setMaxTime(8);
		
		// Insert more lines for the next test. The internal logic dictates that
		// the maximum additional fetched data can be 100 rows if the query took
		// 0 seconds the first time. There for 150 should suffice.
		for (int counter = 0; counter < 300; counter++)
		{
			dba.executeStatement("INSERT INTO `limittest` VALUES (0, SUBSTRING(MD5(RAND()) FROM 1 FOR 12), TRUNCATE(RAND() * 1000, 3))");
		}
		
		fetchedData = dbs.fetch(null, null, 0, 5);
		System.out.println(fetchedData.size());
		Assert.assertNotNull("No data was fetched.", fetchedData);
		Assert.assertTrue("No additional data was fetched, the test case can not work in these circumstances. Please run the test again.", fetchedData.size() > 5);
		Assert.assertTrue("All additional data was fetched, the test case can not work in these circumstances. Please run the test again.", fetchedData.size() < 299);
		Assert.assertNotNull("Additional fetched data is null terminated even though there should be more data.", fetchedData.get(fetchedData.size() - 1));
	}
	
	/**
	 * Tests CURRENT_TIMESTAMP default values.
	 * 
	 * @throws Throwable
	 */
	@Test
    public void testDefaultCurrentTimeStamp() throws Throwable
    {
	    prepareTestTables();
	    
        DBStorage dbs = new DBStorage();
        dbs.setDBAccess(dba);
        dbs.setFromClause("TEST_TIMESTAMP");
        dbs.setWritebackTable("TEST_TIMESTAMP");
        dbs.open();
        
        DirectObjectConnection con = new DirectObjectConnection();
        con.put("testTimestamp", dbs);
        
        MasterConnection macon = new MasterConnection(con);
        macon.open();
        
        RemoteDataSource rds = new RemoteDataSource(macon);
        rds.open();

        RemoteDataBook rdbTest = new RemoteDataBook();
        rdbTest.setDataSource(rds);
        rdbTest.setName("testTimestamp");
        rdbTest.open();
        
        int iSearchNext = rdbTest.searchNext(new Equals("SOME_VALUE", "TEST_VALUE"));
        if (iSearchNext >= 0)
        {
            rdbTest.setSelectedRow(iSearchNext);
            rdbTest.delete();
        }
        
        rdbTest.insert(false);
        rdbTest.setValue("SOME_VALUE", "TEST_VALUE");
        rdbTest.saveSelectedRow();
        
        iSearchNext = rdbTest.searchNext(new Equals("SOME_VALUE", "TEST_VALUE"));
        Assert.assertTrue("INSERT failed!", iSearchNext >= 0);
        rdbTest.setSelectedRow(iSearchNext);
        Assert.assertNotNull("CREATED_AT not set!", rdbTest.getValue("CREATED_AT"));
//        Assert.assertNotNull("CREATED2_AT not set!", rdbTest.getValue("CREATED2_AT")); // 2 columns with default current_timestamp not possible in all mysql/ mariadbs
    }
	
} 	// TestMySQLDBAccess
