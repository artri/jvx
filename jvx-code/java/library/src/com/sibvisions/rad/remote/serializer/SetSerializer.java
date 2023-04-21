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
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

import com.sibvisions.rad.remote.UniversalSerializer;

/**
 * The serializer for any kind of {@link Set}.
 *  
 * @author Martin Handsteiner
 */
public class SetSerializer extends AbstractSizedSerializer 
                            implements ITypeSerializer<Set>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** min. byte value of <code>Set</code> type. */
	private static final int TYPE_SET_MIN = 73;

	/** max. byte value of <code>Set</code> type. */
	private static final int TYPE_SET_MAX = 75;

	/** Supported Lists. */
	private ArrayList<Class<? extends Set>> registeredSets = new ArrayList<Class<? extends Set>>();
	
	/** List indexes. */
	private HashMap<Class<? extends Set>, Integer> setOrder = new HashMap<Class<? extends Set>, Integer>();
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Constructs a new instance of <code>SetSerializer</code> with support for
	 * {@link HashSet}, {@link LinkedHashSet} and {@link TreeSet}.
	 */
	public SetSerializer()
	{
        registerSet(HashSet.class);
        registerSet(LinkedHashSet.class);
        registerSet(TreeSet.class);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface Implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public Class<Set> getTypeClass()
	{
		return Set.class;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getMinValue()
	{
		return TYPE_SET_MIN;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getMaxValue()
	{
		return TYPE_SET_MAX;
	}

	/**
	 * {@inheritDoc}
	 */
	public Set read(UniversalSerializer pSerializer, DataInputStream pIn, int pTypeValue, TypeCache pCache) throws Exception
	{
		int size = readSize(pIn, pTypeValue, TYPE_SET_MIN, TYPE_SET_MAX);

		int index = pIn.readUnsignedByte();
		
		Set set;
		try
		{
		    set = registeredSets.get(index).newInstance();
		}
		catch (Exception ex)
		{
		    set = new HashSet(size);
		}
		
		for (int i = 0; i < size; i++)
		{
		    set.add(pSerializer.read(pIn, pCache));
		}
		
		return set;
	}

	/**
	 * {@inheritDoc}
	 */
	public void write(UniversalSerializer pSerializer, DataOutputStream pOut, Set pObject, TypeCache pCache) throws Exception
	{
		writeSize(pOut, pObject.size(), TYPE_SET_MIN, TYPE_SET_MAX);
		
		Integer index = setOrder.get(pObject.getClass());
		if (index == null)
		{
			pOut.writeByte(0);
		}
		else
		{
			pOut.writeByte(index.intValue());
		}
		
		for (Object object : pObject)
		{
			pSerializer.write(pOut, object, pCache);
		}
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Registers a set type.
	 * 
	 * @param pSetType set type.
	 */
	public void registerSet(Class<? extends Set> pSetType)
	{
		if (setOrder.get(pSetType) == null)
		{
			setOrder.put(pSetType, Integer.valueOf(registeredSets.size()));
			registeredSets.add(pSetType);
		}
	}
	
}	// ListSerializer
