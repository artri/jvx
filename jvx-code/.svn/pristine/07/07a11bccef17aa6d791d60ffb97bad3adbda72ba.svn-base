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

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.util.WeakHashMap;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the <code>Internalize</code> class.
 * 
 * @author Martin Handsteiner
 * @see Internalize
 */
public class TestInternalize extends MemoryInfo
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
		// Checking the hash algorithm, the duration and the memory useage.
		// Comparing the own hash implementation compared to hashmap.
		
		Runtime runtime = Runtime.getRuntime();

		System.out.println("memory begin: " + (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024);

		int size = 1000000;
		int different = size / 5;
		
		Object[] array = new Object[size];
		for (int i = 0; i < size; i++)
		{
//			array[i] = new String((i % different) + "_" + (i % different));
//			array[i] = new String("" + (i % different));
//			array[i] = new BigDecimal(i % different);
			array[i] = new Integer(i % different);
		}
		
		System.out.println("memory all object: " + (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024);
		long start = System.nanoTime();
		
		for (int i = 0; i < size; i++)
		{
			array[i] = Internalize.internNoSync(array[i]);
//			array[i] = new Integer(i % different);
		}
		System.out.println(Internalize.getObjectCount());
		
		System.out.println("Duration:     " + (System.nanoTime() - start) / 1000 + "us");
		
		try
		{
			for (int i = 0; i < 20; i++)
			{
				runtime.runFinalization();
				runtime.gc();
				Thread.sleep(20);
			}
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("memory intern objects: " + (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024);

		Internalize.clearCache();
		
		try
		{
			for (int i = 0; i < 20; i++)
			{
				runtime.runFinalization();
				runtime.gc();
				Thread.sleep(20);
			}
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (int i = 0; i < size; i++)
		{
//			array[i] = new String((i % different) + "_" + (i % different));
//			array[i] = new String("" + (i % different));
//			array[i] = new BigDecimal(i % different);
			array[i] = new Integer(i % different);
		}
		
		WeakHashMap<Object, WeakReference> cache = new WeakHashMap<Object, WeakReference>();
		
		System.out.println("memory all objects: " + (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024);
		start = System.nanoTime();
		
		for (int i = 0; i < size; i++)
		{
			Object val = array[i];
			WeakReference weak = cache.get(val);
			if (weak != null)
			{
				Object iVal = weak.get();
				if (iVal == null)
				{
					weak = null;
				}
				val = iVal;
			}
			if (weak == null)
			{
				weak = new WeakReference(val);
				cache.put(val, weak);
			}
			array[i] = val;
//			array[i] = new Integer(i % different);
		}

		System.out.println("Duration:     " + (System.nanoTime() - start) / 1000 + "us");

		try
		{
			for (int i = 0; i < 20; i++)
			{
				runtime.runFinalization();
				runtime.gc();
				Thread.sleep(20);
			}
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("memory map objects: " + (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Tests the Internalize functionality.
	 */
	@Test
	public void testIntern()
	{
		Internalize.clearCache();
		
		String str1 = "123";
		String str2 = new String(str1);
		
		Assert.assertFalse("String reference is equal!", str1 == str2);
		Assert.assertTrue("Internal string reference is different! ", Internalize.intern(str1) == Internalize.intern(str2));

		BigDecimal big1 = new BigDecimal("123");
		BigDecimal big2 = new BigDecimal(str1);

		Assert.assertFalse("String reference is equal!", big1 == big2);
		Assert.assertTrue("Internal string reference is different!", Internalize.intern(big1) == Internalize.intern(big2));
	}
	
	/**
	 * Tests the Internalize.rehashAndClearUnused functionality.
	 */
	@Test
	public void testRehashAndClearUnused()
	{
		int iSize = 100000;
		
		Integer[] iUsed = new Integer[iSize];
		
		for (int i = 0; i < iSize; i++)
		{
			iUsed[i] = Internalize.intern(new Integer(i));
			
			if (i % 10 == 0)
			{
				iUsed[i] = null;
			}
		}
		
		iUsed[0] = Internalize.intern(new Integer(0));
	}
	
}	// TestInternalize
