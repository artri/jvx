/*
 * Copyright 2023 SIB Visions GmbH
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
 * 08.06.2023 - [JR] - creation
 */
package com.sibvisions.util.type;

/**
 * The <code>ResourceLoaderContext</code> is simple context for resource loading.
 * 
 * @author René Jahn
 */
public final class ResourceLoaderContext extends ResourceLoader
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /** the current ResourceLoaderContext instance. */
    private static ThreadLocal<ResourceLoaderContext> instance = new ThreadLocal<ResourceLoaderContext>();
    
    /** the initial thread. */
    private Thread thread;
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Creates a new instance of <code>ResourceLoaderContext</code>.
     */
    public ResourceLoaderContext()
    {
        thread = Thread.currentThread();
        
        setInstance(this);
    }

    /**
     * Creates a new instance of <code>ResourceLoaderContext</code> with
     * initial copy of {@link ResourceLoader}.
     * 
     * @param pLoader the resource loader for copying settings 
     */
    public ResourceLoaderContext(ResourceLoader pLoader)
    {
    	this();
    	
    	if (pLoader != null)
    	{
	    	clDefault = pLoader.clDefault;
	    	liResourceArchive = pLoader.liResourceArchive;
    	}
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Gets the current instance of <code>ResourceLoaderContext</code>.
     * 
     * @return the current instance
     */
    public static ResourceLoaderContext getCurrentInstance()
    {
        return instance.get();
    }
    
    /**
     * Sets the current context instance.
     * 
     * @param pContext the context instance
     */
    protected synchronized void setInstance(ResourceLoaderContext pContext)
    {
        instance.set(pContext);
    }
    
    /**
     * Release any resources associated with this <code>ResourceLoaderContext</code> instance.
     *
     * @see #isReleased
     */
    public final synchronized void release()
    {
        instance.set(null);
    }    
    
    /**
     * Gets the release state of this <code>ResourceContext</code>.
     *  
     * @return <code>true</code> if there is no current instance of <code>ResourceLoaderContext</code> (means
     *         that the <code>ResourceLoaderContext</code> is released); otherwise <code>false</code>
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
    
}   // ResourceLoaderContext
