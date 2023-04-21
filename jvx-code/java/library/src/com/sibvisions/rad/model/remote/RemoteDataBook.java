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
 * 12.10.2008 - [RH] - creation
 * 11.11.2008 - [RH] - delete details before master; insert master before details added
 * 28.04.2009 - [RH] - interface reviewed, javadoc review, NLS removed 
 * 30.04.2009 - [RH] - use of IStorage over an AbstractConnection implemented  
 * 04.05.2009 - [RH] - getClientColumnMetaData() added for CSV Export
 * 07.05.2009 - [RH] - AbstractConnection moved to RemoteDataSource
 * 05.10.2009 - [JR] - memSort, memFilter: methods made public
 * 23.11.2009 - [RH] - ColumnMetaData is replaced with the MetaData class
 * 04.12.2009 - [RH] - setWritebackEnabled(false) if no PrimaryKey columns found on server
 * 28.12.2009 - [JR] - open: catch exception when an exception occurs with auto link columns (no UI mode)
 * 22.02.2010 - [JR] - #67: open: concat name and subStorages to access the sub storage
 * 06.03.2010 - [JR] - getClientMetaData -> getMetaData, getClientColumnMetaData -> getColumnMetaData
 * 25.03.2010 - [JR] - #103: initServerMetaData: retrieve cached metadata and set them into the datasource
 *                   - #92: createNewRow: set default values from metadata  
 * 28.03.2010 - [JR] - #47: open: allowed value check and automatic choice cell editor support
 * 07.10.2010 - [JR] - #117: cache storage fall back to storage without cache
 * 23.02.2011 - [RH] - #199: if also a MasterReference is set over the same columns, then we set this column invisible default.
 *                           over setAutoLinkInvisibleForMasterReferences() can this features be disabled.
 * 06.03.2011 - [JR] - #115: 
 *                     * open: moved allowed values to MemDataBook
 *                     * createNewRow: moved default value to MemDataBook 
 * 09.06.2011 - [RH] - #387 - RemoteDataBook open doesn't check if the name is null
 * 19.08.2011 - [JR] - #459: setMetaDataCacheEnabled
 */
package com.sibvisions.rad.model.remote;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map.Entry;

import javax.rad.genui.UIFactoryManager;
import javax.rad.genui.celleditor.UILinkedCellEditor;
import javax.rad.model.ColumnDefinition;
import javax.rad.model.ColumnView;
import javax.rad.model.IChangeableDataRow;
import javax.rad.model.IDataBook;
import javax.rad.model.IDataRow;
import javax.rad.model.IDataSource;
import javax.rad.model.MetaDataCacheOption;
import javax.rad.model.ModelException;
import javax.rad.model.RowDefinition;
import javax.rad.model.datatype.BinaryDataType;
import javax.rad.model.datatype.StringDataType;
import javax.rad.model.reference.ColumnMapping;
import javax.rad.model.reference.ReferenceDefinition;
import javax.rad.model.reference.StorageReferenceDefinition;
import javax.rad.persist.ColumnMetaData;
import javax.rad.persist.DataSourceException;
import javax.rad.persist.MetaData;
import javax.rad.persist.MetaData.Feature;
import javax.rad.remote.AbstractConnection;
import javax.rad.ui.celleditor.ILinkedCellEditor;

import com.sibvisions.rad.model.mem.MemDataBook;
import com.sibvisions.rad.model.mem.MemDataPage;
import com.sibvisions.rad.model.remote.RemoteDataSource.MetaDataCacheRole;
import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.log.ILogger;
import com.sibvisions.util.log.ILogger.LogLevel;
import com.sibvisions.util.log.LoggerFactory;

/**
 * The <code>RemoteDataBook</code> is a storage independent table, and handles all operations
 * based on the the MemDatabook base class.
 * It communicates to the IStorage and uses the storage methods to fetch, insert
 * update, delete the data on server.
 * 
 * @see com.sibvisions.rad.model.mem.MemDataBook
 * @see javax.rad.model.IDataPage
 * @see javax.rad.model.IDataBook
 * @see javax.rad.model.IRowDefinition
 * @see javax.rad.model.IChangeableDataRow
 * 
 * @author Roland Hörmann
 */
public class RemoteDataBook extends MemDataBook
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The logger for protocol the performance. */
	private static ILogger logger = LoggerFactory.getInstance(RemoteDataBook.class);

	/** the cache for fast access to caching storages. */
	private static Hashtable<String, Boolean> htCachingStorage = new Hashtable<String, Boolean>();
	
	/** It holds the column names of the server column meta data in the client RowDefinition. */
	private transient String[] saMetaDataColumnNames;
	
	/** It holds the column names of the server column meta data in the client RowDefinition. */
	private transient String[] saBlockFetchColumnNames;
	
	/** It holds the column names of the server column meta data in the client RowDefinition. */
	private transient String[] saBlockFetchReferencedColumnNames;
	
	/** stores the block fetches. */
	private transient Hashtable<Object, Integer> htFetchedRowsForBlock = null;
	
	/** The meta data for this RemoteDataBook. */
	private transient MetaData mdMetaData;
	
	/** The correct column indexes of the fetched data, after opening the data book. */
	private transient int[] iaFetchColumnIndexes;

	/** The correct column indexes of the master reference. */
	private transient int[] iaMasterColumnIndexes;

	/** Indicates whether the server object is a caching storage. */
	private transient boolean bCachingStorage = true;

	/** whether the meta data cache is enabled (default: <code>true</code>). */
	private transient boolean bMetaDataCacheEnabled = true;

	/** If fetch is disabled, no fetch call to server is done. This is for performance tuning reasons. */
	private transient boolean bFetchEnabled = true;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>RemoteDataBook</code>.
	 */
	public RemoteDataBook()
	{
		super();
		
		setMemSort(false);
		setMemFilter(false);
		setDeleteCascade(false);
		setWritebackEnabled(true);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean isDataPageRefetchPossible()
	{
		return saBlockFetchColumnNames == null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected MemDataPage createDataPage(IDataRow pMasterDataRow)
	{
		return new RemoteDataPage(this, pMasterDataRow);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDataSource(IDataSource pDataSource) throws ModelException
	{
    	synchronized (rootDataBook)
    	{
			if (!(pDataSource instanceof RemoteDataSource))
			{
				throw new ModelException("It's not an RemoteDataSource! -> it has to be one!");
			}
			
			super.setDataSource(pDataSource);
    	}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public RemoteDataSource getDataSource()
	{
		return (RemoteDataSource)super.getDataSource();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void open() throws ModelException
	{
    	synchronized (rootDataBook)
    	{
			if (!isOpen())
			{
				if (getConnection() == null)
				{
					throw new ModelException("The remote connection to the server IStorage is null!");
				}			
	
				// #387 - RemoteDataBook open doesn't check if the name is null 
				if (getName() == null)
				{
					throw new ModelException("DataBook Name is null!");				
				}
	
				try 
				{
					initServerMetaData();
				}
				catch (Exception exception)
				{
					throw new ModelException("RowDefintion can't init with meta data from storage!", exception);
				}
	
				if (mdMetaData != null)
				{
					//Features
	
					if (!mdMetaData.isSupported(Feature.Filter))
					{
						setMemFilter(true);
					}
					
					if (!mdMetaData.isSupported(Feature.Sort))
					{
						setMemSort(true);
					}
	
					if (!mdMetaData.isSupported(Feature.WriteBack))
					{
						setWritebackEnabled(false);
					}
					
					//Columns
					
					ColumnMetaData[] cmds = mdMetaData.getColumnMetaData();
					saMetaDataColumnNames = new String[cmds.length];
					
					if (rdRowDefinition == null)
					{
						rdRowDefinition = new RowDefinition();
					}
	
					HashMap<String, RemoteDataBook> linkDataBooks = new HashMap<String, RemoteDataBook>();
					
					boolean useGlobalDataBookForLinkedCellEditor = 
						   RemoteDataSource.getMetaDataCacheRole() != MetaDataCacheRole.Off
						&& getDataSource().getMetaDataCacheOption() != MetaDataCacheOption.Off;
					
					for (int i = 0; i < cmds.length; i++)
					{
					    ColumnMetaData cmd = cmds[i];
					    ColumnDefinition coldef;
					    
						if (rdRowDefinition.getColumnDefinitionIndex(cmd.getName()) < 0)
						{
						    coldef = cmd.createColumnDefinition();
							rdRowDefinition.addColumnDefinition(coldef);
							
							if (UIFactoryManager.getFactory() != null)
							{
								StorageReferenceDefinition linkReference = cmd.getLinkReference();
								if (linkReference != null
								        && !(coldef.getDataType() instanceof BinaryDataType))
								{
									RemoteDataBook rdbReferencedDataBook = getDataBookForLinkedCellEditor(linkReference, linkDataBooks, useGlobalDataBookForLinkedCellEditor);
	
									ColumnView visibleCols = new ColumnView();
									for (String columnName : linkReference.getReferencedColumnNames())
									{
										if (visibleCols.getColumnNameIndex(columnName) < 0
												&& !RowDefinition.isColumnIgnored(columnName)
												&& rdbReferencedDataBook.getRowDefinition().getColumnDefinition(columnName).getDataType().getTypeIdentifier() 
												    != BinaryDataType.TYPE_IDENTIFIER)
										{
											visibleCols.addColumnNames(columnName);
										}
									}
									boolean hasVisibleColumnsFromServer = visibleCols.getColumnCount() > 0;
									if (!hasVisibleColumnsFromServer)
									{
										String[] repCols = rdbReferencedDataBook.mdMetaData.getRepresentationColumnNames();
										
										if (repCols != null)
										{
										    for (String columnName : repCols)
                                            {
                                                if (visibleCols.getColumnNameIndex(columnName) < 0
                                                        && !RowDefinition.isColumnIgnored(columnName)
                                                        && rdbReferencedDataBook.getRowDefinition().getColumnDefinition(columnName).getDataType().getTypeIdentifier() 
                                                        != BinaryDataType.TYPE_IDENTIFIER)
                                                {
                                                    visibleCols.addColumnNames(columnName);
                                                }
                                            }
										}
									}
									
									ReferenceDefinition referenceDefinition = new ReferenceDefinition();
									referenceDefinition.setReferencedDataBook(rdbReferencedDataBook);
									referenceDefinition.setReferencedColumnNames(linkReference.getReferencedColumnNames());
									referenceDefinition.setColumnNames(linkReference.getColumnNames());
									
									String referencedColumnName = referenceDefinition.getReferencedColumnName(coldef.getName());
									String displayConcatMask = null;
									if (visibleCols.getColumnNameIndex(referencedColumnName) < 0)
									{
									    if (hasVisibleColumnsFromServer && linkReference.getColumnNames().length < linkReference.getReferencedColumnNames().length)
									    {
									        displayConcatMask = " ";
									    }
									    else if (rdbReferencedDataBook.getRowDefinition().getColumnDefinition(referencedColumnName).getDataType().getTypeIdentifier()
	                                            != BinaryDataType.TYPE_IDENTIFIER)
									    {
									        visibleCols.addColumnNames(0, referencedColumnName);
									    }
									}
									
									UILinkedCellEditor linkedCellEditor = new UILinkedCellEditor();
									linkedCellEditor.setLinkReference(referenceDefinition);
									linkedCellEditor.setDefaultColumnView(visibleCols);
									linkedCellEditor.setDisplayConcatMask(displayConcatMask);
									
									linkedCellEditor.setValidationEnabled(
											   !(coldef.getDataType() instanceof StringDataType)
											|| ArrayUtil.contains(rdbReferencedDataBook.getRowDefinition().getPrimaryKeyColumnNames(), 
													referencedColumnName)
											|| !cmd.isWritable());
									
									ArrayUtil<String> searchColumns = new ArrayUtil<String>();
									ArrayUtil<String> searchReferencedColumns = new ArrayUtil<String>();
									
									// #900 - includedSearchColumns for overlapping LinkCellEditors
									String[] currColumns = referenceDefinition.getColumnNames();
									for (ColumnMetaData scmd : cmds)
									{
										StorageReferenceDefinition srd = scmd.getLinkReference();
										if (srd != null)
										{
											String[] subColumns = srd.getColumnNames();
											
											if (subColumns.length < currColumns.length 
													&& ArrayUtil.containsAll(currColumns, subColumns))
											{
												for (String subColumn : subColumns)
												{
													if (!searchColumns.contains(subColumn))
													{
														searchColumns.add(subColumn);
														searchReferencedColumns.add(referenceDefinition.getReferencedColumnName(subColumn));
													}
												}
											}
										}
								    }
									if (searchColumns.size() > 0)
									{
										linkedCellEditor.setSearchColumnMapping(
												new ColumnMapping(
														searchColumns.toArray(new String[searchColumns.size()]), 
														searchReferencedColumns.toArray(new String[searchReferencedColumns.size()])));
									}
									
									coldef.getDataType().setDefaultCellEditor(linkedCellEditor);
								}
							}
						}
						else
						{
						    coldef = rdRowDefinition.getColumnDefinition(cmd.getName());
						    
					        // All server columns are writeable from the view of a client. Thats because this columns are written to the server.
					        // If the server writes this changes to the storage depend on the writable state of the column from the server and
					        // not from the client.
						    coldef.setWritable(true);
					        
					        // Initialize filterable and sortable
					        // - Calculated columns cannot be filtered and sorted
					        // - Binary types cannot be filtered and sorted
					        // - Clobs cannot be filtered and sorted, but we cannot check it here, these columns has to be disabled manually.
					        boolean filterAndSortable = !cmd.isCalculated() 
					                && cmd.getTypeIdentifier() != BinaryDataType.TYPE_IDENTIFIER;
					        
					        coldef.setFilterable(filterAndSortable);
					        coldef.setSortable(filterAndSortable);
						}
						saMetaDataColumnNames[i] = cmd.getName();
					}
					
					iaFetchColumnIndexes = new int[cmds.length];
					for (int i = 0; i < cmds.length; i++)
					{
						iaFetchColumnIndexes[i] = rdRowDefinition.getColumnDefinitionIndex(cmds[i].getName());
					}
					
					if (rdRowDefinition.getPrimaryKeyColumnNames() == null)
					{
						String[] saPKs = mdMetaData.getPrimaryKeyColumnNames();
						if (saPKs == null)
						{
							setWritebackEnabled(false);
						}
						else
						{
							rdRowDefinition.setPrimaryKeyColumnNames(saPKs);						
						}
					}
				}
				if (saBlockFetchColumnNames == null)
				{
					iaMasterColumnIndexes = null;
					saBlockFetchReferencedColumnNames = null;
				}
				else if (getMasterReference() == null)
				{
					throw new ModelException("Block fetch is only allowed if a master reference is set!");
				}
				else if (ArrayUtil.containsAll(getMasterReference().getColumnNames(), saBlockFetchColumnNames))
				{
					String[] masterColumnNames = getMasterReference().getColumnNames();
					String[] masterReferencedColumnNames = getMasterReference().getReferencedColumnNames();
					iaMasterColumnIndexes = new int[masterColumnNames.length];
					
					for (int i = 0; i < iaMasterColumnIndexes.length; i++)
					{
						iaMasterColumnIndexes[i] = rdRowDefinition.getColumnDefinitionIndex(masterColumnNames[i]);
					}
	
					saBlockFetchReferencedColumnNames = new String[saBlockFetchColumnNames.length];
					for (int i = 0; i < saBlockFetchColumnNames.length; i++)
					{
						saBlockFetchReferencedColumnNames[i] = masterReferencedColumnNames[ArrayUtil.indexOf(masterColumnNames, saBlockFetchColumnNames[i])];
					}
				}
				else
				{
					throw new ModelException("Block fetch have to be a subset of the master reference columns!");
				}
				super.open();
			}
    	}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close()
	{
    	synchronized (rootDataBook)
    	{
			if (isOpen())
			{
				htFetchedRowsForBlock = null;

                super.close();
			}
    	}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected IDataRow executeLockAndRefetch(IChangeableDataRow pDataRow) throws ModelException
	{
		try 
		{	
			long lMillis = System.currentTimeMillis();
			
			Object[] values;
			String[] pkColumns = mdMetaData.getPrimaryKeyColumnNames();
			if (pkColumns == null)
			{
				values = pDataRow.getValues(saMetaDataColumnNames);
			}
			else
			{
				values = new Object[mdMetaData.getColumnMetaDataCount()];
				
				for (String pkColumn : pkColumns)
				{
					values[mdMetaData.getColumnMetaDataIndex(pkColumn)] = pDataRow.getValue(pkColumn);
				}
			}
			
			Object[] oResult = (Object[])getConnection().call(getName(), "refetchRow", values);
				
			if (logger.isEnabled(LogLevel.DEBUG))
			{
				logger.debug("acConnection.refetchRow(", getName(), ",", values, ") in ",
							 Long.valueOf(System.currentTimeMillis() - lMillis), "ms");
			}
				
			if (oResult != null)
 			{
				IDataRow result = pDataRow.createDataRow(null);
				
				result.setValues(saMetaDataColumnNames, oResult);
				
				return result;
 			}
			
			return null;
		}
		catch (Throwable throwable)
		{
			throw new ModelException("Execute Lock and Refetch failed!", throwable);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected IDataRow executeInsert(IChangeableDataRow pDataRow) throws ModelException
	{
		try 
		{
			long lMillis = System.currentTimeMillis();
			
			Object[] oResult = (Object[])getConnection().call(getName(), "insert", pDataRow.getValues(saMetaDataColumnNames));
				
			if (logger.isEnabled(LogLevel.DEBUG))
			{
				logger.debug("acConnection.insert(", getName(), ",", pDataRow.getValues(saMetaDataColumnNames), ") in ",
							 Long.valueOf(System.currentTimeMillis() - lMillis), "ms");
			}

 			if (oResult != null)
 			{
				IDataRow result = pDataRow.createDataRow(null);
				
				result.setValues(saMetaDataColumnNames, oResult);
				
				return result;
 			}
 			return null;
		}
		catch (Throwable throwable)
		{
			throw new ModelException("Execute Insert failed!", throwable);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected IDataRow executeUpdate(IChangeableDataRow pDataRow) throws ModelException
	{
		try 
		{
			long lMillis = System.currentTimeMillis();
			
			Object[] oResult = (Object[])getConnection().call(getName(), 
					                                          "update", 
					                                          pDataRow.getOriginalDataRow().getValues(saMetaDataColumnNames), 
					                                          pDataRow.getValues(saMetaDataColumnNames));
				
			if (logger.isEnabled(LogLevel.DEBUG))
			{
				logger.debug("acConnection.update(", getName(), ",", pDataRow.getOriginalDataRow().getValues(saMetaDataColumnNames), ",",
						pDataRow.getValues(saMetaDataColumnNames), ") in ",
						Long.valueOf(System.currentTimeMillis() - lMillis), "ms");
			}
			
 			if (oResult != null)
 			{
				IDataRow result = pDataRow.createDataRow(null);
				
				result.setValues(saMetaDataColumnNames, oResult);
				
				return result;
 			}
 			return null;
		}
		catch (Throwable throwable)
		{
			throw new ModelException("Execute Update failed!", throwable);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void executeDelete(IChangeableDataRow pDataRow) throws ModelException
	{
		try
		{
			long lMillis = System.currentTimeMillis();
			
			getConnection().call(getName(), "delete", pDataRow.getOriginalDataRow().getValues(saMetaDataColumnNames));
				
			if (logger.isEnabled(LogLevel.DEBUG))
			{
				logger.debug("acConnection.delete(", getName(), ",", pDataRow.getOriginalDataRow().getValues(saMetaDataColumnNames), ") in ",
							 Long.valueOf(System.currentTimeMillis() - lMillis), "ms");
			}
		}
		catch (Throwable throwable)
		{
			throw new ModelException("Execute Delete failed!", throwable);
		}				
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void executeRefresh()  throws ModelException
	{
		super.clear();
		
		htFetchedRowsForBlock = null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void executeRefreshDataPage()  throws ModelException
	{
		clearCurrentDataPage();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setMemSort(boolean pMemSort)
	{
    	synchronized (rootDataBook)
    	{
			if (isOpen() && !mdMetaData.isSupported(Feature.Sort))
			{
				return;
			}
			
			super.setMemSort(pMemSort);
    	}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isMemSort()
	{
		//changed method visibility to public
		
		return super.isMemSort();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setMemFilter(boolean pMemFilter)
	{
    	synchronized (rootDataBook)
    	{
			if (isOpen() && !mdMetaData.isSupported(Feature.Filter))
			{
				return;
			}
			
			super.setMemFilter(pMemFilter);
    	}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isMemFilter()
	{
		//changed method visibility to public
		
		return super.isMemFilter();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setWritebackEnabled(boolean pWritebackEnabled)
	{
    	synchronized (rootDataBook)
    	{
			if (isOpen() && !mdMetaData.isSupported(Feature.WriteBack))
			{
				return;
			}
	
			super.setWritebackEnabled(pWritebackEnabled);
    	}
	}	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isWritebackEnabled()
	{
		//changed method visibility to public

		return super.isWritebackEnabled();
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
	
	/**
	 * Searches for an existing RemoteDataBook, that can be used for the linked cell editor.
	 * @param pLinkReference the link reference.
	 * @param pLocalReferencedDataBooks the local generated data books.
	 * @param pUseGlobalDataBooks true, if global remote data books should be used.
	 * @return RemoteDataBook that can be used.
	 * @throws ModelException if it fails.
	 */
	private RemoteDataBook getDataBookForLinkedCellEditor(
			StorageReferenceDefinition pLinkReference,
			HashMap<String, RemoteDataBook> pLocalReferencedDataBooks,
			boolean pUseGlobalDataBooks) throws ModelException
	{
		String referencedStorageName = pLinkReference.getReferencedStorage();
		String fullStorageName;
		if (referencedStorageName.startsWith("."))
		{
			fullStorageName = getName() + referencedStorageName;
		}
		else
		{
			fullStorageName = referencedStorageName;
		}
		
		IDataBook[] dataBooks = getDataSource().getDataBooks();
		
		RemoteDataBook existingDataBook = pLocalReferencedDataBooks.get(referencedStorageName);

		if (existingDataBook == null)
		{
			if (pUseGlobalDataBooks)
			{
				for (int i = 0; existingDataBook == null && i < dataBooks.length; i++)
				{
					IDataBook dataBook = dataBooks[i];
					if (dataBook instanceof RemoteDataBook && dataBook.getName().equals(fullStorageName))
					{
						existingDataBook = (RemoteDataBook)dataBook;
					}
				}
			}
			
			if (existingDataBook == null)
			{
				existingDataBook = new RemoteDataBook();
				existingDataBook.setName(fullStorageName);
				existingDataBook.setDataSource(getDataSource());
				existingDataBook.setSelectionMode(SelectionMode.DESELECTED);
				//mark as special book
				existingDataBook.putObject(ILinkedCellEditor.class.getName(), Boolean.TRUE);
				existingDataBook.open();
			}
			pLocalReferencedDataBooks.put(referencedStorageName, existingDataBook);
		}
		
		return existingDataBook;
	}
	
	/**
	 * This can be used for performance tuning.
	 * If false, no fetch will be called on server. All DataPages will be empty.
	 * After enable fetching again, the data book has to be reloaded.
	 * 
	 * @return true, if calling fetch on server is enabled
	 */
	public boolean isFetchEnabled()
	{
		return bFetchEnabled;
	}
	
	/**
	 * This can be used for performance tuning.
	 * If false, no fetch will be called on server. All DataPages will be empty.
	 * After enable fetching again, the data book has to be reloaded.
	 * 
	 * @param pFetchEnabled true, if calling fetch on server is enabled
	 */
	public void setFetchEnabled(boolean pFetchEnabled)
	{
		bFetchEnabled = pFetchEnabled;
	}	

	/**
	 * Returns the block fetch columns.
	 * The block fetch columns have to be a subset of the master link columns.
	 * The rows are fetched with these columns, and sorted into the corresponding pages.
	 * 
	 * @return the block fetch columns.
	 */
	public String[] getBlockFetchColumnNames()
	{
		return saBlockFetchColumnNames;
	}
	
	/**
	 * Sets the block fetch columns.
	 * The block fetch columns have to be a subset of the master link columns.
	 * The rows are fetched with these columns, and sorted into the corresponding pages.
	 * 
	 * @param pBlockFetchColumnNames the block fetch columns.
	 * @throws ModelException if the data book is already open.
	 */
	public void setBlockFetchColumnNames(String[] pBlockFetchColumnNames) throws ModelException
	{
    	synchronized (rootDataBook)
    	{
			if (isOpen())
			{
				throw new ModelException("It's not allowed on open DataBooks!");			
			}
			
			saBlockFetchColumnNames = pBlockFetchColumnNames;
    	}
	}	
	
	/**
	 * Sets the metadata cache enabled. If it is disabled, every open performs a remote call.
	 * 
	 * @param pCacheEnabled <code>true</code> to enable the cache
	 */
	public void setMetaDataCacheEnabled(boolean pCacheEnabled)
	{
		bMetaDataCacheEnabled = pCacheEnabled;
	}
	
	/**
	 * Gets whether metadata cache is enabled.
	 * 
	 * @return <code>true</code> if the cache is enabled, <code>false</code> otherwise
	 */
	public boolean isMetaDataCacheEnabled()
	{
		return bMetaDataCacheEnabled;
	}
	
	/**
	 * It gets the meta data from the Server for this RemoteDataBook.
	 * 
	 * @throws DataSourceException	if an error during communication or Storage interaction occur.
	 */
	private void initServerMetaData() throws DataSourceException
	{
		try
		{
			RemoteDataSource dataSource = getDataSource();
			
			String sDataSourceName = dataSource.getConnection().getLifeCycleName();
			String sName = getName(); 
			
			String sCacheKey = sDataSourceName + "." + sName;
			
			if (bMetaDataCacheEnabled)
			{
				//try to access cached meta-data
				mdMetaData = dataSource.getMetaData(sCacheKey);
			}
			else
			{
				mdMetaData = null;
			}

			if (mdMetaData == null)
			{
			    Boolean bCache = htCachingStorage.get(sCacheKey);
				if (bCache != null)
				{
				    bCachingStorage = bCache.booleanValue();
				}
				else
				{
				    bCachingStorage = true; //try to access a cached storage
				}
				
				long lMillis = System.currentTimeMillis();
				
				if (bCachingStorage && bMetaDataCacheEnabled)
				{
					try
					{
                        //make a request to the server
                        Object[] objResult = getConnection().call(new String[] {sName, sName},
                                                                  new String[] {"getMetaData", "getMetaDataFromCache"}, 
                                                                  new Object[][] { {sDataSourceName, sName}, 
                                                                                   {sDataSourceName}});
        
                        mdMetaData = (MetaData)objResult[0];

                        //save available MetaData
						Hashtable<String, MetaData> htMetaDataCache = (Hashtable<String, MetaData>)objResult[1];
						
						if (htMetaDataCache != null)
						{
							for (Entry<String, MetaData> entry : htMetaDataCache.entrySet())
							{
								dataSource.putMetaData(entry.getKey(), entry.getValue());
							}
						}
                        
                        logger.debug("getMetaDataFromCache(", sDataSourceName, ", ", sName, ") in ", Long.valueOf(System.currentTimeMillis() - lMillis), "ms");
					}
					catch (NoSuchMethodException nsme)
					{
					    bCachingStorage = false;
					}
					//cache information for later use
	                htCachingStorage.put(sCacheKey, Boolean.valueOf(bCachingStorage));
				}
				
				if (!bCachingStorage || !bMetaDataCacheEnabled)
				{
					mdMetaData = (MetaData)getConnection().call(sName, "getMetaData");
					
					if (bMetaDataCacheEnabled)
					{
						dataSource.putMetaData(sCacheKey, mdMetaData);
					}
					
					logger.debug("getMetaData(", sName, ") in ", Long.valueOf(System.currentTimeMillis() - lMillis), "ms");
				}
				
			}
			else
			{				
				logger.debug("use cached metadata: ", sName);
			}
		}
		catch (Throwable throwable)
		{
			throw new DataSourceException("Init ServerMetaData failed!", throwable);
		}
	}

	/**
	 * Returns the server meta data column names.
	 *
	 * @return the server meta data column names.
	 */
	protected String[] getMetaDataColumnNames()
	{
		return saMetaDataColumnNames;
	}
	
	/**
	 * Returns the correct column indexes of the fetched data, after opening the data book.
	 *
	 * @return the correct column indexes of the fetched data, after opening the data book.
	 */
	protected int[] getFetchColumnIndexes()
	{
		return iaFetchColumnIndexes;
	}
	
	/**
	 * Returns the correct master column indexes of the fetched data, after opening the data book.
	 *
	 * @return the correct master column indexes of the fetched data, after opening the data book.
	 */
	protected int[] getMasterColumnIndexesForBlock()
	{
		return iaMasterColumnIndexes;
	}
	
	/**
	 * Returns the amount of fetched rows per block.
	 *
	 * @param pMasterDataRow the master data row.
	 * @return the amount of fetched rows per block.
	 * @throws ModelException if it fails.
	 */
	protected int getFetchedRowsForBlock(IDataRow pMasterDataRow) throws ModelException
	{
		if (htFetchedRowsForBlock == null)
		{
			return 0;
		}
		else
		{
			Integer rows = htFetchedRowsForBlock.get(pMasterDataRow.createDataRow(saBlockFetchReferencedColumnNames));
			if (rows == null)
			{
				return 0;
			}
			else
			{
				return rows.intValue();
			}
		}
	}
	
	/**
	 * Sets the amount of fetched rows per block.
	 *
	 * @param pMasterDataRow the master data row.
	 * @param pFetchedRows the amount of fetched rows per block.
	 * @throws ModelException if it fails.
	 */
	protected void setFetchedRowsForBlock(IDataRow pMasterDataRow, int pFetchedRows) throws ModelException
	{
		if (htFetchedRowsForBlock == null)
		{
			htFetchedRowsForBlock = new Hashtable<Object, Integer>();
		}
		htFetchedRowsForBlock.put(pMasterDataRow.createDataRow(saBlockFetchReferencedColumnNames), Integer.valueOf(pFetchedRows));
	}
	
	/**
	 * Returns the AbstractConnection of the RemoteDataSource for this StorageDataSource.
	 * 
	 * @return the AbstractConnection of the RemoteDataSource for this StorageDataSource.
	 */
	protected AbstractConnection getConnection()
	{
		RemoteDataSource rds = getDataSource();
		
		if (rds == null)
		{
			return null;
		}
		
		return rds.getConnection();
	}
	
	/**
	 * Returns whether the server storage is a cached storage or a storage without cache.
	 * 
	 * @return <code>true</code> if the server storage uses a cache, <code>false</code> otherwise
	 */
	protected boolean isCachingStorage()
	{
		return bCachingStorage;
	}

}	// RemoteDataBook
