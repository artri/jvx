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
 * 01.10.2008 - [HM] - creation
 * 11.10.2008 - [JR] - testRemove: test for remove right part bug
 * 19.11.2008 - [RH] - testStaticAdd; new test case with Object[] and add different types
 */
package com.sibvisions.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the <code>ArrayUtil</code>.
 * 
 * @author Martin Handsteiner
 * @see ArrayUtil
 */
public class TestImmutableArray extends MemoryInfo
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Tests add and remove functions of <code>ArrayUtil</code>.
	 */
	@Test
	public void testStaticAddRemove()
	{
		ImmutableArray<String> columns = new ImmutableArray<String>("ID", "NAME", "KOMMENTAR");
		
		ImmutableArray<String> addTest = columns.add("GEAENDERT_VON", "GEAENDERT_AM");
		
		ImmutableArray<String> removeTest = columns.remove("ID");
		
		Assert.assertTrue(columns.equals(new ImmutableArray<String>("ID", "NAME", "KOMMENTAR")));

		Assert.assertTrue(addTest.equals(new ImmutableArray<String>("ID", "NAME", "KOMMENTAR", "GEAENDERT_VON", "GEAENDERT_AM")));
		
		Assert.assertTrue(removeTest.equals(new ImmutableArray<String>("NAME", "KOMMENTAR")));
		
		Assert.assertTrue(addTest.removeLast(2).equals(columns));
		
		Assert.assertTrue(removeTest.equals(columns.removeFirst()));
		
		System.out.println(columns);
		System.out.println(addTest);
		System.out.println(removeTest);
	}	

	
}	// TestImmutableArray
