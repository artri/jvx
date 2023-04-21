/*
 * Copyright 2022 SIB Visions GmbH
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
 * 02.04.2022 - [JR] - creation
 */
package com.sibvisions.rad.server.security.mfa.auth;

import javax.rad.remote.IConnectionConstants;
import javax.rad.server.IConfiguration;
import javax.rad.server.ISession;

import com.sibvisions.rad.remote.mfa.IMFAConstants;
import com.sibvisions.rad.remote.mfa.MFAException;
import com.sibvisions.rad.server.security.AbstractSecurity;
import com.sibvisions.rad.server.security.UserInfo;
import com.sibvisions.rad.server.security.mfa.IMFAuthenticator;
import com.sibvisions.rad.server.security.mfa.MFAHandler;
import com.sibvisions.rad.server.security.mfa.MFAHandler.AccessToken;
import com.sibvisions.util.type.CommonUtil;
import com.sibvisions.util.type.StringUtil;

/**
 * The <code>WaitMFAuthenticator</code> is a multi-factor authentication implementation for
 * a wait process. It's necessary to confirm the authentication with a different mechanism.
 * 
 * @author René Jahn
 */
public class WaitMFAuthenticator extends AbstractSecurity
                                 implements IMFAuthenticator 
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members‚
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** default allowed tan characters. */
	public static final String CHARS_PAYLOAD = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public void init(ISession pSession, UserInfo pUser) 
	{
		AccessToken token = MFAHandler.createToken(pSession, TYPE_WAIT);
		
		if (token != null)
		{
			sendMatchingCode(token, pSession, pUser);
			
			try
			{
				MFAHandler.init(token);
			}
			catch (SecurityException se)
			{
				throw (SecurityException)prepareException(se);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean validate(ISession pSession) 
	{
		AccessToken token;
		
		try
		{
			token = MFAHandler.getToken(pSession);
		}
		catch (SecurityException se)
		{
			throw (SecurityException)prepareException(se);
		}
		
		if (token != null)
		{
			checkConfirmation(token, pSession);
			
			return true;
		}
		else
		{
			return false;
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Sends a newly created matching code.
	 * 
	 * @param pToken the access token
	 * @param pSession the session
	 * @param pUser the user information
	 */
	private void sendMatchingCode(AccessToken pToken, ISession pSession, UserInfo pUser)
	{
		String sMatchingCode = createMatchingCode(pSession);

		pToken.setAttribute(IMFAConstants.PROP_PAYLOAD, sMatchingCode);
		
		try
		{
			IWaitNotificationHandler handler = createHandler(pSession, pUser);

			pToken.put("HANDLER", handler);
			
			handler.sendNotification(pSession, pUser, pToken, sMatchingCode);
		}
		catch (Exception e)
		{
			error(e);
			
			if (e instanceof SecurityException)
			{
				throw (SecurityException)prepareException(e);
			}
			else
			{
				throw (SecurityException)prepareException(new SecurityException("Sending matching code failed!", e));
			}
		}
		
		pToken.put("MATCHING_CODE", sMatchingCode);
	}
	
	/**
	 * Creates a new matching code.
	 * 
	 * @param pSession the session
	 * @return the matching code
	 */
	protected String createMatchingCode(ISession pSession)
	{
		return StringUtil.createRandomText(CHARS_PAYLOAD, 8, 0);
	}
	
	/**
	 * Checks if authentication is confirmed.
	 * 
	 * @param pToken the access token
	 * @param pSession the session
	 */
	private void checkConfirmation(AccessToken pToken, ISession pSession) 
	{
		String sMatchingCode = (String)pToken.get("MATCHING_CODE");
		
		if (CommonUtil.equals(sMatchingCode, (String)pSession.getProperty(IConnectionConstants.MFA_PAYLOAD)))
		{
			IWaitNotificationHandler handler = (IWaitNotificationHandler)pToken.get("HANDLER");
			
			if (handler != null)
			{
				if (handler.isConfirmed(pSession, pToken, sMatchingCode))
				{
					MFAHandler.destroy(pToken);
					
					return;
				}

				MFAException mfae = new MFAException();
				mfae.setToken(pToken.getIdentifier());
				mfae.setType(MFAException.TYPE_WAIT);
				mfae.setState(MFAException.STATE_RETRY);
				
				throw mfae;
			}
			else
			{
				MFAHandler.destroy(pToken);
				
				throw (SecurityException)prepareException(new SecurityException("Notification handler was not created!"));
			}
		}
		else
		{
			MFAHandler.destroy(pToken);

			if (isHideFailureReason(pSession.getConfig()))
			{
				throw (SecurityException)prepareException(new SecurityException("Confirmation failed!"));
			}
			else
			{
				throw (SecurityException)prepareException(new SecurityException("Invalid matching code!"));
			}
		}
	}
	
	/**
	 * Creates a new instance of notification handler.
	 * 
	 * @param pSession the notification handler
	 * @param pUser the user info
	 * @return the handler
	 */
	protected IWaitNotificationHandler createHandler(ISession pSession, UserInfo pUser)
	{
		IConfiguration cfg = pSession.getConfig();
		
		String sCustomHandler = cfg.getProperty("/application/securitymanager/mfa/authenticator/notificationhandler");
		
		if (!StringUtil.isEmpty(sCustomHandler))
		{
			try
			{
				return (IWaitNotificationHandler)Class.forName(sCustomHandler).newInstance();
			}
			catch (ClassNotFoundException cnfe)
			{
				if (isHideFailureReason(cfg))
				{
					debug("Notification handler '", sCustomHandler, "' was not found!");
					
					throw new SecurityException("Can't create notification handler!");
				}
				else
				{
					throw new SecurityException("Notification handler '" + sCustomHandler + "' was not found!");
				}
			}
			catch (InstantiationException ie)
			{
				if (isHideFailureReason(cfg))
				{
					debug("Can't instantiate notification handler '", sCustomHandler, "'!");
					
					throw new SecurityException("Can't create notification handler!");
				}
				else
				{
					throw new SecurityException("Can't instantiate notification handler '" + sCustomHandler + "'!");
				}
			}
			catch (IllegalAccessException iae)
			{
				if (isHideFailureReason(cfg))
				{
					debug("Notification handler '", sCustomHandler, "' not accessible!");
					
					throw new SecurityException("Can't create notification handler!");
				}
				else
				{
					throw new SecurityException("Notification handler '" + sCustomHandler + "' not accessible!");
				}
			}
		}
		else
		{
			if (isHideFailureReason(cfg))
			{
				debug("Can't create handler because no handler is configured!");
				
				throw new SecurityException("Can't create notification handler!");
			}
			else
			{
				throw new SecurityException("Can't create handler because no handler is configured!");
			}
		}
	}	

}	// WaitMFAuthenticator
