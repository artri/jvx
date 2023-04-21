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
 * 23.11.2009 - [RH] - ColumnMetaData && PrimaryKey Column is replaced with MetaData class
 */
package com.sibvisions.rad.persist.jdbc;

import org.junit.Test;


/**
 * Tests all Functions of {@link DB2DBAccess} with a DB2 9 database.
 * 
 * @author Roland Hörmann
 * @see com.sibvisions.rad.persist.jdbc.DB2DBAccess
 */
public class TestDB2DBAccessV9 extends TestDB2DBAccessV10
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Gets the hostname.
     * 
     * @return the hostname or IP
     */
    @Override
    protected String getHostName()
    {
        return "192.168.1.201:50000";
    }
    
    /**
     * Don't test!
     * 
     * @throws Exception never thrown
     */
    @Test
    @Override
    public void testFilterLikeReverse() throws Exception
    {    
    }
    
} 	// TestDB2DBAccessV9
