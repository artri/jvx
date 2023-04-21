/*
 * Copyright 2020 SIB Visions GmbH
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
 * 27.03.2020 - [JR] - creation
 */
package com.sibvisions.rad.server.http.rest;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.Method;
import org.restlet.engine.application.CorsFilter;
import org.restlet.engine.header.HeaderConstants;
import org.restlet.routing.Filter;

/**
 * The <code>AuthByPassCorsFilter</code> is a {@link CorsFilter} which skips the authentication OPTIONS
 * request and handles the "real" request as CORS request. 
 * 
 * @author René Jahn
 */
public class AuthByPassCorsFilter extends CorsFilter 
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>AuthByPassCorsFilter</code> for given context.
	 * 
	 * @param pContext the context
	 */
	public AuthByPassCorsFilter(Context pContext)
	{
		super(pContext);
	}
	
	/**
	 * Creates a new instance of <code>AuthByPassCorsFilter</code> for given context
	 * and "next" restlet.
	 * 
	 * @param pContext the context
	 * @param pNext the next restlet
	 */
	public AuthByPassCorsFilter(Context pContext, Restlet pNext)
	{
		super(pContext, pNext);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected int beforeHandle(Request request, Response response) 
	{
		//IF we use: setSkippingResourceForCorsOptions
		//everything would work without extra code, but OPTIONS request for services won't work
		//so we have to work-around to support real OPTIONS requests

		//A) GET Request
		//
		//Browser sends an OPTION request for authorization and a second request for the real METHOD (like GET)
		//The second method is defined in "Access-Control-Request-Method" header of first request
		//We ignore the first request and handle the second request
		
		//B) OPTIONS Request
		//
		//The OPTIONS request is special, because the first and second request METHODS are same and are detected
		//as preflight request. A preflight request is specific in CorsResponseHelper, so we have to work-around
		//in afterHandle
		
		if (request.getMethod().equals(Method.OPTIONS) 
			&& (request.getClientInfo() == null || !request.getClientInfo().isAuthenticated())
			&& getCorsResponseHelper().isCorsRequest(request))
		{
			if (request.getHeaders().getFirst(HeaderConstants.HEADER_AUTHORIZATION, true) == null)
			{
    			Set<String> accHeaders = request.getAccessControlRequestHeaders();
    			
    			if (request.getAccessControlRequestMethod() != null)
    			{
					boolean bSkip = !request.getMethod().equals(request.getAccessControlRequestMethod()); 
	
					if (!bSkip && accHeaders != null)
	    			{
	    				for (String head : accHeaders)
	    				{
	    					if (HeaderConstants.HEADER_AUTHORIZATION.equalsIgnoreCase(head))
	    					{
	    						bSkip = true;
	    						break;
	    					}
	    				}
	    			}
					
					if (bSkip)
					{
						getLogger().info("SKIP authorization request for CORS");
							
	        			response.setAllowedMethods(new HashSet<Method>(Arrays.asList(Method.GET, Method.POST, Method.PUT, Method.DELETE, Method.OPTIONS)));
	        			return Filter.SKIP;
					}
    			}
			}
		}
		
		return super.beforeHandle(request, response);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void afterHandle(Request request, Response response) 
	{
		//If we have a CORS request and no access-control-request-method -> do the same as the super call, but
		//without preflight check in case of an OPTION request
		if (getCorsResponseHelper().isCorsRequest(request)
			&& request.getAccessControlRequestMethod() == null
			&& request.getMethod().equals(Method.OPTIONS))
		{
			getLogger().info("ByPass default CORS handling");
			
			String sOrigin = request.getHeaders().getFirstValue("Origin", true);
			
	        // Header 'Allow' is not relevant in CORS request.
	        response.getAllowedMethods().clear();

	        if (!getAllowedOrigins().contains("*") && !getAllowedOrigins().contains(sOrigin)) 
	        {
	            // Origin not allowed
	            getLogger().fine("Origin " + sOrigin + " not allowed for CORS request");
	            
	            return;
	        }
			
            // Header 'Access-Control-Expose-Headers'
            if (getExposedHeaders() != null && !getExposedHeaders().isEmpty()) 
            {
                response.setAccessControlExposeHeaders(getExposedHeaders());
            }
            
            if (isAllowedCredentials()) 
            {
                response.setAccessControlAllowCredentials(Boolean.TRUE);
            }
            
            // Header 'Access-Control-Allow-Origin'
            if (!isAllowedCredentials() && getAllowedOrigins().contains("*")) 
            {
                response.setAccessControlAllowOrigin("*");
            } 
            else 
            {
                response.setAccessControlAllowOrigin(sOrigin);
            }
		}
		else
		{
			super.afterHandle(request, response);
		}
	}

}	// AuthByPassCorsFilter
