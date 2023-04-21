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
 * The serializer for byte arrays.
 *  
 * @author Martin Handsteiner
 */
public class ByteArraySerializer extends AbstractSizedSerializer implements ITypeSerializer<byte[]>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** min. byte value of <code>String</code> type. */
	private static final int TYPE_BYTE_ARRAY_MIN = 30;

	/** max. byte value of <code>String</code> type. */
	private static final int TYPE_BYTE_ARRAY_MAX = 32;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface Implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public Class<byte[]> getTypeClass()
	{
		return byte[].class;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getMinValue()
	{
		return TYPE_BYTE_ARRAY_MIN;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getMaxValue()
	{
		return TYPE_BYTE_ARRAY_MAX;
	}

	/**
	 * {@inheritDoc}
	 */
	public byte[] read(UniversalSerializer pSerializer, DataInputStream pIn, int pTypeValue, TypeCache pCache) throws Exception
	{
		int size = readSize(pIn, pTypeValue, TYPE_BYTE_ARRAY_MIN, TYPE_BYTE_ARRAY_MAX);

		byte[] array = new byte[size];
		pIn.readFully(array);
		
		return array;
	}

	/**
	 * {@inheritDoc}
	 */
	public void write(UniversalSerializer pSerializer, DataOutputStream pOut, byte[] pObject, TypeCache pCache) throws Exception
	{
		writeSize(pOut, pObject.length, TYPE_BYTE_ARRAY_MIN, TYPE_BYTE_ARRAY_MAX);
		pOut.write(pObject);
	}
	
}	// ByteArraySerializer
