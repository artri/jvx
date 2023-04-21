/*
 * Copyright 2021 SIB Visions GmbH
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
 * 17.03.2021 - [JR] - creation
 */
package com.sibvisions.rad.server.http.rest;

import java.io.IOException;

import javax.rad.server.ISession;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sibvisions.rad.server.DirectServerSession;
import com.sibvisions.rad.server.http.HttpContext;
import com.sibvisions.rad.server.security.SecurityContext;

/**
 * The <code>ServerServlet</code> handles lifecycle connections.
 * 
 * @author René Jahn
 */
public class RESTServlet extends FilteredServlet 
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("deprecation")
	@Override
    public void service(HttpServletRequest pRequest, HttpServletResponse pResponse) throws ServletException, IOException 
    {
        HttpContext ctxt = new HttpContext(pRequest, pResponse); 

        try
        {
        	SecurityContext sctxt = new SecurityContext();
        	sctxt.setHidePackages(false);

    		try
    		{
        		RESTServerContextImpl srvctxt = new RESTServerContextImpl();
        		srvctxt.setManaged(true);

        		try
	        	{
        			try
        	    	{
        	    		super.service(pRequest, pResponse);
        	    	}
        	    	finally
        	    	{
        	    		ISession session = srvctxt.getRequestSession();
        	    		
        	    		if (session instanceof DirectServerSession)
        	    		{
        	    			((DirectServerSession)session).destroy();
        	    		}
        	    	}
        		}
	        	finally
	        	{
	        		srvctxt.release();
	        	}
    		}
    		finally
    		{
    			sctxt.release();
    		}	        	
        }
        finally
        {
            ctxt.release();
        }
    }
    
}	// RESTServlet
