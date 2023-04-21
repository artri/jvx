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
 * 01.10.2008 - [HM] - creation
 * 17.11.2008 - [RH] - transients added, optimized
 * 19.11.2008 - [RH] - filter redesign
 */
package javax.rad.model.condition;

import javax.rad.model.IDataRow;
import javax.rad.model.IRowDefinition;
import javax.rad.model.ModelException;
import javax.rad.model.datatype.IDataType;

/**
 * Thats the base Class for all <code>CompareCondition</code>s.<br>
 * e.g. Equals, ...
 * 
 * @author Martin Handsteiner, Roland Hörmann
 */
public abstract class CompareCondition extends BaseCondition
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** The value to use for the compare. */
    private Object    			oValue;
    
	/** The column to use for the compare. */
	private String  			sColumnName;
	
    /** Determines, if null values will be ignored. */
    private boolean   			bIgnoreNull;
    
    /** The IDataRow to use for the compare. */
    private transient IDataRow  drDataRow;
    
    /** The column name in the IDataRow to use for the compare. */
    private transient String    sDataRowColumnName;

    /** The row definition used the last time, to optimize speed. */
    private transient IRowDefinition lastRowDefinition;

    /** The data type used the last time, to optimize speed. */
    private transient IDataType    lastDataType;

    /** The column index used the last time, to optimize speed. */
    private transient int    lastColumnIndex;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Constructs the base for a CompareCondition.
     */
    public CompareCondition()
    {
    }
    
    /**
     * Constructs the base for a CompareCondition, with a column, the value to check
     * and that null values will be ignored. setIgnoreNull(false)
     * 
     * @param pColumnName
     * 			the column name to use for the compare operation
     * @param pValue
     * 			the value to use in the compare.
     */
    public CompareCondition(String pColumnName, Object pValue)
    {
    	this(pColumnName, pValue, false);    	
    }

    /**
     * Constructs the base for a CompareCondition, with a column, the value to check
     * and if null values should be ignored.
     * 
     * @param pColumnName
     * 			the column name to use for the compare operation
     * @param pValue
     * 			the value to use in the compare.
     * @param pIgnoreNull
     * 			determines if null values should be ignored.
     */
    public CompareCondition(String pColumnName, Object pValue, boolean pIgnoreNull)
    {
    	oValue = pValue;
    	sColumnName = pColumnName;
    	bIgnoreNull = pIgnoreNull;
    	drDataRow = null;
    	sDataRowColumnName = pColumnName;
    }
    
    /**
     * Constructs the base for a CompareCondition, with a column, the IDataRow to check
     * and if null values should be ignored. It uses the column name of the 
     * ComparCondition for the column name in the IDataRow to get the value.
     * 
     * @param pDataRow
     * 			the IDataRow to use for the compare.
     * @param pColumnName
     * 			the column name to use for the compare operation
     */
    public CompareCondition(IDataRow pDataRow, String pColumnName)
    {
    	this(pDataRow, pColumnName, pColumnName, true);
    }
    
    /**
     * Constructs the base for a CompareCondition, with a column, the IDataRow to check
     * and that null values will be ignored. setIgnoreNull(true). It uses the column name 
     * of the ComparCondition for the column name in the IDataRow to get the value.
     * 
     * @param pDataRow
     * 			the IDataRow to use for the compare.
     * @param pColumnName
     * 			the column name to use for the compare operation
     * @param pIgnoreNull
     * 			determines if null values should be ignored.
     */
    public CompareCondition(IDataRow pDataRow, String pColumnName, boolean pIgnoreNull)
    {
    	this(pDataRow, pColumnName, pColumnName, pIgnoreNull);    
    }
    
    /**
     * Constructs the base for a CompareCondition, with a column, the IDataRow to check,
     * the column name to use in the IDataRow to get the value and that null values will 
     * be ignored. setIgnoreNull(true). 
     * 
     * @param pDataRow
     * 			the IDataRow to use for the compare.
     * @param pDataRowColumnName
     * 			the column name in the IDataRow to use for the compare. 
     * @param pColumnName
     * 			the column name to use for the compare operation
     */
    public CompareCondition(IDataRow pDataRow, String pDataRowColumnName, String pColumnName)
    {
    	this(pDataRow, pDataRowColumnName, pColumnName, true);
    }
    
    /**
     * Constructs the base for a CompareCondition, with a column, the IDataRow to check,
     * the column name to use in the IDataRow to get the value and if null values should 
     * be ignored.
     * 
     * @param pDataRow
     * 			the IDataRow to use for the compare.
     * @param pDataRowColumnName
     * 			the column name in the IDataRow to use for the compare. 
     * @param pColumnName
     * 			the column name to use for the compare operation
     * @param pIgnoreNull
     * 			determines if null values should be ignored.
     */
    public CompareCondition(IDataRow pDataRow, String pDataRowColumnName, String pColumnName, boolean pIgnoreNull)
    {
    	oValue = null;
    	drDataRow   	   = pDataRow;
    	sDataRowColumnName = pDataRowColumnName;
    	sColumnName 	   = pColumnName;
    	bIgnoreNull 	   = pIgnoreNull;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * It checks if the pValue fulfill the <code>ICondition</code>, under usage of
	 * the specified <code>IDataType</code>.
	 *  
	 * @param pDataType
	 * 			the <code>IDataType</code> to use for the compare operation 
	 * @param pValue
	 * 			the value to check against.
	 * @return true, if the <code>ICondition</code> is fulfilled.
	 */
	protected abstract boolean isFulfilled(IDataType pDataType, Object pValue);

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
    public boolean isFulfilled(IDataRow pDataRow)
	{
		if (bIgnoreNull && getValue() == null)
		{
			return true;
		}
		else
		{
			try
			{
				if (pDataRow.getRowDefinition() != lastRowDefinition)
				{
					lastRowDefinition = pDataRow.getRowDefinition();
					lastColumnIndex = lastRowDefinition.getColumnDefinitionIndex(sColumnName);
					if (lastColumnIndex < 0)
					{
						lastDataType = null;
					}
					else
					{
						lastDataType =  lastRowDefinition.getColumnDefinition(lastColumnIndex).getDataType();
					}
				}
				if (lastDataType == null)
				{
					return false;
				}
				else
				{
					return isFulfilled(lastDataType, pDataRow.getValue(lastColumnIndex));
				}
			}
			catch (ModelException modelException)
			{
				return false;
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
	public ICondition clone()
	{
	    Object value = getValue();
	    
	    if (value == null && bIgnoreNull)
	    {
	        return null;
	    }
	    else
	    {
    		CompareCondition iResult = (CompareCondition)super.clone();
    		
    		iResult.drDataRow = null;
    		iResult.sDataRowColumnName = null;
    		iResult.oValue = value;
    
    		return iResult;
	    }
	}	

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Returns the column name to use in the compare.
     * 
     * @return the column name to use in the compare.
     */
    public String getColumnName()
    {
    	return sColumnName;
    }
    
    /**
     * Sets the column name to use in the compare.
     * 
     * @param pColumnName
     * 			the column name to use in the compare.
     */
    public void setColumnName(String pColumnName)
    {
    	sColumnName = pColumnName;
    }
    
    /**
     * Returns the data row to use in the compare.
     * 
     * @return the data row to use in the compare.
     */
    public IDataRow getDataRow()
    {
    	return drDataRow;
    }
    
    /**
     * Returns the data row column name to use in the compare.
     * 
     * @return the data row column name to use in the compare.
     */
    public String getDataRowColumnName()
    {
    	return sDataRowColumnName;
    }
    
    /**
     * Returns the value to use in the compare.
     * 
     * @return the value to use in the compare.
     */
    public Object getValue()
    {
		if (drDataRow != null)
		{
			try
			{
				return drDataRow.getValue(sDataRowColumnName);				
			}
			catch (ModelException modelException)
			{
				throw new RuntimeException("The DataRow column '" + sDataRowColumnName + 
										   "' doesn't exist in DataRow - " + drDataRow);
			}
		}
		else
		{
			return oValue;
		}
    }
    
    /**
     * Sets the value to use in the compare.
     * 
     * @param pValue
     * 			the value to use in the compare.
     */
    public void setValue(Object pValue)
    {
    	if ("".equals(pValue))
    	{
    		oValue = null;
    	}
    	else
    	{
    		oValue = pValue;
    	}
    }
    
    /**
     * Returns if null values will be ignored.
     * 
     * @return if null values will be ignored.
     */
    public boolean isIgnoreNull()
    {
    	return bIgnoreNull;
    }
    
    /**
     * Sets if null values will be ignored.
     * 
     * @param pIgnoreNull
     * 			true, if null values will be ignored.
     */
    public void setIgnoreNull(boolean pIgnoreNull)
    {
    	bIgnoreNull = pIgnoreNull;
    }   
	
}	// CompareCondition
