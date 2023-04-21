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
 * 30.11.2010 - [JR] - creation
 * 02.12.2010 - [JR] - don't use password in equals and hashCode, because it causes problems
 *                     after password changes (old/new credentials in combination with caching)
 * 03.12.2010 - [JR] - #200: allow empty password                     
 */
package com.sibvisions.rad.persist.jdbc;

import com.sibvisions.util.type.CommonUtil;

/**
 * The <code>DBCredentials</code> contains information to establish a 
 * database connection.
 * 
 * @author René Jahn
 */
public class DBCredentials
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the JDBC driver name/class. */
	private String sDriver;
	
	/** the db connect url. */
	private String sUrl;
	
	/** the db username. */
	private String sUserName;
	
	/** the db password. */
	private String sPassword;
	
	/** the calculated hascode. */
	private int iHash;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of <code>DBCredentials</code>.
	 * 
	 * @param pDriver the JDBC driver name/class
	 * @param pUrl the connection url
	 * @param pUserName the username
	 * @param pPassword the password
	 */
	public DBCredentials(String pDriver, String pUrl, String pUserName, String pPassword)
	{
		sDriver = getValue(pDriver, null);
		sUrl = getValue(pUrl, null);
		
		sUserName = getValue(pUserName, "");
		sPassword = getValue(pPassword, "");
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
		StringBuilder sb = new StringBuilder();
		
		sb.append("Driver = '");
		sb.append(sDriver);
		sb.append("', Url = '");
		sb.append(sUrl);
		sb.append("', Username = '");
		sb.append(sUserName);
		sb.append("', Password ");
		
		if (sPassword != null)
		{
			sb.append("is set");
		}
		else
		{
			sb.append("= null");
		}
		
		return sb.toString();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object pObject)
	{
		if (pObject != null && pObject instanceof DBCredentials)
		{
			//don't check password!
			return CommonUtil.equals(sDriver, ((DBCredentials)pObject).sDriver)
			       && CommonUtil.equals(sUrl, ((DBCredentials)pObject).sUrl)
			       && CommonUtil.equals(sUserName, ((DBCredentials)pObject).sUserName);
		}
		
		return false; 
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode()
	{
		if (iHash == 0)
		{
			iHash = 3;
			
			if (sDriver != null)
			{
				iHash = 31 * iHash + sDriver.hashCode();
			}
			
			if (sUrl != null)
			{
				iHash = 31 * iHash + sUrl.hashCode();
			}
			
			if (sUserName != null)
			{
				iHash = 31 * iHash + sUserName.hashCode();
			}

			//don't use password
		}
	
		return iHash;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the checked string value from an unchecked string value.
	 * 
	 * @param pValue an unchecked string value (<code>null</code>, empty or any other string)
	 * @param pEmptyValue the return value for empty values
	 * @return <code>null</code> if the unchecked value is <code>null</code> or empty. Otherwise
	 *         the unchecked value
	 */
	private static String getValue(String pValue, String pEmptyValue)
	{
		if (pValue == null)
		{
			return null;
		}
		
		if (pValue.trim().length() == 0)
		{
			return pEmptyValue;
		}
		
		return pValue;
	}
	
	/**
	 * Gets the JDBC driver name/class.
	 * 
	 * @return the full qualified name of the JDBC driver class
	 */
	public String getDriver()
	{
		return sDriver;
	}
	
	/**
	 * Gets the connection url. It contains hostname, port and database name.
	 * 
	 * @return the connection url
	 */
	public String getUrl()
	{
		return sUrl;
	}
	
	/**
	 * Gets the username, needed to establish a valid connection.
	 * 
	 * @return the username
	 */
	public String getUserName()
	{
		return sUserName;
	}
	
	/**
	 * Gets the password, needed to establish a valid connection.
	 * 
	 * @return the password
	 */
	public String getPassword()
	{
		return sPassword;
	}
	
}	// DBCredentials
