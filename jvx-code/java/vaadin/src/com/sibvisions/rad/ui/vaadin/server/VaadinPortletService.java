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
 * 20.04.2013 - [JR] - fireSessionDestroy overwritten
 * 18.03.2014 - [JR] - #978: changed UI detection and requestStart, requestEnd order
 * 16.02.2015 - [TK] - #1274: adding system messages provider handling
 * 17.02.2015 - [JR] - used ServiceUtil
 */
package com.sibvisions.rad.ui.vaadin.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import com.sibvisions.rad.server.http.HttpContext;
import com.sibvisions.rad.ui.vaadin.impl.VaadinUI;
import com.sibvisions.rad.ui.vaadin.server.communication.VaadinCachedResourceRequestHandler;
import com.sibvisions.rad.ui.vaadin.server.communication.VaadinFileUploadHandler;
import com.sibvisions.rad.ui.vaadin.server.communication.VaadinPortletBootstrapHandler;
import com.sibvisions.rad.ui.vaadin.server.communication.VaadinPortletUIInitHandler;
import com.sibvisions.rad.ui.vaadin.server.communication.VaadinUnsupportedBrowserHandler;
import com.sibvisions.util.type.StringUtil;
import com.vaadin.server.ConnectorResourceHandler;
import com.vaadin.server.DeploymentConfiguration;
import com.vaadin.server.RequestHandler;
import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionExpiredException;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;
import com.vaadin.server.UnsupportedBrowserHandler;
import com.vaadin.server.VaadinPortlet;
import com.vaadin.server.VaadinPortletSession;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinResponse;
import com.vaadin.server.VaadinSession;
import com.vaadin.server.WrappedSession;
import com.vaadin.server.communication.FileUploadHandler;
import com.vaadin.server.communication.PortletBootstrapHandler;
import com.vaadin.server.communication.PortletUIInitHandler;
import com.vaadin.shared.ui.ui.UIConstants;
import com.vaadin.ui.UI;
import com.vaadin.util.CurrentInstance;

/**
 * The <code>VaadinPortletService</code> class creates customized instances of the communication manager
 * and sets the error handler for sessions.
 * 
 * @author Thomas Krautinger
 */
public class VaadinPortletService extends com.vaadin.server.VaadinPortletService
                                  implements SessionInitListener
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /** See com.vaadin.server.ServletPortletHelper. **/
    private static final String UPLOAD_URL_PREFIX = "APP/UPLOAD/";

    /** the "configured" URL prefix. */
    private String sURLPrefix;
    
    /** whether Vaadin should be shared (true) between webapps or should be used per webapp (false). */
    private boolean bShared;
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	

	/**
	 * Creates a new instance of <code>VaadinPortletService</code>.
	 * 
	 * @param pPortlet the portlet.
	 * @param pDeploymentConfiguration the deploayment configuration.
	 * @throws ServiceException if the creation failed.
	 */
	public VaadinPortletService(VaadinPortlet pPortlet, DeploymentConfiguration pDeploymentConfiguration) throws ServiceException
	{
		super(pPortlet, pDeploymentConfiguration);
		
        Properties prop = pDeploymentConfiguration.getInitParameters();
        
        bShared = Boolean.valueOf((String)prop.get("vaadin.shared")).booleanValue();
        
        //Don't use vaadin' Constants.PORTAL_PARAMETER_VAADIN_RESOURCE_PATH because it's very specific
        sURLPrefix = (String)prop.get("vaadin.staticFileLocationPrefix");
        
        configureSystemMessagesProvider();
        
        addSessionInitListener(this);
	}
		
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    public void sessionInit(SessionInitEvent pEvent)
    {
        //Configure the error handler -> needs lock -> don't override crea
        pEvent.getSession().setErrorHandler(new VaadinErrorHandler());
    }	
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
    @Override
    protected List<RequestHandler> createRequestHandlers() throws ServiceException 
    {
    	List<RequestHandler> handlers = super.createRequestHandlers();
    
    	for (int i = 0; i < handlers.size(); i++)
    	{
    		RequestHandler handler = handlers.get(i);
    		
    		if (handler instanceof UnsupportedBrowserHandler)
    		{
    			handlers.set(i, new VaadinUnsupportedBrowserHandler());    			
    		}
    		else if (handler instanceof PortletUIInitHandler)
    		{
    			handlers.set(i, new VaadinPortletUIInitHandler());   
    		}
    		else if (handler instanceof PortletBootstrapHandler)
    		{
    		    handlers.set(i, new VaadinPortletBootstrapHandler());
    		}
            else if (handler instanceof ConnectorResourceHandler)
            {
                handlers.add(i, new VaadinCachedResourceRequestHandler());
                i++;
            }
            else if (handler instanceof FileUploadHandler)
            {
                handlers.set(i, new VaadinFileUploadHandler());
            }
    	}

        return handlers;
    }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getStaticFileLocation(VaadinRequest pRequest)
	{
	    if (bShared)
	    {
            //Use vaadin resources like theme or widgetset from ROOT application 
	        //(shared for all web application contexts)
	        return super.getStaticFileLocation(pRequest);
	    }
	    else
	    {
            String sPrefix = sURLPrefix;

            if (StringUtil.isEmpty(sPrefix))
            {
                //don't use empty or null prefix
                sPrefix = "";
            }
            
	        //Use vaadin resources like theme or widgetset from web application context
            return sPrefix + pRequest.getContextPath();
	    }
	}
	
	/**
	 * {@inheritDoc}
	 */
    @Override
    protected VaadinSession createVaadinSession(VaadinRequest request) throws ServiceException 
    {
    	return new FactorySession(this);
    }
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fireSessionDestroy(VaadinSession pVaadinSession)
	{
		try
		{	
            boolean bLock;
            
            try
            {
                bLock = pVaadinSession.getLockInstance().tryLock(3, TimeUnit.SECONDS);
            }
            catch (InterruptedException ie)
            {
                bLock = false;
            }
            
            try
            {
                for (UI ui : new ArrayList<UI>(pVaadinSession.getUIs())) 
                {
                    if (ui instanceof VaadinUI)
                    {
                        ((VaadinUI)ui).destroy();
                    }
                }
            }
            finally
            {
                if (bLock)
                {
                    pVaadinSession.getLockInstance().unlock();
                }
            }
		}
		finally
		{
			super.fireSessionDestroy(pVaadinSession);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void requestStart(VaadinRequest pRequest, VaadinResponse pResponse)
	{
        //e.g. PUSH requests
        if (HttpContext.getCurrentInstance() == null)
        {
            HttpContext ctxt = new HttpContext(pRequest, pResponse);
            
            CurrentInstance.set(HttpContext.class, ctxt);
        }
	    
		super.requestStart(pRequest, pResponse);

		UI ui = getUI(pRequest, pResponse);

        if (ui != null)
        {
            ((VaadinUI)ui).notifyRequestStart();
            ((VaadinUI)ui).notifyBeforeUI();
            
            CurrentInstance.set(VaadinUI.class, (VaadinUI)ui);
        }
        
        VaadinContext ctxt = new VaadinContext();
        
        CurrentInstance.set(VaadinContext.class, ctxt);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void requestEnd(VaadinRequest pRequest, VaadinResponse pResponse, VaadinSession pSession)
	{
	    try
	    {
	    	VaadinUI ui = CurrentInstance.get(VaadinUI.class);
    
            if (ui != null)
            {
                ((VaadinUI)ui).notifyAfterUI();
                ((VaadinUI)ui).notifyRequestEnd();
            }
	    }
	    finally
	    {
            VaadinContext vctxt = CurrentInstance.get(VaadinContext.class);
            HttpContext hctxt = CurrentInstance.get(HttpContext.class);

            super.requestEnd(pRequest, pResponse, pSession);
	        
            if (vctxt != null)
            {
                vctxt.release();
            }
            
            if (hctxt != null)
            {
                hctxt.release();
            }
	    }
	}
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Returns the Vaadin ui.
	 * 
	 * @param pRequest the current request.
	 * @param pResponse the current response.
	 * @return the vaadin ui.
	 */
	@SuppressWarnings("deprecation")
    private UI getUI(VaadinRequest pRequest, VaadinResponse pResponse)
	{
		UI ui = null;

        if (com.vaadin.server.ServletPortletHelper.isFileUploadRequest(pRequest)) 
        {
            String pathInfo = pRequest.getPathInfo();
            
            // strip away part until the data we are interested starts
            int startOfData = pathInfo.indexOf(UPLOAD_URL_PREFIX) + UPLOAD_URL_PREFIX.length();
            
            String uppUri = pathInfo.substring(startOfData);
            String[] parts = uppUri.split("/", 4); // 0= UIid, 1 = cid, 2= name, 3 // = sec key
            String uiId = parts[0];
            
            VaadinSession session;
			try
			{
				session = pRequest.getService().findVaadinSession(pRequest);
				ui = session.getUIById(Integer.parseInt(uiId));   
			}
			catch (ServiceException e)
			{
				e.printStackTrace();
			}
			catch (SessionExpiredException e)
			{
				e.printStackTrace();
			}
                   	
        }
        
        //don't use UI.getCurrent() because it's possible that this returns the wrong UI
        
        if (ui == null)
        {
            WrappedSession wsess = pRequest.getWrappedSession(false);

            if (wsess != null)
            {
                VaadinSession session = VaadinSession.getForSession(this, wsess);
                
                if (session != null)
                {
                    String sId = pRequest.getParameter(UIConstants.UI_ID_PARAMETER);
                    
                    if (sId != null) 
                    {
                        ui = session.getUIById(Integer.parseInt(sId));
                    }
                }
            }
        }

        return ui;
	}
	
    /**
     * Configures the system messages provider.
     */
    protected void configureSystemMessagesProvider()
    {
        ServiceUtil.configureSystemMessagesProvider(this);
    }	
    
	//****************************************************************
    // Subclass definition
    //****************************************************************

    /**
     * The <code>FactorySession</code> forwards specific information to the current UI.
     * 
     * @author René Jahn
     */
    public static class FactorySession extends VaadinPortletSession
    {
	    /**
	     * Creates a new instance of <code>FactorySession</code>.
	     * 
	     * @param pService the service
	     */
	    public FactorySession(VaadinPortletService pService)
	    {
	        super(pService);
	    }    	
	    
	    /**
	     * {@inheritDoc}
	     */
	    @Override
	    public void unlock()
	    {
	    	super.unlock();
	    	
	    	UI ui = UI.getCurrent();
	    	
	    	if (ui != null)
	    	{
	    		if (ui instanceof VaadinUI)
	    		{
		    		VaadinUI vui = CurrentInstance.get(VaadinUI.class);	 
		    		
		    		//means that requestStart wasn't called before
		    		if (vui == null)
		    		{
                        ((VaadinUI)ui).notifyAfterUI();
		    			((VaadinUI)ui).notifyRequestEnd();
		    		}
	    		}
	    	}
	    }
	    
    }	// FactorySession    
		
} 	// VaadinPortletService
