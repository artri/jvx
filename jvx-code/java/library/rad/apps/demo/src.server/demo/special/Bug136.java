/*
 * Copyright 2010 SIB Visions GmbH
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
 * 16.06.2010 - [JR] - creation
 */
package demo.special;

import com.sibvisions.rad.persist.jdbc.DBAccess;
import com.sibvisions.rad.persist.jdbc.DBStorage;
import com.sibvisions.rad.server.GenericBean;

/**
 * Test class for #136.
 * 
 * @author René Jahn
 */
public class Bug136 extends GenericBean
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
	public DBAccess getMyAccess() throws Exception
	{
		DBAccess dba = (DBAccess)get("myAccess");
		
		if (dba == null)
		{
			dba = DBAccess.getDBAccess("jdbc:mysql://localhost:3306/test");
			dba.setUsername("test");
			dba.setPassword("test");
			dba.open();
			
			put("myAccess", dba);
		}
		
		return dba;
	}
	
	/**
	 * Returns the hdr storage.
	 * 
	 * @return the hdr storage.
	 * @throws Exception if the storage can not be opened
	 */
	public DBStorage getHdr() throws Exception
	{
		DBStorage dbsHdr = (DBStorage)get("hdr");

		if (dbsHdr == null)
		{
			dbsHdr = new DBStorage();
			dbsHdr.setDBAccess(getMyAccess());
			dbsHdr.setRefetch(true);
			dbsHdr.setFromClause("test.v_hdr");
			dbsHdr.setWritebackTable("test.hdr");
			dbsHdr.setAutoLinkReference(true);
			dbsHdr.open();

			put("hdr", dbsHdr);
		}

		return dbsHdr;
	}

	/**
	 * Returns the inventry storage.
	 * 
	 * @return the inventry storage.
	 * @throws Exception if the storage can not be opened
	 */
	public DBStorage getInventry() throws Exception
	{
		DBStorage dbsHdr = (DBStorage)get("inventry");

		if (dbsHdr == null)
		{
			dbsHdr = new DBStorage();
			dbsHdr.setDBAccess(getMyAccess());
			dbsHdr.setFromClause("test.v_inv_items");
			dbsHdr.setWritebackTable("test.inventry");
			dbsHdr.setAutoLinkReference(true);
			dbsHdr.open();

			put("inventry", dbsHdr);
		}

		return dbsHdr;
	}

	/**
	 * Returns the items storage.
	 * 
	 * @return the items storage.
	 * @throws Exception if the storage can not be opened
	 */
	public DBStorage getItems() throws Exception
	{
		DBStorage dbsItems = (DBStorage)get("items");

		if (dbsItems == null)
		{
			dbsItems = new DBStorage();
			dbsItems.setDBAccess(getMyAccess());
			dbsItems.setFromClause("test.items");
			dbsItems.setWritebackTable("test.items");
			dbsItems.open();

			put("items", dbsItems);
		}

		return dbsItems;
	}

	/**
	 * Returns the parties storage.
	 * 
	 * @return the parties storage.
	 * @throws Exception if the storage can not be opened
	 */
	public DBStorage getParties() throws Exception
	{
		DBStorage dbsParty = (DBStorage)get("parties");

		if (dbsParty == null)
		{
			dbsParty = new DBStorage();
			dbsParty.setDBAccess(getMyAccess());
			dbsParty.setFromClause("test.party");
			dbsParty.setWritebackTable("test.party");
			dbsParty.open();

			put("parties", dbsParty);
		}

		return dbsParty;
	}

	/**
	 * Returns the doctors storage.
	 * 
	 * @return the doctors storage.
	 * @throws Exception if the storage can not be opened
	 */
	public DBStorage getDoctors() throws Exception
	{
		DBStorage dbsDoctors = (DBStorage)get("doctors");

		if (dbsDoctors == null)
		{
			dbsDoctors = new DBStorage();
			dbsDoctors.setDBAccess(getMyAccess());
			dbsDoctors.setFromClause("test.doctors");
			dbsDoctors.setWritebackTable("test.doctors");
			dbsDoctors.open();

			put("doctors", dbsDoctors);
		}

		return dbsDoctors;
	}

	/**
	 * Returns the stock storage.
	 * 
	 * @return the stock storage.
	 * @throws Exception if the storage can not be opened
	 */
	public DBStorage getStock() throws Exception
	{
		DBStorage dbsStock = (DBStorage)get("stock");

		if (dbsStock == null)
		{
			dbsStock = new DBStorage();
			dbsStock.setDBAccess(getMyAccess());
			dbsStock.setFromClause("test.v_stock_items");
			// dbsStock.setWritebackTable("V_STOCK");
			dbsStock.open();

			put("stock", dbsStock);
		}

		return dbsStock;
	}
	
}	// Bug136
