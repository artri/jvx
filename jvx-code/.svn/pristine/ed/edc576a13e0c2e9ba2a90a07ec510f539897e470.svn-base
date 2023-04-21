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
 * 23.11.2008 - [JR] - creation
 * 10.12.2008 - [JR] - Map constructor doesn't call super to avoid NullPointerException in put [BUGFIX]
 */
package com.sibvisions.util;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

/**
 * The <code>OrderedHashtable</code> extends a {@link Hashtable} and keeps
 * the add order. It works according to the FiFo principle. The first key
 * added is the first key in the key list.
 * 
 * @author René Jahn
 * 
 * @param <K> the key object type
 * @param <V> the value object type
 * 
 * @see Hashtable
 */
public class OrderedHashtable<K, V> extends Hashtable<K, V>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the list of keys to keep the add/put order. */
	private Vector<K> vOrderedKeys;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
     * Creates a new instance of <code>OrderedHashtable</code> with a default 
     * initial capacity (11) and load factor (0.75).
	 * 
	 * @see Hashtable#Hashtable()
	 */
	public OrderedHashtable()
	{
		super();
		
		vOrderedKeys = new Vector<K>(11);
	}

	/**
	 * 
     * Creates a new instance of <code>OrderedHashtable</code> with the specified initial 
     * capacity and default load factor (0.75).
     *
     * @param pCapacity the initial capacity of the hashtable
     * @throws IllegalArgumentException if the initial capacity is less than zero
     * @see Hashtable#Hashtable(int)
	 */
	public OrderedHashtable(int pCapacity)
	{
		super(pCapacity);
		
		vOrderedKeys = new Vector<K>(pCapacity);
	}

	/**
     * Creates a new instance of <code>OrderedHashtable</code> with the specified initial
     * capacity and the specified load factor.
     *
	 * @param pCapacity the initial capacity of the hashtable
	 * @param pLoadFactor the load factor of the hashtable
	 * @exception IllegalArgumentException if the initial capacity is less than zero, 
	 *                                     or if the load factor is nonpositive.
	 * @see Hashtable#Hashtable(int, float)                                     
	 */
	public OrderedHashtable(int pCapacity, float pLoadFactor)
	{
		super(pCapacity, pLoadFactor);
		
		vOrderedKeys = new Vector(pCapacity);
	}
	
    /**
     * Creates a new instance of <code>OrderedHashtable</code> with the same mappings 
     * as the given {@link Map}. The hashtable is created with an initial capacity sufficient 
     * to hold the mappings in the given {@link Map} and a default load factor (0.75).
     *
     * @param pMap the map whose mappings are to be placed in this map.
     * @throws NullPointerException if the specified map is null.
     */
    public OrderedHashtable(Map<? extends K, ? extends V> pMap) 
    {
    	this(Math.max(2 * pMap.size(), 11), 0.75f);
    	
    	vOrderedKeys = new Vector(Math.max(2 * pMap.size(), 11));
    	
    	putAll(pMap);
    }

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Maps the specified <code>pKey</code> to the specified
     * <code>pValue</code> in this hashtable. Neither the key nor the
     * value can be <code>null</code>. <p>
     *
     * The value can be retrieved by calling the <code>get</code> method
     * with a key that is equal to the original key.<p>
     * 
     * The order will be stored to guarantee the FiFo principle. 
	 *
	 * @param pKey the hashtable key
	 * @param pValue the value
	 * @return the previous value of the specified key in this hashtable,
     *         or <code>null</code> if it did not have one
	 * @throws NullPointerException  if the key or value is <code>null</code>
     * @see #get(Object)
	 */
	public synchronized V put(K pKey, V pValue)
	{
		V oldValue = super.put(pKey, pValue);
			
		//only replace the value -> no reordering
		if (oldValue == null)
		{
			vOrderedKeys.add(pKey);
		}
		
		return oldValue;
	}

	/**
	 * Removes the key (and its corresponding value) from this
     * hashtable. This method does nothing if the key is not in the hashtable.
	 *
	 * @param pKey the key that needs to be removed
	 * @return the value to which the key had been mapped in this hashtable,
     *         or <code>null</code> if the key did not have a mapping
     * @throws NullPointerException if the key is <code>null</code>         
	 */
	public synchronized V remove(Object pKey)
	{
		V oldValue = super.remove(pKey);
		
		if (oldValue != null)
		{
			vOrderedKeys.removeElement(pKey);
		}
		
		return oldValue;
	}
	
	/**
	 * Creates a shallow copy of this hashtable. All the structure of the
     * hashtable itself is copied, but the keys and values are not cloned.
	 *
	 * @return a clone of the hashtable
	 */
	@Override
	public synchronized Object clone()
	{
		OrderedHashtable<K, V> clone = (OrderedHashtable<K, V>)super.clone();
		
		//don't forget the key order! 
		clone.vOrderedKeys = (Vector<K>)vOrderedKeys.clone();
		
		return clone;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void clear()
	{
		super.clear();
		
		vOrderedKeys.removeAllElements();
	}
	
	/**
	 * Returns an enumeration of the keys in this hashtable. It is guaranteed that the
	 * order is the same as the keys have been added (FiFo principle).
	 *
	 * @return an enumeration of the keys in this hashtable
	 */
	@Override
	public synchronized Enumeration<K> keys()
	{
		return ((Vector<K>)vOrderedKeys.clone()).elements();
	}

	/**
	 * {@inheritDoc}
	 */
    @Override
    public synchronized Set<K> keySet()
    {
    	Set<K> set = new LinkedHashSet<K>(((Vector<K>)vOrderedKeys.clone()));
        
        return set;
    }
	
    /**
     * {@inheritDoc}
     */
    @Override
	public synchronized Enumeration<V> elements()
	{
	    return new Enumeration<V>() 
	    {
	        Enumeration en = ((Vector<K>)vOrderedKeys.clone()).elements();
	        
	        public boolean hasMoreElements() 
	        {
	            return en.hasMoreElements();
	        }

	        public V nextElement() 
	        {
	            return get(en.nextElement());
	        }
	     };	
	}
    
    /**
     * {@inheritDoc} 
     */
    @Override
    public synchronized Set<Map.Entry<K, V>> entrySet()
    {
        //keep add order
        Set set = new LinkedHashSet<Map.Entry<K, V>>();
        
        Object key;
        
        for (int i = 0, cnt = vOrderedKeys.size(); i < cnt; i++)
        {
            key = vOrderedKeys.get(i);

            set.add(new Entry(key, get(key)));
        }
        
        return set;
    }

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets a key by index.
	 * 
	 * @param pIndex the index of the key
	 * @return the key
	 * @throws ArrayIndexOutOfBoundsException if the index is out of range
     *	       ({@code index < 0 || index >= size()})
	 */
	public synchronized K getKey(int pIndex)
	{
		return vOrderedKeys.get(pIndex);
	}
	
	//****************************************************************
    // Subclass definition
    //****************************************************************

    /**
     * The <code>Entry</code> is a simple data container for key, value pairs.
     * 
     * @param <K> the key object type
     * @param <V> the value object type 
     */
    private static class Entry<K, V> implements Map.Entry<K, V>
    {
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Class members
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        
        /** the key. */
        private K key;

        /** the value. */
        private V value;

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Initialization
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     
        /**
         * Creates a new instance of <code>Entry</code>.
         * 
         * @param pKey the key
         * @param pValue the value
         */
        protected Entry(K pKey, V pValue)
        {
            key = pKey;
            value = pValue;
        }

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Overwritten methods
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /**
         * {@inheritDoc}
         */
        @Override
        protected Object clone()
        {
            return new Entry<K, V>(key, value);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean equals(Object o)
        {
            if (!(o instanceof Map.Entry))
            {
                return false;
            }
            
            Map.Entry e = (Map.Entry)o;

            return (key == null ? e.getKey() == null : key.equals(e.getKey())) && (value == null ? e.getValue() == null : value.equals(e.getValue()));
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int hashCode()
        {
            return 13 ^ (value == null ? 0 : value.hashCode());
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString()
        {
            return key.toString() + "=" + value.toString();
        }
        
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // User-defined methods
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /**
         * Gets the key.
         * 
         * @return the key
         */
        public K getKey()
        {
            return key;
        }

        /**
         * Gets the value.
         * 
         * @return the value
         */
        public V getValue()
        {
            return value;
        }

        /**
         * Sets the value.
         * 
         * @param pValue the value
         * @return the previous value
         */
        public V setValue(V pValue)
        {
            if (value == null)
            {
                throw new NullPointerException();
            }

            V oldValue = value;
            value = pValue;
            
            return oldValue;
        }
        
    }   // Entry

}	// OrderedHashtable
