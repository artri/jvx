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
 * 29.03.2022 - [JR] - creation
 */
package com.sibvisions.rad.server.security.mfa.auth;

import javax.rad.remote.IConnectionConstants;
import javax.rad.server.IConfiguration;
import javax.rad.server.ISession;

import com.sibvisions.rad.server.security.AbstractSecurity;
import com.sibvisions.rad.server.security.UserInfo;
import com.sibvisions.rad.server.security.mfa.IMFAuthenticator;
import com.sibvisions.rad.server.security.mfa.MFAHandler;
import com.sibvisions.rad.server.security.mfa.MFAHandler.AccessToken;
import com.sibvisions.util.type.CommonUtil;
import com.sibvisions.util.type.StringUtil;

/**
 * The <code>TextInputMFAuthenticator</code> is a multi-factor authentication implementation
 * for simple text input confirmation.
 * 
 * @author René Jahn
 */
public class TextInputMFAuthenticator extends AbstractSecurity
                                      implements IMFAuthenticator 
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members‚
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** default allowed password characters. */
	public static final String CHARS_PWD = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public void init(ISession pSession, UserInfo pUser) 
	{
		AccessToken token = MFAHandler.createToken(pSession, TYPE_TEXTINPUT);
		
		if (token != null)
		{
			sendConfirmationCode(token, pSession, pUser);
			
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
			checkConfirmationCode(token, pSession);
			
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
	 * Sends a newly created confirmation code.
	 * 
	 * @param pToken the access token
	 * @param pSession the session
	 * @param pUser the user information
	 */
	private void sendConfirmationCode(AccessToken pToken, ISession pSession, UserInfo pUser)
	{
		String sCode = createConfirmationCode(pSession);
		
		try
		{
			IPayloadNotificationHandler handler = createHandler(pSession, pUser);
			
			handler.sendNotification(pSession, pUser, pToken, sCode);
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
				throw (SecurityException)prepareException(new SecurityException("Sending confirmation code failed!", e));
			}
		}
		
		pToken.put("CODE", sCode);
	}
	
	/**
	 * Creates a new confirmation code.
	 * 
	 * @param pSession the session
	 * @return the verification code
	 */
	protected String createConfirmationCode(ISession pSession)
	{
		return StringUtil.createRandomText(CHARS_PWD, 10, 5);
	}
	
	/**
	 * Checks the confirmation code and destroys the access token.
	 * 
	 * @param pToken the access token
	 * @param pSession the session
	 * @throws SecurityException if code is wrong
	 */
	private void checkConfirmationCode(AccessToken pToken, ISession pSession)
	{
		String sCode = (String)pToken.get("CODE");
		
		try
		{
			if (!isConfirmationCodeValid(sCode, (String)pSession.getProperty(IConnectionConstants.MFA_PAYLOAD)))
			{
				throw (SecurityException)prepareException(new SecurityException("Wrong confirmation code!"));				
			}
		}
		finally
		{
			MFAHandler.destroy(pToken);
		}
	}
	
	/**
	 * Gets whether the the confirmation code is the right one (is valid).
	 * 
	 * @param pExpectedCode the expected confirmation code
	 * @param pUsedCode the used code
	 * @return if the used code is the same as the expected code
	 */
	protected boolean isConfirmationCodeValid(String pExpectedCode, String pUsedCode) 
	{
		return CommonUtil.equals(pExpectedCode, pUsedCode);
	}
	
	/**
	 * Creates a new instance of notification handler.
	 * 
	 * @param pSession the notification handler
	 * @param pUser the user information
	 * @return the handler
	 */
	protected IPayloadNotificationHandler createHandler(ISession pSession, UserInfo pUser)
	{
		if (pUser == null)
		{
			throw new SecurityException("User information is missing!");
		}
		
		IConfiguration cfg = pSession.getConfig();
		
		String sCustomHandler = cfg.getProperty("/application/securitymanager/mfa/authenticator/notificationhandler");
		
		if (!StringUtil.isEmpty(sCustomHandler))
		{
			try
			{
				return (IPayloadNotificationHandler)Class.forName(sCustomHandler).newInstance();
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
		else if (!StringUtil.isEmpty(pUser.getEmailAddress()))
		{
			return new DefaultTextInputNotificationHandler();
		}
		else
		{
			if (isHideFailureReason(cfg))
			{
				debug("Can't send confirmation code because no custom handler was set and the email address is empty!");
				
				throw new SecurityException("Sending confirmation code failed!");
			}
			else
			{
				throw new SecurityException("Can't send confirmation code because no custom handler was set and the email address is empty!");
			}
		}		
	}	

}	// TextInputMFAuthenticator
