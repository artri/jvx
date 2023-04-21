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
 * 24.11.2020 - [HM] - creation
 */
package com.sibvisions.util.type;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests {@link NumberUtil} methods.
 * 
 * @author Martin Handsteiner
 * @see DateUtil
 */
public class TestNumberUtil
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Check all locales for currency.
	 *
	 * @throws Exception if the parsing went wrong.
	 */
	@Test
	public void testNumberFormat() throws Exception
	{
	    String[] exampleNumbers = new String[] {"1000000", "1000000.23", "1000.23"};
	    
	    NumberUtil numberUtil = new NumberUtil();
	    BigDecimal testNumber = null;
	    String numberStr = null;

        for (Locale locale : Locale.getAvailableLocales())
        {
            try
            {
                numberUtil.setNumberPattern(((DecimalFormat)DecimalFormat.getNumberInstance(locale)).toPattern(), locale);

                for (String exampleNumber : exampleNumbers)
                {
                    testNumber = new BigDecimal(exampleNumber);
                    
                    numberStr = numberUtil.format(testNumber);
                    Number number = numberUtil.parse(numberStr);

                    if (!number.equals(testNumber))
                    {
                        Assert.assertEquals(testNumber, number);
                    }
                }
            }
            catch (Exception e)
            {
                Assert.fail(e.getMessage() + ": " + locale.toString() + "  " + numberUtil.getNumberPattern() + "  " + numberStr + "  " + testNumber);
            }
        }
	}
	
	/**
	 * Tests swiss format.
	 */
	@Test
	public void testCh()
	{
		LocaleUtil.setDefault(new Locale("de", "CH"));
        char groupSep = new DecimalFormatSymbols(LocaleUtil.getDefault()).getGroupingSeparator();

		Assert.assertEquals("123" + groupSep + "456" + groupSep + "789.23", NumberUtil.format(new BigDecimal(123456789.23), "0,000.00"));			
	}
	
	/**
	 * The number use cases.
	 * @param numberUtil the numberUtil to parse
	 * @throws Exception if it fails.
	 */
	private void checkNumbers(NumberUtil numberUtil) throws Exception
	{
        Assert.assertEquals(new BigDecimal("1000000"), numberUtil.parse("1,000,000"));
        Assert.assertEquals(new BigDecimal("1000000"), numberUtil.parse("1.000.000"));
        Assert.assertEquals(new BigDecimal("1000000.23"), numberUtil.parse("1,000,000.23"));
        Assert.assertEquals(new BigDecimal("1000000.23"), numberUtil.parse("1.000.000,23"));
        Assert.assertEquals(new BigDecimal("1000.23"), numberUtil.parse("1000,23"));
        Assert.assertEquals(new BigDecimal("1000.23"), numberUtil.parse("1000.23"));
//        Assert.assertEquals(new BigDecimal("1000.23"), numberUtil.parse("€ 1000,23"));
//        Assert.assertEquals(new BigDecimal("1000.23"), numberUtil.parse("€ 1000.23"));
        Assert.assertEquals(new BigDecimal("-1000.23"), numberUtil.parse("-1000.23"));
        Assert.assertEquals(new BigDecimal("-1000.23"), numberUtil.parse("-1000,23"));
        Assert.assertEquals(new BigDecimal("1000.23"), numberUtil.parse("1 000.23"));
        Assert.assertEquals(new BigDecimal("1000.23"), numberUtil.parse("1 000,23"));
        Assert.assertEquals(new BigDecimal("1000.23"), numberUtil.parse("1,000.23"));
        Assert.assertEquals(new BigDecimal("1000.23"), numberUtil.parse("1.000,23"));
        Assert.assertEquals(new BigDecimal("1000.23"), numberUtil.parse("10,0023E2"));
        Assert.assertEquals(new BigDecimal("1000.23"), numberUtil.parse("10.0023E2"));
        Assert.assertEquals(new BigDecimal("1000.23"), numberUtil.parse("10002.3E-1"));
        Assert.assertEquals(new BigDecimal("1000.23"), numberUtil.parse("10002,3E-1"));
        Assert.assertEquals(new BigDecimal("1.232"), numberUtil.parse("123,2%"));
        Assert.assertEquals(new BigDecimal("1.232"), numberUtil.parse("123.2%"));
	}
	
	
	/**
     * Check all locales for currency.
     *
     * @throws Exception if the parsing went wrong.
     */
    @Test
    public void testSpecificNumberFormats() throws Exception
    {
        NumberUtil numberUtil = new NumberUtil();

        checkNumbers(numberUtil);
        
        numberUtil.setNumberPattern("€ #,##0.00");

      Assert.assertEquals(new BigDecimal("1000.23"), numberUtil.parse("€ 1000,23"));
      Assert.assertEquals(new BigDecimal("1000.23"), numberUtil.parse("€ 1000.23"));
        
        checkNumbers(numberUtil);

        numberUtil.setNumberPattern("#,##0.00", Locale.US);

        checkNumbers(numberUtil);
        
        numberUtil.setNumberPattern("#,##0.00%");
        
        checkNumbers(numberUtil);
        
    }

    /**
     * Check all locales for currency.
     *
     * @throws Exception if the parsing went wrong.
     */
    @Test
    public void testNumberConversion() throws Exception
    {
        try
        {
            NumberUtil numberUtil = new NumberUtil();
            numberUtil.setStrictFormatCheck(true);

            System.out.println(numberUtil.parse("Peter Road 12asdkjhasdk"));
            
            Assert.fail("Strings in numbers should not be valid!");
        }
        catch (Exception ex)
        {
            // Exception
        }
        
    }

    /**
     * Check all locales for currency.
     *
     * @throws Exception if the parsing went wrong.
     */
    @Test
    public void testNumberFormat2() throws Exception
    {
        NumberUtil numberUtil = new NumberUtil();

        numberUtil.setNumberPattern("#,##0.00'%'");
        
        System.out.println(numberUtil.format(new BigDecimal("0.04")));
        System.out.println(numberUtil.format(new BigDecimal("70.04")));
    }
    
}	// TestDateUtil
