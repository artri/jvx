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

import java.util.List;
import java.util.Map.Entry;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the functionality of {@link ChangedHashtable}.
 * 
 * @author René Jahn
 */
public class TestChangedHashtable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Tests the {@link ChangedHashtable#put(Object, Object)} method.
	 */
	@Test
	public void testPut()
	{
		ChangedHashtable<String, String> chtPut = new ChangedHashtable<String, String>();
		
		
		Assert.assertNull(chtPut.put("KEY", "VALUE"));
		Assert.assertEquals("VALUE", chtPut.put("KEY", "VALUE-2"));
		
		Assert.assertEquals("VALUE-2", chtPut.put("KEY", null));
		Assert.assertFalse("KEY was found!", chtPut.containsKey("KEY"));
		Assert.assertNull(chtPut.put("KEY", "VALUE"));
		
		Assert.assertEquals("{KEY=VALUE}", chtPut.toString());
	}

	/**
	 * Tests the {@link ChangedHashtable#remove(Object)} method.
	 */
	@Test
	public void testRemove()
	{
		ChangedHashtable<String, String> chtRemove = new ChangedHashtable<String, String>();
		
		
		chtRemove.put("KEY", "VALUE");
		chtRemove.put("KEY-2", "VALUE-2");
		chtRemove.put("NEU", "A");
		
		chtRemove.put("NEU", null);	//Implizites remove
		
		chtRemove.remove("KEY-2");
		
		Assert.assertEquals(1, chtRemove.size());
	}
	
	/**
	 * Tests the {@link ChangedHashtable#getChanges()} method.
	 */
	@Test
	public void testGetChanges()
	{
		ChangedHashtable<String, String> chtChanges = new ChangedHashtable<String, String>();
		
		
		//MIT Tracking
		chtChanges.put("KEY", "VALUE");
		chtChanges.put("KEY-2", "VALUE");
		chtChanges.put("NEU", "A");
		chtChanges.put("NEU", null);

		//OHNE Tracking
		chtChanges.put("IGNORE", "?", false);
		chtChanges.put("EGAL", "A", false);
		
		List<Entry<String, String>> liChanges = chtChanges.getChanges();

		Assert.assertEquals(4, chtChanges.size());
		Assert.assertEquals(4, liChanges.size());
		
		Assert.assertEquals("KEY", liChanges.get(0).getKey());
		Assert.assertEquals("VALUE", liChanges.get(0).getValue());
		Assert.assertEquals("KEY-2", liChanges.get(1).getKey());
		Assert.assertEquals("VALUE", liChanges.get(1).getValue());
		Assert.assertEquals("NEU", liChanges.get(2).getKey());
		Assert.assertEquals("A", liChanges.get(2).getValue());
		Assert.assertEquals("NEU", liChanges.get(3).getKey());
		Assert.assertNull(liChanges.get(3).getValue());
		
		//Remove MIT Tracking
		Assert.assertEquals("VALUE", chtChanges.remove("KEY-2"));
		
		liChanges = chtChanges.getChanges();

		Assert.assertEquals(3, chtChanges.size());
		Assert.assertEquals(1, liChanges.size());
		
		Assert.assertEquals("KEY-2", liChanges.get(0).getKey());
		Assert.assertNull(liChanges.get(0).getValue());

		//Remove OHNE Tracking
		Assert.assertEquals("?", chtChanges.remove("IGNORE", false));
		
		liChanges = chtChanges.getChanges();

		Assert.assertEquals(2, chtChanges.size());
		Assert.assertNull("Invalid changes are available!", liChanges);
		
		chtChanges = new ChangedHashtable<String, String>();
		chtChanges.put("A key", "A value");
		chtChanges.put("B key", "B value");
		
		Assert.assertEquals(2, chtChanges.size());
		
		liChanges = chtChanges.getChanges();
		
		Assert.assertEquals(2, liChanges.size());
		
		ChangedHashtable<String, String> chtChanges2 = new ChangedHashtable<String, String>(chtChanges, true);
		
		liChanges = chtChanges2.getChanges();
		
		Assert.assertNotNull(liChanges);
	}

	/**
	 * Tests some TestCases for getChanges.
	 */
	@Test
	public void testGetChangesTestCases()
	{
		// Get Changes is null, if there are no changes. (Default java behavior is list with size null)
		ChangedHashtable<String, Object> chtChanges = new ChangedHashtable<String, Object>();
	
		Assert.assertNull(chtChanges.getChanges());
		
		// Every Change of a value is noticed. 
		
		chtChanges.put("test", Integer.valueOf(5));
		chtChanges.put("test", Integer.valueOf(6));
		chtChanges.put("test", Integer.valueOf(7));
		chtChanges.put("test", Integer.valueOf(7));
		chtChanges.put("test", Integer.valueOf(7));
		chtChanges.remove("test");
	
		Assert.assertEquals(4, chtChanges.getChanges().size());
		
		// Every Change of a value is noticed. Case 1 
		
		chtChanges.put("test", Integer.valueOf(5));
		chtChanges.put("test", Integer.valueOf(6));
		chtChanges.put("test", Integer.valueOf(7));
		chtChanges.put("test", Integer.valueOf(7));
		chtChanges.put("test", Integer.valueOf(7));
		chtChanges.remove("test");

		// Get type specific changes.
		chtChanges.put("test2", "Wert");
		Assert.assertEquals(2, chtChanges.getChanges(String.class).size());

		// Get type specific changes.

		chtChanges.put("test1", Integer.valueOf(1));
		chtChanges.put("test2", "Wert");
		
		Assert.assertEquals(1, chtChanges.getChanges(Integer.class).size());
	}
	
	/**
	 * Tests getChanges with a class type.
	 * 
	 * @see ChangedHashtable#getChanges(Class)
	 */
	@Test
	public void testGetChangesWithClass()
	{
		ChangedHashtable<String, Object> chtChanges = new ChangedHashtable<String, Object>();
		
		//MIT Tracking
		chtChanges.put("KEY", "VALUE");
		chtChanges.put("KEY-2", Integer.valueOf(12));
	
		List<Entry<String, Object>> liChanges = chtChanges.getChanges(String.class);
		
		Assert.assertTrue(1 == liChanges.size());
		
		Assert.assertEquals("KEY", liChanges.get(0).getKey());
		Assert.assertEquals("VALUE", liChanges.get(0).getValue());
		
		chtChanges.put("KEY-2", Integer.valueOf(13));

		liChanges = chtChanges.getChanges(Integer.class);
		
		Assert.assertTrue(1 == liChanges.size());
		
		Assert.assertEquals("KEY-2", liChanges.get(0).getKey());
		Assert.assertEquals(Integer.valueOf(13), liChanges.get(0).getValue());
	}

}	// TestChangedHashtable
