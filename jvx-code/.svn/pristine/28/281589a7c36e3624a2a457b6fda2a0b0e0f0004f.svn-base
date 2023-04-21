/*
 * Copyright 2011 SIB Visions GmbH
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
 * 31.07.2011 - [JR] - creation
 */
package com.sibvisions.rad.server.security;

import org.junit.Before;
import org.junit.Test;

import com.sibvisions.rad.IPackageSetup;
import com.sibvisions.rad.server.DetachedSession;

/**
 * Tests the functionality of {@link AbstractSecurityManager}.
 * 
 * @author René Jahn
 */
public class TestAbstractSecurityManager
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
	 * Test security manager creation through a {@link DetachedSession}.
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testCreateSecurityManager() throws Exception
	{
		DetachedSession sess = new DetachedSession("xmlusers");
		sess.setUserName("rene");
		sess.setPassword("rene");
		
		ISecurityManager secman = AbstractSecurityManager.createSecurityManager(sess);
		secman.validateAuthentication(sess);
	}
	
}	// TestAbstractSecurityManager
