/*
 * Copyright 2013 SIB Visions GmbH 
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
 * 01.03.2013 - [TK] - creation
 */
package com.sibvisions.rad.ui.vaadin.server;

import java.io.IOException;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import com.sibvisions.rad.server.http.HttpContext;
import com.vaadin.server.DeploymentConfiguration;
import com.vaadin.server.ServiceException;

/**
 * The <code>VaadinPortlet</code> handles portlet requests.
 * 
 * @author Thomas Krautinger
 */
public class VaadinPortlet extends com.vaadin.server.VaadinPortlet
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected VaadinPortletService createPortletService(DeploymentConfiguration pDeploymentConfiguration) throws ServiceException
	{
        VaadinPortletService service = new VaadinPortletService(this, pDeploymentConfiguration);
        service.init();

        return service;
    }
	
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("deprecation")
    @Override
    protected void handleRequest(PortletRequest pRequest, PortletResponse pResponse) throws PortletException, IOException 
    {
        HttpContext ctxt = new HttpContext(pRequest, pResponse);
        
        try
        {
            super.handleRequest(pRequest, pResponse);
        }
        finally
        {
            ctxt.release();
        }
    }

} 	// VaadinPortlet
