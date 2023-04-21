/*
 * Copyright 2021 SIB Visions GmbH
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
 * 30.04.2021 - [MB] - creation
 */
package com.sibvisions.rad.persist.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.rad.model.condition.CompareCondition;
import javax.rad.model.condition.LikeIgnoreCase;
import javax.rad.model.condition.LikeReverseIgnoreCase;
import javax.rad.model.datatype.StringDataType;
import javax.rad.persist.DataSourceException;
import javax.rad.type.bean.Bean;

import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.ImmutableTimestamp;
import com.sibvisions.util.log.ILogger.LogLevel;
import com.sibvisions.util.type.StringUtil;
import com.sibvisions.util.type.StringUtil.CaseSensitiveType;

/**
 * The <code>InformixDBAccess</code> is the implementation for Informix
 * databases.<br>
 * 
 * @see com.sibvisions.rad.persist.jdbc.DBAccess
 * @author Michael Bartl
 */
public class InformixDBAccess extends DBAccess
{
    /** DB transactions supported. */
    private boolean hasTransactions = false;

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Creates a new instance of <code>InformixDBAccess</code>.
     */
    public InformixDBAccess()
    {
        setDriver("com.informix.jdbc.IfxDriver");
        setQuoteCharacters("\"", "\"");
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    @Override
    protected void prepareConnection(Connection pConnection) throws SQLException
    {
        if (pConnection.getTransactionIsolation() != Connection.TRANSACTION_NONE)
        {
            try
            {
                super.prepareConnection(pConnection);
                hasTransactions = true;
            }
            catch (SQLException e)
            {
                // this connection doesn't have transactions
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAutoQuote(String pName)
    {
        return StringUtil.getCaseSensitiveType(pName) != CaseSensitiveType.LowerCase || !isValidIdentifier(pName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAutoCommit(boolean pEnable) throws DataSourceException
    {
        if (hasTransactions)
        {
            super.setAutoCommit(pEnable);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void commit() throws DataSourceException
    {
        if (hasTransactions)
        {
            super.commit();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getAliveQuery()
    {
        return "select 1 from systables where tabid = 1";
    }

    /**
     * Converts an object to a standard value for the specific database. Not all
     * values are supported from
     * the underlying database, e.g. java.sql.Timestamp is preferred instead of
     * java.util.Date.
     * 
     * @param pValue
     *            any value
     * @return the database specific value
     */
    @Override
    protected Object convertValueToDatabaseSpecificObject(Object pValue)
    {
        if (pValue instanceof ImmutableTimestamp)
        {
            return new Timestamp(((Date)pValue).getTime());
        }
        else
        {
            return pValue;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String createWhereColumn(ServerMetaData pServerMetaData, CompareCondition pCompare, String pColumnName)
    {
        String sColumn = super.createWhereColumn(pServerMetaData, pCompare, pColumnName);

        // #2128
        if (pCompare instanceof LikeIgnoreCase || pCompare instanceof LikeReverseIgnoreCase)
        {
            try
            {
                ServerColumnMetaData scmd = pServerMetaData.getServerColumnMetaData(pCompare.getColumnName());

                if (scmd.getTypeIdentifier() != StringDataType.TYPE_IDENTIFIER)
                {
                    return "''||" + sColumn;
                }
            }
            catch (Exception e)
            {
                debug(e);
            }
        }

        return sColumn;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected TableInfo getTableInfoIntern(String pWriteBackTable) throws DataSourceException
    {
        TableInfo tableInfo = super.getTableInfoIntern(pWriteBackTable);

        String schema = tableInfo.getSchema();

        try
        {
            schema = schema == null ? getConnection().getMetaData().getUserName() : schema;
        }
        catch (SQLException e)
        {
            debug(e);
        }

        return new TableInfo(tableInfo.getCatalog(), schema, tableInfo.getTable());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUsername(String pUsername)
    {
        if (pUsername != null)
        {
            super.setUsername(pUsername.toLowerCase());            
        }
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void open() throws DataSourceException
    {
        super.open();
        if (getUrl().contains("DELIMIDENT="))
        {
            setQuoteCharacters("\"", "\"");
        }
        else
        {
            setQuoteCharacters("", "");
        }
    }
    
    /**
     * {@inheritDoc}
     */
    protected List<Key> getUniqueKeysIntern(String pCatalog, String pSchema, String pTable) throws DataSourceException
    {   
        try
        {
            ArrayUtil<Key> auResult = new ArrayUtil<Key>();
            ArrayUtil<Name> auUniqueKeyColumns = new ArrayUtil<Name>();
            
            long lMillis = System.currentTimeMillis();
            
            List<Bean> result = executeQuery("select * from informix.sysconstraints where tabid > 99 and constrtype = 'U'");
            
            for (Bean entry : result)
            {
                String keyName = (String) entry.get("CONSTRNAME");
                String quotedName = keyName;
                if (isAutoQuote(keyName))
                {
                    quotedName = quote(keyName);    
                }
                auUniqueKeyColumns.add(new Name(keyName.toUpperCase(), keyName, quotedName));
                
                Name[] columns = auUniqueKeyColumns.toArray(new Name[auUniqueKeyColumns.size()]);
                auResult.add(new Key(keyName, columns));
                auUniqueKeyColumns.clear();
            }
            
            if (isLogEnabled(LogLevel.DEBUG))
            {
                debug("getUKs(", pTable, ") in ", Long.valueOf(System.currentTimeMillis() - lMillis), "ms");
            }

            return auResult;
        }
        catch (SQLException sqlException)
        {
            error("Unique Keys couldn't determined from database! - ", pTable, sqlException);
            
            return null;
        }
    }
} // InformixDBAccess
