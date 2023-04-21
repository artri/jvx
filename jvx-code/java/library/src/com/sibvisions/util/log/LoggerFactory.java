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
 * 22.04.2009 - [JR] - creation
 * 08.05.2009 - [JR] - getInstance: init if no factory is set
 * 13.12.2011 - [JR] - set/getLevel with Class parameter
 * 16.01.2015 - [JR] - #1231: check "real" factory class names
 * 06.05.2015 - [JR] - #1380: changed destroy from package private to public
 * 29.10.2015 - [JR] - #1503: isInitialized introduced
 * 16.08.2016 - [JR] - #1673: read system property for the default factory
 */
package com.sibvisions.util.log;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.WeakHashMap;

import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.log.ILogger.LogLevel;
import com.sibvisions.util.log.jdk.JdkLoggerFactory;
import com.sibvisions.util.type.CommonUtil;
import com.sibvisions.util.type.ResourceUtil;
import com.sibvisions.util.type.StringUtil;

/**
 * The <code>LoggerFactory</code> is a factory for creating {@link ILogger} instances,
 * for the configured log technology. 
 * 
 * To use the configured log factory implement the following call in your starter:
 * 
 * <code>
 * LoggerFactory.init("com.package.log.MyLoggerFactory");
 * </code>
 * Later you can create an {@link ILogger} instance and use it as follows:
 * <code>
 * ILogger logger = LoggerFactory.getInstance("com.project.MyWindow");<br>
 * 
 * logger.debug("My first log message!");
 * </code>.
 * 
 * @author René Jahn
 */
public abstract class LoggerFactory
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** the constant name for the default logger factory implementation. */
    public static final String LOGFACTORY_DEFAULT = "LoggerFactory.default";

    
	/** the logger factory implementation. */
	private static LoggerFactory factory = null;
	
	/** the cache for all created logger. */
	private static WeakHashMap<String, ILogger> whmLogger = null;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Invisible constructor, because the <code>LoggerFactory</code> is a utility class.
	 */
	protected LoggerFactory()
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates an <code>ILogger</code>.
	 * 
	 * @param pName the name for which a logger should be returned/created
	 * @return the {@link ILogger} implementation 
	 */
	public abstract ILogger createLogger(String pName);
	
	/**
	 * Initializes the factory instance.
	 */
	public abstract void init();
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of {@link LoggerFactory}. If the <code>pClassName</code> was not
	 * found then the {@link JdkLoggerFactory} will be instantiated.
	 * 
	 * @param pClassName the log factory implementation
	 */
	public static void init(String pClassName)
	{
	    //#1231
	    //create a class because it's possible that the given class was not found -> the security check should
	    //use the "correct" instance for comparison
	    LoggerFactory factoryTemp;
	    
	    try
        {
            factoryTemp = (LoggerFactory)Class.forName(pClassName).newInstance();
        }
        catch (Exception e)
        {
        	String sFactory = System.getProperty(LOGFACTORY_DEFAULT);
        	
        	if (StringUtil.isEmpty(sFactory))
        	{
        		InputStream stream = ResourceUtil.getResourceAsStream("loggerfactory.properties");
        	
	        	if (stream != null)
	        	{
	        		Properties prop = new Properties();
	        		
	        		try
	        		{
	        			prop.load(stream);
	        			
	        			sFactory = (String)prop.get("default.class");
	        		}
	        		catch (Exception ex)
	        		{
	        			ex.printStackTrace();
	        		}
	        		finally
	        		{
	        			CommonUtil.close(stream);
	        		}
	        	}
        	}
        	
            try
            {
				factoryTemp = (LoggerFactory)Class.forName(sFactory).newInstance();
            }
            catch (Exception ex)
            {
                //use the default factory
                factoryTemp = new JdkLoggerFactory();
            }
        }
	    
	    //don't allow different log factories
		if (factory != null) 
		{
			if (!factory.getClass().getName().equals(factoryTemp.getClass().getName()))
			{
				throw new RuntimeException("The log factory was already initialized: " + factory.getClass().getName());
			}
			
			//use the current factory!
			return;
		}
		
        //initialize the factory instance
	    factoryTemp.init();

	    factory = factoryTemp;
	}
	
	/**
	 * Gets whether this factory has been initialized.
	 * 
	 * @return <code>true</code> if already initialized, <code>false</code> otherwise
	 */
	public static boolean isInitialized()
	{
	    return factory != null && factory.isInitDone();
	}
	
	/**
	 * Destroys the internal cache and logging configuration.
	 */
	public static void destroy()
	{
	    factory = null;
	    whmLogger = null;
	}
	
	/**
	 * Construct (if necessary) and return an {@link ILogger} instance, using the factory's
	 * current set of configuration attributes.
	 * 
	 * @param pClass the class for which a logger should be returned/created
	 * @return the logger for the specified class
	 * @throws RuntimeException if the log factory was not initialized
	 * @see #getInstance(String)
	 */
	public static ILogger getInstance(Class pClass)
	{
		return getInstance(pClass.getName()); 
	}

	/**
	 * Construct (if necessary) and return an {@link ILogger} instance, using the factory's
	 * current set of configuration attributes.
	 * 
	 * @param pName the name for which a logger should be returned/created
	 * @return the logger for the specified class
	 * @throws RuntimeException if the log factory was not initialized
	 */
	public static synchronized ILogger getInstance(String pName)
	{
		if (factory == null)
		{
			//use the default factory
			init(null);
		}
	
		ILogger log;
		
		//null is not allowed -> use empty string
		if (pName == null)
		{
			pName = "";
		}
		
		if (whmLogger == null)
		{
			whmLogger = new WeakHashMap<String, ILogger>();
			log = null;
		}
		else 
		{
			log = whmLogger.get(pName);
		}
		
		if (log == null)
		{
			log = factory.createLogger(pName);
			
			whmLogger.put(pName, log);
		}
		
		return log;
	}
	
	/**
	 * Sets the level of a specific logger.
	 *  
	 * @param pClass the class
	 * @param pLevel the log level
	 */
	public static void setLevel(Class pClass, LogLevel pLevel)
	{
		setLevel(pClass.getName(), pLevel); 
	}
	
	/**
	 * Sets the level of a specific logger.
	 *  
	 * @param pName the logger name
	 * @param pLevel the log level
	 */
	public static void setLevel(String pName, LogLevel pLevel)
	{
		getInstance(pName).setLevel(pLevel);
	}
	
	/**
	 * Gets the level of a specific logger.
	 * 
	 * @param pClass the class
	 * @return the log level
	 */
	public static LogLevel getLevel(Class pClass)
	{
		return getLevel(pClass.getName());
	}
	
	/**
	 * Gets the level of a specific logger.
	 * 
	 * @param pName the logger name
	 * @return the log level
	 */
	public static LogLevel getLevel(String pName)
	{
		return getInstance(pName).getLevel();
	}
	
	/**
	 * Gets all created logger instances.
	 * 
	 * @return an enumeration with {@link ILogger}s
	 */
	public static List<ILogger> getLogger()
	{
		ArrayUtil<ILogger> log = new ArrayUtil<ILogger>();

		if (whmLogger != null)
		{
			log.addAll(whmLogger.values());
		}
		
		return log;
	}
	
	/**
	 * Gets whether factory is initialized.
	 * 
	 * @return <code>true</code> in any case because only {@link #init()} creates a new factory instance
	 */
	protected boolean isInitDone()
	{
	    return factory != null;
	}
	
}	// LoggerFactory
