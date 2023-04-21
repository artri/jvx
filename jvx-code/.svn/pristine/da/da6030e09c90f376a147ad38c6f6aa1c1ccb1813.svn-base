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
 * 20.01.2010 - [HM] - creation
 */
package com.sibvisions.rad.remote.serializer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import com.sibvisions.rad.remote.UniversalSerializer;
import com.sibvisions.util.ArrayUtil;

/**
 * The serializer for any kind of {@link List}.
 *  
 * @author Martin Handsteiner
 */
public class ListSerializer extends AbstractSizedSerializer 
                            implements ITypeSerializer<List>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** min. byte value of <code>String</code> type. */
	private static final int TYPE_LIST_MIN = 42;

	/** max. byte value of <code>String</code> type. */
	private static final int TYPE_LIST_MAX = 44;

	/** Supported Lists. */
	private ArrayList<Class<? extends List>> registeredLists = new ArrayList<Class<? extends List>>();
	
	/** List indexes. */
	private HashMap<Class<? extends List>, Integer> listOrder = new HashMap<Class<? extends List>, Integer>();
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Constructs a new instance of <code>ListSerializer</code> with support for
	 * {@link ArrayList}, {@link ArrayUtil} and {@link Vector}.
	 */
	public ListSerializer()
	{
		registerList(ArrayList.class);
		registerList(ArrayUtil.class);
		registerList(Vector.class);
        registerList(LinkedList.class);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface Implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public Class<List> getTypeClass()
	{
		return List.class;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getMinValue()
	{
		return TYPE_LIST_MIN;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getMaxValue()
	{
		return TYPE_LIST_MAX;
	}

	/**
	 * {@inheritDoc}
	 */
	public List read(UniversalSerializer pSerializer, DataInputStream pIn, int pTypeValue, TypeCache pCache) throws Exception
	{
		int size = readSize(pIn, pTypeValue, TYPE_LIST_MIN, TYPE_LIST_MAX);

		int index = pIn.readUnsignedByte();
		
		List list;
		try
		{
			list = registeredLists.get(index).newInstance();
		}
		catch (Exception ex)
		{
			list = new ArrayList(size);
		}
		
		for (int i = 0; i < size; i++)
		{
			list.add(pSerializer.read(pIn, pCache));
		}
		
		return list;
	}

	/**
	 * {@inheritDoc}
	 */
	public void write(UniversalSerializer pSerializer, DataOutputStream pOut, List pObject, TypeCache pCache) throws Exception
	{
		writeSize(pOut, pObject.size(), TYPE_LIST_MIN, TYPE_LIST_MAX);
		
		Integer index = listOrder.get(pObject.getClass());
		if (index == null)
		{
			pOut.writeByte(0);
		}
		else
		{
			pOut.writeByte(index.intValue());
		}
		
		for (int i = 0; i < pObject.size(); i++)
		{
			pSerializer.write(pOut, pObject.get(i), pCache);
		}
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Registers a list type.
	 * 
	 * @param pListType list type.
	 */
	public void registerList(Class<? extends List> pListType)
	{
		if (listOrder.get(pListType) == null)
		{
			listOrder.put(pListType, Integer.valueOf(registeredLists.size()));
			registeredLists.add(pListType);
		}
	}
	
}	// ListSerializer
