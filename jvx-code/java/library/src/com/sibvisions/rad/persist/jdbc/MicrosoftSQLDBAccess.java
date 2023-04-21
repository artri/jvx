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
 * 30.05.2009 - [RH] - creation.
 * 23.11.2009 - [RH] - ColumnMetaData && PrimaryKey Column is replaced with MetaData class
 * 02.03.2010 - [RH] - reorganized MetaData -> ServerMetaData, ColumnMetaData -> ServerColumnMetaData
 * 27.03.2010 - [JR] - #92: default value support
 * 09.10.2010 - [JR] - #114: used CheckConstraintSupport to detect allowed values    
 * 28.12.2010 - [RH] - #230: quoting of all DB objects like columns, tables, views.  
 * 11.03.2011 - [RH] - #308: DB specific automatic quoting implemented   
 * 21.07.2011 - [RH] - #437: MSSQLDBAccess should translate quote in addMSSQLSpecificAutoIncSupport
 * 14.09.2011 - [JR] - #470: changed default schema detection                 
 * 15.05.2014 - [JR] - #1038: CommonUtil.close used                            
 */
package com.sibvisions.rad.persist.jdbc;

/**
 * The <code>MicrosoftSQLDBAccess</code> is the implementation for MS SQL databases.<br>
 *  
 * @see com.sibvisions.rad.persist.jdbc.DBAccess
 * 
 * @author Martin Handsteiner
 */
public class MicrosoftSQLDBAccess extends MSSQLDBAccess
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of <code>MicrosoftSQLDBAccess</code> Object.
	 */
	public MicrosoftSQLDBAccess()
	{
        setDriver("com.microsoft.sqlserver.jdbc.SQLServerDriver"); 
	}
		
} 	// MicrosoftSQLDBAccess
