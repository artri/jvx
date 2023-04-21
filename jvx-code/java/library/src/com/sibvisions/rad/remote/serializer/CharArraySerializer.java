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
 * The serializer for {@link Character} array.
 *  
 * @author Martin Handsteiner
 */
public class CharArraySerializer extends AbstractSizedSerializer 
                                 implements ITypeSerializer<char[]>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** min. byte value of <code>String</code> type. */
	private static final int TYPE_CHAR_ARRAY_MIN = 36;

	/** max. byte value of <code>String</code> type. */
	private static final int TYPE_CHAR_ARRAY_MAX = 38;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface Implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public Class<char[]> getTypeClass()
	{
		return char[].class;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getMinValue()
	{
		return TYPE_CHAR_ARRAY_MIN;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getMaxValue()
	{
		return TYPE_CHAR_ARRAY_MAX;
	}

	/**
	 * {@inheritDoc}
	 */
	public char[] read(UniversalSerializer pSerializer, DataInputStream pIn, int pTypeValue, TypeCache pCache) throws Exception
	{
		int size = readSize(pIn, pTypeValue, TYPE_CHAR_ARRAY_MIN, TYPE_CHAR_ARRAY_MAX);

		char[] array = new char[size];
		for (int i = 0; i < size; i++)
		{
			array[i] = pIn.readChar();
		}
		
		return array;
	}

	/**
	 * {@inheritDoc}
	 */
	public void write(UniversalSerializer pSerializer, DataOutputStream pOut, char[] pObject, TypeCache pCache) throws Exception
	{
		writeSize(pOut, pObject.length, TYPE_CHAR_ARRAY_MIN, TYPE_CHAR_ARRAY_MAX);
		
		for (int i = 0; i < pObject.length; i++)
		{
			pOut.writeChar(pObject[i]);
		}
	}
	
}	// CharArraySerializer
