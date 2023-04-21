/*
 * Copyright 2014 SIB Visions GmbH
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
 * 25.04.2014 - [JR] - creation
 */
package com.sibvisions.rad.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

import javax.rad.model.IDataBook;
import javax.rad.model.datatype.BigDecimalDataType;
import javax.rad.model.datatype.IDataType;
import javax.rad.model.datatype.StringDataType;
import javax.rad.model.datatype.TimestampDataType;

import org.junit.Assert;
import org.junit.Test;

import com.sibvisions.util.type.DateUtil;

/**
 * Tests the functionality of {@link DataBookBuilder}.
 * 
 * @author René Jahn
 */
public class TestDataBookBuilder
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Tests the given array (of 5 elements, with 2 duplicate values).
     * 
     * @param pArray the array
     * @param pDataType the expected datatype for the value column
     * @throws Exception if test fails
     */
    private void testBuildWithArray(Object[] pArray, int pDataType) throws Exception
    {
        if (pArray != null && pArray.length != 0 && pArray.length != 5)
        {
            Assert.fail("Source array must have 5 elements!");
        }
        
        //not unique
        
        IDataBook book = DataBookBuilder.build(pArray);
        
        Assert.assertEquals(pArray != null ? pArray.length : 0, book.getRowCount());
        Assert.assertEquals(BigDecimalDataType.TYPE_IDENTIFIER, book.getRowDefinition().getColumnDefinition("ID").getDataType().getTypeIdentifier());
        Assert.assertEquals(pDataType, book.getRowDefinition().getColumnDefinition("VALUE").getDataType().getTypeIdentifier());
        
        if (pArray != null && pArray.length > 0)
        {
            Assert.assertEquals(book.getDataRow(1).getValue("ID"), BigDecimal.valueOf(1));
        }
        
        //unique
        
        book = DataBookBuilder.build(pArray, true);
        
        IDataType dtValue = book.getRowDefinition().getColumnDefinition("VALUE").getDataType();

        Assert.assertEquals(BigDecimalDataType.TYPE_IDENTIFIER, book.getRowDefinition().getColumnDefinition("ID").getDataType().getTypeIdentifier());
        Assert.assertEquals(pDataType, dtValue.getTypeIdentifier());
        
        if (pArray != null && pArray.length > 0)
        {
            Assert.assertEquals(pArray != null ? 3 : 0, book.getRowCount());

            Assert.assertEquals(book.getDataRow(1).getValue("ID"), BigDecimal.valueOf(2));
            Assert.assertEquals(0, dtValue.compareTo(book.getDataRow(1).getValue("VALUE"), pArray[2]));
            Assert.assertEquals(book.getDataRow(2).getValue("ID"), BigDecimal.valueOf(4));
            Assert.assertEquals(0, dtValue.compareTo(book.getDataRow(2).getValue("VALUE"), pArray[4]));
        }
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Tests {@link DataBookBuilder#build(Object[])} with {@link String}s.
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testBuildWithStringArray() throws Exception
	{
	    String[] saValues = new String[] {"First", "First", "Second", "First", "Third"};
	    
	    testBuildWithArray(saValues, StringDataType.TYPE_IDENTIFIER);
	    
        testBuildWithArray(new String[0], StringDataType.TYPE_IDENTIFIER);
	    testBuildWithArray(null, StringDataType.TYPE_IDENTIFIER);
	}	

    /**
     * Tests {@link DataBookBuilder#build(Object[])} with {@link Integer}s.
     * 
     * @throws Exception if test fails
     */
    @Test
    public void testBuildWithIntegerArray() throws Exception
    {
        Integer[] iaValues = new Integer[] {Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(1), Integer.valueOf(3)};
        
        testBuildWithArray(iaValues, BigDecimalDataType.TYPE_IDENTIFIER);
    }   

    /**
     * Tests {@link DataBookBuilder#build(Object[])} with {@link Date}s.
     * 
     * @throws Exception if test fails
     */
    @Test
    public void testBuildWithDateArray() throws Exception
    {
        Date[] iaValues = new Date[] {DateUtil.getTimestamp(2010, 1, 1), DateUtil.getTimestamp(2010, 1, 1), DateUtil.getTimestamp(2010, 2, 1), 
                                      DateUtil.getTimestamp(2010, 1, 1), DateUtil.getTimestamp(2010, 3, 1)};
        
        testBuildWithArray(iaValues, TimestampDataType.TYPE_IDENTIFIER);
    }   
    
    /**
     * Tests {@link DataBookBuilder#build(Object[])} with {@link Integer}sand {@link String}s.
     * 
     * @throws Exception if test fails
     */
    @Test
    public void testBuildWithMixedArray() throws Exception
    {
        Object[] oaValues = new Object[] {"First", "First", Integer.valueOf(2), Integer.valueOf(1), Integer.valueOf(2)};
        
        IDataBook book = DataBookBuilder.build(oaValues);
        
        Assert.assertEquals(5, book.getRowCount());
        Assert.assertEquals(BigDecimalDataType.TYPE_IDENTIFIER, book.getRowDefinition().getColumnDefinition("ID").getDataType().getTypeIdentifier());
        Assert.assertEquals(StringDataType.TYPE_IDENTIFIER, book.getRowDefinition().getColumnDefinition("VALUE").getDataType().getTypeIdentifier());
        
        book = DataBookBuilder.build(oaValues, true);
        
        IDataType dtValue = book.getRowDefinition().getColumnDefinition("VALUE").getDataType();

        Assert.assertEquals(3, book.getRowCount());
        Assert.assertEquals(BigDecimalDataType.TYPE_IDENTIFIER, book.getRowDefinition().getColumnDefinition("ID").getDataType().getTypeIdentifier());
        Assert.assertEquals(StringDataType.TYPE_IDENTIFIER, dtValue.getTypeIdentifier());
        
        Assert.assertEquals(book.getDataRow(1).getValue("ID"), BigDecimal.valueOf(2));
        Assert.assertEquals(0, dtValue.compareTo(book.getDataRow(1).getValue("VALUE"), oaValues[2]));
        Assert.assertEquals(book.getDataRow(2).getValue("ID"), BigDecimal.valueOf(3));
        Assert.assertEquals(0, dtValue.compareTo(book.getDataRow(2).getValue("VALUE"), oaValues[3]));
    }   
    
    /**
     * Tests {@link DataBookBuilder#build(java.util.Map)}.
     * 
     * @throws Exception if test fails
     */
    @Test
    public void testBuildWithMap() throws Exception
    {
        HashMap<Integer, String> hmpMapping = new HashMap<Integer, String>();
        
        hmpMapping.put(Integer.valueOf(1), "First");
        hmpMapping.put(Integer.valueOf(2), "Second");
        hmpMapping.put(Integer.valueOf(3), "Third");
        
        IDataBook book = DataBookBuilder.build(hmpMapping);
        
        Assert.assertEquals(3, book.getRowCount());
        
        Assert.assertEquals(BigDecimalDataType.TYPE_IDENTIFIER, book.getRowDefinition().getColumnDefinition("ID").getDataType().getTypeIdentifier());
        Assert.assertEquals(StringDataType.TYPE_IDENTIFIER, book.getRowDefinition().getColumnDefinition("VALUE").getDataType().getTypeIdentifier());
    }
    
}	// TestDataBookBuilder
