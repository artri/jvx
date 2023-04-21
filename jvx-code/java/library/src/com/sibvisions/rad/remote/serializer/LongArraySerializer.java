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

import com.sibvisions.rad.remote.UniversalSerializer;

/**
 * The serializer for {@link Long} arrays.
 *  
 * @author Martin Handsteiner
 */
public class LongArraySerializer extends AbstractSizedSerializer 
                                 implements ITypeSerializer<long[]>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** min. byte value of <code>long</code> type. */
	private static final int TYPE_LONG_ARRAY_MIN = 65;

	/** max. byte value of <code>long</code> type. */
	private static final int TYPE_LONG_ARRAY_MAX = 67;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface Implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public Class<long[]> getTypeClass()
	{
		return long[].class;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getMinValue()
	{
		return TYPE_LONG_ARRAY_MIN;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getMaxValue()
	{
		return TYPE_LONG_ARRAY_MAX;
	}

	/**
	 * {@inheritDoc}
	 */
	public long[] read(UniversalSerializer pSerializer, DataInputStream pIn, int pTypeValue, TypeCache pCache) throws Exception
	{
		int size = readSize(pIn, pTypeValue, TYPE_LONG_ARRAY_MIN, TYPE_LONG_ARRAY_MAX);

		long[] array = new long[size];
		for (int i = 0; i < size; i++)
		{
			array[i] = pIn.readLong();
		}
		
		return array;
	}

	/**
	 * {@inheritDoc}
	 */
	public void write(UniversalSerializer pSerializer, DataOutputStream pOut, long[] pObject, TypeCache pCache) throws Exception
	{
		writeSize(pOut, pObject.length, TYPE_LONG_ARRAY_MIN, TYPE_LONG_ARRAY_MAX);

		for (int i = 0; i < pObject.length; i++)
		{
			pOut.writeLong(pObject[i]);
		}
	}
	
}	// LongArraySerializer
