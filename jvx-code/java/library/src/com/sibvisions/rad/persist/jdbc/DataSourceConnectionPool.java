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

import javax.sql.DataSource;

import com.sibvisions.util.log.ILogger;
import com.sibvisions.util.log.LoggerFactory;
import com.sibvisions.util.type.CommonUtil;
import com.sibvisions.util.type.StringUtil;

/**
 * The <code>IConnectionPool</code> is an interface to allow simple custom connection pool implementations.
 * 
 * @see com.sibvisions.rad.persist.jdbc.DBAccess
 * 
 * @author Martin Handsteiner
 */
public class DataSourceConnectionPool implements IConnectionPool
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The logger. */
	private static ILogger logger;

	/** The data source. */
	private DataSource dataSource;
	/** The user name. */
	private String username;
	/** The password. */
	private String password;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new DataSourceConnectionPool.
	 * @param pDataSource the data source.
	 */
	public DataSourceConnectionPool(DataSource pDataSource)
	{
		this(pDataSource, null, null);
	}

	/**
	 * Creates a new DataSourceConnectionPool.
	 * @param pDataSource the data source.
	 * @param pUsername the username.
	 * @param pPassword the password.
	 */
	public DataSourceConnectionPool(DataSource pDataSource, String pUsername, String pPassword)
	{
		if (pDataSource == null)
		{
			throw new IllegalArgumentException("The DataSource may not be null!");
		}
		
		dataSource = pDataSource;
		username = pUsername;
		password = pPassword;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public Connection getConnection() throws SQLException
	{
		debug("Get connection from connection pool: ", dataSource);
		
		if (!StringUtil.isEmpty(username) && !StringUtil.isEmpty(password))
		{
			return dataSource.getConnection(username, password); 
		}
		else
		{
			return dataSource.getConnection();
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void releaseConnection(Connection pConnection)
	{
		debug("Release connection of connection pool: ", dataSource);
		CommonUtil.close(pConnection);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the data source.
	 * @return the data source.
	 */
	public DataSource getDataSource()
	{
		return dataSource;
	}

	/**
	 * Gets the user name.
	 * @return the user name.
	 */
	public String getUsername()
	{
		return username;
	}

	/**
	 * Gets the password.
	 * @return the password.
	 */
	public String getPassword()
	{
		return password;
	}

    /**
     * Logs debug information.
     * 
     * @param pInfo the debug information
     */
    protected static void debug(Object... pInfo)
    {
        if (logger == null)
        {
            logger = LoggerFactory.getInstance(DataSourceConnectionPool.class);
        }
        
        logger.debug(pInfo);
    }


} 	// DataSourceConnectionPool
