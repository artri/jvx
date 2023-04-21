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
 * 19.02.2013 - [SW] - creation
 * 18.03.2014 - [JR] - #978: changed UI detection and requestStart, requestEnd order
 * 16.02.2015 - [TK] - #1274: adding system messages provider handling
 * 					   #1275: adding servlet bootstrap handler
 * 17.02.2015 - [JR] - used ServiceUtil
 */
package com.sibvisions.rad.ui.vaadin.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.rad.genui.UIFactoryManager;

import com.sibvisions.rad.server.http.HttpContext;
import com.sibvisions.rad.ui.vaadin.impl.VaadinUI;
import com.sibvisions.rad.ui.vaadin.server.communication.VaadinCachedResourceRequestHandler;
import com.sibvisions.rad.ui.vaadin.server.communication.VaadinFileUploadHandler;
import com.sibvisions.rad.ui.vaadin.server.communication.VaadinServletBootstrapHandler;
import com.sibvisions.rad.ui.vaadin.server.communication.VaadinServletUIInitHandler;
import com.sibvisions.rad.ui.vaadin.server.communication.VaadinUnsupportedBrowserHandler;
import com.sibvisions.util.log.LoggerFactory;
import com.vaadin.server.ConnectorResourceHandler;
import com.vaadin.server.DeploymentConfiguration;
import com.vaadin.server.RequestHandler;
import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionExpiredException;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;
import com.vaadin.server.SystemMessages;
import com.vaadin.server.UICreateEvent;
import com.vaadin.server.UIProvider;
import com.vaadin.server.UnsupportedBrowserHandler;
import com.vaadin.server.VaadinPortletRequest;
import com.vaadin.server.VaadinPortletResponse;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinResponse;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinServletRequest;
import com.vaadin.server.VaadinServletResponse;
import com.vaadin.server.VaadinSession;
import com.vaadin.server.VaadinSession.State;
import com.vaadin.server.WrappedSession;
import com.vaadin.server.communication.FileUploadHandler;
import com.vaadin.server.communication.ServletBootstrapHandler;
import com.vaadin.server.communication.ServletUIInitHandler;
import com.vaadin.shared.ui.ui.UIConstants;
import com.vaadin.ui.UI;
import com.vaadin.util.CurrentInstance;

/**
 * 
 * The <code>VaadinServletService</code> class creates customized instances of the communication manager
 * and sets the error handler for sessions.
 * 
 * @author Stefan Wurm
 */
public class VaadinServletService extends com.vaadin.server.VaadinServletService 
                                  implements SessionInitListener
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** See {@link com.vaadin.server.ServletPortletHelper}. **/
    private static final String UPLOAD_URL_PREFIX = "APP/UPLOAD/";
    
    /** parameter to set whether to close idle UIs (true | false). */
    private static final String SERVLET_PARAMETER_CLOSE_IDLE_UIS = "closeIdleUIs";
    
    /** parameter name for marking idle expired UIs. */
    private static final String PARAM_IDLE_EXPIRED = "IDLE_EXPIRED";
    
    
    /** whether to close idle UIs. */
    private boolean bCloseIdleUIs;

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	

	/**
	 * Creates a new instance of <code>VaadinServletService</code>.
	 * 
	 * @param pServlet the vaadin servlet.
	 * @param pDeploymentConfiguration the deploayment configuration.
	 * @throws ServiceException if the creation failed.
	 */
	public VaadinServletService(VaadinServlet pServlet, DeploymentConfiguration pDeploymentConfiguration) throws ServiceException
	{
		super(pServlet, pDeploymentConfiguration);
		
		bCloseIdleUIs = pDeploymentConfiguration.getApplicationOrSystemProperty(SERVLET_PARAMETER_CLOSE_IDLE_UIS, "false").equalsIgnoreCase("true");
		
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
    		else if (handler instanceof ServletUIInitHandler)
    		{
    			handlers.set(i, new VaadinServletUIInitHandler());   
    		}
    		else if (handler instanceof ServletBootstrapHandler)
    		{
    		    handlers.set(i, new VaadinServletBootstrapHandler());
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
    	        		try
    	        		{
    	        			((VaadinUI)ui).destroy();
    	        		}
    	        		catch (Throwable th)
    	        		{
    	        			//ignore
    	        		}
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
	    	Object request;
	    	Object response;
	    	
	    	if (pRequest instanceof VaadinServletRequest)
	    	{
	    		request = ((VaadinServletRequest)pRequest).getRequest();
	    	}
	    	else if (pRequest instanceof VaadinPortletRequest)
	    	{
	    		request = ((VaadinPortletRequest)pRequest).getRequest();
	    	}
	    	else
	    	{
	    		request = pRequest;
	    	}
	    	
	    	if (pResponse instanceof VaadinServletResponse)
	    	{
	    		response = ((VaadinServletResponse)pRequest).getResponse();
	    	}
	    	else if (pResponse instanceof VaadinPortletResponse)
	    	{
	    		response = ((VaadinPortletResponse)pRequest).getPortletResponse();
	    	}
	    	else
	    	{
	    		response = pResponse;
	    	}
	    	
	        HttpContext ctxt = new HttpContext(request, response);
	        
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
                
                CurrentInstance.set(VaadinUI.class, null);
            }
	    }
	    finally
	    {
            VaadinContext vctxt = CurrentInstance.get(VaadinContext.class);
            HttpContext hctxt = CurrentInstance.get(HttpContext.class);

            checkIdleUIs(pSession);

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
	
	/**
	 * {@inheritDoc}
	 */
	@Override
    protected VaadinSession createVaadinSession(VaadinRequest request) throws ServiceException 
    {
	    if (!bCloseIdleUIs)
	    {
	        return new FactorySession(this);
	    }
	    else
	    {
	        return new UISession(this);
	    }
    }	
    
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean preserveUIOnRefresh(UIProvider pProvider, UICreateEvent pEvent) 
	{
	    if (isPreserveUIOnRefresh())
	    {
	        return true;
	    }

        //Fallback to Detault
	    return super.preserveUIOnRefresh(pProvider, pEvent);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
    public VaadinSession findVaadinSession(VaadinRequest request) throws ServiceException, 
                                                                         SessionExpiredException
    {
		try
		{
	        final VaadinSession session = super.findVaadinSession(request);
	        
	        if (session != null)
	        {
		        session.lock();
		        
		        final UI ui;
		        
		        try
		        {
			        ui = session.getService().findUI(request);
		        }
		        finally
		        {
		        	session.unlock();
		        }
		        
		        if (ui instanceof VaadinUI)
		        {
		            if (((VaadinUI)ui).getObject(PARAM_IDLE_EXPIRED) != null)
		            {
		                if (!ui.isClosing()) 
		                {
		                    ui.accessSynchronously(new Runnable() 
		                    {
		                        @Override
		                        public void run() 
		                        {
		                        	boolean bLock;
		                        	
                    			    try
								    {
						    		    bLock = session.getLockInstance().tryLock(3, TimeUnit.SECONDS);
								    }
								    catch (InterruptedException ie)
								    {
								        bLock = false;
								    }

		                        	try
		                        	{
			                            ui.close();
			                            session.removeUI(ui);
		                        	}
		                        	finally
		                        	{
		                        		if (bLock)
		                        		{
		                        			session.getLockInstance().unlock();
		                        		}
		                        	}
		                        }
		                    });
		                }
		                
		                throw new SessionExpiredException();
		            }
		        }
	        }
	        
	        return session;
		}
		catch (SessionExpiredException sse)
		{
			//temporary logging, to find problems
			LoggerFactory.getInstance(getClass()).debug(sse);
			
			throw sse;
		}
    }
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public UI findUI(VaadinRequest pRequest)
	{
		UI ui = super.findUI(pRequest);

		if (ui instanceof VaadinUI)
		{
			//to be sure!
            if (UIFactoryManager.getFactory() == null)
            {
            	((VaadinUI)ui).notifyRequestStart();
            	((VaadinUI)ui).notifyBeforeUI();
            }
		}
		
		return ui;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public SystemMessages getSystemMessages(Locale pLocale, VaadinRequest pRequest)
	{
		HttpContext ctxt = null;
		VaadinContext vctxt = null;
		
	    //e.g. PUSH requests if session is expired
	    if (HttpContext.getCurrentInstance() == null)
	    {
	    	if (pRequest instanceof VaadinServletRequest)
	    	{
		        ctxt = new HttpContext(((VaadinServletRequest) pRequest).getHttpServletRequest(), null);
	    	}
	    }
	    
	    if (VaadinContext.getCurrentInstance() == null)
	    {
	    	vctxt = new VaadinContext();
	    }
		
	    try
	    {
	    	return super.getSystemMessages(pLocale, pRequest);
	    }
	    finally
	    {
	    	if (vctxt != null)
	    	{
	    		vctxt.release();
	    	}

	    	if (ctxt != null)
	    	{
	    		ctxt.release();
	    	}
	    }
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets whether preserve UI on refresh was set via URL parameter or via deployment configuration.
	 * 
	 * @return <code>true</code> if preserve UI on refresh was configured, <code>false</code> otherwise 
	 *         (also means: use default implementation)
	 */
	public boolean isPreserveUIOnRefresh()
	{
        //1: URL parameter
        if (getCurrentRequest().getParameter(VaadinUI.PARAM_PRESERVEONREFRESH) != null)
        {
            return true;
        }
        
        //2: Settings
        String sPreserve = getDeploymentConfiguration().getApplicationOrSystemProperty(VaadinUI.PARAM_PRESERVEONREFRESH, "false");
        
        if (Boolean.parseBoolean(sPreserve))
        {
            return true;
        }
	    
        return false;
	}
	
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
				
				if (session != null)
				{
					session.lock();
					
					try
					{
						ui = session.getUIById(Integer.parseInt(uiId));
					}
					finally
					{
						session.unlock();
					}
				}
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
                lockSession(wsess);
                
                try
                {
                    VaadinSession session = VaadinSession.getForSession(this, wsess);

                    if (session != null)
                    {
                        session.lock();
                        
                        try
                        {
                            String sId = pRequest.getParameter(UIConstants.UI_ID_PARAMETER);
                            
                            if (sId != null) 
                            {
                                ui = session.getUIById(Integer.parseInt(sId));
                            }
                        }
                        finally
                        {
                            session.unlock();
                        }
                    }
                }
                finally
                {
                    unlockSession(wsess);
                }
            }
        }

        return ui;
	}
	
	/**
	 * Gets a hot deployment property. Such a property should be detected on demand because
	 * the servlet service is per servlet and not per UI.
	 * 
	 * @param pName the property name
	 * @return the value
	 */
	public String getHotDeploymentProperty(String pName)
	{
	    return null;
	}
	
	/**
	 * Configures the system messages provider.
	 */
	protected void configureSystemMessagesProvider()
	{
        ServiceUtil.configureSystemMessagesProvider(this);
	}
	
	/**
	 * Checks idle UIs. If <code>closeIdleUIs</code> parameter is set, the check will close all UIs which are idle.
	 * 
	 * @param pSession the session to check
	 */
    protected void checkIdleUIs(VaadinSession pSession)
    {
        if (bCloseIdleUIs)
        {
            if (pSession != null) 
            {
                pSession.lock();
                
                try 
                {
                    if (isSessionActive(pSession)) 
                    {
                        closeIdleUIs(pSession);
                    }    
                } 
                finally 
                {
                    pSession.unlock();
                }
            }
        }
    }
    
    /**
     * Gets whether the session is active.
     * 
     * @param pSession the session
     * @return <code>true</code> if session is active, <code>false</code> otherwise
     */
    private boolean isSessionActive(VaadinSession pSession) 
    {
        //copied code from super class
        if (pSession.getState() != State.OPEN || pSession.getSession() == null) 
        {
            return false;
        } 
        else 
        {
            long now = System.currentTimeMillis();
            
            int timeout = 1000 * (getDeploymentConfiguration().isCloseIdleSessions() ? pSession.getSession().getMaxInactiveInterval() : -1);
            return timeout < 0
                   || now - pSession.getLastRequestTimestamp() < timeout;
        }
    }

    /**
     * Closes all idle UIs in the given session.
     * 
     * @param pSession the session
     */
    private void closeIdleUIs(VaadinSession pSession) 
    {
        final String sessionId = pSession.getSession().getId();
        
        for (final UI ui : pSession.getUIs()) 
        {
            if (isIdle(ui)) 
            {
                if (ui instanceof VaadinUI)
                {
                    ui.accessSynchronously(new Runnable() 
                    {
                        @Override
                        public void run() 
                        {
                            LoggerFactory.getInstance(VaadinServletService.class).debug("Closing idle UI ", Integer.valueOf(ui.getUIId()),  " in session: ", sessionId);
                                                        
                            ((VaadinUI)ui).putObject(PARAM_IDLE_EXPIRED, Boolean.TRUE);
                        }
                    });
                }
            }
        }
    }
    
    /**
     * Gets whether an UI is idle. An UI is idle if its inactive for the max inactive interval.
     * 
     * @param ui the UI
     * @return <code>true</code> if UI is idle
     */
    private boolean isIdle(UI ui) 
    {
        if (ui.isClosing()) 
        {
            return false;
        } 
        else 
        {
            long now = System.currentTimeMillis();
            
            int timeout = 1000 * ui.getSession().getSession().getMaxInactiveInterval();
            return timeout >= 0 && now - ((VaadinUI)ui).getLastRequestTimestamp() >= timeout;
        }
    }

	//****************************************************************
    // Subclass definition
    //****************************************************************

    /**
     * The <code>FactorySession</code> forwards specific information to the current UI.
     * 
     * @author René Jahn
     */
    public static class FactorySession extends VaadinSession
    {
	    /**
	     * Creates a new instance of <code>FactorySession</code>.
	     * 
	     * @param pService the service
	     */
	    public FactorySession(VaadinService pService)
	    {
	        super(pService);
	    }    	
	    
	    /**
	     * {@inheritDoc}
	     */
	    @Override
	    public void unlock()
	    {
	    	try
	    	{
		    	super.unlock();
	    	}
	    	finally
	    	{
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
	    }
	    
    }	// FactorySession
    
	/**
	 * The <code>UISession</code> forwards specific information to the current UI.
	 * 
	 * @author René Jahn
	 */
	public static class UISession extends FactorySession
	{
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Initialization
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	    /**
	     * Creates a new instance of <code>UISession</code>.
	     * 
	     * @param pService the service
	     */
	    public UISession(VaadinService pService)
	    {
	        super(pService);
	    }

	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Overwritten methods
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	    /**
	     * {@inheritDoc}
	     */
	    public void setLastRequestTimestamp(long pTimestamp)
	    {
	        super.setLastRequestTimestamp(pTimestamp);
	        
	        UI ui = UI.getCurrent();
	        
	        if (ui instanceof VaadinUI)
	        {
	            ((VaadinUI)ui).setLastRequestTimestamp(pTimestamp);
	        }
	    }
	    
	}  // UISession
	
}	// VaadinServletService
