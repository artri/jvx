/*
 * Copyright 2014 SIB Visions GmbH
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
 * 22.11.2014 - [JR] - creation
 * 27.08.2019 - [JR] - #2048: deny setting alive interval
 */
package com.sibvisions.rad.server;

import java.util.Hashtable;

import javax.rad.remote.IConnectionConstants;
import javax.rad.server.ICallBackBroker;
import javax.rad.server.ICloseableSession;
import javax.rad.server.IConfiguration;

/**
 * The <code>WrappedSession</code> allows restricted access to the 
 * {@link AbstractSession}.
 * 
 * @author René Jahn
 */
final class WrappedSession implements ICloseableSession,
                                      ICallBackSession
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** the "hidden" session. */
    protected final AbstractSession session;
    
    /** whether the session can be closed. */
    private final boolean bCloseable;
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Creates a new instance of <code>WrappedSession</code> for an {@link javax.rad.server.ISession}.
     * 
     * @param pSession an {@link javax.rad.server.ISession} implementation
     */
    WrappedSession(AbstractSession pSession)
    {
        this(pSession, false);
    }
    
    /**
     * Creates a new instance of <code>WrappedSession</code> for an {@link javax.rad.server.ISession}.
     * 
     * @param pSession an {@link javax.rad.server.ISession} implementation
     * @param pCloseable whether the session can be closed manually
     */
    WrappedSession(AbstractSession pSession, boolean pCloseable)
    {
        session = pSession;
        bCloseable = pCloseable;
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
    	if  (session != null)
    	{
    		return session.getId().toString();
    	}
    	
    	return "null session@" + super.toString();
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
    public String getLifeCycleName()
    {
        return session.getLifeCycleName();
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
    public Object getProperty(String pName)
    {
        return session.getProperty(pName);
    }
    
    /**
     * {@inheritDoc}
     */
    public Hashtable<String, Object> getProperties()
    {
        return (Hashtable<String, Object>)session.getProperties().clone();
    }
    
    /**
     * {@inheritDoc}
     */
    public void setProperty(String pName, Object pValue)
    {
        if (pName != null && pName.startsWith(IConnectionConstants.PREFIX_CLIENT))
        {
            throw new SecurityException("It's not allowed to change client properties through the SessionContext!");
        }
        
        session.setProperty(pName, pValue);
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
    public void setAliveInterval(long pAliveInterval)
    {
    	//#2048
    	throw new SecurityException("It's not allowed to change alive interval through the SessionContext");
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
    public boolean isAlive(long pAccessTime)
    {
        return session.isAlive(pAccessTime);
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
    public int getMaxInactiveInterval()
    {
        return session.getMaxInactiveInterval();
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
    public IConfiguration getConfig()
    {
        return session.getApplicationZone().getConfig();
    }
    
    /**
     * {@inheritDoc}
     */
    public void close()
    {
        if (bCloseable)
        {
            session.getSessionManager().destroy(session);
        }
        else
        {
            throw new SecurityException("It's not allowed to close the session!");
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public ICallBackBroker getCallBackBroker()
    {
    	return session.getCallBackBroker();
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Checks if the session is valid.
     * 
     * @return <code>true</code> if session is valid
     * @see {@link DefaultSessionManager#isValid(Object)}
     */
    public boolean isValid()
    {
        return session != null && session.getSessionManager().isValid(session);
    }
        
}   // WrappedSession
