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
 * 12.06.2014 - [JR] - synchronized methods
 *                   - removeKey returns a list of all remoed objects
 */
package com.sibvisions.util;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

/**
 * The <code>GroupHashtable</code> is a utility class to group multiple key/value pairs
 * with a single group key.<br> Its implementation has a Hashtable, for the group, which 
 * contains multiple Hashtables, for the key/value pairs.
 * 
 * @author René Jahn
 *
 * @param <G> group class
 * @param <K> key class
 * @param <V> value class
 */
public class GroupHashtable<G, K, V>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** contains the group mapping. */
	private Hashtable<G, Hashtable<K, V>> htGroup = null;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Associates the specified value with the specified key to the group.
	 * 
	 * @param pGroup group identifier
	 * @param pKey key with which the specified value is to be associated
	 * @param pValue value to be associated with the specified key
	 */
	public synchronized void put(G pGroup, K pKey, V pValue)
	{
		Hashtable<K, V> htValues = null;
		
		
		if (htGroup == null)
		{
			htGroup = new Hashtable<G, Hashtable<K, V>>();
		}
		else
		{
			htValues = htGroup.get(pGroup);
		}
		
		if (htValues == null)
		{
			htValues = new Hashtable<K, V>();
			
			htGroup.put(pGroup, htValues);
		}
		
		htValues.put(pKey, pValue);
	}

	/**
	 * Associates the specified values with the specified group.
	 * 
	 * @param pGroup group identifier
	 * @param pValues values to be associated with the specified group
	 */
	public synchronized void put(G pGroup, Hashtable<K, V> pValues)
	{
		if (htGroup == null)
		{
			htGroup = new Hashtable<G, Hashtable<K, V>>();
		}
		
		htGroup.put(pGroup, pValues);
	}

	/**
	 * Returns the value to which the specified key is mapped.
	 * 
	 * @param pGroup group identifier
	 * @param pKey the key whose associated value is to be returned
	 * @return the value to which the specified key is mapped or null
	 *         if the key or the group is not mapped. 
	 */
	public synchronized V get(G pGroup, K pKey)
	{
		Hashtable<K, V> htValues = get(pGroup);
		

		if (htValues == null)
		{
			return null;
		}
		else
		{
			return htValues.get(pKey);
		}
	}
	
	/**
	 * Returns the internal <code>Hashtable</code> associated with the <code>pGroup</code>.
	 * 
	 * @param pGroup group identifier
	 * @return the <code>Hashtable</code> which is mapped to the specified group or <code>null</code>
	 *         if the <code>pGroup</code> is not mapped
	 */
	public synchronized Hashtable<K, V> get(G pGroup)
	{
		if (htGroup == null)
		{
			return null;
		}
		else
		{
			return htGroup.get(pGroup);
		}
	}
	
	/**
	 * Removes the mapping of an entire group if it is present.
	 * 
	 * @param pGroup group identifier
	 * @return true if the group was removed, false if the group is not present 
	 *         or can not be removed
	 */
	public synchronized boolean remove(G pGroup)
	{
		if (htGroup != null)
		{
			return htGroup.remove(pGroup) != null;
		}
		
		return false;
	}
	
	/**
	 * Removes the mapping for a key from its group if it is present.
	 * 
	 * @param pGroup group identifier
	 * @param pKey key to remove
	 * @return the previous value associated with the key or 
	 *         null if there is no mapping the key
	 */
	public synchronized V remove(G pGroup, K pKey)
	{
		Hashtable<K, V> htValues = get(pGroup);
		
		
		if (htValues == null)
		{
			return null;
		}
		else
		{
			return htValues.remove(pKey);
		}
	}
	
	/**
	 * Returns the group count.
	 * 
	 * @return number of groups
	 */
	public synchronized int size()
	{
		if (htGroup == null)
		{
			return 0;
		}
		
		return htGroup.size();
	}
	
	/**
	 * Returns the number of group elements.
	 * 
	 * @param pGroup group identifier
	 * @return number of group elements
	 */
	public synchronized int size(G pGroup)
	{
		Hashtable<K, V> htValues;
		
		
		if (htGroup == null)
		{
			return 0;
		}
		else
		{
			htValues = htGroup.get(pGroup);
			
			if (htValues == null)
			{
				return 0;
			}
			
			return htValues.size();
		}
	}
	
	/**
	 * Returns an enumeration of the values associated with a group,
	 * in this hashtable. Use the Enumeration methods on the returned 
	 * object to fetch the elements sequentially.
	 * 
	 * @param pGroup group identifier
	 * @return an enumeration of the values in this hashtable
	 */
	public synchronized Enumeration<V> elements(G pGroup)
	{
		Hashtable<K, V> htValues = get(pGroup);
		
		
		if (htValues == null)
		{
			return null;
		}
		else
		{
			return htValues.elements();
		}
	}
	
	/**
	 * Returns an enumeration of the keys associated with a group,
	 * in this hashtable.
	 * 
	 * @param pGroup group identifier
	 * @return enumeration of the keys in this hashtable
	 */
	public synchronized Enumeration<K> keys(G pGroup)
	{
		Hashtable<K, V> htValues = get(pGroup);
		
		
		if (htValues == null)
		{
			return null;
		}
		else
		{
			return htValues.keys();
		}
	}
	
	/**
	 * Returns an enumeration of all groups.
	 * 
	 * @return enumeration of groups
	 */
	public synchronized Enumeration<G> groups()
	{
	    if (htGroup == null)
	    {
	        return null;
	    }
	    else
	    {
	        return htGroup.keys();
	    }
	}

	/**
	 * Removes the given key from all groups, if possible.
	 * 
	 * @param pKey the key to remove
	 * @return the list of removed values or <code>null</code> if no value was removed
	 */
	public synchronized List<V> removeKey(K pKey)
	{
	    if (htGroup != null)
	    {
	        List<V> liValues = new ArrayUtil<V>();
	        
	        for (Enumeration<Hashtable<K, V>> enElements = htGroup.elements(); enElements.hasMoreElements();)
	        {
	            liValues.add(enElements.nextElement().remove(pKey));
	        }
	        
	        if (!liValues.isEmpty())
	        {
	            return liValues;
	        }
	    }
	    
	    return null;
	}

	/**
	 * Gets whether the group exists.
	 * 
	 * @param pGroup the group identifier
	 * @return <code>true</code> if the group exists, <code>false</code> otherwise
	 */
	public synchronized boolean containsGroup(G pGroup)
	{
	    if (htGroup == null)
	    {
	        return false;
	    }
	    
	    return htGroup.containsKey(pGroup);
	}
	
	/**
	 * Returns true if the group maps one or more keys to this value.
	 *  
	 * @param pGroup group identifier
	 * @param pValue value whose presence is to be tested
	 * @return true if the group maps one or more keys to the specified value, false otherwise
	 */
	public synchronized boolean containsValue(G pGroup, V pValue)
	{
		Hashtable<K, V> htValues = get(pGroup);
		
		
		if (htValues == null)
		{
			return false;
		}
		else
		{
			return htValues.containsValue(pValue);
		}
	}
	
	/**
	 * Tests if the specified object is a key in the group mapping.
	 * 
	 * @param pGroup group identifier
	 * @param pKey possible key
	 * @return true if and only if the specified key is mapped in the group, false otherwise
	 */
	public synchronized boolean containsKey(G pGroup, K pKey)
	{
		Hashtable<K, V> htValues = get(pGroup);
		
		if (htValues == null)
		{
			return false;
		}
		else
		{
			return htValues.containsKey(pKey);
		}
	}
	
	/**
	 * Clears all entries.
	 */
	public synchronized void clear()
	{
		if (htGroup != null)
		{
			htGroup.clear();
		}
	}
	
	/**
	 * Clears all entries from a specific group.
	 * 
	 * @param pGroup the group identifier
	 */
	public synchronized void clear(G pGroup)
	{
		Hashtable<K, V> htValues = get(pGroup);

		if (htValues != null)
		{
			htValues.clear();
		}
	}
	
}	// GroupHashtable
