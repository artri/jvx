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

/**
 * The base serializer for objects with size/length.
 *  
 * @author Martin Handsteiner
 */
public abstract class AbstractSizedSerializer
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Reads the content length based on the type identifier.
	 * 
	 * @param pIn input stream
	 * @param pTypeValue type definition with included content length
	 * @param pMin start value for the type definition
	 * @param pMax max value for the type definition
	 * @return content length
	 * @throws Exception if the content length cannot be calculated
	 */
	public int readSize(DataInputStream pIn, int pTypeValue, int pMin, int pMax) throws Exception
	{
		int diff = pMax - pMin + 1;
		
		if (pTypeValue < pMin + diff - 3)
		{
			return pTypeValue - pMin;
		}
		else if (pTypeValue == pMin + diff - 3)
		{
			return pIn.readUnsignedByte() + diff - 3;
		}
		else if (pTypeValue == pMin + diff - 2)
		{
			return pIn.readUnsignedShort() + 253 + diff;
		}
		else
		{
			return pIn.readInt();
		}
	}

	/**
	 * Writes the type definition which includes the length of the content.
	 * 
	 * @param pOut output stream
	 * @param pSize length of the content
	 * @param pMin start value for the type definition
	 * @param pMax max value for the type definition
	 * @throws Exception if it is not possible to write the calculated type definition to the stream
	 */
	public void writeSize(DataOutputStream pOut, int pSize, int pMin, int pMax) throws Exception
	{
		int diff = pMax - pMin + 1;
		
		if (pSize < diff - 3)
		{
			pOut.writeByte(pMin + pSize);
		}
		else if (pSize < 253 + diff)
		{
			pOut.writeByte(pMin + diff - 3);
			pOut.writeByte(pSize - diff + 3);
		}
		else if (pSize < 65789 + diff)
		{
			pOut.writeByte(pMin + diff - 2);
			pOut.writeShort(pSize - 253 - diff);
		}
		else
		{
			pOut.writeByte(pMin + diff - 1);
			pOut.writeInt(pSize);
		}
	}
	
}	// AbstractSizedSerializer
