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

import java.math.BigDecimal;

import javax.rad.model.ModelException;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests all Functions of {@link StringDataType} .<br>
 * 
 * @see com.sibvisions.rad.model.mem.DataTypees.StringDataType
 * 
 * @author Roland Hörmann
 */
public class TestStringDataType
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Test some base functions in the StringDataType.
	 * 
	 * @throws Exception
	 *             if not all StringDataType methods work correctly
	 */	
	@Test
	public void testBaseFunctions() throws Exception
	{
		// test new StringDataType
		StringDataType sctStringDataType = new StringDataType();
		sctStringDataType.setSize(10);
		Assert.assertEquals("123", sctStringDataType.convertAndCheckToTypeClass(new Long(123)));
		Assert.assertEquals(null, sctStringDataType.convertAndCheckToTypeClass(null));
		Assert.assertEquals(null, sctStringDataType.convertAndCheckToTypeClass(new byte[] {}));
		Assert.assertEquals("ab", sctStringDataType.convertAndCheckToTypeClass(new byte[] { 'a', 'b'}));
		Assert.assertEquals("123.456", sctStringDataType.convertAndCheckToTypeClass(new BigDecimal("123.456")));
		
		// test to large
		Long lLong = new Long(12345678901L);
		try
		{
			sctStringDataType.convertAndCheckToTypeClass(lLong);

			// no Exception means its allowed it, but it isn't, VALUE TO LARGE
			Assert.assertTrue(false);
		}
		catch (ModelException modelException)
		{
			if (!modelException.getMessage().equals(
					"String is too large! - length from " + 
					((String)sctStringDataType.convertToTypeClass(lLong)).length() + 
					" to " + sctStringDataType.getSize()))
			{
				throw new Exception(modelException);
			}
		}
				
		// test clone 
		StringDataType sctClone = sctStringDataType.clone();
		Assert.assertEquals(sctStringDataType.getSize(), sctClone.getSize());
		sctClone.setSize(1234);
		Assert.assertTrue(sctStringDataType.getSize() != sctClone.getSize());				
	}

	/**
	 * Tests autotrim of StringDataType.
	 * 
	 * @throws Exception
	 *             if not all StringDataType methods work correctly
	 */	
	@Test
	public void testAutoTrim() throws Exception
	{
		StringDataType sctStringDataType = new StringDataType();
		sctStringDataType.setAutoTrimEnd(true);

		Assert.assertEquals(" Hallo", sctStringDataType.convertAndCheckToTypeClass(" Hallo \n"));
		
		sctStringDataType.setAutoTrimEnd(false);

		Assert.assertEquals(" Hallo \n", sctStringDataType.convertAndCheckToTypeClass(" Hallo \n"));
	}
	
	
} 	// TestStringDataType
