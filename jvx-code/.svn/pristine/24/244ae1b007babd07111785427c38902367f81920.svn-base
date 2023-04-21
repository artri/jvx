/*
 * Copyright 2023 SIB Visions GmbH
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
 * 27.03.2023 - [JR] - creation
 */
package com.sibvisions.rad.server.http.rest;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.restlet.Response;
import org.restlet.Server;
import org.restlet.engine.adapter.HttpResponse;
import org.restlet.engine.adapter.ServerCall;
import org.restlet.engine.header.HeaderConstants;
import org.restlet.ext.servlet.ServerServlet;
import org.restlet.ext.servlet.internal.ServletCall;

/**
 * The <code>FilteredServlet</code> filters the response before sending it.
 * 
 * @author René Jahn
 */
public class FilteredServlet extends ServerServlet 
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
    protected ServerCall createCall(Server pServer, HttpServletRequest pHttpRequest, HttpServletResponse pHttpResponse) 
    {
        return new ServletCall(pServer, pHttpRequest, pHttpResponse)
		{
        	public void sendResponse(Response pResponse) throws IOException 
        	{
        		if (pResponse instanceof HttpResponse)
        		{
        			((HttpResponse)pResponse).getHttpCall().getResponseHeaders().set(HeaderConstants.HEADER_SERVER, "REST API");
        		}
        		
        		super.sendResponse(pResponse);
        	}
		};
    }
    
}	// ServerServlet
