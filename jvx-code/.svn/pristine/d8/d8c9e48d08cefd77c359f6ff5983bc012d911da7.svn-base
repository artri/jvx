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
 * 19.04.2010 - [JR] - creation
 */
package com.sibvisions.apps.simpleapp;

import javax.rad.server.IConfiguration;
import javax.rad.server.SessionContext;

import com.sibvisions.rad.persist.jdbc.DBAccess;
import com.sibvisions.rad.persist.jdbc.HSQLDBAccess;
import com.sibvisions.rad.server.GenericBean;

/**
 * Session object for the application.
 * 
 * @author René Jahn
 */
public class Session extends GenericBean
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Returns access to the database.
	 * 
	 * @return the access to the database
	 * @throws Exception if the datasource can not be opened
	 */
	public DBAccess getDBAccess() throws Exception
	{
		DBAccess dba = (DBAccess)get("dbAccess");
		
		if (dba == null)
		{
			IConfiguration cfgSession = SessionContext.getCurrentSessionConfig();
			
			dba = new HSQLDBAccess();
			
			//read the configuration from the config file
			dba.setUrl(cfgSession.getProperty("/application/securitymanager/database/url")); 
			dba.setUsername(cfgSession.getProperty("/application/securitymanager/database/username"));
			dba.setPassword(cfgSession.getProperty("/application/securitymanager/database/password"));
			dba.open();
					
			put("dbAccess", dba);
		}
		
		return dba;
	}	
	
	/**
	 * This method sleeps 5 seconds.
	 * 
	 * @throws Exception if sleep fails
	 */
	public void asyncServerAction() throws Exception
	{
		Thread.sleep(5000);
		
		System.out.println("Async server action: done");
	}

	/**
	 * This method will return immediately.
	 */
	public void asyncDownloadAction()
	{
	    System.out.println("Async download action");
	}
	
}	// Session
