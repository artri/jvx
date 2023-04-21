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
 * 23.11.2008 - [JR] - creation
 */
package com.sibvisions.util;


/**
 * The <code>Memory</code> class is a utility class for memory optimization
 * and information.
 * 
 * @author René Jahn
 */
public final class Memory implements Runnable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the garbage thread. */
	private static Thread thGc = null;

	/** the current thread run count. */
	private static volatile int lThreadRuns = 0;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Invisible constructor because <code>Memory</code> is a utility
	 * class.
	 */
	private Memory()
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Triggers the finalization of objects and the garbage collector.
	 */
	public void run()
	{
		try
		{
			Runtime runtime = Runtime.getRuntime();
			
		    long lOldMemory;
		    long lMemory;
		    long lDiff;
		    
		    int i;
		    
			while (!ThreadHandler.isStopped())
			{
		        Thread.sleep(10000);
		        
		        if (lThreadRuns >= 10) 
		        {
					lOldMemory = 0;
					lDiff = 2049;
					lMemory = runtime.freeMemory();
					
					//max. 10 runs or the memory difference doesn't change enough
					for (i = 0; i < 10 && lDiff > 1024; i++) 
					{
						runtime.runFinalization();
						runtime.gc();
						
						lOldMemory = lMemory;

						Thread.sleep(100);						

						lMemory = runtime.freeMemory();

						lDiff = Math.abs(lOldMemory - lMemory);
					}

					// if garbage succeded, wait 100 sec, else try in 10 seconds again
					// after 2 further unsuccessful attempts without any gc call (lThreadRuns is set to 10 again)
					// wait long, and let the default garbage collector do.
					if (i < 10 || lThreadRuns > 11) 
					{
						lThreadRuns = 0;
					}
		        }
		        
		        lThreadRuns++;
			}
		}
		catch (Exception e)
		{
			//nothing to be done
		}
		
		//avoid method calls -> set null for gc() null check!
		thGc = null;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Starts the garbage collector triggering thread.
	 */
	public static synchronized void startGc()
	{
		if (ThreadHandler.isStopped(thGc))
		{
			thGc = ThreadHandler.start(new Memory());
		}
	}
	
	/**
	 * Stops the garbage collector triggering thread.
	 */
	public static synchronized void stopGc()
	{
		thGc = ThreadHandler.stop(thGc);
	}
	
	/**
	 * Starts the garbage collector as soon as possible.
	 */
	public static synchronized void gc()
	{
		//start "now" (some seconds delay)
		lThreadRuns = 10;
		
		//avoid method calls - check thread direct
		if (thGc == null)
		{
			startGc();
		}
	}
	
}	// Memory
