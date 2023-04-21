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
 * 01.10.2008 - [RH] - creation
 */
package research;

import java.lang.reflect.Array;

/**
 * <code>AdvancedArray</code> extends the functionality to Java Arrays, in a way
 * to handle it as Java Lists. This brings functionality and short usage of
 * memory together.
 * 
 * @author Roland Hörmann
 */
public class AdvancedArray<E>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	private E[]	eaStorage;
	
	private Class cElementClass;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public AdvancedArray(Class cElementClass, int iInitialCapacity)
	{
		this.cElementClass = cElementClass;
		eaStorage = ensureSize(iInitialCapacity);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	private E[] ensureSize(int iSize)
	{
		return (E[])Array.newInstance(cElementClass.getComponentType(), iSize);
	}

	public int size()
	{
		return eaStorage.length;
	}

	public boolean isEmpty()
	{
		return size() == 0;
	}

	public void add(E element)
	{
		add(size(), element);
	}

	public void add(int iIndex, E element)
	{
		E[] eaNewStorage = ensureSize(size() + 1);
		System.arraycopy(eaStorage, 0, eaNewStorage, 0, size());
		eaStorage[size()] = element;
	}

	public void remove(Object oObject)
	{
		remove(indexOf(oObject));
	}

	public void remove(int iIndex)
	{

	}

	public int indexOf(Object oObject)
	{
		return -1;
	}

	public boolean contains(Object oObject)
	{
		return indexOf(oObject) >= 0;
	}

} // AdvancedArray
