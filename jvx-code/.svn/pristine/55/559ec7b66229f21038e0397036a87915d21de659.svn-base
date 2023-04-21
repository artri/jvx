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
 * 05.05.2009 - [JR] - creation
 */
package com.sibvisions.util.log.jdk;

import org.junit.Assert;
import org.junit.Test;

import com.sibvisions.util.log.AbstractLogger;
import com.sibvisions.util.log.ILogger;
import com.sibvisions.util.log.ILogger.LogLevel;

/**
 * Tests {@link JdkStandardLogger} methods.
 * 
 * @author René Jahn
 * @see JdkStandardLogger
 */
public class TestJdkStandardLogger extends AbstractLogger
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	protected ILogger getLogger()
	{
		return new JdkStandardLogger(getClass().getName());
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Tests {@link ILogger#getLevel()}.
	 */
	@Test
	public void testGetLevel()
	{
		ILogger log = getLogger();
		
		//the security logger is always OFF
		Assert.assertEquals(LogLevel.OFF, log.getLevel());
	}

}	// TestJdkStandardLogger
