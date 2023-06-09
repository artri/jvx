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
 * 11.10.2008 - [RH] - toString() optimized, (DataBook) checks changed to IDataBook
 * 02.11.2008 - [RH] - convertObjectToStorage and back removed, use IDataBook instead of DataBook
 *                     convertAndCheckToTypeClass add in setValue for correct DataType conversion 
 * 17.11.2008 - [RH] - createEmptyRow() optimized 
 * 09.04.2009 - [RH] - interface review - compareTo uses SortDefinition
 *                                        equals added
 *                                        getValue(int) added
 *                                        getValue(s) throw no ModelException anymore
 *                                        register,unregisterEditingControl removed
 *                                        clone renamed to createDataRow()
 *                                        IChangeableDataRow methods/functionality moved to ChangeableDataRow
 * 16.04.2009 - [RH] - remove/add/getDetailDataBooks moved to IDataBook.   
 *                     constructor optimized.   
 * 18.04.2009 - [RH] - get/set/DataPage/RowIndex moved to ChangeableDataRow                                 
 * 12.06.2009 - [JR] - toString: used StringBuilder [PERFORMANCE]
 * 30.03.2010 - [RH] - #6: getValueAsString in IDataRow   
 * 06.05.2010 - [JR] - getValuesAsString implemented
 * 31.03.2011 - [JR] - #318: add/removeControl forwarded to the IRowDefinition
 * 20.12.2012 - [RH] - setDefaultValues() is add (former createDataRow function in MemDataBook)
 * 03.04.2014 - [RZ] - #2 - added and implemented eventValuesChanged(String pColumnName)
 * 07.09.2015 - [JR] - #1461: hasControl implemented
 * 10.11.2015 - [JR] - #1516: check index in getValueIntern()
 * 16.09.2019 - [JR] - #2050: put/getObject implemented 
 */
package com.sibvisions.rad.model.mem;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.rad.model.ColumnDefinition;
import javax.rad.model.IDataRow;
import javax.rad.model.IRowDefinition;
import javax.rad.model.ModelException;
import javax.rad.model.RowDefinition;
import javax.rad.model.SortDefinition;
import javax.rad.model.event.DataRowEvent;
import javax.rad.model.event.DataRowHandler;
import javax.rad.model.ui.IControl;
import javax.rad.util.IObjectStore;

import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.type.ExceptionUtil;

/**
 * An <code>DataRow</code> is a list of table column's.<br>
 * The <code>DataRow</code> is also a storage independent row.<br>
 * 
 * <br>Example:
 * <pre>
 * <code>
 * // construct a RowDefinition
 * RowDefinition rdRowDefinition = new RowDefinition();
 * 
 * // construct some ColumnDefinitions
 * ColumnDefinition cdId   = new ColumnDefinition("id");
 * ColumnDefinition cdName = new ColumnDefinition("name");
 * 
 * rdRowDefinition.addColumnDefinition(cdId);		
 * rdRowDefinition.addColumnDefinition(cdName);
 *
 * // construct DataRow
 * DataRow drDataRow = new DataRow(rdRowDefinition);
 * 
 * drDataRow.setValue("id", new BigDecimal(1));
 * drDataRow.setValue("name", "The name");  
 * </code>
 * </pre>
 * 
 * @see javax.rad.model.IDataRow
 * @see javax.rad.model.RowDefinition
 * @see com.sibvisions.rad.model.remote.RemoteDataBook
 * @see com.sibvisions.rad.model.mem.MemDataPage
 * 
 * @author Roland H�rmann
 */
public class DataRow implements IDataRow, 
                                IObjectStore,
                                Serializable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The <code>RowDefinition</code> of this <code>DataRow</code>. */
	protected IRowDefinition	rdRowDefinition;

	/** The storage for this <code>DataRow</code>. */
	protected Object[]			oaStorage = null;

	/** The <code>ArrayUtil</code> of all <code>IRepaintListeners</code>. */
	private transient ArrayUtil<WeakReference<IControl>>	auControls;

    /** The map which maps column names to {@link DataRowHandler}s. */
    private transient Map<String, DataRowHandler> hmpEventValuesChanged;

    /** additional objects. */
	private transient HashMap<String, Object> hmObjects;
	
	/** whether dispatching events is enabled. */
	protected transient boolean bDispatchEventsEnabled = true;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Constructs a <code>DataRow</code> without a new instance of <code>IRowDefinition</code>.
	 */
	public DataRow()
	{		
		this(null, null);
	}
	
	/**
	 * Constructs a <code>DataRow</code> with a given <code>IRowDefinition</code>.
	 * 
	 * @param pRowDefinition the <code>IRowDefinition</code>
	 */
	public DataRow(IRowDefinition pRowDefinition)
	{
		this(pRowDefinition, null);
	}

	/**
	 * Constructs a <code>DataRow</code> with a given <code>IRowDefinition</code> 
	 * and initialize it a copy of the <code>Object[]</code> data.
	 * 
	 * @param pRowDefinition the <code>IRowDefinition</code>
	 * @param pData the <code>Object[]</code> with data of the <code>DataRow</code>.
	 */
	protected DataRow(IRowDefinition pRowDefinition, Object[] pData)
	{
		if (pRowDefinition == null)
		{
			rdRowDefinition = new RowDefinition();
		}
		else
		{
			rdRowDefinition = pRowDefinition;
		}
		oaStorage = pData;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public IRowDefinition getRowDefinition()
	{
		return rdRowDefinition;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object getValue(int pColumnIndex) throws ModelException
	{
		if (pColumnIndex < 0 || pColumnIndex >= rdRowDefinition.getColumnCount())
		{
			throw new ModelException("Column index '" + pColumnIndex + "' doesn't exist!");
		}		
		
		return getValueIntern(pColumnIndex);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Object getValue(String pColumnName) throws ModelException
	{
		int iColumnIndex = rdRowDefinition.getColumnDefinitionIndex(pColumnName);
		if (iColumnIndex < 0)
		{
			throw new ModelException("Column name '" + pColumnName + "' doesn't exist!");
		}		
		
		return getValueIntern(iColumnIndex);
	}

    /**
     * {@inheritDoc}
     */
    public Object getRawValue(int pColumnIndex) throws ModelException
    {
        if (pColumnIndex < 0 || pColumnIndex >= rdRowDefinition.getColumnCount())
        {
            throw new ModelException("Column index '" + pColumnIndex + "' doesn't exist!");
        }       
        
        return getRawValueIntern(pColumnIndex);
    }
    
    /**
     * {@inheritDoc}
     */
    public Object getRawValue(String pColumnName) throws ModelException
    {
        int iColumnIndex = rdRowDefinition.getColumnDefinitionIndex(pColumnName);
        if (iColumnIndex < 0)
        {
            throw new ModelException("Column name '" + pColumnName + "' doesn't exist!");
        }       
        
        return getRawValueIntern(iColumnIndex);
    }
    
    /**
     * {@inheritDoc}
     */
    public String getValueAsString(int pColumnIndex) throws ModelException
    {
        Object obj = getValue(pColumnIndex);
        return rdRowDefinition.getColumnDefinition(pColumnIndex).getDataType().convertToString(obj);
    }
    
	/**
	 * {@inheritDoc}
	 */
	public String getValueAsString(String pColumnName) throws ModelException
	{
		Object obj = getValue(pColumnName);
		return rdRowDefinition.getColumnDefinition(pColumnName).getDataType().convertToString(obj);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setValue(String pColumnName, Object pValue) throws ModelException
	{
		boolean hasListener = hmpEventValuesChanged != null;
		DataRowHandler dataRowHandler;
		DataRowHandler columnHandler;
		if (hasListener)
		{
			dataRowHandler = hmpEventValuesChanged.get(null);
			columnHandler = hmpEventValuesChanged.get(pColumnName);
			
			hasListener = DataRowHandler.isDispatchable(bDispatchEventsEnabled, dataRowHandler) || DataRowHandler.isDispatchable(bDispatchEventsEnabled, columnHandler);
		}
		else
		{
			dataRowHandler = null;
			columnHandler = null;
		}
		
		IDataRow drOldRow;
		if (hasListener)
		{
			drOldRow = this.createDataRow(null);
		}
		else
		{
			drOldRow = null;
		}

		int columnIndex = rdRowDefinition.getColumnDefinitionIndex(pColumnName);
		if (columnIndex < 0)
		{
			throw new ModelException("Column name doesn't exist! - " + pColumnName);
		}
		setValueIntern(columnIndex, pValue, rdRowDefinition.getColumnDefinition(columnIndex));
		
		if (hasListener)
		{
			String[] changedColumns = new String[] {pColumnName};
			if (DataRowHandler.isDispatchable(bDispatchEventsEnabled, dataRowHandler))
			{
				dataRowHandler.dispatchEvent(bDispatchEventsEnabled, new DataRowEvent(this, changedColumns, drOldRow));
			}
			
			if (DataRowHandler.isDispatchable(bDispatchEventsEnabled, columnHandler))
			{
				columnHandler.dispatchEvent(bDispatchEventsEnabled, new DataRowEvent(this, changedColumns, drOldRow));
			}
		}
		
		notifyRepaintControls();
	}

	/**
	 * {@inheritDoc}
	 */
	public Object[] getValues(String[] pColumnNames) throws ModelException
	{
		if (pColumnNames == null || pColumnNames == rdRowDefinition.getColumnNames())
		{
			Object[] values = new Object[rdRowDefinition.getColumnCount()];

			if (oaStorage != null)
			{
				for (int i = 0; i < values.length; i++)
				{
					values[i] = getValueIntern(i);
				}
			}
			
			return values;
		}
		else
		{
			Object[] values = new Object[pColumnNames.length];
			
			int[] columnIndexes = rdRowDefinition.getColumnDefinitionIndexes(pColumnNames);
			for (int i = 0; i < columnIndexes.length; i++)
			{
                int columnIndex = columnIndexes[i];
                if (columnIndex < 0)
                {
                    throw new ModelException("Column '" + pColumnNames[i] + 
                            "' doesn't exists in RowDefinition! - Check that the DataBook is open or the column is added before.");             
                }
                else if (oaStorage != null)
                {
                    values[i] = getValueIntern(columnIndex);
                }
			}
			
			return values;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String[] getValuesAsString(String[] pColumnNames) throws ModelException
	{
		String[] sColumnNames;
		
		if (pColumnNames == null)
		{
			sColumnNames = rdRowDefinition.getColumnNames();
		}
		else
		{
			sColumnNames = pColumnNames;
		}

		String[] sResult = new String[sColumnNames.length];
		
		for (int i = 0; i < sColumnNames.length; i++)
		{
			sResult[i] = getValueAsString(sColumnNames[i]);
		}
		
		return sResult;

	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setValues(String[] pColumnNames, Object[] pValues) throws ModelException
	{
		if (pColumnNames == null)
		{
			pColumnNames = rdRowDefinition.getColumnNames();
		}
		else if (pColumnNames.length == 0) // no event, if no column is set!
		{
			return;
		}
		
		boolean hasListener = hmpEventValuesChanged != null; //	&& hmpEventValuesChanged.containsKey(null);
		DataRowHandler dataRowHandler;
		if (hasListener)
		{
			dataRowHandler = hmpEventValuesChanged.get(null);
			
			hasListener = DataRowHandler.isDispatchable(bDispatchEventsEnabled, dataRowHandler);
			
			DataRowHandler columnHandler;
			for (int i = 0; !hasListener && i < pColumnNames.length; i++)
			{
				columnHandler = hmpEventValuesChanged.get(pColumnNames[i]);
				hasListener = DataRowHandler.isDispatchable(bDispatchEventsEnabled, columnHandler);
			}
		}
		else
		{
			dataRowHandler = null;
		}

		IDataRow drOldRow;
		if (hasListener)
		{
			drOldRow = this.createDataRow(null);
		}
		else
		{
			drOldRow = null;
		}
	
		setValuesIntern(pColumnNames, pValues);
		
		if (hasListener)
		{
			if (DataRowHandler.isDispatchable(bDispatchEventsEnabled, dataRowHandler))
			{
				dataRowHandler.dispatchEvent(bDispatchEventsEnabled, new DataRowEvent(this, pColumnNames, drOldRow));
			}

			for (String columnName : pColumnNames)
			{
				dataRowHandler = hmpEventValuesChanged.get(columnName);
				if (DataRowHandler.isDispatchable(bDispatchEventsEnabled, dataRowHandler))
				{
					dataRowHandler.dispatchEvent(bDispatchEventsEnabled, new DataRowEvent(this, pColumnNames, drOldRow));
				}
			}
		}
		
		notifyRepaintControls();		
	}

	/**
	 * {@inheritDoc}
	 */
	public IDataRow createDataRow(String[] pColumnNames) throws ModelException
	{
		return new DataRow(rdRowDefinition.createRowDefinition(pColumnNames), 
				getValuesIntern(pColumnNames));
	}

	/**
	 * {@inheritDoc}
	 */
	@Deprecated
	public IDataRow createEmptyRow(String[] pColumnNames) throws ModelException
	{
		return createEmptyDataRow(pColumnNames);
	}

	/**
	 * {@inheritDoc}
	 */
	public IDataRow createEmptyDataRow(String[] pColumnNames) throws ModelException
	{
		return new DataRow(rdRowDefinition.createRowDefinition(pColumnNames));
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean equals(Object pObject)
	{
		if (pObject instanceof IDataRow)
		{
			return compareTo((IDataRow)pObject) == 0;
		}
		else
		{
			return super.equals(pObject);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int compareTo(IDataRow pDataRow)
	{
		return compareTo(pDataRow, null);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int compareTo(IDataRow pDataRow, SortDefinition pSortDefinition)
	{
		if (pDataRow == null)
		{
			return 1;
		}
		
        IRowDefinition rowDef = pDataRow.getRowDefinition();
		String[] saColumnNames;
		boolean[] bAscending;
		if (pSortDefinition == null)
		{
		    if (rowDef.getColumnCount() > rdRowDefinition.getColumnCount())
		    {
		        saColumnNames = rowDef.getColumnNames();
		    }
		    else
		    {
		        saColumnNames = rdRowDefinition.getColumnNames();
		    }
			
			bAscending = null;
		}
		else
		{
			saColumnNames = pSortDefinition.getColumns();
			bAscending = pSortDefinition.isAscending();
		}
		int[] iaColumnIndexes = rdRowDefinition.getColumnDefinitionIndexes(saColumnNames);
		int[] iaDataRowColumnIndexes = rowDef.getColumnDefinitionIndexes(saColumnNames);
				
		for (int i = 0; i < iaColumnIndexes.length; i++)
		{
			int iColumnIndex = iaColumnIndexes[i];
			int iDataRowColumnIndex = iaDataRowColumnIndexes[i];
			
			if (iColumnIndex >= 0 || iDataRowColumnIndex >= 0)
			{
    			if (iColumnIndex < 0)
    			{
    			    return -1;
    			}
    			else if (iDataRowColumnIndex < 0)
    			{
    			    return 1;
    			}
    			else
    			{
    				try
    				{
    					int compare = rdRowDefinition.getColumnDefinition(iColumnIndex).getDataType().
    					                 compareTo(getValue(iColumnIndex), pDataRow.getValue(iaDataRowColumnIndexes[i]));
    					
    					if (compare != 0)
    					{
    						if (bAscending != null && i < bAscending.length && !bAscending[i])
    						{
    							return -compare;
    						}
    						else
    						{
    							return compare;
    						}
    					}
    				}
    				catch (ModelException modelException)
    				{
    					return -1;
    				}
    			}
			}
		}
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean equals(IDataRow pDataRow, String[] pColumnNames)
	{
		return compareTo(pDataRow, new SortDefinition(pColumnNames)) == 0;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void addControl(IControl pControl)
	{
		if (auControls == null)
		{
			auControls = new ArrayUtil<WeakReference<IControl>>();
		}
		else
		{
			for (int i = auControls.size() - 1; i >= 0; i--)
			{
				if (auControls.get(i).get() == null)
				{
					auControls.remove(i);
				}
			}
		}
		
		if (auControls.indexOfReference(pControl) < 0)
		{
			auControls.add(new WeakReference<IControl>(pControl));
		}
		
		//forward the control to the row definition
		rdRowDefinition.addControl(pControl);
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeControl(IControl pControl)
	{
		if (auControls != null)
		{
			for (int i = auControls.size() - 1; i >= 0; i--)
			{
				IControl control = auControls.get(i).get();
				if (control == null || control == pControl)
				{
					auControls.remove(i);
				}
			}
		}
		
		//forward the control to the row definition
		rdRowDefinition.removeControl(pControl);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public IControl[] getControls()
	{
		ArrayUtil<IControl> result = new ArrayUtil<IControl>();
		if (auControls != null)
		{
			for (int i = auControls.size() - 1; i >= 0; i--)
			{
				IControl control = auControls.get(i).get();
				if (control == null)
				{
					auControls.remove(i);
				}
				else
				{
					result.add(0, control);
				}
			}
		}
		return result.toArray(new IControl[result.size()]);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public DataRowHandler eventValuesChanged()
	{
		return eventValuesChanged(null);
	}

	/**
	 * {@inheritDoc}
	 */
	public DataRowHandler eventValuesChanged(String pColumnName)
	{
		if (hmpEventValuesChanged == null)
		{
			hmpEventValuesChanged = new HashMap<String, DataRowHandler>();
		}
		
		DataRowHandler drh = hmpEventValuesChanged.get(pColumnName);
		
        if (drh == null)
        {
            drh = new DataRowHandler();
            
            hmpEventValuesChanged.put(pColumnName, drh);
        }
        
        return drh;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void notifyRepaintControls()
	{		
		invokeRepaintListeners();
	}
		
	/**
	 * {@inheritDoc}
	 */
	public void saveEditingControls() throws ModelException
	{
		invokeSaveEditingControls();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void cancelEditingControls()
	{
		invokeCancelEditingControls();
	}
	
    /**
     * {@inheritDoc}
     */
    public boolean isDispatchEventsEnabled()
    {
        return bDispatchEventsEnabled;
    }
    
    /**
     * {@inheritDoc}
     */
    public void setDispatchEventsEnabled(boolean pEnabled)
    {
        bDispatchEventsEnabled = pEnabled;
    }

    /**
     * {@inheritDoc}
     */
    public Collection<String> getObjectNames()
    {
        if (hmObjects == null)
        {
            return Collections.emptySet();
        }
        else
        {
            return new ArrayList(hmObjects.keySet());
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public Object putObject(String pName, Object pValue)
    {
        if (pValue == null)
        {
            if (hmObjects == null)
            {
                return null;
            }
            else
            {
                Object o = hmObjects.remove(pName);
                
                if (hmObjects.isEmpty())
                {
                    hmObjects = null;
                }
                
                return o;
            }
        }
        else
        {
            if (hmObjects == null)
            {
                hmObjects = new HashMap<String, Object>();
            }
            
            return hmObjects.put(pName, pValue);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public Object getObject(String pName)
    {
        if (hmObjects == null)
        {
            return null;
        }
        
        return hmObjects.get(pName);
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
		StringBuilder sbResult = new StringBuilder();

		String[] sColumnNames = rdRowDefinition.getColumnNames();

		try
		{
			sbResult.append("{");

			for (int i = 0; i < sColumnNames.length; i++)
			{
				String columnName = sColumnNames[i];
				
				if (i > 0)
				{
					sbResult.append(", ");
				}
				sbResult.append(columnName);
				sbResult.append("=");
				sbResult.append(getValue(columnName));
			}
			
			sbResult.append("}");
		}
		catch (ModelException modelException)
		{
			sbResult.append("\n");
			sbResult.append(ExceptionUtil.dump(modelException, true));
		}
		
		return sbResult.toString();
	}	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode()
	{
		String[] sColumns = rdRowDefinition.getColumnNames();
		int h = 0;
        
		try
		{
			for (int i = 0; i < sColumns.length; i++)
			{
				h = (h * 37);
				Object oValue = getValue(sColumns[i]);
				if (oValue != null)
				{
					h += oValue.hashCode();
				}
			}
		}
		catch (ModelException modelException)
		{
			return h;
		}
		return h;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Gets the raw unprepared value intern.
     * @param pColumnIndex the column index
     * @return the prepared value.
     * @throws ModelException if it fails.
     */
    private final Object getRawValueIntern(int pColumnIndex) throws ModelException
    {
        if (pColumnIndex < 0 || pColumnIndex >= rdRowDefinition.getColumnCount())
        {
            return null;
        }
        
        if (oaStorage == null || pColumnIndex >= oaStorage.length)
        {
            return null;
        }
        else
        {
            return oaStorage[pColumnIndex];
        }
    }
    
	/**
	 * Gets and prepares the value intern.
	 * @param pColumnIndex the column index
	 * @return the prepared value.
	 * @throws ModelException if it fails.
	 */
	private final Object getValueIntern(int pColumnIndex) throws ModelException
	{
		Object value = getRawValueIntern(pColumnIndex);
		
	    if (value != null)
	    {
    		Object preparedValue = rdRowDefinition.getColumnDefinition(pColumnIndex).getDataType().prepareValue(value);
    		if (preparedValue != value)
    		{
    			if (preparedValue != null
    					&& value.getClass() != preparedValue.getClass())
    			{
    				oaStorage[pColumnIndex] = preparedValue; // Just store the new value, if the type changes.
    			}
    			return preparedValue;
    		}
	    }
		
		return value;
	}
	
	/**
	 * Sets the value of the named column in this <code>IDataRow</code>.
	 * 
	 * @param pColumnIndex
	 *            the column name
	 * @param pValue
	 *            the new value for the column in this <code>IDataRow</code>
	 * @param pColumnDefinition
	 *            the <code>ColumnDefinition</code>
	 * @throws ModelException 
	 *            if the column name is not in this <code>IDataRow</code>
	 *            or the pValue is not convertible/too large to/for the <code>IDataType</code> 
	 *            of the column     
	 */
	protected void setValueIntern(int pColumnIndex, Object pValue, ColumnDefinition pColumnDefinition) throws ModelException
	{
		if (pValue == null)
		{
			if (oaStorage != null && pColumnIndex < oaStorage.length)
			{
				oaStorage[pColumnIndex] = pValue;
			}
		}
		else
		{
			// if ColumnDefinition been added!! -> Extend AbstractStorage
			if (oaStorage == null || pColumnIndex >= oaStorage.length)
			{
				Object[] alNewStorage = new Object[rdRowDefinition.getColumnCount()];
				if (oaStorage != null)
				{
					System.arraycopy(oaStorage, 0, alNewStorage, 0, oaStorage.length);
				}
				oaStorage = alNewStorage;
			}
			
			oaStorage[pColumnIndex] = pColumnDefinition.getDataType().convertAndCheckToTypeClass(pValue);
		}
	}	
	
    /**
     * Gets the values without overhead.
     * @param pColumnNames the column names.
     * @return the values
     */
    private final Object[] getValuesIntern(String[] pColumnNames)
    {
        if (oaStorage == null)
        {
            return null;
        }
        else if (pColumnNames == null || pColumnNames == rdRowDefinition.getColumnNames())
        {
            return oaStorage.clone();
        }
        else
        {
            Object[] values = new Object[pColumnNames.length];
            int[] columnIndexes = rdRowDefinition.getColumnDefinitionIndexes(pColumnNames);
            for (int i = 0; i < pColumnNames.length; i++)
            {
                int index = columnIndexes[i];
                if (index >= 0 && index < oaStorage.length)
                {
                    values[i] = oaStorage[index];
                }
            }
            return values;
        }
    }

    
	/**
	 * It sets the values without throwing an event.
	 * 
	 * @param pColumnNames		the column names to use. 
	 * @param pValues			the values to use
	 * @throws ModelException	if the value couldn't set into the memory.
	 */
	protected void setValuesIntern(String[] pColumnNames, Object[] pValues) throws ModelException
	{		
		if (pColumnNames == null || pColumnNames == rdRowDefinition.getColumnNames())
		{
			if (pValues == null)
			{
				for (int i = 0, count = rdRowDefinition.getColumnCount(); i < count; i++)
				{
					setValueIntern(i, null, rdRowDefinition.getColumnDefinition(i));
				}
			}
			else
			{
				for (int i = 0, count = rdRowDefinition.getColumnCount(); i < count; i++)
				{
					setValueIntern(i, i < pValues.length ? pValues[i] : null, rdRowDefinition.getColumnDefinition(i));
				}
			}
		}
		else
		{
			int[] columnIndexes = rdRowDefinition.getColumnDefinitionIndexes(pColumnNames);
			
			if (pValues == null)
			{
				for (int i = 0; i < columnIndexes.length; i++)
				{
					int columnIndex = columnIndexes[i];
					if (columnIndex < 0)
					{
						throw new ModelException("Column name doesn't exist! - " + pColumnNames[i]);
					}
					else
					{
						setValueIntern(columnIndex, null, rdRowDefinition.getColumnDefinition(columnIndex));
					}
				}
			}
			else
			{
				for (int i = 0; i < columnIndexes.length; i++)
				{
					int columnIndex = columnIndexes[i];
					if (columnIndex < 0)
					{
						throw new ModelException("Column name doesn't exist! - " + pColumnNames[i]);
					}
					else
					{
						setValueIntern(columnIndex, i < pValues.length ? pValues[i] : null, rdRowDefinition.getColumnDefinition(columnIndex));
					}
				}
			}
		}
	} 

	/**
	 * It invokes for each <code>IComponent</code> the <code>notifyRepaint()</code> method.<br>
	 */
	protected void invokeRepaintListeners()
	{		
		if (auControls != null)
		{
			IControl[] controls = getControls();
			for (int i = 0; i < controls.length; i++)
			{
				controls[i].notifyRepaint();
			}
		}
	}
		
	/**
	 * It invokes for each <code>IComponent</code>  the <code>saveEditing()</code> method.
	 * @throws ModelException if saving the editors fails.
	 */
	protected void invokeSaveEditingControls() throws ModelException
	{
		if (auControls != null)
		{
			IControl[] controls = getControls();
			for (int i = 0; i < controls.length; i++)
			{
				controls[i].saveEditing();
			}
		}
	}
	
	/**
	 * It invokes for each <code>IComponent</code>  the <code>cancelEditing()</code> method.<br>
	 */
	protected void invokeCancelEditingControls()
	{
		if (auControls != null)
		{
			IControl[] controls = getControls();
			for (int i = 0; i < controls.length; i++)
			{
				controls[i].cancelEditing();
			}
		}
	}
	
	/**
	 * Sets the Default Values from the RowDefinition to the DataRow.
	 * 
	 * @throws ModelException if the set value fails
	 */
	public void setDefaultValues() throws ModelException
	{
		for (int i = 0, count = rdRowDefinition.getColumnCount(); i < count; i++)
		{
			ColumnDefinition cd = rdRowDefinition.getColumnDefinition(i);
			Object defaultValue = cd.getDefaultValue();
			if (defaultValue != null)
			{
				setValueIntern(i, defaultValue, cd);
			}
		}
	}
	
	/**
	 * Gets whether at least one control was added to this row.
	 * 
	 * @return <code>true</code> if this row has at least one control
	 */
    public boolean hasControls()
    {
        if (auControls != null)
        {
            for (int i = auControls.size() - 1; i >= 0; i--)
            {
                //cleanup as long as no control was found
                IControl control = auControls.get(i).get();
                if (control == null)
                {
                    auControls.remove(i);
                }
                else
                {
                    return true;
                }
            }
        }
        
        return false;
    }
    
} 	// DataRow
