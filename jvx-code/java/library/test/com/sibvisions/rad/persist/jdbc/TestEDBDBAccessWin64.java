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
 * 20.09.2016 - [JR] - creation
 */
package com.sibvisions.rad.persist.jdbc;

import org.junit.Test;

/**
 * Tests all Functions of {@link TestPostgreSQLDBAccess} with windows 7, 64bit, and postgresql 9.5.
 * 
 * @author René Jahn
 * @see com.sibvisions.rad.persist.jdbc.PostgreSQLDBAccess
 */
public class TestEDBDBAccessWin64 extends TestPostgreSQLDBAccess
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /**
     * {@inheritDoc}
     */
    @Override
    protected String getHostName()
    {
        return "192.168.2.15:5444";
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected String getJDBCScheme()
    {
    	return "jdbc:edb:";
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected EDBDBAccess createDBAccess()
    {
    	return new EDBDBAccess();    	
    }    
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isUpdateWithAliasSupported()
    {
    	return true;
    }
    
    /**
     * Tests complex query parsing and parameter replacement.
     * 
     * @throws Exception if test fails
     */
    @Test
    @Override
    public void testStringTypeUnspecified() throws Exception
    {
        // com.edb.util.PGobject from edb jdbc driver would be needed.
    }
    
} 	// TestPostgreSQLDBAccessWin64v95
