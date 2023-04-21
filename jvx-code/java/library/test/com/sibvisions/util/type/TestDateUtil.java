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
 * 21.03.2013 - [RH] - creation
 */
package com.sibvisions.util.type;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Assert;
import org.junit.Test;

import com.sibvisions.util.ImmutableTimestamp;

/**
 * Tests {@link DateUtil} methods.
 * 
 * @author Roland Hörmann
 * @see DateUtil
 */
public class TestDateUtil
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Assert that date input with and without weekday is supported (ticket #1792).
	 *
	 * @throws Exception if the parsing went wrong.
	 */
	@Test
	public void testDateWithWeekday() throws Exception
	{
		Calendar cal = Calendar.getInstance();
		cal.set(2017, Calendar.MAY, 15, 0, 0, 0);
		cal.clear(Calendar.MILLISECOND);
		Date expected = cal.getTime();
		
		DateUtil dateUtil = new DateUtil("E, dd.MM.yyyy");		
		// correct day of week
		try
		{
		    Assert.assertEquals(expected, dateUtil.parse("Mont, 15.Mai.2017"));
		}
		catch (ParseException pe)
		{
		    Assert.assertEquals(expected, dateUtil.parse("Mont, 15.May.2017"));
		}
		
		Assert.assertEquals(expected, dateUtil.parse("Mo, 15.05.2017"));
		// correct day of week
		Assert.assertEquals(expected, dateUtil.parse("Montag, 15.05.2017"));
		// wrong day of week (could happen during manual entering the date)
		Assert.assertEquals(expected, dateUtil.parse("Fr, 15.05.2017"));
		// no valid day of week
		Assert.assertEquals(expected, dateUtil.parse("15.05.2017"));
		
		dateUtil = new DateUtil("EEEE, dd.MM.yyyy");
		Assert.assertEquals(expected, dateUtil.parse("Mo, 15.05.2017"));
		Assert.assertEquals(expected, dateUtil.parse("Montag, 15.05.2017"));
		Assert.assertEquals(expected, dateUtil.parse("F, 15.05.2017"));
		Assert.assertEquals(expected, dateUtil.parse("15.05.2017"));
		Assert.assertEquals(expected, dateUtil.parse("15.05.17"));
		Assert.assertEquals(expected, dateUtil.parse("150517"));
		Assert.assertEquals(expected, dateUtil.parse("15052017"));
	}
	
	/**
	 * Assert that date input with and without weekday is supported (ticket #1792).
	 *
	 * @throws Exception if the parsing went wrong.
	 */
	@Test
	public void testDateWithCalendarWeeks() throws Exception
	{
		Calendar cal = Calendar.getInstance();
		cal.set(2017, 0, 02, 0, 0, 0);
		cal.clear(Calendar.MILLISECOND);
		Date expected = cal.getTime();
			
		DateUtil dateUtil = new DateUtil("yyyy/ww E");		
		Assert.assertEquals(expected, dateUtil.parse("2017/01 Mo"));
		Assert.assertEquals(expected, dateUtil.parse("17/01 Mo"));
		Assert.assertEquals(expected, dateUtil.parse("17/1 Mo"));
		Assert.assertEquals(expected, dateUtil.parse("2017/01"));	// Kann Montag kann als default angenommen werden?		
		
		dateUtil = new DateUtil("yyyy/w EEEE");		
		Assert.assertEquals(expected, dateUtil.parse("2017/01 Montag"));
		Assert.assertEquals(expected, dateUtil.parse("17/01 Montag"));
		Assert.assertEquals(expected, dateUtil.parse("17/1 Montag"));
		Assert.assertEquals(expected, dateUtil.parse("2017/1"));
		
		dateUtil = new DateUtil("yy/w EEEE");		
		Assert.assertEquals(expected, dateUtil.parse("2017/01 Montag"));
		Assert.assertEquals(expected, dateUtil.parse("17/01 Montag"));
		Assert.assertEquals(expected, dateUtil.parse("17/1 Montag"));
		Assert.assertEquals(expected, dateUtil.parse("17/1"));

		dateUtil = new DateUtil("'KW'w E, yy");
		Assert.assertEquals(expected, dateUtil.parse("KW01 Mon, 17"));
		Assert.assertEquals(expected, dateUtil.parse("KW1 2017"));
		Assert.assertEquals(expected, dateUtil.parse("KW1 17"));
	}
	
	/**
	 * Assert that date input with and without ISO-8601 Timezone is supported.
	 *
	 * @throws Exception if the parsing went wrong.
	 */
	@Test
	public void testTimeWithISO8601Timezone() throws Exception
	{
        TimeZone defaultTimeZone = TimeZone.getDefault();
        Locale defaultLocale = Locale.getDefault();
        
        Locale.setDefault(Locale.ENGLISH);
        TimeZone.setDefault(TimeZone.getTimeZone("CET"));

        try
        {
    		Calendar cal = Calendar.getInstance();
    		cal.set(Calendar.HOUR_OF_DAY, 16);
    		cal.set(Calendar.MINUTE, 17);
    		cal.set(Calendar.SECOND, 18);
    		cal.set(Calendar.MILLISECOND, 0);
    		cal.set(Calendar.MONTH, 0);
    		cal.set(Calendar.YEAR, 1970);
    		cal.set(Calendar.DAY_OF_MONTH, 1);
    		ImmutableTimestamp expected = new ImmutableTimestamp(cal.getTimeInMillis());
    
    		DateUtil dateUtil = new DateUtil("yyyy-MM-dd'T'HH:mm:ssz");
    		// This testcase depends on the current timezone. So it is commented out, to have consistent results.
    //        Assert.assertEquals(expected, dateUtil.parse("1970-01-01T17:17:18")); // for CEST
    //        Assert.assertEquals(expected, dateUtil.parse("1970-01-01T171718"));   // for CEST
    //        Assert.assertEquals(expected, dateUtil.parse("1970-01-01T16:17:18")); // for CET
    //        Assert.assertEquals(expected, dateUtil.parse("1970-01-01T161718"));   // for CET
            Assert.assertEquals(expected, dateUtil.parse("1970-01-01T16:17:18CET"));
            Assert.assertEquals(expected, dateUtil.parse("1970-01-01T15:17:18z"));
            Assert.assertEquals(expected, dateUtil.parse("1970-01-01T15:17:18Z"));
            Assert.assertEquals(expected, dateUtil.parse("1970-01-01T17:17:18+0200"));
            Assert.assertEquals(expected, dateUtil.parse("1970-01-01T17:17:18+02:00"));
            Assert.assertEquals(expected, dateUtil.parse("1970-01-01T17:17:18+02"));
            Assert.assertEquals(expected, dateUtil.parse("1970-01-01T12:17:18-03"));
    
    		dateUtil = new DateUtil("HH:mm:ss z");	
    		Assert.assertEquals(expected, dateUtil.parse("17:17:18 +0200"));		
    		Assert.assertEquals(expected, dateUtil.parse("17:17:18 +02:00"));		
    		Assert.assertEquals(expected, dateUtil.parse("17:17:18 +02"));		
    		Assert.assertEquals(expected, dateUtil.parse("12:17:18 -03"));		
    		Assert.assertEquals(expected, dateUtil.parse("15:17:18 Z"));		
    		Assert.assertEquals(expected, dateUtil.parse("16:17:18"));
    		
    		dateUtil = new DateUtil("HH:mm:ss zz");
    		Assert.assertEquals(expected, dateUtil.parse("17:17:18 +0200"));		
    		Assert.assertEquals(expected, dateUtil.parse("17:17:18 +02:00"));		
    		Assert.assertEquals(expected, dateUtil.parse("17:17:18 +02"));		
    		Assert.assertEquals(expected, dateUtil.parse("12:17:18 -0300"));		
    		Assert.assertEquals(expected, dateUtil.parse("15:17:18 Z"));		
    		Assert.assertEquals(expected, dateUtil.parse("16:17:18"));
    
    		dateUtil = new DateUtil("HH:mm:ss zzz");
    		Assert.assertEquals(expected, dateUtil.parse("17:17:18 +0200"));		
    		Assert.assertEquals(expected, dateUtil.parse("17:17:18 +02:00"));		
    		Assert.assertEquals(expected, dateUtil.parse("17:17:18 +02"));		
    		Assert.assertEquals(expected, dateUtil.parse("12:17:18 -03:00"));		
    		Assert.assertEquals(expected, dateUtil.parse("15:17:18 Z"));		
    		Assert.assertEquals(expected, dateUtil.parse("16:17:18"));
    
    		dateUtil = new DateUtil("HH:mm:ss zzz");
    		Assert.assertEquals(expected, dateUtil.parse("17:17:18 GMT+02:00"));
    //		Assert.assertEquals(expected, dateUtil.parse("16:17:18 MEZ")); // Will only work in german
    		Assert.assertEquals(expected, dateUtil.parse("15:17:18 GMT"));
    		Assert.assertEquals(expected, dateUtil.parse("16:17:18"));
    
    		dateUtil = new DateUtil("zzz HH:mm:ss");
            Assert.assertEquals(expected, dateUtil.parse("GMT+02:00 17:17:18"));
    //        Assert.assertEquals(expected, dateUtil.parse("MEZ 16:17:18")); // Will only work in german
            Assert.assertEquals(expected, dateUtil.parse("GMT 15:17:18"));
            Assert.assertEquals(expected, dateUtil.parse("16:17:18"));
        }
        finally
        {
            TimeZone.setDefault(defaultTimeZone);
            Locale.setDefault(defaultLocale);
        }
	}
	
	
	
	/**
	 * Assert that date input with and without RFC-822 Timezone ist supported.
	 *
	 * @throws Exception if the parsing went wrong.
	 */
	@Test
	public void testTimeWithRFC822Timezone() throws Exception
	{
        TimeZone defaultTimeZone = TimeZone.getDefault();
        
        TimeZone.setDefault(TimeZone.getTimeZone("CET"));
        
        try
        {
    		Calendar cal = TimeZoneUtil.getDefaultCalendar();
    		cal.set(Calendar.HOUR_OF_DAY, 16);
    		cal.set(Calendar.MINUTE, 17);
    		cal.set(Calendar.SECOND, 18);
    		cal.set(Calendar.MILLISECOND, 0);
    		cal.set(Calendar.MONTH, 0);
    		cal.set(Calendar.YEAR, 1970);
    		cal.set(Calendar.DAY_OF_MONTH, 1);
    		Date expected = cal.getTime();
    		
    		DateUtil dateUtil = new DateUtil("HH:mm:ss Z");	
    		Assert.assertEquals(expected, dateUtil.parse("17:17:18 +0200"));		
    		Assert.assertEquals(expected, dateUtil.parse("12:17:18 -0300"));		
    		Assert.assertEquals(expected, dateUtil.parse("15:17:18 +0000"));		
    		Assert.assertEquals(expected, dateUtil.parse("16:17:18"));
        }
        finally
        {
            TimeZone.setDefault(defaultTimeZone);
        }
	}
	
	/**
	 * Assert that date input with and without weekday is supported (ticket #1792).
	 *
	 * @throws Exception if the parsing went wrong.
	 */
	@Test
	public void testTimeWithMillisecondsAndMeridiem() throws Exception
	{
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 16);
		cal.set(Calendar.MINUTE, 17);
		cal.set(Calendar.SECOND, 18);
		cal.set(Calendar.MILLISECOND, 234);
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.YEAR, 1970);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		Date expected = cal.getTime();

		cal.set(Calendar.MILLISECOND, 0);
		Date expectedWithoutMillis = cal.getTime();

		cal.set(Calendar.HOUR_OF_DAY, 4);
		Date expected04 = cal.getTime();

		cal.set(Calendar.HOUR_OF_DAY, 16);
		cal.set(Calendar.SECOND, 0);
		Date expected1617 = cal.getTime();

		cal.set(Calendar.MINUTE, 0);
		Date expected16 = cal.getTime();

		DateUtil dateUtil = new DateUtil("hh:mm:ss.S a");
		
		Assert.assertEquals(expected, dateUtil.parse("04:17:18.234 PM"));
		Assert.assertEquals(expected, dateUtil.parse("04:17:18.234PM"));
		Assert.assertEquals(expected, dateUtil.parse("16:17:18.234AM"));
		Assert.assertEquals(expected, dateUtil.parse("16:17:18.234PM"));
		Assert.assertEquals(expected, dateUtil.parse("16:17:18.234"));
		Assert.assertEquals(expectedWithoutMillis, dateUtil.parse("04:17:18 PM"));
		Assert.assertEquals(expectedWithoutMillis, dateUtil.parse("04:17:18 p"));
		Assert.assertEquals(expectedWithoutMillis, dateUtil.parse("04:17:18p"));
		Assert.assertEquals(expectedWithoutMillis, dateUtil.parse("16:17:18"));
		Assert.assertEquals(expected1617, dateUtil.parse("16:17"));
		Assert.assertEquals(expected16, dateUtil.parse("16"));
		Assert.assertEquals(expected1617, dateUtil.parse("04:17 pm"));
		Assert.assertEquals(expected1617, dateUtil.parse("04:17pm"));
		Assert.assertEquals(expected16, dateUtil.parse("04 pm"));
		Assert.assertEquals(expected16, dateUtil.parse("04pm"));

		Assert.assertEquals(expected04, dateUtil.parse("04:17:18"));
	}
	
	/**
	 * Test ticket #650.
	 *
	 * @throws Exception if the parsing went wrong.
	 */
	@Test
	public void testBug650() throws Exception
	{
		DateUtil dateUtil = new DateUtil("yyyy-MM-dd HH:mm:ss");
		 
		Date date = dateUtil.parse("2002-01-01");
		
		Assert.assertEquals(Timestamp.valueOf("2002-01-01 00:00:00.0").getTime(), date.getTime());
	}

	/**
	 * Test year with 2 digits.
	 *
	 * @throws Exception if the parsing went wrong.
	 */
	@Test
	public void test2DigitYear() throws Exception
	{
		DateUtil dateUtil = new DateUtil("dd.MM.yyyy HH:mm");
		
		Assert.assertEquals(dateUtil.parse("01.01.2016"), dateUtil.parse("01.01.16"));
		Assert.assertEquals(dateUtil.parse("01.01.2016"), dateUtil.parse("1 1 16"));
	}

	/**
	 * Test date without delimiers.
	 *
	 * @throws Exception if the parsing went wrong.
	 */
	@Test
	public void testDateWithoutDelimiter() throws Exception
	{
		DateUtil reference = new DateUtil("dd.MM.yyyy HH:mm:ss");
		DateUtil dateUtilEn = new DateUtil("yyyyMMddHHmmss");
		DateUtil dateUtilDe = new DateUtil("ddMMyyyyHHmmss");
		
		Assert.assertEquals(reference.parse("03.04.2016 23:14:13"), dateUtilEn.parse("20160403231413"));
		Assert.assertEquals(reference.parse("03.04.2016 00:00:00"), dateUtilDe.parse("03042016"));
		Assert.assertEquals(reference.parse("03.04.2016 00:00:00"), dateUtilDe.parse("030416"));
	}

	/**
	 * Test date in non strict check (default).
	 *
	 * @throws Exception if the parsing went wrong.
	 */
	@Test
	public void testDateNonStrictFormatCheck() throws Exception
	{
	    int iYear = Calendar.getInstance().get(Calendar.YEAR);
	    String sYear2 = ("" + iYear).substring(2);
	    
		DateUtil reference = new DateUtil("dd.MMM.yyyy HH:mm");
		Date expected = reference.parse("31.Jan." + iYear + " 00:00");
		
		Assert.assertEquals(expected, reference.parse("31.Jänner"));
		Assert.assertEquals(expected, reference.parse("31.Jan"));
		Assert.assertEquals(expected, reference.parse("31.Jan"));
		Assert.assertEquals(expected, reference.parse("31.01." + iYear));
		Assert.assertEquals(expected, reference.parse("3101" + sYear2));
		Assert.assertEquals(expected, reference.parse("3101" + iYear));
		Assert.assertEquals(expected, reference.parse("31-01-" + sYear2));
		Assert.assertEquals(expected, reference.parse("3101" + iYear + "0000"));
		try
		{
			Date advanced4 = reference.parse("32.01.2016 00:00");
			
			System.out.println(advanced4);
			
			throw new Exception("Not Ok!");
		}
		catch (ParseException ex)
		{
			// Ok
		}
	}

	/**
	 * Test date in non strict check (default).
	 *
	 * @throws Exception if the parsing went wrong.
	 */
	@Test
	public void testDateStrictFormatCheck() throws Exception
	{
		DateUtil reference = new DateUtil("dd.MMM.yyyy HH:mm");
		reference.setStrictFormatCheck(true);
		
		Date expected = reference.parse("31.Jän.2016 00:00");
		
		Date advanced = reference.parse("31.Jänner.2016 00:00");
		
		Date advanced2 = reference.parse("31.Jan.2016 00:00");
		Date advanced3 = reference.parse("31.Januar.2016 00:00");

		try
		{
			Date advanced4 = reference.parse("31.01.2016 00:00");
			
			throw new Exception("Not Ok!");
		}
		catch (ParseException ex)
		{
			// Ok
		}
	}
	
	/**
	 * Tests if the constructors run without exception.
	 * 
	 * @throws Exception if the test fails.
	 */
	@Test
	public void testConstructors() throws Exception
	{
		new DateUtil();
		
		new DateUtil((String)null);
		new DateUtil(null, null);
		
		new DateUtil("dd MM yyyy", null);
		new DateUtil("dd MM yyyy", Locale.CANADA);
		new DateUtil(null, Locale.CANADA);
	}

	/**
     * Tests the DateUtil with the default formatting of all Locales.
     * 
     * @throws Exception if the test fails.
     */
    @Test
    public void testDateUtilInitialization() throws Exception
    {
        DateUtil dateUtil = new DateUtil();

        Locale[] availableLocales = Locale.getAvailableLocales();
        for (int z = 0; z < availableLocales.length; z++)
        {
            Locale locale = availableLocales[z];
            try
            {
                dateUtil.setDatePattern(null, locale);
            }
            catch (Exception ex)
            {
                Assert.fail("DateUtil for locale: " + locale + " cannot be initialized!");
            }
        }
    }

        
	/**
	 * Tests the DateUtil with the default formatting of all Locales.
	 * 
	 * @throws Exception if the test fails.
	 */
	@Test
	public void testLocaleDefaultFormats() throws Exception
	{
		Calendar refCalendar = Calendar.getInstance();
		refCalendar.set(2018, 1, 2, 3, 4, 0);
		
		Date refDate = refCalendar.getTime();
		Date testDate = new Date();
		DateUtil dateUtil = new DateUtil();
		String dateStr = null;
		Date date = null;
		
		Locale[] availableLocales = Locale.getAvailableLocales();
		
		for (int z = 0; z < availableLocales.length; z++)
		{
		    Locale locale = availableLocales[z];
			try
			{
				dateUtil.setDatePattern(null, locale);

				// Check 8 years to have leap year and am pm dates.
				for (int i = 0; i < 366 * 2 * 8; i++)
				{
					testDate.setTime(refDate.getTime() / 1000 * 1000 + i * 60000L * 60 * 12);
					dateStr = dateUtil.format(testDate);
					date = dateUtil.parse(dateStr);

					if (!date.equals(testDate))
					{
                        dateUtil.parse(dateStr); // For Debugging Reasons

                        System.out.println("Locale:           " + locale + " Index: " + z);
	                    System.out.println("Pattern:          " + dateUtil.getDatePattern());
                        System.out.println("Formatted Date:   " + dateStr);
                        System.out.println("Original Date:    " + new Timestamp(testDate.getTime()) + " TimeMillis: " + testDate.getTime());
                        System.out.println("Parsed Date:      " + new Timestamp(date.getTime()) + " TimeMillis: " + date.getTime());

	                    try
	                    {
	                        SimpleDateFormat dateFormat = new SimpleDateFormat(dateUtil.getDatePattern(), locale); 
	                        Date javaParsedDate = dateFormat.parse(dateStr);
	                        
	                        System.out.println("SimpleDateFormat: " + new Timestamp(javaParsedDate.getTime()) + " TimeMillis: " + javaParsedDate.getTime());
	                        if (!testDate.equals(javaParsedDate))
	                        {
	                            System.out.println("Warning SimpleDateFormat parses wrong date!");
	                        }
	                        else
	                        {
	                            System.out.println("Date is wrong parsed!");
//	                            Assert.assertEquals(testDate, date);
	                            break; // Only one error report per locale! 
	                        }
	                    }
	                    catch (Exception ex)
	                    {
	                        System.out.println("SimpleDateFormat cannot parse the formatted Date!");
	                        
                            Assert.assertEquals(testDate, date);
	                    }
					}
				}
				
			}
			catch (Exception e)
			{
			    try
			    {
			        dateUtil.parse(dateStr); // For Debugging Reasons
			    }
			    catch (Exception ex)
			    {
			        // Ignore
			    }

                System.out.println("Locale:           " + locale + " Index: " + z);
                System.out.println("Pattern:          " + dateUtil.getDatePattern());
                System.out.println("Formatted Date:   " + dateStr);
                System.out.println("Original Date:    " + new Timestamp(testDate.getTime()) + " TimeMillis: " + testDate.getTime());
                if (date != null)
                {
                    System.out.println("Parsed Date:      " + new Timestamp(date.getTime()) + " TimeMillis: " + date.getTime());
                }

                try
                {
                    SimpleDateFormat dateFormat = new SimpleDateFormat(dateUtil.getDatePattern(), locale); 
                    Date javaParsedDate = dateFormat.parse(dateStr);
                    
                    System.out.println("SimpleDateFormat: " + new Timestamp(javaParsedDate.getTime()) + " TimeMillis: " + javaParsedDate.getTime());
                    if (date.equals(javaParsedDate))
                    {
                        System.out.println("Warning SimpleDateFormat parses wrong date!");
                    }
                }
                catch (Exception ex)
                {
                    System.out.println("SimpleDateFormat cannot parse the formatted Date!");
                }
				
                Assert.fail(e.getMessage() + ": " + locale.toString() + "  " + dateUtil.getDatePattern() + "  " + dateStr + "  " + testDate);
			}
		}
	}
	
	/**
	 * Tests the the DateUtil with different Locales.
	 * 
	 * @throws Exception if the test fails.
	 */
	@Test
	public void testLocaleUsage() throws Exception
	{
		Date date = newDate(2018, 2, 7);
		
		DateUtil undefined = new DateUtil("EE MM yyyy");
		
		DateUtil english = new DateUtil("EE MM yyyy", new Locale("en", "US"));
		DateUtil german = new DateUtil("EE MM yyyy", new Locale("de", "DE"));
		DateUtil spanish = new DateUtil("EE MM yyyy", new Locale("es", "ES"));
		
		LocaleUtil.setDefault(new Locale("fr", "FR"));
		
		Assert.assertEquals("mer 02 2018", undefined.format(date).replace(".", ""));
		Assert.assertEquals("Wed 02 2018", english.format(date).replace(".", ""));
		Assert.assertEquals("Mi 02 2018", german.format(date).replace(".", ""));
		Assert.assertEquals("mié 02 2018", spanish.format(date).replace(".", ""));
		
		// Do it a second time to check if the internal state has been corrupted.
		Assert.assertEquals("mer 02 2018", undefined.format(date).replace(".", ""));
		Assert.assertEquals("Wed 02 2018", english.format(date).replace(".", ""));
		Assert.assertEquals("Mi 02 2018", german.format(date).replace(".", ""));
		Assert.assertEquals("mié 02 2018", spanish.format(date).replace(".", ""));
		
		// Check if the undefined locale follows the default Locale.
		LocaleUtil.setDefault(new Locale("de", "DE"));
		Assert.assertEquals("Mi 02 2018", undefined.format(date).replace(".", ""));
	}
	
	/**
	 * Tests that creating a {@link DateUtil} with a simple pattern works.
	 * 
	 * @throws Exception if the test fails.
	 */
	@Test
	public void testSingleDateParts1815() throws Exception
	{
		Assert.assertEquals("13", new DateUtil("dd").format(new DateUtil("dd").parse("13")));
		Assert.assertEquals("05", new DateUtil("MM").format(new DateUtil("MM").parse("5")));
		Assert.assertEquals("13", new DateUtil("HH").format(new DateUtil("HH").parse("13")));
		Assert.assertEquals("13", new DateUtil("mm").format(new DateUtil("mm").parse("13")));
		Assert.assertEquals("1313", new DateUtil("yyyy").format(new DateUtil("yyyy").parse("1313")));
	}
	
	/**
	 * Tests the DateUtil for possible failing format patterns, for example the
	 * spanish one.
	 * 
	 * @throws Exception when the test failes.
	 */
	@Test
	public void testFailingFormatPatterns1853() throws Exception
	{
		Date expected = newDate(2017, 1, 2);
		
		DateUtil dateUtil = new DateUtil("' KW'w E, yy");
		Assert.assertEquals(expected, dateUtil.parse(" KW01 Mon, 17"));
		Assert.assertEquals(expected, dateUtil.parse(" KW1 2017"));
		Assert.assertEquals(expected, dateUtil.parse(" KW1 17"));
		
		dateUtil = new DateUtil("d' de 'MMMM' de 'yyyy H:mm");
		Assert.assertEquals(expected, dateUtil.parse("02 de 01 de 2017 0:00"));
	}

	/**
	 * Creates a new {@link Date}.
	 * 
	 * @param pYear the year.
	 * @param pMonth the month (1-based).
	 * @param pDay the day (1-based)
	 * @return the {@link Date}.
	 */
	private static final Date newDate(int pYear, int pMonth, int pDay)
	{
		Calendar cal = Calendar.getInstance();
		cal.set(pYear, pMonth - 1, pDay, 0, 0, 0);
		cal.clear(Calendar.MILLISECOND);
		
		return cal.getTime();
	}
	
	/**
	 * Tests Asia/Taipei.
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testTaipeiBug2201() throws Exception
	{
	    TimeZone defaultTimeZone = TimeZone.getDefault();
	    Locale defaultLocale = Locale.getDefault();
	    
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Taipei"));
		Locale.setDefault(new Locale("zh", "TW", ""));
		
		try
		{
            new DateUtil("yyyy~MÄÄÄh:mm:ss");
    		new DateUtil("y~M뤤頡h:mm:ss");
		}
		finally
		{
            TimeZone.setDefault(defaultTimeZone);
            Locale.setDefault(defaultLocale);
		}
	}
	
	/**
	 * Tests Asia/Taipei.
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testJpnBug2201() throws Exception
	{
		/*
	    TimeZone defaultTimeZone = TimeZone.getDefault();
	    Locale defaultLocale = Locale.getDefault();
	    
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+09:00"));
		Locale.setDefault(new Locale("ja", "JP", ""));
		
        new DateUtil("");
		
        TimeZone.setDefault(defaultTimeZone);
        Locale.setDefault(defaultLocale);
        */
	}	
	
    /**
     * Tests Finish old.
     * 
     * @throws Exception if test fails
     */
    @Test
    public void testFinishOldJVm() throws Exception
    {
        Locale defaultLocale = Locale.getDefault();
        
        Locale.setDefault(new Locale("fi", "FI", ""));
        
		Calendar cal = Calendar.getInstance();
		cal.set(118, 1, 2, 3, 4, 0);
		cal.clear(Calendar.MILLISECOND);

		Date date = cal.getTime();
        
        DateUtil du = new DateUtil("d. MMMM'ta 'yyyy H:mm");
        
        String format = du.format(date);
        
        Date parsedDate = du.parse(format);
        
        Assert.assertEquals(date, parsedDate);
        
        Locale.setDefault(defaultLocale);
    }
    
    /**
     * Tests strict parsing with time zone.
     * @throws Exception if it fails.
     */
    @Test
    public void testTimeWithISO8601TimezoneX() throws Exception
    {
        TimeZone defaultTimeZone = TimeZone.getDefault();
        
        TimeZone.setDefault(TimeZone.getTimeZone("CET"));
        
        try
        {
            Calendar cal = TimeZoneUtil.getDefaultCalendar();
            cal.set(Calendar.HOUR_OF_DAY, 16);
            cal.set(Calendar.MINUTE, 17);
            cal.set(Calendar.SECOND, 18);
            cal.set(Calendar.MILLISECOND, 0);
            cal.set(Calendar.MONTH, 0);
            cal.set(Calendar.YEAR, 1970);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            Date expected = cal.getTime();
    
            DateUtil dateUtil = new DateUtil("yyyy-MM-dd'T'HH:mm:ss.SX");
            dateUtil.setStrictFormatCheck(true);
            
            Assert.assertEquals(expected, dateUtil.parse("1970-01-01T15:17:18.0Z"));
            Assert.assertEquals(expected, dateUtil.parse("1970-01-01T15:17:18.0+00:00"));
            Assert.assertEquals(expected, dateUtil.parse("1970-01-01T15:17:18Z"));
            Assert.assertEquals(expected, dateUtil.parse("1970-01-01T15:17:18-00:00"));
            Assert.assertEquals(expected, dateUtil.parse("1970-01-01T14:17:18-0100"));
            Assert.assertEquals(expected, dateUtil.parse("1970-01-01T16:17:18.0+0100"));
        }
        finally
        {
            TimeZone.setDefault(defaultTimeZone);
        }
    }

    /**
     * Tests months between function.
     * @throws Exception if it fails.
     */
    @Test
    public void testMonthsBetween() throws Exception
    {
        Date startDate1 = DateUtil.getTimestamp(2020, 1, 29, 23, 15, 0);
        Date endDate1   = DateUtil.getTimestamp(2020, 6, 29, 23, 15, 0);
        double months1 = DateUtil.monthsBetween(endDate1, startDate1);

        Date startDate2 = DateUtil.getTimestamp(2020, 1, 29, 23, 15, 0);
        Date endDate2   = DateUtil.getTimestamp(2020, 6, 29, 22, 15, 0);
        double months2 = DateUtil.monthsBetween(endDate2, startDate2);
        
        Date startDate3 = DateUtil.getTimestamp(2020, 1, 29, 22, 15, 0);
        Date endDate3   = DateUtil.getTimestamp(2020, 1, 31, 23, 15, 0);
        double months3 = DateUtil.monthsBetween(endDate3, startDate3);

        Date startDate4 = DateUtil.getTimestamp(2020, 1, 31, 23, 15, 0);
        Date endDate4   = DateUtil.getTimestamp(2020, 2, 29, 23, 15, 0);
        double months4 = DateUtil.monthsBetween(endDate4, startDate4);

        Date startDate5 = DateUtil.getTimestamp(2020, 1, 30, 23, 15, 0);
        Date endDate5   = DateUtil.getTimestamp(2020, 2, 29, 23, 15, 0);
        double months5 = DateUtil.monthsBetween(endDate5, startDate5);
        
        Date startDate6 = DateUtil.getTimestamp(2020, 1, 29, 23, 15, 0);
        Date endDate6   = DateUtil.getTimestamp(2020, 2, 29, 23, 15, 0);
        double months6 = DateUtil.monthsBetween(endDate6, startDate6);
        
        Date startDate7 = DateUtil.getTimestamp(2020, 3, 11, 23, 15, 0);
        Date endDate7   = DateUtil.getTimestamp(2020, 4, 10, 23, 15, 0);
        double months7 = DateUtil.monthsBetween(endDate7, startDate7);
        
        Date startDate8 = DateUtil.getTimestamp(2020, 2, 29, 23, 15, 0);
        Date endDate8   = DateUtil.getTimestamp(2020, 3, 31, 23, 15, 0);
        double months8 = DateUtil.monthsBetween(endDate8, startDate8);
        
        Date startDate9 = DateUtil.getTimestamp(2020, 2, 29, 23, 15, 0);
        Date endDate9   = DateUtil.getTimestamp(2020, 3, 30, 23, 15, 0);
        double months9 = DateUtil.monthsBetween(endDate9, startDate9);
        
        Date startDate10 = DateUtil.getTimestamp(2020, 2, 29, 23, 15, 0);
        Date endDate10   = DateUtil.getTimestamp(2020, 3, 29, 23, 15, 0);
        double months10 = DateUtil.monthsBetween(endDate10, startDate10);
        
//        System.out.println(months1);
//        System.out.println(months2);
//        System.out.println(months3);
//        System.out.println(months4);
//        System.out.println(months5);
//        System.out.println(months6);
//        System.out.println(months7);
//        System.out.println(months8);
//        System.out.println(months9);
//        System.out.println(months10);
//        System.out.println(DateUtil.addMonths(startDate1, months1) + "  " + endDate1);
//        System.out.println(DateUtil.addMonths(startDate2, months2) + "  " + endDate2);
//        System.out.println(DateUtil.addMonths(startDate3, months3) + "  " + endDate3);
//        System.out.println(DateUtil.addMonths(startDate4, months4) + "  " + endDate4);
//        System.out.println(DateUtil.addMonths(startDate5, months5) + "  " + endDate5);
//        System.out.println(DateUtil.addMonths(startDate6, months6) + "  " + endDate6);
//        System.out.println(DateUtil.addMonths(startDate7, months7) + "  " + endDate7);
//        System.out.println(DateUtil.addMonths(startDate8, months8) + "  " + endDate8);
//        System.out.println(DateUtil.addMonths(startDate9, months9) + "  " + endDate9);
//        System.out.println(DateUtil.addMonths(startDate10, months10) + "  " + endDate10);

        Assert.assertEquals(5d, months1, Double.MIN_VALUE);

        Assert.assertEquals(endDate1, DateUtil.addMonths(startDate1, months1));
        Assert.assertEquals(endDate2, DateUtil.addMonths(startDate2, months2));
        Assert.assertEquals(endDate3, DateUtil.addMonths(startDate3, months3));
        Assert.assertEquals(endDate4, DateUtil.addMonths(startDate4, months4));
        Assert.assertEquals(endDate5, DateUtil.addMonths(startDate5, months5));
        Assert.assertEquals(endDate6, DateUtil.addMonths(startDate6, months6));
        Assert.assertEquals(endDate7, DateUtil.addMonths(startDate7, months7));
        Assert.assertEquals(endDate8, DateUtil.addMonths(startDate8, months8));
        Assert.assertEquals(endDate9, DateUtil.addMonths(startDate9, months9));
        Assert.assertEquals(endDate10, DateUtil.addMonths(startDate10, months10));

        double monthsN1 = DateUtil.monthsBetween(startDate1, endDate1);
        double monthsN2 = DateUtil.monthsBetween(startDate2, endDate2);
        double monthsN3 = DateUtil.monthsBetween(startDate3, endDate3);
        double monthsN4 = DateUtil.monthsBetween(startDate4, endDate4);
        double monthsN5 = DateUtil.monthsBetween(startDate5, endDate5);
        double monthsN6 = DateUtil.monthsBetween(startDate6, endDate6);
        double monthsN7 = DateUtil.monthsBetween(startDate7, endDate7);
        double monthsN8 = DateUtil.monthsBetween(startDate8, endDate8);
        double monthsN9 = DateUtil.monthsBetween(startDate9, endDate9);
        double monthsN10 = DateUtil.monthsBetween(startDate10, endDate10);

//        System.out.println(monthsN1);
//        System.out.println(monthsN2);
//        System.out.println(monthsN3);
//        System.out.println(monthsN4);
//        System.out.println(monthsN5);
//        System.out.println(monthsN6);
//        System.out.println(monthsN7);
//        System.out.println(monthsN8);
//        System.out.println(monthsN9);
//        System.out.println(monthsN10);
//        System.out.println(DateUtil.addMonths(endDate1, monthsN1) + "  " + startDate1);
//        System.out.println(DateUtil.addMonths(endDate2, monthsN2) + "  " + startDate2);
//        System.out.println(DateUtil.addMonths(endDate3, monthsN3) + "  " + startDate3);
//        System.out.println(DateUtil.addMonths(endDate4, monthsN4) + "  " + startDate4);
//        System.out.println(DateUtil.addMonths(endDate5, monthsN5) + "  " + startDate5);
//        System.out.println(DateUtil.addMonths(endDate6, monthsN6) + "  " + startDate6);
//        System.out.println(DateUtil.addMonths(endDate7, monthsN7) + "  " + startDate7);
//        System.out.println(DateUtil.addMonths(endDate8, monthsN8) + "  " + startDate8);
//        System.out.println(DateUtil.addMonths(endDate9, monthsN9) + "  " + startDate9);
//        System.out.println(DateUtil.addMonths(endDate10, monthsN10) + "  " + startDate10);
        
        Assert.assertEquals(-5d, monthsN1, Double.MIN_VALUE);

        Assert.assertEquals(startDate1, DateUtil.addMonths(endDate1, monthsN1));
        Assert.assertEquals(startDate2, DateUtil.addMonths(endDate2, monthsN2));
        Assert.assertEquals(startDate3, DateUtil.addMonths(endDate3, monthsN3));
        Assert.assertEquals(startDate4, DateUtil.addMonths(endDate4, monthsN4));
        Assert.assertEquals(startDate5, DateUtil.addMonths(endDate5, monthsN5));
        Assert.assertEquals(startDate6, DateUtil.addMonths(endDate6, monthsN6));
        Assert.assertEquals(startDate7, DateUtil.addMonths(endDate7, monthsN7));
        Assert.assertEquals(startDate8, DateUtil.addMonths(endDate8, monthsN8));
        Assert.assertEquals(startDate9, DateUtil.addMonths(endDate9, monthsN9));
        Assert.assertEquals(startDate10, DateUtil.addMonths(endDate10, monthsN10));
    }

    /**
     * Tests months between function.
     * @throws Exception if it fails.
     */
    @Test
    public void testMonthsBetweenFromEnd() throws Exception
    {
        Date startDate1 = DateUtil.getTimestamp(2020, 1, 29, 23, 15, 0);
        Date endDate1   = DateUtil.getTimestamp(2020, 6, 29, 23, 15, 0);
        double months1 = DateUtil.monthsBetweenRelativeToEnd(endDate1, startDate1);

        Date startDate2 = DateUtil.getTimestamp(2020, 1, 29, 23, 15, 0);
        Date endDate2   = DateUtil.getTimestamp(2020, 6, 29, 22, 15, 0);
        double months2 = DateUtil.monthsBetweenRelativeToEnd(endDate2, startDate2);
        
        Date startDate3 = DateUtil.getTimestamp(2020, 1, 29, 22, 15, 0);
        Date endDate3   = DateUtil.getTimestamp(2020, 1, 31, 23, 15, 0);
        double months3 = DateUtil.monthsBetweenRelativeToEnd(endDate3, startDate3);

        Date startDate4 = DateUtil.getTimestamp(2020, 1, 31, 23, 15, 0);
        Date endDate4   = DateUtil.getTimestamp(2020, 2, 29, 23, 15, 0);
        double months4 = DateUtil.monthsBetweenRelativeToEnd(endDate4, startDate4);

        Date startDate5 = DateUtil.getTimestamp(2020, 1, 30, 23, 15, 0);
        Date endDate5   = DateUtil.getTimestamp(2020, 2, 29, 23, 15, 0);
        double months5 = DateUtil.monthsBetweenRelativeToEnd(endDate5, startDate5);
        
        Date startDate6 = DateUtil.getTimestamp(2020, 1, 29, 23, 15, 0);
        Date endDate6   = DateUtil.getTimestamp(2020, 2, 29, 23, 15, 0);
        double months6 = DateUtil.monthsBetweenRelativeToEnd(endDate6, startDate6);
        
        Date startDate7 = DateUtil.getTimestamp(2020, 3, 11, 23, 15, 0);
        Date endDate7   = DateUtil.getTimestamp(2020, 4, 10, 23, 15, 0);
        double months7 = DateUtil.monthsBetweenRelativeToEnd(endDate7, startDate7);
        
        Date startDate8 = DateUtil.getTimestamp(2020, 2, 29, 23, 15, 0);
        Date endDate8   = DateUtil.getTimestamp(2020, 3, 31, 23, 15, 0);
        double months8 = DateUtil.monthsBetweenRelativeToEnd(endDate8, startDate8);
        
        Date startDate9 = DateUtil.getTimestamp(2020, 2, 29, 23, 15, 0);
        Date endDate9   = DateUtil.getTimestamp(2020, 3, 30, 23, 15, 0);
        double months9 = DateUtil.monthsBetweenRelativeToEnd(endDate9, startDate9);
        
        Date startDate10 = DateUtil.getTimestamp(2020, 2, 29, 23, 15, 0);
        Date endDate10   = DateUtil.getTimestamp(2020, 3, 29, 23, 15, 0);
        double months10 = DateUtil.monthsBetweenRelativeToEnd(endDate10, startDate10);
        
//        System.out.println(months1);
//        System.out.println(months2);
//        System.out.println(months3);
//        System.out.println(months4);
//        System.out.println(months5);
//        System.out.println(months6);
//        System.out.println(months7);
//        System.out.println(months8);
//        System.out.println(months9);
//        System.out.println(months10);
//        System.out.println(DateUtil.addMonthsFromEnd(startDate1, months1) + "  " + endDate1);
//        System.out.println(DateUtil.addMonthsFromEnd(startDate2, months2) + "  " + endDate2);
//        System.out.println(DateUtil.addMonthsFromEnd(startDate3, months3) + "  " + endDate3);
//        System.out.println(DateUtil.addMonthsFromEnd(startDate4, months4) + "  " + endDate4);
//        System.out.println(DateUtil.addMonthsFromEnd(startDate5, months5) + "  " + endDate5);
//        System.out.println(DateUtil.addMonthsFromEnd(startDate6, months6) + "  " + endDate6);
//        System.out.println(DateUtil.addMonthsFromEnd(startDate7, months7) + "  " + endDate7);
//        System.out.println(DateUtil.addMonthsFromEnd(startDate8, months8) + "  " + endDate8);
//        System.out.println(DateUtil.addMonthsFromEnd(startDate9, months9) + "  " + endDate9);
//        System.out.println(DateUtil.addMonthsFromEnd(startDate10, months10) + "  " + endDate10);

        Assert.assertEquals(1d, months4, Double.MIN_VALUE);

        Assert.assertEquals(endDate1, DateUtil.addMonthsRelativeToEnd(startDate1, months1));
        Assert.assertEquals(endDate2, DateUtil.addMonthsRelativeToEnd(startDate2, months2));
        Assert.assertEquals(endDate3, DateUtil.addMonthsRelativeToEnd(startDate3, months3));
        Assert.assertEquals(endDate4, DateUtil.addMonthsRelativeToEnd(startDate4, months4));
        Assert.assertEquals(endDate5, DateUtil.addMonthsRelativeToEnd(startDate5, months5));
        Assert.assertEquals(endDate6, DateUtil.addMonthsRelativeToEnd(startDate6, months6));
        Assert.assertEquals(endDate7, DateUtil.addMonthsRelativeToEnd(startDate7, months7));
        Assert.assertEquals(endDate8, DateUtil.addMonthsRelativeToEnd(startDate8, months8));
        Assert.assertEquals(endDate9, DateUtil.addMonthsRelativeToEnd(startDate9, months9));
        Assert.assertEquals(endDate10, DateUtil.addMonthsRelativeToEnd(startDate10, months10));

        double monthsN1 = DateUtil.monthsBetweenRelativeToEnd(startDate1, endDate1);
        double monthsN2 = DateUtil.monthsBetweenRelativeToEnd(startDate2, endDate2);
        double monthsN3 = DateUtil.monthsBetweenRelativeToEnd(startDate3, endDate3);
        double monthsN4 = DateUtil.monthsBetweenRelativeToEnd(startDate4, endDate4);
        double monthsN5 = DateUtil.monthsBetweenRelativeToEnd(startDate5, endDate5);
        double monthsN6 = DateUtil.monthsBetweenRelativeToEnd(startDate6, endDate6);
        double monthsN7 = DateUtil.monthsBetweenRelativeToEnd(startDate7, endDate7);
        double monthsN8 = DateUtil.monthsBetweenRelativeToEnd(startDate8, endDate8);
        double monthsN9 = DateUtil.monthsBetweenRelativeToEnd(startDate9, endDate9);
        double monthsN10 = DateUtil.monthsBetweenRelativeToEnd(startDate10, endDate10);

//        System.out.println(monthsN1);
//        System.out.println(monthsN2);
//        System.out.println(monthsN3);
//        System.out.println(monthsN4);
//        System.out.println(monthsN5);
//        System.out.println(monthsN6);
//        System.out.println(monthsN7);
//        System.out.println(monthsN8);
//        System.out.println(monthsN9);
//        System.out.println(monthsN10);
//        System.out.println(DateUtil.addMonthsFromEnd(endDate1, monthsN1) + "  " + startDate1);
//        System.out.println(DateUtil.addMonthsFromEnd(endDate2, monthsN2) + "  " + startDate2);
//        System.out.println(DateUtil.addMonthsFromEnd(endDate3, monthsN3) + "  " + startDate3);
//        System.out.println(DateUtil.addMonthsFromEnd(endDate4, monthsN4) + "  " + startDate4);
//        System.out.println(DateUtil.addMonthsFromEnd(endDate5, monthsN5) + "  " + startDate5);
//        System.out.println(DateUtil.addMonthsFromEnd(endDate6, monthsN6) + "  " + startDate6);
//        System.out.println(DateUtil.addMonthsFromEnd(endDate7, monthsN7) + "  " + startDate7);
//        System.out.println(DateUtil.addMonthsFromEnd(endDate8, monthsN8) + "  " + startDate8);
//        System.out.println(DateUtil.addMonthsFromEnd(endDate9, monthsN9) + "  " + startDate9);
//        System.out.println(DateUtil.addMonthsFromEnd(endDate10, monthsN10) + "  " + startDate10);
        
        Assert.assertEquals(-1d, monthsN4, Double.MIN_VALUE);

        Assert.assertEquals(startDate1, DateUtil.addMonthsRelativeToEnd(endDate1, monthsN1));
        Assert.assertEquals(startDate2, DateUtil.addMonthsRelativeToEnd(endDate2, monthsN2));
        Assert.assertEquals(startDate3, DateUtil.addMonthsRelativeToEnd(endDate3, monthsN3));
        Assert.assertEquals(startDate4, DateUtil.addMonthsRelativeToEnd(endDate4, monthsN4));
        Assert.assertEquals(startDate5, DateUtil.addMonthsRelativeToEnd(endDate5, monthsN5));
        Assert.assertEquals(startDate6, DateUtil.addMonthsRelativeToEnd(endDate6, monthsN6));
        Assert.assertEquals(startDate7, DateUtil.addMonthsRelativeToEnd(endDate7, monthsN7));
        Assert.assertEquals(startDate8, DateUtil.addMonthsRelativeToEnd(endDate8, monthsN8));
        Assert.assertEquals(startDate9, DateUtil.addMonthsRelativeToEnd(endDate9, monthsN9));
        Assert.assertEquals(startDate10, DateUtil.addMonthsRelativeToEnd(endDate10, monthsN10));
    }

    /**
     * Tests months between function.
     * @throws Exception if it fails.
     */
    @Test
    public void testDaysBetweenAndAddDays() throws Exception
    {
        Date startDate1 = DateUtil.getTimestamp(2020, 1, 29, 23, 15, 0);
        Date endDate1   = DateUtil.getTimestamp(2020, 6, 29, 23, 15, 0);
        double days1 = DateUtil.daysBetween(endDate1, startDate1);

        Date startDate2 = DateUtil.getTimestamp(2020, 1, 29, 23, 15, 0);
        Date endDate2   = DateUtil.getTimestamp(2020, 6, 29, 22, 15, 0);
        double days2 = DateUtil.daysBetween(endDate2, startDate2);
        
        Date startDate3 = DateUtil.getTimestamp(2020, 1, 29, 23, 15, 0);
        Date endDate3   = DateUtil.getTimestamp(2020, 1, 31, 22, 15, 0);
        double days3 = DateUtil.daysBetween(endDate3, startDate3);
        
//      System.out.println(days1);
//      System.out.println(days2);
//      System.out.println(days3);
//      System.out.println(DateUtil.addDays(startDate1, days1));
//      System.out.println(DateUtil.addDays(startDate2, days2));
//      System.out.println(DateUtil.addDays(startDate3, days3));

        Assert.assertEquals(152d, days1, Double.MIN_VALUE);
        
        Assert.assertEquals(endDate1, DateUtil.addDays(startDate1, days1));
        Assert.assertEquals(endDate2, DateUtil.addDays(startDate2, days2));
        Assert.assertEquals(endDate3, DateUtil.addDays(startDate3, days3));
        
        double daysN1 = DateUtil.daysBetween(startDate1, endDate1);
        double daysN2 = DateUtil.daysBetween(startDate2, endDate2);
        double daysN3 = DateUtil.daysBetween(startDate3, endDate3);

//      System.out.println(daysN1);
//      System.out.println(daysN2);
//      System.out.println(daysN3);
//      System.out.println(DateUtil.addDays(endDate1, daysN1));
//      System.out.println(DateUtil.addDays(endDate2, daysN2));
//      System.out.println(DateUtil.addDays(endDate3, daysN3));

        Assert.assertEquals(-152d, daysN1, Double.MIN_VALUE);
        
        Assert.assertEquals(startDate1, DateUtil.addDays(endDate1, daysN1));
        Assert.assertEquals(startDate2, DateUtil.addDays(endDate2, daysN2));
        Assert.assertEquals(startDate3, DateUtil.addDays(endDate3, daysN3));
    }

    /**
     * Tests trunc function.
     * @throws Exception if it fails.
     */
    @Test
    public void testTrunc() throws Exception
    {
        Date startDate = DateUtil.getTimestamp(2020, 2, 27, 23, 15, 13, 137);

//        System.out.println(DateUtil.trunc(startDate, Calendar.YEAR));
//        System.out.println(DateUtil.trunc(startDate, Calendar.MONTH));
//        System.out.println(DateUtil.trunc(startDate, Calendar.DAY_OF_MONTH));
//        System.out.println(DateUtil.trunc(startDate, Calendar.HOUR_OF_DAY));
//        System.out.println(DateUtil.trunc(startDate, Calendar.MINUTE));
//        System.out.println(DateUtil.trunc(startDate, Calendar.SECOND));
//        System.out.println(DateUtil.lastDayOfYear(startDate));
//        System.out.println(DateUtil.lastMillisecondOfYear(startDate));
//        System.out.println(DateUtil.lastDayOfMonth(startDate));
//        System.out.println(DateUtil.lastMillisecondOfMonth(startDate));
//        System.out.println(DateUtil.lastMillisecondOfDay(startDate));
        
        Assert.assertEquals("2020-01-01 00:00:00.0", DateUtil.trunc(startDate, Calendar.YEAR).toString());
        Assert.assertEquals("2020-02-01 00:00:00.0", DateUtil.trunc(startDate, Calendar.MONTH).toString());
        Assert.assertEquals("2020-02-27 00:00:00.0", DateUtil.trunc(startDate, Calendar.DAY_OF_MONTH).toString());
        Assert.assertEquals("2020-02-27 23:00:00.0", DateUtil.trunc(startDate, Calendar.HOUR_OF_DAY).toString());
        Assert.assertEquals("2020-02-27 23:15:00.0", DateUtil.trunc(startDate, Calendar.MINUTE).toString());
        Assert.assertEquals("2020-02-27 23:15:13.0", DateUtil.trunc(startDate, Calendar.SECOND).toString());
        Assert.assertEquals("2020-12-31 00:00:00.0", DateUtil.lastDayOfYear(startDate).toString());
        Assert.assertEquals("2020-12-31 23:59:59.999", DateUtil.lastMillisecondOfYear(startDate).toString());
        Assert.assertEquals("2020-02-29 00:00:00.0", DateUtil.lastDayOfMonth(startDate).toString());
        Assert.assertEquals("2020-02-29 23:59:59.999", DateUtil.lastMillisecondOfMonth(startDate).toString());
        Assert.assertEquals("2020-02-27 23:59:59.999", DateUtil.lastMillisecondOfDay(startDate).toString());
    }

    /**
     * Tests trunc function.
     * @throws Exception if it fails.
     */
    @Test
    public void testNextDate() throws Exception
    {
        Date referenceDate = DateUtil.getTimestamp(2020, 2, 1, 0, 0, 0, 0);
        Date startDate = DateUtil.getTimestamp(2020, 2, 5, 23, 15, 13, 137);

//        System.out.println(DateUtil.getNextDate(startDate, referenceDate, 7, Calendar.DAY_OF_MONTH));
//        System.out.println(DateUtil.getNextDate(startDate, referenceDate, 1, Calendar.MONTH));
//        System.out.println(DateUtil.getNextDate(startDate, null, 1, Calendar.MONTH));

        Assert.assertEquals("2020-02-08 00:00:00.0", DateUtil.getNextDate(startDate, referenceDate, 7, Calendar.DAY_OF_MONTH).toString());
        Assert.assertEquals("2020-03-01 00:00:00.0", DateUtil.getNextDate(startDate, referenceDate, 1, Calendar.MONTH).toString());
        Assert.assertEquals("2020-03-05 23:15:13.137", DateUtil.getNextDate(startDate, null, 1, Calendar.MONTH).toString());
    }
    
    /**
     * Test for ticket <a href="https://oss.sibvisions.com/index.php?do=details&task_id=2570">2570</a>.
     */
    @Test
    public void test2570()
    {
    	DateUtil du = new DateUtil();
    	du.setDatePattern("EEE# MMM d# ''yy");
    }
    
    /**
     * Test for ticket <a href="https://oss.sibvisions.com/index.php?do=details&task_id=2571">2571</a>.
     */
    @Test
    public void test2571() throws Exception
    {
        LocaleUtil.setThreadDefault(Locale.GERMAN);
        try
        {
        	DateUtil du = new DateUtil();
        	du.setDatePattern("yyyy.MM.dd G 'at' HH:mm:ss z");
    
        	Assert.assertEquals(DateUtil.getTimestamp(-2020, 02, 01), du.parse("2021.02.01 v. Chr. at 00:00:00 MEZ"));
        	Assert.assertEquals(DateUtil.getTimestamp(2021, 02, 01), du.parse("2021.02.01 n. Chr. at 00:00:00 MEZ"));
        	Assert.assertEquals(DateUtil.getTimestamp(2021, 02, 01), du.parse("2021.02.01 n. Chr. at 00:00:00 MEZ"));
        	Assert.assertEquals(DateUtil.getTimestamp(2021, 02, 01), du.parse("2021.02.01 n.Chr. at 00:00:00 MEZ"));
        	Assert.assertEquals(DateUtil.getTimestamp(2021, 02, 01), du.parse("2021.02.01 n. C. at 00:00:00 MEZ"));
        	Assert.assertEquals(DateUtil.getTimestamp(2021, 02, 01), du.parse("2021.02.01 n.C. at 00:00:00 MEZ"));
        	Assert.assertEquals(DateUtil.getTimestamp(2021, 02, 01), du.parse("2021.02.01 nC at 00:00:00 MEZ"));
        }
        finally
        {
            LocaleUtil.setThreadDefault(null);
        }
    }

    /**
     * Test for ticket <a href="https://oss.sibvisions.com/index.php?do=details&task_id=2286">2286</a>.
     */
    @Test
    public void test2286() throws Exception
    {
        try
        {
            Locale[] availableLocales = Locale.getAvailableLocales();
            
            for (int z = 0; z < availableLocales.length; z++)
            {
                LocaleUtil.setThreadDefault(availableLocales[z]);
                try
                {
                    DateUtil du = new DateUtil();
                    
                    du.setDatePattern("y/MM/dd H:mm:ss");
        
                    du.setDatePattern(
                            ((SimpleDateFormat)SimpleDateFormat.getDateInstance(SimpleDateFormat.LONG, LocaleUtil.getDefault())).toPattern());
                    du.setDatePattern(
                            ((SimpleDateFormat)SimpleDateFormat.getDateInstance(SimpleDateFormat.MEDIUM, LocaleUtil.getDefault())).toPattern());
                    du.setDatePattern(
                            ((SimpleDateFormat)SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.LONG, SimpleDateFormat.SHORT, LocaleUtil.getDefault())).toPattern());
                    du.setDatePattern(
                            ((SimpleDateFormat)SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.MEDIUM, SimpleDateFormat.SHORT, LocaleUtil.getDefault())).toPattern());
                    du.setDatePattern(
                            ((SimpleDateFormat)SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT, LocaleUtil.getDefault())).toPattern());
                }
                catch (Exception ex)
                {
                    System.out.println("DateUtil failed for: " + LocaleUtil.getDefault());
                }
            }
        }
        finally
        {
            LocaleUtil.setThreadDefault(null);
        }
    }

    /**
     * Test parsing of similar timestamp formats.
     * eg DB2 has Timestamp format like: 2005-01-02-12.21.22.012333
     */
    @Test
    public void testSimilarTimestampFormat() throws Exception
    {
        DateUtil dateUtil = new DateUtil("yyyy-MM-dd HH:mm:ss");
        
        Assert.assertEquals("2011-01-01 12:21:22.0", dateUtil.parse("2011-01-01-12.21.22.0123456").toString());
    }

    /**
     * Test parsing of similar timestamp formats.
     * eg DB2 has Timestamp format like: 2005-01-02-12.21.22.012333
     */
    @Test
    public void testTimeZone() throws Exception
    {
        DateUtil dateUtil1 = new DateUtil();
        dateUtil1.setDatePattern("yyyy-MM-dd HH:mm:ss", null, TimeZone.getTimeZone("CET"));
        
        DateUtil dateUtil2 = new DateUtil();
        dateUtil2.setDatePattern("yyyy-MM-dd HH:mm:ss", null, TimeZone.getTimeZone("GMT"));

        TimeZoneUtil.setDefault(TimeZone.getTimeZone("CET"));
        Timestamp date = DateUtil.getTimestamp(2022, 12, 24);
        TimeZoneUtil.setDefault(null);
                
        Assert.assertEquals("2022-12-24 00:00:00", dateUtil1.format(date));
        Assert.assertEquals("2022-12-23 23:00:00", dateUtil2.format(date));
        
        Date d1 = dateUtil1.parse("2022-12-24 00:00:00");
        Date d2 = dateUtil2.parse("2022-12-23 23:00:00");
        Assert.assertEquals(d1.getTime(), d2.getTime());
        
        Assert.assertEquals("2022-12-24 00:00:00.0", d1.toString());
        Assert.assertEquals("2022-12-23 23:00:00.0", d2.toString());
    }

}	// TestDateUtil
