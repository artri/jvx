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
 * 13.05.2013 - [SW] - creation
 */
package com.sibvisions.rad.ui.vaadin.server.communication;

import java.io.IOException;

import com.sibvisions.util.type.StringUtil;
import com.vaadin.server.UnsupportedBrowserHandler;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinResponse;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;

/**
 * The {@link VaadinUnsupportedBrowserHandler} is an
 * {@link UnsupportedBrowserHandler} extension which can be disabled.
 * <p>
 * It can be controlled by setting the {@link #PARAMETER_NAME parameter} in the
 * {@code web.xml}, it expects a boolean and is by default enabled. Also it can
 * be overriden by setting this parameter as URL parameter on the request.
 * 
 * @author Stefan Wurm
 */
public class VaadinUnsupportedBrowserHandler extends UnsupportedBrowserHandler
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constants
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The name of the parameter for controlling this handler. */
	public static final String PARAMETER_NAME = "checkForUnsupportedBrowser";
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link VaadinUnsupportedBrowserHandler}.
	 */
	public VaadinUnsupportedBrowserHandler()
	{
		super();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean handleRequest(VaadinSession pSession, VaadinRequest pRequest, VaadinResponse pResponse) throws IOException
	{
		pSession.lock();
		
		try
		{
			if (!isActive(pSession, pRequest))
			{
				return false;
			}
		}
		finally
		{
			pSession.unlock();
		}
		
		return super.handleRequest(pSession, pRequest, pResponse);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Checks if this handler is active.
	 * 
	 * @param pSession the current {@link VaadinSession}.
	 * @param pRequest the current {@link VaadinRequest}.
	 * @return {@code true} if this handler is active.
	 */
	private boolean isActive(VaadinSession pSession, VaadinRequest pRequest)
	{
		// First, check if it is set as URL parameter.
		
		String stringValue = pRequest.getParameter(PARAMETER_NAME);
		
		if (!StringUtil.isEmpty(stringValue))
		{
			boolean enabled = Boolean.parseBoolean(stringValue);
			
			// We need to cache the value in the session to make sure that
			// subsequent calls made from the client will not be denied access
			// because they lack the (manual) URL parameter.
			pSession.setAttribute(PARAMETER_NAME, Boolean.valueOf(enabled));
			
			return enabled;
		}
		
		// Then check for a cached value on the session.
		
		if (pSession.getAttribute(PARAMETER_NAME) instanceof Boolean)
		{
			return ((Boolean)pSession.getAttribute(PARAMETER_NAME)).booleanValue();
		}
		
		// When all that failed, let's check the configuration.
		
		stringValue = VaadinServlet.getCurrent().getInitParameter(PARAMETER_NAME);
		
		if (!StringUtil.isEmpty(stringValue))
		{
			return Boolean.parseBoolean(stringValue);
		}
		
		// And finally we give up and return the default.
		
		return true;
	}
	
} // VaadinUnsupportedBrowserHandler 
