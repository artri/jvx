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
 * 04.04.2014 - [JR] - creation
 * 16.02.2015 - [TK] - #1275: adding handling for additional application parameters and error details
 */
package com.sibvisions.rad.ui.vaadin.ext.ui.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.sibvisions.rad.ui.vaadin.ext.ui.client.panel.LayoutedPanelConnector;
import com.vaadin.client.ApplicationConfiguration;
import com.vaadin.client.WidgetSet;

/**
 * The <code>ApplicationConnection</code> extends the default {@link com.vaadin.client.ApplicationConnection}
 * and hides specific "communication problem" messages.
 * 
 * @author René Jahn
 */
public class ApplicationConnection extends com.vaadin.client.ApplicationConnection
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** the flag indicates whether the communication error details are visible or hidden. */
	private boolean showCommunicationErrorDetails = true;
	
	/** the flag indicates whether the authentication error details are visible or hidden. */
	private boolean showAuthenticationErrorDetails = true;
	
	/** the flag indicates whether the session expired error details are visible or hidden. */
	private boolean showSessionExpiredErrorDetails = true;
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Creates a new instance of <code>ApplicationConnection</code>.
     */
    public ApplicationConnection()
    {
		addHandler(ResponseHandlingStartedEvent.TYPE, new ApplicationConnection.CommunicationHandler() 
		{
			@Override
			public void onResponseHandlingStarted(ResponseHandlingStartedEvent e) 
			{
				LayoutedPanelConnector.layoutChanged("ResponseHandlingStarted");  // Reset layout calculation when new request is handled.
			}
			@Override
			public void onResponseHandlingEnded(ResponseHandlingEndedEvent e) 
			{
			}
			@Override
			public void onRequestStarting(RequestStartingEvent e) 
			{
			}
		});
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void init(WidgetSet pWidgetSet, ApplicationConfiguration pConfiguration)
    {
    	super.init(pWidgetSet, pConfiguration);
    	
    	// Load additional configuration parameters
    	JsConfiguration jsConfiguration = getJsConfiguration(pConfiguration.getRootPanelId());
    	
    	showCommunicationErrorDetails = jsConfiguration.getBoolean("comErrMsgDetails") != Boolean.FALSE;
    	showAuthenticationErrorDetails = jsConfiguration.getBoolean("authErrMsgDetails") != Boolean.FALSE;
    	showSessionExpiredErrorDetails = jsConfiguration.getBoolean("sessExpMsgDetails") != Boolean.FALSE;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void showCommunicationError(String pDetails, int pStatusCode)
    {
    	//not the best solution, but no chance to overwrite the private method where this happens!
        if (pStatusCode >= 0
            && !getConfiguration().isStandalone() 
            && pDetails != null && pDetails.indexOf(" - Original JSON-text:") >= 0)
        {
            return;
        }
        else
        {
        	String details = pDetails;
        	
        	if (!showCommunicationErrorDetails)
        	{
        		details = null;
        	}
        	
            super.showCommunicationError(details, pStatusCode);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void showAuthenticationError(String pDetails)
    {
    	String details = pDetails;
    	
    	if (!showAuthenticationErrorDetails)
    	{
    		details = null;
    	}
    	
    	super.showAuthenticationError(details);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void showSessionExpiredError(String pDetails)
    {
    	String details = pDetails;
    	
    	if (!showSessionExpiredErrorDetails)
    	{
    		details = null;
    	}
    	
    	super.showSessionExpiredError(details);
    }
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
	/**
	 * Gets the configuration object for a specific application from the bootstrap javascript.
	 * 
	 * @param pApplicationId the id of the application to get configuration data for
	 * @return a native javascript object containing the configuration data
	 */
	private static native JsConfiguration getJsConfiguration(String pApplicationId)
	/*-{
		return $wnd.vaadin.getApp(pApplicationId);
	 }-*/;

    //****************************************************************
    // Subclass definition
    //****************************************************************	
	
	/**
	 * Helper class for reading configuration options from the bootstrap javascript.
	 * 
	 * @author Thomas Krautinger
	 */
	private static class JsConfiguration extends JavaScriptObject
	{
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/** 
		 * Creates a new instance of <code>JsConfiguration</code>.
		 */
		protected JsConfiguration()
		{
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// User-defined methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
         * Reads a configuration parameter as a boolean object.
         * 
         * @param pName the name of the configuration parameter
         * 
         * @return boolean value of the configuration parameter, or <code>null</code> if no value is defined
         */
        private native Boolean getBoolean(String pName)
        /*-{
            var value = this.getConfig(pName);
            if (value === null || value === undefined) {
                return null;
            } else {
                 // $entry not needed as function is not exported
                return @java.lang.Boolean::valueOf(Z)(value);
            } 
        }-*/;

	}  // JsConfiguration

}   // ApplicationConnection
