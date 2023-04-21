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
 * 14.11.2019 - [RH] - creation
 */
package javax.rad.model.condition;

import javax.rad.model.IDataRow;


/**
 * The <code>EndsWithIgnoreCase</code> condition is a <code>LikeIgnoreCase</code> condition with a leading *.
 * 
 * @author Martin Handsteiner
 */
public class EndsWithIgnoreCase extends LikeIgnoreCase
{
	
 	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>EndsWithIgnoreCase</code>.
	 */
	public EndsWithIgnoreCase() 
    {
    }
    
	/**
	 * Creates a new instance of <code>EndsWithIgnoreCase</code> with a defined compare value.
	 * 
	 * @param pColumnName the column name for the value comparison
	 * @param pValue the value to compare. % is used as wildcard symbol for any characters.
	 */
	public EndsWithIgnoreCase(String pColumnName, Object pValue) 
    {
    	super(pColumnName, pValue);
    }
    
	/**
	 * Creates a new instance of <code>EndsWithIgnoreCase</code> with a defined compare value. It's
	 * possible to compare null values if desired.
	 * 
	 * @param pColumnName the column name for the value comparison
	 * @param pValue the value to compare. % is used as wildcard symbol for any characters.
	 * @param pIgnoreNull true if null values are desired
	 */
    public EndsWithIgnoreCase(String pColumnName, Object pValue, boolean pIgnoreNull) 
    {
    	super(pColumnName, pValue, pIgnoreNull);
    }
    
    /**
     * Constructs a new EndsWithIgnoreCase Condition, with a column, the IDataRow to check
     * and if null values should be ignored. It uses the column name of the 
     * ComparCondition for the column name in the IDataRow to get the value.
     * 
     * @param pDataRow
     * 			the IDataRow to use for the compare.
     * @param pColumnName
     * 			the column name to use for the compare operation
     */
    public EndsWithIgnoreCase(IDataRow pDataRow, String pColumnName)
    {
    	super(pDataRow, pColumnName);
    }
    
    /**
     * Constructs a new EndsWithIgnoreCase Condition, with a column, the IDataRow to check
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
    public EndsWithIgnoreCase(IDataRow pDataRow, String pColumnName, boolean pIgnoreNull)
    {
    	super(pDataRow, pColumnName, pIgnoreNull);    
    }
    
    /**
     * Constructs a new EndsWithIgnoreCase Condition, with a column, the IDataRow to check,
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
    public EndsWithIgnoreCase(IDataRow pDataRow, String pDataRowColumnName, String pColumnName)
    {
    	super(pDataRow, pDataRowColumnName, pColumnName);
    }
    
    /**
     * Constructs a new EndsWithIgnoreCase Condition, with a column, the IDataRow to check,
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
    public EndsWithIgnoreCase(IDataRow pDataRow, String pDataRowColumnName, String pColumnName, boolean pIgnoreNull)
    {
    	super(pDataRow, pDataRowColumnName, pColumnName, pIgnoreNull);
    }
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Returns the value to use in the compare.
     * 
     * @return the value to use in the compare.
     */
    public Object getValue()
    {
    	Object search = super.getValue();
    	
    	if (search == null)
    	{
    		return null;
    	}
    	else if (search instanceof String)
		{
			if (search != searchValue)
			{
				searchTransformed = ((String)search).toLowerCase().replace('%', '*').replace('_', '?');
				if (!searchTransformed.startsWith("*"))
				{
					searchTransformed = "*" + searchTransformed;
				}
				
				searchValue = search;
			}
			return searchTransformed;
		}
    	else
		{
			if (search != searchValue)
			{
				searchTransformed = "*" + search.toString().toLowerCase();
				
				searchValue = search;
			}
			return searchTransformed;
		}
    }
	
}	// EndsWithIgnoreCase
