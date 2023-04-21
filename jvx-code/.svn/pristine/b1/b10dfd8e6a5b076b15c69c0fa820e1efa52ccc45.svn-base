/*
 * Copyright 2021 SIB Visions GmbH
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
 * 02.05.2021 - [MB] - creation
 */
package com.sibvisions.rad.persist.jdbc;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.rad.model.condition.ICondition;
import javax.rad.model.condition.LikeIgnoreCase;
import javax.rad.type.bean.Bean;
import javax.rad.type.bean.IBean;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the {@link InformixDBAccess} class.
 * 
 * @author Michael Bartl
 */
public class TestInformixDBAccess extends TestDBAccess
{
    /** LOCAL INFORMIX DB URL. */
    //private final String url = "jdbc:informix-sqli://sibnb9:9088/inftest:informixserver=ol_informix1210;DELIMIDENT=Y;";
    /** VM INFORMIX DB URL. */
    private final String url = "jdbc:informix-sqli://192.168.2.12:9088/inftest:informixserver=ol_informix1210;DELIMIDENT=Y;";
    
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * {@inheritDoc}
     */
    @Before
    @Override
    public void open() throws Exception
    {    
    	checkConnectionError();
    	
        dba = new InformixDBAccess();
        dba.setUrl(getURL());
        dba.setUsername("informix");
        dba.setPassword("sibvisions");
        dba.setAutoCommit(true);
        
        try
        {
        	dba.open();
        	
        	setConnectionError(false);
        }
        catch (Exception e)
        {
        	setConnectionError(true);
        	
        	throw e;
        }
        
        // drop test table
        dba.executeStatement("drop table if exists USERS");
        dba.executeStatement("drop table if exists DETAIL");
        dba.executeStatement("drop table if exists TEST");
        dba.executeStatement("drop table if exists TEST_DEFAULTS");
        dba.executeStatement("drop table if exists TEST_SORT");
        dba.executeStatement("drop table if exists ADRESSEN");

        dba.executeStatement("create table USERS("
                + "  ID               DECIMAL(16) primary key constraint USER_PK not null,\r\n"
                + "  USERNAME         VARCHAR(200) unique constraint USER_UK not null,\r\n"
                + "  PASSWORD         VARCHAR(200),\r\n"
                + "  CHANGE_PASSWORD  CHAR(1) default 'N' not null,\r\n"
                + "  CHANGE_PASSWORD2 CHAR(1) default 'N' not null,\r\n"
                + "  ACTIVE           CHAR(1) default 'Y' not null,\r\n"
                + "  VALID_FROM       DATE,\r\n"
                + "  VALID_TO         DATE,\r\n"
                + "  CREATED_BY       VARCHAR(200) not null,\r\n"
                + "  CREATED_ON       DATE not null,\r\n"
                + "  CHANGED_BY       VARCHAR(200) not null,\r\n"
                + "  CHANGED_ON       DATE not null,\r\n"
                + "  TITLE            VARCHAR(64),\r\n"
                + "  FIRST_NAME       VARCHAR(200),\r\n"
                + "  LAST_NAME        VARCHAR(200),\r\n"
                + "  EMAIL            VARCHAR(200),\r\n"
                + "  PHONE            VARCHAR(200),\r\n"
                + "  MOBIL            VARCHAR(200),\r\n"
                + "  FAX              VARCHAR(200),\r\n"
                + "  ABTEILUNG        VARCHAR(64),\r\n"
                + "  STRASSE          VARCHAR(64),\r\n"
                + "  PLZ              VARCHAR(6),\r\n"
                + "  ORT              VARCHAR(64)\r\n"
                + ");");
        
		// create test_defaults table
        dba.executeStatement(
				"CREATE TABLE test_defaults (active char(1) DEFAULT 'N' NOT NULL, " +
						"datetimeval DATETIME YEAR TO SECOND DEFAULT DATETIME (2010-01-01 12:00:00) YEAR TO SECOND, " +
						"numberval decimal(11) DEFAULT 1234, " +
						"text varchar(50) DEFAULT 'TEXT Test', " +
						"dateval date DEFAULT '2001-01-01')");
       
		// create test table
        dba.executeStatement("create table TEST (" + 
                            "\"ID\" serial primary key, " +
                            "\"NAME\" varchar(100)"
                            + ")");
		
		// create detail table
        dba.executeStatement("CREATE TABLE DETAIL (ID decimal(18) NOT null PRIMARY key CONSTRAINT DETAIL_PK, " + 
                             "NAME varchar(100)," + 
                             "TEST_ID serial, " + 
                             "foreign key (TEST_ID) references TEST(\"ID\") CONSTRAINT TEST_FK)");
		
		// create test sort table
        dba.executeStatement(
				"create table test_sort ("
						+ "\"ID\" serial primary key,"
						+ "\"NAME\" varchar(100),"
						+ "\"SORT\" integer)");
		
        dba.executeStatement(
				"CREATE TABLE ADRESSEN ("
						+ "ID serial primary key,"
						+ "POST_ID decimal(18) NOT NULL,"
						+ "STRA_ID decimal(18) NOT NULL,"
						+ "HAUSNUMMER decimal(18) NOT NULL,"
						+ "TUERNUMMER decimal(18),"
						+ "STIEGE decimal(18))");
        
        dba2 = new InformixDBAccess();
        dba2.setUrl(getURL());
        dba2.setUsername("informix");
        dba2.setPassword("sibvisions");
        dba2.setAutoCommit(true);

        dba2.open();
        Assert.assertTrue(dba2.isOpen());
        
        DatabaseMetaData metaData = dba.getConnection().getMetaData();
        metaData.getSchemas();
    }
    
    /** 
     * Return the DB URL to connect to.
     * @return the DB URL
     */
    protected String getURL()
    {
        return url;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Creates test tables for new tests.
     * 
     * @throws Exception
     *             if the connect or the create table fails
     */
    protected void createTestTables() throws Exception
    {
        dba.executeStatement("create table test_unquoted_lowercase (id serial primary key, name varchar(100))");
        dba.executeStatement("create table TEST_UNQUOTED_UPPERCASE (ID serial primary key, NAME varchar(100))");
        dba.executeStatement("create table `Test_Quoted` (`Id` serial primary key, `Name` varchar(100))");
        dba.executeStatement("create table TEST_PK_FETCH (SOME_VALUE varchar(32) primary key, " +
                             "created_AT datetime year to second NOT NULL DEFAULT DATETIME (2010-01-01 12:00:00) YEAR TO SECOND)");
        dba.executeStatement("create table TEST_COLUMN_SPECIALCHAR (ID integer not null primary key constraint TESC_PK, \"FIRST NAME\" varchar(100), \"LAST#NAME\" varchar(100))");
    }

    /**
     * {@inheritDoc}
     */
    @Test
    @Override
    public void testGetDefaultValue() throws Exception
    {
        Map<String, Object> htDefaults = dba.getDefaultValues(null, null, "test_defaults");

        Assert.assertEquals("N", getDefaultValue(htDefaults, "active"));
        Assert.assertEquals(new BigDecimal("1234.0000000000"), getDefaultValue(htDefaults, "numberval"));
        Assert.assertEquals("2001-01-01 00:00:00.0", getDefaultValue(htDefaults, "dateval").toString());
        Assert.assertEquals("2010-01-01 12:00:00.0", getDefaultValue(htDefaults, "datetimeval").toString());
        Assert.assertEquals("TEXT Test", getDefaultValue(htDefaults, "text"));
    }

    /**
     * Checks default allowed value detection. This test throws an exception if
     * some datatypes are not supported for
     * a specific database, e.g. Oracle & DB2 (no boolean datatype)
     * 
     * @throws Exception
     *             if the test fails
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
            // nothing to be done
        }

        dba.executeStatement("CREATE TABLE test_defaultallowed (dummyname varchar(100), boolval boolean DEFAULT 't', zahl integer DEFAULT 15, text varchar(100) DEFAULT 'Hallo')");

        DBStorage dbs = new DBStorage();
        dbs.setDBAccess(dba);
        dbs.setWritebackTable("test_defaultallowed");
        dbs.open();

        Assert.assertArrayEquals(new Boolean[] { Boolean.TRUE, Boolean.FALSE }, dbs.getMetaData().getColumnMetaData("BOOLVAL").getAllowedValues());
        // #1751 #1750
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

        List<IBean> liBean = dbs.fetchBean(null, null, 0, -1);

        Assert.assertEquals(2, liBean.size());

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
        
        Assert.assertEquals("UPPER(m.`NAME`) LIKE UPPER(?)".toLowerCase(), dba.getSQL(smd, cond, true).toLowerCase());
        Assert.assertArrayEquals(new Object[] {"hand%"}, dba.getParameter(cond));
    }

    /**
     * Test meta data.
     * 
     * @throws Exception if it fails.
     */
    @Test
    public void testMetaData() throws Exception
    {
        ResultSet res = dba.getConnection().createStatement().executeQuery(
                "select trim(owner) \"TABLE_SCHEMA\", "
                + "tabname \"TABLE_NAME\", "
                + "trim(CASE WHEN tabtype='V' THEN 'VIEW' WHEN tabtype='T' THEN 'TABLE' END) \"TABLE_TYPE\" "
                + "from informix.systables t "
                + "where owner = USER "
                + "and tabid > 99 "
                + "and (tabtype = 'T' or tabtype = 'V')"
                );
        
        while (res.next())
        {
            for (int i = 1, count = res.getMetaData().getColumnCount(); i <= count; i++)
            {
                System.out.print(res.getMetaData().getColumnName(i) + " = " + res.getObject(i) + ", ");
            }
            System.out.println();
        }
    }
    
    /**
     * Test getMetaData.getTables.
     * 
     * @throws Exception if it fails.
     */
    @Test
    public void testMetaDataJDBC() throws Exception
    {
        ResultSet res = dba.getConnection().getMetaData().getTables(null, null, null, null);
        
        while (res.next())
        {
            for (int i = 1, count = res.getMetaData().getColumnCount(); i <= count; i++)
            {
                System.out.print(res.getMetaData().getColumnName(i) + " = " + res.getObject(i) + ", ");
            }
            System.out.println();
        }
    }
    
    /**
     * Test Quote setting with DELIMIDENT on/off.
     * 
     * @throws Exception if it fails.
     */
    @Test
    public void testQuoteDelimIdent() throws Exception
    {
        InformixDBAccess informixDBAccess = new InformixDBAccess();
        informixDBAccess.setUrl(getURL());
        informixDBAccess.setUsername("informix");
        informixDBAccess.setPassword("sibvisions");
        informixDBAccess.open();
        
        assertEquals("\"", informixDBAccess.getOpenQuoteCharacter());
        
        informixDBAccess.close();
        informixDBAccess.setUrl(getURL().replace("DELIMIDENT=Y;", ""));
        informixDBAccess.open();

        assertEquals("", informixDBAccess.getOpenQuoteCharacter());
    }
} // TestInformixDBAccess
