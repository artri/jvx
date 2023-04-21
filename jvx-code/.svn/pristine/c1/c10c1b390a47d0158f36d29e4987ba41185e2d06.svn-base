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
 * 01.10.2008 - [MH] - creation
 * 17.11.2008 - [RH] - new constructors for IDataRow and ColumnName added
 * 18.11.2008 - [RH] - class simplified
 */
package javax.rad.model.condition;

import javax.rad.model.IDataRow;
import javax.rad.model.datatype.IDataType;

/**
 * The <code>Equals</code> condition implements the comparison of values.
 * 
 * @author Martin Handsteiner
 */
public class Equals extends CompareCondition
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>Equals</code>.
	 */
	public Equals() 
    {
    }
    
	/**
	 * Creates a new instance of <code>Equals</code> with a defined compare value.
	 * 
	 * @param pColumnName the column name for the value comparison
	 * @param pValue the value to compare 
	 */
	public Equals(String pColumnName, Object pValue) 
    {
    	super(pColumnName, pValue);
    }
    
	/**
	 * Creates a new instance of <code>Equals</code> with a defined compare value. It's
	 * possible to compare null values if desired.
	 * 
	 * @param pColumnName the column name for the value comparison
	 * @param pValue the value to compare
	 * @param pIgnoreNull true if null values are desired
	 */
    public Equals(String pColumnName, Object pValue, boolean pIgnoreNull) 
    {
    	super(pColumnName, pValue, pIgnoreNull);
    }
    
    /**
     * Constructs a new Equals Condition, with a column, the IDataRow to check
     * and if null values should be ignored. It uses the column name of the 
     * ComparCondition for the column name in the IDataRow to get the value.
     * 
     * @param pDataRow
     * 			the IDataRow to use for the compare.
     * @param pColumnName
     * 			the column name to use for the compare operation
     */
    public Equals(IDataRow pDataRow, String pColumnName)
    {
    	super(pDataRow, pColumnName);
    }
    
    /**
     * Constructs a new Equals Condition, with a column, the IDataRow to check
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
    public Equals(IDataRow pDataRow, String pColumnName, boolean pIgnoreNull)
    {
    	super(pDataRow, pColumnName, pIgnoreNull);    
    }
    
    /**
     * Constructs a new Equals Condition, with a column, the IDataRow to check,
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
    public Equals(IDataRow pDataRow, String pDataRowColumnName, String pColumnName)
    {
    	super(pDataRow, pDataRowColumnName, pColumnName);
    }
    
    /**
     * Constructs a new Equals Condition, with a column, the IDataRow to check,
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
    public Equals(IDataRow pDataRow, String pDataRowColumnName, String pColumnName, boolean pIgnoreNull)
    {
    	super(pDataRow, pDataRowColumnName, pColumnName, pIgnoreNull);
    }
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
    @Override
	protected boolean isFulfilled(IDataType pDataType, Object pValue)
	{
		return pDataType.compareTo(pValue, getValue()) == 0;
	}
	
}	// Equals
