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
 * 17.02.2015 - [JR] - creation
 */
package com.sibvisions.rad.ui.vaadin.server;

import com.vaadin.server.DeploymentConfiguration;
import com.vaadin.server.SystemMessagesProvider;
import com.vaadin.server.VaadinService;

import elemental.json.JsonObject;

/**
 * The <code>ServiceUtil</code> is a simple utility class for {@link VaadinService}.
 * 
 * @author René Jahn
 */
public final class ServiceUtil
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** the system message provider (singleton instance). */
    private static SystemMessagesProvider messageProvider; 
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Invisible constructor because <code>ServiceUtil</code> is a utility
     * class.
     */
    private ServiceUtil()
    {
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Appends additional application parameters for message handling. The property names are <code>communicationerrordetails</code>,
     * <code>authenticationerrordetails</code> and <code>sessionexpirederrordetails</code>. The values are boolean (true, false).
     * 
     * @param pService the vaadin service (servlet or portlet)
     * @param pParameters the original parameters
     * @return the original with additional parameters
     */
    public static JsonObject configureApplicationParameter(VaadinService pService, JsonObject pParameters)
    {
        DeploymentConfiguration configuration = pService.getDeploymentConfiguration();
        
        //Be careful with changes, because com.sibvisions.rad.ui.vaadin.client.ApplicationConnection uses the same constant values
        pParameters.put("comErrMsgDetails", Boolean.valueOf(configuration.getApplicationOrSystemProperty("communicationerrordetails", "true")).booleanValue());
        pParameters.put("authErrMsgDetails", Boolean.valueOf(configuration.getApplicationOrSystemProperty("authenticationerrordetails", "true")).booleanValue());
        pParameters.put("sessExpMsgDetails", Boolean.valueOf(configuration.getApplicationOrSystemProperty("sessionexpirederrordetails", "true")).booleanValue());
        
        return pParameters;
    }
    
    /**
     * Configures the system messages provider, if needed and set via application or system property. The property name
     * is <code>systemmessagesprovider</code>. The value has to be a full qualified java class name.
     * 
     * @param pService the vaadin service (servlet or portlet)
     */
    public static void configureSystemMessagesProvider(VaadinService pService)
    {
        //DON'T set null, because it will fail!
        
        if (messageProvider == null)
        {
            //No need to synchronize creation - it's only a small tuning to use a singleton instance
            String systemMessagesClassName = pService.getDeploymentConfiguration().getApplicationOrSystemProperty("systemmessagesprovider", null);
            
            if (systemMessagesClassName != null)
            {
                try
                {
                    Class<?> clazz = Class.forName(systemMessagesClassName);
                    
                    messageProvider = (SystemMessagesProvider)clazz.newInstance();
                    
                    pService.setSystemMessagesProvider(messageProvider);
                }
                catch (Throwable thr)
                {
                    thr.printStackTrace();
                }
            }
        }
        else
        {
            pService.setSystemMessagesProvider(messageProvider);
        }
    }
    
}   // ServiceUtil
