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
 * 29.03.2014 - [JR] - creation
 */
package com.sibvisions.rad.ui.vaadin.server.communication;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import com.sibvisions.rad.ui.vaadin.ext.IDownloadStream;
import com.sibvisions.rad.ui.vaadin.ext.VaCachedResource;
import com.sibvisions.rad.ui.vaadin.impl.VaadinUI;
import com.vaadin.server.DownloadStream;
import com.vaadin.server.RequestHandler;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinResponse;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ApplicationConstants;

/**
 * The <code>VaadinCachedResourceRequestHandler</code> is a special {@link RequestHandler} that serves
 * {@link VaCachedResource}s. The difference to {@link com.vaadin.server.ConnectorResourceHandler} is that resources,
 * like images, will be re-used per UI.
 * 
 * @author René Jahn
 */
public class VaadinCachedResourceRequestHandler implements RequestHandler
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    // APP/cachedresource/[idcode]/[cachekey]/[filename.xyz]
    /** the resource (URL) pattern. */
    private static final Pattern RESOURCE_PATTERN = Pattern.compile("^/?" + 
                                                                    ApplicationConstants.APP_PATH + '/' + 
                                                                    VaCachedResource.RESOURCE_PATH + '/' + 
                                                                    "(\\d+)/(\\d+)/(.*)");
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Creates a new instance of <code>VaadinCachedResourceRequestHandler</code>.
     */
    public VaadinCachedResourceRequestHandler()
    {
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean handleRequest(VaadinSession pSession, VaadinRequest pRequest, VaadinResponse pResponse) throws IOException
    {
        String requestPath = pRequest.getPathInfo();
        
        if (requestPath == null) 
        {
            return false;
        }
        
        Matcher matcher = RESOURCE_PATTERN.matcher(requestPath);
        
        if (!matcher.matches()) 
        {
            return false;
        }
        
        //String sIdentityCode = matcher.group(1);
        String sCacheKey = matcher.group(2);

        pSession.lock();
        
        IDownloadStream download;
        
        try 
        {
            download = VaadinUI.getCachedResource(sCacheKey);
            
            if (download == null) 
            {
                return error(pRequest, pResponse, "Ignoring cachedresource request for non-existent resource " + sCacheKey);
            }

        } 
        finally 
        {
            pSession.unlock();
        }

        DownloadStream stream = download.getStream();

        stream.writeResponse(pRequest, pResponse);

        return true;
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Loggs an error.
     * 
     * @param pRequest the request
     * @param pResponse the response
     * @param pMessage the log message
     * @return <code>true</code> means request was handled
     * @throws IOException if setting error to response fails 
     */
    private static boolean error(VaadinRequest pRequest, VaadinResponse pResponse, String pMessage) throws IOException 
    {
        Logger.getLogger(VaadinCachedResourceRequestHandler.class.getName()).log(Level.WARNING, pMessage);
        pResponse.sendError(HttpServletResponse.SC_NOT_FOUND, pRequest.getPathInfo() + " can not be found");
        
        // Request handled (though not in a nice way)
        return true;
    }
    
}   // VaadinCachedResourceRequestHandler
