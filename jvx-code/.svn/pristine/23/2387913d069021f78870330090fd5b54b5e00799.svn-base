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
 * 01.12.2010 - [JR] - creation
 */
package com.sibvisions.rad.server.security;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sibvisions.rad.IPackageSetup;
import com.sibvisions.rad.persist.jdbc.DBCredentials;
import com.sibvisions.rad.server.config.Configuration;
import com.sibvisions.util.xml.XmlNode;

/**
 * Tests the functionality of {@link DataSourceHandler}.
 * 
 * @author René Jahn
 */
public class TestDataSourceHandler
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Reset configuration before each test.
	 */
	@Before
	public void beforeTest()
	{
		System.setProperty(IPackageSetup.CONFIG_BASEDIR, "");
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Tests {@link DataSourceHandler#create(XmlNode)}.
	 */
	@Test
	public void testCreateWithXml()
	{
		XmlNode xmn = new XmlNode("database");
		
		xmn.setNode("/driver", "DRIVER");
		xmn.setNode("/url", "URL");
		xmn.setNode("/username", "USERNAME");
		xmn.setNode("/password", "PASSWORD");
		
		DBCredentials cred = DataSourceHandler.createDBCredentials(xmn);
		
		Assert.assertEquals("DRIVER", cred.getDriver());
		Assert.assertEquals("URL", cred.getUrl());
		Assert.assertEquals("USERNAME", cred.getUserName());
		Assert.assertEquals("PASSWORD", cred.getPassword());
		
		xmn.setNode("/driver", "  ");
		
		cred = DataSourceHandler.createDBCredentials(xmn);
		
		Assert.assertNull(cred.getDriver());
		
		xmn.removeNode("/driver");
		
		cred = DataSourceHandler.createDBCredentials(xmn);
		
		Assert.assertNull(cred.getDriver());
	}
	
	/**
	 * Tests {@link DataSourceHandler#createDBCredentials(com.sibvisions.rad.server.config.ApplicationZone, String)}.
	 * 
	 * @throws Exception if the test fails
	 */
	@Test
	public void testCreateWithApplicationZone() throws Exception
	{
		DBCredentials cred = DataSourceHandler.createDBCredentials(Configuration.getApplicationZone("datasources"), "default");
		
		Assert.assertEquals("org.hsqldb.jdbcDriver", cred.getDriver());
		
		Assert.assertNull("Invalid datasource found!", DataSourceHandler.createDBCredentials(Configuration.getApplicationZone("datasources"), "???"));
		Assert.assertNull("Invalid datasource found!", DataSourceHandler.createDBCredentials(Configuration.getApplicationZone("datasources"), null));
	}
	
}	// TestDataSourceHandler
