/*
 * Copyright 2016 SIB Visions GmbH
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
 * 07.06.2016 - [RZ] - creation.                          
 */
package com.sibvisions.rad.persist.jdbc;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.rad.persist.DataSourceException;

/**
 * The {@link H2DBAccess} is the implementation for the H2 database.
 * 
 * @see com.sibvisions.rad.persist.jdbc.DBAccess
 * 
 * @author Robert Zenz
 */
public class H2DBAccess extends DBAccess
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link H2DBAccess}.
	 */
	public H2DBAccess()
	{
		setDriver("org.h2.Driver");
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getColumnName(ResultSetMetaData pMetaData, int pColumn) throws SQLException
	{
		// We need to return the column label because H2 is JDBC compliant and
		// does set the column name as name, and not the label.
		// If we have "select A "B" from C" then "A" is the column name and
		// "B" is the column label.
		return pMetaData.getColumnLabel(pColumn);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Map<String, Object> getDefaultValuesIntern(String pCatalog, String pSchema, String pTable) throws DataSourceException
	{
		// We must convert the table name to uppercase to make sure that the
		// metadata for that table is loaded.
		return super.getDefaultValuesIntern(pCatalog, pSchema, pTable.toUpperCase());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected List<ForeignKey> getForeignKeysIntern(String pCatalog, String pSchema, String pTable) throws DataSourceException
	{
		// We must convert the table name to uppercase to make sure that the
		// metadata for that table is loaded.
		return super.getForeignKeysIntern(pCatalog, pSchema, pTable.toUpperCase());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void updateColumnsWithGeneratedKeys(Object[] pNewDataRow, ResultSet pInsert, ServerMetaData pMetaData) throws SQLException
	{
		ResultSetMetaData mdat = pInsert.getMetaData();

		Object[] oCopy = null;
		
		boolean bColumnMode = true;
		
		//if all columns are available in the metadata -> update columns by name
		for (int i = 1, cnt = mdat.getColumnCount(), idx; i < cnt && bColumnMode; i++)
		{
			idx = pMetaData.getServerColumnMetaDataIndex(mdat.getColumnName(i));
			
			if (idx >= 0)
			{
				if (oCopy == null)
				{
					oCopy = pNewDataRow.clone();					
				}
				
				oCopy[idx] = pInsert.getObject(i);
			}
			else
			{
				bColumnMode = false;
			}
		}
		
		if (bColumnMode && oCopy != null)
		{
			for (int i = 0; i < oCopy.length; i++)
			{
				pNewDataRow[i] = oCopy[i];
			}
		}
		else
		{
			super.updateColumnsWithGeneratedKeys(pNewDataRow, pInsert, pMetaData);
		}
	}
	
}	// H2DBAccess
