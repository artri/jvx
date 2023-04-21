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
 * 11.05.2011 - [JR] - closeConnections implemented
 * 25.01.2019 - [JR] - close connection listener implemented
 */
package com.sibvisions.rad.server;

import java.util.List;

import javax.rad.server.ISession;
import javax.rad.server.event.ISessionListener;

import com.sibvisions.rad.server.monitoring.ICloseConnectionListener;
import com.sibvisions.rad.server.security.ISecurityManager;
import com.sibvisions.util.ArrayUtil;

/**
 * The <code>Monitoring</code> class will be used as server-side object for
 * administer the communication server.
 * 
 * The object must be injected to a life-cycle object before it's usable.<br>
 * You can inject the object with the following definition
 * 
 * <pre>
 * &#064;Inject(origin = &quot;server&quot;, name = &quot;monitoring&quot;)
 * private Monitoring monitoring = null;
 * </pre>
 * 
 * @author René Jahn
 */
public final class Monitoring implements ISessionListener
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /** instance of the communication server. */
    private Server server                  = null;
    
    /** the session-id of the last destroyed session. */
    private Object oLastDestroyedSessionId = null;
    
	/** list of all registered close connection listeners. */
	private ArrayUtil<ICloseConnectionListener> auCloseConnectionListener = new ArrayUtil<ICloseConnectionListener>();
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Creates an instance of <code>Monitoring</code> for a specific
     * communication server.
     * 
     * @param pServer the communication server
     */
    public Monitoring(Server pServer)
    {
        this.server = pServer;
        
        server.getSessionManager().addSessionListener(this);
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * {@inheritDoc}
     */
    public void sessionCreated(ISession pSession)
    {
    }
    
    /**
     * {@inheritDoc}
     */
    public void sessionDestroyed(ISession pSession)
    {
        if (pSession != null)
        {
            oLastDestroyedSessionId = pSession.getId();
        }
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Gets the startup time of the server.
     * 
     * @return the startup time
     */
    public long getServerStartupTime()
    {
        return server.getStartupTime();
    }
    
    /**
     * Gets the number of opened sessions.
     * 
     * @return session count
     */
    public int getSessionCount()
    {
        return server.getSessionManager().getSessionCount();
    }
    
    /**
     * Gets the id of the last destroyed session.
     * 
     * @return the session identifier or null if no session was destroyed
     */
    public Object getLastDestroyedSessionId()
    {
        return oLastDestroyedSessionId;
    }
    
    /**
     * Gets the session identifiers of all currently opened sessions.
     * 
     * @return a list with session identifiers
     */
    public ArrayUtil<Object> getSessionIds()
    {
        return server.getSessionManager().getSessionIds();
    }
    
    /**
     * Gets all available master session ids.
     * 
     * @return the list of currently available master session ids
     */
    public ArrayUtil<Object> getMasterSessionIds()
    {
        return getMasterSessionIds(null);
    }
    
    /**
     * Gets all available master session ids for a specific application.
     * 
     * @param pApplicationName the application name
     * @return the list of currently available master session ids
     */
    public ArrayUtil<Object> getMasterSessionIds(String pApplicationName)
    {
        DefaultSessionManager smanager = server.getSessionManager();

        ArrayUtil<Object> auIds = smanager.getSessionIds();
        
        AbstractSession session;
        
        for (int i = auIds.size() - 1; i >= 0; i--)
        {
            try
            {
                session = smanager.get(auIds.get(i));
                
                if (!(session instanceof MasterSession))
                {
                    auIds.remove(i);
                }
                else if (pApplicationName != null && !pApplicationName.equals(session.getApplicationName()))
                {
                    auIds.remove(i);
                }
            }
            catch (Throwable th)
            {
                auIds.remove(i);
            }
        }
        
        return auIds;
    }
    
    /**
     * Gets all available sub sessions for the given master session.
     * 
     * @param pSessionId the master session id
     * @return the list of available sub session ids 
     */
    public ArrayUtil<Object> getSubSessionIds(Object pSessionId)
    {
        DefaultSessionManager smanager = server.getSessionManager();
        
        ArrayUtil<Object> auIds = new ArrayUtil<Object>();
        
        try
        {
            AbstractSession session = smanager.get(pSessionId);
            
            if (session instanceof MasterSession)
            {
                ArrayUtil<SubSession> auSub = ((MasterSession)session).getSubSessions();
                
                if (auSub != null)
                {
                    for (int i = 0, cnt = auSub.size(); i < cnt; i++)
                    {
                        auIds.add(auSub.get(i).getId());
                    }
                }                
            }
            else
            {
                throw new IllegalArgumentException("Given session is not a master session!");
            }
        }
        catch (Throwable th)
        {
            // no sessions available
        }
        
        return auIds;
    }
    
    /**
     * Close all connections for a specific application.
     * 
     * @param pApplicationName the application name
     */
    public void closeConnections(String pApplicationName)
    {
        DefaultSessionManager sessman = server.getSessionManager();
        
        List<Object> liIds = sessman.getSessionIds();
        
        ISession sess;
        
        for (Object oId : liIds)
        {
            try
            {
                sess = sessman.get(oId);
                
                if (pApplicationName.equals(sess.getApplicationName()))
                {
                    server.destroySession(oId);
                }
            }
            catch (Throwable th)
            {
                // maybe already expired
            }
        }
        
        releaseSecurityManager(sessman.getSecurityManagerFromCache(pApplicationName));
        
        fireCloseConnections(pApplicationName);
    }
    
    /**
     * Releases the security manger for a specific application.
     * 
     * @param pApplicationName the application name.
     */
    public void releaseSecurityManager(String pApplicationName)
    {
        releaseSecurityManager(server.getSessionManager().getSecurityManagerFromCache(pApplicationName));
    }
    
    /**
     * Releases the given security manager(s).
     * 
     * @param pSecurityManager the security manager list
     */
    private void releaseSecurityManager(ISecurityManager[] pSecurityManager)
    {
        if (pSecurityManager != null)
        {
            for (int i = 0; i < pSecurityManager.length; i++)
            {
                pSecurityManager[i].release();
            }
        }
    }

    /**
     * Removes the cached security managers from the cache.
     * 
     * @param pApplicationName the application name.
     */
    public void removeSecurityManager(String pApplicationName)
    {
    	server.getSessionManager().removeSecurityManagersFromCache(pApplicationName);
    }
    
	/**
	 * Adds an {@link ICloseConnectionListener} to the list of known listeners.
	 * 
	 * @param pListener the listener to add
	 */
	public final void addCloseConnectionListener(ICloseConnectionListener pListener)
	{
	    synchronized (auCloseConnectionListener)
	    {
	        auCloseConnectionListener.add(pListener);
	    }
	}
	
	/**
	 * Removes an {@link ICloseConnectionListener} from the list of known listeners.
	 * 
	 * @param pListener the listener to remove
	 */

	public final void removeCloseConnectionListener(ICloseConnectionListener pListener)
	{
        synchronized (auCloseConnectionListener)
        {
			auCloseConnectionListener.remove(pListener);
		}
	}
    
	/**
	 * Notifies all registered {@link ICloseConnectionListener}s about close connections.
	 *  
	 * @param pApplicationName the name of the application
	 */
	private void fireCloseConnections(String pApplicationName)
	{
		if (pApplicationName == null)
		{
			return;
		}
		
		ArrayUtil<ICloseConnectionListener> auCopy;
		
		synchronized (auCloseConnectionListener)
	    {
		    auCopy = new ArrayUtil<ICloseConnectionListener>(auCloseConnectionListener);
	    }
		
    	for (int i = 0, anz = auCopy.size(); i < anz; i++)
    	{
    	    auCopy.get(i).closeConnections(pApplicationName);
    	}
	}	
    
}   // Monitoring
