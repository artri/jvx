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
 * 01.10.2008 - [RH] - creationetRepresentationColumnNames
 * 19.11.2008 - [RH] - Parameter Array at select() and k() removed
 * 25.11.2008 - [RH] - lockAndRefetch() added
 * 27.11.2008 - [RH] - writeCSV to stream added
 * 10.02.2009 - [JR] - writeCSVToTempFileStorage: first char upper, other characters lower case [BUGFIX]
 * 20.03.2009 - [JR] - writeCSV: last (null) row excluded [BUGFIX]
 * 29.04.2009 - [RH] - renamed to DBStorage, NLS removed, javadoc reviewed
 * 04.05.2009 - [RH] - interface review
 * 14.07.2009 - [JR] - initMetaData implemented [BUGFIX]
 *                   - open: called initMetaData [BUGFIX]
 *                   - initMetaData: called setPrimaryKeyColumns (single point of contact) 
 *                   - insertBean, updateBean, deleteBean, fetchBean, fetchRowBean implemented
 * 29.07.2009 - [JR] - createCSVFile without filename parameter
 * 31.07.2009 - [JR] - createCSVFile build filename without extension...     
 * 03.08.2009 - [JR] - added get/setMetaDataCache methods    
 * 13.10.2009 - [JR] - fetchBean: isOpen check [BUGFIX]    
 * 14.10.2009 - [RH] - refetch bug with more then one row for the primary key fixed.     
 * 15.10.2009 - [RH] - if writebackTable is null, no insert, update, delete is performed. 
 *                     refetch works over getFromClause() [BUGFIX]
 *            - [JR] - fetchBean(ICondition pFilter) implemented    
 * 18.10.2009 - [RH] - refetchRow returns null if no writebackTable is set -> no pk! [BUGFIXED]
 * 22.10.2009 - [JR] - updateBean needs only one IBean as property [FEATURE]
 * 23.11.2009 - [RH] - ColumnMetaData && PrimaryKey Column is replaced with MetaData class
 * 28.11.2009 - [RH] - is/Set[Default]AutoLinkReference added - enable/disable the automatic mechanism.
 * 04.12.2009 - [RH] - QueryColumns with alias bug fixed
 *                     Primary Key column bug (use now writeback cols instead query cols.)
 * 28.12.2009 - [JR] - getMetaData: checked from.equals(writeback) [FEATURE]
 * 29.12.2009 - [JR] - getMetaData: 
 *                     * checked if UK == PK columns and ignore the UK as representation columns if that is the case
 *                     * code review
 *                     * use first non PK column as link column (if available)
 *                     * checked and ignored if there is no link column        
 * 22.02.2010 - [JR] - #67: 
 *                     * getSubStorages implemented
 *                     * open: MetaData caching only when SessionContext is available
 *                     * getMetaData: create substorage reference definition without object name     
 * 02.03.2010 - [RH] - #75 - createAutomaticLinkstorage(), removeLinkReference(), getFK() functions added
 * 06.03.2010 - [JR] - #71: changed bean handling  
 * 13.03.2010 - [JR] - #88: getMetaData: used TYPE_IDENTIFIER      
 * 23.03.2010 - [RH] - automatic added AutoLinkCellEditors now sort over the first visible column (setDefaultSort)
 *                   - missing setMetaData() call for sub DBStorage created with createAutomaticLinkReference(...) added     
 *                     -> cache bug fixed.
 * 27.03.2010 - [JR] - #92: getDefaultValues integrated   
 *                   - #103: ICachedStorage instead of IStorage                  
 * 28.03.2010 - [JR] - #47: openIntern: allowed values integrated
 * 30.03.2010 - [JR] - #110: set/getAllowedValues, set/getDefaultAllowedValuesm set/getDefaultValue, set/getDefaultDefaultValue
 * 16.06.2010 - [JR] - getFK dropped (not used)
 * 29.09.2010 - [RH] - countRows renamed to getEstimatedRowCount()
 * 04.10.2010 - [RH] - #164: Nullable detection fixed for AutoLinkReferences.
 * 06.10.2010 - [JR] - putMetaDataToCache, getMetaData: group null check [BUGFIX]
 * 09.10.2010 - [JR] - getMetaData: use allowed values only if at least one value is set
 * 30.10.2010 - [JR] - extends AbstractCachedStorage
 * 09.11.2010 - [JR] - used log mechanism from parent class
 * 19.11.2010 - [RH] - getUKs, getPKs return Type changed to a <code>Key</code>, so the usage is adapted.          
 * 23.12.2010 - [RH] - getUKs doesn't return PKs ->check removed
 *                     #225: setDefaultSort for AutoLinkReference Storages changed to the AutoLinkReference column!      
 *                     #226: No duplicate columns anymore in representation columns, if it will be build from intersecting UKs  
 *                     #224: wrong AutomaticLinkColumnName fixed - unused ForeignKeys will be not used in Joins.
 * 03.01.2010 - [RH] - #136 - Mysql PK refetch not working -> extract the plan table without schema. 
 * 05.01.2011 - [RH] - #233 - XXXbean() methods moved to AbstractStorage.
 * 06.01.2011 - [JR] - #234: used ColumnMetaDataInfo 
 * 29.01.2011 - [JR] - #211: getMetaData now calls getDefaultAllowedValues if needed
 * 16.02.2011 - [JR] - #287: metadata cache implemented
 * 11.03.2011 - [RH] - #308: DB specific automatic quoting implemented         
 *                     #309: isNullable will be determined from the writebackTable instead of the fromClause
 * 12.04.2011 - [JR] - getMetaData: added url and username to cache key                          
 * 19.08.2011 - [JR] - #459: check if metadata cache is enabled instead of global metadata cache
 * 22.08.2011 - [JR] - #462: refreshMetaData implemented
 * 23.11.2011 - [JR] - cleanup internal metadata cache if global cache is cleared
 * 12.10.2012 - [JR] - #597: ignored BinaryDataType when setting PK with all columns 
 * 19.11.2012 - [JR] - code review: @Override added to abstract methods
 * 18.03.2013 - [RH] - #632: DBStorage: Update on Synonym (Oracle) doesn't work - Synonym Support implemented
 * 03.04.2013 - [JR] - #654: writeCSV changed to iterate through records
 * 04.06.2013 - [JR] - #669: remove metadata from cache 
 * 11.07.2013 - [JR] - #727: PrimaryKeyType set
 * 19.07.2013 - [RH] - #733: AfterWhereClause in DBStorage makes SQL Exception
 * 19.10.2013 - [RH] - #844: DBStorage with table from different schema
 * 06.05.2014 - [RZ] - #1029: Added the possibility to set an ORDER BY clause.
 * 15.05.2014 - [JR] - #1038: CommonUtil.close used  
 * 05.02.2015 - [JR] - #1254: use default charset for CSV creation  
 * 06.02.2015 - [JR] - #1256: writeCSV now uses client columns for fetching data (and lazy fetching)     
 * 25.08.2016 - [JR] - #1676: writeBack feature if insteadOf events are used    
 * 11.12.2019 - [JR] - #2136: pre/postConfigureAutomaticLinkStorage introduced               
 */
package com.sibvisions.rad.persist.jdbc;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.rad.model.ModelException;
import javax.rad.model.RowDefinition;
import javax.rad.model.SortDefinition;
import javax.rad.model.condition.And;
import javax.rad.model.condition.CompareCondition;
import javax.rad.model.condition.Equals;
import javax.rad.model.condition.ICondition;
import javax.rad.model.datatype.BinaryDataType;
import javax.rad.model.datatype.IDataType;
import javax.rad.model.datatype.StringDataType;
import javax.rad.model.reference.StorageReferenceDefinition;
import javax.rad.persist.ColumnMetaData;
import javax.rad.persist.DataSourceException;
import javax.rad.persist.ILinkableStorage;
import javax.rad.persist.ISubStorage;
import javax.rad.persist.MetaData;
import javax.rad.persist.MetaData.Feature;
import javax.rad.remote.IConnectionConstants;
import javax.rad.server.ISession;
import javax.rad.server.SessionContext;
import javax.rad.type.bean.Bean;
import javax.rad.type.bean.IBean;
import javax.rad.util.TranslationMap;

import com.sibvisions.rad.model.DataBookCSVExporter;
import com.sibvisions.rad.model.Filter;
import com.sibvisions.rad.persist.AbstractCachedStorage;
import com.sibvisions.rad.persist.AbstractStorage;
import com.sibvisions.rad.persist.jdbc.DBAccess.ParameterizedStatement;
import com.sibvisions.rad.persist.jdbc.ServerMetaData.PrimaryKeyType;
import com.sibvisions.rad.server.annotation.Accessible;
import com.sibvisions.rad.server.protocol.ICategoryConstants;
import com.sibvisions.rad.server.protocol.ICommandConstants;
import com.sibvisions.rad.server.protocol.ProtocolFactory;
import com.sibvisions.rad.server.protocol.Record;
import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.type.BeanUtil;
import com.sibvisions.util.type.CommonUtil;
import com.sibvisions.util.type.StringUtil;
import com.sibvisions.util.type.StringUtil.TextType;

/**
 * The <code>DBStorage</code> is a IStorage for SQL database specific features.<br>
 * <br>
 * The DBStorage allows to influence the creation of the SELECT statement to get the data.<br>
 * The following template shows how the SELECT will be constructed out of the specified properties.<br>
 * <pre>
 * <code>
 * SELECT getBeforeQueryColumns() getQueryColumns()
 * FROM   getFromClause()
 * WHERE  getFilter()
 * AND    getMasterReference()
 * AND    getWhereClause()
 * getAfterWhereClause()
 * ORDER BY getSort()/getOrderByClause()
 * </code>
 * </pre>
 * Example:
 * <pre>
 * <code> 
 * SELECT DISTINCT // ++ comment, optimizer hints   // getBeforeQueryColumns() 
 *        a.COL1 C, b.COL2 D, a.FK_ID FK_ID, ...    // getQueryColumns() or all columns of getFromClause()
 *        a.ADDCOL1 X, b.ADDCOL2 y                  // getAdditionalQueryColumns()
 * FROM   TABLE1 a,                                 // getFromClause() or automatic query built with getWriteBackTable(),
 *        TABLE2 b,                                 // ForeignKeys and createAutomaticLinkReference(..)
 *        ...
 * WHERE  C LIKE 'a%' AND D IS NOT NULL ...         // getFilter()
 * AND    FK_ID = 23                                // getMasterReference() get all detail rows to a 
 *                                                     specific master row in an other DataBook
 * AND    a.ID = b.FK_ID ...                        // getWhereClause() 
 * GROUP BY C, D                                    // getAfterWhereClause()
 * ORDER BY C DESC							        // getSort() or getOrderByClause()
 * LIMIT 5 OFFSET 0                                 // getAfterOrderByClause()
 * </code>
 * </pre>
 * 
 * @see javax.rad.persist.IStorage
 * @see javax.rad.persist.ColumnMetaData
 * @see com.sibvisions.rad.model.remote.RemoteDataBook
 * @see com.sibvisions.rad.persist.jdbc.DBAccess
 * @author Roland Hörmann
 */
public class DBStorage extends AbstractCachedStorage 
                       implements ILinkableStorage, 
                                  ISubStorage
{	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The prefix for substorages. */
	public static final String SUBSTORAGE_PREFIX = ".subStorages.";
	/** The prefix for automatic link references. */
	private static final String AUTOMATICLINKREFERENCE_PREFIX = ".FK.";
	
	/** dummy automatic link entry. */
    private static final Bean DUMMY_AUTOMATIC_LINK_ENTRY = new Bean();
	
	/** The server storage meta data. */
	private ServerMetaData mdServerMetaData = new ServerMetaData();
	
	/** The list of sub storages. */
	private HashMap<String, ISubStorage> hmpSubStorages = new HashMap<String, ISubStorage>();
		
	/** The list of manual automatic link references. */
	private ArrayUtil<StorageReferenceDefinition>  auManualLinkReferences = new ArrayUtil<StorageReferenceDefinition>();

	/** The list of automatic link references. */
	private ArrayUtil<StorageReferenceDefinition>  auAutomaticLinkReferences = new ArrayUtil<StorageReferenceDefinition>();
	/** The sorted list of automatic link references. */
	private ArrayUtil<StorageReferenceDefinition> auSortedAutomaticLinkReferences = new ArrayUtil<StorageReferenceDefinition>();
	
	/** The DBAccess of this Storage. */
	private DBAccess  		dbAccess;
	
	/** The FROM clause with the query tables and LEFT/RIGHT INNER OUTER JOIN. */
	private String			sFromClause;

	/** The FROM clause before open(). */
	private String			sFromClauseBeforeOpen;
	
	/** The list of query columns to use in the SELECT statement to get the data from the storage. */
	private String[]		saQueryColumns;
	
	/** The list of additional query columns to use in the SELECT statement to get the data from the storage. */
	private String[]		saAdditionalQueryColumns;
	
	/** The list of query columns before open(). */
	private String[]		saQueryColumnsBeforeOpen;
	
	/** The string to place in the SELECT statement between the SELECT and the first query column. */
	private String      	sBeforeQueryColumns;

	/** The string to place in the SELECT statement after the last WHERE condition from the
	 *  Filter or MasterReference (Master-Detail Condition). */
	private String      	sWhereClause;

	/** The string to place in the SELECT statement after the WHERE clause and before the ORDER BY clause. */
	private String      	sAfterWhereClause;

	/** The string to place in the ORDER BY section if no default sort is set. */
	private String			sOrderByClause;
	
    /** The string to place in the SELECT statement after the ORDER BY clause. */
    private String          sAfterOrderByClause;
    
	/** The list of write back columns to use in the INSERT, UPDATE statements.  */
	private String[]		saWritebackColumns;
	
	/** The write table to use in the INSERT, UPDATE statements.  */
	private String			sWritebackTable;

	/** The schema name of the writeback table. */
	private String			sSchema;
	
	/** the catalog name of the writeback table. */
	private String			sCatalog;
	
	/** The default sort is always used, if no sort is specified.  */
	private SortDefinition	sdDefaultSort;

	/** The restrict condition is always used to fetch data.  */
	private ICondition		cRestrictCondition;

	/** Determines the automatic link reference mode.  */
	private Boolean			bAutoLinkReference;

	/** The open state of this DBStorage. */
	private boolean	 		bIsOpen  = false;

	/** Determines the default automatic link reference mode. */
	private static boolean	bDefaultAutoLinkReference = true;
	
	/** Determines the automatic default value detection. */
	private Boolean			bDefaultValue;
	
	/** Determines the default automatic default value detection. */
	private static boolean 	bDefaultDefaultValue = true;
	
	/** Determines the allowed values detection. */
	private Boolean			bAllowedValues;
	
	/** Determines if lazy fetch is used. */
	private boolean			bLazyFetchEnabled = true;
	
	/** Determines the default allowed values detection. */
	private static boolean	bDefaultAllowedValues = true;

	/** Determines if restrict condition should be included when row is refetched. */
	private Boolean 		bRefetchIncludeRestrictCondition; 

    /** If set these primary key columns are used for refetch instead of the real primary key columns. */
    private String[]        saRefetchPrimaryKeyColumns = null; 

	/** Determines if restrict condition should be included when row is refetched. */
	private static boolean 	bDefaultRefetchIncludeRestrictCondition = true; 
			
	/** <code>true</code>, if row should be locked on refetch. In case of autocommit, it is never locked. */
	private boolean 		bLockOnRefetch = true;
	
	/** Determines whether the values over available not database link references should be loaded. */
	private boolean         bLoadNotDatabaseAutoLinkValues = false;
	
	/** The filter of the substorages. */
	private ICondition 		subStorageConditions = null;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization 
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>DBStorage</code>.
	 */
	public DBStorage()
	{
		super();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeCSV(OutputStream pOutputStream, 
			             String[] pColumnNames, 
			             String[] pLabels, 
			             ICondition pFilter, 
			             SortDefinition pSort, 
			             String pSeparator) throws Exception
	{
	    ISession session = SessionContext.getCurrentSession();
	    
        String sEncoding = null;

        if (session != null)
	    {
            sEncoding = (String)session.getProperty(IConnectionConstants.CLIENT_FILE_ENCODING);
            
            if (sEncoding == null)
            {
                sEncoding = (String)session.getProperty(IConnectionConstants.PREFIX_CLIENT + "defaultCharset");
            }
	    }

        if (StringUtil.isEmpty(sEncoding))
	    {
	        sEncoding = DataBookCSVExporter.getDefaultEncoding(); 
	    }
	    
		OutputStreamWriter out = new OutputStreamWriter(pOutputStream, sEncoding);

		try
		{
			pFilter = createFilter(pFilter);
	
			if (pSort == null)
			{
				pSort = sdDefaultSort;
			}
			else
			{
				checkSort(pSort);
			}
			
			if (pColumnNames == null)
			{
				pColumnNames = mdServerMetaData.getColumnNames();
			}
			
			if (pLabels == null)
			{
				pLabels = new String[pColumnNames.length];
				
				for (int i = 0; i < pColumnNames.length; i++)
				{
					pLabels[i] = mdServerMetaData.getServerColumnMetaData(pColumnNames[i]).getLabel();
					
					if (pLabels[i] == null)
					{
						pLabels[i] = ColumnMetaData.getDefaultLabel(pColumnNames[i]);
					}
				}
			}
			
			// write column headers with the defined label
			for (int i = 0; i < pLabels.length; i++)
			{
				if (i > 0)
				{
					out.write(pSeparator);
				}
				out.write(StringUtil.quote(pLabels[i], '"'));
			}
			out.write("\n");
			
            String[] saFetchQueryColumns;
            
            // if before query colums are set, we can't use client columns because the result may be different!
            if (sBeforeQueryColumns != null)
            {
                //don't change the original array!
                saFetchQueryColumns = saQueryColumns.clone();
            }
            else
            {
                //don't change the original array!
                saFetchQueryColumns = pColumnNames.clone();
            }
            
			//cache the column datatypes
			IDataType[] dataTypes = new IDataType[pColumnNames.length];
			//cache the column index
			int[] iColIndex = new int[pColumnNames.length];
			
			ServerColumnMetaData scmd;
			
			for (int i = 0, anz = pColumnNames.length; i < anz; i++)
			{
			    String columnName = pColumnNames[i];
			    
                scmd = mdServerMetaData.getServerColumnMetaData(columnName);
                
                dataTypes[i] = scmd.getColumnMetaData().getDataType();
                
                //result (length) always contains columns from metadata 
                iColIndex[i] = mdServerMetaData.getServerColumnMetaDataIndex(columnName);

                if (sBeforeQueryColumns == null)
                {
                    saFetchQueryColumns[i] = scmd.getRealQueryColumnName();
                
                    if (StringUtil.isEmpty(saFetchQueryColumns[i]))
                    {
                        saFetchQueryColumns[i] = columnName;
                    }
                    else
                    {
                        // Ensure the correct column name, as otherwise it cannot be detected by fetch in DBAccess, and is 
                        saFetchQueryColumns[i] += " as " + columnName; 
                    }
                }
			}
			
			Object[] oData;
			
			List<Object[]> lResult;
			
			int iStart = 0;
			
			boolean bAllFetched = false;
			
			String sFromClauseIntern = getFromClauseIntern();
			
			while (!bAllFetched)
			{
				lResult = dbAccess.fetch(mdServerMetaData, sBeforeQueryColumns, saFetchQueryColumns, sFromClauseIntern,
				            			 pFilter, sWhereClause, sAfterWhereClause,
				            			 pSort, sOrderByClause, sAfterOrderByClause, iStart, 500,
				            			 true);

				//continue fetching
				iStart += lResult.size();
				
				//write rows
				for (int i = 0, anz = lResult.size(); i < anz; i++)
				{
					oData = lResult.get(i);
					
					if (oData != null)
					{
						for (int j = 0; j < pColumnNames.length; j++)
						{
							if (j > 0)
							{
								out.write(pSeparator);
							}
							
							DataBookCSVExporter.writeQuoted(out, dataTypes[j], oData[iColIndex[j]], pSeparator);
						}		
						
						out.write("\n");
					}
					else
					{
						bAllFetched = true;
					}
				}
			}
			
			out.flush();
		}
		finally
		{
		    CommonUtil.close(out);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isOpen()
	{
		return bIsOpen;
	}

	/**
	 * Opens the DBStorage and checks if the DBAccess is != null.
	 * If automatic link celleditors are used, then the implicit link DBStorge objects are put with "detailtablename"
	 * as sub storage.
	 * 
	 * @throws DataSourceException if the DBAccess is null
	 */
	@Override
	public void open() throws DataSourceException
	{
        Record record = ProtocolFactory.openRecord(ICategoryConstants.STORAGE, ICommandConstants.STORAGE_OPEN);
        
        try
        {
        	//same check as in openInternal
    		if (dbAccess == null)
    		{
    			throw new DataSourceException("DBAccess isn't set!");
    		}
        	
    		dbAccess.fireEventBeforeOpenDBStorage(this);    		

    		sFromClauseBeforeOpen = getFromClause();
    		saQueryColumnsBeforeOpen = getQueryColumns();
    		
    		if (record != null)
    		{
    		    record.setParameter(sFromClauseBeforeOpen, saQueryColumnsBeforeOpen);
    		}
    		
    		openInternal(false);
    		
    		dbAccess.fireEventAfterOpenDBStorage(this);
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
	protected List<Object[]> executeFetch(ICondition pFilter, SortDefinition pSort, int pFromRow, int pMinimumRowCount) throws DataSourceException
	{
        Record record = ProtocolFactory.openRecord(ICategoryConstants.STORAGE, ICommandConstants.STORAGE_EXEC_FETCH);
        
        try
        {
    		if (!isOpen())
    		{
    			throw new DataSourceException("DBStorage isn't open!");
    		}
	
    		pFilter = createFilter(pFilter);
    
    		if (pSort == null)
    		{
    			pSort = sdDefaultSort;
    		}
    		else
    		{
    			checkSort(pSort);
    		}
    
    		if (pMinimumRowCount >= 0 && pMinimumRowCount < 3) // Ensure, that 0, 1, 2 are reserved for 0 (meta data), 1 (row count), 2 (refetch row)
    		{
    		    pMinimumRowCount = 3;
    		}
    		
            if (record != null)
            {
                record.setParameter(pFilter, pSort, Integer.valueOf(pFromRow), Integer.valueOf(pMinimumRowCount));
            }
            
    		List<Object[]> liResult = dbAccess.fetch(
    				mdServerMetaData,
    				sBeforeQueryColumns,
    				saQueryColumns,
    				getFromClauseIntern(),
    				pFilter,
    				sWhereClause, sAfterWhereClause,
    				pSort,
    				sOrderByClause,
    				pFromRow,
    				pMinimumRowCount,
    				bLazyFetchEnabled);
    		
    		if (bLoadNotDatabaseAutoLinkValues && isAutoLinkReference())
            {
                setNotDatabaseAutoLinkValues(liResult);
            }
    		
    		return liResult;
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
	protected Object[] executeRefetchRow(Object[] pDataRow) throws DataSourceException
	{
        Record record = ProtocolFactory.openRecord(ICategoryConstants.STORAGE, ICommandConstants.STORAGE_EXEC_REFETCH);

        try
        {
    		if (!isOpen())
    		{
    			throw new DataSourceException("DBStorage isn't open!");
    		}

		    return refetchRow(pDataRow, isLockOnRefetch());
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
	protected Object[] executeInsert(Object[] pDataRow) throws DataSourceException
	{
	    Record record = ProtocolFactory.openRecord(ICategoryConstants.STORAGE, ICommandConstants.STORAGE_EXEC_INSERT);
	    
	    try
	    {
    		if (!isOpen())
    		{
    			throw new DataSourceException("DBStorage isn't open!");
    		}
    		if (getWritebackTable() == null)
    		{
    			return null;
    		}
    
    		Object[] oResult = dbAccess.insert(getWritebackTable(), mdServerMetaData, pDataRow);
    		
    		if (isRefetch())
    		{
    			Object[] oRefetchedRow = refetchRow(oResult, false);
    			if (oRefetchedRow != null)
    			{
    				return oRefetchedRow;
    			}
    		}
    		return oResult;
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
	protected Object[] executeUpdate(Object[] pOldDataRow, Object[] pNewDataRow) throws DataSourceException
	{
        Record record = ProtocolFactory.openRecord(ICategoryConstants.STORAGE, ICommandConstants.STORAGE_EXEC_UPDATE);
        
        try
        {
    		if (!isOpen())
    		{
    			throw new DataSourceException("DBStorage isn't open!");
    		}
    		if (getWritebackTable() == null)
    		{
    			return null;
    		}
    
    		Object[] oResult = dbAccess.update(getWritebackTable(),	mdServerMetaData, pOldDataRow, pNewDataRow);
    		
    		if (isRefetch())
    		{
    			Object[] oRefetchedRow = refetchRow(oResult, false); // ensure oResult is not null, if refetch fails.
    			if (oRefetchedRow != null)
    			{
    				return oRefetchedRow;
    			}
    		}
    		
    		return oResult;
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
	protected void executeDelete(Object[] pDeleteDataRow) throws DataSourceException
	{
        Record record = ProtocolFactory.openRecord(ICategoryConstants.STORAGE, ICommandConstants.STORAGE_EXEC_DELETE);
        
        try
        {
    		if (!isOpen())
    		{
    			throw new DataSourceException("DBStorage isn't open!");			
    		}
    
    		if (getWritebackTable() == null)
    		{
    			return;
    		}
    		dbAccess.delete(getWritebackTable(), mdServerMetaData, pDeleteDataRow);
        }
        finally
        {
            CommonUtil.close(record);
        }
	}	
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Accessible
	public MetaData getMetaData() throws DataSourceException
	{
		MetaData metaData = mdServerMetaData.getMetaData();
		
        //if instead of triggers are available, the write back feature should be enabled!
        if (hasInsteadOfInsertEventHandler()
            || hasInsteadOfUpdateEventHandler()
            || hasInsteadOfDeleteEventHandler())
        {
            metaData.addFeature(Feature.WriteBack);
        }
        
		return metaData;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getEstimatedRowCount(ICondition pFilter) throws DataSourceException
	{
		if (!isOpen())
		{
			throw new DataSourceException("DBStorage isn't open!");
		}
		
		pFilter = createFilter(pFilter);

		List<Object[]> olResult = dbAccess.fetch(mdServerMetaData, sBeforeQueryColumns, 
												 new String[] { "COUNT(*)"}, getFromClauseIntern(),
								                 pFilter, sWhereClause, sAfterWhereClause, 
									             null, 0, 1,
									             false);
		
		if (olResult != null && olResult.size() >= 1 && olResult.get(0).length > 0)
		{
			Object oResult = olResult.get(0)[0];
			if (oResult instanceof Integer)
			{
				return ((Integer)oResult).intValue();
			}
			else if (oResult instanceof BigDecimal)
			{
				return ((BigDecimal)oResult).intValue();
			}
			throw new DataSourceException("countRows() result data typ not supported! - " + oResult.getClass().getName());
		}
		return 0;
	}
	
	/**
	 * {@inheritDoc}
	 */
    public String putSubStorage(ISubStorage pSubStorage)
    {
        String sStorageName = createSubStorageName(pSubStorage); 
        
        hmpSubStorages.put(sStorageName, pSubStorage);
        
        return SUBSTORAGE_PREFIX + sStorageName;
    }

    /**
     * {@inheritDoc}
     */
    public ISubStorage getSubStorage(String pStorageName)
    {
        if (pStorageName.startsWith(SUBSTORAGE_PREFIX))
        {
            return hmpSubStorages.get(pStorageName.substring(SUBSTORAGE_PREFIX.length()));
        }
        else
        {
            return null;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Accessible
    public Map<String, ISubStorage> getSubStorages()
    {
        return hmpSubStorages;
    }

    /**
     * {@inheritDoc}
     */
    public void createAutomaticLinkReference(String[] pColumns, ISubStorage pStorage, String[] pReferenceColumns) throws DataSourceException
    {
        String sStorageName = putSubStorage(pStorage);
        
        StorageReferenceDefinition srdLink = new StorageReferenceDefinition(pColumns, sStorageName, pReferenceColumns);

        if (isOpen())
        {
            installAutomaticLinkReferenceIntern(srdLink);
        }
        else
        {
            auManualLinkReferences.add(srdLink);
        }
    }
    
	/**
	 * {@inheritDoc}
	 */
    public String createSubStorageName()
    {
        String sStorageName;
        
        if (getName() != null)
        {
            sStorageName = getName();
        }
        else
        {
            if (getWritebackTable() != null)
            {
                sStorageName = getWritebackTable();
            }
            else
            {
                sStorageName = getFromClause();
            }
            int index = sStorageName.lastIndexOf('.');
            if (index >= 0)
            {
                sStorageName = sStorageName.substring(index + 1);
            }
            sStorageName = StringUtil.convertToMemberName(sStorageName);
        }
        
        return sStorageName;
    }

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        
        result = prime * result + ((bAutoLinkReference == null) ? 0 : bAutoLinkReference.hashCode());
        result = prime * result + ((sAfterWhereClause == null) ? 0 : sAfterWhereClause.hashCode());
        result = prime * result + ((sBeforeQueryColumns == null) ? 0 : sBeforeQueryColumns.hashCode());
        result = prime * result + ((sFromClause == null) ? 0 : sFromClause.hashCode());
        result = prime * result + ((sWhereClause == null) ? 0 : sWhereClause.hashCode());
        result = prime * result + ((sWritebackTable == null) ? 0 : sWritebackTable.hashCode());
        result = prime * result + Arrays.hashCode(saQueryColumns);
        result = prime * result + Arrays.hashCode(saWritebackColumns);
        
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        
        if (obj == null)
        {
            return false;
        }
        
        if (getClass() != obj.getClass())
        {
            return false;
        }
        
        DBStorage other = (DBStorage)obj;
        
        if (!CommonUtil.equals(getBeforeQueryColumns(), other.getBeforeQueryColumns()))
        {
            return false;
        }
        
        if (!Arrays.equals(getQueryColumns(), other.getQueryColumns()))
        {
            return false;
        }
        
        if (!Arrays.equals(getAdditionalQueryColumns(), other.getAdditionalQueryColumns()))
        {
            return false;
        }
        
        if (!CommonUtil.equals(getFromClause(), other.getFromClause()))
        {
            return false;
        }
        
        if (!CommonUtil.equals(getWhereClause(), other.getWhereClause()))
        {
            return false;
        }
        
        if (!CommonUtil.equals(getAfterWhereClause(), other.getAfterWhereClause()))
        {
            return false;
        }
        
        if (!CommonUtil.equals(getOrderByClause(), other.getOrderByClause()))
        {
            return false;
        }
        
        if (!CommonUtil.equals(getAfterOrderByClause(), other.getAfterOrderByClause()))
        {
            return false;
        }
        
        if (!CommonUtil.equals(getWritebackTable(), other.getWritebackTable()))
        {
            return false;
        }
        
        if (!Arrays.equals(getWritebackColumns(), other.getWritebackColumns()))
        {
            return false;
        }
        
        if (isAutoLinkReference() != other.isAutoLinkReference())
        {
            return false;
        }
        
        return true;
    }
    
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
        StringBuffer sbResult = new StringBuffer();
        
        sbResult.append("IsOpen()=");
        sbResult.append(bIsOpen);
        sbResult.append("\n");
        sbResult.append(dbAccess);
        sbResult.append("\n");

        sbResult.append(sBeforeQueryColumns);
        sbResult.append(" ");
        for (int i = 0; saQueryColumns != null && i < saQueryColumns.length; i++)
        {
        	if (i > 0)
        	{
        		sbResult.append(",");
        	}
            sbResult.append(saQueryColumns[i]);
        }
        sbResult.append("\n");
        sbResult.append(sFromClause);
        sbResult.append("\n");
        sbResult.append(sWhereClause);
        sbResult.append("\n");
        sbResult.append(sAfterWhereClause);
        sbResult.append("\n");
        
        sbResult.append("Writeback:\n");
        sbResult.append(getWritebackTable());
        sbResult.append("\n");

        for (int i = 0; saWritebackColumns != null && i < saWritebackColumns.length; i++)
        {
        	if (i > 0)
        	{
        		sbResult.append(",");
        	}
            sbResult.append(saWritebackColumns[i]);
        }

        return sbResult.toString();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Sets a new meta data. This is only possible, if the storage is not open.
	 * 
	 * @param pMetaData the new meta data.
	 * @throws DataSourceException if the Storage is already open.
	 */
	public void setMetaData(MetaData pMetaData) throws DataSourceException
	{
		if (isOpen())
		{
			throw new DataSourceException("Metadata can not be changed when DBStorage is already open!");
		}

		mdServerMetaData.setMetaData(pMetaData);
	}
	
	/**
	 * Opens the DBStorage and checks if the DBAccess is != null. It doesn't cache the MetaData!
	 * 
	 * @param pUseRepresentationColumnsAsQueryColumns <code>yes</code> if the QueryColumns are set with 
	 *                                                all representation columns including the Primary Key columns.
	 * @throws DataSourceException if the DBAccess is null.
	 */
	protected void openInternal(boolean pUseRepresentationColumnsAsQueryColumns) throws DataSourceException
	{
		if (dbAccess == null)
		{
			throw new DataSourceException("DBAccess isn't set!");
		}
		
		createMetaData(getBeforeQueryColumns(),
                       getQueryColumns(),
                       getFromClause(),
                       getWhereClause(),
                       getAfterWhereClause(),
                       getWritebackTable(),
                       getWritebackColumns(),
                       isAutoLinkReference(),
                       pUseRepresentationColumnsAsQueryColumns);
		
		bIsOpen = true;
	}
	
	/**
	 * Refreshes the MetaData with initial settings.
	 * 
	 * @throws DataSourceException if the DBAccess is null.
	 */
	protected void refreshMetaData() throws DataSourceException
	{
		setFromClause(sFromClauseBeforeOpen);
		setQueryColumns(saQueryColumnsBeforeOpen);
		
		openInternal(false);
	}
	
	/**
	 * Adds a sub storage to the internal list of all sub storages.
	 * 
	 * @param pSubStorage the sub storage to use.
	 * @return the storage name to use.
	 */
	private String putSubStorage(DBStorage pSubStorage)
	{
		String sStorageName = createSubStorageName(pSubStorage); 
		
		hmpSubStorages.put(sStorageName, pSubStorage);
		
		return SUBSTORAGE_PREFIX + sStorageName;
	}

	/**
     * Creates and sets a new <code>StorageReferenceDefinition</code> with the specified DBStorage and
     * columns and reference columns on all <code>pColumns</code>. 
     * e.g. its used to make an automatic linked celleditor from a custom written view and set it on all <code>pColumns</code>. 
     * 
     * @param pColumns the columns to use.
     * @param pStorage the Storage.
     * @param pReferenceColumns the reference columns to use.
     * @throws DataSourceException if the <code>StorageReferenceDefinition</code> couldn't created.
     */ 
    public void createAutomaticLinkReference(String[] pColumns, DBStorage pStorage, String[] pReferenceColumns) throws DataSourceException
    {
        String sStorageName = putSubStorage(pStorage);
        
        StorageReferenceDefinition srdLink = new StorageReferenceDefinition(pColumns, sStorageName, pReferenceColumns);

        if (isOpen())
        {
            installAutomaticLinkReferenceIntern(srdLink);
        }
        else
        {
            auManualLinkReferences.add(srdLink);
        }
    }

	/**
	 * Creates and sets a new <code>StorageReferenceDefinition</code> with the specified FromClause and
	 * columns and reference columns on all <code>pColumns</code>. 
	 * e.g. its used to make an automatic linked celleditor from a custom written view and set it on all <code>pColumns</code>. 
	 * 
	 * @param pColumns the columns to use.
	 * @param pWritebackTable the FromClause. e.g. VIEW name.
	 * @param pReferenceColumns	the reference columns to use.
	 * @throws DataSourceException if the <code>StorageReferenceDefinition</code> couldn't created.
	 */	
	public void createAutomaticLinkReference(String[] pColumns, String pWritebackTable, String[] pReferenceColumns) throws DataSourceException
	{
		if (isOpen())
		{
			StorageReferenceDefinition linkReference = new StorageReferenceDefinition(pColumns,  pWritebackTable, pReferenceColumns);
			
			// This will create the SubStorage and correct the StorageReferenceDefinition refDef
			createAutomaticLinkStorage(linkReference, mdServerMetaData.getWritableColumnNames()); 

			installAutomaticLinkReferenceIntern(linkReference);
		}
		else
		{
			auManualLinkReferences.add(new StorageReferenceDefinition(pColumns, pWritebackTable, pReferenceColumns));
		}
	}

    /**
     * Creates and sets a new <code>StorageReferenceDefinition</code> with the specified <code>ISubStorage</code> and
     * column and reference column on <code>pColumn</code>. 
     * 
     * @param pColumn          the columns to use.
     * @param pWritebackTable  the FromClause. e.g. VIEW name. 
     * @param pReferenceColumn the reference columns to use.
     * @throws DataSourceException  if the <code>StorageReferenceDefinition</code> couldn't created.
     */
    public void createAutomaticLinkReference(String pColumn, String pWritebackTable, String pReferenceColumn) throws DataSourceException
    {
        String[] columns = pColumn == null ? null : new String[] {pColumn};
        String[] referenceColumns = pReferenceColumn == null ? null : new String[] {pReferenceColumn};

        createAutomaticLinkReference(columns, pWritebackTable, referenceColumns);
    }

    /**
     * Creates and sets a new <code>StorageReferenceDefinition</code> with the specified <code>ISubStorage</code> and
     * column and reference columns on all <code>pColumn</code>. 
     * This gives the client a hint, for the used column view and sets display concat mask.
     * 
     * @param pColumn           the columns to use.
     * @param pWritebackTable   the FromClause. e.g. VIEW name.
     * @param pReferenceColumns the reference columns to use.
     * @throws DataSourceException  if the <code>StorageReferenceDefinition</code> couldn't created.
     */
    public void createAutomaticLinkReference(String pColumn, String pWritebackTable, String[] pReferenceColumns) throws DataSourceException
    {
        String[] columns = pColumn == null ? null : new String[] {pColumn};

        createAutomaticLinkReference(columns, pWritebackTable, pReferenceColumns);
    }

    /**
     * Creates and sets a new <code>StorageReferenceDefinition</code> with the specified <code>ISubStorage</code> and
     * column and reference column on <code>pColumn</code>. 
     * 
     * @param pColumn          the columns to use.
     * @param pStorage         the <code>ISubStorage</code> to use.
     * @param pReferenceColumn the reference columns to use.
     * @throws DataSourceException  if the <code>StorageReferenceDefinition</code> couldn't created.
     */
    public void createAutomaticLinkReference(String pColumn, ISubStorage pStorage, String pReferenceColumn) throws DataSourceException
    {
        String[] columns = pColumn == null ? null : new String[] {pColumn};
        String[] referenceColumns = pReferenceColumn == null ? null : new String[] {pReferenceColumn};

        createAutomaticLinkReference(columns, pStorage, referenceColumns);
    }

    /**
     * Creates and sets a new <code>StorageReferenceDefinition</code> with the specified <code>ISubStorage</code> and
     * column and reference columns on all <code>pColumn</code>. 
     * This gives the client a hint, for the used column view and sets display concat mask.
     * 
     * @param pColumn           the columns to use.
     * @param pStorage          the <code>ISubStorage</code> to use.
     * @param pReferenceColumns the reference columns to use.
     * @throws DataSourceException  if the <code>StorageReferenceDefinition</code> couldn't created.
     */
    public void createAutomaticLinkReference(String pColumn, ISubStorage pStorage, String[] pReferenceColumns) throws DataSourceException
    {
        String[] columns = pColumn == null ? null : new String[] {pColumn};

        createAutomaticLinkReference(columns, pStorage, pReferenceColumns);
    }
	
	/**
	 * Adds, the automatic link reference.
	 * It is sorted by the column count of foreignkey columns.
	 * The write back columns are used, to detect the foreign key columns. 
	 * 
	 * @param pReference the automatic link reference.
	 * @param pWriteBackColumns the writeBackColumns.
	 */
	private void addAutomaticLinkReferenceIntern(StorageReferenceDefinition pReference, String[] pWriteBackColumns)
	{
		String[] refColumns = ArrayUtil.intersect(pReference.getColumnNames(), pWriteBackColumns);
		if (refColumns.length == 0)
		{
			refColumns = pReference.getColumnNames();
		}
		boolean refIsFK = pReference.getReferencedStorage().startsWith(AUTOMATICLINKREFERENCE_PREFIX);
		String refStorageName = pReference.getReferencedStorage().replace(AUTOMATICLINKREFERENCE_PREFIX, "").
				replace(SUBSTORAGE_PREFIX, "").toLowerCase();
		
		for (int i = auAutomaticLinkReferences.size() - 1; i >= 0; i--)
		{
			StorageReferenceDefinition linkRef = auAutomaticLinkReferences.get(i);
			
			String[] linkRefColumns = ArrayUtil.intersect(linkRef.getColumnNames(), pWriteBackColumns);
			if (linkRefColumns.length == 0)
			{
				linkRefColumns = linkRef.getColumnNames();
			}
			boolean linkRefIsFK = linkRef.getReferencedStorage().startsWith(AUTOMATICLINKREFERENCE_PREFIX);
			String linkRefStorageName = linkRef.getReferencedStorage().replace(AUTOMATICLINKREFERENCE_PREFIX, "").
					replace(SUBSTORAGE_PREFIX, "").toLowerCase();
			
			if (refStorageName.equals(linkRefStorageName))
			{
				if (refColumns.length < linkRefColumns.length && ArrayUtil.containsAll(linkRefColumns, refColumns))
				{
					if (refIsFK == linkRefIsFK)
					{
						return;
					}
					else
					{
						auAutomaticLinkReferences.remove(i);
					}
				}
				else if (refColumns.length > linkRefColumns.length && ArrayUtil.containsAll(refColumns, linkRefColumns))
				{
					auAutomaticLinkReferences.remove(i);
				}
			}
		}
		
		for (int i = 0, size = auAutomaticLinkReferences.size(); i < size; i++)
		{
			StorageReferenceDefinition linkRef = auAutomaticLinkReferences.get(i);
			
			String[] linkRefColumns = ArrayUtil.intersect(linkRef.getColumnNames(), pWriteBackColumns);
			if (linkRefColumns.length == 0)
			{
				linkRefColumns = linkRef.getColumnNames();
			}
			if (linkRefColumns.length > refColumns.length)
			{
				auAutomaticLinkReferences.add(i, pReference);
				return;
			}
			else if (linkRefColumns.length == refColumns.length	&& ArrayUtil.containsAll(linkRefColumns, refColumns))
			{
				auAutomaticLinkReferences.set(i, pReference);
				return;
			}
		}
		auAutomaticLinkReferences.add(pReference);
	}

	/**
	 * Gets all sub reference definitions to the given reference definition.
	 * 
	 * @param pReference the reference definition. 
	 * @param pWriteBackColumns the write back columns.
	 * @return all sub reference definitions.
	 */
	private List<StorageReferenceDefinition> getSubAutomaticLinkReferences(StorageReferenceDefinition pReference, String[] pWriteBackColumns)
	{
		String[] refColumns = ArrayUtil.intersect(pReference.getColumnNames(), pWriteBackColumns);

		List<StorageReferenceDefinition> result = new ArrayUtil<StorageReferenceDefinition>();
		
		for (StorageReferenceDefinition linkRef : auAutomaticLinkReferences)
		{
			String[] linkRefColumns = ArrayUtil.intersect(linkRef.getColumnNames(), pWriteBackColumns);
			
			if (linkRefColumns.length >= refColumns.length)
			{
				return result;
			}
			else if (linkRefColumns.length > 0 && ArrayUtil.containsAll(refColumns, linkRefColumns))
			{
				result.add(linkRef);
			}
		}
		return result;
	}
		
	/**
	 * Creates and sets a new <code>StorageReferenceDefinition</code> with the specified <code>DBStorage</code>(or from clause) and
	 * columns and reference columns on all <code>pColumns</code>. 
	 * 
	 * @param pReferenceDefinition the reference definition.
	 * @throws DataSourceException if the <code>StorageReferenceDefinition</code> couldn't created.
	 */
	protected void installAutomaticLinkReferenceIntern(StorageReferenceDefinition pReferenceDefinition) throws DataSourceException
	{		
		String[] writeBackColumns = mdServerMetaData.getWritableColumnNames();
		
		addAutomaticLinkReferenceIntern(pReferenceDefinition, writeBackColumns);
		
		String[] columnsToSetReference = pReferenceDefinition.getColumnNames();
		
		List<StorageReferenceDefinition> subStorages = getSubAutomaticLinkReferences(pReferenceDefinition, writeBackColumns);
		for (StorageReferenceDefinition subRefDef : subStorages)
		{
			columnsToSetReference = ArrayUtil.removeAll(columnsToSetReference, subRefDef.getColumnNames());
		}
		
		for (int i = 0; i < columnsToSetReference.length; i++)
		{
			try
			{
				mdServerMetaData.getMetaData().getColumnMetaData(columnsToSetReference[i]).setLinkReference(pReferenceDefinition);
			}
			catch (ModelException ex)
			{
				throw new DataSourceException("Can't install automatic link reference!", ex);
			}
		}
	}
	
	/**
	 * Creates the sub storage name for an AutomaticLinkReference. 
	 * 
	 * @param pSubStorage the <code>ISubStorage</code> to use.
	 * @return the substorage name for an AutomaticLinkReference. 
	 */
	protected String createSubStorageName(ISubStorage pSubStorage)
	{
	    String sStorageName = pSubStorage.createSubStorageName();
        String sUniqueStorageName = sStorageName;
        
		int count = 1;
		while (hasDifferentStorageWithName(pSubStorage, sUniqueStorageName))
		{
			count++;
			sUniqueStorageName = sStorageName + count;
		}
		return sUniqueStorageName;
	}

	/**
	 * Has a registered SubStorage with the given name, that is different.
	 * 
	 * @param pStorage the storage.
	 * @param pStorageName the storage.
	 * @return true, if it has a registered SubStorage with the given name, that is different.
	 */
	private boolean hasDifferentStorageWithName(ISubStorage pStorage, String pStorageName)
	{
		ISubStorage existingStorage = hmpSubStorages.get(pStorageName);
		
		if (existingStorage == null)
		{
			return false;
		}
		else
		{
			return !(existingStorage.equals(pStorage));
		}
	}
	
    /**
	 * Creates a new <code>DBStorage</code> which is configured for automatic link cell editors. The auto link
	 * reference feature is disabled for this storage.
	 *  
	 * @param pReferenceDefinition the storage definition.
	 * @param pWritebackColumnNames	the write back columns
	 * @throws DataSourceException if the from clause causes errors or the metadata are not available
	 */
	protected void createAutomaticLinkStorage(StorageReferenceDefinition pReferenceDefinition, String[] pWritebackColumnNames) throws DataSourceException
	{
        Record record = ProtocolFactory.openRecord(ICategoryConstants.STORAGE, ICommandConstants.STORAGE_CREATE_AUTOLINK);

        try
        {
    	    TranslationMap tmpAutoLinkColumnNames = DBAccess.getAutomaticLinkColumnNameTranslation();
    		
    		String writeBackTable = pReferenceDefinition.getReferencedStorage();
    		String[] refDefColumns = pReferenceDefinition.getColumnNames();
    		String[] refDefReferencedColumns = pReferenceDefinition.getReferencedColumnNames();
    		
    		String[] refDefFKColumns = ArrayUtil.intersect(refDefColumns, pWritebackColumnNames);
    		
    
    		boolean isAutomaticLinkReference = writeBackTable.startsWith(AUTOMATICLINKREFERENCE_PREFIX);
    		if (isAutomaticLinkReference)
    		{
    			writeBackTable = writeBackTable.substring(AUTOMATICLINKREFERENCE_PREFIX.length());
    		}
    		
    		DBStorage dbsReferencedStorage = new DBStorage();
    		dbsReferencedStorage.setDBAccess(dbAccess);
    		dbsReferencedStorage.setWritebackTable(writeBackTable);
    		dbsReferencedStorage.setAutoLinkReference(false);
    
    		List<StorageReferenceDefinition> subReferences = getSubAutomaticLinkReferences(pReferenceDefinition, pWritebackColumnNames);
    			
    		if (subReferences.size() > 0) // If sub references are found, the columns are added to this reference.
    	    {							  // The necessary foreign keys are created manually, to avoid endless loops, and unnecessary joins.
    			for (StorageReferenceDefinition subRefDef : subReferences)
    	    	{
    				String[] subRefDefColumns = subRefDef.getColumnNames();
    	    		String[] subRefDefReferencedColumns = subRefDef.getReferencedColumnNames();
    	    		String   subRefDefReferencedStorage = subRefDef.getReferencedStorage();
    	    		
    	    		ISubStorage iSubStorage = getSubStorage(subRefDefReferencedStorage);
    	    		
    	    		if (iSubStorage instanceof DBStorage)
    	    		{
    	    		    DBStorage subRefDefStorage = (DBStorage)iSubStorage;
                        String[] newSubRefDefColumns = new String[subRefDefColumns.length];
                        
                        if (subRefDefStorage.getWritebackTable() != null 
                                && !writeBackTable.toLowerCase().equals(subRefDefStorage.getWritebackTable().toLowerCase()))
                        {
                            for (int i = 0; i < newSubRefDefColumns.length; i++)
                            {
                                int pos = ArrayUtil.indexOf(refDefColumns, subRefDefColumns[i]);
                                if (pos < 0)
                                {
                                    String newSubRefColumnName = subRefDefColumns[i]; 
                                    int prefixIndex = subRefDefColumns[i].indexOf('_');
                                    if (prefixIndex >= 0)
                                    {
                                        String prefix = newSubRefColumnName.substring(0, prefixIndex);
                                        String newPrefix = null;
                                        for (int j = 0; newPrefix == null && j < refDefColumns.length; j++)
                                        {
                                            if (prefix.equals(tmpAutoLinkColumnNames.translate(refDefColumns[j])))
                                            {
                                                newPrefix = tmpAutoLinkColumnNames.translate(refDefReferencedColumns[j]);
                                            }
                                        }
                                        if (newPrefix != null)
                                        {
                                            newSubRefColumnName = newPrefix + newSubRefColumnName.substring(prefix.length());
                                        }
                                    }
                                    newSubRefDefColumns[i] = newSubRefColumnName; 
                                    // This column does not exist in this ref def, it should be added to the reference columns.
                                    refDefColumns = ArrayUtil.add(refDefColumns, subRefDefColumns[i]);
                                    refDefReferencedColumns = ArrayUtil.add(refDefReferencedColumns, newSubRefDefColumns[i]);
                                }
                                else
                                {
                                    // The corresponding column of the foreignkey has to be used. The prefix for above should be calculated from this column.
                                    newSubRefDefColumns[i] = refDefReferencedColumns[pos];
                                    // Remove the foreign key column for prefix generation.
                                    refDefFKColumns = ArrayUtil.remove(refDefFKColumns, subRefDefColumns[i]);
                                }
                            }
                            
                            dbsReferencedStorage.createAutomaticLinkReference(newSubRefDefColumns, subRefDefStorage, subRefDefReferencedColumns);
                        }
    	    		}
    	    	}
    	    }
    		
    		preConfigureAutomaticLinkStorage(dbsReferencedStorage);

    		dbsReferencedStorage.openInternal(false);
    		
    		if (isAutomaticLinkReference)
    		{
    			int maxColumnLength = ((DBAccess)dbAccess).getMaxColumnLength();
    			int maxPrefixLength = maxColumnLength / 2 + 5;
    			
    			// First unused foreign key column defines the prefix! 
    			String prefix;
    			if (refDefFKColumns.length > 0)
    			{
	    			prefix = tmpAutoLinkColumnNames.translate(refDefFKColumns[0]) + "_";
	    			if (prefix.length() > maxPrefixLength)
	    			{
	    				prefix = prefix.substring(0, maxPrefixLength - 1) + '_';
	    			}
    			}
    			else // If FKs are overdefined, the prefix cannot be generated from foreign key columns
    			{    // the table name is used
    				prefix = dbsReferencedStorage.getWritebackTable();
    				if (prefix == null)
    				{
    					prefix = dbsReferencedStorage.getFromClause();
    				}
    				prefix = StringUtil.getText(prefix, TextType.LettersDigitsWhitespace).replace("/n", " ").replace("/r", " ").replace("/t", " ");
    				int index = prefix.indexOf(' ');
    				if (index < 0)
    				{
    					index = prefix.length();
    				}
    				prefix = prefix.substring(0, Math.min(4, index)).toUpperCase() + '_';
    			}
    			
    			String[] representationColumns = dbsReferencedStorage.getMetaData().getRepresentationColumnNames();
    			
				String[] newRefDefColumns = refDefColumns;						// We need the original columns, to reset for a second try
				String[] newRefDefReferencedColumns = refDefReferencedColumns;
				
				List<String> duplicates = new ArrayUtil<String>();				// List of dupicate columns
    			for (int i = 0; i < representationColumns.length; i++)			// Try old algorithm first, to be compatible
    			{
    				if (!ArrayUtil.contains(newRefDefReferencedColumns, representationColumns[i]))
    				{
    					String columnName = prefix + representationColumns[i];
    					if (columnName.length() > maxColumnLength)
    					{
    						columnName = columnName.substring(0, maxColumnLength);
    					}
    					if (ArrayUtil.contains(newRefDefColumns, columnName))
    					{
    						duplicates.add(columnName);
    					}
						newRefDefColumns = ArrayUtil.add(newRefDefColumns, columnName);
						newRefDefReferencedColumns = ArrayUtil.add(newRefDefReferencedColumns, representationColumns[i]);
    				}
    			}
    			if (duplicates.size() > 0) // In case we have duplicate columns, we can try a better algorithm 
    			{
    				newRefDefColumns = refDefColumns;
    				newRefDefReferencedColumns = refDefReferencedColumns;

    				String[] splitPrefix = prefix.split("_", -1);	// create a shorter prefix
    				for (int i = 0; i < splitPrefix.length; i++)
    				{
    					String sprPart = splitPrefix[i];
    					
    					if (sprPart.length() > 4)
    					{
    						splitPrefix[i] = sprPart.substring(0, 4);
    					}
    				}
    				String newPrefix = StringUtil.concat("_", splitPrefix);

	    			for (int i = 0; i < representationColumns.length; i++)
	    			{
    					String columnNamePart = representationColumns[i];
	    				if (!ArrayUtil.contains(newRefDefReferencedColumns, columnNamePart))
	    				{
	    					String columnName = prefix + columnNamePart;
	    					if (columnName.length() > maxColumnLength)
	    					{
	    						columnName = columnName.substring(0, maxColumnLength);
	    					}
	    					if (duplicates.contains(columnName))	// if it is one of the duplicate columns try to shorten it
	    					{
	    						String[] splitColumn = columnNamePart.split("_", -1);
	    						int maxPartlength = splitColumn.length > 3 ? 4 : 8;
	    						for (int j = 0; j < splitColumn.length - 1; j++)
	    						{
	    							String sprPart = splitColumn[j];
	    	    					if (sprPart.length() > maxPartlength)
	    	    					{
	    	    						splitColumn[j] = sprPart.substring(0, maxPartlength);
	    	    					}
	    						}
	    						columnNamePart = StringUtil.concat("_", splitColumn);
	    					}
    						columnName = newPrefix + columnNamePart;
	    					if (columnName.length() > maxColumnLength)
	    					{
	    						columnName = columnName.substring(0, maxColumnLength);
	    					}

	    					int counter = 2;
    						while (ArrayUtil.contains(newRefDefColumns, columnName))
    						{
    							String strCount = "" + counter;
    							if (columnName.length() > maxColumnLength - strCount.length())
    							{
    								columnName = columnName.substring(0, maxColumnLength - strCount.length());
    							}
    							columnName = columnName + strCount; 
    						}
	    					
    						newRefDefColumns = ArrayUtil.add(newRefDefColumns, columnName);
    						newRefDefReferencedColumns = ArrayUtil.add(newRefDefReferencedColumns, representationColumns[i]);
	    				}
	    			}
    			}
    			refDefColumns = newRefDefColumns;
    			refDefReferencedColumns = newRefDefReferencedColumns;
    		}
/*   		// Restrict columns of SubStorages to referenced columns.
    		try
    		{
    			ServerColumnMetaData[] serverColumnMetaData = new ServerColumnMetaData[refDefReferencedColumns.length];
    			String[] queryColumns = new String[refDefReferencedColumns.length];
    			for (int i = 0; i < refDefReferencedColumns.length; i++)
    			{
    				int index = dbsReferencedStorage.mdServerMetaData.getServerColumnMetaDataIndex(refDefReferencedColumns[i]);
    				serverColumnMetaData[i] = dbsReferencedStorage.mdServerMetaData.getServerColumnMetaData(index);
    				if (dbsReferencedStorage.getQueryColumns() == null)
    				{
    					queryColumns[i] = refDefReferencedColumns[i];
    				}
    				else
    				{
    					queryColumns[i] = dbsReferencedStorage.getQueryColumns()[index];
    				}
    			}
    			MetaData md = new MetaData();
    			md.setPrimaryKeyColumnNames(dbsReferencedStorage.mdServerMetaData.getPrimaryKeyColumnNames());
    			md.setAutoIncrementColumnNames(dbsReferencedStorage.mdServerMetaData.getAutoIncrementColumnNames());
    			md.setRepresentationColumnNames(dbsReferencedStorage.mdServerMetaData.getRepresentationColumnNames());
    			dbsReferencedStorage.mdServerMetaData.setMetaData(md);
    			dbsReferencedStorage.mdServerMetaData.setServerColumnMetaData(serverColumnMetaData);
    			dbsReferencedStorage.setQueryColumns(queryColumns);
    		}
    		catch (Exception ex)
    		{
    			debug(ex); // Ignore, as it was only a try to optimize fetched data.
    		}
*/
    		String sStorageName = putSubStorage(dbsReferencedStorage);
    
    		if (record != null)
    		{
    		    record.setParameter(sStorageName);
    		}
	    		
    		pReferenceDefinition.setColumnNames(refDefColumns);
    		pReferenceDefinition.setReferencedColumnNames(refDefReferencedColumns);
    		pReferenceDefinition.setReferencedStorage(sStorageName);
    		
    		postConfigureAutomaticLinkStorage(pReferenceDefinition, dbsReferencedStorage);
        }
        finally
        {
            CommonUtil.close(record);
        }
	}
	
	/**
	 * Configures the given sub storage before it will be opened.
	 * 
	 * @param pStorage the default configured sub storage.
	 */
	protected void preConfigureAutomaticLinkStorage(DBStorage pStorage)
	{
		//relevant for sub classes
	}
	
	/**
	 * Configures the given sub storage before it will be opened.
	 * 
	 * @param pReferenceDefinition the configured reference definition.
	 * @param pStorage the sub storage.
	 */
	protected void postConfigureAutomaticLinkStorage(StorageReferenceDefinition pReferenceDefinition, DBStorage pStorage)
	{
		//relevant for sub classes
	}

	/**
	 * True, if one of the writable columns is nullable.
	 * 
	 * @param pColumns the columns.
	 * @param pColumnMetaData the meta data.
	 * @return True, if one of the writable columns is nullable.
	 */
	protected boolean isAutomaticLinkNullable(String[] pColumns, ServerColumnMetaData[] pColumnMetaData)
	{
		boolean result = true;
		for (int i = 0; i < pColumnMetaData.length; i++)
		{
			ServerColumnMetaData columnMetaData = pColumnMetaData[i];
			
			if (ArrayUtil.contains(pColumns, columnMetaData.getName()))
			{
				if (columnMetaData.isWritable())
				{
					if (columnMetaData.isNullable())
					{
						return true;
					}
					else
					{
						result = false;
					}
				}
			}
		}
		
		return result;
	}
	
	/**
	 * It creates and returns a from clause with joins over all foreign tables.
	 *  
	 * @param pWriteBackTable the write back table to use.
	 * @param pWriteBackColumnMetaData the meta data.
	 * @return a from clause with joins over all foreign tables.
	 * @throws DataSourceException	if an error occur in determining the from clause and query columns.
	 */
	private String getFromClause(String pWriteBackTable, ServerColumnMetaData[] pWriteBackColumnMetaData) throws DataSourceException
	{
		StringBuilder sbFromClause = new StringBuilder(pWriteBackTable);
		sbFromClause.append(" m");
		
        ArrayUtil<String> auColumnNames = new ArrayUtil<String>();
		for (int i = 0; i < pWriteBackColumnMetaData.length; i++)
        {
        	auColumnNames.add(pWriteBackColumnMetaData[i].getName());
        }
		String[] writeBackColumns = auColumnNames.toArray(new String[auColumnNames.size()]);

		subStorageConditions = null;
		
		for (int i = 0, size = auSortedAutomaticLinkReferences.size(); i < size; i++)
		{
			sbFromClause.append("\n");
		    
			StorageReferenceDefinition linkReference = auSortedAutomaticLinkReferences.get(i);
			
			String[] columnNames = linkReference.getColumnNames();
			String[] referencedColumnNames = linkReference.getReferencedColumnNames();

			String[] allColumnNames = auColumnNames.toArray(new String[auColumnNames.size()]);
			String[] columnsToJoin = ArrayUtil.removeAll(columnNames, allColumnNames);
			auColumnNames.addAll(columnsToJoin);
			
			ISubStorage iSubStorage = getSubStorage(linkReference.getReferencedStorage());
			
			if (iSubStorage instanceof DBStorage)
			{
			    DBStorage storage = (DBStorage)iSubStorage;
	            String[] refWriteBackColumnNames = storage.mdServerMetaData.getWritableColumnNames();

	            String[] fkColumns = ArrayUtil.intersect(columnNames, writeBackColumns);
	            String[] pkColumns = new String[fkColumns.length];
	            for (int j = 0; j < fkColumns.length; j++)
	            {
	                pkColumns[j] = referencedColumnNames[ArrayUtil.indexOf(columnNames, fkColumns[j])];
	            }
	            String[] realPkColumns = storage.mdServerMetaData.getPrimaryKeyColumnNames();
	            if (realPkColumns != null 
	                    && realPkColumns.length > 0
	                    && ArrayUtil.containsAll(pkColumns, realPkColumns))
	            {
	                pkColumns = realPkColumns;
	                fkColumns = new String[pkColumns.length];
	                for (int j = 0; j < pkColumns.length; j++)
	                {
	                    fkColumns[j] = columnNames[ArrayUtil.indexOf(referencedColumnNames, pkColumns[j])];
	                }
	            }
                if (fkColumns.length > 0)  // if no column of link reference is part of writeback columns, do not join, instead of exception!
                {
    	            // use correct join type 
    	            if (isAutomaticLinkNullable(columnNames, pWriteBackColumnMetaData)
    	                    || storage.getRestrictCondition() != null)
    	            {
    	                sbFromClause.append("       LEFT OUTER JOIN ");
    	            }
    	            else
    	            {
    	                sbFromClause.append("       INNER JOIN ");
    	            }
    	            
    	            // if all columns to join exist in the referenced write back table, a simple join can be made.
    	            boolean simpleJoin = storage.getWritebackTable() != null && storage.getRestrictCondition() == null;
    	            for (int j = 0; simpleJoin && j < columnsToJoin.length; j++)
    	            {
    	                simpleJoin = ArrayUtil.contains(refWriteBackColumnNames, linkReference.getReferencedColumnName(columnsToJoin[j]));
    	            }
    	            
    	            // We only use the writeback table if there is no restrict condition
    	            // on this storage. Otherwise we will make a subquery out of it.
    	            if (simpleJoin)
    	            {
    	                sbFromClause.append(storage.getWritebackTable());
    	            }
    	            else
    	            {
    	                // We must clone the restrict condition at this point, because
    	                // later on we will alter the restrict condition (like change
    	                // the column name) to turn them into a named parameter.
    	                // If we would not clone it, we would alter the original one
    	                // which might render the storage unusable.
    	                ICondition subStorageRestrictCondition = null;
    
    	                if (storage.getRestrictCondition() != null)
    	                {
    	                    subStorageRestrictCondition = storage.getRestrictCondition().clone();
    	                }
    	                
    	                ParameterizedStatement statement = dbAccess.getParameterizedSelectStatement(
    	                        storage.mdServerMetaData,
    	                        storage.getBeforeQueryColumns(),
    	                        storage.getQueryColumns(),
    	                        storage.getFromClause(),
    	                        subStorageRestrictCondition,
    	                        storage.getWhereClause(),
    	                        storage.getAfterWhereClause(),
    	                        null,
    	                        null,
    	                        null,
    	                        0,
    	                        -1);
    	                
    	                statement.setStatement(statement.getStatement().replaceAll("\\s+", " "));
    	                
    	                if (subStorageRestrictCondition != null)
    	                {
    	                    if (subStorageConditions == null)
    	                    {
    	                        subStorageConditions = subStorageRestrictCondition;
    	                    }
    	                    else if (subStorageRestrictCondition != null)
    	                    {
    	                        subStorageConditions = subStorageConditions.and(subStorageRestrictCondition);
    	                    }
    	                    if (storage.subStorageConditions != null)
    	                    {
    	                        subStorageConditions = subStorageConditions.and(storage.subStorageConditions);
    	                    }
    	                }
    	                
    	                String parameterPrefix = linkReference.getReferencedStorage();
    	                parameterPrefix = parameterPrefix.toUpperCase();
    	                parameterPrefix = parameterPrefix.replace('.', '_');
    	                
    	                int conditionIndex = 0;
    	                int questionMarkIndex = statement.getStatement().indexOf('?');
    	                
    	                while (questionMarkIndex > 0 && conditionIndex < statement.getNames().size())
    	                {
    	                    String name = statement.getNames().get(conditionIndex);
    	                    CompareCondition condition = statement.getNameToCondition().get(name);
    	                    name = name.toUpperCase();
    	                    
    	                    if (condition != null)
    	                    {
    	                        name = parameterPrefix + "_" + name;
    	                        condition.setColumnName(name);
    	                    }
    	                    statement.setStatement(statement.getStatement().substring(0, questionMarkIndex)
    	                            + ":" + name
    	                            + statement.getStatement().substring(questionMarkIndex + 1));
    	                    
    	                    conditionIndex++;
    	                    questionMarkIndex = statement.getStatement().indexOf('?', questionMarkIndex);
    	                }
    	                
    	                sbFromClause.append("(");
    	                sbFromClause.append(statement.getStatement());
    	                sbFromClause.append(")");
    	            }
    	            int counter = i + 1;
    	            
    	            sbFromClause.append(" l");
    	            sbFromClause.append(counter);
    	            sbFromClause.append(" ON ");

    	            boolean first = true;
    	            for (int j = 0; j < fkColumns.length; j++)
    	            {
    	                ServerColumnMetaData columnMetaData = pWriteBackColumnMetaData[ArrayUtil.indexOf(writeBackColumns, fkColumns[j])];
    
    	                if (!first)
    	                {
    	                    sbFromClause.append(" AND ");
    	                }
    	                first = false;
    
    	                sbFromClause.append("m.");
    	                sbFromClause.append(columnMetaData.getColumnName().getQuotedName());
    	                sbFromClause.append(" = ");
    	                sbFromClause.append("l");
    	                sbFromClause.append(counter);
    	                sbFromClause.append('.');
    	                try
    	                {
    	                    sbFromClause.append(storage.mdServerMetaData.getServerColumnMetaData(pkColumns[j]).getColumnName().getQuotedName());
    	                }
    	                catch (ModelException pEx)
    	                {
    	                    throw new DataSourceException(pEx.getMessage());
    	                }
    	            }
	            }
			}
		}
		
		return sbFromClause.toString();
	}

	/**
	 * Generates the indexes for sub storages.
	 * 
	 * @param pWriteBackColumnMetaData the write back columns, defines the index order.
	 */
	private void generateSubStorageIndex(ServerColumnMetaData[] pWriteBackColumnMetaData)
	{
		auSortedAutomaticLinkReferences.clear();
		ArrayUtil<Integer> auIndexes = new ArrayUtil<Integer>();
		
		ArrayUtil<String> auColumnNames = new ArrayUtil<String>();
		
		for (int i = 0; i < pWriteBackColumnMetaData.length; i++)
		{
			auColumnNames.add(pWriteBackColumnMetaData[i].getName());
		}
		
		for (StorageReferenceDefinition linkReference : auAutomaticLinkReferences)
		{
			String[] allColumnNames = auColumnNames.toArray(new String[auColumnNames.size()]);
			
			String[] columnNames = linkReference.getColumnNames();
			
			String[] columnsToJoin = ArrayUtil.removeAll(columnNames, allColumnNames);
			
			if (columnsToJoin.length > 0)
			{
				int index = -1;
				for (int i = 0; i < columnNames.length; i++)
				{
					int ind = auColumnNames.indexOf(columnNames[i]);
					if (ind > index)
					{
						index = ind;
					}
				}
				
				int pos = auIndexes.size();
				while (pos > 0 && index < auIndexes.get(pos - 1).intValue())
				{
					pos--;
				}
				auIndexes.add(pos, Integer.valueOf(index));
				auSortedAutomaticLinkReferences.add(pos, linkReference);
				
				auColumnNames.addAll(index + 1, columnsToJoin);
			}
		}
	}
	
	/**
	 *  It creates and returns the query columns, with all write back columns and includes the columns for the 
	 *  LinkReferences (automatic link celleditors).
	 *  It also removes all intersecting and unused ForeignKeys from the list of ForeignKeys!
	 *  
	 * @param pWriteBackColumnMetaData the write columns.
	 * @return the query columns, with all write back columns and includs the columns for the LinkReferences (automatic link celleditors).
	 * @throws DataSourceException if it fails.
	 */
	private String[] getQueryColumns(ServerColumnMetaData[] pWriteBackColumnMetaData) throws DataSourceException
	{
		ArrayUtil<String> auQueryColumns = new ArrayUtil<String>();
		ArrayUtil<String> auColumnNames = new ArrayUtil<String>();
		
		for (int i = 0; i < pWriteBackColumnMetaData.length; i++)
		{
			auQueryColumns.add("m." + pWriteBackColumnMetaData[i].getColumnName().getQuotedName());
			auColumnNames.add(pWriteBackColumnMetaData[i].getName());
		}
		for (StorageReferenceDefinition linkReference : auAutomaticLinkReferences)
		{
			String[] allColumnNames = auColumnNames.toArray(new String[auColumnNames.size()]);
			
			String[] columnNames = linkReference.getColumnNames();
			String[] referencedColumnNames = linkReference.getReferencedColumnNames();
			
			String[] columnsToJoin = ArrayUtil.removeAll(columnNames, allColumnNames);
			
			if (columnsToJoin.length > 0 && columnsToJoin.length < columnNames.length) // if no column of link reference is part of writeback columns, do not join!
			{
				int counter = auSortedAutomaticLinkReferences.indexOfReference(linkReference) + 1;
				
				ISubStorage iSubStorage = getSubStorage(linkReference.getReferencedStorage());
				
				if (iSubStorage instanceof DBStorage)
				{
				    DBStorage storage = (DBStorage)iSubStorage;

	                int index = -1;
	                for (int i = 0; i < columnNames.length; i++)
	                {
	                    int ind = auColumnNames.indexOf(columnNames[i]);
	                    if (ind > index)
	                    {
	                        index = ind;
	                    }
	                }
	                
	                String[] queryCols = new String[columnsToJoin.length];
	                for (int i = 0; i < columnsToJoin.length; i++)
	                {
	                    String referencedColumn = referencedColumnNames[ArrayUtil.indexOf(columnNames, columnsToJoin[i])];
	                    
	                    try
	                    {
	                        queryCols[i] = "l" + counter + "." 
	                                    + storage.mdServerMetaData.getServerColumnMetaData(referencedColumn).getColumnName().getQuotedName() +
	                                    " " + columnsToJoin[i];
	                    }
	                    catch (ModelException ex)
	                    {
	                        throw new DataSourceException("Column was not found!", ex);
	                    }
	                }
	                
	                auQueryColumns.addAll(index + 1, queryCols);
	                auColumnNames.addAll(index + 1, columnsToJoin);
				}
			}
		}

		return auQueryColumns.toArray(new String[auQueryColumns.size()]);
	}	
	
	/**
	 * Returns the meta data for the specified DBStorage with all its parameters.
	 * 
	 * @param pBeforeQueryColumns the before query columns.
	 * @param pQueryColumns	the query columns.
	 * @param pFromClause the from clause with query tables and join definitions.
	 * @param pWhereClause the last where condition in query.
	 * @param pAfterWhereClause the after where clause in query.
	 * @param pWritebackTable the write back table to use.
	 * @param pWritebackColumns the write back columns to use.
	 * @param pAutoLinkReference <code>yes</code> if the <code>LinkReferences</code> are automatic set.
	 * @param pUseRepresentationColumnsAsQueryColumns <code>yes</code> if the QueryColumns are set with 
	 *                                                all representation columns including the Primary Key columns.
	 * @return the ServerMetaData 
	 * @throws DataSourceException if the meta data couldn't determined.
	 */
	protected ServerMetaData createMetaData(String pBeforeQueryColumns,
        						            String[] pQueryColumns,
        						            String pFromClause,
        								    String pWhereClause,
        								    String pAfterWhereClause,
        								    String pWritebackTable,
        								    String[] pWritebackColumns,
        								    boolean pAutoLinkReference,
        								    boolean pUseRepresentationColumnsAsQueryColumns) throws DataSourceException
	{	
        Key         pk = null;
        List<Key>   auUniqueKeyColumns  = null;
        Map<String, Object> htDefaults = null;
        Map<String, Object[]> htAllowed = null;

        ServerColumnMetaData[] cmdWritebackColumns = null;
        String[] writebackColumnNames = null;
        
        MetaData metaData;
        
        boolean primaryKeySet;
        boolean representationColumnsSet;

        
        metaData = mdServerMetaData.getMetaData();
        
        primaryKeySet = metaData.getPrimaryKeyColumnNames() != null;
        representationColumnsSet = metaData.getRepresentationColumnNames() != null;

        if (pWritebackTable != null)
		{
			// check if the WritebackTable is a Synonym, if so use the real table instead.
			String sRealWritebackTable = dbAccess.getTableForSynonym(pWritebackTable);
			
			// no Where Clause for determining meta data just use "metadata where clause)
			TableInfo mdInfoWritebackTable = dbAccess.getTableInfo(sRealWritebackTable);
			cmdWritebackColumns = dbAccess.getColumnMetaData(sRealWritebackTable, null, null, null, null);
				
			// Set all columns writeable for further getFromClause and isAutomaticLinkNullable check. 
			// Otherwise all AutoLink are joined over INNER JOIN!!!
			for (int i = 0; i < cmdWritebackColumns.length; i++)
			{
				cmdWritebackColumns[i].setWritable(true);
			}
			
			writebackColumnNames = BeanUtil.toArray(cmdWritebackColumns, new String[cmdWritebackColumns.length], "name");
				
			//cache information
			sCatalog = mdInfoWritebackTable.getCatalog();
			sSchema  = mdInfoWritebackTable.getSchema();
	
			debug("WritebackTable Schema=", sSchema, ",Catalog=", sCatalog);
			
			// #136 - Mysql PK refetch not working -> extract the plan table without schema. 
			String sTableWithoutSchema = mdInfoWritebackTable.getTable();

			// if auto link reference should be analysed, automatic link references are created from foreign keys.
			if (pAutoLinkReference)
			{
				List<ForeignKey>	auForeignKeys = dbAccess.getForeignKeys(sCatalog, sSchema, sTableWithoutSchema);
				
				String sDefaultSchema = ((DBAccess)dbAccess).getDefaultSchema();
				if (sDefaultSchema == null && sSchema != null && pWritebackTable.equalsIgnoreCase(sRealWritebackTable)
						&& !DBAccess.removeQuotes(pWritebackTable).toUpperCase().startsWith(sSchema.toUpperCase()))
				{
					sDefaultSchema = sSchema;
				}

				for (ForeignKey fk : auForeignKeys)
				{
					String sAutomaticLinkStorageFromClause = fk.getPKTable().getQuotedName();
					String sPKSchema = (fk.getPKSchema() == null) ? null : fk.getPKSchema().getQuotedName();
					
					// Check if the default Schema is different to the schema of the writeback table
					// in that case prefix the schema for the FROM Clause of the AutoLink Storage
					if ((sDefaultSchema == null && sPKSchema != null)
							|| (sDefaultSchema != null && !sDefaultSchema.equals(sPKSchema)))
					{
						sAutomaticLinkStorageFromClause = sPKSchema + "." + sAutomaticLinkStorageFromClause;
					}
					
					StorageReferenceDefinition refDef = new StorageReferenceDefinition(
							BeanUtil.toArray(fk.getFKColumns(), new String[fk.getFKColumns().length], "name"),
							AUTOMATICLINKREFERENCE_PREFIX  + sAutomaticLinkStorageFromClause,
							BeanUtil.toArray(fk.getPKColumns(), new String[fk.getPKColumns().length], "name")
					);
					addAutomaticLinkReferenceIntern(refDef, writebackColumnNames);
				}
			}
			
			if (isDefaultValue())
			{
				htDefaults = dbAccess.getDefaultValues(sCatalog, sSchema, sTableWithoutSchema);
			}
				
			if (isAllowedValues())
			{
				htAllowed = dbAccess.getAllowedValues(sCatalog, sSchema, sTableWithoutSchema);

				//#211
				//Set default allowed values, if available, to column which does not have default values 
				Object[] oAllowed;
				
				for (int i = 0, anz = cmdWritebackColumns.length; i < anz; i++)
				{
					if (htAllowed == null || htAllowed.get(cmdWritebackColumns[i].getName()) == null)
					{
						oAllowed = dbAccess.getDefaultAllowedValues(sCatalog, sSchema, sTableWithoutSchema, cmdWritebackColumns[i]);
						
						if (oAllowed != null)
						{
							if (htAllowed == null)
							{
								htAllowed = new Hashtable<String, Object[]>();
							}
							
							htAllowed.put(cmdWritebackColumns[i].getName(), oAllowed);
						}
					}
				}
			}
			
			if (!primaryKeySet)
			{
				pk = dbAccess.getPrimaryKey(sCatalog, sSchema, sTableWithoutSchema);
			}
			if (!representationColumnsSet)
			{
				auUniqueKeyColumns  = dbAccess.getUniqueKeys(sCatalog, sSchema, sTableWithoutSchema);
			}

			// Add all manual link references in any case. This will overwrite link references created from foreign keys.
			for (StorageReferenceDefinition linkRef : auManualLinkReferences)
			{
				addAutomaticLinkReferenceIntern(linkRef, writebackColumnNames);
			}
			
			// Analyse all automatic link references, and create storages, if necessary.
			for (StorageReferenceDefinition refDef : auAutomaticLinkReferences)
			{
				String writeBackTableOrStorageName = refDef.getReferencedStorage();
				
				if (!writeBackTableOrStorageName.startsWith(SUBSTORAGE_PREFIX))
				{
					createAutomaticLinkStorage(refDef, writebackColumnNames);
				}
			}
			
			//-------------------------------------------------------------------------
			// Update from clause and query columns with the link table information
			// (if the user didn't set this properties)
			//-------------------------------------------------------------------------

			if (pFromClause == null)
			{	
				generateSubStorageIndex(cmdWritebackColumns);
				
				String[] queryColumns = getQueryColumns(cmdWritebackColumns);
				if (pQueryColumns == null)
				{
					pQueryColumns = queryColumns;
				}
				else
				{
					for (int i = 0; i < pQueryColumns.length; i++)
					{
						String queryColumn = pQueryColumns[i].toLowerCase();
						if (!queryColumn.contains(".") && !StringUtil.containsWhitespace(queryColumn))
						{
							String newRealName = null;
							for (int j = 0; newRealName == null && j < queryColumns.length; j++)
							{
								String name = queryColumns[j].toLowerCase();
								int index = name.length() - queryColumn.length() - 1;
								if (name.endsWith(queryColumn)
										&& (index < 0 || Character.isWhitespace(name.charAt(index)) || name.charAt(index) == '.'))
								{
									newRealName = queryColumns[j];
								}
							}
							if (newRealName != null)
							{
								pQueryColumns[i] = newRealName;
							}
						}
					}
				}
				setQueryColumns(pQueryColumns);
				
				pFromClause = getFromClause(pWritebackTable, cmdWritebackColumns);
				setFromClause(pFromClause);
			}
		}

        //No writeback table -> No writeback feature!
        if (pWritebackTable == null
            && !hasInsteadOfInsertEventHandler()
            && !hasInsteadOfUpdateEventHandler()
            && !hasInsteadOfDeleteEventHandler())
        {
            metaData.removeFeature(Feature.WriteBack);
        }
	
        ServerColumnMetaData[] cmda = null;
        
        if (pQueryColumns == null)
        {
    		// Use Where Clause for determining meta data
    		// This ensures that in case of wrong definition an error will occur on open.
    		// Defining complex selects, queries will be executed faster.
        	cmda = dbAccess.getColumnMetaData(pFromClause, pQueryColumns, 
    			                              pBeforeQueryColumns, pWhereClause, pAfterWhereClause);
        	
        	pQueryColumns = new String[cmda.length];
        	for (int i = 0; i < cmda.length; i++)
    		{
        		pQueryColumns[i] = cmda[i].getColumnName().getQuotedName();
    		}

			setQueryColumns(pQueryColumns);
        }
        
        if (saAdditionalQueryColumns != null)
        {
        	pQueryColumns = ArrayUtil.addAll(pQueryColumns, saAdditionalQueryColumns);
        	setQueryColumns(pQueryColumns);
		}
        
		if (cmda == null || saAdditionalQueryColumns != null)
		{
    		// Use Where Clause for determining meta data
    		// This ensures that in case of wrong definition an error will occur on open.
    		// Defining complex selects, queries will be executed faster.
    		cmda = dbAccess.getColumnMetaData(pFromClause, pQueryColumns, 
    			                              pBeforeQueryColumns, pWhereClause, pAfterWhereClause);
        }

        if (isAutoLinkReference())
        {
            addNotDatabaseLinkedColumns(metaData);
        }

		// set Auto increment array, default and allowed values
		
	    mdServerMetaData.setServerColumnMetaData(cmda);
	    ArrayUtil<Name> saAutoIncrementCols = new ArrayUtil<Name>(); 
	    
		for (int i = 0; i < cmda.length; i++)
		{
			cmda[i].setCalculated(false);
			
			//collect auto increment columns
			if (cmda[i].isAutoIncrement())
			{
				saAutoIncrementCols.add(cmda[i].getColumnName());
			}
			
			//set default values
			if (htDefaults != null)
			{
				cmda[i].setDefaultValue(htDefaults.get(cmda[i].getName()));
			}
			
			//set allowed values
			if (htAllowed != null)
			{
				Object[] oAllowed = htAllowed.get(cmda[i].getName());
				
				//use only if values are set!
				if (oAllowed != null && oAllowed.length > 0)
				{
					cmda[i].setAllowedValues(oAllowed);
				}
			}
			
			// #309 - isNullable will be determined from the writebackTable instead of the fromClause 
			// all columns of writeWriteBackTable are writeable
			// get essential information of write back table.
			if (cmdWritebackColumns != null)
			{
				int columnIndex = ServerMetaData.getServerColumnMetaDataIndex(cmdWritebackColumns, cmda[i].getName());
				
				if (columnIndex >= 0)
				{
					cmda[i].setDetectedType(cmdWritebackColumns[columnIndex].getDetectedType());
					cmda[i].setSQLTypeName(cmdWritebackColumns[columnIndex].getSQLTypeName());
					
					if (cmda[i].getAllowedValues() == null && cmdWritebackColumns[columnIndex].getAllowedValues() != null)
					{
						cmda[i].setAllowedValues(cmdWritebackColumns[columnIndex].getAllowedValues());
					}
					
					cmda[i].setNullable(cmdWritebackColumns[columnIndex].isNullable());
					
					cmda[i].setWritable(true);
				}
			}
		}
		
		if (!saAutoIncrementCols.isEmpty())
		{
			mdServerMetaData.setAutoIncrementColumnNames(saAutoIncrementCols.toArray(new Name[saAutoIncrementCols.size()]));
		}
			
		//----------------------------------------------------------------
		// PRIMARY KEY detection
		//----------------------------------------------------------------
		
		if (!primaryKeySet)
		{
			if (pk != null && pk.getColumns() != null && pk.getColumns().length > 0)
			{
				mdServerMetaData.setPrimaryKeyColumnNames(pk.getColumns());
				mdServerMetaData.setPrimaryKeyType(PrimaryKeyType.PrimaryKeyColumns);
                if (!ArrayUtil.containsAll(mdServerMetaData.getColumnNames(), mdServerMetaData.getPrimaryKeyColumnNames()))
                {
                    mdServerMetaData.getMetaData().setPrimaryKeyColumnNames(null);
                    debug("Primary Key of write back table " + pWritebackTable + " is removed, because it is not part of query columns!");
                }
			}
			else if (auUniqueKeyColumns != null && auUniqueKeyColumns.size() > 0)
			{
				mdServerMetaData.setPrimaryKeyColumnNames(auUniqueKeyColumns.get(0).getColumns());
				mdServerMetaData.setPrimaryKeyType(PrimaryKeyType.UniqueKeyColumns);
                if (!ArrayUtil.containsAll(mdServerMetaData.getColumnNames(), mdServerMetaData.getPrimaryKeyColumnNames()))
                {
                    mdServerMetaData.getMetaData().setPrimaryKeyColumnNames(null);
                    debug("Primary Key of write back table " + pWritebackTable + " is removed, because unique columns are not part of query columns!");
                }
			}
			else
			{
				String[] writableColumns = ArrayUtil.intersect(mdServerMetaData.getColumnNames(), writebackColumnNames);
				
				ArrayUtil<Name> auColumns = new ArrayUtil<Name>();
				
				for (int i = 0; i < writableColumns.length; i++)
				{
					try
					{
						ServerColumnMetaData serverColumnMetaData = mdServerMetaData.getServerColumnMetaData(writableColumns[i]);
						ColumnMetaData       columnMetaData = serverColumnMetaData.getColumnMetaData();
//						int sqlType = serverColumnMetaData.getSQLType();
						
						if (columnMetaData.getTypeIdentifier() != BinaryDataType.TYPE_IDENTIFIER
						        && (columnMetaData.getTypeIdentifier() != StringDataType.TYPE_IDENTIFIER 
						                 || (columnMetaData.getPrecision() <= 2000 && !serverColumnMetaData.getRealQueryColumnName().contains("\'"))))
                                            // Postgres delivers datatype OTHERS in this case so maybe its better, to ensure, that 
						                    // the real sql type is NCHAR, NVARCHAR, CHAR or VARCHAR
//						                 || (sqlType == DBAccess.NCHAR || sqlType == DBAccess.NVARCHAR || sqlType == Types.CHAR || sqlType == Types.VARCHAR)))
						{
							auColumns.add(serverColumnMetaData.getColumnName());
						}
					}
					catch (ModelException e)
					{
						// Ignore. If column does not exist, it is no primary key column anyway
					}
				}
				
				if (auColumns.size() > 0)
				{
					mdServerMetaData.setPrimaryKeyColumnNames(auColumns.toArray(new Name[auColumns.size()]));
					mdServerMetaData.setPrimaryKeyType(PrimaryKeyType.AllColumns);
				}
			}
		}
				
		//----------------------------------------------------------------
		// UNIQUE KEY columns as representation columns
		//----------------------------------------------------------------
		
		if (!representationColumnsSet && auUniqueKeyColumns != null && auUniqueKeyColumns.size() > 0)
		{
			ArrayUtil<Name> auColumns = new ArrayUtil<Name>();
			
			for (int i = 0; i < auUniqueKeyColumns.size(); i++)
			{
				Name[]    ukColumns   = auUniqueKeyColumns.get(i).getColumns(); 
				
				for (int j = 0; j < ukColumns.length; j++)
				{
					// #226: No duplicate columns anymore in representation columns, if it will be build from intersecting UKs
					if (auColumns.indexOf(ukColumns[j]) < 0)
					{
						auColumns.add(ukColumns[j]);
					}
				}
			}

			if (auColumns.size() > 0)
			{
				Name[] saColumns = new Name[auColumns.size()];
				auColumns.toArray(saColumns);
			
				mdServerMetaData.setRepresentationColumnNames(saColumns);
			}
		}
			
		if (pWritebackTable != null)
		{
			//----------------------------------------------------------------
			// ADD automatic link table reference definition
			//----------------------------------------------------------------

			String[] writebackColumnLabels = BeanUtil.toArray(cmdWritebackColumns, new String[cmdWritebackColumns.length], "label");
			HashMap<String, String> doubledColumns = new HashMap<String, String>();
			for (StorageReferenceDefinition linkReference : auAutomaticLinkReferences)
			{
				String[] saColumns = linkReference.getColumnNames();
				String[] saRefColumns = linkReference.getReferencedColumnNames();
				
				ISubStorage iSubStorage = getSubStorage(linkReference.getReferencedStorage());
				
				if (iSubStorage instanceof DBStorage)
				{
				    DBStorage refStorage = (DBStorage)iSubStorage;
				    
	                try
	                {
	                    for (int j = 0; j < saColumns.length; j++)
	                    {
	                        //configure the link-column, if available
	                        ServerColumnMetaData cmd = mdServerMetaData.getServerColumnMetaData(saColumns[j]);

	                        if (cmd.getLinkReference() == null)
	                        {
	                            if (!cmd.isWritable())
	                            {
	                                String label = refStorage.getMetaData().getColumnMetaData(saRefColumns[j]).getLabel();
	                                if (doubledColumns.put(label, label) != null && !ArrayUtil.contains(writebackColumnLabels, label))
	                                {
	                                    writebackColumnLabels = ArrayUtil.add(writebackColumnLabels, label);
	                                }
	                            }
	                        }
	                    }
	                }
	                catch (ModelException modelException)
	                {
	                    // Silent, it is logged afterwards.
	                }
				}
			}
			
			for (StorageReferenceDefinition linkReference : auAutomaticLinkReferences)
			{
				String[] saColumns = linkReference.getColumnNames();
				String[] saRefColumns = linkReference.getReferencedColumnNames();
				String storageName = linkReference.getReferencedStorage();
				
			    ISubStorage refStorage = getSubStorage(storageName);
                int index = storageName.lastIndexOf(".");
                if (index >= 0)
                {
                    storageName = storageName.substring(index + 1);
                }
                storageName = StringUtil.formatInitCap(storageName) + " ";
                
                // remove not existing columns from link reference, due to not existing columns
                // either because of manual set from clause or manual set query columns. 
                String[] existingColumns = ArrayUtil.intersect(saColumns, mdServerMetaData.getColumnNames());
                if (!Arrays.equals(saColumns, existingColumns))
                {
                    String[] existingRefColumns = new String[existingColumns.length];
                    for (int i = 0; i < existingRefColumns.length; i++)
                    {
                        existingRefColumns[i] = saRefColumns[ArrayUtil.indexOf(saColumns, existingColumns[i])];
                    }
                    saColumns = existingColumns;
                    saRefColumns = existingRefColumns;
                    linkReference.setColumnNames(saColumns);
                    linkReference.setReferencedColumnNames(saRefColumns);
                }
                
                try
                {
                    boolean isNullable = isAutomaticLinkNullable(saColumns, cmdWritebackColumns);
                
                    for (int j = 0; j < saColumns.length; j++)
                    {
                        //configure the link-column, if available
                        ColumnMetaData cmd = mdServerMetaData.getMetaData().getColumnMetaData(saColumns[j]);

                        if (cmd.getLinkReference() == null)
                        {
                            if (!cmd.isWritable())
                            {
                                String label = refStorage.getMetaData().getColumnMetaData(saRefColumns[j]).getLabel();
                                if (ArrayUtil.contains(writebackColumnLabels, label))
                                {
                                    label = storageName + label;
                                }
                                cmd.setLabel(label);
                                cmd.setNullable(isNullable);
                            }
                            cmd.setLinkReference(linkReference);
                        }
                    }
                }
                catch (ModelException modelException)
                {
                    // column not found or StorageReferenceDefinition couldn't created
                    debug(Arrays.toString(mdServerMetaData.getColumnNames()));
                    debug(modelException);
                }
            }
		}
		else
		{
			for (StorageReferenceDefinition linkReference : auManualLinkReferences)
			{
				if (!linkReference.getReferencedStorage().startsWith(SUBSTORAGE_PREFIX))
				{
					// This will create the SubStorage and correct the StorageReferenceDefinition refDef
					createAutomaticLinkStorage(linkReference, mdServerMetaData.getWritableColumnNames()); 
				}

				installAutomaticLinkReferenceIntern(linkReference);
			}
		}
	
		if (!representationColumnsSet && mdServerMetaData.getRepresentationColumnNames() != null)
		{
			String[] reprCols = mdServerMetaData.getRepresentationColumnNames();
			
			String[] result = null;
			for (int k = 0; result == null && k < reprCols.length; k++)
			{
				try
				{
					if (mdServerMetaData.getMetaData().getColumnMetaData(reprCols[k]).getTypeIdentifier() == StringDataType.TYPE_IDENTIFIER
							|| reprCols.length == 1) // a dedicated Unique key column on any data type will be preferred
					{
						result = new String[] {reprCols[k]};
					}
				}
				catch (ModelException ex)
				{
					// do nothing
				}
			}
			mdServerMetaData.setRepresentationColumnNames(result);
		}
		
		//no representation columns available -> use all available columns as representation columns
		if (!representationColumnsSet && mdServerMetaData.getRepresentationColumnNames() == null)
		{
			Name firstStringColumn = null;
			ArrayUtil<Name> nRepCols = new ArrayUtil<Name>();
			ArrayUtil<Name> allCols = new ArrayUtil<Name>();
			for (int k = 0; firstStringColumn == null && k < cmda.length; k++)
			{
				if ((!ArrayUtil.contains(mdServerMetaData.getPrimaryKeyColumnNames(), cmda[k].getName()) || mdServerMetaData.getPrimaryKeyType() == PrimaryKeyType.AllColumns)
						&& !RowDefinition.isColumnIgnored(cmda[k].getName()))
				{
					if (cmda[k].getColumnMetaData().getTypeIdentifier() == StringDataType.TYPE_IDENTIFIER)
					{
						firstStringColumn = cmda[k].getColumnName();
					}
					if (cmda[k].getColumnMetaData().getTypeIdentifier() != BinaryDataType.TYPE_IDENTIFIER)
					{
						nRepCols.add(cmda[k].getColumnName());
					}
				}
				if (cmda[k].getColumnMetaData().getTypeIdentifier() != BinaryDataType.TYPE_IDENTIFIER)
				{
					allCols.add(cmda[k].getColumnName());
				}
			}
			if (firstStringColumn != null)
			{
				mdServerMetaData.setRepresentationColumnNames(new Name[] {firstStringColumn});
			}
			else if (nRepCols.size() > 0)
			{
				mdServerMetaData.setRepresentationColumnNames(nRepCols.toArray(new Name[nRepCols.size()]));
			}
			else
			{
				mdServerMetaData.setRepresentationColumnNames(allCols.toArray(new Name[allCols.size()]));
			}
		}
		
		return mdServerMetaData;
	}	
		
	/**
	 * It close the DBStorage.
	 */
	public void close()
	{
		bIsOpen = false;

		setFromClause(sFromClauseBeforeOpen);
		setQueryColumns(saQueryColumnsBeforeOpen);
		auAutomaticLinkReferences.clear();
		auSortedAutomaticLinkReferences.clear();
	}
	
	/**
	 * Set the <code>IDBAccess</code> of this <code>DBStorage</code>.
	 * 
	 * @param pDBAccess the <code>IDBAccess</code> of this <code>DBStorage</code>.
	 */
	public void setDBAccess(IDBAccess pDBAccess)
	{
		dbAccess = (DBAccess)pDBAccess;
	}

	/**
	 * Returns the <code>IDBAccess</code> of this <code>DBStorage</code>.
	 * 
	 * @return the <code>IDBAccess</code> of this <code>DBStorage</code>.
	 */
	public IDBAccess getDBAccess()
	{
		return dbAccess;
	}
	
	/**
	 * Returns the query tables to use in the SELECT statement to get the data from the storage.
	 *
	 * @return the query tables to use in the SELECT statement to get the data from the storage.
	 */
	public String getFromClause()
	{
		return sFromClause;
	}

	/**
	 * Returns the query tables to use in the SELECT statement to get the data from the storage.
	 *
	 * @return the query tables to use in the SELECT statement to get the data from the storage.
	 */
	private String getFromClauseIntern()
	{
		if (sFromClause == null)
		{
			return sWritebackTable;
		}
		return sFromClause;
	}
	
	/**
	 * Sets the query tables to use in the SELECT statement to get the data from the storage.
	 *
	 * @param pFromClause the queryTables to set.
	 */
	public void setFromClause(String pFromClause)
	{
		sFromClause = pFromClause;
	}

	/**
	 * Returns the query columns of the SELECT statement.
	 *
	 * @return the query columns of the SELECT statement.
	 */
	public String[] getQueryColumns()
	{
		return saQueryColumns;
	}

	/**
	 * Sets the query columns of the SELECT statement.
	 *
	 * @param pQueryColumns the queryColumns to set.
	 */
	public void setQueryColumns(String[] pQueryColumns)
	{
		saQueryColumns = pQueryColumns;
	}

	/**
	 * Returns the additional query columns of the SELECT statement.
	 *
	 * @return the additional query columns of the SELECT statement.
	 */
	public String[] getAdditionalQueryColumns()
	{
		return saAdditionalQueryColumns;
	}

	/**
	 * Sets the additional query columns of the SELECT statement.
	 *
	 * @param pAdditionalQueryColumns the additional queryColumns to set.
	 */
	public void setAdditionalQueryColumns(String[] pAdditionalQueryColumns)
	{
		saAdditionalQueryColumns = pAdditionalQueryColumns;
	}

	/**
	 * Returns the string to place in the SELECT statement between the SELECT and the first query column.
	 *
	 * @return the string to place in the SELECT statement between the SELECT and the first query column.
	 */
	public String getBeforeQueryColumns()
	{
		return sBeforeQueryColumns;
	}

	/**
	 * Sets the string to place in the SELECT statement between the SELECT and the first query column.
	 *
	 * @param pBeforeQueryColumns the beforeQueryColumns to set.
	 */
	public void setBeforeQueryColumns(String pBeforeQueryColumns)
	{
		sBeforeQueryColumns = pBeforeQueryColumns;
	}

	/**
	 * Returns the string to place in the SELECT statement after the last WHERE condition from the
	 * Filter or MasterReference (Master-Detail Condition).
	 *
	 * @return the string to place in the SELECT statement after the last WHERE condition from the
	 *  	   Filter or MasterReference (Master-Detail Condition).
	 */
	public String getWhereClause()
	{
		return sWhereClause;
	}

	/**
	 * Sets the string to place in the SELECT statement after the last WHERE condition from the
	 * Filter or MasterReference (Master-Detail Condition).
	 *
	 * @param pWhereClause the last Where Condition to set.
	 */
	public void setWhereClause(String pWhereClause)
	{
		sWhereClause = pWhereClause;
	}

	/**
	 * Returns the string to place in the SELECT statement after the WHERE clause and before the ORDER BY clause.
	 *
	 * @return the string to place in the SELECT statement after the WHERE clause and before the ORDER BY clause.
	 */
	public String getAfterWhereClause()
	{
		return sAfterWhereClause;
	}

	/**
	 * Sets the string to place in the SELECT statement after the WHERE clause and before the ORDER BY clause.
	 * e.g. GROUP BY a, HAVING ...
	 *
	 * @param pAfterWhereClause the afterWhereClause to set.
	 */
	public void setAfterWhereClause(String pAfterWhereClause)
	{
		sAfterWhereClause = pAfterWhereClause;
	}
	
	/**
	 * Returns the string to place in the ORDER BY section if no default sort is set.
	 * 
	 * @return the string to place in the ORDER BY section if no default sort is set.
	 */
	public String getOrderByClause()
	{
		return sOrderByClause;
	}
	
	/**
	 * Sets the string to place in the ORDER BY section if no default sort is set.
	 * 
	 * @param pOrderByClause the string to place in the ORDER BY section if no default sort is set.
	 */
	public void setOrderByClause(String pOrderByClause)
	{
		sOrderByClause = pOrderByClause;
	}

	/**
     * Returns the string to place in the SELECT statement after the ORDER BY clause.
     * 
     * @return the string to place in the SELECT statement after the ORDER BY clause.
     */
    public String getAfterOrderByClause()
    {
        return sAfterOrderByClause;
    }
    
    /**
     * Sets the string to place in the SELECT statement after the ORDER BY clause.
     * 
     * @param pAfterOrderByClause the string to place in the SELECT statement after the ORDER BY clause.
     */
    public void setAfterOrderByClause(String pAfterOrderByClause)
    {
        sAfterOrderByClause = pAfterOrderByClause;
    }
	
	/**
	 * Returns the list of write back columns to use in the INSERT, UPDATE statements.
	 *
	 * @return the writeback columns.
	 */
	public String[] getWritebackColumns()
	{
		return saWritebackColumns;
	}

	/**
	 * Sets the list of write back columns to use in the INSERT, UPDATE statements.
	 *
	 * @param pWritebackColumns the writeback columns to set.
	 */
	public void setWritebackColumns(String[] pWritebackColumns)
	{
		this.saWritebackColumns = pWritebackColumns;
	}

	/**
	 * Returns the WritebackTable, which is used for the INSERT, UPDATE, DELETE calls.
	 *
	 * @return the writeback table.
	 */
	public String getWritebackTable()
	{
		return sWritebackTable;
	}

	/**
	 * Sets the WritebackTable, which is used for the INSERT, UPDATE, DELETE calls.
	 *
	 * @param pWritebackTable the writeback table to set.
	 */
	public void setWritebackTable(String pWritebackTable)
	{
		sWritebackTable = pWritebackTable;
	}	
	
	/**
	 * Returns the default sort. It is used, if no other sort is defined.
	 *
	 * @return the default sort.
	 */
	public SortDefinition getDefaultSort()
	{
		return sdDefaultSort;
	}

	/**
	 * Sets the default sort. It is used, if no other sort is defined.
	 *
	 * @param pDefaultSort the default sort.
	 */
	public void setDefaultSort(SortDefinition pDefaultSort)
	{
		sdDefaultSort = pDefaultSort;
	}	
	
	/**
	 * Returns the restrict condition. It is always added with And to any given condition.
	 *
	 * @return the restrict condition.
	 */
	public ICondition getRestrictCondition()
	{
		return cRestrictCondition;
	}

	/**
	 * Sets the restrict condition. It is always added with And to any given condition.
	 *
	 * @param pRestrictCondition the restrict condition.
	 */
	public void setRestrictCondition(ICondition pRestrictCondition)
	{
		cRestrictCondition = pRestrictCondition;
	}	
	
	/**
	 * Sets if the automatic link reference detection is en- or disabled. The automatic link reference is defined
	 * through a foreign key reference in the database.
	 *
	 * @param pAutoLinkReference true if the automatic LinkReference mode is on, <code>false</code> to disable 
	 *                           auto link reference mode.
	 */
	public void setAutoLinkReference(boolean pAutoLinkReference)
	{
		bAutoLinkReference = Boolean.valueOf(pAutoLinkReference);
	}	
	
	/**
	 * Returns if the automatic LinkReference mode is on or off.	
	 *
	 * @return <code>true</code>if the automatic LinkReference mode is on, otherwise <code>false</code>.
	 * @see #setAutoLinkReference(boolean)
	 */
	public boolean isAutoLinkReference()
	{
		if (bAutoLinkReference == null)
		{
			return bDefaultAutoLinkReference;
		}
		
		return bAutoLinkReference.booleanValue();
	}
	
	/**
	 * Sets the default automatic link reference detection mode. The automatic link reference is defined
	 * through a foreign key reference in the database.
	 * 
	 * @param pDefaultAutoLinkReference the default automatic LinkReference mode to use.
	 */
	public static void setDefaultAutoLinkReference(boolean pDefaultAutoLinkReference)
	{
		bDefaultAutoLinkReference = pDefaultAutoLinkReference;
	}

	/**
	 * Returns the default automatic LinkReference mode.
	 * 
	 * @return the default automatic LinkReference mode.
	 * @see #setDefaultAutoLinkReference(boolean)
	 */
	public static boolean isDefaultAutoLinkReference()
	{
		return bDefaultAutoLinkReference;
	}
	
	/**
	 * Sets the default value detection en- or disabled. The default value is a default value
	 * for a column in the database
	 * 
	 * @param pDefaultValue <code>true</code> to enable the default value detection.
	 */
	public void setDefaultValue(boolean pDefaultValue)
	{
		bDefaultValue = Boolean.valueOf(pDefaultValue);
	}
	
	/**
	 * Returns the default value detection mode.
	 * 
	 * @return <code>true</code> if the default value detection is enabled, <code>false</code> otherwise.
	 * @see #setDefaultValue(boolean)
	 */
	public boolean isDefaultValue()
	{
		if (bDefaultValue == null)
		{
			return bDefaultDefaultValue;
		}
		
		return bDefaultValue.booleanValue();
	}

	/**
	 * Sets the default - default value detection en- or disabled. The default value is a default value
	 * for a column in the database.
	 * 
	 * @param pDefaultValue <code>true</code> to enable the default - default value detection.
	 */
	public static void setDefaultDefaultValue(boolean pDefaultValue)
	{
		bDefaultDefaultValue = pDefaultValue;
	}
	
	/**
	 * Returns the default - default value detection mode.
	 * 
	 * @return <code>true</code> if the default - default value detection is enabled, <code>false</code> otherwise.
	 * @see #setDefaultDefaultValue(boolean)
	 */
	public static boolean isDefaultDefaultValue()
	{
		return bDefaultDefaultValue;
	}

	/**
	 * Sets the allowed value detection en- or disabled. An allowed value is defined in the database
	 * through check constraints.
	 * 
	 * @param pAllowedValues <code>true</code> to enable the allowed value detection, <code>false</code> otherwise.
	 */
	public void setAllowedValues(boolean pAllowedValues)
	{
		bAllowedValues = Boolean.valueOf(pAllowedValues);
	}
	
	/**
	 * Returns the allowed value detection mode.
	 * 
	 * @return <code>true</code> if the allowed value detection is enabled, <code>false</code> otherwise.
	 * @see #setAllowedValues(boolean)
	 */
	public boolean isAllowedValues()
	{
		if (bAllowedValues == null)
		{
			return bDefaultAllowedValues;
		}
		
		return bAllowedValues.booleanValue();
	}
	
	/**
	 * Sets the default allowed value detection en- or disabled. An allowed value is defined in the database
	 * through check constraints.
	 * 
	 * @param pAllowedValues <code>true</code> to enable the default allowed value detection, <code>false</code> otherwise.
	 */
	public static void setDefaultAllowedValues(boolean pAllowedValues)
	{
		bDefaultAllowedValues = pAllowedValues;
	}
	
	/**
	 * Returns the default allowed value detection mode.
	 * 
	 * @return <code>true</code> if the default allowed value detection is enabled, <code>false</code> otherwise.
	 * @see #setDefaultAllowedValues(boolean)
	 */
	public static boolean isDefaultAllowedValues()
	{
		return bDefaultAllowedValues;
	}
	
	/**
	 * Sets, if restrict condition should be included when row is refetched.
	 * 
	 * @param pRefetchIncludeRestrictCondition <code>true</code> if restrict condition should be included when row is refetched, <code>false</code> otherwise.
	 */
	public void setRefetchIncludeRestrictCondition(boolean pRefetchIncludeRestrictCondition)
	{
		bRefetchIncludeRestrictCondition = Boolean.valueOf(pRefetchIncludeRestrictCondition);
	}
	
	/**
	 * Returns, if restrict condition should be included when row is refetched.
	 * 
	 * @return <code>true</code> if restrict condition should be included when row is refetched, <code>false</code> otherwise.
	 * @see #setRefetchIncludeRestrictCondition(boolean)
	 */
	public boolean isRefetchIncludeRestrictCondition()
	{
		if (bRefetchIncludeRestrictCondition == null)
		{
			return bDefaultRefetchIncludeRestrictCondition;
		}
		
		return bRefetchIncludeRestrictCondition.booleanValue();
	}
	
    /**
     * If set these primary key columns are used for refetch instead of the real primary key columns.
     * 
     * @param pRefetchPrimaryKeyColumns pRefetchPrimaryKeyColumns
     */
    public void setRefetchPrimaryKeyColumns(String[] pRefetchPrimaryKeyColumns)
    {
        saRefetchPrimaryKeyColumns = pRefetchPrimaryKeyColumns;
    }
    
    /**
     * If set these primary key columns are used for refetch instead of the real primary key columns.
     * 
     * @return the primary key columns that should be used instead of the real primary key columns for refetch.
     * @see #setRefetchPrimaryKeyColumns(String[])
     */
    public String[] getRefetchPrimaryKeyColumns()
    {
        return saRefetchPrimaryKeyColumns;
    }
    
	/**
	 * Sets globally, if restrict condition should be included when row is refetched.
	 * 
	 * @param pRefetchIncludeRestrictCondition <code>true</code> if restrict condition should be included when row is refetched, <code>false</code> otherwise.
	 */
	public static void setDefaultRefetchIncludeRestrictCondition(boolean pRefetchIncludeRestrictCondition)
	{
		bDefaultRefetchIncludeRestrictCondition = pRefetchIncludeRestrictCondition;
	}
	
	/**
	 * Returns globally, if restrict condition should be included when row is refetched.
	 * 
	 * @return <code>true</code> if restrict condition should be included when row is refetched, <code>false</code> otherwise.
	 * @see #setDefaultRefetchIncludeRestrictCondition(boolean)
	 */
	public static boolean isDefaultRefetchIncludeRestrictCondition()
	{
		return bDefaultRefetchIncludeRestrictCondition;
	}
	
	/**
	 * If lazy fetch should be enabled.
	 * 
	 * @return <code>true</code> if lazy fetch should be enabled.
	 */
	public boolean isLazyFetchEnabled()
	{
		return bLazyFetchEnabled;
	}
	
	/**
	 * Sets if lazy fetch should be enabled.
	 * 
	 * @param pLazyFetchEnabled if lazy fetch should be enabled.
	 */
	public void setLazyFetchEnabled(boolean pLazyFetchEnabled)
	{
		bLazyFetchEnabled = pLazyFetchEnabled;
	}

	/**
	 * True, if refetch should lock the row.
	 * 
	 * @return <code>true</code> if refetch should lock the row.
	 */
	public boolean isLockOnRefetch()
	{
		return bLockOnRefetch;
	}
	
	/**
	 * True, if refetch should lock the row.
	 * 
	 * @param pLockOnRefetch if refetch should lock the row.
	 */
	public void setLockOnRefetch(boolean pLockOnRefetch)
	{
		bLockOnRefetch = pLockOnRefetch;
	}
	
	/**
	 * Gets the catalog name of the writeback table. 
	 * 
	 * @return the detected catalog name or <code>null</code> if no writeback table is set.
	 */
	protected String getWritebackTableCatalog()
	{
		return sCatalog;
	}
	
	/**
	 * Gets the schema name of the writeback table. 
	 * 
	 * @return the detected schema name or <code>null</code> if no writeback table is set.
	 */
	protected String getWritebackTableSchema()
	{
		return sSchema;
	}
	/**
	 * Refetch and optional locks the specified DataRow via PK.
	 * 
	 * @param pDataRow the DataRow to refetch.
	 * @param pLock true if the row is locked before. 
	 * @return the refetched DataRow.
	 * @throws DataSourceException if the refetch fails.
	 */
	protected Object[] refetchRow(Object[] pDataRow, boolean pLock) throws DataSourceException 
	{
		if (pDataRow == null)
		{
			return null;
		}
		if (getWritebackTable() == null)
		{
			return null;
		}

		// construct Filter over PK cols...
		ICondition cFilter = Filter.createEqualsFilter(saRefetchPrimaryKeyColumns == null ? mdServerMetaData.getPrimaryKeyColumnNames() : saRefetchPrimaryKeyColumns, 
				pDataRow, mdServerMetaData.getMetaData().getColumnMetaData());
		if (isRefetchIncludeRestrictCondition() && getRestrictCondition() != null)
		{
			cFilter = cFilter.and(getRestrictCondition());
		}

		// lock row in storage
		if (pLock && !dbAccess.isAutoCommit())
		{
			dbAccess.lockRow(getWritebackTable(), mdServerMetaData, cFilter);
			
			//TODO [RH] Optimize return also the values in same step!!! Attention with AutoCommit
		}

		if (subStorageConditions != null)
		{
			cFilter = new And(subStorageConditions, cFilter);
		}

		// refetch data
		List<Object[]> olResult = dbAccess.fetch(mdServerMetaData, sBeforeQueryColumns, saQueryColumns, getFromClauseIntern(),
									             cFilter, sWhereClause, sAfterWhereClause, 
									             null, 0, 2,
									             bLazyFetchEnabled);
		
		if (bLoadNotDatabaseAutoLinkValues && isAutoLinkReference())
        {
            setNotDatabaseAutoLinkValues(olResult);
        }

		if (olResult != null && olResult.size() == 2 && olResult.get(1) == null)
		{
			Object[] result = olResult.get(0);
			
			for (int i = 0; i < result.length; i++)
			{
				// Ensure, that values not coming from the database are available in the result again.
				if (i < pDataRow.length && mdServerMetaData.getServerColumnMetaData(i) == null)
				{
					result[i] = pDataRow[i];
				}
			}
			
			return result;
		}
		return null;
	}

	/**
	 * Creates the filter based on the {@link #cRestrictCondition},
	 * {@link #subStorageConditions} and the given {@link ICondition}.
	 * 
	 * @param pAdditional the additional {@link ICondition}.
	 * @return the filter.
	 */
	protected ICondition createFilter(ICondition pAdditional)
	{
		ICondition result = null;
		
		if (cRestrictCondition != null)
		{
		    result = new And();
		    result.and(cRestrictCondition);
		}
		
		if (subStorageConditions != null)
		{
            if (result == null)
            {
                result = new And();
            }

            result.and(subStorageConditions);
		}
		
		if (pAdditional != null)
		{
			checkCondition(pAdditional);
			
		    if (result == null)
		    {
		    	//very important to wrap into And. Without And, the "original" condition could be changed
		    	result = new And(pAdditional);
		    }
		    else
		    {
		    	result.and(new And(pAdditional));
		    }
		}
		
		return result;
	}
	
	/**
	 * Adds missing column meta data for all not DB substorages.
	 * 
	 * @param pMetaData the metadata
	 * @throws DataSourceException if add fails
	 */
	private void addNotDatabaseLinkedColumns(MetaData pMetaData) throws DataSourceException
    {
	    for (StorageReferenceDefinition srdLinkRef : auManualLinkReferences)
        {
	        ISubStorage iSubStorage = getSubStorage(srdLinkRef.getReferencedStorage());
	        if (iSubStorage != null && !(iSubStorage instanceof DBStorage))
	        {
	            bLoadNotDatabaseAutoLinkValues = true;
	            
	            MetaData mdSubStorage = iSubStorage.getMetaData();
	            
	            String[] saColumnNames = srdLinkRef.getColumnNames();
	            for (String sColumnName : saColumnNames)
	            {
	                if (pMetaData.getColumnMetaDataIndex(sColumnName) < 0)
	                {
	                    ColumnMetaData cmd = new ColumnMetaData(sColumnName);
	                    
	                    try
	                    {
	                        ColumnMetaData cmdSubStorage = mdSubStorage.getColumnMetaData(srdLinkRef.getReferencedColumnName(sColumnName));
	                        cmd.setTypeIdentifier(cmdSubStorage.getTypeIdentifier());
	                        cmd.setPrecision(cmdSubStorage.getPrecision());
	                        cmd.setScale(cmdSubStorage.getScale());
	                    }
	                    catch (ModelException e)
	                    {
	                        debug(e);
	                    }
	                    
	                    pMetaData.addColumnMetaData(cmd);
	                }
	            }
	        }
        }
    }
	
	/**
     * Loads the combo box values from not database substorages defined in manual link references.
     * 
     * @param pWritableColumns the writable columns
     * @return the combobox values {@link HashMap}
     * @throws DataSourceException if load fails
     */
    private HashMap<String, HashMap<Object, IBean>> loadNotDatabaseComboBoxValues(String[] pWritableColumns) throws DataSourceException
    {
        long lStartMillis = System.currentTimeMillis();
        
        HashMap<String, HashMap<Object, IBean>> hmComboBoxValues = new HashMap<String, HashMap<Object, IBean>>();
        
        for (StorageReferenceDefinition srdLinkRef : auAutomaticLinkReferences)
        {
            ISubStorage iSubStorage = getSubStorage(srdLinkRef.getReferencedStorage());
            
            if (iSubStorage != null && !(iSubStorage instanceof DBStorage))
            {
                AbstractStorage as = (AbstractStorage)iSubStorage;
                
                HashMap<Object, IBean> hmComboBoxValuesInner = new HashMap<Object, IBean>();
                
                String[] saCommonColumns = ArrayUtil.intersect(srdLinkRef.getColumnNames(), pWritableColumns);
                
                if (saCommonColumns.length > 0)
                {
                    List<IBean> liComboBoxValues;
                    
                    try
                    {
                        liComboBoxValues = as.fetchBean(null, null, 0, -1);
                        
                        if (saCommonColumns.length == 1)
                        {
                            for (IBean bnComboBoxValue : liComboBoxValues)
                            {
                                hmComboBoxValuesInner.put(bnComboBoxValue.get(srdLinkRef.getReferencedColumnName(saCommonColumns[0])), bnComboBoxValue);
                            }
                        }
                        else
                        {
                            for (IBean bnComboBoxValue : liComboBoxValues)
                            {
                                ArrayUtil<Object> auCommonValues = getComposedKey(srdLinkRef, saCommonColumns, bnComboBoxValue);
                                
                                hmComboBoxValuesInner.put(auCommonValues, bnComboBoxValue);
                            }
                        }
                        
                        hmComboBoxValues.put(srdLinkRef.getReferencedStorage(), hmComboBoxValuesInner);
                    }
                    catch (DataSourceException e)
                    {
                        error("Loading values from substorage: " + srdLinkRef.getReferencedStorage() + " failed!");
                    }
                }
            }
        }
        
        long lEndMillis = System.currentTimeMillis();
        
        debug("Loading combobox values duration: " + (lEndMillis - lStartMillis) + " ms");
        
        return hmComboBoxValues;
    }
	
	/**
     * Creates the composed key.
     * 
     * @param pLinkRef the {@link StorageReferenceDefinition}
     * @param pColumns the columns
     * @param pBean the bean
     * @return the composed key
     */
    private ArrayUtil<Object> getComposedKey(StorageReferenceDefinition pLinkRef, String[] pColumns, IBean pBean)
    {
        ArrayUtil<Object> auComposedKey = new ArrayUtil<Object>();
        
        for (String sColumn : pColumns)
        {
            auComposedKey.add(pBean.get(pLinkRef.getReferencedColumnName(sColumn)));
        }
        
        return auComposedKey;
    }
    
    /**
     * Creates the composed key.
     * 
     * @param pLinkRef the {@link StorageReferenceDefinition}
     * @param pColumns the columns
     * @param pRowData the row data
     * @return the composed key {@link ArrayUtil}
     * @throws DataSourceException if an error occurs
     */
    private ArrayUtil<Object> getComposedKey(StorageReferenceDefinition pLinkRef, String[] pColumns, Object[] pRowData) throws DataSourceException
    {
        ArrayUtil<Object> auComposedKey = new ArrayUtil<Object>();
        
        for (String sColumn : pColumns)
        {
            int iColumnIndex = getMetaData().getColumnMetaDataIndex(sColumn);
            
            auComposedKey.add(pRowData[iColumnIndex]);
        }
        
        return auComposedKey;
    }
    
    /**
     * Sets the values over available not database link references into given row.
     * 
     * @param pRowData the row data
     * @param pComboBoxValues the combobox values
     * @param pWritableColumns the writable columns
     * @throws DataSourceException if an error occurs
     */
    private void setNotDatabaseAutoLinkValues(Object[] pRowData, HashMap<String, HashMap<Object, IBean>> pComboBoxValues, String[] pWritableColumns) throws DataSourceException
    {
        if (pComboBoxValues == null || pRowData == null)
        {
            return;
        }
        
        for (StorageReferenceDefinition srdLinkRef : auAutomaticLinkReferences)
        {
            String sReferencedStorage = srdLinkRef.getReferencedStorage();
            ISubStorage iSubStorage = getSubStorage(sReferencedStorage);
            
            if (iSubStorage != null && !(iSubStorage instanceof DBStorage))
            {
                HashMap<Object, IBean> hmComboBoxValuesSingle = pComboBoxValues.get(sReferencedStorage);
                
                if (hmComboBoxValuesSingle != null)
                {
                    String[] saCommonColumns = ArrayUtil.intersect(srdLinkRef.getColumnNames(), pWritableColumns);
                    
                    String[] saReferencedColumnNames = srdLinkRef.getReferencedColumnNames();
                    String[] saColumnNames = srdLinkRef.getColumnNames();
                    
                    if (saCommonColumns.length == 1)
                    {
                        String sFKColumn = saCommonColumns[0];
                        
                        int iFKColumnIndex = getMetaData().getColumnMetaDataIndex(sFKColumn);
                        
                        Object oFKValue = pRowData[iFKColumnIndex];
                        
                        if (oFKValue != null)
                        {
                            IBean iBean = hmComboBoxValuesSingle.get(pRowData[iFKColumnIndex]);
                            
                            if (iBean == null)
                            {
                                AbstractStorage as = (AbstractStorage)iSubStorage;
                                
                                List<IBean> liComboBoxValues = as.fetchBean(new Equals(srdLinkRef.getReferencedColumnName(sFKColumn), oFKValue), null, 0, -1);
                                
                                if (liComboBoxValues.size() > 0)
                                {
                                    iBean = liComboBoxValues.get(0);
                                }
                                
                                if (iBean == null)
                                {
                                    iBean = DUMMY_AUTOMATIC_LINK_ENTRY;
                                }
                                
                                hmComboBoxValuesSingle.put(oFKValue, iBean);
                            }
                            
                            if (iBean != null && !iBean.equals(DUMMY_AUTOMATIC_LINK_ENTRY))
                            {
                                for (int i = 0; i < saColumnNames.length; i++)
                                {
                                    if (!saColumnNames[i].equals(sFKColumn))
                                    {
                                        int iColumnIndex = getMetaData().getColumnMetaDataIndex(saColumnNames[i]);
                                        pRowData[iColumnIndex] = iBean.get(saReferencedColumnNames[i]);
                                    }
                                }
                            }
                        }
                    }
                    else
                    {
                        ArrayUtil<Object> auComposedKey = getComposedKey(srdLinkRef, saCommonColumns, pRowData);
                        
                        IBean iBean = hmComboBoxValuesSingle.get(auComposedKey);
                        
                        if (iBean == null)
                        {
                            AbstractStorage as = (AbstractStorage)iSubStorage;
                            
                            ICondition icond = null;
                            
                            for (int i = 0; i < saCommonColumns.length; i++)
                            {
                                String sCommonColumn = saCommonColumns[i];
                                
                                int iFKColumnIndex = getMetaData().getColumnMetaDataIndex(sCommonColumn);
                                
                                Equals equals = new Equals(srdLinkRef.getReferencedColumnName(sCommonColumn), pRowData[iFKColumnIndex]);
                                
                                if (i == 0)
                                {
                                    icond = equals;
                                }
                                else
                                {
                                    icond.and(equals);
                                }
                            }
                            
                            List<IBean> liComboBoxValues = as.fetchBean(icond, null, 0, -1);
                            
                            if (liComboBoxValues.size() > 0)
                            {
                                iBean = liComboBoxValues.get(0);
                            }
                            
                            if (iBean == null)
                            {
                                iBean = DUMMY_AUTOMATIC_LINK_ENTRY;
                            }
                            
                            hmComboBoxValuesSingle.put(auComposedKey, iBean);
                        }
                        
                        if (iBean != null && !iBean.equals(DUMMY_AUTOMATIC_LINK_ENTRY))
                        {
                            for (int i = 0; i < saColumnNames.length; i++)
                            {
                                if (!ArrayUtil.contains(saCommonColumns, saColumnNames[i]))
                                {
                                    int iColumnIndex = getMetaData().getColumnMetaDataIndex(saColumnNames[i]);
                                    pRowData[iColumnIndex] = iBean.get(saReferencedColumnNames[i]);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Sets the values over available not database link references.
     * 
     * @param pFetchData the fetch data
     * @throws DataSourceException if it fails
     */
    private void setNotDatabaseAutoLinkValues(List<Object[]> pFetchData) throws DataSourceException
    {
        String[] saWritableColumns = mdServerMetaData.getWritableColumnNames();
        HashMap<String, HashMap<Object, IBean>> hmComboBoxValues = loadNotDatabaseComboBoxValues(saWritableColumns);
        
        
        for (Object[] oaFetchRow : pFetchData)
        {
            setNotDatabaseAutoLinkValues(oaFetchRow, hmComboBoxValues, saWritableColumns);
        }
    }
	
} 	// DBStorage
