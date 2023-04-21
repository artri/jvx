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
 * 23.09.2011 - [JR] - creation
 */
package com.sibvisions.rad.server.security;

import javax.rad.server.ISession;

/**
 * The <code>DenySecurityManager</code> is a test security manager which does not allow to create
 * a new connection.
 * 
 * @author René Jahn
 */
public class DenySecurityManager extends AbstractSecurityManager
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	
	public void validateAuthentication(ISession pSession) throws Exception
	{
		pSession.setProperty("property.new", "SENT");
		
		throw new Exception("Denied");
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
	public IAccessController getAccessController(ISession pSession) throws Exception
	{
		return null;
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
	public void release()
	{
	}

}	// DenySecurityManager
