/*
 * Copyright 2011 SIB Visions GmbH
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
 * 19.11.2011 - [JR] - creation
 * 26.01.2012 - [JR] - don't extend Hashtable (does not compile with jdk 1.5, 1.7, warning with 1.6) because
 *                     of generics name clash            
 */
package com.sibvisions.util;

import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.sibvisions.util.type.StringUtil;

/**
 * The <code>KeyValueList</code> is a special <code>IdentityHashMap</code>. It
 * mapps multiple values to a single key.
 * 
 * @author René Jahn
 *
 * @param <K> key class
 * @param <V> value class
 */
public class IdentityKeyValueList<K, V>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the key value cache. */
	private IdentityHashMap<K, List<V>> hmpValues = new IdentityHashMap<K, List<V>>();

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return StringUtil.toString(hmpValues);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object pObject)
	{
		return (pObject != null && pObject == this)
			   || hmpValues.equals(pObject);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode()
	{
		return hmpValues.hashCode();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Adds the specified <code>pValue</code> to a list of values which are 
	 * mapped to the <code>pKey</code>. Neither the <code>pKey</code> nor the 
	 * <code>pValue</code> can be <code>null</code>.
	 * 
	 * @param pKey the key
	 * @param pValue the new value
	 * @throws NullPointerException if the key or value is <code>null</code>
	 */
	public synchronized void put(K pKey, V pValue)
	{
		put(pKey, pValue, false);
	}
	
	/**
	 * Adds the specified <code>pValue</code> collection to a list of values which are 
	 * mapped to the <code>pKey</code>. Neither the <code>pKey</code> nor the 
	 * <code>pValue</code> can be <code>null</code>.
	 * 
	 * @param pKey the key
	 * @param pValues the new value collection
	 * @throws NullPointerException if the key or value is <code>null</code>
	 */
	public synchronized void putAll(K pKey, Collection<V> pValues)
	{
		for (V value : pValues)
		{
			put(pKey, value, false);
		}
	}

	/**
	 * Adds the specified <code>pValue</code> to a list of values which are 
	 * mapped to the <code>pKey</code>. Neither the <code>pKey</code> nor the 
	 * <code>pValue</code> can be <code>null</code>. It is possible to add the value
	 * only if it is not already added.
	 * 
	 * @param pKey the key
	 * @param pValue the new value
	 * @param pUnique <code>true</code> to add the value only if it is not already in the list, <code>false</code> to add
	 *                the value in any case
	 * @throws NullPointerException if the key or value is <code>null</code>
	 */
	public synchronized void put(K pKey, V pValue, boolean pUnique)
	{
		List<V> liValues = hmpValues.get(pKey);
		
		
		if (liValues == null)
		{
			liValues = new ArrayUtil<V>();
			
			hmpValues.put(pKey, liValues);
			
			liValues.add(pValue);
		}
		else
		{
			if (!pUnique || liValues.indexOf(pValue) < 0)
			{
				liValues.add(pValue);
			}
		}
	}
	
    /**
     * Returns the value list for the specified key in the list.
     *
     * @param pKey the key
     * @return the list of values that are mapped to the key or <code>null</code> if the key
     *         is unknown
     * @throws NullPointerException if the key is <code>null</code>
     */
	public synchronized List<V> get(K pKey)
	{
		return hmpValues.get(pKey);
	}
	
	/**
	 * Removes a specific <code>pValue</code> from a list of values which is mapped to the 
	 * <code>pKey</code>.
	 *  
	 * @param pKey the key
	 * @param pValue the value to be removed
	 */
	public synchronized void remove(K pKey, V pValue)
	{
		List<V> liValues = hmpValues.get(pKey);
		
		if (liValues != null)
		{
			liValues.remove(pValue);
			
			//remove empty lists
			if (liValues.size() == 0)
			{
				hmpValues.remove(pKey);
			}
		}
	}
	
    /**
     * Clears the list so that it contains no keys. 
     */
	public synchronized void clear()
	{
		hmpValues.clear();
	}
	
    /**
     * Removes the key (and its corresponding value) from this 
     * list. This method does nothing if the key is not in the list.
     *
     * @param pKey the key that needs to be removed.
     * @return the list of values to which the key had been mapped,
     *          or <code>null</code> if the key did not have a values.
     * @throws NullPointerException if the key is <code>null</code>.
     */
	public synchronized List<V> remove(K pKey)
	{
		return hmpValues.remove(pKey);
	}
	
    /**
     * Tests if the specified key is in this list.
     * 
     * @param pKey possible key.
     * @return <code>true</code> if and only if the specified key 
     *          is in this list, <code>false</code> otherwise.
     * @throws NullPointerException if the key is <code>null</code>.
     */
	public synchronized boolean containsKey(K pKey)
	{
		return hmpValues.containsKey(pKey);
	}
	
    /**
     * Tests if the specified values is in this list.
     * 
     * @param pValue possible value.
     * @return <code>true</code> if and only if the specified value 
     *          is in this list, <code>false</code> otherwise.
     */
	public synchronized boolean containsValue(V pValue)
	{
		for (Collection<V> values : hmpValues.values())
		{
			for (V element : values)
			{
				if (element.equals(pValue))
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Tests whether this list maps a key which contains a specific value.
	 *  
	 * @param pKey the key.
	 * @param pValue the value.
	 * @return <code>true</code> when the value is mapped with the given key, otherwise <code>false</code>.
	 */
	public synchronized boolean contains(K pKey, V pValue)
	{
		List<V> liValues = hmpValues.get(pKey);
		
		if (liValues == null)
		{
			return false;
		}
		else
		{
			return liValues.contains(pValue);
		}
	}

    /**
     * Returns the number of keys.
     *
     * @return the number of keys.
     */	
	public synchronized int size()
	{
		return hmpValues.size();
	}
	
    /**
     * Tests if the list is empty.
     *
     * @return <code>true</code> if the list is empty, <code>false</code> otherwise.
     */
	public synchronized boolean isEmpty()
	{
		return hmpValues.isEmpty();
	}

    /**
     * Returns a collection of the values in this list.
     *
     * @return a collection of the values in this list.
     * @see java.util.Collection
     */	
	public Collection<List<V>> values()
	{
		return hmpValues.values();
	}
	
    /**
     * Returns a Set view of the keys contained in this list. The Set
     * is backed by the list, so changes to the list are reflected
     * in the Set, and vice-versa. The Set supports element removal
     * (which removes the corresponding entry from the list), but not
     * element addition.
     *
     * @return a set view of the keys contained in this list.
     */
	public Set<K> keySet()
	{
		return hmpValues.keySet();
	}
	
	
    /**
     * Returns a Set view of the entries contained in this list.
     * Each element in this collection is a Map.Entry. The Set is
     * backed by the list, so changes to the list are reflected in
     * the Set, and vice-versa.  The Set supports element removal
     * (which removes the corresponding entry from the list),
     * but not element addition.
     *
     * @return a set view of the mappings contained in this list.
     * @see   Entry
     */
	public Set<Map.Entry<K, List<V>>> entrySet()
	{
		return hmpValues.entrySet();
	}	
	
}	// IdentityKeyValueList
