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
 * 01.10.2008 - [RH] - creation
 * 23.11.2009 - [RH] - ColumnMetaData && PrimaryKey Column is replaced with MetaData class
 * 03.03.2010 - [JR] - used ServerMetaData
 * 10.03.2010 - [RH] - #81: test case added
 * 13.03.2010 - [JR] - #90: testBug90 implemented
 * 27.03.2010 - [JR] - #92: default value test   
 * 30.12.2010 - [JR] - integrated tests from TestSqlSupport  
 * 29.01.2011 - [JR] - #211: test case      
 * 11.03.2011 - [RH] - #308 - DB specific automatic quoting implemented                      
 * 28.04.2011 - [RH] - #341 - LikeReverse Condition, LikeReverseIgnoreCase Condition   
 * 03.06.2011 - [JR] - testMetaData: #308 upper-case column test
 * 23.12.2011 - [JR] - fixed test cases
 * 08.05.2012 - [JR] - #575: test case added
 * 20.11.2012 - [JR] - #589, #607: test cases added
 * 07.04.2014 - [RZ] - r5500 changed default of ServerColumnMetaData.isWriteable() to false, made columns in tests explicit writable
 * 06.05.2014 - [RZ] - #1029: added testSorting()
 * 01.02.2017 - [JR] - #1751: test case testDefaultAllowedValues extended
 * 20.02.2020 - [DJ] - #2207: is alive query
 */
package com.sibvisions.rad.persist.jdbc;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.rad.model.ColumnDefinition;
import javax.rad.model.MetaDataCacheOption;
import javax.rad.model.ModelException;
import javax.rad.model.SortDefinition;
import javax.rad.model.condition.And;
import javax.rad.model.condition.CompareCondition;
import javax.rad.model.condition.Equals;
import javax.rad.model.condition.ICondition;
import javax.rad.model.condition.Like;
import javax.rad.model.condition.LikeIgnoreCase;
import javax.rad.model.condition.LikeReverse;
import javax.rad.model.condition.Not;
import javax.rad.model.condition.Or;
import javax.rad.model.datatype.BigDecimalDataType;
import javax.rad.model.datatype.StringDataType;
import javax.rad.model.datatype.TimestampDataType;
import javax.rad.persist.ColumnMetaData;
import javax.rad.persist.DataSourceException;
import javax.rad.remote.MasterConnection;
import javax.rad.type.bean.Bean;
import javax.rad.type.bean.IBean;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sibvisions.naming.ContextFactory;
import com.sibvisions.rad.model.Filter;
import com.sibvisions.rad.model.remote.RemoteDataBook;
import com.sibvisions.rad.model.remote.RemoteDataSource;
import com.sibvisions.rad.persist.jdbc.DBAccess.ParameterizedStatement;
import com.sibvisions.rad.persist.jdbc.event.ConnectionEvent;
import com.sibvisions.rad.persist.jdbc.event.type.IConfigureConnectionListener;
import com.sibvisions.rad.persist.jdbc.event.type.IUnconfigureConnectionListener;
import com.sibvisions.rad.util.DirectObjectConnection;
import com.sibvisions.util.log.ILogger.LogLevel;
import com.sibvisions.util.log.LoggerFactory;
import com.sibvisions.util.type.DateUtil;


/**
 * Tests all Functions of {@link DBAccess}.
 * 
 * @author Roland Hörmann
 * @see com.sibvisions.rad.persist.jdbc.DBAccess
 */
public class TestDBAccess
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The HSQLDBAccess Test instance. */
	protected DBAccess	dba;

	/** The HSQLDBAccess Test2 instance. */
	protected DBAccess	dba2;
	
	/** a flag for broken database connection. */
	private static boolean bConnectionError;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Sets the static members.
	 */
	@BeforeClass
	public static void beforeClass()
	{
		//LocaleUtil.setDefault(new Locale("en"));
		bConnectionError = false;
	}
	
	/** 
	 * Connect to the Test Database and create a Test Table.
	 * 
	 * @throws Exception
	 *             if the connect or the create table fails
	 */
	@Before
	public void open() throws Exception
	{
		checkConnectionError();
		
		dba = new HSQLDBAccess();
		
		// set connect properties
		dba.setUrl("jdbc:hsqldb:hsql://localhost/testdb");
		dba.setUsername("sa");
		dba.setPassword("");
	
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

		// drop test table
		try
		{
			dba.executeStatement("drop table detail");
		}
		catch (SQLException se)
		{
			se.printStackTrace();
		}
		try
		{
			dba.executeStatement("drop table test");
		}
		catch (SQLException se)
		{
			se.printStackTrace();
		}

		try
		{
			dba.executeStatement("drop table test_defaults");
		}
		catch (SQLException se)
		{
			se.printStackTrace();
		}

		try
		{
			dba.executeStatement("drop table test_sort");
		}
		catch (SQLException se)
		{
			se.printStackTrace();
		}
		
		// create test_defaults table
		dba.executeStatement(
				"CREATE TABLE test_defaults (active char(1) DEFAULT 'N' NOT NULL, " +
				                            "datetimeval timestamp DEFAULT '2010-01-01 12:00:00', " +
				                            "numberval decimal(11) DEFAULT '1234', " +
				                            "text varchar(50) DEFAULT 'TEXT Test', " +
				                            "dateval date DEFAULT '2001-01-01')");
		
		// create test table
		dba.executeStatement(
				"create table test (id integer GENERATED BY DEFAULT AS IDENTITY primary key, name varchar(100))");
		
		// create detail table
		dba.executeStatement(
				"create table detail (id integer GENERATED BY DEFAULT AS IDENTITY primary key, name varchar(100), " +
				"test_id INTEGER, FOREIGN KEY (test_id) REFERENCES test (id))");

		// create test sort table
		dba.executeStatement(
				"create table test_sort (id integer GENERATED BY DEFAULT AS IDENTITY primary key, name varchar(100), sort integer)");
		
		// Test db 2
		dba2 = new HSQLDBAccess();
		dba2.setUrl("jdbc:hsqldb:hsql://localhost/personsdb"); 
		dba2.setUsername("sa");
		dba2.setPassword("");		
		
		dba2.open();
	}

	/**
	 * Tests {@link DBAccess} events.
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testEvents() throws Exception
	{
	    final StringBuilder sbEvents = new StringBuilder();
	    
	    dba.close();
	    
	    dba.eventConfigureConnection().addListener(new IConfigureConnectionListener()
        {
            public void configureConnection(ConnectionEvent pEvent) throws Throwable
            {
                Assert.assertNotNull(pEvent.getConnection());
                Assert.assertNotNull(pEvent.getDBAccess());
                
                sbEvents.append("configure\n");
            }
        });
	    
	    dba.eventUnconfigureConnection().addListener(new IUnconfigureConnectionListener()
        {
            public void unconfigureConnection(ConnectionEvent pEvent) throws Throwable
            {
                Assert.assertNotNull(pEvent.getConnection());
                Assert.assertNotNull(pEvent.getDBAccess());

                sbEvents.append("unconfigure\n");
            }
        });
	    
	    dba.open();
	    dba.close();
	    
	    Assert.assertEquals("configure\nunconfigure\n", sbEvents.toString());
	}
	
	/**
	 * Close the db connection.
	 * 
	 * @throws Exception if the connection can not be closed
	 */
	@After
	public void close() throws Exception
	{
		if (!bConnectionError)
		{
		    if (dba != null)
		    {
		        dba.close();
		    }
			
			if (dba2 != null)
			{
			    dba2.close();
			}
		}
	}	

	/** 
	 * Prepares test tables for new tests.
	 * 
	 * @throws Exception
	 *             if the connect or the create table fails
	 */
	protected void prepareTestTables() throws Exception
	{
		dropTestTables();

		createTestTables();
	}

	/** 
	 * Creates test tables for new tests.
	 * 
	 * @throws Exception
	 *             if the connect or the create table fails
	 */
	protected void createTestTables() throws Exception
	{
		dba.executeStatement("create table test_unquoted_lowercase (id integer GENERATED BY DEFAULT AS IDENTITY primary key, name varchar(100))");

		dba.executeStatement("create table TEST_UNQUOTED_UPPERCASE (ID integer GENERATED BY DEFAULT AS IDENTITY primary key, NAME varchar(100))");

		dba.executeStatement("create table `Test_Quoted` (`Id` integer GENERATED BY DEFAULT AS IDENTITY primary key, `Name` varchar(100))");
		
		dba.executeStatement("create table TEST_PK_FETCH (Some_Value varchar(32), created_AT timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, PRIMARY KEY (SOME_VALUE))"); 
	
        dba.executeStatement("create table TEST_COLUMN_SPECIALCHAR (ID integer not null, \"FIRST NAME\" varchar(100), \"LAST#NAME\" varchar(100), "
                + "constraint TESC_PK primary key (ID))");
	}
	
	/** 
	 * Drops test tables for new tests.
	 */
	protected void dropTestTables()
	{
		try
		{
			dba.executeStatement("drop table test_unquoted_lowercase");
		}
		catch (SQLException e)
		{
			//ignore
		}
		try
		{
			dba.executeStatement("drop table TEST_UNQUOTED_UPPERCASE");
		}
		catch (SQLException e)
		{
			//ignore
		}
		
		try
		{
			dba.executeStatement("drop table `Test_Quoted`");
		}
		catch (SQLException e)
		{
			//ignore
		}
		
		try
		{
			dba.executeStatement("drop table TEST_PK_FETCH");
		}
		catch (SQLException e)
		{
			//ignore
		}

        try
        {
            dba.executeStatement("drop table TEST_COLUMN_SPECIALCHAR");
        }
        catch (SQLException e)
        {
            //ignore
        }

        try
        {
            dba.executeStatement("drop table TEST_PK_FETCH");
        }
        catch (SQLException e)
        {
            //ignore
        }
		try
        {
            dba.executeStatement("drop table TEST_TIMESTAMP");
        }
        catch (SQLException e)
        {
            //ignore
        }
        try
        {
            dba.executeStatement("drop table test_timestamp");
        }
        catch (SQLException e)
        {
            //ignore
        }
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Returns a random number in a predefined range.
	 * 
	 * @param iFirst first possible number
	 * @param iLast last possible number
	 * @return number between <code>iFirst</code> and <code>iLast</code>
	 */
	public int getRandom(int iFirst, int iLast)
	{
		return (int)(Math.random() * (float)(iLast - iFirst)) + iFirst;
	}
	
	/**
	 * Gets a value from a mapped key/value list. The key will be checked upper or lower case.
	 * 
	 * @param pValues the key/value map
	 * @param pColumn the key for which we want the value
	 * @return the found value or <code>null</code> if the key is unknown
	 */
	protected Object getDefaultValue(Map<String, Object> pValues, String pColumn)
	{
		Object oValue = pValues.get(pColumn);
		
		if (oValue == null)
		{
			oValue = pValues.get(pColumn.toUpperCase());
		}
		
		return oValue;
	}

	/**
	 * Checks if the db connection has errors and stops the test.
	 */
	protected void checkConnectionError()
	{
		if (bConnectionError)
		{
			Assert.fail("DB connection error!");
		}
	}
	
	/**
	 * Sets the state of the connection error.
	 * 
	 * @param pError <code>true</code> means that the connection is broken, <code>false</code> if the connection is 
	 *               ready to use
	 */
	protected void setConnectionError(boolean pError)
	{
		bConnectionError = pError;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Test the BaseStatement (open(), close(), commit(), rollback(),
	 * executeQuery(), executeUpdate(), isConnected(),
	 * getPreparedStatement(String)()) methods.
	 * 
	 * @throws Exception
	 *             if not all methods work correctly
	 */
	@Test
	public void testBaseStatements() throws Exception
	{
		// insert data into test table
		PreparedStatement psPreparedStatement = dba.getPreparedStatement(
				"insert into TEST (`ID`, `NAME`) values(?,?)");
		psPreparedStatement.setInt(1, 1);
		psPreparedStatement.setString(2, "projectX");
		psPreparedStatement.executeUpdate();
		
		psPreparedStatement.setInt(1, 2);
		psPreparedStatement.setString(2, "projectX - subprojects");
		psPreparedStatement.executeUpdate();
		
		// select data from test table
		psPreparedStatement = dba.getPreparedStatement("select * from TEST");
		ResultSet rs = psPreparedStatement.executeQuery();

		if (rs.next())
		{
			Assert.assertTrue(rs.getInt("ID") >= 1 && rs.getString("NAME").equals("projectX"));
		}
		else
		{
			Assert.assertTrue(false);
		}

		if (rs.next())
		{
			Assert.assertTrue(rs.getInt("ID") >= 2 
					          && rs.getString("NAME").equals("projectX - subprojects"));
		}
		else
		{
			Assert.assertTrue(false);
		}
		
		Assert.assertFalse(rs.next());
	}

	/**
	 * Test insert(), update() delete() select() fetch(), getRowCount() methods.
	 * 
	 * @throws Exception
	 *             if not all methods work correctly
	 */
	@Test
	public void testInsertUpdateDelete() throws Exception
	{
		ServerMetaData smd = new ServerMetaData();
		
		ServerColumnMetaData scmd = new ServerColumnMetaData(new Name("ID", dba.quote("ID")));
		scmd.getColumnMetaData().setTypeIdentifier(BigDecimalDataType.TYPE_IDENTIFIER);
		scmd.setSQLType(Types.NUMERIC);
		scmd.setSQLTypeName("numeric");
		scmd.setWritable(true);
		
		smd.addServerColumnMetaData(scmd);

		scmd = new ServerColumnMetaData(new Name("NAME", dba.quote("NAME")));
		scmd.setSQLType(Types.VARCHAR);
		scmd.setSQLTypeName("varchar");
		scmd.setWritable(true);
		
		smd.addServerColumnMetaData(scmd);
		smd.setAutoIncrementColumnNames(new Name[] { new Name("ID", dba.quote("ID"))});
		smd.setPrimaryKeyColumnNames(new Name[] { new Name("ID", dba.quote("ID"))});

		// insert()
		Object[] oaNew = dba.insert("TEST", smd, new Object[] {Integer.valueOf(1), "insert()"});
		Object[] oaNew2 = dba.insert("TEST", smd, new Object[] {BigDecimal.valueOf(2), "insert2()"});

		// update()
		dba.update("TEST", smd, oaNew, new Object[] {BigDecimal.valueOf(1), "update()"});

		// select data from test table		
		List<Object[]> lResult = dba.fetch(smd, null, null, "TEST", null, null, null, null, 0, 2);		

		boolean bUpdateOK = false;
		for (int i = 0; i < lResult.size() && lResult.get(i) != null; i++)
		{
			if (lResult.get(i)[1].equals("update()"))
			{
				bUpdateOK = true;
			}
		}
		Assert.assertFalse(!bUpdateOK);

		// delete()
		dba.delete("TEST", smd, oaNew2);

		// select data from test table
		lResult = dba.fetch(smd, null, null, "TEST", null, null, null, null, 0, 1);		

		if (lResult != null)
		{
			Assert.assertEquals(oaNew[0].toString(), lResult.get(0)[0].toString());
			Assert.assertEquals("update()", lResult.get(0)[1]);
		}
	}
	
	/**
	 * Test getMetaData() methods.
	 * 
	 * @throws Throwable if the meta data aren't correct
	 */
	@Test
	public void testMetaData() throws Throwable
	{
		DBStorage dbs = new DBStorage();
		dbs.setDBAccess(dba);
		dbs.setWritebackTable("TEST");
		dbs.setFromClause("TEST");
		dbs.open();

		//Test for #308
		DirectObjectConnection con = new DirectObjectConnection();
		con.put("dbs", dbs);
		
		MasterConnection macon = new MasterConnection(con);
		macon.open();
		
		RemoteDataSource rds = new RemoteDataSource(macon);
		rds.open();
		
		RemoteDataBook rdb = new RemoteDataBook();
		rdb.setDataSource(rds);
		rdb.setName("dbs");
		rdb.open();
		
		Assert.assertArrayEquals(new String[] {"ID", "NAME"}, rdb.getRowDefinition().getColumnNames());
	}
	
	/**
	 * Test the "filter" with the select(), fetch(), getRowCount() methods.
	 * 
	 * @throws Exception
	 *             if the Filter doesn't work correctly
	 */
	@Test
	public void testFilter() throws Exception
	{
		ServerMetaData smd = new ServerMetaData();
		
		ServerColumnMetaData scmd = new ServerColumnMetaData(new Name("ID", dba.quote("ID")));
		scmd.getColumnMetaData().setTypeIdentifier(BigDecimalDataType.TYPE_IDENTIFIER);
		scmd.setSQLType(Types.NUMERIC);
		scmd.setSQLTypeName("numeric");
		scmd.setRealQueryColumnName(dba.quote("ID"));		
		scmd.setWritable(true);
		
		smd.addServerColumnMetaData(scmd);

		scmd = new ServerColumnMetaData(new Name("NAME", dba.quote("NAME")));
		scmd.setSQLType(Types.VARCHAR);
		scmd.setSQLTypeName("varchar");
		scmd.setRealQueryColumnName(dba.quote("NAME"));
		scmd.setWritable(true);		

		smd.addServerColumnMetaData(scmd);
		smd.setAutoIncrementColumnNames(new Name[] { new Name("ID", dba.quote("ID"))});
		smd.setPrimaryKeyColumnNames(new Name[] { new Name("ID", dba.quote("ID"))});

		// insert()
		dba.insert("TEST", smd, new Object[] {Integer.valueOf(1), "insert()"});
		dba.insert("TEST", smd, new Object[] {Integer.valueOf(2), "insert2()"});

		Object[] oaNew3 = dba.insert("TEST", smd, new Object[] {Integer.valueOf(3), "zinsert2()"});

		// select data from test table with filter
		ICondition filter = new Like("NAME", "z%");
		
		List<Object[]> lResult = dba.fetch(smd, null, null, "TEST", filter, null, null, null, 0, 1);
		
		if (lResult != null)
		{
			Assert.assertTrue(lResult.get(0)[0].toString().equals(oaNew3[0].toString())
					          && lResult.get(0)[1].equals("zinsert2()"));
		}
		else
		{
			Assert.fail();
		}
		
		Assert.assertNotNull(dba.fetch(smd, null, null, "TEST", new Not(filter), null, null, null, 0, 1));
		
		// Test, if NPE occures
		try
		{
			lResult = dba.fetch(smd, null, null, "TEST", new And(), null, null, null, 0, 1);
		}
		catch (Exception ex)
		{
			Assert.fail("fetch with new And()");
		}
	}

	/**
	 * Test fetch(...)  with cache method.
	 * 
	 * @throws Exception
	 *             if the meta data aren't correct
	 */
	@Test
	public void testFetchCache() throws Exception
	{
		DBStorage storage = new DBStorage();
		storage.setDBAccess(dba2);
		
		ServerMetaData smd = storage.createMetaData(null, null, null, null, null, "ADRESSEN", null, false, false);  
		
		List<Object[]> lResult = dba2.fetch(smd, null, null, "ADRESSEN", null, null, null, null, 0, 1);
		
		int iSize = 0;
		while (true)
		{
			if (lResult != null)
			{
				for (int i = 0; i < lResult.size(); i++)
				{
					// if end
					if (lResult.get(i) == null)
					{
						return;
					}
				}
				iSize += lResult.size();
			}
			
			lResult = dba2.fetch(smd, null, new String[] { "*" }, "ADRESSEN", null, null, null, null, iSize, 1);
		}
	}
	
	/**
	 * Test duplicate columns.
	 * 
	 * @throws Exception
	 *             if the meta data aren't correct
	 */
	@Test	
	public void testBug81() throws Exception
	{
		try 
		{
			dba.getColumnMetaData("TEST", new String [] { dba.quote("ID"), dba.quote("NAME"), dba.quote("NAME")}, 
					              null, null, null);
		}
		catch (DataSourceException dataSourceException)
		{
			if (!dataSourceException.getMessage().toLowerCase().startsWith(
					"Duplicate definition of 'name' in DBStorage".toLowerCase()))
			{
				throw dataSourceException;
			}
		}
	}
	
	/**
	 * Test BigDecimal return types.
	 * 
	 * @throws Exception if the test fails
	 */
	@Test
	public void testBug90() throws Exception
	{
		DBStorage dbs = new DBStorage();
		dbs.setDBAccess(dba);
		dbs.setFromClause("TEST");
		dbs.open();
		
		PreparedStatement psPreparedStatement = dba.getPreparedStatement(
				"insert into TEST (" + DBAccess.QUOTE + "NAME" + DBAccess.QUOTE + ") values(?)");
		psPreparedStatement.setString(1, "projectX");
		psPreparedStatement.executeUpdate();
		
		List<Object[]> liRows = dbs.fetch(null, null, 0, -1);
		
		Assert.assertNotNull(liRows);
		Assert.assertEquals(2, liRows.size());
		
		Assert.assertTrue("Invalid class: " + liRows.get(0)[0].getClass(), liRows.get(0)[0] instanceof BigDecimal);
	}
	
	/**
	 * Tests default value detection through JDBC driver.
	 * 
	 * @throws Exception if the test fails because the metadata are wrong
	 */
	@Test
	public void testGetDefaultValue() throws Exception
	{
		Map<String, Object> htDefaults = dba.getDefaultValues(null, null, "test_defaults");
		
		System.out.println(htDefaults);
		
		Assert.assertEquals("N", getDefaultValue(htDefaults, "active"));
		Assert.assertEquals(new BigDecimal("1234"), getDefaultValue(htDefaults, "numberval"));
		Assert.assertEquals("2001-01-01 00:00:00.0", getDefaultValue(htDefaults, "dateval").toString());
		Assert.assertEquals("2010-01-01 12:00:00.0", getDefaultValue(htDefaults, "datetimeval").toString());
		Assert.assertEquals("TEXT Test", getDefaultValue(htDefaults, "text"));
	}
	
	/**
	 * Tests DBAccess creation with an existing connection.
	 * 
	 * @throws Exception if the DBAccess creation failed
	 */
	@Test
	public void testCreateWithConnection() throws Exception
	{
		DBAccess dbaNew = DBAccess.getDBAccess(dba.getConnection());
		
		Assert.assertEquals(dbaNew.getClass().getName(), dba.getClass().getName());
	}

	/**
	 * Tests where clause creation.
	 * 
	 * @throws Exception Condition is wrong.
	 */
	@Test
	public void testSql() throws Exception
	{
		DBStorage storage = new DBStorage();
		storage.setDBAccess(dba);
			
		ServerMetaData smd = storage.createMetaData(null, null, null, null, null, "TEST", null, false, false);  
		
		ICondition cond = new Equals("ID", BigDecimal.valueOf(15)).and(new Equals("NAME", "a").or(new Equals("NAME", "b")));
		Assert.assertEquals("m.ID = ? AND (m.NAME = ? OR m.NAME = ?)".toLowerCase(), dba.getSQL(smd, cond, true).toLowerCase().replace("`", ""));
		
		cond = new Equals("ID", BigDecimal.valueOf(15)).and(new Equals("NAME", "a")).or(new Equals("NAME", "b"));
		Assert.assertEquals("(m.ID = ? AND m.NAME = ?) OR m.NAME = ?".toLowerCase(), dba.getSQL(smd, cond, true).toLowerCase().replace("`", ""));
		
		cond = new Not(new Equals("ID", BigDecimal.valueOf(15)).and(new Equals("NAME", "a")).or(new Equals("NAME", "b")));
		Assert.assertEquals("NOT ((m.ID = ? AND m.NAME = ?) OR m.NAME = ?)".toLowerCase(), dba.getSQL(smd, cond, true).toLowerCase().replace("`", ""));
		
		if (dba instanceof PostgreSQLDBAccess) // In postgresql null and '' is different, so both will be checked.
		{
    		cond = new Equals("ID", BigDecimal.valueOf(15)).and(new Not(new Equals("NAME", "a").or(new Equals("NAME", "b"))));
    		Assert.assertEquals("m.ID = ? AND NOT (m.NAME = ? OR m.NAME = ?)".toLowerCase(), dba.getSQL(smd, cond, true).toLowerCase().replace("`", ""));
		}
	}
	
	/**
	 * Test of LikeIgnoreCase.
	 * 
	 * @throws Exception Condition is wrong.
	 */
	@Test
	public void testLikeIgnoreCase() throws Exception
	{
		DBStorage storage = new DBStorage();
		storage.setDBAccess(dba);
			
		ServerMetaData smd = storage.createMetaData(null, null, null, null, null, "TEST", null, false, false);  
		
		ICondition cond = new LikeIgnoreCase("NAME", "hand*");
		
		Assert.assertEquals("UPPER(m.NAME) LIKE UPPER(?)".toLowerCase(), dba.getSQL(smd, cond, true).toLowerCase());
		Assert.assertArrayEquals(new Object[] {"hand%"}, dba.getParameter(cond));
	}	
	
	/**
	 * Checks default allowed value detection. This test throws an exception if some datatypes are not supported for
	 * a specific database, e.g. Oracle & DB2 (no boolean datatype)
	 * 
	 * @throws Exception if the test fails
	 */
	@Test
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
		
		dba.executeStatement("CREATE TABLE test_defaultallowed (dummyname varchar(100), boolval boolean DEFAULT true, zahl integer DEFAULT 15, text varchar(100) DEFAULT 'Hallo')");
		
		DBStorage dbs = new DBStorage();
		dbs.setDBAccess(dba);
		dbs.setWritebackTable("test_defaultallowed");
		dbs.open();
		
		Assert.assertArrayEquals(new Boolean[] {Boolean.TRUE, Boolean.FALSE}, dbs.getMetaData().getColumnMetaData("BOOLVAL").getAllowedValues());
		//#1751 #1750
		Assert.assertEquals(Boolean.TRUE, dbs.getMetaData().getColumnMetaData("BOOLVAL").getDefaultValue());
		Assert.assertEquals(BigDecimal.valueOf(15), dbs.getMetaData().getColumnMetaData("ZAHL").getDefaultValue());
		Assert.assertEquals("Hallo", dbs.getMetaData().getColumnMetaData("TEXT").getDefaultValue());
		
		Bean bnData = new Bean();
        bnData.put("DUMMYNAME", "A");
		bnData.put("BOOLVAL", Boolean.FALSE);
		dbs.insert(bnData);
		
		bnData = new Bean();
		bnData.put("DUMMYNAME", "B");
        dbs.insert(bnData);
        
        bnData = new Bean();
        bnData.put("BOOLVAL", BigDecimal.valueOf(1));
        dbs.insert(bnData);

        List<IBean> liBean = dbs.fetchBean(null, null, 0, -1);
        
        Assert.assertEquals(3, liBean.size());
        
        for (IBean bean : liBean)
        {
            if (bean.get("DUMMYNAME") == null)
            {
                Assert.assertEquals(Boolean.TRUE, bean.get("BOOLVAL"));
            }
            else if ("A".equals(bean.get("DUMMYNAME")))
            {
                Assert.assertEquals(Boolean.FALSE, bean.get("BOOLVAL"));
            }
            else
            {
                Assert.assertEquals(Boolean.TRUE, bean.get("BOOLVAL"));                
            }
        }
	}
	
    /**
     * checks if a column starting with a number is quoted correctly.
     * 
     * @throws Exception if the test fails
     */
    @Test
    public void testColumnStartsWithNumber() throws Exception
    {
        try
        {
            dba.executeStatement("drop table test_columnquote");
        }
        catch (SQLException se)
        {
            //nothing to be done
        }
        
        dba.executeStatement("CREATE TABLE test_columnquote (`15dummyname` varchar(100))");
        
        LoggerFactory.getInstance(DBAccess.class).setLevel(LogLevel.ALL);
        try
        {
            DBStorage dbs = new DBStorage();
            dbs.setDBAccess(dba);
            dbs.setWritebackTable("test_columnquote");
            dbs.open();
            
            dbs.fetchBean(null, null, 0, -1);
        }
        finally
        {
            LoggerFactory.getInstance(DBAccess.class).setLevel(LogLevel.ERROR);
        }
    }
    
	/**
	 * Test if the schema can be detected in all DBs.
	 * 
	 * @throws Exception if the test fails
	 */
	@Test
	public void testSchemaDetect() throws Exception
	{
		TableInfo mdInfo = dba.getTableInfo("TEST");

		Assert.assertNotNull(mdInfo.getSchema());
	}
	
	/**
	 * Test the LikeReserve Filter. 
	 * 
	 * @throws Exception if not all DataBook methods work correctly
	 */
	@Test
	public void testFilterLikeReverse() throws Exception
	{
		ServerMetaData smd = new ServerMetaData();
		
		ServerColumnMetaData scmd = new ServerColumnMetaData(new Name("ID", dba.quote("ID")));
		scmd.getColumnMetaData().setTypeIdentifier(BigDecimalDataType.TYPE_IDENTIFIER);
		scmd.setSQLType(Types.NUMERIC);
		scmd.setSQLTypeName("numeric");
		scmd.setRealQueryColumnName(dba.quote("ID"));		
		scmd.setWritable(true);
		
		smd.addServerColumnMetaData(scmd);

		scmd = new ServerColumnMetaData(new Name("NAME", dba.quote("NAME")));
		scmd.setSQLType(Types.VARCHAR);
		scmd.setSQLTypeName("varchar");
		scmd.setRealQueryColumnName(dba.quote("NAME"));		
		scmd.setWritable(true);
		
		smd.addServerColumnMetaData(scmd);
		smd.setAutoIncrementColumnNames(new Name[] { new Name("ID", dba.quote("ID"))});
		smd.setPrimaryKeyColumnNames(new Name[] { new Name("ID", dba.quote("ID"))});

		// insert()
		dba.insert("TEST", smd, new Object[] {Integer.valueOf(1), "*name*"});

		// select data from test table with filter
		ICondition filter = new LikeReverse("NAME", "First name");
		
		List<Object[]> lResult = dba.fetch(smd, null, null, "TEST", filter, null, null, null, 0, 2);
		
		Assert.assertNotNull(lResult);
		Assert.assertEquals(2, lResult.size());
		Assert.assertNotNull(lResult.get(0));
		Assert.assertNull(lResult.get(1));
	}

	/**
	 * Test DB Storage.
	 * 
	 * @throws Exception if the test fails
	 */
	@Test
	public void testDBStorageTableName() throws Exception
	{
	    String[] columnNames = new String[] {"ID", "NAME", "TEST_ID", "TEST_NAME"};
	    
		DBStorage detail = new DBStorage();
		detail.setDBAccess(dba);
		detail.setWritebackTable("detail");
		detail.open();
		
		Assert.assertArrayEquals(columnNames, detail.getMetaData().getColumnNames());

		detail = new DBStorage();
		detail.setDBAccess(dba);
		detail.setWritebackTable("DETAIL");
		detail.open();
		
		Assert.assertArrayEquals(columnNames, detail.getMetaData().getColumnNames());
	}

	/**
	 * Tests to set a timestamp column with a java.util.Date.
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testBug575() throws Exception
	{
		DBStorage dbs = new DBStorage();
		dbs.setDBAccess(dba);
		dbs.setWritebackTable("TEST_DEFAULTS");
		dbs.setAutoLinkReference(false);
		dbs.setAllowedValues(false);
		dbs.setDefaultValue(false);
		dbs.open();
		
		Bean bnNew = new Bean();
		bnNew.put("ACTIVE", "Y");
		bnNew.put("CHECKME", "J");
		bnNew.put("TEXT", "N");
		bnNew.put("DATETIMEVAL", DateUtil.getTimestamp(2010, 2, 12, 2, 5, 3));

		dbs.insert(bnNew);
		dbs.delete(bnNew);
	}	
	
	/**
	 * Tests if an already opened.
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testCR607() throws Exception
	{
		Assert.assertTrue(dba.isOpen());
		
		Connection con = dba.getConnection();
		
		dba.open();
		
		Assert.assertEquals(con, dba.getConnection());
	}
	
	/**
	 * Tests DBAccess detection via JNDI.
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testCR589() throws Exception
	{
		//Setup temporary context
		
		String sOldProp = System.getProperty(Context.INITIAL_CONTEXT_FACTORY);
		
		System.setProperty(Context.INITIAL_CONTEXT_FACTORY, ContextFactory.class.getName());

		try
		{
			InitialContext ctxt = new InitialContext();
			
			//Check DBAccess detection
	
			ctxt.bind("java:/dba", dba);
			
			DBAccess dbacc = DBAccess.getDBAccess("java:/dba");
			
			Assert.assertSame(dba, dbacc);
			
			//Check Connection detection
			
			ctxt.bind("java:/con", dba.getConnection());
			
			dbacc = DBAccess.getDBAccess("java:/con");
			
			Assert.assertSame(dba.getConnection(), dbacc.getConnection());
			
			DBCredentials dbcred = new DBCredentials(null, "java:/ds", "none", "none");
			            
            ctxt.bind("java:/ds", new DummyDataSource(dba));
            
            DBAccess dbaDs = DBAccess.getDBAccess(dbcred);
            dbaDs.open();
		}
		finally
		{
			if (sOldProp == null)
			{
				System.clearProperty(Context.INITIAL_CONTEXT_FACTORY);
			}
			else
			{
				System.setProperty(Context.INITIAL_CONTEXT_FACTORY, sOldProp);
			}
		}
	}
	
	/**
	 * Tests the sorting options (default sort (SortDefinition), order by clause).
	 * 
	 * Written for #1029.
	 * 
	 * @throws Exception if the test fails.
	 */
	@Test
	public void testSorting() throws Exception
	{
		ServerMetaData smd = new ServerMetaData();
		
		ServerColumnMetaData scmd = new ServerColumnMetaData(new Name("ID", dba.quote("ID")));
		scmd.getColumnMetaData().setTypeIdentifier(BigDecimalDataType.TYPE_IDENTIFIER);
		scmd.setSQLType(Types.NUMERIC);
		scmd.setSQLTypeName("numeric");
		scmd.setRealQueryColumnName(dba.quote("ID"));		
		scmd.setWritable(true);
		
		smd.addServerColumnMetaData(scmd);

		scmd = new ServerColumnMetaData(new Name("NAME", dba.quote("NAME")));
		scmd.setSQLType(Types.VARCHAR);
		scmd.setSQLTypeName("varchar");
		scmd.setRealQueryColumnName(dba.quote("NAME"));		
		scmd.setWritable(true);
		
		smd.addServerColumnMetaData(scmd);

		scmd = new ServerColumnMetaData(new Name("SORT", dba.quote("SORT")));
		scmd.setSQLType(Types.NUMERIC);
		scmd.setSQLTypeName("numeric");
		scmd.setRealQueryColumnName(dba.quote("SORT"));		
		scmd.setWritable(true);
		
		smd.addServerColumnMetaData(scmd);
		
		smd.setAutoIncrementColumnNames(new Name[] { new Name("ID", dba.quote("ID"))});
		smd.setPrimaryKeyColumnNames(new Name[] { new Name("ID", dba.quote("ID"))});
		
		dba.insert("TEST_SORT", smd, new Object[] {Integer.valueOf(1), "H", Integer.valueOf(44)});
		dba.insert("TEST_SORT", smd, new Object[] {Integer.valueOf(2), "C", Integer.valueOf(2)});
		dba.insert("TEST_SORT", smd, new Object[] {Integer.valueOf(3), "G", Integer.valueOf(16)});
		dba.insert("TEST_SORT", smd, new Object[] {Integer.valueOf(4), "Y", Integer.valueOf(97)});
		dba.insert("TEST_SORT", smd, new Object[] {Integer.valueOf(5), "U", Integer.valueOf(45)});
		dba.insert("TEST_SORT", smd, new Object[] {Integer.valueOf(6), "L", Integer.valueOf(32)});
		dba.insert("TEST_SORT", smd, new Object[] {Integer.valueOf(7), "A", Integer.valueOf(87)});
		
		DBStorage dbs = new DBStorage();
		dbs.setDBAccess(dba);
		dbs.setFromClause("TEST_SORT");
		dbs.open();
		
		Object[] expectedValues = null;
		int expectedCounter = 0;
		
		
		// Default
		
		// ORDER BY clause
		dbs.setOrderByClause("`SORT` ASC");
		
		expectedValues = new Object[] {"C", "G", "L", "H", "U", "A", "Y"};
		expectedCounter = 0;
		for (Object[] values : dbs.fetch(null, null, 0, 0))
		{
			if (values == null)
			{
				break;
			}
			
			Assert.assertEquals(expectedValues[expectedCounter++], values[1]);
		}
		
		// default sort without ORDER BY
		dbs.setDefaultSort(new SortDefinition("NAME"));
		dbs.setOrderByClause(null);
		
		expectedValues = new Object[] {"A", "C", "G", "H", "L", "U", "Y"};
		expectedCounter = 0;
		for (Object[] values : dbs.fetch(null, null, 0, 0))
		{
			if (values == null)
			{
				break;
			}
			
			Assert.assertEquals(expectedValues[expectedCounter++], values[1]);
		}
		
		// default sort wins over ORDER BY
		dbs.setDefaultSort(new SortDefinition("NAME"));
		dbs.setOrderByClause("sort ASC");
		
		expectedValues = new Object[] {"A", "C", "G", "H", "L", "U", "Y"};
		expectedCounter = 0;
		for (Object[] values : dbs.fetch(null, null, 0, 0))
		{
			if (values == null)
			{
				break;
			}
			
			Assert.assertEquals(expectedValues[expectedCounter++], values[1]);
		}
	}
	
	/**
	 * Gets the select part of a query column.
	 * @param pQueryColumn the query column
	 * @param pColumnName the column name
	 * @return the select part of a query column.
	 */
	private String getOldQueryColumns(String pQueryColumn, String pColumnName)
	{
		String sQueryColumn = pQueryColumn.toLowerCase();
		String sName = pColumnName.toLowerCase();
		int iLen = sQueryColumn.length() - sName.length() - 1;
		
		if (sQueryColumn.endsWith(sName) 
		    && iLen > 0
			&& Character.isWhitespace(sQueryColumn.charAt(iLen)))
		{
			sQueryColumn = pQueryColumn.substring(0, iLen).trim(); 
			iLen = sQueryColumn.length() - 3;
			
			if (sQueryColumn.endsWith("as") 
			    && iLen > 0
				&& Character.isWhitespace(sQueryColumn.charAt(iLen)))
			{
				sQueryColumn = pQueryColumn.substring(0, iLen).trim(); 
			}
		}
		else
		{
			sQueryColumn = pQueryColumn;
		}
		
		return sQueryColumn;
	}
	
	/**
	 * Gets the select part of a query column.
	 * @param pQueryColumn the query column
	 * @return the select part of a query column.
	 */
	private String getNewQueryColumns(String pQueryColumn)
	{
		String realQueryColumn = pQueryColumn.trim();
		
		int aliasIndex = -1;
		if (realQueryColumn.endsWith("\"") && realQueryColumn.length() > 2)
		{
			aliasIndex = realQueryColumn.lastIndexOf('\"', realQueryColumn.length() - 2);
		}
		else if (realQueryColumn.length() > 0 && Character.isLetterOrDigit(realQueryColumn.charAt(realQueryColumn.length() - 1)))
		{
			aliasIndex = realQueryColumn.lastIndexOf(' ');
		}
		
		if (aliasIndex >= 0)
		{
			realQueryColumn = realQueryColumn.substring(0, aliasIndex).trim();
		}
		
		if (realQueryColumn.length() > 3 && realQueryColumn.toLowerCase().endsWith(" as"))
		{
			realQueryColumn = realQueryColumn.substring(0, realQueryColumn.length() - 3).trim();
		}
		
		return realQueryColumn; 
	}

	/**
	 * Gets the select part of a query column.
	 * @param pQueryColumn the query column
	 * @param pColumnName the column name
	 */
	private void testQueryColumns(String pQueryColumn, String pColumnName)
	{
		Assert.assertEquals("m.TEST", getNewQueryColumns(pQueryColumn));
	}
	
	/**
	 * Tests the creation of real query columns.
	 * 
	 * @throws Exception if the test fails.
	 */
	@Test
	public void testRealQueryColumn() throws Exception
	{
		testQueryColumns("m.TEST as Test", "TEST");
		testQueryColumns("m.TEST as Test ", "TEST");
		testQueryColumns("m.TEST as \"TEST\"", "TEST");
		testQueryColumns("m.TEST as \"Test\"", "Test");
		testQueryColumns("m.TEST as\"Test\"", "Test");
		testQueryColumns("m.TEST AS Test", "TEST");
		testQueryColumns("m.TEST Test", "TEST");
		testQueryColumns("m.TEST Test ", "TEST");
		testQueryColumns("m.TEST \"TEST\"", "TEST");
		testQueryColumns("m.TEST \"Test\"", "Test");
		testQueryColumns("m.TEST\"Test\"", "Test");
		testQueryColumns("m.TEST ", "Test");
		Assert.assertNotEquals("(select x as y from", getNewQueryColumns("(select x as y from dual)"));
	}
	
	/**
	 * Tests the creation of real query columns.
	 * 
	 * @throws Exception if the test fails.
	 */
	@Test
	public void testCustomColumnMetaData() throws Exception
	{
		try
		{
			dba.setColumnMetaDataCreator(this, "createColumnMetaData");
            DBAccess.clearMetaData();
			
			ServerColumnMetaData[] result = dba.getColumnMetaData("TEST", null, null, null, null);
			
			Assert.assertEquals(MyColumnMetaData.class, result[0].getColumnMetaData().getClass());
			Assert.assertEquals(MyColumnDefinition.class, result[0].getColumnMetaData().createColumnDefinition().getClass());
		}
		finally
		{
			dba.setColumnMetaDataCreator(null);
			DBAccess.clearMetaData();
		}
	}
	
	/**
	 * Creates the test column meta data.
	 * @param pDBAccess the db access
	 * @param pResultSetMetaData the resultset metadata
	 * @param pResultSetColumnIndex the column index
	 * @return the column metadata
	 * @throws DataSourceException the exception
	 * @throws SQLException the exception
	 */
	public ColumnMetaData createColumnMetaData(DBAccess pDBAccess, ResultSetMetaData pResultSetMetaData, int pResultSetColumnIndex) 
			throws DataSourceException, SQLException
	{
		MyColumnMetaData cmd = new MyColumnMetaData();
		
		pDBAccess.initializeColumnMetaData(pResultSetMetaData, pResultSetColumnIndex, cmd);
		
		return cmd;
	}

	/**
	 * Custom ColumnMetaData.
	 * 
	 * @author Martin Handsteiner
	 */
	public static class MyColumnMetaData extends ColumnMetaData
	{
		/** short label. */
		private String shortLabel;

		/**
		 * Gets the short label.
		 * @return the short label.
		 */
		public String getShortLabel()
		{
			return shortLabel;
		}

		/**
		 * Sets the short label.
		 * @param pShortLabel the short label.
		 */
		public void setShortLabel(String pShortLabel)
		{
			shortLabel = pShortLabel;
		}
		
		/**
		 * {@inheritDoc}
		 */
		public ColumnDefinition createColumnDefinition() throws ModelException
		{
			MyColumnDefinition mycolumndef = new MyColumnDefinition();
			
			initializeColumnDefinition(mycolumndef);
			
			return mycolumndef;
		}
	}

	/**
	 * Custom ColumnMetaData.
	 * 
	 * @author Martin Handsteiner
	 */
	public static class MyColumnDefinition extends ColumnDefinition
	{
		/** short label. */
		private String shortLabel;

		/**
		 * Gets the short label.
		 * @return the short label.
		 */
		public String getShortLabel()
		{
			return shortLabel;
		}

		/**
		 * Sets the short label.
		 * @param pShortLabel the short label.
		 */
		public void setShortLabel(String pShortLabel)
		{
			shortLabel = pShortLabel;
		}
	}

	/**
	 * Tests the locking of a row.
	 * 
	 * @param pTableName the table to test.
	 * @throws Exception if it fails.
	 */
	private void testInsertLockUpdateDeleteIntern(String pTableName) throws Exception
	{
		prepareTestTables();
		
		DBStorage dbs = new DBStorage();
		dbs.setDBAccess(dba);
		dbs.setWritebackTable(pTableName);
		dbs.setLockOnRefetch(true);
		dbs.open();
		
		Object[] result = dbs.insert(new Object[] {null, "test Locking!"});
		
		dba.setAutoCommit(false);
		try
		{
			result = dbs.refetchRow(result);
		
			Object[] updateRow = new Object[] {result[0], "updated test value!"};
			
			Object[] newResult = dbs.update(result, updateRow);
			
			dba.commit();
		}
		finally
		{
			dba.setAutoCommit(true);
		}
		
		List<Object[]> fetchResult = dbs.fetch(new Equals("ID", BigDecimal.valueOf(-1)), new SortDefinition(false, "NAME"), 0, -1);
		
		
		dbs.delete(result);
	}
	
	/**
	 * Tests the locking of a row.
	 * 
	 * @throws Exception if the test fails.
	 */
	@Test
	public void testInsertLockUpdateDeleteUnquotedLowercase() throws Exception
	{
		testInsertLockUpdateDeleteIntern("test_unquoted_lowercase");
	}

	/**
	 * Tests the locking of a row.
	 * 
	 * @throws Exception if the test fails.
	 */
	@Test
	public void testInsertLockUpdateDeleteUnquotedUppercase() throws Exception
	{
		testInsertLockUpdateDeleteIntern("TEST_UNQUOTED_UPPERCASE");
	}

	/**
	 * Tests the locking of a row.
	 * 
	 * @throws Exception if the test fails.
	 */
	@Test
	public void testInsertLockUpdateDeleteQuoted() throws Exception
	{
		testInsertLockUpdateDeleteIntern("`Test_Quoted`");
	}

	/**
	 * Tests refetch of PK when inserting a new row. The write back table contains
	 * the schema name.
	 * 
	 * @throws Exception if the test fails
	 */
	@Test
	public void testExecuteSql() throws Exception
	{
		Assert.assertTrue(dba.executeSql("select count(*) from TEST").get(0) instanceof BigDecimal);
	}

	/**
	 * Tests refetch of PK when inserting a new row. The write back table contains
	 * the schema name.
	 * 
	 * @throws Exception if the test fails
	 */
	@Test
	public void testExecuteQuery() throws Exception
	{
		testBaseStatements();
		System.out.println(dba.executeQuery("select * from TEST"));
	}
	
	/**
	 * Tests refetch of PK when inserting a new row. The write back table contains
	 * the schema name.
	 * 
	 * @throws Exception if the test fails
	 */
	@Test
	public void testReturningKeys() throws Exception
	{
		List<Bean> keys = dba.executeInsertStatement("insert into TEST ( `NAME` ) values ('Hallo')", new String[] {"ID"});
		
		Assert.assertTrue(keys.size() == 1 && keys.get(0).get("ID") != null);
	}
	
	
//	/**
//	 * Tests default null binding to columns. 
//	 * 
//	 * @throws Exception if the test fails
//	 */
	// [TODO] Implement proper null binding.
//	@Test
//	public void testNullValueBinding() throws Exception
//	{
//		try
//		{
//			dba.executeStatement("drop table TESTNULLBINDING");
//		}
//		catch (Exception ex)
//		{
//			// Do nothing
//		}
//		dba.executeStatement("create table TESTNULLBINDING (id numeric(18) NOT NULL, name varchar(100), counter numeric(18), birthday timestamp)");
//
//		dba.executeStatement("insert into TESTNULLBINDING (id, name, counter, birthday) values (?, ?, ?, ?)", BigDecimal.valueOf(1), "Name", null, null);
//		dba.executeStatement("insert into TESTNULLBINDING (id, name, counter, birthday) values (?, ?, ?, ?)", BigDecimal.valueOf(2), null, "1", null);
//		dba.executeStatement("insert into TESTNULLBINDING (id, name, counter, birthday) "+
//	                         "values (?, ?, ?, ?)", BigDecimal.valueOf(3), null, null, new Timestamp(120, 0, 1, 0, 0, 0, 0));
//
//		Assert.assertEquals("[{id=1, name=name, counter=null, birthday=null},"+ 
//	                         "{id=2, name=null, counter=1, birthday=null}, {id=3, name=null, counter=null, birthday=2020-01-01 00:00:00.0}]", 
//				String.valueOf(dba.executeQuery("select * from TESTNULLBINDING")).toLowerCase());
//		dba.executeStatement("update TESTNULLBINDING set name = ? where id = ?", null, BigDecimal.valueOf(1));
//		dba.executeStatement("update TESTNULLBINDING set counter = ? where id = ?", null, BigDecimal.valueOf(2));
//		dba.executeStatement("update TESTNULLBINDING set birthday = ? where id = ?", null, BigDecimal.valueOf(3));
//		Assert.assertEquals("[{id=1, name=null, counter=null, birthday=null}, "+
//	                          {id=2, name=null, counter=null, birthday=null}, "+
//	                          {id=3, name=null, counter=null, birthday=null}]", String.valueOf(dba.executeQuery("select * from TESTNULLBINDING")).toLowerCase());
//	}
	
	
	
	/**
	 * get Query column name without alias.
	 * 
	 * @throws Exception if the test fails
	 */
	@Test
	public void testRealQueryColumnName() throws Exception
	{
		Assert.assertEquals("`Hallo`", dba.getRealQueryColumnName("`Hallo`"));
		Assert.assertEquals("`Hallo`", dba.getRealQueryColumnName("`Hallo` `Hallo`"));
		Assert.assertEquals("`Hallo`", dba.getRealQueryColumnName("`Hallo` as `Hallo`"));
        Assert.assertEquals("`Hallo`", dba.getRealQueryColumnName("`Hallo` as Hallo"));

		Assert.assertEquals("HALLO_", dba.getRealQueryColumnName("HALLO_"));
		Assert.assertEquals("HALLO_", dba.getRealQueryColumnName("HALLO_ HALLO_"));
		Assert.assertEquals("HALLO_", dba.getRealQueryColumnName("HALLO_ as HALLO_"));
	
		Assert.assertEquals("(select `HALLO_` from dual)", dba.getRealQueryColumnName("(select `HALLO_` from dual)"));
		Assert.assertEquals("(select `HALLO_` from dual)", dba.getRealQueryColumnName("(select `HALLO_` from dual) HALLO_"));
		Assert.assertEquals("(select `HALLO_` from dual)", dba.getRealQueryColumnName("(select `HALLO_` from dual) as HALLO_"));

		Assert.assertEquals("(select `HALLO_` from dual)", dba.getRealQueryColumnName("(select `HALLO_` from dual)"));
		Assert.assertEquals("(select `HALLO_` from dual)", dba.getRealQueryColumnName("(select `HALLO_` from dual) `HALLO_`"));
		Assert.assertEquals("(select `HALLO_` from dual)", dba.getRealQueryColumnName("(select `HALLO_` from dual) as `HALLO_`"));
	}

	/**
	 * Tests the {@link DBAccess#findNamedParameters(String, List)} function.
	 * 
	 * @throws Exception if the test fails.
	 */
	@Test
	public void testFindNamedParameters() throws Exception
	{
		dba.setQuoteCharacters("\"", "\"");
		
		assertNamedParameters("");
		assertNamedParameters("select * from TEST");
		assertNamedParameters("select * from TEST where COLUMN = :PARAM", "PARAM");
		assertNamedParameters("select * from TEST where COLUMN = :PARAM and COLUMN = :PARAM2 and COLUMN = :PARAM3", "PARAM", "PARAM2", "PARAM3");
		
		assertNamedParameters("select ':NOT_A_PARAM' from TEST where COLUMN = :PARAM and COLUMN = :PARAM2", "PARAM", "PARAM2");
		assertNamedParameters("select COLUMN /* :NOT_A_PARAM */ from TEST where COLUMN = :PARAM and COLUMN = :PARAM2", "PARAM", "PARAM2");
		assertNamedParameters("select COLUMN from TEST where COLUMN = :PARAM and COLUMN = :PARAM2 -- :NOT_A_PARAM", "PARAM", "PARAM2");
		
		assertNamedParameters(":PARAM", "PARAM");
		assertNamedParameters("':NOT_A_PARAM'");
		assertNamedParameters("/* :NOT_A_PARAM */ ");
		assertNamedParameters("-- :NOT_A_PARAM");
		
		assertNamedParameters("' '' :NOT_A_PARAM'");
		assertNamedParameters("' '' /* -- */ :NOT_A_PARAM'");
		assertNamedParameters("' '' /* -- */' :PARAM", "PARAM");
		
		assertNamedParameters("\":NOT_A_PARAM\" :PARAM2", "PARAM2");

		assertNamedParameters("/* 'Hallo' = :PARAM1 */ 'XX' = :PARAM2 /* :PARAM3 */", "PARAM2");
		assertNamedParameters("/* 'Hallo' = :PARAM1 */ to_char('24:12', 'HH24:MI' = :PARAM2 /* :PARAM3 */", "PARAM2");
		
		assertNamedParameters(":_STARTS_WITH_UNDERSCORE", "_STARTS_WITH_UNDERSCORE");
		
		// Test sanity
		dba.setQuoteCharacters(null, null);
		assertNamedParameters("' '' /* -- */' \":PARAM2\" :PARAM", "PARAM2", "PARAM");
		dba.setQuoteCharacters("", "");
		assertNamedParameters("' '' /* -- */' \":PARAM2\" :PARAM", "PARAM2", "PARAM");
		
		// Lower and upper case
		assertNamedParameters("select COLUMN from TEST where COLUMN = :param and COLUMN = :pArAM2 -- :NOT_A_PARAM", "param", "pArAM2");
		
		// Multipart names
		assertNamedParameters("select COLUMN from TEST where COLUMN = :info.value and COLUMN = :some$value.complex#value -- :NOT_A_PARAM", 
				              "info.value", "some$value.complex#value");
		
		
		assertNamedParameters("SELECT * FROM (select kundkst, bez from kundkst /*where kundnr = :block.kundnr*/ " +
		                      "where kundnr = :BLOCK.KUNDNR and aktiv = 'J' order by 1) WHERE 1=2)", 
				              "BLOCK.KUNDNR");
		
		assertNamedParameters("SELECT * FROM (select kundkst, bez from kundkst /*where kundnr = :BLOCK.KUNDNR*/ " +
			                  "where kundnr = :BLOCK.KUNDNR and aktiv = 'J' order by 1) WHERE 1=2)", 
				              "BLOCK.KUNDNR");
		assertNamedParameters("SELECT * FROM (select kundkst, bez from kundkst /*where kundnr = :BLOCK.KUNDNR*/ " +
				              "where kundnr = (:BLOCK.KUNDNR or kundnr1 = :BLOCK.KUNDNR) and aktiv = 'J' order by 1) WHERE 1=2)", 
					          "BLOCK.KUNDNR", "BLOCK.KUNDNR");		
	}

	/**
	 * Tests {@link DBAccess#replaceNamedParameters(String, Map, ServerMetaData, boolean)}.
	 */
	@Test
	public void testReplaceNamedPrameter()
	{
		HashMap<String, CompareCondition> hmpConditions = new HashMap<String, CompareCondition>();
		hmpConditions.put("BLOCK.KUNDNR", null);
		
		Assert.assertEquals("SELECT * FROM (select kundkst, bez from kundkst /*where kundnr = :block.kundnr*/ " +
						    "where kundnr = ? and aktiv = 'J' order by 1) WHERE 1=2)",
				            dba.replaceNamedParameters("SELECT * FROM (select kundkst, bez from kundkst /*where kundnr = :block.kundnr*/ " +
						                               "where kundnr = :BLOCK.KUNDNR and aktiv = 'J' order by 1) WHERE 1=2)", 
				                                       hmpConditions, null, true));
		
		//replacement at the end
		Assert.assertEquals("select * from dual where nr = ?", dba.replaceNamedParameters("select * from dual where nr = :BLOCK.KUNDNR", hmpConditions, null, true));

		//no replacements
		Assert.assertEquals("select * from dual where nr = 1", dba.replaceNamedParameters("select * from dual where nr = 1", hmpConditions, null, true));
		
		Assert.assertEquals("select * from dual where nr = 1 and nr2 = ? and nr3 = ? and nr4 = ? and nr5 = BLOCK.KUNDNR", 
				            dba.replaceNamedParameters("select * from dual where nr = 1 and nr2 = :BLOCK.KUNDNR and nr3 = :BLOCK.A and nr4 = :BLOCK.KUNDNR and nr5 = BLOCK.KUNDNR", 
				            		                   hmpConditions, null, true));
	}
	
	/**
	 * Tests the {@link DBAccess#findAndCreateReducedCondition(ICondition, Set, Map)} function.
	 */
	@Test
	public void testFindAndCreateReducedCondition()
	{
		Assert.assertNull(Filter.findAndCreateReducedCondition(null, new HashSet<String>(), new HashMap<String, CompareCondition>(), true));
		
		// The following variables will be reused during testing.
		ICondition condition = null;
		ICondition reducedCondition = null;
		Set<String> names = new HashSet<String>();
		Map<String, CompareCondition> nameToCondition = new HashMap<String, CompareCondition>();
		
		// Simple testing, one Equals.
		condition = new Equals("NAME", null);
		names.clear();
		nameToCondition.clear();
		reducedCondition = Filter.findAndCreateReducedCondition(condition, names, nameToCondition, true);
		Assert.assertSame(condition, reducedCondition);
		Assert.assertTrue(nameToCondition.isEmpty());
		
		names.add("NAME");
		reducedCondition = Filter.findAndCreateReducedCondition(condition, names, nameToCondition, true);
		Assert.assertNull(reducedCondition);
		Assert.assertSame(condition, nameToCondition.get("NAME"));
		
		// Simple testing, one Equals inside a Not.
		condition = new Not(new Equals("NAME", null));
		names.clear();
		nameToCondition.clear();
		reducedCondition = Filter.findAndCreateReducedCondition(condition, names, nameToCondition, true);
		Assert.assertTrue(reducedCondition instanceof Not);
		Assert.assertSame(((Not)condition).getCondition(), ((Not)reducedCondition).getCondition());
		Assert.assertTrue(nameToCondition.isEmpty());
		
		names.add("NAME");
		reducedCondition = Filter.findAndCreateReducedCondition(condition, names, nameToCondition, true);
		Assert.assertNull(reducedCondition);
		Assert.assertFalse(nameToCondition.isEmpty());
		Assert.assertSame(((Not)condition).getCondition(), nameToCondition.get("NAME"));
		
		// Two equals inside a notted and.
		condition = new Not(new And(new Equals("NAME", null), new Equals("NO_REMOVED", null)));
		names.clear();
		nameToCondition.clear();
		names.add("NAME");
		reducedCondition = Filter.findAndCreateReducedCondition(condition, names, nameToCondition, true);
		Assert.assertNotNull(reducedCondition);
		Assert.assertTrue(reducedCondition instanceof Not);
		Assert.assertTrue(((Not)reducedCondition).getCondition() instanceof Equals);
		Assert.assertSame(((And)((Not)condition).getCondition()).getConditions()[1], ((Not)reducedCondition).getCondition());
		Assert.assertFalse(nameToCondition.isEmpty());
		Assert.assertSame(((And)((Not)condition).getCondition()).getConditions()[0], nameToCondition.get("NAME"));
		
		// Ands
		condition = new And(new Equals("NAME", null), new Equals("NO_REMOVED", null));
		names.clear();
		nameToCondition.clear();
		reducedCondition = Filter.findAndCreateReducedCondition(condition, names, nameToCondition, true);
		Assert.assertTrue(reducedCondition instanceof And);
		Assert.assertSame(((And)condition).getConditions()[0], ((And)reducedCondition).getConditions()[0]);
		Assert.assertSame(((And)condition).getConditions()[1], ((And)reducedCondition).getConditions()[1]);
		
		// Ors
		condition = new Or(new Equals("NAME", null), new Equals("NO_REMOVED", null));
		names.clear();
		nameToCondition.clear();
		reducedCondition = Filter.findAndCreateReducedCondition(condition, names, nameToCondition, true);
		Assert.assertTrue(reducedCondition instanceof Or);
		Assert.assertSame(((Or)condition).getConditions()[0], ((Or)reducedCondition).getConditions()[0]);
		Assert.assertSame(((Or)condition).getConditions()[1], ((Or)reducedCondition).getConditions()[1]);
		
		// Ands with removal.
		condition = new And(new Equals("name_MiXed", null));
		names.clear();
		nameToCondition.clear();
		names.add("name_MiXed");
		reducedCondition = Filter.findAndCreateReducedCondition(condition, names, nameToCondition, false);
		Assert.assertNull(reducedCondition);
		
		// Ands with many.
		condition = new And(new Equals("NAME", null), new Equals("NO_REMOVED", null));
		names.clear();
		nameToCondition.clear();
		names.add("NAME");
		reducedCondition = Filter.findAndCreateReducedCondition(condition, names, nameToCondition, true);
		Assert.assertSame(((And)condition).getConditions()[1], reducedCondition);
		
		// Case insensitivity
		condition = new Equals("naMe", null);
		names.clear();
		nameToCondition.clear();
		names.add("NAME");
		reducedCondition = Filter.findAndCreateReducedCondition(condition, names, nameToCondition, true);
		Assert.assertNull(reducedCondition);
		Assert.assertSame(condition, nameToCondition.get("NAME"));
		
        // Case insensitivity
        condition = new Equals("naMe", null);
        names.clear();
        nameToCondition.clear();
        names.add("Name");
        reducedCondition = Filter.findAndCreateReducedCondition(condition, names, nameToCondition, true);
        Assert.assertNull(reducedCondition);
        Assert.assertSame(condition, nameToCondition.get("Name"));
        
		// Complex names
		condition = new Equals("prefix$name.value", null);
		names.clear();
		nameToCondition.clear();
		names.add("PREFIX$NAME.VALUE");
		reducedCondition = Filter.findAndCreateReducedCondition(condition, names, nameToCondition, true);
		Assert.assertNull(reducedCondition);
		Assert.assertSame(condition, nameToCondition.get("PREFIX$NAME.VALUE"));
	}

	/**
	 * Tests the statement generation of {@link DBAccess}.
	 * 
	 * @throws Exception if the test fails.
	 */
	@Test
	public void testStatementGeneration() throws Exception
	{
		MetaDataExposingStorage dbs = new MetaDataExposingStorage();
		dbs.setDBAccess(dba);
		dbs.setWritebackTable("TEST");
		dbs.open();
		// We can close it immediately again, because the ServerMetaData is
		// being retained.
		dbs.close();
		
		// Simple.
		ParameterizedStatement statement = dba.getParameterizedSelectStatement(
				dbs.getServerMetaData(),
				null,
				new String[] { "column_a", "column_b", "column_c", "column_d", "column_e" },
				"test",
				null,
				null,
				null,
				null,
				null,
				null,
				0,
				-1);
		Assert.assertEquals(
				"SELECT column_a,\n"
				+ "       column_b,\n"
				+ "       column_c,\n"
				+ "       column_d,\n"
				+ "       column_e\n"
				+ "  FROM test\n", statement.getStatement());
		
		// Before query columns.
		statement = dba.getParameterizedSelectStatement(
				dbs.getServerMetaData(),
				"DISTINCT",
				new String[] { "column_a", "column_b", "column_c", "column_d", "column_e" },
				"test",
				null,
				null,
				null,
				null,
				null,
				null,
				0,
				-1);
		Assert.assertEquals(
				"SELECT DISTINCT\n"
				+ "       column_a,\n"
				+ "       column_b,\n"
				+ "       column_c,\n"
				+ "       column_d,\n"
				+ "       column_e\n"
				+ "  FROM test\n", statement.getStatement());
		
		// With filter.
		statement = dba.getParameterizedSelectStatement(
				dbs.getServerMetaData(),
				null,
				new String[] {  "column_a", "column_b", "column_c", "column_d", "column_e" },
				"test",
				new Equals("column_b", "value=b").and(new Equals("column_c", "value-c")).or(new Equals("column_d", "value-d")),
				null,
				null,
				null,
				null,
				null,
				0,
				-1);
		Assert.assertEquals(
				"SELECT column_a,\n"
				+ "       column_b,\n"
				+ "       column_c,\n"
				+ "       column_d,\n"
				+ "       column_e\n"
				+ "  FROM test\n"
				+ " WHERE (column_b = ? AND column_c = ?) OR column_d = ?\n", statement.getStatement());
		
		// With where clause.
		statement = dba.getParameterizedSelectStatement(
				dbs.getServerMetaData(),
				null,
				new String[] {  "column_a", "column_b", "column_c", "column_d", "column_e" },
				"test",
				null,
				"column_e = 0",
				null,
				null,
				null,
				null,
				0,
				-1);
		Assert.assertEquals(
				"SELECT column_a,\n"
				+ "       column_b,\n"
				+ "       column_c,\n"
				+ "       column_d,\n"
				+ "       column_e\n"
				+ "  FROM test\n"
				+ " WHERE column_e = 0 \n", statement.getStatement());
		
		// With filter and where clause
		statement = dba.getParameterizedSelectStatement(
				dbs.getServerMetaData(),
				null,
				new String[] {  "column_a", "column_b", "column_c", "column_d", "column_e" },
				"test",
				new Equals("column_b", "value=b").and(new Equals("column_c", "value-c")).or(new Equals("column_d", "value-d")),
				"column_e = 0",
				null,
				null,
				null,
				null,
				0,
				-1);
		Assert.assertEquals(
				"SELECT column_a,\n"
				+ "       column_b,\n"
				+ "       column_c,\n"
				+ "       column_d,\n"
				+ "       column_e\n"
				+ "  FROM test\n"
				+ " WHERE ((column_b = ? AND column_c = ?) OR column_d = ?) AND (column_e = 0) \n", statement.getStatement());
		
		// With everything
		statement = dba.getParameterizedSelectStatement(
				dbs.getServerMetaData(),
				"DISTINCT",
				new String[] {  "column_a", "column_b", "column_c", "column_d", "column_e" },
				"test",
				new Equals("column_b", "value=b").and(new Equals("column_c", "value-c")).or(new Equals("column_d", "value-d")),
				"column_e = 0",
				"and specialcondition = 1",
				new SortDefinition(new String [] { "column_f", "column_g" }, new boolean[] { true, false }),
				null,
				null,
				0,
				-1);
		Assert.assertEquals(
				"SELECT DISTINCT\n"
				+ "       column_a,\n"
				+ "       column_b,\n"
				+ "       column_c,\n"
				+ "       column_d,\n"
				+ "       column_e\n"
				+ "  FROM test\n"
				+ " WHERE ((column_b = ? AND column_c = ?) OR column_d = ?) AND (column_e = 0) \n"
				+ "and specialcondition = 1\n"
				+ "ORDER BY column_f ASC, column_g DESC\n", statement.getStatement());
	}
	
	/**
	 * Tests if the DBAccess stays in a usable state even when a call to
	 * a function has failed.
	 * 
	 * @throws Exception when the test fails.
	 */
	@Test
	public void testRecoveryAfterFailedFunctionCallTicket1870() throws Exception
	{
		if (!dba.isUseSavepoints())
		{
			return;
		}
		
		prepareTestTables();
		
		// Sanity check
		dba.executeQuery("select * from test_unquoted_lowercase");
		
		try
		{
			dba.executeFunction("NON_EXISTING", Types.CHAR);
			
			Assert.fail("The previous function call should have produced an exception, but it did not.");
		}
		catch (Exception e)
		{
			// The exception is expected behavior, we can ignore it.
		}
		
		// This statement should work again.
		dba.executeQuery("select * from test_unquoted_lowercase");
		
		// Now try the same with autocommit disabled.
		
		dba.setAutoCommit(false);
		
		try
		{
			dba.executeFunction("NON_EXISTING", Types.CHAR);
			
			Assert.fail("The previous function call should have produced an exception, but it did not.");
		}
		catch (Exception e)
		{
			// The exception is expected behavior, we can ignore it.
		}
		
		// This statement should work again.
		dba.executeQuery("select * from test_unquoted_lowercase");
		
		dba.commit();
	}
	
	/**
	 * Tests if the DBAccess stays in a usable state even when the last
	 * statement in a transaction as broken. 
	 * 
	 * @throws Exception when the test fails.
	 */
	@Test
	public void testRecoveryAfterFailedStatementTicket1870() throws Exception
	{
		if (!dba.isUseSavepoints())
		{
			return;
		}

		prepareTestTables();
		
		// Sanity check
		dba.executeQuery("select * from test_unquoted_lowercase");
		
		try
		{
			dba.executeQuery("selebrokenct * from test_unquoted_lowercase");
			
			Assert.fail("The previous SQL statement should have produced an exception, but it did not.");
		}
		catch (SQLException e)
		{
			// The exception is expected behavior, we can ignore it.
		}
		
		// This statement should work again.
		dba.executeQuery("select * from test_unquoted_lowercase");
		
		// Now try the same with autocommit disabled.
		
		dba.setAutoCommit(false);
		
		try
		{
			dba.executeQuery("selebrokenct * from test_unquoted_lowercase");
			
			Assert.fail("The previous SQL statement should have produced an exception, but it did not.");
		}
		catch (SQLException e)
		{
			// The exception is expected behavior, we can ignore it.
		}
		
		// This statement should work again.
		dba.executeQuery("select * from test_unquoted_lowercase");
		
		dba.commit();
	}
	
	/**
	 * Tests if the DBAccess behaves correctly (in regards to data integrity)
	 * if a statement fails.
	 * 
	 * @throws Exception when the test fails.
	 */
	@Test
	public void testRecoveryAfterFailedStatementTicket1870DataSanityCheck1() throws Exception
	{
		if (!dba.isUseSavepoints())
		{
			return;
		}

		prepareTestTables();
		
		dba.setAutoCommit(false);
		
		DBStorage storage = new DBStorage();
		storage.setDBAccess(dba);
		storage.setWritebackTable("test_unquoted_lowercase");
		storage.open();
		
		// Sanity check if the table is actually empty.
		Assert.assertTrue(storage.fetchBean(null, null, 0, -1).isEmpty());
		
		IBean row = storage.createEmptyBean();
		row.put("ID", new BigDecimal("1"));
		row.put("NAME", "Test1");
		
		storage.insert(row);
		
		row.put("ID", "FAIL");
		row.put("NAME", "Test2");
		
		try
		{
			storage.insert(row);
			
			Assert.fail("The insert of invalid data should have failed, but did not.");
		}
		catch (DataSourceException e)
		{
			// The exception is expected behavior, we can ignore it.
		}
		
		row.put("ID", new BigDecimal("3"));
		row.put("NAME", "Test3");
		
		storage.insert(row);
		
		dba.commit();
		
		List<IBean> rows = storage.fetchBean(null, new SortDefinition("ID"), 0, -1);
		
		Assert.assertEquals(2, rows.size());
		Assert.assertEquals("Test1", rows.get(0).get("NAME"));
		Assert.assertEquals("Test3", rows.get(1).get("NAME"));
	}
	
	/**
	 * Tests if the DBAccess behaves correctly (in regards to data integrity)
	 * if a statement fails.
	 * 
	 * @throws Exception when the test fails.
	 */
	@Test
	public void testRecoveryAfterFailedStatementTicket1870DataSanityCheck2() throws Exception
	{
		if (!dba.isUseSavepoints())
		{
			return;
		}

		prepareTestTables();
		
		dba.setAutoCommit(false);
		
		DBStorage storage = new DBStorage();
		storage.setDBAccess(dba);
		storage.setWritebackTable("test_unquoted_lowercase");
		storage.open();
		
		// Sanity check if the table is actually empty.
		Assert.assertTrue(storage.fetchBean(null, null, 0, -1).isEmpty());
		
		IBean row = storage.createEmptyBean();
		row.put("ID", new BigDecimal("1"));
		row.put("NAME", "Test1");
		
		storage.insert(row);
		
		row.put("ID", "FAIL");
		row.put("NAME", "Test2");
		
		try
		{
			storage.insert(row);
			
			Assert.fail("The insert of invalid data should have failed, but did not.");
		}
		catch (DataSourceException e)
		{
			// The exception is expected behavior, we can ignore it.
		}
		
		dba.rollback();
		
		Assert.assertTrue(storage.fetchBean(null, null, 0, -1).isEmpty());
	}
	
	/**
	 * Tests if the DBAccess stays in a usable state after a fetch failed.
	 * 
	 * @throws Exception when the test fails.
	 */
	@Test
	public void testRecoveryAfterFailedStatementTicket1870Fetching() throws Exception
	{
		if (!dba.isUseSavepoints())
		{
			return;
		}

		prepareTestTables();
		
		dba.setAutoCommit(false);
		
		DBStorage storage = new DBStorage();
		storage.setDBAccess(dba);
		storage.setWritebackTable("test_unquoted_lowercase");
		storage.open();
		
		try
		{
			storage.fetchBean(null, new SortDefinition("GROUPASBY"), 0, -1);
			
			Assert.fail("The fetch should have failed, but did not.");
		}
		catch (DataSourceException e)
		{
			// The exception is expected behavior, we can ignore it.
		}
		
		storage.fetchBean(null, null, 0, -1);
	}

	/**
	 * Tests if there is a stackoverflow on close.
	 * 
	 * @throws Exception when the test fails.
	 */
	@Test
	public void testStackoverflowOnClose() throws Exception
	{
		dba.setAutoCommit(false);
		dba.getConnection().close();
		
		dba.close();
	}

	/**
	 * Tests fetching the PK, reported in <a href="https://oss.sibvisions.com/index.php?do=details&task_id=2057">#2057</a>.
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testFetchPKBug2057() throws Exception
	{
		prepareTestTables();
		
		DBStorage dbs = new DBStorage();
		dbs.setDBAccess(dba);
		dbs.setWritebackTable("TEST_PK_FETCH");
		dbs.open();
		
		Bean bn = new Bean();
		bn.put("SOME_VALUE", "PK1");
		
		bn = dbs.insert(bn);
		
		Assert.assertEquals("PK1", bn.get("SOME_VALUE"));
		Assert.assertNotNull(bn.get("CREATED_AT"));
	}
	
	/**
	 * Tests is connection alive query.
	 */
	@Test
	public void testIsAlive()
	{
		Assert.assertTrue("DBAccess is not alive!", dba.isAlive());
	}
	
    /**
	 * A small utility function for asserting behavior of the {@link DBAccess#findNamedParameters(String, List)} function.
	 * 
	 * @param pValue the value to parse.
	 * @param pExpectedValues the expected values.
	 */
	private void assertNamedParameters(String pValue, String... pExpectedValues)
	{
		List<String> namedParameters = dba.findNamedParameters(pValue);
		
		Assert.assertArrayEquals(pExpectedValues, namedParameters.toArray(new String[namedParameters.size()]));
	}
	
	/**
	 * Test of LikeIgnoreCase with numeric column (<a href="https://oss.sibvisions.com/index.php?do=details&task_id=2128">HSQLDB fulltext filter error</a>).
	 * 
	 * @throws Exception Condition is wrong.
	 */
	@Test
	public void testLikeIgnoreCaseWithNumber() throws Exception
	{
		DBStorage storage = new DBStorage();
		storage.setFromClause("ADRESSEN");
		storage.setDBAccess(dba2);
		storage.open();	
		
		ICondition cond = new LikeIgnoreCase("ID", "Müller");
		
		storage.fetch(cond, null, 0, -1);
	}

    /**
     * Test of LikeIgnoreCase with numeric column (<a href="https://oss.sibvisions.com/index.php?do=details&task_id=2128">HSQLDB fulltext filter error</a>).
     * 
     * @throws Exception Condition is wrong.
     */
    @Test
    public void testClearMetaDataWithApplicationNameTicket2588() throws Exception
    {
        dba.setMetaDataCacheOption(MetaDataCacheOption.On);
        
        DBAccess.clearMetaData(""); // remove application name - identifier reference

        DBStorage dbs = new DBStorage();
        dbs.setDBAccess(dba);
        dbs.setWritebackTable("TEST");
        dbs.open();
        
        Assert.assertTrue("MetaData is cached, but is not", DBAccess.getColumnMetaDataCache(dba.getIdentifier()) != null);

        DBAccess.clearMetaData("");
        
        Assert.assertTrue("MetaData should be cleared, but is not", DBAccess.getColumnMetaDataCache(dba.getIdentifier()) == null);

        DBAccess.clearMetaData();
        dba.setMetaDataCacheOption(MetaDataCacheOption.Default);
    }
	
    /**
     * Tests the getEstimatedRowCount function.
     */
    @Test
    public void testGetEstimatedRowCount() throws Exception
    {
        DBStorage dbs = new DBStorage();
        dbs.setDBAccess(dba);
        dbs.setWritebackTable("TEST");
        dbs.open();
        
        System.out.println(dbs.getEstimatedRowCount(null));
    }

    /**
     * Tests columns with special characters like space or #.
     */
    @Test
    public void testColumnNameWithSpecialChar() throws Exception
    {
        prepareTestTables();
        
        DBStorage dbs = new DBStorage();
        dbs.setDBAccess(dba);
        dbs.setWritebackTable("TEST_COLUMN_SPECIALCHAR");
        dbs.open();
        
        IBean row = dbs.createEmptyBean();
        row.put("ID", 15);
        row.put("FIRST NAME", "Hallo");
        row.put("LAST#NAME", "Martin");
        
        dbs.insert(row);
        
        IBean newRow = row.clone();
        newRow.put("FIRST NAME", "Hallo2");
        newRow.put("LAST#NAME", "Martin2");
        
        dbs.update(row, newRow);

        
        List<IBean> result = dbs.fetchBean(null, null, 0, -1);

//        System.out.println(result);
        
        Assert.assertEquals("Hallo2", result.get(0).get("FIRST NAME"));
        Assert.assertEquals("Martin2", result.get(0).get("LAST#NAME"));
    }

    /**
     * Tests removeQuotes, translateQuotes and toIdentifier.
     */
    @Test
    public void testRemoveTranslateQuotesAndToIdentifer() throws Exception
    {
        DBAccess dbx = new DBAccess();
        dbx.setQuoteCharacters("[", "]");
        
        Assert.assertEquals("Hallo`Martin",  DBAccess.removeQuotes("`Hallo\\`Martin`"));
        Assert.assertEquals("Oida Hallo`Martin wie ` gehts?",  DBAccess.removeQuotes("Oida `Hallo\\`Martin` wie \\` gehts?"));
        
        Assert.assertEquals("[Hallo`Martin]",  dbx.translateQuotes("`Hallo\\`Martin`"));
        Assert.assertEquals("Oida [Hallo`Martin] wie ` gehts?",  dbx.translateQuotes("Oida `Hallo\\`Martin` wie \\` gehts?"));
        
        Assert.assertEquals("Hallo`Martin",  DBAccess.toIdentifier("`Hallo\\`Martin`", true));
        Assert.assertEquals("OIDAHallo`MartinWIE`GEHTS?",  DBAccess.toIdentifier("Oida `Hallo\\`Martin` wie \\` gehts?", true));
    }

    /**
     * Tests translate to default value.
     */
    @Test
    public void testTranslateDefaultValue() throws Exception
    {
        DBAccess db = new DBAccess();
        Assert.assertEquals(DateUtil.getTimestamp(9999, 12, 31), db.translateDefaultValue("TEST", TimestampDataType.TYPE_IDENTIFIER, "('9999-12-31')"));
        Assert.assertEquals("getdate()", db.translateDefaultValue("TEST", TimestampDataType.TYPE_IDENTIFIER, "(getdate())"));
        Assert.assertEquals(DateUtil.getTimestamp(2010, 1, 1, 12, 0, 0), 
                db.translateDefaultValue("TEST", TimestampDataType.TYPE_IDENTIFIER, "'2010-01-01 12:00:00'::timestamp without time zone"));
        Assert.assertEquals("PS265 (RSA)", db.translateDefaultValue("TEST", StringDataType.TYPE_IDENTIFIER, "('PS265 (RSA)')"));
        Assert.assertEquals("TEXT Test", db.translateDefaultValue("TEST", StringDataType.TYPE_IDENTIFIER, "'TEXT Test'::character varying"));
        Assert.assertEquals(BigDecimal.valueOf(30), db.translateDefaultValue("TEST", BigDecimalDataType.TYPE_IDENTIFIER, "((30))"));
        Assert.assertEquals(BigDecimal.valueOf(15), db.translateDefaultValue("TEST", BigDecimalDataType.TYPE_IDENTIFIER, "15"));
    }
    
    
	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * The {@link MetaDataExposingStorage} which is an extension of
	 * {@link DBStorage} and exposes the {@link ServerMetaData}.
	 * 
	 * @author Robert Zenz
	 */
	private static final class MetaDataExposingStorage extends DBStorage
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/** The {@link ServerMetaData}. */
		private ServerMetaData serverMetaData = null;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of {@link MetaDataExposingStorage}.
		 */
		public MetaDataExposingStorage()
		{
			super();
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Overwritten methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		protected ServerMetaData createMetaData(
				String pBeforeQueryColumns,
				String[] pQueryColumns,
				String pFromClause,
				String pWhereClause,
				String pAfterWhereClause,
				String pWritebackTable,
				String[] pWritebackColumns,
				boolean pAutoLinkReference,
				boolean pUseRepresentationColumnsAsQueryColumns) throws DataSourceException
		{
			serverMetaData = super.createMetaData(
					pBeforeQueryColumns,
					pQueryColumns,
					pFromClause,
					pWhereClause,
					pAfterWhereClause,
					pWritebackTable,
					pWritebackColumns,
					pAutoLinkReference,
					pUseRepresentationColumnsAsQueryColumns);
			
			return serverMetaData;
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// User-defined methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Gets the {@link ServerMetaData}.
		 *
		 * @return the {@link ServerMetaData}.
		 */
		public ServerMetaData getServerMetaData()
		{
			return serverMetaData;
		}
		
	}	// MetaDataExposingStorage
	
} 	// TestDBAccess
