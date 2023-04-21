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
 * 05.10.2008 - [JR] - changePassword defined
 * 30.10.2008 - [JR] - changed Parameter lists -> only use AbstractSession
 * 28.09.2009 - [JR] - ISession instead of AbstractSession
 * 07.10.2009 - [JR] - logout defined
 * 06.06.2010 - [JR] - #49: getAccessController defined 
 * 11.05.2011 - [JR] - release defined
 */
package com.sibvisions.rad.server.security;

import javax.rad.server.ISession;

/**
 * The <code>ISecurityManager</code> has methods to check the access
 * of a user to a special application.
 *  
 * @author René Jahn
 */
public interface ISecurityManager
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Validates if a session has valid credentials to access an application.
	 * 
	 * @param pSession the session which needs access
	 * @throws Exception if the access is denied (invalid username or password, password needs to be changed, ...)
	 */
	public void validateAuthentication(ISession pSession) throws Exception;
	
	/**
	 * Changes the password for a user.
	 * 
	 * @param pSession the session which wants to change the password
	 * @throws Exception if it's not possible to change the password
	 */
	public void changePassword(ISession pSession) throws Exception;
	
	/**
	 * Performs a manual or automatic logout.
	 * 
	 * @param pSession the session which performs the logout
	 */
	public void logout(ISession pSession);
	
	/**
	 * Gets the access controller for a session. The controller handles the access
	 * to server side objects.
	 * 
	 * @param pSession the session for which the access controller is needed
	 * @return the access controller or <code>null</code> if no access controller should be used
	 * @throws Exception if the access controller could not be created
	 */
	public IAccessController getAccessController(ISession pSession) throws Exception;
	
	/**
	 * Releases all used resources.
	 */
	public void release();
	
}	// ISecurityManager
