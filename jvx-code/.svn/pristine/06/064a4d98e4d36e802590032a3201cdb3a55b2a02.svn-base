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
 * 11.10.2008 - [RH] - remove bug fixed, offset was missing ! 
 * 24.04.2009 - [JR] - indexOf, equals, lastIndexOf: object compare in both directions:
 *                     objSearch.equals(obj) and obj.equals(objSearch) [BUGFIX]
 * 29.12.2009 - [JR] - containsAll with [] parameter implemented
 * 27.08.2010 - [JR] - removeAll implemented
 * 13.05.2011 - [JR] - #354: toArray(index, length) implemented
 * 30.07.2015 - [JR] - static char support
 */
package com.sibvisions.util;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.util.AbstractList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.RandomAccess;

import com.sibvisions.util.type.CommonUtil;

/**
 * Resizable-array implementation of the <code>List</code> interface.  Implements
 * all optional list operations, and permits all elements, including
 * <code>null</code>.  In addition to implementing the <code>List</code> interface,
 * this class provides methods to manipulate the size of the array that is
 * used internally to store the list.  (This class is roughly equivalent to
 * <code>Vector</code>, except that it is unsynchronized.)<p>
 *
 * This list implementation can also handle arrays of primitive data types.<p>
 * 
 * It is very fast with add operations at the beginning and at the end of the list.
 * A special mechanism reserves space at the beginning and at the end of the list.
 * In usual usages (adding at the beginning or at the end) it is very fast. In
 * average it is 2 times faster than ArrayList.<p>
 *
 * Special static functions helps with the directly handling of arrays.<p> 
 * <code>
 *   int[] node = ArrayUtil.add(new int[] {1, 2, 3}, 4);
 * </code>
 *
 * @author  Martin Handsteiner
 * @see	    java.util.Collection
 * @see	    java.util.List
 * @see	    java.util.ArrayList
 * @see	    java.util.Vector
 * @param <E> placeholder for an object type 
 */
public class ArrayUtil<E> extends AbstractList<E> 
                          implements RandomAccess, 
                                     Cloneable, 
                                     Serializable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** The minimal reserved space in the array. */
	private static final int MIN_SIZE = 16;
	
    /**
     * The array buffer into which the elements of the ArrayList are stored.
     * The capacity of the ArrayUtil is the length of this array buffer.
     * It is an object so also primitive arrays can be handled.
     */
	private E[] array;
	
    /** The first index where the elements are stored. */
	private int offset;
	
    /** The size of the ArrayUtil (the number of elements it contains). */
	private int size;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
     * Constructs an empty list with initial size {@link #MIN_SIZE}.
     */
    public ArrayUtil() 
	{
		this(0);
	}

	/**
     * Constructs an empty list.
     * 
     * @param pInitialSize the initial size.
     */
    public ArrayUtil(int pInitialSize) 
	{
		array  = null;
		offset = pInitialSize - pInitialSize / 2;
		size   = 0;
	}

	/**
     * Constructs a list with the given array. The given array is used directly.
     * If it should be cloned, it has to be done outside manually. 
     *
     * @param pSourceArray the array for this list.
     * @throws NullPointerException if the specified pSourceArray is null.
     */
    public ArrayUtil(E... pSourceArray) 
	{
		this(pSourceArray, 0, -1);
	}

	/**
     * Constructs a list with the given array. The given array is used directly.
     * If it should be cloned, it has to be done outside manually.
     * The list has the given size. If the size is &lt; 0 the size is set to the array length. 
     * If the size is greater than the array length, an IndexOutOfBoundsException is thrown. 
     *
     * @param pSourceArray the array for this list.
     * @param pSize        the size of this list.
     * @throws NullPointerException if the specified pSourceArray is null.
     * @throws IndexOutOfBoundsException if the specified pSize is greater than the array length.
     */
	public ArrayUtil(E[] pSourceArray, int pSize) 
	{
		this(pSourceArray, 0, pSize);
	}

	/**
     * Constructs a list with the given array. The given array is used directly.
     * If it should be cloned, it has to be done outside manually.
     * The list starts at the given offset. The list has the given size. 
     * If the size is &lt; 0 the size is set to the array length. If offset + size is 
     * greater than the array length, an IndexOutOfBoundsException is thrown. 
     *
     * @param pSourceArray the array for this list.
     * @param pOffset      the offset at which the list starts.
     * @param pSize        the size of this list.
     * @throws IndexOutOfBoundsException if the specified pOffset + pSize is greater than the array length.
     */
	public ArrayUtil(E[] pSourceArray, int pOffset, int pSize) 
	{
		array = pSourceArray;
		int length = (array == null) ? 0 : array.length;
		
		if (pSize < 0)
		{
			size = length;
		}
		else
		{
			if (pSize > length)
			{
				throw new IndexOutOfBoundsException("The size is " + pSize + " and should be smaller or equal than length " + length + "!");
			}
			size = pSize;
		}
		if (pOffset < 0 || pOffset + pSize > length) 
		{
			throw new IndexOutOfBoundsException("The offset is " + pOffset + " and should be between 0 and length " + length + " - size " + size + "!");
		}
		offset = pOffset;
	}

    /**
     * Constructs a list containing the elements of the specified
     * collection.
     *
     * @param pCollection the collection whose elements are to be placed into this list.
     * @throws NullPointerException if the specified collection is null.
     */
    public ArrayUtil(Collection<? extends E> pCollection) 
    {
		size = pCollection != null ? pCollection.size() : 0;
		
		if (size == 0)
		{
			array = null;
			offset = 0;
		}
		else
		{
			if (pCollection instanceof ArrayUtil)
			{
				array = ((ArrayUtil<? extends E>)pCollection).array.clone();
				offset = ((ArrayUtil<? extends E>)pCollection).offset;
			}
			else
			{
    			array = (E[])pCollection.toArray();
    			offset = 0;
    		}
    	}
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Increases the capacity of this <code>ArrayUtil</code> instance, if
     * necessary, to ensure  that it can hold at least the number of elements
     * specified by the minimum capacity argument. The buffer space is filled in
     * when needed at the begin or at the end. 
     *
     * @param   pIndex   the index where elements should be inserted.
     * @param   pAmount  the amount that is inserted.
     */
	private void ensureCapacity(int pIndex, int pAmount) 
	{
		if (pIndex < 0 || pIndex > size)
		{
			throw new IndexOutOfBoundsException("The index is " + pIndex + " and should be between 0 and size " + size + "!");
		}
		else if (pIndex < size / 2)
		{
			if (pAmount <= offset)
			{
				if (pIndex > 0)
				{
					System.arraycopy(array, offset, array, offset - pAmount, pIndex);
				}
				offset -= pAmount;
			}
			else
			{
				int newSize = array.length - offset + pAmount;
				int newLength = (array.length < MIN_SIZE) ? MIN_SIZE : array.length;
				while (newLength < newSize)
				{
					newLength += newLength / 2;
				}
				E[] result = (E[])new Object[newLength];
				int newOffset = newLength - newSize;
				if (pIndex < size)
				{
					System.arraycopy(array, offset + pIndex, result, newOffset + pIndex + pAmount, size - pIndex);
				}
				if (pIndex > 0)
				{
					System.arraycopy(array, offset, result, newOffset, pIndex);
				}
				array = result;
				offset = newOffset;
			}
		}
		else
		{   
			int newSize = offset + size + pAmount;
			if (array != null && newSize <= array.length)
			{
				if (pIndex < size)
				{
					System.arraycopy(array, offset + pIndex, array, offset + pIndex + pAmount, size - pIndex);
				}
			}
			else
			{
				int newLength = (array == null) ? (offset > 0) ? offset * 2 : MIN_SIZE 
						                        : (array.length < MIN_SIZE) ? MIN_SIZE : array.length;
				while (newLength < newSize)
				{
					newLength += newLength / 2;
				}
                E[] result;
                try
                {
                    result = (E[])new Object[newLength];
                }
                catch (OutOfMemoryError pOutOfMemoryError)
                {
                    Internalize.clearCache(); // Directly release all WeakReference objects, to release as much memory as possible.
                    
                    result = (E[])new Object[newLength];
                }

				if (array != null)
				{
					if (pIndex > 0)
					{
						System.arraycopy(array, offset, result, offset, pIndex);
					}
					if (pIndex < size)
					{
						System.arraycopy(array, offset + pIndex, result, offset + pIndex + pAmount, size - pIndex);
					}
				}
				array = result;
			}
		}
		modCount++;
		size += pAmount;  
    }

	/**
	 * {@inheritDoc}
	 */
	public boolean add(E pElement)
	{
		add(size, pElement);
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	public void add(int pIndex, E pElement)
	{
		ensureCapacity(pIndex, 1);
		array[offset + pIndex] = pElement;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean addAll(Collection<? extends E> pCollection)
	{
		return addAll(size, pCollection);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean addAll(int pIndex, Collection<? extends E> pCollection)
	{
        if (pCollection instanceof ArrayUtil)
        {
        	return addAll(pIndex, ((ArrayUtil<? extends E>)pCollection).array, ((ArrayUtil<?>)pCollection).offset, ((ArrayUtil<?>)pCollection).size);
        }
        else
        {
    		return addAll(pIndex, (E[])pCollection.toArray(), 0, -1);
        }
	}

    /**
     * Appends all of the elements in the specified Array to the end of
     * this list. 
     *
     * @param pArray the elements to be inserted into this list.
     * @return <code>true</code> if this list changed as a result of the call.
     */
	public boolean addAll(E[] pArray)
	{
		return addAll(size, pArray, 0, -1);
	}

    /**
     * Inserts all of the elements in the specified Array into this
     * list, starting at the specified position.  Shifts the element
     * currently at that position (if any) and any subsequent elements to
     * the right (increases their indices).
     *
     * @param pIndex index at which to insert first element
     *		    from the specified collection.
     * @param pArray elements to be inserted into this list.
     * @return <code>true</code> if this list changed as a result of the call.
     * @throws    IndexOutOfBoundsException if index out of range <code>(index
     *		  &lt; 0 || index &gt; size())</code>.
     */
	public boolean addAll(int pIndex, E[] pArray)
	{
		return addAll(pIndex, pArray, 0, -1);
	}

    /**
     * Inserts size elements in the specified Array starting at offset into this
     * list, starting at the specified position.  Shifts the element
     * currently at that position (if any) and any subsequent elements to
     * the right (increases their indices).
     *
     * @param pIndex index at which to insert first element
     *		    from the specified collection.
     * @param pArray elements to be inserted into this list.
     * @param pOffset the offset from that should be copied.
     * @param pSize the size that should be copied.
     * @return <code>true</code> if this list changed as a result of the call.
     * @throws    IndexOutOfBoundsException if index out of range <code>(index
     *		  &lt; 0 || index &gt; size())</code>.
     */
	public boolean addAll(int pIndex, E[] pArray, int pOffset, int pSize)
	{
        if (pSize == 0 || pArray == null)
        {
        	return false;
        }
        else
        {
        	if (pSize < 0)
        	{
        		pSize = pArray.length;
        	}
        	ensureCapacity(pIndex, pSize);
        	System.arraycopy(pArray, pOffset, array, offset + pIndex, pSize);
        	return true;
        }
	}

    /**
     * Merges this collection with given array. The merged collection will only contain unique values.
     *
     * @param pArray the array to merge
     */
    public void merge(E[] pArray)
    {
        Object[] merged;

        E[] result;
        
        if (pArray != null)
        {
            result = (E[])new Object[Math.max(size + pArray.length, MIN_SIZE)];
            
            //we use an object array instead of a method because this code is used in methods with primitive parameter types 
            //and we won't use three methods for one util method!
            merged = new Object[] {array, pArray};
        }
        else
        {
            result = (E[])new Object[array.length];

            merged = new Object[] {array};
        }
        
        E[] element;
        
        int iElementPos = 0;
        
        for (int i = 0; i < merged.length; i++)
        {
            element = (E[])merged[i];
            
            if (element != null)
            {
                for (int j = 0; j < element.length; j++)
                {
                    if (indexOf(result, element[j]) < 0)
                    {
                        result[iElementPos++] = element[j];
                    }
                }
            }
        }
        modCount++;
        offset = 0;
        size = iElementPos;
        array = result;
    }
	
    /**
     * Sets the size. If the new size is greater than the 
     * current size, new <code>null</code> items are added to the end. If the new size is less than the current size, all 
     * components at index <code>pSize</code> and greater are discarded.
     *
     * @param   pSize   the new size of this vector.
     */
	public void setSize(int pSize)
	{
		if (pSize > size)
		{
			ensureCapacity(size, pSize - size);
		}
		else if (pSize < size)
		{
			removeRangeIntern(pSize, size);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void clear()
	{
		modCount++;
		array  = null;
		offset = 0;
		size = 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean contains(Object pObject)
	{
		return indexOf(pObject) >= 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public E get(int pIndex)
	{
		if (pIndex < 0 || pIndex >= size)
		{
			throw new IndexOutOfBoundsException("The index is " + pIndex + " and should be between 0 and size " + size + "!");
		}
		return array[offset + pIndex];
	}

	/**
	 * {@inheritDoc}
	 */
	public int indexOf(Object pObject)
	{
		return indexOf(pObject, 0);
	}

    /**
     * Returns the index in this list of the first occurence of the specified
     * element started at pIndex, or -1 if the list does not contain this element.  
     * More formally, returns the lowest index <code>i</code> such that <code>(o==null ?
     * get(i)==null : o.equals(get(i)))</code>, or -1 if there is no such
     * index.<p>
     *
     * @param pObject element to search for.
     * @param pIndex  index at which the search starts.
     * 
     * @return the index in this List of the first occurence of the specified
     * 	       element started at pIndex, or -1 if the List does not contain this element.
     */
	public int indexOf(Object pObject, int pIndex)
	{
		if (pIndex >= 0)
		{
			Object objSearch;
			Object obj;
			
			int offsetSize = offset + size;
			
			//use the "real" object, out of WeakReferences for searching
			if (pObject instanceof WeakReference<?>)
			{
				objSearch = ((WeakReference<?>)pObject).get();
			}
			else
			{
				objSearch = pObject;
			}
			
			for (int i = offset + pIndex; i < offsetSize; i++)
		    {
		    	obj = array[i];
		    	
		    	if (obj instanceof WeakReference<?>)
		    	{
		    		obj = ((WeakReference<?>)obj).get();
		    	}
		    	
				if (objSearch == obj 
					|| (obj != null && obj.equals(objSearch))
					|| (objSearch != null && objSearch.equals(obj)))
				{
				    return i - offset;
				}
		    }
		}
		return -1;
	}

    /**
     * Returns true if this list contains the reference from the
     * specified element.  
     *
     * @param pObject element to search for.
     * 
     * @return true if this list contains the reference from the
     * specified element. 
     */
	public boolean containsReference(Object pObject)
	{
		return indexOfReference(pObject) >= 0;
	}

    /**
     * Returns the index in this list of the first occurence of the reference from the
     * specified element, or -1 if the list does not contain this element.  
     * More formally, returns the lowest index <code>i</code> such that <code>o==get(i)</code>, 
     * or -1 if there is no such index.<p>
     *
     * @param pObject element to search for.
     * 
     * @return the index in this List of the first occurence of the reference from specified
     * 	       element, or -1 if the List does not contain this element.
     */
	public int indexOfReference(Object pObject)
	{
		return indexOfReference(pObject, 0);
	}
	
    /**
     * Returns the index in this list of the first occurence of the reference from the
     * specified element started at pIndex, or -1 if the list does not contain this element.  
     * More formally, returns the lowest index <code>i</code> such that <code>o==get(i)</code>, 
     * or -1 if there is no such index.<p>
     *
     * @param pObject element to search for.
     * @param pIndex  index at which the search starts.
     * 
     * @return the index in this List of the first occurence of the reference from specified
     * 	       element started at pIndex, or -1 if the List does not contain this element.
     */
	public int indexOfReference(Object pObject, int pIndex)
	{
		if (pIndex >= 0)
		{
			Object objSearch;
			Object obj;
			
			int offsetSize = offset + size;
			
			//use the "real" object, out of WeakReferences for searching
			if (pObject instanceof WeakReference<?>)
			{
				objSearch = ((WeakReference<?>)pObject).get();
			}
			else
			{
				objSearch = pObject;
			}
			
		    for (int i = offset + pIndex; i < offsetSize; i++)
		    {
		    	obj = array[i];
		    	
		    	if (obj instanceof WeakReference<?>)
		    	{
		    		obj = ((WeakReference<?>)obj).get();
		    	}
		    	
				if (objSearch == obj)
				{
				    return i - offset;
				}
		    }
		}
		return -1;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isEmpty()
	{
		return size == 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public int lastIndexOf(Object pObject)
	{
		return lastIndexOf(pObject, -1);
	}
	
    /**
     * Returns the index in this list of the last occurence of the specified
     * element started at pIndex, or -1 if the list does not contain this element.  
     * More formally, returns the lowest index <code>i</code> such that <code>(o==null ?
     * get(i)==null : o.equals(get(i)))</code>, or -1 if there is no such
     * index.<p>
     *
     * @param pObject element to search for.
     * @param pIndex  index at which the search starts.
     * 
     * @return the index in this List of the last occurence of the specified
     * 	       element started at pIndex, or -1 if the List does not contain this element.
     */
	public int lastIndexOf(Object pObject, int pIndex)
	{
		if (pIndex < size)
		{
			if (pIndex < 0)
			{
				pIndex = size - 1;
			}
			
			Object objSearch;
			Object obj;
			
			//use the "real" object, out of WeakReferences for searching
			if (pObject instanceof WeakReference<?>)
			{
				objSearch = ((WeakReference<?>)pObject).get();
			}
			else
			{
				objSearch = pObject;
			}
			
		    for (int i = offset + pIndex; i >= offset; i--)
		    {
		    	obj = array[i];
		    	
		    	if (obj instanceof WeakReference<?>)
		    	{
		    		obj = ((WeakReference<?>)obj).get();
		    	}
		    	
				if (objSearch == obj 
					|| (objSearch != null && objSearch.equals(obj))
					|| (obj != null && obj.equals(objSearch)))
				{
				    return i - offset;
				}
		    }
		}
		return -1;
	}

    /**
     * Returns the index in this list of the last occurence of the reference from the
     * specified element, or -1 if the list does not contain this element.  
     * More formally, returns the lowest index <code>i</code> such that <code>o==get(i)</code>, 
     * or -1 if there is no such index.<p>
     *
     * @param pObject element to search for.
     * 
     * @return the index in this List of the last occurence of the reference from specified
     * 	       element, or -1 if the List does not contain this element.
     */
	public int lastIndexOfReference(Object pObject)
	{
		return lastIndexOfReference(pObject, -1);
	}
	
    /**
     * Returns the index in this list of the first occurence of the reference from the
     * specified element started at pIndex, or -1 if the list does not contain this element.  
     * More formally, returns the lowest index <code>i</code> such that <code>o==get(i)</code>, 
     * or -1 if there is no such index.<p>
     *
     * @param pObject element to search for.
     * @param pIndex  index at which the search starts.
     * 
     * @return the index in this List of the first occurence of the reference from specified
     * 	       element started at pIndex, or -1 if the List does not contain this element.
     */
	public int lastIndexOfReference(Object pObject, int pIndex)
	{
		if (pIndex < size)
		{
			if (pIndex < 0)
			{
				pIndex = size - 1;
			}
			
			Object objSearch;
			Object obj;
			
			//use the "real" object, out of WeakReferences for searching
			if (pObject instanceof WeakReference<?>)
			{
				objSearch = ((WeakReference<?>)pObject).get();
			}
			else
			{
				objSearch = pObject;
			}
			
		    for (int i = offset + pIndex; i >= offset; i--)
		    {
		    	obj = array[i];
		    	
		    	if (obj instanceof WeakReference<?>)
		    	{
		    		obj = ((WeakReference<?>)obj).get();
		    	}
		    	
				if (objSearch == obj)
				{
				    return i - offset;
				}
		    }
		}
		return -1;
	}

    /**
     * Removes the elements between pFromIndex inclusive and pToIndex exclusive of 
     * this <code>ArrayUtil</code> instance.
     * If there is too much space left in the array the empty space will be reduced. 
     *
     * @param pFromIndex index of first element to be removed.
     * @param pToIndex index after last element to be removed.
     */
	public void removeRange(int pFromIndex, int pToIndex)
	{
		if (pFromIndex < 0)
		{
			pFromIndex = size - 1;
		}
		else if (pFromIndex > size)
		{
			throw new IndexOutOfBoundsException("The fromIndex is " + pFromIndex + " and should be smaller or equal than size " + size + "!");
		}
		if (pToIndex < 0)
		{
			pToIndex = size;
		}
		else if (pToIndex > size)
		{
			throw new IndexOutOfBoundsException("The toIndex is " + pToIndex + " and should be smaller or equal than size " + size + "!");
		}
		if (pFromIndex < pToIndex)
		{
		    removeRangeIntern(pFromIndex, pToIndex);
		}
	}
	
    /**
     * Removes the elements between pFromIndex inclusive and pToIndex exclusive of 
     * this <code>ArrayUtil</code> instance.
     * If there is too much space left in the array the empty space will be reduced. 
     *
     * @param pFromIndex index of first element to be removed.
     * @param pToIndex index after last element to be removed.
     */
    private void removeRangeIntern(int pFromIndex, int pToIndex)
    {
		modCount++;
		int amount = pToIndex - pFromIndex;
		int newSize = size - amount;
		int newLength = array.length;
		while (newSize < newLength - newLength / 3 && newLength > MIN_SIZE)
		{
			newLength -= newLength / 3;
		}
		if (newLength == array.length)
		{
			if (pFromIndex < newSize - pFromIndex)
			{
				//moves the left part to the right
				//| 1 2 3 4 5 6 7 8 9 | (size = 9) (remove 4, index=3)
				//-> null | 1 2 3 5 6 7 8 9 | (offset = 1, size = 8)
				if (pFromIndex > 0)
				{
					System.arraycopy(array, offset, array, offset + amount, pFromIndex);
				}
				// set references to null!
				for (int i = offset + amount - 1; i >= offset; i--)
				{
					array[i] = null;
				}
				offset += amount;
			}
			else
			{
				//moves the right part to the left
				//null | 1 2 3 5 6 7 8 9 |(offset = 1, size = 8) (remove 7, index = 5)
				//null | 1 2 3 5 7 8 9 | null
				if (pFromIndex < newSize)
				{
					System.arraycopy(array, offset + pToIndex, array, offset + pFromIndex, newSize - pFromIndex);
				}
				// set references to null!
				for (int i = offset + size - 1; i >= offset + newSize; i--)
				{
					array[i] = null;
				}
			}
		}
		else
		{
			int newOffset = (newLength - newSize) / 2;
			E[] result = (E[])new Object[newLength];
			if (pFromIndex > 0)
			{
				System.arraycopy(array, offset, result, newOffset, pFromIndex);
			}
			if (pFromIndex < newSize)
			{
				System.arraycopy(array, offset + pToIndex, result, newOffset + pFromIndex, newSize - pFromIndex);
			}
			array = result;
			offset = newOffset;
		}
		size = newSize;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean remove(Object pElement)
	{
		int index = indexOf(pElement);
		
		if (index >= 0)
		{
			removeRangeIntern(index, index + 1);
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public E remove(int pIndex)
	{
		E oldElement = get(pIndex);
		removeRangeIntern(pIndex, pIndex + 1);
		return oldElement;
	}

    /**
     * Removes the last element.
     */
	public void removeLast()
	{
		remove(size - 1);
	}

    /**
     * Removes from this collection all of its elements that are contained in
     * the specified array. <p>
     *
     * This implementation iterates over this collection, checking each
     * element returned by the iterator in turn to see if it's contained
     * in the specified array.  If it's so contained, it's removed from
     * this collection with the iterator's <code>remove</code> method.<p>
     *
     * Note that this implementation will throw an
     * <code>UnsupportedOperationException</code> if the iterator returned by the
     * <code>iterator</code> method does not implement the <code>remove</code> method
     * and this collection contains one or more elements in common with the
     * specified collection.
     *
     * @param pArray elements to be removed from this collection.
     * @return <code>true</code> if this collection changed as a result of the
     *         call.
     * @throws UnsupportedOperationException if the <code>removeAll</code> method
     * 	       is not supported by this collection.
     *
     * @see #remove(Object)
     * @see #contains(Object)
     */
	public boolean removeAll(E[] pArray)
	{
		if (pArray == null)
		{
			return false;
		}
		else
		{
			boolean modified = false;
			
			Iterator<?> e = iterator();
			
			while (e.hasNext()) 
			{
			    if (ArrayUtil.indexOf(pArray, e.next()) >= 0) 
			    {
			    	e.remove();
			    	modified = true;
			    }
			}
			
			return modified;			
		}
	}
	
    /**
     * Removes all elements from the given index.
     *
     * @param pIndex   the index starting to remove all elements.
     */
	public void truncate(int pIndex)
	{
		removeRangeIntern(pIndex, size);
	}

	/**
	 * {@inheritDoc}
	 */
	public E set(int pIndex, E pElement)
	{
		E oldElement = get(pIndex);
		array[offset + pIndex] = pElement;
		return oldElement;
	}

	/**
	 * {@inheritDoc}
	 */
	public int size()
	{
		return size;
	}

    /**
     * Returns an enumeration of this collection.  This provides
     * interoperability with legacy APIs that require an enumeration
     * as input.
     *
     * @return an enumeration over this collection.
     * @see Enumeration
     */
	public Enumeration<E> enumeration()
	{
		return Collections.enumeration(this);
	}
	
    /**
     * Returns a shallow copy of this <code>ArrayUtil</code> instance.  (The
     * elements themselves are not copied.)
     *
     * @return  a clone of this <code>ArrayUtil</code> instance.
     */
    public ArrayUtil<E> clone() 
    {
		try 
		{ 
		    ArrayUtil<E> result = (ArrayUtil<E>)super.clone();
		    if (array != null)
		    {
		    	result.array = array.clone();
		    }
		    return result;
		} 
		catch (CloneNotSupportedException e) 
		{ 
		    // this shouldn't happen, since we are Cloneable
		    throw new InternalError();
		}
    }

	/**
	 * {@inheritDoc}
	 */
	public Object[] toArray()
	{
		Object[] result = new Object[size];
		if (array != null)
		{
			System.arraycopy(array, offset, result, 0, size);
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	public <T> T[] toArray(T[] pArray)
	{
        if (pArray.length < size)
        {
        	pArray = (T[])Array.newInstance(pArray.getClass().getComponentType(), size);
        }
		if (array != null)
		{
	        System.arraycopy(array, offset, pArray, 0, size);
		}
        if (pArray.length > size)
        {
        	pArray[size] = null;
        }
        return pArray;
	}
	
	/**
	 * {@inheritDoc}
	 */
    public boolean equals(Object pObject) 
    {
    	if (pObject == this)
    	{
    	    return true;
    	}
    	else if (pObject instanceof ArrayUtil)
    	{
    		ArrayUtil<?> arrayUtil = (ArrayUtil<?>)pObject;
    		if (size != arrayUtil.size)
    		{
    			return false;
    		}
    		else if (size == 0)
    		{
    			return true;
    		}
    		else
    		{
    			int end = offset + size;
    			int i2 = arrayUtil.offset;
    			for (int i = offset; i < end; i++, i2++)
    			{
    				Object o1 = array[i++];
    				Object o2 = arrayUtil.array[i2++];
    				if (!CommonUtil.equals(o1, o2))
    				{
    					return false;
    				}
    			}
    			return true;
    		}
    	}
    	else if (pObject instanceof Collection)
    	{
    		Collection<?> collection = (Collection<?>)pObject;
    		if (size != collection.size())
    		{
    			return false;
    		}
    		else if (size == 0)
    		{
    			return true;
    		}
    		else
    		{
    			Iterator<?> it = collection.iterator();
    			int end = offset + size;
    			for (int i = offset; i < end; i++)
    			{
    				Object o1 = array[i];
    				Object o2 = it.next();
    				if (!CommonUtil.equals(o1, o2))
    				{
    					return false;
    				}
    			}
    			return true;
    		}
    	}
    	else
    	{
    		return false;
    	}
    }

	/**
	 * {@inheritDoc}
	 */
    public int hashCode() 
    {
    	int hashCode = 0;
    	int end = offset + size;
    	Object obj;
        if (size < 16) 
        {
          for (int i = offset; i < end; i++) 
          {
        	  hashCode *= 37;
        	  obj = array[i];
        	  if (obj != null) 
       		  {
        		  hashCode += obj.hashCode();
       		  }
          }
        }
        else 
        {
          int skip = size / 16;
          for (int i = offset; i < end; i += skip) 
          {
        	  hashCode *= 41;
        	  obj = array[i];
        	  if (obj != null) 
       		  {
        		  hashCode += obj.hashCode();
       		  }
          }
        }
        return hashCode;
    }

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// static User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Inserts the specified element at the specified position in the given
     * array. Shifts the element currently at that position (if any) and
     * any subsequent elements to the right (adds one to their indices).
     * If the index is negative the element is added at the end.
     *
     * @param <T> Component Type of Array.
     * @param pSourceArray array in which to add.
     * @param pIndex index at which the specified element is to be inserted.
     * @param pElement element to be inserted.
     * @return returns a new array if needed.
     * @throws    IndexOutOfBoundsException if index is out of range
     *		  <code>(index &gt; size())</code>.
     */
	public static <T> T[] add(T[] pSourceArray, int pIndex, T pElement)
	{
		int length = (pSourceArray == null) ? 0 : pSourceArray.length;
		if (pIndex < 0)
		{
			pIndex = length;
		}
		else if (pIndex > length)
		{
			throw new IndexOutOfBoundsException("The index is " + pIndex + ", should be smaller or equal to length " + length + "!");
		}
		T[] result;
		if (pSourceArray == null)
		{
			result = (T[])new Object[length + 1];
		}
		else
		{
			result = (T[])Array.newInstance(pSourceArray.getClass().getComponentType(), length + 1);
		}
		if (pIndex > 0)
		{
			System.arraycopy(pSourceArray, 0, result, 0, pIndex);
		}
		if (pIndex < length)
		{
			System.arraycopy(pSourceArray, pIndex, result, pIndex + 1, length - pIndex);
		}
		
		result[pIndex] = pElement;
		return result;
	}
	
    /**
     * Inserts the specified element at the end in the given
     * array.
     *
     * @param <T> Component Type of Array.
     * @param pSourceArray array in which to add.
     * @param pElement element to be inserted.
     * @return returns a new array if needed.
     */
	public static <T> T[] add(T[] pSourceArray, T pElement)
	{
		return add(pSourceArray, -1, pElement);
	}
	
	/**
     * Inserts the specified array at the specified position in the given
     * array. Shifts the element currently at that position (if any) and
     * any subsequent elements to the right.
     * If the index is negative the element is added at the end.
     *
     * @param <T> Component Type of Array.
     * @param pSourceArray array in which to add.
     * @param pIndex index at which the specified element is to be inserted.
     * @param pArray elements to be inserted.
     * @return returns a new array if needed.
     * @throws IndexOutOfBoundsException if index is out of range
     *		   <code>(index &gt; size())</code>.
     */
    public static <T> T[] addAll(T[] pSourceArray, int pIndex, T[] pArray)
    {
		int length = (pSourceArray == null) ? 0 : pSourceArray.length;
		if (pIndex < 0)
		{
			pIndex = length;
		}
		else if (pIndex > length)
		{
			throw new IndexOutOfBoundsException("The index is " + pIndex + ", should be smaller or equal to length " + length + "!");
		}
    	if (pArray == null || pArray.length == 0)
    	{
    		return pSourceArray;
    	}
    	else
    	{
    		T[] result;
    		if (pSourceArray == null)
    		{
    			result = (T[])Array.newInstance(pArray.getClass().getComponentType(), length + pArray.length);
    		}
    		else
    		{
    			result = (T[])Array.newInstance(pSourceArray.getClass().getComponentType(), length + pArray.length);
    		}
    		
			if (pIndex > 0)
			{
				System.arraycopy(pSourceArray, 0, result, 0, pIndex);
			}
			if (pIndex < length)
			{
				System.arraycopy(pSourceArray, pIndex, result, pIndex + pArray.length, length - pIndex);
			}
    		System.arraycopy(pArray, 0, result, pIndex, pArray.length);
			return result;
    	}
    }
	
    /**
     * Inserts the specified array at the end in the given array. 
     *
     * @param <T> Component Type of Array
     * @param pSourceArray array in which to add
     * @param pArray elements to be inserted
     * @return returns a new array if needed
     */
   	public static <T> T[] addAll(T[] pSourceArray, T[] pArray)
    {
    	return addAll(pSourceArray, -1, pArray);
    }
   	
    /**
     * Merges the values of two arrays. The result will contain unique values.
     *
     * @param pSourceArray the source array
     * @param pArray the array to merge
     * @return the result with unique values
     */
    public static boolean[] merge(boolean[] pSourceArray, boolean[] pArray)
    {
        if (pSourceArray == null && pArray == null)
        {
            return null;
        }
        
        boolean[] full = new boolean[2];

        //we use an object array instead of a method because this code is used in methods with primitive parameter types 
        //and we won't use three methods for one util method!
        Object[] merged = new Object[] {pSourceArray, pArray};
        
        boolean[] element;

        boolean bTrue = false;
        boolean bFalse = false;
        
        int iElementPos = 0;
        
        for (int i = 0; i < merged.length && iElementPos < 2; i++)
        {
            element = (boolean[])merged[i];
            
            if (element != null)
            {
                for (int j = 0; j < element.length && iElementPos < 2; j++)
                {
                    if (element[j])
                    {
                        if (!bTrue)
                        {
                            bTrue = true;
                            full[iElementPos++] = true;
                        }
                    }
                    else
                    {
                        if (!bFalse)
                        {
                            bFalse = true;
                            full[iElementPos++] = false;
                        }
                    }
                }
            }
        }
        
        if (iElementPos == 2)
        {
            return full;
        }
        else
        {
            boolean[] result = new boolean[iElementPos];
            System.arraycopy(full, 0, result, 0, iElementPos);

            return result;
        }        
    }
    
    /**
     * Merges the values of two arrays. The result will contain unique values.
     *
     * @param pSourceArray the source array
     * @param pArray the array to merge
     * @return the result with unique values
     */
    public static float[] merge(float[] pSourceArray, float[] pArray)
    {
        if (pSourceArray == null && pArray == null)
        {
            return null;
        }
        
        float[] full = new float[(pSourceArray != null ? pSourceArray.length : 0) + (pArray != null ? pArray.length : 0)];

        //we use an object array instead of a method because this code is used in methods with primitive parameter types 
        //and we won't use three methods for one util method!
        Object[] merged = new Object[] {pSourceArray, pArray};
        
        float[] element;
        
        int iElementPos = 0;
        
        for (int i = 0; i < merged.length; i++)
        {
            element = (float[])merged[i];
            
            if (element != null)
            {
                for (int j = 0; j < element.length; j++)
                {
                    if (indexOf(full, element[j]) < 0)
                    {
                        full[iElementPos++] = element[j];
                    }
                }
            }
        }
        
        float[] result = new float[iElementPos];
        System.arraycopy(full, 0, result, 0, iElementPos);
        
        return result;
    }

    /**
     * Merges the values of two arrays. The result will contain unique values.
     *
     * @param pSourceArray the source array
     * @param pArray the array to merge
     * @return the result with unique values
     */
    public static double[] merge(double[] pSourceArray, double[] pArray)
    {
        if (pSourceArray == null && pArray == null)
        {
            return null;
        }
        
        double[] full = new double[(pSourceArray != null ? pSourceArray.length : 0) + (pArray != null ? pArray.length : 0)];

        //we use an object array instead of a method because this code is used in methods with primitive parameter types 
        //and we won't use three methods for one util method!
        Object[] merged = new Object[] {pSourceArray, pArray};
        
        double[] element;
        
        int iElementPos = 0;
        
        for (int i = 0; i < merged.length; i++)
        {
            element = (double[])merged[i];
            
            if (element != null)
            {
                for (int j = 0; j < element.length; j++)
                {
                    if (indexOf(full, element[j]) < 0)
                    {
                        full[iElementPos++] = element[j];
                    }
                }
            }
        }
        
        double[] result = new double[iElementPos];
        System.arraycopy(full, 0, result, 0, iElementPos);
        
        return result;
    }
    
    /**
     * Merges the values of two arrays. The result will contain unique values.
     *
     * @param pSourceArray the source array
     * @param pArray the array to merge
     * @return the result with unique values
     */
    public static byte[] merge(byte[] pSourceArray, byte[] pArray)
    {
        if (pSourceArray == null && pArray == null)
        {
            return null;
        }
        
        int[] full = new int[(pSourceArray != null ? pSourceArray.length : 0) + (pArray != null ? pArray.length : 0)];

        //we use an object array instead of a method because this code is used in methods with primitive parameter types 
        //and we won't use three methods for one util method!
        Object[] merged = new Object[] {pSourceArray, pArray};
        
        int[] element;
        
        int iElementPos = 0;
        
        for (int i = 0; i < merged.length; i++)
        {
            element = (int[])merged[i];
            
            if (element != null)
            {
                for (int j = 0; j < element.length; j++)
                {
                    if (indexOf(full, element[j]) < 0)
                    {
                        full[iElementPos++] = element[j];
                    }
                }
            }
        }
        
        byte[] result = new byte[iElementPos];
        System.arraycopy(full, 0, result, 0, iElementPos);
        
        return result;
    }
    
    /**
     * Merges the values of two arrays. The result will contain unique values.
     *
     * @param pSourceArray the source array
     * @param pArray the array to merge
     * @return the result with unique values
     */
    public static int[] merge(int[] pSourceArray, int[] pArray)
    {
        if (pSourceArray == null && pArray == null)
        {
            return null;
        }
        
        int[] full = new int[(pSourceArray != null ? pSourceArray.length : 0) + (pArray != null ? pArray.length : 0)];

        //we use an object array instead of a method because this code is used in methods with primitive parameter types 
        //and we won't use three methods for one util method!
        Object[] merged = new Object[] {pSourceArray, pArray};
        
        int[] element;
        
        int iElementPos = 0;
        
        for (int i = 0; i < merged.length; i++)
        {
            element = (int[])merged[i];
            
            if (element != null)
            {
                for (int j = 0; j < element.length; j++)
                {
                    if (indexOf(full, element[j]) < 0)
                    {
                        full[iElementPos++] = element[j];
                    }
                }
            }
        }
        
        int[] result = new int[iElementPos];
        System.arraycopy(full, 0, result, 0, iElementPos);
        
        return result;
    }
    
    /**
     * Merges the values of two arrays. The result will contain unique values.
     *
     * @param pSourceArray the source array
     * @param pArray the array to merge
     * @return the result with unique values
     */
    public static long[] merge(long[] pSourceArray, long[] pArray)
    {
        if (pSourceArray == null && pArray == null)
        {
            return null;
        }
        
        long[] full = new long[(pSourceArray != null ? pSourceArray.length : 0) + (pArray != null ? pArray.length : 0)];

        //we use an object array instead of a method because this code is used in methods with primitive parameter types 
        //and we won't use three methods for one util method!
        Object[] merged = new Object[] {pSourceArray, pArray};
        
        long[] element;
        
        int iElementPos = 0;
        
        for (int i = 0; i < merged.length; i++)
        {
            element = (long[])merged[i];
            
            if (element != null)
            {
                for (int j = 0; j < element.length; j++)
                {
                    if (indexOf(full, element[j]) < 0)
                    {
                        full[iElementPos++] = element[j];
                    }
                }
            }
        }
        
        long[] result = new long[iElementPos];
        System.arraycopy(full, 0, result, 0, iElementPos);
        
        return result;
    }    
    
    /**
     * Merges the values of two arrays. The result will contain unique values.
     *
     * @param pSourceArray the source array
     * @param pArray the array to merge
     * @return the result with unique values
     */
    public static char[] merge(char[] pSourceArray, char[] pArray)
    {
        if (pSourceArray == null && pArray == null)
        {
            return null;
        }
        
        char[] full = new char[(pSourceArray != null ? pSourceArray.length : 0) + (pArray != null ? pArray.length : 0)];

        //we use an object array instead of a method because this code is used in methods with primitive parameter types 
        //and we won't use three methods for one util method!
        Object[] merged = new Object[] {pSourceArray, pArray};
        
        char[] element;
        
        int iElementPos = 0;
        
        for (int i = 0; i < merged.length; i++)
        {
            element = (char[])merged[i];
            
            if (element != null)
            {
                for (int j = 0; j < element.length; j++)
                {
                    if (indexOf(full, element[j]) < 0)
                    {
                        full[iElementPos++] = element[j];
                    }
                }
            }
        }
        
        char[] result = new char[iElementPos];
        System.arraycopy(full, 0, result, 0, iElementPos);
        
        return result;
    }
    
   	/**
   	 * Merges the values of two arrays. The result will contain unique values.
   	 *
   	 * @param <T> Component Type of Array
   	 * @param pSourceArray the source array
   	 * @param pArray the array to merge
   	 * @return the result with unique values
   	 */
   	public static <T> T[] merge(T[] pSourceArray, T[] pArray)
   	{
        if (pSourceArray == null && pArray == null)
        {
            return null;
        }
        
        T[] full = (T[])Array.newInstance(pSourceArray != null ? pSourceArray.getClass().getComponentType() : pArray.getClass().getComponentType(), 
                                          (pSourceArray != null ? pSourceArray.length : 0) + (pArray != null ? pArray.length : 0));

        //we use an object array instead of a method because this code is used in methods with primitive parameter types 
        //and we won't use three methods for one util method!
        Object[] merged = new Object[] {pSourceArray, pArray};
        
        T[] element;
        
        int iElementPos = 0;
        
        for (int i = 0; i < merged.length; i++)
        {
            element = (T[])merged[i];
            
            if (element != null)
            {
                for (int j = 0; j < element.length; j++)
                {
                    if (indexOf(full, element[j]) < 0)
                    {
                        full[iElementPos++] = element[j];
                    }
                }
            }
        }
   	    
        T[] result = (T[])Array.newInstance(full.getClass().getComponentType(), iElementPos);
        System.arraycopy(full, 0, result, 0, iElementPos);
        
        return result;
   	}
   	
    /**
     * Removes all elements between fromIndex (inclusive) and toIndex (exclusive of given array).
     *
     * @param <T> Component Type of Array.
     * @param pSourceArray array in which to delete.
     * @param pFromIndex   the first index.
     * @param pToIndex     the last index (not included).
     * @return returns a new array if needed.
     */
	public static <T> T[] removeRange(T[] pSourceArray, int pFromIndex, int pToIndex)
	{
		int length = (pSourceArray == null) ? 0 : pSourceArray.length;
		if (pFromIndex < 0)
		{
			pFromIndex = length - 1;
		}
		else if (pFromIndex > length)
		{
			throw new IndexOutOfBoundsException("The fromIndex is " + pFromIndex + " and should be smaller or equal than " + length + "!");
		}
		if (pToIndex < 0)
		{
			pToIndex = length;
		}
		else if (pToIndex < pFromIndex || pToIndex > length)
		{
			throw new IndexOutOfBoundsException("The toIndex is " + pToIndex + " and should be between fromIndex + " + pFromIndex + " and length " + length + "!");
		}
		if (pFromIndex == pToIndex)
		{
			return pSourceArray;
		}
		else
		{
			int amount = pToIndex - pFromIndex;
			T[] result = (T[])Array.newInstance(pSourceArray.getClass().getComponentType(), length - amount);
			if (pFromIndex > 0)
			{
				System.arraycopy(pSourceArray, 0, result, 0, pFromIndex);
			}
			if (pToIndex < length)
			{
				System.arraycopy(pSourceArray, pToIndex, result, pFromIndex, length - pToIndex);
			}
			return result;
		}
	}
	
    /**
     * Removes one element at the given index of given array.
     *
     * @param <T> Component Type of Array.
     * @param pSourceArray array in which to delete.
     * @param pIndex   the index where elements should be deleted.
     * @return returns a new array if needed.
     */
	public static <T> T[] remove(T[] pSourceArray, int pIndex)
	{
		return removeRange(pSourceArray, pIndex, pIndex + 1);
	}
	
    /**
     * Removes the last element of given array.
     *
     * @param <T> Component Type of Array.
     * @param pSourceArray array in which to delete.
     * @return returns a new array if needed.
     */
	public static <T> T[] removeLast(T[] pSourceArray)
	{
		return removeRange(pSourceArray, -1, -1);
	}
	
    /**
     * Removes the given element of given array.
     *
     * @param <T> Component Type of Array.
     * @param pSourceArray array in which to delete.
     * @param pObject   the given element.
     * @return returns a new array if needed.
     */
	public static <T> T[] remove(T[] pSourceArray, T pObject)
	{
		int index = indexOf(pSourceArray, pObject);
		
		if (index < 0)
		{
			return pSourceArray;
		}
		else
		{
			return removeRange(pSourceArray, index, index + 1);
		}
	}
	
    /**
     * Removes the given element of given array.
     *
     * @param <T> component Type of Array.
     * @param pSourceArray array in which to delete.
     * @param pObject   the given element.
     * @return returns a new array if needed.
     */
	public static <T> T[] removeReference(T[] pSourceArray, T pObject)
	{
		int index = indexOfReference(pSourceArray, pObject);
		
		if (index < 0)
		{
			return pSourceArray;
		}
		else
		{
			return removeRange(pSourceArray, index, index + 1);
		}
	}
	
    /**
     * Removes the elements of a given array from the given source array. 
     *
     *@param <T> component type of array.
     * @param pSourceArray the original elements.
     * @param pArray elements to be removed.
     * @return the source array without removed elements.
     */
	public static <T> T[] removeAll(T[] pSourceArray, T[] pArray)
	{
		if (pSourceArray == null)
		{
			return null;
		}

		T[] result = (T[])Array.newInstance(pSourceArray.getClass().getComponentType(), pSourceArray.length);
		
		if (pArray == null)
		{
			System.arraycopy(pSourceArray, 0, result, 0, pSourceArray.length);
			
			return result;
		}
		
		int i = 0;
		for (T element : pSourceArray)
		{
			if (ArrayUtil.indexOf(pArray, element) < 0)
			{
				result[i++] = element;
			}
		}
	
		if (i < pSourceArray.length)
		{
			T[] resize = (T[])Array.newInstance(pSourceArray.getClass().getComponentType(), i);
			
			System.arraycopy(result, 0, resize, 0, i);
			
			return resize;
		}
		else
		{
			return result;
		}
	}
	
    /**
     * Creates an array which contains all elements that are available in both arrays. 
     * If the second array is null, a clone of the first array is returned. 
     *
     *@param <T> component type of array.
     * @param pFirstArray the first set of elements.
     * @param pSecondArray the second set of elements.
     * @return the new array with intersecting elements
     */
	public static <T> T[] intersect(T[] pFirstArray, T[] pSecondArray)
	{
		if (pFirstArray == null)
		{
			return null;
		}

		T[] result = (T[])Array.newInstance(pFirstArray.getClass().getComponentType(), pFirstArray.length);
		
		if (pSecondArray == null)
		{
			System.arraycopy(pFirstArray, 0, result, 0, pFirstArray.length);
			
			return result;
		}
		
		int i = 0;
		for (T element : pFirstArray)
		{
			if (ArrayUtil.indexOf(pSecondArray, element) >= 0 && ArrayUtil.indexOf(result, element, 0, i) < 0)
			{
				result[i++] = element;
			}
		}
	
		if (i < pFirstArray.length)
		{
			T[] resize = (T[])Array.newInstance(pFirstArray.getClass().getComponentType(), i);
			
			System.arraycopy(result, 0, resize, 0, i);
			
			return resize;
		}
		else
		{
			return result;
		}
	}
	
    /**
     * Removes all elements from the given index of given array.
     *
     * @param <T> Component Type of Array.
     * @param pSourceArray array in which to delete.
     * @param pIndex   the index starting to remove all elements.
     * @return returns a new array if needed.
     */
	public static <T> T[] truncate(T[] pSourceArray, int pIndex)
	{
		return removeRange(pSourceArray, pIndex, -1);
	}

    /**
     * Removes all Elements.
     *
     * @param <T> Component Type of Array.
     * @param pSourceArray array in which to delete.
     * @return returns a new array if needed.
     */
	public static <T> T[] clear(T[] pSourceArray)
	{
		return removeRange(pSourceArray, 0, -1);
	}

    /**
     * Returns true if this list contains the specified
     * element.  
     *
     * @param <T> Component Type of Array.
     * @param pSourceArray array in which to search.
     * @param pObject element to search for.
     * 
     * @return true if this list contains the specified
     * element.
     */
	public static <T> boolean contains(T[] pSourceArray, T pObject)
	{
		return indexOf(pSourceArray, pObject, 0) >= 0;
	}

    /**
     * Returns the index in this list of the first occurence of the specified
     * element, or -1 if the list does not contain this element.  
     * More formally, returns the lowest index <code>i</code> such that <code>(o==null ?
     * get(i)==null : o.equals(get(i)))</code>, or -1 if there is no such
     * index.<p>
     *
     * @param <T> Component Type of Array.
     * @param pSourceArray array in which to search.
     * @param pObject element to search for.
     * 
     * @return the index in this List of the first occurence of the specified
     * 	       element, or -1 if the List does not contain this element.
     */
	public static <T> int indexOf(T[] pSourceArray, T pObject)
	{
		return indexOf(pSourceArray, pObject, 0, pSourceArray == null ? 0 : pSourceArray.length);
	}

    /**
     * Returns the index in this list of the first occurence of the specified
     * element started at pIndex, or -1 if the list does not contain this element.  
     * More formally, returns the lowest index <code>i</code> such that <code>(o==null ?
     * get(i)==null : o.equals(get(i)))</code>, or -1 if there is no such
     * index.<p>
     *
     * @param <T> Component Type of Array.
     * @param pSourceArray array in which to search.
     * @param pObject element to search for.
     * @param pIndex  index at which the search starts.
     * 
     * @return the index in this List of the first occurence of the specified
     * 	       element started at pIndex, or -1 if the List does not contain this element.
     */
	public static <T> int indexOf(T[] pSourceArray, T pObject, int pIndex)
	{
		return indexOf(pSourceArray, pObject, pIndex, pSourceArray == null ? 0 : pSourceArray.length);
	}
	
    /**
     * Returns the index in this list of the first occurence of the specified
     * element started at pIndex, or -1 if the list does not contain this element.  
     * More formally, returns the lowest index <code>i</code> such that <code>(o==null ?
     * get(i)==null : o.equals(get(i)))</code>, or -1 if there is no such
     * index.<p>
     *
     * @param <T> Component Type of Array.
     * @param pSourceArray array in which to search.
     * @param pObject element to search for.
     * @param pIndex  index at which the search starts.
     * @param pLength  length of the source array
     * 
     * @return the index in this List of the first occurence of the specified
     * 	       element started at pIndex, or -1 if the List does not contain this element.
     */
	public static <T> int indexOf(T[] pSourceArray, T pObject, int pIndex, int pLength)
	{
		if (pIndex >= 0)
		{
		    for (int i = pIndex; i < pLength; i++)
		    {
		    	if (CommonUtil.equals(pObject, pSourceArray[i]))
		    	{
		    		return i;
		    	}
		    }
		}
		return -1;
	}

    /**
     * Returns true if this list contains the reference of the specified
     * element.  
     *
     * @param <T> Component Type of Array.
     * @param pSourceArray array in which to search.
     * @param pObject element to search for.
     * 
     * @return true if this list contains the reference of the specified
     * element.  
     */
	public static <T> boolean containsReference(T[] pSourceArray, T pObject)
	{
		return indexOfReference(pSourceArray, pObject, 0) >= 0;
	}

    /**
     * Returns the index in this list of the first occurence of the reference from the
     * specified element, or -1 if the list does not contain this element.  
     * More formally, returns the lowest index <code>i</code> such that <code>o==get(i)</code>, 
     * or -1 if there is no such index.<p>
     *
     * @param <T> Component Type of Array.
     * @param pSourceArray array in which to search.
     * @param pObject element to search for.
     * 
     * @return the index in this List of the first occurence of the reference from specified
     * 	       element, or -1 if the List does not contain this element.
     */
	public static <T> int indexOfReference(T[] pSourceArray, T pObject)
	{
		return indexOfReference(pSourceArray, pObject, 0);
	}
	
    /**
     * Returns the index in this list of the first occurence of the reference from the
     * specified element started at pIndex, or -1 if the list does not contain this element.  
     * More formally, returns the lowest index <code>i</code> such that <code>o==get(i)</code>, 
     * or -1 if there is no such index.<p>
     *
     * @param <T> Component Type of Array.
     * @param pSourceArray array in which to search.
     * @param pObject element to search for.
     * @param pIndex  index at which the search starts.
     * 
     * @return the index in this List of the first occurence of the reference from specified
     * 	       element started at pIndex, or -1 if the List does not contain this element.
     */
	public static <T> int indexOfReference(T[] pSourceArray, T pObject, int pIndex)
	{
		if (pIndex >= 0)
		{
			Object objSearch;
			Object obj;
			
			int length = (pSourceArray == null) ? 0 : pSourceArray.length;
			
			//use the "real" object, out of WeakReferences for searching
			if (pObject instanceof WeakReference<?>)
			{
				objSearch = ((WeakReference<?>)pObject).get();
			}
			else
			{
				objSearch = pObject;
			}
			
		    for (int i = pIndex; i < length; i++)
		    {
		    	obj = pSourceArray[i];
		    	
		    	if (obj instanceof WeakReference<?>)
		    	{
		    		obj = ((WeakReference<?>)obj).get();
		    	}
		    	
				if (objSearch == obj)
				{
				    return i;
				}
		    }
		}
		return -1;
	}
	
    /**
     * Returns the index in this list of the last occurence of the specified
     * element, or -1 if the list does not contain this element.  
     * More formally, returns the lowest index <code>i</code> such that <code>(o==null ?
     * get(i)==null : o.equals(get(i)))</code>, or -1 if there is no such
     * index.<p>
     *
     * @param <T> Component Type of Array.
     * @param pSourceArray array in which to search.
     * @param pObject element to search for.
     * 
     * @return the index in this List of the last occurence of the specified
     * 	       element, or -1 if the List does not contain this element.
     */
	public static <T> int lastIndexOf(T[] pSourceArray, T pObject)
	{
		return lastIndexOf(pSourceArray, pObject, -1);
	}
	
    /**
     * Returns the index in this list of the last occurence of the specified
     * element started at pIndex, or -1 if the list does not contain this element.  
     * More formally, returns the lowest index <code>i</code> such that <code>(o==null ?
     * get(i)==null : o.equals(get(i)))</code>, or -1 if there is no such
     * index.<p>
     *
     * @param <T> Component Type of Array.
     * @param pSourceArray array in which to search.
     * @param pObject element to search for.
     * @param pIndex  index at which the search starts.
     * 
     * @return the index in this List of the last occurence of the specified
     * 	       element started at pIndex, or -1 if the List does not contain this element.
     */
	public static <T> int lastIndexOf(T[] pSourceArray, T pObject, int pIndex)
	{
		int length = (pSourceArray == null) ? 0 : pSourceArray.length;
		if (pIndex < length)
		{
			if (pIndex < 0)
			{
				pIndex = length - 1;
			}
			
			Object objSearch;
			Object obj;
			
			//use the "real" object, out of WeakReferences for searching
			if (pObject instanceof WeakReference<?>)
			{
				objSearch = ((WeakReference<?>)pObject).get();
			}
			else
			{
				objSearch = pObject;
			}
			
		    for (int i = pIndex; i >= 0; i--)
		    {
		    	obj = pSourceArray[i];
		    	
		    	if (obj instanceof WeakReference<?>)
		    	{
		    		obj = ((WeakReference<?>)obj).get();
		    	}
		    	
				if (objSearch == obj 
					|| (objSearch != null && objSearch.equals(obj))
					|| (obj != null && obj.equals(objSearch)))
				{
				    return i;
				}
		    }
		}
		return -1;
	}

    /**
     * Returns the index in this list of the first occurence of the reference from the
     * specified element, or -1 if the list does not contain this element.  
     * More formally, returns the lowest index <code>i</code> such that <code>o==get(i)</code>, 
     * or -1 if there is no such index.<p>
     *
     * @param <T> Component Type of the List.
     * @param pSourceList list in which to search.
     * @param pObject element to search for.
     * 
     * @return the index in this List of the first occurence of the reference from specified
     * 	       element, or -1 if the List does not contain this element.
     */
	public static <T> int indexOfReference(List<T> pSourceList, T pObject)
	{
		return indexOfReference(pSourceList, pObject, 0);
	}
	
    /**
     * Returns the index in this list of the first occurence of the reference from the
     * specified element started at pIndex, or -1 if the list does not contain this element.  
     * More formally, returns the lowest index <code>i</code> such that <code>o==get(i)</code>, 
     * or -1 if there is no such index.<p>
     *
     * @param <T> Component Type of the List.
     * @param pSourceList list in which to search.
     * @param pObject element to search for.
     * @param pIndex  index at which the search starts.
     * 
     * @return the index in this List of the first occurence of the reference from specified
     * 	       element started at pIndex, or -1 if the List does not contain this element.
     */
	public static <T> int indexOfReference(List<T> pSourceList, T pObject, int pIndex)
	{
		if (pIndex >= 0)
		{
			Object objSearch;
			Object obj;
			
			int length = (pSourceList == null) ? 0 : pSourceList.size();
			
			//use the "real" object, out of WeakReferences for searching
			if (pObject instanceof WeakReference<?>)
			{
				objSearch = ((WeakReference<?>)pObject).get();
			}
			else
			{
				objSearch = pObject;
			}
			
		    for (int i = pIndex; i < length; i++)
		    {
		    	obj = pSourceList.get(i);
		    	
		    	if (obj instanceof WeakReference<?>)
		    	{
		    		obj = ((WeakReference<?>)obj).get();
		    	}
		    	
				if (objSearch == obj)
				{
				    return i;
				}
		    }
		}
		return -1;
	}
	
    /**
     * Returns the index in this list of the last occurence of the specified
     * element, or -1 if the list does not contain this element.  
     * More formally, returns the lowest index <code>i</code> such that <code>(o==null ?
     * get(i)==null : o.equals(get(i)))</code>, or -1 if there is no such
     * index.<p>
     *
     * @param <T> Component Type of the List.
     * @param pSourceList list in which to search.
     * @param pObject element to search for.
     * 
     * @return the index in this List of the last occurence of the specified
     * 	       element, or -1 if the List does not contain this element.
     */
	public static <T> int lastIndexOf(List<T> pSourceList, T pObject)
	{
		return lastIndexOf(pSourceList, pObject, -1);
	}
	
    /**
     * Returns the index in this list of the last occurence of the specified
     * element started at pIndex, or -1 if the list does not contain this element.  
     * More formally, returns the lowest index <code>i</code> such that <code>(o==null ?
     * get(i)==null : o.equals(get(i)))</code>, or -1 if there is no such
     * index.<p>
     *
     * @param <T> Component Type of the List.
     * @param pSourceList list in which to search.
     * @param pObject element to search for.
     * @param pIndex  index at which the search starts.
     * 
     * @return the index in this List of the last occurence of the specified
     * 	       element started at pIndex, or -1 if the List does not contain this element.
     */
	public static <T> int lastIndexOf(List<T> pSourceList, T pObject, int pIndex)
	{
		int length = (pSourceList == null) ? 0 : pSourceList.size();
		if (pIndex < length)
		{
			if (pIndex < 0)
			{
				pIndex = length - 1;
			}
			
			Object objSearch;
			Object obj;
			
			//use the "real" object, out of WeakReferences for searching
			if (pObject instanceof WeakReference<?>)
			{
				objSearch = ((WeakReference<?>)pObject).get();
			}
			else
			{
				objSearch = pObject;
			}
			
		    for (int i = pIndex; i >= 0; i--)
		    {
		    	obj = pSourceList.get(i);
		    	
		    	if (obj instanceof WeakReference<?>)
		    	{
		    		obj = ((WeakReference<?>)obj).get();
		    	}
		    	
				if (objSearch == obj 
					|| (objSearch != null && objSearch.equals(obj))
					|| (obj != null && obj.equals(objSearch)))
				{
				    return i;
				}
		    }
		}
		return -1;
	}

    /**
     * Returns the index in this list of the last occurence of the reference from the
     * specified element, or -1 if the list does not contain this element.  
     * More formally, returns the lowest index <code>i</code> such that <code>o==get(i)</code>, 
     * or -1 if there is no such index.<p>
     *
     * @param <T> Component Type of Array.
     * @param pSourceArray array in which to search.
     * @param pObject element to search for.
     * 
     * @return the index in this List of the last occurence of the reference from specified
     * 	       element, or -1 if the List does not contain this element.
     */
	public static <T> int lastIndexOfReference(T[] pSourceArray, T pObject)
	{
		return lastIndexOfReference(pSourceArray, pObject, -1);
	}
	
    /**
     * Returns the index in this list of the first occurence of the reference from the
     * specified element started at pIndex, or -1 if the list does not contain this element.  
     * More formally, returns the lowest index <code>i</code> such that <code>o==get(i)</code>, 
     * or -1 if there is no such index.<p>
     *
     * @param <T> Component Type of Array.
     * @param pSourceArray array in which to search.
     * @param pObject element to search for.
     * @param pIndex  index at which the search starts.
     * 
     * @return the index in this List of the first occurence of the reference from specified
     * 	       element started at pIndex, or -1 if the List does not contain this element.
     */
	public static <T> int lastIndexOfReference(T[] pSourceArray, T pObject, int pIndex)
	{
		int length = (pSourceArray == null) ? 0 : pSourceArray.length;
		if (pIndex < length)
		{
			if (pIndex < 0)
			{
				pIndex = length - 1;
			}
			
			Object objSearch;
			Object obj;
			
			//use the "real" object, out of WeakReferences for searching
			if (pObject instanceof WeakReference<?>)
			{
				objSearch = ((WeakReference<?>)pObject).get();
			}
			else
			{
				objSearch = pObject;
			}
			
		    for (int i = pIndex; i >= 0; i--)
		    {
		    	obj = pSourceArray[i];
		    	
		    	if (obj instanceof WeakReference<?>)
		    	{
		    		obj = ((WeakReference<?>)obj).get();
		    	}
		    	
				if (objSearch == obj)
				{
				    return i;
				}
		    }
		}
		return -1;
	}
	
    /**
     * Inserts the specified element at the specified position in the given
     * array. Shifts the element currently at that position (if any) and
     * any subsequent elements to the right (adds one to their indices).
     * If the index is negative the element is added at the end.
     *
     * @param pSourceArray array in which to add.
     * @param pIndex index at which the specified element is to be inserted.
     * @param pElement element to be inserted.
     * @return returns a new array if needed.
     * @throws    IndexOutOfBoundsException if index is out of range
     *		  <code>(index &gt; size())</code>.
     */
	public static boolean[] add(boolean[] pSourceArray, int pIndex, boolean pElement)
	{
		int length = (pSourceArray == null) ? 0 : pSourceArray.length;
		if (pIndex < 0)
		{
			pIndex = length;
		}
		else if (pIndex > length)
		{
			throw new IndexOutOfBoundsException("The index is " + pIndex + ", should be smaller or equal to length " + length + "!");
		}
		boolean[] result = new boolean[length + 1];
		if (pIndex > 0)
		{
			System.arraycopy(pSourceArray, 0, result, 0, pIndex);
		}
		if (pIndex < length)
		{
			System.arraycopy(pSourceArray, pIndex, result, pIndex + 1, length - pIndex);
		}
		result[pIndex] = pElement;
		return result;
	}
	
    /**
     * Inserts the specified element at the end in the given
     * array.
     *
     * @param pSourceArray array in which to add.
     * @param pElement element to be inserted.
     * @return returns a new array if needed.
     */
	public static boolean[] add(boolean[] pSourceArray, boolean pElement)
	{
		return add(pSourceArray, -1, pElement);
	}
	
	/**
     * Inserts the specified array at the specified position in the given
     * array. Shifts the element currently at that position (if any) and
     * any subsequent elements to the right.
     * If the index is negative the element is added at the end.
     *
     * @param pSourceArray array in which to add.
     * @param pIndex index at which the specified element is to be inserted.
     * @param pArray elements to be inserted.
     * @return returns a new array if needed.
     * @throws IndexOutOfBoundsException if index is out of range
     *		   <code>(index &gt; size())</code>.
     */
    public static boolean[] addAll(boolean[] pSourceArray, int pIndex, boolean[] pArray)
    {
		int length = (pSourceArray == null) ? 0 : pSourceArray.length;
		if (pIndex < 0)
		{
			pIndex = length;
		}
		else if (pIndex > length)
		{
			throw new IndexOutOfBoundsException("The index is " + pIndex + ", should be smaller or equal to length " + length + "!");
		}
    	if (pArray == null || pArray.length == 0)
    	{
    		return pSourceArray;
    	}
    	else
    	{
			boolean[] result = new boolean[length + pArray.length];
			if (pIndex > 0)
			{
				System.arraycopy(pSourceArray, 0, result, 0, pIndex);
			}
			if (pIndex < length)
			{
				System.arraycopy(pSourceArray, pIndex, result, pIndex + pArray.length, length - pIndex);
			}
    		System.arraycopy(pArray, 0, result, pIndex, pArray.length);
			return result;
    	}
    }
	
    /**
     * Inserts the specified array at the end in the given
     * array. 
     *
     * @param pSourceArray array in which to add.
     * @param pArray elements to be inserted.
     * @return returns a new array if needed.
     */
   	public static boolean[] addAll(boolean[] pSourceArray, boolean[] pArray)
    {
    	return addAll(pSourceArray, -1, pArray);
    }
	
    /**
     * Removes all elements between fromIndex (inclusive) and toIndex (exclusive of given array).
     *
     * @param pSourceArray array in which to delete.
     * @param pFromIndex   the first index.
     * @param pToIndex     the last index (not included).
     * @return returns a new array if needed.
     */
	public static boolean[] removeRange(boolean[] pSourceArray, int pFromIndex, int pToIndex)
	{
		int length = (pSourceArray == null) ? 0 : pSourceArray.length;
		if (pFromIndex < 0)
		{
			pFromIndex = length - 1;
		}
		else if (pFromIndex > length)
		{
			throw new IndexOutOfBoundsException("The fromIndex is " + pFromIndex + " and should be smaller or equal than " + length + "!");
		}
		if (pToIndex < 0)
		{
			pToIndex = length;
		}
		else if (pToIndex < pFromIndex || pToIndex > length)
		{
			throw new IndexOutOfBoundsException("The toIndex is " + pToIndex + " and should be between fromIndex + " + pFromIndex + " and length " + length + "!");
		}
		if (pFromIndex == pToIndex)
		{
			return pSourceArray;
		}
		else
		{
			int amount = pToIndex - pFromIndex;
			boolean[] result = new boolean[length - amount];
			if (pFromIndex > 0)
			{
				System.arraycopy(pSourceArray, 0, result, 0, pFromIndex);
			}
			if (pToIndex < length)
			{
				System.arraycopy(pSourceArray, pToIndex, result, pFromIndex, length - pToIndex);
			}
			return result;
		}
	}
	
    /**
     * Removes one element at the given index of given array.
     *
     * @param pSourceArray array in which to delete.
     * @param pIndex   the index where elements should be deleted.
     * @return returns a new array if needed.
     */
	public static boolean[] remove(boolean[] pSourceArray, int pIndex)
	{
		return removeRange(pSourceArray, pIndex, pIndex + 1);
	}
	
    /**
     * Removes the last element of given array.
     *
     * @param pSourceArray array in which to delete.
     * @return returns a new array if needed.
     */
	public static boolean[] removeLast(boolean[] pSourceArray)
	{
		return removeRange(pSourceArray, -1, -1);
	}
	
    /**
     * Removes all elements from the given index of given array.
     *
     * @param pSourceArray array in which to delete.
     * @param pIndex   the index starting to remove all elements.
     * @return returns a new array if needed.
     */
	public static boolean[] truncate(boolean[] pSourceArray, int pIndex)
	{
		return removeRange(pSourceArray, pIndex, -1);
	}

    /**
     * Removes all Elements.
     *
     * @param pSourceArray array in which to delete.
     * @return returns a new array if needed.
     */
	public static boolean[] clear(boolean[] pSourceArray)
	{
		return removeRange(pSourceArray, 0, -1);
	}

    /**
     * Returns true if this list contains the specified
     * element.  
     *
     * @param pSourceArray array in which to search.
     * @param pObject element to search for.
     * 
     * @return true if this list contains the specified
     * element.
     */
	public static boolean contains(boolean[] pSourceArray, boolean pObject)
	{
		return indexOf(pSourceArray, pObject, 0) >= 0;
	}

    /**
     * Returns the index in this list of the first occurence of the specified
     * element, or -1 if the list does not contain this element.  
     * More formally, returns the lowest index <code>i</code> such that <code>(o==null ?
     * get(i)==null : o.equals(get(i)))</code>, or -1 if there is no such
     * index.<p>
     *
     * @param pSourceArray array in which to search.
     * @param pObject element to search for.
     * 
     * @return the index in this List of the first occurence of the specified
     * 	       element, or -1 if the List does not contain this element.
     */
	public static int indexOf(boolean[] pSourceArray, boolean pObject)
	{
		return indexOf(pSourceArray, pObject, 0);
	}

    /**
     * Returns the index in this list of the first occurence of the specified
     * element started at pIndex, or -1 if the list does not contain this element.  
     * More formally, returns the lowest index <code>i</code> such that <code>(o==null ?
     * get(i)==null : o.equals(get(i)))</code>, or -1 if there is no such
     * index.<p>
     *
     * @param pSourceArray array in which to search.
     * @param pObject element to search for.
     * @param pIndex  index at which the search starts.
     * 
     * @return the index in this List of the first occurence of the specified
     * 	       element started at pIndex, or -1 if the List does not contain this element.
     */
	public static int indexOf(boolean[] pSourceArray, boolean pObject, int pIndex)
	{
		if (pIndex >= 0)
		{
			int length = (pSourceArray == null) ? 0 : pSourceArray.length;
		    for (int i = pIndex; i < length; i++)
		    {
				if (pObject == pSourceArray[i])
				{
				    return i;
				}
		    }
		}
		return -1;
	}
	
    /**
     * Returns the index in this list of the last occurence of the specified
     * element, or -1 if the list does not contain this element.  
     * More formally, returns the lowest index <code>i</code> such that <code>(o==null ?
     * get(i)==null : o.equals(get(i)))</code>, or -1 if there is no such
     * index.<p>
     *
     * @param pSourceArray array in which to search.
     * @param pObject element to search for.
     * 
     * @return the index in this List of the last occurence of the specified
     * 	       element, or -1 if the List does not contain this element.
     */
	public static int lastIndexOf(boolean[] pSourceArray, boolean pObject)
	{
		return lastIndexOf(pSourceArray, pObject, -1);
	}
	
    /**
     * Returns the index in this list of the last occurence of the specified
     * element started at pIndex, or -1 if the list does not contain this element.  
     * More formally, returns the lowest index <code>i</code> such that <code>(o==null ?
     * get(i)==null : o.equals(get(i)))</code>, or -1 if there is no such
     * index.<p>
     *
     * @param pSourceArray array in which to search.
     * @param pObject element to search for.
     * @param pIndex  index at which the search starts.
     * 
     * @return the index in this List of the last occurence of the specified
     * 	       element started at pIndex, or -1 if the List does not contain this element.
     */
	public static int lastIndexOf(boolean[] pSourceArray, boolean pObject, int pIndex)
	{
		int length = (pSourceArray == null) ? 0 : pSourceArray.length;
		if (pIndex < length)
		{
			if (pIndex < 0)
			{
				pIndex = length - 1;
			}
		    for (int i = pIndex; i >= 0; i--)
		    {
				if (pObject == pSourceArray[i])
				{
				    return i;
				}
		    }
		}
		return -1;
	}

    /**
     * Inserts the specified element at the specified position in the given
     * array. Shifts the element currently at that position (if any) and
     * any subsequent elements to the right (adds one to their indices).
     * If the index is negative the element is added at the end.
     *
     * @param pSourceArray array in which to add.
     * @param pIndex index at which the specified element is to be inserted.
     * @param pElement element to be inserted.
     * @return returns a new array if needed.
     * @throws    IndexOutOfBoundsException if index is out of range
     *        <code>(index &gt; size())</code>.
     */
    public static char[] add(char[] pSourceArray, int pIndex, char pElement)
    {
        int length = (pSourceArray == null) ? 0 : pSourceArray.length;
        if (pIndex < 0)
        {
            pIndex = length;
        }
        else if (pIndex > length)
        {
            throw new IndexOutOfBoundsException("The index is " + pIndex + ", should be smaller or equal to length " + length + "!");
        }
        char[] result = new char[length + 1];
        if (pIndex > 0)
        {
            System.arraycopy(pSourceArray, 0, result, 0, pIndex);
        }
        if (pIndex < length)
        {
            System.arraycopy(pSourceArray, pIndex, result, pIndex + 1, length - pIndex);
        }
        result[pIndex] = pElement;
        return result;
    }
    
    /**
     * Inserts the specified element at the end in the given
     * array.
     *
     * @param pSourceArray array in which to add.
     * @param pElement element to be inserted.
     * @return returns a new array if needed.
     */
    public static char[] add(char[] pSourceArray, char pElement)
    {
        return add(pSourceArray, -1, pElement);
    }
    
    /**
     * Inserts the specified array at the specified position in the given
     * array. Shifts the element currently at that position (if any) and
     * any subsequent elements to the right.
     * If the index is negative the element is added at the end.
     *
     * @param pSourceArray array in which to add.
     * @param pIndex index at which the specified element is to be inserted.
     * @param pArray elements to be inserted.
     * @return returns a new array if needed.
     * @throws IndexOutOfBoundsException if index is out of range
     *         <code>(index &gt; size())</code>.
     */
    public static char[] addAll(char[] pSourceArray, int pIndex, char[] pArray)
    {
        int length = (pSourceArray == null) ? 0 : pSourceArray.length;
        if (pIndex < 0)
        {
            pIndex = length;
        }
        else if (pIndex > length)
        {
            throw new IndexOutOfBoundsException("The index is " + pIndex + ", should be smaller or equal to length " + length + "!");
        }
        if (pArray == null || pArray.length == 0)
        {
            return pSourceArray;
        }
        else
        {
            char[] result = new char[length + pArray.length];
            if (pIndex > 0)
            {
                System.arraycopy(pSourceArray, 0, result, 0, pIndex);
            }
            if (pIndex < length)
            {
                System.arraycopy(pSourceArray, pIndex, result, pIndex + pArray.length, length - pIndex);
            }
            System.arraycopy(pArray, 0, result, pIndex, pArray.length);
            return result;
        }
    }
    
    /**
     * Inserts the specified array at the end in the given
     * array. 
     *
     * @param pSourceArray array in which to add.
     * @param pArray elements to be inserted.
     * @return returns a new array if needed.
     */
    public static char[] addAll(char[] pSourceArray, char[] pArray)
    {
        return addAll(pSourceArray, -1, pArray);
    }
    
    /**
     * Removes all elements between fromIndex (inclusive) and toIndex (exclusive of given array).
     *
     * @param pSourceArray array in which to delete.
     * @param pFromIndex   the first index.
     * @param pToIndex     the last index (not included).
     * @return returns a new array if needed.
     */
    public static char[] removeRange(char[] pSourceArray, int pFromIndex, int pToIndex)
    {
        int length = (pSourceArray == null) ? 0 : pSourceArray.length;
        if (pFromIndex < 0)
        {
            pFromIndex = length - 1;
        }
        else if (pFromIndex > length)
        {
            throw new IndexOutOfBoundsException("The fromIndex is " + pFromIndex + " and should be smaller or equal than " + length + "!");
        }
        if (pToIndex < 0)
        {
            pToIndex = length;
        }
        else if (pToIndex < pFromIndex || pToIndex > length)
        {
            throw new IndexOutOfBoundsException("The toIndex is " + pToIndex + " and should be between fromIndex + " + pFromIndex + " and length " + length + "!");
        }
        if (pFromIndex == pToIndex)
        {
            return pSourceArray;
        }
        else
        {
            int amount = pToIndex - pFromIndex;
            char[] result = new char[length - amount];
            if (pFromIndex > 0)
            {
                System.arraycopy(pSourceArray, 0, result, 0, pFromIndex);
            }
            if (pToIndex < length)
            {
                System.arraycopy(pSourceArray, pToIndex, result, pFromIndex, length - pToIndex);
            }
            return result;
        }
    }
    
    /**
     * Removes one element at the given index of given array.
     *
     * @param pSourceArray array in which to delete.
     * @param pIndex   the index where elements should be deleted.
     * @return returns a new array if needed.
     */
    public static char[] remove(char[] pSourceArray, int pIndex)
    {
        return removeRange(pSourceArray, pIndex, pIndex + 1);
    }
    
    /**
     * Removes the last element of given array.
     *
     * @param pSourceArray array in which to delete.
     * @return returns a new array if needed.
     */
    public static char[] removeLast(char[] pSourceArray)
    {
        return removeRange(pSourceArray, -1, -1);
    }
    
    /**
     * Removes all elements from the given index of given array.
     *
     * @param pSourceArray array in which to delete.
     * @param pIndex   the index starting to remove all elements.
     * @return returns a new array if needed.
     */
    public static char[] truncate(char[] pSourceArray, int pIndex)
    {
        return removeRange(pSourceArray, pIndex, -1);
    }

    /**
     * Removes all Elements.
     *
     * @param pSourceArray array in which to delete.
     * @return returns a new array if needed.
     */
    public static char[] clear(char[] pSourceArray)
    {
        return removeRange(pSourceArray, 0, -1);
    }

    /**
     * Returns true if this list contains the specified
     * element.  
     *
     * @param pSourceArray array in which to search.
     * @param pObject element to search for.
     * 
     * @return true if this list contains the specified
     * element.
     */
    public static boolean contains(char[] pSourceArray, char pObject)
    {
        return indexOf(pSourceArray, pObject, 0) >= 0;
    }

    /**
     * Returns the index in this list of the first occurence of the specified
     * element, or -1 if the list does not contain this element.  
     * More formally, returns the lowest index <code>i</code> such that <code>(o==null ?
     * get(i)==null : o.equals(get(i)))</code>, or -1 if there is no such
     * index.<p>
     *
     * @param pSourceArray array in which to search.
     * @param pObject element to search for.
     * 
     * @return the index in this List of the first occurence of the specified
     *         element, or -1 if the List does not contain this element.
     */
    public static int indexOf(char[] pSourceArray, char pObject)
    {
        return indexOf(pSourceArray, pObject, 0);
    }

    /**
     * Returns the index in this list of the first occurence of the specified
     * element started at pIndex, or -1 if the list does not contain this element.  
     * More formally, returns the lowest index <code>i</code> such that <code>(o==null ?
     * get(i)==null : o.equals(get(i)))</code>, or -1 if there is no such
     * index.<p>
     *
     * @param pSourceArray array in which to search.
     * @param pObject element to search for.
     * @param pIndex  index at which the search starts.
     * 
     * @return the index in this List of the first occurence of the specified
     *         element started at pIndex, or -1 if the List does not contain this element.
     */
    public static int indexOf(char[] pSourceArray, char pObject, int pIndex)
    {
        if (pIndex >= 0)
        {
            int length = (pSourceArray == null) ? 0 : pSourceArray.length;
            for (int i = pIndex; i < length; i++)
            {
                if (pObject == pSourceArray[i])
                {
                    return i;
                }
            }
        }
        return -1;
    }
    
    /**
     * Returns the index in this list of the last occurence of the specified
     * element, or -1 if the list does not contain this element.  
     * More formally, returns the lowest index <code>i</code> such that <code>(o==null ?
     * get(i)==null : o.equals(get(i)))</code>, or -1 if there is no such
     * index.<p>
     *
     * @param pSourceArray array in which to search.
     * @param pObject element to search for.
     * 
     * @return the index in this List of the last occurence of the specified
     *         element, or -1 if the List does not contain this element.
     */
    public static int lastIndexOf(char[] pSourceArray, char pObject)
    {
        return lastIndexOf(pSourceArray, pObject, -1);
    }
    
    /**
     * Returns the index in this list of the last occurence of the specified
     * element started at pIndex, or -1 if the list does not contain this element.  
     * More formally, returns the lowest index <code>i</code> such that <code>(o==null ?
     * get(i)==null : o.equals(get(i)))</code>, or -1 if there is no such
     * index.<p>
     *
     * @param pSourceArray array in which to search.
     * @param pObject element to search for.
     * @param pIndex  index at which the search starts.
     * 
     * @return the index in this List of the last occurence of the specified
     *         element started at pIndex, or -1 if the List does not contain this element.
     */
    public static int lastIndexOf(char[] pSourceArray, char pObject, int pIndex)
    {
        int length = (pSourceArray == null) ? 0 : pSourceArray.length;
        if (pIndex < length)
        {
            if (pIndex < 0)
            {
                pIndex = length - 1;
            }
            for (int i = pIndex; i >= 0; i--)
            {
                if (pObject == pSourceArray[i])
                {
                    return i;
                }
            }
        }
        return -1;
    }
	
    /**
     * Inserts the specified element at the specified position in the given
     * array. Shifts the element currently at that position (if any) and
     * any subsequent elements to the right (adds one to their indices).
     * If the index is negative the element is added at the end.
     *
     * @param pSourceArray array in which to add.
     * @param pIndex index at which the specified element is to be inserted.
     * @param pElement element to be inserted.
     * @return returns a new array if needed.
     * @throws    IndexOutOfBoundsException if index is out of range
     *		  <code>(index &gt; size())</code>.
     */
	public static byte[] add(byte[] pSourceArray, int pIndex, int pElement)
	{
		int length = (pSourceArray == null) ? 0 : pSourceArray.length;
		if (pIndex < 0)
		{
			pIndex = length;
		}
		else if (pIndex > length)
		{
			throw new IndexOutOfBoundsException("The index is " + pIndex + ", should be smaller or equal to length " + length + "!");
		}
		byte[] result = new byte[length + 1];
		if (pIndex > 0)
		{
			System.arraycopy(pSourceArray, 0, result, 0, pIndex);
		}
		if (pIndex < length)
		{
			System.arraycopy(pSourceArray, pIndex, result, pIndex + 1, length - pIndex);
		}
		result[pIndex] = (byte)pElement;
		return result;
	}
	
    /**
     * Inserts the specified element at the end in the given
     * array.
     *
     * @param pSourceArray array in which to add.
     * @param pElement element to be inserted.
     * @return returns a new array if needed.
     */
	public static byte[] add(byte[] pSourceArray, int pElement)
	{
		return add(pSourceArray, -1, pElement);
	}
	
	/**
     * Inserts the specified array at the specified position in the given
     * array. Shifts the element currently at that position (if any) and
     * any subsequent elements to the right.
     * If the index is negative the element is added at the end.
     *
     * @param pSourceArray array in which to add.
     * @param pIndex index at which the specified element is to be inserted.
     * @param pArray elements to be inserted.
     * @return returns a new array if needed.
     * @throws IndexOutOfBoundsException if index is out of range
     *		   <code>(index &gt; size())</code>.
     */
    public static byte[] addAll(byte[] pSourceArray, int pIndex, byte[] pArray)
    {
		int length = (pSourceArray == null) ? 0 : pSourceArray.length;
		if (pIndex < 0)
		{
			pIndex = length;
		}
		else if (pIndex > length)
		{
			throw new IndexOutOfBoundsException("The index is " + pIndex + ", should be smaller or equal to length " + length + "!");
		}
    	if (pArray == null || pArray.length == 0)
    	{
    		return pSourceArray;
    	}
    	else
    	{
			byte[] result = new byte[length + pArray.length];
			if (pIndex > 0)
			{
				System.arraycopy(pSourceArray, 0, result, 0, pIndex);
			}
			if (pIndex < length)
			{
				System.arraycopy(pSourceArray, pIndex, result, pIndex + pArray.length, length - pIndex);
			}
    		System.arraycopy(pArray, 0, result, pIndex, pArray.length);
			return result;
    	}
    }
	
    /**
     * Inserts the specified array at the end in the given
     * array. 
     *
     * @param pSourceArray array in which to add.
     * @param pArray elements to be inserted.
     * @return returns a new array if needed.
     */
    public static byte[] addAll(byte[] pSourceArray, byte[] pArray)
    {
        return addAll(pSourceArray, -1, pArray);
    }
    
    /**
     * Inserts the specified element at the specified position in the given
     * array. Shifts the element currently at that position (if any) and
     * any subsequent elements to the right (adds one to their indices).
     * If the index is negative the element is added at the end.
     *
     * @param pSourceArray array in which to add.
     * @param pIndex index at which the specified element is to be inserted.
     * @param pElement element to be inserted.
     * @return returns a new array if needed.
     * @throws    IndexOutOfBoundsException if index is out of range
     *        <code>(index &gt; size())</code>.
     */
    public static int[] add(int[] pSourceArray, int pIndex, int pElement)
    {
        int length = (pSourceArray == null) ? 0 : pSourceArray.length;
        if (pIndex < 0)
        {
            pIndex = length;
        }
        else if (pIndex > length)
        {
            throw new IndexOutOfBoundsException("The index is " + pIndex + ", should be smaller or equal to length " + length + "!");
        }
        int[] result = new int[length + 1];
        if (pIndex > 0)
        {
            System.arraycopy(pSourceArray, 0, result, 0, pIndex);
        }
        if (pIndex < length)
        {
            System.arraycopy(pSourceArray, pIndex, result, pIndex + 1, length - pIndex);
        }
        result[pIndex] = pElement;
        return result;
    }
    
    /**
     * Inserts the specified element at the end in the given
     * array.
     *
     * @param pSourceArray array in which to add.
     * @param pElement element to be inserted.
     * @return returns a new array if needed.
     */
    public static int[] add(int[] pSourceArray, int pElement)
    {
        return add(pSourceArray, -1, pElement);
    }
    
    /**
     * Inserts the specified array at the specified position in the given
     * array. Shifts the element currently at that position (if any) and
     * any subsequent elements to the right.
     * If the index is negative the element is added at the end.
     *
     * @param pSourceArray array in which to add.
     * @param pIndex index at which the specified element is to be inserted.
     * @param pArray elements to be inserted.
     * @return returns a new array if needed.
     * @throws IndexOutOfBoundsException if index is out of range
     *         <code>(index &gt; size())</code>.
     */
    public static int[] addAll(int[] pSourceArray, int pIndex, int[] pArray)
    {
        int length = (pSourceArray == null) ? 0 : pSourceArray.length;
        if (pIndex < 0)
        {
            pIndex = length;
        }
        else if (pIndex > length)
        {
            throw new IndexOutOfBoundsException("The index is " + pIndex + ", should be smaller or equal to length " + length + "!");
        }
        if (pArray == null || pArray.length == 0)
        {
            return pSourceArray;
        }
        else
        {
            int[] result = new int[length + pArray.length];
            if (pIndex > 0)
            {
                System.arraycopy(pSourceArray, 0, result, 0, pIndex);
            }
            if (pIndex < length)
            {
                System.arraycopy(pSourceArray, pIndex, result, pIndex + pArray.length, length - pIndex);
            }
            System.arraycopy(pArray, 0, result, pIndex, pArray.length);
            return result;
        }
    }
    
    /**
     * Inserts the specified array at the end in the given
     * array. 
     *
     * @param pSourceArray array in which to add.
     * @param pArray elements to be inserted.
     * @return returns a new array if needed.
     */
   	public static int[] addAll(int[] pSourceArray, int[] pArray)
    {
    	return addAll(pSourceArray, -1, pArray);
    }
	
    /**
     * Removes all elements between fromIndex (inclusive) and toIndex (exclusive of given array).
     *
     * @param pSourceArray array in which to delete.
     * @param pFromIndex   the first index.
     * @param pToIndex     the last index (not included).
     * @return returns a new array if needed.
     */
	public static byte[] removeRange(byte[] pSourceArray, int pFromIndex, int pToIndex)
	{
		int length = (pSourceArray == null) ? 0 : pSourceArray.length;
		if (pFromIndex < 0)
		{
			pFromIndex = length - 1;
		}
		else if (pFromIndex > length)
		{
			throw new IndexOutOfBoundsException("The fromIndex is " + pFromIndex + " and should be smaller or equal than " + length + "!");
		}
		if (pToIndex < 0)
		{
			pToIndex = length;
		}
		else if (pToIndex < pFromIndex || pToIndex > length)
		{
			throw new IndexOutOfBoundsException("The toIndex is " + pToIndex + " and should be between fromIndex + " + pFromIndex + " and length " + length + "!");
		}
		if (pFromIndex == pToIndex)
		{
			return pSourceArray;
		}
		else
		{
			int amount = pToIndex - pFromIndex;
			byte[] result = new byte[length - amount];
			if (pFromIndex > 0)
			{
				System.arraycopy(pSourceArray, 0, result, 0, pFromIndex);
			}
			if (pToIndex < length)
			{
				System.arraycopy(pSourceArray, pToIndex, result, pFromIndex, length - pToIndex);
			}
			return result;
		}
	}
	
    /**
     * Removes all elements between fromIndex (inclusive) and toIndex (exclusive of given array).
     *
     * @param pSourceArray array in which to delete.
     * @param pFromIndex   the first index.
     * @param pToIndex     the last index (not included).
     * @return returns a new array if needed.
     */
    public static int[] removeRange(int[] pSourceArray, int pFromIndex, int pToIndex)
    {
        int length = (pSourceArray == null) ? 0 : pSourceArray.length;
        if (pFromIndex < 0)
        {
            pFromIndex = length - 1;
        }
        else if (pFromIndex > length)
        {
            throw new IndexOutOfBoundsException("The fromIndex is " + pFromIndex + " and should be smaller or equal than " + length + "!");
        }
        if (pToIndex < 0)
        {
            pToIndex = length;
        }
        else if (pToIndex < pFromIndex || pToIndex > length)
        {
            throw new IndexOutOfBoundsException("The toIndex is " + pToIndex + " and should be between fromIndex + " + pFromIndex + " and length " + length + "!");
        }
        if (pFromIndex == pToIndex)
        {
            return pSourceArray;
        }
        else
        {
            int amount = pToIndex - pFromIndex;
            int[] result = new int[length - amount];
            if (pFromIndex > 0)
            {
                System.arraycopy(pSourceArray, 0, result, 0, pFromIndex);
            }
            if (pToIndex < length)
            {
                System.arraycopy(pSourceArray, pToIndex, result, pFromIndex, length - pToIndex);
            }
            return result;
        }
    }
    
    /**
     * Removes one element at the given index of given array.
     *
     * @param pSourceArray array in which to delete.
     * @param pIndex   the index where elements should be deleted.
     * @return returns a new array if needed.
     */
	public static byte[] remove(byte[] pSourceArray, int pIndex)
	{
		return removeRange(pSourceArray, pIndex, pIndex + 1);
	}
	
    /**
     * Removes one element at the given index of given array.
     *
     * @param pSourceArray array in which to delete.
     * @param pIndex   the index where elements should be deleted.
     * @return returns a new array if needed.
     */
    public static int[] remove(int[] pSourceArray, int pIndex)
    {
        return removeRange(pSourceArray, pIndex, pIndex + 1);
    }
    
    /**
     * Removes the last element of given array.
     *
     * @param pSourceArray array in which to delete.
     * @return returns a new array if needed.
     */
	public static int[] removeLast(int[] pSourceArray)
	{
		return removeRange(pSourceArray, -1, -1);
	}
	
    /**
     * Removes all elements from the given index of given array.
     *
     * @param pSourceArray array in which to delete.
     * @param pIndex   the index starting to remove all elements.
     * @return returns a new array if needed.
     */
	public static byte[] truncate(byte[] pSourceArray, int pIndex)
	{
		return removeRange(pSourceArray, pIndex, -1);
	}

    /**
     * Removes all elements from the given index of given array.
     *
     * @param pSourceArray array in which to delete.
     * @param pIndex   the index starting to remove all elements.
     * @return returns a new array if needed.
     */
    public static int[] truncate(int[] pSourceArray, int pIndex)
    {
        return removeRange(pSourceArray, pIndex, -1);
    }

    /**
     * Removes all Elements.
     *
     * @param pSourceArray array in which to delete.
     * @return returns a new array if needed.
     */
	public static byte[] clear(byte[] pSourceArray)
	{
		return removeRange(pSourceArray, 0, -1);
	}

    /**
     * Removes all Elements.
     *
     * @param pSourceArray array in which to delete.
     * @return returns a new array if needed.
     */
    public static int[] clear(int[] pSourceArray)
    {
        return removeRange(pSourceArray, 0, -1);
    }

    /**
     * Returns true if this list contains the specified
     * element.  
     *
     * @param pSourceArray array in which to search.
     * @param pObject element to search for.
     * 
     * @return true if this list contains the specified
     * element.
     */
    public static boolean contains(byte[] pSourceArray, int pObject)
    {
        return indexOf(pSourceArray, pObject, 0) >= 0;
    }

    /**
     * Returns true if this list contains the specified
     * element.  
     *
     * @param pSourceArray array in which to search.
     * @param pObject element to search for.
     * 
     * @return true if this list contains the specified
     * element.
     */
	public static boolean contains(int[] pSourceArray, int pObject)
	{
		return indexOf(pSourceArray, pObject, 0) >= 0;
	}

    /**
     * Returns the index in this list of the first occurence of the specified
     * element, or -1 if the list does not contain this element.  
     * More formally, returns the lowest index <code>i</code> such that <code>(o==null ?
     * get(i)==null : o.equals(get(i)))</code>, or -1 if there is no such
     * index.<p>
     *
     * @param pSourceArray array in which to search.
     * @param pObject element to search for.
     * 
     * @return the index in this List of the first occurence of the specified
     * 	       element, or -1 if the List does not contain this element.
     */
	public static int indexOf(int[] pSourceArray, int pObject)
	{
		return indexOf(pSourceArray, pObject, 0);
	}

    /**
     * Returns the index in this list of the first occurence of the specified
     * element started at pIndex, or -1 if the list does not contain this element.  
     * More formally, returns the lowest index <code>i</code> such that <code>(o==null ?
     * get(i)==null : o.equals(get(i)))</code>, or -1 if there is no such
     * index.<p>
     *
     * @param pSourceArray array in which to search.
     * @param pObject element to search for.
     * @param pIndex  index at which the search starts.
     * 
     * @return the index in this List of the first occurence of the specified
     * 	       element started at pIndex, or -1 if the List does not contain this element.
     */
	public static int indexOf(int[] pSourceArray, int pObject, int pIndex)
	{
		if (pIndex >= 0)
		{
			int length = (pSourceArray == null) ? 0 : pSourceArray.length;
		    for (int i = pIndex; i < length; i++)
		    {
				if (pObject == pSourceArray[i])
				{
				    return i;
				}
		    }
		}
		return -1;
	}
	
    /**
     * Returns the index in this list of the first occurence of the specified
     * element, or -1 if the list does not contain this element.  
     * More formally, returns the lowest index <code>i</code> such that <code>(o==null ?
     * get(i)==null : o.equals(get(i)))</code>, or -1 if there is no such
     * index.<p>
     *
     * @param pSourceArray array in which to search.
     * @param pObject element to search for.
     * 
     * @return the index in this List of the first occurence of the specified
     *         element, or -1 if the List does not contain this element.
     */
    public static int indexOf(byte[] pSourceArray, int pObject)
    {
        return indexOf(pSourceArray, pObject, 0);
    }

    /**
     * Returns the index in this list of the first occurence of the specified
     * element started at pIndex, or -1 if the list does not contain this element.  
     * More formally, returns the lowest index <code>i</code> such that <code>(o==null ?
     * get(i)==null : o.equals(get(i)))</code>, or -1 if there is no such
     * index.<p>
     *
     * @param pSourceArray array in which to search.
     * @param pObject element to search for.
     * @param pIndex  index at which the search starts.
     * 
     * @return the index in this List of the first occurence of the specified
     *         element started at pIndex, or -1 if the List does not contain this element.
     */
    public static int indexOf(byte[] pSourceArray, int pObject, int pIndex)
    {
        if (pIndex >= 0)
        {
            int length = (pSourceArray == null) ? 0 : pSourceArray.length;
            for (int i = pIndex; i < length; i++)
            {
                if (pObject == pSourceArray[i])
                {
                    return i;
                }
            }
        }
        return -1;
    }
    
    /**
     * Returns the index in this list of the last occurence of the specified
     * element, or -1 if the list does not contain this element.  
     * More formally, returns the lowest index <code>i</code> such that <code>(o==null ?
     * get(i)==null : o.equals(get(i)))</code>, or -1 if there is no such
     * index.<p>
     *
     * @param pSourceArray array in which to search.
     * @param pObject element to search for.
     * 
     * @return the index in this List of the last occurence of the specified
     * 	       element, or -1 if the List does not contain this element.
     */
	public static int lastIndexOf(byte[] pSourceArray, int pObject)
	{
		return lastIndexOf(pSourceArray, pObject, -1);
	}
	
    /**
     * Returns the index in this list of the last occurence of the specified
     * element started at pIndex, or -1 if the list does not contain this element.  
     * More formally, returns the lowest index <code>i</code> such that <code>(o==null ?
     * get(i)==null : o.equals(get(i)))</code>, or -1 if there is no such
     * index.<p>
     *
     * @param pSourceArray array in which to search.
     * @param pObject element to search for.
     * @param pIndex  index at which the search starts.
     * 
     * @return the index in this List of the last occurence of the specified
     * 	       element started at pIndex, or -1 if the List does not contain this element.
     */
	public static int lastIndexOf(byte[] pSourceArray, int pObject, int pIndex)
	{
		int length = (pSourceArray == null) ? 0 : pSourceArray.length;
		if (pIndex < length)
		{
			if (pIndex < 0)
			{
				pIndex = length - 1;
			}
		    for (int i = pIndex; i >= 0; i--)
		    {
				if (pObject == pSourceArray[i])
				{
				    return i;
				}
		    }
		}
		return -1;
	}
	
    /**
     * Returns the index in this list of the last occurence of the specified
     * element, or -1 if the list does not contain this element.  
     * More formally, returns the lowest index <code>i</code> such that <code>(o==null ?
     * get(i)==null : o.equals(get(i)))</code>, or -1 if there is no such
     * index.<p>
     *
     * @param pSourceArray array in which to search.
     * @param pObject element to search for.
     * 
     * @return the index in this List of the last occurence of the specified
     *         element, or -1 if the List does not contain this element.
     */
    public static int lastIndexOf(int[] pSourceArray, int pObject)
    {
        return lastIndexOf(pSourceArray, pObject, -1);
    }
    
    /**
     * Returns the index in this list of the last occurence of the specified
     * element started at pIndex, or -1 if the list does not contain this element.  
     * More formally, returns the lowest index <code>i</code> such that <code>(o==null ?
     * get(i)==null : o.equals(get(i)))</code>, or -1 if there is no such
     * index.<p>
     *
     * @param pSourceArray array in which to search.
     * @param pObject element to search for.
     * @param pIndex  index at which the search starts.
     * 
     * @return the index in this List of the last occurence of the specified
     *         element started at pIndex, or -1 if the List does not contain this element.
     */
    public static int lastIndexOf(int[] pSourceArray, int pObject, int pIndex)
    {
        int length = (pSourceArray == null) ? 0 : pSourceArray.length;
        if (pIndex < length)
        {
            if (pIndex < 0)
            {
                pIndex = length - 1;
            }
            for (int i = pIndex; i >= 0; i--)
            {
                if (pObject == pSourceArray[i])
                {
                    return i;
                }
            }
        }
        return -1;
    }
    
    /**
     * Inserts the specified element at the specified position in the given
     * array. Shifts the element currently at that position (if any) and
     * any subsequent elements to the right (adds one to their indices).
     * If the index is negative the element is added at the end.
     *
     * @param pSourceArray array in which to add.
     * @param pIndex index at which the specified element is to be inserted.
     * @param pElement element to be inserted.
     * @return returns a new array if needed.
     * @throws    IndexOutOfBoundsException if index is out of range
     *		  <code>(index &gt; size())</code>.
     */
	public static long[] add(long[] pSourceArray, int pIndex, long pElement)
	{
		int length = (pSourceArray == null) ? 0 : pSourceArray.length;
		if (pIndex < 0)
		{
			pIndex = length;
		}
		else if (pIndex > length)
		{
			throw new IndexOutOfBoundsException("The index is " + pIndex + ", should be smaller or equal to length " + length + "!");
		}
		long[] result = new long[length + 1];
		if (pIndex > 0)
		{
			System.arraycopy(pSourceArray, 0, result, 0, pIndex);
		}
		if (pIndex < length)
		{
			System.arraycopy(pSourceArray, pIndex, result, pIndex + 1, length - pIndex);
		}
		result[pIndex] = pElement;
		return result;
	}
	
    /**
     * Inserts the specified element at the end in the given
     * array.
     *
     * @param pSourceArray array in which to add.
     * @param pElement element to be inserted.
     * @return returns a new array if needed.
     */
	public static long[] add(long[] pSourceArray, long pElement)
	{
		return add(pSourceArray, -1, pElement);
	}
	
	/**
     * Inserts the specified array at the specified position in the given
     * array. Shifts the element currently at that position (if any) and
     * any subsequent elements to the right.
     * If the index is negative the element is added at the end.
     *
     * @param pSourceArray array in which to add.
     * @param pIndex index at which the specified element is to be inserted.
     * @param pArray elements to be inserted.
     * @return returns a new array if needed.
     * @throws IndexOutOfBoundsException if index is out of range
     *		   <code>(index &gt; size())</code>.
     */
    public static long[] addAll(long[] pSourceArray, int pIndex, long[] pArray)
    {
		int length = (pSourceArray == null) ? 0 : pSourceArray.length;
		if (pIndex < 0)
		{
			pIndex = length;
		}
		else if (pIndex > length)
		{
			throw new IndexOutOfBoundsException("The index is " + pIndex + ", should be smaller or equal to length " + length + "!");
		}
    	if (pArray == null || pArray.length == 0)
    	{
    		return pSourceArray;
    	}
    	else
    	{
    		long[] result = new long[length + pArray.length];
			if (pIndex > 0)
			{
				System.arraycopy(pSourceArray, 0, result, 0, pIndex);
			}
			if (pIndex < length)
			{
				System.arraycopy(pSourceArray, pIndex, result, pIndex + pArray.length, length - pIndex);
			}
    		System.arraycopy(pArray, 0, result, pIndex, pArray.length);
			return result;
    	}
    }
	
    /**
     * Inserts the specified array at the end in the given
     * array. 
     *
     * @param pSourceArray array in which to add.
     * @param pArray elements to be inserted.
     * @return returns a new array if needed.
     */
   	public static long[] addAll(long[] pSourceArray, long[] pArray)
    {
    	return addAll(pSourceArray, -1, pArray);
    }
	
    /**
     * Removes all elements between fromIndex (inclusive) and toIndex (exclusive of given array).
     *
     * @param pSourceArray array in which to delete.
     * @param pFromIndex   the first index.
     * @param pToIndex     the last index (not included).
     * @return returns a new array if needed.
     */
	public static long[] removeRange(long[] pSourceArray, int pFromIndex, int pToIndex)
	{
		int length = (pSourceArray == null) ? 0 : pSourceArray.length;
		if (pFromIndex < 0)
		{
			pFromIndex = length - 1;
		}
		else if (pFromIndex > length)
		{
			throw new IndexOutOfBoundsException("The fromIndex is " + pFromIndex + " and should be smaller or equal than " + length + "!");
		}
		if (pToIndex < 0)
		{
			pToIndex = length;
		}
		else if (pToIndex < pFromIndex || pToIndex > length)
		{
			throw new IndexOutOfBoundsException("The toIndex is " + pToIndex + " and should be between fromIndex + " + pFromIndex + " and length " + length + "!");
		}
		if (pFromIndex == pToIndex)
		{
			return pSourceArray;
		}
		else
		{
			int amount = pToIndex - pFromIndex;
			long[] result = new long[length - amount];
			if (pFromIndex > 0)
			{
				System.arraycopy(pSourceArray, 0, result, 0, pFromIndex);
			}
			if (pToIndex < length)
			{
				System.arraycopy(pSourceArray, pToIndex, result, pFromIndex, length - pToIndex);
			}
			return result;
		}
	}
	
    /**
     * Removes one element at the given index of given array.
     *
     * @param pSourceArray array in which to delete.
     * @param pIndex   the index where elements should be deleted.
     * @return returns a new array if needed.
     */
	public static long[] remove(long[] pSourceArray, int pIndex)
	{
		return removeRange(pSourceArray, pIndex, pIndex + 1);
	}
	
    /**
     * Removes the last element of given array.
     *
     * @param pSourceArray array in which to delete.
     * @return returns a new array if needed.
     */
	public static long[] removeLast(long[] pSourceArray)
	{
		return removeRange(pSourceArray, -1, -1);
	}
	
    /**
     * Removes all elements from the given index of given array.
     *
     * @param pSourceArray array in which to delete.
     * @param pIndex   the index starting to remove all elements.
     * @return returns a new array if needed.
     */
	public static long[] truncate(long[] pSourceArray, int pIndex)
	{
		return removeRange(pSourceArray, pIndex, -1);
	}

    /**
     * Removes all Elements.
     *
     * @param pSourceArray array in which to delete.
     * @return returns a new array if needed.
     */
	public static long[] clear(long[] pSourceArray)
	{
		return removeRange(pSourceArray, 0, -1);
	}
	
    /**
     * Returns true if this list contains the specified
     * element.  
     *
     * @param pSourceArray array in which to search.
     * @param pObject element to search for.
     * 
     * @return true if this list contains the specified
     * element.
     */
	public static boolean contains(long[] pSourceArray, long pObject)
	{
		return indexOf(pSourceArray, pObject, 0) >= 0;
	}

    /**
     * Returns the index in this list of the first occurence of the specified
     * element, or -1 if the list does not contain this element.  
     * More formally, returns the lowest index <code>i</code> such that <code>(o==null ?
     * get(i)==null : o.equals(get(i)))</code>, or -1 if there is no such
     * index.<p>
     *
     * @param pSourceArray array in which to search.
     * @param pObject element to search for.
     * 
     * @return the index in this List of the first occurence of the specified
     * 	       element, or -1 if the List does not contain this element.
     */
	public static int indexOf(long[] pSourceArray, long pObject)
	{
		return indexOf(pSourceArray, pObject, 0);
	}

    /**
     * Returns the index in this list of the first occurence of the specified
     * element started at pIndex, or -1 if the list does not contain this element.  
     * More formally, returns the lowest index <code>i</code> such that <code>(o==null ?
     * get(i)==null : o.equals(get(i)))</code>, or -1 if there is no such
     * index.<p>
     *
     * @param pSourceArray array in which to search.
     * @param pObject element to search for.
     * @param pIndex  index at which the search starts.
     * 
     * @return the index in this List of the first occurence of the specified
     * 	       element started at pIndex, or -1 if the List does not contain this element.
     */
	public static int indexOf(long[] pSourceArray, long pObject, int pIndex)
	{
		if (pIndex >= 0)
		{
			int length = (pSourceArray == null) ? 0 : pSourceArray.length;
		    for (int i = pIndex; i < length; i++)
		    {
				if (pObject == pSourceArray[i])
				{
				    return i;
				}
		    }
		}
		return -1;
	}
	
    /**
     * Returns the index in this list of the last occurence of the specified
     * element, or -1 if the list does not contain this element.  
     * More formally, returns the lowest index <code>i</code> such that <code>(o==null ?
     * get(i)==null : o.equals(get(i)))</code>, or -1 if there is no such
     * index.<p>
     *
     * @param pSourceArray array in which to search.
     * @param pObject element to search for.
     * 
     * @return the index in this List of the last occurence of the specified
     * 	       element, or -1 if the List does not contain this element.
     */
	public static int lastIndexOf(long[] pSourceArray, long pObject)
	{
		return lastIndexOf(pSourceArray, pObject, -1);
	}
	
    /**
     * Returns the index in this list of the last occurence of the specified
     * element started at pIndex, or -1 if the list does not contain this element.  
     * More formally, returns the lowest index <code>i</code> such that <code>(o==null ?
     * get(i)==null : o.equals(get(i)))</code>, or -1 if there is no such
     * index.<p>
     *
     * @param pSourceArray array in which to search.
     * @param pObject element to search for.
     * @param pIndex  index at which the search starts.
     * 
     * @return the index in this List of the last occurence of the specified
     * 	       element started at pIndex, or -1 if the List does not contain this element.
     */
	public static int lastIndexOf(long[] pSourceArray, long pObject, int pIndex)
	{
		int length = (pSourceArray == null) ? 0 : pSourceArray.length;
		if (pIndex < length)
		{
			if (pIndex < 0)
			{
				pIndex = length - 1;
			}
		    for (int i = pIndex; i >= 0; i--)
		    {
				if (pObject == pSourceArray[i])
				{
				    return i;
				}
		    }
		}
		return -1;
	}
	
    /**
     * Inserts the specified element at the specified position in the given
     * array. Shifts the element currently at that position (if any) and
     * any subsequent elements to the right (adds one to their indices).
     * If the index is negative the element is added at the end.
     *
     * @param pSourceArray array in which to add.
     * @param pIndex index at which the specified element is to be inserted.
     * @param pElement element to be inserted.
     * @return returns a new array if needed.
     * @throws    IndexOutOfBoundsException if index is out of range
     *		  <code>(index &gt; size())</code>.
     */
	public static float[] add(float[] pSourceArray, int pIndex, float pElement)
	{
		int length = (pSourceArray == null) ? 0 : pSourceArray.length;
		if (pIndex < 0)
		{
			pIndex = length;
		}
		else if (pIndex > length)
		{
			throw new IndexOutOfBoundsException("The index is " + pIndex + ", should be smaller or equal to length " + length + "!");
		}
		float[] result = new float[length + 1];
		if (pIndex > 0)
		{
			System.arraycopy(pSourceArray, 0, result, 0, pIndex);
		}
		if (pIndex < length)
		{
			System.arraycopy(pSourceArray, pIndex, result, pIndex + 1, length - pIndex);
		}
		result[pIndex] = pElement;
		return result;
	}
	
    /**
     * Inserts the specified element at the end in the given
     * array.
     *
     * @param pSourceArray array in which to add.
     * @param pElement element to be inserted.
     * @return returns a new array if needed.
     */
	public static float[] add(float[] pSourceArray, float pElement)
	{
		return add(pSourceArray, -1, pElement);
	}
	
	/**
     * Inserts the specified array at the specified position in the given
     * array. Shifts the element currently at that position (if any) and
     * any subsequent elements to the right.
     * If the index is negative the element is added at the end.
     *
     * @param pSourceArray array in which to add.
     * @param pIndex index at which the specified element is to be inserted.
     * @param pArray elements to be inserted.
     * @return returns a new array if needed.
     * @throws IndexOutOfBoundsException if index is out of range
     *		   <code>(index &gt; size())</code>.
     */
    public static float[] addAll(float[] pSourceArray, int pIndex, float[] pArray)
    {
		int length = (pSourceArray == null) ? 0 : pSourceArray.length;
		if (pIndex < 0)
		{
			pIndex = length;
		}
		else if (pIndex > length)
		{
			throw new IndexOutOfBoundsException("The index is " + pIndex + ", should be smaller or equal to length " + length + "!");
		}
    	if (pArray == null || pArray.length == 0)
    	{
    		return pSourceArray;
    	}
    	else
    	{
    		float[] result = new float[length + pArray.length];
			if (pIndex > 0)
			{
				System.arraycopy(pSourceArray, 0, result, 0, pIndex);
			}
			if (pIndex < length)
			{
				System.arraycopy(pSourceArray, pIndex, result, pIndex + pArray.length, length - pIndex);
			}
    		System.arraycopy(pArray, 0, result, pIndex, pArray.length);
			return result;
    	}
    }
	
    /**
     * Inserts the specified array at the end in the given
     * array. 
     *
     * @param pSourceArray array in which to add.
     * @param pArray elements to be inserted.
     * @return returns a new array if needed.
     */
   	public static float[] addAll(float[] pSourceArray, float[] pArray)
    {
    	return addAll(pSourceArray, -1, pArray);
    }
	
    /**
     * Removes all elements between fromIndex (inclusive) and toIndex (exclusive of given array).
     *
     * @param pSourceArray array in which to delete.
     * @param pFromIndex   the first index.
     * @param pToIndex     the last index (not included).
     * @return returns a new array if needed.
     */
	public static float[] removeRange(float[] pSourceArray, int pFromIndex, int pToIndex)
	{
		int length = (pSourceArray == null) ? 0 : pSourceArray.length;
		if (pFromIndex < 0)
		{
			pFromIndex = length - 1;
		}
		else if (pFromIndex > length)
		{
			throw new IndexOutOfBoundsException("The fromIndex is " + pFromIndex + " and should be smaller or equal than " + length + "!");
		}
		if (pToIndex < 0)
		{
			pToIndex = length;
		}
		else if (pToIndex < pFromIndex || pToIndex > length)
		{
			throw new IndexOutOfBoundsException("The toIndex is " + pToIndex + " and should be between fromIndex + " + pFromIndex + " and length " + length + "!");
		}
		if (pFromIndex == pToIndex)
		{
			return pSourceArray;
		}
		else
		{
			int amount = pToIndex - pFromIndex;
			float[] result = new float[length - amount];
			if (pFromIndex > 0)
			{
				System.arraycopy(pSourceArray, 0, result, 0, pFromIndex);
			}
			if (pToIndex < length)
			{
				System.arraycopy(pSourceArray, pToIndex, result, pFromIndex, length - pToIndex);
			}
			return result;
		}
	}
	
    /**
     * Removes one element at the given index of given array.
     *
     * @param pSourceArray array in which to delete.
     * @param pIndex   the index where elements should be deleted.
     * @return returns a new array if needed.
     */
	public static float[] remove(float[] pSourceArray, int pIndex)
	{
		return removeRange(pSourceArray, pIndex, pIndex + 1);
	}
	
    /**
     * Removes the last element of given array.
     *
     * @param pSourceArray array in which to delete.
     * @return returns a new array if needed.
     */
	public static float[] removeLast(float[] pSourceArray)
	{
		return removeRange(pSourceArray, -1, -1);
	}
	
    /**
     * Removes all elements from the given index of given array.
     *
     * @param pSourceArray array in which to delete.
     * @param pIndex   the index starting to remove all elements.
     * @return returns a new array if needed.
     */
	public static float[] truncate(float[] pSourceArray, int pIndex)
	{
		return removeRange(pSourceArray, pIndex, -1);
	}

    /**
     * Removes all Elements.
     *
     * @param pSourceArray array in which to delete.
     * @return returns a new array if needed.
     */
	public static float[] clear(float[] pSourceArray)
	{
		return removeRange(pSourceArray, 0, -1);
	}
	
    /**
     * Returns true if this list contains the specified
     * element.  
     *
     * @param pSourceArray array in which to search.
     * @param pObject element to search for.
     * 
     * @return true if this list contains the specified
     * element.
     */
	public static boolean contains(float[] pSourceArray, float pObject)
	{
		return indexOf(pSourceArray, pObject, 0) >= 0;
	}

    /**
     * Returns the index in this list of the first occurence of the specified
     * element, or -1 if the list does not contain this element.  
     * More formally, returns the lowest index <code>i</code> such that <code>(o==null ?
     * get(i)==null : o.equals(get(i)))</code>, or -1 if there is no such
     * index.<p>
     *
     * @param pSourceArray array in which to search.
     * @param pObject element to search for.
     * 
     * @return the index in this List of the first occurence of the specified
     * 	       element, or -1 if the List does not contain this element.
     */
	public static int indexOf(float[] pSourceArray, float pObject)
	{
		return indexOf(pSourceArray, pObject, 0);
	}

    /**
     * Returns the index in this list of the first occurence of the specified
     * element started at pIndex, or -1 if the list does not contain this element.  
     * More formally, returns the lowest index <code>i</code> such that <code>(o==null ?
     * get(i)==null : o.equals(get(i)))</code>, or -1 if there is no such
     * index.<p>
     *
     * @param pSourceArray array in which to search.
     * @param pObject element to search for.
     * @param pIndex  index at which the search starts.
     * 
     * @return the index in this List of the first occurence of the specified
     * 	       element started at pIndex, or -1 if the List does not contain this element.
     */
	public static int indexOf(float[] pSourceArray, float pObject, int pIndex)
	{
		if (pIndex >= 0)
		{
			int length = (pSourceArray == null) ? 0 : pSourceArray.length;
		    for (int i = pIndex; i < length; i++)
		    {
				if (pObject == pSourceArray[i])
				{
				    return i;
				}
		    }
		}
		return -1;
	}
	
    /**
     * Returns the index in this list of the last occurence of the specified
     * element, or -1 if the list does not contain this element.  
     * More formally, returns the lowest index <code>i</code> such that <code>(o==null ?
     * get(i)==null : o.equals(get(i)))</code>, or -1 if there is no such
     * index.<p>
     *
     * @param pSourceArray array in which to search.
     * @param pObject element to search for.
     * 
     * @return the index in this List of the last occurence of the specified
     * 	       element, or -1 if the List does not contain this element.
     */
	public static int lastIndexOf(float[] pSourceArray, float pObject)
	{
		return lastIndexOf(pSourceArray, pObject, -1);
	}
	
    /**
     * Returns the index in this list of the last occurence of the specified
     * element started at pIndex, or -1 if the list does not contain this element.  
     * More formally, returns the lowest index <code>i</code> such that <code>(o==null ?
     * get(i)==null : o.equals(get(i)))</code>, or -1 if there is no such
     * index.<p>
     *
     * @param pSourceArray array in which to search.
     * @param pObject element to search for.
     * @param pIndex  index at which the search starts.
     * 
     * @return the index in this List of the last occurence of the specified
     * 	       element started at pIndex, or -1 if the List does not contain this element.
     */
	public static int lastIndexOf(float[] pSourceArray, float pObject, int pIndex)
	{
		int length = (pSourceArray == null) ? 0 : pSourceArray.length;
		if (pIndex < length)
		{
			if (pIndex < 0)
			{
				pIndex = length - 1;
			}
		    for (int i = pIndex; i >= 0; i--)
		    {
				if (pObject == pSourceArray[i])
				{
				    return i;
				}
		    }
		}
		return -1;
	}
	
    /**
     * Inserts the specified element at the specified position in the given
     * array. Shifts the element currently at that position (if any) and
     * any subsequent elements to the right (adds one to their indices).
     * If the index is negative the element is added at the end.
     *
     * @param pSourceArray array in which to add.
     * @param pIndex index at which the specified element is to be inserted.
     * @param pElement element to be inserted.
     * @return returns a new array if needed.
     * @throws    IndexOutOfBoundsException if index is out of range
     *		  <code>(index &gt; size())</code>.
     */
	public static double[] add(double[] pSourceArray, int pIndex, double pElement)
	{
		int length = (pSourceArray == null) ? 0 : pSourceArray.length;
		if (pIndex < 0)
		{
			pIndex = length;
		}
		else if (pIndex > length)
		{
			throw new IndexOutOfBoundsException("The index is " + pIndex + ", should be smaller or equal to length " + length + "!");
		}
		double[] result = new double[length + 1];
		if (pIndex > 0)
		{
			System.arraycopy(pSourceArray, 0, result, 0, pIndex);
		}
		if (pIndex < length)
		{
			System.arraycopy(pSourceArray, pIndex, result, pIndex + 1, length - pIndex);
		}
		result[pIndex] = pElement;
		return result;
	}
	
    /**
     * Inserts the specified element at the end in the given
     * array.
     *
     * @param pSourceArray array in which to add.
     * @param pElement element to be inserted.
     * @return returns a new array if needed.
     */
	public static double[] add(double[] pSourceArray, double pElement)
	{
		return add(pSourceArray, -1, pElement);
	}
	
	/**
     * Inserts the specified array at the specified position in the given
     * array. Shifts the element currently at that position (if any) and
     * any subsequent elements to the right.
     * If the index is negative the element is added at the end.
     *
     * @param pSourceArray array in which to add.
     * @param pIndex index at which the specified element is to be inserted.
     * @param pArray elements to be inserted.
     * @return returns a new array if needed.
     * @throws IndexOutOfBoundsException if index is out of range
     *		   <code>(index &gt; size())</code>.
     */
    public static double[] addAll(double[] pSourceArray, int pIndex, double[] pArray)
    {
		int length = (pSourceArray == null) ? 0 : pSourceArray.length;
		if (pIndex < 0)
		{
			pIndex = length;
		}
		else if (pIndex > length)
		{
			throw new IndexOutOfBoundsException("The index is " + pIndex + ", should be smaller or equal to length " + length + "!");
		}
    	if (pArray == null || pArray.length == 0)
    	{
    		return pSourceArray;
    	}
    	else
    	{
    		double[] result = new double[length + pArray.length];
			if (pIndex > 0)
			{
				System.arraycopy(pSourceArray, 0, result, 0, pIndex);
			}
			if (pIndex < length)
			{
				System.arraycopy(pSourceArray, pIndex, result, pIndex + pArray.length, length - pIndex);
			}
    		System.arraycopy(pArray, 0, result, pIndex, pArray.length);
			return result;
    	}
    }
	
    /**
     * Inserts the specified array at the end in the given
     * array. 
     *
     * @param pSourceArray array in which to add.
     * @param pArray elements to be inserted.
     * @return returns a new array if needed.
     */
   	public static double[] addAll(double[] pSourceArray, double[] pArray)
    {
    	return addAll(pSourceArray, -1, pArray);
    }
	
    /**
     * Removes all elements between fromIndex (inclusive) and toIndex (exclusive of given array).
     *
     * @param pSourceArray array in which to delete.
     * @param pFromIndex   the first index.
     * @param pToIndex     the last index (not included).
     * @return returns a new array if needed.
     */
	public static double[] removeRange(double[] pSourceArray, int pFromIndex, int pToIndex)
	{
		int length = (pSourceArray == null) ? 0 : pSourceArray.length;
		if (pFromIndex < 0)
		{
			pFromIndex = length - 1;
		}
		else if (pFromIndex > length)
		{
			throw new IndexOutOfBoundsException("The fromIndex is " + pFromIndex + " and should be smaller or equal than " + length + "!");
		}
		if (pToIndex < 0)
		{
			pToIndex = length;
		}
		else if (pToIndex < pFromIndex || pToIndex > length)
		{
			throw new IndexOutOfBoundsException("The toIndex is " + pToIndex + " and should be between fromIndex + " + pFromIndex + " and length " + length + "!");
		}
		if (pFromIndex == pToIndex)
		{
			return pSourceArray;
		}
		else
		{
			int amount = pToIndex - pFromIndex;
			double[] result = new double[length - amount];
			if (pFromIndex > 0)
			{
				System.arraycopy(pSourceArray, 0, result, 0, pFromIndex);
			}
			if (pToIndex < length)
			{
				System.arraycopy(pSourceArray, pToIndex, result, pFromIndex, length - pToIndex);
			}
			return result;
		}
	}
	
    /**
     * Removes one element at the given index of given array.
     *
     * @param pSourceArray array in which to delete.
     * @param pIndex   the index where elements should be deleted.
     * @return returns a new array if needed.
     */
	public static double[] remove(double[] pSourceArray, int pIndex)
	{
		return removeRange(pSourceArray, pIndex, pIndex + 1);
	}
	
    /**
     * Removes the last element of given array.
     *
     * @param pSourceArray array in which to delete.
     * @return returns a new array if needed.
     */
	public static double[] removeLast(double[] pSourceArray)
	{
		return removeRange(pSourceArray, -1, -1);
	}
	
    /**
     * Removes all elements from the given index of given array.
     *
     * @param pSourceArray array in which to delete.
     * @param pIndex   the index starting to remove all elements.
     * @return returns a new array if needed.
     */
	public static double[] truncate(double[] pSourceArray, int pIndex)
	{
		return removeRange(pSourceArray, pIndex, -1);
	}

    /**
     * Removes all Elements.
     *
     * @param pSourceArray array in which to delete.
     * @return returns a new array if needed.
     */
	public static double[] clear(double[] pSourceArray)
	{
		return removeRange(pSourceArray, 0, -1);
	}
	
    /**
     * Returns true if this list contains the specified
     * element.  
     *
     * @param pSourceArray array in which to search.
     * @param pObject element to search for.
     * 
     * @return true if this list contains the specified
     * element.
     */
	public static boolean contains(double[] pSourceArray, double pObject)
	{
		return indexOf(pSourceArray, pObject, 0) >= 0;
	}

    /**
     * Returns the index in this list of the first occurence of the specified
     * element, or -1 if the list does not contain this element.  
     * More formally, returns the lowest index <code>i</code> such that <code>(o==null ?
     * get(i)==null : o.equals(get(i)))</code>, or -1 if there is no such
     * index.<p>
     *
     * @param pSourceArray array in which to search.
     * @param pObject element to search for.
     * 
     * @return the index in this List of the first occurence of the specified
     * 	       element, or -1 if the List does not contain this element.
     */
	public static int indexOf(double[] pSourceArray, double pObject)
	{
		return indexOf(pSourceArray, pObject, 0);
	}

    /**
     * Returns the index in this list of the first occurence of the specified
     * element started at pIndex, or -1 if the list does not contain this element.  
     * More formally, returns the lowest index <code>i</code> such that <code>(o==null ?
     * get(i)==null : o.equals(get(i)))</code>, or -1 if there is no such
     * index.<p>
     *
     * @param pSourceArray array in which to search.
     * @param pObject element to search for.
     * @param pIndex  index at which the search starts.
     * 
     * @return the index in this List of the first occurence of the specified
     * 	       element started at pIndex, or -1 if the List does not contain this element.
     */
	public static int indexOf(double[] pSourceArray, double pObject, int pIndex)
	{
		if (pIndex >= 0)
		{
			int length = (pSourceArray == null) ? 0 : pSourceArray.length;
		    for (int i = pIndex; i < length; i++)
		    {
				if (pObject == pSourceArray[i])
				{
				    return i;
				}
		    }
		}
		return -1;
	}
	
    /**
     * Returns the index in this list of the last occurence of the specified
     * element, or -1 if the list does not contain this element.  
     * More formally, returns the lowest index <code>i</code> such that <code>(o==null ?
     * get(i)==null : o.equals(get(i)))</code>, or -1 if there is no such
     * index.<p>
     *
     * @param pSourceArray array in which to search.
     * @param pObject element to search for.
     * 
     * @return the index in this List of the last occurence of the specified
     * 	       element, or -1 if the List does not contain this element.
     */
	public static int lastIndexOf(double[] pSourceArray, double pObject)
	{
		return lastIndexOf(pSourceArray, pObject, -1);
	}
	
    /**
     * Returns the index in this list of the last occurence of the specified
     * element started at pIndex, or -1 if the list does not contain this element.  
     * More formally, returns the lowest index <code>i</code> such that <code>(o==null ?
     * get(i)==null : o.equals(get(i)))</code>, or -1 if there is no such
     * index.<p>
     *
     * @param pSourceArray array in which to search.
     * @param pObject element to search for.
     * @param pIndex  index at which the search starts.
     * 
     * @return the index in this List of the last occurence of the specified
     * 	       element started at pIndex, or -1 if the List does not contain this element.
     */
	public static int lastIndexOf(double[] pSourceArray, double pObject, int pIndex)
	{
		int length = (pSourceArray == null) ? 0 : pSourceArray.length;
		if (pIndex < length)
		{
			if (pIndex < 0)
			{
				pIndex = length - 1;
			}
		    for (int i = pIndex; i >= 0; i--)
		    {
				if (pObject == pSourceArray[i])
				{
				    return i;
				}
		    }
		}
		return -1;
	}

	/**
	 * Checks if a specific array contains all elements of another array. It doesn't check
	 * if the array sizes are equal or the elements are duplicted.
	 * 
	 * @param <T> Component Type of Array
	 * @param pSource the source array
	 * @param pElements the elements to find
	 * @return <code>true</code> if the source array contains all elements, <code>false</code> otherwise
	 * @throws NullPointerException if the source array is null
	 */
	public static <T> boolean containsAll(T[] pSource, T[] pElements)
	{
		if (pSource == null)
		{
			return false;
		}
		
		if (pElements == null)
		{
			return true;
		}
		
		for (T element : pElements)
		{
			if (indexOf(pSource, element) < 0)
			{
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Checks if a specific array contains at least one element of another array.
	 * 
	 * @param <T> Component Type of Array
	 * @param pSource the source array
	 * @param pElements the elements to find
	 * @return <code>true</code> if the source array contains at least one element, <code>false</code> otherwise
	 * @throws NullPointerException if the source array is null
	 */
	public static <T> boolean containsOne(T[] pSource, T[] pElements)
	{
		if (pElements == null)
		{
			return false;
		}
		
		for (T element : pElements)
		{
			if (indexOf(pSource, element) >= 0)
			{
				return true;
			} 
		}
		
		return false;
	}
	
	/**
	 * Checks if the current array contains all elements of a specific array. It doesn't check
	 * if the array sizes are equal or the elements are duplicted.
	 * 
	 * @param pElements the elements to find
	 * @return <code>true</code> if the current array contains all elements, <code>false</code> otherwise
	 */
	public boolean containsAll(E[] pElements)
	{
		if (pElements == null)
		{
			return true;
		}
		
		for (Object obj : pElements)
		{
			if (indexOf(obj) < 0)
			{
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Checks if the current array contains at least one element of a specific array.
	 * 
	 * @param pElements the elements to find
	 * @return <code>true</code> if the current array contains at least one element, <code>false</code> otherwise
	 */
	public boolean containsOne(E[] pElements)
	{
		if (pElements == null)
		{
			return false;
		}
		
		for (Object obj : pElements)
		{
			if (indexOf(obj) >= 0)
			{
				return true;
			}
		}
		
		return true;
	}

	/**
	 * Creats a new array with a subset of the current elements.
	 * 
	 * @param pStart the start index
	 * @param pLength the length
	 * @return the elements as array
	 */
	public E[] toArray(int pStart, int pLength)
	{
		if (size < pStart + pLength)
		{
			throw new IllegalArgumentException("Source array has " + size + " element(s). Start index " + 
					                           pStart + " and length " + pLength + " are not possible!");
		}
		
		E[] result = (E[])Array.newInstance(array.getClass().getComponentType(), pLength);
		
		System.arraycopy(array, offset + pStart, result, 0, pLength);
		
		return result;
	}

	/**
	 * Gets the first element.
	 * 
	 * @return the first element
	 */
	public E first()
	{
		if (array == null || size == 0)
		{
			throw new NoSuchElementException();
		}
		
		return array[offset];
	}
	
	/**
	 * Gets the last element.
	 * 
	 * @return the last element
	 */
	public E last()
	{
		if (array == null || size == 0 || offset + size - 1 < 0)
		{
			throw new NoSuchElementException();
		}
		
		return array[offset + size - 1];
	}
	
	/**
	 * Gets a singleton instance of an empty enumeration.
	 * 
	 * @param <T> the element type
	 * @return the empty enumeration
	 */
	public static <T> Enumeration<T> emptyEnumeration() 
	{
        return (Enumeration<T>)EmptyEnumeration.EMPTY_ENUMERATION;
    }
	
    //****************************************************************
    // Subclass definition
    //****************************************************************

	/**
	 * The <code>EmptyEnumeration</code> is a simple {@link Enumeration} without elements.
	 * 
	 * @param <E> the element type
	 * @author René Jahn
	 */
    private static class EmptyEnumeration<E> implements Enumeration<E> 
    {
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Class members
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 
    	/** the singleton instance. */
        private static final EmptyEnumeration<Object> EMPTY_ENUMERATION = new EmptyEnumeration<Object>();

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Interface implementation
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /**
         * {@inheritDoc}
         */
        public boolean hasMoreElements() 
        { 
        	return false; 
        }
        
        /**
         * {@inheritDoc}
         */
        public E nextElement() 
        { 
        	throw new NoSuchElementException(); 
        }
        
    }	// EmptyEnumeration
	
}	// ArrayUtil
