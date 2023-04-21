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
 * 15.04.2009 - [JR] - creation
 */
package com.sibvisions.util.log;

/**
 * The <code>ILogger</code> defines methods for logging within an application. 
 * 
 * @author René Jahn
 */
public interface ILogger
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Type definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the log level enum. */
	public enum LogLevel
	{
		/** all valid log levels. */
		OFF, ALL, DEBUG, INFO, ERROR 
	};
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the logger name.
	 * 
	 * @return the name
	 */
	public String getName();
	
	/** 
	 * Sets the log level. 
	 * 
	 * @param pLevel the log level
	 */
	public void setLevel(LogLevel pLevel);
	
	/**
	 * Gets whether the log level is set or used from a parent logger.
	 * 
	 * @return <code>true</code> if the logger has its own log level
	 */
	public boolean isLevelSet();
	
	/**
	 * Gets the log level.
	 * 
	 * @return the log level
	 */
	public LogLevel getLevel();
	
	/**
	 * Logs information with {@link LogLevel#DEBUG}.
	 * 
	 * @param pInfo any log information
	 */
	public void debug(Object... pInfo);
	
	/**
	 * Logs information with {@link LogLevel#INFO}.
	 * 
	 * @param pInfo any log information
	 */
	public void info(Object... pInfo);
	
	/**
	 * Logs information with {@link LogLevel#ERROR}.
	 * 
	 * @param pInfo any log information
	 */
	public void error(Object... pInfo);
	
	/**
	 * Checks if the current log level is enabled/allowed to log.
	 * 
	 * @param pLevel the log level to check
	 * @return <code>true</code> if the log level is allowed to log
	 */
	public boolean isEnabled(LogLevel pLevel);
	
}	// ILogger
