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
 * The serializer for {@link Short}.
 *  
 * @author Martin Handsteiner
 */
public class ShortSerializer implements ITypeSerializer<Short>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** <code>Short</code> type. */
	private static final int TYPE_SHORT = 9;
	
	/** <code>Short</code> type, if the value equals 0. */
	private static final int TYPE_SHORT_0 = 10;
	
	/** <code>Float</code> 0. */
	private static final Short SHORT_0 = Short.valueOf((short)0);
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface Implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public Class<Short> getTypeClass()
	{
		return Short.class;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getMinValue()
	{
		return TYPE_SHORT;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getMaxValue()
	{
		return TYPE_SHORT_0;
	}

	/**
	 * {@inheritDoc}
	 */
	public Short read(UniversalSerializer pSerializer, DataInputStream pIn, int pTypeValue, TypeCache pCache) throws Exception
	{
		if (pTypeValue == TYPE_SHORT)
		{
			return Short.valueOf(pIn.readShort());
		}
		else
		{
			return SHORT_0;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void write(UniversalSerializer pSerializer, DataOutputStream pOut, Short pObject, TypeCache pCache) throws Exception
	{
		short value = pObject.shortValue();
		
		if (value == 0)
		{
			pOut.writeByte(TYPE_SHORT_0);
		}
		else
		{
			pOut.writeByte(TYPE_SHORT);
			pOut.writeShort(value);
		}
	}
	
}	// ShortSerializer
