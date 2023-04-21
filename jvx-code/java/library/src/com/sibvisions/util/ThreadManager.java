/*
 * Copyright 2013 SIB Visions GmbH
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
 * 17.11.2013 - [JR] - creation
 */
package com.sibvisions.util;

import java.lang.Thread.State;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.List;

/**
 * The <code>ThreadManager</code> manages threads. The threads get a
 * meaningful name and will be stored in an internal list. The manager
 * can stop and start new threads.<br>
 * It should be used to collect all threads of an application. The access
 * to the thread cache is synchronized.
 * 
 * @author René Jahn
 */
public class ThreadManager
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the internal "known" threads. */
	private ArrayUtil<WeakReference<Thread>> auCached = new ArrayUtil<WeakReference<Thread>>();
	
	/** the internal "stopped" threads. */
	private ArrayUtil<WeakReference<Thread>> auStopped = new ArrayUtil<WeakReference<Thread>>();

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>ThreadManager</code>.
	 */
	public ThreadManager()
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Adds a thread to the "known" thread list if not already added. The name of the thread
	 * will be extended with the call information of this method. The advantage is that the 
	 * <code>ThreadHandler</code> can interrupt and list all user-defined threads.
	 * 
	 * @param pThread the thread, which is to be added
	 * @return <code>true</code> if the thread was added, otherwise <code>false</code>
	 */
	public boolean add(Thread pThread)
	{
		if (addIntern(pThread))
		{
			setThreadName(pThread);
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Adds a thread to the "known" thread list if not already added.
	 * 
	 * @param pThread the thread, which is to be added 
	 * @return <code>true</code> if the thread was added, otherwise <code>false</code>
	 */
	private boolean addIntern(Thread pThread)
	{
		synchronized(auCached)
		{
			check(auCached);
		
			//don't cache more than once!
			if (auCached.indexOf(pThread) >= 0)
			{
				return false;
			}
		
			//add the thread to the internal cache
			auCached.add(new WeakReference(pThread));
		}
		
		return true;
	}
	
	/**
	 * Removes a thread from the "known" thread list.
	 * 
	 * @param pThread the thread, which is to be removed
	 */
	public void remove(Thread pThread)
	{
		if (pThread != null)
		{
			synchronized(auCached)
			{
				//removes the thread from the internal thread
				auCached.remove(pThread);
			}
		}
	}
	
	/**
	 * Starts a thread and adds it to the "known" threads. The name of the thread will
	 * be set to the threads class name and the call information of this method.
	 * If a running thread will be startet, the thread will be re
	 * 
	 * @param pRunnable a {@link Thread} or another {@link Runnable} implementation
	 * @return a new {@link Thread} with the <code>pRunnable</code> or <code>pRunnable</code>
	 *         if it's an instance of {@link Thread} and the thread is still alive. 
	 * @see #getRunningThreads()
	 * @throws IllegalStateException if the <code>pRunnable</code> is an instance of a finished {@link Thread}
	 */
	public Thread start(Runnable pRunnable)
	{
		if (pRunnable != null)
		{
			Thread thNew;
			
			//use the existing or create a new thread
			if (pRunnable instanceof Thread)
			{
				thNew = (Thread)pRunnable;

				synchronized(auCached)
				{
					if (thNew.isAlive() || auCached.indexOf(thNew) >= 0)
					{
						return thNew;
					}
				}
				
				//restart a finished thread
				if (thNew.getState() == State.TERMINATED)
				{
					throw new IllegalStateException("Thread may not be restarted!");
				}
			}
			else
			{
				thNew = new Thread(pRunnable);
			}
			
			if (addIntern(thNew))
			{
				setThreadName(thNew);
				thNew.start();
				
				return thNew;
			}
		}
		
		return null;
	}
	
	/**
	 * Stops/interrupts a thread and removes it from the "known" threads. The stopped
	 * thread will be available as pending stop thread.
	 * 
	 * @param pThread the thread, which is to be stopped
	 * @return <code>null</code>
	 * @see #getPendingStoppedThreads()
	 */
	public Thread stop(Thread pThread)
	{
		if (pThread == null)
		{
			return null;
		}
		
		//before interrupting the thread, add the thread to the pending stop threads,
		//because isStopped uses the pending queue
		synchronized(auStopped)
		{
			auStopped.add(new WeakReference(pThread));
		}

		pThread.interrupt();
		
		remove(pThread);
		
		check(auStopped);
		
		return null;
	}
	
	/**
	 * Stops/interrupts all "known" threads.
	 * 
	 * @see #stop(Thread)
	 */
	public void stop()
	{
		ArrayUtil<WeakReference<Thread>> auCopy;
		
		synchronized(auCached)
		{
			auCopy = new ArrayUtil<WeakReference<Thread>>(auCached);
		}	
		
		Thread thWeak;
		
		for (WeakReference<Thread> weak : auCopy)
		{
			thWeak = weak.get();
			
			if (thWeak != null)
			{
				stop(thWeak);
			}
		}
	}
	
	/**
	 * Gets if the current thread is stopped.
	 * 
	 * @return <code>true</code> if the current thread is stopped, otherwise <code>false</code>
	 */
	public boolean isStopped()
	{
		return isStopped(Thread.currentThread());
	}
	
	/**
	 * Gets if a desired thread is stopped.
	 * 
	 * @param pThread the thread
	 * @return <code>true</code> if <code>pThread</code> is stopped or <code>pThread == null</code>, otherwise <code>false</code>
	 */
	public boolean isStopped(Thread pThread)
	{
		if (pThread == null)
		{
			return true;
		}
		
		synchronized(auStopped)
		{
			return auStopped.indexOf(pThread) >= 0 || !pThread.isAlive();
		}
	}
	
	/**
	 * Removes unreferenced objects from the cache.
	 * 
	 * @param pCache the thread cache
	 */
	private void check(ArrayUtil<WeakReference<Thread>> pCache)
	{
		ArrayUtil<WeakReference<Thread>> auCopy;
		
		synchronized(pCache)
		{
			auCopy = new ArrayUtil<WeakReference<Thread>>(pCache);
		}
		
		Thread thWeak;
		
		for (WeakReference<Thread> weak : auCopy)
		{
			thWeak = weak.get();
			
			if (thWeak == null)
			{
				synchronized(pCache)
				{
					pCache.remove(weak);
				}
			}
		}		
	}
	
	/**
	 * Gets all alive threads from the cache and removes unreferenced and not running
	 * threads. 
	 * 
	 * @param pCache the thread cache
	 * @return the list of all alive threads from the cache.
	 */
	private List<Thread> getAlive(ArrayUtil<WeakReference<Thread>> pCache)
	{
		ArrayUtil<WeakReference<Thread>> auCopy;
		
		synchronized(pCache)
		{
			auCopy = new ArrayUtil<WeakReference<Thread>>(pCache);
		}
		
		Thread thWeak;
		
		ArrayUtil<Thread> auResult = new ArrayUtil<Thread>();
		
		WeakReference<Thread> wref;
		
		for (Iterator<WeakReference<Thread>> it = auCopy.iterator(); it.hasNext();)
		{
			wref = it.next();
			
			thWeak = wref.get();
			
			if (thWeak == null || !thWeak.isAlive())
			{
				synchronized(auStopped)
				{
					auStopped.remove(wref);
				}
			}
			else
			{
				auResult.add(thWeak);
			}
		}
		
		return auResult;
	}
	
	/**
	 * Gets all pending stopped threads which are already alive.
	 * 
	 * @return a list of threads
	 */
	public List<Thread> getPendingStoppedThreads()
	{
		return getAlive(auStopped);
	}
	
	/**
	 * Gets all "known" and alive threads.
	 * 
	 * @return a list of threads
	 */
	public List<Thread> getRunningThreads()
	{
		return getAlive(auCached);
	}
	
	/**
	 * Sets the name of the given thread. The new name has following format:
	 * oldname: the.thread.class -> the.name.of.the.CallerMethod(...)
	 * 
	 * @param pThread the thread
	 */
	private void setThreadName(Thread pThread)
	{
		String sName = pThread.getName();
		
		StackTraceElement[] stack = new Exception().getStackTrace();
		
		String sCaller = null;
		
		String sPredefName = getClass().getPackage().getName() + ".Thread";
		
		for (int i = 0; i < stack.length && sCaller == null; i++)
		{
			if (!stack[i].getClassName().startsWith(sPredefName))
			{
				sCaller = stack[i].toString();
			}
		}
		
		//Set the threads name
		pThread.setName((sName != null ? sName + ": " : "") + pThread.getClass().getName() + " -> " + sCaller);
	}

}	// ThreadManager
