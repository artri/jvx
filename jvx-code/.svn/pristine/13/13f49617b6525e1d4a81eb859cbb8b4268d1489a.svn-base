/*
 * Copyright 2018 SIB Visions GmbH
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
 * 03.12.2018 - [JR] - creation
 */
package com.sibvisions.rad.server.http.rest.security;

import java.util.logging.Level;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Status;
import org.restlet.security.Authenticator;
import org.restlet.security.Verifier;

import com.sibvisions.rad.server.http.rest.LifeCycleConnector;

/**
 * The <code>ForwardAuthenticator</code> is a simple {@link Authenticator} for authentication with a simple {@link Verifier}.
 * 
 * @author René Jahn
 */
public class ForwardAuthenticator extends Authenticator
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** the verifier to use. */
	private Verifier verifier;
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of <code>ForwardAuthenticator</code>.
	 * 
	 * @param pContext the context
	 */
	public ForwardAuthenticator(Context pContext)
	{
		super(pContext);
	}
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Abstract methods implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	protected boolean authenticate(Request pRequest, Response pResponse) 
	{
		boolean loggable = pRequest.isLoggable() && getLogger().isLoggable(Level.FINE);
		
		
		if (verifier != null)
		{
			int iVerification = verifier.verify(pRequest, pResponse);
			
			if (iVerification == Verifier.RESULT_VALID)
			{
				if (loggable)
				{
					getLogger().fine("Authentication succeeded. Valid credentials provided.");
				}
				
				return true;
			}
			else
			{
				if (!(super.getNext() instanceof Authenticator))
				{
					if (loggable)
					{
		               getLogger().log(Level.FINE, "Authentication or authorization failed for this URI: " + pResponse.getRequest().getResourceRef());
					}
					
					pResponse.setStatus(Status.CLIENT_ERROR_FORBIDDEN);
				}

				return false;
			}
		}
		
		if (loggable)
		{
			getLogger().fine("Authentication failed. Invalid credentials provided.");
		}		
		
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected int unauthenticated(Request pRequest, Response pResponse)
	{
		//if response is marked as errornous, don't continue
		if (pResponse.getStatus().getCode() != Status.SUCCESS_OK.getCode())
		{
			return super.unauthenticated(pRequest, pResponse);
		}

		if (super.getNext() instanceof Authenticator)
		{
			//the forwarder shouldn't stop the authentication mechanism. If there's another authenticator as next
			return CONTINUE;
		}
		else
		{
			return super.unauthenticated(pRequest, pResponse);
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("deprecation")
	@Override
	protected void afterHandle(Request pRequest, Response pResponse)
	{
		try
		{
			super.afterHandle(pRequest, pResponse);
		}
		finally
		{
			LifeCycleConnector.destroy(pRequest);
		}
	}
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	

	/**
	 * Sets the credentials verifier.
	 * 
	 * @param pVerifier the credentials verifier
	 */
	public void setVerifier(Verifier pVerifier)
	{
		verifier = pVerifier;
	}
	
	/**
	 * Gets the credentials verifier.
	 * 
	 * @return the credentials verifier
	 */
	public Verifier getVerifier()
	{
		return verifier;
	}
	
}	// ForwardAuthenticator
