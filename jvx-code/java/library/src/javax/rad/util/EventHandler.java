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
 * 16.04.2008 - [HM] - creation
 * 07.11.2010 - [JR] - addListener by index
 * 05.12.2010 - [JR] - ignore SilentAbortException
 *                   - logging implemented
 * 22.12.2012 - [JR] - #454: en/disable event dispatching    
 * 13.03.2013 - [JR] - getLastDispatchedObject, getCurrentDispatchObject implemented
 * 27.05.2013 - [JR] - getWrappedException implemented 
 * 09.07.2015 - [JR] - #1432: reduced optimization in ListenerHandler   
 * 09.12.2015 - [JR] - #1540: guaranteed reset of first dispatch           
 */
package javax.rad.util;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.List;

import javax.rad.util.event.proxy.ProxyFactory;
import javax.rad.util.event.proxy.impl.AbstractListenerProxy;

import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.Reflective;
import com.sibvisions.util.log.ILogger;
import com.sibvisions.util.log.LoggerFactory;

/**
 * Platform and technology independent event handler.
 * It is designed for use with UI elements and non UI elements.
 * There can be used with any Listener Interface. and implicit forwarded
 * to any function.  
 * <code>
 *   button.eventAction().addListener(
 *     new ActionListener()
 *     {
 *       public void actionPerformed(ActionEvent pEvent)
 *       {
 *         doSave();
 *       }
 *     });
 * </code>
 * <code>
 *   button.eventAction().addListener(this, "doSave");
 * </code>
 * 
 * @author Martin Handsteiner
 * 
 * @param <L> the Listener type
 */
public class EventHandler<L>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the logger. */
	private static ILogger logger = LoggerFactory.getInstance(EventHandler.class);

	/** Array for null parameter in case of array type. */
	private static final Object[] NULL_PARAMETER = new Object[1];

	/** The event type class. */
	private Class<L> listenerType;
	/** The listener method. */
	private Method listenerMethod;
	/** True, if the parameters has to be wrapped in an object array. */
	private boolean callListenerMethodReflective;
	/** True, if listener method has one parameter. */
	private boolean listenerMethodHasOneParameter;
	/** The listener method. */
	private Class[] parameterTypes;

	/** The listeners. */
	private List<ListenerHandler> listeners = null;
	/** The default listener. */
	private ListenerHandler defaultListener = null;
    /** The listeners. */
    private List<ListenerHandler> internalListeners = null;
	
	/** the last dispatched object. */
	private static WeakReference<Object> wrefLastDispatchedObject = null;

	/** the current dispatch object. */
	private static WeakReference<Object> wrefCurrentDispatchObject = null;
	
	/** whether dispatch call is first call (recursive call prevention). */
	private boolean bFirstDispatch = true;
    /** whether dispatch call is first call (recursive call prevention). */
    private boolean bFirstInternalDispatch = true;
	
	/** whether event dispatching should be dispatched. */
	private boolean bDispatchEvents = true;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Constructs a new EventHandler, the listener type may only have 1 method.
	 *  
	 * @param pListenerType the listener type interface.
	 * @param pParameterTypes parameter types to check additional.
	 */
	public EventHandler(Class<L> pListenerType, Class... pParameterTypes)
	{
		this(pListenerType, null, pParameterTypes);
	}
	
	/**
	 * Constructs a new EventHandler.
	 *  
	 * @param pListenerType the listener type interface.
	 * @param pListenerMethodName the method to be called inside the interface.
	 * @param pParameterTypes parameter types to check additional.
	 */
	public EventHandler(Class<L> pListenerType, String pListenerMethodName, Class... pParameterTypes)
	{
		if (!pListenerType.isInterface())
		{
			throw new IllegalArgumentException("The listener type class has to be an interface!");
		}
		
		listenerMethod = findMethodByName(pListenerType, pListenerMethodName);
		
		listenerType = pListenerType;
		
		Class[] paramTypes = listenerMethod.getParameterTypes();
		callListenerMethodReflective = paramTypes.length > 0 && Object[].class.isAssignableFrom(paramTypes[paramTypes.length - 1]);
		listenerMethodHasOneParameter = paramTypes.length == 1;
				
		if (pParameterTypes == null || pParameterTypes.length > 0)
		{
			if (!Reflective.isParamTypeListValid(paramTypes, pParameterTypes, listenerMethod.isVarArgs()))
			{
				throw new IllegalArgumentException("The given parameter types are not suitable to the parameter types of the listener method!");
			}
			
			parameterTypes = pParameterTypes;
		}
		else
		{
			parameterTypes = null;
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Searches the given method.
	 * 
	 * @param pListenerType the interface to search in.
	 * @param pListenerMethodName the method to search for.
	 * @return the method, if found.
	 * @throws IllegalArgumentException if the method does not exist. 
	 */
	private Method findMethodByName(Class<L> pListenerType, String pListenerMethodName)
	{
		Method[] methods = pListenerType.getMethods();
		if (pListenerMethodName == null)
		{
			if (methods.length == 1)
			{
				return methods[0];
			}
			throw new IllegalArgumentException("Listener class " + pListenerType.getName() + " has more than 1 method, the listener method has to be specified!");
		}
		else
		{
			for (int i = 0; i < methods.length; i++)
			{
				if (pListenerMethodName.equals(methods[i].getName()))
				{
					return methods[i];
				}
			}
			throw new IllegalArgumentException("Listener method " + pListenerMethodName + " not found in class " + pListenerType.getName() + "!");
		}
	}
	
	/**
	 * Adds a listener.
	 * If the Listener is already added, it is removed before, and added at the end.
	 * 
	 * @param pListener the listener.
	 */
	public void addListener(L pListener)
	{
		addListener(pListener, -1);
	}
	
	/**
	 * Adds a listener at a given index.
	 * If the Listener is already added, it is removed before, and added at the new position.
	 * 
	 * @param pListener the listener.
	 * @param pIndex the index to add.
	 */
	public synchronized void addListener(L pListener, int pIndex)
	{
		if (listeners == null)
		{
			listeners = new ArrayUtil<ListenerHandler>();
		}
		
		int index = listeners.indexOf(pListener);
		
		if (index >= 0)
		{
		    removeHandler(index);
			
			if (pIndex > index)
			{
				pIndex--;
			}
		}
		
		ListenerHandler handler = null;
		
		if (Proxy.isProxyClass(pListener.getClass()))
		{
			handler = (ListenerHandler)Proxy.getInvocationHandler(pListener);
		}
		else if (pListener instanceof AbstractListenerProxy)
		{
			Object oHandler = ((AbstractListenerProxy)pListener).getHandler();
			
			if (oHandler instanceof EventHandler.ListenerHandler)
			{
				handler = (ListenerHandler)oHandler;
			}
		}
		
		if (handler == null)
		{
			handler = new ListenerHandler(pListener);
		}
		
		addHandler(pIndex, handler);
	}
	
	/**
	 * Adds a listener.
	 * If the Listener is already added, it is removed before, and added at the end.
	 * 
	 * @param pListener the listener object.
	 * @param pMethodName the method name.
	 */
	public void addListener(Object pListener, String pMethodName)
	{
		addListener(pListener, pMethodName, -1);
	}
	
	/**
	 * Adds a listener.
	 * If the listener is already added, it is remove before, and added at the end.
	 * 
	 * @param pListener the listener.
	 */
	public void addListener(IRunnable pListener)
	{
		addListener(pListener, "run");
	}
	
	/**
	 * Adds a listener.
	 * If the listener is already added, it is remove before, and added at the end.
	 * 
	 * @param pListener the listener.
	 * @param pIndex the index to add
	 */
	public void addListener(IRunnable pListener, int pIndex)
	{
		addListener(pListener, "run", pIndex);
	}
	
	/**
	 * Adds a listener at a given position.
	 * If the Listener is already added, it is removed before, and added at the new position.
	 * 
	 * @param pListener the listener object.
	 * @param pMethodName the method name.
	 * @param pIndex the index to add
	 */
	public synchronized void addListener(Object pListener, String pMethodName, int pIndex)
	{
		if (listeners == null)
		{
			listeners = new ArrayUtil<ListenerHandler>();
		}
		
		ListenerHandler handler = new ListenerHandler(pListener, pMethodName);
		
		int index = listeners.indexOf(handler);
		
		if (index >= 0)
		{
			removeHandler(index);
			
			if (pIndex > index)
			{
				pIndex--;
			}
		}
		
		addHandler(pIndex, handler);
	}	
	
	/**
	 * Adds a listener handler to the listener list.
	 * 
	 * @param pIndex the index in the list
	 * @param pHandler the handler to add
	 */
	protected void addHandler(int pIndex, ListenerHandler pHandler)
	{
        if (pIndex < 0)
        {
            listeners.add(pHandler);
        }
        else
        {
            listeners.add(pIndex, pHandler);
        }
	}
	
	/**
	 * Removes the handler with given index.
	 * 
	 * @param pIndex the index
	 * @return the removed handler
	 */
	protected ListenerHandler removeHandler(int pIndex)
	{
	    return listeners.remove(pIndex);
	}
	
	/**
	 * Removes the given handler.
	 * 
	 * @param pObject the handler
	 * @return <code>true</code> if object was removed, <code>false</code> otherwise
	 */
	private boolean removeHandler(Object pObject)
	{
	    int iIndex = listeners.indexOf(pObject);

	    if (iIndex >= 0)
	    {
	        return removeHandler(iIndex) != null;
	    }
	    else
	    {
	        return false;
	    }
	}
	
	/**
	 * Removes the listener at the position.
	 * 
	 * @param pIndex the position.
	 */
	public synchronized void removeListener(int pIndex)
	{
		if (listeners != null)
		{
		    removeHandler(pIndex);
			
			if (listeners.isEmpty())
			{
				listeners = null;
			}
		}
	}
	
	/**
	 * Removes all listener methods added with the given object, or the listener, if an interface was added.
	 * 
	 * @param pListener the listener.
	 */
	public synchronized void removeListener(Object pListener)
	{
		if (listeners != null)
		{
			while (removeHandler(pListener))
			{
				// Do Nothing
			}
			
			if (listeners.isEmpty())
			{
				listeners = null;
			}
		}
	}
	
	/**
	 * Removes the listener added with the method.
	 * 
	 * @param pListener the listener object.
	 * @param pMethodName the method name.
	 */
	public synchronized void removeListener(Object pListener, String pMethodName)
	{
		if (listeners != null)
		{
		    removeHandler(new ListenerHandler(pListener, pMethodName));
			
			if (listeners.isEmpty())
			{
				listeners = null;
			}
		}
	}
	
	/**
	 * Removes all known listeners.
	 */
	public synchronized void removeAllListeners()
	{
		if (listeners != null)
		{
			listeners = null;
		}
	}

	/**
	 * Gets the count of listeners.
	 * 
	 * @return the count of listeners.
	 */
	public synchronized int getListenerCount()
	{
		if (listeners == null)
		{
			return 0;
		}
		else
		{
			return listeners.size();
		}
	}
	
	/**
	 * Gets the listener at the position.
	 * 
	 * @param pIndex the position.
	 * @return the listener.
	 */
	public synchronized L getListener(int pIndex)
	{
		if (listeners == null)
		{
			throw new IndexOutOfBoundsException("Index: " + pIndex + ", Size: 0");
		}
		else
		{
			return listeners.get(pIndex).listenerInterface;
		}
	}
	
	/**
	 * Gets all listeners.
	 * 
	 * @return all listeners.
	 */
	public synchronized L[] getListeners()
	{
		if (listeners == null)
		{
			return (L[])Array.newInstance(listenerType, 0);		
		}
		else
		{
			L[] result = (L[])Array.newInstance(listenerType, listeners.size());
			for (int i = listeners.size() - 1; i >= 0; i--)
			{
				result[i] = listeners.get(i).listenerInterface;
			}
			
			return result;
		}
	}
	
	/**
	 * Sets the default listener.
	 * 
	 * @param pListener the listener.
	 */
	public void setDefaultListener(L pListener)
	{
		if (pListener == null)
		{
			defaultListener = null;
		}
		else if (defaultListener == null || !defaultListener.equals(pListener))
		{
			defaultListener = new ListenerHandler(pListener);
		}
	}
	
	/**
	 * Sets the default listener.
	 * 
	 * @param pListener the listener object.
	 * @param pMethodName the method name.
	 */
	public void setDefaultListener(Object pListener, String pMethodName)
	{
		if (pListener == null)
		{
			defaultListener = null;
		}
		else if (defaultListener == null || !defaultListener.equals(pListener))
		{
			defaultListener = new ListenerHandler(pListener, pMethodName);
		}
	}
	
	/**
	 * Gets the default listener.
	 * 
	 * @return the default listener.
	 */
	public L getDefaultListener()
	{
		if (defaultListener == null)
		{
			return null;
		}
		else
		{
			return defaultListener.listenerInterface;
		}
	}

    /**
     * Adds an internal listener.
     * If the Listener is already added, it is removed before, and added at the end.
     * 
     * @param pListener the listener.
     */
    public void addInternalListener(L pListener)
    {
        addInternalListener(pListener, -1);
    }
    
    /**
     * Adds an internal listener at a given index.
     * If the Listener is already added, it is removed before, and added at the new position.
     * 
     * @param pListener the listener.
     * @param pIndex the index to add.
     */
    public synchronized void addInternalListener(L pListener, int pIndex)
    {
        if (internalListeners == null)
        {
            internalListeners = new ArrayUtil<ListenerHandler>();
        }
        
        int index = internalListeners.indexOf(pListener);
        
        if (index >= 0)
        {
            removeInternalHandler(index);
            
            if (pIndex > index)
            {
                pIndex--;
            }
        }
        
        ListenerHandler handler = null;
        
        if (Proxy.isProxyClass(pListener.getClass()))
        {
            handler = (ListenerHandler)Proxy.getInvocationHandler(pListener);
        }
        else if (pListener instanceof AbstractListenerProxy)
        {
            Object oHandler = ((AbstractListenerProxy)pListener).getHandler();
            
            if (oHandler instanceof EventHandler.ListenerHandler)
            {
                handler = (ListenerHandler)oHandler;
            }
        }
        
        if (handler == null)
        {
            handler = new ListenerHandler(pListener);
        }
        
        addInternalHandler(pIndex, handler);
    }
    
    /**
     * Adds an internal listener.
     * If the Listener is already added, it is removed before, and added at the end.
     * 
     * @param pListener the listener object.
     * @param pMethodName the method name.
     */
    public void addInternalListener(Object pListener, String pMethodName)
    {
        addInternalListener(pListener, pMethodName, -1);
    }
    
    /**
     * Adds an internal listener.
     * If the listener is already added, it is remove before, and added at the end.
     * 
     * @param pListener the listener.
     */
    public void addInternalListener(IRunnable pListener)
    {
        addInternalListener(pListener, "run");
    }
    
    /**
     * Adds an internal listener.
     * If the listener is already added, it is remove before, and added at the end.
     * 
     * @param pListener the listener.
     * @param pIndex the index to add
     */
    public void addInternalListener(IRunnable pListener, int pIndex)
    {
        addInternalListener(pListener, "run", pIndex);
    }
    
    /**
     * Adds an internal listener at a given position.
     * If the Listener is already added, it is removed before, and added at the new position.
     * 
     * @param pListener the listener object.
     * @param pMethodName the method name.
     * @param pIndex the index to add
     */
    public synchronized void addInternalListener(Object pListener, String pMethodName, int pIndex)
    {
        if (internalListeners == null)
        {
            internalListeners = new ArrayUtil<ListenerHandler>();
        }
        
        ListenerHandler handler = new ListenerHandler(pListener, pMethodName);
        
        int index = internalListeners.indexOf(handler);
        
        if (index >= 0)
        {
            removeInternalHandler(index);
            
            if (pIndex > index)
            {
                pIndex--;
            }
        }
        
        addInternalHandler(pIndex, handler);
    }   
    
    /**
     * Adds an internal listener handler to the listener list.
     * 
     * @param pIndex the index in the list
     * @param pHandler the handler to add
     */
    protected void addInternalHandler(int pIndex, ListenerHandler pHandler)
    {
        if (pIndex < 0)
        {
            internalListeners.add(pHandler);
        }
        else
        {
            internalListeners.add(pIndex, pHandler);
        }
    }
    
    /**
     * Removes the internal listener handler with given index.
     * 
     * @param pIndex the index
     * @return the removed handler
     */
    protected ListenerHandler removeInternalHandler(int pIndex)
    {
        return internalListeners.remove(pIndex);
    }
    
    /**
     * Removes the given internal listener handler.
     * 
     * @param pObject the handler
     * @return <code>true</code> if object was removed, <code>false</code> otherwise
     */
    private boolean removeInternalHandler(Object pObject)
    {
        int iIndex = internalListeners.indexOf(pObject);

        if (iIndex >= 0)
        {
            return removeInternalHandler(iIndex) != null;
        }
        else
        {
            return false;
        }
    }
    
    /**
     * Removes the internal listener at the position.
     * 
     * @param pIndex the position.
     */
    public synchronized void removeInternalListener(int pIndex)
    {
        if (internalListeners != null)
        {
            removeInternalHandler(pIndex);
            
            if (internalListeners.isEmpty())
            {
                internalListeners = null;
            }
        }
    }
    
    /**
     * Removes all internal listener methods added with the given object, or the listener, if an interface was added.
     * 
     * @param pListener the listener.
     */
    public synchronized void removeInternalListener(Object pListener)
    {
        if (internalListeners != null)
        {
            while (removeInternalHandler(pListener))
            {
                // Do Nothing
            }
            
            if (internalListeners.isEmpty())
            {
                internalListeners = null;
            }
        }
    }
    
    /**
     * Removes the internal listener added with the method.
     * 
     * @param pListener the listener object.
     * @param pMethodName the method name.
     */
    public synchronized void removeInternalListener(Object pListener, String pMethodName)
    {
        if (internalListeners != null)
        {
            removeInternalHandler(new ListenerHandler(pListener, pMethodName));
            
            if (internalListeners.isEmpty())
            {
                internalListeners = null;
            }
        }
    }
    
    /**
     * Removes all known internal listeners.
     */
    public synchronized void removeAllInternalListeners()
    {
        if (internalListeners != null)
        {
            internalListeners = null;
        }
    }

    /**
     * Gets the count of internal listeners.
     * 
     * @return the count of internal listeners.
     */
    public synchronized int getInternalListenerCount()
    {
        if (internalListeners == null)
        {
            return 0;
        }
        else
        {
            return internalListeners.size();
        }
    }
    
    /**
     * Gets the internal listener at the position.
     * 
     * @param pIndex the position.
     * @return the internal listener.
     */
    public synchronized L getInternalListener(int pIndex)
    {
        if (internalListeners == null)
        {
            throw new IndexOutOfBoundsException("Index: " + pIndex + ", Size: 0");
        }
        else
        {
            return internalListeners.get(pIndex).listenerInterface;
        }
    }
    
    /**
     * Gets all internal listeners.
     * 
     * @return all internal listeners.
     */
    public synchronized L[] getInternalListeners()
    {
        if (internalListeners == null)
        {
            return (L[])Array.newInstance(listenerType, 0);     
        }
        else
        {
            L[] result = (L[])Array.newInstance(listenerType, internalListeners.size());
            for (int i = internalListeners.size() - 1; i >= 0; i--)
            {
                result[i] = internalListeners.get(i).listenerInterface;
            }
            
            return result;
        }
    }
    
    /**
     * Returns true, if dispatch will invoke any listener.
     * This is true if:
     * <ul>
     *   <li>isDispatchEventEnabled is true</li>
     *   <li>at least one listener is added or a default listener is set</li>
     *   <li>it is not called recursive</li>
     * </ul>
     * 
     * @return true, if dispatch will invoke any listener.
     */
    public boolean isDispatchable()
    {
       return isDispatchableIntern(true); 
    }
    
	/**
	 * Returns true, if dispatch will invoke any listener.
	 * This is true if:
	 * <ul>
	 *   <li>isDispatchEventEnabled is true</li>
	 *   <li>at least one listener is added or a default listener is set</li>
	 *   <li>it is not called recursive</li>
	 * </ul>
     * An external enabled flag allowes to disable events, but ensures that internal listeners are still executed.
     * 
     * @param pExternalEnabled an external enabled flag.
	 * @return true, if dispatch will invoke any listener.
	 */
	protected synchronized boolean isDispatchableIntern(boolean pExternalEnabled)
	{
		return (bFirstDispatch && bDispatchEvents && pExternalEnabled && (listeners != null || defaultListener != null)) 
		        || (bFirstInternalDispatch && internalListeners != null);
	}

    /**
     * Returns true, if dispatch will invoke any listener.
     * This is true if:
     * <ul>
     *   <li>isDispatchEventEnabled is true</li>
     *   <li>at least one listener is added or a default listener is set</li>
     *   <li>it is not called recursive</li>
     * </ul>
     * 
     * @param pEventHandler the event handler to check, if it is dispatchable.
     * @return true, if dispatch will invoke any listener.
     */
    public static boolean isDispatchable(EventHandler pEventHandler)
    {
        return isDispatchableIntern(true, pEventHandler);
    }
    
	/**
	 * Returns true, if dispatch will invoke any listener.
	 * This is true if:
	 * <ul>
	 *   <li>isDispatchEventEnabled is true</li>
	 *   <li>at least one listener is added or a default listener is set</li>
	 *   <li>it is not called recursive</li>
	 * </ul>
     * An external enabled flag allowes to disable events, but ensures that internal listeners are still executed.
     * 
     * @param pExternalEnabled an external enabled flag.
	 * @param pEventHandler the event handler to check, if it is dispatchable.
	 * @return true, if dispatch will invoke any listener.
	 */
	protected static boolean isDispatchableIntern(boolean pExternalEnabled, EventHandler pEventHandler)
	{
		return pEventHandler != null && pEventHandler.isDispatchableIntern(pExternalEnabled);
	}

    /**
     * Dispatches the given events to all listeners.
     * 
     * @param pEventParameter the event parameter.
     * @return the return value of the deaultListener, if it is called, or null if dispatching is disabled or 
     *         no listeners were called
     * @throws Throwable if an exception occurs.
     */
    public Object dispatchEvent(Object... pEventParameter) throws Throwable
    {
        return dispatchEventIntern(true, pEventParameter);
    }
    
	/**
	 * Dispatches the given events to all listeners.
	 * An external enabled flag allowes to disable events, but ensures that internal listeners are still executed.
	 * 
     * @param pExternalEnabled an external enabled flag.
	 * @param pEventParameter the event parameter.
	 * @return the return value of the deaultListener, if it is called, or null if dispatching is disabled or 
	 *         no listeners were called
	 * @throws Throwable if an exception occurs.
	 */
	protected Object dispatchEventIntern(boolean pExternalEnabled, Object... pEventParameter) throws Throwable
	{
	    Object result = null;
	    
		if (isDispatchableIntern(pExternalEnabled))
		{
            Throwable throwable = null;

            boolean isDispatchingEvent = bFirstDispatch && bDispatchEvents && pExternalEnabled;
            if (isDispatchingEvent)
            {
                bFirstDispatch = false;
                try
                {
                    List<ListenerHandler> liHandlers;
                    synchronized (this)
                    {
                        liHandlers = listeners == null ? null : new ArrayUtil<ListenerHandler>(listeners); //copy, because it is possible that a listener removes itself.
                    }
    
//                  setLastDispatchedObject(); // ??? This clears the wrefLastDispatchedObject, which means that in event the last one cannot be queried ???
                    if (pEventParameter != null && pEventParameter.length > 0)
                    {
                        wrefCurrentDispatchObject = new WeakReference<Object>(pEventParameter[0]);
                    }
                    else
                    {
                        wrefCurrentDispatchObject = null; // Ensure correct current, not one of another event.
                    }

                    if (liHandlers != null)
                    {
                        for (ListenerHandler handler : liHandlers)
                        {
                            handler.dispatchEvent(pEventParameter);
                        }
                    }
                    else if (defaultListener != null)
                    {
                        result = defaultListener.dispatchEvent(pEventParameter);
                    }
                }
                catch (Throwable pThrowable)
                {
                    logger.debug(pThrowable);
                    
                    throwable = pThrowable;
                }
                finally
                {
                    bFirstDispatch = true;
                }
            }
            if (bFirstInternalDispatch)
            {
                bFirstInternalDispatch = false;
                try
                {
                    List<ListenerHandler> liInternalHandlers;
    	            synchronized (this)
    	            {
    	                liInternalHandlers = internalListeners == null ? null : new ArrayUtil<ListenerHandler>(internalListeners);
    	            }

	                if (liInternalHandlers != null)
                    {
                        for (ListenerHandler handler : liInternalHandlers)
                        {
                            handler.dispatchEvent(pEventParameter);
                        }
                    }
		        }
                catch (Throwable pThrowable)
                {
                    logger.error(pThrowable); // internal listeners will only be logged, and not thrown!
                }
                finally
                {
                    bFirstInternalDispatch = true;
                }
		    }
            
            if (isDispatchingEvent) // only handle current and last dispatched object for normal events, internal events will occur more often. 
            {
                setLastDispatchedObject();
            }
            if (throwable != null)
            {
                throw getWrappedExceptionAllowSilent(throwable);
            }
		}
		
		return result;
	}
	
	/**
	 * Creates a new listener interface for calling the given method for the given object.
	 * 
	 * @param pListener the object.
	 * @param pMethodName the method.
	 * @return the Interface.
	 */
	public L createListener(Object pListener, String pMethodName)
	{
		return new ListenerHandler(pListener, pMethodName).listenerInterface;
	}

	/**
	 * Gets whether event dispatching is enabled.
	 * 
	 * @return <code>true</code> if event dispatching is enabled, <code>false</code> if it's disabled
	 */
	public boolean isDispatchEventsEnabled()
	{
		return bDispatchEvents;
	}
	
	/**
	 * Sets whether event dispatching is en- or disabled.
	 * 
	 * @param pEnabled <code>true</code> to enable dispatching, <code>false</code> to ignore dispatching
	 */
	public void setDispatchEventsEnabled(boolean pEnabled)
	{
		bDispatchEvents = pEnabled;
	}
	
	/**
	 * Sets the current object as last dispatched objects.
	 */
	private static void setLastDispatchedObject()
	{
		if (wrefCurrentDispatchObject != null)
		{
			wrefLastDispatchedObject = wrefCurrentDispatchObject;
			
			wrefCurrentDispatchObject = null;
		}
	}
	
	/**
	 * Gets the last dispatched object.
	 * 
	 * @return the object
	 */
	public static Object getLastDispatchedObject()
	{
		if (wrefLastDispatchedObject != null)
		{
			return wrefLastDispatchedObject.get();
		}
		
		return null;
	}
	
	/**
	 * Gets the current dispatch object.
	 * 
	 * @return the object or <code>null</code> if no object was found
	 */
	public static Object getCurrentDispatchObject()
	{
		if (wrefCurrentDispatchObject != null)
		{
			return wrefCurrentDispatchObject.get();
		}
		
		return null;
	}
	
	/**
	 * Gets the cause of an exception that is a wrapper exception, like {@link InvocationTargetException}.
	 * 
	 * @param pCause the wrapper exception
	 * @return the wrapped(inner) exception
	 */
	public static Throwable getWrappedException(Throwable pCause)
	{
		Throwable th = pCause;
		
		while ((th instanceof InvocationTargetException
			    || th instanceof UndeclaredThrowableException
			    || th instanceof SilentAbortException)
			    && th.getCause() != null)
		{
			th = th.getCause();
		}
		
		return th;
	}
	
	/**
	 * Gets the cause of an exception that is a wrapper exception, like {@link InvocationTargetException}.
	 * This methods doesn't unwrap exception that were wrapped with {@link SilentAbortException}.
	 * 
	 * @param pCause the wrapper exception
	 * @return the wrapped(inner) exception
	 */
	public static Throwable getWrappedExceptionAllowSilent(Throwable pCause)
	{
		Throwable th = pCause;
		
		while ((th instanceof InvocationTargetException
				|| th instanceof UndeclaredThrowableException) 
				&& th.getCause() != null)
		{
			th = th.getCause();
		}
		
		return th;
	}
	
    /**
     * Extracts the invoked method name of the given listener, if possible.
     * If it is a lambda or object with string method, the called method is returned.
     * Otherwise this listener method name is returned.
     * 
     * @param pListener the listener
     * @return the invoked method name
     */
    public String extractMethodName(Object pListener)
    {
        String method = extractInvokedMethodName(pListener);
        
        if (method == null)
        {
            return listenerMethod.getName();
        }
        
        return method;
    }
    
	/**
	 * Extracts the invoked method name of the given listener, if possible.
	 * If it is a lambda or object with string method, the called method is returned.
	 * Otherwise null is returned.
	 * 
	 * @param pListener the listener
	 * @return the invoked method name
	 */
	public static String extractInvokedMethodName(Object pListener)
	{
	    String method = extractInvokedLambdaMethodName(pListener);
	    
	    if (method == null)
	    {
	        String listenerString = pListener.toString();
        
            if (!listenerString.contains("@"))
            {
                int arrowIndex = listenerString.indexOf(" -> ");
                
                if (arrowIndex >= 0)
                {
                    int bracketIndex = listenerString.indexOf('(', arrowIndex + 4);
                    if (bracketIndex >= 0)
                    {
                        int methodStart = listenerString.lastIndexOf('.', bracketIndex);
                        
                        if (methodStart >= 0)
                        {
                            return listenerString.substring(methodStart + 1, bracketIndex);
                        }
                    }
                }
            }
        }
        
        return method;
	}

    /**
     * Extracts the invoked method name of the given listener, if possible.
     * If it is a lambda or object with string method, the called method is returned.
     * Otherwise null is returned.
     * 
     * @param pListener the listener
     * @return the invoked method name
     */
    public static String extractInvokedLambdaMethodName(Object pListener)
    {
        String listenerString = pListener.getClass().getName();
        
        if (listenerString != null)
        {
            if (listenerString.contains("$$Lambda$"))
            {
                try
                {
                    // Maybe a way to analyse.
//                    Method m = lambda.getClass().getDeclaredMethod("writeReplace");
//                    m.setAccessible(true);
//                    SerializedLambda sl=(SerializedLambda)m.invoke(lambda);
//                    return sl.getImplMethodName();
                    
                    ByteArrayOutputStream baOut = new ByteArrayOutputStream();
                    ObjectOutputStream oOut = new ObjectOutputStream(baOut);
                    oOut.writeObject(pListener);
                    oOut.close();
                    
                    byte[] clazzBytes = baOut.toByteArray();

                    String classCheck = extractStringFromClass(clazzBytes, 6);
                    if ("java.lang.invoke.SerializedLambda".equals(classCheck))
                    {
                        int methodReference = findMethodReference(clazzBytes);
                        if (methodReference >= 0)
                        {
                            String methodClazz = extractStringFromClass(clazzBytes, methodReference);
                            String method = extractStringFromClass(clazzBytes, methodReference + methodClazz.length() + 3);
                            
                            return method;
                        }
                    }
                }
                catch (Throwable ex)
                {
                    logger.debug(ex);
                }                   
            }
        }
        
        return null;
    }

    /**
     * Extracts a String from byte code.
     * @param pBytes the byte code
     * @param pPos the position
     * @return the String
     * @throws Throwable if it fails.
     */
    private static String extractStringFromClass(byte[] pBytes, int pPos) throws Throwable
    {
        int len = (pBytes[pPos] & 0xFF) * 0x100 + (pBytes[pPos + 1] & 0xFF);
        
        return new String(pBytes, pPos + 2, len, "UTF-8");
    }
    
    /**
     * Finds the method reference inside the lambda class.
     * @param pBytes the byte code
     * @return the index.
     */
    private static int findMethodReference(byte[] pBytes)
    {
        int index = ArrayUtil.lastIndexOf(pBytes, 0x29);
        if (index >= 0 && pBytes[index + 1] == 0x56 && pBytes[index + 2] == 0x74)
        {
            return index + 3;
        }
        
        return -1;
    }
	
	//****************************************************************
	// Subclass definition
	//****************************************************************

	/**
	 * Generic Listener that calls reflective the given method.
	 * The Method is searched in the order:
	 * <code>
	 *   1) public void methodName(E pEvent);
	 *   2) public void methodName();
	 * </code>
	 * If the method does not exist, a NoSuchMethodException is thrown.
	 *   
	 * @author Martin Handsteiner
	 */
	protected class ListenerHandler implements InvocationHandler
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/** The listener interface. */
		private L listenerInterface;
		
		/** The listener. */
		private Object listener;
		/** The listener. */
		private Method method;
		/** True, if the method should be called with event. */
		private boolean callWithParameter;
		/** True, if the method should be called with reflective. */
		private boolean callReflective;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Constructs a new ListenerHandler with an listener interface.
		 * @param pListenerInterface the listener interface.
		 */
		public ListenerHandler(L pListenerInterface)
		{
			listenerInterface = pListenerInterface;
			listener = null;
		}
		
		/**
		 * Constructs a new ListenerHandler with a proxy listener interface.
		 * @param pListener the listener.
		 * @param pMethodName the method name.
		 */
		public ListenerHandler(Object pListener, String pMethodName)
		{
			Class listenerClass = pListener.getClass();
			Class[] paramTypes;
			if (parameterTypes == null)
			{
				paramTypes = listenerMethod.getParameterTypes();
			}
			else
			{
				paramTypes = parameterTypes;
			}
			
			// We need to check explicitly for IRunnable here, because Lambdas
			// are a private class which only implement IRunnable, which makes
			// it impossible to invoke the run method based on the implementing
			// class information. We need to get the method information instead
			// directly from the IRunnable interface to be able to invoke it.
			//
			// Short: You can't invoke methods of private classes, lambdas are
			// private classes.
			if (pListener instanceof IRunnable && "run".equals(pMethodName))
			{
				try
				{
					method = IRunnable.class.getMethod("run");
				}
				catch (NoSuchMethodException e)
				{
					// Not possible.
				}
				
				callWithParameter = false;
				callReflective = false;
			}
			else
			{
				try
				{
					method = listenerClass.getMethod(pMethodName, paramTypes);
					callWithParameter = true;
					callReflective = false;
				}
				catch (NoSuchMethodException pNoSuchMethodException1)
				{
					try
					{
						method = Reflective.getCompatibleMethod(listenerClass, pMethodName, paramTypes);
						callWithParameter = true;
						callReflective = true;
					}
					catch (NoSuchMethodException pNoSuchMethodException2)
					{
						try
						{
							method = listenerClass.getMethod(pMethodName);
							callWithParameter = false;
							callReflective = false;
						}
						catch (NoSuchMethodException pNoSuchMethodException3)
						{
							throw new IllegalArgumentException("Method " + pMethodName + " not found in class " + listenerClass.getName() + "!");
						}
					}
				}
			}

			listener = pListener;
			
			try
			{
				listenerInterface = (L)ProxyFactory.createListener(pListener.getClass().getClassLoader(), listenerType, this);
			}
			catch (Throwable th)
			{
				logger.info("No static proxy available for '", listenerType, "'");
				
				listenerInterface = (L)Proxy.newProxyInstance(pListener.getClass().getClassLoader(), 
															  new Class[] {listenerType},
															  this);
			}
		}

		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Interface implementation
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * {@inheritDoc}
		 */
		public Object invoke(Object pProxy, Method pMethod, Object[] pArguments) throws Throwable
		{
            String sMethodName = pMethod.getName();

            try
			{
				if (sMethodName == listenerMethod.getName() || sMethodName.equals(listenerMethod.getName()))
				{
					if (callWithParameter)
					{
						return method.invoke(listener, pArguments);
					}
					else
					{
						return method.invoke(listener);
					}
				}
				else if ("hashCode" == sMethodName || "hashCode".equals(sMethodName))
				{
					return Integer.valueOf(listener.hashCode());
				}
				else if ("equals" == sMethodName || "equals".equals(sMethodName))
				{
					return Boolean.valueOf(listenerInterface == pArguments[0] || listener.equals(pArguments[0]));
				}
				else if ("toString" == sMethodName || "toString".equals(sMethodName))
				{
				    if (listener instanceof IRunnable && "run".equals(method.getName()))
		            {
				        String lambdaMethodName = extractInvokedLambdaMethodName(listener);
				        
				        if (lambdaMethodName == null)
				        {
	                        return listener.toString();
				        }
				        else
				        {
    				        String className = listener.getClass().getName();
    				        int classNameEnd = className.indexOf("$$Lambda$");
    				        if (classNameEnd >= 0)
    				        {
    				            className = className.substring(0, classNameEnd);
    				        }
    				        
    				        return EventHandler.this.listenerMethod.toString() + " -> public void " + className + "." + lambdaMethodName + "()";
				        }
		            }
				    else
				    {
				        return EventHandler.this.listenerMethod.toString() + " -> " + method.toString();
				    }
				}
			}
			catch (Throwable pThrowable)
			{
				logger.debug(pThrowable);

				throw getWrappedException(pThrowable);
			}
			
			throw new UnsupportedOperationException("The call of method " + sMethodName + " is not supported!");
		}

		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Overwritten methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int hashCode()
		{
			if (listener == null)
			{
				return listenerInterface.hashCode();
			}
			else
			{
				return listener.hashCode();
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean equals(Object pObject)
		{
			if (pObject != null && pObject.getClass() == ListenerHandler.class)
			{
				ListenerHandler handler = (ListenerHandler)pObject;
				
				return listener == handler.listener && method.equals(handler.method);
			}
			else
			{
				return listenerInterface == pObject || (listener != null && listener == pObject);
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString()
		{
		    if (listener == null)
			{
				return listenerInterface.toString();
			}
			else
			{
				return listener.toString();
			}
		}

		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// User-defined methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Invokes the listener or the proxy interface.
		 * 
		 * @param pArguments the Arguments.
		 * @return the result.
		 * @throws Throwable if an error occurs.
		 */
		public Object dispatchEvent(Object... pArguments) throws Throwable
		{
			if (listener == null)
			{
				if (callListenerMethodReflective)
				{
					return Reflective.call(listenerInterface, listenerMethod, pArguments);
				}
				else if (listenerMethodHasOneParameter && pArguments == null)
				{
					return listenerMethod.invoke(listenerInterface, NULL_PARAMETER);
				}
				else
				{
					return listenerMethod.invoke(listenerInterface, pArguments);
				}
			}
			else
			{
				if (callWithParameter)
				{
					if (callReflective)
					{
						return Reflective.call(listener, method, pArguments);
					}
					else
					{
						return method.invoke(listener, pArguments);
					}
				}
				else
				{
					return method.invoke(listener);
				}
			}
		}

	}	// ListenerHandler
	
}	// EventHandler
