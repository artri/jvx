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
 * 01.10.2008 - [RH] - creation
 */
package demo;

import com.sibvisions.rad.persist.jdbc.DBStorage;


/**
 * The <code>Company</code> class is the life-cycle object for the CompanyFrame.
 * It uses the initialization mechanism with <code>init</code> methods.
 * 
 * @author René Jahn
 */
public class Company extends Session
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of <code>Company</code>.
	 */
	public Company()
	{	
	    InstanceChecker.add(Company.class);
	    
		System.out.println("CALL: Company.<init>");
	}

	/**
	 * Initialization of the the company databook.
	 * 
	 * @return the company databook
	 * @throws Throwable if the initialization throws an error
	 */
	@SuppressWarnings("unused")
	private DBStorage initCompany() throws Throwable
	{
		DBStorage dbCompany = new DBStorage();

		dbCompany.setDBAccess(getDataSource());
		dbCompany.setFromClause("FIRMEN");
		dbCompany.setWritebackTable("FIRMEN");
		dbCompany.open();
		
		return dbCompany;
	}
	
	/**
	 * Initialization of the the persons databook.
	 * 
	 * @return the persons databook
	 * @throws Throwable if the initialization throws an error
	 */
	@SuppressWarnings("unused")
	private DBStorage initPersons() throws Throwable
	{
		DBStorage dbPersons = new DBStorage();
		
		dbPersons.setDBAccess(getDataSource());
		dbPersons.setFromClause("PERSONEN");
		dbPersons.setWritebackTable("PERSONEN");
		dbPersons.open();
				
		return dbPersons;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Returns the company databook.
	 * 
	 * @return the company databook.
	 */
	public DBStorage getCompany()
	{
		return (DBStorage)get("company");
	}

	/**
	 * Returns the persons databook.
	 * 
	 * @return the persons databook.
	 */
	public DBStorage getPersons()
	{
		return (DBStorage)get("persons");
	}
	
}	// Company
