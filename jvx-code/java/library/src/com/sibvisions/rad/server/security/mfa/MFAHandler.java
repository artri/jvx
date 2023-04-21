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
package com.sibvisions.rad.server.security.mfa;

import java.util.UUID;

import javax.rad.remote.IConnectionConstants;
import javax.rad.server.ISession;
import javax.rad.type.bean.Bean;

import com.sibvisions.rad.remote.mfa.IMFAConstants;
import com.sibvisions.rad.remote.mfa.MFAException;
import com.sibvisions.util.ObjectCacheInstance;
import com.sibvisions.util.log.LoggerFactory;

/**
 * The <code>MFAHandler</code> is a utility class for multi-factor authentication. 
 * 
 * @author René Jahn
 */
public final class MFAHandler 
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the mfa token cache. */
	private static ObjectCacheInstance oci 		= new ObjectCacheInstance();
	
	/** default timeout: 5 minutes. */
	private static final long TIMEOUT_DEFAULT 	= 300000;
	
	/** the timeout delay for user handling. */
	private static final long TIMEOUT_DELAY 	=  5000;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Invisible constructor because <code>MFAHandler</code> is a utility class.
	 */
	private MFAHandler()
	{
		// No instance needed.
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the access token for the given session.
	 * 
	 * @param pSession the session
	 * @return the access token or <code>null</code> if access token was not found or mfa not initialized
	 * @throws SecurityException if access token was found but is invalid, e.g. timed out
	 */
	public static AccessToken getToken(ISession pSession)
	{
		if (pSession != null)
		{
			Object oToken = pSession.getProperty(IConnectionConstants.MFA_TOKEN);
			
			//a token means -> initialized
			if (oToken != null)
			{
				
				AccessToken token = (AccessToken)oci.get(oToken);

				//check if token is valid
				if (token == null)
				{
					throw new SecurityException("Invalid access token for Multi-factor authentication!");
				}
				
				return token;
			}
		}
		
		return null;
	}
	
	/**
	 * Gets whether multi-factor authentication is used for the given session.
	 * 
	 * @param pSession the session to check
	 * @return <code>false</code> if mfa is used, <code>true</code> otherwise
	 */
	public static boolean isMFAuthentication(ISession pSession)
	{
		return getToken(pSession) != null;
	}
	
	/**
	 * Initializes multi-factor authentication if not already initialized. If it's already
	 * initialized but is timed-out, an exception will be thrown.
	 * 
	 * @param pSession the session to use
	 * @param pType the authentication type
	 * @return MFAException if not already initialized, <code>null</code> otherwise
	 * @throws SecurityException if already initialized but timed out
	 */
	public static AccessToken createToken(ISession pSession, int pType)
	{
		if (!isMFAuthentication(pSession))
		{
			long lTimeout = 0;
			
			String sTimeout = pSession.getConfig().getProperty("/application/securitymanager/mfa/timeout");
			
			if (sTimeout != null)
			{
				try
				{
					lTimeout = Long.parseLong(sTimeout);
				}
				catch (Exception e)
				{
					LoggerFactory.getInstance(MFAHandler.class).error("Timeout for MFA is set but not a valid number!");
					
					lTimeout = -1;
				}
			}
			
			return createToken(pSession, lTimeout, pType);
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * Initializes multi-factor authentication if not already initialized. If it's already
	 * initialized but is timed-out, an exception will be thrown.
	 * 
	 * @param pSession the session to use
	 * @param pType the authentication type
	 * @param pTimeout the token timeout
	 * @return MFAException if not already initialized, <code>null</code> otherwise
	 * @throws SecurityException if already initialized but timed out
	 */
	public static AccessToken createToken(ISession pSession, long pTimeout, int pType)
	{
		if (!isMFAuthentication(pSession))
		{
			long lTimeout = pTimeout;
			
			if (lTimeout <= 0)
			{
				lTimeout = TIMEOUT_DEFAULT;
			}
			
			String sToken = "mfa-" + UUID.randomUUID().toString();
			
			MFAException mfae = new MFAException();
			mfae.setToken(sToken);
			mfae.setTimeout(lTimeout);
			mfae.setType(pType);
			mfae.setState(IMFAConstants.STATE_INIT);

			AccessToken token = new AccessToken(sToken, mfae);

			//5 sec plus because of network traffic, communication, etc...
			oci.put(sToken, token, lTimeout + TIMEOUT_DELAY);

			return token;
		}
		else
		{
			return null;
		}
	}

	/**
	 * Gets whether the given access token is valid.
	 * 
	 * @param pToken the access token
	 * @return <code>true</code> if valid, <code>false</code> if invalid/timed out
	 */
	public static boolean isValid(AccessToken pToken)
	{
		return pToken != null && oci.get(pToken.identifier) != null;
	}
	
	/**
	 * Initializes multi-factor authentication.
	 * 
	 * @param pToken the access token
	 */
	public static void init(AccessToken pToken)
	{
		if (pToken == null)
		{
			throw new SecurityException("Can't init multi-factor authentication without access token!");
		}
			
		if (!pToken.isValid())
		{
			throw new SecurityException("Invalid access token for Multi-factor authentication!");
		}
		
		throw pToken.mfaTrigger;
	}
	
	/**
	 * Destroys the given access token and makes it unusable for authentication.
	 * 
	 * @param pToken the access token
	 */
	public static void destroy(AccessToken pToken)
	{
		if (pToken != null)
		{			
			oci.remove(pToken.identifier);
		}
	}
	
    //****************************************************************
    // Subclass definition
    //****************************************************************

	/**
	 * The <code>AccessToken</code> is the class for handling multi-factor authentication.
	 * It contains the mfa identifier and the mfa trigger exception.
	 * 
	 * @author René Jahn
	 */
	public static final class AccessToken extends Bean
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/** the multi-factor exception. */
		private MFAException mfaTrigger;
		
		/** the multi-factor identifier. */
		private Object identifier;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Creates a new instance of <code>AccessToken</code>.
		 * 
		 * @param pIdentifier the identifier
		 * @param pMFA the exception used for MFA initialization
		 */
		private AccessToken(Object pIdentifier, MFAException pMFA)
		{
			identifier = pIdentifier;
			mfaTrigger = pMFA;
		}

		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// User-defined methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Gets the identifier.
		 * 
		 * @return the identifier
		 */
		public final Object getIdentifier()
		{
			return identifier;
		}
		
		/**
		 * Gets the timeout.
		 * 
		 * @return the timeout
		 */
		public final long getTimeout()
		{
			return mfaTrigger.getTimeout() - MFAHandler.TIMEOUT_DELAY;
		}
		
		/**
		 * Sets the timeout.
		 * 
		 * @param pTimeout the timeout
		 */
		public final void setTimeout(long pTimeout)
		{
			mfaTrigger.setTimeout(pTimeout);
			
			//if we change the timeout of the token, we must update the object cache as well
			//otherwise the timeout doesn't work
			oci.put(identifier, this, pTimeout + TIMEOUT_DELAY);
		}
		
		/**
		 * Gets whether this access token is valid.
		 * 
		 * @return <code>true</code> if valid, <code>false</code> otherwise
		 */
		public final boolean isValid()
		{
			return MFAHandler.isValid(this);
		}

		/**
		 * Sets an additional attribute for the authentication.
		 * 
		 * @param pName the name
		 * @param pValue the value
		 */
		public final void setAttribute(String pName, Object pValue)
		{
			mfaTrigger.put(pName, pValue);
		}
		
		/**
		 * Gets the value of a specific authentication attribute.
		 * 
		 * @param pName the name of the attribute
		 * @return the value
		 */
		public final Object getAttribute(String pName)
		{
			return mfaTrigger.get(pName);
		}
		
	}	// AccessToken
	
}	// MFAHandler
