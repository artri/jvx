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
 * The serializer for {@link Float}.
 *  
 * @author Martin Handsteiner
 */
public class FloatSerializer implements ITypeSerializer<Float>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** <code>Float</code> type. */
	private static final int TYPE_FLOAT = 5;

	/** <code>Float</code> type, if the value equals 0. */
	private static final int TYPE_FLOAT_0 = 6;
	
	/** <code>Float</code> 0. */
	private static final Float FLOAT_0 = Float.valueOf(0);
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface Implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public Class<Float> getTypeClass()
	{
		return Float.class;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getMinValue()
	{
		return TYPE_FLOAT;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getMaxValue()
	{
		return TYPE_FLOAT_0;
	}

	/**
	 * {@inheritDoc}
	 */
	public Float read(UniversalSerializer pSerializer, DataInputStream pIn, int pTypeValue, TypeCache pCache) throws Exception
	{
		if (pTypeValue == TYPE_FLOAT)
		{
			return Float.valueOf(pIn.readFloat());
		}
		else
		{
			return FLOAT_0;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void write(UniversalSerializer pSerializer, DataOutputStream pOut, Float pObject, TypeCache pCache) throws Exception
	{
		float value = pObject.floatValue();
		
		if (value == 0)
		{
			pOut.writeByte(TYPE_FLOAT_0);
		}
		else
		{
			pOut.writeByte(TYPE_FLOAT);
			pOut.writeFloat(value);
		}
	}
	
}	// FloatSerializer
