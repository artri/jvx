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
 * 01.10.2008 - [RH] - creation
 * 02.11.2008 - [RH] - conversion of object to storage and back removed 
 */
package javax.rad.model.datatype;


import javax.rad.model.ModelException;

import org.junit.Assert;
import org.junit.Test;

import com.sibvisions.util.type.CodecUtil;

/**
 * Tests all Functions of {@link BinaryDataType} .<br>
 * 
 * @see com.sibvisions.rad.model.mem.DataTypees.BinaryDataType
 * 
 * @author Roland Hörmann
 */
public class TestBinaryDataType
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Test some base functions in the BinaryDataType.
	 * 
	 * @throws Exception
	 *             if not all BinaryDataType methods work correctly
	 */	
	@Test
	public void testBaseFunctions() throws Exception
	{
		// test new BinaryDataType
		BinaryDataType sctBinaryDataType = new BinaryDataType();
		sctBinaryDataType.setSize(10);
		byte[] baBinary = new byte [] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		Assert.assertEquals(baBinary, sctBinaryDataType.convertAndCheckToTypeClass(baBinary));
		Assert.assertEquals(null, sctBinaryDataType.convertAndCheckToTypeClass(null));
		byte [] baResultBinary = (byte[])sctBinaryDataType.convertAndCheckToTypeClass(CodecUtil.encodeBase64("lalala".getBytes()));

		Assert.assertTrue(baResultBinary[0] ==  'l');
		Assert.assertTrue(baResultBinary[1] ==  'a');
		Assert.assertTrue(baResultBinary[2] ==  'l');
		Assert.assertTrue(baResultBinary[3] ==  'a');
		Assert.assertTrue(baResultBinary[4] ==  'l');
		Assert.assertTrue(baResultBinary[5] ==  'a');
			
		// test to large
		byte[] baBinaryTooLarge = new byte [] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
		try
		{
			sctBinaryDataType.convertAndCheckToTypeClass(baBinaryTooLarge);

			// no Exception means its allowed it, but it isn't, VALUE TO LARGE
			Assert.assertTrue(false);
		}
		catch (ModelException modelException)
		{
			if (!modelException.getMessage().equals(
					"Binary too large! - length from " +
					baBinaryTooLarge.length + " to " + sctBinaryDataType.getSize()))
			{
				throw new Exception(modelException);
			}
		}
		
		byte[] oObject  = new byte[] { 'd', 'f', 'g' };

		// test clone 
		BinaryDataType bctClone = sctBinaryDataType.clone();
		Assert.assertEquals(sctBinaryDataType.getSize(), bctClone.getSize());
		bctClone.setSize(1234);
		Assert.assertTrue(sctBinaryDataType.getSize() != bctClone.getSize());

		// test compareTo
		Assert.assertEquals(sctBinaryDataType.compareTo(null, null), 0);
		Assert.assertEquals(sctBinaryDataType.compareTo(null, oObject), -1);
		Assert.assertEquals(sctBinaryDataType.compareTo(oObject, null), 1);
		Assert.assertEquals(sctBinaryDataType.compareTo(oObject, oObject), 0);		
	}
	
} 	// TestBinaryDataType
