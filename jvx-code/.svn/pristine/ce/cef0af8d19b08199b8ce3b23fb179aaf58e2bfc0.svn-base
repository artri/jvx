/*
 * Copyright 2022 SIB Visions GmbH
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
 * 02.04.2022 - [JR] - creation
 */
package com.sibvisions.rad.server.security;

import javax.rad.server.IConfiguration;

import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.log.ILogger;
import com.sibvisions.util.log.ILogger.LogLevel;
import com.sibvisions.util.log.LoggerFactory;

/**
 * The <code>AbstractSecurity</code> offers base logging features.
 * 
 * @author René Jahn
 */
public abstract class AbstractSecurity 
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the logger. */
	private ILogger logger;
	
	/** the list of additional hidden packages. */
	private static ArrayUtil<String> auHiddenPackages = null;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /**
     * Gets the security environment.
     * 
     * @param pConfig the configuration
     * @return the environment e.g. <code>prod</code>
     */
    public static String getEnvironment(IConfiguration pConfig)
    {
    	return pConfig.getProperty("/application/securitymanager/environment");    	
    }
    
    /**
     * Gets whether the environment is production.
     * 
     * @param pConfig the configuration which contains the environment setting
     * @return <code>true</code> if <code>prod</code> as environment is set, <code>false</code> otherwise
     */
    public static boolean isProd(IConfiguration pConfig)
    {
    	return "prod".equals(getEnvironment(pConfig));
    }
    
    /**
     * Gets whether the failure reason for an authentication problem should be hidden. The {@link #getEnvironment(IConfiguration)}
     * will be used to decide if the reason will be hidden. If <code>extern</code> is used, no details will be available. 
     * 
     * @param pConfig the configuration
     * @return <code>true</code> to hide the reason, <code>false</code> otherwise
     */
    protected boolean isHideFailureReason(IConfiguration pConfig)
    {
    	return isProd(pConfig);
    } 
    
	/**
	 * Hides the StackTraceElements of "com.sibvisions.rad.*" when the given exception is a
	 * {@link SecurityException}. If {@link LogLevel#DEBUG} is enabled, the stack won't be
	 * changed.
	 * 
	 * @param pException the occured exception
	 * @return the changed exception
	 */
	public static Throwable prepareException(Throwable pException)
	{
		return prepareException(pException, false);
	}
	
	/**
	 * Hides the StackTraceElements of "com.sibvisions.rad.*" when the given exception is a
	 * {@link SecurityException}. If {@link LogLevel#DEBUG} is enabled, the stack won't be
	 * changed, but it's possible to force changing.
	 * 
	 * @param pException the occured exception
	 * @param pForce force exception hiding
	 * @return the changed exception
	 */
	public static Throwable prepareException(Throwable pException, boolean pForce)
	{
		if (pException instanceof SecurityException)
		{
			if (pForce || !LoggerFactory.getInstance(AbstractSecurity.class).isEnabled(LogLevel.DEBUG))
			{
				SecurityContext sctxt = SecurityContext.getCurrentInstance();
				
				if (sctxt == null || sctxt.isHidePackages())
				{
					ArrayUtil<StackTraceElement> auStack = new ArrayUtil<StackTraceElement>();
					
					StackTraceElement[] stack = pException.getStackTrace();
					
					if (stack != null)
					{
						for (int i = 0; i < stack.length; i++)
						{
							if (!stack[i].getClassName().startsWith("com.sibvisions.rad")
								&& !isHiddenPackage(stack[i].getClassName()))
							{
								auStack.add(stack[i]);
							}
						}
					}
	
					stack = auStack.toArray(new StackTraceElement[auStack.size()]);
					
					pException.setStackTrace(stack);
				}
			}
		}
		
		return pException;
	}
	
	/**
	 * Adds a package name to the hidden package list.
	 * 
	 * @param pPackage the full qualified java package name e.g. com.sibvisions
	 */
	public static void addHiddenPackage(String pPackage)
	{
		if (pPackage != null)
		{
			if (auHiddenPackages == null)
			{
				auHiddenPackages = new ArrayUtil<String>();
			}
			
			auHiddenPackages.add(pPackage);
		}
	}
	
	/**
	 * Removes a package name from the hidden package list.
	 * 
	 * @param pPackage the full qualified java package name e.g. com.sibvisions
	 */
	public static void removeHiddenPackage(String pPackage)
	{
		if (pPackage != null && auHiddenPackages != null)
		{
			auHiddenPackages.remove(pPackage);
			
			if (auHiddenPackages.size() == 0)
			{
				auHiddenPackages = null;
			}
		}
	}
	
	/**
	 * Checks if a class or package name is excluded through the hidden package list.
	 * 
	 * @param pJavaName the full qualified java class or package name e.g. com.sibvisions.rad.IPackageSetup
	 * @return <code>true</code> if the name contains a hidden package name
	 */
	public static boolean isHiddenPackage(String pJavaName)
	{
		if (pJavaName == null || auHiddenPackages == null)
		{
			return false;
		}
		
		for (int i = 0, anz = auHiddenPackages.size(); i < anz; i++)
		{
			if (pJavaName.startsWith(auHiddenPackages.get(i)))
			{
				return true;
			}
		}
		
		return false;
	}    

	/**
	 * Logs debug information.
	 * 
	 * @param pInfo the debug information
	 */
	public void debug(Object... pInfo)
	{
		if (logger == null)
		{
			logger = LoggerFactory.getInstance(getClass());
		}
		
		logger.debug(pInfo);
	}

	/**
	 * Logs information.
	 * 
	 * @param pInfo the information
	 */
	public void info(Object... pInfo)
	{
		if (logger == null)
		{
			logger = LoggerFactory.getInstance(getClass());
		}
		
		logger.info(pInfo);
	}
	
	/**
	 * Logs error information.
	 * 
	 * @param pInfo the error information
	 */
	public void error(Object... pInfo)
	{
		if (logger == null)
		{
			logger = LoggerFactory.getInstance(getClass());
		}

		logger.error(pInfo);
	}
	
}	// AbstractSecurity
