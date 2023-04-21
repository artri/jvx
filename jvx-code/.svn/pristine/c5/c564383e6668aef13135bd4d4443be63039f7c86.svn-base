/*
 * Copyright 2011 SIB Visions GmbH
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
 * 11.12.2011 - [JR] - creation
 * 23.12.2012 - [JR] - #534: logging
 */
package com.sibvisions.rad.server.http.rest;

import java.util.HashMap;

import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.ChallengeResponse;
import org.restlet.data.Status;
import org.restlet.ext.servlet.ServletUtils;
import org.restlet.security.Verifier;

import com.sibvisions.rad.server.DirectServerSession;
import com.sibvisions.rad.server.http.HttpContext;
import com.sibvisions.rad.server.http.rest.service.RESTSessionContextImpl;
import com.sibvisions.rad.server.security.SecurityContext;
import com.sibvisions.util.log.LoggerFactory;
import com.sibvisions.util.type.StringUtil;

/**
 * The <code>SecurityManagerVerifier</code> uses the security mangaer of an application to authenticate a user.
 * 
 * @author René Jahn
 */
public class SecurityManagerVerifier implements Verifier
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** the connection to the adapter/application. */
    private RESTAdapter adapter;
    
    /** whether the challenge response should be ignored. */
    private boolean bIgnoreChallengeResponse;
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Creates a new instance of <code>SecurityManagerVerifier</code>.
     * 
     * @param pAdapter the application/REST adapter
     */
    public SecurityManagerVerifier(RESTAdapter pAdapter)
    {
        this(pAdapter, false);
    }
    
    /**
     * Creates a new instance of <code>SecurityManagerVerifier</code>.
     * 
     * @param pAdapter the application/REST adapter
     * @param pIgnoreChallengeResponse whether the challenge response should be ignore for the verification
     */    
    public SecurityManagerVerifier(RESTAdapter pAdapter, boolean pIgnoreChallengeResponse)
    {
        adapter = pAdapter;
        bIgnoreChallengeResponse = pIgnoreChallengeResponse;
    }
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /**
     * {@inheritDoc}
     */
    public int verify(Request pRequest, Response pResponse) 
    {
		if (!bIgnoreChallengeResponse && pRequest.getChallengeResponse() == null) 
        {
            return RESULT_MISSING;
        } 
        else 
        {
            if (HttpContext.getCurrentInstance() != null)
            {
            	return verifyIntern(pRequest, pResponse);
            }
            else
            {
        		@Deprecated
                HttpContext ctxt = new HttpContext(ServletUtils.getRequest(pRequest), ServletUtils.getResponse(pResponse));
                
                try
                {
                	SecurityContext sctxt = new SecurityContext();
                	sctxt.setHidePackages(false);
                	
                	try
                	{
                		RESTServerContextImpl srvctxt = new RESTServerContextImpl();

                		try
        	        	{
                			return verifyIntern(pRequest, pResponse);
        	        	}
                		finally
                		{
                			srvctxt.release();
                		}
                	}
                	finally
                	{
                		sctxt.release();
                	}
                }
                finally
                {
                	ctxt.release();
                }
            }
        }
    }

    /**
     * Verifies if creating a session for the reqeust lifecycle object is possible.
     * 
     * @param pRequest the request
     * @param pResponse the response
     * @return the verification result
     */
    private int verifyIntern(Request pRequest, Response pResponse)
    {
    	String sApplicationName = (String)pRequest.getAttributes().get(RESTAdapter.PARAM_APP_NAME);
    	
		if (sApplicationName == null)
		{
			return RESULT_MISSING;
		}
    	
    	String sIdentifier = getIdentifier(pRequest, pResponse);
        String sSecret = getSecret(pRequest, pResponse);

    	DirectServerSession session = null;
    
        try
        {
            //it's important to set the environment
            HashMap<String, Object> hmpParams = RESTAdapter.createConnectionProperties(adapter, pRequest);
            
        	session = DirectServerSession.createMasterSession(sApplicationName, sIdentifier, sSecret, hmpParams);
    	    session.setSessionContextImpl(RESTSessionContextImpl.class);

        	adapter.configureSession(session);
        	
        	((RESTServerContextImpl)RESTServerContextImpl.getCurrentInstance()).setRequestSession(session);
        	
        	try
        	{
            	pRequest.getClientInfo().setUser(new LifeCycleConnector(adapter, session, hmpParams, 
            			                                                (String)pRequest.getAttributes().get(RESTAdapter.PARAM_LCO_CLASS)));
        	}
        	catch (Throwable th)
        	{
        		pResponse.setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
        		
        		throw th;
        	}
    	
        	return RESULT_VALID;
        }
        catch (Throwable th)
        {            	
        	if (!bIgnoreChallengeResponse)
        	{
        		LoggerFactory.getInstance(SecurityManagerVerifier.class).error(th);
        	}
        	
        	if (session != null)
        	{
            	try
            	{
            		session.destroy();
            	}
            	catch (Throwable thr)
            	{
            		//nothing to be done
            	}
            	
            	((RESTServerContextImpl)RESTServerContextImpl.getCurrentInstance()).setRequestSession(null);
        	}
        	
        	return RESULT_INVALID;
        }
	}
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Returns the user identifier.
     * 
     * @param pRequest The request to inspect.
     * @param pResponse The response to inspect.
     * @return The user identifier or <code>null</code> if not set.
     */
    protected String getIdentifier(Request pRequest, Response pResponse) 
    {
    	if (!bIgnoreChallengeResponse)
    	{
	    	ChallengeResponse chr = pRequest.getChallengeResponse();
	    	
	    	if (chr != null)
	    	{
	    		String sIdentifier = chr.getIdentifier();
	    		
	    		if (!StringUtil.isEmpty(sIdentifier))
	    		{
	                return sIdentifier;
	    		}
	    	}
    	}
    	
    	return null;
    }

    /**
     * Returns the secret provided by the user.
     * 
     * @param pRequest The request to inspect.
     * @param pResponse The response to inspect.
     * @return The secret provided by the user or <code>null</code> if not set
     */
    protected String getSecret(Request pRequest, Response pResponse) 
    {
    	if (!bIgnoreChallengeResponse)
    	{
	    	ChallengeResponse chr = pRequest.getChallengeResponse();
	    	
	    	if (chr != null)
	    	{
	    		char[] chSecret = chr.getSecret();
	    		
	    		if (chSecret != null && chSecret.length > 0)
	    		{
	                return new String(chSecret);
	    		}
	    	}
    	}
    	
    	return null;
    }    
    
}	// SecurityManagerVerifier
