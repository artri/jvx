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
 * 26.04.2009 - [JR] - creation
 * 29.12.2009 - [JR] - publish implemented
 */
package com.sibvisions.util.log.jdk;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import com.sibvisions.util.log.ILogger;

/**
 * The <code>JdkStandardLogger</code> is a special logger for the case that a 
 * security manager denies the access to the log properties.<br>
 * The log will be written to <code>System.out</code> and the {@link JdkLineFormatter}
 * will be used.
 * 
 * @author René Jahn
 */
public class JdkStandardLogger implements ILogger
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the configured log levels. */
	private static WeakHashMap<String, LogLevel> htLogLevels = new WeakHashMap<String, LogLevel>(); 
	
	/** the log formatter. */
	private static JdkLineFormatter formatter = null;
	
	/** the name of the logger. */
	private String name;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>JdkStandardLogger</code>.
	 * 
	 * @param pName the name for the logger (the class name is recommended)
	 */
	public JdkStandardLogger(String pName)
	{
		name = pName;
		
		//search the level from an already defined logger
		LogLevel level = null;
		
		int iPos = pName.lastIndexOf('.');
		
		while (iPos >= 0 && level == null)
		{
			level = htLogLevels.get(pName.substring(0, iPos));
			
			iPos = pName.lastIndexOf('.', iPos - 1);
		}

		setLevel(level);
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
	    LogLevel level = pLevel;
	    
	    if (level == null)
	    {
	        level = LogLevel.OFF;
	    }
	    
		htLogLevels.put(name, level);
		
		//update all sub-logger levels
		
		String sSearchName = name + ".";
		
		for (Map.Entry<String, LogLevel> entry : htLogLevels.entrySet())
		{
			if (entry.getKey() != name 
				&& entry.getKey().startsWith(sSearchName))
			{
				entry.setValue(level);
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isLevelSet()
	{
		return htLogLevels.get(name) != null;
	}

	/**
	 * {@inheritDoc}
	 */
	public LogLevel getLevel()
	{
		return htLogLevels.get(name);
	}

	/**
	 * {@inheritDoc}
	 */
	public void debug(Object... pInfo)
	{
		logWithCaller(LogLevel.DEBUG, pInfo);
	}

	/**
	 * {@inheritDoc}
	 */
	public void info(Object... pInfo)
	{
		logWithCaller(LogLevel.INFO, pInfo);
	}

	/**
	 * {@inheritDoc}
	 */
	public void error(Object... pInfo)
	{
		logWithCaller(LogLevel.ERROR, pInfo);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isEnabled(LogLevel pLevel)
	{
		LogLevel level = getLevel();
		
		return level != LogLevel.OFF && pLevel.ordinal() >= level.ordinal();
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
    private final void logWithCaller(LogLevel pLogLevel, Object... pInfo)
    {
    	LogLevel level = getLevel();
    	
    	//don't call another method -> save calls
    	if (level != LogLevel.OFF 
    		&& pLogLevel.ordinal() >= level.ordinal())
    	{
	    	String sClass = null;
	    	String sMethod = null;
	
	    	StackTraceElement[] ste = new Throwable().getStackTrace();
	
	    	
	    	//try to detect the call-info through the class name, if matching
			for (int i = 0; i < ste.length; i++)
			{
				if (name.equals(ste[i].getClassName()))
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
					if (!ste[i].getClassName().equals(sClassName))
					{
						sClass = ste[i].getClassName();
						sMethod = ste[i].getMethodName() + " (Line: " + ste[i].getLineNumber() + ")";
	
						i = ste.length;
					}
				}
			}

			//Translate the log level
			Level lvlTranslated;
			
			switch (pLogLevel)
			{
				case ALL:
					lvlTranslated = Level.ALL;
					break;
				case DEBUG:
					lvlTranslated = Level.FINE;
					break;
				case INFO:
					lvlTranslated = Level.INFO;
					break;
				case ERROR:
					lvlTranslated = Level.SEVERE;
					break;
				default:
					lvlTranslated = Level.OFF;
			}
			
			LogRecord lr = new LogRecord(lvlTranslated, JdkLineFormatter.concat(pInfo));
			lr.setSourceClassName(sClass);
			lr.setSourceMethodName(sMethod);

			publish(lr);
		}
    }	

    /**
     * Prints out the log record to System.out.
     * 
     * @param pRecord the log record
     */
    protected void publish(LogRecord pRecord)
    {
    	if (formatter == null)
    	{
    		formatter = new JdkLineFormatter();
    	}
    	
		System.out.print(formatter.format(pRecord));
    }
    
}	// JdkStandardLogger
