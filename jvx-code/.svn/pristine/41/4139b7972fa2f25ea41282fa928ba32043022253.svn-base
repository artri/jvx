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
 * 09.04.2009 - [RH] - creation
 * 18.04.2009 - [RH] - get/set/DataPage/RowIndex moved to ChangeableDataRow        
 * 31.03.2011 - [RH] - #163 - IChangeableDataRow should support isWritableColumnChanged             
 * 19.12.2012 - [RH] - Code review - setUpdating(), setDeleting(), setInserting(), setUID(), 
 *                                   store(), restore(), setDetailChanged moved from MemDataBook    
 * 28.02.2013 - [RH] - #646 - isWritableColumnChanged() is wrong in ChangeableDataRow         
 */
package com.sibvisions.rad.model.mem;

import java.io.Serializable;

import javax.rad.model.ColumnDefinition;
import javax.rad.model.IChangeableDataRow;
import javax.rad.model.IDataPage;
import javax.rad.model.IDataRow;
import javax.rad.model.IRowDefinition;
import javax.rad.model.ModelException;

import com.sibvisions.util.type.ExceptionUtil;

/**
 * An <code>IChangeableDataRow</code> extends the <code>IDataRow</code> with support for
 * the change state of the row, some supporting methods and an unique ID column.
 * 
 * @see javax.rad.model.IDataRow
 * @see javax.rad.model.IDataPage
 * @see javax.rad.model.IDataBook
 * 
 * @author Roland Hörmann
 */
public class ChangeableDataRow extends DataRow 
                               implements IChangeableDataRow, 
                                          Serializable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constants
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** Internal Offset in the AbstractStorage Array for the Inserting, etc.. states. */
	protected static final int    	  INTERNAL_OFFSET     = 3;
	
	/** Internal state for INSERTING. */
	protected static final Integer    INSERTING = Integer.valueOf(1);
	/** Internal state for UPDATING. */
	protected static final Integer    UPDATING  = Integer.valueOf(2);
	/** Internal state for UPDATING, if minimum one writeable column is involved. */
	protected static final Integer    WRITABLE_COLUMN_CHANGED  = Integer.valueOf(3);
	/** Internal state for DELETING. */
	protected static final Integer    DELETING  = Integer.valueOf(4);

	/** Internal state for changed detail rows. */
	protected static final Integer    DETAILS_CHANGED = Integer.valueOf(1);
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The corresponding<code>IDataPage</code> of the <code>IDataRow</code> in the <code>IDataBook</code>. */
	protected transient IDataPage	dpDataPage = null;
	
	/** The row index of the <code>IDataRow</code> in the <code>IDataBook</code>. */
	protected transient int		iRowIndex = -1;    
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Internal default constructor for the MemDataBook.
	 */
	protected ChangeableDataRow()
	{	
	}

	/**
	 * Constructs a <code>DataRow</code> with a given <code>IRowDefinition</code>.
	 * 
	 * @param pRowDefinition the <code>IRowDefinition</code>
	 */
	public ChangeableDataRow(IRowDefinition pRowDefinition)
	{
		this(pRowDefinition, null, null, -1);
	}
	
	/**
	 * Constructs a <code>DataRow</code> with a given <code>IRowDefinition</code> 
	 * and initialize it a copy of the <code>Object[]</code> data.
	 * 
	 * @param pRowDefinition the <code>IRowDefinition</code>
	 * @param pData the <code>Object[]</code> with data of the <code>DataRow</code>.
	 * @param pDataPage the corresponding<code>IDataPage</code> of the <code>IDataRow</code> in the <code>IDataBook</code>
	 * @param pRowIndex the row index of the <code>IDataRow</code> in the <code>IDataBook</code>
	 */
	protected ChangeableDataRow(IRowDefinition pRowDefinition, Object[] pData, IDataPage pDataPage, int pRowIndex)
	{
		super(pRowDefinition, pData);
		
		dpDataPage = pDataPage;
		iRowIndex  = pRowIndex;		
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


	/**
	 * {@inheritDoc}
	 */
	public IDataPage getDataPage()
	{
		return dpDataPage;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getRowIndex()
	{
		return iRowIndex;
	}
		
	/**
	 * {@inheritDoc}
	 */
	public Object getUID() throws ModelException
	{
		if (oaStorage != null)
		{
			int iCount = rdRowDefinition.getColumnCount();
			
			if (oaStorage.length > iCount)
			{
				return oaStorage[iCount];
			}
		}
		
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isInserting() throws ModelException
	{
		if (oaStorage == null)
		{
			return false;
		}
		int iCount = rdRowDefinition.getColumnCount() + 1; 
		if (oaStorage.length <= iCount)
		{
			return false;
		}			
		return oaStorage[iCount] == INSERTING;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isUpdating()  throws ModelException
	{
		if (oaStorage == null)
		{
			return false;
		}
		int iCount = rdRowDefinition.getColumnCount() + 1;
		if (oaStorage.length <= iCount)
		{
			return false;
		}			
		
		Object iState = oaStorage[iCount];
		return iState == UPDATING || iState == WRITABLE_COLUMN_CHANGED;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isDeleting() throws ModelException
	{
		if (oaStorage == null)
		{
			return false;
		}
		int iCount = rdRowDefinition.getColumnCount() + 1;
		if (oaStorage.length <= iCount)
		{
			return false;
		}			
		return oaStorage[iCount] == DELETING;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isDetailChanged() throws ModelException
	{
		if (oaStorage == null)
		{
			return false;
		}
		int iCount = rdRowDefinition.getColumnCount() + 2; 
		if (oaStorage.length <= iCount)
		{
			return false;
		}			
		
		Object oState = oaStorage[iCount];
		if (oState == null)
		{
			return false;
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Deprecated
	public IDataRow getOriginalRow() throws ModelException
	{
		return getOriginalDataRow();
	}

	/**
	 * {@inheritDoc}
	 */
	public IDataRow getOriginalDataRow() throws ModelException
	{
		if (oaStorage == null)
		{
			return null;
		}
		// copy the saved original row (the last columns in the Object[]) to the result		
		int iColumnCount = rdRowDefinition.getColumnCount();
		if (oaStorage.length <= iColumnCount)
		{
			return null;
		}			
		Object oState = oaStorage[iColumnCount + 1];
		if (oState == DELETING)
		{
			return this;
		}
		else if (oState == UPDATING || oState == WRITABLE_COLUMN_CHANGED)
		{
			Object[] oaResultRow = new Object[iColumnCount];
			if (oaStorage.length > INTERNAL_OFFSET + iColumnCount)
			{
				System.arraycopy(oaStorage, INTERNAL_OFFSET + iColumnCount, 
						         oaResultRow, 0, 
						         oaStorage.length - INTERNAL_OFFSET - iColumnCount);
			}
			return new DataRow(rdRowDefinition, oaResultRow);
		}
		else
		{
			return null;
		}
	}	

    /** 
     * {@inheritDoc}
     */
    public boolean isChanged() throws ModelException
    {
        return isDeleting() || isUpdating() || isInserting();
    }
    
	/** 
	 * {@inheritDoc}
	 */
	// #163 - IChangeableDataRow should support isWritableColumnChanged 
	public boolean isWritableColumnChanged() throws ModelException
	{
		if (oaStorage == null)
		{
			return false;
		}
		int iColCount = rdRowDefinition.getColumnCount() + 1;
		if (oaStorage.length <= iColCount)
		{
			return false;
		}			
		return oaStorage[iColCount] == WRITABLE_COLUMN_CHANGED;
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
		StringBuilder sbResult = new StringBuilder(super.toString());
		
		try
		{
			if (sbResult.length() > 2)
			{
				sbResult.insert(1, ", ");
			}
			
			sbResult.insert(1, "UID=" + getUID());

			Object change;
			if (isInserting())
			{
				change = "I";
			}
			else if (isDeleting())
			{
				change = "D";
			}
			else if (isUpdating())
			{
				change = "U";
			}
			else
			{
				change = " ";
			}
			Object detailsChange;
			if (isDetailChanged())
			{
				detailsChange = "DC";
			}
			else
			{
				detailsChange = "  ";
			}
			sbResult.insert(sbResult.length() - 1, ", CHANGES=" + change + detailsChange);
		}
		catch (ModelException me)
		{
			sbResult.append("\n");
			sbResult.append(ExceptionUtil.dump(me, true));
		}
		
		return sbResult.toString();
	}	
		
	/** 
	 * {@inheritDoc}
	 */
	@Override
	protected void setValueIntern(int pColumnIndex, Object pValue, ColumnDefinition pColumnDefinition) throws ModelException
	{
		super.setValueIntern(pColumnIndex, pValue, pColumnDefinition);
		
		// #646 - isWritableColumnChanged() is wrong in ChangeableDataRow 
		// just set it for pColumnName, not for all columns!
		setWritableColumnChanged(pColumnIndex, pColumnDefinition);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Sets the internal Unique Identifier for the ChangeableDataRow.
	 * 
     * @param pUID the new Unique Identifier
	 */
	protected void setUID(Object pUID)
	{
		extentStorage();
		oaStorage[rdRowDefinition.getColumnCount()] = pUID;
	}
	
	/**
	 * It marks this ChangeableDataRow as INSERTING.
	 */
	protected void setInserting()
	{
		oaStorage = null;
		extentStorage();
		oaStorage[rdRowDefinition.getColumnCount() + 1] = ChangeableDataRow.INSERTING;			
	}
	
	/**
	 * It marks this ChangeableDataRow as UPDATING.
	 */
	protected void setUpdating()
	{
		int iColumnCount = rdRowDefinition.getColumnCount();
		int size = iColumnCount * 2 + INTERNAL_OFFSET;

		if (oaStorage == null || oaStorage.length < size)
		{
			Object[] oaNewRow = new Object[size];
			if (oaStorage != null)
			{
				System.arraycopy(oaStorage, 0, oaNewRow, 0, oaStorage.length); // copy all the information!!!
			}
			// set new Object[] into the Row
			oaStorage = oaNewRow;
		}
		System.arraycopy(oaStorage, 0, oaStorage, INTERNAL_OFFSET + iColumnCount, iColumnCount);
		
		oaStorage[iColumnCount + 1] = UPDATING;
	}
	
	/**
	 * It marks this ChangeableDataRow as DELETING.
	 */
	protected void setDeleting()
	{
		extentStorage();
		oaStorage[rdRowDefinition.getColumnCount() + 1] = DELETING;			
	}
	
	/**
	 * It clears all changes in the ChangeableDataRow, and use 
	 * the original values before the changes. 
	 *
	 * @throws ModelException if isUpdate() couldn't correct determined.
	 */
	protected void restore() throws ModelException
	{
		int iColumnCount = rdRowDefinition.getColumnCount();

		if (isUpdating())
		{
			System.arraycopy(oaStorage, INTERNAL_OFFSET + iColumnCount, 
							 oaStorage, 0, 
					         iColumnCount);
		}
		for (int i = iColumnCount + INTERNAL_OFFSET; i < oaStorage.length; i++)
		{
			oaStorage[i] = null;
		}
		oaStorage[iColumnCount + 1] = null;
	}	

	/**
	 * It stores the changes in memory as stored. That means the changes will be made and
	 * the change state will be removed.
	 * 
	 * @throws ModelException	that the override methods could throw an exception
	 */
	protected void store() throws ModelException
	{
		// It clears all changes in the ChangeableDataRow, but prevents the actual values.
		int iColumnCount = rdRowDefinition.getColumnCount();

		for (int i = iColumnCount + INTERNAL_OFFSET; i < oaStorage.length; i++)
		{
			oaStorage[i] = null;
		}

		oaStorage[iColumnCount + 1] = null;
	}
	
	/**
	 * True, If under this row in the detail DataBook one or more changed 
	 * (isInserting/Updating/Deleting() == true) detail rows existing.
	 * 
	 * @param bChanged true if changed detail rows exists
	 */
	protected void setDetailChanged(boolean bChanged)
	{
		// set the detail change state correct.
		extentStorage();
		if (bChanged)
		{
			oaStorage[rdRowDefinition.getColumnCount() + 2] = DETAILS_CHANGED;				
		}
		else
		{
			// remove Details changed flag.
			oaStorage[rdRowDefinition.getColumnCount() + 2] = null;				
		}
	}

	/**
	 * It extends the internal storage, if the first change in the ChangeableDataRow 
	 * (is the DataBook itself) is made.
	 */
	private void extentStorage()
	{
		int iColumnCount = rdRowDefinition.getColumnCount();
		if (oaStorage == null || oaStorage.length <= iColumnCount)
		{
			// extend the AbstractStorage and copy the current values into it.
			Object[] oaNewRow = new Object[iColumnCount + INTERNAL_OFFSET];
			if (oaStorage != null)
			{
				System.arraycopy(oaStorage, 0, oaNewRow, 0, iColumnCount);
			}
			oaStorage = oaNewRow;
        }
	}
	
	/**
	 * It marks this ChangeableDataRow as WRITEABLE_COLUMN_CHANGED.
	 * 
	 * @param pColumnIndex the column index that has changed.
	 * @param pColumnDefinition the column definition.
	 * @throws ModelException if the column didn't found.
	 */
	private void setWritableColumnChanged(int pColumnIndex, ColumnDefinition pColumnDefinition) throws ModelException
	{
		if (oaStorage != null)
		{
			int iCount = rdRowDefinition.getColumnCount() + 1;
			if (oaStorage.length > iCount)
			{
				// #163 - IChangeableDataRow should support isWritableColumnChanged 
				// set the state here correct!
				if (oaStorage[iCount] == UPDATING && pColumnDefinition.isWritable())
				{
					oaStorage[iCount] = WRITABLE_COLUMN_CHANGED;
				}
			}
		}
	}
	
} 	// ChangeableDataRow
