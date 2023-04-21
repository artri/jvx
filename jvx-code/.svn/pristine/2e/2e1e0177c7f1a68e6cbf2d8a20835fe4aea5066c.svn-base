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
 * The serializer for {@link Double}.
 *  
 * @author Martin Handsteiner
 */
public class DoubleSerializer implements ITypeSerializer<Double>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** <code>Double</code> type. */
	private static final int TYPE_DOUBLE = 7;
	
	/** <code>Double</code> type, if the value equals 0. */
	private static final int TYPE_DOUBLE_0 = 8;
	
	/** <code>Float</code> 0. */
	private static final Double DOUBLE_0 = Double.valueOf(0);
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface Implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public Class<Double> getTypeClass()
	{
		return Double.class;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getMinValue()
	{
		return TYPE_DOUBLE;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getMaxValue()
	{
		return TYPE_DOUBLE_0;
	}

	/**
	 * {@inheritDoc}
	 */
	public Double read(UniversalSerializer pSerializer, DataInputStream pIn, int pTypeValue, TypeCache pCache) throws Exception
	{
		if (pTypeValue == TYPE_DOUBLE)
		{
			return Double.valueOf(pIn.readDouble());
		}
		else
		{
			return DOUBLE_0;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void write(UniversalSerializer pSerializer, DataOutputStream pOut, Double pObject, TypeCache pCache) throws Exception
	{
		double value = pObject.doubleValue();
		
		if (value == 0)
		{
			pOut.writeByte(TYPE_DOUBLE_0);
		}
		else
		{
			pOut.writeByte(TYPE_DOUBLE);
			pOut.writeDouble(value);
		}
	}
	
}	// DoubleSerializer
