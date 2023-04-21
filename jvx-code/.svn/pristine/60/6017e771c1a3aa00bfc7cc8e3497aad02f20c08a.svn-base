/*
 * Copyright 2015 SIB Visions GmbH
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
 * 01.06.2015 - [HM] - creation
 */
package com.sibvisions.rad.persist.jdbc.event;

import java.sql.Connection;

import com.sibvisions.rad.persist.jdbc.DBAccess;

/**
 * The <code>ConnectionEvent</code> serves information about connection and dBAccess.
 * 
 * @author Martin Handsteiner
 */
public class ConnectionEvent
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** the dBAccess. */
    private DBAccess dBAccess;
    
    /** the connection. */
    private Connection connection;
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Creates a new instance of <code>ConnectionEvent</code>.
     * 
     * @param pDBAccess the dBAccess
     * @param pConnection the connection
     */
    public ConnectionEvent(DBAccess pDBAccess, Connection pConnection)
    {
        dBAccess = pDBAccess;
        connection = pConnection;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Gets the dBAccess.
     * 
     * @return the dBAccess
     */
    public DBAccess getDBAccess()
    {
        return dBAccess;
    }
    
    /**
     * Gets the connection.
     * 
     * @return the connection
     */
    public Connection getConnection()
    {
        return connection;
    }
    
}   // ConnectionEvent
