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
 */
package com.sibvisions.rad.persist.jdbc;

/**
 * It stores all relevant information to a Table.
 *  
 * @author Martin Handsteiner
 */
public class TableInfo
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the catalog name. */
	private String sCatalog = null;
	
	/** the schema name. */
	private String sSchema = null;
	
	/** the table name. */
	private String sTable = null;
	
	/**
	 * Constructs a new TableInfo.
	 * @param pCatalog the catalog
	 * @param pSchema the schema
	 * @param pTable the table
	 */
	public TableInfo(String pCatalog, String pSchema, String pTable)
	{
		sCatalog = pCatalog;
		sSchema = pSchema;
		sTable = pTable;
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
        
        sbResult.append("TableInfo: Catalog=");
        sbResult.append(sCatalog);
        sbResult.append(", Schema=");
        sbResult.append(sSchema);
        sbResult.append(", Table=");
        sbResult.append(sTable);
        
        return sbResult.toString();
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
	/**
	 * Gets the catalog name.
	 * 
	 * @return the catalog name
	 */
	public String getCatalog()
	{
		return sCatalog;
	}

	/**
	 * Gets the schema name.
	 * 
	 * @return the schema
	 */
	public String getSchema()
	{
		return sSchema;
	}
	
	/**
	 * Gets the table name.
	 * 
	 * @return the table name
	 */
	public String getTable()
	{
		return sTable;
	}
	
}	// TableInfo	

