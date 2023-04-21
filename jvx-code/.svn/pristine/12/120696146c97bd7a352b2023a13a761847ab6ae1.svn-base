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
 * 01.12.2009 - [RH] - #35: datetime (0000-00-00 00:00:00) workaround
 * 09.12.2009 - [JR] - open overwritten, call setDBProperty instead of setUrl
 * 10.03.2010 - [RH] - Alias detection in MySQL 5.1 broken - workaround added.
 *                     Ticket #82 fixed
 * 28.12.2010 - [RH] - #230: quoting of all DB objects like columns, tables, views. 
 * 11.03.2011 - [RH] - #308: DB specific automatic quoting implemented  
 * 25.03.2011 - [RH] - setUsername remove quotes if exists.      
 * 07.07.2011 - [RH] - #418: getPK, getUKs, getFKs, getDefaultValues() fails, because catalog and schema is mixed up in mysql   
 * 27.07.2011 - [RH] - #442: PK not found in DBStorage for MySQL DBs if the writeback table use schema prefix   
 * 20.11.2011 - [RH] - #512: MySQL return the SQLType wrong for TEXT columns
 * 17.12.2011 - [JR] - #526: enum and set datatype support           
 *                   - #528: createStorage
 * 15.05.2014 - [JR] - #1038: CommonUtil.close used     
 * 15.10.2018 - [JR] - #1956: ConnectorJ 8.x support                       
 */
package com.sibvisions.rad.persist.jdbc;

import java.math.BigDecimal;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.rad.model.SortDefinition;
import javax.rad.model.condition.ICondition;
import javax.rad.persist.ColumnMetaData;
import javax.rad.persist.DataSourceException;

import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.log.ILogger.LogLevel;
import com.sibvisions.util.type.CommonUtil;
import com.sibvisions.util.type.StringUtil;

/**
 * The <code>MySQLDBAccess</code> is the implementation for MySQL databases.<br>
 *  
 * @see com.sibvisions.rad.persist.jdbc.DBAccess
 * 
 * @author Roland Hörmann
 */
public class MySQLDBAccess extends DBAccess
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the enum datatype. */
	public static final int TYPE_ENUM = -900;

	/** the set datatype. */
	public static final int TYPE_SET = -901;

	/** the version number of the JDBC driver. */
	protected int iDriverVersion;

    /** the version number of the JDBC driver. */
	protected boolean bMariaDBDriver;

    /** Uses limit/ offset for fetch, to get faster query response. */
    private boolean limitFetchEnabled = true;
    
    /** The maximum fetch amount when using limit paging. */
    private int maximumFetchAmount = 10000;

    /** The maximum number of rows to fetch when doing an additional fetch. */
    private int maximumAdditionalFetchAmount = -1;
    
    /** 
     * The minimum fetch amount when using limit paging.
     * This is set to 60 to optimize read ahead for vaadin.
     * Vaadin tries to fetch 60 rows initially, and this prevents fetching twice. 
     */
    private int minimumFetchAmount = 60;
    
    /** detection, if the current query uses autoLimit. */
    private boolean autoLimit = false;
    /** detection, if the current query uses autoLimit. */
    private boolean firstCallOfParameterizedSelectStatement = false;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Constructs a new MySQLDBAccess Object.
	 */
	public MySQLDBAccess()
	{
		initDriver();
		
		setQuoteCharacters("`", "`"); //no quoting in MySQL, just write it case sensitive correctly - setQuoteCharacters("`", "`");
	}

	/**
	 * Initializes the driver.
	 */
	protected void initDriver()
	{
		try
		{
			//#1956
			setDriver("com.mysql.cj.jdbc.Driver");
		}
		catch (Exception e)
		{
		    try
		    {
		        setDriver("com.mysql.jdbc.Driver");
			}
			catch (Exception ex)
			{
			    setDriver("org.mariadb.jdbc.Driver");
			}
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
    /**
     * {@inheritDoc}
     */
    @Override
    public void setDriver(String pDriver)
    {
        try
        {
            Driver driver = (Driver)Class.forName(pDriver).newInstance();

            bMariaDBDriver = pDriver.contains("mariadb");
            iDriverVersion = driver.getMajorVersion();
            
            super.setDriver(pDriver);
        }
        catch (RuntimeException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }


	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void open() throws DataSourceException
	{
		configureBeforeOpen();
		
		if (bMariaDBDriver && iDriverVersion >= 3)
		{
		    String url = getUrl();
		    
		    if (url != null && !url.contains("permitMysqlScheme") && url.startsWith("jdbc:mysql:"))
		    {
		        url = url + (url.contains("?") ? "&" : "?") + "permitMysqlScheme";
		        
		        setUrl(url);
		    }
		}
		else
		{
	        setDBProperty("useOldAliasMetadataBehavior", "true");
		}
		
		super.open();
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
    protected String getColumnName(ResultSetMetaData pMetaData, int pColumn) throws SQLException
    {
        if (bMariaDBDriver && iDriverVersion >= 3)
        {
            return pMetaData.getColumnLabel(pColumn);
        }
        else
        {
            return pMetaData.getColumnName(pColumn);
        }
    }
	
	/* 
	 * {@inheritDoc}
	 */
	@Override
	protected ServerColumnMetaData[] getColumnMetaDataIntern(String pFromClause, 
            										     	 String[] pQueryColumns,
            										     	 String pBeforeQueryColumns, 
            										     	 String pWhereClause, 
            										     	 String pAfterWhereClause) throws DataSourceException
	{		
		ServerColumnMetaData[] scmd = super.getColumnMetaDataIntern(pFromClause, pQueryColumns, pBeforeQueryColumns, pWhereClause, pAfterWhereClause);
		
		int iPrecision;
		
		Hashtable<String, ColumnInfo> htColInf = null;
		
		for (int i = 0; i < scmd.length; i++)
		{
			int iType = scmd[i].getSQLType();
			
			if (iType == Types.LONGVARCHAR)
			{
				// #512 - SqlType TEXT is returned as VARCHAR.
				ColumnInfo cinf = null;

				//get detailed table information, if possible
				if (htColInf == null)
				{
					htColInf = getColumnInfo(pFromClause);
				}
				
				if (htColInf != null)
				{
					cinf = htColInf.get(scmd[i].getColumnName().getRealName());
					
					if (cinf != null)
					{
						scmd[i].setSQLTypeName(cinf.type);
					}
				}

				//Use precision to detect type 
				if (cinf == null)
				{
					iPrecision = scmd[i].getPrecision();
					
					if (iPrecision > 0xFFFFFF)
					{
						scmd[i].setSQLTypeName("LONGTEXT");
					}
					else if (iPrecision > 0xFFFF)
					{
						scmd[i].setSQLTypeName("MEDIUMTEXT");
					}
					else if (iPrecision > 0xFF)
					{
						scmd[i].setSQLTypeName("TEXT");
					}
					else
					{
						scmd[i].setSQLTypeName("TINYTEXT");
					}
				}
			}
			else if (iType == Types.CHAR)
			{
				//check ENUM or SET 
				
				ColumnInfo cinf = null;

				//get detailed table information, if possible
				if (htColInf == null)
				{
					htColInf = getColumnInfo(pFromClause);
				}
				
				if (htColInf != null)
				{
					cinf = htColInf.get(scmd[i].getColumnName().getRealName());
					
					if (cinf != null)
					{
						String sType = cinf.type.toLowerCase();
						
						if (sType.startsWith("enum"))
						{
							scmd[i].setDetectedType(TYPE_ENUM);
							scmd[i].setSQLTypeName(cinf.type);
						}
						else if (sType.startsWith("set"))
						{
							scmd[i].setDetectedType(TYPE_SET);
							scmd[i].setSQLTypeName(cinf.type);
						}
						if (scmd[i].getDetectedType() == TYPE_ENUM || scmd[i].getDetectedType() == TYPE_SET)
						{
							scmd[i].setAllowedValues(extractValues(scmd[i].getSQLTypeName()));
						}					
					}
				}
			}
		}
		
		return scmd;
    }
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	protected TableInfo getTableInfoIntern(String pWriteBackTable) throws DataSourceException
	{	
		TableInfo tableInfo = super.getTableInfoIntern(pWriteBackTable);
		
		// #442 - PK not found in DBStorage for MySQL DBs if the writeback table use schema prefix 
		// TODO ??? Table is already splitted in super class. If there is a "." it is in a view_name
		//      also the table can be null!
		String sTable = tableInfo.getTable();
		if (sTable != null && sTable.contains("."))
		{
			sTable = sTable.substring(sTable.lastIndexOf(".") + 1);
		}
		
		return new TableInfo(null, tableInfo.getCatalog(), sTable);
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	protected Key getPrimaryKeyIntern(String pCatalog, String pSchema, String pTable) throws DataSourceException
	{
		return super.getPrimaryKeyIntern(pSchema, null, pTable);
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	protected List<ForeignKey> getForeignKeysIntern(String pCatalog, String pSchema, String pTable) throws DataSourceException
	{
		List<ForeignKey> lFKs = super.getForeignKeysIntern(pSchema, null, pTable);
		
		for (int i = 0; i < lFKs.size(); i++)
		{
			ForeignKey fk = lFKs.get(i);
			fk.setPKSchema(fk.getPKCatalog());
			fk.setPKCatalog(null);
		}
			
		return lFKs;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	protected List<Key> getUniqueKeysIntern(String pCatalog, String pSchema, String pTable) throws DataSourceException
	{
		ResultSet rsResultSet = null;
		try
		{
			ArrayUtil<Key>  auResult           = new ArrayUtil<Key>();
			ArrayUtil<Name> auUniqueKeyColumns = new ArrayUtil<Name>();
			
			long lMillis = System.currentTimeMillis();
			DatabaseMetaData dbMetaData = getConnectionIntern().getMetaData();
			
			rsResultSet = dbMetaData.getIndexInfo(removeQuotes(pSchema), null, removeQuotes(pTable), true, false);
			
			if (!rsResultSet.next())
			{
			    CommonUtil.close(rsResultSet);
			    
				return auResult;
			}
			
			String sUKName = null;
			do
			{
				if (rsResultSet.getString("COLUMN_NAME") != null)
				{
					if (sUKName != null && !rsResultSet.getString("INDEX_NAME").equals(sUKName))
					{
						Key uk = new Key(sUKName, auUniqueKeyColumns.toArray(new Name[auUniqueKeyColumns.size()]));
						auResult.add(uk);
						auUniqueKeyColumns.clear();
					}
					sUKName = rsResultSet.getString("INDEX_NAME");
				
					auUniqueKeyColumns.add(new Name(rsResultSet.getString("COLUMN_NAME"), quote(rsResultSet.getString("COLUMN_NAME"))));
				}
			}
			while (rsResultSet.next());
			
			//[JR] #188
			if (auUniqueKeyColumns.size() > 0)
			{
				Key uk = new Key(sUKName, auUniqueKeyColumns.toArray(new Name[auUniqueKeyColumns.size()]));
				auResult.add(uk);
			}
			
			if (auResult.size() > 0)
			{
				// remove PKs, because a PK is also a index, but we don't wanna return them too.
				Key pk = getPrimaryKey(pCatalog, pSchema, pTable);
				if (pk != null)
				{
					for (int i = auResult.size() - 1; i >= 0; i--)
					{
						Name[] ukCols = auResult.get(i).getColumns();
						if (ArrayUtil.containsAll(ukCols, pk.getColumns()) && ukCols.length == pk.getColumns().length)
						{
							auResult.remove(i);
						}
					}
				}
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
		finally
		{
		    CommonUtil.close(rsResultSet);
		}			
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	protected Map<String, Object> getDefaultValuesIntern(String pCatalog, String pSchema, String pTable) throws DataSourceException
	{
		return super.getDefaultValuesIntern(pSchema, null, pTable);
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
	public List<Object[]> fetch(
			ServerMetaData pServerMetaData,
			String pBeforeQueryColumns,
			String[] pQueryColumns,
			String pFromClause,
			ICondition pFilter,
			String pWhereClause,
			String pAfterWhereClause,
			SortDefinition pSort,
			String pOrderByClause,
            String pAfterOrderByClause,
			int pFromRow,
			int pMinimumRowCount,
			boolean pAllowLazyFetch) throws DataSourceException
	{
        long start = System.currentTimeMillis();
        
        if (getMaxTime() > 0 && pFromRow > 0 && pMinimumRowCount > 0)
        {
            // If there is a max time set we will fetch dynamically more rows.
            // This is needed because of the MySQL only supports the limit
            // clause. Which means that every fetch afterwards will have to
            // reselect already fetched lines to get the new once.
            // The fetch time would accumulate to a quite big value, so we are
            // fetching *way* more rows to keep it low.
            pMinimumRowCount = Math.max(pMinimumRowCount, pFromRow); 
            
            if (maximumFetchAmount > 0 && pMinimumRowCount > maximumFetchAmount)
            {
                pMinimumRowCount = maximumFetchAmount;
            }
        }
        if (pMinimumRowCount > 2 && getMaxTime() > 0) // Only, if it is not a refetch row or row count.
        {
            pMinimumRowCount = Math.max(pMinimumRowCount, minimumFetchAmount); 
        }

        // No different explain plan in MySQL like it is in Postgres due to limit clause... 
//        if (limitFetchEnabled && pSort == null && StringUtil.isEmpty(pOrderByClause) && pServerMetaData.getPrimaryKeyColumnNames() != null
//              && pMinimumRowCount > 2) // Only, if it is not a refetch row or row count.
//        {
//            pSort = new SortDefinition(pServerMetaData.getPrimaryKeyColumnNames());
//        }
        
        firstCallOfParameterizedSelectStatement = true;
        List<Object[]> fetched = super.fetch(
                pServerMetaData,
                pBeforeQueryColumns,
                pQueryColumns,
                pFromClause,
                pFilter,
                pWhereClause,
                pAfterWhereClause,
                pSort,
                pOrderByClause,
                pAfterOrderByClause,
                pFromRow,
                pMinimumRowCount,
                pAllowLazyFetch);
        
        if (autoLimit
                && pMinimumRowCount > 2 // do not fetch further, as it is only for refetch row, row count or meta data
                && fetched.size() == pMinimumRowCount + 1
                && fetched.get(fetched.size() - 1) == null)
        {
            // Remove the null termination, there might be more rows to fetch.
            fetched.remove(fetched.size() - 1);
            
            long fetchTime = Math.max(1, System.currentTimeMillis() - start);
            
            if (// pMinimumRowCount > 2 && // do not fetch further, as it is only for refetch row, row count or meta data
                    // the check moved up as there should in general not be fetched afterwards
                fetchTime < getMaxTime())
            {
                // If the first fetch took less time than the maximum fetch
                // time, we will fetch more rows.
                int nextFetchRowCount = calculateAdditionalFetchRowCount(fetched.size(), fetchTime);
                
                if (maximumFetchAmount > 0 && nextFetchRowCount > maximumFetchAmount)
                {
                    nextFetchRowCount = maximumFetchAmount;
                }
                
                if (maximumAdditionalFetchAmount > 0 && nextFetchRowCount > maximumAdditionalFetchAmount)
                {
                    nextFetchRowCount = maximumAdditionalFetchAmount;
                }

                if (nextFetchRowCount > 0)
                {
                    List<Object[]> additionalFetched = super.fetch(
                            pServerMetaData,
                            pBeforeQueryColumns,
                            pQueryColumns,
                            pFromClause,
                            pFilter,
                            pWhereClause, 
                            pAfterWhereClause,
                            pSort,
                            pOrderByClause,
                            pAfterOrderByClause,
                            pFromRow + fetched.size(),
                            nextFetchRowCount,
                            pAllowLazyFetch);

                    // Check if the additional fetched data is null terminated
                    // even though there might be more data which could be fetched.
                    if (additionalFetched.size() == nextFetchRowCount + 1
                            && additionalFetched.get(additionalFetched.size() - 1) == null)
                    {
                        // Remove the null termination.
                        additionalFetched.remove(additionalFetched.size() - 1);
                    }
                    
                    fetched.addAll(additionalFetched);
                }
            }
        }
        
        return fetched;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ParameterizedStatement getParameterizedSelectStatement(
			ServerMetaData pServerMetaData,
			String pBeforeQueryColumns,
			String[] pQueryColumns,
			String pFromClause,
			ICondition pFilter,
			String pWhereClause,
			String pAfterWhereClause,
			SortDefinition pSort,
			String pOrderByClause,
			String pAfterOrderByClause,
			int pFromRow,
			int pMinimumRowCount) throws DataSourceException
	{
		ParameterizedStatement statement = super.getParameterizedSelectStatement(
				pServerMetaData,
				pBeforeQueryColumns,
				pQueryColumns,
				pFromClause,
				pFilter,
				pWhereClause,
				pAfterWhereClause,
				pSort,
				pOrderByClause,
				pAfterOrderByClause,
				pFromRow,
				pMinimumRowCount);
		
        boolean autoLimitDetect = limitFetchEnabled && !statement.getStatement().replaceAll("\\s+", " ").toLowerCase().contains(" limit ");
        if (autoLimitDetect)
        {
            if (pFromRow > 0 || pMinimumRowCount >= 0) // Only, if it is not a refetch row or row count.
            {
                statement.setStatement(statement.getStatement() + " limit ? offset ?");
                statement.getValues().add(BigDecimal.valueOf(pMinimumRowCount > 0 ? pMinimumRowCount : Integer.MAX_VALUE));
                statement.getValues().add(BigDecimal.valueOf(pFromRow > 0 ? pFromRow : 0));
            }
            else
            {
                autoLimitDetect = false;
            }
        }
        if (firstCallOfParameterizedSelectStatement)
        {
            firstCallOfParameterizedSelectStatement = false;
            autoLimit = autoLimitDetect;
        }
		
		return statement;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected int getDiscardRowCount(int pFromRow, int pMinimumRowCount)
	{
		return 0;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
    protected void initializeDataType(ResultSetMetaData pResultSetMetaData, int pResultSetColumnIndex, ColumnMetaData pColumnMetaData) throws SQLException, DataSourceException
    {
        super.initializeDataType(pResultSetMetaData, pResultSetColumnIndex, pColumnMetaData);
        
        switch(pResultSetMetaData.getColumnType(pResultSetColumnIndex))
        {
            case Types.DATE:
            case Types.TIME:
            case Types.TIMESTAMP:
            case TIME_WITH_TIMEZONE_V8:
            case TIMESTAMP_WITH_TIMEZONE:
            case TIMESTAMP_WITH_TIMEZONE_V8:
            case TIMESTAMP_WITH_LOCAL_TIMEZONE:
                int iPrecision = pResultSetMetaData.getPrecision(pResultSetColumnIndex);
                if (iPrecision > 19)
                {
                    pColumnMetaData.setScale(iPrecision - 20);
                }
                break;
            default:
                break;
        }
    }

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Uses limit/ offset for fetch, to get faster query response.
     * It is enabled by default.
     * 
     * @return if limit/ offset for paging is enabled.
     */
    public boolean isLimitFetchEnabled()
    {
        return limitFetchEnabled;
    }

    /**
     * Uses limit/ offset for fetch, to get faster query response.
     * It is enabled by default.
     * 
     * @param pLimitPagingEnabled true to enable limit/ offset for paging.
     */
    public void setLimitFetchEnabled(boolean pLimitPagingEnabled)
    {
        limitFetchEnabled = pLimitPagingEnabled;
    }

    /**
     * The maximum fetch amount when using limit paging.
     * 
     * @return the maximum fetch amount
     */
    public int getMaximumFetchAmount()
    {
        return maximumFetchAmount;
    }

    /**
     * The maximum fetch amount when using limit paging.
     * 
     * @param pMaximumFetchAmount maximum fetch amount
     */
    public void setMaximumFetchAmount(int pMaximumFetchAmount)
    {
        maximumFetchAmount = pMaximumFetchAmount;
    }

    /**
     * Gets the maximum amount of additional rows to fetch.
     * 
     * @return the maximum amount of additional rows to fetch.
     * @see #setMaximumAdditionalFetchAmount(int)
     */
    public int getMaximumAdditionalFetchAmount()
    {
        return maximumAdditionalFetchAmount;
    }

    /**
     * Sets the maximum amount of additional rows to fetch.
     * <p>
     * If a fetch took less than {@link #getMaxTime()}, there will be
     * a second, additional fetch issues to fetch more data. This allows
     * to set an upper limit on that second, additional fetch.
     * <p>
     * When set to {@code 0}, no additional data will be fetched. When
     * set to {@code -1} or less, there will be no limit imposed.
     * 
     * @param pMaximumAdditionalFetchAmount the maximum amount for the additional
     *                                      fetch to fetch.
     * @see #getMaximumAdditionalFetchAmount()
     */
    public void setMaximumAdditionalFetchAmount(int pMaximumAdditionalFetchAmount)
    {
        maximumAdditionalFetchAmount = pMaximumAdditionalFetchAmount;
    }

    /**
     * The minimum fetch amount when using limit paging.
     * This is set to 60 to optimize read ahead for vaadin.
     * Vaadin tries to fetch 60 rows initially, and this prevents fetching twice.
     * 
     * @return the minimum fetch amount
     */
    public int getMinimumFetchAmount()
    {
        return minimumFetchAmount;
    }

    /**
     * The minimum fetch amount when using limit paging.
     * This is set to 60 to optimize read ahead for vaadin.
     * Vaadin tries to fetch 60 rows initially, and this prevents fetching twice.
     * 
     * @param pMinimumFetchAmount minimum fetch amount
     */
    public void setMinimumFetchAmount(int pMinimumFetchAmount)
    {
        minimumFetchAmount = pMinimumFetchAmount;
    }

	/**
	 * Get detailed column information for the given table. The column details contains the
	 * real data type and not the JDBC data type.
	 * 
	 * @param pTableName the name of the table
	 * @return the information per column
	 */
	protected Hashtable <String, ColumnInfo> getColumnInfo(String pTableName)
	{
		Statement stmt = null;
		
		ResultSet res = null;
		
		
		try
		{
			stmt = getConnectionIntern().createStatement();
		
			res = stmt.executeQuery("show columns from " + pTableName);
		
			ColumnInfo cinf;
	
			Hashtable<String, ColumnInfo> htColumns = new Hashtable<String, ColumnInfo>();
			
			while (res.next())
			{
				cinf = new ColumnInfo();
				cinf.type  = res.getString("Type");
				cinf.nullable  = res.getString("Null");
				cinf.key       = res.getString("Key");
				cinf.defaultvalue = res.getString("Default");
				
				htColumns.put(res.getString("Field"), cinf);
			}
			
			return htColumns;
		}
		catch (SQLException sqle)
		{
			debug(sqle);
			
			//empty!
			return new Hashtable<String, ColumnInfo>();
		}
		finally
		{
		    CommonUtil.close(res, stmt);
		}
	}

	/**
	 * Extracts the values from a valuelist, separated with <code>,</code>.
	 * 
	 * @param pTypeDefinition the data type definition e.g. enum ('y', 'n')
	 * @return an array with values
	 */
	protected String[] extractValues(String pTypeDefinition)
	{
		int iPos = pTypeDefinition.indexOf('(');
		
		String sValueList = pTypeDefinition.substring(iPos + 1, pTypeDefinition.lastIndexOf(')'));
		
		String[] sValues = sValueList.split(",");
		
		//clean values
		for (int i = 0; i < sValues.length; i++)
		{
			if (sValues[i].equalsIgnoreCase("null"))
			{
				sValues[i] = null;
			}
			else
			{
				sValues[i] = StringUtil.removeQuotes(sValues[i].trim(), "'");
				
				if (sValues[i].length() == 0)
				{
					sValues[i] = null;
				}
			}
		}
		
		return sValues;
	}
	
	/**
	 * Calculates the amount of rows to fetch additionally to an already happened fetch.
	 * 
	 * @param pFetchedRowCount the already fetched row count.
	 * @param pFetchTimeInMilliSeconds the time the fetch took, in milliseconds.
	 * @return the amount of rows to additionally fetch. Can be zero or less to fetch nothing.
	 */
	protected int calculateAdditionalFetchRowCount(int pFetchedRowCount, long pFetchTimeInMilliSeconds)
	{
		return (int)(pFetchedRowCount * ((getMaxTime() - pFetchTimeInMilliSeconds) / pFetchTimeInMilliSeconds));
	}
	
	/**
	 * Configures this instance before opening the connection.
	 */
	protected void configureBeforeOpen()
	{
		//support Maria and MySQL JDBC driver in the same classpath 
//		setDBProperty("disableMariaDbDriver", "");
		
		if (iDriverVersion < 8)
		{
			//#35
			//JDBC driver workaround for 0000-00-00 00:00:00
			
			setDBProperty("zeroDateTimeBehavior", "convertToNull");
		}	
		else
		{
			setDBProperty("zeroDateTimeBehavior", "CONVERT_TO_NULL");
			
			//see https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-usagenotes-known-issues-limitations.html
			//see https://community.oracle.com/thread/4144569
			//docu: see https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-reference-configuration-properties.html
			if (getDBProperty("serverTimezone") == null)
			{
				setDBProperty("serverTimezone", TimeZone.getDefault().getID());
			}
		}
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************

	/**
	 * The <code>ColumnInfo</code> stores detailed table column information.
	 * 
	 * @author René Jahn
	 */
	private static class ColumnInfo
	{
		/** the field name. */
		@SuppressWarnings("unused")
		String field;

		/** the type. */
		String type;
		
		/** mandatory or nullable. */
		@SuppressWarnings("unused")
		String nullable;
		
		/** key definition. */
		@SuppressWarnings("unused")
		String key;

		/** the default value. */
		@SuppressWarnings("unused")
		String defaultvalue;
		
	}	// ColumnInfo
	
} 	// MySQLDBAccess
