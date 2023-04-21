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
 * 28.04.2011 - [RH] - creation
 *                     #341 -  LikeReverse Condition, LikeReverseIgnoreCase Condition 
 */
package javax.rad.model.condition;

import javax.rad.model.IDataRow;
import javax.rad.model.datatype.IDataType;

import com.sibvisions.util.type.StringUtil;


/**
 * The <code>LikeReverse</code> condition implements the comparison of values with wildcards.
 * *,? is used as wildcard symbol for any characters. (? any character, * unlimited number of any characters)
 * The reverse version interchange the value with the value to compare. In that way you can use in the list of values to compare wildcards instead
 * in the value himself.
 * 
 * @author Roland H�rmann
 */
public class LikeReverse extends CompareCondition
{
 	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>LikeReverse</code>.
	 */
	public LikeReverse() 
    {
    }
    
	/**
	 * Creates a new instance of <code>LikeReverse</code> with a defined compare value.
	 * 
	 * @param pColumnName the column name for the value comparison
	 * @param pValue the value to compare. % is used as wildcard symbol for any characters.
	 */
	public LikeReverse(String pColumnName, Object pValue) 
    {
    	super(pColumnName, pValue);
    }
    
	/**
	 * Creates a new instance of <code>LikeReverse</code> with a defined compare value. It's
	 * possible to compare null values if desired.
	 * 
	 * @param pColumnName the column name for the value comparison
	 * @param pValue the value to compare. % is used as wildcard symbol for any characters.
	 * @param pIgnoreNull true if null values are desired
	 */
    public LikeReverse(String pColumnName, Object pValue, boolean pIgnoreNull) 
    {
    	super(pColumnName, pValue, pIgnoreNull);
    }
    
    /**
     * Constructs a new LikeReverse Condition, with a column, the IDataRow to check
     * and if null values should be ignored. It uses the column name of the 
     * ComparCondition for the column name in the IDataRow to get the value.
     * 
     * @param pDataRow
     * 			the IDataRow to use for the compare.
     * @param pColumnName
     * 			the column name to use for the compare operation
     */
    public LikeReverse(IDataRow pDataRow, String pColumnName)
    {
    	super(pDataRow, pColumnName);
    }
    
    /**
     * Constructs a new LikeReverse Condition, with a column, the IDataRow to check
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
    public LikeReverse(IDataRow pDataRow, String pColumnName, boolean pIgnoreNull)
    {
    	super(pDataRow, pColumnName, pIgnoreNull);    
    }
    
    /**
     * Constructs a new LikeReverse Condition, with a column, the IDataRow to check,
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
    public LikeReverse(IDataRow pDataRow, String pDataRowColumnName, String pColumnName)
    {
    	super(pDataRow, pDataRowColumnName, pColumnName);
    }
    
    /**
     * Constructs a new LikeReverse Condition, with a column, the IDataRow to check,
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
    public LikeReverse(IDataRow pDataRow, String pDataRowColumnName, String pColumnName, boolean pIgnoreNull)
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
		Object search = getValue();
		
		if (pValue == null && search == null) 
	    {
	    	return true;
	    }
	    else if (pValue == null || search == null) 
	    {
	    	return false;
	    }
	    else
	    {
			String value;
			if (pValue instanceof String)
			{
				value = ((String)pValue).replace('%', '*').replace('_', '?');
			}
			else
			{
				value = pDataType.convertToString(pValue).replace('%', '*').replace('_', '?');
			}
			if (search instanceof String)
			{
				return StringUtil.like((String)search, value); // reverse!!
			}
			else
			{
				return search.toString().equals(value);
			}
	    }
	}
	
}	// LikeReverse
