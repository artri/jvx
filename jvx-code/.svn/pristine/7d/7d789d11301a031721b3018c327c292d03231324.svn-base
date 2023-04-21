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

import java.math.BigDecimal;

import org.junit.Test;

/**
 * Tests the <code>Internalize</code> class.
 * 
 * @author Martin Handsteiner
 * @see Internalize
 */
public class TestInternalizePerformance extends TestInternalize
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
		int size = 1000000;
		int different = size / 5;
		
		Object[] array = new Object[size];

		long start = System.nanoTime();
		
		for (int i = 0; i < size; i++)
		{
			array[i] = Internalize.internNoSync(new Integer(i % different));
//			array[i] = new Integer(i % different);
		}
		
		System.out.print("Duration:     " + (System.nanoTime() - start) / 1000 + "us");
		
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Tests the Performance.
	 */
	@Test
	public void testPerformance()
	{
		int size = 100000;
		int different = size / 5;
		
		long start;
		long mxHeap;
		long mxNonHeap;
		
		String[] array = new String[size];
		String[] arrayOwn = new String[size];
		String[] arrayStd = new String[size];

		
		freeMem();
		mxHeap = usedMXHeap();
		mxNonHeap = usedMXNonHeap();
		start = System.nanoTime();
		for (int i = 0; i < array.length; i++)
		{
			array[i] = "TestString: " + (i % different);
		}
		System.out.print("Duration String:        " + (System.nanoTime() - start) / 1000 + "us");
		freeMem();
		System.out.print("  HeapMem: " + (usedMXHeap() - mxHeap) / 1024 + "kB");
		System.out.println("  NonHeapMem: " + (usedMXNonHeap() - mxNonHeap) / 1024 + "kB");

		freeMem();
		mxHeap = usedMXHeap();
		mxNonHeap = usedMXNonHeap();
		start = System.nanoTime();
		for (int i = 0; i < size; i++)
		{
			arrayOwn[i] = Internalize.intern(array[i]);
		}
		
		System.out.print("Duration Internalize:   " + (System.nanoTime() - start) / 1000 + "us");
		freeMem();
		System.out.print("  HeapMem: " + (usedMXHeap() - mxHeap) / 1024 + "kB");
		System.out.println("  NonHeapMem: " + (usedMXNonHeap() - mxNonHeap) / 1024 + "kB");
		
		arrayOwn = new String[size];
		
		freeMem();
		mxHeap = usedMXHeap();
		mxNonHeap = usedMXNonHeap();
		start = System.nanoTime();
		synchronized (Internalize.class)
		{
			for (int i = 0; i < size; i++)
			{
				arrayOwn[i] = Internalize.internNoSync(array[i]);
			}
		}
		
		System.out.print("Duration Internalize NoSynch:   " + (System.nanoTime() - start) / 1000 + "us");
		freeMem();
		System.out.print("  HeapMem: " + (usedMXHeap() - mxHeap) / 1024 + "kB");
		System.out.println("  NonHeapMem: " + (usedMXNonHeap() - mxNonHeap) / 1024 + "kB");
		
		freeMem();
		mxHeap = usedMXHeap();
		mxNonHeap = usedMXNonHeap();
		start = System.nanoTime();
		for (int i = 0; i < size; i++)
		{
			arrayStd[i] = array[i].intern();
		}
		
		System.out.print("Duration String.intern: " + (System.nanoTime() - start) / 1000 + "us");
		freeMem();
		System.out.print("  HeapMem: " + (usedMXHeap() - mxHeap) / 1024 + "kB");
		System.out.println("  NonHeapMem: " + (usedMXNonHeap() - mxNonHeap) / 1024 + "kB");
		
	}
	
	/**
	 * Tests the Performance.
	 */
	@Test
	public void testPerformanceBigDecimal()
	{
		int size = 100000;
		int different = size / 5;
		
		long start;
		long mxHeap;
		long mxNonHeap;
		
		Object[] array = new Object[size];
		Object[] arrayOwn = new Object[size];
		Object[] arrayByt = new Object[size];
		freeMem();
		mxHeap = usedMXHeap();
		mxNonHeap = usedMXNonHeap();
		start = System.nanoTime();
		for (int i = 0; i < array.length; i++)
		{
			array[i] = new BigDecimal(i % different);
		}
		System.out.print("Duration BigDecimal:     " + (System.nanoTime() - start) / 1000 + "us");
		freeMem();
		System.out.print("  HeapMem: " + (usedMXHeap() - mxHeap) / 1024 + "kB");
		System.out.println("  NonHeapMem: " + (usedMXNonHeap() - mxNonHeap) / 1024 + "kB");

		array = new Object[size];
		
		freeMem();
		mxHeap = usedMXHeap();
		mxNonHeap = usedMXNonHeap();
		start = System.nanoTime();
		for (int i = 0; i < size; i++)
		{
			arrayOwn[i] = Internalize.intern(array[i]);
		}
		
		System.out.print("Duration Internalize:    " + (System.nanoTime() - start) / 1000 + "us");
		freeMem();
		System.out.print("  HeapMem: " + (usedMXHeap() - mxHeap) / 1024 + "kB");
		System.out.println("  NonHeapMem: " + (usedMXNonHeap() - mxNonHeap) / 1024 + "kB");

		array = new Object[size];
		
		freeMem();
		mxHeap = usedMXHeap();
		mxNonHeap = usedMXNonHeap();
		start = System.nanoTime();
		for (int i = 0; i < size; i++)
		{
			BigDecimal result = new BigDecimal(i % different);
			int scale = result.scale();
			byte[] val = result.movePointRight(scale).toBigInteger().toByteArray();
			byte[] res = new byte[val.length + 3];
			System.arraycopy(val, 0, res, 3, val.length);
			res[0] = (byte)(scale & 0xff);
			res[1] = (byte)((scale >> 8) & 0xff);
			res[2] = (byte)((scale >> 16) & 0xff);
		    arrayByt[i] = res;
		}
		
		System.out.print("Duration byte[]:     " + (System.nanoTime() - start) / 1000 + "us");
		freeMem();
		freeMem();
		System.out.print("  HeapMem: " + (usedMXHeap() - mxHeap) / 1024 + "kB");
		System.out.println("  NonHeapMem: " + (usedMXNonHeap() - mxNonHeap) / 1024 + "kB");
	}
	
}	// TestInternalizePerformance
