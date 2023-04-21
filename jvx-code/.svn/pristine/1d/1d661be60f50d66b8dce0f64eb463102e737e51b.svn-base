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
 * 14.07.2009 - [JR] - creation
 */
package com.sibvisions.util.type;

import java.util.Hashtable;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test the functionality of {@link BeanUtil}.
 * 
 * @author René Jahn
 */
public class TestBeanUtil
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Tests {@link BeanUtil#toArray(Object[], Object[], String)}.
	 */
	@Test
	public void testToArray()
	{
		//Create test objects
		
		Hashtable[] beans = new Hashtable[4];
		
		Hashtable<String, Object> bean0 = new Hashtable<String, Object>();
		Hashtable<String, Object> bean1 = new Hashtable<String, Object>();
		Hashtable<String, Object> bean2 = new Hashtable<String, Object>();
		Hashtable<String, Object> bean3 = new Hashtable<String, Object>();
		
		bean0.put("name", "Martin");
		bean0.put("address", "Street-0");

		bean1.put("name", "Roland");
		bean1.put("address", "Street-1");

		bean2.put("name", "David");
		bean2.put("address", "Street-2");

		bean3.put("name", "René");
		bean3.put("address", "Street-3");

		Hashtable<String, Object> beanSub0 = new Hashtable<String, Object>();
		Hashtable<String, Object> beanSub2 = new Hashtable<String, Object>();
		
		beanSub0.put("id", "0");
		
		beanSub2.put("id", "2");
		
		bean0.put("subbean", beanSub0);
		
		bean2.put("subbean", beanSub2);

		
		beans[0] = bean0;
		beans[1] = bean1;
		beans[2] = bean2;
		beans[3] = bean3;
		
		//CHECKs
		
		String[] sResult = new String[beans.length];
		
		BeanUtil.toArray(beans, sResult, "address");
		
		Assert.assertArrayEquals(new String[] {"Street-0", "Street-1", "Street-2", "Street-3"}, sResult);
		
		Hashtable[] ibResult = new Hashtable[beans.length];
		
		BeanUtil.toArray(beans, ibResult, "subbean");
		
		Assert.assertArrayEquals(new Hashtable[] {beanSub0, null, beanSub2, null}, ibResult);

		BeanUtil.toArray(beans, sResult, "subbean.id");
		
		Assert.assertArrayEquals(new String[] {"0", null, "2", null}, sResult);
	}
	
}	// TestBeanUtil
