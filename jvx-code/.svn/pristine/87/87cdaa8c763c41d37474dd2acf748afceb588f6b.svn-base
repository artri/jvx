/*
 * Copyright 2014 SIB Visions GmbH
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
 * 20.10.2014 - [RZ] - creation
 */
package javax.rad.ui.genui;

import java.util.ArrayList;
import java.util.List;

import javax.rad.genui.UIResource;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test {@link UIResource}.
 */
public class TestUIResource
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Tests the method {@link UIResource#getObjectNames()}.
	 */
	@Test
	public void testGetObjectNames()
	{
		UIResource resource = new TestResource();
		
		// No objects set should yield an empty list.
		Assert.assertNotNull(resource.getObjectNames());
		
		resource.putObject("Name A", new Object());
		resource.putObject("Name B", new Object());
		resource.putObject("Name C", new Object());
		
		resource.putObject("null", new Object());
		resource.putObject("null", null);
		
		List<String> names = new ArrayList<String>(resource.getObjectNames());
		
		Assert.assertTrue(names.contains("Name A"));
		Assert.assertTrue(names.contains("Name B"));
		Assert.assertTrue(names.contains("Name C"));
		Assert.assertFalse(names.contains("null"));
	}
	
	/**
	 * Tests the methods {@link UIResource#getObject(String)} and
	 * {@link UIResource#putObject(String, Object)}.
	 */
	@Test
	public void testGetPutObject()
	{
		UIResource resource = new TestResource();
		
		resource.putObject("null", "Should be deleted with next line.");
		resource.putObject("null", null);
		
		resource.putObject("String", "Should be overwritten with next line.");
		resource.putObject("String", "This is a test");
		
		resource.putObject("Boolean", Boolean.TRUE);
		
		resource.putObject("Long", Long.valueOf(58L));
		
		Assert.assertNull(resource.getObject("null"));
		Assert.assertEquals("This is a test", resource.getObject("String"));
		Assert.assertEquals(Boolean.TRUE, resource.getObject("Boolean"));
		Assert.assertEquals(Long.valueOf(58L), resource.getObject("Long"));
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * An {@link UIResource} extension that is used for testing.
	 */
	private static final class TestResource extends UIResource
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of {@link TestResource}.
		 */
		public TestResource()
		{
			super(null);
		}
		
	}	// TestResource
	
}	// TestUIResource
