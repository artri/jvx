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
 * 24.10.2008 - [RH] - creation
 * 11.11.2008 - [RH] - use getMasterRow in getCompleteFilter()
 * 10.04.2009 - [RH] - interface review - Max millis for fetch removed in client side.
 *                                        java doc added.
 * 18.04.2009 - [RH] - javadoc reviewed, NLS removed.
 * 30.04.2009 - [RH] - use of IStorage over an AbstractConnection implemented  
 * 16.10.2009 - [RH] - fetchAll in one request implemented -> fetchToRow(-1)=fetchAll
 * 27.03.2010 - [JR] - #103: fetchToRow: caching keys as parameters for the fetch call
 * 17.06.2010 - [JR] - #135: don't send filter to the server if mem sort is enabled
 * 07.10.2010 - [JR] - #117: cache storage fallback to storage without cache
 * 15.02.2011 - [JR] - #287: removed fetch with cache information
 * 17.02.2011 - [RH] - #290: if the master DataBook is in DATASOURCE level && Inserting() no fetch is necessary, because no rows can exists!
 * 30.05.2011 - [HM] - #374 - If an insert happens, after that more rows have to be fetched, then wrong rows will be fetched
 * 10.02.2012 - [JR] - #546: getCompleteFilter: clone existing filter
 */
package com.sibvisions.rad.model.remote;

import java.util.List;

import javax.rad.model.IDataRow;
import javax.rad.model.ModelException;
import javax.rad.model.SortDefinition;
import javax.rad.model.condition.Equals;
import javax.rad.model.condition.ICondition;
import javax.rad.model.reference.ReferenceDefinition;

import com.sibvisions.rad.model.mem.MemDataBook;
import com.sibvisions.rad.model.mem.MemDataPage;
import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.Internalize;
import com.sibvisions.util.log.ILogger;
import com.sibvisions.util.log.LoggerFactory;

/**
 * A <code>RemoteDataPage</code> extends the <code>MemDataPage</code> with storage operations.
 * It fetches the data rows and the count of rows from the IDBAccess.
 * 
 * @see javax.rad.model.IDataBook
 * @see javax.rad.model.IDataPage
 * @see javax.rad.model.IChangeableDataRow
 * @author Roland Hörmann
 */
public class RemoteDataPage extends MemDataPage
{	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The logger. */
	private static ILogger logger = LoggerFactory.getInstance(RemoteDataPage.class);
	/** The amount of already fetched rows. */
	private int fetchedRows = 0;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Constructs a new RemoteDataPage for the specified IDataBook and the corresponding 
	 * master row. 
	 * 
	 * @param pDataBook 		the IDataBook which uses this MemDataPage 
	 * @param pMasterDataRow	the corresponding master row of the master IDataBook of the 
	 *                          above specified IDataBook 
	 */
	public RemoteDataPage(MemDataBook pDataBook, IDataRow pMasterDataRow)
	{
		super(pDataBook, pMasterDataRow);
		setAllFetched(!getDataBook().isFetchEnabled());
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
	/**
	 * {@inheritDoc}
	 */
	@Override
	public RemoteDataBook getDataBook()
	{
		return (RemoteDataBook)super.getDataBook();
	}
	
	/**
	 * It adds an new IDataRow to the DataPage in the object[] storage.
	 * 
	 * @param pValues			the values.
	 * @throws ModelException	if the IDataRow couldn't add to the storage.
	 */
	protected void addFetchedRow(Object[] pValues) throws ModelException
	{
		super.addFetchedRow(pValues);
		fetchedRows++;
	}

	/**
	 * It tries to fetches to the specified row index in the storage.
	 *  
	 * @param pRowIndex		the row index to use.
	 * @throws ModelException	if an error occur during storage interaction.
	 */
	@Override
	public void fetchToRow(int pRowIndex) throws ModelException
	{
    	synchronized (rootDataBook)
    	{
			if (pRowIndex < getRowCountInternal() && pRowIndex >= 0)
			{
				return;
			}
	
			if (!isAllFetched())
			{
				RemoteDataBook rdb = getDataBook();
				
				// #290- if the master DataBook is in DATASOURCE level && isInserting() no fetch is necessary, because no rows can exists!
				// moved to getDataPageIntern, because the detail page hasn't to be the corresponding to the current selected master
				// e.g. getDataPage, Tree, ...
				try
				{		
					String sRdbName = rdb.getName();
					
					int columnCount = rdRowDefinition.getColumnCount();
					int[] fetchColumnIndexes = rdb.getFetchColumnIndexes();
					int[] masterColumnIndexes = rdb.getMasterColumnIndexesForBlock();
					String[] masterColumns;
					IDataRow masterDataRow;
					int fetchedRowsForBlock;
					
					if (masterColumnIndexes != null)
					{
						masterDataRow = getMasterDataRow().createEmptyDataRow(null);
						masterColumns = masterDataRow.getRowDefinition().getColumnNames();
						fetchedRowsForBlock = rdb.getFetchedRowsForBlock(getMasterDataRow());
						
						if (fetchedRowsForBlock < 0)
						{
							setAllFetched(true);
							return;
						}
					}
					else
					{
						masterDataRow = null;
						masterColumns = null;
						fetchedRowsForBlock = fetchedRows;
					}
					
					List<Object[]> lResult;
					
					do
					{
						long lMillis = System.currentTimeMillis();
						int iMinimumRowCount;
						if (pRowIndex < 0 || rdb.getReadAhead() < 0)
						{
						    iMinimumRowCount = -1;
						}
						else
						{
							iMinimumRowCount = pRowIndex + rdb.getReadAhead() - fetchedRows + 1;
						}
						// #561: column does not exist on server
						SortDefinition sort;
						if (rdb.isMemSort())
						{
							sort = null;
						}
						else
						{
							sort = rdb.getSort();
						}
						ICondition filter = getCompleteFilter();
						lResult = (List<Object[]>)rdb.getConnection().call(
								sRdbName, 
								"fetch",
								filter, 
								sort, 
								Integer.valueOf(fetchedRowsForBlock), 
								Integer.valueOf(iMinimumRowCount));
						logger.debug("acConnection.fetch(", sRdbName, ",", 
									 filter, ",", sort, ",", 
								     Integer.valueOf(fetchedRowsForBlock), ",", 
								     Integer.valueOf(iMinimumRowCount), 
								     ") in ", Long.valueOf(System.currentTimeMillis() - lMillis), "ms");
						
						for (int i = 0; i < lResult.size(); i++)
						{
							Object[] data = lResult.get(i);
							
							if (data == null)
							{
								setAllFetched(true);
							}
							else
							{
								if (data.length != fetchColumnIndexes.length)
								{
									throw new ModelException("Remote storage returned " + data.length + " value(s) but " + 
											                 fetchColumnIndexes.length + " were expected (check meta data)!");
								}
								
								Object[] values = new Object[columnCount];
								
								for (int j = 0; j < fetchColumnIndexes.length; j++)
								{
									int index = fetchColumnIndexes[j];
									// Lost internalize due to Ticket: 1643
									values[index] = Internalize.intern(rdRowDefinition.getColumnDefinition(index).getDataType().convertToTypeClass(data[j]));
								}
								
								if (masterColumnIndexes == null)
								{
									addFetchedRow(values);
								}
								else
								{
									if (masterDataRow == null)
									{
										masterDataRow = getMasterDataRow().createEmptyDataRow(null);
										masterColumns = masterDataRow.getRowDefinition().getColumnNames();
									}
									for (int j = 0; j < masterColumnIndexes.length; j++)
									{
										masterDataRow.setValue(masterColumns[j], values[masterColumnIndexes[j]]);
									}
	
									RemoteDataPage dataPage = (RemoteDataPage)rdb.getDataPage(masterDataRow);
									
									dataPage.addFetchedRow(values);
									fetchedRowsForBlock++;
								}
							}
						}
					}
					while (masterColumnIndexes != null && !isAllFetched() && (pRowIndex < 0 || pRowIndex >= getRowCountInternal()));
	
					if (masterColumnIndexes != null)
					{
						if (isAllFetched())
						{
							rdb.setFetchedRowsForBlock(getMasterDataRow(), -1);
						}
						else
						{
							rdb.setFetchedRowsForBlock(getMasterDataRow(), fetchedRowsForBlock);
						}
					}
				}
				catch (Throwable throwable)
				{
					setAllFetched(true);
	
					throw new ModelException("Fetch to Row failed!", throwable);
				}
				finally
				{
				    rdb.notifyRepaintControls();
				}
			}
    	}
	}	
	
	/**
	 * Returns the count of rows in the storage for this RemoteDataPage in the RemoteDataBook.
	 * 
	 * @return the count of rows in the storage for this RemoteDataPage in the RemoteDataBook.
	 * @throws ModelException if an error occur during storage interaction. 
	 */
	@Override
	public int getEstimatedRowCount() throws ModelException
	{
		try
		{
			long lMillis = System.currentTimeMillis();
			
			RemoteDataBook rdb = getDataBook();
			
			Integer iResult = (Integer)rdb.getConnection().call(rdb.getName(), "getEstimatedRowCount", getCompleteFilter());
				
			logger.debug("acConnection.getEstimatedRowCount(", rdb.getName(), ",", 
					     getCompleteFilter(), ") in ",
					     Long.valueOf(System.currentTimeMillis() - lMillis), "ms");
			
			return iResult.intValue();			
		}
		catch (Throwable throwable)
		{
			throw new ModelException("getEstimatedRowCount failed!", throwable);
		}		
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
	
	/**
	 * Returns the complete filter - getFilter() + MasterReference() - for this RemoteDataPage.
	 * This is used to fetch only the data for this RemoteDataPage.  
	 * 
	 * @return the complete filter - getFilter() + MasterReference() - for this RemoteDataPage.
	 * @throws ModelException	if an error occur during constructing the filter.
	 */
	private ICondition getCompleteFilter() throws ModelException
	{
		ICondition cCompleteFilter;
		
		RemoteDataBook book = getDataBook();
		
		//#135
		if (book.isMemFilter())
		{
			cCompleteFilter = null;
		}
		else
		{
			cCompleteFilter = book.getFilter();
			
			//#546
			if (cCompleteFilter != null)
			{
				cCompleteFilter = cCompleteFilter.clone();
			}
		}

		ReferenceDefinition rdToMasterDataBook = book.getMasterReference();

		if (rdToMasterDataBook != null)
		{
			String[] saSourceColumnNames = rdToMasterDataBook.getColumnNames();
			String[] saTargetColumnNames = rdToMasterDataBook.getReferencedColumnNames();
			String[] saBlockFetchColumns = book.getBlockFetchColumnNames();
				
			IDataRow drMasterRow = getMasterDataRow();
			
			for (int i = 0; i < saSourceColumnNames.length; i++)
			{
				if (saBlockFetchColumns == null || ArrayUtil.indexOf(saBlockFetchColumns, saSourceColumnNames[i]) >= 0)
				{
					Equals cCondition = new Equals(saSourceColumnNames[i], 
							                       drMasterRow.getValue(saTargetColumnNames[i]), false);
					if (cCompleteFilter == null)
					{
						cCompleteFilter = cCondition;
					}
					else
					{
						cCompleteFilter = cCompleteFilter.and(cCondition);
					}
				}
			}
		}
		return cCompleteFilter;
	}	
	
} // RemoteDataPage
