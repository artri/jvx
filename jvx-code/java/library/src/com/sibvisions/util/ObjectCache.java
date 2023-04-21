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
 * 10.02.2009 - [JR] - creation
 * 12.02.2009 - [JR] - get: update time (cache object longer)
 * 16.02.2009 - [JR] - get: removed update time (cache objects for the configured timeout and not longer!)
 * 05.04.2009 - [JR] - put: key parameter
 *                   - Object key instead of String key
 * 14.06.2009 - [JR] - get: null key check [BUGFIX]    
 * 02.01.2010 - [JR] - put: null object removes the key    
 * 17.09.2013 - [JR] - removed Memory.gc
 * 04.02.2014 - [JR] - used clone instead of unsynchronized copy creation
 * 23.07.2015 - [JR] - used ObjectCacheInstance as singleton           
 */
package com.sibvisions.util;

/**
 * The <code>ObjectCache</code> is a utility class to cache/store objects for a
 * period of time. The cache handles the object expiration and the access to the
 * cached objects. The cache stores an object with a unique access key. With this
 * access key it's possible to access the object from the store.
 * 
 * @author René Jahn
 */
public final class ObjectCache
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constants
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The constant that represents an infinite timeout. */
	public static final int TIMEOUT_INFINITE = -1;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** the singleton instance. */
    private static ObjectCacheInstance cache = new ObjectCacheInstance();
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Invisible constructor, because the <code>ObjectCache</code> is a utility class.
	 */
	private ObjectCache()
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
		return ObjectCacheInstance.createKey();
	}
	
	/**
	 * Puts an object to the cache, with a timeout for expiration.
	 * 
	 * @param pObject the cachable object
	 * @param pTimeout the timeout (in millis) for the object. After this time, the
	 *                 object will be removed from the cache
	 * @return the access key for the object                 
	 */
	public static Object put(Object pObject, long pTimeout)
	{
	    return cache.put(pObject, pTimeout);
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
	public static Object put(Object pObject, long pTimeout, IInvalidator pInvalidator)
	{
		return cache.put(pObject, pTimeout, pInvalidator);
	}

	/**
	 * Puts a validatable object to the cache.
	 * 
	 * @param pObject the cachable object
	 * @return the access key for the object                 
	 */
	public static Object put(IValidatable pObject)
	{
	    return cache.put(pObject);
	}
	
	/**
	 * Puts a validatable object to the cache with an additional invalidator.
	 * 
	 * @param pObject the cachable object
	 * @param pInvalidator the {@link IInvalidator} to use
	 * @return the access key for the object                 
	 */
	public static Object put(IValidatable pObject, IInvalidator pInvalidator)
	{
	    return cache.put(pObject, pInvalidator);
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
	public static Object put(Object pKey, Object pObject, long pTimeout)
	{
	    return cache.put(pKey, pObject, pTimeout);
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
	public static Object put(Object pKey, Object pObject, long pTimeout, IInvalidator pInvalidator)
	{
	    return cache.put(pKey, pObject, pTimeout, pInvalidator);
	}

	/**
	 * Puts a validatable object to the cache, with a specific key.
	 * 
	 * @param pKey the key for accessing the object
	 * @param pObject the cachable object
	 * @return the previous cached object or <code>null</code> if there was no previous object
	 *         or the object is expired               
	 */
	public static Object put(Object pKey, IValidatable pObject)
	{
	    return cache.put(pKey, pObject);
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
	public static Object put(Object pKey, IValidatable pObject, IInvalidator pInvalidator)
	{
	    return cache.put(pKey, pObject, pInvalidator);
	}

	/**
	 * Gets an object from the cache.
	 *  
	 * @param pKey the access key of the object
	 * @return the object or <code>null</code> if the object was expired or
	 *         the key was not found
	 */
	public static Object get(Object pKey)
	{
	    return cache.get(pKey);
	}
	
	/**
	 * Removes an object from the cache.
	 * 
	 * @param pKey the access key of the object
	 * @return the removed object or <code>null</code> if the object was expired or
	 *         the key was not found
	 */
	public static Object remove(Object pKey)
	{
	    return cache.remove(pKey);
	}
	
}	// ObjectCache
