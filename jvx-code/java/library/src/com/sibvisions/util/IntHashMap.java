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

import java.io.Serializable;

/**
 * The <code>IntHashMap</code> is a very efficient int hash map.
 * 
 * @param <T> any Object.
 * @author Martin Handsteiner
 */
public final class IntHashMap<T> implements Serializable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the minimum size of the internal object cache. */
	private static final int MIN_SIZE = 31; //17; // 63719; //9337; //63719;//63709; //64969; //573473;
	
	
    /** the values. */
    private T[] values = (T[])new Object[MIN_SIZE];

	/** the keys. */
	private int[] keys = new int[MIN_SIZE];

    /** size used. */
    private int size = 0;

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>IntHashMap</code>.
	 */
	public IntHashMap()
	{
	}
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        StringBuilder result = new StringBuilder();
        
        result.append('{');
        
        for (int i = 0; i < keys.length; i++)
        {
            T value = values[i];
            
            if (value != null)
            {
                if (result.length() > 1)
                {
                    result.append(", ");
                }
                result.append(keys[i]);
                result.append('=');
                result.append(value);
            }
        }
        
        result.append('}');
        
        return result.toString();
    }
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Clears the interned objects. 
	 */
	public void clear()
	{
		keys = new int[MIN_SIZE];
		values = (T[])new Object[MIN_SIZE];
		size = 0;
	}
	
	/**
	 * Gets the amount of internalized objects.
	 * 
	 * @return the amount of internalized objects.
	 */
	public int size()
	{
		return size;
	}
	
	/**
	 * Create wide spreading hash code.
	 * 
	 * @param pHashCode the original hash code.
	 * @return wide spreading hash code,
	 */
	private final int hashCode(int pHashCode)
	{
		long val2 = (pHashCode < 0) ? -pHashCode : pHashCode;
		int temp = (int)(((int)(val2 >>> 32)) * 31 + (val2 & 0xffffffffL));
		
	    return ((31 * ((pHashCode < 0) ? -temp : temp)) & 0x7fffffff) % values.length;
	}
	
	/**
	 * Puts the value for the given key.
	 * 
	 * @param pKey the key.
	 * @param pValue the value.
	 * @return the old value.
	 */
	public T put(int pKey, T pValue)
	{
		int index = hashCode(pKey);
		T oldValue = values[index];
		while (pKey != keys[index] && oldValue != null)
		{
			if (++index == keys.length)
			{
				index = 0;
			}
			oldValue = values[index];
		}
		keys[index] = pKey;
		values[index] = pValue;
		if (oldValue == null)
		{
			size++;
			if (size > values.length / 2)
			{
				rehashAll(values.length * 2);
			}
		}
		
		return oldValue;
	}

	/**
	 * Gets the value for key.
	 * 
	 * @param pKey the key
	 * @return the value
	 */
	public T get(int pKey)
	{
		int index = hashCode(pKey);
		T value = values[index];
		while (pKey != keys[index] && value != null)
		{
			if (++index == keys.length)
			{
				index = 0;
			}
			value = values[index];
		}
		
		return value;
	}
	
	/**
	 * Removes the key.
	 * 
	 * @param pKey the key
	 * @return the removed value
	 */
	public T remove(int pKey)
	{
		int index = hashCode(pKey);
		T value = values[index];
		while (pKey != keys[index] && value != null)
		{
			if (++index == keys.length)
			{
				index = 0;
			}
			value = values[index];
		}
		
		keys[index] = 0;
		values[index] = null;
		
		if (value != null)
		{
			size--;
			
			if (values.length > MIN_SIZE && size < values.length / 4)
			{
				rehashAll(values.length / 2);
			}
			else // Replace all values placed due to aviation strategy
			{
				if (++index == keys.length)
				{
					index = 0;
				}
				T rehashValue = values[index];
				while (rehashValue != null)
				{
					int rehashKey = keys[index];
					if (hashCode(rehashKey) != index)
					{
						keys[index] = 0;
						values[index] = null;
						size--;
						
						put(rehashKey, rehashValue);
					}
					
					if (++index == keys.length)
					{
						index = 0;
					}
					rehashValue = values[index];
				}
			}
		}
		
		return value;
	}
	
	/**
	 * Rehash the cache if it is to small or to large.
	 * 
	 * @param pNewSize the new size.
	 */ 
	private void rehashAll(int pNewSize)
	{
		int[] oldIndexes = keys;
		T[] oldObjects = values;
		
		keys = new int[pNewSize];
		values = (T[])new Object[pNewSize];
		size = 0;

		for (int i = 0; i < oldIndexes.length; i++)
		{
			T value = oldObjects[i];
			
			if (value != null)
			{
				put(oldIndexes[i], value);
			}
		}
	}
	
}	// IntHashMap
