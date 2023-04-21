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
 * 11.05.2011 - [HM] - creation
 * 24.07.2020 - [JR] - invokeLater queue en/disable
 */
package javax.rad.ui;

import java.util.ArrayList;

import javax.rad.util.ExceptionHandler;

/**
 * InvokeLaterThread supports invokeLater notification for GUI Controls, when the thread ends.
 * This is necessary to guarantee that invokeLater GUI actions caused in this Thread occurs
 * after finishing this thread.
 * InvokeLater action can also be triggered immediately by executeInvokeLater.
 * This call guarantees thread save execution of the invokeLater actions in this thread. 
 * All InvokeLater actions are executed in the technology dependent GUI Thread.
 * 
 * @author Martin Handsteiner
 */
public class InvokeLaterThread extends Thread
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The factory incanciating this thread. */
	private IFactory factory;
	
	/** The runnable to execute. */
	private Runnable runnable;
	
	/** The invokeLater queue. */
	private ArrayList<Runnable> invokeLaterQueue = new ArrayList<Runnable>();
	
	/** invoke later queue enabled. */
	private boolean bQueueEnabled = false;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Constructs a new InvokeLaterThread.
	 * 
	 * @param pFactory the Factory to execute invokeLater actions.
	 * @param pRunnable the Runnable of this thread.
	 */
	public InvokeLaterThread(IFactory pFactory, Runnable pRunnable)
	{
		super();
		
		factory = pFactory;
		runnable = pRunnable;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void run()
	{
		try
		{
			if (runnable != null)
			{
				runnable.run();
			}
		}
		finally
		{
			executeInvokeLater();
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
	/**
	 * Gets the factory.
	 * 
	 * @return the factory
	 */
	protected IFactory getFactory()
	{
		return factory;
	}
	
	/**
	 * Gets the runnable.
	 * 
	 * @return the runnable
	 */
	protected Runnable getRunnable()
	{
		return runnable;
	}
	
	/**
	 * Executes the given Runnable after this Thread is finished.
	 * 
	 * @param pRunnable the Runnable to be executed after the Thread is finished.
	 */
	public void invokeLater(Runnable pRunnable)
	{
		if (bQueueEnabled)
		{
			synchronized (invokeLaterQueue)
			{
				invokeLaterQueue.add(pRunnable);
			}
		}
		else
		{
			try
			{
				if (factory == null)
				{
					pRunnable.run();
				}
				else
				{
					factory.invokeAndWait(pRunnable);
				}
			}
			catch (Throwable th)
			{
				th.printStackTrace();
			}
		}
	}
	
	/**
	 * Executes thread save all invokeLater actions immediate.
	 */
	public void executeInvokeLater()
	{
		boolean bEmpty;
		
		synchronized (invokeLaterQueue)
		{
			bEmpty = invokeLaterQueue.isEmpty();
		}
		
		while (!bEmpty)
		{
			ArrayList<Runnable> queue = invokeLaterQueue;
			
			synchronized (queue)
			{
				invokeLaterQueue = new ArrayList<Runnable>();
			}
			
			for (int i = 0; i < queue.size(); i++)
			{
				Runnable invokeLaterRunnable = queue.get(i);
				
				try
				{
					if (factory == null)
					{
						invokeLaterRunnable.run();
					}
					else
					{
						factory.invokeAndWait(invokeLaterRunnable);
					}
				}
				catch (Throwable th)
				{
					ExceptionHandler.show(th);
				}
			}
			
			synchronized (invokeLaterQueue)
			{
				bEmpty = invokeLaterQueue.isEmpty();
			}
		}
	}
	
	/**
	 * Sets the enabled state of invoke later queue.
	 * 
	 * @param pEnabled <code>true</code> to enable invoke later queueing, <code>false</code> to disable
	 */
	public static void setInvokeLaterQueueEnabled(boolean pEnabled)
	{
		Thread th = Thread.currentThread();
		
		if (th instanceof InvokeLaterThread)
		{
			((InvokeLaterThread)th).bQueueEnabled = pEnabled;
		}
	}
	
	/**
	 * Gets whether invoke later queue is enabled.
	 * 
	 * @return <code>true</code> if enabled, <code>false</code> if disabled
	 */
	public static boolean isInvokeLaterQueueEnabled()
	{
		Thread th = Thread.currentThread();
		
		if (th instanceof InvokeLaterThread)
		{
			return ((InvokeLaterThread)th).bQueueEnabled;
		}
		
		//default
		return false;
	}
	
	/**
	 * Executes the invoke later queue, if current Thread is an {@link InvokeLaterThread}.
	 */
	public static void executeInvokeLaterQueue()
	{
		Thread th = Thread.currentThread();
		
		if (th instanceof InvokeLaterThread)
		{
			((InvokeLaterThread)th).executeInvokeLater();
		}
	}
	
}	// InvokeLaterThread
