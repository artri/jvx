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
package apps.firstapp;

import javax.rad.server.SessionContext;

import com.sibvisions.rad.persist.jdbc.DBAccess;
import com.sibvisions.rad.persist.jdbc.IDBAccess;
import com.sibvisions.rad.server.security.DataSourceHandler;

/**
 * The LCO for the session.
 *
 * @author René Jahn
 */
public class Session extends Application
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Returns access to the database.
	 * 
	 * @return the database access
	 * @throws Exception if a connection error occurs
	 */
	public IDBAccess getDBAccess() throws Exception
	{
		DBAccess dba = (DBAccess)get("dBAccess");
		
		if (dba == null)
		{
			dba = DBAccess.getDBAccess(DataSourceHandler.createDBCredentials(SessionContext.getCurrentSessionConfig(), "default"));
			dba.open();
					
			put("dBAccess", dba);
		}
		
		return dba;
	}	
	
	/**
	 * Gets the application name via session context.
	 * 
	 * @return the application name
	 */
	public String getApplicationName()
	{
		return SessionContext.getCurrentSession().getApplicationName();
	}

}	// Session
