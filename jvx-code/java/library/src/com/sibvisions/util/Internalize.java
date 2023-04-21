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
 * 01.10.2008 - [HM] - creation
 */
package com.sibvisions.util;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;

/**
 * Intern functionality for all immutable Objects.
 * It's about 2 times faster than String.intern().
 * 
 * @author Martin Handsteiner
 */
public final class Internalize
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the minimum size of the internal object cache. */
	private static final int MIN_SIZE = 63719; //9337; //63719;//63709; //64969; //573473;
	
	/** out of memory detected. */
	private static boolean outOfMemoryDetectedOrMaxSizeReached = false;

	/** rehash counter. */
	private static long nextRehashCheck = -1;

	/** size used. */
	private static int size = 0;

	/** the object cache. */
	private static WeakReference<?>[] cache = new WeakReference<?>[MIN_SIZE];

	/** the maximum size of the internal object cache. */
	private static int maxSize;

	/** True, if internalize is enabled. */
	private static boolean disabled;
	
	/** True, if internalize is enabled. */
	private static long minimalRehashCheckInterval;
	
    /** True, if native string intern should be used. */
    private static boolean useNativeStringIntern;
    
    /** the object cache. */
    private static HashMap<Object, Object> fixedCache = new HashMap<Object, Object>();

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Invisible constructor, because the <code>Internalize</code> class is a utility class.
	 */
	private Internalize()
	{
	}

	static
	{
		try
		{
			disabled = Boolean.valueOf(System.getProperty("com.sibvisions.util.Internalize.disabled")).booleanValue();
		}
		catch (Exception ex)
		{
			disabled = false;
		}

		try
		{
			maxSize = Integer.valueOf(System.getProperty("com.sibvisions.util.Internalize.maxObjectCount")).intValue() * 2;
		}
		catch (Exception ex)
		{
			maxSize = 5200000;  // <= 1 GB Heap
//		    maxSize = 15500000; //  > 2 GB Heap
		}

		try
		{
			minimalRehashCheckInterval = Long.valueOf(System.getProperty("com.sibvisions.util.Internalize.minimalRehashCheckInterval")).intValue();
		}
		catch (Exception ex)
		{
			minimalRehashCheckInterval = 10000;
		}
		
		useNativeStringIntern = "1.7".compareTo(System.getProperty("java.specification.version")) <= 0;
		
		for (int i = Byte.MIN_VALUE; i <= Byte.MAX_VALUE; i++)
		{
		    Byte v = Byte.valueOf((byte)i);
		    fixedCache.put(v,  v);
		}
        for (char i = 0; i <= 127; i++)
        {
            Character v = Character.valueOf(i);
            fixedCache.put(v,  v);
        }
        for (int i = -128; i <= 127; i++)
        {
            Integer v = Integer.valueOf(i);
            fixedCache.put(v,  v);
        }
        for (long i = -128; i <= 127; i++)
        {
            Long v = Long.valueOf(i);
            fixedCache.put(v,  v);
        }
        for (long i = -128L; i <= 127L; i++)
        {
            Long v = Long.valueOf(i);
            fixedCache.put(v,  v);
        }
        for (long i = -16L; i <= 16L; i++)
        {
            BigInteger v = BigInteger.valueOf(i);
            fixedCache.put(v,  v);
        }
        for (long i = -16L; i <= 511L; i++)
        {
            BigDecimal v = BigDecimal.valueOf(i);
            fixedCache.put(v,  v);
        }
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Clears the interned objects. 
	 */
	public static final synchronized void clearCache()
	{
		cache = new WeakReference<?>[MIN_SIZE];
		size = 0;
	}
	
	/**
	 * The minimal interval, the rehashing is done in ms.
	 * Default is 5000ms.
	 * @return minimal interval, the rehashing is done in ms.
	 */
	public static final long getMinimalRehashCheckInterval()
	{
		return minimalRehashCheckInterval;
	}
	
	/**
	 * The minimal interval, the rehashing is done in ms.
	 * Default is 5000ms.
	 * @param pMinimalRehashCheckInterval minimal interval, the rehashing is done in ms.
	 */
	public static final void setMinimalRehashCheckInterval(long pMinimalRehashCheckInterval)
	{
		minimalRehashCheckInterval = pMinimalRehashCheckInterval;
	}
	
	/**
	 * True, if internalize is disabled. Default it is enabled.
	 * @return True, if internalize is disabled.
	 */
	public static final boolean isDisabled()
	{
		return disabled;
	}
	
	/**
	 * True, if internalize is disabled. Default it is enabled.
	 * @param pInternalizeDisabled True, if internalize is disabled.
	 */
	public static final void setDisabled(boolean pInternalizeDisabled)
	{
		if (disabled != pInternalizeDisabled)
		{
			disabled = pInternalizeDisabled;
			
			if (disabled)
			{
				clearCache();
			}
		}
	}
	
	/**
	 * Gets the amount of internalized objects.
	 * @return the amount of internalized objects.
	 */
	public static final int getObjectCount()
	{
		return size;
	}
	
	/**
	 * Gets the maximum amount of internalized objects.
	 * The maximum will never be reached exactly, as the cache size is based on primes.
	 * It is only guaranteed, that the object count will not be greater then max object count. 
	 * 
	 * @return the maximum amount of internalized objects.
	 */
	public static final int getMaxObjectCount()
	{
		return maxSize / 2;
	}
	
	/**
	 * Sets the maximum amount of internalized objects.
	 * The maximum will never be reached exactly, as the cache size is based on primes.
	 * It is only guaranteed, that the object count will not be greater then max object count. 
	 * 
	 * @param pMaximumInternalizedObjectCount the maximum amount of internalized objects.
	 */
	public static final void setMaxObjectCount(int pMaximumInternalizedObjectCount)
	{
		maxSize = pMaximumInternalizedObjectCount * 2;
	}
	
	/**
	 * Create wide spreading hash code.
	 * @param pHashCode the original hash code.
	 * @param pClass the class.
	 * @return wide spreading hash code,
	 */
	private static final int hashCode(int pHashCode, Class pClass)
	{
		if (pClass == BigDecimal.class || pClass == BigInteger.class)
		{
			return pHashCode & 0x7fffffff;
		}
		else
		{
			long val2 = (pHashCode < 0) ? -pHashCode : pHashCode;
			int temp = (int)(((int)(val2 >>> 32)) * 31 +
				      (val2 & 0xffffffffL));
		    return (31 * ((pHashCode < 0) ? -temp : temp)) & 0x7fffffff;
		}
	}
	
	/**
	 * Internalizes any Immutable Object. The function is synchronized.
	 * 
	 * @param <T> any immutable Object.
	 * @param pObject the Object to internalize.
	 * @return the internalized Object.
	 */
	public static final synchronized <T> T intern(T pObject)
	{
		return internNoSync(pObject);
	}
	
	/**
	 * Internalizes any Immutable Object. The function is not synchronized.
	 * It is provided for fast usage, in loops. The implementation has to be like:
	 * <pre>
	 *   synchronized(Internalize.class)
	 *   {
	 *      ... 
	 *        ... = Internalize.internNoSync(...);
	 *      ...
	 *   }
	 * </pre>
	 * 
	 * @param <T> any immutable Object.
	 * @param pObject the Object to internalize.
	 * @return the internalized Object.
	 */
	public static final <T> T internNoSync(T pObject)
	{
		if (disabled || pObject == null)
		{
			return pObject;
		}
		else if (useNativeStringIntern && pObject instanceof String)
		{
		    return (T)((String)pObject).intern();
		}
		else
		{
		    Object fixed = fixedCache.get(pObject);
		    if (fixed != null)
		    {
		        return (T)fixed;
		    }

			int index = hashCode(pObject.hashCode(), pObject.getClass()) % cache.length;
			 
			int newFreeIndex = -1;
			WeakReference<?> result = cache[index];
			Object resultObject = null;
			while (result != null && !pObject.equals(resultObject = result.get()))
			{
				// Implicit fast clearing of unused References.
				// Access of evasion strategy gets faster.
				if (resultObject == null)
				{
					if (newFreeIndex < 0)
					{
						newFreeIndex = index;
					}

					cache[index] = null;
					size--;
				}
				if (++index == cache.length)
				{
					index = 0;
				}
				result = cache[index];
			}
			
			if (result == null) 
			{
				if (outOfMemoryDetectedOrMaxSizeReached)
				{
					// Out of Memory detected.
					// Now Internalize tries to use as less as possible memory. 
					// Rehash need less than 20 ms, To ensure application performance only every two seconds
					// free memory is checked.
					if (System.currentTimeMillis() > nextRehashCheck)
					{
						rehashAndClearUnused();
					}
				}
				else
				{
					// new Free Place in hash array for storing Object
					if (newFreeIndex >= 0)
					{
						cache[newFreeIndex] = new WeakReference<Object>(pObject, null);
					}
					else
					{
						cache[index] = new WeakReference<Object>(pObject, null);
					}

					size++;
					if (size > cache.length / 2)
					{
						rehashAndClearUnused();
					}
				}
				return pObject;
			}
			else
			{
				// new Free Place in hash array for storing Object
				if (newFreeIndex >= 0)
				{
					cache[index] = null;
					cache[newFreeIndex] = result;
				}
				return (T)resultObject;
			}
		}
	}

	/**
	 * Rehash the cache if it is to small or to large.
	 */ 
	private static final void rehashAndClearUnused()
	{
		// First of all drop all empty references
		WeakReference<?> result;
		for (int i = 0; i < cache.length; i++)
		{
			result = cache[i];
			if (result != null && result.get() == null)
			{
				cache[i] = null;
				size--;
			}
		}
		// check new length
		int newLength = cache.length;
		if (size > newLength / 2)
		{
			newLength *= 3;
		}
		else
		{
            outOfMemoryDetectedOrMaxSizeReached = false;
			while (newLength > MIN_SIZE && size < newLength / 8)
			{
				newLength /= 3;
			}
		}
		if (newLength > maxSize || outOfMemoryDetectedOrMaxSizeReached)
		{
			outOfMemoryDetectedOrMaxSizeReached = true;

			nextRehashCheck = System.currentTimeMillis() + minimalRehashCheckInterval;
		}
		else if (newLength != cache.length)
		{
			try
			{
				WeakReference<?>[] newCache = new WeakReference<?>[newLength];

				// do rehash
				for (int i = 0; i < cache.length; i++)
				{
					result = cache[i];
					if (result != null)
					{
						Object object = result.get();
						// Prevent NullPointer caused from threading
						if (object == null)
						{
							size--;
						}
						else
						{
							int index = hashCode(object.hashCode(), object.getClass()) % newCache.length;

							while (newCache[index] != null)
							{
								if (++index == newCache.length)
								{
									index = 0;
								}
							}
							newCache[index] = result;
						}
					}
				}
				cache = newCache;
			}
			catch (OutOfMemoryError pOutOfMemoryError)
			{
			    clearCache(); // Directly release all WeakReferenceObjects, to release as much memory as possible.
			    
//              outOfMemoryDetectedOrMaxSizeReached = true;
//
//              nextRehashCheck = System.currentTimeMillis() + minimalRehashCheckInterval;
			}
		}
	}
	
}	// Internalize
