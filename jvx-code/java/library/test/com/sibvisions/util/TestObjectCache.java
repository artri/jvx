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
 * 11.02.2009 - [JR] - creation
 */
package com.sibvisions.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the <code>ObjectCache</code> functionality.
 * 
 * @author René Jahn
 * @see ObjectCache
 */
public class TestObjectCache
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Tests the put method.
	 * 
	 * @throws InterruptedException if the test fails
	 */
	@Test
	public void testPutGet() throws InterruptedException
	{
		String sCache = "Cached object";
		
		Object oKey = ObjectCache.put(sCache, 10000);
		
		Assert.assertNotNull("Access key not assigned!", oKey);
		
		String sObject = (String) ObjectCache.get(oKey);
		
		Assert.assertEquals(sCache, sObject);
		
		Thread.sleep(11000);
		
		sObject = (String) ObjectCache.get(oKey);
		
		Assert.assertNull("Object already cached!", sObject);
	}
	
	/**
	 * Tests different timeouts.
	 * 
	 * @throws InterruptedException if the test fails
	 */
	@Test
	public void testChangeTimeout() throws InterruptedException
	{
		Object oCacheKey = ObjectCache.put("Cached object", 10000);
		Object oTimeoutKey = ObjectCache.put("Timeout", 2000);
		
		Thread.sleep(3000);
		
		String sObject = (String) ObjectCache.get(oCacheKey);
		
		Assert.assertNotNull("Object not in cache!", sObject);
		
		sObject = (String) ObjectCache.get(oTimeoutKey);
		
		Assert.assertNull("Object still in cache!", sObject);
		
		Thread.sleep(8000);
		
		//Empty cache check
		
		sObject = (String) ObjectCache.get(oCacheKey);
		
		Assert.assertNull("Object still in cache!", sObject);
	}
	
	/**
	 * Tests the support for custom validatable object.
	 */
	@Test
	public void testCustomValidatableObject()
	{
		ValidatableObject object = new ValidatableObject();
		
		Object key = ObjectCache.put(object);
		
		Assert.assertNotNull("Validatable Object is not in the cache!", ObjectCache.get(key));
		
		object.setValid(false);
		
		Assert.assertNull("Validatable Object is still in the cache!", ObjectCache.get(key));
	}
	
	/**
	 * Tests the remove method.
	 */
	@Test
	public void testRemove()
	{
		Object oKey = ObjectCache.put("Remove", 0);
		
		ObjectCache.remove(oKey);
		
		Assert.assertNull("Object still in cache!", ObjectCache.get(oKey));
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * A custom validatable object for testing.
	 * 
	 * @author Robert Zenz
	 */
	private static final class ValidatableObject implements IValidatable
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/** If this object is valid. */
		private boolean valid = true;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of {@link ValidatableObject}.
		 */
		public ValidatableObject()
		{
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Interface implementation
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * {@inheritDoc}
		 */
		public boolean isValid()
		{
			return valid;
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// User-defined methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Sets if this object is valid..
		 * 
		 * @param pValid if this object is valid..
		 */
		public void setValid(boolean pValid)
		{
			valid = pValid;
		}
		
	}	// ValidatableObject
	
}	// TestObjectCache
