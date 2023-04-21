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
 * 04.01.2010 - [RH] - creation
 */
package com.sibvisions.rad.persist.jdbc;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.rad.persist.ColumnMetaData;
import javax.rad.persist.DataSourceException;

/**
 * The <code>IColumnMetaDataCreator</code> is for creating custom <code>ColumnMetaData</code> implementations.
 * 
 * @see com.sibvisions.rad.persist.jdbc.DBAccess
 * 
 * @author Martin Handsteiner
 */
public interface IColumnMetaDataCreator
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a custom <code>ColumnMetaData</code> implementation. 
	 * 
	 * @param pDBAccess the dbAccess that invokes this listener.
	 * @param pResultSetMetaData the result set meta data for creating.
	 * @param pResultSetColumnIndex the result set meta data index.
	 * 
	 * @return the custom column meta data
	 * 
	 * @throws SQLException if an unwanted SQLException occurs
	 * @throws DataSourceException if an know problem occurs during creation
	 */
	public ColumnMetaData createColumnMetaData(DBAccess pDBAccess, ResultSetMetaData pResultSetMetaData, int pResultSetColumnIndex) throws DataSourceException, SQLException;
	
} 	// IColumnMetaDataCreator
