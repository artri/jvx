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
 * 30.05.2009 - [RH] - creation.
 * 23.11.2009 - [RH] - ColumnMetaData && PrimaryKey Column is replaced with MetaData class
 * 02.03.2010 - [RH] - reorganized MetaData -> ServerMetaData, ColumnMetaData -> ServerColumnMetaData
 * 27.03.2010 - [JR] - #92: default value support
 * 09.10.2010 - [JR] - #114: used CheckConstraintSupport to detect allowed values    
 * 28.12.2010 - [RH] - #230: quoting of all DB objects like columns, tables, views.  
 * 11.03.2011 - [RH] - #308: DB specific automatic quoting implemented   
 * 21.07.2011 - [RH] - #437: MSSQLDBAccess should translate quote in addMSSQLSpecificAutoIncSupport
 * 14.09.2011 - [JR] - #470: changed default schema detection                 
 * 15.05.2014 - [JR] - #1038: CommonUtil.close used                            
 */
package com.sibvisions.rad.persist.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.rad.persist.DataSourceException;

import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.log.ILogger.LogLevel;
import com.sibvisions.util.type.CommonUtil;

/**
 * The <code>MSSQLDBAccess</code> is the implementation for MS SQL databases.<br>
 *  
 * @see com.sibvisions.rad.persist.jdbc.DBAccess
 * 
 * @author Roland Hörmann
 */
public class MSSQLDBAccess extends DBAccess
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constants
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the select statement to get the Check constraings in MS SQL. */
	private static String sCheckSelect = "SELECT cu.TABLE_NAME, cu.COLUMN_NAME, c.CHECK_CLAUSE " +
                                           "FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS t, " +
                                                "INFORMATION_SCHEMA.CHECK_CONSTRAINTS c, " +
                                                "INFORMATION_SCHEMA.CONSTRAINT_COLUMN_USAGE cu " +
                                          "WHERE t.CONSTRAINT_TYPE = 'CHECK' " +
                                            "AND t.CONSTRAINT_SCHEMA = c.CONSTRAINT_SCHEMA " +
                                            "AND t.CONSTRAINT_CATALOG = c.CONSTRAINT_CATALOG " +
                                            "AND t.CONSTRAINT_NAME = c.CONSTRAINT_NAME " +
                                            "AND t.TABLE_NAME = ? " +
                                            "AND t.TABLE_NAME = cu.TABLE_NAME " +
                                            "AND t.TABLE_CATALOG = cu.TABLE_CATALOG " +
                                            "AND t.TABLE_SCHEMA = cu.TABLE_SCHEMA " +
                                            "AND t.CONSTRAINT_SCHEMA = cu.CONSTRAINT_SCHEMA " +
                                            "AND t.CONSTRAINT_CATALOG = cu.CONSTRAINT_CATALOG " +
                                            "AND t.CONSTRAINT_NAME = cu.CONSTRAINT_NAME";
	
	/** The select statement to get the Synonyms. */
	private static String sSynonymSelect = "SELECT name, base_object_name " +
										   "FROM sys.synonyms t " +
										   "WHERE t.name = ?";	

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Constructs a new MSSQLDBAccess Object.
	 */
	public MSSQLDBAccess()
	{
		setDriver("net.sourceforge.jtds.jdbc.Driver"); 
		setQuoteCharacters("[", "]");
	}
		
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			
	/**
	 * It checks the auto increment column, if an value should be set, then it adds 
	 * SET IDENTITY_INSERT table ON/OFF to the specified statement.
	 * 
	 * @param pWritebackTable	the Writeback Table to use.
	 * @param pStatement		the SQL Statement to use.
	 * @param pServerMetaData	the <code>ServerMetaData</code> to use.
	 * @param pOld				the old row if it is an update statement, othersiwe null.
	 * @param pNew				the new row of the update/innsert statement.
	 * @return the MS SQL corrected Update/Insert statement.
	 */
	private String addMSSQLSpecificAutoIncSupport(String pWritebackTable, String pStatement, 
			ServerMetaData pServerMetaData, Object[] pOld, Object[] pNew)
	{
		// check auto increment column, if an value should be set, then use SET IDENTITY_INSERT table ON.
		int[] iaAutoIncrementCols = pServerMetaData.getAutoIncrementColumnIndices(); 
		for (int i = 0; iaAutoIncrementCols != null && i < iaAutoIncrementCols.length; i++)
		{
			if (pOld == null && pNew[iaAutoIncrementCols[i]] != null
					|| pOld != null && pNew[iaAutoIncrementCols[i]] != null 
					                && !pNew[iaAutoIncrementCols[i]].equals(pOld[iaAutoIncrementCols[i]]))
			{
				// #437 - MSSQLDBAccess should translate quote in addMSSQLSpecificAutoIncSupport
				return "SET IDENTITY_INSERT " + translateQuotes(pWritebackTable) + " ON " +
					   pStatement + 
					   " SET IDENTITY_INSERT " + translateQuotes(pWritebackTable) + " OFF";
			}
		}
		return pStatement;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] insertDatabaseSpecific(String pWritebackTable, String pInsertStatement, ServerMetaData pServerMetaData, 
            Object[] pNewDataRow, String pDummyColumn)  throws DataSourceException
    {
		pInsertStatement = addMSSQLSpecificAutoIncSupport(pWritebackTable, pInsertStatement, pServerMetaData, null, pNewDataRow);
		
		return insertAnsiSQL(pWritebackTable, pInsertStatement, pServerMetaData, pNewDataRow, pDummyColumn);		
    }
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Map<String, Object[]> getAllowedValuesIntern(String pCatalog, String pSchema, String pTable) throws DataSourceException
	{
		getAndStoreMetaDataIntern(pTable, null, null, null, null);
		
		PreparedStatement psCheck = null;
		
		ResultSet resCheck = null;
		
		Hashtable<String, Object[]> htAllowed = new Hashtable<String, Object[]>();
		
		Hashtable<String, List<String>> htFoundValues = null;		
		
		try
		{
			psCheck = getPreparedStatement(sCheckSelect);
			psCheck.setString(1, pTable);
			
			resCheck = psCheck.executeQuery();
			
			//detect all possible values

			while (resCheck.next())
			{
				//don't check if a single column constraint contains other columns!!!
				//allowed: (([ACTIVE]='N' OR [ACTIVE]='Y') AND [TEXT]='TEST')
				//
				//we collect the information from all constraints and put them together
				
				htFoundValues = CheckConstraintSupport.parseCondition(resCheck.getString(3), htFoundValues, false);			
			}

			// interpret values
			
			htAllowed = CheckConstraintSupport.translateValues(this, cachedColumnMetaData, htFoundValues);
		}
		catch (SQLException sqle)
		{
			throw new DataSourceException("Can't access check constraints for: '" + pTable + "'", formatSQLException(sqle));
		}
		finally
		{
		    CommonUtil.close(resCheck, psCheck);
		}
		
		return htAllowed;
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

	/* 
	 * {@inheritDoc}
	 */
	@Override
	public boolean isAutoQuote(String pName)
	{
		return !isValidIdentifier(pName);
	}	
	
	/* 
	 * {@inheritDoc}
	 */
	@Override
	public void setUsername(String pUsername)
	{
		super.setUsername(DBAccess.removeQuotes(pUsername));
	}
		
	/** 
	 * {@inheritDoc}
	 */
	@Override
	protected TableInfo getTableInfoIntern(String pWriteBackTable) throws DataSourceException
	{	
		TableInfo tableInfo = super.getTableInfoIntern(pWriteBackTable);
		
		String sTable = DBAccess.removeQuotes(tableInfo.getTable());

		// correct schemas detection, because of jdbc driver bug!
		// so the in the pFromClause was the schema specified, but the jdbc driver just return it as it is, so maybe it has the wrong case letters.
		String sSchema = tableInfo.getSchema();
		if (sSchema != null)
		{
			String sSchemaNoQuote = DBAccess.removeQuotes(sSchema);
			if (sSchemaNoQuote.equals(sSchema))
			{
				// ok not quoted schema, mssql is default lowerCase, so try that.
				sSchema = sSchema.toLowerCase();
			}
			else
			{
				// its quoted, just use it like it is without the quotes. Have to be correct!
				sSchema = sSchemaNoQuote;
			}
		}

		//important, because this method is called with and without table alias (TABLE and TABLE m)
		String[] sSchemaTable = splitSchemaTable(sTable);
		
		sTable = sSchemaTable[1];
		
		List<Object[]> tables = checkTablesAndViews(sSchema, sTable);

		if (tables.size() == 1)
		{
			return new TableInfo(tableInfo.getCatalog(), ((Name)tables.get(0)[0]).getRealName(), tableInfo.getTable());				
		}
		
		// more then one table with the same name exists. (different schema)
		// then use the default schema of the user - 99% correct.
		PreparedStatement psDefaultSchema = null;
		ResultSet rsDefaultSchema = null;
		long lStart = System.currentTimeMillis();
		try
		{
			//database_pricincipals were introduced with MSSQL 2005 (v 9) and were not available in MSSQL 2000 (v 8)
			if (getConnectionIntern().getMetaData().getDatabaseMajorVersion() > 8)
			{
				psDefaultSchema = getPreparedStatement(
						"SELECT default_schema_name FROM sys.database_principals WHERE type = 'S' AND name = USER_NAME(DATABASE_PRINCIPAL_ID())");
				rsDefaultSchema = psDefaultSchema.executeQuery();
				
				if (rsDefaultSchema.next())
				{
					sSchema = rsDefaultSchema.getString(1);
					if (sSchema != null)
					{
						tables = checkTablesAndViews(sSchema, sTable);
						if (tables.size() == 1)
						{
							// just one table , then use it.
							return new TableInfo(tableInfo.getCatalog(), ((Name)tables.get(0)[0]).getRealName(), tableInfo.getTable());	
						}
					}
				}
			}
			String sDefault = getDefaultSchema();
			
			if (sDefault != null)
			{
				return new TableInfo(tableInfo.getCatalog(), sDefault, tableInfo.getTable());
			}
			else
			{
				return new TableInfo(tableInfo.getCatalog(), "dbo", tableInfo.getTable());
			}
		}
		catch (SQLException e)
		{
			throw new DataSourceException("Jdbc statement close failed", formatSQLException(e));
		}						
		finally
		{
		    CommonUtil.close(rsDefaultSchema, psDefaultSchema);
    		
    		if (isLogEnabled(LogLevel.DEBUG))
    		{
    		    debug("getColumnMetaData() - getDefaultSchema in time=", Long.valueOf((System.currentTimeMillis() - lStart)));
    		}
		}
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
			
			psResult.setString(1, pSynonym);
			rsResultSet = psResult.executeQuery();
			
			if (!rsResultSet.next())
			{
			    CommonUtil.close(rsResultSet, psResult);
				
				return pSynonym;
			}
			
			String sFullQualifiedTableName  = rsResultSet.getString("BASE_OBJECT_NAME");				
			
			if (isLogEnabled(LogLevel.DEBUG))
			{
			    debug("getTableForSynonym(", pSynonym, ") in ", Long.valueOf((System.currentTimeMillis() - lMillis)), "ms");
			}
			
			return sFullQualifiedTableName;
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
	 * Returns the List of tables and views, if existing.
	 * Columns: Catalog, Schema, Name, Type("TABLE", "VIEW"), Comment
	 * 
	 * @param pSchema 		the schema to search for.
	 * @param pTable 		the table to search for.
	 * @return the List of tables.
	 * @throws DataSourceException	if the tables couldn't determined
	 */
	private List<Object[]> checkTablesAndViews(String pSchema, String pTable) throws DataSourceException
	{
		long lStart = System.currentTimeMillis();

		ResultSet rsTables = null;
		try
		{
			rsTables = getConnectionIntern().getMetaData().getTables(getConnectionIntern().getCatalog(), pSchema, pTable, new String [] { "TABLE", "VIEW" });
			
			List<Object[]> lTables = new ArrayUtil<Object[]>(); 
			
			while (rsTables.next())
			{
				String sSchema = rsTables.getString("TABLE_SCHEM");
				
				if (!sSchema.equals("sys")
						&& !sSchema.equals("INFORMATION_SCHEMA"))
				{
					lTables.add(new Object[] { new Name(rsTables.getString("TABLE_SCHEM"), quote(rsTables.getString("TABLE_SCHEM"))),
											   new Name(rsTables.getString("TABLE_NAME"), quote(rsTables.getString("TABLE_NAME")))});
				}
			}
			
			return lTables;
		}
		catch (SQLException e)
		{
			throw new DataSourceException("Jdbc statement close failed", formatSQLException(e));
		}						
		finally
		{
		    CommonUtil.close(rsTables);

    		if (isLogEnabled(LogLevel.DEBUG))
    		{
    		    debug("getTables() time=", Long.valueOf((System.currentTimeMillis() - lStart)));
    		}
		}
	}
	
} 	// MSSQLDBAccess
