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
 * The serializer for {@link Long}.
 *  
 * @author Martin Handsteiner
 */
public class LongSerializer implements ITypeSerializer<Long>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** <code>Long</code> type. */
	private static final int TYPE_LONG = 13;
	
	/** <code>Long</code> type, if the value equals 0. */
	private static final int TYPE_LONG_0 = 14;
	
	/** <code>Float</code> 0. */
	private static final Long LONG_0 = Long.valueOf(0);
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface Implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public Class<Long> getTypeClass()
	{
		return Long.class;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getMinValue()
	{
		return TYPE_LONG;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getMaxValue()
	{
		return TYPE_LONG_0;
	}

	/**
	 * {@inheritDoc}
	 */
	public Long read(UniversalSerializer pSerializer, DataInputStream pIn, int pTypeValue, TypeCache pCache) throws Exception
	{
		if (pTypeValue == TYPE_LONG)
		{
			return Long.valueOf(pIn.readLong());
		}
		else
		{
			return LONG_0;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void write(UniversalSerializer pSerializer, DataOutputStream pOut, Long pObject, TypeCache pCache) throws Exception
	{
		long value = pObject.longValue();
		
		if (value == 0)
		{
			pOut.writeByte(TYPE_LONG_0);
		}
		else
		{
			pOut.writeByte(TYPE_LONG);
			pOut.writeLong(value);
		}
	}
	
}	// LongSerializer
