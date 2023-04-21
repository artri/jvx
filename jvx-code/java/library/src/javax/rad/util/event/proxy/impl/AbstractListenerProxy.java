/*
 * Copyright 2019 SIB Visions GmbH
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
 * 20.03.2019 - [JR] - creation
 */
package javax.rad.util.event.proxy.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.WeakHashMap;

import javax.rad.util.ExceptionHandler;

/**
 * The <code>AbstractListenerProxy</code> is the base class for listener implementations which
 * will forward the listener methods to an {@link InvocationHandler}.
 * 
 * @author René Jahn
 */
public abstract class AbstractListenerProxy 
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the toString method. */
	private static Method metToString;
	
	/** the hashCode method. */
	private static Method metHashCode;

	/** the equals method. */
	private static Method metEquals;

	/** the invocation handler. */
	protected InvocationHandler handler;
	
	/** the method cache. */
	private static WeakHashMap<Class<?>, HashMap<String, Method>> hmpMethods = new WeakHashMap<Class<?>, HashMap<String, Method>>();
	
	static
	{
		try
		{
			metToString = AbstractListenerProxy.class.getMethod("toString");
			metHashCode = AbstractListenerProxy.class.getMethod("hashCode");
			metEquals = AbstractListenerProxy.class.getMethod("equals", Object.class);
		}
		catch (Exception e)
		{
			//ignore
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>AbstractListenerProxy</code>.
	 * 
	 * @param pHandler the handler
	 */
	public AbstractListenerProxy(InvocationHandler pHandler)
	{
		handler = pHandler;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object pObject)
	{
		try
		{
			return ((Boolean)handler.invoke(null, metEquals, new Object[] {pObject})).booleanValue();
		}
		catch (Throwable th)
		{
			throw new RuntimeException(th);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode()
	{
		try
		{
			return ((Integer)handler.invoke(null, metHashCode, null)).intValue();
		}
		catch (Throwable th)
		{
			throw new RuntimeException(th);
		}
	}	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		try
		{
			return (String)handler.invoke(null, metToString, null);
		}
		catch (Throwable th)
		{
			throw new RuntimeException(th);
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Dispatches an event with given parameters.
	 * 
	 * @param pMethod the event method
	 * @param pParameter the parameter
	 * @return the event call result
	 * @throws Throwable if an exception occurs
	 */
	protected Object dispatch(Method pMethod, Object... pParameter) throws Throwable
	{
		return handler.invoke(null, pMethod, pParameter);
	}
	
	/**
	 * Dispatches an event with given parameters without throwing an exception.
	 * 
	 * @param pMethod the event method
	 * @param pParameter the parameter
	 * @return the event call result
	 */
	protected Object dispatchSilent(Method pMethod, Object... pParameter)
	{
		try
		{
			return dispatch(pMethod, pParameter);
		}
		catch (Throwable th)
		{
			ExceptionHandler.raise(th);
			
			return null;
		}
	}
	
	/**
	 * Gets the method for the given name.
	 *
	 * @param pName the method name
	 * @param pParameter the parameters
	 * @return the method the method
	 * @throws RuntimeException if method detection fails
	 */	
	protected Method method(String pName, Class<?>... pParameter)
	{
		Class<?> clazz = getClass();
		
		HashMap<String, Method> hmpMeth = hmpMethods.get(clazz);
		
		Method met = null;
		
		if (hmpMeth != null)
		{
			met = hmpMeth.get(pName);
		}
		else
		{
			hmpMeth = new HashMap<String, Method>();
			
			hmpMethods.put(clazz, hmpMeth);
		}
		
		if (met == null)
		{
			try
			{
				met = getClass().getMethod(pName, pParameter);
			}
			catch (Exception e)
			{
				throw new RuntimeException(e);
			}
			
			hmpMeth.put(pName, met);
		}
		
		return met;
	}	
	
	/**
	 * Gets the handler.
	 * 
	 * @return the handler
	 */
	public final InvocationHandler getHandler()
	{
		return handler;
	}
	
}	// AbstractListenerProxy
