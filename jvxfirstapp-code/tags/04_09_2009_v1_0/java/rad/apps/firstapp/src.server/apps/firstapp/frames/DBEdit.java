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
 * 25.08.2009 - [JR] - creation
 */
package apps.firstapp.frames;

import com.sibvisions.rad.persist.jdbc.DBStorage;

import apps.firstapp.Session;

/**
 * The LCO for the DBEdit WorkScreen.
 * <p/>
 * @author René Jahn
 */
public class DBEdit extends Session
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Returns the contacts storage.
	 * 
	 * @return the contacts storage
	 * @throws Exception if the initialization throws an error
	 */
	public DBStorage getContacts() throws Exception
	{
		DBStorage dbsContacts = (DBStorage)get("contacts");
		
		if (dbsContacts == null)
		{
			dbsContacts = new DBStorage();
			dbsContacts.setDBAccess(getDBAccess());
			dbsContacts.setFromClause("CONTACTS");
			dbsContacts.setWritebackTable("CONTACTS");
			dbsContacts.open();
			
			put("contacts", dbsContacts);
		}
		
		return dbsContacts;
	}
	
}	// DBEdit
