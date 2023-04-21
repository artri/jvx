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
 * 04.01.2011 - [RH] - creation
 * 04.01.2011 - [RH] - #233 - Server Side Trigger implementation
 *                          - logger, createCSV function moved to AbstractStorage.
 * 05.01.2011 - [RH] - #233 - XXXbean() methods moved and changed to support IBean/POJOs as parameters and return type.
 * 07.01.2011 - [JR] - checked if event was modified (object saving)
 * 10.03.2011 - [JR] - changed formatInitCap call (always remove space characters and format)
 * 11.04.2011 - [JR] - #332: createBeanType implemented
 * 08.06.2011 - [JR] - used convertToMemberName instead of formatMemberName and formatInitCap
 * 16.12.2011 - [JR] - #498: set/get specific name 
 * 08.12.2012 - [JR] - #611: implemented xxxAsBean methods and made a default implementation
 * 19.12.2012 - [JR] - made getAndInitBeanType protected
 * 25.02.2014 - [JR] - #959: throw Exception if property exists and putting value fails
 * 28.02.2014 - [JR] - #961: fixed NPE in setPropertyNameForColumn and introduced getPropertyNameForColumn
 * 27.11.2014 - [JR] - #1192: update(T, T) added
 * 08.02.2019 - [JR] - #1985: put/getObject implmented
 */
package com.sibvisions.rad.persist;

import java.io.OutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.rad.io.IFileHandle;
import javax.rad.io.RemoteFileHandle;
import javax.rad.model.SortDefinition;
import javax.rad.model.condition.CompareCondition;
import javax.rad.model.condition.ICondition;
import javax.rad.model.condition.Not;
import javax.rad.model.condition.OperatorCondition;
import javax.rad.persist.DataSourceException;
import javax.rad.persist.IStorage;
import javax.rad.persist.MetaData;
import javax.rad.remote.IConnectionConstants;
import javax.rad.server.ISession;
import javax.rad.server.SessionContext;
import javax.rad.type.bean.Bean;
import javax.rad.type.bean.BeanType;
import javax.rad.type.bean.IBean;
import javax.rad.type.bean.IBeanType;
import javax.rad.util.EventHandler;
import javax.rad.util.INamedObject;

import com.sibvisions.rad.model.DataBookCSVExporter;
import com.sibvisions.rad.persist.bean.BeanConverter;
import com.sibvisions.rad.persist.bean.IAllFetched;
import com.sibvisions.rad.persist.event.StorageEvent;
import com.sibvisions.rad.persist.event.StorageEvent.ChangedType;
import com.sibvisions.rad.persist.event.StorageHandler;
import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.ICloseable;
import com.sibvisions.util.log.ILogger;
import com.sibvisions.util.log.ILogger.LogLevel;
import com.sibvisions.util.log.LoggerFactory;
import com.sibvisions.util.type.LocaleUtil;
import com.sibvisions.util.type.StringUtil;

/**
 * The <code>AbstractStorage</code> implements the server side triggers mechanism for different types of Storages.
 * It is the abstract base class for storages which needs a trigger mechanism.
 * 
 * @author Roland Hörmann
 */
public abstract class AbstractStorage implements IStorage, 
                                                 INamedObject,
                                                 ICloseable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** undefined value to get a guaranteed difference. */
	private static final StringBuilder UNDEFINED_VALUE = new StringBuilder();

	/** the logger. */
	private static ILogger logger = null;
	
	/** the <code>EventHandler</code> for calculate row event. */
	private StorageHandler eventCalculateRow;
    /** the <code>EventHandler</code> for instead-of insert event. */
    private StorageHandler eventInsteadOfInsert;
	/** the <code>EventHandler</code> for before insert event. */
	private StorageHandler eventBeforeInsert;
	/** the <code>EventHandler</code> for after insert event. */
	private StorageHandler eventAfterInsert;
    /** the <code>EventHandler</code> for instead-of update event. */
    private StorageHandler eventInsteadOfUpdate;
	/** the <code>EventHandler</code> for before update event. */
	private StorageHandler eventBeforeUpdate;
	/** the <code>EventHandler</code> for after update event. */
	private StorageHandler eventAfterUpdate;
    /** the <code>EventHandler</code> for instead-of delete event. */
    private StorageHandler eventInsteadOfDelete;
	/** the <code>EventHandler</code> for before delete event. */
	private StorageHandler eventBeforeDelete;
	/** the <code>EventHandler</code> for after delete event. */
	private StorageHandler eventAfterDelete;
	
	/** determines if after insert and update the row will be refetched. */
	private boolean bRefetch = true;

	/** the {@link BeanConverter} for this storage. */
	private BeanConverter beanConverter = new BeanConverter();
	
	/** additional objects. */
	private HashMap<String, Object> hmpObjects;
	
	/** the storage name. */
	private String sName;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public final List<Object[]> fetch(ICondition pFilter, SortDefinition pSort, int pFromRow, int pMinimumRowCount) throws DataSourceException
	{
		return fetch(false, pFilter, pSort, pFromRow, pMinimumRowCount);
	}

	/**
	 * {@inheritDoc}
	 */
	public final Object[] refetchRow(Object[] pDataRow) throws DataSourceException
	{
		return refetchRow(false, pDataRow);
	}

	/**
	 * {@inheritDoc}
	 */
	public final Object[] insert(Object[] pDataRow) throws DataSourceException
	{
		Object[] oRow = pDataRow;
		
		dispatchEvent(eventBeforeInsert, ChangedType.BEFORE_INSERT, null, oRow);
		
		if (EventHandler.isDispatchable(eventInsteadOfInsert))
		{
		    if (isRefetch())
		    {
		        oRow = oRow.clone();
		    }
		    
		    dispatchEvent(eventInsteadOfInsert, ChangedType.INSTEADOF_INSERT, null, oRow);
		}
		else
		{
    		if (isRefetch())
    		{
    			// Clone the array to make sure that it is not modified.
    			// Otherwise the PK columns would be set into the old array,
    			// but we only want that if refetch is disabled.
    			oRow = executeInsert(oRow.clone());
    		}
    		else
    		{
    			executeInsert(oRow);
    		}
		}
		
		dispatchEvent(eventAfterInsert, ChangedType.AFTER_INSERT, null, oRow);
		dispatchEvent(eventCalculateRow, ChangedType.CALCULATE_ROW, null, oRow);
		
		return trimArray(oRow, pDataRow);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public final Object[] update(Object[] pOldDataRow, Object[] pNewDataRow) throws DataSourceException
	{
		Object[] oOldRow = pOldDataRow;
		Object[] oNewRow = pNewDataRow;
		
		dispatchEvent(eventBeforeUpdate, ChangedType.BEFORE_UPDATE, oOldRow, oNewRow);
		
		if (EventHandler.isDispatchable(eventInsteadOfUpdate))
		{
		    if (isRefetch())
		    {
		        oNewRow = oNewRow.clone();
		    }
		    
		    dispatchEvent(eventInsteadOfUpdate, ChangedType.INSTEADOF_UPDATE, oOldRow, oNewRow);
		}
		else
		{
    		if (isRefetch())
    		{
    			oNewRow = executeUpdate(oOldRow, oNewRow.clone());
    		}
    		else
    		{
    			executeUpdate(oOldRow, oNewRow);
    		}
		}
		
		dispatchEvent(eventAfterUpdate, ChangedType.AFTER_UPDATE, oOldRow, oNewRow);
		dispatchEvent(eventCalculateRow, ChangedType.CALCULATE_ROW, oOldRow, oNewRow);
		
		return trimArray(oNewRow, pNewDataRow);
	}

	/**
	 * {@inheritDoc}
	 */
	public final void delete(Object[] pDeleteDataRow) throws DataSourceException
	{
		dispatchEvent(eventBeforeDelete, ChangedType.BEFORE_DELETE, pDeleteDataRow, null);
		
		if (EventHandler.isDispatchable(eventInsteadOfDelete))
		{
		    dispatchEvent(eventInsteadOfDelete, ChangedType.INSTEADOF_DELETE, pDeleteDataRow, null);
		}
		else
		{
		    executeDelete(pDeleteDataRow);
		}
		
		dispatchEvent(eventAfterDelete, ChangedType.AFTER_DELETE, pDeleteDataRow, null);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Write the current storage with the specified filter and sort into the given output stream as CSV Format.
	 * 
	 * @param pStream the output stream to use for the CSV stream
	 * @param pColumnNames the column names to include in the export
	 * @param pLabels the labels to show as header in the export
	 * @param pFilter the condition
	 * @param pSort the sort definition
	 * @param pSeparator the column separator
	 * @throws Exception if the CSV output couldn't written to stream
	 */
	public abstract void writeCSV(OutputStream pStream, 
			                      String[] pColumnNames, 
			                      String[] pLabels, 
			                      ICondition pFilter, 
			                      SortDefinition pSort, 
			                      String pSeparator) throws Exception;
		
	/**
	 * This method has to be implemented in the Storage implementation and should have the functionality of the refetchRow method.
	 * 
	 * @param pDataRow the specified row as <code>Object[]</code>.
	 * @return the refetched row as <code>Object[]</code>.
	 * @throws DataSourceException if an <code>Exception</code> occur during interacting with the storage.
	 * @see javax.rad.persist.IStorage#refetchRow(Object[])
	 */
	protected abstract Object[] executeRefetchRow(Object[] pDataRow) throws DataSourceException;
	
	/**
	 * This method has to be implemented in the Storage implementation and should have the functionality of the fetch method.
	 * 
	 * @param pFilter the <code>ICondition</code> to use.
	 * @param pSort	the <code>SortDefinition</code> to use.
	 * @param pFromRow the from row index to request from storage.
	 * @param pMinimumRowCount the minimum row count to request, beginning from the pFromRow.
	 * @return the requested rows as <code>List[Object[]]</code>.
	 * @throws DataSourceException if an <code>Exception</code> occur during interacting with the storage.
	 * @see javax.rad.persist.IStorage#fetch(ICondition, SortDefinition, int, int)
	 */
	protected abstract List<Object[]> executeFetch(ICondition pFilter, SortDefinition pSort, int pFromRow, int pMinimumRowCount) throws DataSourceException;
	
	/**
	 * This method has to be implemented in the Storage implementation and should have the functionality of the insert method.
	 * 
	 * @param pDataRow the new row as <code>Object[]</code> to insert.
	 * @return the newly inserted row from this IStorage. <code>null</code> if no insert operation was performed.
	 * @throws DataSourceException if an <code>Exception</code> occur during insert the row to the storage
	 * @see javax.rad.persist.IStorage#insert(Object[])
	 */
	protected abstract Object[] executeInsert(Object[] pDataRow) throws DataSourceException;
	
	/**
	 * This method has to be implemented in the Storage implementation and should have the functionality of the update method.
	 * 
	 * @param pOldDataRow the old row as <code>Object[]</code>
	 * @param pNewDataRow the new row as <code>Object[]</code> to update
	 * @return the updated row as <code>Object[]</code>. <code>null</code> if no update operation was performed.
	 * @throws DataSourceException if an <code>Exception</code> occur during updating the row.
	 * @see javax.rad.persist.IStorage#update(Object[], Object[])
	 */
	protected abstract Object[] executeUpdate(Object[] pOldDataRow, Object[] pNewDataRow) throws DataSourceException;
	
	/**
	 * This method has to be implemented in the Storage implementation and should have the functionality of the delete method.
	 * 
	 * @param pDeleteDataRow the row as <code>Object[]</code> to delete.
	 * @throws DataSourceException if an <code>Exception</code> occur during deleting the row or
	 *             				   if the storage isn't opened or the PrimaryKey is wrong and more/less 
	 *                             then one row is deleted.
	 * @see javax.rad.persist.IStorage#delete(Object[])
	 */
	protected abstract void executeDelete(Object[] pDeleteDataRow) throws DataSourceException;
	
	/**
	 * Opens the storage.
	 * 
	 * @throws DataSourceException if the storage can not be opened
	 */
	public abstract void open() throws DataSourceException;
	
	/**
	 * Returns whether the storage is open and ready to use.
	 * 
	 * @return <code>true</code> if the storage is open, <code>false</code> otherwise
	 */
	public abstract boolean isOpen();
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the {@link BeanConverter} for this storage and also initializes
	 * it with the metadata from this storage.
	 * 
	 * @return the {@link BeanConverter}.
	 * @throws DataSourceException if initializing the {@link BeanConverter}
	 *                             with the metadata failed.
	 */
	public BeanConverter getBeanConverter() throws DataSourceException
	{
		if (isOpen())
		{
			if (!beanConverter.isInitialized())
			{
				beanConverter.setBeanType(createBeanType(getMetaData().getColumnNames()));
			}
		}
		
		return beanConverter;
	}
	
	/**
	 * Sets the {@link BeanConverter} and also initializes it with the metadata from this
	 * storage.
	 * 
	 * @param pConverter the {@link BeanConverter}
	 * @throws DataSourceException if initializing the {@link BeanConverter}
	 *                             with the metadata failed.
	 */
	public void setBeanConverter(BeanConverter pConverter) throws DataSourceException
	{
	    if (pConverter == null)
	    {
	        beanConverter = new BeanConverter();
	    }
	    else
	    {
	        beanConverter = pConverter;
	    }
	}
	
	/**
	 * Fetches data. This methods makes a difference between object[] fetch and bean fetch.
	 * 
	 * @param pAsBean <code>true</code> means called via bean access and <code>false</code> means called via client API
	 * @param pFilter the filter
	 * @param pSort the sort condition
	 * @param pFromRow fetch from row
	 * @param pMinimumRowCount fetch minimum rows
	 * @return the fetched records
	 * @throws DataSourceException if fetch fails
	 */
	private List<Object[]> fetch(boolean pAsBean, ICondition pFilter, SortDefinition pSort, int pFromRow, int pMinimumRowCount) throws DataSourceException
	{
		List<Object[]> liRows;
		
		if (pAsBean)
		{
			liRows = executeFetchAsBean(pFilter, pSort, pFromRow, pMinimumRowCount);
		}
		else
		{
			liRows = executeFetch(pFilter, pSort, pFromRow, pMinimumRowCount);			
		}
		
		if (eventCalculateRow != null)
		{
			for (int i = 0; i < liRows.size() && liRows.get(i) != null; i++)
			{
				dispatchEvent(eventCalculateRow, ChangedType.CALCULATE_ROW, null, liRows.get(i));
			}
		}

		return liRows;
	}
	
	/**
	 * Refetches data. This methods makes a difference between object[] refetch and bean refetch.
	 * 
	 * @param pAsBean <code>true</code> means called via bean access and <code>false</code> means called via client API 
	 * @param pDataRow the old data
	 * @return the new data
	 * @throws DataSourceException if refetch fails
	 */
	private Object[] refetchRow(boolean pAsBean, Object[] pDataRow) throws DataSourceException
	{
		Object[] oValues;
		
		if (pAsBean)
		{
			oValues = executeRefetchRowAsBean(pDataRow);
		}
		else
		{
			oValues = executeRefetchRow(pDataRow);
		}
		
		dispatchEvent(eventCalculateRow, ChangedType.CALCULATE_ROW, null,  oValues);
		
		return trimArray(oValues, pDataRow);
	}
	
	/**
	 * Sets the storage name.
	 * 
	 * @param pName the name
	 */
	public void setName(String pName)
	{
		sName = pName;
	}
	
	/**
	 * Gets the storage name.
	 * 
	 * @return the name
	 */
	public String getName()
	{
		return sName;
	}
	
	/**
	 * Returns if this Storage refetches after insert and update.
	 *
	 * @return if this Storage refetches after insert and update.
	 */
	public boolean isRefetch()
	{
		return bRefetch;
	}

	/**
	 * Sets if this Storage refetches after insert and update.
	 *
	 * @param pRefetch <code>true</code> if this Storage refetches after insert and update.
	 */
	public void setRefetch(boolean pRefetch)
	{
		bRefetch = pRefetch;
	}
	
    /**
     * Gets whether the given log level is enabled.
     * 
     * @param pLevel the level to check
     * @return <code>true</code> if the log level is enabled, <code>false</code> otherwise
     */
    protected boolean isLogEnabled(LogLevel pLevel)
    {
        if (logger == null)
        {
            return LoggerFactory.getInstance(AbstractStorage.class).isEnabled(pLevel);
        }
        
        return logger.isEnabled(pLevel);
    }
	
	/**
	 * Logs debug information.
	 * 
	 * @param pInfo the debug information
	 */
	protected void debug(Object... pInfo)
	{
		if (logger == null)
		{
			logger = LoggerFactory.getInstance(AbstractStorage.class);
		}
		
		logger.debug(pInfo);
	}

	/**
	 * Logs information.
	 * 
	 * @param pInfo the information
	 */
	protected void info(Object... pInfo)
	{
		if (logger == null)
		{
			logger = LoggerFactory.getInstance(AbstractStorage.class);
		}
		
		logger.info(pInfo);
	}
	
	/**
	 * Logs error information.
	 * 
	 * @param pInfo the error information
	 */
	protected void error(Object... pInfo)
	{
		if (logger == null)
		{
			logger = LoggerFactory.getInstance(AbstractStorage.class);
		}

		logger.error(pInfo);
	}	

	/**
	 * Write the current DBStorage with the specified filter and sort to the 
	 * export.csv file in CSV format and returns the file handle. The filename
	 * will be built depending of the object name from the {@link SessionContext}.
	 * 
	 * @param pColumnNames the column names to include in the export
	 * @param pLabels the labels to show as header in the export
	 * @param pFilter the filter to use on the DBStorage
	 * @param pSort the sort to use
	 * @return the file handle for the new generated CSV file. 
	 * @throws Exception if the CSV output couldn't written to stream. 
	 */
	public IFileHandle createCSV(String[] pColumnNames, String[] pLabels, ICondition pFilter, SortDefinition pSort) throws Exception
	{
	    return createCSV(null, pColumnNames, pLabels, pFilter, pSort, null, null);
	}
	
	/**
     * Write the current DBStorage with the specified filter and sort to the 
     * export.csv file in CSV format and returns the file handle. The filename
     * will be built depending of the object name from the {@link SessionContext}.
     * 
     * @param pColumnNames the column names to include in the export
     * @param pLabels the labels to show as header in the export
     * @param pFilter the filter to use on the DBStorage
     * @param pSort the sort to use
     * @param pLanguage the language to use
     * @return the file handle for the new generated CSV file. 
     * @throws Exception if the CSV output couldn't written to stream. 
     */
    public IFileHandle createCSV(String[] pColumnNames, String[] pLabels, ICondition pFilter, SortDefinition pSort, String pLanguage) throws Exception
    {
        return createCSV(null, pColumnNames, pLabels, pFilter, pSort, pLanguage, null);
    }
    
	/**
	 * Write the current DBStorage with the specified filter and sort to the 
	 * export.csv file in CSV format and returns the file handle.
	 * 
	 * @param pFileName the filename to use
	 * @param pColumnNames the column names to include in the export
	 * @param pLabels the labels to show as header in the export
	 * @param pFilter the filter to use on the DBStorage
	 * @param pSort the sort to use
	 * @return the file handle for the new generated CSV file. 
	 * @throws Exception if the CSV output couldn't written to stream. 
	 */
	public IFileHandle createCSV(String pFileName, String[] pColumnNames, String[] pLabels, ICondition pFilter, SortDefinition pSort) throws Exception
	{
		return createCSV(pFileName, pColumnNames, pLabels, pFilter, pSort, null, null);
	}
	
	/**
     * Write the current DBStorage with the specified filter and sort to the 
     * export.csv file in CSV format and returns the file handle. The filename
     * will be built depending of the object name from the {@link SessionContext}.
     * 
     * @param pColumnNames the column names to include in the export
     * @param pLabels the labels to show as header in the export
     * @param pFilter the filter to use on the DBStorage
     * @param pSort the sort to use
     * @param pLanguage the language to use
     * @param pSeparator the separator to use
     * @return the file handle for the new generated CSV file. 
     * @throws Exception if the CSV output couldn't written to stream. 
     */
    public IFileHandle createCSV(String[] pColumnNames, String[] pLabels, ICondition pFilter, SortDefinition pSort, String pLanguage, String pSeparator) throws Exception
    {
        return createCSV(null, pColumnNames, pLabels, pFilter, pSort, pLanguage, pSeparator);
    }
    
    /**
     * Write the current DBStorage with the specified filter and sort to the 
     * export.csv file in CSV format and returns the file handle. The filename
     * will be built depending of the object name from the {@link SessionContext}.
     * 
     * @param pFileName the filename of the csv
     * @param pColumnNames the column names to include in the export
     * @param pLabels the labels to show as header in the export
     * @param pFilter the filter to use on the DBStorage
     * @param pSort the sort to use
     * @param pLanguage the language to use
     * @param pSeparator the separator to use
     * @return the file handle for the new generated CSV file. 
     * @throws Exception if the CSV output couldn't written to stream. 
     */
    public IFileHandle createCSV(String pFileName, String[] pColumnNames, String[] pLabels, 
                                 ICondition pFilter, SortDefinition pSort, String pLanguage, String pSeparator) throws Exception
    {
        String sFileName = pFileName;
        
        if (StringUtil.isEmpty(sFileName))
        {
            SessionContext context = SessionContext.getCurrentInstance();
            
            if (context != null)
            {
                sFileName = context.getObjectName();
            }            
        }
        
        Locale threadDefault = LocaleUtil.getThreadDefault();
        try
        {      
            LocaleUtil.setThreadDefault(LocaleUtil.forLanguageTag(pLanguage));

            RemoteFileHandle fileHandle = new RemoteFileHandle(DataBookCSVExporter.formatCSVFileName(sFileName));

            String sSeparator = pSeparator;
            
            if (StringUtil.isEmpty(sSeparator))
            {
                ISession sess = SessionContext.getCurrentSession();
    
                if (sess != null)
                {
                    String sLang = (String)sess.getProperty(IConnectionConstants.CLIENT_LOCALE_LANGUAGE);
                    String sCountry = (String)sess.getProperty(IConnectionConstants.CLIENT_LOCALE_COUNTRY);
                    String sVariant = (String)sess.getProperty(IConnectionConstants.CLIENT_LOCALE_VARIANT);
    
                    if (sLang != null)
                    {
                        sSeparator = LocaleUtil.getListSeparator(new Locale(sLang, sCountry, sVariant));                
                    }
                    else
                    {
                        sSeparator = LocaleUtil.getListSeparator();
                    }
                }
                else
                {
                    sSeparator = LocaleUtil.getListSeparator();
                }
            }
            
            OutputStream os = fileHandle.getOutputStream();
            writeCSV(os, pColumnNames, pLabels, pFilter, pSort, sSeparator);
            os.close();
            
            return fileHandle;
        }
        finally
        {
            LocaleUtil.setThreadDefault(threadDefault);
        }
    }

    /**
     * Gets the EventHandler for calculate row event.
     * This is called after fetch, refetchRow, insert and update method calls.
     * 
     * @return the EventHandler for after deleted event.
     */
	public StorageHandler eventCalculateRow()
	{
		if (eventCalculateRow == null)
		{
			eventCalculateRow = new StorageHandler();
		}
		return eventCalculateRow;
	}
		
    /**
     * Gets the EventHandler for instead-of insert event.
     * 
     * @return the EventHandler for instead-of insert event.
     */
    public StorageHandler eventInsteadOfInsert()
    {
        if (eventInsteadOfInsert == null)
        {
            eventInsteadOfInsert = new StorageHandler();
        }
        return eventInsteadOfInsert;
    }
	
	/**
     * Gets the EventHandler for before insert event.
     * 
     * @return the EventHandler for before insert event.
     */
	public StorageHandler eventBeforeInsert()
	{
		if (eventBeforeInsert == null)
		{
			eventBeforeInsert = new StorageHandler();
		}
		return eventBeforeInsert;
	}
	
    /**
     * Gets the EventHandler for after insert event.
     * 
     * @return the EventHandler for after insert event.
     */
	public StorageHandler eventAfterInsert()
	{
		if (eventAfterInsert == null)
		{
			eventAfterInsert = new StorageHandler();
		}
		return eventAfterInsert;
	}
		
    /**
     * Gets the EventHandler for instead-of update event.
     * 
     * @return the EventHandler for instead-of update event.
     */
    public StorageHandler eventInsteadOfUpdate()
    {
        if (eventInsteadOfUpdate == null)
        {
            eventInsteadOfUpdate = new StorageHandler();
        }
        return eventInsteadOfUpdate;
    }

    /**
     * Gets the EventHandler for before update event.
     * 
     * @return the EventHandler for before update event.
     */
	public StorageHandler eventBeforeUpdate()
	{
		if (eventBeforeUpdate == null)
		{
			eventBeforeUpdate = new StorageHandler();
		}
		return eventBeforeUpdate;
	}
	
    /**
     * Gets the EventHandler for after update event.
     * 
     * @return the EventHandler for after update event.
     */
	public StorageHandler eventAfterUpdate()
	{
		if (eventAfterUpdate == null)
		{
			eventAfterUpdate = new StorageHandler();
		}
		return eventAfterUpdate;
	}
		
    /**
     * Gets the EventHandler for instead-of delete event.
     * 
     * @return the EventHandler for instead-of delete event.
     */
    public StorageHandler eventInsteadOfDelete()
    {
        if (eventInsteadOfDelete == null)
        {
            eventInsteadOfDelete = new StorageHandler();
        }
        return eventInsteadOfDelete;
    }

    /**
     * Gets the EventHandler for before delete event.
     * 
     * @return the EventHandler for before delete event.
     */
	public StorageHandler eventBeforeDelete()
	{
		if (eventBeforeDelete == null)
		{
			eventBeforeDelete = new StorageHandler();
		}
		return eventBeforeDelete;
	}
	
    /**
     * Gets the EventHandler for after delete event.
     * 
     * @return the EventHandler for after delete event.
     */
	public StorageHandler eventAfterDelete()
	{
		if (eventAfterDelete == null)
		{
			eventAfterDelete = new StorageHandler();
		}
		return eventAfterDelete;
	}
	
    /**
     * Gets whether an insteadOf insert event was assigned.
     * 
     * @return <code>true</code> if an insteadOf insert event was added, <code>false</code> otherwise
     */
    protected boolean hasInsteadOfInsertEventHandler()
    {
        return eventInsteadOfInsert != null && eventInsteadOfInsert.getListenerCount() > 0;
    } 	

    /**
     * Gets whether an insteadOf update event was assigned.
     * 
     * @return <code>true</code> if an insteadOf update event was added, <code>false</code> otherwise
     */
    protected boolean hasInsteadOfUpdateEventHandler()
    {
        return eventInsteadOfUpdate != null && eventInsteadOfUpdate.getListenerCount() > 0;
    }   

    /**
     * Gets whether an insteadOf delete event was assigned.
     * 
     * @return <code>true</code> if an insteadOf delete event was added, <code>false</code> otherwise
     */
    protected boolean hasInsteadOfDeleteEventHandler()
    {
        return eventInsteadOfDelete != null && eventInsteadOfDelete.getListenerCount() > 0;
    }   

	/**
	 * Gets the bean type based on the meta data.
	 * 
	 * @return the bean type
	 * @throws DataSourceException if an <code>Exception</code> occurs during
	 *             interacting with the storage
	 */
	protected IBeanType getAndInitBeanType() throws DataSourceException
	{
		return getBeanConverter().getBeanType();
	}
	
	/**
	 * Creates a {@link BeanType} for the given column names.
	 * 
	 * @param pColumnNames the column names which are allowed for beans
	 * @return the bean type
	 */
	protected BeanType createBeanType(String[] pColumnNames)
	{
		return new BeanType(pColumnNames);
	}

	/**
	 * Creates a new bean with all column names from the meta data. Only this column names are allowed.
	 * 
	 * @return a new instance of an {@link IBean} implementation
	 * @throws DataSourceException if an <code>Exception</code> occurs during interacting with the storage
	 */
	public IBean createEmptyBean() throws DataSourceException
	{
		return getBeanConverter().createEmptyBean();
	}

	/**
	 * Creates a bean with given values. The bean contains the property names
	 * from the column meta data and not more.
	 * 
	 * @param pValues the values in same order as the meta data
	 * @return a new bean
	 * @throws DataSourceException if an <code>Exception</code> occurs during
	 *             interacting with the storage
	 */
	protected IBean createBean(Object[] pValues) throws DataSourceException
	{
		return getBeanConverter().createBean(pValues);
	}
	
	/**
	 * Dispatches the given event with the given old and new row. The given
	 * instances of the old and new row will be changed directly.
	 * 
	 * @param pStorageHandler the {@link StorageHandler} of the event. Can be
	 *            {@code null}, which will make this method return directly.
	 * @param pType the {@link ChangedType} of the event.
	 * @param pOld the old row. May be {@code null}.
	 * @param pNew the new row. May be {@code null}.
	 * @throws DataSourceException if this method fails.
	 */
	private void dispatchEvent(StorageHandler pStorageHandler, ChangedType pType, IBean pOld, IBean pNew) throws DataSourceException
	{
		if (EventHandler.isDispatchable(pStorageHandler))
		{
			StorageEvent event = new StorageEvent(this, getBeanConverter(), pType, pOld, pNew);
			
			pStorageHandler.dispatchEvent(event);
			
			// Make sure the bean is updated.
			event.getOld();
			event.getNew();
		}
	}
	
	/**
	 * Dispatches the given event with the given old and new row. The given
	 * instances of the old and new row will be changed directly.
	 * 
	 * @param pStorageHandler the {@link StorageHandler} of the event. Can be
	 *            {@code null}, which will make this method return directly.
	 * @param pType the {@link ChangedType} of the event.
	 * @param pOld the old row. May be {@code null}.
	 * @param pNew the new row. May be {@code null}.
	 * @throws DataSourceException if this method fails.
	 */
	private void dispatchEvent(StorageHandler pStorageHandler, ChangedType pType, Object[] pOld, Object[] pNew) throws DataSourceException
	{
		if (EventHandler.isDispatchable(pStorageHandler))
		{
			StorageEvent event = new StorageEvent(this, getBeanConverter(), pType, pOld, pNew);
			
			pStorageHandler.dispatchEvent(event);
			
			// Make sure the array is updated.
			event.getOldAsArray();
			event.getNewAsArray();
		}
	}
	
	/**
	 * Dispatches the given event with the given old and new row. The given
	 * instances of the old and new row will be changed directly.
	 * 
	 * @param <T> the type.
	 * @param pStorageHandler the {@link StorageHandler} of the event. Can be
	 *            {@code null}, which will make this method return directly.
	 * @param pType the {@link ChangedType} of the event.
	 * @param pOld the old row. May be {@code null}.
	 * @param pNew the new row. May be {@code null}.
	 * @throws DataSourceException if this method fails.
	 */
	private <T> void dispatchEvent(StorageHandler pStorageHandler, ChangedType pType, T pOld, T pNew) throws DataSourceException
	{
		if (EventHandler.isDispatchable(pStorageHandler))
		{
			StorageEvent event = new StorageEvent(this, getBeanConverter(), pType, pOld, pNew);
			
			pStorageHandler.dispatchEvent(event);
			
			// Make sure the POJO is updated.
			if (pOld != null)
			{
				event.getOld(pOld.getClass());
			}
			if (pNew != null)
			{
				event.getNew(pNew.getClass());
			}
		}
	}
	
	/**
	 * Ensures that the result array has the same length as the reference array.
	 * 
	 * @param pResult the result array, that has to be trimmed.
	 * @param pReference the reference array, that gives the correct length.
	 * @return pResult, if the array has the same length as the reference array, or
	 *         a trimmed or extended array, if the reference is longer or shorter.
	 */
	private Object[] trimArray(Object[] pResult, Object[] pReference)
	{
		if (pReference == null || pResult == null || pResult.length == pReference.length)
		{
			return pResult;
		}
		else
		{
			Object[] result = new Object[pReference.length];
			
			System.arraycopy(pResult, 0, result, 0, Math.min(result.length, pResult.length));
			
			return result;
		}
	}
	
	/**
	 * Creates a POJO from the given type and with given values.
	 * 
	 * @param <T> the type of the POJO
	 * @param pClass the class of the POJO
	 * @param pValues the values for the properties in the same order as the
	 *            meta data
	 * @return the POJO
	 * @throws DataSourceException if an <code>Exception</code> occurs during
	 *             interacting with the storage
	 */
	protected <T> T createPOJO(Class<T> pClass, Object[] pValues) throws DataSourceException
	{
		return getBeanConverter().createPOJO(pClass, pValues);
	}
	
	/**
	 * Creates a POJO from the given type and with the values from a bean.
	 * 
	 * @param <T> the type of the POJO
	 * @param pClass the class of the POJO
	 * @param pBean the bean with values for the POJO
	 * @return the POJO
	 * @throws DataSourceException if an <code>Exception</code> occurs during
	 *             interacting with the storage
	 */
	public <T> T createPOJO(Class<T> pClass, IBean pBean) throws DataSourceException
	{
		return getBeanConverter().createPOJO(pClass, pBean);
	}
	
	/**
	 * Sets the property name (Java standard) for a given column name. The name will be used
	 * for synchronizing POJOs with beans.
	 * 
	 * @param pColumnName the column name
	 * @param pPropertyName the java property name e.g. firstName instead of FIRST_NAME
	 * @throws DataSourceException if access to {@link BeanConverter} fails
	 */
	public void setPropertyNameForColumn(String pColumnName, String pPropertyName) throws DataSourceException
	{
		BeanConverter bconv = getBeanConverter();
		
		if (pPropertyName == null)
		{
			bconv.removePropertyNameForColumn(pColumnName);
		}
		else
		{
			bconv.setPropertyNameForColumn(pColumnName, pPropertyName);
		}

        //Update property if already exists
		if (bconv.isInitialized())
		{
		    IBeanType beanType = bconv.getBeanType();
		    int iPos = beanType.getPropertyIndex(pColumnName);
				
			if (iPos >= 0)
			{
				if (pPropertyName == null)
				{
					//reset property name
					bconv.setPojoPropertyName(iPos, StringUtil.convertToMemberName(pColumnName));
				}
				else
				{
					//set new name
					bconv.setPojoPropertyName(iPos, pPropertyName);
				}
			}
		}
	}
	
	/**
	 * Gets the property name (Java standard) for the given column name.
	 * 
	 * @param pColumnName the column name e.g. FIRST_NAME
	 * @return the java property name e.g. firstName instead of FIRST_NAME
	 * @throws DataSourceException if access to {@link BeanConverter} fails
	 */
	public String getPropertyNameForColumn(String pColumnName) throws DataSourceException
	{
		BeanConverter bconv = getBeanConverter();
		
	    String sPropertyName = bconv.getPropertyNameForColumn(pColumnName);
	    
	    if (sPropertyName == null)
	    {
	        if (bconv.isInitialized())
	        {
	            IBeanType beanType = bconv.getBeanType();
	            int iPos = beanType.getPropertyIndex(pColumnName);
	                
                if (iPos >= 0)
                {
                    return bconv.getPojoPropertyName(iPos);
                }
	        }

	        //no access to metadata means we should build the property name from the column name
            return StringUtil.convertToMemberName(pColumnName);
	    }
	    
	    return sPropertyName;
	}
	
	/**
	 * Returns the requested bean from the storage.
	 * 
	 * @param pFilter the filter as <code>ICondition</code> to get exactly one Bean.
	 * @return the requested row as {@link IBean}, or <code>null</code> if no row was found
	 * @throws DataSourceException if an <code>Exception</code> occurs during interacting with the storage or
	 *                             more than one Bean were found
	 */
	public IBean fetchBean(ICondition pFilter) throws DataSourceException
	{
		return (IBean)fetch(IBean.class, pFilter);
	}
	
	/**
	 * Returns the requested bean/POJO from the storage.
	 * 
	 * @param <T> the type of the POJO or bean.
	 * @param pClass the class of the POJO or bean.
	 * @param pFilter the filter as <code>ICondition</code> to use get exactly one Bean.
	 * @return the requested Bean/POJO, or <code>null</code> if no Bean/POJO was found
	 * @throws DataSourceException if an <code>Exception</code> occur during interacting with the storage or
	 *                             more than one Bean/POJO were found
	 */
	public <T> T fetch(Class<T> pClass, ICondition pFilter) throws DataSourceException
	{
		List<Object[]> liObject = fetch(true, pFilter, null, 0, 2);

		int iSize = liObject.size();
		
		if (iSize < 2)
		{
			return null;
		}

		if (iSize == 2 && liObject.get(1) == null)
		{
	        BeanConverter bconv = getBeanConverter();
	        
			if (pClass == null || IBean.class.isAssignableFrom(pClass))
			{
				return (T)new ChangeableBean(bconv.createBean(liObject.get(0)));
			}
			else
			{
				return (T)bconv.createPOJO(pClass, liObject.get(0));
			}			
		}

		throw new DataSourceException("More than one bean available!");
	}
	
	/**
	 * Returns the requested list of beans from the storage.
	 * It uses the filter to reduce the result, and the sort for the order.
	 * 
	 * @param pFilter the filter as <code>ICondition</code> to use
	 * @param pSort the <code>SortDefinition</code> to use
	 * @param pFromRow the from row index to request from storage
	 * @param pMinimumRowCount the minimum row count to request, beginning from the pFromRow.
	 * @return the requested list of Beans from the storage.
	 * @throws DataSourceException if an <code>Exception</code> occur during interacting with the storage.
	 */
	public List<IBean> fetchBean(ICondition pFilter, SortDefinition pSort, int pFromRow, int pMinimumRowCount) throws DataSourceException
	{
		return (List<IBean>)fetch(IBean.class, pFilter, pSort, pFromRow, pMinimumRowCount);
	}

	/**
	 * Returns the requested list of beans/POJOs from the storage.
	 * It uses the filter to reduce the result, and the sort for the order.
	 * 
	 * @param <T> the type of the POJO or bean.
	 * @param pClass the class of the POJO or bean.
	 * @param pFilter the filter as <code>ICondition</code> to use
	 * @param pSort the <code>SortDefinition</code> to use
	 * @param pFromRow the from row index to request from storage
	 * @param pMinimumRowCount the minimum row count to request, beginning from the pFromRow.
	 * @return the requested list of Beans/POJOs from the storage.
	 * @throws DataSourceException if an <code>Exception</code> occur during interacting with the storage.
	 */
	public <T> List<T> fetch(Class<T> pClass, ICondition pFilter, SortDefinition pSort, int pFromRow, int pMinimumRowCount) throws DataSourceException
	{
		List<Object[]> liObject = fetch(true, pFilter, pSort, pFromRow, pMinimumRowCount);
		
		// create beans from the objects
		List<T> liBean;
		
		if (liObject.get(liObject.size() - 1) == null)
		{
		    liBean = new AllFetchedList<T>(liObject.size());
		}
		else
		{
		    liBean = new ArrayUtil<T>(liObject.size());
		}
		
		Object[] oValues;
		
		BeanConverter bconv = getBeanConverter();
		boolean isIBean = pClass == null || IBean.class.isAssignableFrom(pClass);
		
		for (int i = 0, anz = liObject.size(); i < anz; i++)
		{
			oValues = liObject.get(i);
			
			if (oValues != null)
			{
				if (isIBean)
				{
					liBean.add((T)new ChangeableBean(bconv.createBean(oValues)));
				}
				else
				{
					liBean.add((T)bconv.createPOJO(pClass, oValues));
				}					
			}
		}
		
		return liBean;
	}
	
	/**
	 * Refetches the specified bean/POJO and returns a bean/POJO with current values from the storage.
	 * 
	 * @param <T> the type of the POJO or bean.	
	 * @param pObject the specified bean/POJO to refetch from the storage. 
	 * @return the refetched bean/POJO with all values from the storage.
	 * @throws DataSourceException if an <code>Exception</code> occur during interacting with the Storage.
	 */
	public <T> T refetch(T pObject) throws DataSourceException
	{
	    BeanConverter bconv = getBeanConverter();
	    
		if (pObject instanceof IBean) 
		{
			return (T)bconv.createBean(refetchRow(true, bconv.createArray(pObject)));
		}
		else
		{
			return (T)bconv.createPOJO(pObject.getClass(), refetchRow(true, bconv.createArray(pObject)));
		}
	}
	
	/**
	 * Inserts the new bean/POJO and returns a bean/POJO with current values from the storage.
	 * 
	 * @param <T> the type of the POJO or bean.	
	 * @param pObject the new IBean/POJO to insert to the storage.
	 * @return the newly inserted IBean/POJO with all automatically filled in values from the storage.
	 * @throws DataSourceException if an <code>Exception</code> occur during insert into the storage
	 */
	public <T> T insert(T pObject) throws DataSourceException
	{
		if (pObject instanceof IBean)
		{
			IBean row = (IBean)pObject;
			
			dispatchEvent(eventBeforeInsert, ChangedType.BEFORE_INSERT, null, row);
			
	        if (EventHandler.isDispatchable(eventInsteadOfInsert))
	        {
	            if (isRefetch())
	            {
	                row = (IBean)row.clone();
	            }
	            
	            dispatchEvent(eventInsteadOfInsert, ChangedType.INSTEADOF_INSERT, null, row);
	        }
	        else
	        {
	            BeanConverter bconv = getBeanConverter();

	            Object[] array = bconv.createArray(row);
    			
    			if (isRefetch())
    			{
    				// If refetch is enabled, we clone the bean and use the refetched
    				// values to update that clone. The original bean is unmodified.
    				array = executeInsertAsBean(array);
    				row = (IBean)row.clone();
    			}
    			else
    			{
    				// If refetch is disabled, we only want the PK changes,
    				// so we ignore the refetched values as the PKs are set into
    				// the original array.
    				// We later use that array to update the original bean.
    				executeInsertAsBean(array);
    			}
    			
    			bconv.updateBean(row, array);
	        }
	        
			dispatchEvent(eventAfterInsert, ChangedType.AFTER_INSERT, null, row);
			dispatchEvent(eventCalculateRow, ChangedType.CALCULATE_ROW, null, row);
			
			return (T)row;
		}
		else
		{
			T row = pObject;
			
			dispatchEvent(eventBeforeInsert, ChangedType.BEFORE_INSERT, null, row);
			
			if (EventHandler.isDispatchable(eventInsteadOfInsert))
			{
                if (isRefetch())
                {
                    row = (T)BeanType.getBeanType(row).clone(row);
                }
                
                dispatchEvent(eventInsteadOfInsert, ChangedType.INSTEADOF_INSERT, null, row);
			}
			else
			{
		        BeanConverter bconv = getBeanConverter();

		        Object[] array = bconv.createArray(row);
    			
    			if (isRefetch())
    			{
    				// If refetch is enabled, we clone the POJO and use the refetched
    				// values to update that clone. The original POJO is unmodified.
    				array = executeInsert(array);
    				row = (T)BeanType.getBeanType(row).clone(row);
    			}
    			else
    			{
    				// If refetch is disabled, we only want the PK changes,
    				// so we ignore the refetched values as the PKs are set into
    				// the original array.
    				// We later use that array to update the original POJO.
    				executeInsert(array);
    			}
    			
    			bconv.updatePOJO(row, array);
			}
			
			dispatchEvent(eventAfterInsert, ChangedType.AFTER_INSERT, null, row);
			dispatchEvent(eventCalculateRow, ChangedType.CALCULATE_ROW, null, row);
			
			return row;
		}
	}	

    /**
     * Updates a bean/POJO with the PrimaryKey columns and provides values. It returns a bean/POJO with the 
     * current values from the storage.
     * 
     * @param <T> the type of the POJO or bean. 
     * @param pObject the new IBean/POJO to use for the update
     * @return the updated IBean/POJO from the storage.
     * @throws DataSourceException if an <code>Exception</code> occur during updating process in the storage.
     */
    public <T> T update(T pObject) throws DataSourceException
    {
        return update(null, pObject);
    }	
	
	/**
	 * Updates a bean/POJO with the PrimaryKey columns and provides values. It returns a bean/POJO with the 
	 * current values from the storage.
	 * 
	 * @param <T> the type of the POJO or bean.
	 * @param pOldObject the old IBean/POJO to use for the update	
	 * @param pNewObject the new IBean/POJO to use for the update
	 * @return the updated IBean/POJO from the storage.
	 * @throws DataSourceException if an <code>Exception</code> occur during updating process in the storage.
	 */
	public <T> T update(T pOldObject, T pNewObject) throws DataSourceException
	{
		MetaData metaData = getMetaData();

		BeanConverter bconv = getBeanConverter();
		IBeanType beanType = bconv.getBeanType();
		
		Object[] oNewValues = bconv.createArray(pNewObject);
		Object[] oOldValues;

		if (pOldObject != null)
		{
		    oOldValues = bconv.createArray(pOldObject);
		}
		else if (pNewObject instanceof ChangeableBean)
		{
		    oOldValues = bconv.createArray(((ChangeableBean)pNewObject).getOriginalBean());
		}
		else
		{
    		if (pNewObject instanceof IBean) 
    		{
    			oOldValues = oNewValues.clone();
    			String[] columnsSet = ((IBean)pNewObject).getBeanType().getPropertyNames();
    			for (int i = 0, idx; i < columnsSet.length; i++)
    			{
    				idx = beanType.getPropertyIndex(columnsSet[i]);
    				if (idx >= 0)
    				{
    					oOldValues[idx] = UNDEFINED_VALUE;
    				}
    			}
    		}
    		else
    		{
    			oOldValues = new Object[oNewValues.length];
    			for (int i = 0; i < oOldValues.length; i++)
    			{
    				oOldValues[i] = UNDEFINED_VALUE;
    			}
    		}
    		
            String[] sPKCols = metaData.getPrimaryKeyColumnNames();
            
            if (sPKCols != null)
            {
                //copy PK columns -> important for update!
                for (int i = 0, idx; i < sPKCols.length; i++)
                {
                    idx = beanType.getPropertyIndex(sPKCols[i]);
                    
                    oOldValues[idx] = oNewValues[idx];
                }
            }
		}
		
		dispatchEvent(eventBeforeUpdate, ChangedType.BEFORE_UPDATE, oOldValues, oNewValues);
		
		if (pNewObject instanceof IBean)
		{
			IBean row = (IBean)pNewObject;
			
			if (EventHandler.isDispatchable(eventInsteadOfUpdate))
			{
			    dispatchEvent(eventInsteadOfUpdate, ChangedType.INSTEADOF_UPDATE, oOldValues, oNewValues);

			    if (isRefetch())
			    {
			        row = (IBean)row.clone();
			    }
			}
			else
			{
    			if (isRefetch())
    			{
    				oNewValues = executeUpdateAsBean(oOldValues, oNewValues);
    				
    				row = (IBean)row.clone();
    			}
    			else
    			{
    				executeUpdateAsBean(oOldValues, oNewValues);
    			}
			}
			
            dispatchEvent(eventAfterUpdate, ChangedType.AFTER_UPDATE, oOldValues, oNewValues);
			dispatchEvent(eventCalculateRow, ChangedType.CALCULATE_ROW, null, oNewValues);
			
			//Update after events, to update the row with changed values
            bconv.updateBean(row, oNewValues);

            return (T)row;
		}
		else
		{
			Object row = pNewObject;

			if (EventHandler.isDispatchable(eventInsteadOfUpdate))
			{
			    dispatchEvent(eventInsteadOfUpdate, ChangedType.INSTEADOF_UPDATE, oOldValues, oNewValues);
			    
			    if (isRefetch())
			    {
			        row = BeanType.getBeanType(pNewObject).clone(pNewObject);
			    }
			}
			else
			{
    			if (isRefetch())
    			{
    				oNewValues = executeUpdate(oOldValues, oNewValues);
    				
    				row = BeanType.getBeanType(pNewObject).clone(pNewObject);
    			}
    			else
    			{
    				executeUpdate(oOldValues, oNewValues);
    			}
			}

            dispatchEvent(eventAfterUpdate, ChangedType.AFTER_UPDATE, oOldValues, oNewValues);
			dispatchEvent(eventCalculateRow, ChangedType.CALCULATE_ROW, null, oNewValues);
			
            //Update after events, to update the row with changed values
            bconv.updatePOJO(row, oNewValues);

            return (T)row;
		}
	}

	/**
	 * Deletes the specified bean/POJO from the storage.
	 * 
	 * @param <T> the type of the POJO or bean.
	 * @param pObject the bean/POJO to delete from the storage.
	 * @throws DataSourceException if an <code>Exception</code> occur during the delete operation in the storage.
	 */
	public <T> void delete(T pObject) throws DataSourceException
	{
		if (pObject instanceof IBean)
		{
			IBean bean = (IBean) pObject;
			
			dispatchEvent(eventBeforeDelete, ChangedType.BEFORE_DELETE, bean, null);
			
			if (EventHandler.isDispatchable(eventInsteadOfDelete))
		    {
			    dispatchEvent(eventInsteadOfDelete, ChangedType.INSTEADOF_DELETE, bean, null);
		    }
			else
			{
			    executeDeleteAsBean(getBeanConverter().createArray(bean));
		    }
			
			dispatchEvent(eventAfterDelete, ChangedType.AFTER_DELETE, bean, null);
		}
		else
		{
			dispatchEvent(eventBeforeDelete, ChangedType.BEFORE_DELETE, pObject, null);

			if (EventHandler.isDispatchable(eventInsteadOfDelete))
	        {
			    dispatchEvent(eventInsteadOfDelete, ChangedType.INSTEADOF_DELETE, pObject, null);
	        }
			else
			{
			    executeDelete(getBeanConverter().createArray(pObject));
			}
			
			dispatchEvent(eventAfterDelete, ChangedType.AFTER_DELETE, pObject, null);
		}
	}
	
	/**
	 * This method will be used if {@link #refetch(Object)} was called.
	 * 
	 * @param pDataRow the specified row as <code>Object[]</code>.
	 * @return the refetched row as <code>Object[]</code>.
	 * @throws DataSourceException if an <code>Exception</code> occur during interacting with the storage.
	 * @see javax.rad.persist.IStorage#refetchRow(Object[])
	 */
	protected Object[] executeRefetchRowAsBean(Object[] pDataRow) throws DataSourceException
	{
		return executeRefetchRow(pDataRow);
	}
	
	/**
	 * This method will be used if {@link #fetchBean(ICondition, SortDefinition, int, int)} was called.
	 * 
	 * @param pFilter the <code>ICondition</code> to use.
	 * @param pSort	the <code>SortDefinition</code> to use.
	 * @param pFromRow the from row index to request from storage.
	 * @param pMinimumRowCount the minimum row count to request, beginning from the pFromRow.
	 * @return the requested rows as <code>List[Object[]]</code>.
	 * @throws DataSourceException if an <code>Exception</code> occur during interacting with the storage.
	 * @see javax.rad.persist.IStorage#fetch(ICondition, SortDefinition, int, int)
	 */
	protected List<Object[]> executeFetchAsBean(ICondition pFilter, SortDefinition pSort, int pFromRow, int pMinimumRowCount) throws DataSourceException
	{
		return executeFetch(pFilter, pSort, pFromRow, pMinimumRowCount);
	}
	
	/**
	 * This method will be used if {@link #insert(Object)} was called.
	 * 
	 * @param pDataRow the new row as <code>Object[]</code> to insert.
	 * @return the newly inserted row from this IStorage.
	 * @throws DataSourceException if an <code>Exception</code> occur during insert the row to the storage
	 * @see javax.rad.persist.IStorage#insert(Object[])
	 */
	protected Object[] executeInsertAsBean(Object[] pDataRow) throws DataSourceException
	{
		return executeInsert(pDataRow);
	}
	
	/**
	 * This method will be used if {@link #update(Object)} was called.
	 * 
	 * @param pOldDataRow the old row as <code>Object[]</code>
	 * @param pNewDataRow the new row as <code>Object[]</code> to update
	 * @return the updated row as <code>Object[]</code>.
	 * @throws DataSourceException if an <code>Exception</code> occur during updating the row.
	 * @see javax.rad.persist.IStorage#update(Object[], Object[])
	 */
	protected Object[] executeUpdateAsBean(Object[] pOldDataRow, Object[] pNewDataRow) throws DataSourceException
	{
		return executeUpdate(pOldDataRow, pNewDataRow);
	}
	
	/**
	 * This method will be used if {@link #delete(Object)} was called.
	 * 
	 * @param pDeleteDataRow the row as <code>Object[]</code> to delete.
	 * @throws DataSourceException if an <code>Exception</code> occur during deleting the row or
	 *             				   if the storage isn't opened or the PrimaryKey is wrong and more/less 
	 *                             then one row is deleted.
	 * @see javax.rad.persist.IStorage#delete(Object[])
	 */
	protected void executeDeleteAsBean(Object[] pDeleteDataRow) throws DataSourceException
	{
		executeDelete(pDeleteDataRow);
	}
	
	/**
	 * Puts an additional cached object to this storage.
	 * 
	 * @param pName the object name
	 * @param pValue the object
	 * @return the old object if an object was already added
	 */
	public Object putObject(String pName, Object pValue)
	{
        if (pValue == null)
        {
            if (hmpObjects == null)
            {
                return null;
            }
            else
            {
                Object o = hmpObjects.remove(pName);
    			
    			if (hmpObjects.isEmpty())
    			{
    				hmpObjects = null;
    			}
    			
    			return o;
            }
		}
		else
		{
	        if (hmpObjects == null)
	        {
	            hmpObjects = new HashMap<String, Object>();
	        }
	        
			return hmpObjects.put(pName, pValue);
		}
	}
	
	/**
	 * Gets an additional cached object from this storage.
	 * 
	 * @param pName the object name
	 * @return the old object if an object was already added, <code>null</code> otherwise
	 */
	public Object getObject(String pName)
	{
		if (hmpObjects == null)
		{
			return null;
		}
		
		return hmpObjects.get(pName);
	}
	
	/**
	 * Checks if the given string contains invalid characters.
	 * 
	 * @param pString the string.
	 * @throws IllegalArgumentException if string contains a character which is not a letter, digit or <code>._-$# </code>.
	 */
	protected void checkChars(String pString)
	{
		if (pString == null)
		{
			return;
		}
		
		try
		{
			if (ArrayUtil.contains(getMetaData().getColumnNames(), pString))
			{
			    return;
			}
		}
		catch (DataSourceException dse)
		{
			throw new RuntimeException(dse);
		}

		for (char currentChar : pString.toCharArray())
		{
			if (!Character.isLetterOrDigit(currentChar) && "._-$# ".indexOf(currentChar) < 0)
			{
				throw new IllegalArgumentException("Invalid characters detected.");
			}
		}
	}

	/**
	 * Checks if given sort definition contains invalid characters.
	 * 
	 * @param pSort the sort definition.
	 * @throws IllegalArgumentException if sort definition contains a character which is not a letter, digit or <code>._-$# </code>.
	 */
	protected void checkSort(SortDefinition pSort)
	{
		for (String column : pSort.getColumns())
		{
			checkChars(column);
		}
	}
	
	/**
	 * Checks if the given condition contains invalid characters.
	 * 
	 * @param pCondition the condition.
	 * @throws IllegalArgumentException if condition contains a character which is not a letter, digit or <code>._-$# </code>.
	 */
	protected void checkCondition(ICondition pCondition)
	{
		if (pCondition instanceof CompareCondition)
		{
			checkChars(((CompareCondition)pCondition).getColumnName());
		}
		else if (pCondition instanceof Not)
		{
			checkCondition(((Not)pCondition).getCondition());
		}
		else if (pCondition instanceof OperatorCondition)
		{
			for (ICondition cond : ((OperatorCondition)pCondition).getConditions())
			{
				checkCondition(cond);
			}
		}
	}
	
    //****************************************************************
    // Subclass definition
    //****************************************************************
	
	/**
	 * The <code>AllFetchedList</code> is an {@link ArrayUtil} wrapper that implements the marker interface 
	 * {@link IAllFetched}.
	 * 
     * @param <E> the element type
     * 
	 * @author René Jahn
	 */
	protected static class AllFetchedList<E> extends ArrayUtil<E>
	                                         implements IAllFetched
    {
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Initialization
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    
	    /**
	     * Constructs an empty list with initial size {@link #MIN_SIZE}.
	     */
	    public AllFetchedList() 
	    {
	        super();
	    }

	    /**
	     * Constructs an empty list.
	     * 
	     * @param pInitialSize the initial size.
	     */
	    public AllFetchedList(int pInitialSize) 
	    {
	        super(pInitialSize);
	    }

	    /**
	     * Constructs a list with the given array. The given array is used directly.
	     * If it should be cloned, it has to be done outside manually. 
	     *
	     * @param pSourceArray the array for this list.
	     * @throws NullPointerException if the specified pSourceArray is null.
	     */
	    public AllFetchedList(E... pSourceArray) 
	    {
	        super(pSourceArray);
	    }

	    /**
	     * Constructs a list with the given array. The given array is used directly.
	     * If it should be cloned, it has to be done outside manually.
	     * The list has the given size. If the size is &lt; 0 the size is set to the array length. 
	     * If the size is greater than the array length, an IndexOutOfBoundsException is thrown. 
	     *
	     * @param pSourceArray the array for this list.
	     * @param pSize        the size of this list.
	     * @throws NullPointerException if the specified pSourceArray is null.
	     * @throws IndexOutOfBoundsException if the specified pSize is greater than the array length.
	     */
	    public AllFetchedList(E[] pSourceArray, int pSize) 
	    {
	        super(pSourceArray, pSize);
	    }

	    /**
	     * Constructs a list with the given array. The given array is used directly.
	     * If it should be cloned, it has to be done outside manually.
	     * The list starts at the given offset. The list has the given size. 
	     * If the size is &lt; 0 the size is set to the array length. If offset + size is 
	     * greater than the array length, an IndexOutOfBoundsException is thrown. 
	     *
	     * @param pSourceArray the array for this list.
	     * @param pOffset      the offset at which the list starts.
	     * @param pSize        the size of this list.
	     * @throws IndexOutOfBoundsException if the specified pOffset + pSize is greater than the array length.
	     */
	    public AllFetchedList(E[] pSourceArray, int pOffset, int pSize) 
	    {
	        super(pSourceArray, pOffset, pSize);
	    }

	    /**
	     * Constructs a list containing the elements of the specified
	     * collection.
	     *
	     * @param pCollection the collection whose elements are to be placed into this list.
	     * @throws NullPointerException if the specified collection is null.
	     */
	    public AllFetchedList(Collection<? extends E> pCollection) 
	    {
	        super(pCollection);
	    }
	    
    }   // AllFetchedList
	
    /**
     * The <code>ChangeableBean</code> stores the original bean before changed, like the {@link javax.rad.model.IChangeableDataRow} does.
     * 
     * @author Martin Handsteiner
     */
    public static class ChangeableBean extends Bean
    {
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Class members
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    	/** the original bean. */
        private Bean originalBean;
        
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Initialization
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /**
         * Constructs a <code>ChangeableBean</code> from a given <code>Bean</code>.
         * 
         * @param pOriginalBean the original bean.
         */
        public ChangeableBean(IBean pOriginalBean)
        {
            super(pOriginalBean);
            
            originalBean = (Bean)pOriginalBean;
        }
        
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Overwritten methods
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString()
        {
            return super.toString() + "(original=" + originalBean + ")";
        }

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // User-defined methods
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /**
         * The original bean.
         * 
         * @return the original bean
         */
        public Bean getOriginalBean()
        {
            return originalBean;
        }
        
    }	// ChangeableBean

}	// AbstractStorage
