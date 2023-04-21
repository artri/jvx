/*
 * Copyright 2015 SIB Visions GmbH
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
 * 23.07.2015 - [JR] - creation
 */
package com.sibvisions.util;

import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;

/**
 * The <code>ObjectCacheInstance</code> is a utility class to cache/store objects for a
 * period of time. The cache handles the object expiration and the access to the
 * cached objects. The cache stores an object with a unique access key. With this
 * access key it's possible to access the object from the store.
 * 
 * @author René Jahn
 */
public class ObjectCacheInstance
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the current timeout-check interval. */
	private static final long CHECK_DELAY = 30000L;
	
    /** the object cache. */
	private Hashtable<Object, Element> htStore = new Hashtable<Object, Element>();
	
	/** the timeout-check thread. */
	private Thread thCheckTimeout = null;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>ObjectCacheInstance</code>.
	 */
	public ObjectCacheInstance()
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a random acces key.
	 * 
	 * @return the access key                 
	 */
	public static Object createKey()
	{
		return UUID.randomUUID().toString();
	}
	
	/**
	 * Puts an object to the cache, with a timeout for expiration.
	 * 
	 * @param pObject the cachable object
	 * @param pTimeout the timeout (in millis) for the object. After this time, the
	 *                 object will be removed from the cache
	 * @return the access key for the object                 
	 */
	public Object put(Object pObject, long pTimeout)
	{
		return put(pObject, pTimeout, (IInvalidator)null);
	}
	
	/**
	 * Puts an object to the cache, with a timeout for expiration and an additional invalidator.
	 * 
	 * @param pObject the cachable object
	 * @param pTimeout the timeout (in millis) for the object. After this time, the
	 *                 object will be removed from the cache
	 * @param pInvalidator the {@link IInvalidator} to use                
	 * @return the access key for the object                 
	 */
	public Object put(Object pObject, long pTimeout, IInvalidator pInvalidator)
	{
		if (pObject == null)
		{
			throw new NullPointerException("Object is null!");
		}
		
		Object sKey = createKey();
		
		put(sKey, pObject, pTimeout, pInvalidator);
		
		return sKey;
	}

	/**
	 * Puts a validatable object to the cache.
	 * 
	 * @param pObject the cachable object
	 * @return the access key for the object                 
	 */
	public Object put(IValidatable pObject)
	{
		return put(pObject, (IInvalidator)null);
	}
	
	/**
	 * Puts a validatable object to the cache with an additional invalidator.
	 * 
	 * @param pObject the cachable object
	 * @param pInvalidator the {@link IInvalidator} to use
	 * @return the access key for the object                 
	 */
	public Object put(IValidatable pObject, IInvalidator pInvalidator)
	{
		if (pObject == null)
		{
			throw new NullPointerException("Object is null!");
		}
		
		Object sKey = createKey();
		
		put(sKey, pObject, pInvalidator);
		
		return sKey;
	}
	
	/**
	 * Puts an object to the cache, with a specific key and timeout for expiration.
	 * 
	 * @param pKey the key for accessing the object
	 * @param pObject the cachable object
	 * @param pTimeout the timeout (in millis) for the object. After this time, the
	 *                 object will be removed from the cache
	 * @return the previous cached object or <code>null</code> if there was no previous object
	 *         or the object is expired               
	 */
	public Object put(Object pKey, Object pObject, long pTimeout)
	{
		return put(pKey, pObject, pTimeout, (IInvalidator)null);
	}

	/**
	 * Puts an object to the cache, with a specific key and timeout for expiration and an 
	 * additional invalidator.
	 * 
	 * @param pKey the key for accessing the object
	 * @param pObject the cachable object
	 * @param pTimeout the timeout (in millis) for the object. After this time, the
	 *                 object will be removed from the cache
	 * @param pInvalidator the {@link IInvalidator} to use                 
	 * @return the previous cached object or <code>null</code> if there was no previous object
	 *         or the object is expired               
	 */
	public Object put(Object pKey, Object pObject, long pTimeout, IInvalidator pInvalidator)
	{
		if (pObject == null)
		{
			return htStore.remove(pKey);
		}
		else
		{
			return put(pKey, new Element(pObject, pTimeout, pInvalidator));
		}
	}

	/**
	 * Puts a validatable object to the cache, with a specific key.
	 * 
	 * @param pKey the key for accessing the object
	 * @param pObject the cachable object
	 * @return the previous cached object or <code>null</code> if there was no previous object
	 *         or the object is expired               
	 */
	public Object put(Object pKey, IValidatable pObject)
	{
		return put(pKey, pObject, (IInvalidator)null);
	}

	/**
	 * Puts a validatable object to the cache, with a specific key and an additional invalidator.
	 * 
	 * @param pKey the key for accessing the object
	 * @param pObject the cachable object
	 * @param pInvalidator the {@link IInvalidator} to use
	 * @return the previous cached object or <code>null</code> if there was no previous object
	 *         or the object is expired               
	 */
	public Object put(Object pKey, IValidatable pObject, IInvalidator pInvalidator)
	{
		if (pObject == null)
		{
			return htStore.remove(pKey);
		}
		else
		{
			return put(pKey, new Element(pObject, pInvalidator));
		}
	}

	/**
	 * Gets an object from the cache.
	 *  
	 * @param pKey the access key of the object
	 * @return the object or <code>null</code> if the object was expired or
	 *         the key was not found
	 */
	public Object get(Object pKey)
	{
		if (pKey == null)
		{
			return null;
		}
		
		Element element = htStore.get(pKey);
		
		if (element == null)
		{
			return null;
		}
		else 
		{
			if (element.isValid())
			{
				return element.object;
			}
			else
			{
				htStore.remove(pKey);
				
				return null;
			}
		}
	}
	
	/**
	 * Removes an object from the cache.
	 * 
	 * @param pKey the access key of the object
	 * @return the removed object or <code>null</code> if the object was expired or
	 *         the key was not found
	 */
	public Object remove(Object pKey)
	{
		Element element = htStore.remove(pKey);
		
		if (element != null && element.isValid())
		{
			return element.object;
		}
		
		return null;
	}
	
	/**
	 * Puts the given {@link Element} with the given key into the cache,
	 * returns the old object (if any).
	 * 
	 * @param pKey the key.
	 * @param pElement the {@link Element}.
	 * @return the old object. {@code null} if there is none.
	 */
	private Object put(Object pKey, Element pElement)
	{
		Element elOld = htStore.put(pKey, pElement);
		
		startTimeoutCheck();
		
		if (elOld != null && elOld.isValid())
		{
			return elOld.object;
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * Starts the timeout check.
	 */
	private void startTimeoutCheck()
	{
		if (ThreadHandler.isStopped(thCheckTimeout))
		{
			thCheckTimeout = ThreadHandler.start(new Check());
		}
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************

	/**
	 * The <code>Element</code> encapsulates an exchange object. It includes the
	 * last access time and a timeout. After the timeout, the object is invalid 
	 * and can not be used.
	 * 
	 * @author René Jahn
	 */
	private static final class Element
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/** the creation time. Needed to calculate the expiration. */
		private long creation = System.currentTimeMillis();
		
		/** element timeout. Needed to calculate the expiration. */
		private long timeout;

		/** the cached object. */
		private Object object;
		
		/** the invalidatable. */
		private IInvalidator invalidator;
		
		/** If the cached object can validate itself. */
		private boolean isValidatable;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Creates a new instance of <code>Element</code> with an object
		 * and the desired timeout.
		 * 
		 * @param pObject the cached object
		 * @param pTimeout the timeout (in millis)
		 * @param pInvalidator the invalidatable object.
		 */
		private Element(Object pObject, long pTimeout, IInvalidator pInvalidator)
		{
			object = pObject;
			timeout = pTimeout;
			invalidator = pInvalidator;
		}
		
		/**
		 * Creates a new instance of {@link Element}.
		 * 
		 * @param pObject the validatable object.
		 * @param pInvalidator the invalidatable object.
		 */
		private Element(IValidatable pObject, IInvalidator pInvalidator)
		{
			object = pObject;
			isValidatable = true;
			invalidator = pInvalidator;
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// User-defined methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Determines if the element and the containing object is valid or expired.
		 * The element is expired if the timeout has reached.
		 * 
		 * @return <code>true</code> if the timeout has not reached or the timeout is endless (<= 0);
		 *         <code>false</code> if the timeout has reached.  
		 *         
		 */
		public boolean isValid()
		{
			if (isValidatable)
			{
				// Let the object decide itself if it is valid or not.
				return ((IValidatable)object).isValid();
			}
			else
			{
				return timeout <= 0 || creation + timeout >= System.currentTimeMillis();
			}
		}
		
	}	// Element
	
	/**
	 * The <code>Check</code> class handles the timeout check of cached elements in
	 * the store. If an element is timed out, it will be removed from the store.
	 * 
	 * @author René Jahn
	 */
	private class Check implements Runnable
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Interface implementation
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Checks the valid state of {@link Element}s.
		 */
		public void run()
		{
			try
			{
				while (!ThreadHandler.isStopped(thCheckTimeout))
				{
					Thread.sleep(CHECK_DELAY);
					
				    Hashtable<Object, Element> htClone = (Hashtable<Object, Element>)htStore.clone();

				    Element eRemoved;
				    
					//remove invalid elements from the store
					for (Map.Entry<Object, Element> entry : htClone.entrySet())
					{
						if (!entry.getValue().isValid())
						{
							eRemoved = htStore.remove(entry.getKey());
							
							//maybe already removed in other thread
							if (eRemoved != null)
							{
								if (eRemoved.invalidator != null)
								{
									eRemoved.invalidator.invalidate(eRemoved.object);
								}
								else if (eRemoved.object instanceof IInvalidator)
								{
									((IInvalidator)eRemoved.object).invalidate(eRemoved.object);
								}
							}
						}
					}
					
					htClone = null;
				}
			}
			catch (InterruptedException ie)
			{
				//not a problem, because the thread will be started with the next put action
			}
		}
		
	}	// Check
	
}	// ObjectCache
