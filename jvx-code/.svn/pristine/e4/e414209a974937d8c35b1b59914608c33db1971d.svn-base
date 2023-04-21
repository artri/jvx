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
 * 19.11.2008 - [JR] - creation
 */
package com.sibvisions.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the <code>ThreadHandler</code> class.
 * 
 * @author René Jahn
 * @see ThreadHandler
 */
public class TestThreadHandler implements Runnable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the mark for pending stop test. */
	private boolean bPending;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public void run()
	{
		long lStart = System.currentTimeMillis();
		
		while (!ThreadHandler.isStopped())
		{
			try
			{
				Thread.sleep(100);
				
				//run max. 5 seconds
				if (bPending || System.currentTimeMillis() - lStart >= 5000)
				{
					while (bPending)
					{
						try
						{
							Thread.sleep(100);
						}
						catch (InterruptedException ie)
						{
							//run "endless"
						}
					}
					
					return;
				}
			}
			catch (InterruptedException iex)
			{
				//do nothing and stop
			}
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Tests the start functionality.
	 * 
	 * @throws Exception if the test fails
	 */
	@Test
	public void testStart() throws Exception
	{
		Thread th = ThreadHandler.start(new TestThreadHandler());
		
		Thread.sleep(2000);
		
		Assert.assertFalse("Thread already stopped!", ThreadHandler.isStopped(th));
		
		Thread.sleep(4000);
		
		Assert.assertTrue("Thread not stopped!", ThreadHandler.isStopped(th));
		
		Assert.assertEquals(0, ThreadHandler.getRunningThreads().size());
		Assert.assertEquals(0, ThreadHandler.getPendingStoppedThreads().size());
		
		//start Thread via Handler
		ThreadHandler.stop(th);
	}

	/**
	 * Test restarting a terminated thread.
	 * 
	 * @throws Exception if the test fails
	 */
	@Test (expected = IllegalStateException.class)
	public void testIllegalStart() throws Exception
	{
		Thread th = ThreadHandler.start(new TestThreadHandler());
		ThreadHandler.stop(th);

		Thread.sleep(200);

		//try to restart
		ThreadHandler.start(th);
	}
	
	/**
	 * Tests the stop functionality.
	 * 
	 * @throws Exception if the test fails
	 */
	@Test
	public void testStop() throws Exception
	{
		Thread th = ThreadHandler.start(new TestThreadHandler());
		
		Thread.sleep(1000);
		
		ThreadHandler.stop(th);
		
		Thread.sleep(500);

		Assert.assertEquals(0, ThreadHandler.getRunningThreads().size());
		Assert.assertEquals(0, ThreadHandler.getPendingStoppedThreads().size());
	}

	/**
	 * Tests the stop functionality.
	 * 
	 * @throws Exception if the test fails
	 */
	@Test
	public void testStopAll() throws Exception
	{
		ThreadHandler.start(new TestThreadHandler());
		ThreadHandler.start(new TestThreadHandler());
		ThreadHandler.start(new TestThreadHandler());
		ThreadHandler.start(new TestThreadHandler());
		
		Assert.assertEquals(4, ThreadHandler.getRunningThreads().size());

		Thread.sleep(500);
		
		ThreadHandler.stop();
		
		Thread.sleep(500);

		Assert.assertEquals(0, ThreadHandler.getRunningThreads().size());
		Assert.assertEquals(0, ThreadHandler.getPendingStoppedThreads().size());
	}
	
	
	/**
	 * Tests the stop pending functionality.
	 * 
	 * @throws Exception if the test fails
	 */
	@Test
	public void testStopPending() throws Exception
	{
		TestThreadHandler handler = new TestThreadHandler();
		handler.bPending = true;
		
		Thread th = ThreadHandler.start(handler);
		
		Thread.sleep(500);
		
		ThreadHandler.stop(th);
		
		Assert.assertEquals(0, ThreadHandler.getRunningThreads().size());
		Assert.assertEquals(1, ThreadHandler.getPendingStoppedThreads().size());
		
		handler.bPending = false;
		
		Thread.sleep(500);
		
		Assert.assertEquals(0, ThreadHandler.getRunningThreads().size());
		Assert.assertEquals(0, ThreadHandler.getPendingStoppedThreads().size());
	}	
	
	
	/**
	 * Tests the add functionality.
	 */
	@Test
	public void testAdd()
	{
		//manual start
		Thread th = new Thread(new TestThreadHandler());
		th.start();
		
		Assert.assertTrue("Thread not added to handler", ThreadHandler.add(th));
		Assert.assertFalse("Thread illegaly added to handler", ThreadHandler.add(th));
		
		Assert.assertEquals(1, ThreadHandler.getRunningThreads().size());

		ThreadHandler.stop(th);
		
		Assert.assertEquals(0, ThreadHandler.getRunningThreads().size());
	}
	
	/**
	 * Tests the system defined name of a thread.
	 * 
	 * @throws Exception if the test fails
	 */
	@Test
	public void testThreadName() throws Exception
	{
		Thread th = ThreadHandler.start(new TestThreadHandler());
		
		System.out.println(th.getName());
		
		Assert.assertTrue("Invalid Thread name: " + th.getName(), 
				          th.getName().contains(": java.lang.Thread -> com.sibvisions.util.TestThreadHandler.testThreadName("));
		
		Thread.sleep(200);

		th = new Thread(new TestThreadHandler());

		Assert.assertTrue("Thread not added to handler", ThreadHandler.add(th));
		
		
		Assert.assertTrue("Invalid Thread name: " + th.getName(), 
				          th.getName().contains(": java.lang.Thread -> com.sibvisions.util.TestThreadHandler.testThreadName("));

		ThreadHandler.stop();
	}
	
}	// TestThreadHandler
