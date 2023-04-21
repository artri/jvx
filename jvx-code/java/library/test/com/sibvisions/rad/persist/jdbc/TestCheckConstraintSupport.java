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
 * 09.10.2010 - [JR] - creation
 */
package com.sibvisions.rad.persist.jdbc;

import java.util.Hashtable;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * The <code>TestCheckConstraintParser</code> tests the {@link CheckConstraintSupport}.
 * 
 * @author René Jahn
 */
public class TestCheckConstraintSupport
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Test methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Tests {@link CheckConstraintSupport#parseCondition(String)}.
	 */
	@Test
	public void testParseCondition()
	{
		Hashtable<String, List<String>> htColumns = CheckConstraintSupport.parseCondition("ACTIVE='J' or ACTIVE = 'N'", false);
		
		Assert.assertEquals(1, htColumns.size());
		Assert.assertTrue("Column 'ACTIVE' not found!", htColumns.containsKey("ACTIVE"));
		Assert.assertEquals(2, htColumns.get("ACTIVE").size());
		Assert.assertTrue("Value 'J' not found!", htColumns.get("ACTIVE").contains("'J'"));
		Assert.assertTrue("Value 'N' not found!", htColumns.get("ACTIVE").contains("'N'"));
		
		
		htColumns = CheckConstraintSupport.parseCondition("break = 'N' AND (ACTIVE='J' or ACTIVE = 'N')", false);

		Assert.assertEquals(2, htColumns.size());
		Assert.assertTrue("Column 'ACTIVE' not found!", htColumns.containsKey("ACTIVE"));
		Assert.assertTrue("Column 'break' not found!", htColumns.containsKey("break"));
		Assert.assertEquals(2, htColumns.get("ACTIVE").size());
		Assert.assertTrue("Value 'J' not found!", htColumns.get("ACTIVE").contains("'J'"));
		Assert.assertTrue("Value 'N' not found!", htColumns.get("ACTIVE").contains("'N'"));
		Assert.assertEquals(1, htColumns.get("break").size());
		Assert.assertTrue("Value 'J' not found!", htColumns.get("ACTIVE").contains("'J'"));

		
		htColumns = CheckConstraintSupport.parseCondition("ACTIVE IN ('J', 'N') OR BREAK IN ('N')", false);
		
		Assert.assertEquals(2, htColumns.size());
		Assert.assertTrue("Column 'ACTIVE' not found!", htColumns.containsKey("ACTIVE"));
		Assert.assertTrue("Column 'BREAK' not found!", htColumns.containsKey("BREAK"));
		Assert.assertEquals(2, htColumns.get("ACTIVE").size());
		Assert.assertTrue("Value 'J' not found!", htColumns.get("ACTIVE").contains("'J'"));
		Assert.assertTrue("Value 'N' not found!", htColumns.get("ACTIVE").contains("'N'"));
		Assert.assertEquals(1, htColumns.get("BREAK").size());
		Assert.assertTrue("Value 'J' not found!", htColumns.get("ACTIVE").contains("'J'"));

		
		htColumns = CheckConstraintSupport.parseCondition("ACTIVE IN ('J', 'N')", false);

		Assert.assertEquals(1, htColumns.size());
		Assert.assertTrue("Column 'ACTIVE' not found!", htColumns.containsKey("ACTIVE"));
		Assert.assertEquals(2, htColumns.get("ACTIVE").size());
		Assert.assertTrue("Value 'J' not found!", htColumns.get("ACTIVE").contains("'J'"));
		Assert.assertTrue("Value 'N' not found!", htColumns.get("ACTIVE").contains("'N'"));

		
		htColumns = CheckConstraintSupport.parseCondition("ACTIVE_CHECK IN ('J', 'N')", false);
		
		Assert.assertEquals(1, htColumns.size());
		Assert.assertTrue("Column 'ACTIVE_CHECK' not found!", htColumns.containsKey("ACTIVE_CHECK"));
		Assert.assertEquals(2, htColumns.get("ACTIVE_CHECK").size());
		Assert.assertTrue("Value 'J' not found!", htColumns.get("ACTIVE_CHECK").contains("'J'"));
		Assert.assertTrue("Value 'N' not found!", htColumns.get("ACTIVE_CHECK").contains("'N'"));

		
		htColumns = CheckConstraintSupport.parseCondition("NR IN (1, 2) OR NR_2 = 12.5", false);
		
		Assert.assertEquals(2, htColumns.size());
		Assert.assertTrue("Column 'NR' not found!", htColumns.containsKey("NR"));
		Assert.assertTrue("Column 'NR_2' not found!", htColumns.containsKey("NR_2"));
		Assert.assertEquals(2, htColumns.get("NR").size());
		Assert.assertTrue("Value '1' not found!", htColumns.get("NR").contains("1"));
		Assert.assertTrue("Value '2' not found!", htColumns.get("NR").contains("2"));
		Assert.assertEquals(1, htColumns.get("NR_2").size());
		Assert.assertTrue("Value '12.5' not found!", htColumns.get("NR_2").contains("12.5"));
	}
	
	/**
	 * Tests {@link CheckConstraintSupport#parseCondition(String)} with predefined allowed values.
	 */
	@Test
	public void testParseConditionWithPredefinedValues()
	{
		Hashtable<String, List<String>> htColumns = CheckConstraintSupport.parseCondition("ACTIVE='J' or ACTIVE = 'N'", true);
		
		Assert.assertEquals(1, htColumns.size());
		Assert.assertTrue("Column 'ACTIVE' not found!", htColumns.containsKey("ACTIVE"));
		Assert.assertEquals(2, htColumns.get("ACTIVE").size());
		Assert.assertTrue("Value 'J' not found!", htColumns.get("ACTIVE").contains("'J'"));
		Assert.assertTrue("Value 'N' not found!", htColumns.get("ACTIVE").contains("'N'"));
		
		
		htColumns = CheckConstraintSupport.parseCondition("ACTIVE='A' or active = 'B' OR ACTIVE='N' or break = 'Y'", htColumns, true);
		
		Assert.assertEquals(2, htColumns.size());
		Assert.assertTrue("Column 'ACTIVE' not found!", htColumns.containsKey("ACTIVE"));
		Assert.assertTrue("Column 'BREAK' not found!", htColumns.containsKey("BREAK"));
		Assert.assertEquals(4, htColumns.get("ACTIVE").size());
		Assert.assertTrue("Value 'J' not found!", htColumns.get("ACTIVE").contains("'J'"));
		Assert.assertTrue("Value 'N' not found!", htColumns.get("ACTIVE").contains("'N'"));
		Assert.assertTrue("Value 'A' not found!", htColumns.get("ACTIVE").contains("'A'"));
		Assert.assertTrue("Value 'B' not found!", htColumns.get("ACTIVE").contains("'B'"));
		Assert.assertEquals(1, htColumns.get("BREAK").size());
		Assert.assertTrue("Value 'Y' not found!", htColumns.get("BREAK").contains("'Y'"));
	}	
	
	/**
	 * Tests {@link CheckConstraintSupport#translateValues(DBAccess, String, String, String, Hashtable)}.
	 */
	@Test
	public void testTranslateValueWithNull()
	{
		//NullPointerException check
		CheckConstraintSupport.translateValues(null, null, null);
	}
	
}	// TestCheckConstraintParser
