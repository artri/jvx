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
 * 03.12.2009 - [RH] - creation
 * 11.03.2011 - [RH] - #308 - DB specific automatic quoting implemented
 * 07.07.2011 - [RH] - #418 - getPK, getUKs, getFKs, getDefaultValues() fails, because catalog and schema is mixed up in mysql                              
 */
package com.sibvisions.rad.persist.jdbc;

import java.util.Arrays;

/**
 * It stores all relevant information, like FK Columns, PK Columns, etc. of an Foreign Key.
 *  
 * @author Roland Hörmann
 */
public class ForeignKey implements Cloneable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The primary key table. */
	private Name nPKTable;
	
	/** The catalog of the primary key table. */
	private Name nPKCatalog;
	
	/** The schema of the primary key table. */
	private Name nPKSchema;
		
	/** The primary key columns. */
	private Name[] naPKColumns;
	
	/** The foreign key columns. */
	private Name[] naFKColumns;

	/** The foreign key name. */
	private String sFKName;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Construct a Select cache object.
	 * 
	 * @param pPKTable		The primary key table
	 * @param pPKCatalog	The catalog of the primary key table
	 * @param pPKSchema		The schema of the primary key table
	 */
	public ForeignKey(Name pPKTable, Name pPKCatalog, Name pPKSchema)
	{
		nPKTable = pPKTable;
		nPKCatalog = pPKCatalog;
		nPKSchema = pPKSchema;
	}
	
	/**
	 * Construct a Select cache object.
	 * 
	 * @param pFKColumns				The foreign key columns
	 * @param pPKTable					The table
	 * @param pPKColumns				The the primary key columns
	 * @param pLinkReferenceColumns		The columns of interest
	 */
	public ForeignKey(String[] pFKColumns, String pPKTable, String[] pPKColumns, String[] pLinkReferenceColumns)
	{
		int index = pPKTable.indexOf('.');
		
		if (index < 0)
		{
			nPKTable = new Name(pPKTable, pPKTable);
			nPKCatalog = null;
			nPKSchema = null;
		}
		else
		{
			nPKTable = new Name(pPKTable.substring(index + 1), pPKTable.substring(index + 1));
			nPKCatalog = null;
			nPKSchema = new Name(pPKTable.substring(0, index), pPKTable.substring(0, index));
		}
		
		naFKColumns = createNames(pFKColumns);
		naPKColumns = createNames(pPKColumns);
	}
	
	/**
	 * Creates the name array.
	 * @param pNames the names.
	 * @return the names.
	 */
	private static Name[] createNames(String[] pNames)
	{
		Name[] result = new Name[pNames.length];
		
		for (int i = 0; i < result.length; i++)
		{
			result[i] = new Name(pNames[i], pNames[i]);
		}
		return result;
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
		
		sbResult.append("sPKTable :: ");
		sbResult.append(nPKTable);
		sbResult.append(", sPKCatalog=");
		sbResult.append(nPKCatalog);
		sbResult.append(", pPKSchema=");
		sbResult.append(nPKSchema);
		sbResult.append(", saPKColumns=");
		sbResult.append(Arrays.toString(naPKColumns));
		sbResult.append(", saFKColumns=");
		sbResult.append(Arrays.toString(naFKColumns));
		sbResult.append(", sFKName=");
		sbResult.append(sFKName);
		sbResult.append("\n");
		
		return sbResult.toString();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ForeignKey clone()
	{
		try
		{
			ForeignKey fk = (ForeignKey)super.clone();
		
			if (naFKColumns != null)
			{
				fk.naFKColumns = naFKColumns.clone();
			}
			
			if (naPKColumns != null)
			{
				fk.naPKColumns = naPKColumns.clone();
			}
			
			return fk;
		}
		catch (CloneNotSupportedException cnse)
		{
		    // this shouldn't happen, since we are Cloneable
		    throw new InternalError();
		}
	}
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Returns the primary key columns.
	 *
	 * @return the primary key columns
	 */
	public Name[] getPKColumns()
	{
		return naPKColumns;
	}

	/**
	 * Sets the primary key columns.
	 *
	 * @param pPKColumns 
	 *			the primary key columns to set
	 */
	public void setPKColumns(Name[] pPKColumns)
	{
		naPKColumns = pPKColumns;
	}

	/**
	 * Returns the foreign key columns.
	 *
	 * @return the foreign key columns.
	 */
	public Name[] getFKColumns()
	{
		return naFKColumns;
	}

	/**
	 * Sets the foreign key columns.
	 *
	 * @param pFKColumns 
	 *			the foreign key columns to set
	 */
	public void setFKColumns(Name[] pFKColumns)
	{
		naFKColumns = pFKColumns;
	}

	/**
	 * Returns the primary key table.
	 *
	 * @return the primary key table
	 */
	public Name getPKTable()
	{
		return nPKTable;
	}

	/**
	 * Sets the primary key table.
	 *
	 * @param pPKTable the primary key table
	 */
	public void setPKTable(Name pPKTable)
	{
		nPKTable = pPKTable;
	}

	/**
	 * Returns the catalog of the primary key table.
	 *
	 * @return the catalog of the primary key table
	 */
	public Name getPKCatalog()
	{
		return nPKCatalog;
	}

	/**
	 * Sets the primary key catalog name.
	 * 
	 * @param pPKCatalog the primary key catalog name.
	 */
	public void setPKCatalog(Name pPKCatalog) 
	{
		nPKCatalog = pPKCatalog;
	}	
	
	/**
	 * Returns the schema of the primary key table.
	 *
	 * @return the schema of the primary key table.
	 */
	public Name getPKSchema()
	{
		return nPKSchema;
	}
	
	/**
	 * Sets the primary key Schema name.
	 * 
	 * @param pPKSchema	the primary key schema name to use.
	 */
	public void setPKSchema(Name pPKSchema)
	{
		nPKSchema = pPKSchema;
	}

	/**
	 * Returns the foreign key name.
	 *
	 * @return the foreign key name.
	 */
	public String getFKName()
	{
		return sFKName;
	}

	/**
	 * Sets the foreign key name.
	 *
	 * @param pFKName 
	 *			the foreign key name.
	 */
	public void setFKName(String pFKName)
	{
		sFKName = pFKName;
	}

}	// ForeignKey
