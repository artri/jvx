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

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.Status;
import org.restlet.security.ChallengeAuthenticator;

import com.sibvisions.rad.server.http.rest.LifeCycleConnector;

/**
 * The <code>BasicAuthenticator</code> is a simple {@link ChallengeAuthenticator} for {@link ChallengeScheme#HTTP_BASIC}.
 * 
 * @author René Jahn
 */
public class BasicAuthenticator extends ChallengeAuthenticator
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of <code>BasicAuthenticator</code>.
	 * 
	 * @param pContext the context
	 */
	public BasicAuthenticator(Context pContext)
	{
		super(pContext, ChallengeScheme.HTTP_BASIC, "Application access");
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
	
	/**
	 * {@inheritDoc}
	 */
	@Override	
	public void challenge(Response pResponse, boolean pStale)
	{
		if (pResponse.getStatus().getCode() != Status.SUCCESS_OK.getCode())
		{
			return;
		}
		
		super.challenge(pResponse, pStale);
	}
	
}	// BasicAuthenticator
