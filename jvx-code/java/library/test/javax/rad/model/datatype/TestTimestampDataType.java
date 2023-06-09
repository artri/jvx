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

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Test;

import com.sibvisions.util.type.LocaleUtil;

/**
 * Tests all Functions of {@link TimestampDataType}.
 * 
 * @author Roland H�rmann
 * @see com.sibvisions.rad.model.mem.DataTypees.TimestampDataType
 */
public class TestTimestampDataType
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Test some base functions in the {@link TimestampDataType}.
	 * 
	 * @throws Exception if timestamp conversion fails
	 */	
	@Test
	public void testBaseFunctions() throws Exception
	{
		Locale loc = LocaleUtil.getDefault();
		
		try
		{
			LocaleUtil.setDefault(new Locale("en"));
			
			// test new TimestampDataType
			TimestampDataType sctTimestampDataType = new TimestampDataType();
			
			Assert.assertEquals(new Timestamp(123), sctTimestampDataType.convertAndCheckToTypeClass(new Long(123)));
			Assert.assertEquals(null, sctTimestampDataType.convertAndCheckToTypeClass(null));
			Assert.assertEquals(Timestamp.valueOf("2010-05-07 11:08:00.000000000"), sctTimestampDataType.convertAndCheckToTypeClass("2010-05-07 11:08"));
			
			Date dNow = new Date();
			Assert.assertEquals(new Timestamp(dNow.getTime()), sctTimestampDataType.convertAndCheckToTypeClass(dNow));
			
			// test convert from/to storage
			Timestamp oObject = new Timestamp(dNow.getTime());
			
			// test clone 
			Assert.assertEquals(sctTimestampDataType.getSize(), sctTimestampDataType.clone().getSize());
			
			// test DateFormat, convertToString()
			System.out.println(sctTimestampDataType.convertToString(oObject));
			sctTimestampDataType.setDateFormat(((SimpleDateFormat)DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT)).toPattern());
			System.out.println(sctTimestampDataType.convertToString(oObject));

			LocaleUtil.setDefault(new Locale("de"));
			
			sctTimestampDataType.setDateFormat(null);

			Timestamp timestamp = (Timestamp)sctTimestampDataType.convertToTypeClass("12. M�rz 2013");
			
			Assert.assertEquals(1363042800000L, timestamp.getTime());
		}
		finally
		{
			LocaleUtil.setDefault(loc);
		}
	}

    /**
     * Test some base functions in the {@link TimestampDataType}.
     * 
     * @throws Exception if timestamp conversion fails
     */ 
    @Test
    public void testFractionalSeconds() throws Exception
    {
        TimestampDataType dt = new TimestampDataType();
        dt.setFractionalSecondsPrecision(0);
        
        Timestamp stamp = new Timestamp(System.currentTimeMillis());
        
        Timestamp converted = (Timestamp)dt.convertToTypeClass(stamp);
        
        Assert.assertEquals(0, converted.getNanos());
    }
	
} // TestTimestampDataType
