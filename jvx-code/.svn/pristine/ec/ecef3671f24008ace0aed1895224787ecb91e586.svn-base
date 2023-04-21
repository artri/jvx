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
 * 01.08.2011 - [JR] - init() ensurs root-logger level
 * 29.10.2015 - [JR] - #1503: isInitDone implemented
 * 26.11.2016 - [JR] - #1710: support config.file as system property
 */
package com.sibvisions.util.log.jdk;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import com.sibvisions.util.log.ILogger;
import com.sibvisions.util.log.LoggerFactory;
import com.sibvisions.util.type.ResourceUtil;

/**
 * The <code>JdkLoggerFactory</code> is the {@link LoggerFactory} implementation
 * for the java logging API.
 * 
 * @author René Jahn
 */
public class JdkLoggerFactory extends LoggerFactory
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** marker for initialized factory. */
	private boolean bInit;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ILogger createLogger(String pName)
	{
		if (bInit)
		{
		 	return new JdkLogger(pName);
		}
		else
		{
			return new JdkStandardLogger(pName);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init()
	{
		bInit = false;
		
		//loads the library specific configuration
		try
		{
		    //#1710
		    try
		    {
		        String sConfig = System.getProperty("java.util.logging.config.file");
		     
		        if (sConfig != null)
		        {
		            //already initialized
		            bInit = true;
		        }
		    }
		    catch (Exception e)
		    {
		        //nothing to be done
		    }

		    InputStream isProp = ResourceUtil.getResourceAsStream("logging.properties");
			
			if (isProp != null)
			{
				try
				{
//					Properties prop = new Properties();
//					prop.load(isProp);
//					
//					String sKey;
//					String sName;
//					
//					for (Entry<Object, Object> entry : prop.entrySet())
//					{
//						sKey = (String)entry.getKey();
//						
//						if (sKey.endsWith(".level"))
//						{
//							sName = sKey.substring(0, sKey.length() - 6);
//
//							try
//							{
//								Logger log = LogManager.getLogManager().getLogger(sName);
//
//								if (log == null)
//								{
//									log = Logger.getLogger(sName);
//								}
//								
//								if (log != null)
//								{
//									log.setLevel(Level.parse((String)entry.getValue()));
//								}
//							}
//							catch (Exception e)
//							{
//								e.printStackTrace();
//							}
//						}
//					}
					
					LogManager.getLogManager().readConfiguration(new BufferedInputStream(isProp));
					
					String sValue = LogManager.getLogManager().getProperty(".level");
					
					//be sure that the root level from our configuration file is set!
					try
					{
						Level lvl = Level.parse(sValue);
						
						Logger lgRoot = Logger.getLogger("");
						
						if (lgRoot != null && lvl != null)
						{
							if (lgRoot.getLevel() != lvl)
							{
								lgRoot.setLevel(lvl);
							}
						}
					}
					catch (Exception e)
					{
						//nothing to be done
					}
				}
				finally
				{
					try
					{
						isProp.close();
					}
					catch (Exception e)
					{
						//nothing to be done
					}
				}
				
				bInit = true;
			}
		}
		catch (SecurityException se)
		{
			handleException(se);
		}
		catch (IOException e)
		{
			handleException(e);
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
     * Returns whether the log manager is initialized through a property file.
     * 
     * @return <code>true</code> if the logging.properties was used to init the log manager
     */
	@Override
    protected boolean isInitDone()
    {
        return bInit && super.isInitDone();
    }
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Handle an internal exception.
	 * 
	 * @param pException the occured exception
	 */
	protected void handleException(Exception pException)
	{
		if (pException instanceof IOException)
		{
			pException.printStackTrace();
		}
	}

}	// JdkLoggerFactory
