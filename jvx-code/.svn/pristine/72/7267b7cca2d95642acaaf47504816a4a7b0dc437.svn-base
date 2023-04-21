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
 * 19.11.2008 - [RH] - creation
 * 20.11.2008 - [RH] - createFilter() for typical default Filters
 * 04.05.2009 - [RH] - createEqualsFilter(Object[],...) added
 * 14.07.2009 - [JR] - createEqualsFilter: meta data added and used for column positions
 * 18.10.2009 - [RH] - createEqualsFilter - if the ColumnNames size == 0 then return null, instead of an index out of bounds exception. [BUGFIX]
 * 11.12.2012 - [JR] - createFullTextFilter: pIncludedColumns added
 * 23.02.2013 - [JR] - toString implemented
 *                   - createCondition with removabel column names implemented
 * 24.11.2013 - [JR] - createEqualsFilter with additional parameter to ignore/allow null values
 * 25.04.2014 - [JR] - #1022: check mem filter  
 * 15.12.2021 - [JR] - more getEquals value methods (DataRow, Map) and search recursive                 
 */
package com.sibvisions.rad.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.rad.application.ApplicationUtil;
import javax.rad.io.IFileHandle;
import javax.rad.model.ColumnDefinition;
import javax.rad.model.IDataBook;
import javax.rad.model.IDataRow;
import javax.rad.model.ModelException;
import javax.rad.model.RowDefinition;
import javax.rad.model.condition.And;
import javax.rad.model.condition.CompareCondition;
import javax.rad.model.condition.ContainsIgnoreCase;
import javax.rad.model.condition.Equals;
import javax.rad.model.condition.GreaterEquals;
import javax.rad.model.condition.ICondition;
import javax.rad.model.condition.LessEquals;
import javax.rad.model.condition.Like;
import javax.rad.model.condition.LikeIgnoreCase;
import javax.rad.model.condition.Not;
import javax.rad.model.condition.OperatorCondition;
import javax.rad.model.condition.Or;
import javax.rad.model.datatype.BigDecimalDataType;
import javax.rad.model.datatype.BinaryDataType;
import javax.rad.model.datatype.BooleanDataType;
import javax.rad.model.datatype.IDataType;
import javax.rad.model.datatype.LongDataType;
import javax.rad.model.datatype.ObjectDataType;
import javax.rad.model.datatype.StringDataType;
import javax.rad.model.datatype.TimestampDataType;
import javax.rad.persist.ColumnMetaData;
import javax.rad.ui.celleditor.ILinkedCellEditor;

import com.sibvisions.rad.model.mem.DataRow;
import com.sibvisions.rad.model.mem.MemDataBook;
import com.sibvisions.rad.model.remote.RemoteDataBook;
import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.type.StringUtil;

/**
 * The <code>Filter</code> class provide helper functions to create combined  <code>ICondition</code>s.<br> 
 * 
 * @see javax.rad.model.condition.ICondition
 * @author Roland Hörmann, Martin Handsteiner
 */
public class Filter
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the not found object. */
    private static final Object NOTFOUND = new Object();
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
	/**
	 * Should not be used.
	 */
	protected Filter() 
	{ 
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Creates a Filter with AND combined LIKE conditions over all filter column names. 
     * 
     * @param pFilterDataRow        the IDataRow used for the Filter
     * @return an Filter with AND combined LIKE conditions over all filter column names.
     */
    public static ICondition createLikeFilter(IDataRow pFilterDataRow)
    {
        return createLikeFilter(pFilterDataRow, null);
    }
    
	/**
	 * Creates a Filter with AND combined LIKE conditions over all filter column names. 
	 * 
	 * @param pFilterDataRow		the IDataRow used for the Filter
	 * @param pFilterColumnNames	the Columns to use in the Filter
	 * @return an Filter with AND combined LIKE conditions over all filter column names.
	 */
	public static ICondition createLikeFilter(IDataRow pFilterDataRow, String[] pFilterColumnNames)
	{
        if (pFilterColumnNames == null)
        {
            pFilterColumnNames = getFilterableColumnNames(pFilterDataRow);
        }
        else if (pFilterColumnNames.length == 0)
        {
            return null;
        }
        
		ICondition cResult = new Like(pFilterDataRow, pFilterColumnNames[0]);
		
		for (int i = 1; i < pFilterColumnNames.length; i++)
		{
			cResult = cResult.and(new Like(pFilterDataRow, pFilterColumnNames[i]));
		}
		return cResult;
	}

    /**
     * Creates a Filter with AND combined LikeIgnoreCase conditions over all filter column names. 
     * 
     * @param pFilterDataRow        the IDataRow used for the Filter
     * @return an Filter with AND combined LikeIgnoreCase conditions over all filter column names.
     */
    public static ICondition createLikeIgnoreCaseFilter(IDataRow pFilterDataRow)
    {
        return createLikeIgnoreCaseFilter(pFilterDataRow, null);
    }
    
	/**
	 * Creates a Filter with AND combined LikeIgnoreCase conditions over all filter column names. 
	 * 
	 * @param pFilterDataRow		the IDataRow used for the Filter
	 * @param pFilterColumnNames	the Columns to use in the Filter
	 * @return an Filter with AND combined LikeIgnoreCase conditions over all filter column names.
	 */
	public static ICondition createLikeIgnoreCaseFilter(IDataRow pFilterDataRow, String[] pFilterColumnNames)
	{
        if (pFilterColumnNames == null)
        {
            pFilterColumnNames = getFilterableColumnNames(pFilterDataRow);
        }
        else if (pFilterColumnNames.length == 0)
        {
            return null;
        }
        
		ICondition cResult = new LikeIgnoreCase(pFilterDataRow, pFilterColumnNames[0]);
		
		for (int i = 1; i < pFilterColumnNames.length; i++)
		{
			cResult = cResult.and(new LikeIgnoreCase(pFilterDataRow, pFilterColumnNames[i]));
		}
		return cResult;
	}

    /**
     * Creates a Filter with AND combined EQUALS conditions over all filter column names. The new filter ignores
     * NULL values.
     * 
     * @param pFilterDataRow        the IDataRow used for the Filter
     * @return an Filter with AND combined EQUALS conditions over all filter column names.
     */
    public static ICondition createEqualsFilter(IDataRow pFilterDataRow)
    {
        return createEqualsFilter(pFilterDataRow, null, true);
    }
    
	/**
	 * Creates a Filter with AND combined EQUALS conditions over all filter column names. The new filter ignores
	 * NULL values.
	 * 
	 * @param pFilterDataRow		the IDataRow used for the Filter
	 * @param pFilterColumnNames	the Columns to use in the Filter
	 * @return an Filter with AND combined EQUALS conditions over all filter column names.
	 */
	public static ICondition createEqualsFilter(IDataRow pFilterDataRow, String[] pFilterColumnNames)
	{
		return createEqualsFilter(pFilterDataRow, pFilterColumnNames, true);
	}
	
	/**
	 * Creates a Filter with AND combined EQUALS conditions over all filter column names. 
	 * 
	 * @param pFilterDataRow		the IDataRow used for the Filter
	 * @param pFilterColumnNames	the Columns to use in the Filter
	 * @param pIgnoreNull           <code>true</code> to ignore null values (don't use columns with null value in filter)
	 * @return an Filter with AND combined EQUALS conditions over all filter column names.
	 */
	public static ICondition createEqualsFilter(IDataRow pFilterDataRow, String[] pFilterColumnNames, boolean pIgnoreNull)
	{
        if (pFilterColumnNames == null)
        {
            pFilterColumnNames = getFilterableColumnNames(pFilterDataRow);
        }
        else if (pFilterColumnNames.length == 0)
        {
            return null;
        }
        
        ICondition cResult = new Equals(pFilterDataRow, pFilterColumnNames[0], pIgnoreNull);
		
		for (int i = 1; i < pFilterColumnNames.length; i++)
		{
			cResult = cResult.and(new Equals(pFilterDataRow, pFilterColumnNames[i], pIgnoreNull));
		}
		return cResult;
	}
	
    /**
     * Creates a Filter with AND combined EQUALS conditions over all filter column names. 
     * 
     * @param pColumnNames        the column names
     * @param pValues             the values
     * @return an Filter with AND combined EQUALS conditions over all filter column names.
     */
    public static ICondition createEqualsFilter(String[] pColumnNames, Object[] pValues)
    {
        if (pColumnNames == null || pColumnNames.length == 0)
        {
            return null;
        }
        
        ICondition cResult = new Equals(pColumnNames[0], pValues == null || pValues.length == 0 ? null : pValues[0]);
        
        for (int i = 1; i < pColumnNames.length; i++)
        {
            cResult = cResult.and(new Equals(pColumnNames[i], pValues == null || pValues.length <= i ? null : pValues[i]));
        }
        return cResult;
    }
    
	/**
	 * Creates a Filter with AND combined EQUALS conditions over all filter column names. 
	 * 
	 * @param pColumnNames		the column names to use
	 * @param pDataRow			the values Object[] of the row to use.
	 * @param pColumnMetaData   the meta data for the columns used by the <code>pDataRow</code>
	 * @return an Filter with AND combined EQUALS conditions over all filter column names.
	 */
	public static ICondition createEqualsFilter(String[] pColumnNames, Object[] pDataRow, ColumnMetaData[] pColumnMetaData)
	{
		if (pColumnNames.length == 0)
		{
			return null;
		}
		
		int[] iColumnPos = new int[pColumnNames.length];
		int iPos;

		//search the positions of the columns in the meta data
		for (int i = 0, anz = pColumnMetaData.length; i < anz; i++)
		{
			iPos = ArrayUtil.indexOf(pColumnNames, pColumnMetaData[i].getName());
			
			if (iPos >= 0)
			{
				iColumnPos[iPos] = i;
			}
		}

		//use the correct column positions
		ICondition cPKFilter = new Equals(pColumnNames[0], pDataRow[iColumnPos[0]]);		
		for (int i = 1; i < pColumnNames.length; i++)
		{
			cPKFilter = cPKFilter.and(new Equals(pColumnNames[i], pDataRow[iColumnPos[i]]));
		}
		
		return cPKFilter;
	}	
	
	/**
	 * Creates an Filter with AND combined and for each <br>
	 * - column with the DataType String with LikeIgnoreCase condition and <br>
	 * - column with the DataType BigDecimal &amp; Boolean with Equals condition and <br>
	 * - for two column in order with the DataType Timestamp the first with GreaterEquals 
	 *   and the second with LessEquals condition and <br>
	 * - one column with the DataType Timestamp with Equals condition and <br>
	 * - column with an ILinkedCellEditor.setValidationEnabled(true) with Equals otherwise with a LikeIgnoreCase condition<br>
	 * over all columns. 
	 * 
	 * @param pFilterDataRow		the IDataRow used for the Filter
	 * @return an Filter with AND combined with a guessed default conditions over all columns.
	 */
	public static ICondition createFilter(IDataRow pFilterDataRow)
	{
		return createFilter(pFilterDataRow, null);
	}
	
	/**
	 * Creates an Filter with AND combined and for each <br>
	 * - column with the DataType String with LikeIgnoreCase condition and <br>
	 * - column with the DataType BigDecimal &amp; Boolean with Equals condition and <br>
	 * - for two column in order with the DataType Timestamp the first with GreaterEquals 
	 *   and the second with LessEquals condition and <br>
	 * - one column with the DataType Timestamp with Equals condition and <br>
	 * - column with an ILinkedCellEditor.setValidationEnabled(true) with Equals otherwise with a LikeIgnoreCase condition<br>
	 * over all filter column names. 
	 * 
	 * @param pFilterDataRow		the IDataRow used for the Filter
	 * @param pFilterColumnNames	the Columns to use in the Filter
	 * @return an Filter with AND combined with a guessed default conditions over all filter column names.
	 */
	public static ICondition createFilter(IDataRow pFilterDataRow, String[] pFilterColumnNames)
	{
		if (pFilterColumnNames == null)
		{
			pFilterColumnNames = getFilterableColumnNames(pFilterDataRow);
		}
		else if (pFilterColumnNames.length == 0)
		{
		    return null;
		}

		ICondition cResult  = null;
		ICondition cCurrent = null;
				
		for (int i = 0; i < pFilterColumnNames.length; i++)
		{
			int columnIndex = pFilterDataRow.getRowDefinition().getColumnDefinitionIndex(pFilterColumnNames[i]);
			ColumnDefinition cdColumn = pFilterDataRow.getRowDefinition().getColumnDefinition(columnIndex);
			IDataType       dtDataType = cdColumn.getDataType();
			
			if (dtDataType instanceof TimestampDataType
			    && i + 1 < pFilterColumnNames.length
			    && pFilterDataRow.getRowDefinition().getColumnDefinition(
			            pFilterDataRow.getRowDefinition().getColumnDefinitionIndex(pFilterColumnNames[i + 1])).getDataType() instanceof TimestampDataType)
			{
				// - for two column in order with the DataType Timestamp the first with GreaterEquals 
				//   and the second with LessEquals condition
				cCurrent = new GreaterEquals(pFilterDataRow, pFilterColumnNames[i]);
			}
			else if (dtDataType instanceof TimestampDataType
				     && cCurrent instanceof GreaterEquals)
			{
				// - for two column in order with the DataType Timestamp the first with GreaterEquals 
				//   and the second with LessEquals condition
				cCurrent = new LessEquals(pFilterDataRow, pFilterColumnNames[i]);
			}
			else if (dtDataType instanceof BigDecimalDataType
			    || dtDataType instanceof BooleanDataType
			    // - column with an ILinkedCellEditor.setValidationEnabled(true) with Equals
			    || dtDataType.getCellEditor() instanceof ILinkedCellEditor
			       && ((ILinkedCellEditor)dtDataType.getCellEditor()).isValidationEnabled()
			    // - one column with the DataType Timestamp with Equals condition
			    || dtDataType instanceof TimestampDataType)
			{
				cCurrent = new Equals(pFilterDataRow, pFilterColumnNames[i]);
			}
			else if (dtDataType instanceof StringDataType)
			{
				cCurrent = new LikeIgnoreCase(pFilterDataRow, pFilterColumnNames[i]);		
			}
			else
			{
				cCurrent = null;
			}

			if (cCurrent != null)
			{
				if (cResult == null)
				{
					cResult = cCurrent;
				}
				else
				{
					cResult = cResult.and(cCurrent);
				}
			}
		}
		
		return cResult;
	}
	
    /**
     * Extended full text filter, that supports containing all words and quotes. 
     * 
     * @param pDataBook the databook to search.
     * @param pSearchString the search string.
     * @param pIncludedColumns the list of column names for filtering or <code>null</code> to use all column names
     * @return The full text filter
     */
    public static ICondition createExtendedFullTextFilter(IDataBook pDataBook, String pSearchString, String... pIncludedColumns)
    {
        if (StringUtil.isEmpty(pSearchString))
        {
            return null;
        }
        else
        {
            List<String> quotedValues = StringUtil.separateList(pSearchString, "\"", true);
            LinkedHashSet<String> searchValues = new LinkedHashSet<String>();
	            
            boolean split = true;
            for (String part : quotedValues)
            {
                if (split)
                {
                    searchValues.addAll(StringUtil.separateList(part, " ", true));
                }
                else
                {
                    searchValues.add(part);
                }
                split = !split;
            }
	            
            And result = new And();
	            
            for (String searchValue : searchValues)
            {
                result.add(Filter.createFullTextFilter(pDataBook, searchValue, pIncludedColumns));
            }

            if (result.getConditions().length == 0)
            {
                return null;
            }
            else if (result.getConditions().length == 1)
            {
                return result.getConditions()[0];
            }
            else
            {
                return result;
            }
        }
    }

	
	/**
	 * Full text filter.
	 * 
	 * @param pDataBook the databook to search.
	 * @param pSearchString the search string.
	 * @param pIncludedColumns the list of column names for filtering or <code>null</code> to use all column names
	 * @return The full text filter
	 */
	public static ICondition createFullTextFilter(IDataBook pDataBook, String pSearchString, String... pIncludedColumns)
	{
		if (StringUtil.isEmpty(pSearchString))
		{
			return null;
		}
		else
		{
			boolean columnsAreSet = pIncludedColumns != null && pIncludedColumns.length > 0;
			
            String[] columnNames;
			if (columnsAreSet)
			{
				columnNames = pIncludedColumns;
			}
			else
			{
				columnNames = ApplicationUtil.getAllVisibleColumns(pDataBook); // pDataBook.getRowDefinition().getColumnNames();
			}
			
			boolean bIsMemFilter = false;
			
			if (pDataBook instanceof MemDataBook)
			{
			    if (pDataBook instanceof RemoteDataBook)
			    {
			        bIsMemFilter = ((RemoteDataBook)pDataBook).isMemFilter();
			    }
			    else
			    {
			        bIsMemFilter = true;
			    }
			}
			
			Or orCondition = new Or();
	
			for (String columnName : columnNames)
			{
				try
				{
					ColumnDefinition cdColumn = pDataBook.getRowDefinition().getColumnDefinition(columnName);
					
					if (!(cdColumn.getDataType() instanceof BinaryDataType)
						&& (cdColumn.isWritable() || bIsMemFilter)
						&& (columnsAreSet || cdColumn.isFilterable())) 
					{
						orCondition.add(new ContainsIgnoreCase(columnName, pSearchString));
					}
				}
				catch (ModelException e)
				{
					// do nothing
				}
			}
			
			return orCondition;
		}
	}

    /**
     * Gets all filterable columns of the data book.
     * 
     * @param pDataRow the data row
     * @return the filterable columns
     */
    public static String[] getFilterableColumnNames(IDataRow pDataRow)
    {
        return getFilterableColumnNames(pDataRow, null);
    }
    
	/**
	 * Gets all filterable columns of the data book.
	 * 
	 * @param pDataRow the data row
     * @param pColumnNames the column names base, or null in case of all column names.
	 * @return the filterable columns
	 */
	public static String[] getFilterableColumnNames(IDataRow pDataRow, String[] pColumnNames)
	{
	    if (pColumnNames == null)
	    {
	        pColumnNames = pDataRow.getRowDefinition().getColumnNames();
	    }
	    
        boolean bIsMemFilter = false;
        if (pDataRow instanceof MemDataBook)
        {
            if (pDataRow instanceof RemoteDataBook)
            {
                bIsMemFilter = ((RemoteDataBook)pDataRow).isMemFilter();
            }
            else
            {
                bIsMemFilter = true;
            }
        }

        ArrayList<String> result = new ArrayList<String>(pColumnNames.length);
        for (String columnName : pColumnNames)
        {
            try
            {
                ColumnDefinition cdColumn = pDataRow.getRowDefinition().getColumnDefinition(columnName);
                
                if (!(cdColumn.getDataType() instanceof BinaryDataType)
                    && (cdColumn.isWritable() || bIsMemFilter)
                    && cdColumn.isFilterable()) 
                {
                    result.add(columnName);
                }
            }
            catch (ModelException e)
            {
                // do nothing
            }
        }
        
        return result.toArray(new String[result.size()]);
	}
	
	/**
	 * Creates a new {@link ICondition} from the given {@link ICondition} and ignores or uses specific columns.
	 * 
	 * @param pCondition the condition
	 * @param pInclude <code>true</code> to include only given column, <code>false</code> to exclude given columns
	 * @param pColumns columns which should be ignored or used
	 * @return the condition without removed columns
	 */
	public static ICondition createCondition(ICondition pCondition, boolean pInclude, String... pColumns)
	{
		ICondition condition = null;
		
		if (pCondition instanceof CompareCondition)
		{
			CompareCondition cCompare = (CompareCondition)pCondition;
			
			String sColumnName = cCompare.getColumnName();
			
			if (pInclude)
			{
				if (ArrayUtil.contains(pColumns, sColumnName))
				{
					condition = cCompare;
				}
			}
			else
			{
				//ignore given columns!
				if (!ArrayUtil.contains(pColumns, sColumnName))
				{
					condition = cCompare;
				}
			}
		}
		else if (pCondition instanceof OperatorCondition)
		{			
			OperatorCondition cOperator = (OperatorCondition)pCondition;
						
			ICondition cond;
			
			ICondition[] caConditions = cOperator.getConditions();
			
			for (int i = 0; i < caConditions.length; i++)
			{
				cond = createCondition(caConditions[i], pInclude, pColumns);
							
				if (cond != null)
				{
					if (i > 0 && condition != null)
					{
						if (cOperator instanceof And)
						{
							condition = condition.and(cond);
						}
						else if (cOperator instanceof Or)
						{
							condition = condition.or(cond);
						}
					}
					else 
					{
						condition = cond;
					}
				}
			}
		}
		else if (pCondition instanceof Not)
		{
			ICondition cCond = ((Not)pCondition).getCondition();
			
			ICondition cond = createCondition(cCond, pInclude, pColumns);
		
			if (cond != null)
			{
				condition = new Not(cond);
			}
		}
		
		return condition;
	}     

    /**
     * Gets the value from the first {@link Equals} condition of the given filter.
     * 
     * @param pFilter the filter
     * @param pColumn the column name 
     * @return the value or <code>null</code> if the column was not found
     */
    public static Object getEqualsValue(ICondition pFilter, String pColumn)
    {
    	Object result = findEqualsValue(pFilter, pColumn);
    	
    	if (result == NOTFOUND)
    	{
    		return null;
    	}
    	else
    	{
    		return result;
    	}
    }
	
    /**
     *  the value from the first {@link Equals} condition of the given filter.
     * 
     * @param pCondition the filter
     * @param pColumn the column name 
     * @return the value or <code>null</code> if the column was not found
     */
    private static Object findEqualsValue(ICondition pCondition, String pColumn)
    {
    	if (pCondition == null)
    	{
    		return NOTFOUND;
    	}
    	
        if (pCondition instanceof OperatorCondition)
        {
        	Object result;
        	
            for (ICondition cond : ((OperatorCondition)pCondition).getConditions())
            {
            	result = findEqualsValue(cond, pColumn);
            	
            	if (result != NOTFOUND)
            	{
            		return result;
            	}
            }
        }
        else if (pCondition instanceof Equals)
        {
            if (pColumn.equals(((Equals)pCondition).getColumnName()))
            {
                return ((Equals)pCondition).getValue();
            }
        }
        
        return NOTFOUND;
    }	
    
	/**
	 * Gets all values of {@link Equals} conditions from the given condition. If multiple {@link Equals}
	 * values are available for the same column, the last one will be used.
	 *  
	 * @param pRow the row where the values should be stored. Only available columns will be used.
	 * @param pCondition the filter to use
	 * @return the given row if not <code>null</code> or a new row with all found values
	 * @throws Exception if value detection fails
	 */
	public static IDataRow getEqualsValues(IDataRow pRow, ICondition pCondition) throws Exception
	{
		Map<String, Object> hmpValues = getEqualsValues(pCondition);
		
		IDataRow row = pRow;
		
		if (row == null)
		{
			//create a custom row
			RowDefinition rowdef = new RowDefinition();

			Object value;
			
			IDataType dataType;
			
			for (Entry<String, Object> entry : hmpValues.entrySet())
			{
				value = entry.getValue();
				
				if (value instanceof String)
				{
					dataType = new StringDataType();
				}
				else if (value instanceof Long)
				{
					dataType = new LongDataType();
				}
				else if (value instanceof Number)
				{
					dataType = new BigDecimalDataType();
				}
				else if (value instanceof Boolean)
				{
					dataType = new BooleanDataType();
				}
				else if (value instanceof byte[]
						 || value instanceof IFileHandle)
				{
					dataType = new BinaryDataType();
				}
				else if (value instanceof Date)
				{
					dataType = new TimestampDataType();
				}
				else
				{
					dataType = new ObjectDataType();
				}
				
				rowdef.addColumnDefinition(new ColumnDefinition(entry.getKey(), dataType));
			}
			
			row = new DataRow(rowdef);
		}
		
		for (String column : row.getRowDefinition().getColumnNames())
		{
			row.setValue(column, hmpValues.get(column));
		}
		
		return row;
	}    
	
    /**
     * Gets all values of {@link Equals} conditions of the given filter. If multiple {@link Equals}
	 * values are available for the same column, the last one will be used.
     * 
     * @param pCondition the filter
     * @return pFilter the found values
     */
	public static Map<String, Object> getEqualsValues(ICondition pCondition)
	{
		HashMap<String, Object> hmpValues = new HashMap<String, Object>();
		
		findEqualsValues(hmpValues, pCondition);
		
		return hmpValues;
	}
	
    /**
     * Search all values of {@link Equals} conditions of the given filter. If multiple {@link Equals}
	 * values are available for the same column, the last one will be used.
     * 
     * @param pValues the values cache
     * @param pFilter the filter
     */
    private static void findEqualsValues(Map<String, Object> pValues, ICondition pFilter)
    {
    	if (pFilter == null)
    	{
    		return;
    	}
    	
        if (pFilter instanceof OperatorCondition)
        {
            for (ICondition cond : ((OperatorCondition)pFilter).getConditions())
            {
            	findEqualsValues(pValues, cond);
            }
        }
        else if (pFilter instanceof Equals)
        {
        	String sColumn = ((Equals)pFilter).getColumnName();
        	
        	if (sColumn != null)
        	{
        		pValues.put(sColumn, ((Equals)pFilter).getValue());
        	}
        }
    }	
	
    /**
     * Finds and removes the {@link ICondition} with the given column names from
     * the given {@link ICondition}. It adds the name and value to the given
     * {@link Map}.
     * <p>
     * Note that this function is subject to be changed in a future version, and
     * can there for not be considered stable.
     * 
     * @param pCondition the {@link ICondition} in which to search.
     * @param pNames the names of the conditions to search.
     * @param pNameToCondition the {@link Map} into which the {@link ICondition}
     *            is inserted.
     * @param pIgnoreCase whether the case of letters should be ignored.           
     * @return the changed condition.
     */
    public static ICondition findAndCreateReducedCondition(ICondition pCondition, Set<String> pNames, Map<String, CompareCondition> pNameToCondition, boolean pIgnoreCase)
    {
        if (pCondition instanceof OperatorCondition)
        {
            OperatorCondition operatorCondition = (OperatorCondition)pCondition;
            
            ICondition[] subConditions = operatorCondition.getConditions();
            
            for (int index = subConditions.length - 1; index >= 0; index--)
            {
                ICondition reducedSubCondition = findAndCreateReducedCondition(subConditions[index], pNames, pNameToCondition, pIgnoreCase);
                
                if (reducedSubCondition != null)
                {
                    subConditions[index] = reducedSubCondition;
                }
                else
                {
                    subConditions = ArrayUtil.remove(subConditions, index);
                }
            }

            if (subConditions.length == 0)
            {
                return null;
            }
            else if (subConditions.length == 1)
            {
                return subConditions[0];
            }
            else if (operatorCondition instanceof And)
            {
                return new And(subConditions);
            }
            else if (operatorCondition instanceof Or)
            {
                return new Or(subConditions);
            }
        }
        else if (pCondition instanceof CompareCondition)
        {
            CompareCondition compareCondition = (CompareCondition)pCondition;
            
            String sColumn = compareCondition.getColumnName();
            
            if (sColumn != null)
            {
                boolean found = false;
                
                if (pIgnoreCase)
                {
                	String sCurrentColumn;
                	
                	for (Iterator<String> it = pNames.iterator(); it.hasNext() && !found;)
                	{
                		sCurrentColumn = it.next();
                		
                		found = sCurrentColumn.equalsIgnoreCase(sColumn);
                		
                		if (found)
                		{
                			sColumn = sCurrentColumn;
                		}
                	}
                }
                else
                {
                	found = pNames.contains(sColumn);
                }
                
                if (found)
                {
                    ICondition cond = pNameToCondition.get(sColumn);
                    
                    if (cond == null || compareCondition instanceof Equals)
                    {
                        pNameToCondition.put(sColumn, compareCondition);
                    }
                    
                    return null;
                }
            }
            else
            {
                return null;
            }
        }
        else if (pCondition instanceof Not)
        {
            ICondition reducedCondition = findAndCreateReducedCondition(((Not)pCondition).getCondition(), pNames, pNameToCondition, pIgnoreCase);
            
            if (reducedCondition != null)
            {
                return new Not(reducedCondition);
            }
            else
            {
                return null;
            }
        }
        
        return pCondition;
    }

}	// Filter
