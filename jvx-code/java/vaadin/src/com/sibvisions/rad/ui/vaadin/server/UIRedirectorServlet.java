/*
 * Copyright 2014 SIB Visions GmbH
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
 * 29.11.2014 - [JR] - creation
 */
package com.sibvisions.rad.ui.vaadin.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sibvisions.util.type.StringUtil;

/**
 * The <code>UIRedirectorServlet</code> is a simple servlet that redirects to the vaadin UI. It appends
 * the configured path to the URI, if needed.
 * 
 * @author René Jahn
 */
public class UIRedirectorServlet extends HttpServlet
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void doGet(HttpServletRequest pRequest, HttpServletResponse pResponse) throws ServletException, 
                                                                                         IOException
    {
        String sPath = getServletConfig().getInitParameter("redirectPath");

        if (sPath != null)
        {
            String sURI = pRequest.getRequestURI();
            
            if (sURI.endsWith("index.html"))
            {
                sURI = sURI.substring(0, sURI.length() - 10);
            }
            
            if (!sURI.endsWith("/"))
            {
                sURI += "/";
            }
            
            if (sPath.startsWith("/"))
            {
                sPath = sPath.substring(1);
            }
            
            if (!sPath.endsWith("/"))
            {
                sPath += "/";
            }
            
            String sQuery = pRequest.getQueryString();
            
            if (!StringUtil.isEmpty(sQuery))
            {
                sQuery = "?" + sQuery;
            }
            else
            {
                sQuery = "";
            }
            
            pResponse.sendRedirect(sURI + sPath + sQuery);
        }
    }
    
}   // UIRedirectorServlet
