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
 * The serializer for {@link Integer}.
 *  
 * @author Martin Handsteiner
 */
public class IntegerSerializer implements ITypeSerializer<Integer>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** <code>Integer</code> type. */
	private static final int TYPE_INTEGER = 11;
	
	/** <code>Integer</code> type, if the value equals 0. */
	private static final int TYPE_INTEGER_0 = 12;
	
	/** <code>Float</code> 0. */
	private static final Integer INTEGER_0 = Integer.valueOf(0);
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface Implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public Class<Integer> getTypeClass()
	{
		return Integer.class;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getMinValue()
	{
		return TYPE_INTEGER;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getMaxValue()
	{
		return TYPE_INTEGER_0;
	}

	/**
	 * {@inheritDoc}
	 */
	public Integer read(UniversalSerializer pSerializer, DataInputStream pIn, int pTypeValue, TypeCache pCache) throws Exception
	{
		if (pTypeValue == TYPE_INTEGER)
		{
			return Integer.valueOf(pIn.readInt());
		}
		else
		{
			return INTEGER_0;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void write(UniversalSerializer pSerializer, DataOutputStream pOut, Integer pObject, TypeCache pCache) throws Exception
	{
		int value = pObject.intValue();
		
		if (value == 0)
		{
			pOut.writeByte(TYPE_INTEGER_0);
		}
		else
		{
			pOut.writeByte(TYPE_INTEGER);
			pOut.writeInt(value);
		}
	}
	
}	// IntegerSerializer
