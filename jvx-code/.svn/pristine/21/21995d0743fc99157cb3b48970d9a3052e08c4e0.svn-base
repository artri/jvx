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
 * 03.03.2011 - [RH] - creation 
 * 11.03.2011 - [RH] - #308 - DB specific automatic quoting implemented   
 * 21.07.2011 - [RH] - #435 - jdbc.Name should have setters.          
 */
package com.sibvisions.rad.persist.jdbc;

/**
 * A <code>ServerColumnMetaData</code> is a description of the data type and other
 * attributes of a table storage column. (persists on server)
 * It also includes the server relevant infos, in addition to the <code>ColumnMetaData</code> just for the client.
 *  
 * @see javax.rad.model.datatype.IDataType
 * @see javax.rad.persist.ColumnMetaData
 * 
 * @author Roland Hörmann
 */
public class Name implements Cloneable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The Name to use in the application for that object. default is toUpperCase(). */
	private String			sName;
	
	/** The real name to use in the database for that object. e.g. real column name like DfDfD */
	private String			sRealName;
	
	/** The quoted name, if quoting is necessary. */
	private String          sQuotedName;
		
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Constructs a <code>Name</code> object.
	 */
	public Name()
	{
	}
	
	/**
	 * Constructs a <code>Name</code> with a specific real name.
	 * 
	 * @param pRealName 	the real name to use.
	 * @param pQuotedName 	the quoted name to use for the database.
	 */
	public Name(String pRealName, String pQuotedName)
	{
		sRealName = pRealName;
		
		if (sRealName != null)
		{
			sName = sRealName.toUpperCase();
		}
		
		sQuotedName = pQuotedName;
	}
		
	/**
	 * Constructs a <code>Name</code> with a specific real name.
	 * 
	 * @param pName 		the name.
	 * @param pRealName 	the real name to use.
	 * @param pQuotedName 	the quoted name to use for the database.
	 */
	public Name(String pName, String pRealName, String pQuotedName)
	{
		sName = pName;
		sRealName = pRealName;
		sQuotedName = pQuotedName;
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
		
		sbResult.append("RealName=");
		sbResult.append(sRealName);
		sbResult.append(",Name=");
		sbResult.append(sName);
		sbResult.append(",QuotedName=");
		sbResult.append(sQuotedName);
		
		return sbResult.toString();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Name clone()
	{
		try
		{
			return (Name)super.clone();
		}
		catch (CloneNotSupportedException cnse)
		{
		    // this shouldn't happen, since we are Cloneable
		    throw new InternalError();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object pObject)
	{
		if (pObject == this)
		{
			return true;
		}
		else if (pObject instanceof Name)
		{
			Name nTarget = (Name)pObject;
			
			if ((sName == null && nTarget.getName() == null)
					|| (sName != null && sName.equals(nTarget.getName())))
			{
				return true;
			}
		}
		return false;
	}	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode()
	{
		if (sName == null)
		{
			return 0;
		}
		else
		{
			return sName.hashCode();
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Sets the real name.
	 * 
	 * @param pRealName	the real name to use.
	 */
	public void setRealName(String pRealName)
	{
		sRealName = pRealName;
	}
	
	/**
	 * Returns the real name.
	 * 
	 * @return the real name
	 */
	public String getRealName()
	{
		return sRealName;
	}
	
	/**
	 * Sets the name use in the application.
	 * 
	 * @param pName	the name to us in the application
	 */
	public void setName(String pName)
	{
		sName = pName;
	}
	
	/**
	 * Returns the name to use in the application. default is toUpperCase().
	 * 
	 * @return the name to use in the application.
	 */
	public String getName()
	{
		return sName;
	}

	/**
	 * Sets the quoted name to use in the database.
	 * 
	 * @param pQuotedName	the quoted name to use in the database
	 */
	public void setQuotedName(String pQuotedName)
	{
		sQuotedName = pQuotedName;
	}
	
	/**
	 * Returns the quoted name to use in the database.
	 * 
	 * @return the quoted name to use in the database.
	 */
	public String getQuotedName()
	{
		return sQuotedName;
	}

	/**
	 * Returns true if the quoted name should be used.
	 * 
	 * @return true if the quoted name should be used.
	 */
	public boolean isQuoted()
	{
		return !sRealName.equals(sQuotedName);
	}
	
} 	// Name
