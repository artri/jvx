/*
 * Copyright 2013 SIB Visions GmbH
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
 * 05.03.2013 - [JR] - creation
 */
package com.sibvisions.util.type;

/**
 * The <code>ByteUtil</code> contains methods for byte(array) operations and manipulations.
 * 
 * @author René Jahn
 */
public final class ByteUtil
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Invisible constructor because <code>ByteUtil</code> is a utility
	 * class.
	 */
	private ByteUtil()
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the integer value of a byte array. This method assumes little endian byte order.
	 * 
	 * @param pValue the byte array
	 * @param pStart the start position
	 * @return the integer value
	 */
    public static int toIntLittleEndian(byte[] pValue, int pStart)
    {
        int iResult = 0;


        for (int i = pStart; i < pStart + 4; i++)
        {
            iResult |= (pValue[i] & 255) << (i * 8);
        }

        return iResult;
    }

	/**
	 * Gets the integer value of a byte array. This method assumes big endian byte order.
	 * 
	 * @param pValue the byte array
	 * @param pStart the start position
	 * @return the integer value
	 */
	public static int toInt(byte[] pValue, int pStart)
	{
		int iResult = 0;

		//Convert exactly 4 Byte to an INT
		//If there are not enough bytes, an Exception will be thrown

		//& 255 is necessary, to use the bits and not the value!!!
		for (int i = 0, j = 3; i < 4; i++, j--) 
		{ 
			iResult |= ((pValue[pStart + i] & 255) << (j * 8)); 
		}

		return iResult;
	}

	/**
	 * Gets the bytes of an integer value (little endian).
	 * 
	 * @param pValue the value
	 * @return the byte array (size = 4)
	 */
	public static byte[] toByteLittleEndian(int pValue)
	{
		byte[] byResult = new byte[4];

		byResult[0] = (byte)(pValue & 255);
		byResult[1] = (byte)((pValue >> 8) & 255);
		byResult[2] = (byte)((pValue >> 16) & 255);
		byResult[3] = (byte)((pValue >> 24) & 255);

		return byResult;
	}
	
	/**
	 * Gets the bytes of an integer value (big endian).
	 * 
	 * @param pValue the value
	 * @return the byte array (size = 4)
	 */
	public static byte[] toByte(int pValue)
	{
		byte[] byResult = new byte[4];

		byResult[3] = (byte)(pValue & 255);
		byResult[2] = (byte)((pValue >> 8) & 255);
		byResult[1] = (byte)((pValue >> 16) & 255);
		byResult[0] = (byte)((pValue >> 24) & 255);

		return byResult;
	}

}	// ByteUtil
