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
 * 04.11.2014 - [TL] - creation.
 * 11.11.2014 - [TL] - Primary Keys are now get from the MetaData.
 */
package com.sibvisions.rad.persist.jdbc;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.rad.persist.DataSourceException;

import com.sibvisions.rad.persist.jdbc.ServerMetaData.PrimaryKeyType;
import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.GroupHashtable;
import com.sibvisions.util.log.ILogger.LogLevel;
import com.sibvisions.util.type.CommonUtil;
import com.sibvisions.util.type.StringUtil;

/**
 * The <code>HanaDBAccess</code> is the implementation for SAP HANA databases.<br>
 * 
 * @see com.sibvisions.rad.persist.jdbc.DBAccess
 * @author Thomas Lehner
 */
public class HanaDBAccess extends DBAccess
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constants
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** Cache of <code>Sequences</code>'s to improve performance. */
	private static GroupHashtable<String, String, String>  ghtSequenceCache    = new GroupHashtable<String, String, String>();

	/** the datatype mapping. */
	private static HashMap<String, Integer>                hmpDataTypes        = new HashMap<String, Integer>();
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	static
	{
	    hmpDataTypes.put("ALPHANUM", Integer.valueOf(Types.VARCHAR));
	    hmpDataTypes.put("BIGINT", Integer.valueOf(Types.BIGINT));
	    hmpDataTypes.put("BINARY", Integer.valueOf(Types.BINARY));
	    hmpDataTypes.put("BINTEXT", Integer.valueOf(Types.BINARY));
	    hmpDataTypes.put("BLOB", Integer.valueOf(Types.BLOB));
	    hmpDataTypes.put("CHAR", Integer.valueOf(Types.CHAR));
	    hmpDataTypes.put("CLOB", Integer.valueOf(Types.CLOB));
	    hmpDataTypes.put("DATE", Integer.valueOf(Types.DATE));
	    hmpDataTypes.put("DECIMAL", Integer.valueOf(Types.DECIMAL));
	    hmpDataTypes.put("DOUBLE", Integer.valueOf(Types.DOUBLE));
	    hmpDataTypes.put("INTEGER", Integer.valueOf(Types.INTEGER));
	    hmpDataTypes.put("NCHAR", Integer.valueOf(Types.OTHER));
	    hmpDataTypes.put("NCLOB", Integer.valueOf(Types.OTHER));
	    hmpDataTypes.put("NVARCHAR", Integer.valueOf(Types.OTHER));
	    hmpDataTypes.put("REAL", Integer.valueOf(Types.REAL));
	    hmpDataTypes.put("SECONDDATE", Integer.valueOf(Types.OTHER));
	    hmpDataTypes.put("SHORTTEXT", Integer.valueOf(Types.VARCHAR));
	    hmpDataTypes.put("SMALLDECIMAL", Integer.valueOf(Types.DECIMAL));
	    hmpDataTypes.put("SMALLINT", Integer.valueOf(Types.SMALLINT));
	    hmpDataTypes.put("ST_GEOMETRY", Integer.valueOf(Types.OTHER));
	    hmpDataTypes.put("ST_POINT", Integer.valueOf(Types.OTHER));
	    hmpDataTypes.put("ST_POINTZ", Integer.valueOf(Types.OTHER));
	    hmpDataTypes.put("TEXT", Integer.valueOf(Types.VARCHAR));
	    hmpDataTypes.put("TIME", Integer.valueOf(Types.TIME));
	    hmpDataTypes.put("TIMESTAMP", Integer.valueOf(Types.TIMESTAMP));
	    hmpDataTypes.put("TINYINT", Integer.valueOf(Types.TINYINT));
	    hmpDataTypes.put("VARBINARY", Integer.valueOf(Types.VARBINARY));
	    hmpDataTypes.put("VARCHAR", Integer.valueOf(Types.VARCHAR));
	}
	
	/**
	 * Constructs a new HanaDBAccess Object.
	 */
	public HanaDBAccess()
	{
		setDriver("com.sap.db.jdbc.Driver");
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] insert(String pWriteBackTable, ServerMetaData pServerMetaData, Object[] pNewDataRow) throws DataSourceException
	{
		checkIsOpen();

		if (pWriteBackTable == null)
		{
			throw new DataSourceException("Missing WriteBackTable!");
		}

		String columnId = null;
		String sequenceName = null;
		BigDecimal nextval = null;

		StringBuilder sInsertStatement = new StringBuilder("INSERT INTO ");
		sInsertStatement.append(pWriteBackTable);
		sInsertStatement.append(" (");

		// add column names to insert
		int iColumnCount = 0;
		String sDummyColumn = null;

		String[] saAutoIncrement;

		PreparedStatement psColumnId = null;
		PreparedStatement psSequenceName = null;
		PreparedStatement psGetNextVal = null;
		
		ResultSet rsColumnId = null;
		ResultSet rsSequenceName = null;
		ResultSet rsGetNextVal = null;
		
		try
		{
			psColumnId = getPreparedStatement("select column_id from table_columns where table_name = ? and column_name = ?");
			psSequenceName = getPreparedStatement("select sequence_name from sequences where sequence_name like ?");

			TableInfo tinfo = getTableInfoIntern(pWriteBackTable);

			saAutoIncrement = getAutoIncrementColumns(tinfo.getSchema(), pWriteBackTable);
			
			for (String columnName : saAutoIncrement)
			{
				int index = pServerMetaData.getServerColumnMetaDataIndex(columnName);

				if (pNewDataRow[index] == null)
				{
					String dbAccessIdentifier = getIdentifier();
					String tableIdentifier = createIdentifier(tinfo.getSchema(), tinfo.getTable());

					sequenceName = ghtSequenceCache.get(dbAccessIdentifier, tableIdentifier);

					if (sequenceName == null)
					{
						psColumnId.setString(1, removeQuotes(pWriteBackTable));
						psColumnId.setString(2, columnName);
						rsColumnId = psColumnId.executeQuery();
						
						if (rsColumnId.next())
						{
							columnId = rsColumnId.getString(1);
						}

						psSequenceName.setString(1, "%" + columnId + "%");
						rsSequenceName = psSequenceName.executeQuery();
						
						if (rsSequenceName.next())
						{
							sequenceName = rsSequenceName.getString(1);
						}
						
						if (sequenceName != null)
						{
							ghtSequenceCache.put(dbAccessIdentifier, tableIdentifier, sequenceName);
						}
					}
					
					psGetNextVal = getPreparedStatement("select " + sequenceName + ".nextval from dummy");
					
					rsGetNextVal = psGetNextVal.executeQuery();
					if (rsGetNextVal.next())
					{
						nextval = rsGetNextVal.getBigDecimal(1);
					}
					pNewDataRow[index] = nextval;
				}
			}
		}
		catch (Exception e)
		{
			debug("Can't find sequence for ", pWriteBackTable, e);
		}
		finally
		{
			CommonUtil.close(rsColumnId, psColumnId, rsSequenceName, psSequenceName, rsGetNextVal, psGetNextVal);
		}

		ServerColumnMetaData[] cmdServerColumnMetaData = pServerMetaData.getServerColumnMetaData();
		int[] iaWriteables = pServerMetaData.getWritableColumnIndices();

		for (int i = 0; i < iaWriteables.length; i++)
		{
			if (pNewDataRow[iaWriteables[i]] != null)
			{
				if (iColumnCount > 0)
				{
					sInsertStatement.append(", ");
				}
				sInsertStatement.append(cmdServerColumnMetaData[iaWriteables[i]].getColumnName().getQuotedName());
				iColumnCount++;
			}
		}

		if (iColumnCount == 0)
		{
			// if no storable columns, put in a dummy one
			for (int i = 0; iColumnCount == 0 && i < iaWriteables.length; i++)
			{
				if (!cmdServerColumnMetaData[iaWriteables[i]].isAutoIncrement())
				{
					sDummyColumn = cmdServerColumnMetaData[iaWriteables[i]].getColumnName().getQuotedName();
					sInsertStatement.append(sDummyColumn);
					iColumnCount++;
				}
			}
		}

		// Add values '?' to insert
		sInsertStatement.append(") VALUES (");

		if (iColumnCount > 0)
		{
			sInsertStatement.append("?");

			for (int i = 1; i < iColumnCount; i++)
			{
				sInsertStatement.append(",?");
			}
		}
		sInsertStatement.append(")");

		pNewDataRow = insertDatabaseSpecific(pWriteBackTable, sInsertStatement.toString(), pServerMetaData, pNewDataRow, sDummyColumn);

		setModified(Boolean.TRUE);

		if (isLogEnabled(LogLevel.DEBUG))
		{
			debug(sInsertStatement, "[", pNewDataRow, "]");
		}

		// NO PK, and all columns are null -> Exception -> but shouldn't happen
		// if we want import
		// empty records via API calls
		if (pServerMetaData.getPrimaryKeyType() != PrimaryKeyType.AllColumns)
		{
			// check Empty PK and throw Exception
			boolean bPKEmpty = true;
			int[] iPKColsIndices = pServerMetaData.getPrimaryKeyColumnIndices();

			if (iPKColsIndices != null)
			{
				for (int i = 0; i < iPKColsIndices.length && bPKEmpty; i++)
				{
					if (pNewDataRow[iPKColsIndices[i]] != null)
					{
						bPKEmpty = false;
					}
				}
			}

			if (bPKEmpty)
			{
				throw new DataSourceException("Primary key column empty after insert! " + pWriteBackTable);
			}
		}
		
		return pNewDataRow;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] insertDatabaseSpecific(String pWriteBackTable, String pInsertStatement, ServerMetaData pServerMetaData,
			                               Object[] pNewDataRow, String pDummyColumn) throws DataSourceException
	{
		return insertHana(pWriteBackTable, pInsertStatement, pServerMetaData, pNewDataRow, pDummyColumn);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected TableInfo getTableInfoIntern(String pWriteBackTable) throws DataSourceException
	{
		TableInfo tableInfo = super.getTableInfoIntern(pWriteBackTable);

		String schema;
		
		schema = tableInfo.getSchema();
		
		if (schema == null)
		{
			PreparedStatement psSchema = null;
			ResultSet rsSchema = null;

			try
			{
				psSchema = getPreparedStatement("select CURRENT_SCHEMA from dummy");
				rsSchema = psSchema.executeQuery();
				if (rsSchema.next())
				{
					schema = rsSchema.getString(1);
				}
			}
			catch (SQLException se)
			{
				throw new DataSourceException("Can't select the current Schema!", formatSQLException(se));
			}
			finally
			{
				CommonUtil.close(rsSchema, psSchema);
			}

		}
		
		return new TableInfo(tableInfo.getCatalog(), schema, tableInfo.getTable());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
    protected String getColumnName(ResultSetMetaData pMetaData, int pColumn) throws SQLException
    {
    	return pMetaData.getColumnLabel(pColumn);
    }
	
    /**
     * {@inheritDoc}
     */
    @Override
    protected Map<String, Object> getDefaultValuesIntern(String pCatalog, String pSchema, String pTable) throws DataSourceException
    {
        PreparedStatement ps = null; 
        ResultSet res = null;
        
        try
        {
            ps = getPreparedStatement("SELECT COLUMN_NAME, DATA_TYPE_NAME, DEFAULT_VALUE FROM SYS.TABLE_COLUMNS " +
                    "WHERE SCHEMA_NAME = ? AND TABLE_NAME= ?"); 
            ps.setString(1, pSchema);
            ps.setString(2, pTable);
            
            res = ps.executeQuery();
            
            Map<String, Object> htDefaults = new Hashtable<String, Object>();

            while (res.next())
            {
            	String sValue = res.getString("DEFAULT_VALUE");
                
                if (!StringUtil.isEmpty(sValue))
                {
                	String sColumnName = res.getString("COLUMN_NAME");
                    
                    try
                    {
                    	Object objValue = translateDefaultValue(sColumnName, convertType(res.getString("DATA_TYPE_NAME")), sValue.trim());
                        
                        if (objValue != null)
                        {
                            htDefaults.put(new Name(sColumnName, quote(sColumnName)).getName(), objValue); // Use Name class, to have one point of change.
                        }
                    }
                    catch (Exception e)
                    {
                        // no default value
                        //debug(sValue, e);
                    }
                }
            }
            
            return htDefaults;
        }
        catch (SQLException sqle)
        {
            throw new DataSourceException("Get default values failed!", formatSQLException(sqle));
        }
        finally
        {
            CommonUtil.close(res, ps);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected List<Key> getUniqueKeysIntern(String pCatalog, String pSchema, String pTable) throws DataSourceException
    {
        long lMillis = System.currentTimeMillis();
        
        PreparedStatement ps = null;
        ResultSet res = null;
        
        try
        {
            ArrayUtil<Key> auResult = new ArrayUtil<Key>();

            ps = getPreparedStatement("SELECT CONSTRAINT_NAME, COLUMN_NAME FROM SYS.CONSTRAINTS " +
                    "WHERE SCHEMA_NAME = ? AND TABLE_NAME = ? ORDER BY CONSTRAINT_NAME, POSITION");
            ps.setString(1, pSchema);
            ps.setString(2, pTable);
            
            res = ps.executeQuery();
            
            ArrayUtil<Name> auColumns = new ArrayUtil<Name>();
            
            String sColumn;
            String sUKName = null;
            
            while (res.next())
            {
                if (sUKName != null && !res.getString("CONSTRAINT_NAME").equals(sUKName))
                {
                    Key uk = new Key(sUKName, auColumns.toArray(new Name[auColumns.size()]));
                    
                    auResult.add(uk);
                    auColumns.clear();
                }
                
                sColumn = res.getString("COLUMN_NAME");
                
                auColumns.add(new Name(sColumn, quote(sColumn)));
                
                sUKName = res.getString("CONSTRAINT_NAME");
            }
            
            if (auColumns.size() > 0)
            {
                auResult.add(new Key(sUKName, auColumns.toArray(new Name[auColumns.size()])));
            }
            
            if (isLogEnabled(LogLevel.DEBUG))
            {
                debug("getUKs(", pTable, ") in ", Long.valueOf(System.currentTimeMillis() - lMillis), "ms");
            }
            
            return auResult;
        }
        catch (SQLException sqle)
        {
            error("Unique Keys couldn't determined from database! - ", pTable, sqle);
            
            return null;
        }
        finally
        {
            CommonUtil.close(res, ps);
        }
    }    

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<ForeignKey> getForeignKeysIntern(String pCatalog, String pSchema, String pTable) throws DataSourceException
    {
        long lMillis = System.currentTimeMillis();

        PreparedStatement ps = null;
        ResultSet res = null;
        
        try
        {
            ArrayUtil<ForeignKey> auForeignKeys = new ArrayUtil<ForeignKey>();
            
            ps = getPreparedStatement("SELECT CONSTRAINT_NAME, COLUMN_NAME, REFERENCED_SCHEMA_NAME, REFERENCED_TABLE_NAME, REFERENCED_COLUMN_NAME " +
                    "FROM SYS.REFERENTIAL_CONSTRAINTS " +
                  "WHERE SCHEMA_NAME = ? AND TABLE_NAME = ? ORDER BY CONSTRAINT_NAME, POSITION");
            ps.setString(1, pSchema);
            ps.setString(2, pTable);
            
            res = ps.executeQuery();
            
            ArrayUtil<Name> auPKColumns = new ArrayUtil<Name>();
            ArrayUtil<Name> auFKColumns = new ArrayUtil<Name>();
            
            String sRefSchema = null;
            String sRefTable = null;
            String sFKName = null;
            
            ForeignKey fkForeignKey;
            
            while (res.next())
            {
                if (sFKName != null && !res.getString("CONSTRAINT_NAME").equals(sFKName))
                {
                    fkForeignKey = new ForeignKey(new Name(sRefTable, quote(sRefTable)), 
                                                  null, 
                                                  new Name(sRefSchema, quote(sRefSchema)));
                    fkForeignKey.setFKName(sFKName);
                    fkForeignKey.setFKColumns(auFKColumns.toArray(new Name[auFKColumns.size()]));
                    fkForeignKey.setPKColumns(auPKColumns.toArray(new Name[auPKColumns.size()]));
                    
                    auForeignKeys.add(fkForeignKey);
                    
                    auPKColumns.clear();            
                    auFKColumns.clear();    
                }

                auPKColumns.add(new Name(res.getString("REFERENCED_COLUMN_NAME"), quote(res.getString("REFERENCED_COLUMN_NAME"))));
                auFKColumns.add(new Name(res.getString("COLUMN_NAME"), quote(res.getString("COLUMN_NAME"))));

                sRefSchema = res.getString("REFERENCED_SCHEMA_NAME");
                sRefTable  = res.getString("REFERENCED_TABLE_NAME");
                sFKName    = res.getString("CONSTRAINT_NAME");
            }
            
            if (auPKColumns.size() > 0)
            {
                fkForeignKey = new ForeignKey(new Name(sRefTable, quote(sRefTable)), 
                                              null, 
                                              new Name(sRefSchema, quote(sRefSchema)));
                fkForeignKey.setFKName(sFKName);
                fkForeignKey.setFKColumns(auFKColumns.toArray(new Name[auFKColumns.size()]));
                fkForeignKey.setPKColumns(auPKColumns.toArray(new Name[auPKColumns.size()]));
                    
                auForeignKeys.add(fkForeignKey);
            }
            
            if (isLogEnabled(LogLevel.DEBUG))
            {
                debug("getFKs(", pTable, ") in ", Long.valueOf(System.currentTimeMillis() - lMillis), "ms");
            }
            
            return auForeignKeys;
        }
        catch (SQLException sqlException)
        {
            throw new DataSourceException("Foreign Keys couldn't determined from database! - " + pTable, formatSQLException(sqlException));
        }
        finally
        {
            CommonUtil.close(res, ps);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void commitOrRollbackBeforeClose()
    {
        try
        {
            Connection con = getConnectionIntern();

            //Hana throws an Exception if you call commit if autocommit is enabled
            if (!con.getAutoCommit())
            {
                con.rollback();
            }
        }
        catch (SQLException sqle)
        {
            debug(sqle);
        }
    }
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Returns the newly inserted row from a SAP Hana Database. <br>
	 * It gets the next value for all autogenerated Columns and sets them
	 * manually.
	 * 
	 * @param pWriteBackTable the table to use for the insert
	 * @param pInsertStatement the SQL Statement to use for the insert
	 * @param pServerMetaData the meta data to use.
	 * @param pNewDataRow the new row (Object[]) with the values to insert
	 * @param pDummyColumn true, if all writeable columns are null, but for a
	 *            correct INSERT it have
	 *            to be minimum one column to use in the syntax.
	 * @return the newly inserted row from a Ansi SQL Database.
	 * @throws DataSourceException
	 *             if an <code>Exception</code> occur during insert to the
	 *             storage
	 */
	protected Object[] insertHana(String pWriteBackTable, String pInsertStatement, ServerMetaData pServerMetaData,
			                      Object[] pNewDataRow, String pDummyColumn) throws DataSourceException
	{
		PreparedStatement psInsert = null;

		try
		{
			psInsert = getPreparedStatement(pInsertStatement);
			
			if (getTransactionTimeout() >= 0)
			{
				try
				{
					psInsert.setQueryTimeout(getTransactionTimeout());
				}
				catch (Throwable ex)
				{
					// Ignore not implemented Exceptions.
				}
			}

			ServerColumnMetaData[] cmdServerColumnMetaData = pServerMetaData.getServerColumnMetaData();
			int[] iaWriteables = pServerMetaData.getWritableColumnIndices();

			if (pDummyColumn == null)
			{
				setColumnsToStore(psInsert, cmdServerColumnMetaData, iaWriteables, pNewDataRow, null);
			}
			else
			{
				// set null value for it!
				for (int i = 0; i < cmdServerColumnMetaData.length; i++)
				{
					if (cmdServerColumnMetaData[i].getColumnName().getQuotedName().equals(pDummyColumn))
					{
						psInsert.setObject(1, null, cmdServerColumnMetaData[i].getSQLType());
						break;
					}
				}
			}
			if (psInsert.executeUpdate() == 1)
			{
				return pNewDataRow;
			}
			
			throw new DataSourceException("Insert failed! - Result row count != 1" + pInsertStatement);
		}
		catch (SQLException sqlException)
		{
			throw new DataSourceException("Insert failed! - " + pInsertStatement, formatSQLException(sqlException));
		}
		finally
		{
			CommonUtil.close(psInsert);
		}
	}
	
	/**
	 * Returns all auto_incremenent columns for this table.
	 * 
	 * @param pSchema the schema to use.
	 * @param pTableName the table to use.
	 * @return all auto_incremenent columns for this table.
	 * @throws Exception if the auto increment columns couldn't determined.
	 */
	private String[] getAutoIncrementColumns(String pSchema, String pTableName) throws Exception
	{
		ArrayUtil<String> auAutoIncrement = new ArrayUtil<String>();

		PreparedStatement psAutoIncColumns = null;
		ResultSet rsAutoIncColumns = null;

		try
		{
			String sTableNameNoQuote = DBAccess.removeQuotes(pTableName);

			psAutoIncColumns = getPreparedStatement(
					"select column_name from table_columns where generation_type = 'BY DEFAULT AS IDENTITY' and table_name = ? and schema_name = ?");
			psAutoIncColumns.setString(1, sTableNameNoQuote);
			psAutoIncColumns.setString(2, pSchema);

			rsAutoIncColumns = psAutoIncColumns.executeQuery();

			while (rsAutoIncColumns.next())
			{
				String incColumn = rsAutoIncColumns.getString(1);

				if (incColumn != null)
				{
					auAutoIncrement.add(incColumn);
				}
			}
		}
		finally
		{
			CommonUtil.close(rsAutoIncColumns, psAutoIncColumns);
		}

		String[] saAutoIncrement = new String[auAutoIncrement.size()];
		return auAutoIncrement.toArray(saAutoIncrement);
	}	
	
    /**
     * Gets the standard JDBC type for the given Hana Data type (table: DATA_TYPES).
     *  
     * @param pTypeName the data type name
     * @return the standard JDBC type or {@link Types#OTHER} if type is not convertable
     * @see Types
     */
    private int convertType(String pTypeName)
    {
        Integer iValue = hmpDataTypes.get(pTypeName);
        
        if (iValue == null)
        {
            return Types.OTHER;
        }
        else
        {
            return iValue.intValue();
        }
    }
	
}	// HanaDBAccess
