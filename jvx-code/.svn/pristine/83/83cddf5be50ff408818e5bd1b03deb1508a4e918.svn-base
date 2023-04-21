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
 * 29.09.2022 - [JR] - creation
 */
package com.sibvisions.rad.server.security.mfa.auth;

import javax.rad.remote.IConnectionConstants;
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
 * The <code>MultiWaitMFAuthenticator</code> uses multiple multi-factor triggers. The first trigger inits a wait without confirmation code.
 * After some seconds, a confirmation code is created and a new init sends the confirmation code.
 * 
 * <code>
 * The use case is: Standard user authentication        = Factor 1 
 *                  -> wait for code generation 
 *                  -> Accept (e.g. in an external App) = Factor 2 
 *                  -> Code genration 
 *                  -> Accept (in Application)          = Factor 3
 * </code>                 
 * 
 * @author René Jahn
 */
public class MultiWaitMFAuthenticator extends AbstractSecurity
 									  implements IMFAuthenticator  
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members‚
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the counter. */
	private int iValidationCount = 0;
	
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
			iValidationCount = 0;
			
			System.out.println("Init MFA");
			
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
			if (iValidationCount == 10)
			{
				String sExpected = (String)token.get("CODE");
				String sUserCode = (String)pSession.getProperty(IConnectionConstants.MFA_PAYLOAD);
				
				if (!StringUtil.isEmpty(sUserCode)
					&& CommonUtil.equals(sExpected, sUserCode))
				{
					System.out.println("Authentication successful with code " + sExpected);
					return true;
				}
			}
			
			iValidationCount++;

			MFAException mfae = new MFAException();
			mfae.setToken(token.getIdentifier());
			mfae.setType(MFAException.TYPE_WAIT);
			
			if (iValidationCount == 3)
			{
				String sCode = StringUtil.createRandomText(WaitMFAuthenticator.CHARS_PAYLOAD, 8, 0);
				
				//update token as well, because the code is relevant for later check
				token.put("CODE", sCode);
				
				System.out.println("Send init with payload: " + sCode);
				
				mfae.put(IMFAConstants.PROP_PAYLOAD, sCode);
				mfae.setState(MFAException.STATE_INIT);
			}
			else
			{
				//don't send code in RETRY
				//mfae.put(IMFAConstants.PROP_PAYLOAD, token.get("CODE"));
				
				System.out.println("Send wait retry...");
				
				mfae.setState(MFAException.STATE_RETRY);
			}
			
			throw mfae;
		}
		else
		{
			return false;
		}
	}

}	// MultiWaitMFAuthenticator
