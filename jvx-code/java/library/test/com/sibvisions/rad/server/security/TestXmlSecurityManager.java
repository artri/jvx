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
 * 27.05.2009 - [JR] - creation
 * 04.10.2009 - [JR] - change password with old password (new feature)
 * 22.09.2011 - [JR] - #475: MasterSession access check (without access controller)
 */
package com.sibvisions.rad.server.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

import javax.rad.remote.IConnectionConstants;
import javax.rad.remote.MasterConnection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sibvisions.rad.server.DirectServerConnection;
import com.sibvisions.rad.server.config.Configuration;
import com.sibvisions.util.xml.XmlNode;
import com.sibvisions.util.xml.XmlWorker;

/**
 * Test the functionality of {@link XmlSecurityManager}.
 * 
 * @see XmlSecurityManager
 * 
 * @author René Jahn
 */
public class TestXmlSecurityManager
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Resets the users file.
	 * 
	 * @throws Exception if the users file can not be changed
	 */
	@Before
	public void beforeTest() throws Exception
	{
		//prepare the config
		XmlWorker worker = new XmlWorker();
		
		File fiUsers = new File(Configuration.getApplicationZone("xmlusers").getDirectory(), "users.xml");
		
		XmlNode xmnUsers = worker.read(new FileInputStream(fiUsers));
		
		List<XmlNode> liNodes = xmnUsers.getNodes("/users/user");
		
		for (XmlNode node : liNodes)
		{
			if ("rene".equals(node.getNode("name").getValue()))
			{
				node.setNode("password", "rene");
			}
		}
		
		worker.write(new FileOutputStream(fiUsers), xmnUsers);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Tests {@link XmlSecurityManager#changePassword(com.sibvisions.rad.server.AbstractSession)}.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testChangePassword() throws Throwable
	{
		MasterConnection macon = new MasterConnection(new DirectServerConnection());
		
		//1. login
		
		macon.setApplicationName("xmlusers");
		macon.setUserName("rene");
		macon.setPassword("rene");
		
		macon.open();
		
		//2. change password
		
		macon.setNewPassword("rene", "newpassword");

		Assert.assertEquals("newpassword", macon.getProperties().get(IConnectionConstants.PASSWORD));
		Assert.assertEquals("newpassword", macon.getProperty(IConnectionConstants.PASSWORD));
		
		Assert.assertNull("New password property is not UNSET!", macon.getProperty(IConnectionConstants.NEWPASSWORD));
		Assert.assertNull("Old password property is not UNSET!", macon.getProperty(IConnectionConstants.OLDPASSWORD));
		
		macon.close();

		//3. login with invalid password
		
		macon.setPassword("rene");
		
		try
		{
			macon.open();
			
			Assert.fail("Opened connection with an invalid password!");
		}
		catch (SecurityException se)
		{
			Assert.assertEquals("Invalid password for 'rene' and application 'xmlusers'", se.getMessage());
		}
		
		//4. login with new password
		macon.setPassword("newpassword");
		
		macon.open();
		
		//5. change password (back)
		
		macon.setNewPassword("newpassword", "rene");
		macon.close();
		
		//6. login with original password
		
		macon.setPassword("rene");
		macon.open();

		try
		{
			//7. change password with invalid old password
			macon.setNewPassword("invalid", "newpassword");
		}
		catch (SecurityException se)
		{
			Assert.assertEquals("Invalid password for 'rene' and application 'xmlusers'", se.getMessage());
		}
		
		Assert.assertNotNull("New password property is UNSET!", macon.getProperty(IConnectionConstants.NEWPASSWORD));
		Assert.assertNotNull("Old password property is UNSET!", macon.getProperty(IConnectionConstants.OLDPASSWORD));
		
		macon.close();
	}
	
	/**
	 * Tests xml authentication with more than one user.
	 * 
	 * @throws Throwable if the bug is still present
	 */
	@Test
	public void testBug133() throws Throwable
	{
		MasterConnection macon = new MasterConnection(new DirectServerConnection());
		
		//1. user
		
		macon.setApplicationName("xmlusers");
		macon.setUserName("admin");
		macon.setPassword("admin");
		
		macon.open();
		macon.close();

		macon.setUserName("rene");
		macon.setPassword("rene");
		
		macon.open();
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
		macon.setApplicationName("xmlusers");
		macon.setUserName("admin");
		macon.setPassword("admin");
		
		try
		{
			macon.open();
		}
		catch (SecurityException se)
		{
			Assert.assertEquals("Access denied!", se.getMessage());
		}
		finally
		{
			macon.close();
		}

		//XMLSecurityManager has not AccessController -> the following should work without problems
		try
		{
			macon.setLifeCycleName("xmlusers.Session");
			macon.open();
		}
		finally
		{
			macon.close();
		}
	}
	
}	// TestXmlSecurityManager
