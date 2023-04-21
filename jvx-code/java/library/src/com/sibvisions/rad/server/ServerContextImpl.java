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
 * 27.05.2015 - [JR] - #1397: ICallHandler support
 */
package com.sibvisions.rad.server;

/**
 * The <code>ServerContextImpl</code> is an internal {@link javax.rad.server.ServerContext} implementation.
 * 
 * @author René Jahn
 */
class ServerContextImpl extends AbstractServerContext
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /** the server. */
    private Server server;

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Creates a new instance of <code>ServerContextImpl</code>.
     * 
     * @param pServer the server
     */
    ServerContextImpl(Server pServer)
    {
        server = pServer;
    }
    
    /**
     * Creates a new instance of <code>ServerContextImpl</code> for a given
     * system.
     * 
     * @param pServer the server
     * @param pIdentifier the system identifier
     */
    ServerContextImpl(Server pServer, String pIdentifier)
    {
    	super(pIdentifier);
    	
    	server = pServer;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Abstract methods implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    public String getServerIdentifier()
    {
        return server.getInstanceKey();
    }
    
}   // ServerContextImpl
