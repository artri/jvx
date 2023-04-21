/*
 * Copyright 2013 SIB Visions GmbH
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
 * 09.10.2013 - [JR] - creation
 */
package com.sibvisions.rad.remote;

import org.junit.BeforeClass;
import org.junit.Test;

import remote.net.TestVMConnection;

import com.sibvisions.rad.server.TestDirectServerConnection;

/**
 * Tests connection performance of all implemented {@link javax.rad.remote.IConnection}s.
 * 
 * @author René Jahn
 */
public class TestConnectionPerformance
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Initializes {@link BaseConnectionTest#beforeClass()}.
	 */
	@BeforeClass
	public static void beforeClass()
	{
		BaseConnectionTest.beforeClass();
	}
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Test methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Tests {@link TestHttpConnection#callServerMethodsConcurrently()}.
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testHttpConnection() throws Exception
	{
		new TestHttpConnection().callServerMethodsConcurrently();
	}

	/**
	 * Tests {@link TestVMConnection#callServerMethodsConcurrently()}.
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testVMConnection() throws Exception
	{
		new TestVMConnection().callServerMethodsConcurrently();
	}

	/**
	 * Tests {@link TestDirectServerConnection#callServerMethodsConcurrently()}.
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testDirectServerConnection() throws Exception
	{
		new TestDirectServerConnection().callServerMethodsConcurrently();
	}
	
}	// TestConnectionPerformance
