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
import java.util.Arrays;

import com.sibvisions.rad.remote.UniversalSerializer;

/**
 * The serializer for {@link String}.
 *  
 * @author Martin Handsteiner
 */
public class StringSerializer extends AbstractSizedSerializer implements ITypeSerializer<String>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** min. byte value of <code>String</code> type. */
	private static final int TYPE_STRING_MIN = 99;

	/** max. byte value of <code>String</code> type. */
	private static final int TYPE_STRING_MAX = 255;

	/** Caches the timestamp. */
	private static String[] stringCache = new String[256];

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface Implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public Class<String> getTypeClass()
	{
		return String.class;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getMinValue()
	{
		return TYPE_STRING_MIN;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getMaxValue()
	{
		return TYPE_STRING_MAX;
	}

	/**
	 * {@inheritDoc}
	 */
	public String read(UniversalSerializer pSerializer, DataInputStream pIn, int pTypeValue, TypeCache pCache) throws Exception
	{
		int size = readSize(pIn, pTypeValue, TYPE_STRING_MIN, TYPE_STRING_MAX);

		byte[] byContent = new byte[size];
		pIn.readFully(byContent);
		
		if (byContent.length < 64)
		{
	        int index = Arrays.hashCode(byContent) & 0xFF;
			
	        String result = stringCache[index];
			
			if (result == null || !Arrays.equals(byContent, result.getBytes("UTF8")))
			{
				result = new String(byContent, "UTF8");
				
				stringCache[index] = result;
			}

			return result;
		}
		
		return new String(byContent, "UTF8");
	}

	/**
	 * {@inheritDoc}
	 */
	public void write(UniversalSerializer pSerializer, DataOutputStream pOut, String pObject, TypeCache pCache) throws Exception
	{
		byte[] byContent = ((String)pObject).getBytes("UTF8");
		
		writeSize(pOut, byContent.length, TYPE_STRING_MIN, TYPE_STRING_MAX);
		pOut.write(byContent);
	}
	
}	// StringSerializer
