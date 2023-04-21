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

import java.sql.Connection;
import java.sql.SQLException;

/**
 * The <code>IConnectionPool</code> is an interface to allow simple custom connection pool implementations.
 * 
 * @see com.sibvisions.rad.persist.jdbc.DBAccess
 * 
 * @author Martin Handsteiner
 */
public interface IConnectionPool
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets a connection from the pool.
	 * The DBAccess ensures, that the connection is released, at least on closing the DBAccess.
	 * 
	 * @return the jdbc connection
	 * @throws SQLException if connection is not available
	 */
	public Connection getConnection() throws SQLException;
	
	/**
	 * Gets a connection from the pool.
	 * The DBAccess ensures, that the connection is released, at least on closing the DBAccess.
	 * 
	 * @param pConnection the jdbc connection
	 */
	public void releaseConnection(Connection pConnection);
	
} 	// IConnectionPool
