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
 * 29.12.2009 - [JR] - containsAll checks
 * 27.08.2010 - [JR] - removeAll tests
 * 30.07.2015 - [JR] - static char tests
 */
package com.sibvisions.util;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Vector;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the <code>ArrayUtil</code>.
 * 
 * @author Martin Handsteiner
 * @see ArrayUtil
 */
public class TestArrayUtil extends MemoryInfo
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** Random number generator. */
	private static Random random = new Random(); 

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Main method for performance and memory tests without junit.
	 * 
	 * @param pArgs command line arguments
	 */
	public static void main(String[] pArgs)
	{
		TestArrayUtil test = new TestArrayUtil();
		
		
		try 
		{
			test.testPerformanceAndMemoryUsage();
			test.testAveragePerformance();
			test.testMemoryUsage();
		}
		catch (Throwable ex)
		{
			ex.printStackTrace();
		}
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Tests static add and remove functions of <code>ArrayUtil</code>.
	 */
//	@Test
	public void testStaticAddRemove()
	{
		System.out.println("--- Static Add and Remove Tests ---");
		System.out.println("-----------------------------------");
		
		int[] node = ArrayUtil.add(new int[] {1, 2}, 3);
		
		System.out.println(Arrays.toString(node));
		Assert.assertTrue("add failed", node.length == 3);
		
		node = ArrayUtil.removeLast(node);
		System.out.println(Arrays.toString(node));
		Assert.assertTrue("removeLast failed", node.length == 2);

		node = ArrayUtil.addAll(node, 1, new int[] {7, 8, 9});
		System.out.println(Arrays.toString(node));
		Assert.assertTrue("addAll failed", node.length == 5);

		node = ArrayUtil.removeRange(node, 2, 4);
		System.out.println(Arrays.toString(node));
		Assert.assertTrue("removeRange failed", node.length == 3);

		node = ArrayUtil.clear(node);
		System.out.println(Arrays.toString(node));
		Assert.assertTrue("clear failed", node.length == 0);
		
		System.out.println();
	}	

	/**
	 * Tests performance and memory usage of ArrayUtil compared to ArrayList.
	 */
//	@Test
	public void testPerformanceAndMemoryUsage()
	{
		System.out.println("--- Performance and Memory Usage Tests ---");
		System.out.println("------------------------------------------");
		
for (int x = 0; x < 3; x++)
{
		long start;
		long mem;
		
		int rowCount = 100000;
		
		int count;
		Iterator<String> it;

		freeMem();
		mem = usedMemory();
		ArrayList<String> list1 = new ArrayList<String>();
		start = System.nanoTime();
		for (int i = 0; i < rowCount; i++)
		{
			list1.add(0, "Hallo");
		}
		System.out.print("Duration 'add(0)' ArrayList:    " + (System.nanoTime() - start) / 1000 + "us");
		freeMem();
		System.out.print("  Mem: " + (usedMemory() - mem) / 1024 + "kB");
		System.out.println("  Size: " + list1.size());
		
		start = System.nanoTime();
		it = list1.iterator();
		count = 0;
		while (it.hasNext())
		{
			if (it.next() != null)
			{
				count++;
			}
		}
		System.out.print("Duration 'iterator()' ArrayList:    " + (System.nanoTime() - start) / 1000 + "us");
		System.out.println("  Size: " + count);
		
		start = System.nanoTime();
		count = 0;
		for (int i = list1.size() - 1; i >= 0; i--)
		{
			if (list1.get(i) != null)
			{
				count++;
			}
		}
		System.out.print("Duration 'for i--' ArrayList:    " + (System.nanoTime() - start) / 1000 + "us");
		System.out.println("  Size: " + count);
		
		start = System.nanoTime();
		count = 0;
		for (int i = 0; i < list1.size(); i++)
		{
			if (list1.get(i) != null)
			{
				count++;
			}
		}
		System.out.print("Duration 'for i++' ArrayList:    " + (System.nanoTime() - start) / 1000 + "us");
		System.out.println("  Size: " + count);
		
        start = System.nanoTime();
        count = 0;
        for (int i = 0; i < 500; i++)
        {
            count += list1.indexOf("Hallo2");
        }
        System.out.print("Duration 'indexOf' ArrayList:    " + (System.nanoTime() - start) / 1000 + "us");
        System.out.println("  Size: " + count);
        
		start = System.nanoTime();
		for (int i = rowCount / 2; i < rowCount; i++)
		{
			list1.remove(0);
		}
		System.out.print("Duration 'remove(0)' ArrayList: " + (System.nanoTime() - start) / 1000 + "us");
		freeMem();
		System.out.print("  Mem: " + (usedMemory() - mem) / 1024 + "kB");
		System.out.println("  Size: " + list1.size());
		
		start = System.nanoTime();
		list1.clear();
		System.out.print("Duration 'clear' ArrayList:     " + (System.nanoTime() - start) / 1000 + "us");
		freeMem();
		System.out.print("  Mem: " + (usedMemory() - mem) / 1024 + "kB");
		System.out.println("  Size: " + list1.size());
		
		list1 = null;
		
		freeMem();
		mem = usedMemory();
		ArrayList<String> list2 = new ArrayList<String>();
		start = System.nanoTime();
		for (int i = 0; i < rowCount; i++)
		{
			list2.add("Hallo");
		}
		System.out.print("Duration 'add' ArrayList:       " + (System.nanoTime() - start) / 1000 + "us");
		freeMem();
		System.out.print("  Mem: " + (usedMemory() - mem) / 1024 + "kB");
		System.out.println("  Size: " + list2.size());
		
		start = System.nanoTime();
		for (int i = rowCount / 2; i < rowCount; i++)
		{
			list2.remove(list2.size() - 1);
		}
		System.out.print("Duration 'remove' ArrayList:    " + (System.nanoTime() - start) / 1000 + "us");
		freeMem();
		System.out.print("  Mem: " + (usedMemory() - mem) / 1024 + "kB");
		System.out.println("  Size: " + list2.size());
		
		start = System.nanoTime();
		list2.clear();
		System.out.print("Duration 'clear' ArrayList:     " + (System.nanoTime() - start) / 1000 + "us");
		freeMem();
		System.out.print("  Mem: " + (usedMemory() - mem) / 1024 + "kB");
		System.out.println("  Size: " + list2.size());
		
		list2 = null;
		
		System.out.println("---");

		freeMem();
		mem = usedMemory();
		ArrayUtil<String> arr1 = new ArrayUtil<String>();
		start = System.nanoTime();
		for (int i = 0; i < rowCount; i++)
		{
			arr1.add(0, "Hallo");
		}
		
		System.out.print("Duration 'add(0)' ArrayUtil:    " + (System.nanoTime() - start) / 1000 + "us");
		freeMem();
		System.out.print("  Mem: " + (usedMemory() - mem) / 1024 + "kB");
		System.out.println("  Size: " + arr1.size());
		
		start = System.nanoTime();
		it = arr1.iterator();
		count = 0;
		while (it.hasNext())
		{
			if (it.next() != null)
			{
				count++;
			}
		}
		System.out.print("Duration 'iterator()' ArrayUtil:    " + (System.nanoTime() - start) / 1000 + "us");
		System.out.println("  Size: " + count);
		
		start = System.nanoTime();
		count = 0;
		for (int i = arr1.size() - 1; i >= 0; i--)
		{
			if (arr1.get(i) != null)
			{
				count++;
			}
		}
		System.out.print("Duration 'for i--' ArrayUtil:    " + (System.nanoTime() - start) / 1000 + "us");
		System.out.println("  Size: " + count);
		
		start = System.nanoTime();
		count = 0;
		for (int i = 0; i < arr1.size(); i++)
		{
			if (arr1.get(i) != null)
			{
				count++;
			}
		}
		System.out.print("Duration 'for i++' ArrayUtil:    " + (System.nanoTime() - start) / 1000 + "us");
		System.out.println("  Size: " + count);
		
        start = System.nanoTime();
        count = 0;
        for (int i = 0; i < 500; i++)
        {
            count += arr1.indexOf("Hallo2");
        }
        System.out.print("Duration 'indexOf' ArrayUtil:    " + (System.nanoTime() - start) / 1000 + "us");
        System.out.println("  Size: " + count);
        
		start = System.nanoTime();
		for (int i = rowCount / 2; i < rowCount; i++)
		{
			arr1.remove(0);
		}
		System.out.print("Duration 'remove(0)' ArrayUtil: " + (System.nanoTime() - start) / 1000 + "us");
		freeMem();
		System.out.print("  Mem: " + (usedMemory() - mem) / 1024 + "kB");
		System.out.println("  Size: " + arr1.size());
		
		start = System.nanoTime();
		arr1.clear();
		System.out.print("Duration 'clear' ArrayUtil:     " + (System.nanoTime() - start) / 1000 + "us");
		freeMem();
		System.out.print("  Mem: " + (usedMemory() - mem) / 1024 + "kB");
		System.out.println("  Size: " + arr1.size());

		arr1 = null;
		
		freeMem();
		mem = usedMemory();
		ArrayUtil<String> arr2 = new ArrayUtil<String>();
		start = System.nanoTime();
		for (int i = 0; i < rowCount; i++)
		{
			arr2.add("Hallo");
		}
		System.out.print("Duration 'add' ArrayUtil:       " + (System.nanoTime() - start) / 1000 + "us");
		freeMem();
		System.out.print("  Mem: " + (usedMemory() - mem) / 1024 + "kB");
		System.out.println("  Size: " + arr2.size());

		start = System.nanoTime();
		for (int i = rowCount / 2; i < rowCount; i++)
		{
			arr2.remove(arr2.size() - 1);
		}
		System.out.print("Duration 'remove' ArrayUtil:    " + (System.nanoTime() - start) / 1000 + "us");
		freeMem();
		System.out.print("  Mem: " + (usedMemory() - mem) / 1024 + "kB");
		System.out.println("  Size: " + arr2.size());
		
		start = System.nanoTime();
		arr2.clear();
		System.out.print("Duration 'clear' ArrayUtil:     " + (System.nanoTime() - start) / 1000 + "us");
		freeMem();
		System.out.print("  Mem: " + (usedMemory() - mem) / 1024 + "kB");
		System.out.println("  Size: " + arr2.size());
		System.out.println();

		arr2 = null;
}
	}	

	/**
	 * Tests average performance and memory usage of ArrayUtil compared to ArrayList.
	 */
//	@Test
	public void testAveragePerformance()
	{
		System.out.println("--- Average Performance Tests ---");
		System.out.println("---------------------------------");
		
		long start;
		long mem;
		
		int rowCount = 200000;

		freeMem();
		mem = usedMemory();
		ArrayList<String> list = new ArrayList<String>();
		start = System.nanoTime();
		list.add(0, "Hallo");
		for (int i = 1; i < rowCount; i++)
		{
			list.add(random.nextInt(i), "Hallo");
		}
		System.out.print("Duration add ArrayList:    " + (System.nanoTime() - start) / 1000 + "us");
		freeMem();
		System.out.print("  Mem: " + (usedMemory() - mem) / 1024 + "kB");
		System.out.println("  Size: " + list.size());
		
		start = System.nanoTime();
		for (int i = rowCount; i > 0; i--)
		{
			list.remove(random.nextInt(i));
		}
		System.out.print("Duration remove ArrayList: " + (System.nanoTime() - start) / 1000 + "us");
		freeMem();
		System.out.print("  Mem: " + (usedMemory() - mem) / 1024 + "kB");
		System.out.println("  Size: " + list.size());
		
		freeMem();
		mem = usedMemory();
		ArrayUtil<String> arr = new ArrayUtil<String>();
		start = System.nanoTime();
		arr.add(0, "Hallo");
		for (int i = 1; i < rowCount; i++)
		{
			arr.add(random.nextInt(i), "Hallo");
		}

		System.out.println("---");

		System.out.print("Duration add ArrayUtil:    " + (System.nanoTime() - start) / 1000 + "us");
		freeMem();
		System.out.print("  Mem: " + (usedMemory() - mem) / 1024 + "kB");
		System.out.println("  Size: " + arr.size());

		start = System.nanoTime();
		for (int i = rowCount; i > 0; i--)
		{
			arr.remove(random.nextInt(i));
		}
		System.out.print("Duration remove ArrayUtil: " + (System.nanoTime() - start) / 1000 + "us");
		freeMem();
		System.out.print("  Mem: " + (usedMemory() - mem) / 1024 + "kB");
		System.out.println("  Size: " + list.size());
		System.out.println();
	}	

	/**
	 * Tests memory usage of ArrayList in ArrayList (DataPage Simulation)
	 * against Object[] in ArrayUtil.
	 */
//	@Test
	public void testMemoryUsage()
	{
		System.out.println("--- Memory Usage Tests (DataPage Simulation) ---");
		System.out.println("------------------------------------------------");
		
		long mem;
		int rowCount = 100000;

		ArrayList<Object> rowAL = new ArrayList<Object>();
/*		
 		rowAL.add(new BigDecimal(12343));
		rowAL.add("Name der Zeile");
		rowAL.add("Attribut1");
		rowAL.add("Attribut2");
		rowAL.add(new BigDecimal(23));
		rowAL.add("Referenz Name");
*/		
		freeMem();
		mem = usedMemory();
		ArrayList<ArrayList> pageAL = new ArrayList<ArrayList>();
		for (int i = 0; i < rowCount; i++)
		{
			pageAL.add((ArrayList)rowAL.clone()); // ohne Buffer
//			pageAL.add(new ArrayList<Object>());  // mit Buffer
		}
		freeMem();
		System.out.print("ArrayList Mem: " + (usedMemory() - mem) / 1024 + "kB");
		System.out.println("  Size: " + pageAL.size());
		
		Object[] rowAU = rowAL.toArray();
		
		freeMem();
		mem = usedMemory();
		ArrayUtil<Object[]> pageAU = new ArrayUtil<Object[]>();
		for (int i = 0; i < rowCount; i++)
		{
			pageAU.add(rowAU.clone());
		}
		freeMem();
		System.out.print("ArrayUtil Mem: " + (usedMemory() - mem) / 1024 + "kB");
		System.out.println("  Size: " + pageAU.size());
		System.out.println();
	}	
	
	/**
	 * Tests the initial size of an <code>ArrayUtil</code>.
	 */
	@Test
	public void testInitialSize()
	{
		ArrayUtil<String> auInit = new ArrayUtil<String>(1);
		
		auInit.add("EINS");
		
		Assert.assertEquals(1, auInit.size());
		
		auInit.add("ZWEI");
		
		Assert.assertEquals(2, auInit.size());
	}
	
	/**
	 * Tests all static add method calls.
	 */
	@Test
	public void testStaticAdd()
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// double
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		double[] dValue = ArrayUtil.add(new double[] {1D, 2D, 5.6D}, 8D);
		
		Assert.assertEquals(4, dValue.length);
		Assert.assertEquals(2D, dValue[1], 0);
		
		dValue = ArrayUtil.add(dValue, 1, 4.4D);
		
		Assert.assertEquals(5, dValue.length);
		Assert.assertEquals(4.4D, dValue[1], 0);
		
		try
		{
			dValue = ArrayUtil.add(dValue, 10, 4.4D);
			
			Assert.fail("Invalid index: 10");
		}
		catch (IndexOutOfBoundsException iobe)
		{
			//it works
		}
		
		dValue = null;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// float
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		float[] fValue = ArrayUtil.add(new float[] {1F, 2F, 5.6F}, 8F);
		
		Assert.assertEquals(4, fValue.length);
		Assert.assertEquals(2F, fValue[1], 0);
		
		fValue = ArrayUtil.add(fValue, 1, 4.4F);
		
		Assert.assertEquals(5, fValue.length);
		Assert.assertEquals(4.4F, fValue[1], 0);
		
		try
		{
			fValue = ArrayUtil.add(fValue, 10, 4.4F);
			
			Assert.fail("Invalid index: 10");
		}
		catch (IndexOutOfBoundsException iobe)
		{
			//it works
		}
		
		fValue = null;

		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// int
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		int[] iValue = ArrayUtil.add(new int[] {1, 2, 5}, 8);
		
		Assert.assertEquals(4, iValue.length);
		Assert.assertEquals(2, iValue[1]);
		
		iValue = ArrayUtil.add(iValue, 1, 4);
		
		Assert.assertEquals(5, iValue.length);
		Assert.assertEquals(4, iValue[1]);

		try
		{
			iValue = ArrayUtil.add(iValue, 10, 4);
			
			Assert.fail("Invalid index: 10");
		}
		catch (IndexOutOfBoundsException iobe)
		{
			//it works
		}

		iValue = null;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// long
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		long[] lValue = ArrayUtil.add(new long[] {1L, 2L, 5L}, 8L);
		
		Assert.assertEquals(4, lValue.length);
		Assert.assertEquals(2L, lValue[1]);
		
		lValue = ArrayUtil.add(lValue, 1, 4L);
		
		Assert.assertEquals(5, lValue.length);
		Assert.assertEquals(4L, lValue[1]);
		
		try
		{
			lValue = ArrayUtil.add(lValue, 10, 4L);
			
			Assert.fail("Invalid index: 10");
		}
		catch (IndexOutOfBoundsException iobe)
		{
			//it works
		}

		lValue = null;

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // char
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        
        char[] chValue = ArrayUtil.add(new char[] {'1', '2', '5'}, '8');
        
        Assert.assertEquals(4, chValue.length);
        Assert.assertEquals('2', chValue[1]);
        
        chValue = ArrayUtil.add(chValue, 1, '4');
        
        Assert.assertEquals(5, chValue.length);
        Assert.assertEquals('4', chValue[1]);

        try
        {
            chValue = ArrayUtil.add(chValue, 10, '4');
            
            Assert.fail("Invalid index: 10");
        }
        catch (IndexOutOfBoundsException iobe)
        {
            //it works
        }

        chValue = null;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Object
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		Assert.assertEquals(1, ArrayUtil.add((Object[])null, "TEST").length);
		
		Vector<String> vList1 = new Vector<String>(1);
		Vector<String> vList2 = new Vector<String>(2);
		Vector<String> vList3 = new Vector<String>(1);
		
		Object[] oList1 = {vList1};
		Object[] oResult;
		
		vList1.add("1.0");
		
		vList2.add("2.0");
		vList2.add("2.1");
		
		vList3.add("3.0");

		oResult = ArrayUtil.add(oList1, vList2);
		
		Assert.assertEquals(2, oResult.length);
		Assert.assertEquals("[[1.0], [2.0, 2.1]]", Arrays.toString(oResult));
		
		oResult = ArrayUtil.add(oResult, 1, vList3);
		
		Assert.assertEquals(3, oResult.length);
		Assert.assertEquals("[[1.0], [3.0], [2.0, 2.1]]", Arrays.toString(oResult));

		try
		{
			oResult = ArrayUtil.add(oList1, 10, vList2);
			
			Assert.fail("Invalid index: 10");
		}
		catch (IndexOutOfBoundsException iobe)
		{
			//it works
		}
		
		oResult = null;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// String
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		String[] sNames = new String[0];
		
		sNames = ArrayUtil.add(sNames, "A");
	
		Assert.assertArrayEquals(new String[] {"A"}, sNames);
		
		sNames = new String[] {"B"};
		
		sNames = ArrayUtil.add(sNames, "C");
		
		Assert.assertArrayEquals(new String[] {"B", "C"}, sNames);
		
		sNames = null;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Object
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		Object[] oaDifferentObjects = new Object[] {};
		
		oaDifferentObjects = ArrayUtil.add(oaDifferentObjects, Integer.valueOf(15));
		oaDifferentObjects = ArrayUtil.add(oaDifferentObjects, "a");
	}
	
	/**
	 * Tests all static addAll method calls.
	 */
	@Test
	public void testStaticAddAll()
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// double
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		double[] dValue = ArrayUtil.addAll(new double[] {1D, 2D, 2.5D}, new double[] {8D, 9.987D});
		
		Assert.assertEquals(5, dValue.length);
		Assert.assertEquals(8D, dValue[3], 0);
		
		dValue = ArrayUtil.addAll(dValue, 1, new double[] {88D, 99D});
		
		Assert.assertEquals(7, dValue.length);
		Assert.assertEquals(1D, dValue[0], 0);
		Assert.assertEquals(88D, dValue[1], 0);
		Assert.assertEquals(99D, dValue[2], 0);
		Assert.assertEquals(2D, dValue[3], 0);

		try
		{
			dValue = ArrayUtil.addAll(dValue, 100, new double[] {100D});
			
			Assert.fail("Invalid index: 100");
		}
		catch (IndexOutOfBoundsException iobe)
		{
			//it works
		}

		double[] dResult;
		
		//null adden versuchen
		dResult = ArrayUtil.addAll(dValue, null);
		
		Assert.assertTrue("Different double[] instance!", dResult == dValue);
		
		//leeres Array adden versuchen
		dResult = ArrayUtil.addAll(dValue, new double[] {});

		Assert.assertTrue("Different double[] instance!", dResult == dValue);
		
		dResult = null;
		dValue = null;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// float
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		float[] fValue = ArrayUtil.addAll(new float[] {1F, 2F, 2.5F}, new float[] {8F, 9.987F});
		
		Assert.assertEquals(5, fValue.length);
		Assert.assertEquals(8D, fValue[3], 0);
		
		fValue = ArrayUtil.addAll(fValue, 1, new float[] {88F, 99F});
		
		Assert.assertEquals(7, fValue.length);
		Assert.assertEquals(1F, fValue[0], 0);
		Assert.assertEquals(88F, fValue[1], 0);
		Assert.assertEquals(99F, fValue[2], 0);
		Assert.assertEquals(2F, fValue[3], 0);
		
		try
		{
			fValue = ArrayUtil.addAll(fValue, 100, new float[] {100F});
			
			Assert.fail("Invalid index: 100");
		}
		catch (IndexOutOfBoundsException iobe)
		{
			//it works
		}
		
		float[] fResult;
		
		//null adden versuchen
		fResult = ArrayUtil.addAll(fValue, null);
		
		Assert.assertTrue("Different float[] instance!", fResult == fValue);
		
		//leeres Array adden versuchen
		fResult = ArrayUtil.addAll(fValue, new float[] {});

		Assert.assertTrue("Different float[] instance!", fResult == fValue);
		
		fResult = null;
		fValue = null;

		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// int
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		int[] iValue = ArrayUtil.addAll(new int[] {1, 2, 2}, new int[] {8, 9});
		
		Assert.assertEquals(5, iValue.length);
		Assert.assertEquals(8, iValue[3]);
		
		iValue = ArrayUtil.addAll(iValue, 1, new int[] {88, 99});
		
		Assert.assertEquals(7, iValue.length);
		Assert.assertEquals(1, iValue[0]);
		Assert.assertEquals(88, iValue[1]);
		Assert.assertEquals(99, iValue[2]);
		Assert.assertEquals(2, iValue[3]);
		
		try
		{
			iValue = ArrayUtil.addAll(iValue, 100, new int[] {100});
			
			Assert.fail("Invalid index: 100");
		}
		catch (IndexOutOfBoundsException iobe)
		{
			//it works
		}

		int[] iResult;
		
		//null adden versuchen
		iResult = ArrayUtil.addAll(iValue, null);
		
		Assert.assertTrue("Different int[] instance!", iResult == iValue);
		Assert.assertArrayEquals(iResult, iValue);
		
		//leeres Array adden versuchen
		iResult = ArrayUtil.addAll(iValue, new int[] {});

		Assert.assertTrue("Different int[] instance!", iResult == iValue);
		Assert.assertArrayEquals(iResult, iValue);
		
		iResult = null;
		iValue = null;
		
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // char
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        
        char[] chValue = ArrayUtil.addAll(new char[] {'1', '2', '2'}, new char[] {'8', '9'});
        
        Assert.assertEquals(5, chValue.length);
        Assert.assertEquals('8', chValue[3]);
        
        chValue = ArrayUtil.addAll(chValue, 1, new char[] {'A', 'B'});
        
        Assert.assertEquals(7, chValue.length);
        Assert.assertEquals('1', chValue[0]);
        Assert.assertEquals('A', chValue[1]);
        Assert.assertEquals('B', chValue[2]);
        Assert.assertEquals('2', chValue[3]);
        
        try
        {
            chValue = ArrayUtil.addAll(chValue, 100, new char[] {'C'});
            
            Assert.fail("Invalid index: 100");
        }
        catch (IndexOutOfBoundsException iobe)
        {
            //it works
        }

        char[] chResult;
        
        //null adden versuchen
        chResult = ArrayUtil.addAll(chValue, null);
        
        Assert.assertTrue("Different char[] instance!", chResult == chValue);
        Assert.assertArrayEquals(chResult, chValue);
        
        //leeres Array adden versuchen
        chResult = ArrayUtil.addAll(chValue, new char[] {});

        Assert.assertTrue("Different char[] instance!", chResult == chValue);
        Assert.assertArrayEquals(chResult, chValue);
        
        chResult = null;
        chValue = null;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// long
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		long[] lValue = ArrayUtil.addAll(new long[] {1L, 2L, 2L}, new long[] {8L, 9L});
		
		Assert.assertEquals(5, lValue.length);
		Assert.assertEquals(8L, lValue[3]);
		
		lValue = ArrayUtil.addAll(lValue, 1, new long[] {88L, 99L});
		
		Assert.assertEquals(7, lValue.length);
		Assert.assertEquals(1L, lValue[0]);
		Assert.assertEquals(88L, lValue[1]);
		Assert.assertEquals(99L, lValue[2]);
		Assert.assertEquals(2L, lValue[3]);
		
		try
		{
			lValue = ArrayUtil.addAll(lValue, 100, new long[] {100L});
			
			Assert.fail("Invalid index: 100");
		}
		catch (IndexOutOfBoundsException iobe)
		{
			//it works
		}
		
		long[] lResult;
		
		//null adden versuchen
		lResult = ArrayUtil.addAll(lValue, null);
		
		Assert.assertTrue("Different long[] instance!", lResult == lValue);
		Assert.assertArrayEquals(lResult, lValue);
		
		//leeres Array adden versuchen
		lResult = ArrayUtil.addAll(lValue, new long[] {});

		Assert.assertTrue("Different long[] instance!", lResult == lValue);
		Assert.assertArrayEquals(lResult, lValue);
		
		lResult = null;
		lValue = null;

		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Object
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		Vector<String> vList1 = new Vector<String>(1);
		Vector<String> vList2 = new Vector<String>(2);
		Vector<String> vList3 = new Vector<String>(2);
		
		Object[] oList1 = {vList1};
		Object[] oList2 = {vList2, vList3};
		Object[] oResult;
		
		vList1.add("1.0");
		
		vList2.add("2.0");
		vList2.add("2.1");
		
		vList3.add("3.0");
		vList3.add("3.1");

		//null adden versuchen
		oResult = ArrayUtil.addAll(oList1, null);
		
		Assert.assertTrue("Different Object[] instance!", oResult == oList1);
		Assert.assertArrayEquals(oResult, oList1);
		
		//leeres Array adden versuchen
		oResult = ArrayUtil.addAll(oList1, new Object[] {});

		Assert.assertTrue("Different Object[] instance!", oResult == oList1);
		Assert.assertArrayEquals(oResult, oList1);

		//ohne Index
		oResult = ArrayUtil.addAll(oList1, oList2);
		
		Assert.assertEquals(3, oResult.length);
		Assert.assertEquals("[[1.0], [2.0, 2.1], [3.0, 3.1]]", Arrays.toString(oResult));

		oResult = ArrayUtil.addAll(oList1, 0, oList2);
		
		Assert.assertEquals(3, oResult.length);
		Assert.assertEquals("[[2.0, 2.1], [3.0, 3.1], [1.0]]", Arrays.toString(oResult));
		
		try
		{
			oResult = ArrayUtil.addAll(oResult, 100, oList2);
			
			Assert.fail("Invalid index: 100");
		}
		catch (IndexOutOfBoundsException iobe)
		{
			//it works
		}
		
		oResult = null;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// String
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		String[] sNames = new String[0];
		
		sNames = ArrayUtil.addAll(sNames, new String[] {"A", "B", "C"});
	
		Assert.assertArrayEquals(new String[] {"A", "B", "C"}, sNames);
		
		sNames = new String[] {"B"};
		
		sNames = ArrayUtil.addAll(sNames, new String[] {"C", "ERNTE", "TASSE"});
		
		Assert.assertArrayEquals(new String[] {"B", "C", "ERNTE", "TASSE"}, sNames);
		
		sNames = null;
		
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Object
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		Object[] oaDifferentObjects = new Object[] {};
		
		oaDifferentObjects = ArrayUtil.addAll(oaDifferentObjects, new Object[] { Integer.valueOf(15), "a" });
		oaDifferentObjects = ArrayUtil.addAll(oaDifferentObjects, new Object[] { Integer.valueOf(15), "a" });
	}
	
	/**
	 * Tests static addAll with a null source.
	 */
	@Test
	public void testStaticAddAllWithNullSource()
	{
		Hashtable<String, String[]> htObjects = new Hashtable<String, String[]>();
		
		//without ClassCastException
		htObjects.put("KEY", (String[])ArrayUtil.addAll(null, new String[] {"1"}));
	}
	
	/**
	 * Tests all static clear method calls.
	 */
	@Test
	public void testStaticClear()
	{
		double[] dValue = {1D, 2D, 3D, 4.4D, 6D};
		float[]  fValue = {1F, 2F, 3F, 4.4F, 6F};
		int[]    iValue = {1, 2, 3, 4, 6};
		long[]   lValue = {1L, 2L, 3L, 4L, 6L};
		char[]   chValue = {'1', '2', '3', '4', '6'};
		
		Object[] oValue = {"1.0", "2.0"};
	
		
		dValue = ArrayUtil.clear(dValue);
		
		Assert.assertEquals(0, dValue.length);

		
		fValue = ArrayUtil.clear(fValue);
		
		Assert.assertEquals(0, fValue.length);
		
		
		iValue = ArrayUtil.clear(iValue);
		
		Assert.assertEquals(0, iValue.length);
		
		
		lValue = ArrayUtil.clear(lValue);
		
		Assert.assertEquals(0, lValue.length);
		

		oValue = ArrayUtil.clear(oValue);
		
		Assert.assertEquals(0, oValue.length);

		
        chValue = ArrayUtil.clear(chValue);
        
        Assert.assertEquals(0, chValue.length);
	}
	
	/**
	 * Tests all static indexOf method calls.
	 */
	@Test
	public void testStaticIndexOf()
	{
		Assert.assertEquals(3, ArrayUtil.indexOf(new double[] {1D, 1.25D, 2D, 9.08D, 6D}, 9.08D));
		Assert.assertEquals(-1, ArrayUtil.indexOf(new double[] {1D, 1.25D, 2D, 9.08D, 6D}, 0D));
		Assert.assertEquals(3, ArrayUtil.indexOf(new double[] {1D, 1.25D, 2D, 9.08D, 6D}, 9.08D, 2));
		Assert.assertEquals(-1, ArrayUtil.indexOf(new double[] {1D, 1.25D, 2D, 9.08D, 6D}, 9.08D, 4));
		
		Assert.assertEquals(1, ArrayUtil.indexOf(new float[] {1F, 1.25F, 2F, 9.08F, 6F}, 1.25F));
		Assert.assertEquals(-1, ArrayUtil.indexOf(new float[] {1F, 1.25F, 2F, 9.08F, 6F}, 0F));
		Assert.assertEquals(1, ArrayUtil.indexOf(new float[] {1F, 1.25F, 2F, 9.08F, 6F}, 1.25F, 1));
		Assert.assertEquals(-1, ArrayUtil.indexOf(new float[] {1F, 1.25F, 2F, 9.08F, 6F}, 1.25F, 2));
		
		Assert.assertEquals(4, ArrayUtil.indexOf(new long[] {1L, 125L, 2L, 908L, 6L}, 6L));
		Assert.assertEquals(-1, ArrayUtil.indexOf(new long[] {1L, 125L, 2L, 908L, 6L}, 0L));
		Assert.assertEquals(4, ArrayUtil.indexOf(new long[] {1L, 125L, 2L, 908L, 6L}, 6L, 2));
		Assert.assertEquals(-1, ArrayUtil.indexOf(new long[] {1L, 125L, 2L, 908L, 6L}, 6L, 6));
		
		Assert.assertEquals(2, ArrayUtil.indexOf(new int[] {1, 15, 2, 9, 6}, 2));
		Assert.assertEquals(-1, ArrayUtil.indexOf(new int[] {1, 15, 2, 9, 6}, 0));
		Assert.assertEquals(2, ArrayUtil.indexOf(new int[] {1, 15, 2, 9, 2}, 2, 1));
		Assert.assertEquals(-1, ArrayUtil.indexOf(new int[] {1, 15, 2, 9, 2}, 2, 90));

		Assert.assertEquals(1, ArrayUtil.indexOf(new Object[] {"1.0", new BigDecimal(200), Integer.valueOf(10)}, new BigDecimal(200)));
		Assert.assertEquals(-1, ArrayUtil.indexOf(new Object[] {"1.0", new BigDecimal(200), Integer.valueOf(10)}, new BigDecimal(0)));
		Assert.assertEquals(1, ArrayUtil.indexOf(new Object[] {"1.0", new BigDecimal(200), Integer.valueOf(10)}, new BigDecimal(200), 1));
		Assert.assertEquals(-1, ArrayUtil.indexOf(new Object[] {"1.0", new BigDecimal(200), Integer.valueOf(10)}, new BigDecimal(200), 2));

        Assert.assertEquals(2, ArrayUtil.indexOf(new char[] {'1', 'F', '2', '9', '6'}, '2'));
        Assert.assertEquals(-1, ArrayUtil.indexOf(new char[] {'1', 'F', '2', '9', '6'}, '0'));
        Assert.assertEquals(2, ArrayUtil.indexOf(new char[] {'1', 'F', '2', '9', '6'}, '2', 1));
        Assert.assertEquals(-1, ArrayUtil.indexOf(new char[] {'1', 'F', '2', '9', '6'}, '2', 90));
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// weak references
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		String sWeak = "Weak search";
		WeakReference<String> wrString = new WeakReference<String>("Weak search");

		WeakReference[] weak = new WeakReference[] {wrString};
		
		
		Assert.assertEquals(-1, ArrayUtil.indexOf(weak, "Not found"));
		Assert.assertEquals(0, ArrayUtil.indexOf(weak, sWeak));
		Assert.assertEquals(0, ArrayUtil.indexOf(weak, wrString));

		
		String[] string = new String[] {sWeak};
		
		Assert.assertEquals(-1, ArrayUtil.indexOf(string, "Not found"));
		Assert.assertEquals(0, ArrayUtil.indexOf(string, sWeak));
		Assert.assertEquals(0, ArrayUtil.indexOf(string, wrString));
	}
	
	/**
	 * Tests all static indexOfReference method calls.
	 */
	@Test
	public void testStaticIndexOfReference()
	{
		Integer i100 = new Integer(100);
		Vector<String> vValue = new Vector<String>(1);
		
		Object[] oRef1 = {"1.0", new BigDecimal(1), vValue, i100};
		
		
		Assert.assertEquals(3, ArrayUtil.indexOfReference(oRef1, i100));
		Assert.assertEquals(-1, ArrayUtil.indexOfReference(oRef1, new Integer(100)));
		
		Assert.assertEquals(3, ArrayUtil.indexOfReference(oRef1, i100, 2));
		Assert.assertEquals(-1, ArrayUtil.indexOfReference(oRef1, i100, 50));
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// weak references
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		String sWeak = new String("Weak search");
		WeakReference<String> wrString = new WeakReference<String>("Weak search");
		WeakReference<String> wrStringRef = new WeakReference<String>(sWeak);

		WeakReference[] weak = new WeakReference[] {wrString, wrStringRef};
		
		Assert.assertEquals(-1, ArrayUtil.indexOfReference(weak, "Not found"));
		Assert.assertEquals(1, ArrayUtil.indexOfReference(weak, sWeak));
		Assert.assertEquals(1, ArrayUtil.indexOfReference(weak, wrStringRef));
		
		String[] string = new String[] {new String("Weak search"), sWeak};
		
		Assert.assertEquals(-1, ArrayUtil.indexOfReference(string, "Not found"));
		Assert.assertEquals(1, ArrayUtil.indexOfReference(string, sWeak));
		Assert.assertEquals(1, ArrayUtil.indexOfReference(string, wrStringRef));
		
	}
	
	/**
	 * Tests all static lastIndexOf method calls.
	 */
	@Test
	public void testStaticLastIndexOf()
	{
		Assert.assertEquals(6, ArrayUtil.lastIndexOf(new double[] {1D, 1.25D, 2D, 9.08D, 6D, 5D, 9.08D, 8}, 9.08D));
		Assert.assertEquals(-1, ArrayUtil.lastIndexOf(new double[] {1D, 1.25D, 2D, 9.08D, 6D, 5D, 9.08D, 8}, 0D));
		Assert.assertEquals(6, ArrayUtil.lastIndexOf(new double[] {1D, 1.25D, 2D, 9.08D, 6D, 5D, 9.08D, 8}, 9.08D, 7));
		Assert.assertEquals(6, ArrayUtil.lastIndexOf(new double[] {1D, 1.25D, 2D, 9.08D, 6D, 5D, 9.08D, 8}, 9.08D, -1));
		Assert.assertEquals(3, ArrayUtil.lastIndexOf(new double[] {1D, 1.25D, 2D, 9.08D, 6D, 5D, 9.08D, 8}, 9.08D, 4));
		Assert.assertEquals(-1, ArrayUtil.lastIndexOf(new double[] {1D, 1.25D, 2D, 9.08D, 6D, 5D, 9.08D, 8}, 9.08D, 90));
		
		Assert.assertEquals(7, ArrayUtil.lastIndexOf(new float[] {1F, 1.25F, 2F, 9.08F, 6F, 5F, 1.251F, 1.25F, 9.08F}, 1.25F));
		Assert.assertEquals(-1, ArrayUtil.lastIndexOf(new float[] {1F, 1.25F, 2F, 9.08F, 6F, 5F, 9F, 9.08F}, 0F));
		Assert.assertEquals(8, ArrayUtil.lastIndexOf(new float[] {1F, 1.25F, 2F, 9.08F, 6F, 5F, 9F, 9.08F, 1.25F}, 1.25F, 8));
		Assert.assertEquals(8, ArrayUtil.lastIndexOf(new float[] {1F, 1.25F, 2F, 9.08F, 6F, 5F, 9F, 9.08F, 1.25F}, 1.25F, -1));
		Assert.assertEquals(-1, ArrayUtil.lastIndexOf(new float[] {1F, 1.25F, 2F, 9.08F, 6F, 5F, 9F, 9.08F}, 1.25F, 0));

		Assert.assertEquals(9, ArrayUtil.lastIndexOf(new long[] {1L, 125L, 2L, 908L, 6L, 0L, 1L, 2L, 5L, 6L, 7L, 8L}, 6L));
		Assert.assertEquals(-1, ArrayUtil.lastIndexOf(new long[] {1L, 125L, 2L, 908L, 6L, 0L, 1L, 2L, 5L, 6L, 7L, 8L}, -1L));
		Assert.assertEquals(7, ArrayUtil.lastIndexOf(new long[] {1L, 125L, 2L, 908L, 6L, 0L, 1L, 2L, 5L, 6L, 7L, 8L}, 2L, 8));
		Assert.assertEquals(7, ArrayUtil.lastIndexOf(new long[] {1L, 125L, 2L, 908L, 6L, 0L, 1L, 2L, 5L, 6L, 7L, 8L}, 2L, -1));
		Assert.assertEquals(-1, ArrayUtil.lastIndexOf(new long[] {1L, 125L, 2L, 908L, 6L, 0L, 1L, 2L, 5L, 6L, 7L, 8L}, 2L, 1));
		
		Assert.assertEquals(7, ArrayUtil.lastIndexOf(new int[] {1, 15, 2, 9, 6, 7, 5, 2, 4, 5, 6, 7, 8, 7, 6, 5}, 2));
		Assert.assertEquals(-1, ArrayUtil.lastIndexOf(new int[] {1, 15, 2, 9, 6, 7, 5, 2, 4, 5, 6, 7, 8, 7, 6, 5}, 0));
		Assert.assertEquals(2, ArrayUtil.lastIndexOf(new int[] {1, 15, 2, 9, 6, 7, 5, 2, 4, 5, 6, 7, 8, 7, 6, 5}, 2, 3));
		Assert.assertEquals(7, ArrayUtil.lastIndexOf(new int[] {1, 15, 2, 9, 6, 7, 5, 2, 4, 5, 6, 7, 8, 7, 6, 5}, 2, -1));
		Assert.assertEquals(-1, ArrayUtil.lastIndexOf(new int[] {1, 15, 2, 9, 6, 7, 5, 2, 4, 5, 6, 7, 8, 7, 6, 5}, 2, 90));

        Assert.assertEquals(7, ArrayUtil.lastIndexOf(new char[] {'1', 'F', '2', '9', '6', '7', '5', '2', '4', '5', '6', '7', '8', '7', '6', '5'}, '2'));
        Assert.assertEquals(-1, ArrayUtil.lastIndexOf(new char[] {'1', 'F', '2', '9', '6', '7', '5', '2', '4', '5', '6', '7', '8', '7', '6', '5'}, '0'));
        Assert.assertEquals(2, ArrayUtil.lastIndexOf(new char[] {'1', 'F', '2', '9', '6', '7', '5', '2', '4', '5', '6', '7', '8', '7', '6', '5'}, '2', 3));
        Assert.assertEquals(7, ArrayUtil.lastIndexOf(new char[] {'1', 'F', '2', '9', '6', '7', '5', '2', '4', '5', '6', '7', '8', '7', '6', '5'}, '2', -1));
        Assert.assertEquals(-1, ArrayUtil.lastIndexOf(new char[] {'1', 'F', '2', '9', '6', '7', '5', '2', '4', '5', '6', '7', '8', '7', '6', '5'}, '2', 90));
		
		Assert.assertEquals(4, ArrayUtil.lastIndexOf(new Object[] {"1.0", new BigDecimal(200), Integer.valueOf(10), 
				                                                   "2.0", new BigDecimal(200), Integer.valueOf(100)}, new BigDecimal(200)));
		Assert.assertEquals(-1, ArrayUtil.lastIndexOf(new Object[] {"1.0", new BigDecimal(200), Integer.valueOf(10), 
				                                                    "2.0", new BigDecimal(200), Integer.valueOf(100)}, new BigDecimal(0)));
		Assert.assertEquals(1, ArrayUtil.lastIndexOf(new Object[] {"1.0", new BigDecimal(200), Integer.valueOf(10), 
				                                                   "2.0", new BigDecimal(200), Integer.valueOf(100)}, new BigDecimal(200), 1));
		Assert.assertEquals(4, ArrayUtil.lastIndexOf(new Object[] {"1.0", new BigDecimal(200), Integer.valueOf(10), 
                                                                   "2.0", new BigDecimal(200), Integer.valueOf(100)}, new BigDecimal(200), -1));
		Assert.assertEquals(-1, ArrayUtil.lastIndexOf(new Object[] {"1.0", new BigDecimal(200), Integer.valueOf(10), 
				                                                    "2.0", new BigDecimal(200), Integer.valueOf(100)}, new BigDecimal(200), 0));
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// weak references
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		String sWeak = "Weak search";
		WeakReference<String> wrString = new WeakReference<String>("Weak search");
		WeakReference<String> wrString2 = new WeakReference<String>("Weak search");
		
		WeakReference[] weak = new WeakReference[] 
		                       {
							       new WeakReference<String>("First"), 
							       wrString, 
							       new WeakReference<String>("Second"), 
							       wrString2, 
							       new WeakReference<String>("Fourth")
							   };
		
		Assert.assertEquals(-1, ArrayUtil.lastIndexOf(weak, "Not found"));
		Assert.assertEquals(3, ArrayUtil.lastIndexOf(weak, sWeak));
		Assert.assertEquals(3, ArrayUtil.lastIndexOf(weak, wrString));
		
		String[] string = new String[] {"First", sWeak, "Second", sWeak, "Fifth"};
		
		Assert.assertEquals(-1, ArrayUtil.lastIndexOf(string, "Not found"));
		Assert.assertEquals(3, ArrayUtil.lastIndexOf(string, sWeak));
		Assert.assertEquals(3, ArrayUtil.lastIndexOf(string, wrString));
	}
	
	/**
	 * Tests all static lastIndexOfReference method calls.
	 */
	@Test
	public void testStaticLastIndexOfReference()
	{
		Integer i100 = new Integer(100);
		Vector<String> vValue = new Vector<String>(1);
		
		Object[] oRef1 = {"1.0", new BigDecimal(1), vValue, i100, vValue, i100, new Integer(100)};
		
		
		Assert.assertEquals(5, ArrayUtil.lastIndexOfReference(oRef1, i100));
		Assert.assertEquals(-1, ArrayUtil.lastIndexOfReference(oRef1, new Integer(100)));
		
		Assert.assertEquals(3, ArrayUtil.lastIndexOfReference(oRef1, i100, 4));
		Assert.assertEquals(-1, ArrayUtil.lastIndexOfReference(oRef1, i100, 2));
		Assert.assertEquals(5, ArrayUtil.lastIndexOfReference(oRef1, i100, -1));
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// weak references
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		String sWeak = "Weak search";
		WeakReference<String> wrString = new WeakReference<String>("Weak search");
		WeakReference<String> wrStringRef = new WeakReference<String>(sWeak);
		
		WeakReference[] weak = new WeakReference[] 
		                       {
							       wrString, 
							       wrStringRef, 
							       wrString, 
							       wrStringRef
							   };

		Assert.assertEquals(-1, ArrayUtil.lastIndexOfReference(weak, "Not found"));
		Assert.assertEquals(3, ArrayUtil.lastIndexOfReference(weak, sWeak));
		Assert.assertEquals(3, ArrayUtil.lastIndexOfReference(weak, wrStringRef));
		
		String[] string = new String[] {new String("Weak search"), sWeak, sWeak, new String("empty"), sWeak};

		Assert.assertEquals(-1, ArrayUtil.lastIndexOfReference(string, "Not found"));
		Assert.assertEquals(4, ArrayUtil.lastIndexOfReference(string, sWeak));
		Assert.assertEquals(4, ArrayUtil.lastIndexOfReference(string, wrStringRef));
	}

	/**
	 * Tests all static remove method calls.
	 */
	@Test
	public void testStaticRemove()
	{
		double[] dValue = {1D, 2D, 3D, 4.4D, 6D};
		float[]  fValue = {1F, 2F, 3F, 4.4F, 6F};
		int[]    iValue = {1, 2, 3, 4, 6};
		long[]   lValue = {1L, 2L, 3L, 4L, 6L};
		char[]   chValue = {'1', '2', '3', '4', '6'};

		Object[] oValue = {"1.0", "2.0", "3.0"};
	
		
		dValue = ArrayUtil.remove(dValue, 1);
		
		Assert.assertEquals(4, dValue.length);

		try
		{
			dValue = ArrayUtil.remove(dValue, -1);

			Assert.fail("Invalid index: -1");
		}
		catch (IndexOutOfBoundsException iobe)
		{
			//it works
		}
		
		
		fValue = ArrayUtil.remove(fValue, 2);
		
		Assert.assertEquals(4, fValue.length);

		try
		{
			fValue = ArrayUtil.remove(fValue, -1);

			Assert.fail("Invalid index: -1");
		}
		catch (IndexOutOfBoundsException iobe)
		{
			//it works
		}

		
		iValue = ArrayUtil.remove(iValue, 4);
		
		Assert.assertEquals(4, iValue.length);

		try
		{
			iValue = ArrayUtil.remove(iValue, -1);

			Assert.fail("Invalid index: -1");
		}
		catch (IndexOutOfBoundsException iobe)
		{
			//it works
		}
		
		
		lValue = ArrayUtil.remove(lValue, 0);
		
		Assert.assertEquals(4, lValue.length);
		
		try
		{
			lValue = ArrayUtil.remove(lValue, -1);

			Assert.fail("Invalid index: -1");
		}
		catch (IndexOutOfBoundsException iobe)
		{
			//it works
		}
		
		
        chValue = ArrayUtil.remove(chValue, 4);
        
        Assert.assertEquals(4, chValue.length);

        try
        {
            chValue = ArrayUtil.remove(chValue, -1);

            Assert.fail("Invalid index: -1");
        }
        catch (IndexOutOfBoundsException iobe)
        {
            //it works
        }

        
		oValue = ArrayUtil.remove(oValue, 1);
		
		Assert.assertEquals(2, oValue.length);
		
		try
		{
			oValue = ArrayUtil.remove(oValue, -1);

			Assert.fail("Invalid index: -1");
		}
		catch (IndexOutOfBoundsException iobe)
		{
			//it works
		}
	}
	
	/**
	 * Tests all static removeLast method calls.
	 */
	@Test
	public void testStaticRemoveLast()
	{
		double[] dValue = {1D, 2D, 3D, 4.4D, 6D};
		float[]  fValue = {1F, 2F, 3F, 4.4F, 6F};
		int[]    iValue = {1, 2, 3, 4, 6};
		long[]   lValue = {1L, 2L, 3L, 4L, 6L};
		char[]   chValue = {'1', '2', '3', '4', '6'};
		
		Object[] oValue = {"1.0", "2.0", "3.0"};
	
		
		dValue = ArrayUtil.removeLast(dValue);
		
		Assert.assertEquals(4, dValue.length);
		Assert.assertEquals(4.4D, dValue[3], 0);
		
		
		fValue = ArrayUtil.removeLast(fValue);
		
		Assert.assertEquals(4, fValue.length);
		Assert.assertEquals(4.4F, fValue[3], 0);

		
		iValue = ArrayUtil.removeLast(iValue);
		
		Assert.assertEquals(4, iValue.length);
		Assert.assertEquals(4, iValue[3]);

		
		lValue = ArrayUtil.removeLast(lValue);
		
		Assert.assertEquals(4, lValue.length);
		Assert.assertEquals(4, lValue[3]);

		
        chValue = ArrayUtil.removeLast(chValue);
        
        Assert.assertEquals(4, chValue.length);
        Assert.assertEquals('4', chValue[3]);
		

		oValue = ArrayUtil.removeLast(oValue);
		
		Assert.assertEquals(2, oValue.length);
		Assert.assertEquals("2.0", oValue[1]);
	}

	/**
	 * Tests all static removeRange method calls.
	 */
	@Test
	public void testStaticRemoveRange()
	{
		double[] dValue = {1D, 2D, 3D, 4.4D, 6D};
		float[]  fValue = {1F, 2F, 3F, 4.4F, 6F};
		int[]    iValue = {1, 2, 3, 4, 6};
		long[]   lValue = {1L, 2L, 3L, 4L, 6L};
		char[]   chValue = {'1', '2', '3', '4', '6'};
		
		Object[] oValue = {"1.0", "2.0", "3.0", "4.0", "5.0"};
	
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// double
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		dValue = ArrayUtil.removeRange(dValue, 1, 3);
		
		Assert.assertEquals(3, dValue.length);
		Assert.assertEquals(1D, dValue[0], 0);
		Assert.assertEquals(4.4D, dValue[1], 0);
		
		dValue = new double[] {1D, 2D, 3D, 4.4D, 6D};
		dValue = ArrayUtil.removeRange(dValue, -1, dValue.length);

		Assert.assertEquals(4, dValue.length);
		Assert.assertEquals(1D, dValue[0], 0);
		Assert.assertEquals(4.4D, dValue[3], 0);
		
		dValue = new double[] {1D, 2D, 3D, 4.4D, 6D};
		dValue = ArrayUtil.removeRange(dValue, 1, -1);

		Assert.assertEquals(1, dValue.length);
		Assert.assertEquals(1D, dValue[0], 0);
		
		dValue = new double[] {1D, 2D, 3D, 4.4D, 6D};
		try
		{
			dValue = ArrayUtil.removeRange(dValue, 100, -1);
			
			Assert.fail("Invalid range: [100, -1]");
		}
		catch (IndexOutOfBoundsException iobe)
		{
			//it works
		}
		
		dValue = ArrayUtil.removeRange(dValue, 0, 0);
		
		Assert.assertEquals(5, dValue.length);
		
		try
		{
			dValue = ArrayUtil.removeRange(dValue, 8, 7);
			
			Assert.fail("Invalid range: [8, 7]");
		}
		catch (IndexOutOfBoundsException iobe)
		{
			//it works
		}
		
		try
		{
			dValue = ArrayUtil.removeRange(dValue, 0, 20);
			
			Assert.fail("Invalid range: [0, 20]");
		}
		catch (IndexOutOfBoundsException iobe)
		{
			//it works
		}
		
		dValue = null;

		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// float
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		fValue = ArrayUtil.removeRange(fValue, 0, 1);
		
		Assert.assertEquals(4, fValue.length);
		Assert.assertEquals(2F, fValue[0], 0);
		
		fValue = new float[] {1F, 2F, 3F, 4.4F, 6F};
		fValue = ArrayUtil.removeRange(fValue, -1, fValue.length);

		Assert.assertEquals(4, fValue.length);
		Assert.assertEquals(1F, fValue[0], 0);
		Assert.assertEquals(4.4F, fValue[3], 0);
		
		fValue = new float[] {1F, 2F, 3F, 4.4F, 6F};
		fValue = ArrayUtil.removeRange(fValue, 1, -1);

		Assert.assertEquals(1, fValue.length);
		Assert.assertEquals(1F, fValue[0], 0);
		
		fValue = new float[] {1F, 2F, 3F, 4.4F, 6F};
		try
		{
			fValue = ArrayUtil.removeRange(fValue, 100, -1);
			
			Assert.fail("Invalid range: [100, -1]");
		}
		catch (IndexOutOfBoundsException iobe)
		{
			//it works
		}
		
		fValue = ArrayUtil.removeRange(fValue, 0, 0);
		
		Assert.assertEquals(5, fValue.length);
		
		try
		{
			fValue = ArrayUtil.removeRange(fValue, 8, 7);
			
			Assert.fail("Invalid range: [8, 7]");
		}
		catch (IndexOutOfBoundsException iobe)
		{
			//it works
		}
		
		try
		{
			fValue = ArrayUtil.removeRange(fValue, 0, 20);
			
			Assert.fail("Invalid range: [0, 20]");
		}
		catch (IndexOutOfBoundsException iobe)
		{
			//it works
		}
		
		fValue = null;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// int
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		iValue = ArrayUtil.removeRange(iValue, 0, 5);
		
		Assert.assertEquals(0, iValue.length);

		iValue = new int[] {1, 2, 3, 4, 6};
		iValue = ArrayUtil.removeRange(iValue, 0, 4);
		
		Assert.assertEquals(1, iValue.length);
		
		iValue = new int[] {1, 2, 3, 4, 6};
		iValue = ArrayUtil.removeRange(iValue, -1, iValue.length);

		Assert.assertEquals(4, iValue.length);
		Assert.assertEquals(1, iValue[0]);
		Assert.assertEquals(4, iValue[3]);
		
		iValue = new int[] {1, 2, 3, 4, 6};
		iValue = ArrayUtil.removeRange(iValue, 1, -1);

		Assert.assertEquals(1, iValue.length);
		Assert.assertEquals(1, iValue[0]);
		
		iValue = new int[] {1, 2, 3, 4, 6};
		try
		{
			iValue = ArrayUtil.removeRange(iValue, 100, -1);
			
			Assert.fail("Invalid range: [100, -1]");
		}
		catch (IndexOutOfBoundsException iobe)
		{
			//it works
		}
		
		iValue = ArrayUtil.removeRange(iValue, 0, 0);
		
		Assert.assertEquals(5, iValue.length);
		
		try
		{
			iValue = ArrayUtil.removeRange(iValue, 8, 7);
			
			Assert.fail("Invalid range: [8, 7]");
		}
		catch (IndexOutOfBoundsException iobe)
		{
			//it works
		}
		
		try
		{
			iValue = ArrayUtil.removeRange(iValue, 0, 20);
			
			Assert.fail("Invalid range: [0, 20]");
		}
		catch (IndexOutOfBoundsException iobe)
		{
			//it works
		}
		
		iValue = null;

		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// long
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		lValue = ArrayUtil.removeRange(lValue, 3, 5);
		
		Assert.assertEquals(3, lValue.length);
		Assert.assertEquals(3, lValue[2]);
		
		lValue = new long[] {1L, 2L, 3L, 4L, 6L};
		lValue = ArrayUtil.removeRange(lValue, -1, lValue.length);

		Assert.assertEquals(4, lValue.length);
		Assert.assertEquals(1, lValue[0]);
		Assert.assertEquals(4, lValue[3]);
		
		lValue = new long[] {1L, 2L, 3L, 4L, 6L};
		lValue = ArrayUtil.removeRange(lValue, 1, -1);

		Assert.assertEquals(1, lValue.length);
		Assert.assertEquals(1, lValue[0]);
		
		lValue = new long[] {1L, 2L, 3L, 4L, 6L};
		try
		{
			lValue = ArrayUtil.removeRange(lValue, 100, -1);
			
			Assert.fail("Invalid range: [100, -1]");
		}
		catch (IndexOutOfBoundsException iobe)
		{
			//it works
		}
		
		lValue = ArrayUtil.removeRange(lValue, 0, 0);
		
		Assert.assertEquals(5, lValue.length);
		
		try
		{
			lValue = ArrayUtil.removeRange(lValue, 8, 7);
			
			Assert.fail("Invalid range: [8, 7]");
		}
		catch (IndexOutOfBoundsException iobe)
		{
			//it works
		}
		
		try
		{
			lValue = ArrayUtil.removeRange(lValue, 0, 20);
			
			Assert.fail("Invalid range: [0, 20]");
		}
		catch (IndexOutOfBoundsException iobe)
		{
			//it works
		}
		
		lValue = null;

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // int
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        
        chValue = ArrayUtil.removeRange(chValue, 0, 5);
        
        Assert.assertEquals(0, chValue.length);

        chValue = new char[] {'1', '2', '3', '4', '6'};
        chValue = ArrayUtil.removeRange(chValue, 0, 4);
        
        Assert.assertEquals(1, chValue.length);
        
        chValue = new char[] {'1', '2', '3', '4', '6'};
        chValue = ArrayUtil.removeRange(chValue, -1, chValue.length);

        Assert.assertEquals(4, chValue.length);
        Assert.assertEquals('1', chValue[0]);
        Assert.assertEquals('4', chValue[3]);
        
        chValue = new char[] {'1', '2', '3', '4', '6'};
        chValue = ArrayUtil.removeRange(chValue, 1, -1);

        Assert.assertEquals(1, chValue.length);
        Assert.assertEquals('1', chValue[0]);
        
        chValue = new char[] {'1', '2', '3', '4', '6'};

        try
        {
            chValue = ArrayUtil.removeRange(chValue, 100, -1);
            
            Assert.fail("Invalid range: [100, -1]");
        }
        catch (IndexOutOfBoundsException iobe)
        {
            //it works
        }
        
        chValue = ArrayUtil.removeRange(chValue, 0, 0);
        
        Assert.assertEquals(5, chValue.length);
        
        try
        {
            chValue = ArrayUtil.removeRange(chValue, 8, 7);
            
            Assert.fail("Invalid range: [8, 7]");
        }
        catch (IndexOutOfBoundsException iobe)
        {
            //it works
        }
        
        try
        {
            chValue = ArrayUtil.removeRange(chValue, 0, 20);
            
            Assert.fail("Invalid range: [0, 20]");
        }
        catch (IndexOutOfBoundsException iobe)
        {
            //it works
        }
        
        chValue = null;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Object
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		oValue = ArrayUtil.removeRange(oValue, 1, 5);
		
		Assert.assertEquals(1, oValue.length);
		Assert.assertEquals("1.0", oValue[0]);
		
		oValue = new Object[] {"1.0", "2.0", "3.0", "4.0", "5.0"};
		oValue = ArrayUtil.removeRange(oValue, -1, oValue.length);

		Assert.assertEquals(4, oValue.length);
		Assert.assertEquals("1.0", oValue[0]);
		Assert.assertEquals("4.0", oValue[3]);
		
		oValue = new Object[] {"1.0", "2.0", "3.0", "4.0", "5.0"};
		oValue = ArrayUtil.removeRange(oValue, 1, -1);

		Assert.assertEquals(1, oValue.length);
		Assert.assertEquals("1.0", oValue[0]);
		
		oValue = new Object[] {"1.0", "2.0", "3.0", "4.0", "5.0"};
		try
		{
			oValue = ArrayUtil.removeRange(oValue, 100, -1);
			
			Assert.fail("Invalid range: [100, -1]");
		}
		catch (IndexOutOfBoundsException iobe)
		{
			//it works
		}
		
		oValue = ArrayUtil.removeRange(oValue, 0, 0);
		
		Assert.assertEquals(5, oValue.length);
		
		try
		{
			oValue = ArrayUtil.removeRange(oValue, 8, 7);
			
			Assert.fail("Invalid range: [8, 7]");
		}
		catch (IndexOutOfBoundsException iobe)
		{
			//it works
		}
		
		try
		{
			oValue = ArrayUtil.removeRange(oValue, 0, 20);
			
			Assert.fail("Invalid range: [0, 20]");
		}
		catch (IndexOutOfBoundsException iobe)
		{
			//it works
		}
		
		oValue = null;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// String
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		String[] sNames = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
		
		sNames = ArrayUtil.removeRange(sNames, 2, 6); 
	
		Assert.assertArrayEquals(new String[] {"0", "1", "6", "7", "8", "9"}, sNames);
		
		sNames = null;
	}

	/**
	 * Tests all static truncate method calls.
	 */
	@Test
	public void testStaticTruncate()
	{
		double[] dValue = {1D, 2D, 3D, 4.4D, 6D};
		float[]  fValue = {1F, 2F, 3F, 4.4F, 6F};
		int[]    iValue = {1, 2, 3, 4, 6};
		long[]   lValue = {1L, 2L, 3L, 4L, 6L};
		char[]   chValue = {'1', '2', '3', '4', '6'};
		
		Object[] oValue = {"1.0", "2.0"};
	
		
		dValue = ArrayUtil.truncate(dValue, 2);
		
		Assert.assertEquals(2, dValue.length);

		
		fValue = ArrayUtil.truncate(fValue, 2);
		
		Assert.assertEquals(2, fValue.length);
		
		
		iValue = ArrayUtil.truncate(iValue, 2);
		
		Assert.assertEquals(2, iValue.length);
		
		
		lValue = ArrayUtil.truncate(lValue, 2);
		
		Assert.assertEquals(2, lValue.length);
		

        chValue = ArrayUtil.truncate(chValue, 2);
        
        Assert.assertEquals(2, chValue.length);

        
        oValue = ArrayUtil.truncate(oValue, 2);
		
		Assert.assertEquals(2, oValue.length);
	}
	
	/**
	 * Tests all constructors.
	 */
	@Test
	public void testConstructor()
	{
        Object[] saElements = null;
        ArrayUtil au = new ArrayUtil(saElements);

        Assert.assertEquals(0, au.size());

        Collection<Object> col = null;
        
        au = new ArrayUtil(col);
        
        Assert.assertEquals(0, au.size());
        
		Vector<String> vValues = new Vector<String>();
		
		
		vValues.add("A");
		vValues.add("B");
		
		new ArrayUtil<Object>();
		new ArrayUtil<Object>(10);
		new ArrayUtil<Object>();
		new ArrayUtil<Object>(new Object[] {"1.0", "2.0"});
		new ArrayUtil<Object>(new Object[] {"1.0", "2.0"}, 1);
		
		new ArrayUtil<Object>(new Vector<Object>());
		new ArrayUtil<Object>(new ArrayUtil<Object>(new Object[] {"A"}));
		new ArrayUtil<Object>(vValues);
		
		new ArrayUtil<Object>(new Object[] {"1.0", "2.0"}, 0, 2);
		new ArrayUtil<Object>(new Object[] {"1.0", "2.0"}, 0, -1);
		
		new ArrayUtil<Object>(new Object[] {"1.0", "2.0"}, 1, 1);
		
		try
		{
			new ArrayUtil<Object>(new Object[] {"1.0", "2.0"}, 0, 3);
			
			Assert.fail("Invalid size!");
		}
		catch (IndexOutOfBoundsException iobe)
		{
			//it works!
		}
		
		try
		{
			new ArrayUtil<Object>(new Object[] {"1.0", "2.0"}, 1, 2);
			
			Assert.fail("Invalid offset/size combination!");
		}
		catch (IndexOutOfBoundsException iobe)
		{
			//it works!
		}
	}
	
	/** 
	 * Tests all add method calls.
	 */
	@Test
	public void testAdd()
	{
		ArrayUtil<String> au = new ArrayUtil<String>();
		
		
		Assert.assertTrue("Can't add \"A\"!", au.add("A"));
		Assert.assertTrue("Can't add \"B\"!", au.add("B"));
		
		Assert.assertEquals(2, au.size());
		Assert.assertEquals(au.get(0), "A");
		Assert.assertEquals(au.get(1), "B");
		
		au.add(0, "C");
		
		Assert.assertEquals(3, au.size());
		Assert.assertEquals(au.get(0), "C");
	}
	
	/** 
	 * Tests setSize.
	 */
	@Test
	public void testSetSize()
	{
		ArrayUtil<String> au = new ArrayUtil<String>();
		
		au.add("Hallo");

		au.setSize(100);
		Assert.assertTrue("Size is not 100!", au.size() == 100);

		au.setSize(500);
		Assert.assertTrue("Size is not 500!", au.size() == 500);
		
		au.setSize(100);
		Assert.assertTrue("Size is not 100!", au.size() == 100);

		au.setSize(1);
		Assert.assertTrue("Size is not 1!", au.size() == 1);

		Assert.assertEquals(au.get(0), "Hallo");
	}
	
	/**
	 * Tests all addAll method calls.
	 */
	@Test
	public void testAddAll()
	{
		ArrayUtil<String> au = new ArrayUtil<String>();
		
		ArrayList<String> alValues = new ArrayList<String>();
		Vector<String> vValues = new Vector<String>();
		
		String[] sNull = null;
		
		
		alValues.add("A");
		alValues.add("B");
		alValues.add("C");
		
		vValues.add("1");
		vValues.add("2");

		Assert.assertTrue("Can't add collection!", au.addAll(alValues));
		Assert.assertTrue("Can't add String[]!", au.addAll(new String[] {"AA", "BB"}));

		Assert.assertTrue("Can't add collection at index 0!", au.addAll(0, vValues));
		Assert.assertTrue("Can't add String[] at index 2!", au.addAll(2, new String[] {"11", "22"}));
		Assert.assertTrue("Can't add String[] at index 4!", au.addAll(4, new String[] {"O1"}, 0, -1));
		
		Assert.assertEquals("[1, 2, 11, 22, O1, A, B, C, AA, BB]", Arrays.toString(au.toArray()));
		
		Assert.assertFalse("Can add empty [] with invalid size!", au.addAll(0, new String[] {}, 0, 0));
		Assert.assertFalse("Can add null!", au.addAll(0, sNull));
	}
	
	/**
	 * Tests the clear method.
	 */
	@Test
	public void testClear()
	{
		ArrayUtil<String> au = new ArrayUtil<String>();

		
		au.add("A");
		au.add("B");
		au.add("C");
		
		Assert.assertEquals(3, au.size());
		
		au.clear();
		
		Assert.assertEquals(0, au.size());
	}
	
	/**
	 * Tests the contains method.
	 */
	@Test
	public void testContains()
	{
		ArrayUtil<String> au = new ArrayUtil<String>();

		
		au.add("A");
		au.add("B");
		au.add("C");
		
		Assert.assertTrue("\"C\" is not in an element of the list!", au.contains("C"));
		Assert.assertFalse("\"X\" is an element of the list!", au.contains("X"));	
		Assert.assertFalse("Integer(1) is an element of the list!", au.contains(new Integer(1)));	
	}
	
	/**
	 * Tests the equals method.
	 */
	@Test
	public void testEquals()
	{
		ArrayUtil<String> auEmpty = new ArrayUtil<String>();
		ArrayUtil<String> auString = new ArrayUtil<String>();
		ArrayUtil<String> auStringEqual = new ArrayUtil<String>();
		ArrayUtil<String> auStringNotEqual = new ArrayUtil<String>();
		ArrayUtil<Integer> auInteger = new ArrayUtil<Integer>();
		
		Vector<String> vEmpty = new Vector<String>();
		Vector<String> vStringEqual = new Vector<String>();
		Vector<String> vStringNotEqual = new Vector<String>();
		Vector<Integer> vInteger = new Vector<Integer>();
		
		
		auString.add("A");
		auString.add("B");
		auString.add("C");
		
		auStringEqual.add("A");
		auStringEqual.add("B");
		auStringEqual.add("C");

		auStringNotEqual.add("A");
		auStringNotEqual.add("B");
		auStringNotEqual.add("A");

		auInteger.add(new Integer(2));
		
		vStringEqual.addAll(auStringEqual);
		vStringNotEqual.addAll(auStringNotEqual);
		vInteger.addAll(auInteger);
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// General
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		Assert.assertFalse("null is equal!", auString.equals(null));
		Assert.assertFalse("Integer(1) is equal!", auString.equals(new Integer(1)));

		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// ArrayUtil
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		Assert.assertFalse("Integer[] is equal!", auString.equals(auInteger));
		Assert.assertFalse("Empty[] is equal!", auString.equals(auEmpty));
		Assert.assertFalse("A different [] is equal!", auString.equals(auStringNotEqual));
		
		Assert.assertTrue("same instance is not equal!", auString.equals(auString));
		Assert.assertTrue("two empty [] are not equal!", auEmpty.equals(new ArrayUtil<Integer>()));
		Assert.assertTrue("equal instance is not equal!", auString.equals(auStringEqual));
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Collection
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		Assert.assertFalse("Integer collection is equal!", auString.equals(vInteger));
		Assert.assertFalse("Empty collection is equal!", auString.equals(vEmpty));
		Assert.assertFalse("A different collection is equal!", auString.equals(vStringNotEqual));
		
		Assert.assertTrue("empty[] and empty collection are not equal!", auEmpty.equals(new Vector<Integer>()));
		Assert.assertTrue("equal collection is not equal!", auString.equals(vStringEqual));
	}
	
	/**
	 * Tests the get method.
	 */
	@Test
	public void testGet()
	{
		ArrayUtil<String> auList = new ArrayUtil<String>(new String[] {"EINS", "ZWEI"});
		
		Assert.assertEquals(2, auList.size());
		Assert.assertEquals("EINS", auList.get(0));
		Assert.assertEquals("ZWEI", auList.get(1));
		
		try
		{
			auList.get(3);
			
			Assert.fail("Invalid index 3");
		}
		catch (IndexOutOfBoundsException iobe)
		{
			//it works
		}
		
		ArrayUtil<String> auEmpty = new ArrayUtil<String>();
		
		try
		{
			auEmpty.get(1);
			
			Assert.fail("Invalid index 1 with size 0");
		}
		catch (IndexOutOfBoundsException iobe)
		{
			//it works
		}
	}
	
	/**
	 * Tests the hashCode method.
	 */
	@Test
	public void testHashCode()
	{
		ArrayUtil<Object> auList = new ArrayUtil<Object>(new Object[] {"EINS", "ZWEI", new Integer(2), new BigDecimal(5)});

		//Berechnung mit wenigen Elementen
		auList.hashCode();
		
		auList.addAll(auList);
		auList.addAll(auList);
		auList.addAll(auList);
		auList.addAll(auList);
		auList.addAll(auList);
		
		//Berechnung mit vielen Elementen
		auList.hashCode();
	}
	
	/**
	 * Tests all indexOf methods.
	 */
	@Test
	public void testIndexOf()
	{
		ArrayUtil<Object> auList = new ArrayUtil<Object>(new Object[] {"EINS", "ZWEI", new Integer(2), new BigDecimal(5)});

		
		Assert.assertEquals(2, auList.indexOf(new Integer(2)));
		Assert.assertEquals(-1, auList.indexOf(new Integer(2), 3));
		
		Assert.assertEquals(3, auList.indexOf(new BigDecimal(5), 2));
		Assert.assertEquals(-1, auList.indexOf(new BigDecimal(5), -1));
		Assert.assertEquals(-1, auList.indexOf(new BigDecimal(5), 30));
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// weak references
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		ArrayUtil<WeakReference<String>> auWeak = new ArrayUtil<WeakReference<String>>();
		
		String sWeak = "Weak search";
		WeakReference<String> wrString = new WeakReference<String>("Weak search");
		
		
		auWeak.add(wrString);
		
		Assert.assertEquals(-1, auWeak.indexOf("Not found"));
		Assert.assertEquals(0, auWeak.indexOf(sWeak));
		Assert.assertEquals(0, auWeak.indexOf(wrString));
		
		ArrayUtil<String> auString = new ArrayUtil<String>();
		
		auString.add(sWeak);
		
		Assert.assertEquals(-1, auString.indexOf("Not found"));
		Assert.assertEquals(0, auString.indexOf(sWeak));
		Assert.assertEquals(0, auString.indexOf(wrString));
	}
	
	/**
	 * Tests all indexOfReference methods.
	 */
	@Test
	public void testIndexOfReference()
	{
		Integer i2 = new Integer(2);
		BigDecimal bd5 = new BigDecimal(5);
		
		ArrayUtil<Object> auList = new ArrayUtil<Object>(new Object[] {"EINS", i2, "ZWEI", new Integer(2), new BigDecimal(5), bd5});

		
		Assert.assertEquals(1, auList.indexOfReference(i2));
		Assert.assertEquals(-1, auList.indexOfReference(new Integer(2)));

		Assert.assertEquals(5, auList.indexOfReference(bd5), 2);
		Assert.assertEquals(-1, auList.indexOfReference(new BigDecimal(5), 0));
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// weak references
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		ArrayUtil<WeakReference<String>> auWeak = new ArrayUtil<WeakReference<String>>();
		
		String sWeak = new String("Weak search");
		WeakReference<String> wrString = new WeakReference<String>(new String("Weak search"));
		WeakReference<String> wrStringRef = new WeakReference<String>(sWeak);
		
		
		auWeak.add(wrString);
		auWeak.add(wrStringRef);

		Assert.assertEquals(-1, auWeak.indexOfReference("Not found"));
		Assert.assertEquals(1, auWeak.indexOfReference(sWeak));
		Assert.assertEquals(1, auWeak.indexOfReference(wrStringRef));
		
		ArrayUtil<String> auString = new ArrayUtil<String>();
		
		auString.add(new String("Weak search"));
		auString.add(sWeak);
		
		Assert.assertEquals(-1, auString.indexOfReference("Not found"));
		Assert.assertEquals(1, auString.indexOfReference(sWeak));
		Assert.assertEquals(1, auString.indexOfReference(wrStringRef));
	}

	/**
	 * Tests the isEmpty method.
	 */
	@Test
	public void testIsEmpty()
	{
		ArrayUtil<Object> auEmpty = new ArrayUtil<Object>(new Object[] {"EINS", "ZWEI", new Integer(2), new BigDecimal(5)});
		
		
		Assert.assertFalse("[] is empty!", auEmpty.isEmpty());
		
		auEmpty.clear();
		
		Assert.assertTrue("[] is not empty!", auEmpty.isEmpty());
	}	
	
	/**
	 * Tests all lastIndexOf methods.
	 */
	@Test
	public void testLastIndexOf()
	{
		ArrayUtil<Object> auList = new ArrayUtil<Object>(new Object[] {new Integer(2), "EINS", new BigDecimal(5), 
				                                                       "ZWEI", new Integer(2), new BigDecimal(5), 
				                                                       new Integer(2), new BigDecimal(5), 
				                                                       new BigDecimal(5)});

		
		Assert.assertEquals(6, auList.lastIndexOf(new Integer(2)));
		Assert.assertEquals(-1, auList.lastIndexOf(new Integer(1)));

		Assert.assertEquals(6, auList.lastIndexOf(new Integer(2), -1));
		Assert.assertEquals(6, auList.lastIndexOf(new Integer(2), 7));
		Assert.assertEquals(-1, auList.lastIndexOf(new BigDecimal(5), 1));
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// weak references
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		ArrayUtil<WeakReference<String>> auWeak = new ArrayUtil<WeakReference<String>>();
		
		String sWeak = "Weak search";
		WeakReference<String> wrString = new WeakReference<String>("Weak search");
		WeakReference<String> wrString2 = new WeakReference<String>("Weak search");
		
		
		auWeak.add(new WeakReference<String>("First"));
		auWeak.add(wrString);
		auWeak.add(new WeakReference<String>("Second"));
		auWeak.add(wrString2);
		auWeak.add(new WeakReference<String>("Fourth"));
		
		Assert.assertEquals(-1, auWeak.lastIndexOf("Not found"));
		Assert.assertEquals(3, auWeak.lastIndexOf(sWeak));
		Assert.assertEquals(3, auWeak.lastIndexOf(wrString));
		
		ArrayUtil<String> auString = new ArrayUtil<String>();
		
		auString.add("First");
		auString.add(sWeak);
		auString.add("Second");
		auString.add(sWeak);
		auString.add("Fourth");
		
		Assert.assertEquals(-1, auString.lastIndexOf("Not found"));
		Assert.assertEquals(3, auString.lastIndexOf(sWeak));
		Assert.assertEquals(3, auString.lastIndexOf(wrString));
	}
	
	/**
	 * Tests all lastIndexOfReference methods.
	 */
	@Test
	public void testLastIndexOfReference()
	{
		Integer i2 = new Integer(2);
		BigDecimal bd5 = new BigDecimal(5);
		
		ArrayUtil<Object> auList = new ArrayUtil<Object>(new Object[] {i2, "EINS", bd5, new BigDecimal(5), 
				                                                       "ZWEI", new Integer(2), i2, new BigDecimal(5), 
				                                                       new Integer(2), new BigDecimal(5), 
				                                                       bd5, new BigDecimal(5)});

		
		Assert.assertEquals(6, auList.lastIndexOfReference(i2));
		Assert.assertEquals(-1, auList.lastIndexOfReference(new Integer(2)));

		Assert.assertEquals(6, auList.lastIndexOfReference(i2, -1));
		Assert.assertEquals(-1, auList.lastIndexOfReference(new Integer(2), 7));
		Assert.assertEquals(-1, auList.lastIndexOfReference(new BigDecimal(5), 1));
		Assert.assertEquals(0, auList.lastIndexOfReference(i2, 2));
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// weak references
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		ArrayUtil<WeakReference<String>> auWeak = new ArrayUtil<WeakReference<String>>();
		
		String sWeak = new String("Weak search");
		WeakReference<String> wrString = new WeakReference<String>(new String("Weak search"));
		WeakReference<String> wrStringRef = new WeakReference<String>(sWeak);
		
		
		auWeak.add(wrString);
		auWeak.add(wrStringRef);
		auWeak.add(wrString);
		auWeak.add(wrStringRef);

		Assert.assertEquals(-1, auWeak.lastIndexOfReference("Not found"));
		Assert.assertEquals(3, auWeak.lastIndexOfReference(sWeak));
		Assert.assertEquals(3, auWeak.lastIndexOfReference(wrStringRef));
		
		ArrayUtil<String> auString = new ArrayUtil<String>();
		
		auString.add(new String("Weak search"));
		auString.add(sWeak);
		auString.add(sWeak);
		auString.add(new String("empty"));
		auString.add(sWeak);
		
		Assert.assertEquals(-1, auString.lastIndexOfReference("Not found"));
		Assert.assertEquals(4, auString.lastIndexOfReference(sWeak));
		Assert.assertEquals(4, auString.lastIndexOfReference(wrStringRef));
	}

	/**
	 * Tests all remove method calls.
	 */
	@Test
	public void testRemove()
	{
		ArrayUtil<String> auRemove = new ArrayUtil<String>();

		
		//Entfernen per Index
		
		auRemove.add("1");
		auRemove.add("2");
		auRemove.add("3");
		auRemove.add("4");
		auRemove.add("5");
		auRemove.add("6");
		auRemove.add("7");
		auRemove.add("8");
		auRemove.add("9");
		
		auRemove.remove(3);
		Assert.assertEquals("[1, 2, 3, 5, 6, 7, 8, 9]", auRemove.toString());

		auRemove.remove(5);
		Assert.assertEquals("[1, 2, 3, 5, 6, 8, 9]", auRemove.toString());
		
		//Entfernen per Index

		auRemove = new ArrayUtil<String>();
		auRemove.add("EINS");
		
		Assert.assertEquals(1, auRemove.size());
		Assert.assertEquals("EINS", auRemove.get(0));
		
		Assert.assertEquals("Can't remove first element", "EINS", auRemove.remove(0));
		Assert.assertEquals(0, auRemove.size());
		
		
		//Entfernen per Object
		auRemove = new ArrayUtil<String>();
		
		auRemove.add("EINS");
		
		Assert.assertEquals(1, auRemove.size());
		Assert.assertEquals("EINS", auRemove.get(0));
		
		Assert.assertTrue("Can't remove first element!", auRemove.remove("EINS"));
		Assert.assertEquals(0, auRemove.size());
		
		//Mehr Elemente hinzufügen als initial size und danach removen
		auRemove = new ArrayUtil<String>(5);
		
		auRemove.add("EINS");
		auRemove.add("ZWEI");
		auRemove.add("DREI");
		auRemove.add("VIER");
		auRemove.add("FÜNF");
		auRemove.add("SECHS");
		
		Assert.assertEquals(6, auRemove.size());
		Assert.assertTrue("Can't remove fifth element!", auRemove.remove("FÜNF"));
		Assert.assertEquals(5, auRemove.size());
		
		Assert.assertFalse("Removed an unknown element!!", auRemove.remove("EMPTY"));
	}
	
	/**
	 * Tests the removeLast method.
	 */
	@Test
	public void testRemoveLast()
	{
		ArrayUtil<String> auRemove = new ArrayUtil<String>(new String[] {"A", "B", "C", "D"});

		
		Assert.assertEquals(4, auRemove.size());
		auRemove.removeLast();
		Assert.assertEquals(3, auRemove.size());
		Assert.assertEquals("C", auRemove.get(2));
	}
	
	/**
	 * Tests the removeRange method.
	 */
	@Test
	public void testRemoveRange()
	{
		ArrayUtil<String> auRemove = new ArrayUtil<String>(new String[] {"A", "B", "C", "D", "E", "F"});

		
		auRemove.removeRange(2, 4);
		
		Assert.assertEquals("[A, B, E, F]", Arrays.toString(auRemove.toArray()));
		
		
		auRemove = new ArrayUtil<String>(new String[] {"A", "B", "C", "D", "E", "F"});
		auRemove.removeRange(-1, auRemove.size());

		Assert.assertEquals(5, auRemove.size());
		Assert.assertEquals("A", auRemove.get(0));
		Assert.assertEquals("E", auRemove.get(4));
		
		auRemove = new ArrayUtil<String>(new String[] {"A", "B", "C", "D", "E", "F"});
		auRemove.removeRange(1, -1);

		Assert.assertEquals(1, auRemove.size());
		Assert.assertEquals("A", auRemove.get(0));
		
		auRemove = new ArrayUtil<String>(new String[] {"A", "B", "C", "D", "E", "F"});
		try
		{
			auRemove.removeRange(100, -1);
			
			Assert.fail("Invalid range: [100, -1]");
		}
		catch (IndexOutOfBoundsException iobe)
		{
			//it works
		}
		
		auRemove.removeRange(0, 0);
		
		Assert.assertEquals(6, auRemove.size());
		
		try
		{
			auRemove.removeRange(8, 7);
			
			Assert.fail("Invalid range: [8, 7]");
		}
		catch (IndexOutOfBoundsException iobe)
		{
			//it works
		}
		
		try
		{
			auRemove.removeRange(0, 20);
			
			Assert.fail("Invalid range: [0, 20]");
		}
		catch (IndexOutOfBoundsException iobe)
		{
			//it works
		}
		
		auRemove = new ArrayUtil<String>(new String[] {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L"});
		auRemove.addAll(auRemove);
		auRemove.addAll(auRemove);
		auRemove.addAll(auRemove);

		//code-coverage for rarely used code
		auRemove.removeRange(10, 51);
		
		auRemove.addAll(auRemove);
		auRemove.addAll(auRemove);
		auRemove.addAll(auRemove);
		auRemove.addAll(auRemove);
		auRemove.addAll(auRemove);
		
        //code-coverage for rarely used code
		auRemove.removeRange(50, 100);
	}

	/**
	 * Tests {@link ArrayUtil#removeAll(Object[])} and {@link ArrayUtil#removeAll(Object[], Object[])}.
	 */
	@Test
	public void testRemoveAll()
	{
		ArrayUtil<String> auEntries = new ArrayUtil<String>(new String[] {"A", "B", "C", "D", "E", "F"});
		
		Assert.assertTrue(auEntries.removeAll(new String[] {"A", "C", "E", "X"}));
	
		Assert.assertEquals("[B, D, F]", auEntries.toString());
		
		Assert.assertFalse(auEntries.removeAll(new String[] {"Y"}));
		
		String[] sEntries = new String[] {"A", "B", "C", "D", "E", "F"};
		
		String[] sResult = ArrayUtil.removeAll(sEntries, new String[] {"A", "C", "E", "X"});
		
		Assert.assertArrayEquals(new String[] {"B", "D", "F"}, sResult);
		
		Assert.assertNull(ArrayUtil.removeAll(null, new String[] {"A"}));
		Assert.assertArrayEquals(sEntries, ArrayUtil.removeAll(sEntries, null));
		
		sResult = ArrayUtil.removeAll(sEntries, new String[] {"A", "C", "E"});
		
		sResult[0] = ".";
		
		//ensure that the result is not a reference to the original array!
		Assert.assertEquals("A", sEntries[0]);
		Assert.assertEquals(".", sResult[0]);
	}
	
	/**
	 * Tests the set method.
	 */
	@Test
	public void testSet()
	{
		ArrayUtil<String> auSet = new ArrayUtil<String>(new String[] {"A", "B", "C", "D", "E", "F"});
		
		
		Assert.assertEquals("B", auSet.get(1));
		
		Assert.assertEquals("B", auSet.set(1, "HABEN"));
		
		Assert.assertEquals(6, auSet.size());
		Assert.assertEquals("A", auSet.get(0));
		Assert.assertEquals("HABEN", auSet.get(1));
		Assert.assertEquals("C", auSet.get(2));
	}
	
	/**
	 * Tests all toArray method calls.
	 */
	@Test
	public void testToArray()
	{
		ArrayUtil<Object> auArray = new ArrayUtil<Object>(new Object[] {"A", "B", Integer.valueOf(1), BigDecimal.valueOf(2)});
		
		Object[] oArray;
		
		
		oArray = (Object[])auArray.toArray();
		
		Assert.assertEquals(4, oArray.length);
		Assert.assertEquals("A", oArray[0]);
		Assert.assertEquals(new Integer(1), oArray[2]);
		
		try
		{
			oArray = null;
			auArray.toArray(oArray);
			
			Assert.fail("copy [] into null object!");
		}
		catch (NullPointerException npe)
		{
			//it works
		}
		
		//corret size
		oArray = new Object[auArray.size()];
		auArray.toArray(oArray);
		
		Assert.assertEquals(4, oArray.length);
		Assert.assertEquals("A", oArray[0]);
		Assert.assertEquals(new Integer(1), oArray[2]);
		
		//array too small
		oArray = new Object[1];
		oArray = auArray.toArray(oArray);
		
		Assert.assertEquals(4, oArray.length);
		Assert.assertEquals("A", oArray[0]);
		
		//array too long
		oArray = new Object[oArray.length * 2];
		oArray = auArray.toArray(oArray);
		
		Assert.assertEquals(8, oArray.length);
		Assert.assertEquals("A", oArray[0]);
		Assert.assertEquals(new Integer(1), oArray[2]);
		Assert.assertEquals(null, oArray[5]);	//Ende erkennen
		
		//WITH index and length
		
		oArray = new Object[0];

		oArray = auArray.toArray(1, 1);
		
		Assert.assertArrayEquals(new Object[] {"B"}, oArray);
		
		try
		{
			oArray = auArray.toArray(3, 2);
			
			Assert.fail("Invalid index + length used!");
		}
		catch (IllegalArgumentException iae)
		{
			Assert.assertEquals("Source array has 4 element(s). Start index 3 and length 2 are not possible!", iae.getMessage());
		}
		
		oArray = auArray.toArray(1, 3);
		
		Assert.assertArrayEquals(new Object[] {"B", Integer.valueOf(1), BigDecimal.valueOf(2)}, oArray);
	}
	
	/**
	 * Tests the truncate method.
	 */
	@Test
	public void testTruncate()
	{
		ArrayUtil<String> au = new ArrayUtil<String>(new String[] {"A", "B", "C"});

		
		au.truncate(2);
		
		Assert.assertEquals(2, au.size());
		Assert.assertEquals("A", au.get(0));
		Assert.assertEquals("B", au.get(1));
	}
	
	/**
	 * Tests the enumeration method.
	 */
	@Test
	public void testEnumeration()
	{
		String[] sValues = {"1", "2", "3", "4", "5"};
		
		ArrayUtil<String> au = new ArrayUtil<String>(sValues);
		
		int i = 0;
		
		
		for (Enumeration<String> en = au.enumeration(); en.hasMoreElements();)
		{
			Assert.assertEquals(en.nextElement(), sValues[i++]);
		}
		
		Assert.assertEquals(sValues.length, i);
	}

	/**
	 * Tests the containsAll method.
	 */
	@Test
	public void testContainsAll()
	{
		String[] sValues = {"1", "2", "3", "4", "5"};
		
		ArrayUtil<String> au = new ArrayUtil<String>(sValues);

		Assert.assertTrue("Not all elements are in the source array!", au.containsAll(new String[] {"2", "2", "3"}));
		Assert.assertFalse("The source array doesn't contain some elements!", au.containsAll(new String[] {"1", "6"}));
		
		Assert.assertTrue("Null check failed!", au.containsAll((String[])null));
		Assert.assertTrue("Empty check failed!", au.containsAll(new String[] {}));
		
		Assert.assertTrue(ArrayUtil.containsAll(new String[] {null, "A", "B"}, new String[] {"A", null, "B"}));
		Assert.assertFalse(ArrayUtil.containsAll(new String[] {null, "B"}, new String[] {null, "A", "B"}));
	}
	
	/**
	 * Tests the static containsAll method.
	 */
	@Test
	public void testStaticContainsAll()
	{
		String[] sValues = {"1", "2", "3", "4", "5"};

		Assert.assertTrue("Not all elements are in the source array!", ArrayUtil.containsAll(sValues, new String[] {"2", "2", "3"}));
		Assert.assertFalse("The source array doesn't contain some elements!", ArrayUtil.containsAll(sValues, new String[] {"1", "6"}));

		Assert.assertTrue("Null check failed!", ArrayUtil.containsAll(sValues, null));
		Assert.assertTrue("Empty check failed!", ArrayUtil.containsAll(sValues, new String[] {}));
		Assert.assertFalse("Null source check failed!", ArrayUtil.containsAll(null, null));
	}
	
    /**
     * Tests {@link ArrayUtil#merge(Object[], Object[])}.
     */
    @Test
    public void testStaticMerge()
    {
        // boolean
        
        boolean[] baFirst = new boolean[] {true, false, false};
        boolean[] baSecond = new boolean[] {true, true};
        
        boolean[] baMerge = ArrayUtil.merge(baFirst, baSecond);

        Assert.assertEquals(2, baMerge.length);
        Assert.assertTrue(baMerge[0]);
        Assert.assertFalse(baMerge[1]);
        
        baFirst = new boolean[] {true, true, true};
        baSecond = null;
        
        baMerge = ArrayUtil.merge(baFirst, baSecond);

        Assert.assertEquals(1, baMerge.length);
        Assert.assertTrue(baMerge[0]);

        baFirst = new boolean[] {false, false, false};
        baSecond = null;
        
        baMerge = ArrayUtil.merge(baFirst, baSecond);

        Assert.assertEquals(1, baMerge.length);
        Assert.assertFalse(baMerge[0]);

        baFirst = new boolean[] {false, false, false, true};
        baSecond = null;
        
        baMerge = ArrayUtil.merge(baFirst, baSecond);

        Assert.assertEquals(2, baMerge.length);
        Assert.assertFalse(baMerge[0]);
        Assert.assertTrue(baMerge[1]);
        
        baFirst = null;
        baSecond = new boolean[] {false, false, false};
        
        baMerge = ArrayUtil.merge(baFirst, baSecond);

        Assert.assertEquals(1, baMerge.length);
        Assert.assertFalse(baMerge[0]);

        baFirst = null;
        baSecond = null;
        
        baMerge = ArrayUtil.merge(baFirst, baSecond);
               
        Assert.assertNull(baMerge);
        
        //float
        
        float[] faFirst = new float[] {1f, 0.35f, 23.12f};
        float[] faSecond = new float[] {0.1f, 0.35f};
        
        float[] faMerge = ArrayUtil.merge(faFirst, faSecond);

        Assert.assertEquals(4, faMerge.length);
        
        faFirst = new float[] {0.4f, 0.6f, 0.4f};
        faSecond = null;
        
        faMerge = ArrayUtil.merge(faFirst, faSecond);

        Assert.assertEquals(2, faMerge.length);
        Assert.assertEquals(0.4f, faMerge[0], 0);
        Assert.assertEquals(0.6f, faMerge[1], 0);

        faFirst = null;
        faSecond = new float[] {0.4f, 0.6f, 0.4f};
        
        faMerge = ArrayUtil.merge(faFirst, faSecond);

        Assert.assertEquals(2, faMerge.length);
        Assert.assertEquals(0.4f, faMerge[0], 0);
        Assert.assertEquals(0.6f, faMerge[1], 0);
        
        faFirst = null;
        faSecond = null;
        
        faMerge = ArrayUtil.merge(faFirst, faSecond);

        Assert.assertNull(faMerge);
        
        //double
        
        double[] daFirst = new double[] {1, 0.35, 23.12};
        double[] daSecond = new double[] {0.1, 0.35};
        
        double[] daMerge = ArrayUtil.merge(daFirst, daSecond);

        Assert.assertEquals(4, daMerge.length);
        
        daFirst = new double[] {0.4, 0.6, 0.4};
        daSecond = null;
        
        daMerge = ArrayUtil.merge(daFirst, daSecond);

        Assert.assertEquals(2, daMerge.length);
        Assert.assertEquals(0.4, daMerge[0], 0);
        Assert.assertEquals(0.6, daMerge[1], 0);

        daFirst = null;
        daSecond = new double[] {0.4, 0.6, 0.4};
        
        daMerge = ArrayUtil.merge(daFirst, daSecond);

        Assert.assertEquals(2, daMerge.length);
        Assert.assertEquals(0.4, daMerge[0], 0);
        Assert.assertEquals(0.6, daMerge[1], 0);
        
        daFirst = null;
        daSecond = null;
        
        daMerge = ArrayUtil.merge(daFirst, daSecond);

        Assert.assertNull(daMerge);        

        //int
        
        int[] iaFirst = new int[] {1, 35, 23};
        int[] iaSecond = new int[] {1, 45};
        
        int[] iaMerge = ArrayUtil.merge(iaFirst, iaSecond);

        Assert.assertEquals(4, iaMerge.length);
        
        iaFirst = new int[] {4, 6, 4};
        iaSecond = null;
        
        iaMerge = ArrayUtil.merge(iaFirst, iaSecond);

        Assert.assertEquals(2, iaMerge.length);
        Assert.assertEquals(4, iaMerge[0], 0);
        Assert.assertEquals(6, iaMerge[1], 0);

        iaFirst = null;
        iaSecond = new int[] {4, 6, 4};
        
        iaMerge = ArrayUtil.merge(iaFirst, iaSecond);

        Assert.assertEquals(2, iaMerge.length);
        Assert.assertEquals(4, iaMerge[0], 0);
        Assert.assertEquals(6, iaMerge[1], 0);
        
        iaFirst = null;
        iaSecond = null;
        
        iaMerge = ArrayUtil.merge(iaFirst, iaSecond);

        Assert.assertNull(iaMerge);        

        //long
        
        long[] laFirst = new long[] {1, 35, 23};
        long[] laSecond = new long[] {1, 45};
        
        long[] laMerge = ArrayUtil.merge(laFirst, laSecond);

        Assert.assertEquals(4, laMerge.length);
        
        laFirst = new long[] {4, 6, 4};
        laSecond = null;
        
        laMerge = ArrayUtil.merge(laFirst, laSecond);

        Assert.assertEquals(2, laMerge.length);
        Assert.assertEquals(4, laMerge[0], 0);
        Assert.assertEquals(6, laMerge[1], 0);

        laFirst = null;
        laSecond = new long[] {4, 6, 4};
        
        laMerge = ArrayUtil.merge(laFirst, laSecond);

        Assert.assertEquals(2, laMerge.length);
        Assert.assertEquals(4, laMerge[0], 0);
        Assert.assertEquals(6, laMerge[1], 0);
        
        laFirst = null;
        laSecond = null;
        
        laMerge = ArrayUtil.merge(laFirst, laSecond);

        Assert.assertNull(laMerge);        
        
        //char
        
        char[] chaFirst = new char[] {'1', 'X', 'Y'};
        char[] chaSecond = new char[] {'1', 'Z'};
        
        char[] chaMerge = ArrayUtil.merge(chaFirst, chaSecond);

        Assert.assertEquals(4, chaMerge.length);
        
        chaFirst = new char[] {'4', '6', '4'};
        chaSecond = null;
        
        chaMerge = ArrayUtil.merge(chaFirst, chaSecond);

        Assert.assertEquals(2, chaMerge.length);
        Assert.assertEquals('4', chaMerge[0], 0);
        Assert.assertEquals('6', chaMerge[1], 0);

        chaFirst = null;
        chaSecond = new char[] {'4', '6', '4'};
        
        chaMerge = ArrayUtil.merge(chaFirst, chaSecond);

        Assert.assertEquals(2, chaMerge.length);
        Assert.assertEquals('4', chaMerge[0], 0);
        Assert.assertEquals('6', chaMerge[1], 0);
        
        chaFirst = null;
        chaSecond = null;
        
        chaMerge = ArrayUtil.merge(chaFirst, chaSecond);

        Assert.assertNull(chaMerge);        
        
        // String
        
        String[] saFirst = {"A", "B", "C"};
        String[] saSecond = {"D", "E", "F"};

        String[] saMerge = ArrayUtil.merge(saFirst, saSecond);      
        
        Assert.assertEquals(6, saMerge.length);
        Assert.assertArrayEquals(new String[] {"A", "B", "C", "D", "E", "F"}, saMerge);
        
        saFirst = new String[] {"A", "B", "C"};
        saSecond = new String[] {"A", "E", "B"};

        saMerge = ArrayUtil.merge(saFirst, saSecond);

        Assert.assertEquals(4, saMerge.length);
        Assert.assertArrayEquals(new String[] {"A", "B", "C", "E"}, saMerge);
        
        saFirst = new String[] {"A", "B", "B", "C"};
        saSecond = new String[] {"C", "B", "A"};
        
        saMerge = ArrayUtil.merge(saFirst, saSecond);
        
        Assert.assertEquals(3, saMerge.length);
        Assert.assertArrayEquals(new String[] {"A", "B", "C"}, saMerge);
        
        saFirst = null;
        saSecond = new String[] {"A", "B", "C"};
        
        saMerge = ArrayUtil.merge(saFirst, saSecond);
        Assert.assertEquals(3, saMerge.length);
        Assert.assertArrayEquals(new String[] {"A", "B", "C"}, saMerge);
        
        saFirst = new String[] {"A", "A", "C"};
        saSecond = null;
        
        saMerge = ArrayUtil.merge(saFirst, saSecond);
        Assert.assertEquals(2, saMerge.length);
        Assert.assertArrayEquals(new String[] {"A", "C"}, saMerge);
        
        saFirst = null;
        saSecond = null;
        
        saMerge = ArrayUtil.merge(saFirst, saSecond);
        Assert.assertNull(saMerge);
    }	

	/**
	 * Tests the clone method.
	 */
	@Test
	public void testClone()
	{
		ArrayUtil<String> au = new ArrayUtil<String>();

		au.clone();		
	}

	/**
	 * Tests {@link ArrayUtil#first()} method.
	 */
	@Test
	public void testFirst()
	{
		ArrayUtil<String> auValues = new ArrayUtil<String>();
		
		try
		{
			auValues.first();
			
			Assert.fail("First element found");
		}
		catch (NoSuchElementException e)
		{
			//works
		}

		auValues.add("First");
		auValues.remove("First");

		try
		{
			auValues.first();
			
			Assert.fail("First element found");
		}
		catch (NoSuchElementException e)
		{
			//works
		}

		auValues.add("First");
		auValues.clear();

		try
		{
			auValues.first();
			
			Assert.fail("First element found");
		}
		catch (NoSuchElementException e)
		{
			//works
		}

		auValues.add("First");
		auValues.add("Second");
		auValues.add("Third");
		auValues.add("Fourth");
		
		Assert.assertEquals("First", auValues.first());
	}

	/**
	 * Tests {@link ArrayUtil#last()} method.
	 */
	@Test
	public void testLast()
	{
		ArrayUtil<String> auValues = new ArrayUtil<String>();
		
		try
		{
			auValues.last();
			
			Assert.fail("Last element found");
		}
		catch (NoSuchElementException e)
		{
			//works
		}

		auValues.add("One");
		auValues.remove("One");

		try
		{
			auValues.last();
			
			Assert.fail("Last element found");
		}
		catch (NoSuchElementException e)
		{
			//works
		}

		auValues.add("One");
		auValues.clear();

		try
		{
			auValues.last();
			
			Assert.fail("Last element found");
		}
		catch (NoSuchElementException e)
		{
			//works
		}

		auValues.add("One");
		auValues.add("Two");
		auValues.add("Three");
		
		Assert.assertEquals("Three", auValues.last());
	}

	/**
	 * Tests {@link ArrayUtil#merge(Object[], Object[])}.
	 */
	@Test
	public void testMerge()
	{
	    ArrayUtil<String> auData = new ArrayUtil<String>();
	    auData.addAll(new String[] {"A", "B", "C"});
	    auData.merge(new String[] {"D", "E", "F"});

	    String[] saMerge = auData.toArray(new String[auData.size()]);	    
	    
	    Assert.assertEquals(6, saMerge.length);
	    Assert.assertArrayEquals(new String[] {"A", "B", "C", "D", "E", "F"}, saMerge);

        auData = new ArrayUtil<String>();
        auData.addAll(new String[] {"A", "B", "C"});
        auData.merge(new String[] {"A", "E", "B"});

        saMerge = auData.toArray(new String[auData.size()]);       
        
        Assert.assertEquals(4, saMerge.length);
        Assert.assertArrayEquals(new String[] {"A", "B", "C", "E"}, saMerge);
	    
        auData = new ArrayUtil<String>();
        auData.addAll(new String[] {"A", "B", "B", "C"});
        auData.merge(new String[] {"C", "B", "A"});

        saMerge = auData.toArray(new String[auData.size()]);       
        
        Assert.assertEquals(3, saMerge.length);
        Assert.assertArrayEquals(new String[] {"A", "B", "C"}, saMerge);
        
        auData = new ArrayUtil<String>();
        auData.addAll(new String[] {"A", "A", "C"});
        auData.merge(null);

        saMerge = auData.toArray(new String[auData.size()]);       
        
        Assert.assertEquals(2, saMerge.length);
        Assert.assertArrayEquals(new String[] {"A", "C"}, saMerge);
	}

	/**
	 * Tests {@link ArrayUtil#intersect(Object[], Object[])}.
	 */
	@Test
	public void testIntersect()
	{
	    String[] saSource = new String[] {"A", "B", "C", "D"};
	    String[] saNew    = new String[] {"A", "C", "F"};
	    
	    Assert.assertArrayEquals(new String[] {"A", "C"}, ArrayUtil.intersect(saSource, saNew));
	}
	
	/**
	 * Tests if remove check of iterator works.
	 */
	@Test
	public void testIteratorRemoveTicket1489()
	{
		try
		{
			List<String> list = new ArrayUtil<String>();

			list.add("1");
			list.add("2");
			list.add("3");
			
			for (String obj : list)
			{
				list.remove(obj);
			}
			
			Assert.fail("List is changed during iteration!");
		}
		catch (ConcurrentModificationException ex)
		{
			// This is Ok!!!!
		}
	}

   /**
     * Tests remove on null array.
     */
    @Test
    public void testRemoveOnNullArray()
    {
        // Should not throw an exception.
        ArrayUtil.remove(null, "Hallo");
    }
	
}	// TestArrayUtil
