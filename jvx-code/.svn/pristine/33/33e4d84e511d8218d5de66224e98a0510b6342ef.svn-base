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
 * 11.12.2011 - [JR] - creation
 * 19.06.2017 - [JR] - #1799: create methods with additional properties
 * 11.07.2017 - [JR] - #1805: ICloseable implemented
 */
package com.sibvisions.rad.server;

import java.util.Hashtable;
import java.util.Map;

import javax.rad.remote.IConnectionConstants;
import javax.rad.server.ICloseableSession;
import javax.rad.server.IConfiguration;
import javax.rad.server.ISession;
import javax.rad.server.SessionContext;

import com.sibvisions.util.ChangedHashtable;

/**
 * The <code>DirectServerSession</code> is a wrapper for server-side sessions. It is fully attached and forwards 
 * every method call to the server-side session. To create a new instance of <code>DirectServerSession</code> use
 * 
 * <code>
 * DirectServerSession session = DirectServerSession.createMasterSession(...);
 * </code>
 * or
 * <code> 
 * session.createSubSession(...);
 * </code>
 * 
 * @author René Jahn
 */
public class DirectServerSession implements ICloseableSession
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the server session. */
	private final AbstractSession session;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>DirectServerSession</code> for the given session id.
	 * 
	 * @param pSessionId the unique session id
	 */
	DirectServerSession(Object pSessionId)
	{
		session = Server.getInstance().getSessionManager().get(pSessionId);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public Object getId()
	{
		return session.getId();
	}

	/**
	 * {@inheritDoc}
	 */
	public String getApplicationName()
	{
		return session.getApplicationName();
	}

	/**
	 * {@inheritDoc}
	 */
	public String getLifeCycleName()
	{
		return session.getLifeCycleName();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getUserName()
	{
		return session.getUserName();
	}

	/**
	 * {@inheritDoc}
	 */
	public String getPassword()
	{
		return session.getPassword();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Object get(String pObjectName) throws Throwable
	{
		return session.get(pObjectName);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Object put(String pObjectName, Object pObject) throws Throwable
	{
		return session.put(pObjectName, pObject);
	}

	/**
	 * {@inheritDoc}
	 */
	public Object call(String pObjectName, String pMethod, Object... pParams) throws Throwable
	{
		return session.call(pObjectName, pMethod, pParams);
	}

	/**
	 * {@inheritDoc}
	 */
	public Object callAction(String pAction, Object... pParams) throws Throwable
	{
		return session.callAction(pAction, pParams);
	}

	/**
	 * {@inheritDoc}
	 */
	public IConfiguration getConfig()
	{
		return session.getConfig();
	}

	/**
	 * {@inheritDoc}
	 */
	public long getLastAccessTime()
	{
		return session.getLastAccessTime();
	}

	/**
	 * {@inheritDoc}
	 */
	public long getLastAliveTime()
	{
		return session.getLastAliveTime();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setProperty(String pName, Object pValue)
	{
		session.setProperty(pName, pValue);
	}

	/**
	 * {@inheritDoc}
	 */
	public Object getProperty(String pName)
	{
		return session.getProperty(pName);
	}

	/**
	 * {@inheritDoc}
	 */
	public Hashtable<String, Object> getProperties()
	{
		return session.getProperties();
	}

	/**
	 * {@inheritDoc}
	 */
	public long getStartTime()
	{
		return session.getStartTime();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isAlive(long pAccessTime)
	{
		return session.isAlive(pAccessTime);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isInactive(long pAccessTime)
	{
		return session.isInactive(pAccessTime);
	}

	/**
	 * {@inheritDoc}
	 */
	public long getAliveInterval()
	{
		return session.getAliveInterval();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setAliveInterval(long pAliveInterval)
	{
		session.setAliveInterval(pAliveInterval);
	}

	/**
	 * {@inheritDoc}
	 */
	public int getMaxInactiveInterval()
	{
		return session.getMaxInactiveInterval();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setMaxInactiveInterval(int pMaxInactiveInterval)
	{
		session.setMaxInactiveInterval(pMaxInactiveInterval);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void close() throws Throwable
	{
	    destroy();
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Destroys the session.
	 */
	public void destroy()
	{
		Server.getInstance().destroySession(session.getId());
	}

    /**
     * Creates a new instance of <code>DirectServerSession</code> for the given application and properties.
     *
     * @param pApplicationName the name of the application
     * @param pProperties the additional session properties
     * @return the newly created session
     * @throws Throwable if session creation fails
     */
    public static DirectServerSession createMasterSession(String pApplicationName, Map<String, Object> pProperties) throws Throwable
    {
        return createMasterSession(pApplicationName, null, null, pProperties);     
    }

    /**
	 * Creates a new instance of <code>DirectServerSession</code> for the given application and credentials.
	 *
	 * @param pApplicationName the name of the application
	 * @param pUserName the user name
	 * @param pPassword the password
	 * @return the newly created session
	 * @throws Throwable if session creation fails
	 */
	public static DirectServerSession createMasterSession(String pApplicationName, String pUserName, String pPassword) throws Throwable
	{
		return createMasterSession(pApplicationName, pUserName, pPassword, null);
	}
	
    /**
     * Creates a new instance of <code>DirectServerSession</code> for the given application, credentials and properties.
     *
     * @param pApplicationName the name of the application
     * @param pUserName the user name
     * @param pPassword the password
     * @param pProperties the additional session properties
     * @return the newly created session
     * @throws Throwable if session creation fails
     */
	public static DirectServerSession createMasterSession(String pApplicationName, String pUserName, String pPassword, Map<String, Object> pProperties) throws Throwable
	{
        ChangedHashtable<String, Object> chtProperties = createProperties(pProperties);
        chtProperties.put(IConnectionConstants.APPLICATION, pApplicationName);
        
        if (pUserName != null)
        {
            chtProperties.put(IConnectionConstants.USERNAME, pUserName);
        }
        
        if (pPassword != null)
        {
            chtProperties.put(IConnectionConstants.PASSWORD, pPassword);
        }
        
        Object oSessionId = Server.getInstance().createSession(chtProperties);
        
        return new DirectServerSession(oSessionId);	    
	}
	
	/**
	 * Creates a new instance of <code>DirectServerSession</code> as sub session for the given life-cycle object name.
	 * 
	 * @param pLifeCycleName the full qualified class name of the life-cycle object
	 * @return the newly created session
	 * @throws Throwable if session creation fails
	 */
	public DirectServerSession createSubSession(String pLifeCycleName) throws Throwable
	{
	    return createSubSession(pLifeCycleName, null);
	}
	
    /**
     * Creates a new instance of <code>DirectServerSession</code> as sub session for the given life-cycle object name
     * and properties.
     * 
     * @param pLifeCycleName the full qualified class name of the life-cycle object
     * @param pProperties the additional session properties
     * @return the newly created session
     * @throws Throwable if session creation fails
     */
	public DirectServerSession createSubSession(String pLifeCycleName, Map<String, Object> pProperties) throws Throwable
	{
        ChangedHashtable<String, Object> chtProperties = createProperties(pProperties);
        chtProperties.put(IConnectionConstants.LIFECYCLENAME, pLifeCycleName);
        
        Object oSessionId = Server.getInstance().createSubSession(session.getId(), chtProperties);
        
        return new DirectServerSession(oSessionId);
	}

	/**
	 * Creates a new {@link ChangedHashtable} instance with given properties as base.
	 * 
	 * @param pProperties the base properties
	 * @return the newly created {@link ChangedHashtable}
	 */
	protected static ChangedHashtable<String, Object> createProperties(Map<String, Object> pProperties)
	{
        if (pProperties != null)
        {
            return new ChangedHashtable<String, Object>(pProperties);
        }
        else
        {
            return new ChangedHashtable<String, Object>();
        }
	}
	
	/**
	 * Gets the internal session.
	 * 
	 * @return the {@link ISession}
	 */
	ISession getSession()
	{
		return session;
	}
	
	/**
	 * Sets the {@link SessionContext} implementation.
	 * 
	 * @param pClass the implementation class
	 */
	public void setSessionContextImpl(Class<? extends SessionContext> pClass)
	{
		session.setSessionContextImpl(pClass);
	}
	
}	//DirectServerSession
