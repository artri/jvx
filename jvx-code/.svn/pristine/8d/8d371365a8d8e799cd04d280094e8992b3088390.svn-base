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
 * 17.11.2013 - [JR] - use singleton instead of static
 */
package com.sibvisions.util;

import java.util.List;

/**
 * The <code>ThreadHandler</code> is a utility class for {@link ThreadManager}. It
 * is the singleton access to global thread management.
 * 
 * @author René Jahn
 */
public final class ThreadHandler
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the thread manager instance. */
	private static ThreadManager manager = new ThreadManager();

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Invisible constructor, because the <code>ThreadHandler</code> is a utility class.
	 */
	private ThreadHandler()
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Adds a thread to the thread list.
	 * 
	 * @param pThread the thread
	 * @return <code>true</code> if the thread was added, otherwise <code>false</code>
	 * @see ThreadManager#add(Thread)
	 */
	public static boolean add(Thread pThread)
	{
		return manager.add(pThread);
	}
	
	/**
	 * Removes a thread from the list.
	 * 
	 * @param pThread the thread
	 * @see ThreadManager#remove(Thread)
	 */
	public static void remove(Thread pThread)
	{
		manager.remove(pThread);
	}
	
	/**
	 * Starts a thread and adds it to the thread list.
	 * 
	 * @param pRunnable a {@link Thread} or another {@link Runnable} implementation
	 * @return a new {@link Thread} with the <code>pRunnable</code> or <code>pRunnable</code>
	 *         if it's an instance of {@link Thread} and the thread is still alive. 
	 * @see #getRunningThreads()
	 * @see ThreadManager#start(Runnable)
	 */
	public static Thread start(Runnable pRunnable)
	{
		return manager.start(pRunnable);
	}
	
	/**
	 * Stops/interrupts a thread and removes it from the thread list.
	 * 
	 * @param pThread the thread, which is to be stopped
	 * @return <code>null</code>
	 * @see #getPendingStoppedThreads()
	 * @see ThreadManager#stop(Thread)
	 */
	public static Thread stop(Thread pThread)
	{
		return manager.stop(pThread);
	}
	
	/**
	 * Stops/interrupts all threads from the thread list.
	 * 
	 * @see #stop(Thread)
	 * @see ThreadManager#stop()
	 */
	public static void stop()
	{
		manager.stop();
	}
	
	/**
	 * Gets if the current thread is stopped.
	 * 
	 * @return <code>true</code> if the current thread is stopped, otherwise <code>false</code>
	 * @see ThreadManager#isStopped()
	 */
	public static boolean isStopped()
	{
		return manager.isStopped();
	}
	
	/**
	 * Gets if the given thread is stopped.
	 * 
	 * @param pThread the thread
	 * @return <code>true</code> if <code>pThread</code> is stopped or <code>pThread == null</code>, otherwise <code>false</code>
	 * @see ThreadManager#isStopped(Thread)
	 */
	public static boolean isStopped(Thread pThread)
	{
		return manager.isStopped(pThread);
	}
	
	/**
	 * Gets all pending stopped threads which are already alive.
	 * 
	 * @return a list of threads
	 * @see ThreadManager#getPendingStoppedThreads()
	 */
	public static List<Thread> getPendingStoppedThreads()
	{
		return manager.getPendingStoppedThreads();
	}
	
	/**
	 * Gets all alive threads from the thread list.
	 * 
	 * @return a list of threads
	 * @see ThreadManager#getRunningThreads()
	 */
	public static List<Thread> getRunningThreads()
	{
		return manager.getRunningThreads();
	}

}	// ThreadHandler
