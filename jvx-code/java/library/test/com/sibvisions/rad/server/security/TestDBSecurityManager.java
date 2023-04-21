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
 * 22.09.2011 - [JR] - #475: MasterSession access check (with access controller)
 * 23.09.2011 - [JR] - #475: test user-defined allow mapping
 */
package com.sibvisions.rad.server.security;

import javax.rad.remote.MasterConnection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sibvisions.rad.IPackageSetup;
import com.sibvisions.rad.persist.jdbc.DBCredentials;
import com.sibvisions.rad.server.AbstractSession;
import com.sibvisions.rad.server.DetachedSession;
import com.sibvisions.rad.server.DirectServerConnection;
import com.sibvisions.rad.server.Server;
import com.sibvisions.rad.server.config.Configuration;

/**
 * Tests the functionality of {@link DBSecurityManager}.
 * 
 * @author René Jahn
 */
public class TestDBSecurityManager
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
	 * Tests {@link DBSecurityManager#getCredentials(javax.rad.server.IConfiguration)}.
	 * 
	 * @throws Exception if the test fails
	 */
	@Test
	public void testGetCredentials() throws Exception
	{
		//connection is configured in the securitymanager tag
		DBCredentials cred = DBSecurityManager.getCredentials(Configuration.getApplicationZone("demo").getConfig());
		
		Assert.assertEquals("Driver = 'org.hsqldb.jdbcDriver', Url = 'jdbc:hsqldb:hsql://localhost/demodb;ifexists=true', Username = 'sa', Password is set", cred.toString());
		
		//connection is configured as datasource
		cred = DBSecurityManager.getCredentials(Configuration.getApplicationZone("datasources").getConfig());
		
		Assert.assertEquals("Driver = 'org.hsqldb.jdbcDriver', Url = 'jdbc:hsqldb:hsql://localhost/demodb;ifexists=true', Username = 'sa', Password is set", cred.toString());
		
		//Convention over Configuration
		cred = DBSecurityManager.getCredentials(Configuration.getApplicationZone("defaultdatasource").getConfig());
		
		Assert.assertEquals("Driver = 'org.hsqldb.jdbcDriver', Url = 'jdbc:hsqldb:hsql://localhost/demodb;ifexists=true', Username = 'sa', Password is set", cred.toString());
	}

	/**
	 * Tests {@link DBSecurityManager#getConnection()}.
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testGetConnection() throws Exception
	{
		DetachedSession sess = new DetachedSession("demo");
		sess.setUserName("rene");
		sess.setPassword("rene");
		
		ISecurityManager secman = sess.getSecurityManager();
		secman.validateAuthentication(sess);
		
		((DBSecurityManager)secman).getConnection();
		
		sess.setUserName("martin");
		sess.setPassword("martin");
		
		secman.validateAuthentication(sess);

		((DBSecurityManager)secman).getConnection();
	}

	/**
	 * Test the access controller.
	 * 
	 * @throws Throwable if test fails
	 */
	@Test
	public void testAccessControl() throws Throwable
	{
		DirectServerConnection dscon = new DirectServerConnection();
		
		MasterConnection macon = new MasterConnection(dscon);
		macon.setLifeCycleName("demo.special.NoAccess");
		macon.setApplicationName("demo");
		macon.setUserName("rene");
		macon.setPassword("rene");
		
		try
		{
			macon.open();
			
			Assert.fail("Access to 'demo.special.NoAccess' is allowed but should not!");
		}
		catch (SecurityException se)
		{
			Assert.assertEquals("Access to 'demo.special.NoAccess' denied!", se.getMessage());
		}

		//default LCO should be possible
		try
		{
			macon.setLifeCycleName(null);
			macon.open();
		}
		finally
		{
			macon.close();
		}
		
		//configured LCO name (via config.xml) should be possible
		try
		{
			macon.setLifeCycleName("demo.Session");
			macon.open();
		}
		finally
		{
			macon.close();
		}
		
		//configured LCO name (via DB access rules) should be possible
		try
		{
			macon.setLifeCycleName("demo.special.Address");
			macon.open();
		}
		finally
		{
			macon.close();
		}
	}
	
	/**
	 * Checks whether the user-defined allow mappings (via config.xml) works.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testUserDefinedAllowedLifeCycleObjects() throws Throwable
	{
		DirectServerConnection dscon = new DirectServerConnection();
		
		MasterConnection macon = new MasterConnection(dscon);
		macon.setApplicationName("demo");
		macon.setUserName("rene");
		macon.setPassword("rene");
		
		try
		{
			macon.open();
			
			AbstractSession session = Server.getInstance().getSessionManager().get(macon.getConnectionId());
			
			IAccessController acc = session.getAccessController();
			
			Assert.assertNotNull("User-defined LCO mapping does not work!", acc.find(null, "demo.special.DoesNotExist"));
		}
		finally
		{
			macon.close();
		}
	}
	
}	// TestDBSecurityManager
