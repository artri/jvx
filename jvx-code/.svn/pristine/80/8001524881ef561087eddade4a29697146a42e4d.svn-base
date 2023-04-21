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
 * 24.07.2009 - [HM] - creation
 */
package javax.rad.model.reference;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests all Functions of {@link ColumnMapping} .<br>
 * 
 * @see javax.rad.model.referenceColumnMapping
 * @author Martin Handsteiner
 */
public class TestColumnMapping
{
	
	/**
	 * Performance Test String equals, String intern equals and String intern ==.
	 * @param args the args
	 */
	public static void main(String[] args)
	{
		String str1 = "Hallo Martin";
		String str2 = new String(str1);
		
		@SuppressWarnings("unused")
		int c = 0;
		
		long time = System.currentTimeMillis();
		for (int i = 0; i < 500000000; i++)
		{
			if (str1.equals(str2))
			{
				c++;
			}
		}
		System.out.println((System.currentTimeMillis() - time));
		
		str2 = str2.intern();
		time = System.currentTimeMillis();
		for (int i = 0; i < 500000000; i++)
		{
			if (str1.equals(str2))
			{
				c++;
			}
		}
		System.out.println((System.currentTimeMillis() - time));

		time = System.currentTimeMillis();
		for (int i = 0; i < 500000000; i++)
		{
			if (str1 == str2 || str1.equals(str2))
			{
				c++;
			}
		}
		System.out.println((System.currentTimeMillis() - time));
}
	
	/**
	 * Test some base functions in the ColumnMapping.
	 * 
	 * @throws Exception
	 *             if not all RowDefinition methods work correctly
	 */	
	@Test
	public void testBaseFunctions() throws Exception
	{
		ColumnMapping mapping = new ColumnMapping();
		
		Assert.assertTrue(mapping.getColumnNames() != null);
		Assert.assertTrue(mapping.getReferencedColumnNames() != null);
		Assert.assertTrue(mapping.getReferencedColumnNames().length >= mapping.getColumnNames().length);

		mapping.setReferencedColumnNames(new String[] {"BDLD_ID"});
		
		Assert.assertTrue(mapping.getColumnNames() != null);
		Assert.assertTrue(mapping.getReferencedColumnNames() != null);
		Assert.assertTrue(mapping.getReferencedColumnNames().length >= mapping.getColumnNames().length);

		mapping.setColumnNames(new String[] {"ID", "BUNDESLAND"});
		
		Assert.assertTrue(mapping.getColumnNames() != null);
		Assert.assertTrue(mapping.getReferencedColumnNames() != null);
		Assert.assertTrue(mapping.getReferencedColumnNames().length >= mapping.getColumnNames().length);
		Assert.assertTrue(mapping.getReferencedColumnNames()[1].equals("BUNDESLAND"));
		
		mapping = new ColumnMapping(new String[] {"ID"}, null);
		
		Assert.assertTrue(mapping.getColumnNames() != null);
		Assert.assertTrue(mapping.getReferencedColumnNames() != null);
		Assert.assertTrue(mapping.getReferencedColumnNames().length >= mapping.getColumnNames().length);
		
		mapping = new ColumnMapping(null, new String[] {"BDLD_ID"});
		
		Assert.assertTrue(mapping.getColumnNames() != null);
		Assert.assertTrue(mapping.getReferencedColumnNames() != null);
		Assert.assertTrue(mapping.getReferencedColumnNames().length >= mapping.getColumnNames().length);
	}

}
