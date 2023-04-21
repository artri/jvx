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
 * 05.10.2009 - [RH] - creation.
 * 27.03.2010 - [JR] - #92: default value support         
 * 28.04.2011 - [RH] - #341 -  LikeReverse Condition, LikeReverseIgnoreCase Condition   
 * 20.02.2020 - [DJ] - #2207: is alive query
 * 20.02.2020 - [JR] - #2128: support ignore-case conditions for non string columns                         
 */
package com.sibvisions.rad.persist.jdbc;

import java.util.Map;

import javax.rad.model.condition.CompareCondition;
import javax.rad.model.condition.LikeIgnoreCase;
import javax.rad.model.condition.LikeReverseIgnoreCase;
import javax.rad.model.datatype.StringDataType;
import javax.rad.persist.DataSourceException;

/**
 * The <code>DerbyDBAccess</code> is the implementation for Derby databases. Client driver version.<br>
 *  
 * @see com.sibvisions.rad.persist.jdbc.DBAccess
 * 
 * @author Roland Hörmann
 */
public class DerbyDBAccess extends DBAccess
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constants
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the select statement for alive check. */
	private static String sAliveSelect = "values 1";

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Constructs a new DerbyDBAccess Object.
	 */
	public DerbyDBAccess()
	{
		setDriver("org.apache.derby.jdbc.ClientDriver");
		// [RH] [HM] integrate embedded. 
//		setDriverName("org.apache.derby.jdbc.EmbeddedDriver");	
	}
		
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean supportsGetGeneratedKeys()
	{
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Map<String, Object> getDefaultValuesIntern(String pCatalog, String pSchema, String pTable) throws DataSourceException
	{
		return super.getDefaultValuesIntern(pCatalog, pSchema, pTable.toUpperCase());
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	protected String createReplace(String pSource, String pOld, String pNew) 
	{
		// Derby doesn't support any replace command. -> LikeReverseXXX doesn't support *->% replacement in DB!!!
		return pSource;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String createWhereColumn(ServerMetaData pServerMetaData, CompareCondition pCompare, String pColumnName)
	{
		String sColumn = super.createWhereColumn(pServerMetaData, pCompare, pColumnName);

		//#2128
		if (pCompare instanceof LikeIgnoreCase
			|| pCompare instanceof LikeReverseIgnoreCase)
		{
			try
			{
				ServerColumnMetaData scmd = pServerMetaData.getServerColumnMetaData(pCompare.getColumnName());
				
				if (scmd.getTypeIdentifier() != StringDataType.TYPE_IDENTIFIER)
				{
					return "CAST(CAST(" + sColumn + " AS CHAR(64)) AS VARCHAR(32672))";
				}
			}
			catch (Exception e)
			{
				debug(e);
			}
		}
		
		return sColumn;
	}	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
    public String getAliveQuery()
    {
        return sAliveSelect;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValidIdentifierStart(String pName)
    {
        return pName.length() > 0 && (Character.isJavaIdentifierStart(pName.charAt(0)) && pName.charAt(0) != '$');
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValidIdentifier(String pName)
    {
        if (isValidIdentifierStart(pName))
        {
            for (int i = 1, count = pName.length(); i < count; i++)
            {
                if (!Character.isJavaIdentifierPart(pName.charAt(i)) || pName.charAt(i) == '$')
                {
                    return false;
                }
            }
            
            return true;
        }
        else
        {
            return false;
        }
    }

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
} 	// DerbyDBAccess
