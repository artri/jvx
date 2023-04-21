/*
 * Copyright 2023 SIB Visions GmbH
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
 * 05.09.2019 - [JR] - creation
 */
package com.sibvisions.util;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * The original implementation was: https://github.com/gfx/Java-WeakIdentityHashMap see 
 * <a href="https://github.com/gfx/Java-WeakIdentityHashMap/blob/master/library/src/main/java/com/github/gfx/util/WeakIdentityHashMap.java">WeakIdentityHashMap.java</a>
 * 
 * The <code>WeakIdentityHashMap</code> is an implementation of <code>IdentityHashMap</code> with keys which
 * are WeakReferences. A key/value mapping is removed when the key is no longer referenced. 
 * All optional operations (adding and removing) are supported. Keys and values can be any objects. 
 * Note that the garbage collector acts similar to a second thread on this collection, possibly removing keys.
 *
 * @param <K> the key type
 * @param <V> the value type

 * @see java.util.IdentityHashMap
 * @see java.util.WeakHashMap
 * @see java.lang.ref.WeakReference
 */
public class WeakIdentityHashMap<K, V> extends AbstractMap<K, V> 
                                       implements Map<K, V> 
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** default element size. */
	private static final int DEFAULT_SIZE = 16;
	/** default element load factor. */
	private static final float DEFAULT_LOAD_FACTOR = 0.75f;

	/** the reference queue. */
	private final ReferenceQueue<K> referenceQueue;
	
	/** the entries. */
	private Entry<K, V>[] elementData;

	/** the key set. */
	private Set<K> keySet;
	
	/** the values. */
	private Collection<V> valuesCollection;

	/** the element count. */
	private int elementCount;
	
	/** the current load factor. */
	private final int loadFactor;
	
	/** the threshold. */
	private int threshold;
	
	/** the modification count. */
	private volatile int modCount;

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Constructs a new empty {@code WeakIdentityHashMap} instance.
	 */
	public WeakIdentityHashMap() 
	{
		this(DEFAULT_SIZE);
	}

	/**
	 * Constructs a new {@code WeakIdentityHashMap} instance with the specified
	 * capacity.
	 *
	 * @param pCapacity the initial capacity of this map.
	 * @throws IllegalArgumentException if the capacity is less than zero.
	 */
	public WeakIdentityHashMap(int pCapacity) 
	{
		this(pCapacity, DEFAULT_LOAD_FACTOR);
	}

	/**
	 * Constructs a new {@code WeakIdentityHashMap} instance with the specified
	 * capacity and load factor.
	 *
	 * @param pCapacity   the initial capacity of this map.
	 * @param pLoadFactor the initial load factor.
	 * @throws IllegalArgumentException if the capacity is less than zero or the
	 *                                  load factor is less or equal to zero.
	 */
	public WeakIdentityHashMap(int pCapacity, float pLoadFactor) 
	{
		if (pCapacity < 0) 
		{
			throw new IllegalArgumentException("capacity < 0: " + pCapacity);
		}
		
		if (pLoadFactor <= 0) 
		{
			throw new IllegalArgumentException("loadFactor <= 0: " + pLoadFactor);
		}
		
		elementCount = 0;
		elementData = newEntryArray(pCapacity == 0 ? 1 : pCapacity);
		
		loadFactor = (int) (pLoadFactor * 10000);
		
		computeMaxSize();
		
		referenceQueue = new ReferenceQueue<K>();
	}

	/**
	 * Constructs a new {@code WeakIdentityHashMap} instance containing the mappings
	 * from the specified map.
	 *
	 * @param map the mappings to add.
	 */
	public WeakIdentityHashMap(Map<? extends K, ? extends V> map) 
	{
		this(map.size() * 2);
		
		putAll(map);
	}

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Removes all mappings from this map, leaving it empty.
	 *
	 * @see #isEmpty()
	 * @see #size()
	 */
	@Override
	public void clear() 
	{
		if (elementCount > 0) 
		{
			elementCount = 0;
			Arrays.fill(elementData, null);
			modCount++;
			
			while (referenceQueue.poll() != null) 
			{
				// do nothing
			}
		}
	}

	/**
	 * Returns whether this map contains the specified key.
	 *
	 * @param key the key to search for.
	 * @return {@code true} if this map contains the specified key, {@code false}
	 *         otherwise.
	 */
	@Override
	public boolean containsKey(Object key) 
	{
		return getEntry(key) != null;
	}

	/**
	 * Returns a set containing all of the mappings in this map. Each mapping is an
	 * instance of {@link Map.Entry}. As the set is backed by this map, changes in
	 * one will be reflected in the other. It does not support adding operations.
	 *
	 * @return a set of the mappings.
	 */
	@Override
	public Set<Map.Entry<K, V>> entrySet() 
	{
		poll();
		
		return new AbstractSet<Map.Entry<K, V>>() 
		{
			@Override
			public int size() 
			{
				return WeakIdentityHashMap.this.size();
			}

			@Override
			public void clear() 
			{
				WeakIdentityHashMap.this.clear();
			}

			@Override
			public boolean remove(Object object) 
			{
				if (contains(object)) 
				{
					WeakIdentityHashMap.this.remove(((Map.Entry<?, ?>) object).getKey());
					
					return true;
				}
				
				return false;
			}

			@Override
			public boolean contains(Object object) 
			{
				if (object instanceof Map.Entry) 
				{
					Entry<?, ?> entry = getEntry(((Map.Entry<?, ?>) object).getKey());
					
					if (entry != null) 
					{
						Object key = entry.get();
						
						if (key != null || entry.isNull) 
						{
							return object.equals(entry);
						}
					}
				}
				
				return false;
			}

			@Override
			public Iterator<Map.Entry<K, V>> iterator() 
			{
				return new HashIterator<Map.Entry<K, V>>(new Entry.Type<Map.Entry<K, V>, K, V>() 
				{
					public Map.Entry<K, V> get(Map.Entry<K, V> entry) 
					{
						return entry;
					}
				});
			}
		};
	}

	/**
	 * Returns a set of the keys contained in this map. The set is backed by this
	 * map so changes to one are reflected by the other. The set does not support
	 * adding.
	 *
	 * @return a set of the keys.
	 */
	@Override
	public Set<K> keySet() 
	{
		poll();
		
		if (keySet == null) 
		{
			keySet = new AbstractSet<K>() 
			{
				@Override
				public boolean contains(Object object) 
				{
					return containsKey(object);
				}

				@Override
				public int size() 
				{
					return WeakIdentityHashMap.this.size();
				}

				@Override
				public void clear() 
				{
					WeakIdentityHashMap.this.clear();
				}

				@Override
				public boolean remove(Object key) 
				{
					if (containsKey(key)) 
					{
						WeakIdentityHashMap.this.remove(key);
						
						return true;
					}
					return false;
				}

				@Override
				public Iterator<K> iterator() 
				{
					return new HashIterator<K>(new Entry.Type<K, K, V>() 
					{
						public K get(Map.Entry<K, V> entry) 
						{
							return entry.getKey();
						}
					});
				}
			};
		}
		
		return keySet;
	}

	/**
	 * <p>
	 * Returns a collection of the values contained in this map. The collection is
	 * backed by this map so changes to one are reflected by the other. The
	 * collection supports remove, removeAll, retainAll and clear operations, and it
	 * does not support add or addAll operations.
	 * </p>
	 * <p>
	 * This method returns a collection which is the subclass of AbstractCollection.
	 * The iterator method of this subclass returns a "wrapper object" over the
	 * iterator of map's entrySet(). The size method wraps the map's size method and
	 * the contains method wraps the map's containsValue method.
	 * </p>
	 * <p>
	 * The collection is created when this method is called at first time and
	 * returned in response to all subsequent calls. This method may return
	 * different Collection when multiple calls to this method, since it has no
	 * synchronization performed.
	 * </p>
	 * 
	 * @return a collection of the values contained in this map.
	 */
	@Override
	public Collection<V> values() 
	{
		poll();
		
		if (valuesCollection == null) 
		{
			valuesCollection = new AbstractCollection<V>() 
			{
				@Override
				public int size() 
				{
					return WeakIdentityHashMap.this.size();
				}

				@Override
				public void clear() 
				{
					WeakIdentityHashMap.this.clear();
				}

				@Override
				public boolean contains(Object object) 
				{
					return containsValue(object);
				}

				@Override
				public Iterator<V> iterator() 
				{
					return new HashIterator<V>(new Entry.Type<V, K, V>() 
					{
						public V get(Map.Entry<K, V> entry) 
						{
							return entry.getValue();
						}
					});
				}
			};
		}
		
		return valuesCollection;
	} 

	/**
	 * Returns the value of the mapping with the specified key.
	 *
	 * @param key the key.
	 * @return the value of the mapping with the specified key, or {@code null} if
	 *         no mapping for the specified key is found.
	 */
	@Override
	public V get(Object key) 
	{
		Entry<K, V> entry = getEntry(key);
		
		return entry != null ? entry.value : null;
	}

	/**
	 * Returns whether this map contains the specified value.
	 *
	 * @param value the value to search for.
	 * @return {@code true} if this map contains the specified value, {@code false}
	 *         otherwise.
	 */
	@Override
	public boolean containsValue(Object value) 
	{
		poll();
		
		if (value != null) 
		{
			for (int i = elementData.length; --i >= 0;) 
			{
				Entry<K, V> entry = elementData[i];
				
				while (entry != null) 
				{
					K key = entry.get();
					
					if ((key != null || entry.isNull) && value.equals(entry.value)) 
					{
						return true;
					}
					
					entry = entry.next;
				}
			}
		} 
		else 
		{
			for (int i = elementData.length; --i >= 0;) 
			{
				Entry<K, V> entry = elementData[i];
				
				while (entry != null) 
				{
					K key = entry.get();
					
					if ((key != null || entry.isNull) && entry.value == null) 
					{
						return true;
					}
					
					entry = entry.next;
				}
			}
		}
		return false;
	}

	/**
	 * Gets whether the map is empty.
	 * 
	 * @return <code>true</code> if this map is empty. <code>false</code> otherwise.
	 */
	@Override
	public boolean isEmpty() 
	{
		return size() == 0;
	}
	
	/**
	 * Maps the specified key to the specified value.
	 *
	 * @param key   the key.
	 * @param value the value.
	 * @return the value of any previous mapping with the specified key or
	 *         {@code null} if there was no mapping.
	 */
	@Override
	public V put(K key, V value) 
	{
		poll();
		int index = 0;
		Entry<K, V> entry;
		
		if (key != null) 
		{
			index = (System.identityHashCode(key) & 0x7FFFFFFF) % elementData.length;
			entry = elementData[index];
			
			while (entry != null && !(key == entry.get())) 
			{
				entry = entry.next;
			}
		} 
		else 
		{
			entry = elementData[0];
			
			while (entry != null && !entry.isNull) 
			{
				entry = entry.next;
			}
		}
		
		if (entry == null) 
		{
			modCount++;
			
			if (++elementCount > threshold) 
			{
				rehash();
				index = key == null ? 0 : (System.identityHashCode(key) & 0x7FFFFFFF) % elementData.length;
			}
			
			entry = new Entry<K, V>(key, value, referenceQueue);
			entry.next = elementData[index];
			elementData[index] = entry;
			
			return null;
		}
		
		V result = entry.value;
		entry.value = value;
		
		return result;
	}
	

	/**
	 * Removes the mapping with the specified key from this map.
	 *
	 * @param key the key of the mapping to remove.
	 * @return the value of the removed mapping or {@code null} if no mapping for
	 *         the specified key was found.
	 */
	@Override
	public V remove(Object key) 
	{
		poll();
		int index = 0;
		Entry<K, V> entry, last = null;
		
		if (key != null) 
		{
			index = (System.identityHashCode(key) & 0x7FFFFFFF) % elementData.length;
			entry = elementData[index];
			
			while (entry != null && !(key == entry.get())) 
			{
				last = entry;
				entry = entry.next;
			}
		} 
		else 
		{
			entry = elementData[0];
			
			while (entry != null && !entry.isNull) 
			{
				last = entry;
				entry = entry.next;
			}
		}
		
		if (entry != null) 
		{
			modCount++;
			
			if (last == null) 
			{
				elementData[index] = entry.next;
			} 
			else 
			{
				last.next = entry.next;
			}
			elementCount--;
			
			return entry.value;
		}
		return null;
	}

	/**
	 * Gets the number of elements in this map.
	 * 
	 * @return the number of elements.
	 */
	@Override
	public int size() 
	{
		poll();
		
		return elementCount;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new entry array.
	 * 
	 * @param <K> the key type
	 * @param <V> the value type
	 * @param size the number of elements
	 * @return the new array
	 */
	private static <K, V> Entry<K, V>[] newEntryArray(int size) 
	{
		return new Entry[size];
	}

	/**
	 * Computes the maximum element size.
	 */
	private void computeMaxSize() 
	{
		threshold = (int) ((long) elementData.length * loadFactor / 10000);
	}

	/**
	 * Gets the entry for given key.
	 * 
	 * @param key the key
	 * @return the entry
	 */
	private Entry<K, V> getEntry(Object key) 
	{
		poll();
		
		if (key != null) 
		{
			int index = (System.identityHashCode(key) & 0x7FFFFFFF) % elementData.length;
			Entry<K, V> entry = elementData[index];
			
			while (entry != null) 
			{
				if (key == entry.get()) 
				{
					return entry;
				}
				
				entry = entry.next;
			}
			
			return null;
		}
		Entry<K, V> entry = elementData[0];
		
		while (entry != null) 
		{
			if (entry.isNull) 
			{
				return entry;
			}
			
			entry = entry.next;
		}
		
		return null;
	}
	
	/**
	 * Polls the reference queue.
	 */
	private void poll() 
	{
		Entry<K, V> toRemove;
		
		while ((toRemove = (Entry<K, V>) referenceQueue.poll()) != null) 
		{
			removeEntry(toRemove);
		}
	}

	/**
	 * Removes an entry form the element array.
	 * 
	 * @param toRemove the element to remove
	 */
	private void removeEntry(Entry<K, V> toRemove) 
	{
		Entry<K, V> entry, last = null;
		int index = (toRemove.hash & 0x7FFFFFFF) % elementData.length;
		entry = elementData[index];
		
		// Ignore queued entries which cannot be found, the user could
		// have removed them before they were queued, i.e. using clear()
		while (entry != null) 
		{
			if (toRemove == entry) 
			{
				modCount++;
				
				if (last == null) 
				{
					elementData[index] = entry.next;
				} 
				else 
				{
					last.next = entry.next;
				}
				
				elementCount--;
				break;
			}
			last = entry;
			entry = entry.next;
		}
	}

	/**
	 * Rehash the entries.
	 */
	private void rehash() 
	{
		assert elementData.length > 0;
		int length = elementData.length * 2;
		
		Entry<K, V>[] newData = newEntryArray(length);
	
		for (Entry<K, V> entry : elementData) 
		{
			while (entry != null) 
			{
				int index = entry.isNull ? 0 : (entry.hash & 0x7FFFFFFF) % length;
				
				Entry<K, V> next = entry.next;
				entry.next = newData[index];
				newData[index] = entry;
				entry = next;
			}
		}
		
		elementData = newData;
		computeMaxSize();
	}
	
	//****************************************************************
    // Subclass definition
    //****************************************************************

	private static final class Entry<K, V> extends WeakReference<K> 
	                                       implements Map.Entry<K, V> 
	{
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    // Class members
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/** the hash. */
		final int hash;
		
		/** if value is null. */
		boolean isNull;
		
		/** the value. */
		V value;
		
		/** the next entry. */
		Entry<K, V> next;

	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    // Initialization
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Creates a new entry.
		 * 
		 * @param key the key
		 * @param object the value
		 * @param queue the reference queue
		 */
		Entry(K key, V object, ReferenceQueue<K> queue) 
		{
			super(key, queue);
			isNull = key == null;
			hash = isNull ? 0 : System.identityHashCode(key);
			value = object;
		}

	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    // Interface implementation
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * {@inheritDoc}
		 */
		public V getValue() 
		{
			return value;
		}

		/**
		 * {@inheritDoc}
		 */
		public V setValue(V object) 
		{
			V result = value;
			value = object;
			
			return result;
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
			return hash + (value == null ? 0 : value.hashCode());
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean equals(Object other)
		{
			if (!(other instanceof Map.Entry)) 
			{
				return false;
			}
			
			Map.Entry<?, ?> entry = (Map.Entry<?, ?>) other;
			Object key = super.get();
			
			return (key == entry.getKey())
					&& (value == null ? value == entry.getValue() : value.equals(entry.getValue()));
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() 
		{
			return super.get() + "=" + value;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public K getKey() 
		{
			return super.get();
		}

		//****************************************************************
	    // Subclass definition
	    //****************************************************************

		interface Type<R, K, V> 
		{
			/**
			 * Gets the entry.
			 * 
			 * @param entry the entry
			 * @return the entry
			 */
			R get(Map.Entry<K, V> entry);
		}

	}	// Entry
	
	private class HashIterator<R> implements Iterator<R> 
	{
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    // Class members
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/** the position. */
		private int position = 0, expectedModCount;

		/** the next entry. */
		private Entry<K, V> currentEntry, nextEntry;
		
		/** the next key. */
		private K nextKey;
		
		/** the type. */
		private final Entry.Type<R, K, V> type;

	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    // Initialization
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of <code>HashIterator</code>.
		 * 
		 * @param pType the type
		 */
		HashIterator(Entry.Type<R, K, V> pType) 
		{
			type = pType;
			
			expectedModCount = modCount;
		}

	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    // Interface implementation
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * {@inheritDoc}
		 */
		public boolean hasNext() 
		{
			if (nextEntry != null && (nextKey != null || nextEntry.isNull)) 
			{
				return true;
			}
			
			while (true) 
			{
				if (nextEntry == null) 
				{
					while (position < elementData.length) 
					{
						nextEntry = elementData[position++];
								
						if (nextEntry != null) 
						{
							break;
						}
					}
					
					if (nextEntry == null) 
					{
						return false;
					}
				}
				// ensure key of next entry is not gc'ed
				nextKey = nextEntry.get();
				
				if (nextKey != null || nextEntry.isNull) 
				{
					return true;
				}
				
				nextEntry = nextEntry.next;
			}
		}

		/**
		 * {@inheritDoc}
		 */
		public R next() 
		{
			if (expectedModCount == modCount) 
			{
				if (hasNext()) 
				{
					currentEntry = nextEntry;
					nextEntry = currentEntry.next;
					R result = type.get(currentEntry);
					// free the key
					nextKey = null;
					
					return result;
				}
				
				throw new NoSuchElementException();
			}
			
			throw new ConcurrentModificationException();
		}

		/**
		 * {@inheritDoc}
		 */
		public void remove() 
		{
			if (expectedModCount == modCount) 
			{
				if (currentEntry != null) 
				{
					removeEntry(currentEntry);
					currentEntry = null;
					expectedModCount++;
					// cannot poll() as that would change the expectedModCount
				} 
				else 
				{
					throw new IllegalStateException();
				}
			} 
			else 
			{
				throw new ConcurrentModificationException();
			}
		}
		
	}	// HashIterator	
	
}	// WeakIdentityHashMap
