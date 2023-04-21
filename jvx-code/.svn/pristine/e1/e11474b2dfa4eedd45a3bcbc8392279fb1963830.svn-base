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

import org.junit.Test;

/**
 * Tests all Functions of {@link TestMySQLDBAccess} with linux db.
 * 
 * @author Roland Hörmann
 * @see com.sibvisions.rad.persist.jdbc.MySQLDBAccess
 */
public class TestMySQLDBAccessLinux extends TestMySQLDBAccess
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
        return "192.168.1.223";
    }	
	
    /**
	 * Test DB Storage.
	 * 
	 * @throws Exception if the test fails
	 */
	@Test
	public void testDBStorageTableName() throws Exception
	{
		// can't be testet without:
		// So it appears you should set lower_case_table_names to 1 in the MySQL config file.
	}

} 	// TestMySQLDBAccessLinux
