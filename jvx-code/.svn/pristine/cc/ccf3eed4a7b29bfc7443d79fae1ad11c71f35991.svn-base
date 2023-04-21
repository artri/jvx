/*
 * Copyright 2017 SIB Visions GmbH
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
 * 01.08.2017 - [JR] - creation
 * 27.11.2017 - [JR] - #1858: getMetaDataWhereClause overwritten
 */
package com.sibvisions.rad.persist.jdbc;

import java.sql.SQLException;

import javax.rad.persist.DataSourceException;

import com.sibvisions.rad.persist.jdbc.param.AbstractParam;

/**
 * The <code>TiberoDBAccess</code> is the implementation for Tibero databases.<br>
 *  
 * @see com.sibvisions.rad.persist.jdbc.DBAccess
 * 
 * @author René Jahn
 */
public class TiberoDBAccess extends AbstractOracleDBAccess
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** The select statement to get the Synonyms. */
    private static String sSynonymSelect = "select s.org_object_owner table_owner, s.org_object_name table_name, null db_link " +
                                           "FROM user_synonyms s " +
                                           "WHERE s.synonym_name = ?";    
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Creates a new instance of <code>TiberoDBAccess</code>.
     */
    public TiberoDBAccess()
    {
        setDriver("com.tmax.tibero.jdbc.TbDriver");
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Abstract methods implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    @Override
    protected Object convertArrayToList(Object pParam) throws SQLException
    {
        return pParam;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Object convertToArray(AbstractParam pParam) throws SQLException
    {
        return pParam.getValue();
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getTableForSynonymIntern(String pSynonym) throws DataSourceException
    {
        return getTableForSynonymIntern(sSynonymSelect, pSynonym);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected String getMetaDataWhereClause()
    {
        return "ROWNUM < 0";
    }
    
}   // TiberoDBAccess
