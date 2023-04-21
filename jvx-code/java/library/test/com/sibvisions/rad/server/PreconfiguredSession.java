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
 * 14.11.2009 - [JR] - creation
 */
package com.sibvisions.rad.server;

import java.util.Hashtable;
import java.util.List;

import javax.rad.server.IConfiguration;
import javax.rad.server.ISession;

import com.sibvisions.util.xml.XmlNode;

/**
 * The <code>PreconfiguredSession</code> is an {@link ISession} implementation which
 * allows testing of validators.<br/>
 * It's a very simple implementation only with support for accessing the configuration.
 * The configuration can be changed through the developer. That's not the case with
 * standard {@link ISession} implementations.
 * 
 * @author René Jahn
 */
public final class PreconfiguredSession implements ISession
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the configuration based on a {@link Hashtable}. */
	private HashtableConfig config = new HashtableConfig();
	
	/** the username for this session. */
	private String sUserName;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public Object call(String pObjectName, String pMethod, Object... pParams) throws Throwable
	{
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object callAction(String pAction, Object... pParams) throws Throwable
	{
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object get(String pObjectName) throws Throwable
	{
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object put(String pObjectName, Object pObject) throws Throwable
	{
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public long getAliveInterval()
	{
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getApplicationName()
	{
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public IConfiguration getConfig()
	{
		return config;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object getId()
	{
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public long getLastAccessTime()
	{
		return System.currentTimeMillis();
	}

	/**
	 * {@inheritDoc}
	 */
	public long getLastAliveTime()
	{
		return System.currentTimeMillis();
	}

	/**
	 * {@inheritDoc}
	 */
	public String getLifeCycleName()
	{
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getMaxInactiveInterval()
	{
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getPassword()
	{
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public Hashtable<String, Object> getProperties()
	{
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object getProperty(String pName)
	{
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public long getStartTime()
	{
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getUserName()
	{
		return sUserName;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isAlive(long pAccessTime)
	{
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isInactive(long pAccessTime)
	{
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setAliveInterval(long pAliveInterval)
	{
	}

	/**
	 * {@inheritDoc}
	 */
	public void setMaxInactiveInterval(int pMaxInactiveInterval)
	{
	}

	/**
	 * {@inheritDoc}
	 */
	public void setProperty(String pName, Object pValue)
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Sets a specific configuration value.
	 * 
	 * @param pName the parameter name
	 * @param pValue the value
	 */
	public void setConfigValue(String pName, String pValue)
	{
		config.htProperties.put(pName, pValue);
	}
	
	/**
	 * Sets a specific configuration node.
	 * 
	 * @param pName the node name
	 * @param pValue the value
	 */
	public void setNodeValue(String pName, XmlNode pValue)
	{
		config.htNodes.put(pName, pValue);
	}
	
	/**
	 * Sets the username for this session.
	 * 
	 * @param pUserName the user name
	 */
	public void setUserName(String pUserName)
	{
		sUserName = pUserName;
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************

	/**
	 * The <code>HashtableConfig</code> is a simple {@link IConfiguration} implementation based
	 * on a {@link Hashtable}.
	 * 
	 * @author René Jahn
	 * @see IConfiguration
	 */
	static final class HashtableConfig implements IConfiguration 
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/** the configuration keys and nodes. */
		private Hashtable<String, XmlNode> htNodes = new Hashtable<String, XmlNode>();

		/** the configuration keys and values. */
		private Hashtable<String, String> htProperties = new Hashtable<String, String>();

		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Interface implementation
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * {@inheritDoc}
		 */
		public XmlNode getNode(String pName)
		{
			return htNodes.get(pName);
		}

		/**
		 * {@inheritDoc}
		 */
		public List<XmlNode> getNodes(String pName)
		{
			return null;
		}

		/**
		 * {@inheritDoc}
		 */
		public List<String> getProperties(String pName)
		{
			return null;
		}

		/**
		 * {@inheritDoc}
		 */
		public String getProperty(String pName)
		{
			return htProperties.get(pName);
		}

		/**
		 * {@inheritDoc}
		 */
		public String getProperty(String pName, String pDefault)
		{
			String sValue = htProperties.get(pName);
			
			if (sValue == null)
			{
				return pDefault;
			}
			else
			{
				return sValue;
			}
		}
		
	}	// HashtableConfig
	
}	// PreconfiguredSession
