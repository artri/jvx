/*
 * Copyright 2016 SIB Visions GmbH
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
 * 15.01.2016 - [JR] - creation
 */
package com.sibvisions.rad.server.security;

import javax.rad.server.ISession;

import com.sibvisions.rad.server.Server;

/**
 * The <code>CallHandlerSecurityManager</code> is a simpla "allow everyone" security manager, but
 * adds an Integer object with name <code>customObject</code> and the value <code>199</code> to the session. 
 * 
 * @author René Jahn
 */
public class CallHandlerSecurityManager implements ISecurityManager
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * {@inheritDoc}
     */
    public void validateAuthentication(ISession pSession) throws Exception
    {
        try
        {
            pSession.put("customObject", Integer.valueOf(199));
        }
        catch (Throwable th)
        {
            throw new SecurityException(th);
        }
        
        //important for later checks 
        pSession.getProperties().put("isInitializing", Server.getInstance().getSessionManager().isInitializing(pSession));
    }

    /**
     * {@inheritDoc}
     */
    public void changePassword(ISession pSession) throws Exception
    {
    }

    /**
     * {@inheritDoc}
     */
    public void logout(ISession pSession)
    {
    }

    /**
     * {@inheritDoc}
     */
    public IAccessController getAccessController(ISession pSession) throws Exception
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public void release()
    {
    }

}   // CallHandlerSecurityManager
