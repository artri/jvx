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
 * 01.10.2008 - [JR] - creation
 * 02.11.2008 - [JR] - support for userdefined configurations via config.xml
 * 10.11.2008 - [JR] - simple implementation of name detection
 * 10.05.2009 - [JR] - used IConfiguration instead of ApplicationZone
 */
package com.sibvisions.rad.server.config;

import javax.rad.server.IConfiguration;

/**
 * The <code>DBObjects</code> class declares global accessible names 
 * for database objects.
 * 
 * @author René Jahn
 */
public final class DBObjects
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the default yes value if not configured. */
	private static final String DEFAULT_YESVALUE = "Y";
	
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Invisible constructor, because the <code>DBObjects</code> class is a utility class.
	 */
	private DBObjects()
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the name of a table, as it is in the database. The name can be
	 * configured in the application configuration with the following option:
	 * <code>
	 *   &lt;application&gt;
	 *     &lt;databaseobjects&gt;
	 *       &lt;TABLENAME&gt;newname&lt;/TABLENAME&gt;
	 *     &lt;/databaseobjects&gt;
	 *   &lt;/application&gt;
	 * </code>
	 *
	 * @param pConfig the application configuration
	 * @param pTable the table alias
	 * @return the table name or null if the table is not available
	 * @throws Exception if the application configuration is invalid
	 */
	public static String getTableName(IConfiguration pConfig, String pTable) throws Exception
	{
		return pConfig.getProperty("/application/databaseobjects/" + pTable, pTable);
	}

	/**
	 * Gets the name of a column, as it is in the database. The name can be
	 * configured in the application configuration with the following option:
	 * <code>
	 *   &lt;application&gt;
	 *     &lt;databaseobjects&gt;
	 *       &lt;TABLENAME_COLUMNNAME&gt;newname&lt;/TABLENAME_COLUMNNAME&gt;
	 *     &lt;/databaseobjects&gt;
	 *   &lt;/application&gt;  
	 * </code>
	 *
	 * @param pConfig the application configuration
	 * @param pTable the table alias
	 * @param pColumn the column alias
	 * @return the column name or null if the column is not available
	 * @throws Exception if the application configuration is invalid
	 */
	public static String getColumnName(IConfiguration pConfig, String pTable, String pColumn) throws Exception
	{
		return pConfig.getProperty("/application/databaseobjects/" + pTable + "_" + pColumn, pColumn);
	}

	/**
	 * Gets the yes value for yes/no fields. The value can be configured in the application
	 * configuration with following option: 
	 * 
	 * <code>
	 *   &lt;application&gt;
	 *     &lt;databaseobjects&gt;
	 *       &lt;yesvalue&gt;X&lt;/yesvalue&gt;
	 *     &lt;/databaseobjects&gt;
	 *   &lt;/application&gt;
	 * </code> 
	 * 
	 * @param pConfig the application configuration
	 * @return the configured yes value for yes/no columns or {@link #DEFAULT_YESVALUE} value if 
	 *         the value is not configured. 
	 * @throws Exception if the application configuration is invalid
	 */
	public static String getYesValue(IConfiguration pConfig) throws Exception
	{
		return pConfig.getProperty("/application/databaseobjects/yesvalue", DEFAULT_YESVALUE);
	}
	
}	// DBObjects
