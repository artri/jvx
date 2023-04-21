/*
 * Copyright 2018 SIB Visions GmbH
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
 * 21.06.2018 - [JR] - creation
 */
package com.sibvisions.rad.server;

import java.io.File;

import javax.rad.remote.IConnectionConstants;
import javax.rad.server.ICallHandler;
import javax.rad.server.ISession;
import javax.rad.server.ServerContext;
import javax.servlet.http.HttpServletRequest;

import com.sibvisions.rad.IPackageSetup;
import com.sibvisions.rad.server.config.Configuration;
import com.sibvisions.rad.server.http.HttpContext;
import com.sibvisions.util.log.LoggerFactory;
import com.sibvisions.util.type.StringUtil;

/**
 * The <code>AbstractServerContext</code> is a general {@link javax.rad.server.ServerContext} implementation.
 * 
 * @author René Jahn
 */
public abstract class AbstractServerContext extends ServerContext
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /** the associated session. */
    private WrappedSession wsessCurrent;
    
    /** the system identifier. */
    private String sIdentifier;
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Creates a new instance of <code>ServerContextImpl</code>.
     */
    protected AbstractServerContext()
    {
        sIdentifier = createSystemIdentifier();
        
        setInstance(this);
    }

    /**
     * Creates a new instance of <code>ServerContextImpl</code> for a given
     * system.
     * 
     * @param pIdentifier the system identifier
     */
    protected AbstractServerContext(String pIdentifier)
    {
        sIdentifier = pIdentifier;
        
        setInstance(this);
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Abstract methods implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    @Override
    protected void destroy()
    {
        setInstance(null);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ISession getSession()
    {
        return wsessCurrent;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getSystemIdentifier()
    {
        return sIdentifier;
    }

    /**
     * {@inheritDoc}
     */
    public ICallHandler getCallHandler()
    {
        if (wsessCurrent != null)
        {
            AbstractSession session = wsessCurrent.session;
         
            //ServerContext always returns the callhandler of the master session
            if (session instanceof SubSession)
            {
                return ((SubSession)session).getMasterSession().getCallHandler();
            }
            else
            {
                return wsessCurrent.session.getCallHandler();
            }
        }
        
        return null;
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Sets the session.
     * 
     * @param pSession the session
     */
    protected void setSession(ISession pSession)
    {
        if (pSession == null)
        {
            wsessCurrent = null;
        }
        else
        {
        	if (pSession instanceof AbstractSession)
        	{
        		wsessCurrent = new WrappedSession((AbstractSession)pSession);
        	}
        	else if (pSession instanceof WrappedSession)
            {
                wsessCurrent = (WrappedSession)pSession;
            }
            else if (pSession instanceof DirectServerSession)
            {
                wsessCurrent = new WrappedSession((AbstractSession)((DirectServerSession)pSession).getSession());
            }
            else
            {
            	throw new IllegalArgumentException("Given session doesn't offer an 'AbstractSession'!");
            }
        	
        	initIdentifier(pSession);
        }
    }
    
    /**
     * Initializes the session identifier from the given session.
     * 
     * @param pSession the session
     */
    protected void initIdentifier(ISession pSession)
    {
        //don't use getProperty, because it changes the last access time and this means that it won't timeout
        Object obj = pSession.getProperties().get(IConnectionConstants.PREFIX_SERVER + IConnectionConstants.PREFIX_REQUEST + "systemIdentifier");
        
        if (obj != null)
        {
        	//Use the identifier from the session, if set because it's the initial identifier which is maybe different to the current identifier
            sIdentifier = obj.toString();
        }
    }
    
    /**
     * Creates the system identifier.
     * 
     * @return the identifier
     */
    private String createSystemIdentifier()
    {
    	try
    	{
    		String sSysIdent = Configuration.getServerZone().getProperty("/server/systemIdentifier");
    		
    		if (!StringUtil.isEmpty(sSysIdent))
    		{
                LoggerFactory.getInstance(ServerContext.class).debug("Using configured (zone) system identifier: ", sSysIdent);

                return sSysIdent;
    		}
    	}
    	catch (Exception e)
    	{
    		LoggerFactory.getInstance(ServerContext.class).debug(e);
    	}
    	
        //use configured system identifier, if available
        String sSysIdent = System.getProperty(IPackageSetup.SERVER_SYSTEMIDENTIFIER);
        
        if (!StringUtil.isEmpty(sSysIdent))
        {
            LoggerFactory.getInstance(ServerContext.class).debug("Using configured (sysprop) system identifier: ", sSysIdent);

            return sSysIdent;
        }

        //detect system identifier
        
        String sIdent = "";
        
        HttpContext ctxt = HttpContext.getCurrentInstance();
        
        if (ctxt != null)
        {
            Object oRequest = ctxt.getRequest();
            
            if (oRequest instanceof HttpServletRequest)
            {
                sIdent = ((HttpServletRequest)oRequest).getRequestURL().toString();
            }
            else
            {
                //e.g. PortletRequest 
            }
        }
        else
        {
            //no http context available -> check working directory
            
            File fiDir = new File("");
            
            sIdent = fiDir.getAbsolutePath();
        }

        LoggerFactory.getInstance(ServerContext.class).debug("Using system identifier: ", sIdent);
        
        return sIdent;
    }
    
}   // AbstractServerContext
