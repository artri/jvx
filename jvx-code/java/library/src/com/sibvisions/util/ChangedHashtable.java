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
 * 27.10.2008 - [JR] - getMapping, clearChanges implemented
 * 30.10.2008 - [JR] - clear overwritten
 * 04.04.2009 - [JR] - getChanges(Class), getMapping(Class) implemented
 * 23.02.2010 - [JR] - #18
 *                     * Entry is now private and Map.Entry will be used for return values
 *                     * getChanges, getMapping: vararg Class parameter
 * 18.05.2010 - [JR] - put/remove: override parameter added
 * 18.06.2010 - [JR] - isChanged implemented   
 * 18.01.2011 - [JR] - isChanged fixed (did not worked because object parameter was not an entry)
 * 12.02.2016 - [JR] - constructors implemented 
 * 26.11.2019 - [JR] - clearChanges per key implemented                
 */
package com.sibvisions.util;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.sibvisions.util.type.CommonUtil;

/**
 * The <code>ChangedHashtable</code> is a synchronized Hashtable 
 * which tracks changes to keys and values. Synchronized means
 * that the operations are synchronized themselve. <p> 
 * The changes are available as key/value list, for other operations.
 * 
 * @author René Jahn
 * @param <K> the key class
 * @param <V> the value class
 */
public class ChangedHashtable<K, V> extends Hashtable<K, V>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the cache for all changed key/value pairs. */
	private ArrayUtil<Map.Entry<K, V>> auChanges;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Creates a new instance of <code>ChangedHashtable</code>, with a default initial capacity (11)
     * and load factor (0.75).
     */
	public ChangedHashtable()
	{
	    super();
	    
	    auChanges = null;
	}

    /**
     * Creates a new instance of <code>ChangedHashtable</code>, with the specified initial capacity
     * and default load factor (0.75).
     *
     * @param pInitialCapacity the initial capacity of the hashtable
     * @exception IllegalArgumentException if the initial capacity is less than zero
     */	
    public ChangedHashtable(int pInitialCapacity)
    {
        super(pInitialCapacity);
        
        auChanges = null;
    }
    
    /**
     * Creates a new instance of <code>ChangedHashtable</code> with the specified initial
     * capacity and the specified load factor.
     *
     * @param pInitialCapacity the initial capacity of the hashtable
     * @param pLoadFactor the load factor of the hashtable
     * @exception IllegalArgumentException  if the initial capacity is less than zero, 
     *            or if the load factor is nonpositive
     */
    public ChangedHashtable(int pInitialCapacity, float pLoadFactor) 
    {
        super(pInitialCapacity, pLoadFactor);
        
        auChanges = null;
    }
	
    /**
     * Creates a new instance of <code>ChangedHashtable</code> with the same mappings as the given
     * Map. The hashtable is created with an initial capacity sufficient to hold the mappings in 
     * the given Map and a default load factor (0.75). The changes are empty after construction.
     *
     * @param pMap the map whose mappings are to be placed in this map
     * @throws NullPointerException if the specified map is null
     */
    public ChangedHashtable(Map<? extends K, ? extends V> pMap)
    {
        super(pMap);
        
        //no changes if initialized with map 
        auChanges = null;
    }
    
    /**
     * Creates a new instance of <code>ChangedHashtable</code> with the same mappings as the given
     * Map. The hashtable is created with an initial capacity sufficient to hold the mappings in 
     * the given Map and a default load factor (0.75). 
     *
     * @param pMap the map whose mappings are to be placed in this map
     * @param pTrackChanges <code>true</code> to track changes 
     * @throws NullPointerException if the specified map is null
     */
    public ChangedHashtable(Map<? extends K, ? extends V> pMap, boolean pTrackChanges)
    {
    	super(pMap);
    	
    	if (!pTrackChanges)
    	{
    		auChanges = null;
    	}
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Maps the specified key to the specified value in this hashtable. If the
	 * <code>pValue</code> is null, the <code>pKey</code> will removed from this
	 * hashtable, if present.<p>
	 * This method tracks all changes to the hashtable.
	 * 
	 * @param pKey the key with which the specified value is to be associated
	 * @param pValue the value to be associated with the specified key
	 * @return the previous value of the specified <code>pKey</code> in this hashtable,
     *         or <code>null</code> if it did not have one.
	 */
	@Override
	public synchronized V put(K pKey, V pValue)
	{
		return put(pKey, pValue, true, false);
	}
	
	/**
	 * Removes the key (and its corresponding value) from this 
     * hashtable. This method does nothing if the key is not in the hashtable.<p>
     * This method tracks all changes to the hashtable.
     * 
     * @param pKey the key that needs to be removed.
     * @return the value to which the key had been mapped in this hashtable,
     *         or <code>null</code> if the key did not have a mapping.
     * @throws NullPointerException if the key is <code>null</code>.
	 */
	@Override
	public synchronized V remove(Object pKey)
	{
		return remove(pKey, true, false);
	}
	
	/**
	 * Clears this hashtable so that it contains no keys. All keys will be
	 * tracked as changed.
	 */
	@Override
	public synchronized void clear()
	{
		if (!isEmpty())
		{
			auChanges = new ArrayUtil<Map.Entry<K, V>>();

			for (K key : keySet())
			{
				auChanges.add(new Entry(key, null));
			}
		}
		
		super.clear();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Maps the specified key to the specified value in this hashtable with optional
	 * tracking of changes.
	 * 
	 * @param pKey the key with which the specified value is to be associated
	 * @param pValue the value to be associated with the specified key
	 * @param pTrackChanges true to track all changes to the hashtable
	 * @return the previous value of the specified <code>pKey</code> in this hashtable,
     *         or <code>null</code> if it did not have one.
	 */
	public synchronized V put(K pKey, V pValue, boolean pTrackChanges)
	{
		return put(pKey, pValue, pTrackChanges, false);
	}
	
	
	/**
	 * Maps the specified key to the specified value in this hashtable with optional
	 * tracking of changes.
	 * 
	 * @param pKey the key with which the specified value is to be associated
	 * @param pValue the value to be associated with the specified key
	 * @param pTrackChanges true to track all changes to the hashtable
	 * @param pOverride <code>true</code> to track changes, if <code>pTrackChanges = true</code> independent of the 
	 *                  old value
	 * @return the previous value of the specified <code>pKey</code> in this hashtable,
     *         or <code>null</code> if it did not have one.
	 */
	public synchronized V put(K pKey, V pValue, boolean pTrackChanges, boolean pOverride)
	{
		V oldValue;
		
		
		if (pValue == null)
		{
			oldValue = remove(pKey, pTrackChanges, pOverride);
		}
		else
		{		
			oldValue = super.put(pKey, pValue);

			//pOverride check before equals because equals will do more than expected and may cause a performance impact (e.g. DataRow equals)
			if (pTrackChanges && (pOverride || oldValue == null 
			    || !(oldValue.equals(pValue) 
			         || (oldValue instanceof Object[] 
			        	 && pValue instanceof Object[] 
			        	 && Arrays.equals((Object[])oldValue, (Object[])pValue)))))
			{
				if (auChanges == null)
				{
					auChanges = new ArrayUtil<Map.Entry<K, V>>();
				}

				auChanges.add(new Entry(pKey, pValue));
			}
		}
		
		return oldValue;
	}
	
	/**
	 * Removes the key (and its corresponding value) from this 
     * hashtable with optional tracking of changes. 
     * This method does nothing if the key is not in the hashtable.<p>
     * 
     * @param pKey the key that needs to be removed.
	 * @param pTrackChanges true to track all changes to the hashtable
     * @return the value to which the key had been mapped in this hashtable,
     *         or <code>null</code> if the key did not have a mapping.
     * @throws NullPointerException if the key is <code>null</code>.
	 */
	public synchronized V remove(Object pKey, boolean pTrackChanges)
	{
		return remove(pKey, pTrackChanges, false);
	}
	
	/**
	 * Removes the key (and its corresponding value) from this 
     * hashtable with optional tracking of changes. 
     * This method does nothing if the key is not in the hashtable.<p>
     * 
     * @param pKey the key that needs to be removed.
	 * @param pTrackChanges true to track all changes to the hashtable
	 * @param pOverride <code>true</code> to track changes, if <code>pTrackChanges = true</code> independent of the 
	 *                  old value
     * @return the value to which the key had been mapped in this hashtable,
     *         or <code>null</code> if the key did not have a mapping.
     * @throws NullPointerException if the key is <code>null</code>.
	 */
	public synchronized V remove(Object pKey, boolean pTrackChanges, boolean pOverride)
	{
		V oldValue = super.remove(pKey);
		
		if (pTrackChanges && (oldValue != null || pOverride))
		{
			if (auChanges == null)
			{
				auChanges = new ArrayUtil<Map.Entry<K, V>>();
			}

			auChanges.add(new Entry((K)pKey, null));
		}
		
		return oldValue;
	}

	/**
	 * Gets the tracked changes of this <code>Hashtable</code> but return only
	 * the values with a specified class type. If the <code>Hashtable</code> contains
	 * {@link Object} types as values, then you can return all values with {@link Integer}
	 * types or {@link String} types:
	 * 
	 * <code>
	 * ChangedHashtable&lt;String, Object&gt; table = new ChangedHashtable&lt;String, Object&gt;();
	 * List&lt;Entry&lt;String, Object&gt;&gt; changes = table.getChanges(String.class);
	 * </code>
	 * 
	 * <p>ALL changes will be reset.</p>
	 * 
	 * @param pValueType missing or <code>null</code> to return all values, or a list of class types 
	 *                   to return the values with this class types. 
	 *                   (<code>null</code> values are always included)
	 * @return the changed key/values as {@link java.util.Map.Entry} list or <code>null</code> if there are no changes
	 */
	public synchronized List<Map.Entry<K, V>> getChanges(Class<? extends V>... pValueType)
	{
		ArrayUtil<Map.Entry<K, V>> auOldChanges = auChanges;
		
		auChanges = null; 
		
		if (auOldChanges == null || pValueType == null || pValueType.length == 0)
		{
			return auOldChanges;
		}
		else
		{
			Map.Entry<K, V> entry;
			V value;
			boolean bFound;
			
			for (int i = auOldChanges.size() - 1; i >= 0; i--)
			{
				entry = auOldChanges.get(i);	

				value = entry.getValue();
		
				//null is always included!
				if (value == null)
				{
					bFound = true;
				}
				else
				{
					bFound = false;

					for (int j = 0; j < pValueType.length && !bFound; j++)
					{
						if (pValueType[j].isAssignableFrom(value.getClass()))
						{
							bFound = true;
						}
					}
				}
				
				if (!bFound)
				{
					auOldChanges.remove(i);
				}
			}
		}
		
		return auOldChanges;
	}
	
	/**
	 * Gets the last tracked changes of this <code>Hashtable</code> but return only
	 * the values with a specified class type. If the <code>Hashtable</code> contains
	 * {@link Object} types as values, then you can return all values with {@link Integer}
	 * types or {@link String} types:
	 * 
	 * <code>
	 * ChangedHashtable&lt;String, Object&gt; table = new ChangedHashtable&lt;String, Object&gt;();
	 * List&lt;Entry&lt;String, Object&gt;&gt; changes = table.getChanges(String.class);
	 * </code>
	 * 
	 * <p>ALL changes will be reset.</p>
	 * 
	 * @return the changed key/values as {@link java.util.Map.Entry} list or <code>null</code> if there are no changes
	 */
	public synchronized List<Map.Entry<K, V>> getLastChanges()
	{
		ArrayUtil<Map.Entry<K, V>> auOldChanges = auChanges;
		
		auChanges = null;
		
		if (auOldChanges == null)
		{
			return auOldChanges;
		}
		else
		{
			Map<K, K> keys = new LinkedHashMap<K, K>();
			
			Map.Entry<K, V> entry;
			K key;
			
			for (int i = auOldChanges.size() - 1; i >= 0; i--)
			{
				entry = auOldChanges.get(i);	
				key = entry.getKey();
				
				if (keys.get(key) != null)
				{
					auOldChanges.remove(i);
				}
				else
				{
					keys.put(key, key);
				}
			}
		}
		
		return auOldChanges;
	}
	
	/**
	 * Clears the list of changed mappings.
	 */
	public synchronized void clearChanges()
	{
		auChanges = null;
	}
	
	/**
	 * Gets a list of entries but only includes the values of a specific type. If the 
	 * <code>Hashtable</code> contains {@link Object} types as values, then you can 
	 * return all values with {@link Integer} types or {@link String} types:
	 * 
	 * <code>
	 * ChangedHashtable&lt;String, Object&gt; table = new ChangedHashtable&lt;String, Object&gt;();
	 * List&lt;Entry&lt;String, Object&gt;&gt; changes = table.getMapping(String.class);
	 * </code>
	 * 
	 * @param pValueType missing or <code>null</code> to return all values, or a list of class types 
	 *                   to return the values with this class types. 
	 *                   (<code>null</code> values are always included)
	 * @return the current key/values as {@link java.util.Map.Entry} list
	 */
	public synchronized List<Map.Entry<K, V>> getMapping(Class<? extends V>... pValueType)
	{
		ArrayUtil<Map.Entry<K, V>> auMapping = new ArrayUtil<Map.Entry<K, V>>();
		
		V value;
		
		boolean bFound;
		
		for (Map.Entry<K, V> mapping : entrySet())
		{
			value = mapping.getValue();
			
			//null is always included!
			if (value == null)
			{
				bFound = true;
			}
			else
			{
				if (pValueType != null && pValueType.length > 0)
				{
					bFound = false;

					for (int j = 0; j < pValueType.length && !bFound; j++)
					{
						if (pValueType[j].isAssignableFrom(value.getClass()))
						{
							bFound = true;
						}
					}
				}
				else
				{
					bFound = true;
				}
			}
			
			if (bFound)
			{
				auMapping.add(new Entry(mapping.getKey(), mapping.getValue()));
			}
		}
		
		return auMapping;
	}
	
	/**
	 * Checks if a the value of a property is currently marked as changed.
	 * 
	 * @param pKey the property name
	 * @return <code>true</code> if the property value has changed since last access
	 * @see #getChanges(Class...)
	 * @see #getLastChanges()
	 */
	public synchronized boolean isChanged(K pKey)
	{
		if (pKey instanceof Map.Entry)
		{
			return auChanges != null && auChanges.indexOf(pKey) >= 0;
		}
		
		if (auChanges != null)
		{
			for (Enumeration<Map.Entry<K, V>> en = auChanges.enumeration(); en.hasMoreElements();)
			{
				if (CommonUtil.equals(en.nextElement().getKey(), pKey))
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Clears the changes for the given property.
	 * 
	 * @param pKey the property name
	 */
	public synchronized void clearChanges(K pKey)
	{
		if (pKey instanceof Map.Entry)
		{
			if (auChanges != null)
			{
				auChanges.remove(pKey);
			}
			
			return;
		}

		if (auChanges != null)
		{
			for (int i = auChanges.size() - 1; i >= 0; i--)
			{
				if (CommonUtil.equals(auChanges.get(i).getKey(), pKey))
				{
					auChanges.remove(i);
				}
			}
		}
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************

	/**
	 * The <code>Entry</code> encapsulates key/value mappings from the <code>ChangedHashtable</code>.
	 * 
	 * @author René Jahn
	 * 
	 * @param <KI> type of key
	 * @param <VI> type of entry
	 */
	private static final class Entry<KI, VI> implements Map.Entry
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/** the key reference. */
		private KI key;
		
		/** the value reference. */
		private VI value;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Creates a new instance of <code>Entry</code> with key and value
		 * references.
		 * 
		 * @param pKey the key reference
		 * @param pValue the value reference
		 */
		private Entry(KI pKey, VI pValue)
		{
			key = pKey;
			value = pValue;
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Interface implementation
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * {@inheritDoc}
		 */
		public KI getKey()
		{
			return key;
		}

		/**
		 * {@inheritDoc}
		 */
		public VI getValue()
		{
			return value;
		}

		/**
		 * Does nothing.
		 * 
		 * @param pValue the param will be ignored
		 * @return the value
		 * @see #getValue()
		 */
		public VI setValue(Object pValue)
		{
			return value;
		}
		
	}	// Entry
	
}	// ChangedHashtable
