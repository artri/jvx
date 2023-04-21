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
 * 01.10.2008 - [JR] - creation
 * 05.04.2009 - [JR] - moved from research package
 *                   - remove(K, V) implemented
 * 23.11.2011 - [JR] - put with unique option       
 * 26.01.2012 - [JR] - don't extend Hashtable (does not compile with jdk 1.5, 1.7, warning with 1.6) because
 *                     of generics name clash            
 */
package com.sibvisions.util;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.sibvisions.util.type.StringUtil;

/**
 * The <code>KeyValueList</code> mapps multiple values to a single key.
 * 
 * @author René Jahn
 *
 * @param <K> key class
 * @param <V> value class
 */
public class KeyValueList<K, V>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the key value cache. */
	private Hashtable<K, List<V>> htValues = new Hashtable<K, List<V>>();

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return StringUtil.toString(htValues);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object pObject)
	{
		return (pObject != null && pObject == this)
               || htValues.equals(pObject);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode()
	{
		return htValues.hashCode();
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
		List<V> liValues = htValues.get(pKey);
		
		
		if (liValues == null)
		{
			liValues = new ArrayUtil<V>();
			
			htValues.put(pKey, liValues);
			
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
		return htValues.get(pKey);
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
		List<V> liValues = htValues.get(pKey);
		
		if (liValues != null)
		{
			liValues.remove(pValue);
			
			//remove empty lists
			if (liValues.size() == 0)
			{
				htValues.remove(pKey);
			}
		}
	}
	
    /**
     * Clears the list so that it contains no keys. 
     */
	public synchronized void clear()
	{
		htValues.clear();
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
		return htValues.remove(pKey);
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
		return htValues.containsKey(pKey);
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
		for (Collection<V> values : htValues.values())
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
	 * Removes the value from the internal value list. All values will be removed and also all keys without values,
	 * after removal.
	 * 
	 * @param pValue the value to remove
	 * @return <code>true</code> if at least one value was removed, <code>false</code> otherwise
	 */
	public synchronized boolean removeValue(V pValue)
	{
	    List<K> liKeys = null;
	    List<V> liValues;
	    
	    boolean bFound = false;
	    
	    for (Entry<K, List<V>> entry : htValues.entrySet())
	    {
	        liValues = entry.getValue();
	        
	        //remove all values from the list
	        boolean bRemoved;
	        boolean bWasEmpty = liValues.isEmpty();
	        
	        do
	        {
	            bRemoved = liValues.remove(pValue);
	            
	            if (bRemoved)
	            {
	                bFound = true;
	            }
	        }
	        while (bRemoved); 
	        
	        if (!bWasEmpty && liValues.isEmpty())
	        {
	            if (liKeys == null)
	            {
	                liKeys = new ArrayUtil<K>(); 
	            }
	            
	            liKeys.add(entry.getKey());
	        }
	    }
	    
	    if (liKeys != null)
	    {
	        for (int i = 0, cnt = liKeys.size(); i < cnt; i++)
	        {
	            htValues.remove(liKeys.get(i));
	        }
	    }
	    
	    return bFound;
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
		List<V> liValues = htValues.get(pKey);
		
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
		return htValues.size();
	}
	
    /**
     * Tests if the list is empty.
     *
     * @return <code>true</code> if the list is empty, <code>false</code> otherwise.
     */
	public synchronized boolean isEmpty()
	{
		return htValues.isEmpty();
	}

    /**
     * Returns an enumeration of the values in this list.
     * Use the Enumeration methods on the returned object to fetch the elements
     * sequentially.
     *
     * @return an enumeration of the values in this list.
     * @see java.util.Enumeration
     */	
	public synchronized Enumeration<List<V>> elements()
	{
		return htValues.elements();
	}
	
    /**
     * Returns a collection of the values in this list.
     *
     * @return a collection of the values in this list.
     * @see java.util.Collection
     */	
	public synchronized Collection<List<V>> values()
	{
		return htValues.values();
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
		return htValues.keySet();
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
		return htValues.entrySet();
	}
	
}	// KeyValueList
