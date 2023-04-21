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
 * 28.09.2009 - [JR] - creation
 */
package apps.firstapp.security;

import java.util.Hashtable;

import javax.rad.remote.IConnectionConstants;
import javax.rad.server.ISession;

import com.sibvisions.rad.server.security.IAccessController;
import com.sibvisions.rad.server.security.ISecurityManager;

/**
 * The <code>HashtableSecurityManager</code> is a {@link Hashtable} 
 * based {@link ISecurityManager} implementation.
 *  
 * @author René Jahn
 */
public class HashtableSecurityManager implements ISecurityManager
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** contains username/password mapping. */
	private Hashtable<String, String> htUsers = new Hashtable<String, String>();
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>HashtableSecurityManager</code> with
	 * predefined users.
	 */
	public HashtableSecurityManager()
	{
		htUsers.put("username", "password");
		htUsers.put("jvx", "jvx");
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public synchronized void validateAuthentication(ISession pSession)
	{
		checkUser(pSession);
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized void changePassword(ISession pSession)
	{
		checkUser(pSession);
		
		//check old password with current password!
		if (!htUsers.get(pSession.getUserName()).equals(pSession.getProperty(IConnectionConstants.OLDPASSWORD)))
		{
			throw new SecurityException("Invalid password");
		}
		
		//user is valid -> change the password
		htUsers.put(pSession.getUserName(), 
                   (String)pSession.getProperty(IConnectionConstants.NEWPASSWORD));
	}
	
	/**
	 * {@inheritDoc}
	 */
	public synchronized void logout(ISession pSession)
	{
	}
	
	/**
	 * {@inheritDoc}
	 */
	public synchronized IAccessController getAccessController(ISession pSession)
	{
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized void release()
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Checks if the user is known and the password is valid.
	 * 
	 * @param pSession the session to authenticate
	 */
	private void checkUser(ISession pSession)
	{
		String sPwd = htUsers.get(pSession.getUserName());
		
		if (sPwd == null)
		{
			throw new SecurityException("User not found!");
		}
		
		if (!sPwd.equals(pSession.getPassword()))
		{
			throw new SecurityException("Invalid password");
		}
	}

}	// HashtableSecurityManager
