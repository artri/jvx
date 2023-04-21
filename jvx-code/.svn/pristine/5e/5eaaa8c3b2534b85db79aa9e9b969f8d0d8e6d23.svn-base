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
 * Tests the <code>KeyValueList</code> functionality.
 * 
 * @author René Jahn
 * @see KeyValueList
 */
public class TestKeyValueList
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Returns an <code>ArrayList</code> with test entries.
	 * 
	 * @return list of cars
	 */
	public ArrayUtil<String> getCarList()
	{
		ArrayUtil<String> alAuto = new ArrayUtil<String>();
		
		alAuto.add("Audi A4");
		alAuto.add("Audi A3");
		alAuto.add("Audi A6");
		alAuto.add("Audi A8");
		alAuto.add("Skoda Fabia");
		alAuto.add("Renault Megane");
		
		return alAuto;
	}
	
	/**
	 * Gets an <code>KeyValueList</code> with test entries.
	 * 
	 * @return list of names and cars
	 */
	public KeyValueList<String, String> getData()
	{
		KeyValueList<String, String> kvl = new KeyValueList<String, String>();
		

		kvl.put("NAME", "René");
		kvl.put("NAME", "Roland");
		kvl.put("NAME", "Martin");
		
		kvl.putAll("AUTO", getCarList());
		
		return kvl;
	}
	
    /**
     * Gets an <code>KeyValueList</code> with test entries.
     * 
     * @return list of names and cars
     */
    public KeyValueList<String, String> getRedundantData()
    {
        KeyValueList<String, String> kvl = new KeyValueList<String, String>();
        

        kvl.put("NAME", "René");
        kvl.put("NAME", "Roland");
        kvl.put("NAME", "Martin");
        
        kvl.put("NAME2", "René");

        kvl.put("NAME3", "René");
        kvl.put("NAME3", "Martin");

        kvl.putAll("AUTO", getCarList());
        
        return kvl;
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
		KeyValueList<String, String> gal = getData();

		
		Assert.assertEquals(3, gal.get("NAME").size());
		Assert.assertEquals(6, gal.get("AUTO").size());
	}
	
	/**
	 * Tests the {@link KeyValueList#put(Object, Object, boolean)} method.
	 */
	@Test
	public void testPutUnique()
	{
		KeyValueList<String, String> gal = getData();

		gal.put("NAME", "Martin", true);
		
		Assert.assertEquals(3, gal.get("NAME").size());
		
		gal.put("NAME", "Martin", false);
		
		Assert.assertEquals(4, gal.get("NAME").size());
	}

	/**
	 * Tests the containsObject method.
	 */
	@Test
	public void testContains()
	{
		KeyValueList<String, String> kvl = getData();
		
		ArrayUtil<String> auList = new ArrayUtil<String>();
		

		auList.add("Audi A4");

		Assert.assertTrue("Key 'AUTO' is not an element!", kvl.containsKey("AUTO"));
		Assert.assertTrue("Key 'NAME' is not an element!", kvl.containsKey("NAME"));
		Assert.assertFalse("Key '' is an element!", kvl.containsKey(""));
		
		Assert.assertTrue("Value 'Roland' is not an element!", kvl.containsValue("Roland"));
		Assert.assertTrue("Value 'René' is not an element!", kvl.containsValue("René"));

		Assert.assertTrue("'Skoda Fabia' is not an element!", kvl.containsValue("Skoda Fabia"));
	}

	/**
	 * Tests the remove method with key and a value as parameter.
	 */
	@Test
	public void testRemoveValue()
	{
		KeyValueList<String, String> kvl = getData();
		
		Assert.assertTrue("Element count of 'NAME' is not as expected!", 3 == kvl.get("NAME").size());

		kvl.remove("NAME", "Roland");
		
		Assert.assertTrue("Element 'NAME/Roland' is an element!", 2 == kvl.get("NAME").size());
		Assert.assertFalse("Element 'NAME/Roland' is an element!", kvl.get("NAME").contains("Roland"));
		Assert.assertTrue("Element 'NAME/René' is not an element!", kvl.get("NAME").contains("René"));
		Assert.assertTrue("Element 'NAME/Martin' is not an element!", kvl.get("NAME").contains("Martin"));
		
		kvl.remove("NAME", "René");		
		kvl.remove("NAME", "Martin");
		
		Assert.assertNull("Key 'NAME' is an element!", kvl.get("NAME"));
		
		
		kvl = getRedundantData();
		
        Assert.assertEquals(1, kvl.get("NAME2").size());
        Assert.assertEquals(2, kvl.get("NAME3").size());

        kvl.removeValue("René");
		
		Assert.assertNull("Key 'NAME2' is an element!", kvl.get("NAME2"));
        Assert.assertEquals("Key 'NAME2' is an element!", 1, kvl.get("NAME3").size());
	}
	
}	// TestKeyValueList
