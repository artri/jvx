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
 * 01.05.2010 - [JR] - creation
 */
package com.sibvisions.rad.remote.serializer;

import java.util.ArrayList;
import java.util.HashMap;

import javax.rad.type.bean.BeanType;

/**
 * The <code>TypeCache</code> is the cache for {@link BeanType}s which will be read or written from the
 * {@link com.sibvisions.rad.remote.UniversalSerializer} in a single read or write operation.
 * 
 * @author René Jahn
 */
public class TypeCache
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** Definition indexes. */
	private HashMap<BeanType, Integer> beanTypeOrder;

	/** Supported definitions. */
	private ArrayList<BeanType> beanTypeDefinitions;
	

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets a bean type by index.
	 * 
	 * @param pIndex the type index
	 * @return the bean type
	 * @throws ArrayIndexOutOfBoundsException if the index is invalid
	 */
	synchronized BeanType get(int pIndex)
	{
		if (beanTypeDefinitions == null)
		{
			throw new ArrayIndexOutOfBoundsException(pIndex);
		}
		
		return beanTypeDefinitions.get(pIndex);
	}
	
	/**
	 * Puts a bean type to the cache.
	 * 
	 * @param pType the bean type
	 * @return the cache index
	 */
	synchronized Integer put(BeanType pType)
	{
		if (beanTypeDefinitions == null)
		{
			beanTypeDefinitions = new ArrayList<BeanType>();
			beanTypeOrder = new HashMap<BeanType, Integer>();
		}
		
		Integer index = Integer.valueOf(beanTypeDefinitions.size());
		
		beanTypeDefinitions.add(pType);
		beanTypeOrder.put(pType, index);
		
		return index;

	}
	
	/**
	 * Gets the cache index for a specific bean type.
	 * 
	 * @param pType the bean type
	 * @return the index or <code>null</code> if bean type is unknown
	 */
	synchronized Integer indexOf(BeanType pType)
	{
		if (beanTypeOrder == null)
		{
			return null;
		}
		
		return beanTypeOrder.get(pType);
	}

}	// TypeCache
