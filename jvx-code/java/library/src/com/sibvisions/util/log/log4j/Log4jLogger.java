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

import java.util.Enumeration;

import org.apache.log4j.Category;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.sibvisions.util.log.ILogger;
import com.sibvisions.util.log.jdk.JdkLineFormatter;

/**
 * The <code>Log4jLogger</code> is the {@link ILogger} implementation for log4j.
 * 
 * @author Thomas Krautinger
 */
public class Log4jLogger implements ILogger
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the name of the logger. */
	private String sLoggerName; 
	
	/** the log4j logger. */
	private Logger log;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>Log4jLogger</code>.
	 * 
	 * @param pName the name for the logger (the class name is recommended)
	 */
	public Log4jLogger(String pName)
	{
		sLoggerName = pName;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public String getName()
	{
		return sLoggerName;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setLevel(LogLevel pLevel)
	{
		init();

		Level level = translate(pLevel); 

		log.setLevel(level);
		
		//update all sub-logger levels
		
		String sSearchName = sLoggerName + ".";
		Logger logger;
		
		for (Enumeration<?> loggers = LogManager.getCurrentLoggers(); loggers.hasMoreElements();)
		{
			logger = (Logger)loggers.nextElement();
			
			if (!logger.getName().equals(sLoggerName)
				&& logger.getName().startsWith(sSearchName))
			{
				if (logger.getLevel() != null)
				{
					logger.setLevel(level);
				}
			}
		}
	}
	/**
	 * {@inheritDoc}
	 */
	public boolean isLevelSet()
	{
		init();
		
		return log.getLevel() != null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public LogLevel getLevel()
	{
		init();
		
		Level lvl = log.getLevel();
		
		Category logParent = log.getParent();
				
		while (lvl == null
			   && logParent != null)
		{
			lvl = logParent.getLevel();
			
			logParent = logParent.getParent();
		}
		
		if (lvl == null)
		{
			return LogLevel.OFF;
		}
		else if (lvl.equals(Level.OFF))
		{
			return LogLevel.OFF;
		}
		else if (lvl.equals(Level.ALL))
		{
			return LogLevel.ALL;
		}
		else if (lvl.equals(Level.TRACE) 
				 || lvl.equals(Level.DEBUG))
		{
			return LogLevel.DEBUG;
		}
		else if (lvl.equals(Level.INFO)
				 || lvl.equals(Level.WARN))
		{
			return LogLevel.INFO;
		}
		else if (lvl.equals(Level.ERROR)
				 || lvl.equals(Level.FATAL))
		{
			return LogLevel.ERROR;
		}
		
		return LogLevel.OFF;
	}

	/**
	 * {@inheritDoc}
	 */
	public void debug(Object... info)
	{
		logWithCaller(Level.DEBUG, info);
	}

	/**
	 * {@inheritDoc}
	 */
	public void info(Object... info)
	{
		logWithCaller(Level.INFO, info);
	}

	/**
	 * {@inheritDoc}
	 */
	public void error(Object... info)
	{
		logWithCaller(Level.ERROR, info);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isEnabled(LogLevel pLevel)
	{
		init();
		
		return pLevel != LogLevel.OFF
			   && log.isEnabledFor(translate(pLevel));
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
     * Logs a message with caller class, method and line number. 
     * <p>
     * @param pLogLevel log level
     * @param pInfo the log information
     */
    private final void logWithCaller(Level pLogLevel, Object... pInfo)
    {
    	init();
    	
		log.log(pLogLevel, JdkLineFormatter.concat(pInfo));
    }
    
    /**
     * Translates the given {@link LogLevel} to the appropriate {@link Level}.
     * 
     * @param pLevel the log level to translate
     * @return the {@link Level}
     */
    private Level translate(LogLevel pLevel)
    {
    	if (pLevel == null)
    	{
    		return Level.OFF;
    	}
    	
		switch (pLevel)
		{
			case ALL:
				return Level.ALL;
			case DEBUG:
				return Level.DEBUG;
			case INFO:
				return Level.INFO;
			case ERROR:
				return Level.ERROR;
			default:
				return Level.OFF;
		}
    }
    
    /**
     * Initializes the internal logger instancce.
     */
    private void init()
    {
		if (log == null)
		{
			log = Logger.getLogger(sLoggerName);
		}
    }
    
}   // Log4jLogger
