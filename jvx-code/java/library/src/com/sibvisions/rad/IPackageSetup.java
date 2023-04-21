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
 * 01.10.2008 - [JR] - creation
 * 02.09.2009 - [JR] - JVx 0.6.1
 * 01.10.2009 - [JR] - JVx 0.6.5 
 * 05.11.2009 - [JR] - JVx 0.7 (beta)
 * 23.02.2010 - [JR] - JVx 0.7
 * 10.10.2010 - [JR] - JVx 0.8, Server 0.4.1
 */
package com.sibvisions.rad;

/**
 * The <code>IPackageSetup</code> class defines package relevant version information
 * and properties.
 * 
 * @author René Jahn
 */
public interface IPackageSetup
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constants
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** Constant version value. */
	public static final String SERVER_VERSION = "0.4.1";
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constants (system properties)
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** User-defined configuration base directory (String, path to directory). */
	public static final String CONFIG_BASEDIR = "Configuration.basedir";
    
	/** Whether the classpath should be used for zone search (Boolean). */
    public static final String CONFIG_SEARCH_CLASSPATH = "Configuration.search.classpath";
    
    /** The fixed server instancekey (String). */
    public static final String SERVER_INSTANCEKEY = "Server.instanceKey";

    /** The fixed system identifier (String). */
    public static final String SERVER_SYSTEMIDENTIFIER = "Server.systemIdentifier";
	
}	// IPackageSetup
