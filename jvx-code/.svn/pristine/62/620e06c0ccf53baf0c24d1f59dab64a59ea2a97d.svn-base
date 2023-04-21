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

import java.util.Arrays;

/**
 * It stores all relevant information to a Primary or Unique Key.
 *  
 * @author Roland Hörmann
 */
public class Key
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The key name. */
	private String sName;

	/** The key columns. */
	private Name[] naColumns;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Construct a Select cache object.
	 * 
	 * @param pName		The name of key.
	 * @param pColumns	The columns of key.
	 */
	public Key(String pName, Name[] pColumns)
	{
		sName = pName;
		naColumns = pColumns;
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
		
		sbResult.append("Name :: ");
		sbResult.append(sName);
		sbResult.append(", saColumns=");
		sbResult.append(Arrays.toString(naColumns));
		sbResult.append("\n");
		
		return sbResult.toString();
	}

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Returns the key columns.
	 *
	 * @return the key columns
	 */
	public Name[] getColumns()
	{
		return naColumns;
	}

	/**
	 * Returns the key name.
	 *
	 * @return the key name.
	 */
	public String getName()
	{
		return sName;
	}

}	// Key
