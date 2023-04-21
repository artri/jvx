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
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests all Functions of {@link DBAccess} with a MS SQL driver.
 * 
 * @author Roland Hörmann
 * @see com.sibvisions.rad.persist.jdbc.DBAccess
 */
public class TestMicrosoftSQLDBAccess extends TestMSSQLDBAccess
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Creates the MSSQLDBAccess.
     * @return the MSSQLDBAccess.
     */
    protected MSSQLDBAccess createMSSQLDBAccess()
    {
        return new MicrosoftSQLDBAccess();
    }
    
    /**
     * Gets the jdbc url to the Test DB.
     * @return the jdbc url to the Test DB.
     */
    protected String getJdbcUrl()
    {
        return "jdbc:sqlserver://192.168.1.201;databaseName=test";
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
        Assert.assertEquals("2001-01-01 00:00:00.0", getDefaultValue(htDefaults, "dateval").toString());
        Assert.assertEquals("2010-01-01 12:00:00.0", getDefaultValue(htDefaults, "datetimeval").toString());
        Assert.assertEquals("TEXT Test", getDefaultValue(htDefaults, "text"));
    }   
	
} 	// TestMicrosoftSQLDBAccess
