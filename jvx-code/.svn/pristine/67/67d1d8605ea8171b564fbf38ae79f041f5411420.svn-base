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

import org.junit.Assert;
import org.junit.Test;


/**
 * Tests all Functions of {@link BooleanDataType} .<br>
 * 
 * @see com.sibvisions.rad.model.mem.DataTypees.BooleanDataType
 * 
 * @author Roland Hörmann
 */
public class TestBooleanDataType
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Test some base functions in the BooleanDataType.
	 * 
	 * @throws Exception
	 *             if not all BooleanDataType methods work correctly
	 */	
	@Test
	public void testBaseFunctions() throws Exception
	{
		// test new BooleanDataType
		BooleanDataType sctBooleanDataType = new BooleanDataType();
		Assert.assertEquals(Boolean.TRUE, sctBooleanDataType.convertAndCheckToTypeClass(new Long(123)));
		Assert.assertEquals(null, sctBooleanDataType.convertAndCheckToTypeClass(null));
		Assert.assertEquals(Boolean.FALSE, sctBooleanDataType.convertAndCheckToTypeClass("0"));
		Assert.assertEquals(Boolean.TRUE, sctBooleanDataType.convertAndCheckToTypeClass(new Boolean(true)));
				
		// test clone 
		BooleanDataType bctClone = sctBooleanDataType.clone();
		Assert.assertEquals(sctBooleanDataType.getSize(), bctClone.getSize());
	}
	
} 	// TestBooleanDataType
