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
 * 09.05.2009 - [JR] - creation
 * 18.11.2010 - [JR] - #206: removed constructors
 * 02.10.2014 - [JR] - #1126: default constructor changed
 */
package com.sibvisions.rad.server.config;

import java.io.File;
import java.io.InputStream;

/**
 * The <code>ServerZone</code> class encapsulates the access to
 * the server configuration.
 *  
 * @author René Jahn
 */
public class ServerZone extends Zone
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>ServerZone</code>. The zone itself
	 * is virtual and doesn't perform any file-system operations.
	 */
	ServerZone()
	{
		super();
	}
	
	/**
	 * Creates a new instance of <code>ServerZone</code> for a directory.
	 * 
	 * @param pDirectory the zone directory
	 * @throws Exception if the configuration is invalid
	 */
	ServerZone(File pDirectory) throws Exception
	{
		super(pDirectory);
	}

    /**
     * Creates a new instance of <code>ServerZone</code> for a specific configuration.
     * 
     * @param pDirectory the zone directory
     * @param pConfigName the name of the configuration file (e.g. config.xml)
     * @throws Exception if the configuration is invalid
     */
    ServerZone(File pDirectory, String pConfigName) throws Exception
    {
        super(pDirectory, pConfigName);
    }	
    
    /**
     * Creates a new instance of <code>ServerZone</code>. The configuration
     * will be loaded automatically from the stream.
     * 
     * @param pStream the stream
     * @throws Exception if loading configuration fails
     */
    ServerZone(InputStream pStream) throws Exception
    {
        super(pStream);
    }
	
}	// ServerZone
