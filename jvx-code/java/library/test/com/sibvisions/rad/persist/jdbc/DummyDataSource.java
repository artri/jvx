/*
 * Copyright 2014 SIB Visions GmbH
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
 * 17.12.2014 - [JR] - creation
 */
package com.sibvisions.rad.persist.jdbc;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;

/**
 * The <code>DummyDataSource</code> is a simple {@link DataSource} implementation
 * that returns the connection of a {@link DBAccess} object. 
 * 
 * @author René Jahn
 */
public class DummyDataSource implements DataSource
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /** the database access object. */
    private DBAccess dba;
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Creates a new instance of <code>DummyDataSource</code> for the given
     * database access object.
     * 
     * @param pDBAccess the database access object
     */
    public DummyDataSource(DBAccess pDBAccess)
    {
        dba = pDBAccess;
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    public <T> T unwrap(Class<T> pIface) throws SQLException
    {
        return null;
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean isWrapperFor(Class<?> pIface) throws SQLException
    {
        return false;
    }
    
    /**
     * {@inheritDoc}
     */
    public void setLoginTimeout(int pTimeout) throws SQLException 
    {
    }
    
    /**
     * {@inheritDoc}
     */
    public void setLogWriter(PrintWriter pWriter) throws SQLException 
    {
    }
    
    /**
     * {@inheritDoc}
     */
    public int getLoginTimeout() throws SQLException
    {
        return 0;
    }
    
    /**
     * {@inheritDoc}
     */
    public PrintWriter getLogWriter() throws SQLException
    {
        return null;
    }
    
    /**
     * {@inheritDoc}
     */
    public Connection getConnection(String pUserName, String pPassword) throws SQLException
    {
        return dba.getConnection();
    }
    
    /**
     * {@inheritDoc}
     */
    public Connection getConnection() throws SQLException
    {
        return dba.getConnection();
    }

    /**
     * {@inheritDoc}
     */
	public Logger getParentLogger() throws SQLFeatureNotSupportedException 
	{
		return null;
	}
    
}   // DummyDataSource
