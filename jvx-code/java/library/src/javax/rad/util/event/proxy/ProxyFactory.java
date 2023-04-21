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
package javax.rad.util.event.proxy;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.util.HashMap;

import javax.rad.util.event.proxy.impl.RunnableProxy;

/**
 * The <code>ProxyFactory</code> is a factory which creates proxy classes for event listeners.
 * 
 * @author René Jahn
 */
public final class ProxyFactory 
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the custom proxy cache. */
	private static HashMap<String, Class<?>> hmpProxies = new HashMap<String, Class<?>>();
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	

	static
	{
		register(Runnable.class, RunnableProxy.class);
	}
	
	/**
	 * Invisible constructor because <code>ProxyFactory</code> is a utility class.
	 */
	private ProxyFactory()
	{
		// No instance needed.
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
	
	/**
	 * Registers a new proxy class.
	 * 
	 * @param pListenerClass the listener class
	 * @param pProxyClass the proxy class
	 */
	public static void register(Class<?> pListenerClass, Class<?> pProxyClass)
	{
		hmpProxies.put(pListenerClass.getName(), pProxyClass);
	}

	/**
	 * Registers a new proxy class.
	 * 
	 * @param pListenerClass the full qualified listener class name
	 * @param pProxyClass the proxy class
	 */
	public static void register(String pListenerClass, Class<?> pProxyClass)
	{
		hmpProxies.put(pListenerClass, pProxyClass);
	}

	/**
	 * Unregisters a proxy.
	 *  
	 * @param pListenerClass the listener class
	 */
	public static void unregister(Class<?> pListenerClass)
	{
		hmpProxies.remove(pListenerClass.getName());
	}
	
	/**
	 * Unregisters a proxy.
	 *  
	 * @param pListenerClass the full qualified listener class name
	 */
	public static void unregister(String pListenerClass)
	{
		hmpProxies.remove(pListenerClass);
	}
	
	/**
	 * Creates a proxy listener. If no custom proxy is registered for the given listener class, a class
	 * with the postfix <code>Proxy</code> will be searched and used if available, e.g. a.b.c.IActionListener -&gt; a.b.c.IActionListenerProxy.
	 * 
	 * @param pLoader the class loader
	 * @param pListenerClass the listener class
	 * @param pHandler the handler
	 * @return the proxy instance
	 * @param <T> the listener type
	 */
	public static <T> T createListener(ClassLoader pLoader, Class<?> pListenerClass, InvocationHandler pHandler)
	{
		Class<?> clz = hmpProxies.get(pListenerClass.getName());
		
		if (clz == null)
		{
			try
			{
				if (pLoader != null)
				{
					clz = Class.forName(pListenerClass.getName() + "Proxy", false, pLoader);
				}
				else
				{
					clz = Class.forName(pListenerClass.getName() + "Proxy");
				}
			}
			catch (Exception ex)
			{
				clz = null;
			}
		}
		
		if (clz == null)
		{
			throw new IllegalArgumentException("Proxy was not found for class: " + pListenerClass.getName());
		}
		
		try
		{
			Constructor cons = clz.getConstructor(InvocationHandler.class);
			
			return (T)cons.newInstance(pHandler);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

}	// ProxyFactory
