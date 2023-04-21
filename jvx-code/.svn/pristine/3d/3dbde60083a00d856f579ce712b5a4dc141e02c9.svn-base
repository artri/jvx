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
 * 10.02.2023 - [HM] - creation
 */
package com.sibvisions.util.type;

import java.util.Calendar;
import java.util.TimeZone;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests {@link TestTimeZoneUtil} methods.
 * 
 * @author Martin Handsteiner
 * @see DateUtil
 */
public class TestTimeZoneUtil
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
	public void testInstances() throws Exception
	{
	    TimeZone zone1 = TimeZoneUtil.getDefault();
        TimeZone zone2 = TimeZoneUtil.getDefault();

        Assert.assertEquals(System.identityHashCode(zone1), System.identityHashCode(zone2));
	    
        Calendar cal1 = TimeZoneUtil.getCalendar(null, null);
        Calendar cal2 = TimeZoneUtil.getCalendar(null, null);
        
        Assert.assertEquals(System.identityHashCode(cal1), System.identityHashCode(cal2));
	}

}	// TestTimeZoneUtil
