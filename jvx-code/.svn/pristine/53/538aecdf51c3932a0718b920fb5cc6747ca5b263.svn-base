/*
 * Copyright 2015 SIB Visions GmbH
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
 * 01.03.2015 - [JR] - creation
 */
package com.sibvisions.rad.server.security;

import javax.rad.remote.IConnectionConstants;
import javax.rad.server.ISession;

/**
 * The <code>NoSecurityManager</code> is a dummy security manager that does nothing. Use it
 * in environments where you don't need authentication.
 * 
 * @author René Jahn
 */
public class NoSecurityManager implements ISecurityManager
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Creates a new instance of <code>NoSecurityManager</code>.
     */
    public NoSecurityManager()
    {
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * {@inheritDoc}
     */
    public void validateAuthentication(ISession pSession) throws Exception
    {
		pSession.getProperties().put(IConnectionConstants.SECURITY_ENVIRONMENT, AbstractSecurityManager.getEnvironment(pSession.getConfig()));
    }

    /**
     * {@inheritDoc}
     */
    public void changePassword(ISession pSession) throws Exception
    {
        throw new UnsupportedOperationException("Changing password is not possible!");
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
    
}   // NoSecurityManager
