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

import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sibvisions.util.log.ILogger.LogLevel;

/**
 * Tests {@link LoggerFactory} methods.
 * 
 * @author René Jahn
 * @see LoggerFactory
 */
public class TestLoggerFactory
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
		LoggerFactory.init(null);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Tests {@link LoggerFactory#getLogger()}.
	 */
	@Test
	public void testGetLogger()
	{
        List<ILogger> liLog = LoggerFactory.getLogger();
        
        if (liLog.size() == 1 && !liLog.get(0).getName().equals(getClass().getName()))
        {
            Assert.fail("Logger has logger instances!");
        }
		
		LoggerFactory.getInstance("com.sibvisions.util.log");
		
		Assert.assertEquals(liLog.size() + 1, LoggerFactory.getLogger().size());
	}

	/**
	 * Tests {@link LoggerFactory#getLogger()}.
	 */
	@Test
	public void testGetInstance()
	{
		ILogger log = LoggerFactory.getInstance(getClass());
		
		Assert.assertEquals(getClass().getName(), log.getName());
	}	

	/**
	 * Tests {@link LoggerFactory#getLevel(String)}.
	 */
	@Test
	public void testGetLevel()
	{
		ILogger log = LoggerFactory.getInstance(getClass());
		
		log.setLevel(LogLevel.ERROR);
		
		Assert.assertEquals(LogLevel.ERROR, LoggerFactory.getLevel(getClass().getName()));
	}

	/**
	 * Tests {@link LoggerFactory#setLevel(String, LogLevel)}.
	 */
	@Test
	public void testSetLevel()
	{
		LoggerFactory.setLevel(getClass().getName(), LogLevel.DEBUG);
		
		ILogger log = LoggerFactory.getInstance(getClass());
		
		Assert.assertEquals(LogLevel.DEBUG, log.getLevel());
	}
	
}	// TestLoggerFactory
