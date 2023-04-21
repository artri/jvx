/*
 * Copyright 2015 SIB Visions GmbH 
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
 * 16.02.2015 - [TK] - creation
 * 17.02.2015 - [JR] - ServiceUtil used
 */
package com.sibvisions.rad.ui.vaadin.server.communication;

import com.sibvisions.rad.ui.vaadin.server.ServiceUtil;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.communication.ServletBootstrapHandler;

import elemental.json.JsonObject;

/**
 * The <code>VaadinServletBootstrapHandler</code> extends the {@link ServletBootstrapHandler} and
 * modifies the application parameters before sending it back to the client.
 * 
 * @author Thomas Krautinger
 */
public class VaadinServletBootstrapHandler extends ServletBootstrapHandler
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Creates a new instance of <code>VaadinServletBootstrapHandler</code>.
     */
    public VaadinServletBootstrapHandler()
    {
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Appends additional application parameters.
     * 
     * @param pContext {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    @SuppressWarnings("deprecation")
    protected JsonObject getApplicationParameters(BootstrapContext pContext)
    {
    	JsonObject parameters = super.getApplicationParameters(pContext);

    	return ServiceUtil.configureApplicationParameter(pContext.getRequest().getService(), parameters);
    }
    
    /**
     * Gets the theme name from servlet configuration, if available and not configured as URL parameter.
     * 
     * @return the theme name
     */
    @Override
    @SuppressWarnings("deprecation")    
    public String getThemeName(BootstrapContext pContext) 
    {
        String sTheme = pContext.getRequest().getParameter(VaadinServlet.URL_PARAMETER_THEME);
        
        if (sTheme == null) 
        {
            VaadinServlet servlet = VaadinServlet.getCurrent();
            
            sTheme = servlet.getInitParameter(VaadinServlet.URL_PARAMETER_THEME);
            
            if (sTheme == null)
            {
                sTheme = super.getThemeName(pContext);
            }
        }
        
        return sTheme;
    }
    
}   // VaadinServletBootstrapHandler
