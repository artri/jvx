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

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;

/**
 * Base class for memory test classes.
 * 
 * @author René Jahn
 */
public abstract class MemoryInfo
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** Runtime for accessing the memory information. */
	private static Runtime rt = Runtime.getRuntime(); 

	/** memory information via mx. */
	private static MemoryMXBean memory  = ManagementFactory.getMemoryMXBean();

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * MemoryInfo.
	 */
	protected MemoryInfo()
	{
		
	}
	
	/**
	 * Starts the garbage collector.
	 */
	public static void freeMem()
	{
		for (int i = 0; i < 50; i++)
		{
			rt.runFinalization();
			rt.gc();
		}
	}
	
	/**
	 * Returns the used memory.
	 * 
	 * @return total memory - free memory
	 */
	public static long usedMemory()
	{
		return rt.totalMemory() - rt.freeMemory();
	}

	/**
	 * Returns the used non heap memory from mx.
	 * 
	 * @return used non heap memory from mx.
	 */
	public static long usedMXNonHeap()
	{
	    return memory.getNonHeapMemoryUsage().getUsed();
	}

	/**
	 * Returns the used heap memory from mx.
	 * 
	 * @return used heap memory from mx.
	 */
	public static long usedMXHeap()
	{
	    return memory.getHeapMemoryUsage().getUsed();
	}
	
}	// MemoryInfo
