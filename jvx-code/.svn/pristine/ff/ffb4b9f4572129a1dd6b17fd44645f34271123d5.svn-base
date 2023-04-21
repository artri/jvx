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
 * 04.05.2009 - [JR] - creation
 */
package com.sibvisions.util.log;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sibvisions.util.log.ILogger.LogLevel;

/**
 * Base class for {@link ILogger} tests.
 * 
 * @author René Jahn
 * @see ILogger
 */
public abstract class AbstractLogger
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Uninitializes the logger.
     */
    @BeforeClass
    public static void before()
    {
        LoggerFactory.destroy();
    }
    
    /**
     * Initializes the logger before testing it.
     */
    @Before
    public void beforeTest()
    {
        getLogger().setLevel(LogLevel.ERROR);
    }
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the logger for this class.
	 * 
	 * @return an {@link ILogger} implementation
	 */
	protected abstract ILogger getLogger();
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Tests {@link ILogger#isEnabled(com.sibvisions.util.log.ILogger.LogLevel)}.
	 */
	@Test
	public void testIsEnabled()
	{
		ILogger log = getLogger();
		
		try
		{
    		log.setLevel(LogLevel.OFF);
    		
    		Assert.assertFalse("Off is enabled!", log.isEnabled(LogLevel.OFF));
    		Assert.assertFalse("All is enabled!", log.isEnabled(LogLevel.ALL));
    		Assert.assertFalse("Debug is enabled!", log.isEnabled(LogLevel.DEBUG));
    		Assert.assertFalse("Info is enabled!", log.isEnabled(LogLevel.INFO));
    		Assert.assertFalse("Error is enabled!", log.isEnabled(LogLevel.ERROR));
    		
    		log.setLevel(LogLevel.ALL);
    		
    		Assert.assertFalse("Off is enabled!", log.isEnabled(LogLevel.OFF));
    		Assert.assertTrue("All is disabled!", log.isEnabled(LogLevel.ALL));
    		Assert.assertTrue("Debug is disabled!", log.isEnabled(LogLevel.DEBUG));
    		Assert.assertTrue("Info is disabled!", log.isEnabled(LogLevel.INFO));
    		Assert.assertTrue("Error is disabled!", log.isEnabled(LogLevel.ERROR));
    
    		log.setLevel(LogLevel.DEBUG);
    		
    		Assert.assertFalse("Off is enabled!", log.isEnabled(LogLevel.OFF));
    		Assert.assertFalse("All is enabled!", log.isEnabled(LogLevel.ALL));
    		Assert.assertTrue("Debug is disabled!", log.isEnabled(LogLevel.DEBUG));
    		Assert.assertTrue("Info is disabled!", log.isEnabled(LogLevel.INFO));
    		Assert.assertTrue("Error is disabled!", log.isEnabled(LogLevel.ERROR));
    
    		log.setLevel(LogLevel.INFO);
    		
    		Assert.assertFalse("Off is enabled!", log.isEnabled(LogLevel.OFF));
    		Assert.assertFalse("All is enabled!", log.isEnabled(LogLevel.ALL));
    		Assert.assertFalse("Debug is enabled!", log.isEnabled(LogLevel.DEBUG));
    		Assert.assertTrue("Info is disabled", log.isEnabled(LogLevel.INFO));
    		Assert.assertTrue("Error is disabled!", log.isEnabled(LogLevel.ERROR));
    
    		log.setLevel(LogLevel.ERROR);
    		
    		Assert.assertFalse("Off is enabled!", log.isEnabled(LogLevel.OFF));
    		Assert.assertFalse("All is enabled!", log.isEnabled(LogLevel.ALL));
    		Assert.assertFalse("Debug is enabled!", log.isEnabled(LogLevel.DEBUG));
    		Assert.assertFalse("Info is enabled", log.isEnabled(LogLevel.INFO));
    		Assert.assertTrue("Error is disabled!", log.isEnabled(LogLevel.ERROR));
		}
		finally
		{
		    log.setLevel(null);
		    
		    //default level (logging.properties)
		    Assert.assertEquals(LogLevel.OFF, log.getLevel());
		}
	}
	
	/**
	 * Tests {@link ILogger#getLevel()}.
	 */
	@Test
	public void testGetLevel()
	{
		ILogger log = getLogger();
		
        //default level (logging.properties)
		Assert.assertEquals(LogLevel.ERROR, log.getLevel());
	}
	
}	// TestLogger
