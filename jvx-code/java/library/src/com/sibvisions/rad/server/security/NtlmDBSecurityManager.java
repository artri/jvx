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
 * 22.10.2008 - [JR] - creation
 * 03.11.2008 - [JR] - support for local (without username, password) and 
 *                     remote (with username, password) login
 * 12.12.2008 - [JR] - moved configuration of NtlmHelper into the helper     
 *                   - used NtlmHelper as singleton
 *                   - support for authentication via DOMAIN\\username     
 * 27.04.2009 - [JR] - validateAuthentication: NullPointerException if the user name was not submitted [BUGFIX]
 * 11.06.2009 - [JR] - removed reference to NtlmHandler and used ObjectCache instead
 * 28.09.2009 - [JR] - ISession instead of AbstractSession                             
 */
package com.sibvisions.rad.server.security;

import java.util.Hashtable;

import javax.rad.remote.IConnectionConstants;
import javax.rad.server.ISession;

import com.sibvisions.rad.server.security.ntlm.NtlmAuthInfo;
import com.sibvisions.rad.server.security.ntlm.NtlmHelper;
import com.sibvisions.util.ObjectCache;
import com.sibvisions.util.log.ILogger;
import com.sibvisions.util.log.LoggerFactory;

/**
 * The <code>NtlmDBSecurityManager</code> extends the {@link DBSecurityManager} and
 * allows a client to authenticate with its domain credentials. This can be done
 * automatically through the browser or manually through a user input. In both cases
 * the user has to exist in the database. Without a valid username in the database,
 * the user can't log on to the application. 
 * 
 * @author René Jahn
 */
public class NtlmDBSecurityManager extends DBSecurityManager
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the logger. */
	private ILogger log = LoggerFactory.getInstance(getClass());

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void validateAuthentication(ISession pSession) throws Exception
	{
		NtlmAuthInfo auth = getAuthInfo(pSession);
		
		if (auth != null)
		{
			//if an nltm authentication is available -> use the username from the information
			//and ignore the sessions username!
			Hashtable<String, Object> htProps = pSession.getProperties();
			htProps.put(IConnectionConstants.USERNAME, auth.getNtlmAuth().getUsername());
			htProps.put(IConnectionConstants.PREFIX_CLIENT + "domain", auth.getNtlmAuth().getDomain());
		}
		else
		{
			String sUserName = pSession.getUserName(); 
			
			if (sUserName != null)
			{
				int iPos = sUserName.indexOf('\\');
						
				if (iPos > 0)
				{
					Hashtable<String, Object> htProps = pSession.getProperties();
					htProps.put(IConnectionConstants.USERNAME, sUserName.substring(iPos + 1));
					htProps.put(IConnectionConstants.PREFIX_CLIENT + "domain", sUserName.substring(0, iPos));
				}
			}
		}
	
		super.validateAuthentication(pSession);
	}
	
	/**
	 * Checks if the database password is valid or the user is authenticated
	 * via ntlm.
	 * 
	 * @param pSession {@inheritDoc}
	 * @param pPassword {@inheritDoc}
	 * @return {@inheritDoc}
	 * @throws Exception {@inheritDoc}
	 */
	@Override
	protected boolean isPasswordValid(ISession pSession, String pPassword) throws Exception
	{
		return super.isPasswordValid(pSession, pPassword) 
		       || isNtlmAuthenticated(pSession);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Checks if the user was authenticated via ntlm. In that case, the
	 * connection can be established.
	 * 
	 * @param pSession the session which needs access 
	 * @return <code>true</code> if the user was authenticated via ntlm, 
	 *         <code>false</code> otherwise
	 */
	protected boolean isNtlmAuthenticated(ISession pSession)
	{
		NtlmAuthInfo auth = getAuthInfo(pSession);
		
		if (auth != null)
		{
			return true;
		}
		else
		{
			NtlmHelper ntlmHelper = NtlmHelper.getInstance();
			ntlmHelper.setApplicatioName(pSession.getApplicationName());
			
			NtlmAuthInfo authInfo = null;
			
			try
			{
				String sLocal = (String)pSession.getProperty(IConnectionConstants.PREFIX_CLIENT + "AuthLocal"); 

				if (sLocal != null)
				{
					//local mode
					authInfo = ntlmHelper.logon(); 
				}
				else
				{
					//remote mode
					authInfo = ntlmHelper.logon((String)pSession.getProperty(IConnectionConstants.PREFIX_CLIENT + "domain"), 
							                    pSession.getUserName(), 
							                    pSession.getPassword());
				}
			}
			catch (Throwable th)
			{
				log.error(th);
			}
			
			return authInfo != null;
		}
	}

	/**
	 * Gets the ntlm authentication information for a session.
	 * 
	 * @param pSession the session which needs access
	 * @return the ntlm information or <code>null</code> if the information is not available
	 */
	private NtlmAuthInfo getAuthInfo(ISession pSession)
	{
		Object oAuthKey = pSession.getProperty(IConnectionConstants.AUTHKEY);
		
		return (NtlmAuthInfo)ObjectCache.get(oAuthKey);
	}
	
}	// NtlmDBSecurityManager
