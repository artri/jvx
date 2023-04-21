/*
 * Copyright 2016 SIB Visions GmbH
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
 * 06.06.2016 - [RZ] - creation.                          
 */
package com.sibvisions.rad.persist.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import javax.rad.persist.DataSourceException;

/**
 * The {@link SQLiteDBAccess} is the implementation for the SQLite database.
 *  
 * @see com.sibvisions.rad.persist.jdbc.DBAccess
 * 
 * @author Robert Zenz
 */
public class SQLiteDBAccess extends DBAccess
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link SQLiteDBAccess}.
	 */
	public SQLiteDBAccess()
	{
		setDriver("org.sqlite.JDBC");
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isAutoQuote(String pName)
	{
		// SQLite is case-insensitive for table and column names.
		// So we never need to quote them.
		return !isValidIdentifier(pName);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected TableInfo getTableInfoIntern(String pWriteBackTable) throws DataSourceException
	{
		TableInfo tableInfo = super.getTableInfoIntern(pWriteBackTable);
		
		if (tableInfo.getSchema() == null)
		{
			// If there is no schema set, we will assume the main schema.
			return new TableInfo(tableInfo.getCatalog(), "main", tableInfo.getTable());
		}
		
		return tableInfo;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void prepareConnection(Connection pConnection) throws SQLException
	{
		// Do not call super.prepareConnection(Connection) as this will set
		// the transaction level to an invalid (for SQLite) value.
	}
	
}	// SQLiteDBAccess
