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
 * The serializer for {@link Float} arrays.
 *  
 * @author Martin Handsteiner
 */
public class FloatArraySerializer extends AbstractSizedSerializer implements ITypeSerializer<float[]>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** min. byte value of <code>String</code> type. */
	private static final int TYPE_FLOAT_ARRAY_MIN = 56;

	/** max. byte value of <code>String</code> type. */
	private static final int TYPE_FLOAT_ARRAY_MAX = 58;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface Implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public Class<float[]> getTypeClass()
	{
		return float[].class;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getMinValue()
	{
		return TYPE_FLOAT_ARRAY_MIN;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getMaxValue()
	{
		return TYPE_FLOAT_ARRAY_MAX;
	}

	/**
	 * {@inheritDoc}
	 */
	public float[] read(UniversalSerializer pSerializer, DataInputStream pIn, int pTypeValue, TypeCache pCache) throws Exception
	{
		int size = readSize(pIn, pTypeValue, TYPE_FLOAT_ARRAY_MIN, TYPE_FLOAT_ARRAY_MAX);

		float[] array = new float[size];
		for (int i = 0; i < size; i++)
		{
			array[i] = pIn.readFloat();
		}
		
		return array;
	}

	/**
	 * {@inheritDoc}
	 */
	public void write(UniversalSerializer pSerializer, DataOutputStream pOut, float[] pObject, TypeCache pCache) throws Exception
	{
		writeSize(pOut, pObject.length, TYPE_FLOAT_ARRAY_MIN, TYPE_FLOAT_ARRAY_MAX);

		for (int i = 0; i < pObject.length; i++)
		{
			pOut.writeFloat(pObject[i]);
		}
	}
	
}	// FloatArraySerializer
