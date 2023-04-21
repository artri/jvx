/*
 * Copyright 2011 SIB Visions GmbH
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
 * 17.12.2011 - [JR] - creation
 */
package demo.special;

import com.sibvisions.rad.persist.jdbc.DBAccess;
import com.sibvisions.rad.persist.jdbc.DBStorage;
import com.sibvisions.rad.server.GenericBean;

/**
 * Test class for #525.
 * 
 * @author René Jahn
 */
public class Bug525 extends GenericBean
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Returns the db access.
	 * 
	 * @return the db access.
	 * @throws Exception if the storage can not be opened
	 */
	public DBAccess getPgSql() throws Exception
	{
		DBAccess dba = (DBAccess)get("pgSql");
		
		if (dba == null)
		{
			dba = DBAccess.getDBAccess("jdbc:postgresql://10.0.1.6/testdb");
			dba.setUsername("test");
			dba.setPassword("test");
			dba.open();
			
			put("pgSql", dba);
		}
		
		return dba;
	}
	
	/**
	 * Returns the users storage.
	 * 
	 * @return the users storage.
	 * @throws Exception if the storage can not be opened
	 */
	public DBStorage getUsers() throws Exception
	{
		DBStorage dbs = (DBStorage)get("users");

		if (dbs == null)
		{
			dbs = new DBStorage();
			dbs.setDBAccess(getPgSql());
			dbs.setWritebackTable("USERS");
			dbs.setAutoLinkReference(false);
			dbs.open();

			put("users", dbs);
		}

		return dbs;
	}
	
}	// Bug525
