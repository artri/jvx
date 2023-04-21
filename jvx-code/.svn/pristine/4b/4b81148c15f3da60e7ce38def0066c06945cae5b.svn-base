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
 * 18.11.2008 - [JR] - allow inherited properties
 * 04.11.2010 - [JR] - constructor with lazy loading implemented (needed in Configuration)
 * 18.11.2010 - [JR] - #206: removed constructors, clone support and
 * 27.05.2011 - [JR] - #370: checkCaseSensitive implemented 
 * 07.12.2012 - [JR] - check if getServerZone() == null to avoid NullPointerException if server config is missing
 * 02.10.2014 - [JR] - #1126: default constructor changed
 */
package com.sibvisions.rad.server.config;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import com.sibvisions.util.xml.XmlNode;

/**
 * The <code>ApplicationZone</code> class encapsulates the access
 * to an application configuration. 
 * 
 * @author René Jahn
 */
public final class ApplicationZone extends Zone
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** the server zone. */
	private ServerZone zoneServer = null;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>ApplicationZone</code>. The configuration
	 * of the appliation will be loaded automatically.
	 * 
	 * @param pDirectory the application directory
	 * @throws Exception if the application configuration is not valid
	 */
	ApplicationZone(File pDirectory) throws Exception
	{
		super(checkCaseSensitive(pDirectory));
	}
	
	/**
	 * Creates a new instance of <code>ApplicationZone</code>. The configuration
	 * of the application will be loaded automatically from the stream.
	 * 
	 * @param pStream the stream
	 * @throws Exception if loading configuration fails
	 */
	ApplicationZone(InputStream pStream) throws Exception
	{
	    super(pStream);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object clone() throws CloneNotSupportedException
	{
		ApplicationZone zone = (ApplicationZone)super.clone();
		
		if (zoneServer != null)
		{
			zone.zoneServer = (ServerZone)zoneServer.clone();
		}
		
		return zone;
	}
	
	/**
	 * Gets the value of a property from the application configuration file. If the property was not
	 * found then the property will be read from the server configuration file.
	 *
	 * @param pName the property name (e.g /application/securitymanager/class)
	 * @param pDefault the default value if the property is not available in the application and 
	 *                 server configuration
	 * @return the value for the property or <code>pDefault</code> if the property is generally 
	 *         not available
	 * @throws Exception if the configuration(s) is/are invalid
	 */
	@Override
	public synchronized String getProperty(String pName, String pDefault) throws Exception
	{
		String sValue = super.getProperty(pName, null);
		
		if (sValue == null)
		{
			sValue = getServerZone().getProperty("/server" + pName.substring(12), pDefault);
			
			if (sValue == null)
			{
				return pDefault;
			}
		}
		
		return sValue;
	}
	
	/**
	 * Gets a list of values for a property which exists more than once. The list will be extended
	 * with not already containing server properties.
	 * 
	 * @param pName the property name
	 * @return the list of merged values from the application and server configuration or <code>null</code> 
	 *         if the property is generally not available
	 * @throws Exception if the configuration is invalid
	 */
	@Override
	public synchronized List<String> getProperties(String pName) throws Exception
	{
		List<String> liValues = super.getProperties(pName);
		List<String> liGlobal;
		
		liGlobal = getServerZone().getProperties("/server" + pName.substring(12)); 
		
		if (liValues == null)
		{
			//use the global values
			return liGlobal;
		}
		else
		{
			//merge the values
			if (liGlobal != null)
			{
				for (String sValue : liGlobal)
				{
					if (!liValues.contains(sValue))
					{
						liValues.add(sValue);
					}
				}
			}
			
			return liValues;
		}
	}
	
	/**
	 * Gets the xml node or a list of nodes for a property name from the application configuration. The list
	 * will be extended if the server configuration contains the same property. The list first contains the
	 * server nodes and then the application nodes.
	 * 
	 * @param pName {@inheritDoc}
	 * @return the available xml node(s) from the application and server configuration or <code>null</code> 
	 *         if the property is generally not available
	 * @throws Exception if the configuration(s) is/are invalid
	 */
	@Override
	public synchronized List<XmlNode> getNodes(String pName) throws Exception
	{
		List<XmlNode> liValues = super.getNodes(pName);
		List<XmlNode> liGlobal;
		
		liGlobal = getServerZone().getNodes("/server" + pName.substring(12)); 
		
		if (liValues == null)
		{
			return liGlobal;
		}
		else
		{
			//append to the global values, because it's not possible to merge
			if (liGlobal != null)
			{
				liGlobal.addAll(liValues);
				
				return liGlobal;
			}
			else
			{
				return liValues;
			}
		}
	}
	
	/**
	 * Gets the xml node for a property name from the application configuration. If the property was not
	 * found in the application configuration, then the server configuration will be used.
	 * 
	 * @param pName the property name
	 * @return the available xml node from the application or server configuration or <code>null</code>
	 *         if the property is generally not available
	 * @throws Exception if the configuration(s) is/are invalid
	 */
	@Override
	public synchronized XmlNode getNode(String pName) throws Exception
	{
		XmlNode node = super.getNode(pName);
		
		if (node == null)
		{
			return getServerZone().getNode("/server" + pName.substring(12));
		}
		else
		{
			return node;
		}
	}
	
	/**
	 * Sets whether this application zone should be up-to-date.
	 * 
	 * @param pEnabled <code>true</code> to disable up-to-date configuration properties. This disables
	 *                 the up-to-date option of the server zone too (@see {@link #getServerZone()}. 
	 */
	@Override
	public void setUpdateEnabled(boolean pEnabled)
	{
		if (pEnabled)
		{
			zoneServer = null;
		}
		else
		{
		    if (zoneServer == null)
		    {
                ServerZone zone = Configuration.getServerZone();

                try
    			{
                    zone = (ServerZone)zone.clone();
    				zone.setUpdateEnabled(false);
    				
    				zoneServer = zone;
    			}
    			catch (Exception e)
    			{
    				throw new SecurityException("Invalid server configuration!", e);
    			}
		    }
		}
		
		super.setUpdateEnabled(pEnabled);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Returns the name of the application.
	 * 
	 * @return the application name
	 */
	public String getName()
	{
		File fiBase = getDirectory();
		
		return fiBase != null ? fiBase.getName() : null;
	}

	/**
	 * Gets the current server zone. If up-to-date option is disabled, then an frozen copy of the
	 * server zone will be returned.
	 * 
	 * @return the zone of the server or <code>null</code> if the zone has errors
	 * @see ServerZone
	 * @see #setUpdateEnabled(boolean)
	 */
	public ServerZone getServerZone()
	{
		if (zoneServer == null)
		{
            //always use the cached version
            return Configuration.getServerZone();
		}
		
		return zoneServer;
	}
	
	/**
	 * Checks wheter the directory name is case sensitive.
	 * 
	 * @param pDirectory the original directory
	 * @return the original directory if the name is in correct case
	 * @throws Exception if the directory exists but the name is not case sensitive correct
	 */
	private static File checkCaseSensitive(File pDirectory) throws Exception
	{
		if (pDirectory != null)
		{
			File fiCanonic = pDirectory.getCanonicalFile();
			
			//no canonical file -> not our problem (super class checks this case)
			if (fiCanonic != null && !pDirectory.getName().equals(fiCanonic.getName()))
			{
				throw new Exception("Invalid application. Directory name is case sensitive!");
			}
		}
		
		return pDirectory;
	}
	
}	// ApplicationZone
