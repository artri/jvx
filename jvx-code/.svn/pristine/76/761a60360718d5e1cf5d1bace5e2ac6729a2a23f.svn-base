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
 * 30.10.2010 - [JR] - creation
 * 05.01.2011 - [RH] - #233: insert, update, .... methods prefixed with "execute", because of the Server Side Trigger implementation.
 * 13.01.2011 - [JR] - open now initializes metadata, close unsets fields
 * 14.01.2011 - [JR] - executeInsert: restore if an exception occurs
 *                   - executeUpdate: restore if an exception occurs
 * 31.03.2011 - [JR] - #326: default sort
 * 11.04.2011 - [JR] - #332: support invisible columns for insert/update/delete 
 * 13.04.2011 - [JR] - #337: executeUpdate checks changed columns       
 * 19.11.2012 - [JR] - code review: @Override added to abstract methods
 * 08.12.2012 - [JR] - #611: overwrote xxxAsBean methods
 * 24.11.2013 - [JR] - search condition shouldn't ignore NULL values
 * 31.01.2015 - [JR] - #1249: initialized allowed values
 * 04.02.2015 - [JR] - #1251: setDispatchEventsEnabled implemented
 * 28.07.2015 - [JR] - support custom/external databook
 */
package com.sibvisions.rad.persist;

import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import javax.rad.model.ColumnDefinition;
import javax.rad.model.ColumnView;
import javax.rad.model.IDataRow;
import javax.rad.model.IRowDefinition;
import javax.rad.model.ModelException;
import javax.rad.model.RowDefinition;
import javax.rad.model.SortDefinition;
import javax.rad.model.condition.And;
import javax.rad.model.condition.ICondition;
import javax.rad.model.datatype.BigDecimalDataType;
import javax.rad.model.datatype.BinaryDataType;
import javax.rad.model.datatype.BooleanDataType;
import javax.rad.model.datatype.IDataType;
import javax.rad.model.datatype.LongDataType;
import javax.rad.model.datatype.StringDataType;
import javax.rad.model.event.DataBookEvent;
import javax.rad.persist.ColumnMetaData;
import javax.rad.persist.DataSourceException;
import javax.rad.persist.MetaData;
import javax.rad.type.bean.BeanType;

import com.sibvisions.rad.model.DataBookCSVExporter;
import com.sibvisions.rad.model.Filter;
import com.sibvisions.rad.model.mem.MemDataBook;
import com.sibvisions.rad.server.annotation.Accessible;
import com.sibvisions.util.ArrayUtil;

/**
 * The <code>AbstractMemStorage</code> is an {@link AbstractCachedStorage} and holds an internal {@link MemDataBook}
 * for the data. It implements all methods which are necessary that the client is able to receive data.
 * All data manipulation methods like insert/update/delete are abstract and have to be implemented.
 * 
 * @author René Jahn
 */
public abstract class AbstractMemStorage extends AbstractCachedStorage 
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the metadata. */
	private MetaData metadata = null;
	
	/** the memory data book. */
	private MemDataBook book;
	
	/** the cached row definition column names. */
	private String[] saAllColumns = null;

	/** the cached meta data column names. */
	private String[] saQueryColumns = null;
	
	/** the search row. */
	private IDataRow rowSearch;
	
	/** the search condition. */
	private ICondition condSearch;
	
	/** the restrict condition. */
	private ICondition condRestrict;
	
	/** the default sort definition. */
	private SortDefinition sortDefault = null;
	
	/** whether the data manipulation events should be ingored. */
	private boolean bIgnoreEvents = false;
	
	/** whether the databook was set via constructor. */
	private boolean bExternalDataBook = false;
	
	/** whether this storage is open. */
	private boolean bIsOpen = false;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization 
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>AbstractMemStorage</code> with an
	 * external data book.
	 * 
	 * @param pDataBook the data book
	 */
	protected AbstractMemStorage(MemDataBook pDataBook)
	{
	    book = pDataBook;
	    
	    bExternalDataBook = true;
	}
	
	/**
	 * Creates a new instance of <code>AbstractMemStorage</code>.
	 */
	public AbstractMemStorage()
	{
		book = new MemDataBook();

		try
        {
            book.setName("storage");
        }
        catch (ModelException me)
        {
            // not possible
        }
		
		initListener();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeCSV(OutputStream pStream, 
			             String[] pColumnNames, 
			             String[] pLabels, 
			             ICondition pFilter, 
			             SortDefinition pSort, 
			             String pSeparator) throws Exception
	{
		DataBookCSVExporter.writeCSV(book, pStream, pColumnNames, pLabels, pFilter, pSort, pSeparator);
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
	 * {@inheritDoc}
	 */
	@Override
	public void open() throws DataSourceException
	{
		if (!isOpen())
		{
			try
			{
				IRowDefinition rowdef = getRowDefinition();
				
				if (rowdef == null)
				{
				    throw new DataSourceException("RowDefintion can't be null!");
				}
				
				ColumnDefinition coldef;
				
				//#1249
				for (int i = 0, cnt = rowdef.getColumnCount(); i < cnt; i++)
				{
				    coldef = rowdef.getColumnDefinition(i);
				    
				    if (BooleanDataType.TYPE_IDENTIFIER == coldef.getDataType().getTypeIdentifier())
				    {
				        if (coldef.getAllowedValues() == null)
				        {
				            coldef.setAllowedValues(new Boolean[] {Boolean.TRUE, Boolean.FALSE});
				        }
				    }
				}
				
				if (!bExternalDataBook)
				{
    				book.setRowDefinition(rowdef);
    				book.open();
				}
				else
				{
				    initListener();
				    
				    if (!book.isOpen())
    				{
    				    if (book.getName() == null)
    				    {
    				        book.setName(getName());
    				    }
    				    
    				    book.open();
    				}
				}
				
				try
				{
					MetaData meta = new MetaData();
					
					String[] saColums = null;
					
                    ColumnView cview = rowdef.getColumnView(null);

                    if (bExternalDataBook)
					{
					    if (rowdef instanceof RowDefinition)
					    {
					        //default columnview is equal with the detected column view (means, columnview isn't user-defined)
					        if (Arrays.equals(cview.getColumnNames(), ((RowDefinition)rowdef).getDefaultColumnView().getColumnNames()))
					        {
					            //use all column names (with IDs, ...)
					            saColums = rowdef.getColumnNames();
					        }
					    }
					}

                    //default: only defined columns
                    if (saColums == null)
					{
	                    saColums = cview.getColumnNames();
					}

					for (String sColumn : saColums)
					{
						meta.addColumnMetaData(createColumnMetaData(rowdef.getColumnDefinition(sColumn)));
					}

					meta.setPrimaryKeyColumnNames(rowdef.getPrimaryKeyColumnNames());
					
					metadata = meta;
					
					saQueryColumns = saColums;

					//create a list with ALL columns, but keep the order of saQueryColumns
					//That is important because it is possible that the client inserts only
					//fetched columns and the server inserts via Bean or as array, but the
					//server is allowed to insert ALL columns not only client columns
					String[] sCols = rowdef.getColumnNames();
					
					ArrayUtil<String> auAllColumns = new ArrayUtil<String>(saQueryColumns);
					
					for (int i = 0; i < sCols.length; i++)
					{
						if (auAllColumns.indexOf(sCols[i]) < 0)
						{
							auAllColumns.add(sCols[i]);
						}
					}

					saAllColumns = auAllColumns.toArray(new String[auAllColumns.size()]);
					
					//Search tuning
					String[] saSearchCols = rowdef.getPrimaryKeyColumnNames();
					
					//NO PK -> use all columns
					if (saSearchCols == null || saSearchCols.length == 0)
					{
						saSearchCols = saColums;
					}
					
					rowSearch = book.createEmptyDataRow(saAllColumns);
					
					condSearch = Filter.createEqualsFilter(rowSearch, saSearchCols, false);
				}
				catch (ModelException me)
				{
					throw new DataSourceException("Can not build meta data!", me);
				}
			}
			catch (ModelException me)
			{
				throw new DataSourceException("Can not open MemDataBook!", me);
			}
			
			bIsOpen = true;
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Accessible
	public final MetaData getMetaData() throws DataSourceException
	{
		if (!isOpen())
		{
			throw new DataSourceException("AbstractMemStorage isn't open!");			
		}
		
		return metadata;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected final List<Object[]> executeFetch(ICondition pFilter, SortDefinition pSort, int pFromRow, int pMinimumRowCount) throws DataSourceException
	{
		return executeFetch(saQueryColumns, pFilter, pSort, pFromRow, pMinimumRowCount);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected final List<Object[]> executeFetchAsBean(ICondition pFilter, SortDefinition pSort, int pFromRow, int pMinimumRowCount) throws DataSourceException
	{
		return executeFetch(saAllColumns, pFilter, pSort, pFromRow, pMinimumRowCount);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected final Object[] executeRefetchRow(Object[] pDataRow) throws DataSourceException
	{
		if (pDataRow.length > saQueryColumns.length)
		{
			//if we use the client-API on server-side (is possible)
			return executeRefetchRow(saAllColumns, pDataRow);
		}
		else
		{
			return executeRefetchRow(saQueryColumns, pDataRow);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected final Object[] executeRefetchRowAsBean(Object[] pDataRow) throws DataSourceException
	{
		return executeRefetchRow(saAllColumns, pDataRow);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected final Object[] executeInsert(Object[] pDataRow) throws DataSourceException
	{
		if (pDataRow.length > saQueryColumns.length)
		{
			//if we use the client-API on server-side (is possible)
			return executeInsert(saAllColumns, pDataRow);
		}
		else
		{
			return executeInsert(saQueryColumns, pDataRow);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected final Object[] executeInsertAsBean(Object[] pDataRow) throws DataSourceException
	{
		return executeInsert(saAllColumns, pDataRow);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected final Object[] executeUpdate(Object[] pOldDataRow, Object[] pNewDataRow) throws DataSourceException
	{
		if (pNewDataRow.length > saQueryColumns.length)
		{
			//if we use the client-API on server-side (is possible)
			return executeUpdate(saAllColumns, pOldDataRow, pNewDataRow);
		}
		else
		{
			return executeUpdate(saQueryColumns, pOldDataRow, pNewDataRow);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected final Object[] executeUpdateAsBean(Object[] pOldDataRow, Object[] pNewDataRow) throws DataSourceException
	{
		return executeUpdate(saAllColumns, pOldDataRow, pNewDataRow);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected final void executeDelete(Object[] pDeleteDataRow) throws DataSourceException
	{
		if (pDeleteDataRow.length > saQueryColumns.length)
		{
			//if we use the client-API on server-side (is possible)
			executeDelete(saAllColumns, pDeleteDataRow);
		}
		else
		{
			executeDelete(saQueryColumns, pDeleteDataRow);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void executeDeleteAsBean(Object[] pDeleteDataRow) throws DataSourceException
	{
		executeDelete(saAllColumns, pDeleteDataRow);
	}

	/**
	 * {@inheritDoc}
	 */
	public int getEstimatedRowCount(ICondition pFilter) throws DataSourceException
	{
		try
		{
			return book.getEstimatedRowCount();
		}
		catch (ModelException me)
		{
			throw new DataSourceException("Can not calculate estimated row count!", me);
		}
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Returns the row definition for the internal {@link MemDataBook}. The row definition will
	 * be used to create and cache the meta data. The {@link ColumnView} will be used to detect
	 * the column which will be used for meta data creation.
	 * 
	 * @return the row definition
	 * @throws ModelException if the row definition can not be created
	 */
	public abstract RowDefinition getRowDefinition() throws ModelException;
	
	/**
	 * Loads the relevant data into the internal data book. The data book is always the same and
	 * won't be deleted by this storage. This method is invoked when the client tries to fetch data.
	 * 
	 * @param pBook the internal data book
	 * @param pFilter the filter
	 * @throws ModelException if an error occurs during filling
	 */
	public abstract void loadData(MemDataBook pBook, ICondition pFilter) throws ModelException;
	
	/**
	 * Inserts a new row. Called before inserted a new row.
	 * 
	 * @param pEvent the data book event
	 * @throws ModelException if an insert error occurs
	 */
	public abstract void insert(DataBookEvent pEvent) throws ModelException;
	
	/**
	 * Updates an existing row. Called before updated an existing row.
	 * 
	 * @param pEvent the data book event
	 * @throws ModelException if an update error occurs
	 */
	public abstract void update(DataBookEvent pEvent) throws ModelException;

	/**
	 * Deletes an existing row. Called before deleted an existing row.
	 * 
	 * @param pEvent the data book event
	 * @throws ModelException if a delete error occurs
	 */
	public abstract void delete(DataBookEvent pEvent) throws ModelException;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void finalize() throws Throwable
	{
		try
		{
			close();
		}
		catch (Throwable th)
		{
			//nothing to be done
		}
		
		super.finalize();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BeanType createBeanType(String[] pColumnNames)
	{
		//Create a bean type for ALL columns, instead of meta-data columns!
		return super.createBeanType(saAllColumns);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
    /**
     * Initializes the databook listeners.
     */
    private void initListener()
    {
        book.eventBeforeInserted().addListener(this, "doInsert");
        book.eventBeforeInserted().addListener(this, "doValidate");
        book.eventBeforeDeleted().addListener(this, "doDelete");
        book.eventBeforeUpdated().addListener(this, "doUpdate");
        book.eventBeforeUpdated().addListener(this, "doValidate");
    }

    /**
     * Removes all databook listeners.
     */
    private void removeListener()
    {
        book.eventBeforeInserted().removeListener(this, "doInsert");
        book.eventBeforeInserted().removeListener(this, "doValidate");
        book.eventBeforeDeleted().removeListener(this, "doDelete");
        book.eventBeforeUpdated().removeListener(this, "doUpdate");
        book.eventBeforeUpdated().removeListener(this, "doValidate");
    }
	
	/**
	 * It returns the ColumnMetaData to the corresponding <code>ColumnDefinition</code>.
	 * 
	 * @param pColumnDefinition		the ColumnDefinition to use.
	 * @return the ColumnMetaData to the corresponding <code>ColumnDefinition</code>.
	 */
	protected ColumnMetaData createColumnMetaData(ColumnDefinition pColumnDefinition)
	{
		return initializeColumnMetaData(pColumnDefinition, new ColumnMetaData());
	}

	/**
	 * This initializes the ColumnMetaData with the given corresponding <code>ColumnDefinition</code>.
	 * 
	 * @param pColumnDefinition		the ColumnDefinition to use.
	 * @param pColumnMetaData       the ColumnMetaData.
	 * @return ColumnMetaData the result column metadata
	 */
	protected ColumnMetaData initializeColumnMetaData(ColumnDefinition pColumnDefinition, ColumnMetaData pColumnMetaData)
	{
		pColumnMetaData.setName(pColumnDefinition.getName());
		pColumnMetaData.setLabel(pColumnDefinition.getLabel());
		pColumnMetaData.setNullable(pColumnDefinition.isNullable());
		pColumnMetaData.setWritable(pColumnDefinition.isWritable());
		pColumnMetaData.setFilterRequired(pColumnDefinition.isFilterRequired());
		pColumnMetaData.setCalculated(false);

		pColumnMetaData.setDefaultValue(pColumnDefinition.getDefaultValue());
		pColumnMetaData.setAllowedValues(pColumnDefinition.getAllowedValues());

		return initializeDataType(pColumnDefinition, pColumnMetaData); 
	}
	
	/**
	 * This initializes the column meta data with the data of the data type <code>IDataType</code>.
	 * 
	 * @param pColumnDefinition		the ColumnDefinition to use.
	 * @param pColumnMetaData       the ColumnMetaData.
	 * @return ColumnMetaData the result column metadata
	 */
	protected ColumnMetaData initializeDataType(ColumnDefinition pColumnDefinition, ColumnMetaData pColumnMetaData)
	{
		int iTypeIdentifier = pColumnDefinition.getDataType().getTypeIdentifier();
		
		pColumnMetaData.setTypeIdentifier(iTypeIdentifier);

		switch (iTypeIdentifier)
		{
			case BigDecimalDataType.TYPE_IDENTIFIER:
				
				BigDecimalDataType bgDataType = (BigDecimalDataType)pColumnDefinition.getDataType();
				
				pColumnMetaData.setPrecision(bgDataType.getPrecision());
				pColumnMetaData.setScale(bgDataType.getScale());
				pColumnMetaData.setSigned(bgDataType.isSigned());
				break;
				
			case LongDataType.TYPE_IDENTIFIER:
				
				LongDataType loDataType = (LongDataType)pColumnDefinition.getDataType();
				
				pColumnMetaData.setSigned(loDataType.isSigned());
				break;

			case StringDataType.TYPE_IDENTIFIER:
				
				StringDataType strDataType = (StringDataType)pColumnDefinition.getDataType();
				
				pColumnMetaData.setPrecision(strDataType.getSize());
				break;

			case BinaryDataType.TYPE_IDENTIFIER:
				
				BinaryDataType binDataType = (BinaryDataType)pColumnDefinition.getDataType();
				
				pColumnMetaData.setPrecision(binDataType.getSize());
				break;

			default:
		}
		
		return pColumnMetaData;
	}
	

	/**
	 * Closes the storage.
	 */
	public void close()
	{
		if (isOpen())
		{
		    if (!bExternalDataBook)
		    {
		        book.close();
		    }
		    else
		    {
		        removeListener();
		    }
			
			metadata 	   = null;
			saAllColumns   = null;
			saQueryColumns = null;
			rowSearch 	   = null;
			condSearch 	   = null;

			bIgnoreEvents  = false;
			
			bIsOpen = false;
		}
	}

	/**
	 * Fetches data.
	 * 
	 * @param pQueryColumns the query columns
	 * @param pFilter the filter condition
	 * @param pSort the sort definition
	 * @param pFromRow the from row number
	 * @param pMinimumRowCount the minimum row count
	 * @return the fetched data
	 * @throws DataSourceException if fetch fails
	 */
	protected List<Object[]> executeFetch(String[] pQueryColumns, ICondition pFilter, SortDefinition pSort, int pFromRow, int pMinimumRowCount) throws DataSourceException
	{
        if (!isOpen())
        {
            throw new DataSourceException("AbstractMemStorage isn't open!");            
        }
	    
		try
		{
			bIgnoreEvents = true;
			
			book.setSort(null);
			try
			{
				loadData(book, createFilter(pFilter));
				book.saveAllRows();
			}
			finally
			{
				bIgnoreEvents = false;
			}
			
			if (pSort == null)
			{
				book.setSort(sortDefault);
			}
			else
			{
				checkSort(pSort);
				
				book.setSort(pSort);
			}
			
			List<Object[]> liResult = new ArrayUtil<Object[]>();

			for (int i = (pFromRow <= 0 ? 0 : pFromRow), anz = book.getRowCount(); i < anz; i++)
			{
				liResult.add(book.getDataRow(i).getValues(pQueryColumns));
			}
			
			liResult.add(null);
			
			return liResult;
		}
		catch (ModelException me)
		{
			throw new DataSourceException("Error fetching data!", me);
		}
	}
	
	/**
	 * Refetches data.
	 * 
	 * @param pQueryColumns the query columns
	 * @param pDataRow the old data
	 * @return the new data
	 * @throws DataSourceException if refetch fails
	 */
	protected Object[] executeRefetchRow(String[] pQueryColumns, Object[] pDataRow) throws DataSourceException
	{	
		try
		{
			ICondition condPK = Filter.createEqualsFilter(metadata.getPrimaryKeyColumnNames(), pDataRow, metadata.getColumnMetaData());
			
			executeFetch(condPK, null, 0, 2);
			
			searchRow(pQueryColumns, pDataRow);
	
			return book.getValues(pQueryColumns);
		}
		catch (ModelException me)
		{
			try
			{
				book.restoreSelectedRow();
			}
			catch (ModelException mex)
			{
				error(me);
				
				throw new DataSourceException("Refetch failed!", mex);
			}
	
			throw new DataSourceException("Refetch failed!", me);
		}
	}
	
	/**
	 * Inserts data.
	 * 
	 * @param pQueryColumns the query columns
	 * @param pDataRow the data to insert
	 * @return the inserted data (includes changed values)
	 * @throws DataSourceException if insert fails
	 */
	protected Object[] executeInsert(String[] pQueryColumns, Object[] pDataRow) throws DataSourceException
	{
        if (!isOpen())
        {
            throw new DataSourceException("AbstractMemStorage isn't open!");            
        }
	    
		try
		{
			book.insert(false);
			book.setValues(pQueryColumns, pDataRow);
			book.saveSelectedRow();
			
			return book.getValues(pQueryColumns);
		}
		catch (ModelException me)
		{
			try
			{
                book.restoreSelectedRow();
			}
			catch (ModelException mex)
			{
				error(me);
				
				throw new DataSourceException("Insert failed!", mex);
			}
			
			throw new DataSourceException("Insert failed!", me);
		}
	}
	
	/**
	 * Updates data.
	 * 
	 * @param pQueryColumns the query columns
	 * @param pOldDataRow the old values
	 * @param pNewDataRow the new values
	 * @return the updated - new - values
	 * @throws DataSourceException if update fails
	 */
	protected Object[] executeUpdate(String[] pQueryColumns, Object[] pOldDataRow, Object[] pNewDataRow) throws DataSourceException
	{
        if (!isOpen())
        {
            throw new DataSourceException("AbstractMemStorage isn't open!");            
        }

        try
		{
			searchRow(pQueryColumns, pOldDataRow);

			//update only changed columns!
			IDataType dtDataType;
			
			IRowDefinition rowdef = book.getRowDefinition();
			
			ArrayUtil<String> auColumns = new ArrayUtil<String>();
			ArrayUtil<Object> auValues = new ArrayUtil<Object>();
			
			for (int i = 0; i < pNewDataRow.length; i++)
			{
				dtDataType = rowdef.getColumnDefinition(i).getDataType();
				
		        if (dtDataType.compareTo(pNewDataRow[i], pOldDataRow[i]) != 0)
		        {
		        	auColumns.add(pQueryColumns[i]);
		        	auValues.add(pNewDataRow[i]);
		        }
			}
			
			book.setValues((String[])auColumns.toArray(new String[auColumns.size()]), (Object[])auValues.toArray(new Object[auValues.size()]));
			book.saveSelectedRow();
			
			return book.getValues(pQueryColumns);
		}
		catch (ModelException me)
		{
			try
			{
		        book.restoreSelectedRow();
			}
			catch (ModelException mex)
			{
				error(me);
				
				throw new DataSourceException("Update failed!", mex);
			}

			throw new DataSourceException("Update failed!", me);
		}
	}
	
	/**
	 * Deletes data.
	 * 
	 * @param pQueryColumns the query columns
	 * @param pDeleteDataRow the data to delete
	 * @throws DataSourceException if delete fails
	 */
	protected void executeDelete(String[] pQueryColumns, Object[] pDeleteDataRow) throws DataSourceException
	{
        if (!isOpen())
        {
            throw new DataSourceException("AbstractMemStorage isn't open!");            
        }

        try
		{
            searchRow(pQueryColumns, pDeleteDataRow);

			book.delete();
			
			book.saveSelectedRow();
		}
		catch (ModelException me)
		{
			throw new DataSourceException("Delete failed!", me);
		}
	}
	
	/**
	 * Searches a row specified with its values.
	 * 
	 * @param pColumnNames the column names
	 * @param pValues the row values
	 * @throws ModelException if the row search from the {@link MemDataBook} failed
	 */
	private void searchRow(String[] pColumnNames, Object[] pValues) throws ModelException
	{
		rowSearch.setValues(pColumnNames, pValues);
		
		int iRow = book.searchNext(condSearch);
		
		//not found??? (depends on the implementation)
		//-> we try to refetch because we are stateless
		if (iRow < 0)
		{
			bIgnoreEvents = true;
			
			try
			{
				loadData(book, condSearch);
				book.saveAllRows();
			}
			finally
			{
				bIgnoreEvents = false;
			}
			
			iRow = book.searchNext(condSearch);
		}
		
		if (iRow >= 0)
		{
			book.setSelectedRow(iRow);
		}
		else
		{
			throw new DataSourceException("Row was not found!");
		}
	}
	
	/**
	 * The insert notification method from the data book. It ignores
	 * events which occured during {@link #loadData(MemDataBook, ICondition)}.
	 * 
	 * @param pEvent the data book event
	 * @throws ModelException if an insert error occurs
	 */
	public void doInsert(DataBookEvent pEvent) throws ModelException
	{
		if (!bIgnoreEvents)
		{
			insert(pEvent);
		}
	}

	/**
	 * The update notification method from the data book. It ignores
	 * events which occured during {@link #loadData(MemDataBook, ICondition)}.
	 * 
	 * @param pEvent the data book event
	 * @throws ModelException if an update error occurs
	 */
	public void doUpdate(DataBookEvent pEvent) throws ModelException
	{
		if (!bIgnoreEvents)
		{
			update(pEvent);
		}
	}

	/**
	 * The delete notification method from the data book. It ignores
	 * events which occured during {@link #loadData(MemDataBook, ICondition)}.
	 * 
	 * @param pEvent the data book event
	 * @throws ModelException if a delete error occurs
	 */
	public void doDelete(DataBookEvent pEvent) throws ModelException
	{
		if (!bIgnoreEvents)
		{
			delete(pEvent);
		}
	}

	/**
	 * Validates the current record with metadata restrictions.
	 * 
	 * @param pEvent the data book event
	 * @throws ModelException if validation fails
	 */
    public void doValidate(DataBookEvent pEvent) throws ModelException
    {
        if (!bIgnoreEvents)
        {
            validateWithMetaData();
        }
    }
	
    /**
     * Validates all columns.
     * 
     * @throws ModelException if validation fails
     * @see #validateWithMetaData(String[], Object[])
     */
    protected void validateWithMetaData() throws ModelException
    {
        IRowDefinition rowdef = book.getRowDefinition();
        
        String[] sCols = rowdef.getColumnNames();
        Object[] oValues = book.getValues(sCols);
        
        validateWithMetaData(sCols, oValues);        
    }

    /**
	 * Validates the given row with the meta data. The validation checks if all
	 * columns fulfill the meta data restrictions, like nullable.
	 * 
	 * @param pColumns the columns to check
	 * @param pValues the column array ordered in the visible columns order
	 * @throws ModelException if validation fails
	 */
	protected void validateWithMetaData(String[] pColumns, Object[] pValues) throws ModelException
	{
		IRowDefinition rowdef = book.getRowDefinition();
		
		ColumnDefinition coldef;
	
		for (int i = 0; i < pColumns.length; i++)
		{
			coldef = rowdef.getColumnDefinition(pColumns[i]);
			
			if (!coldef.isNullable() && pValues[i] == null)
			{
				throw new ModelException("Column '" + pColumns[i] + "' can not be null!");
			}
		}
	}
	
	/**
	 * Gets the internal {@link MemDataBook}.
	 * 
	 * @return the data book
	 */
	public MemDataBook getDataBook()
	{
		return book;
	}

	/**
	 * Sets the default sort definition. It will be used when no sort definition is defined through the
	 * client fetch call.
	 * 
	 * @param pSort the default sort definition
	 */
	public void setDefaultSort(SortDefinition pSort)
	{
		sortDefault = pSort;
	}
	
	/**
	 * Gets the default sort definition.
	 * 
	 * @return the default sort definition
	 * @see #setDefaultSort(SortDefinition)
	 */
	public SortDefinition getDefaultSort()
	{
		return sortDefault;
	}
	
	/**
	 * Returns the restrict condition. It is always added with And to any given condition.
	 *
	 * @return the restrict condition.
	 */
	public ICondition getRestrictCondition()
	{
		return condRestrict;
	}

	/**
	 * Sets the restrict condition. It is always added with And to any given condition.
	 *
	 * @param pCondition the restrict condition.
	 */
	public void setRestrictCondition(ICondition pCondition)
	{
		condRestrict = pCondition;
	}	
	
	/**
	 * Creates the filter based on the {@link #condRestrict}
	 * and the given {@link ICondition}.
	 * 
	 * @param pFilter the additional {@link ICondition}.
	 * @return the filter.
	 */
	protected ICondition createFilter(ICondition pFilter)
	{
		ICondition result = null;
		
		if (condRestrict != null)
		{
		    result = new And();
		    result.and(condRestrict);
		}
		
		if (pFilter != null)
		{
			checkCondition(pFilter);
			
		    if (result == null)
		    {
		    	//very important to wrap into And. Without And, the "original" condition could be changed
		        result = new And(pFilter);
		    }
		    else
		    {
		    	result.and(new And(pFilter));
		    }
		}
		
		return result;
	}
	
	/**
	 * Gets the row count.
	 * 
	 * @return the row count
	 * @throws ModelException if data access failed
	 */
	public int getRowCount() throws ModelException
	{
		return book.getRowCount();
	}

}	// AbstractMemStorage
