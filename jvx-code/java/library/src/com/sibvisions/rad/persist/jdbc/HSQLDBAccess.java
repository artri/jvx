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
 * 13.05.2009 - [RH] - creation.
 * 23.11.2009 - [RH] - ColumnMetaData && PrimaryKey Column is replaced with MetaData class
 * 02.03.2010 - [RH] - reorganized MetaData -> ServerMetaData, ColumnMetaData -> ServerColumnMetaData
 * 27.03.2010 - [JR] - #92: default value support
 * 29.04.2010 - [JR] - #122: lockRowInternal: check db version
 * 08.05.2010 - [JR] - #127: db version check is not necessary because the db metadata has: supportsSelectForUpdate!
 * 19.11.2010 - [RH] - getUKs, getPKs return Type changed to a <code>Key</code> based result.                                      
 * 06.01.2011 - [JR] - #234: used ColumnMetaDataInfo 
 * 18.11.2011 - [RH] - #510: All XXDBAccess should provide a SQLException format method 
 * 16.01.2011 - [JR] - #537: get_column_name set to false
 * 15.05.2014 - [JR] - #1038: CommonUtil.close used   
 * 09.12.2019 - [JR] - #2128: support ignore-case conditions for non string columns
 * 20.02.2020 - [DJ] - #2207: is alive query
 */
package com.sibvisions.rad.persist.jdbc;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.rad.model.condition.CompareCondition;
import javax.rad.model.condition.LikeIgnoreCase;
import javax.rad.model.condition.LikeReverseIgnoreCase;
import javax.rad.model.datatype.BigDecimalDataType;
import javax.rad.model.datatype.StringDataType;
import javax.rad.persist.DataSourceException;

import com.sibvisions.rad.persist.jdbc.ServerMetaData.PrimaryKeyType;
import com.sibvisions.util.type.CommonUtil;

/**
 * The <code>HSQLDBAccess</code> is the implementation for Hypersonic SQL databases.<br>
 *  
 * @see com.sibvisions.rad.persist.jdbc.DBAccess
 * 
 * @author Roland Hörmann
 */
public class HSQLDBAccess extends DBAccess
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
	/** the select statement for alive check. */
	private static String sAliveSelect = "SELECT CURRENT_TIME AS now FROM (VALUES(0))";
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Constructs a new HSQLDBAccess Object.
	 */
	public HSQLDBAccess()
	{
		setDriver("org.hsqldb.jdbcDriver");		
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
		setDBProperty("get_column_name", "false");
		
		super.open();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Key getPrimaryKeyIntern(String sCatalog, 
         	              String sSchema, 
                          String pTable) throws DataSourceException
    {
		return super.getPrimaryKeyIntern(sCatalog, sSchema, getWritebackTable(pTable));
    }
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected List<ForeignKey> getForeignKeysIntern(String sCatalog, 
      	   						   String sSchema, 
      	   						   String pTable) throws DataSourceException
    {
		return super.getForeignKeysIntern(sCatalog, sSchema, getWritebackTable(pTable));		
    }
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected List<Key> getUniqueKeysIntern(String sCatalog, 
  			 					 String sSchema, 
  			 					 String pTable) throws DataSourceException
    {
		return super.getUniqueKeysIntern(sCatalog, sSchema, getWritebackTable(pTable));		    
    }
	
	/**
	 * {@inheritDoc}
	 */
	@Override		
	protected Object[] insertDatabaseSpecific(String pWritebackTable, String pInsertStatement, ServerMetaData pServerMetaData, 
            Object[] pNewDataRow, String pDummyColumn)  throws DataSourceException
    {
		pNewDataRow = insertAnsiSQL(pWritebackTable, pInsertStatement, pServerMetaData, pNewDataRow, pDummyColumn);
		return postInsert(pServerMetaData, pNewDataRow);
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
	protected String createWhereColumn(ServerMetaData pServerMetaData, CompareCondition pCompare, String pColumnName)
	{
		String sColumn = super.createWhereColumn(pServerMetaData, pCompare, pColumnName);

		//#2128
		if (pCompare instanceof LikeIgnoreCase
			|| pCompare instanceof LikeReverseIgnoreCase)
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
        return pName.length() > 0 && (Character.isJavaIdentifierStart(pName.charAt(0)) && pName.charAt(0) != '$');
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
                if (!Character.isJavaIdentifierPart(pName.charAt(i)) || pName.charAt(i) == '$')
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

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Returns the WritebackTable as toUpper, because of the meta data bug in HSQL.
	 *  
	 * @param pWritebackTable	the write back tabel to use.
	 * @return the WritebackTable as toUpper, because of the meta data bug in HSQL.
	 */
	public String getWritebackTable(String pWritebackTable)
	{	
		if (pWritebackTable != null)
		{
			if (removeQuotes(pWritebackTable).equals(pWritebackTable))
			{
				return pWritebackTable.toUpperCase();
			}
			return pWritebackTable;
		}
		return null;
    }
	
	/**
	 * Hypersonic SQL specific implementation to get the auto increment value of a PK.
	 * This database supports only one auto increment column in the PK, and doesn't
	 * support the jdbc .getGeneratedKeys() method.
	 * 
	 * @param pServerMetaData	the meta data to use.
	 * @param pNewDataRow		the new IDataRow with the values to insert
	 * @return the new inserted row with the new PK value.
	 * @throws DataSourceException
	 *            if an <code>Exception</code> occur during insert to the storage
	 */
	private Object[] postInsert(ServerMetaData pServerMetaData, Object[] pNewDataRow) throws DataSourceException
	{
		int[] primaryKeyColumnIndexes = pServerMetaData.getPrimaryKeyColumnIndices();
		
		// Refetch generated Primary Key, if a PK is defined
		if (pServerMetaData.getPrimaryKeyType() == PrimaryKeyType.PrimaryKeyColumns
				&& primaryKeyColumnIndexes != null)
		{		
			ServerColumnMetaData[] scmd = pServerMetaData.getServerColumnMetaData();
			int primaryKeyColumnIndex = -1;

			for (int i = 0; primaryKeyColumnIndex < 0 && i < primaryKeyColumnIndexes.length; i++)
			{
				if (scmd[primaryKeyColumnIndexes[i]].getTypeIdentifier() == BigDecimalDataType.TYPE_IDENTIFIER
						&& pNewDataRow[primaryKeyColumnIndexes[i]] == null)
				{
					primaryKeyColumnIndex = primaryKeyColumnIndexes[i];
				}
			}
			
			if (primaryKeyColumnIndex >= 0)
			{
				CallableStatement psSelectPK = null;
				ResultSet rsPK = null;
				try 
				{
					psSelectPK = getConnectionIntern().prepareCall("CALL IDENTITY()");		
		
					rsPK = psSelectPK.executeQuery();
	
					if (rsPK.next())
					{		
						// hsqlDB support only one generated Column, we decide it is the first in Primary Key
						if (pServerMetaData.getPrimaryKeyColumnIndices().length > 0)						
						{
		    				pNewDataRow[primaryKeyColumnIndex] = rsPK.getObject(1);						
						}
					}
					
					return pNewDataRow;
				}
				catch (SQLException sqlException)
				{
					throw new DataSourceException("The generated keys couldn't read!", formatSQLException(sqlException));
				}
				finally
				{
				    CommonUtil.close(rsPK, psSelectPK);
				}
			}
		}
		return pNewDataRow;
	}
		
} 	// HSQLDBAccess
