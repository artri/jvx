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
 */
package com.sibvisions.rad.server.config;

import java.io.File;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sibvisions.rad.IPackageSetup;
import com.sibvisions.util.type.ResourceUtil;
import com.sibvisions.util.xml.XmlNode;
import com.sibvisions.util.xml.XmlWorker;

/**
 * Tests the {@link ApplicationZone} methods.
 * 
 * @author René Jahn
 */
public class TestApplicationZone
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
	 * Tests to get properties of an application zone.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testGetProperty() throws Throwable
	{
		ApplicationZone app;
		
		
		//valid configured application
		app = Configuration.getApplicationZone("demo");	
		
		Assert.assertEquals("com.sibvisions.rad.server.security.DBSecurityManager", app.getProperty("/application/securitymanager/class"));
		Assert.assertEquals("org.hsqldb.jdbcDriver", app.getProperty("/application/securitymanager/database/driver"));
		Assert.assertEquals("jdbc:hsqldb:hsql://localhost/demodb;ifexists=true", app.getProperty("/application/securitymanager/database/url"));
		Assert.assertEquals("sa", app.getProperty("/application/securitymanager/database/username"));
		Assert.assertEquals("", app.getProperty("/application/securitymanager/database/password"));
		
		//invalid configured application
		app = Configuration.getApplicationZone("invalid");	

		Assert.assertNull("SecurityManager found!", app.getProperty("/application/securitymanager/class"));

		//unknown application
		try
		{
			Configuration.getApplicationZone("unknown");	
	
			Assert.fail("Application found!");
		}
		catch (Exception be)
		{
			Assert.assertTrue(be.getMessage(), be.getMessage().startsWith("Invalid configuration file:"));
		}
	}
	
	/**
	 * Tests to get inherited properties of an application zone.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testGetInheritedProperty() throws Throwable
	{
		ApplicationZone app = Configuration.getApplicationZone("demo");
		
		Assert.assertEquals("30", app.getProperty("/application/timeout/mastersession"));
		Assert.assertEquals("360", app.getProperty("/application/timeout/subsession"));
	}	

	/**
	 * Tests to get directory locations.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testDirectories() throws Throwable
	{
		String sDir = new File(ResourceUtil.getPathForClass(ResourceUtil.getFqClassName(TestConfiguration.class))).getParent();

		ApplicationZone app = Configuration.getApplicationZone("demo");
		
		Assert.assertEquals(new File(sDir, "rad/apps/demo"), app.getDirectory());
	}
	
	/**
	 * Tests up-to-date feature of config files.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void test262() throws Throwable
	{
		ApplicationZone app = Configuration.getApplicationZone("demo");

		ApplicationZone appClone = (ApplicationZone)app.clone();
		appClone.setUpdateEnabled(false);
		
		Assert.assertEquals("demo.Session", appClone.getProperty("/application/lifecycle/mastersession"));

		//change config file
		XmlWorker xmw = new XmlWorker();
		XmlNode node = xmw.read(app.getFile());
		
		node.setNode("/application/lifecycle/mastersession", "demo.Session2");

		xmw.write(app.getFile(), node);
		
		try
		{
			//detect again
			app = Configuration.getApplicationZone("demo");

			//clone again
			appClone = (ApplicationZone)app.clone();
			appClone.setUpdateEnabled(false);

			Assert.assertEquals("demo.Session2", appClone.getProperty("/application/lifecycle/mastersession"));
		}
		finally
		{
			//reset
			app.setProperty("/application/lifecycle/mastersession", "demo.Session");
		}
	}
	
} 	// TestApplicationZone
