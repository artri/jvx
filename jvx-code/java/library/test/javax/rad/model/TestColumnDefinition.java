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
 */
package javax.rad.model;


import org.junit.Assert;
import org.junit.Test;


/**
 * Tests all Functions of {@link ColumnDefinition} .<br>
 * 
 * @see javax.rad.model.ColumnDefinition
 * @author Roland Hörmann
 */
public class TestColumnDefinition
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Test some base functions in the ColumnDefinition.
	 * 
	 * @throws Exception
	 *             if not all ColumnDefinition methods work correctly
	 */	
	@Test
	public void testBaseFunctions() throws Exception
	{
		// test new ColumnDefinition
		ColumnDefinition cdName = new ColumnDefinition("name");
		Assert.assertEquals("name", cdName.getName());

		// test properties
		cdName.setWidth(10);
		Assert.assertEquals(10, cdName.getWidth());
		cdName.setLabel("Name");
		Assert.assertEquals("Name", cdName.getLabel());
		cdName.setNullable(true);
		Assert.assertEquals(Boolean.TRUE, Boolean.valueOf(cdName.isNullable()));
		cdName.setReadOnly(true);
		Assert.assertEquals(Boolean.TRUE, Boolean.valueOf(cdName.isReadOnly()));
		cdName.setWritable(false);
		Assert.assertEquals(Boolean.FALSE, Boolean.valueOf(cdName.isWritable()));
		System.out.println(cdName);
		
		// check setRowDefinition();
		RowDefinition rdRowDefinition = new RowDefinition();
		cdName.setRowDefinition(rdRowDefinition);
		Assert.assertEquals(rdRowDefinition, cdName.getRowDefinition());
		
		RowDefinition rdNew = new RowDefinition();
		try 
		{
			cdName.setRowDefinition(rdNew);
			// no Exception means its allowed to change it, but it isn't
			Assert.assertTrue(false);
		}
		catch (ModelException modelException)
		{
			if (!modelException.getMessage().equals(
					"ColumnDefinition already added to an RowDefinition! - " + 
							cdName.getRowDefinition().toString()))
			{
				throw new Exception(modelException);
			}
		}

		try 
		{
			cdName.setName("lala");
			// no Exception means its allowed to change it, but it isn't
			Assert.assertTrue(false);
		}
		catch (ModelException modelException)
		{
			if (!modelException.getMessage().equals(
					"ColumnDefinition already added to an RowDefinition! - " + 
					cdName.getRowDefinition().toString()))
			{
				throw new Exception(modelException);
			}
		}		
		
		// test clone()
		ColumnDefinition cdNameClone = cdName.clone();
		Assert.assertEquals("name", cdNameClone.getName());
		System.out.println(cdNameClone);		
	}
	
} 	// TestColumnDefinition
