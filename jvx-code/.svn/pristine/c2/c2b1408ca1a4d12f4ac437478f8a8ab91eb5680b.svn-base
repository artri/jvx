/*
 * Copyright 2019 SIB Visions GmbH
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
 * 06.03.2019 - [JR] - creation
 */
package com.sibvisions.rad.server.security;

/**
 * The <code>SecurityContext</code> is simple context for accessing security relevant objects.
 * 
 * @author René Jahn
 */
public class SecurityContext
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /** the current SecurityContext instance. */
    private static ThreadLocal<SecurityContext> instance = new ThreadLocal<SecurityContext>();
    
    /** the initial thread. */
    private Thread thread;
    
    /** whether special packages should be hidden. */
    private boolean bHidePackages = true;

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Creates a new instance of <code>SecurityContext</code>.
     */
    public SecurityContext()
    {
        thread = Thread.currentThread();
        
        setInstance(this);
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Gets whether special packages should be hidden.
     * 
     * @param pHidePackages <code>true</code> to hide special packages, <code>false</code> otherwise
     */
    public void setHidePackages(boolean pHidePackages)
    {
    	bHidePackages = pHidePackages;
    }
    
    /**
     * Gets whether special packages should be hidden.
     * 
     * @return <code>true</code> to hide special packages, <code>false</code> otherwise
     */
    public boolean isHidePackages()
    {
    	return bHidePackages;
    }
    
    /**
     * Gets the current instance of <code>SecurityContext</code>.
     * 
     * @return the current instance
     */
    public static SecurityContext getCurrentInstance()
    {
        return instance.get();
    }
    
    /**
     * Sets the current context instance.
     * 
     * @param pContext the context instance
     */
    protected synchronized void setInstance(SecurityContext pContext)
    {
        instance.set(pContext);
    }
    
    /**
     * Release any resources associated with this <code>SecurityContext</code> instance.
     *
     * @see #isReleased
     */
    public final synchronized void release()
    {
        instance.set(null);
    }    
    
    /**
     * Gets the release state of this <code>SecurityContext</code>.
     *  
     * @return <code>true</code> if there is no current instance of <code>SecurityContext</code> (means
     *         that the <code>SecurityContext</code> is released); otherwise <code>false</code>
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
    
}   // SecurityContext
