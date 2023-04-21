/*
 * Copyright 2022 SIB Visions GmbH
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
 * 16.05.2022 - [JR] - creation
 */
package com.sibvisions.rad.persist.jdbc;

import java.sql.SQLException;

import com.sibvisions.util.Reflective;

/**
 * The <code>EDBDBAccess</code> is the implementation for EDB databases.<br>
 *  
 * @see com.sibvisions.rad.persist.jdbc.DBAccess
 * 
 * @author René Jahn
 */
public class EDBDBAccess extends PostgreSQLDBAccess
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
     * Creates a new instance of <code>EDBDBAccess</code>.
     */
    public EDBDBAccess()
    {
        setDriver("com.edb.Driver");
        
        setUseSavepoints(true);
    }
    
    /**
     * {@inheritDoc} 
     */
    @Override
    protected Object convertDatabaseSpecificObjectToValue(ServerColumnMetaData pColumnMetaData, Object pValue) throws SQLException
    {   
        if (pValue != null && "PGobject".equals(pValue.getClass().getSimpleName()))
        {
            try
            {
                return Reflective.call(pValue, "getValue");
            }
            catch (Throwable th)
            {
                throw new SQLException(th.getMessage(), th);
            }
        }
        
        return pValue;
    }
    
} 	// EDBDBAccess
