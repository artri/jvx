/*
 * Copyright 2014 SIB Visions GmbH
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
 * 21.04.2014 - [JR] - creation
 */
package com.sibvisions.rad.persist.jdbc;

/**
 * Tests all Functions of {@link TestPostgreSQLDBAccess} with linux db.
 * 
 * @author Ren� Jahn
 * @see com.sibvisions.rad.persist.jdbc.PostgreSQLDBAccess
 */
public class TestPostgreSQLDBAccessLinux extends TestPostgreSQLDBAccess
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
    
} 	// TestPostgreSQLDBAccessLinux
