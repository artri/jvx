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
 * 09.05.2009 - [JR] - made package private
 * 18.11.2010 - [JR] - removed constructors, isValid implemented 
 * 03.12.2011 - [JR] - #9: encryption support
 * 16.06.2013 - [JR] - #673: additional constructor for user-defined filename
 * 03.07.2013 - [JR] - #713: NAME_CONFIG is now package private
 */
package com.sibvisions.rad.server.config;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import javax.rad.server.IConfiguration;

import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.xml.XmlNode;
import com.sibvisions.util.xml.XmlWorker;

/**
 * A <code>Zone</code> is a special area in the server-side code area. A zone 
 * always has a configuration file.
 * 
 * @author René Jahn
 */
public abstract class Zone extends UpToDateConfigFile
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the name of the configuration file. */
	public static final String NAME_CONFIG = "config.xml";
	
	/** the encrypted nodes. */
	private List<String> liEncryptedNodes = null;
	
	/** the zone directory. */
	private File fiDirectory;
	
	/** the configuration. */
	private IConfiguration config;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /**
     * Creates a new instance of <code>Zone</code> as virtual zone.
     */
    Zone()
    {
        super();
    }

    /**
     * Creates a new instance of <code>Zone</code> as virtual zone
     * using content from a stream.
     * 
     * @param pStream the content stream
     * @throws Exception ifreading stream fails
     */
    Zone(InputStream pStream) throws Exception
    {
        super(pStream);
    }
    
    /**
	 * Creates a new instance of <code>Zone</code> for a directory.
	 * 
	 * @param pDirectory the zone directory
	 * @throws Exception if the configuration for the zone is invalid
	 */
	Zone(File pDirectory) throws Exception
	{
		this(pDirectory, NAME_CONFIG);

		setCloneNodes(true);
	}
	
	/**
	 * Creates a new instance of <code>Zone</code> for a specific configuration.
	 * 
	 * @param pDirectory the zone directory
	 * @param pConfigName the name of the configuration file (e.g. config.xml)
	 * @throws Exception if the configuration for the zone is invalid
	 */
	Zone(File pDirectory, String pConfigName) throws Exception
	{
		super(pDirectory, pConfigName);
		
		fiDirectory = pDirectory;
		
        setCloneNodes(true);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected XmlWorker createXmlWorker()
	{
		XmlWorker xmw = new XmlWorker();
		xmw.setAutomaticDecrypt(true);
		
		if (liEncryptedNodes != null)
		{
			for (String sNode : liEncryptedNodes)
			{
				xmw.setEncrypted(sNode, true);
			}
		}
		
		return xmw;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Checks if the zone is valid. That means that the configuration file exists.
	 * 
	 * @param pDirectory the zone directory
	 * @return <code>true</code> if the zone is valid, otherwise <code>false</code>
	 */
	public static boolean isValid(File pDirectory)
	{
	    return isValid(pDirectory, NAME_CONFIG);
	}

    /**
     * Checks if the zone is valid. That means that the configuration file exists.
     * 
     * @param pDirectory the zone directory
     * @param pConfigName the name of the configuration file
     * @return <code>true</code> if the zone is valid, otherwise <code>false</code>
     */
    public static boolean isValid(File pDirectory, String pConfigName)
    {
        if (pDirectory != null)
        {
            return UpToDateConfigFile.isValid(new File(pDirectory, pConfigName));
        }
        
        return false;
    }
	
	/**
	 * Returns the zone directory.
	 * 
	 * @return the zone directory
	 */
	public File getDirectory()
	{
		return fiDirectory;
	}
	
	/**
	 * Gets this Zone as wrapped {@link IConfiguration}.
	 * 
	 * @return the zone with {@link IConfiguration} access
	 */
	public IConfiguration getConfig()
	{
		if (config == null)
		{
			config = new ZoneConfig(this);
		}
		
		return config;
	}
	
	/**
	 * Sets that a node is encrypted.
	 * 
	 * @param pNode the node name without indizes
	 * @return <code>true</code> if the node is added to the encryption list, <code>false</code> otherwise 
	 *         (maybe it is already added)
	 */
	public boolean addEncryptedNode(String pNode)
	{
		if (liEncryptedNodes == null)
		{
			liEncryptedNodes = new ArrayUtil<String>();
		}
		
		if (!liEncryptedNodes.contains(pNode))
		{
			return liEncryptedNodes.add(pNode);
		}
		
		return false;
	}
	
	/**
	 * Remose a node from the list of encrypted nodes.
	 * 
	 * @param pNode the node name without indizes
	 * @return <code>true</code> if the node is removed from the list, <code>false</code> otherwise
	 */
	public boolean removeEncryptedNode(String pNode)
	{
		boolean bRemove = liEncryptedNodes.remove(pNode);
		
		if (liEncryptedNodes.isEmpty())
		{
			liEncryptedNodes = null;
		}
		
		return bRemove;
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * The <code>ZoneConfig</code> allows restricted access to a
	 * config file.
	 * 
	 * @author René Jahn
	 */	
	static final class ZoneConfig implements IConfiguration
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/** the "hidden" zone. */
		private final Zone zone;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Creates a new instance of <code>ZoneConfig</code> for a {@link Zone}.
		 * 
		 * @param pZone the configuration zone
		 */
		ZoneConfig(Zone pZone)
		{
			zone = pZone;
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Interface implementation
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * {@inheritDoc}
		 */
		public String getProperty(String pName)
		{
			try
			{
				return zone.getProperty(pName);
			}
			catch (Exception e)
			{
				throw new IllegalStateException(e);
			}
		}

		/**
		 * {@inheritDoc}
		 */
		public String getProperty(String pName, String pDefault)
		{
			try
			{
				return zone.getProperty(pName, pDefault);
			}
			catch (Exception e)
			{
				throw new IllegalStateException(e);
			}
		}
		
		/**
		 * {@inheritDoc}
		 */
		public List<String> getProperties(String pName)
		{
			try
			{
				return zone.getProperties(pName);
			}
			catch (Exception e)
			{
				throw new IllegalStateException(e);
			}
		}

		/**
		 * {@inheritDoc}
		 */
		public XmlNode getNode(String pName)
		{
			try
			{
				return zone.getNode(pName);
			}
			catch (Exception e)
			{
				throw new IllegalStateException(e);
			}
		}
		
		/**
		 * {@inheritDoc}
		 */
		public List<XmlNode> getNodes(String pName)
		{
			try
			{
				return zone.getNodes(pName);
			}
			catch (Exception e)
			{
				throw new IllegalStateException(e);
			}
		}
		
	}	// ZoneConfig
	
}	// Zone
