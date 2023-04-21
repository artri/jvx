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
 * 13.11.2008 - [RH] - clone() added
 * 08.04.2009 - [RH] - interface review - ISortDefintion interface removed
 * 28.04.2009 - [RH] - Constructors added
 * 15.03.2010 - [JR] - default constructor added for serialization support
 */
package javax.rad.model;

import java.io.Serializable;

/**
 * The <code>SortDefinition</code> class specifies the sort order to use.
 *
 * <br><br>Example:
 * <pre>
 * <code>
 * SortDefinition sSort = new SortDefinition(new String[] { "NAME"}, new boolean [] { true });
 * dbDataBook.setSort(sSort);
 * dbDataBook.open();
 * </code>
 * </pre>
 * 
 * @see com.sibvisions.rad.model.remote.RemoteDataBook
 * @see com.sibvisions.rad.persist.jdbc.DBAccess
 * 
 * @author Roland Hörmann
 */
public class SortDefinition implements Serializable,
                                       Cloneable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * The boolean <code>Array</code> which defines if the corresponding (same index)
	 * column name is sorted ascending (=true) or descending (=false).
	 */
	private String[]		saColumnNames;

	/** 
	 * The column names to sort. 
	 */
	private boolean[]		baAscending;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>SortDefinition</code>.
	 */
	public SortDefinition()
	{
	}
	
	/**
	 * Constructs a <code>SortDefinition</code> object with the column names 
	 * to sort and a default boolean <code>Array</code> which defines if the corresponding 
	 * (same index) is sorted ascending (=true).
	 * 
	 * @param pColumnNames
	 *            the column names to sort, as <code>String[]</code> 
	 */
	public SortDefinition(String... pColumnNames)
	{
		this(pColumnNames, null);
	}

	/**
	 * Constructs a <code>SortDefinition</code> object with the column names 
	 * to sort and the boolean to use for all columns to sort ascending (=true) or 
	 * descending (=false).
	 * 
	 * @param pColumnNames
	 *            the column names to sort, as <code>String[]</code> 
	 * @param pAscending
	 *            the boolean to use for all columns to sort ascending (=true) or 
	 * 			  descending (=false).
	 */
	public SortDefinition(boolean pAscending, String... pColumnNames)
	{
		saColumnNames = pColumnNames.clone();

		baAscending = new boolean[saColumnNames.length];
		for (int i = 0; i < baAscending.length; i++)
		{
			baAscending[i] = pAscending;
		}
	}

	/**
	 * Constructs a <code>SortDefinition</code> object with the column names 
	 * to sort , the boolean <code>Array</code> which defines if the corresponding 
	 * (same index) is sorted ascending (=true) or descending (=false).
	 * 
	 * @param pColumnNames
	 *            the column names to sort, as <code>String[]</code> 
	 * @param pAscending
	 *            the boolean <code>Array</code> which defines if the corresponding 
	 *            (same index) is sorted ascending (=true) or
	 *            descending (=false)
	 */
	public SortDefinition(String[] pColumnNames, boolean[] pAscending)
	{
		saColumnNames = pColumnNames.clone();

		if (pAscending == null)
		{
			baAscending = new boolean[saColumnNames.length];
			for (int i = 0; i < baAscending.length; i++)
			{
				baAscending[i] = true;
			}
		}
		else
		{
			baAscending = pAscending.clone();
		}
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Clone an <code>ISortDefinition</code>.
	 * 
	 * @see java.lang.Object#clone()
	 * 
	 * @return a clone of this <code>ISortDefinition</code>
	 */
	@Override
	public SortDefinition clone()
	{
		try
		{
			SortDefinition sd = (SortDefinition)super.clone();
			sd.baAscending = baAscending.clone();
			sd.saColumnNames = saColumnNames.clone();
							
			return sd;
		}
		catch (CloneNotSupportedException cloneNotSupportedException)
		{
			// should not occur!
			return null;
		}
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Returns the columns to sort.
	 * 
	 * @return the columns to sort.
	 */
	public String[] getColumns()
	{
		return saColumnNames;
	}

	/**
	 * Sets the columns to sort.
	 * 
	 * @param pColumnNames
	 * 				the columns to sort
	 */
	public void setColumns(String... pColumnNames)
	{
		saColumnNames = pColumnNames.clone();
	}

	/**
	 * Returns the boolean <code>Array</code> which defines if the corresponding 
	 * (same index) column is sorted ascending (=true) or descending (=false).
	 * 
	 * @return the boolean <code>Array</code> which defines if the corresponding 
	 * 	       (same index) column is sorted ascending (=true) or descending (=false).
	 */
	public boolean[] isAscending()
	{
		return baAscending;
	}

	/**
	 * Sets the the boolean <code>Array</code> which defines if the corresponding 
	 * (same index) column is sorted ascending (=true) or descending (=false).
	 * 
	 * @param pAscending
	 * 			the boolean <code>Array</code> which defines if the corresponding 
	 * 	        (same index) column is sorted ascending (=true) or descending (=false)
	 */
	public void setAscending(boolean... pAscending)
	{
		baAscending = pAscending.clone();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String toString()
	{
		if (saColumnNames == null)
		{
			return "[]";
		}
		else
		{
			StringBuilder sbResult = new StringBuilder();
			
			sbResult.append("[");
			
			for (int i = 0, cnt = saColumnNames.length; i < cnt; i++)
			{
				if (i > 0)
				{
					sbResult.append(", ");
				}
				
				sbResult.append(saColumnNames[i]);
				
				if (i < baAscending.length)
				{
					sbResult.append(" (");
					sbResult.append(baAscending[i] ? "ascending" : "descending");
					sbResult.append(")");
				}
				else
				{
					sbResult.append(" (?)");
				}
			}
			
			sbResult.append("]");
			
			return sbResult.toString();
		}
	}

} 	// SortDefinition
