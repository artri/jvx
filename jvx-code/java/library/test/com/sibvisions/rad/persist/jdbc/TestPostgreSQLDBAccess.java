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
 * 05.06.2011 - [JR] - test case for #381
 */
package com.sibvisions.rad.persist.jdbc;


import java.math.BigDecimal;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;

import javax.rad.model.condition.Equals;
import javax.rad.model.condition.ICondition;
import javax.rad.model.condition.LikeIgnoreCase;
import javax.rad.persist.DataSourceException;
import javax.rad.remote.MasterConnection;
import javax.rad.type.bean.Bean;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.postgresql.util.PGobject;

import com.sibvisions.rad.model.remote.RemoteDataBook;
import com.sibvisions.rad.model.remote.RemoteDataSource;
import com.sibvisions.rad.persist.jdbc.param.InOutParam;
import com.sibvisions.rad.persist.jdbc.param.InParam;
import com.sibvisions.rad.persist.jdbc.param.OutParam;
import com.sibvisions.rad.util.DirectObjectConnection;
import com.sibvisions.util.type.CommonUtil;
import com.sibvisions.util.type.ExceptionUtil;
import com.sibvisions.util.type.StringUtil;

/**
 * Tests all Functions of {@link PostgreSQLDBAccess}.
 * 
 * @author Roland Hörmann
 * @see com.sibvisions.rad.persist.jdbc.PostgreSQLDBAccess
 */
public class TestPostgreSQLDBAccess extends TestDBAccess
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** 
	 * Connect to the Test Database and create a Test Table.
	 * 
	 * @throws Exception if the connect or the create table fails
	 */
	@Before
	@Override
	public void open() throws Exception
	{
		checkConnectionError();
		
		DriverManager.setLoginTimeout(8);

		dba = createDBAccess();
		
		// set connect properties		
		dba.setUrl(getJDBCScheme() + "//" + getHostName() + "/testdb");
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
		dba2 = createDBAccess();
		dba2.setUrl(getJDBCScheme() + "//" + getHostName() + "/testdb");
		dba2.setUsername("test");
		dba2.setPassword("test");
		
		dba2.open();
		
		/* 
		dba2.getConnection().setAutoCommit(false);
		
    	psPreparedStatement = dba2.getPreparedStatement("delete from adressen", false);
    	dba2.executeUpdate(psPreparedStatement);

		//Adressen befüllen
    	for (int i = 1; i <= 100000; i++)
    	{
			psPreparedStatement = dba2.getPreparedStatement(
					"insert into adressen (post_id, stra_id, hausnummer, stiege, tuernummer) " +
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
	 * Creates test tables for new tests.
	 * 
	 * @throws Exception
	 *             if the connect or the create table fails
	 */
    @Override
	protected void createTestTables() throws Exception
	{
		dba.executeStatement("create table test_unquoted_lowercase (id numeric(18) NOT NULL, name varchar(100), CONSTRAINT tuql_pk PRIMARY KEY (id))");

		dba.executeStatement("create table TEST_UNQUOTED_UPPERCASE (ID numeric(18) NOT NULL, NAME varchar(100), CONSTRAINT tuqu_pk PRIMARY KEY (ID))");

		dba.executeStatement("create table `Test_Quoted` (`Id` numeric(18) NOT NULL, `Name` varchar(100), CONSTRAINT tq_pk PRIMARY KEY (`Id`))");

		dba.executeStatement("create table TEST_PK_FETCH (SOME_VALUE varchar(32) not null, CREATED_AT timestamp not null default CURRENT_TIMESTAMP, " +
		                     "CONSTRAINT tpkf_pk primary key (SOME_VALUE))");
		
		dba.executeStatement("create table TEST_TIMESTAMP (SOME_VALUE varchar(32) not null, CREATED_AT timestamp not null default CURRENT_TIMESTAMP, "
                + "CREATED2_AT timestamp(6) not null default CURRENT_TIMESTAMP,"
                + "CREATED3_AT timestamp(6) not null default CURRENT_TIMESTAMP(6), CONSTRAINT tt_pk primary key (SOME_VALUE))");
		
        dba.executeStatement("create table TEST_COLUMN_SPECIALCHAR (ID integer not null, \"FIRST NAME\" varchar(100), \"LAST#NAME\" varchar(100), "
                + "constraint TESC_PK primary key (ID))");

        dba.executeStatement("CREATE SEQUENCE TEST_ALL_SEQ INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 CYCLE");

		dba.executeStatement("CREATE FUNCTION tuql_trigger() RETURNS trigger AS $tuql_trigger$\n"
				+ "BEGIN\n"
				+ "if (NEW.id is null) then\n"
				+ "NEW.id := nextval('TEST_ALL_SEQ');\n"
				+ "end if;\n"
				+ "RETURN NEW;\n"
				+ "END\n"
				+ "$tuql_trigger$ LANGUAGE plpgsql");
		
		dba.executeStatement("CREATE TRIGGER tuql_trigger BEFORE INSERT ON test_unquoted_lowercase\n"
				+ "FOR EACH ROW EXECUTE PROCEDURE tuql_trigger()");

		dba.executeStatement("CREATE FUNCTION tuqu_trigger() RETURNS trigger AS $tuqu_trigger$\n"
				+ "BEGIN\n"
				+ "if (NEW.id is null) then\n"
				+ "NEW.id := nextval('TEST_ALL_SEQ');\n"
				+ "end if;\n"
				+ "RETURN NEW;\n"
				+ "END\n"
				+ "$tuqu_trigger$ LANGUAGE plpgsql");
		
		dba.executeStatement("CREATE TRIGGER tuqu_trigger BEFORE INSERT ON test_unquoted_uppercase\n"
				+ "FOR EACH ROW EXECUTE PROCEDURE tuqu_trigger()");

		dba.executeStatement("CREATE FUNCTION tq_trigger() RETURNS trigger AS $tq_trigger$\n"
				+ "BEGIN\n"
				+ "if (NEW.`Id` is null) then\n"
				+ "NEW.`Id` := nextval('TEST_ALL_SEQ');\n"
				+ "end if;\n"
				+ "RETURN NEW;\n"
				+ "END\n"
				+ "$tq_trigger$ LANGUAGE plpgsql");
		
		dba.executeStatement("CREATE TRIGGER tq_trigger BEFORE INSERT ON `Test_Quoted`\n"
				+ "FOR EACH ROW EXECUTE PROCEDURE tq_trigger()");
	}
	
	/** 
	 * Drops test tables for new tests.
	 */
    @Override
	protected void dropTestTables()
	{
		super.dropTestTables();
		
		try
		{
			dba.executeStatement("drop sequence TEST_ALL_SEQ");
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		try
		{
			dba.executeStatement("DROP FUNCTION tuql_trigger()");
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		try
		{
			dba.executeStatement("DROP FUNCTION tuqu_trigger()");
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		try
		{
			dba.executeStatement("DROP FUNCTION tq_trigger()");
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}   
    
    /**
     * Test of LikeIgnoreCase.
     * 
     * @throws Exception Condition is wrong.
     */
    @Test
    @Override
    public void testLikeIgnoreCase() throws Exception
    {
        DBStorage storage = new DBStorage();
        storage.setDBAccess(dba);
            
        ServerMetaData smd = storage.createMetaData(null, null, null, null, null, "TEST", null, false, false);  
        
        ICondition cond = new LikeIgnoreCase("NAME", "hand*");
        
        Assert.assertEquals("upper(m.name) like upper(?)", dba.getSQL(smd, cond, true).toLowerCase().replace("`", ""));
        Assert.assertArrayEquals(new Object[] {"hand%"}, dba.getParameter(cond));
    }       
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

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
     * Gets the JDBC scheme.
     * 
     * @return the scheme
     */
    protected String getJDBCScheme()
    {
    	return "jdbc:postgresql:";
    }
    
    /**
     * Creates the database access object.
     * 
     * @return the database access object
     */
    protected PostgreSQLDBAccess createDBAccess()
    {
    	return new PostgreSQLDBAccess();    	
    }
    
    /**
     * Gets whether update with an alias is supported.
     * 
     * @return <code>true</code> if supported
     */
    protected boolean isUpdateWithAliasSupported()
    {
    	return false;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Test methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Tests insert data with lowercase column names.
     * 
     * @throws Throwable if test fails
     */
    @Test
    public void testBug381() throws Throwable
    {
        DBStorage dbs = new DBStorage();
        dbs.setDBAccess(dba);
        dbs.setFromClause("users");
        dbs.setWritebackTable("users");
        dbs.open();

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
        
        rdb.deleteAllRows();
        
        rdb.insert(false);
        rdb.setValue("FIRSTNAME", "JVx");
        rdb.saveSelectedRow();
    }
    
    /**
     * Tests table functions with dynamic parameters.
     * 
     * @throws Throwable if test fails
     */
    @Test
    public void testTableFunction() throws Throwable
    {
        DBStorage dbs = new DBStorage();
        dbs.setDBAccess(dba);
        dbs.setFromClause("getVIP(0, 0)");
        dbs.open();
        dbs.setFromClause("getVIP(:FROM, :TO)");

        DirectObjectConnection con = new DirectObjectConnection();
        con.put("dbs", dbs);
        
        MasterConnection macon = new MasterConnection(con);
        macon.open();
        
        RemoteDataSource rds = new RemoteDataSource(macon);
        rds.open();
        
        RemoteDataBook rdb = new RemoteDataBook();
        rdb.setDataSource(rds);
        rdb.setName("dbs");
        rdb.setFilter(new Equals("FROM", BigDecimal.valueOf(300)).and(new Equals("TO", BigDecimal.valueOf(1000))));
        rdb.open();
        
        rdb.fetchAll();
        
        Assert.assertEquals(2, rdb.getRowCount());
    }
    
    /**
     * Tests alias in set clause of update statement.
     * 
     * @throws Throwable if test fails
     * @see <a href="https://oss.sibvisions.com/index.php?do=details&task_id=1887">#1887</a>
     */
    @Test
    public void testUpdateWithAlias1887() throws Throwable
    {
        DBStorage dbs1 = new DBStorage();
        dbs1.setDBAccess(dba);
        dbs1.setFromClause("person");
        dbs1.setWritebackTable("person");
        dbs1.open();

        DBStorage dbs = new DBStorage();
        dbs.setDBAccess(dba);
        dbs.setFromClause("users");
        dbs.setWritebackTable("users");
        dbs.open();

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
        
        rdb.fetchAll();
        rdb.deleteAllRows();
        rdb.saveAllRows();
        
        rdb.insert(false);
        rdb.setValue("FIRSTNAME", "#1887");
        rdb.saveSelectedRow();
        
        try
        {
            dba.executeSql("update users u set u.firstname = 'fixed' where u.firstname = ?", "#1887");

            if (!isUpdateWithAliasSupported())
            {
            	Assert.fail("Alias shouldn't work in set clause of update statement!");
            }
        }
        catch (Exception e)
        {
            Assert.assertTrue(e.getMessage().indexOf("column \"u\" of relation \"users\" does not exist") >= 0);            
        }

        try
        {
            dba.executeSql("update users u set firstname = 'fixed' where u.firstname = ?", "#1887");
        }
        catch (Exception e)
        {
            Assert.fail("Alias should work in where clause of update statement!");
        }
        
        rdb.reload();
        
        rdb.setSelectedRow(0);
        Assert.assertEquals("fixed", rdb.getValue("FIRSTNAME"));
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
		List<Bean> keys = dba.executeInsertStatement("insert into TEST ( \"NAME\" ) values ('Hallo')", new String[] {"ID"});
		
		Assert.assertTrue(keys.size() == 1 && keys.get(0).get("ID") != null);
	}
	
    /**
     * Tests refetch of PK when inserting a new row. The write back table contains
     * the schema name.
     * 
     * @throws Exception if the test fails
     */
    @Test
    public void testResultSetColumnAccess() throws Exception
    {
        dba.executeStatement("delete from test_unquoted_lowercase");
        dba.executeStatement("insert into test_unquoted_lowercase (id, name) values (1, 'Name 1')");
        
        Statement stat = dba.getConnection().createStatement();
        
        ResultSet res = stat.executeQuery("select * from test_unquoted_lowercase");
        
        while (res.next())
        {
            for (int i = 1, count = res.getMetaData().getColumnCount(); i <= count; i++)
            {
                String columnName = res.getMetaData().getColumnName(i);
                System.out.println(columnName + "  " + res.getObject(columnName.toLowerCase()) + "  " + res.getObject(columnName.toUpperCase()));
            }
        }
        
        CommonUtil.close(res, stat);

        dba.executeStatement("delete from `Test_Quoted`");
        dba.executeStatement("insert into `Test_Quoted` (`Id`, `Name`) values (1, 'Name 1')");
        
        stat = dba.getConnection().createStatement();
        
        res = stat.executeQuery("select * from \"Test_Quoted\"");
        
        while (res.next())
        {
            for (int i = 1, count = res.getMetaData().getColumnCount(); i <= count; i++)
            {
                String columnName = res.getMetaData().getColumnName(i);
                System.out.println(columnName + "  " + res.getObject(columnName.toLowerCase()) + "  " + res.getObject(columnName.toUpperCase()));
            }
        }
        
        CommonUtil.close(res, stat);
    }
    
	/**
	 * Tests refetch of PK when inserting a new row. The write back table contains
	 * the schema name.
	 * 
	 * @throws Exception if the test fails
	 */
	@Test
	public void testparseConstraints() throws Exception
	{
		PostgreSQLDBAccess a = createDBAccess();
		
		Assert.assertEquals("ACTIVE", 		 a.parseColumnName("(((active)::text = ANY ((ARRAY['Y'::character varying, 'N'::character varying])::text[])))"));
		Assert.assertEquals("DB",			 a.parseColumnName("(((db)::text = ANY (ARRAY[('J'::character varying)::text, ('N'::character varying)::text])))"));
		Assert.assertEquals("TODO_ANZEIGEN", a.parseColumnName("(((todo_anzeigen)::text = ANY ((ARRAY['J'::character varying, 'N'::character varying])::text[])))"));
		Assert.assertEquals("INTERN", 		 a.parseColumnName("intern = ANY (ARRAY['J'::bpchar, 'N'::bpchar])"));
		Assert.assertEquals("['Y', 'N']", StringUtil.toString(a.parseValues("(((active)::text = ANY ((ARRAY['Y'::character varying, 'N'::character varying])::text[])))")));
		Assert.assertEquals("['J', 'N']", StringUtil.toString(a.parseValues("(((db)::text = ANY (ARRAY[('J'::character varying)::text, ('N'::character varying)::text])))")));
		Assert.assertEquals("['J', 'N']", StringUtil.toString(a.parseValues("(((todo_anzeigen)::text = ANY ((ARRAY['J'::character varying, 'N'::character varying])::text[])))")));
		Assert.assertEquals("['J', 'N']", StringUtil.toString(a.parseValues("intern = ANY (ARRAY['J'::bpchar, 'N'::bpchar])")));
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
        dbs.setFromClause("test_timestamp");
        dbs.setWritebackTable("test_timestamp");
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
        Assert.assertNotNull("CREATED2_AT not set!", rdbTest.getValue("CREATED2_AT"));
        Assert.assertNotNull("CREATED3_AT not set!", rdbTest.getValue("CREATED3_AT"));
    }
	
    /**
     * Test if the schema can be detected in all DBs.
     * 
     * @throws Exception if the test fails
     */
    @Test
    public void testSchemaDetectWithQuotedTable() throws Exception
    {
        TableInfo mdInfo = dba.getTableInfo("TEST");

        TableInfo mdInfoQuoted = dba.getTableInfo("`test`");

        Assert.assertArrayEquals(new Object[] {mdInfo.getCatalog(), mdInfo.getSchema(), mdInfo.getTable()}, 
                new Object[] {mdInfoQuoted.getCatalog(), mdInfoQuoted.getSchema(), mdInfoQuoted.getTable()});
    }
    
    /**
     * Test if the schema can be detected in all DBs.
     * 
     * @throws Exception if the test fails
     */
    @Test
    public void testAutoIncrementColumn() throws Exception
    {
        try
        {
            dba.executeStatement("create table test_autoincrement (id serial primary key)");
            
            Assert.assertEquals(dba.getColumnMetaData("(select * from test_autoincrement)", null, null, null, null)[0].getColumnMetaData(), 
                    dba.getColumnMetaData("test_autoincrement", null, null, null, null)[0].getColumnMetaData());
            
        }
        finally
        {
            try
            {
                dba.executeStatement("drop table test_autoincrement");
            }
            catch (Exception ex)
            {
                // Ignore
            }
        }
        
    }
    
    /**
     * Tests complex query parsing and parameter replacement.
     * 
     * @throws Exception if test fails
     */
    @Test
    public void testComplexQuery() throws Exception
    {
    	String sQuery = "WITH RECURSIVE cte AS (\r\n" + 
    			"                (         SELECT p.id AS prod_id,\r\n" + 
    			"                            p.servicename AS service,\r\n" + 
    			"                            p.servicename AS parent,\r\n" + 
    			"                            p.servicename AS subservice,\r\n" + 
    			"                            p.id AS subservice_id,\r\n" + 
    			"                            1 AS level,\r\n" + 
    			"                            p.servicename AS superstring,\r\n" + 
    			"                            p.preis_einmalig,\r\n" + 
    			"                            p.preis_jaehrlich,\r\n" + 
    			"                            p.beschreibung,\r\n" + 
    			"                            '-'::character varying AS ist_verpflichtend\r\n" + 
    			"                           FROM produkte p\r\n" + 
    			"                UNION\r\n" + 
    			"                         SELECT p2.id AS prod_id,\r\n" + 
    			"                            p2.servicename AS service,\r\n" + 
    			"                            p2.servicename AS parent,\r\n" + 
    			"                            p1.servicename AS subservice,\r\n" + 
    			"                            p1.id AS subservice_id,\r\n" + 
    			"                            2 AS level,\r\n" + 
    			"                            (p2.servicename::text || ' -> '::text) || p1.servicename::text AS superstring,\r\n" + 
    			"                            p1.preis_einmalig,\r\n" + 
    			"                            p1.preis_jaehrlich,\r\n" + 
    			"                            p1.beschreibung,\r\n" + 
    			"                            bundles.ist_verpflichtend\r\n" + 
    			"                           FROM bundles\r\n" + 
    			"                      LEFT JOIN produkte p1 ON bundles.prod_id = p1.id\r\n" + 
    			"                 LEFT JOIN produkte p2 ON bundles.prod2_id = p2.id\r\n" + 
    			"                WHERE bundles.ist_verpflichtend::text = 'Y'::text)\r\n" + 
    			"        UNION\r\n" + 
    			"                 SELECT cte_1.prod_id,\r\n" + 
    			"                    cte_1.service,\r\n" + 
    			"                    p2.servicename AS parent,\r\n" + 
    			"                    p1.servicename AS subservice,\r\n" + 
    			"                    p1.id AS subservice_id,\r\n" + 
    			"                    cte_1.level + 1,\r\n" + 
    			"                    (cte_1.superstring::text || ' -> '::text) || p1.servicename::text,\r\n" + 
    			"                    p1.preis_einmalig,\r\n" + 
    			"                    p1.preis_jaehrlich,\r\n" + 
    			"                    p1.beschreibung,\r\n" + 
    			"                    bundles.ist_verpflichtend\r\n" + 
    			"                   FROM bundles\r\n" + 
    			"              LEFT JOIN produkte p1 ON bundles.prod_id = p1.id\r\n" + 
    			"         LEFT JOIN produkte p2 ON bundles.prod2_id = p2.id\r\n" + 
    			"    JOIN cte cte_1 ON cte_1.subservice_id = p2.id AND cte_1.level < 6\r\n" + 
    			"   WHERE bundles.ist_verpflichtend::text = 'Y'::text\r\n" + 
    			"        )\r\n" + 
    			"SELECT row_number() OVER () AS id,\r\n" + 
    			"    cte.prod_id,\r\n" + 
    			"    cte.service,\r\n" + 
    			"    cte.parent,\r\n" + 
    			"    cte.subservice,\r\n" + 
    			"    cte.subservice_id,\r\n" + 
    			"    cte.level,\r\n" + 
    			"    cte.superstring,\r\n" + 
    			"    cte.preis_einmalig,\r\n" + 
    			"    cte.preis_jaehrlich,\r\n" + 
    			"    cte.beschreibung,\r\n" + 
    			"    cte.ist_verpflichtend\r\n" + 
    			"   FROM cte\r\n" + 
    			"  ORDER BY cte.level";
    	
    	PreparedStatement stmt = dba.getConnection().prepareStatement(sQuery);
    	try
    	{
    		//statement parsing should work, but metadata are not available
    		stmt.executeQuery();
    	}
    	catch (Exception e)
    	{
    		String sMessage = e.getMessage();
    		
    		if (sMessage.indexOf("relation \"produkte\" does not exist") < 0)
    		{
    			Assert.fail("Statement parsing failed!");
    		}
    	}
    	
    	DBStorage dbs = new DBStorage();
    	dbs.setDBAccess(dba);
    	dbs.setFromClause("(" + sQuery + ") m");
    	
    	try
    	{
    		dbs.open();
    	}
    	catch (DataSourceException dse)
    	{
    		Throwable thRoot = ExceptionUtil.getRootCause(dse);
    		
    		if (thRoot.getMessage().indexOf("relation \"produkte\" does not exist") < 0)
    		{
    			throw dse;
    		}
    	}
    	
    	sQuery = "select * from USERS u where u.ID = :ID_PARAM::decimal";
    	
    	dbs.setFromClause("(" + sQuery + ") m");
    	dbs.open();
    }
    
    /**
     * Tests complex query parsing and parameter replacement.
     * 
     * @throws Exception if test fails
     */
    @Test
    public void testOrderByWithQuery() throws Exception
    {
        DBStorage dbs = new DBStorage();
        dbs.setDBAccess(dba);
        dbs.setFromClause("(select users.id, 'A' as status, firstname||lastname as department from users) m");
        dbs.open();
        
        System.out.println(dbs.fetchBean(null, null, 0, 35)); // This causes an exception, if the pk columns are not sortable...
    }

    /**
     * Tests complex query parsing and parameter replacement.
     * 
     * @throws Exception if test fails
     */
    @Test
    public void testStringTypeUnspecified() throws Exception
    {
//        PostgreSQLDBAccess dba = createDBAccess();
//        
//        // set connect properties       
//        dba.setUrl(getJDBCScheme() + "//" + getHostName() + "/testdb?stringtype=unspecified");
//        dba.setUsername("test");
//        dba.setPassword("test");
//
//        dba.open();

        try
        {
            dba.executeStatement(
                    "create table testArray (" +
                    " id serial primary key," +
                    " name varchar(100)," +
                    " nummer decimal(10)," +
                    " phones text[])");
            
            dba.executeStatement(
                    "insert into testarray (name, nummer, phones)" + 
                    " values ('Lily Bush',null,'{(408)-589-5841}')," + 
                    "        ('William Gate',2,'{(408)-589-5842,(408)-589-58423}')");

            dba.executeStatement(
                    "delete from testarray");
                    
            PGobject n = new PGobject();
            n.setType("unknown");

            PGobject num = new PGobject();
            num.setType("unknown");
            num.setValue("2");
            
            PGobject array1 = new PGobject();
            array1.setType("unknown");
            array1.setValue("{(408)-589-5841}");

            PGobject array2 = new PGobject();
            array2.setType("unknown");
            array2.setValue("{(408)-589-5842,(408)-589-58423}");
            
            dba.executeStatement(
                    "insert into testarray (name, nummer, phones)" + 
                    " values (?, ?, ?)," + 
                    "        (?, ?, ?)",
                    "Lily Bush", n, new InParam(Types.OTHER, "{(408)-589-5841}"), // array1, 
                    "William Gate", num, array2);

            dba.executeStatement(
                    "delete from testarray");

            dba.executeStatement(
                    "insert into testarray (name, nummer, phones)" + 
                    " values (?, ?, ?)," + 
                    "        (?, ?, ?)",
                    "Lily Bush", null, "{(408)-589-5841}", // array1, 
                    "William Gate", "2", "{(408)-589-5842,(408)-589-58423}");
            
            System.out.println(dba.executeQuery("select * from testarray"));
        }
        finally
        {
            try
            {
                dba.executeStatement(
                        "drop table testArray");
            }
            catch (Exception ex)
            {
                // Ignore
            }
        }
    }

    /**
     * Ticket3116. Test null binding directly in sql without parameter.
     * 
     * @throws Exception if test fails
     */
    @Test
    public void testStringTypeUnspecifiedWithNullTicket3116() throws Exception
    {
        dba.executeStatement("insert into TEST ( `NAME` ) values (?)", null);

        dba.executeQuery("select 1 from TEST where `ID` = ?", null);

        List<Bean> result = dba.executeQuery("select 1 where ? is null", null);
        
//        try {
//            DriverManager.registerDriver(new org.postgresql.Driver());
//            Connection conn = DriverManager.getConnection("jdbc:postgresql://192.168.1.201:5432/testdb?stringtype=undefined", "test", "test");
//            PreparedStatement stat = conn.prepareStatement("select 1 where 1=?");                  // this does not work 
//            //stat = conn.prepareStatement("select 1 where cast(? as varchar) is null"); // this works
//            //stat = conn.prepareStatement("select 1 where coalesce(?, null) is null");  // this works
//            //stat.setString(1, null);      //does not work
//            //stat.setNull(1, Types.NULL);  //does not work
//            //stat.setNull(1, Types.OTHER); //does not work
//            stat.setNull(1, Types.VARCHAR); //does not work
//            stat.executeQuery();
//            stat.close();
//            conn.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }    
    
    }
    
    
    /**
     * Tests complex query parsing and parameter replacement.
     * 
     * @throws Exception if test fails
     */
    @Test
    public void testProcedure() throws Exception
    {
        InParam a = new InParam(Types.DECIMAL, BigDecimal.valueOf(1));

        InParam b = new InParam(Types.DECIMAL, BigDecimal.valueOf(2));

        OutParam c = new OutParam(Types.DECIMAL);
        OutParam d = new OutParam(Types.DECIMAL);

        InOutParam e = new InOutParam(Types.DECIMAL, BigDecimal.valueOf(3));
     
        Assert.assertEquals("{C=7, D=8, E=6}", dba.executeFunction("testprocedure", Types.OTHER, a, b, c, d, e).toString());
        
        Assert.assertEquals(BigDecimal.valueOf(7),  c.getValue());
        Assert.assertEquals(BigDecimal.valueOf(8),  d.getValue());
        Assert.assertEquals(BigDecimal.valueOf(6),  e.getValue());
        
        dba.executeProcedure("testprocedure", a, b, c, d, e);

        Assert.assertEquals(BigDecimal.valueOf(10),  c.getValue());
        Assert.assertEquals(BigDecimal.valueOf(11),  d.getValue());
        Assert.assertEquals(BigDecimal.valueOf(9),  e.getValue());
    }
    
    /**
     * Tests limit fetch on big table selects, as the postgres has a bug when no sort is declared.
     * 
     * @throws Exception if test fails
     */
//    @Test
//    public void testFetchWithLimit() throws Exception
//    {
//        PostgreSQLDBAccess dba2 = createDBAccess();
//        
//        // set connect properties       
//        dba2.setUrl("jdbc:postgresql://snowsat-maintain-QAS-postgresqldbserver.postgres.database.azure.com:5432/ksp@snowsat-maintain-QAS-postgresqldbserver");
//        dba2.setUsername("ksp@snowsat-maintain-QAS-postgresqldbserver");
//        dba2.setPassword("ksp");
//        dba2.setMaxTime(250);
//        dba2.open();
//
//        DBStorage storage = new DBStorage();
//        storage.setDBAccess(dba2);
//        storage.setWritebackTable("EQUIPMENTS");
////        storage.setFromClause("V_EQUIPMENTS");
////        storage.setWhereClause("status_id <> 'AA' and status_id <> 'AF' and status_id <> 'ZZ' and GELOESCHT_INTERN = 'N'");
////        storage.setAfterWhereClause("order by status_id desc limit 1000");
//        storage.open();
//    
////        LoggerFactory.getInstance(DBAccess.class).setLevel(LogLevel.ALL);
//        
//        HashSet pks = new HashSet();
//        int rows = 0;
//        List<Object[]> data;
//        long start = System.currentTimeMillis();
//        
//        do
//        {
//            data = storage.fetch(null, null, rows, 35);
//
//            System.out.println(data.size() + "  " + (System.currentTimeMillis() - start) + "ms");
//            for (Object[] dat : data)
//            {
//                if (dat != null)
//                {
//                    if (!pks.add(dat[0]))
//                    {
//                        System.out.println(StringUtil.toString(dat));
//                    }
//                    rows++;
////                    System.out.println(dat[0]);
//                }
//                else
//                {
//                    System.out.println("end");
//                }
//            }
//        }
//        while (data.get(data.size() - 1) != null);
//        
//        System.out.println("rows: " + rows + "  " + pks.size() + "  " + (rows - pks.size()) + "  " + (System.currentTimeMillis() - start) + "ms");
//        
//        CommonUtil.close(storage, dba2);
//    }

} 	// TestPostgreSQLDBAccess
