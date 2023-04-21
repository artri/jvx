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
 * 21.03.2013 - [JR] - creation
 */
package com.sibvisions.util.type;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests {@link ByteUtil} methods.
 * 
 * @author René Jahn
 * @see ByteUtil
 */
public class TestByteUtil
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Tests {@link ByteUtil#toByte(int)}.
	 */
	@Test
	public void testToByte()
	{
		Assert.assertEquals(Integer.MAX_VALUE, ByteUtil.toInt(ByteUtil.toByte(Integer.MAX_VALUE), 0));
	}
	
	/**
	 * Tests {@link ByteUtil#toByteLittleEndian(int)}.
	 */
	@Test
	public void testToByteLitleEndian()
	{
		Assert.assertEquals(Integer.MAX_VALUE, ByteUtil.toIntLittleEndian(ByteUtil.toByteLittleEndian(Integer.MAX_VALUE), 0));
	}
	
}	// TestByteUtil
