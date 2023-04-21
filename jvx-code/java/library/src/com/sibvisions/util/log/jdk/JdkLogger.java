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
 * 24.04.2009 - [JR] - creation
 * 05.05.2009 - [JR] - getLevel: endless loop [BUGFIX]
 * 30.09.2009 - [JR] - setLevel: update sub logger, if they have their own loglevel set
 * 05.05.2010 - [JR] - publish implemented
 */
package com.sibvisions.util.log.jdk;

import java.util.Enumeration;
import java.util.logging.Filter;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.log.ILogger;

/**
 * The <code>JdkLogger</code> is the {@link ILogger} for the java logging API.
 * The logger initializes the jdk logger lazy. Thats means that the {@link Logger}
 * instance will be instantiated when it will be used, e.g. through log output.
 * 
 * @author René Jahn
 */
public class JdkLogger implements ILogger
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** Wrapper function to ignore, to get the details caller position from stacktrace. */
	private static final String[] METHODS_TO_IGNORE = {"debug", "info", "error"};
	
	/** the name of the logger. */
	private String name; 
	
	/** the java logger. */
	private Logger log;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>JdkLogger</code>.
	 * 
	 * @param pName the name for the logger (the class name is recommended)
	 */
	public JdkLogger(String pName)
	{
		name = pName;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setLevel(LogLevel pLevel)
	{
		init();

		Level lvlNew = translate(pLevel); 
		
		log.setLevel(lvlNew);
		
		String sSearchName = name + ".";

		//update all sub-logger levels

		String sSubName;
		Logger logSub;
		
		//Use the LogManager to get all implicit created logger
		for (Enumeration<String> en = LogManager.getLogManager().getLoggerNames(); en.hasMoreElements();)
		{
			sSubName = en.nextElement();
			
			if (!sSubName.equals(name) && sSubName.startsWith(sSearchName))
			{
				logSub = Logger.getLogger(sSubName);
				
				if (logSub.getLevel() != null)
				{
					logSub.setLevel(lvlNew);
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
		
		Logger logParent = log.getParent();
				
		while (lvl == null && logParent != null)
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
		else if (lvl.equals(Level.FINE) 
				 || lvl.equals(Level.FINER)
				 || lvl.equals(Level.FINEST))
		{
			return LogLevel.DEBUG;
		}
		else if (lvl.equals(Level.INFO)
				 || lvl.equals(Level.CONFIG)
				 || lvl.equals(Level.WARNING))
		{
			return LogLevel.INFO;
		}
		else if (lvl.equals(Level.SEVERE))
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
		logWithCaller(Level.FINE, info);
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
		logWithCaller(Level.SEVERE, info);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isEnabled(LogLevel pLevel)
	{
		init();
		
		//Java logging: OFF returns true if ALL is enabled!
		return pLevel != LogLevel.OFF && log.isLoggable(translate(pLevel));
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
    	
		if (log.isLoggable(pLogLevel))
		{
	    	String sClass = null;
	    	String sMethod = null;
	
	    	StackTraceElement[] ste = new Throwable().getStackTrace();
	
	    	//try to detect the call-info through the class name, if matching
			for (int i = 0; i < ste.length; i++)
			{
				if (name.equals(ste[i].getClassName()) && !ArrayUtil.contains(METHODS_TO_IGNORE, ste[i].getMethodName()))
				{
					sClass = ste[i].getClassName();
					sMethod = ste[i].getMethodName() + " (Line: " + ste[i].getLineNumber() + ")";
	
					i = ste.length;
				}
			}
			
			//use the log caller
			if (sClass == null && ste.length > 2)
			{
				String sClassName = getClass().getName();
				for (int i = 0; i < ste.length; i++)
				{
					if (!ste[i].getClassName().equals(sClassName) && !ArrayUtil.contains(METHODS_TO_IGNORE, ste[i].getMethodName()))
					{
						sClass = ste[i].getClassName();
						sMethod = ste[i].getMethodName() + " (Line: " + ste[i].getLineNumber() + ")";
	
						i = ste.length;
					}
				}
			}
			
			LogRecord lr = new LogRecord(pLogLevel, JdkLineFormatter.concat(pInfo));
			lr.setSourceClassName(sClass);
			lr.setSourceMethodName(sMethod);

			//we waste some time to create the LogRecord, but it's better to use a LogRecord for 
			//inherited classes which overrides publish!
			publish(lr);
		}
    }
    
    /**
     * Prints out the log record.
     * 
     * @param pRecord the log record
     */
	@SuppressWarnings("deprecation")
	protected void publish(LogRecord pRecord)
    {
		//the logger checks if it's possible to log with the desired loglevel
		log.logrb(pRecord.getLevel(), pRecord.getSourceClassName(), pRecord.getSourceMethodName(), pRecord.getResourceBundleName(), pRecord.getMessage());
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
				return Level.FINE;
			case INFO:
				return Level.INFO;
			case ERROR:
				return Level.SEVERE;
			default:
				return Level.OFF;
		}
    }
    
    /**
     * Initializes the jdk logger.
     */
    private void init()
    {
		if (log == null)
		{
			log = Logger.getLogger(name);
			
			Logger logParent = log.getParent();
			
			Handler[] handlers;
			
			LogManager lmanager = LogManager.getLogManager();
			
			String sFormatter;
			String sFilter;
			
			//ensure that the configured formatters and filters are used as defined!
			while (logParent != null)
			{
				handlers = logParent.getHandlers();
				for (int i = 0, anz = handlers.length; i < anz; i++)
				{
					sFormatter = lmanager.getProperty(handlers[i].getClass().getName() + ".formatter");
					
					if (sFormatter != null)
					{
						if (!handlers[i].getFormatter().getClass().getName().equals(sFormatter))
						{
							try
							{
								handlers[i].setFormatter((Formatter)Class.forName(sFormatter).newInstance());
							}
							catch (Exception e)
							{
								//don't change the formatter
							}
						}
					}
					
					sFilter = lmanager.getProperty(handlers[i].getClass().getName() + ".filter");
					
					if (sFilter != null)
					{
						if (handlers[i].getFilter() != null 
							&& !handlers[i].getFilter().getClass().getName().equals(sFilter))
						{
							try
							{
								handlers[i].setFilter((Filter)Class.forName(sFilter).newInstance());
							}
							catch (Exception e)
							{
								//don't change the formatter
							}
						}
					}
				}
				
				logParent = logParent.getParent();
			}
		}
    }

}	// JdkLogger
