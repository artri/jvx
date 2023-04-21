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
 */
package com.sibvisions.util;

import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the <code>Internalize</code> class.
 * 
 * @author Martin Handsteiner
 * @see Internalize
 */
public class TestIntHashMap extends MemoryInfo
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Profile.
	 * 
	 * @param pArgs no functionality.
	 */
	public static synchronized void main(String[] pArgs)
	{
		long start = System.currentTimeMillis();
		
		HashMap<String, String> map2 = new HashMap<String, String>();
		
		for (int i = 0; i < 2000000; i++)
		{
			String val = "Test" + i;
			map2.put(val, val);
		}

		System.out.println("Duration " + (System.currentTimeMillis() - start));
		
		start = System.currentTimeMillis();
		
		IntHashMap<String> map = new IntHashMap();
		
		for (int i = 0; i < 2000000; i++)
		{
			String val = "Test" + i;
			map.put(val.hashCode(), val);
		}

		System.out.println("Duration " + (System.currentTimeMillis() - start));
		
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Tests the Internalize functionality.
	 */
	@Test
	public void testPut()
	{
		IntHashMap<String> map = new IntHashMap<String>();
		
		map.put("Martin".hashCode(), "Martin");
		map.put("Robert".hashCode(), "Robert");
		map.put("Rene".hashCode(), "Rene");
		map.put("Lidl".hashCode(), "Lidl");
		map.put("Hofer".hashCode(), "Hofer");
		
		System.out.println(map);
		Assert.assertEquals("Martin", map.get("Martin".hashCode()));
		Assert.assertEquals("Robert", map.get("Robert".hashCode()));
		Assert.assertEquals("Rene", map.get("Rene".hashCode()));
		Assert.assertEquals("Lidl", map.get("Lidl".hashCode()));
		Assert.assertEquals("Hofer", map.get("Hofer".hashCode()));
		Assert.assertEquals(5, map.size());
		
		map.remove("Robert".hashCode());
		System.out.println(map);
		Assert.assertEquals("Martin", map.get("Martin".hashCode()));
		Assert.assertNull(map.get("Robert".hashCode()));
		Assert.assertEquals("Rene", map.get("Rene".hashCode()));
		Assert.assertEquals("Lidl", map.get("Lidl".hashCode()));
		Assert.assertEquals("Hofer", map.get("Hofer".hashCode()));
		Assert.assertEquals(4, map.size());

		map.put("Rene".hashCode(), "Rene");
		Assert.assertEquals(4, map.size());
	}
	
	/**
	 * Tests the rehash functionality.
	 */
	@Test
	public void testRehash()
	{
		IntHashMap<String> map = new IntHashMap<String>();

		for (int i = 0; i < 1000; i++)
		{
			map.put(("Test" + i).hashCode(), ("Test" + i));
		}
	
		System.out.println(map.size());
		System.out.println(map);
		Assert.assertEquals(1000, map.size());
		
		for (int i = 0; i < 1000; i++)
		{
			map.remove(("Test" + i).hashCode());
		}
	
		System.out.println(map.size());
		System.out.println(map);
		Assert.assertEquals(0, map.size());
	}
	
}	// TestIntHashMap
