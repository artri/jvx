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
 * 20.02.2020 - [DJ] - #2207: is alive query
 */
package com.sibvisions.rad.persist.jdbc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.rad.model.condition.ICondition;
import javax.rad.persist.DataSourceException;

import com.sibvisions.rad.persist.jdbc.param.AbstractParam;
import com.sibvisions.rad.persist.jdbc.param.OutParam;
import com.sibvisions.rad.server.protocol.ICategoryConstants;
import com.sibvisions.rad.server.protocol.ICommandConstants;
import com.sibvisions.rad.server.protocol.ProtocolFactory;
import com.sibvisions.rad.server.protocol.Record;
import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.log.ILogger.LogLevel;
import com.sibvisions.util.type.CommonUtil;
/**
 * The <code>AbstractOracleDBAccess</code> is the base implementation for Oracle databases.<br>
 *  
 * @see com.sibvisions.rad.persist.jdbc.DBAccess
 * 
 * @author Roland Hörmann
 */
public abstract class AbstractOracleDBAccess extends DBAccess
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constants
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	// The ultimate Oracle Meta Data Optimization!
	
	/** The select statement to get the Unique keys in Oracle. */
	private static String sConstraintSelect = "SELECT c.constraint_name " +
												   ",c.constraint_type " + 
												   ",c.r_owner " + 
												   ",c.r_constraint_name " +
												   ",c.search_condition " +
									        "FROM all_constraints c " +
									       "WHERE c.owner = ? " +
									         "AND c.table_name = ? " +
									         "AND c.constraint_type in (?, ?, ?, ?) " +
									         "AND (c.constraint_type != 'C' or c.constraint_name not like 'SYS_C%')";
			
	/** The select statement to get the columns from an constraint in Oracle. */
	private static String sConstraintColumnsSelect = "SELECT c.column_name " +
	                                                       ",c.table_name " +
													   "FROM all_cons_columns c " +
													  "WHERE c.owner = ? " +
													    "AND c.constraint_name = ? " +
													  "ORDER BY c.position"; 
	
	/** the select statement to get the Check constraints in Oracle. */
	private static String sDefaultValueSelect = "select c.column_name, c.data_type, c.data_default " +
	                                       "from all_tab_columns c " +
	                                      "where c.owner = ? " +
	                                        "and c.table_name = ? " +
	                                        "and c.data_default is not null";
	
	/** the select statement for alive check. */
	private static String sAliveSelect = "select 1 from dual";
    
	/** The cached table identifier. */
	protected String cachedKeyIdentifier = null;
	/** The cached table identifier. */
	protected String cachedReason = null;
	/** The cached primary key. */
	protected Key cachedPrimaryKey = null;
	/** The cached unique key. */
	protected List<Key> cachedUniqueKeys = null;
	/** The cached foreign key. */
	protected List<ForeignKey> cachedForeignKeys = null;
	/** The cached check constraints. */
	protected Hashtable<String, List<String>> cachedFoundValues = null;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Abstract methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Converts arrays to {@link List} of {@link javax.rad.type.bean.IBean}.
     * @param pParam the param to check
     * @return the param or a list in case of array.
     * @throws SQLException the exception
     */
    protected abstract Object convertArrayToList(Object pParam) throws SQLException;
    
    /**
     * Converts list or array to oracle arrays.
     * @param pParam the param to check
     * @return the param or a list in case of array.
     * @throws SQLException the exception
     */
    protected abstract Object convertToArray(AbstractParam pParam) throws SQLException;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDatabaseSpecificLockStatement(String pWritebackTable, ServerMetaData pServerMetaData, ICondition pPKFilter) throws DataSourceException
	{
		return super.getDatabaseSpecificLockStatement(pWritebackTable, pServerMetaData, pPKFilter) + " NOWAIT";											
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] insertDatabaseSpecific(String pWriteBackTable, String pInsertStatement, ServerMetaData pServerMetaData, 
                                           Object[] pNewDataRow, String pDummyColumn)  throws DataSourceException
    {
		return insertOracle(pWriteBackTable, pInsertStatement, pServerMetaData, pNewDataRow, pDummyColumn);
    }
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void open() throws DataSourceException
	{
		super.open();

		Statement stmt = null;
		
		try
		{
			stmt = getConnectionIntern().createStatement();
			
			stmt.executeUpdate("ALTER SESSION SET NLS_COMP='BINARY'");
			stmt.executeUpdate("ALTER SESSION SET NLS_SORT='BINARY'");
		}
		catch (SQLException ex)
		{
			// Try silent to change nls_sort, nls_comp
		}
		finally
		{
		    CommonUtil.close(stmt);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Connection getConnection() throws SQLException
	{
		cachedKeyIdentifier = null; // Clear temporary meta data, in case the statement changes ddl.
		return super.getConnection();
	}
	
	/**
     * {@inheritDoc}
     */
    @Override
    protected String getAliveQuery()
    {
        return sAliveSelect;
    }

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected List<Key> getUniqueKeysIntern(String pCatalog, 
        					  			    String pSchema, 
        					  			    String pTable) throws DataSourceException
	{
		getAndStoreKeysIntern(pCatalog, pSchema, pTable, "U");
		
		return cachedUniqueKeys;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Key getPrimaryKeyIntern(String pCatalog, String pSchema, String pTable) throws DataSourceException
	{
		getAndStoreKeysIntern(pCatalog, pSchema, pTable, "P");
		
		return cachedPrimaryKey;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected List<ForeignKey> getForeignKeysIntern(String pCatalog, String pSchema, String pTable) throws DataSourceException
	{
        getAndStoreKeysIntern(pCatalog, pSchema, pTable, "R");
		
		return cachedForeignKeys;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Map<String, Object> getDefaultValuesIntern(String pCatalog, String pSchema, String pTable) throws DataSourceException
	{
		PreparedStatement psDefaultValues = null;
		
		ResultSet resDefaultValues = null;
		
		Hashtable<String, Object> htDefaultValues = null;
		
		try
		{
			long lMillis = System.currentTimeMillis();

			psDefaultValues = getPreparedStatement(sDefaultValueSelect);
			psDefaultValues.setString(1, removeQuotes(pSchema));
			psDefaultValues.setString(2, removeQuotes(pTable));
			
			resDefaultValues = psDefaultValues.executeQuery();
			
			//detect all possible values
			
			while (resDefaultValues.next())
			{
				String columnName = resDefaultValues.getString(1);
				String dataType = resDefaultValues.getString(2);
				int type = Types.VARCHAR;
				if (dataType.contains("DATE") || dataType.contains("TIME") || dataType.contains("INTERVAL"))
				{
					type = Types.DATE;
				}
				else if (dataType.contains("NUMBER") || dataType.contains("FLOAT") || dataType.contains("INTEGER"))
				{
					type = Types.DECIMAL;
				}
				String value = resDefaultValues.getString(3);
					
				try
				{
					Object objValue = translateDefaultValue(columnName, type, value.trim());

					if (objValue != null)
					{
		                if (htDefaultValues == null)
		                {
		                    htDefaultValues = new Hashtable<String, Object>();
		                }

		                htDefaultValues.put(columnName, objValue);
					}
				}
				catch (Exception e)
				{
					//no default value
					//debug(value, e);
				}
			}			
			
			if (isLogEnabled(LogLevel.DEBUG))
            {
                debug("getDefaultValuesIntern(", pTable, ") in ", Long.valueOf((System.currentTimeMillis() - lMillis)), "ms");
            }
			
			return htDefaultValues;
		}
		catch (SQLException sqle)
		{
			throw new DataSourceException("Can't access check constraints for: '" + pTable + "'", formatSQLException(sqle));
		}
		finally
		{
		    CommonUtil.close(resDefaultValues, psDefaultValues);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Map<String, Object[]> getAllowedValuesIntern(String pCatalog, String pSchema, String pTable) throws DataSourceException
	{
		getAndStoreKeysIntern(pCatalog, pSchema, pTable, "C");
		
		if (!createIdentifier(pTable, null, null).equalsIgnoreCase(cachedMetaDataIdentifier) 
				&& !createIdentifier(pSchema + "." + pTable, null, null).equalsIgnoreCase(cachedMetaDataIdentifier))
		{
			if (pTable.indexOf(' ') >= 0 || pTable.indexOf('\t') >= 0 || pTable.indexOf('\n') >= 0 || pTable.indexOf('\r') >= 0
					|| pTable.indexOf('(') >= 0 || pTable.indexOf(')') >= 0)
			{
				getAndStoreMetaDataIntern(pTable, null, null, null, null);
			}
			else
			{
				getAndStoreMetaDataIntern(translateQuotes(quote(pSchema) + "." + quote(pTable)), null, null, null, null);
			}
		}

		return CheckConstraintSupport.translateValues(this, cachedColumnMetaData, cachedFoundValues);
	}
  
    /** 
     * {@inheritDoc}
     */
    @Override
    protected TableInfo getTableInfoIntern(String pWriteBackTable) throws DataSourceException
    {   
        TableInfo tableInfo = super.getTableInfoIntern(pWriteBackTable);

        String schema = tableInfo.getSchema();
        if (schema == null)
        {
            schema = getUsername().toUpperCase();
        }
        
        return new TableInfo(tableInfo.getCatalog(), schema, tableInfo.getTable());
    }
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean detectModified()
	{
		try 
		{
			return executeSql("select dbms_transaction.local_transaction_id from dual").get(0) != null;
		}
		catch (SQLException e)
		{
			return super.detectModified();
		}
	}
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected Object convertValueToDatabaseSpecificObject(AbstractParam pParam) throws SQLException
    {
        return convertValueToDatabaseSpecificObject(convertToArray(pParam));
    }
	
    /**
     * {@inheritDoc}
     */
    @Override
    public Object executeFunction(String pFunctionName, OutParam pReturnOutParam, Object... pParameters) throws SQLException
    {
        Record record = ProtocolFactory.openRecord(ICategoryConstants.DATABASE, ICommandConstants.DB_EXEC_FUNCTION);
        
        try
        {
            if (pParameters == null)
            {
                pParameters = new Object[1];
            }
            
            if (record != null)
            {
                if (pParameters != null && pParameters.length > 0)
                {
                    record.setParameter(pFunctionName, pParameters);
                }
                else
                {
                    record.setParameter(pFunctionName);
                }
            }
            
            checkIsOpen();
            
            StringBuilder sqlDeclare = new StringBuilder();
            StringBuilder sqlStatement = new StringBuilder("begin ? := ");
            StringBuilder sqlRetrieve = new StringBuilder();
            
            if (pReturnOutParam.getSqlType() == Types.BOOLEAN)
            {
                sqlStatement.append("sys.diutil.bool_to_int(");
            }
            sqlStatement.append(pFunctionName);
            int[] outBoolIndexes = new int[0];
            
            if (pParameters != null && pParameters.length > 0)
            {
                sqlStatement.append("(");
                for (int i = 0; i < pParameters.length; i++)
                {
                    if (i > 0)
                    {
                        sqlStatement.append(", ");          
                    }
                    if (pParameters[i] instanceof AbstractParam)
                    {
                        AbstractParam apParam = (AbstractParam)pParameters[i];
                        
                        if (AbstractParam.isOutParam(apParam.getType())
                                && apParam.getSqlType() == Types.BOOLEAN)
                        {
                            if (sqlDeclare.length() == 0)
                            {
                                sqlDeclare.append("declare ");
                            }
                            String boolName = "bool" + outBoolIndexes.length;

                            sqlStatement.append(boolName);
                            sqlDeclare.append(boolName).append(" boolean := sys.diutil.int_to_bool(?); ");
                            sqlRetrieve.append("? := sys.diutil.bool_to_int(").append(boolName).append(");");
                            outBoolIndexes = ArrayUtil.add(outBoolIndexes, i);
                        }
                        else if (apParam.getSqlType() == Types.BOOLEAN || apParam.getValue() instanceof Boolean)
                        {
                            sqlStatement.append("sys.diutil.int_to_bool(?)");
                        }
                        else
                        {
                            sqlStatement.append("?");
                        }
                    }
                    else if (pParameters[i] instanceof Boolean)
                    {
                        sqlStatement.append("sys.diutil.int_to_bool(?)");
                    }
                    else
                    {
                        sqlStatement.append("?");
                    }
                }
                sqlStatement.append(")");
            }
            if (pReturnOutParam.getSqlType() == Types.BOOLEAN)
            {
                sqlStatement.append(")");
            }
            sqlStatement.append("; ");
            
            sqlStatement.insert(0, sqlDeclare);
            sqlStatement.append(sqlRetrieve);
            sqlStatement.append(" end;");
            
            String sStmt = translateQuotes(sqlStatement.toString());
            debug("executeFunction -> ", sStmt, pParameters);

            CallableStatement call = null;
            
            try
            {
                call = getConnection().prepareCall(translateQuotes(sqlStatement.toString()));
                if (pReturnOutParam.getSqlType() == Types.BOOLEAN)
                {
                    call.registerOutParameter(outBoolIndexes.length + 1, Types.INTEGER);
                }
                else
                {
                    if (pReturnOutParam.getTypeName() != null)
                    {
                        call.registerOutParameter(outBoolIndexes.length + 1, pReturnOutParam.getSqlType(), pReturnOutParam.getTypeName());
                    }
                    else
                    {
                        call.registerOutParameter(outBoolIndexes.length + 1, pReturnOutParam.getSqlType());
                    }
                }
                
                int boolIndex;
                
                if (pParameters != null)
                {
                    boolIndex = 0;
                    for (int i = 0; i < pParameters.length; i++)
                    {
                        int index = i + outBoolIndexes.length - boolIndex + 2; 
                        if (boolIndex < outBoolIndexes.length && outBoolIndexes[boolIndex] == i)
                        {
                            call.registerOutParameter(pParameters.length + boolIndex + 2, Types.INTEGER);
                            Object val = ((AbstractParam)pParameters[i]).getValue();
                            if (val == null)
                            {
                                call.setNull(boolIndex + 1, Types.INTEGER);
                            }
                            else
                            {
                                int bool = 0;
                                if ((val instanceof Boolean && ((Boolean)val).booleanValue())
                                        || (val instanceof Number && ((Number)val).intValue() != 0)
                                        || (val instanceof String && Boolean.valueOf((String)val).booleanValue()))
                                {
                                    bool = 1;
                                }
                                        
                                call.setObject(boolIndex + 1, Integer.valueOf(bool));
                            }
    
                            boolIndex++;
                        }
                        else if (pParameters[i] == null)
                        {
                            call.setNull(index, Types.VARCHAR);
                        }
                        else
                        {
                            if (pParameters[i] instanceof AbstractParam)
                            {
                                AbstractParam apParam = (AbstractParam)pParameters[i];
        
                                if (AbstractParam.isOutParam(apParam.getType()))
                                {
                                    if (apParam.getTypeName() != null)
                                    {
                                        call.registerOutParameter(index, apParam.getSqlType(), apParam.getTypeName());
                                    }
                                    else
                                    {
                                        call.registerOutParameter(index, apParam.getSqlType());
                                    }
                                }
                                
                                if (apParam.getValue() == null)
                                {
                                    if (apParam.getTypeName() != null)
                                    {
                                        call.setNull(index, apParam.getSqlType(), apParam.getTypeName());
                                    }
                                    else if (apParam.getSqlType() == Types.BOOLEAN)
                                    {
                                        call.setNull(index, Types.INTEGER);
                                    }
                                    else
                                    {
                                        call.setNull(index, apParam.getSqlType());
                                    }
                                }
                                else if (apParam.getSqlType() == Types.BOOLEAN || apParam.getValue() instanceof Boolean)
                                {
                                    Object val = apParam.getValue();
    
                                    int bool = 0;
                                    if ((val instanceof Boolean && ((Boolean)val).booleanValue())
                                            || (val instanceof Number && ((Number)val).intValue() != 0)
                                            || (val instanceof String && Boolean.valueOf((String)val).booleanValue()))
                                    {
                                        bool = 1;
                                    }
    
                                    call.setObject(index, Integer.valueOf(bool), Types.INTEGER);
                                }
                                else if (apParam.getSqlType() == AbstractParam.SQLTYPE_AUTOMATIC)
                                {
                                    call.setObject(index, convertValueToDatabaseSpecificObject(apParam));
                                }
                                else
                                {
                                    call.setObject(index, convertValueToDatabaseSpecificObject(apParam), apParam.getSqlType());
                                }
                            }
                            else if (pParameters[i] instanceof Boolean)
                            {
                                call.setObject(index, ((Boolean)pParameters[i]).booleanValue() ? Integer.valueOf(1) : Integer.valueOf(0));
                            }
                            else
                            {
                                call.setObject(index, convertValueToDatabaseSpecificObject(pParameters[i]));
                            }
                        }
                    }
                }

                if (call.execute())
                {
                    CommonUtil.close(call.getResultSet());
                }
                
                Object oResult = call.getObject(outBoolIndexes.length + 1);
                
                if (pReturnOutParam.getSqlType() == Types.BOOLEAN && oResult != null)
                {
                    oResult = Boolean.valueOf(((Number)oResult).intValue() != 0);
                }
                
                if (pParameters != null)
                {
                    boolIndex = 0;
                    for (int i = 0; i < pParameters.length; i++)
                    {
                        int index = i + outBoolIndexes.length - boolIndex + 2; 
                        if (boolIndex < outBoolIndexes.length && outBoolIndexes[boolIndex] == i)
                        {
                            AbstractParam apParam = (AbstractParam)pParameters[i];
    
                            Object val = call.getObject(pParameters.length + boolIndex + 2);
                            
                            if (val instanceof Number)
                            {
                                apParam.setValue(Boolean.valueOf(((Number)val).intValue() != 0));
                            }
                            else
                            {
                                apParam.setValue(null);
                            }
    
                            boolIndex++;
                        }
                        else if (pParameters[i] instanceof AbstractParam)
                        {
                            AbstractParam apParam = (AbstractParam)pParameters[i];
                            
                            if (AbstractParam.isOutParam(apParam.getType()))
                            {
                                apParam.setValue(convertArrayToList(call.getObject(index)));
                            }
                        }
                    }
                }
                
                pReturnOutParam.setValue(convertArrayToList(oResult));
                
                return pReturnOutParam.getValue();
            }
            finally 
            {
                CommonUtil.close(call);
            }
        }
        catch (Exception e)
        {
        	throw newSQLException(record, e);
        }
        finally
        {
            CommonUtil.close(record);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void executeProcedure(String pProcedureName, Object... pParameters) throws SQLException
    {
        Record record = ProtocolFactory.openRecord(ICategoryConstants.DATABASE,  ICommandConstants.DB_EXEC_PROCEDURE);
        
        try
        {
            if (pParameters == null)
            {
                pParameters = new Object[1];
            }
            
            if (record != null)
            {
                if (pParameters != null && pParameters.length > 0)
                {
                    record.setParameter(pProcedureName, pParameters);
                }
                else
                {
                    record.setParameter(pProcedureName);
                }
            }
            
            checkIsOpen();
            
            StringBuilder sqlDeclare = new StringBuilder();
            StringBuilder sqlStatement = new StringBuilder("begin ");
            StringBuilder sqlRetrieve = new StringBuilder(); 
            sqlStatement.append(pProcedureName);
            int[] outBoolIndexes = new int[0];
            
            if (pParameters != null && pParameters.length > 0)
            {
                sqlStatement.append("(");
                for (int i = 0; i < pParameters.length; i++)
                {
                    if (i > 0)
                    {
                        sqlStatement.append(", ");          
                    }
                    if (pParameters[i] instanceof AbstractParam)
                    {
                        AbstractParam apParam = (AbstractParam)pParameters[i];
                        
                        if (AbstractParam.isOutParam(apParam.getType())
                                && apParam.getSqlType() == Types.BOOLEAN)
                        {
                            if (sqlDeclare.length() == 0)
                            {
                                sqlDeclare.append("declare ");
                            }
                            String boolName = "bool" + outBoolIndexes.length;

                            sqlStatement.append(boolName);
                            sqlDeclare.append(boolName).append(" boolean := sys.diutil.int_to_bool(?); ");
                            sqlRetrieve.append("? := sys.diutil.bool_to_int(").append(boolName).append(");");
                            outBoolIndexes = ArrayUtil.add(outBoolIndexes, i);
                        }
                        else if (apParam.getSqlType() == Types.BOOLEAN || apParam.getValue() instanceof Boolean)
                        {
                            sqlStatement.append("sys.diutil.int_to_bool(?)");
                        }
                        else
                        {
                            sqlStatement.append("?");
                        }
                    }
                    else if (pParameters[i] instanceof Boolean)
                    {
                        sqlStatement.append("sys.diutil.int_to_bool(?)");
                    }
                    else
                    {
                        sqlStatement.append("?");
                    }
                }
                sqlStatement.append(")");
            }
            sqlStatement.append("; ");

            sqlStatement.insert(0, sqlDeclare);
            sqlStatement.append(sqlRetrieve);
            sqlStatement.append(" end;");
            
            String sStmt = translateQuotes(sqlStatement.toString());
            debug("executeProcedure -> ", sStmt, pParameters);
            
            CallableStatement call = null;
            
            try
            {
                call = getConnection().prepareCall(translateQuotes(sStmt));
                
                int boolIndex;
    
                if (pParameters != null)
                {
                    boolIndex = 0;
                    for (int i = 0; i < pParameters.length; i++)
                    {
                        int index = i + outBoolIndexes.length - boolIndex + 1; 
                        if (boolIndex < outBoolIndexes.length && outBoolIndexes[boolIndex] == i)
                        {
                            call.registerOutParameter(pParameters.length + boolIndex + 1, Types.INTEGER);
                            Object val = ((AbstractParam)pParameters[i]).getValue();
                            if (val == null)
                            {
                                call.setNull(boolIndex + 1, Types.INTEGER);
                            }
                            else
                            {
                                int bool = 0;
                                if ((val instanceof Boolean && ((Boolean)val).booleanValue())
                                        || (val instanceof Number && ((Number)val).intValue() != 0)
                                        || (val instanceof String && Boolean.valueOf((String)val).booleanValue()))
                                {
                                    bool = 1;
                                }
                                        
                                call.setObject(boolIndex + 1, Integer.valueOf(bool));
                            }
    
                            boolIndex++;
                        }
                        else if (pParameters[i] == null)
                        {
                            call.setNull(index, Types.VARCHAR);
                        }
                        else
                        {
                            if (pParameters[i] instanceof AbstractParam)
                            {
                                AbstractParam apParam = (AbstractParam)pParameters[i];
        
                                if (AbstractParam.isOutParam(apParam.getType()))
                                {
                                    if (apParam.getTypeName() != null)
                                    {
                                        call.registerOutParameter(index, apParam.getSqlType(), apParam.getTypeName());
                                    }
                                    else
                                    {
                                        call.registerOutParameter(index, apParam.getSqlType());
                                    }
                                }
                                
                                if (apParam.getValue() == null)
                                {
                                    if (apParam.getTypeName() != null)
                                    {
                                        call.setNull(index, apParam.getSqlType(), apParam.getTypeName());
                                    }
                                    else if (apParam.getSqlType() == Types.BOOLEAN)
                                    {
                                        call.setNull(index, Types.INTEGER);
                                    }
                                    else
                                    {
                                        call.setNull(index, apParam.getSqlType());
                                    }
                                }
                                else if (apParam.getSqlType() == Types.BOOLEAN || apParam.getValue() instanceof Boolean)
                                {
                                    Object val = apParam.getValue();
    
                                    int bool = 0;
                                    if ((val instanceof Boolean && ((Boolean)val).booleanValue())
                                            || (val instanceof Number && ((Number)val).intValue() != 0)
                                            || (val instanceof String && Boolean.valueOf((String)val).booleanValue()))
                                    {
                                        bool = 1;
                                    }
    
                                    call.setObject(index, Integer.valueOf(bool), Types.INTEGER);
                                }
                                else if (apParam.getSqlType() == AbstractParam.SQLTYPE_AUTOMATIC)
                                {
                                    call.setObject(index, convertValueToDatabaseSpecificObject(apParam));
                                }
                                else
                                {
                                    call.setObject(index, convertValueToDatabaseSpecificObject(apParam), apParam.getSqlType());
                                }
                            }
                            else if (pParameters[i] instanceof Boolean)
                            {
                                call.setObject(index, ((Boolean)pParameters[i]).booleanValue() ? Integer.valueOf(1) : Integer.valueOf(0));
                            }
                            else
                            {
                                call.setObject(index, convertValueToDatabaseSpecificObject(pParameters[i]));
                            }
                        }
                    }
                }
                
                call.execute();
                
                if (pParameters != null)
                {
                    boolIndex = 0;
                    for (int i = 0; i < pParameters.length; i++)
                    {
                        int index = i + outBoolIndexes.length - boolIndex + 1; 
                        if (boolIndex < outBoolIndexes.length && outBoolIndexes[boolIndex] == i)
                        {
                            AbstractParam apParam = (AbstractParam)pParameters[i];
    
                            Object val = call.getObject(pParameters.length + boolIndex + 1);
                            
                            if (val instanceof Number)
                            {
                                apParam.setValue(Boolean.valueOf(((Number)val).intValue() != 0));
                            }
                            else
                            {
                                apParam.setValue(null);
                            }
    
                            boolIndex++;
                        }
                        else if (pParameters[i] instanceof AbstractParam)
                        {
                            AbstractParam apParam = (AbstractParam)pParameters[i];
                            
                            if (AbstractParam.isOutParam(apParam.getType()))
                            {
                                apParam.setValue(convertArrayToList(call.getObject(index)));
                            }
                        }
                    }
                }
            }
            finally 
            {
                CommonUtil.close(call);
            }
        }
        catch (Exception e)
        {
        	throw newSQLException(record, e);
        }
        finally
        {
            CommonUtil.close(record);
        }
    }   

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Returns the newly inserted row from an Oracle Database. <br>
	 * It uses RETURNING .. INTO to get the primary key values back from the database. 
	 * 
	 * @param pTablename		the table to use for the insert
	 * @param pInsertStatement	the SQL Statement to use for the insert
	 * @param pServerMetaData	the meta data to use.
	 * @param pNewDataRow		the new IDataRow with the values to insert
	 * @param pDummyColumn		true, if all writeable columns are null, but for a correct INSERT it have
	 *                          to be minimum one column to use in the syntax.
	 * @return the newly inserted row from an Oracle Database.
	 * @throws DataSourceException
	 *             if an <code>Exception</code> occur during insert the <code>IDataRow</code> 
	 *             to the storage
	 */	
	protected Object[] insertOracle(String pTablename, String pInsertStatement, ServerMetaData pServerMetaData, 
			                        Object[] pNewDataRow, String pDummyColumn)  throws DataSourceException
	{
		int[] pPKColumnIndices = pServerMetaData.getPrimaryKeyColumnIndices();

		boolean returnPK = false;
		
		if (pPKColumnIndices != null)
		{
			// If there is at least one empty (null) PK column, we will return
			// all PK values.
			for (int i = 0; i < pPKColumnIndices.length && !returnPK; i++)
			{
				returnPK = (pNewDataRow[pPKColumnIndices[i]] == null);
			}
		}
	
		if (returnPK)
		{
			StringBuffer sInsertStatement = new StringBuffer("BEGIN " + pInsertStatement);
			
			// use RETURNING to get all PK column values filled in in from the trigger
			sInsertStatement.append(" RETURNING ");
			
			for (int i = 0; pPKColumnIndices != null && i < pPKColumnIndices.length; i++)
			{
				if (i > 0)
				{
					sInsertStatement.append(", ");
				}
				
				sInsertStatement.append(pServerMetaData.getServerColumnMetaData(pPKColumnIndices[i]).getColumnName().getQuotedName());
			}
			sInsertStatement.append(" INTO ");
			
			for (int i = 0; pPKColumnIndices != null && i < pPKColumnIndices.length; i++)
			{
				if (i > 0)
				{
					sInsertStatement.append(", ");
				}
				sInsertStatement.append("?");
			}
		
			sInsertStatement.append("; END;");
			
			pInsertStatement = sInsertStatement.toString();
		}

		CallableStatement csInsert = null;
		try
		{
			// #436 - OracleDBAccess and PostgresDBAccess should translate JVx quotes in specific insert
			String sSQL = translateQuotes(pInsertStatement);
			debug("insertOracle -> ", sSQL);
			csInsert = getConnectionIntern().prepareCall(sSQL);
		
			ServerColumnMetaData[] cmdServerColumnMetaData = pServerMetaData.getServerColumnMetaData();
			int[] iaWriteables = pServerMetaData.getWritableColumnIndices();
			
			int iLastIndex = 0;
			if (pDummyColumn == null)
			{
				iLastIndex = setColumnsToStore(csInsert, cmdServerColumnMetaData, iaWriteables, pNewDataRow, null);
			}
			else
			{
				for (int i = 0; i < cmdServerColumnMetaData.length; i++)
				{
					if (cmdServerColumnMetaData[i].getColumnName().getQuotedName().equals(pDummyColumn))
					{
						csInsert.setObject(1, null, cmdServerColumnMetaData[i].getSQLType());
						break;
					}
				}					
				iLastIndex = 1;
			}
			
			if (returnPK)
			{
				for (int i = 0; pPKColumnIndices != null && i < pPKColumnIndices.length; i++)
				{
					int iSQLType = cmdServerColumnMetaData[pPKColumnIndices[i]].getColumnMetaData().getTypeIdentifier();
	
					csInsert.registerOutParameter(iLastIndex + i + 1, iSQLType);
				}
			}
			if (csInsert.executeUpdate() == 1)
			{
				// use RETURNING to get the PK column values filled in by the trigger
				// get the out parameters, and set the PK columns
				if (returnPK)
				{
					for (int i = 0; pPKColumnIndices != null && i < pPKColumnIndices.length; i++)
					{
						pNewDataRow[pPKColumnIndices[i]] = csInsert.getObject(iLastIndex + i + 1);
					}
				}
				return pNewDataRow;
			}
			throw new DataSourceException("Insert failed ! - Result row count != 1 ! - " +  pInsertStatement);
		}
		catch (SQLException sqlException)
		{			
			throw new DataSourceException("Insert failed! - " + pInsertStatement, formatSQLException(sqlException));
		}
		finally
		{
		    CommonUtil.close(csInsert);
		}
	}
	
	/**
	 * Gets the table name for a synonym.
	 * 
	 * @param pStatement the synonym metadata statement
	 * @param pSynonym the name of the synonym
	 * @return the name of the table or the <code>pSynonym</code> name if no synonym was found
	 * @throws DataSourceException if synonym detection failed
	 */
    protected String getTableForSynonymIntern(String pStatement, String pSynonym) throws DataSourceException
    {       
        PreparedStatement psResult = null;
        ResultSet rsResultSet = null;
        try
        {   
            long lMillis = System.currentTimeMillis();
            
            psResult = getPreparedStatement(pStatement);
            
            psResult.setString(1, removeQuotes(pSynonym));
            rsResultSet = psResult.executeQuery();
            if (!rsResultSet.next())
            {
                return pSynonym;
            }
            
            // Create schema.table@db_link
            String sSchema = rsResultSet.getString("TABLE_OWNER");
            String sTable  = rsResultSet.getString("TABLE_NAME");
            String sDBLink = rsResultSet.getString("DB_LINK");

            StringBuilder sRealTable = new StringBuilder();
            
            if (sSchema != null)
            {
                sRealTable.append(sSchema);                 
                sRealTable.append('.');                 
            }

            sRealTable.append(sTable);                  

            if (sDBLink != null)
            {
                sRealTable.append('@');                 
                sRealTable.append(sDBLink);                 
            }
            
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
	 * Gets all constraints at once, as it is faster in Oracle.
	 * 
	 * @param pCatalog the catalog
	 * @param pSchema the schema
	 * @param pTable the table
	 * @param pReason the reason
	 * @throws DataSourceException the exception
	 */
	protected void getAndStoreKeysIntern(String pCatalog, 
									    String pSchema, 
									    String pTable, String pReason) throws DataSourceException
	{
		String tableIdentifier = createIdentifier(pCatalog, pSchema, pTable);

		if (cachedKeyIdentifier == null || !cachedKeyIdentifier.equals(tableIdentifier)
				|| ("R".equals(pReason) && "R".equals(cachedReason)) || ("C".equals(pReason) && "C".equals(cachedReason)))
		{
            cachedKeyIdentifier = null;
			cachedReason = pReason;
			cachedPrimaryKey = null;
			cachedUniqueKeys = new ArrayUtil<Key>();
			cachedForeignKeys = new ArrayUtil<ForeignKey>();
			cachedFoundValues = null;
		
			ResultSet 		  rsConstraints = null;
			PreparedStatement psConstraints = null;
			ResultSet 		  rsColumns = null;
			PreparedStatement psColumns = null;
	
			try
			{	
				ArrayUtil<Name> auKeyColumns = new ArrayUtil<Name>();
				
				long lMillis = System.currentTimeMillis();
				
				psConstraints = getPreparedStatement(sConstraintSelect);
				psColumns = getPreparedStatement(sConstraintColumnsSelect);
	
				psConstraints.setString(1, removeQuotes(pSchema));
				psConstraints.setString(2, removeQuotes(pTable));
				psConstraints.setString(3, "P");
				psConstraints.setString(4, "U");
				psConstraints.setString(5, "RC".contains(pReason) ? "C" : "P");
				psConstraints.setString(6, "R".equals(pReason) ? "R" : "P");
				
				rsConstraints = psConstraints.executeQuery();
	
				while (rsConstraints.next())
				{
					String sConstraintType = rsConstraints.getString("CONSTRAINT_TYPE");
					
					if ("C".equals(sConstraintType))
					{
						String searchCondition = rsConstraints.getString("SEARCH_CONDITION");

						cachedFoundValues = CheckConstraintSupport.parseCondition(searchCondition, cachedFoundValues, true);
					}
					else
					{
						String sConstraintName = rsConstraints.getString("CONSTRAINT_NAME");

						auKeyColumns.clear();
						psColumns.setString(1, removeQuotes(pSchema));
						psColumns.setString(2, sConstraintName);
						rsColumns = psColumns.executeQuery();
						while (rsColumns.next())
						{
							auKeyColumns.add(new Name(rsColumns.getString("COLUMN_NAME"), 
									                        quote(rsColumns.getString("COLUMN_NAME"))));
						}
						CommonUtil.close(rsColumns);
						Name[] columns = auKeyColumns.toArray(new Name[auKeyColumns.size()]);
						
						if ("P".equals(sConstraintType))
						{
							cachedPrimaryKey = new Key(sConstraintName, columns);
						}
						else if ("U".equals(sConstraintType))
						{
							cachedUniqueKeys.add(new Key(sConstraintName, columns));
						}
						else if ("R".equals(sConstraintType))
						{
							String sCatalog = getConnectionIntern().getCatalog();
							String sROwner = rsConstraints.getString("R_OWNER");
							String sRConstraintName = rsConstraints.getString("R_CONSTRAINT_NAME");
							String sRTableName = null;
							
							auKeyColumns.clear();
							psColumns.setString(1, sROwner);
							psColumns.setString(2, sRConstraintName);				
							rsColumns = psColumns.executeQuery();
							while (rsColumns.next())
							{
								if (sRTableName ==  null)
								{
									sRTableName = rsColumns.getString("TABLE_NAME");
								}
								auKeyColumns.add(new Name(rsColumns.getString("COLUMN_NAME"), 
				                        quote(rsColumns.getString("COLUMN_NAME"))));
							}
							CommonUtil.close(rsColumns);
							Name[] rColumns = auKeyColumns.toArray(new Name[auKeyColumns.size()]);
		
							ForeignKey fkForeignKey = new ForeignKey(new Name(sRTableName, quote(sRTableName)), 
																	 new Name(sCatalog, quote(sCatalog)), 
																	 new Name(sROwner, quote(sROwner)));
							fkForeignKey.setFKName(sConstraintName);
							fkForeignKey.setFKColumns(columns);
							fkForeignKey.setPKColumns(rColumns);
							
							cachedForeignKeys.add(fkForeignKey);
						}
					}
				}
	
				cachedKeyIdentifier = tableIdentifier;

	            if (isLogEnabled(LogLevel.DEBUG))
	            {
	                debug("getConstraints(", pTable, ") in ", Long.valueOf((System.currentTimeMillis() - lMillis)), "ms");
	            }
			}
			catch (SQLException sqlException)
			{
				throw new DataSourceException("Unique Keys couldn't determined from database! - " + pTable, formatSQLException(sqlException));
			}		
			finally
			{
			    CommonUtil.close(rsConstraints, psConstraints, rsColumns, psColumns);
			}
		}
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

} 	// OracleDBAccess
