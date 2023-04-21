/*
 * Copyright 2021 SIB Visions GmbH
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
 * 17.02.2021 - [JR] - creation
 */
package com.sibvisions.rad.server.http.rest;

import javax.rad.server.ISession;
import javax.rad.server.ServerContext;

import com.sibvisions.rad.server.AbstractServerContext;
import com.sibvisions.rad.server.Server;

/**
 * The <code>RESTServerContextImpl</code> is the {@link ServerContext} for REST services.
 * 
 * @author René Jahn
 */
public class RESTServerContextImpl extends AbstractServerContext 
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** the unwrapped session. */
	private ISession session;
	
	/** whether the context is managed by the server. */
	private boolean bManaged = false;
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>RESTServerContextImpl</code>.
	 */
	public RESTServerContextImpl() 
	{
	}
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Abstract methods implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    public String getServerIdentifier()
    {
        return Server.getInstance().getInstanceKey();
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Sets the request session. This is usually not the same as {@link #setSession(ISession)}.
     * 
     * @param pSession the request session
     */
    public void setRequestSession(ISession pSession)
    {
    	//we need the original session for destroy!
    	session = pSession;
    }
    
    /**
     * Gets the request session.
     * 
     * @return the request session
     * @see #setRequestSession(ISession)
     */
    public ISession getRequestSession()
    {
    	return session;
    }
    
    /**
     * Gets whether the context is managed by the server.
     * 
     * @return <code>true</code> if server managed, <code>false</code> otherwise
     */
    @Deprecated
    public boolean isManaged()
    {
    	return bManaged;
    }
    
    /**
     * Sets whether the context is managed by the server.
     * 
     * @param pManaged <code>true</code> if server managed, <code>false</code> otherwise
     */
    @Deprecated
    void setManaged(boolean pManaged)
    {
    	bManaged = pManaged;
    }
    
}	// RESTServerContextImpl
