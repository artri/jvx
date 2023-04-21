/*
 * Copyright 2011 SIB Visions GmbH
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
 * 31.07.2011 - [JR] - creation
 */
package com.sibvisions.rad.server;

import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;

import javax.rad.server.IConfiguration;
import javax.rad.server.ISession;

import com.sibvisions.rad.server.config.ApplicationZone;
import com.sibvisions.rad.server.config.Configuration;
import com.sibvisions.rad.server.security.AbstractSecurityManager;
import com.sibvisions.rad.server.security.ISecurityManager;
import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.Reflective;
import com.sibvisions.util.type.StringUtil;

/**
 * The <code>DetachedSession</code> is an {@link ISession}. It allows access to a specific application
 * with all its properties but has no connection to the server or other server objects. It has no
 * builtin support for the {@link javax.rad.server.SessionContext}.
 * The <code>DetachedSession</code> is not bound to a {@link DefaultSessionManager} and does not fire session
 * created or destroyed events. It is also not validated against the configured security manager.  
 * 
 * @author René Jahn
 */
public class DetachedSession implements ISession
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the unique session id. */
	private Object oId;
	
	/** the application zone. */
	private ApplicationZone zone;
	
	/** the security manager. */
	private ISecurityManager secman;
	
	/** the session properties. */
	private Hashtable<String, Object> htProperties = null;
	
	/** the session objects. */
	private Hashtable<String, Object> htObjects = null;
	
	/** the user name. */
	private String sUserName;
	
	/** the password. */
	private String sPassword;
	
	/** the session creation time. */
	private long lStart;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of <code>DetachedSession</code> for a specific application.
	 * 
	 * @param pApplicationName the application
	 * @throws Exception if session creation fails because of security manager validation or
	 *                   if the application is invalid/unknown
	 */
	public DetachedSession(String pApplicationName) throws Exception
	{
		//use a frozen configuration as session config
		zone = (ApplicationZone)(ApplicationZone)Configuration.getApplicationZone(pApplicationName).clone();

		//#407
		if (!Boolean.valueOf(zone.getProperty("/application/liveconfig")).booleanValue())
		{
			//BE careful, at the moment it is not possible to change properties/nodes of the configuration via 
			//IConfiguration (which is available in Lifecycle objects).
			//
			//If live-config is enabled, and config is not saved, all changes are lost
			zone.setUpdateEnabled(false);
		}

		oId = UUID.randomUUID().toString();
		
		lStart = System.currentTimeMillis();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public Object getId()
	{
		return oId;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getApplicationName()
	{
		return zone.getName();
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
	public String getPassword()
	{
		return sPassword;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Object put(String pObjectName, Object pObject)
	{
		if (htObjects == null)
		{
			htObjects = new Hashtable<String, Object>();
		}
		
		if (pObject == null)
		{
			return htObjects.remove(pObjectName);
		}
		else
		{
			return htObjects.put(pObjectName, pObject);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Object get(String pObjectName) throws Throwable
	{
		if (htObjects == null)
		{
			return null;
		}
		
		return htObjects.get(pObjectName);
	}

	/**
	 * {@inheritDoc}
	 */
	public Object call(String pObjectName, String pMethod, Object... pParams) throws Throwable
	{
		if (pObjectName == null)
		{
			throw new RuntimeException("Invalid object name '" + pObjectName + "'");
		}

		if (htObjects == null)
		{
			throw new RuntimeException("Unknown object '" + pObjectName + "'");
		}
		
		Object oInvoke = null;
		
		String sObjectName;
		
		StringBuilder sbCurrentObjectName = new StringBuilder();
		
		//search the desired object
		ArrayUtil<String> auNames = StringUtil.separateList(pObjectName, ".", true);
		
		for (int i = 0, anz = auNames.size(); i < anz; i++)
		{
			sObjectName = auNames.get(i);
					
			sObjectName = auNames.get(i);
			
			if (sbCurrentObjectName.length() > 0)
			{
				sbCurrentObjectName.append(".");
			}
			
			sbCurrentObjectName.append(sObjectName);
			
			if (i == 0)
			{
				oInvoke = htObjects.get(sObjectName);
			}

			if (oInvoke == null)
			{
				throw new RuntimeException("Unknown object '" + sbCurrentObjectName.toString() + "'");
			}
			
			try
			{
				oInvoke = Reflective.call(oInvoke, StringUtil.formatMethodName("get", sObjectName));
			}
			catch (NoSuchMethodException nsme)
			{
				if (oInvoke instanceof Map)
				{
					Object oResult = ((Map)oInvoke).get(sObjectName);
					
					if (oResult == null && !((Map)oInvoke).containsKey(sObjectName))
					{
						throw new RuntimeException("Unknown object '" + sObjectName + "'");
					}
					
					//use the result!
					oInvoke = oResult;
				}
				else
				{
					throw new RuntimeException("Unknown object '" + sObjectName + "'", nsme);
				}
			}
		}
		
		return oInvoke;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object callAction(String pAction, Object... pParams) throws Throwable
	{
		throw new UnsupportedOperationException("Actions are not available!");
	}

	/**
	 * {@inheritDoc}
	 */
	public IConfiguration getConfig()
	{
		return zone.getConfig();
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
	public Hashtable<String, Object> getProperties()
	{
		if (htProperties == null)
		{
			htProperties = new Hashtable<String, Object>();
		}
		
		return htProperties;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object getProperty(String pName)
	{
		if (htProperties == null)
		{
			return null;
		}
		
		return htProperties.get(pName);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setProperty(String pName, Object pValue)
	{
		if (htProperties == null)
		{
			htProperties = new Hashtable<String, Object>();
		}
		
		if (pValue == null)
		{
			htProperties.remove(pName);
		}
		else
		{
			htProperties.put(pName, pValue);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public long getStartTime()
	{
		return lStart;
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
	public void setAliveInterval(long pAliveInterval)
	{
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
	public void setMaxInactiveInterval(int pMaxInactiveInterval)
	{
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

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void finalize() throws Throwable
	{
		if (secman != null)
		{
			try
			{
				secman.release();
			}
			catch (Throwable th)
			{
				//nothing to be done
			}
		}
		
		super.finalize();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Sets the username.
	 * 
	 * @param pUserName the user name
	 */
	public void setUserName(String pUserName)
	{
		sUserName = pUserName;
	}
	
	/**
	 * Sets the password.
	 * 
	 * @param pPassword the password
	 */
	public void setPassword(String pPassword)
	{
		sPassword = pPassword;
	}
	
	/**
	 * Gets the security manager for this session.
	 * 
	 * @return the security manager
	 * @throws Exception if creation fails
	 */
	public ISecurityManager getSecurityManager() throws Exception
	{
		if (secman == null)
		{
			secman = AbstractSecurityManager.createSecurityManager(this); 
		}
		
		return secman;
	}
	
}	// DetachedSession
