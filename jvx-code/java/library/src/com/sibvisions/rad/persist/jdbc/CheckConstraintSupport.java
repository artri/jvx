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
 * 09.10.2010 - [JR] - creation
 */
package com.sibvisions.rad.persist.jdbc;

import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;

import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.type.StringUtil;

/**
 * The <code>CheckConstraintSupport</code> is a utility class for check constraints. It
 * helps to parse the allowed values for columns. 
 * 
 * @author René Jahn
 */
class CheckConstraintSupport
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Invisible constructor, because the <code>CheckConstraintSupport</code> is a utility class.
	 */
	protected CheckConstraintSupport()
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Parse a check constraint condition and searches all possible values per column. It does not
	 * interpret expressions like (C1 = 1 AND (C2 = 1 OR C2 = 3)). This methods searches all containing columns
	 * and assigns the possible values. In our example: [C1: 1], [C2: 1, 3].
	 * 
	 * @param pCondition the check constraint condition
	 * @param pUpperCaseColumnName <code>true</code> to use upper case column names instead of preserve the case
	 * @return the mapping between column names and values. It is possible that a single check constraint uses
	 *         more than one column. <code>null</code> will be returned if the condition is <code>null</code>.
	 */
	public static Hashtable<String, List<String>> parseCondition(String pCondition, boolean pUpperCaseColumnName)
	{
		return parseCondition(pCondition, null, pUpperCaseColumnName);
	}
	
	/**
	 * Parse a check constraint condition and searches all possible values per column. It does not
	 * interpret expressions like (C1 = 1 AND (C2 = 1 OR C2 = 3)). This methods searches all containing columns
	 * and assigns the possible values. In our example: [C1: 1], [C2: 1, 3].
	 * It's possible to reuse a predefined mapping of columns and values. If additional values were found, then
	 * the mapping will be updated.
	 * 
	 * @param pCondition the check constraint condition
	 * @param pAllowedValues a predefined mapping of column names and values or <code>null</code> to start from scratch
	 * @param pUpperCaseColumnName <code>true</code> to use upper case column names instead of preserve the case
	 * @return the mapping between column names and values. It is possible that a single check constraint uses
	 *         more than one column. <code>null</code> will be returned if the condition is <code>null</code>.
	 */
	public static Hashtable<String, List<String>> parseCondition(String pCondition, Hashtable<String, List<String>> pAllowedValues, boolean pUpperCaseColumnName)
	{
		if (pCondition == null)
		{
			return null;
		}
		
		String sCondition = pCondition.toUpperCase();
		
		char[] ch = sCondition.toCharArray();

		ArrayUtil<String> auParts = new ArrayUtil<String>();
		
		int iStart = 0;
		
		//-----------------------------------------
		// Parse AND/OR parts
		//-----------------------------------------
		
		//don't start from 0 because before an OR/AND there must be a condition
		for (int i = 4, anz = sCondition.length() - 1; i < anz; i++)
		{
			if (ch[i - 3] == ' ' && ch[i - 2] == 'O' && ch[i - 1] == 'R' && ch[i] == ' ')
			{
				auParts.add(pCondition.substring(iStart, i - 3));
				iStart = i + 1;
			}
			else if (ch[i - 4] == ' ' && ch[i - 3] == 'A' && ch[i - 2] == 'N' && ch[i - 1] == 'D' && ch[i] == ' ')
			{
				auParts.add(pCondition.substring(iStart, i - 4));
				iStart = i + 1;
			}
		}
		
		auParts.add(pCondition.substring(iStart));

		//-----------------------------------------
		// Parse values and check keywoards
		//-----------------------------------------

		String sColumnName;
		
		ArrayUtil<String> auValues;
		List<String> auOldValues;
		
		char[] chRemove = new char[] {'(', ')', '[', ']', '{', '}'};
		
		Hashtable<String, List<String>> htResult;
		
		//reuse existing mapping, if available
		if (pAllowedValues == null)
		{
			htResult = new Hashtable<String, List<String>>();
		}
		else
		{
			htResult = pAllowedValues;
		}
		
		int iEnd;
		
		for (String sPart : auParts)
		{
			sColumnName = null;
			auValues = null;
			
			iStart = sPart.toUpperCase().indexOf(" IN ");
			
			if (iStart > 0)
			{
				//DETECTED: a value list

				sColumnName = sPart.substring(0, iStart);
				
				iStart = sPart.indexOf('(', iStart);
				
				if (iStart > 0)
				{
					iEnd = sPart.indexOf(')', iStart);
					
					if (iEnd > iStart)
					{
					    auValues = StringUtil.separateList(sPart.substring(iStart + 1, iEnd), ",", true);
					}
				}
			}
			else
			{
				iStart = sPart.indexOf('=');
				
				if (iStart > 0)
				{
					//DETECTED: a key value pair
					
					sColumnName = sPart.substring(0, iStart);

					auValues = new ArrayUtil<String>(1);
					auValues.add(sPart.substring(iStart + 1));
				}
			}
			
			if (sColumnName != null && auValues != null)
			{
				//-----------------------------------------
				// Format column name and values
				//-----------------------------------------
				
				sColumnName = StringUtil.removeCharacters(sColumnName, chRemove).trim();
				
				if (pUpperCaseColumnName)
				{
					sColumnName = sColumnName.toUpperCase();
				}
				
				auOldValues = (List<String>)htResult.get(sColumnName);
				
				if (auOldValues == null)
				{
					auOldValues = new ArrayUtil<String>();
					htResult.put(sColumnName, auOldValues);
				}
				
				for (String sValue : auValues)
				{
					sValue = sValue.trim();
					
					iStart = sValue.indexOf('\'');
					iEnd   = sValue.lastIndexOf('\'');
					
					if (iStart >= 0 && iStart < iEnd)
					{
						//if the value is a text 'X' then remove all before and after '
						sValue = sValue.substring(iStart, iEnd + 1);
					}
					else if (iStart < 0)
					{
						//not a text - remove bad brackets
						sValue = StringUtil.removeCharacters(sValue, chRemove);
					}
					
					//avoid duplicates
					if (auOldValues.indexOf(sValue) < 0)
					{
						auOldValues.add(sValue);
					}
				}
			}
		}
		
		return htResult;
	}
	
	/**
	 * Gets the column meta data for the given column name.
	 * @param pColumnMetaData the column meta data array.
	 * @param pColumnName the column name.
	 * @return the column meta data, or null if not found.
	 */
	public static ServerColumnMetaData getColumnMetaData(ServerColumnMetaData[] pColumnMetaData, String pColumnName)
	{
		for (ServerColumnMetaData columnMetaData : pColumnMetaData)
		{
			if (pColumnName.equals(columnMetaData.getName())
				//#1957
			    || pColumnName.equals(columnMetaData.getColumnName().getRealName()))
			{
				return columnMetaData;
			}
		}
		return null;
	}
	
	/**
	 * Translates parsed check constraint values to the column type.
	 * 
	 * @param pAccess the dbaccess.
	 * @param pColumnMetaData the column meta data
	 * @param pMapping the column/parsed values mapping
	 * @return the column/allowed values mapping. Contains only colums when all values were translated 
	 * @throws Exception
	 */
	public static Hashtable<String, Object[]> translateValues(DBAccess pAccess, ServerColumnMetaData[] pColumnMetaData, Hashtable<String, List<String>> pMapping)
	{
		if (pMapping == null)
		{
			return null;
		}
		
		Object[] oValues;
		
		List<String> liValues;
		
		String sColumn;
		
		Hashtable<String, Object[]> htAllowed = null;
		
		
		for (Entry<String, List<String>> entry : pMapping.entrySet())
		{
			sColumn = entry.getKey();
			liValues = entry.getValue();
			
			ServerColumnMetaData columnMetaData = getColumnMetaData(pColumnMetaData, sColumn);

			if (columnMetaData != null)
			{
				int iType = columnMetaData.getSQLType();
							
				ArrayUtil<Object> auObjectValues = new ArrayUtil<Object>(liValues.size());
				
				Object oValue;
				
				try
				{
					//ALL values must be translated to be sure that the check is VALID
					for (String sValue : liValues)
					{
						oValue = pAccess.translateValue(iType, StringUtil.removeQuotes(sValue, "'"));
						
						if (oValue != null)
						{
							auObjectValues.add(oValue);
						}
					}
					
					oValues = auObjectValues.toArray(new Object[auObjectValues.size()]);
					
					if (htAllowed == null)
					{
						htAllowed = new Hashtable<String, Object[]>();								
					}
					
					htAllowed.put(sColumn, oValues);
				}
				catch (Exception e)
				{
					DBAccess.debug("Can't translate check constraint values of '", sColumn, "'", e);
				}
			}
		}
		
		return htAllowed;
	}
	
}	// CheckConstraintSupport
