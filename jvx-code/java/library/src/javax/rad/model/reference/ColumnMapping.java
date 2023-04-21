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
 * 24.07.2009 - [HM] - creation
 * 16.02.2011 - [JR] - #287: clone fixed
 * 23.02.2013 - [JR] - equals and hashCode implemented
 */
package javax.rad.model.reference;

import java.io.Serializable;
import java.util.Arrays;

/**
 * The <code>ColumnMapping</code> class is for mapping different column names
 * between referenced tables.
 *  
 * @author Martin Handsteiner
 */
public class ColumnMapping implements Cloneable, 
                                      Serializable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The source column names of the <code>ColumnMapping</code>. */
	private String[] columnNames;

	/** The referenced column names of the <code>ColumnMapping</code>. */
	private String[] referencedColumnNames;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Constructs the <code>ColumnMapping</code> without column names.
	 */
	public ColumnMapping()
	{
		this(null);
	}
	
	/**
	 * Constructs the <code>ColumnMapping</code> with the same column names and referenced column names.
	 * 
	 * @param pColumnNames source column names of the <code>ColumnMapping</code>.
	 */
	public ColumnMapping(String[] pColumnNames)
	{
		setColumnNames(pColumnNames);
	}
	
	/**
	 * Constructs the <code>ColumnMapping</code> with the different column names and referenced column names.
	 * If the referenced column names are null, the are automatically mapped to the same name as the column names.
	 * 
	 * @param pColumnNames source column names of the <code>ColumnMapping</code>.
	 * @param pReferencedColumnNames referenced column names of the <code>ColumnMapping</code>.
	 */
	public ColumnMapping(String[] pColumnNames, String[] pReferencedColumnNames)
	{
		setColumnNames(pColumnNames);
		setReferencedColumnNames(pReferencedColumnNames);
	}
	
    /**
     * Constructs the <code>ColumnMapping</code> with the different column names and referenced column names.
     * If the referenced column names are null, the are automatically mapped to the same name as the column names.
     * 
     * @param pColumnName source column names of the <code>ColumnMapping</code>.
     * @param pReferencedColumnName referenced column names of the <code>ColumnMapping</code>.
     */
    public ColumnMapping(String pColumnName, String pReferencedColumnName)
    {
        if (pColumnName != null)
        {
            setColumnNames(new String[] {pColumnName});
        }
        
        if (pReferencedColumnName != null)
        {
            setReferencedColumnNames(new String[] {pReferencedColumnName});
        }
    }
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object pObject)
	{
		if (this == pObject)
		{
			return true;
		}
		
		if (pObject == null)
		{
			return false;
		}
		
		if (getClass() != pObject.getClass())
		{
			return false;
		}
		
		ColumnMapping cmpCompare = (ColumnMapping)pObject;
		
		if (!Arrays.equals(columnNames, cmpCompare.columnNames))
		{
			return false;
		}
		
		if (!Arrays.equals(referencedColumnNames, cmpCompare.referencedColumnNames))
		{
			return false;
		}
		
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode()
	{
		final int iPrime = 7;
		
		int iResult = iPrime + Arrays.hashCode(columnNames);
		iResult = iPrime * iResult + Arrays.hashCode(referencedColumnNames);
		
		return iResult;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the source column names of the <code>ColumnMapping</code>.
	 * This function returns never null, at least an empty <code>String[]</code>
	 * is returned.  
	 * 
	 * @return the source column names of the <code>ColumnMapping</code>.
	 */
	public String[] getColumnNames()
	{
		return columnNames;
	}
		
	/**
	 * Sets the source column names of the <code>ColumnMapping</code>.
	 * 
	 * @param pColumnNames the source column names of the <code>ColumnMapping</code>.
	 */
	public void setColumnNames(String[] pColumnNames)
	{
		if (pColumnNames == null)
		{
			columnNames = new String[] {};
		}
		else
		{
			columnNames = pColumnNames;
		}
		setReferencedColumnNames(referencedColumnNames);
	}

	/**
	 * Gets the referenced column names of the <code>ReferenceDefinition</code>.
	 * This function returns never null, at least an array with a length greater or equal
	 * to the length of the array of <code>getColumnNames</code> is returned.  
	 * 
	 * @return the referenced column names of the <code>ReferenceDefinition</code>.
	 */
	public String[] getReferencedColumnNames()
	{
		return referencedColumnNames;
	}

	/**
	 * Gets the corresponding referenced column name to the given column name.
	 * 
	 * @param pColumnName the column name.
	 * @return the referenced column name.
	 */
	public String getReferencedColumnName(String pColumnName)
	{
		if (columnNames.length == 0 && referencedColumnNames.length == 1)
		{
			return referencedColumnNames[0];
		}

		for (int i = 0; i < columnNames.length; i++)
		{
			if (pColumnName == columnNames[i] || pColumnName.equals(columnNames[i]))
			{
				return referencedColumnNames[i];
			}
		}
			
		return pColumnName;
	}
	
	/**
	 * Gets the corresponding column name to the given referenced column name.
	 * 
	 * @param pReferencedColumnName the column name.
	 * @return the column name.
	 */
	public String getColumnName(String pReferencedColumnName)
	{
		for (int i = 0, length = Math.min(referencedColumnNames.length, columnNames.length); i < length; i++)
		{
			if (pReferencedColumnName == referencedColumnNames[i] || pReferencedColumnName.equals(referencedColumnNames[i]))
			{
				return columnNames[i];
			}
		}
		return null;
	}
	
	/**
	 * Sets the referenced column names of the <code>ReferenceDefinition</code>.
	 * 
	 * @param pReferencedColumnNames the referenced column names of the <code>ReferenceDefinition</code>.
	 */
	public void setReferencedColumnNames(String[] pReferencedColumnNames)
	{
		if (pReferencedColumnNames == null)
		{
			referencedColumnNames = columnNames;
		}
		else if (pReferencedColumnNames.length < columnNames.length)
		{
			referencedColumnNames = new String[columnNames.length];
			
			System.arraycopy(pReferencedColumnNames, 0, referencedColumnNames, 0, pReferencedColumnNames.length);
			System.arraycopy(columnNames, pReferencedColumnNames.length, referencedColumnNames, pReferencedColumnNames.length, columnNames.length - pReferencedColumnNames.length);
		}
		else
		{
			referencedColumnNames = pReferencedColumnNames;
		}
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
		StringBuilder result = new StringBuilder();
		
		result.append("columnMapping={");
		for (int i = 0; i < columnNames.length; i++)
		{
			if (i > 0)
			{
				result.append(", ");
			}
			result.append(columnNames[i]);
			result.append("->");
			result.append(referencedColumnNames[i]);
		}
		result.append("}");

		return result.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ColumnMapping clone()
	{
		try
		{
			ColumnMapping result = (ColumnMapping)super.clone();
			
			if (columnNames != null)
			{
				result.columnNames = columnNames.clone();
			}
			
			if (referencedColumnNames != null)
			{
				result.referencedColumnNames = referencedColumnNames.clone();
			}
			
			return result;
		}
		catch (CloneNotSupportedException cnse)
		{
		    // this shouldn't happen, since we are Cloneable
		    throw new InternalError();
		}
	}
	
}	// ColumnMapping
