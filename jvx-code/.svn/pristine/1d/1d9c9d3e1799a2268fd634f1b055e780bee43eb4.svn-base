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
 * 14.09.2009 - [HM] - creation
 */
package com.sibvisions.util;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * The <code>ImmutableArray</code> gives the functionality of immutable arrays.
 * 
 * @author Martin Handsteiner
 *
 * @param <V> value class
 */
public class ImmutableArray<V>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The object array. */
	private V[] array;
	
	/** The hashCode. */
	private transient int hashCode = 0;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Constructs a new Array.
	 * 
	 * @param pArray the array. 
	 */
	public ImmutableArray(V... pArray)
	{
		array = pArray;
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
	    if (hashCode == 0 && array != null)
	    {
	        hashCode = Arrays.hashCode(array);
	    }
	    return hashCode;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
    public boolean equals(Object pObject)
    {
    	if (pObject == this)
    	{
    	    return true;
    	}
    	else if (pObject instanceof ImmutableArray)
    	{
            return Arrays.equals(array, ((ImmutableArray)pObject).array);
    	}
        else if (pObject instanceof Object[])
        {
            return Arrays.equals(array, (Object[])pObject);
        }
    	else
    	{
    	    return false;
    	}
    }

	/**
	 * {@inheritDoc}
	 */
	@Override
    public String toString()
    {
		return Arrays.toString(array);
    }

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the length of the array.
	 * 
	 * @return the length of the array.
	 */
	public int size()
	{
		return array.length;
	}
	
	/**
	 * Gets the value of given index.
	 * 
	 * @param pIndex the index.
	 * @return the value of given index.
	 */
	public V get(int pIndex)
	{
		return array[pIndex];
	}

	/**
	 * Sets the value of a given index.
	 * 
	 * @param pIndex the index.
	 * @param pValue the value.
	 * @return the new ImmutableArray.
	 */
	public ImmutableArray<V> set(int pIndex, V pValue)
	{
		V[] result = array.clone();
		result[pIndex] = pValue;
		
		return new ImmutableArray(result);	
	}
	
	/**
	 * Adds the given <code>ImmutableArray</code> to this <code>ImmutableArray</code>.
	 * 
	 * @param pImmutableArray the <code>ImmutableArray</code>.
	 * @return the new ImmutableArray.
	 */
	public ImmutableArray<V> add(ImmutableArray<V> pImmutableArray)
	{
		return add(array.length, pImmutableArray);
	}
	
	/**
	 * Adds the given array to this <code>ImmutableArray</code>.
	 * 
	 * @param pArray the array.
	 * @return the new ImmutableArray.
	 */
	public ImmutableArray<V> add(V... pArray)
	{
		return add(array.length, pArray);
	}

	/**
	 * Adds the given <code>ImmutableArray</code> to this <code>ImmutableArray</code> at the given index.
	 * 
	 * @param pIndex the index.
	 * @param pImmutableArray the <code>ImmutableArray</code>.
	 * @return the new ImmutableArray.
	 */
	public ImmutableArray<V> add(int pIndex, ImmutableArray<V> pImmutableArray)
	{
		if (pImmutableArray == null)
		{
			return this;
		}
		else
		{
			return add(pIndex, pImmutableArray.array);
		}
	}
	
	/**
	 * Adds the given array to this <code>ImmutableArray</code> at the given index.
	 * 
	 * @param pIndex the index.
	 * @param pArray the array.
	 * @return the new ImmutableArray.
	 */
	public ImmutableArray<V> add(int pIndex, V... pArray)
	{
		if (pArray == null || pArray.length == 0)
		{
			return this;
		}
		else
		{
			return new ImmutableArray(ArrayUtil.addAll(array, pIndex, pArray));
		}
	}

	/**
	 * Gets the index of the given value.
	 * 
	 * @param pValue the value.
	 * @return the index of the given value.
	 */
	public int indexOf(V pValue)
	{
		return ArrayUtil.indexOf(array, pValue);
	}
	
	/**
	 * Gets true, if the array contains the given value.
	 * 
	 * @param pValue the value.
	 * @return true, if the array contains the given value.
	 */
	public boolean contains(V pValue)
	{
		return ArrayUtil.indexOf(array, pValue) >= 0;
	}
	
	/**
	 * Removes the given <code>ImmutableArray</code> to this <code>ImmutableArray</code>.
	 * 
	 * @param pImmutableArray the <code>ImmutableArray</code>.
	 * @return the new ImmutableArray.
	 */
	public ImmutableArray<V> remove(ImmutableArray<V> pImmutableArray)
	{
		if (pImmutableArray == null)
		{
			return this;
		}
		else
		{
			return remove(pImmutableArray.array);
		}
	}
	
	/**
	 * Removes the given array to this <code>ImmutableArray</code>.
	 * 
	 * @param pArray the array.
	 * @return the new ImmutableArray.
	 */
	public ImmutableArray<V> remove(V... pArray)
	{
		if (pArray == null || pArray.length == 0)
		{
			return this;
		}
		else
		{
			V[] temp = array.clone();
			
			int size = temp.length;
			
			for (int i = 0; i < pArray.length; i++)
			{
				int index = indexOf(pArray[i]);
				if (index >= 0)
				{
					size--;
					System.arraycopy(temp, index + 1, temp, index, size - index);
				}
			}
			
			if (size == array.length)
			{
				return this;
			}
			else
			{
				V[] result = (V[])Array.newInstance(array.getClass().getComponentType(), size);
				
				System.arraycopy(temp, 0, result, 0, size);
				return new ImmutableArray(result);
			}
		}
	}

	/**
	 * Removes one element at the given index.
	 * 
	 * @param pIndex the index.
	 * @return the new ImmutableArray.
	 */
	public ImmutableArray<V> remove(int pIndex)
	{
		return remove(pIndex, 1);
	}

	/**
	 * Removes all elements.
	 * 
	 * @return the new ImmutableArray.
	 */
	public ImmutableArray<V> removeAll()
	{
		return remove(0, array.length);
	}

	/**
	 * Removes the first element.
	 * 
	 * @return the new ImmutableArray.
	 */
	public ImmutableArray<V> removeFirst()
	{
		return remove(0, 1);
	}

	/**
	 * Removes the given amount at the beginning.
	 * 
	 * @param pAmount the amount.
	 * @return the new ImmutableArray.
	 */
	public ImmutableArray<V> removeFirst(int pAmount)
	{
		return remove(0, pAmount);
	}

	/**
	 * Removes the last element.
	 * 
	 * @return the new ImmutableArray.
	 */
	public ImmutableArray<V> removeLast()
	{
		return remove(array.length - 1, 1);
	}

	/**
	 * Removes the given amount at the end.
	 * 
	 * @param pAmount the amount.
	 * @return the new ImmutableArray.
	 */
	public ImmutableArray<V> removeLast(int pAmount)
	{
		return remove(array.length - pAmount, pAmount);
	}

	/**
	 * Removes the given amount of elements at the given index.
	 * 
	 * @param pIndex the index.
	 * @param pAmount the amount.
	 * @return the new ImmutableArray.
	 */
	public ImmutableArray<V> remove(int pIndex, int pAmount)
	{
		if (pAmount == 0)
		{
			return this;
		}
		else
		{
			V[] result = (V[])Array.newInstance(array.getClass().getComponentType(), array.length - pAmount);
				
			System.arraycopy(array, 0, result, 0, pIndex);
			System.arraycopy(array, pIndex + pAmount, result, pIndex, result.length - pIndex);
			return new ImmutableArray(result);
		}
	}

	/**
	 * Gets a mutable array of this immutable array. 
	 * 
	 * @return a mutable array,
	 */
	public V[] toArray()
	{
		return array.clone();
	}

}	// ImmutableArray
