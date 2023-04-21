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
import java.math.BigDecimal;
import java.math.BigInteger;

import com.sibvisions.rad.remote.UniversalSerializer;

/**
 * The serializer for {@link BigDecimal}.
 *  
 * @author Martin Handsteiner
 */
public class DecimalSerializer extends AbstractSizedSerializer implements ITypeSerializer<BigDecimal>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** min. byte value of <code>BigDecimal</code> type. */
	private static final int TYPE_BIGDECIMAL_MIN = 79;

	/** max. byte value of <code>BigDecimal</code> type. */
	private static final int TYPE_BIGDECIMAL_MAX = 88;

	/** min. byte value of <code>BigDecimal</code> type, if scale equals 0. */
	private static final int TYPE_BIGDECIMAL_NOSCALE_MIN = 89;

	/** max. byte value of <code>BigDecimal</code> type, if scale equals 0. */
	private static final int TYPE_BIGDECIMAL_NOSCALE_MAX = 98;
	
	/** <code>BigDecimal</code> 0. */
	private static BigDecimal[] bigDecimalCache = new BigDecimal[256];
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface Implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public Class<BigDecimal> getTypeClass()
	{
		return BigDecimal.class;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getMinValue()
	{
		return TYPE_BIGDECIMAL_MIN;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getMaxValue()
	{
		return TYPE_BIGDECIMAL_NOSCALE_MAX;
	}

	/**
	 * {@inheritDoc}
	 */
	public BigDecimal read(UniversalSerializer pSerializer, DataInputStream pIn, int pTypeValue, TypeCache pCache) throws Exception
	{
		int size;
		
		if (pTypeValue < TYPE_BIGDECIMAL_NOSCALE_MIN)
		{
			size = readSize(pIn, pTypeValue, TYPE_BIGDECIMAL_MIN, TYPE_BIGDECIMAL_MAX);
		}
		else
		{
			size = readSize(pIn, pTypeValue, TYPE_BIGDECIMAL_NOSCALE_MIN, TYPE_BIGDECIMAL_NOSCALE_MAX);
		}
		
		byte[] byContent = new byte[size];
		
		pIn.readFully(byContent);
		
		if (pTypeValue < TYPE_BIGDECIMAL_NOSCALE_MIN)
		{
			return new BigDecimal(new BigInteger(byContent), pIn.readUnsignedByte());
		}
		else if (size == 1)
		{
			int index = byContent[0] & 0xff;
			BigDecimal decimal = bigDecimalCache[index];
			if (decimal == null)
			{
				decimal = new BigDecimal(new BigInteger(byContent));
				bigDecimalCache[index] = decimal;
			}
			return decimal;
		}
		else
		{
			return new BigDecimal(new BigInteger(byContent));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void write(UniversalSerializer pSerializer, DataOutputStream pOut, BigDecimal pObject, TypeCache pCache) throws Exception
	{
		int iScale = pObject.scale();
		
		byte[] byContent = pObject.movePointRight(iScale).toBigInteger().toByteArray();
		
		if (iScale == 0)
		{
			writeSize(pOut, byContent.length, TYPE_BIGDECIMAL_NOSCALE_MIN, TYPE_BIGDECIMAL_NOSCALE_MAX);
			pOut.write(byContent);
		}
		else
		{
			writeSize(pOut, byContent.length, TYPE_BIGDECIMAL_MIN, TYPE_BIGDECIMAL_MAX);
			pOut.write(byContent);
			pOut.writeByte(iScale);
		}
	}
	
}	// DecimalSerializer
