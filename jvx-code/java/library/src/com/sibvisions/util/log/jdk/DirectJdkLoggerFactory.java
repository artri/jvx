/*
 * Copyright 2020 SIB Visions GmbH
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
 * 30.01.2020 - [JR] - creation
 */
package com.sibvisions.util.log.jdk;

import com.sibvisions.util.log.ILogger;
import com.sibvisions.util.log.LoggerFactory;

/**
 * The <code>DirectJdkLoggerFactory</code> does no initialization at all. It simply creates a new wrapped jdk logger.
 * 
 * @author René Jahn
 */
public class DirectJdkLoggerFactory extends LoggerFactory
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ILogger createLogger(String pName)
	{
	 	return new JdkLogger(pName);
	}
	
	@Override
	public void init()
	{
		//nothing to be done
	}

}	// DirectJdkLoggerFactory
