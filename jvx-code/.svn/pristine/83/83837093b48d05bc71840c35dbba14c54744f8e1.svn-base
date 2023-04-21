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
 * 23.11.2014 - [JR] - creation
 */
package com.sibvisions.rad.server.http;

/**
 * The <code>HttpContext</code> is simple context for accessing request and response objects.
 * 
 * @author René Jahn
 */
public class HttpContext
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /** the current HttpContext instance. */
    private static ThreadLocal<HttpContext> instance = new ThreadLocal<HttpContext>();
    
    /** the current servlet request. */
    private Object request;
    
    /** the current servlet response. */
    private Object response;
    
    /** the initial thread. */
    private Thread thread;

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Creates a new instance of <code>HttpContext</code>.
     * 
     * @param pRequest the servlet request
     * @param pResponse the servlet response
     */
    public HttpContext(Object pRequest, Object pResponse)
    {
        request = pRequest;
        response = pResponse;

        thread = Thread.currentThread();
        
        setInstance(this);
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Gets the current request.
     * 
     * @return the request
     */
    public Object getRequest()
    {
        return request;
    }
    
    /**
     * Gets the current esponse.
     * 
     * @return the response
     */
    public Object getResponse()
    {
        return response;
    }
    
    /**
     * Gets the current instance of <code>HttpContext</code>.
     * 
     * @return the current instance
     */
    public static HttpContext getCurrentInstance()
    {
        return instance.get();
    }
    
    /**
     * Sets the current context instance.
     * 
     * @param pContext the context instance
     */
    protected synchronized void setInstance(HttpContext pContext)
    {
        instance.set(pContext);
    }
    
    /**
     * Release any resources associated with this <code>HttpContext</code> instance.
     *
     * @see #isReleased
     */
    public final synchronized void release()
    {
        instance.set(null);
    }    
    
    /**
     * Gets the release state of this <code>HttpContext</code>.
     *  
     * @return <code>true</code> if there is no current instance of <code>HttpContext</code> (means
     *         that the <code>HttpContext</code> is released); otherwise <code>false</code>
     */
    public boolean isReleased()
    {
        return instance.get() == null;
    }
    
    /**
     * The initial thread at constructor time.
     * 
     * @return the initial thread
     */
    public Thread getInitialThread()
    {
        return thread;
    }
    
}   // HttpContext
