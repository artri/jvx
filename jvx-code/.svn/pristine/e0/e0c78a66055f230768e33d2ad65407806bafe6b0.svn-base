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
 * 01.10.2008 - [RH] - creation
 * 11.10.2008 - [RH] - bug fixes, toString() optimized
 * 12.10.2008 - [RH] - bug fixes, store(), insert(), restore()
 * 02.11.2008 - [RH] - bug fixes in SetValue 
 *                     - copy original values corrected
 *                     - convertAndCheckToTypeClass add in setValue for correct DataType conversion 
 *                     SelectedDataRow support added
 * 11.11.2008 - [RH] - MasterRow set in MemDataPage
 * 17.11.2008 - [RH] - optimized; ArrayUtil to int[] changed; unnecessary intern removed    
 * 19.11.2008 - [RH] - fetchToRowInternal added for PK check, that no row get requested from AbstractStorage
 * 10.04.2009 - [RH] - interface review - size to getRowCount renamed
 *                                        getDataRow uses an ChangeableDataRow
 *                                        getMasterRow(), getChangedDataRows() added
 *                                        change state management is moved to ChangeableDataRow and MemDataBook
 * 18.04.2009 - [RH] - javadoc reviewed, code optimized                                                     
 * 12.06.2009 - [JR] - toString: used StringBuilder [PERFORMANCE]
 * 16.10.2009 - [RH] - fetchAll in one request implemented -> fecthToRow(-1)=fetchAll
 *                     filter StackOverflow Error fixed [BUGFIXED]
 * 18.10.2009 - [RH] - setFilter(xx), with isMemFilter == true, on an RemoteDataBook, didn't fetched all rows, before it filters in mem. [BUGFIXED]
 * 29.09.2010 - [RH] - countRows renamed to getEstimatedRowCount(),  getEstimatedRowCount() returns in MemDataPage getRowCount() 
 * 08.04.2011 - [RH] - #330 - restoreAllRows fails in DataSource Level.
 * 20.12.2012 - [RH] - Code Review - Changes management in insert, update, delete, store, restore, setDetailChanged is moved 
 *                                   from MemDataBook to the MemDataPage. 
 * 10.04.2013 - [RH] - #617 - saveAllDataBooks doesn't save all rows in DATASOURCE level - fixed
 * 10.04.2013 - [RH] - #617 - restoreAllRows fails with ArrayIndexOutOfBoundsException - fixed
 * 10.04.2013 - [RH] - #618 - restoreAllRows throws an Exception - fixed
 * 12.04.2013 - [RH] - #514 - JVx DataBook, DataPage, DataRow toString should be better formatted - fixed
 */
package com.sibvisions.rad.model.mem;

import java.util.Arrays;
import java.util.Iterator;

import javax.rad.model.IChangeableDataRow;
import javax.rad.model.IDataBook;
import javax.rad.model.IDataPage;
import javax.rad.model.IDataRow;
import javax.rad.model.IRowDefinition;
import javax.rad.model.ModelException;
import javax.rad.model.SortDefinition;
import javax.rad.model.condition.ICondition;
import javax.rad.model.datatype.BigDecimalDataType;
import javax.rad.model.datatype.DataType;
import javax.rad.model.datatype.IDataType;
import javax.rad.model.datatype.TimestampDataType;

import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.type.ExceptionUtil;
import com.sibvisions.util.type.StringUtil;

/**
 * A <code>MemDataPage</code> is the memory implementation for a data page of an 
 * <code>IDataBook</code>.<br>
 * A master <code>IDataBook</code> has one <code>IDataPage</code> for itself. If the 
 * <code>IDataBook</code> is (also) a detail <code>IDataBook</code> 
 * it stores all <code>IDataPage</code>'s for each loaded master row (parent master). <br>
 * 
 * @see javax.rad.model.IDataBook
 * @see javax.rad.model.IDataPage
 * @see javax.rad.model.IChangeableDataRow
 * @author Roland Hörmann
 */
public class MemDataPage implements IDataPage
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The initial storage size for the DataRow/Object array. */
	private static final int	INIT_STORAGE_SIZE = 16;

	/** The initial storage size for the DataRow/Object array. */
	static final int[]			EMPTY_INT_ARRAY = new int[0];

	/** The IDataBook that uses this MemDataPage. */
	private MemDataBook			dbDataBook;
	/** The root IDataBook. */
	protected IDataBook			rootDataBook;
	/** The IRowDefinition that uses this MemDataPage. */
	protected IRowDefinition	rdRowDefinition;
	/** The master row from the corresponding master DataBook. */
	protected IDataRow			drMasterDataRow; 		
	/** The storage array with all DataRows of the DataPage. */
	private ArrayUtil<Object[]>	alStorage;
	
	/** The array with all changes rows. */
	private int[]			 	iaChangedRows = null;
	/** The amount of changedRows. */
	private int					iChangedRowCount = 0;
	
	/** 
	 * The boolean indicates if all rows are fetched from the under laying storage.
	 * In this MemDataPage its this always true.
	 */
	private boolean 			bAllFetched = true;
		
	/** Array with all rows that shows the MemDataBook outside. -> mem sort, filter. */
	private int[]				iaUsedRows = null;
	/** The amount of rows. */
	private int					iUsedRowCount = 0;
		
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Construct a new MemDataPage for the specified IDataBook and the corresponding master row. 
	 * 
	 * @param pDataBook 		the IDataBook which uses this MemDataPage 
	 * @param pMasterDataRow	the corresponding master row of the master IDataBook of the 
	 *                          above specified IDataBook 
	 */
	public MemDataPage(MemDataBook pDataBook, IDataRow pMasterDataRow)
	{
		dbDataBook = pDataBook;
		rdRowDefinition = pDataBook.getRowDefinition();
		rootDataBook = dbDataBook.getRootDataBook();
		
		drMasterDataRow = pMasterDataRow;
		alStorage  = new ArrayUtil<Object[]>(INIT_STORAGE_SIZE);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public IDataBook getDataBook()
	{
		return dbDataBook;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public IDataRow getMasterDataRow()
	{
		return drMasterDataRow;
	}
	
	/**
	 * {@inheritDoc}  
	 */
	public IChangeableDataRow getDataRow(int pDataRowIndex) throws ModelException
	{
    	synchronized (rootDataBook)
    	{
			initFilterSort();
			
			if (!bAllFetched && pDataRowIndex >= alStorage.size())
			{
				fetchToRow(pDataRowIndex);
			}
	
			if (pDataRowIndex >= 0 && pDataRowIndex < getRowCountInternal())
			{
				ChangeableDataRow dataRow = new ChangeableDataRow(rdRowDefinition,
												 alStorage.get(getInternalRowIndex(pDataRowIndex)), // calculate in original data, to be able to cache calculations.
										         this,
										         pDataRowIndex);
				if (dbDataBook.getRowCalculator() != null)
				{
					try 
					{
						dbDataBook.getRowCalculator().calculateRow(dbDataBook, this, dataRow);
					} 
					catch (Throwable e) 
					{
						// Row calculation is silent
					}
				}

				dataRow.oaStorage = dataRow.oaStorage.clone(); // clone after calculation 
				
				return dataRow;
			}
			else
			{
				return null;
			}
    	}
	}

	/**
	 * {@inheritDoc}
	 */
	public void fetchAll() throws ModelException
	{
		if (!bAllFetched)				
		{
			fetchToRow(-1);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isAllFetched() throws ModelException
	{
    	synchronized (rootDataBook)
    	{
			// prevent fetching, when master is still inserting.
			if (drMasterDataRow instanceof IChangeableDataRow && ((IChangeableDataRow)drMasterDataRow).getUID() != null)
			{
				return true;
			}
			else
			{
				return bAllFetched;
			}
    	}
	}

	/**
	 * {@inheritDoc}
	 */
	public int getRowCount() throws ModelException
	{
    	synchronized (rootDataBook)
    	{
			initFilterSort();
	
			return getRowCountInternal();
    	}
	}		
	
	/**
	 * {@inheritDoc}
	 */
	@Deprecated
	public int[] getChangedDataRows()
	{
		return getChangedRows();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int[] getChangedRows()
	{
    	synchronized (rootDataBook)
    	{
			if (iChangedRowCount == 0)
			{
				return EMPTY_INT_ARRAY;
			}
		
			// Convert internal indices to the current Filter/Sort external indices.
			// -> Remove it in the result, if they aren't in the filter result.
			if (iaUsedRows == null)
			{
				int[] iaResultChanges = new int[iChangedRowCount];
	
				System.arraycopy(iaChangedRows, 0, iaResultChanges, 0, iChangedRowCount);
				
				return iaResultChanges;
			}
			else
			{
				int[] iaResultChanges = new int[Math.min(iChangedRowCount, iUsedRowCount)];
	
				int index = 0;
				for (int i = 0; i < iUsedRowCount && index < iChangedRowCount; i++)
				{		
					if (Arrays.binarySearch(iaChangedRows, 0, iChangedRowCount, iaUsedRows[i]) >= 0)
					{
						iaResultChanges[index] = i; 
						index++;
					}
				}
				return ArrayUtil.truncate(iaResultChanges, index);
			}
    	}
	}

	/**
	 * {@inheritDoc}
	 */
	public int searchNext(ICondition pCondition) throws ModelException
	{
		return searchNext(pCondition, 0);
	}

	/**
	 * {@inheritDoc}
	 */
	public int searchNext(ICondition pCondition, int pStartIndex) throws ModelException
	{
    	synchronized (rootDataBook)
    	{
			if (pCondition != null && pStartIndex >= 0)
			{
				initFilterSort();
				
				if (!bAllFetched && pStartIndex >= alStorage.size())
				{
					fetchToRow(pStartIndex);
				}
				
				int rowCount = getRowCountInternal();
				while (pStartIndex < rowCount)
				{
					dbDataBook.rowInstance1.oaStorage = alStorage.get(getInternalRowIndex(pStartIndex));
							
					if (pCondition.isFulfilled(dbDataBook.rowInstance1))
					{
						return pStartIndex;
					}
					pStartIndex++;
					if (!bAllFetched && pStartIndex >= alStorage.size())
					{
						fetchToRow(pStartIndex);
						
						rowCount = getRowCountInternal();
					}
				}
			}
			return -1;
    	}
	}

	/**
	 * {@inheritDoc}
	 */
	public int searchPrevious(ICondition pCondition) throws ModelException
	{
		return searchPrevious(pCondition, -1);
	}

	/**
	 * {@inheritDoc}
	 */
	public int searchPrevious(ICondition pCondition, int pStartIndex) throws ModelException
	{
    	synchronized (rootDataBook)
    	{
			if (pCondition != null && pStartIndex != 0)
			{
				initFilterSort();
				
				if (!bAllFetched)
				{
					if (pStartIndex > alStorage.size())
					{
						fetchToRow(pStartIndex - 1);
					}
					else if (pStartIndex < 0)
					{
						fetchAll();
					}
				}
	
				int rowCount = getRowCountInternal();
				if (pStartIndex < 0 || pStartIndex > rowCount)
				{
					pStartIndex = rowCount - 1;
				}
				else
				{
					pStartIndex--;
				}
				
				while (pStartIndex >= 0)
				{
					dbDataBook.rowInstance1.oaStorage = alStorage.get(getInternalRowIndex(pStartIndex));
					
					if (pCondition.isFulfilled(dbDataBook.rowInstance1))
					{
						return pStartIndex;
					}
					pStartIndex--;
				}
			}
			return -1;
    	}
	}

    /**
     * {@inheritDoc}
     */
	public Iterator<IChangeableDataRow> iterator()
	{
	    return new DataPageIterator();
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
		return toString("");
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
	
	/**
	 * Returns true, if there are changes.
	 * 
	 * @return true, if there are changes
	 */
	public boolean hasChanges()
	{
		return iChangedRowCount > 0;
	}
	
	/**
	 * Sets that the MemDataPage has all fetched. Should only used from derived classes.
	 * 
	 * @param pAllFetched	the boolean with the fetch state.
	 */
	protected void setAllFetched(boolean pAllFetched)
	{
		bAllFetched = pAllFetched;
		clear(); // Ensure, that mem sort is done again after allFetched State is changed.
				 // If Master is inserting, the fetch of details is blocked (isAllFetched=true)
				 // After Store of Master, the detail has to fetch everything.
	}

	/**
	 * It adds an new IDataRow to the DataPage in the object[] storage.
	 * 
	 * @param pValues			the values.
	 * @throws ModelException	if the IDataRow couldn't add to the storage.
	 */
	protected void addFetchedRow(Object[] pValues) throws ModelException
	{
		alStorage.add(pValues);
	}
	
	/**
	 * It inserts a new <code>IChangeableDataRow</code> in the MemDataPage at the specified index. 
	 * 
	 * @param pDataRowIndex		the row index to use.
	 * @param pDataRow			the row. 
     * @return changeCounter    the changeCounter is: 
     *                          1 in case, the page had no changes, and afterwards there are changes
     *                          -1 in case, there were changes and afterwards ther are no changes
     *                          0 otherwise (before and afterwards are changes or no changes
	 * @throws ModelException	if the Filter/Sort couldn't initialized.
	 */
	protected int insert(int pDataRowIndex, ChangeableDataRow pDataRow) throws ModelException
	{
		if (pDataRowIndex > getRowCount())
		{
			throw new ModelException("DataRow index out of bounds");
		}

        boolean hasChangesBefore = hasChanges();
		if (iaUsedRows == null)
		{
			alStorage.add(pDataRowIndex, pDataRow.oaStorage);
			addChange(pDataRowIndex, true);
			if (!pDataRow.isChanged() && !pDataRow.isDetailChanged())
			{
				removeChange(pDataRowIndex, false);
			}
		}
		else
		{
			int pos = alStorage.size();
			alStorage.add(pos, pDataRow.oaStorage);

			if (iUsedRowCount == iaUsedRows.length)
			{
				int[] newUsedRows = new int[Math.max(INIT_STORAGE_SIZE, iUsedRowCount * 2)];
				
				System.arraycopy(iaUsedRows, 0, newUsedRows, 0, pDataRowIndex);
				if (pDataRowIndex < iUsedRowCount)
				{
					System.arraycopy(iaUsedRows, pDataRowIndex, newUsedRows, pDataRowIndex + 1, iUsedRowCount - pDataRowIndex);
				}
				iaUsedRows = newUsedRows;
			}
			else if (pDataRowIndex < iUsedRowCount)
			{
				System.arraycopy(iaUsedRows, pDataRowIndex, iaUsedRows, pDataRowIndex + 1, iUsedRowCount - pDataRowIndex);
			}
			iaUsedRows[pDataRowIndex] = pos;
			iUsedRowCount++;
			
			addChange(pos, true);
			if (!pDataRow.isChanged() && !pDataRow.isDetailChanged())
			{
				removeChange(pos, false);
			}
		}
		boolean hasChangesAfter = hasChanges();
		if (hasChangesBefore != hasChangesAfter)
		{
		    return hasChangesAfter ? 1 : -1;
		}
		else
		{
		    return 0;
		}
	}
	
	/**
	 * Marks the specified row as Deleted with the given index in the MemDataPage. 
	 * if the row is inserting(), then the row is really removed from the MemDataPage.
	 * 
	 * @param pDataRowIndex	the row index to use.
     * @return changeCounter    the changeCounter is: 
     *                          1 in case, the page had no changes, and afterwards there are changes
     *                          -1 in case, there were changes and afterwards ther are no changes
     *                          0 otherwise (before and afterwards are changes or no changes
	 * @throws ModelException	if the delete operation fails.
	 */
	protected int delete(int pDataRowIndex) throws ModelException
	{		
		if (pDataRowIndex >= getRowCount())
		{
			throw new ModelException("Row index out of bounds");
		}	
		
        boolean hasChangesBefore = hasChanges();
		int iInternalIndex = getInternalRowIndex(pDataRowIndex);
		
		// remove current row
		alStorage.remove(iInternalIndex);		
		
		if (iaUsedRows != null)
		{
			iUsedRowCount--;
			if (pDataRowIndex < iUsedRowCount)
			{
				System.arraycopy(iaUsedRows, pDataRowIndex + 1, iaUsedRows, pDataRowIndex, iUsedRowCount - pDataRowIndex);
			}
			shiftIndicies(iInternalIndex, -1);
		}

		removeChange(iInternalIndex, true);		

		boolean hasChangesAfter = hasChanges();
        if (hasChangesBefore != hasChangesAfter)
        {
            return hasChangesAfter ? 1 : -1;
        }
        else
        {
            return 0;
        }
	}

	/**
	 * Returns the internal storage for the specified DataRow Object[].
	 *  
	 * @param pDataRowIndex	the row index.
	 * @return the internal storage for the specified DataRow Object[].
	 * @throws ModelException	if the mem sort and/or filter  fails.
	 */
	protected Object[] getDataRowStorage(int pDataRowIndex) throws ModelException
	{
		initFilterSort();
		
		if (!bAllFetched && pDataRowIndex >= alStorage.size())
		{
			fetchToRow(pDataRowIndex);
		}

		if (pDataRowIndex >= 0 && pDataRowIndex < getRowCountInternal())
		{
			return alStorage.get(getInternalRowIndex(pDataRowIndex));
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * Sets a new Master DataRow, if it changes. Internal function!
	 * 
	 * @param pMasterDataRow	the new master DataRow
	 * @throws ModelException	if the column value couldn't converted 
	 */
	protected void setMasterDataRow(IDataRow pMasterDataRow) throws ModelException
	{
		drMasterDataRow = pMasterDataRow;

		// master row changed, copy master PK to Details FK Columns
		String[] saColumns = dbDataBook.getMasterReference().getColumnNames();
        int[] iaColumnIndicies = new int[saColumns.length];
        IDataType[] dataTypes = new IDataType[saColumns.length];
        
        // cache the column indicies
		for (int j = 0; j < iaColumnIndicies.length; j++)
		{
			int columnIndex = rdRowDefinition.getColumnDefinitionIndex(saColumns[j]);
			iaColumnIndicies[j] = columnIndex;
			dataTypes[j] = rdRowDefinition.getColumnDefinition(columnIndex).getDataType();
		}
        
    	for (int i = 0; i < alStorage.size(); i++)
    	{
    		Object[] dataRow = alStorage.get(i);
    		
    		for (int j = 0; j < iaColumnIndicies.length; j++)
    		{
    			dataRow[iaColumnIndicies[j]] = dataTypes[j].convertAndCheckToTypeClass(drMasterDataRow.getValue(j));		    			
    		}
    	}		
	}
	
	/**
	 * Returns -1. Will/should be overridden in the derived Classes. 
	 * 
	 * @return -1.
	 * @throws ModelException see derived classes. 
	 */
	public int getEstimatedRowCount() throws ModelException
	{		
		return getRowCount();
	}	
	
	/**
	 * Will/should be overridden in the derived Classes to fetch data from the storage. 
	 * 
	 * @param pRowIndex	the row index to use.
	 * @throws ModelException see derived classes. 
	 */
	public void fetchToRow(int pRowIndex) throws ModelException
	{
	}
	
	/**
	 * Returns the row count of this MemDataPage. Internal use only.
	 * 
	 * @return the row count of this MemDataPage. Internal use only. 
	 */
	protected int getRowCountInternal()
	{
		if (iaUsedRows == null)
		{
			return alStorage.size();
		}
		else
		{
			return iUsedRowCount;
		}
	}		
	
	/**
	 * Returns the internal / real row index of the row in the MemDataPage.
	 * Its necessary because of memory sort and filter functionality. 
	 * 
	 * @param pRowIndex	the external used row index.
	 * @return the internal / real row index of the row in the MemDataPage.
	 */
	private int getInternalRowIndex(int pRowIndex)
	{
		if (iaUsedRows == null)
		{
			return pRowIndex;
		}
		else if (pRowIndex >= iUsedRowCount)
		{
			throw new ArrayIndexOutOfBoundsException(pRowIndex);
		}
		else
		{
			return iaUsedRows[pRowIndex];
		}
	}
	
	
	/**
	 * Sets the internal storage for the specified DataRow Object[].
	 * 
	 * @param pDataRowIndex		the row index.
	 * @param pRow				the ChangeableDataRow to use.
	 * @return changeCounter    the changeCounter is: 
	 *                          1 in case, the page had no changes, and afterwards there are changes
	 *                          -1 in case, there were changes and afterwards ther are no changes
	 *                          0 otherwise (before and afterwards are changes or no changes
	 * @throws ModelException	if the mem sort and/or filter  fails.
	 */
	protected int setDataRow(int pDataRowIndex, ChangeableDataRow pRow) throws ModelException
	{
	    boolean hasChangesBefore = hasChanges();
		int index = getInternalRowIndex(pDataRowIndex);
		
		alStorage.set(index, pRow.oaStorage);
		
		// set correct Change for new Storage Object[]
		if (pRow.isChanged() || pRow.isDetailChanged())
		{
			addChange(index, false);
		}
		else
		{
			removeChange(index, false);
		}
		boolean hasChangesAfter = hasChanges();
		if (hasChangesBefore != hasChangesAfter)
		{
		    return hasChangesAfter ? 1 : -1;
		}
		else
		{
		    return 0;
		}
	}	
	
	/**
	 * It shifts all items (row indices) by pShift (-1/+1).
	 *  
	 * @param pRowIndex	all items with >= pRowIndex will be shifted. 
	 * @param pShift	the value to add/shift
	 */
	private void shiftIndicies(int pRowIndex, int pShift)
	{
		for (int i = 0; i < iUsedRowCount; i++)
		{
			int iCurrent = iaUsedRows[i];
			if (iCurrent >= pRowIndex)
			{
				iCurrent += pShift;
				iaUsedRows[i] = iCurrent;
			}
		}
	}
	
	/**
	 * Adds a new change in the internal list of changes.
	 * 
	 * @param pRowIndex	the row index where the change was made.
	 * @param pInsert	true, if it was an insert.
	 */
	private void addChange(int pRowIndex, boolean pInsert)
	{
		if (iaChangedRows == null)
		{
			iaChangedRows = new int[INIT_STORAGE_SIZE];
		}
		int pos = Arrays.binarySearch(iaChangedRows, 0, iChangedRowCount, pRowIndex);
		
		if (pInsert)
		{
			if (pos < 0)
			{
				pos = -pos - 1;
			}
			for (int i = pos; i < iChangedRowCount; i++)
			{
				iaChangedRows[i]++;
			}
		}
		else
		{
			pos = -pos - 1;
		}
		if (pos >= 0)
		{
			if (iChangedRowCount == iaChangedRows.length)
			{
				int[] newChangedRows = new int[iChangedRowCount * 2];
				
				System.arraycopy(iaChangedRows, 0, newChangedRows, 0, pos);
				if (pos < iChangedRowCount)
				{
					System.arraycopy(iaChangedRows, pos, newChangedRows, pos + 1, iChangedRowCount - pos);
				}
				iaChangedRows = newChangedRows;
			}
			else if (pos < iChangedRowCount)
			{
				System.arraycopy(iaChangedRows, pos, iaChangedRows, pos + 1, iChangedRowCount - pos);
			}
			iaChangedRows[pos] = pRowIndex;
			iChangedRowCount++;
		}
	}
	
	/**
	 * Remove an existing change by row index. 
	 * Also if this row is still isInserting(), isUpdating(),isDeleting(), isDetailChanged()!!!
	 * Its used for real delete of a row from the storage.
	 * 
	 * @param pRowIndex	the row index to use.
	 * @param pDelete	true, if it is a delete.
	 * @throws ModelException	if determining of the row state fails.
	 */
	private void removeChange(int pRowIndex, boolean pDelete) throws ModelException
	{
		int pos = Arrays.binarySearch(iaChangedRows, 0, iChangedRowCount, pRowIndex);
		
		if (pos >= 0)
		{
			iChangedRowCount--;
			if (iChangedRowCount > INIT_STORAGE_SIZE && iChangedRowCount == iaChangedRows.length / 2)
			{
				int[] newChangedRows = new int[iChangedRowCount];
				
				System.arraycopy(iaChangedRows, 0, newChangedRows, 0, pos);
				if (pos < iChangedRowCount)
				{
					System.arraycopy(iaChangedRows, pos + 1, newChangedRows, pos, iChangedRowCount - pos);
				}
				iaChangedRows = newChangedRows;
			}
			else if (pos < iChangedRowCount)
			{
				System.arraycopy(iaChangedRows, pos + 1, iaChangedRows, pos, iChangedRowCount - pos);
			}
		}
		else
		{
			pos = -pos - 1;
		}
		if (pDelete)
		{
			for (int i = pos; i < iChangedRowCount; i++)
			{
				iaChangedRows[i]--;
			}
		}
	}	
	
	/**
	 * Initialize the mem filter and sort.
	 * 
	 * @throws ModelException	if the mem sort and/or filter fails.
	 */
	private void initFilterSort() throws ModelException
	{
		if (iaUsedRows == null && dbDataBook != null)
		{
			if (dbDataBook.bMemFilter)
			{
				filter();
			}
			if (dbDataBook.bMemSort)
			{
				sort();
			}
		}		
	}
	
	/**
	 * It clears the mem filter and sort in the MemDataPage.  
	 */
	protected void clear()
	{
		iaUsedRows = null;
	}
	
	/**
	 * It filters the MemDataPage and return an int[] with DataRow indices to use for 
	 * the filter result.
	 * 
	 * @throws ModelException	if the isFulfilled fails.
	 */
	private void filter() throws ModelException
	{
		ICondition cFilter = dbDataBook.getFilter();
		
		if (cFilter != null)
		{
			fetchAll();

			int rowCount = getRowCountInternal();
			int[] newUsedRows = new int[INIT_STORAGE_SIZE];
			int iCount = 0;
			for (int i = 0; i < rowCount; i++)
			{
				dbDataBook.rowInstance1.oaStorage = alStorage.get(i);
				if (cFilter.isFulfilled(dbDataBook.rowInstance1))
				{
					if (iCount == newUsedRows.length)
					{
						int[] newResult = new int[iCount * 2];
						
						System.arraycopy(newUsedRows, 0, newResult, 0, iCount);
						newUsedRows = newResult;
					}

					newUsedRows[iCount] = i;
					iCount++;
				}
			}
			iaUsedRows = newUsedRows;
			iUsedRowCount = iCount;
		}
	}
	
	/**
	 * It sorts the MemDataPage and return an int[] with DataRow indices to use for 
	 * the sorted result.
	 * 
	 * @throws ModelException	if the compareTo fails.
	 */
	private void sort() throws ModelException
	{
		SortDefinition sort = dbDataBook.getSort();
		if (sort != null)
		{
			fetchAll();

			if (iaUsedRows == null)
			{
				iaUsedRows = createArrayWithAscendingIndexes(getRowCountInternal());
				iUsedRowCount = iaUsedRows.length;
			}

			quickSort(iaUsedRows, 0, iUsedRowCount - 1, sort);
			insertionSort(iaUsedRows, 0, iUsedRowCount - 1, sort);
		}
	}

	/**
	 * Creates an array with ascending indexes.
	 * 
	 * @param pLength the length of the array.
	 * @return the array with ascending indexes.
	 */
	private int[] createArrayWithAscendingIndexes(int pLength)
	{
		int[] result = new int[pLength];
		
		for (int i = 0; i < result.length; i++)
		{
			result[i] = i;
		}
		return result;
	}
	
	/**
	 * Swap the source and target index in the result in[].
	 * 
	 * @param pResult the result int[] with the row indexes.
	 * @param pSourceIndex the source index.
	 * @param pTargetIndex the target index.
	 */
	private void swap(int[] pResult, int pSourceIndex, int pTargetIndex)
	{
		int iTemp = pResult[pSourceIndex];
		
		pResult[pSourceIndex] = pResult[pTargetIndex];
		pResult[pTargetIndex] = iTemp;
	}

	/**
	 * QuickSort implementation.
	 * 
	 * @param pResult the result int[] with the row indexes.
	 * @param pFromIndex the from index to sort.
	 * @param pToIndex the to index to sort.
	 * @param pSort the {@link SortDefinition}
	 */
	private void quickSort(int[] pResult, int pFromIndex, int pToIndex, SortDefinition pSort) 
	{
		if (pToIndex - pFromIndex > 4)
		{
			int i = (pToIndex + pFromIndex) / 2;
			
			dbDataBook.rowInstance1.oaStorage = alStorage.get(pResult[pFromIndex]);
			dbDataBook.rowInstance2.oaStorage = alStorage.get(pResult[i]);
			if (dbDataBook.rowInstance1.compareTo(dbDataBook.rowInstance2, pSort) > 0)
			{
				swap(pResult, pFromIndex, i);
			}
			dbDataBook.rowInstance1.oaStorage = alStorage.get(pResult[pFromIndex]);
			dbDataBook.rowInstance2.oaStorage = alStorage.get(pResult[pToIndex]);
			if (dbDataBook.rowInstance1.compareTo(dbDataBook.rowInstance2, pSort) > 0)
			{
				swap(pResult, pFromIndex, pToIndex);
			}
			dbDataBook.rowInstance1.oaStorage = alStorage.get(pResult[i]);
			dbDataBook.rowInstance2.oaStorage = alStorage.get(pResult[pToIndex]);
			if (dbDataBook.rowInstance1.compareTo(dbDataBook.rowInstance2, pSort) > 0)
			{
				swap(pResult, i, pToIndex);
			}

			int j = pToIndex - 1;
			
			swap(pResult, i, j);
			i = pFromIndex;
			
			dbDataBook.rowInstance2.oaStorage = alStorage.get(pResult[j]);
			
			while (true)
			{
				dbDataBook.rowInstance1.oaStorage = alStorage.get(pResult[++i]);
				while (dbDataBook.rowInstance1.compareTo(dbDataBook.rowInstance2, pSort) < 0) 
				{ 
					dbDataBook.rowInstance1.oaStorage = alStorage.get(pResult[++i]);
				}
				
				dbDataBook.rowInstance1.oaStorage = alStorage.get(pResult[--j]);
				while (dbDataBook.rowInstance1.compareTo(dbDataBook.rowInstance2, pSort) > 0)
				{ 
					dbDataBook.rowInstance1.oaStorage = alStorage.get(pResult[--j]);
				}
				
				if (j < i)
				{
					break;
				}
				swap(pResult, i, j);
			}
			
			swap(pResult, i, pToIndex - 1);
			quickSort(pResult, pFromIndex, j, pSort);
			quickSort(pResult, i + 1, pToIndex, pSort);
		}
	}

	/**
	 * Insertion sort implementation.
	 * 
	 * @param pResult the result int[] with the row indexes.
	 * @param pFromIndex the from index to sort
	 * @param pToIndex the to index to sort
	 * @param pSort the {@link SortDefinition}
	 */
	private void insertionSort(int[] pResult, int pFromIndex, int pToIndex, SortDefinition pSort) 
	{
		for (int i = pFromIndex + 1; i <= pToIndex; i++)
		{
			int vIndex = pResult[i];
			dbDataBook.rowInstance2.oaStorage = alStorage.get(vIndex);
			
			int j = i;
			if (j > pFromIndex)
			{
				dbDataBook.rowInstance1.oaStorage = alStorage.get(pResult[j - 1]);
				while (j > pFromIndex && dbDataBook.rowInstance1.compareTo(dbDataBook.rowInstance2, pSort) > 0)
				{
					pResult[j] = pResult[j - 1];
					j--;
					if (j > pFromIndex)
					{
						dbDataBook.rowInstance1.oaStorage = alStorage.get(pResult[j - 1]);
					}
				}
			}
			
			pResult[j] = vIndex;
		}
	}	
	
	/**
	 * Dumps this data page as string.
	 * 
	 * @param pPadding line padding
	 * @return the string
	 */
	String toString(String pPadding)
	{
		StringBuilder sbResult = new StringBuilder();
		
		if (dbDataBook != null)
		{
			sbResult.append(pPadding);
			sbResult.append("DataBook = ");
			sbResult.append(dbDataBook.getName());
			sbResult.append("\n");
		}
		
		try
		{
			if (rdRowDefinition != null)
			{
				sbResult.append(pPadding);
		        sbResult.append("Changes  = [");
		        
		        for (int i = 0; i < iChangedRowCount; i++)
		        {
		        	if (i > 0)
		        	{
		        		sbResult.append(", ");
		        	}
		        	sbResult.append(iaChangedRows[i]);
		        }
		        		        
		        sbResult.append("]");

		        // init padding infos
				String[]       sColumns       = rdRowDefinition.getColumnNames().clone();
				int[]          iaPadSize      = new int[sColumns.length];
				int[]          iaPadAlignment = new int[sColumns.length];
				
				int iMinusCount = 0;
				
				sbResult.append("\n\n");
				sbResult.append(pPadding);
				sbResult.append("[    #][       UID][CHANGES] |");
				
				for (int i = 0, iSize = sColumns.length; i < iSize; i++)
				{
					IDataType dataType = rdRowDefinition.getColumnDefinition(sColumns[i]).getDataType();
					
					if (dataType instanceof DataType)
					{
						iaPadSize[i] = ((DataType)dataType).getSize();
						if (iaPadSize[i] > 50)
						{
							switch (dataType.getTypeIdentifier())
							{
								case BigDecimalDataType.TYPE_IDENTIFIER:
									iaPadSize[i] = 15;
									break;
								case TimestampDataType.TYPE_IDENTIFIER:
									iaPadSize[i] = 20;
									break;
								default:
									iaPadSize[i] = 50;
							}
						}
					}
					
					iaPadAlignment[i] = 0; // ==left
	
					if (dataType instanceof BigDecimalDataType)
					{
						iaPadAlignment[i] = 1; // ==right
					}

					if (sColumns[i] != null && sColumns[i].length() > iaPadSize[i])
					{
						sColumns[i] = sColumns[i].substring(0, iaPadSize[i]);
					}
					
					String sCol = StringUtil.rpad(sColumns[i], iaPadSize[i]);
					iMinusCount += sCol.length();
					sbResult.append(sCol);
					if (i + 1 < iSize)
					{
						sbResult.append("|");
						iMinusCount++;
					}
				}
				sbResult.append("\n");
				
				sbResult.append(pPadding);
				sbResult.append("-------------------------------");
				
				for (int i = 0; i < iMinusCount; i++)
				{
					sbResult.append('-');
				}
				
		        for (int i = 0, iLen = getRowCountInternal(); i < iLen; i++)
				{
					sbResult.append("\n");
		        	sbResult.append(pPadding);
					sbResult.append("[");
					sbResult.append(StringUtil.lpad(Integer.toString(i), 5));
					sbResult.append("]");
		
					int               index = getInternalRowIndex(i);
					ChangeableDataRow cdr   = new ChangeableDataRow(rdRowDefinition,
					        alStorage.get(index),
					        this,
					        i);

					sbResult.append("[");
					sbResult.append(StringUtil.lpad((cdr.getUID() != null ? "" + cdr.getUID() : ""), 10));
					sbResult.append("]");
					
					
					sbResult.append("[");
					if (cdr.isInserting())
					{
						sbResult.append("I");
					}
					else if (cdr.isUpdating())
					{
						sbResult.append("U");
					}
					else if (cdr.isDeleting())
					{
						sbResult.append("D");
					}
					else
					{
						sbResult.append(" ");
					}
					if (cdr.isDetailChanged())
					{
						sbResult.append("DC");
					}
					else
					{
						sbResult.append("  ");
					}
					sbResult.append("    ] |");
	
					for (int j = 0; j < sColumns.length; j++)
					{
						Object value = cdr.getValue(j);
					    
						if (value == null)
					    {
					    	value = "null";
					    }
						else
						{
							value = StringUtil.toString(value).replace("\n", "\\n");
							
							if (((String)value).length() > iaPadSize[j])
							{
								value = ((String)value).substring(0, iaPadSize[j]);
							}
						}
						
						if (iaPadAlignment[j] == 0)
						{
							sbResult.append(StringUtil.rpad(StringUtil.toString(value), iaPadSize[j]));						
						}
						else
						{
							sbResult.append(StringUtil.lpad(StringUtil.toString(value), iaPadSize[j]));						
						}
						if (j + 1 < sColumns.length)
						{
							sbResult.append("|");
						}
					}
				}
			}
		}
		catch (ModelException modelException)
		{
			sbResult.append("\n");
			sbResult.append(ExceptionUtil.dump(modelException, true));
			
	        return sbResult.toString();
		}
        
        return sbResult.toString();
	}	

    /**
     * Fast {@link Iterator} that fetches all rows.
     *  
     * @author Martin Handsteiner
     */
    private class DataPageIterator implements Iterator<IChangeableDataRow>
    {
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Class Members
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /** The index of the iterator. */
        private int index = 0;
        /** The current propertyName. */
        private IChangeableDataRow nextRow;
        
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Interface Implementation
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /**
         * {@inheritDoc}
         */
        public boolean hasNext()
        {
            try
            {
                nextRow = getDataRow(index++);

                return nextRow != null;
            }
            catch (ModelException ex)
            {
                throw new RuntimeException(ex);
            }
        }

        /**
         * {@inheritDoc}
         */
        public IChangeableDataRow next()
        {
            return nextRow;
        }

        /**
         * {@inheritDoc}
         */
        public void remove()
        {
            throw new UnsupportedOperationException();
        }
        
    }   // DataPageIterator
	
} 	// MemDataPage
