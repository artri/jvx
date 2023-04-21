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
package com.sibvisions.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the <code>GroupHashtable</code> functionality.
 * 
 * @author René Jahn
 * @see GroupHashtable
 */
public class TestGroupHashtable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Returns an <code>GroupHashtable</code> with test entries.
	 * 
	 * @return group mapping for names and cars 
	 */
	public GroupHashtable<String, String, String> getData()
	{
		GroupHashtable<String, String, String> ght = new GroupHashtable<String, String, String>();
		
		
		ght.put("GROUP", "NAME", "René");
		ght.put("GROUP", "NAME", "Roland");
		
		ght.put("GROUP", "AUTO", "Audi A8");
		ght.put("GROUP", "AUTO2", "Skoda Fabia");
		
		ght.put("UNKNOWN", "HAUS", "Ziegel massiv");

		return ght;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Tests the put method.
	 */
	@Test
	public void testPut()
	{
		GroupHashtable<String, String, String> ght = getData();
		
		
		try
		{
			ght.put(null, "TEST", "TEST");
			
			Assert.fail("Gruppe 'null' kann zur Liste hinzugefügt werden!");
		}
		catch (NullPointerException npe)
		{
			//egal
		}
		
		try
		{
			ght.put("ABCD", null, "TEST");
			
			Assert.fail("Key 'null' kann zur Liste hinzugefügt werden!");
		}
		catch (NullPointerException npe)
		{
			//egal
		}
		
		try
		{
			ght.put("ABCD", "TEST", null);
			
			Assert.fail("Wert 'null' kann zur Liste hinzugefügt werden!");
		}
		catch (NullPointerException npe)
		{
			//egal
		}
	}
	
	/**
	 * Tests the get method.
	 */
	@Test
	public void testGet()
	{
		GroupHashtable<String, String, String> ght = getData();
		
		
		Assert.assertEquals("Roland", ght.get("GROUP", "NAME"));
		Assert.assertNull(ght.get("", ""));
		Assert.assertNull(ght.get("GROUP", ""));
	}

	/**
	 * Tests the size method.
	 */
	@Test
	public void testSize()
	{
		GroupHashtable<String, String, String> ght = getData();
		
		
		Assert.assertEquals(2, ght.size());
		Assert.assertEquals(3, ght.size("GROUP"));
		Assert.assertEquals(0, ght.size(""));
	}
	
	/**
	 * Tests the put method.
	 */
	@Test
	public void testRemove()
	{
		GroupHashtable<String, String, String> ght = getData();
		
		
		Assert.assertEquals("Skoda Fabia", ght.get("GROUP", "AUTO2"));
		Assert.assertEquals("Skoda Fabia", ght.remove("GROUP", "AUTO2"));
		Assert.assertNull(ght.get("GROUP", "AUTO2"));

		Assert.assertEquals("Roland", ght.get("GROUP", "NAME"));
		Assert.assertTrue(ght.remove("GROUP"));
		Assert.assertNull(ght.get("GROUP", "NAME"));
	}
	
}	// TestGroupHashtable
