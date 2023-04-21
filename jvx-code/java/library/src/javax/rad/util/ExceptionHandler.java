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
 * 01.12.2008 - [JR] - creation
 * 09.10.2011 - [JR] - #478: fireHandleException - copy the current list
 * 02.02.2012 - [JR] - #545: addExceptionListener by index
 */
package javax.rad.util;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.WeakHashMap;

import javax.rad.genui.UIFactoryManager;
import javax.rad.ui.IFactory;
import javax.rad.util.event.IExceptionListener;

import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.log.LoggerFactory;

/**
 * The <code>ExceptionHandler</code> is the global handler for program
 * exceptions. All exceptions should handled with {@link ExceptionHandler#raise(Throwable)}.
 * 
 * @author René Jahn
 */
public final class ExceptionHandler
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the registered {@link IExceptionListener}. */
	private static WeakHashMap<IFactory, List<WeakReference<IExceptionListener>>> factoryListeners = 
		new WeakHashMap<IFactory, List<WeakReference<IExceptionListener>>>();
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Invisible constructor, because the <code>ExceptionHandler</code> 
	 * class is a utility class.
	 */
	private ExceptionHandler()
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Notifies the handler that an exception occured.
	 * 
	 * A SilentAbortException is throws anyway, for breaking the normal code flow.
	 * 
	 * @param pThrowable the exception
	 * @throws SilentAbortException is thrown anyway to break code flow.
	 */
	public static void raise(Throwable pThrowable) 
	{
		if (pThrowable instanceof SilentAbortException)
		{
			throw (SilentAbortException)pThrowable;
		}
		else
		{
			fireHandleException(pThrowable);
			
			throw new SilentAbortException(pThrowable);
		}
	}
	
	/**
	 * Notifies the handler that an exception occured.
	 * 
	 * @param pThrowable the exception
	 */
	public static void show(Throwable pThrowable)
	{
		if (!(pThrowable instanceof SilentAbortException))
		{
			fireHandleException(pThrowable);
		}
	}
	
	/**
	 * Adds a listener to the handler.
	 * 
	 * @param pListener the listener
	 */
	public static void addExceptionListener(IExceptionListener pListener)
	{
		addExceptionListener(pListener, -1);
	}
	
	/**
	 * Adds a listener to the handler, at the given position.
	 * 
	 * @param pListener the listener
	 * @param pIndex the index or <code>-1</code> to add at the end of the list
	 */
	public static void addExceptionListener(IExceptionListener pListener, int pIndex)
	{
		IFactory factory = UIFactoryManager.getFactory();
		
		List<WeakReference<IExceptionListener>> listeners = factoryListeners.get(factory);
		
		if (listeners == null)
		{
			listeners = new ArrayUtil<WeakReference<IExceptionListener>>();
			
			factoryListeners.put(factory, listeners);
		}
		
		if (!listeners.contains(pListener))
		{
			if (pIndex < 0)
			{
				listeners.add(new WeakReference<IExceptionListener>(pListener));
			}
			else
			{
				listeners.add(pIndex, new WeakReference<IExceptionListener>(pListener));
			}
		}
	}
	
	/**
	 * Removes a listener from the handler.
	 * 
	 * @param pListener the listener
	 */
	public static void removeExceptionListener(IExceptionListener pListener)
	{
		IFactory factory = UIFactoryManager.getFactory();
		
		List<WeakReference<IExceptionListener>> listeners = factoryListeners.get(factory);
		
		if (listeners != null)
		{
			listeners.remove(pListener);
			
			if (listeners.isEmpty())
			{
				factoryListeners.remove(factory);
			}
		}
	}
	
	/**
	 * Returns all registered exception listeners.
	 * 
	 * @return a list of exception listeners
	 */
	public static IExceptionListener[] getListeners()
	{
		IFactory factory = UIFactoryManager.getFactory();
		
		List<WeakReference<IExceptionListener>> listeners = factoryListeners.get(factory);
		
		if (listeners != null)
		{
    		List<IExceptionListener> result = new ArrayUtil<IExceptionListener>();
    		
    		for (int i = 0; i < listeners.size(); i++)
    		{
    			IExceptionListener listener = listeners.get(i).get();
    			if (listener != null)
    			{
    				result.add(listener);
    			}
    		}
    
    		return (IExceptionListener[])result.toArray(new IExceptionListener[result.size()]);
		}
		else
		{
		    return new IExceptionListener[0];
		}
	}

	/**
	 * Notifies all listeners that an exception occured.
	 * 
	 * @param pThrowable the exception
	 */
	private static void fireHandleException(Throwable pThrowable)
	{
		IFactory factory = UIFactoryManager.getFactory();

		List<WeakReference<IExceptionListener>> listeners = factoryListeners.get(factory);
		
		if (listeners != null && !listeners.isEmpty())
		{
			//copy the listener list - IMPORTANT because it is possible that during notification e.g. the last listener is removed and a new
			//listener is added. In that case, the new listener is notified but that is not a correct listener!
			listeners = new ArrayUtil<WeakReference<IExceptionListener>>(listeners);
			
			for (int i = 0, cnt = listeners.size(); i < cnt; i++)
			{
				IExceptionListener listener = listeners.get(i).get();
				
				if (listener != null)
				{
					listener.handleException(pThrowable);
				}
			}
		}
		else
		{
		    LoggerFactory.getInstance(ExceptionHandler.class).error(pThrowable);
		}
	}
	
}	// ExceptionHandler
