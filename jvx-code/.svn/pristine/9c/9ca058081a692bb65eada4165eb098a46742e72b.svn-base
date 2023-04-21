/*
 * Copyright 2015 SIB Visions GmbH
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
 * 14.01.2015 - [JR] - creation
 */
package com.sibvisions.util.log.log4j;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sibvisions.util.log.AbstractLogger;
import com.sibvisions.util.log.ILogger;
import com.sibvisions.util.log.LoggerFactory;

/**
 * Tests {@link Log4jLogger} methods.
 * 
 * @author René Jahn
 * @see Log4jLogger
 */
public class TestLog4jLogger extends AbstractLogger
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Initializes the logger.
	 */
	@BeforeClass
	public static void beforeClass()
	{
		Log4jLoggerFactory.init(Log4jLoggerFactory.class.getName());
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	protected ILogger getLogger()
	{
        ILogger log = LoggerFactory.getInstance(getClass().getName());
        
        Assert.assertSame(Log4jLogger.class, log.getClass());
        
        return log;
	}
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Simple debug log message via log4j.
	 */
	@Test
	public void testDebug()
	{
	    LoggerFactory.getInstance("testLogger").debug("JUnit Test!");
	}

}	// TestLog4jLogger
