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
 * 14.07.2015 - [JR] - creation
 */
package com.sibvisions.rad.ui.vaadin.server;

/**
 * The <code>VaadinContext</code> is simple context for accessing request relevant 
 * information.
 * 
 * @author René Jahn
 */
public class VaadinContext
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /** the current VaadinContext instance. */
    private static ThreadLocal<VaadinContext> instance = new ThreadLocal<VaadinContext>();
    
    /** the initial thread. */
    private Thread thread;

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Creates a new instance of <code>VaadinContext</code> for the current thread.
     */
    public VaadinContext()
    {
        thread = Thread.currentThread();
        
        setInstance(this);
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Gets the current instance of <code>VaadinContext</code>.
     * 
     * @return the current instance
     */
    public static VaadinContext getCurrentInstance()
    {
        return instance.get();
    }
    
    /**
     * Sets the current context instance.
     * 
     * @param pContext the context instance
     */
    protected synchronized void setInstance(VaadinContext pContext)
    {
        instance.set(pContext);
    }
    
    /**
     * Release any resources associated with this <code>VaadinContext</code> instance.
     *
     * @see #isReleased
     */
    public final synchronized void release()
    {
        instance.set(null);
    }    
    
    /**
     * Gets the release state of this <code>VaadinContext</code>.
     *  
     * @return <code>true</code> if there is no current instance of <code>VaadinContext</code> (means
     *         that the <code>VaadinContext</code> is released); otherwise <code>false</code>
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
    
}   // VaadinContext
