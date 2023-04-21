/*
 * Copyright 2013 SIB Visions GmbH
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
 */
package com.sibvisions.apps.demoerp.screens;

import com.sibvisions.rad.persist.jdbc.DBStorage;

/**
 * The Customers class is the life-cycle object for CustomerWorkScreen.
 */
public class Customers extends Offers
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the customer database storage.<br>
	 * 
	 * @throws Exception if the DBStorage couldn't initialized.<br>
	 * @return the customer DBStorage.
	 */
	public DBStorage getCustomer() throws Exception
	{
		DBStorage dbsCustomer = (DBStorage)get("customer");
		if (dbsCustomer == null)
		{
			dbsCustomer = new DBStorage();
			dbsCustomer.setWritebackTable("customer");
			dbsCustomer.setDBAccess(getDBAccess());
			dbsCustomer.open();

			put("customer", dbsCustomer);
		}
		return dbsCustomer;
	}

} // Customers
