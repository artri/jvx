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
 * 08.05.2009 - [JR] - creation
 */
package javax.rad.server;

import javax.rad.server.event.IFailedSessionListener;
import javax.rad.server.event.ISessionListener;

/**
 * A <code>AbstractSessionManager</code> defines the access to the server-side
 * session handling.
 * 
 * @author René Jahn
 */
public abstract class AbstractSessionManager
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the associated server. */
	private IServer server;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>AbstractSessionManager</code> for a specific
	 * {@link IServer}.
	 * 
	 * @param pServer the server
	 */
	protected AbstractSessionManager(IServer pServer)
	{
		server = pServer;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets an existing session.
	 * 
	 * @param pSessionId session identifier
	 * @return session object, guaranteed not null
	 * @throws SecurityException if the session is expired or <code>pSessionId</code> is null
	 */
	public abstract ISession get(Object pSessionId);
	
	/**
	 * Adds an {@link ISessionListener} to the list of known listeners.
	 * 
	 * @param pListener the session listener to add
	 */
	public abstract void addSessionListener(ISessionListener pListener);

	/**
	 * Removes an {@link ISessionListener} from the list of known listeners.
	 * 
	 * @param pListener the session listener to remove
	 */
	public abstract void removeSessionListener(ISessionListener pListener);
	
	/**
	 * Gets the list of known listeners.
	 * 
	 * @return a list of {@link ISessionListener} or an empty list
	 */
	public abstract ISessionListener[] getSessionListeners();

	/**
	 * Adds an {@link IFailedSessionListener} to the list of known listeners.
	 * 
	 * @param pListener the session listener to add
	 */
	public abstract void addFailedSessionListener(IFailedSessionListener pListener);

	/**
	 * Removes an {@link IFailedSessionListener} from the list of known listeners.
	 * 
	 * @param pListener the session listener to remove
	 */
	public abstract void removeFailedSessionListener(IFailedSessionListener pListener);
	
	/**
	 * Gets the list of known listeners.
	 * 
	 * @return a list of {@link IFailedSessionListener} or an empty list
	 */
	public abstract IFailedSessionListener[] getFailedSessionListeners();
	
    /**
     * Gets whether the given session identifier is available.
     * 
     * @param pSessionId session identifier
     * @return <code>true</code> if session is available
     */
    public abstract boolean isAvailable(Object pSessionId); 

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the associated {@link IServer}.
	 * 
	 * @return the server
	 */
	public IServer getServer()
	{
		return server;
	}
	
}	// AbstractSessionManager
