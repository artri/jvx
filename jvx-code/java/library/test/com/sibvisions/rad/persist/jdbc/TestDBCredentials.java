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
package com.sibvisions.rad.persist.jdbc;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sibvisions.rad.IPackageSetup;

/**
 * Tests the functionality of {@link DBCredentials}.
 * 
 * @author René Jahn
 */
public class TestDBCredentials
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
	 * Tests {@link DBCredentials#equals(Object)}.
	 */
	@Test
	public void testEquals()
	{
		DBCredentials dbcA  = new DBCredentials("a", "1", "2", "3");
		DBCredentials dbcAA = new DBCredentials("a", "1", "2", "3");

		DBCredentials dbcB  = new DBCredentials("b", "1", "2", "3");
		DBCredentials dbcBB  = new DBCredentials("a", "2", "2", "3");
		DBCredentials dbcBBB  = new DBCredentials("a", "1", "3", "3");
		DBCredentials dbcBBBB  = new DBCredentials("a", "1", "2", "4");
		
		Assert.assertEquals(dbcA, dbcAA);
		Assert.assertEquals(dbcAA, dbcA);
		
		Assert.assertFalse(dbcA.equals(dbcB));
		Assert.assertFalse(dbcA.equals(dbcBB));
		Assert.assertFalse(dbcA.equals(dbcBBB));
		
		//password is included for equals check!
		Assert.assertTrue(dbcA.equals(dbcBBBB));
	}
	
	/**
	 * Tests {@link DBCredentials#hashCode()}.
	 */
	@Test
	public void testHashCode()
	{
		DBCredentials dbcA  = new DBCredentials("a", "1", "2", "3");
		DBCredentials dbcAA = new DBCredentials("a", "1", "2", "3");

		DBCredentials dbcB  = new DBCredentials("b", "1", "2", "3");

		Assert.assertTrue("hashCode() calculation is wrong because different credentials have the same hashCode", dbcA.hashCode() != dbcB.hashCode());
		Assert.assertEquals(dbcA.hashCode(), dbcAA.hashCode());
	}
	
}	// TestDBCredentials
