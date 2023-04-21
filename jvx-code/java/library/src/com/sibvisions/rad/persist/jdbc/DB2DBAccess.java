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
 * 30.05.2009 - [RH] - creation
 * 27.03.2010 - [JR] - #92: default value support 
 * 11.03.2011 - [RH] - #308: DB specific automatic quoting implemented      
 * 25.07.2011 - [RH] - #441: DB2 doesn't support catalogs and return it wrong from the meta data.
 * 15.05.2014 - [JR] - #1038: CommonUtil.close used
 * 20.02.2020 - [DJ] - #2207: is alive query
 */
package com.sibvisions.rad.persist.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

import javax.rad.model.datatype.BigDecimalDataType;
import javax.rad.persist.ColumnMetaData;
import javax.rad.persist.DataSourceException;

import com.sibvisions.util.log.ILogger.LogLevel;
import com.sibvisions.util.type.CommonUtil;

/**
 * The <code>DB2DBAccess</code> is the implementation for DB2 databases.<br>
 *  
 * @see com.sibvisions.rad.persist.jdbc.DBAccess
 * 
 * @author Roland Hörmann
 */
public class DB2DBAccess extends DBAccess
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constants
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The select statement to get the Synonyms. */
	private static String sSynonymSelect = "select t.base_tabschema, t.base_tabname " +
										   "FROM syscat.tables t " +
										   "WHERE t.type='A' AND t.tabname = ?";
	
	/** the select statement for alive check. */
	private static String sAliveSelect = "select 1 from SYSIBM.SYSDUMMY1";
	
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Constructs a new DB2DBAccess Object.
	 */
	public DB2DBAccess()
	{
		setDriver("com.ibm.db2.jcc.DB2Driver");	
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void open() throws DataSourceException
	{
		//JDBC driver correct error messages
		setDBProperty("retrieveMessagesFromServerOnGetMessage", "true");

		super.open();
	}	
	
	/* 
	 * {@inheritDoc}
	 */
	@Override
	public void setUsername(String pUsername)
	{
		super.setUsername(removeQuotes(pUsername));
	}	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Map<String, Object> getDefaultValuesIntern(String pCatalog, String pSchema, String pTable) throws DataSourceException
	{
		return super.getDefaultValuesIntern(pCatalog, pSchema, pTable.toUpperCase());
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	protected TableInfo getTableInfoIntern(String pWriteBackTable) throws DataSourceException
	{	
		TableInfo tableInfo = super.getTableInfoIntern(pWriteBackTable);
		
		return new TableInfo(null, tableInfo.getSchema(), tableInfo.getTable());
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	protected List<ForeignKey> getForeignKeysIntern(String pCatalog, String pSchema, String pTable) throws DataSourceException
	{
		List<ForeignKey> lFKs = super.getForeignKeysIntern(pCatalog, pSchema, pTable);
		
		for (int i = 0; i < lFKs.size(); i++)
		{
			ForeignKey fk = lFKs.get(i);
			fk.setPKCatalog(null);
		}
			
		return lFKs;
	}	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getTableForSynonymIntern(String pSynonym) throws DataSourceException
	{
		PreparedStatement psResult = null;
		ResultSet rsResultSet = null;
		try
		{	
			long lMillis = System.currentTimeMillis();
			
			psResult = getPreparedStatement(sSynonymSelect);
			
			psResult.setString(1, removeQuotes(pSynonym).toUpperCase());
			rsResultSet = psResult.executeQuery();

			if (!rsResultSet.next())
			{
				return pSynonym;
			}
			
			// Create base_tabschema.base_tabname
			String sSchema = rsResultSet.getString("BASE_TABSCHEMA");
			String sTable  = rsResultSet.getString("BASE_TABNAME");

			StringBuilder sRealTable = new StringBuilder();
			
			if (sSchema != null)
			{
				sRealTable.append(sSchema.trim());					
				sRealTable.append('.');
			}

			sRealTable.append(sTable.trim());					
			
			if (isLogEnabled(LogLevel.DEBUG))
			{
			    debug("getTableForSynonym(", pSynonym, ") in ", Long.valueOf((System.currentTimeMillis() - lMillis)), "ms");
			}
			
			return sRealTable.toString();
		}
		catch (SQLException sqlException)
		{
			throw new DataSourceException("Synonyms couldn't determined from database! - " + pSynonym, formatSQLException(sqlException));
		}		
		finally
		{
		    CommonUtil.close(rsResultSet, psResult);
		}			
	}
	
	/**
	 * {@inheritDoc} 
	 */
	@Override
	protected void initializeDataType(ResultSetMetaData pResultSetMetaData, int pResultSetColumnIndex, ColumnMetaData pColumnMetaData) throws SQLException, DataSourceException
	{
		if (pResultSetMetaData.getColumnType(pResultSetColumnIndex) == Types.OTHER) // for DECFLOAT type == Oracle's NUMBER ; scale/precision 0,-1 
		{
			pColumnMetaData.setTypeIdentifier(BigDecimalDataType.TYPE_IDENTIFIER);
			pColumnMetaData.setPrecision(0);
			pColumnMetaData.setScale(-1);
			pColumnMetaData.setSigned(pResultSetMetaData.isSigned(pResultSetColumnIndex)); 
		}
		else
		{
			super.initializeDataType(pResultSetMetaData, pResultSetColumnIndex, pColumnMetaData);
		}
	}
	
	/**
	 * {@inheritDoc} 
	 */
	@Override
    public String getAliveQuery()
    {
        return sAliveSelect;
    }
	
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValidIdentifierStart(String pName)
    {
        return pName.length() > 0 && (Character.isJavaIdentifierStart(pName.charAt(0)) || pName.charAt(0) == '#');
    }

	/**
     * {@inheritDoc}
     */
    @Override
    public boolean isValidIdentifier(String pName)
    {
        if (isValidIdentifierStart(pName))
        {
            for (int i = 1, count = pName.length(); i < count; i++)
            {
                if (!Character.isJavaIdentifierPart(pName.charAt(i)) && pName.charAt(i) != '#')
                {
                    return false;
                }
            }
            
            return true;
        }
        else
        {
            return false;
        }
    }

} 	// DB2DBAccess
