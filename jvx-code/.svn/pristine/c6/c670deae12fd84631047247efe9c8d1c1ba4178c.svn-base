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
 * 23.02.2013 - [JR] - creation
 */
package com.sibvisions.rad.model;

import java.math.BigDecimal;
import java.util.Map;

import javax.rad.model.ColumnDefinition;
import javax.rad.model.IDataRow;
import javax.rad.model.condition.And;
import javax.rad.model.condition.ContainsIgnoreCase;
import javax.rad.model.condition.Equals;
import javax.rad.model.condition.ICondition;
import javax.rad.model.condition.LessEquals;
import javax.rad.model.condition.Like;
import javax.rad.model.condition.Not;
import javax.rad.model.datatype.BigDecimalDataType;

import org.junit.Assert;
import org.junit.Test;

import com.sibvisions.rad.model.mem.DataRow;

/**
 * Tests the functionality of {@link Filter}.
 * 
 * @author René Jahn
 */
public class TestFilter
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Tests {@link Filter#createCondition(ICondition, String...)}.
	 */
	@Test
	public void testCreateCondition()
	{
		ICondition cond = new Equals("ID", BigDecimal.valueOf(1)).and(new Not(new Like("NAME", "name").and(new LessEquals("TEXT", "text")))).or(
		                    new Equals("FOLDER_ID", new BigDecimal("1.1")));
		
		Assert.assertEquals("(ID = 1 AND NOT (NAME LIKE 'name' AND TEXT <= 'text')) OR FOLDER_ID = 1.1", cond.toString());
		Assert.assertEquals("NOT (NAME LIKE 'name' AND TEXT <= 'text') OR FOLDER_ID = 1.1", Filter.createCondition(cond, false, "ID").toString());
		Assert.assertEquals("ID = 1 OR FOLDER_ID = 1.1", Filter.createCondition(cond, false, "NAME", "TEXT").toString());
		Assert.assertEquals("(ID = 1 AND NOT TEXT <= 'text') OR FOLDER_ID = 1.1", Filter.createCondition(cond, false, "NAME").toString());

		Assert.assertEquals("ID = 1", Filter.createCondition(cond, true, "ID").toString());
	}
	
	/**
	 * Tests {@link Filter#getEqualsValues(ICondition)} and {@link Filter#getEqualsValues(com.sibvisions.rad.model.mem.DataRow, ICondition)}.
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testGetEqualValues() throws Exception
	{
		ICondition cond = new Equals("ID", BigDecimal.valueOf(1)).and(
				          new Not(new Equals("NAME", "name").or(new LessEquals("TEXT", "text")))).or(
                                  new Equals("FOLDER_ID", new BigDecimal("1.1")).and(new Like("KEY", "fixed")).or(
                                		  new And(new ContainsIgnoreCase("CAR", "rep"), new Equals("ID", BigDecimal.valueOf(2)), new Like("HNR", Long.valueOf(100)))));
		
		Map<String, Object> mpResult = Filter.getEqualsValues(cond);
		
		Assert.assertEquals(2, mpResult.size());
		Assert.assertEquals(BigDecimal.valueOf(2), mpResult.get("ID"));
		Assert.assertEquals(BigDecimal.valueOf(1.1), mpResult.get("FOLDER_ID"));
		//NOT equals
		Assert.assertNull(mpResult.get("NAME"));
		
		IDataRow row = new DataRow();
		row.getRowDefinition().addColumnDefinition(new ColumnDefinition("FOLDER_ID", new BigDecimalDataType()));
		
		row = Filter.getEqualsValues(row, cond);
		
		Assert.assertEquals(BigDecimal.valueOf(1.1), row.getValue("FOLDER_ID"));
		
		row = Filter.getEqualsValues(null, cond);

		Assert.assertEquals(BigDecimal.valueOf(2), row.getValue("ID"));
		Assert.assertEquals(BigDecimal.valueOf(1.1), row.getValue("FOLDER_ID"));
	}
	
}	// TestFilter
