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
 * 10.06.2016 - [JR] - creation
 */
package com.sibvisions.rad.server;

import javax.rad.server.ISession;

import com.sibvisions.rad.server.security.ISecurityManager;

/**
 * The <code>ISessionValidator</code> is a simple interface for Session validation. It
 * will be used at session construction time. 
 * 
 * @author René Jahn
 */
public interface ISessionValidator
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Method definitions
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Checks if session creation is valid.
     * 
     * @param pSession the session to check
     * @param pSecurityManager the security manager instance
     * @throws Exception if creation should be canceled
     */
    public void validate(ISession pSession, ISecurityManager pSecurityManager) throws Exception;
    
}   // ISessionValidator
