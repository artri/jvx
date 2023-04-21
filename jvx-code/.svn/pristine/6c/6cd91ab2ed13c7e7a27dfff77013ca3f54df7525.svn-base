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
 * 02.11.2008 - [JR] - creation
 */
package com.sibvisions.rad.server.config;

import javax.rad.server.IConfiguration;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the <code>DBObjects</code> functionality.
 * 
 * @author René Jahn
 * @see DBObjects
 */
public class TestDBObjects
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Tests the getTableName and getColumnName methods.
	 * 
	 * @throws Exception if the test fails
	 */
	@Test
	public void testGetName() throws Exception
	{
		//check default configuration
		IConfiguration cfgApp = Configuration.getApplicationZone("demo").getConfig();
		
		Assert.assertEquals("USERS", DBObjects.getTableName(cfgApp, "USERS"));
		Assert.assertEquals("USERNAME", DBObjects.getColumnName(cfgApp, "USERS", "USERNAME"));
		Assert.assertEquals("PASSWORD", DBObjects.getColumnName(cfgApp, "USERS", "PASSWORD"));
		Assert.assertEquals("CHANGE_PASSWORD", DBObjects.getColumnName(cfgApp, "USERS", "CHANGE_PASSWORD"));
		Assert.assertEquals("VALID_FROM", DBObjects.getColumnName(cfgApp, "USERS", "VALID_FROM"));
		Assert.assertEquals("VALID_TO", DBObjects.getColumnName(cfgApp, "USERS", "VALID_TO"));
		Assert.assertEquals("ACTIVE", DBObjects.getColumnName(cfgApp, "USERS", "ACTIVE"));
		
		//check configuration via config.xml
		cfgApp = Configuration.getApplicationZone("democopy").getConfig();
		
		Assert.assertEquals("TBL_USERS", DBObjects.getTableName(cfgApp, "NUSERS"));
		Assert.assertEquals("USERNAME", DBObjects.getColumnName(cfgApp, "NUSERS", "USERNAME"));
		Assert.assertEquals("PASSWORD", DBObjects.getColumnName(cfgApp, "NUSERS", "PASSWORD"));
		Assert.assertEquals("CHANGE_PASSWORD", DBObjects.getColumnName(cfgApp, "NUSERS", "CHANGE_PASSWORD"));
		Assert.assertEquals("COL_VALID_FROM", DBObjects.getColumnName(cfgApp, "NUSERS", "VALID_FROM"));
		Assert.assertEquals("COL_VALID_TO", DBObjects.getColumnName(cfgApp, "NUSERS", "VALID_TO"));
		Assert.assertEquals("COL_ACTIVE", DBObjects.getColumnName(cfgApp, "NUSERS", "ACTIVE"));
	}
	
}	// TestDBObjects
