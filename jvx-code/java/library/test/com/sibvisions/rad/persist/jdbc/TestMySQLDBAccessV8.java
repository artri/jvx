/*
 * Copyright 2019 SIB Visions GmbH
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
 * 28.10.2019 - [JR] - creation
 */
package com.sibvisions.rad.persist.jdbc;

/**
 * Tests all Functions of {@link TestMySQLDBAccess} with MariaDB driver.
 * 
 * @author René Jahn
 * @see com.sibvisions.rad.persist.jdbc.MariaDBAccess
 */
public class TestMySQLDBAccessV8 extends TestMySQLDBAccess
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
        return "192.168.1.236";
    }

} 	// TestMariaDBAccessWithMySql
