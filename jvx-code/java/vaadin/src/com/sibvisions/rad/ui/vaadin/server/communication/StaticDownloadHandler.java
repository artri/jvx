/*
 * Copyright 2016 SIB Visions GmbH 
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
 * 25.05.2016 - [JR] - creation
 */
package com.sibvisions.rad.ui.vaadin.server.communication;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.rad.io.IFileHandle;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sibvisions.rad.ui.vaadin.impl.VaadinUI;
import com.sibvisions.rad.ui.vaadin.server.VaadinServlet;
import com.sibvisions.util.IValidatable;
import com.sibvisions.util.ObjectCache;
import com.sibvisions.util.type.ExceptionUtil;
import com.sibvisions.util.type.FileUtil;
import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ApplicationConstants;
import com.vaadin.ui.UI;
import com.vaadin.util.FileTypeResolver;

/**
 * The <code>StaticDownloadHandler</code> serves static content for an application. It uses the {@link ObjectCache} and 
 * a special URLs to detect a static download. This class should be used to bypass the standard vaadin communication 
 * handlers.
 * 
 * @author René Jahn
 */
public final class StaticDownloadHandler
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** the URL path which marks the static download. */
    private static final String PATH_STATIC_DOWNLOAD = "stdld";
    
    /** The save type. */
    public static final String TYPE_SAVE = "0";
    /** The open type. */
    public static final String TYPE_OPEN = "1";

    // APP/stdld/[idcode]/[type]/[file-id]
    /** the resource (URL) pattern. */
    private static final Pattern RESOURCE_PATTERN = Pattern.compile("^/?" + 
                                                                    ApplicationConstants.APP_PATH + '/' +
                                                                    PATH_STATIC_DOWNLOAD + '/' + 
                                                                    "(\\d+)/(\\d)/(.*)");

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Invisible constructor because <code>StaticDownloadHandler</code> is a utility
     * class.
     */
    private StaticDownloadHandler()
    {
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Gets whether the given request is a static download request. The path info of the request
     * will be used to check against a pattern.
     * 
     * @param pRequest the request
     * @return <code>true</code> if request is a static download request, <code>false</code> otherwise
     */
    public static boolean isStaticDownload(HttpServletRequest pRequest)
    {
        return pRequest.getPathInfo() != null && RESOURCE_PATTERN.matcher(pRequest.getPathInfo()).matches();        
    }
    
    /**
     * Sends the data for the static download request. This method doesn't check if the request
     * is a static download request.
     * 
     * @param pRequest the request
     * @param pResponse the response
     * @see #isStaticDownload(HttpServletRequest)
     */
    public static void send(HttpServletRequest pRequest, HttpServletResponse pResponse)
    {
        Matcher matcher = RESOURCE_PATTERN.matcher(pRequest.getPathInfo());
        matcher.matches();
        
        //String sIdentityCode = matcher.group(1);
        String sTypeId = matcher.group(2);
        String sFileId = matcher.group(3);
        
        IFileHandle handle = null;

        Object o = ObjectCache.get(sFileId);

        try
        {
            if (o instanceof ValidatableFileHandle)
            {
                handle = ((ValidatableFileHandle)o).handle;
            }
            else
            {
                Logger.getLogger(VaadinServlet.class.getName()).log(Level.WARNING, 
                                                                    "Ignoring download request for non-existent file " + sFileId);
                
                pResponse.sendError(HttpServletResponse.SC_NOT_FOUND, pRequest.getPathInfo() + " can not be found");
            }
    
            pResponse.setContentType(FileTypeResolver.getMIMEType(handle.getFileName()));
    
            if (TYPE_SAVE.equals(sTypeId))
            {
                pResponse.setHeader("Content-Disposition", "attachment; filename=\"" + handle.getFileName() + "\"");
                pResponse.setHeader("Content-Length", "" + handle.getLength());
            }
           
            FileUtil.copy(handle.getInputStream(), true, pResponse.getOutputStream(), true);
        }
        catch (IOException ioe)
        {
            Logger.getLogger(VaadinServlet.class.getName()).log(Level.WARNING, ExceptionUtil.dump(ioe, true));
            
            pResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Creates an {@link ExternalResource} for a static download.
     * 
     * @param pUI the connected UI
     * @param pFileHandle the file handle which contains the data
     * @param pType the content type (open, view)
     * @return the resource
     * @see #TYPE_OPEN
     * @see #TYPE_SAVE
     */
    public static ExternalResource createResource(UI pUI, IFileHandle pFileHandle, String pType)
    {
        if (pFileHandle != null)
        {
            Object oKey = ObjectCache.put(new ValidatableFileHandle(pUI, pFileHandle));
            
            //app://APP/stdld/[idcode]/[type]/[file-id]
            return new ExternalResource(ApplicationConstants.APP_PROTOCOL_PREFIX + 
                                        ApplicationConstants.APP_PATH + '/' +
                                        PATH_STATIC_DOWNLOAD + '/' + 
                                        VaadinUI.getIdentityCode() + '/' +
                                        pType + '/' + 
                                        oKey);
        }
        
        return null;
    }
    
    //****************************************************************
    // Subclass definition
    //****************************************************************

    /**
     * The <code>ValidatableFileHandle</code> is an {@link IValidatable} for an {@link IFileHandle}. The
     * object will be valid as long as the UI is attached and the timeout is not exceeded.
     * 
     * @author René Jahn
     */
    public static class ValidatableFileHandle implements IValidatable
    {
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Class members
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /** the default timeout for valid downloads (5 minutes). */
        private static final long TIMEOUT = 300000L;
        
        /** the UI. */
        private UI ui;
        /** the file handle. */
        private IFileHandle handle;

        /** the creation time. */
        private long lCreation;
        
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Initialization
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        
        /**
         * Creates a new instance of <code>ValidatableFileHandle</code> for
         * an UI and a file handle.
         * 
         * @param pUI the UI
         * @param pFileHandle the file handle
         */
        public ValidatableFileHandle(UI pUI, IFileHandle pFileHandle)
        {
            ui = pUI;
            handle = pFileHandle;
            
            lCreation = System.currentTimeMillis();
        }

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Interface implementation
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /**
         * {@inheritDoc}
         */
        public boolean isValid()
        {
            try
            {
                return lCreation + TIMEOUT >= System.currentTimeMillis() && ui.isAttached();
            }
            catch (Exception e)
            {
                return false;
            }
        }
        
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // User-defined methods
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /**
         * Gets the cached file handle.
         * 
         * @return the file handle
         */
        public IFileHandle getFileHandle()
        {
            return handle;
        }
        
    }   // ValidatableFileHandle    
    
}   // StaticDownloadHandler
