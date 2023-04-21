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
 * 14.01.2015 - [TK] - creation
 */
package com.sibvisions.util.log.log4j;

import java.io.InputStream;

import org.apache.log4j.BasicConfigurator;

import com.sibvisions.util.log.ILogger;
import com.sibvisions.util.log.LoggerFactory;
import com.sibvisions.util.type.ResourceUtil;
import com.sibvisions.util.type.StringUtil;

/**
 * The <code>Log4jLoggerFactory</code> is the {@link LoggerFactory} extension for log4j. It supports
 * configuration via log4j.properties or uses {@link BasicConfigurator} if property file is not
 * available.
 * 
 * @author Thomas Krautinger
 */
public class Log4jLoggerFactory extends LoggerFactory
{	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>Log4jLoggerFactory</code>.
	 * 
	 * @throws Exception if log4j is not available in classpath
	 */
	public Log4jLoggerFactory() throws Exception
	{
		//Let us check if the class is available
		Class.forName("org.apache.log4j.Logger");
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ILogger createLogger(String pName)
	{
		return new Log4jLogger(pName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init()
	{
		InputStream isConfig = ResourceUtil.getResourceAsStream("log4j.properties");
		
		if (isConfig == null)
		{
		    String sConfFile;
		    
		    try
		    {
                sConfFile = System.getProperty("log4j.configurationFile");
		    }
		    catch (Exception e)
		    {
		        sConfFile = null;
		    }

            if (!StringUtil.isEmpty(sConfFile))
            {
                isConfig = ResourceUtil.getResourceAsStream(sConfFile);
            }
            else
            {
                isConfig = ResourceUtil.getResourceAsStream("log4j2.xml");
            }
		}
		
		if (isConfig == null)
		{
			BasicConfigurator.configure();
		}
	}

}   // Log4jLoggerFactory
