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
 * 15.06.2012 - [JR] - creation
 */
package com.sibvisions.rad.persist.jdbc;

import org.junit.Test;

/**
 * Test general JDBC calls.
 * 
 * @author René Jahn
 */
public class ConnectionChecker
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Tests PostgreSql connection establishment with SSL.
	 * 
	 * @throws Exception if connection is not established
	 */
	@Test
	public void testPostgreSqlSsl() throws Exception
	{
		DBAccess dba = DBAccess.getDBAccess("jdbc:postgresql://vmpgsqlwinxp.sibvisions.net/testdb?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory");
		
		dba.setUsername("test");
		dba.setPassword("test");
		
		dba.open();
		dba.close();
	}
	
}   // ConnectionChecker
