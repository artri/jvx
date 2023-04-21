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
 * 01.10.2008 - [JR] - creation
 */
package demo.special;

import com.sibvisions.rad.persist.jdbc.DBStorage;
import com.sibvisions.rad.server.http.rest.service.config.StorageServiceConfig;
import com.sibvisions.rad.server.http.rest.service.config.StorageServiceConfig.Option;

import demo.Session;

/**
 * The <code>Address</code> class is the life-cycle object for the AddressFrame.
 * It uses the initialization mechanism within the <code>get</code> methods.
 * 
 * @author René Jahn
 */
public class Address extends Session
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Returns the address storage.
	 * 
	 * @return the address storage.
	 * @throws Throwable if the storage can not be opened
	 */
	public DBStorage getAddress() throws Throwable
	{
		DBStorage dbs = (DBStorage)get("address");
		
		if (dbs == null)
		{
			dbs = new DBStorage();

			dbs.setDBAccess(getDataSource());
			dbs.setWritebackTable("ADRESSEN");
			dbs.open();
			
			put("address", dbs);
		}
		
		return dbs;
	}

	/**
	 * Returns the address storage with additional options.
	 * 
	 * @return the address storage.
	 * @throws Throwable if the storage can not be opened
	 */
	public DBStorage getAddressWithOptions() throws Throwable
	{
		DBStorage dbs = (DBStorage)get("addressWithOptions");
		
		if (dbs == null)
		{
			dbs = new DBStorage();

			dbs.setDBAccess(getDataSource());
			dbs.setWritebackTable("ADRESSEN");
			dbs.open();
			
			//Additional configuration for REST
			StorageServiceConfig cfg = new StorageServiceConfig();
			cfg.setOptionEnabled(Option.Fetch, false);
			cfg.setOptionEnabled(Option.MetaData, false);
			
			dbs.putObject(StorageServiceConfig.class.getName(), cfg);
			
			put("addressWithOptions", dbs);
		}
		
		return dbs;
	}
	
}	// Address
