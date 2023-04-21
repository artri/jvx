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
import java.math.BigInteger;
import java.util.Date;

import javax.rad.model.ModelException;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests all Functions of {@link BigDecimalDataType} .<br>
 * 
 * @see com.sibvisions.rad.model.mem.DataTypees.BigDecimalDataType
 * 
 * @author Roland Hörmann
 */
public class TestBigDecimalDataType
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
		// init BigDecimalDataType
		BigDecimalDataType sctBigDecimalDataType = new BigDecimalDataType();
		sctBigDecimalDataType.setPrecision(13);
		sctBigDecimalDataType.setScale(0);
		sctBigDecimalDataType.setSigned(false);
		
		// test with no digits after point with all supported types
		Assert.assertEquals(new BigDecimal("123"), 
							sctBigDecimalDataType.convertAndCheckToTypeClass(Long.valueOf(123)));
		Assert.assertEquals(new BigDecimal("1234567"), 
							sctBigDecimalDataType.convertAndCheckToTypeClass(new BigInteger("1234567")));
		Assert.assertEquals(null, sctBigDecimalDataType.convertAndCheckToTypeClass(null));
		Date dNow = new Date();
		Assert.assertEquals(BigDecimal.valueOf(dNow.getTime()), 
							sctBigDecimalDataType.convertAndCheckToTypeClass(dNow));
		Assert.assertEquals(new BigDecimal("1234567890123"), 
				sctBigDecimalDataType.convertAndCheckToTypeClass("1234567890123"));
		
		// test to large after the point		
		String sString = "12345678901.1";
		try
		{
			sctBigDecimalDataType.convertAndCheckToTypeClass(sString);

			// no Exception means its allowed it, but it isn't, VALUE TO LARGE
			Assert.assertTrue(false);
		}
		catch (ModelException modelException)
		{
			if (!modelException.getMessage().equals(
					"BigDecimal too large! - (precision/scale) from (" + 
					(sString.length() - 1) + "/" + 1 + ") to (" + 
					sctBigDecimalDataType.getPrecision() + "/" + 
					sctBigDecimalDataType.getScale() + ")"))
			{
				throw new Exception(modelException);
			}
		}
		
		// test to large before point
		sString = "12345678901234.0";
		try
		{
			sctBigDecimalDataType.convertAndCheckToTypeClass(sString);

			// no Exception means its allowed it, but it isn't, VALUE TO LARGE
			Assert.assertTrue(false);
		}
		catch (ModelException modelException)
		{
			if (!modelException.getMessage().equals(
					"BigDecimal too large! - (precision/scale) from (" + 
					(sString.length() - 1) + "/" + 1 + ") to (" + 
					sctBigDecimalDataType.getPrecision() + "/" + 
					sctBigDecimalDataType.getScale() + ")"))
			{
				throw new Exception(modelException);
			}
		}		

		
		// test with digits after point
		sctBigDecimalDataType.setScale(3);		
		Assert.assertEquals(new BigDecimal("1234567890.891"), 
							sctBigDecimalDataType.convertAndCheckToTypeClass("1234567890.891"));
		
		// test to large after the point
		sString = "1234567890.8905";
		try
		{
			sctBigDecimalDataType.convertAndCheckToTypeClass(sString);

			// no Exception means its allowed it, but it isn't, VALUE TO LARGE
			Assert.assertTrue(false);
		}
		catch (ModelException modelException)
		{
			if (!modelException.getMessage().equals(
					"BigDecimal too large! - (precision/scale) from (" + 
					(sString.length() - 1) + "/" + 4 + ") to (" + 
					sctBigDecimalDataType.getPrecision() + "/" + 
					sctBigDecimalDataType.getScale() + ")"))
			{
				throw new Exception(modelException);
			}
		}

		// test to large before point
		sString = "12345678901.891";
		try
		{
			sctBigDecimalDataType.convertAndCheckToTypeClass(sString);

			// no Exception means its allowed it, but it isn't, VALUE TO LARGE
			Assert.assertTrue(false);
		}
		catch (ModelException modelException)
		{
			if (!modelException.getMessage().equals(
					"BigDecimal too large! - (precision/scale) from (" + 
					(sString.length() - 1) + "/" + 3 + ") to (" + 
					sctBigDecimalDataType.getPrecision() + "/" + 
					sctBigDecimalDataType.getScale() + ")"))
			{
				throw new Exception(modelException);
			}
		}		

		// test clone 
		BigDecimalDataType bdctClone = sctBigDecimalDataType.clone();
		Assert.assertEquals(sctBigDecimalDataType.getSize(), bdctClone.getSize());
		bdctClone.setPrecision(15);
		Assert.assertTrue(sctBigDecimalDataType.getSize() != bdctClone.getSize());
	}

	/**
	 * Test Bug 301.
	 * 
	 * @throws Exception
	 *             if not all StringDataType methods work correctly
	 */	
	@Test
	public void testBug301() throws Exception
	{
		BigDecimalDataType bdType = new BigDecimalDataType();
		bdType.setPrecision(4);
		bdType.setScale(2);
		
		try
		{
			bdType.convertAndCheckToTypeClass(new BigDecimal("1234"));
			
			// no Exception means its allowed, but it shouldn't, VALUE TO LARGE
			Assert.assertTrue(false);
		}
		catch (ModelException ex)
		{
			// Failure is correct.
		}
		
		bdType.convertAndCheckToTypeClass(new BigDecimal("12.34"));
		
	}
	
	/**
	 * Test Bug 301.
	 * 
	 * @throws Exception
	 *             if not all StringDataType methods work correctly
	 */	
	@Test
	public void testBigDecimal() throws Exception
	{
		BigDecimalDataType bdType = new BigDecimalDataType();
		
		BigDecimal decimal = new BigDecimal("1.232372E9");
		
		Assert.assertFalse(bdType.convertAndCheckToTypeClass(decimal).toString().contains("E"));
	}
	
    /**
     * Test Bug 301.
     * 
     * @throws Exception
     *             if not all StringDataType methods work correctly
     */ 
    @Test
    public void testPrecisionEqualsScale() throws Exception
    {
        BigDecimalDataType bdType = new BigDecimalDataType();
        bdType.setPrecision(2);
        bdType.setScale(2);
        
        bdType.convertAndCheckToTypeClass("0,12");
        bdType.convertAndCheckToTypeClass("0,1");
        bdType.convertAndCheckToTypeClass("0,");
        bdType.convertAndCheckToTypeClass("0");
    }
    
} 	// TestBigDecimalDataType
