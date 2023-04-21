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
import java.util.Hashtable;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.TreeMap;

import com.sibvisions.rad.remote.UniversalSerializer;

/**
 * The serializer for any kind of {@link Map}.
 *  
 * @author Martin Handsteiner
 */
public class MapSerializer extends AbstractSizedSerializer implements ITypeSerializer<Map>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** min. byte value of <code>String</code> type. */
	private static final int TYPE_MAP_MIN = 45;

	/** max. byte value of <code>String</code> type. */
	private static final int TYPE_MAP_MAX = 47;

	/** Supported Maps. */
	private ArrayList<Class<? extends Map>> registeredMaps = new ArrayList<Class<? extends Map>>();
	
	/** List indexes. */
	private HashMap<Class<? extends Map>, Integer> mapOrder = new HashMap<Class<? extends Map>, Integer>();
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Constructs a new instance of <code>MapSerializer</code> with support for 
	 * {@link HashMap}, {@link Hashtable} and {@link Properties}.
	 */
	public MapSerializer()
	{
		registerMap(HashMap.class);
		registerMap(Hashtable.class);
		registerMap(Properties.class);
		registerMap(LinkedHashMap.class);
        registerMap(IdentityHashMap.class);
        registerMap(TreeMap.class);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface Implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public Class<Map> getTypeClass()
	{
		return Map.class;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getMinValue()
	{
		return TYPE_MAP_MIN;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getMaxValue()
	{
		return TYPE_MAP_MAX;
	}

	/**
	 * {@inheritDoc}
	 */
	public Map read(UniversalSerializer pSerializer, DataInputStream pIn, int pTypeValue, TypeCache pCache) throws Exception
	{
		int size = readSize(pIn, pTypeValue, TYPE_MAP_MIN, TYPE_MAP_MAX);

		int index = pIn.readUnsignedByte();
		
		Map map;
		try
		{
			map = registeredMaps.get(index).newInstance();
		}
		catch (Exception ex)
		{
			map = new HashMap(size);
		}
		
		for (int i = 0; i < size; i++)
		{
			map.put(pSerializer.read(pIn, pCache), pSerializer.read(pIn, pCache));
		}
		
		return map;
	}

	/**
	 * {@inheritDoc}
	 */
	public void write(UniversalSerializer pSerializer, DataOutputStream pOut, Map pObject, TypeCache pCache) throws Exception
	{
		writeSize(pOut, pObject.size(), TYPE_MAP_MIN, TYPE_MAP_MAX);
		
		Integer index = mapOrder.get(pObject.getClass());
		if (index == null)
		{
			pOut.writeByte(0);
		}
		else
		{
			pOut.writeByte(index.intValue());
		}
		
		for (Object entry : pObject.entrySet())
		{
			pSerializer.write(pOut, ((Entry)entry).getKey(), pCache);
			pSerializer.write(pOut, ((Entry)entry).getValue(), pCache);
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Registers a list type.
	 * 
	 * @param pMapType list type.
	 */
	public void registerMap(Class<? extends Map> pMapType)
	{
		if (mapOrder.get(pMapType) == null)
		{
			mapOrder.put(pMapType, Integer.valueOf(registeredMaps.size()));
			registeredMaps.add(pMapType);
		}
	}
	
}	// MapSerializer
