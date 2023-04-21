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
 * 22.11.2014 - [JR] - creation
 * 28.05.2015 - [JR] - #1397: call handler integration 
 */
package javax.rad.server;

/**
 * A <code>ServerContext</code> contains all of the per-request state information related to the processing 
 * of a single server call. It is passed to, and potentially modified by, each phase of the request processing 
 * lifecycle.
 * 
 * A ServerContext instance remains active until its release() method is called.
 * While a ServerContext instance is active, it must not be referenced from any thread other than the one 
 * upon which the server executing this application utilizes for the processing of this call.
 * 
 * @author René Jahn
 * @see ServerContext
 */
public abstract class ServerContext
{    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /** the current ServerContext instance. */
    private static ThreadLocal<ServerContext> instance = new ThreadLocal<ServerContext>();
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Abstract methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Destroyes any resources associated with this ServerContext instance.
     */
    protected abstract void destroy();
    
    /**
     * Gets the session instance if available.
     * 
     * @return the session or <code>null</code> if session is not available
     */
    public abstract ISession getSession();
    
    /**
     * Gets the system identifier. The identifier is unique for each server and
     * deployment mode, e.g. web application deployed as war, desktop application, ...
     * 
     * @return the identifier
     */
    public abstract String getSystemIdentifier();
    
    /**
     * Gets the identifier of the server that started this context.
     * 
     * @return a VM unique server identifier
     */
    public abstract String getServerIdentifier();
    
    /**
     * Gets the {@link ICallHandler} for the current context. The handler is session
     * independent.
     * 
     * @return the call handler or <code>null</code> if no current session is available
     */
    public abstract ICallHandler getCallHandler();    
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Gets the current instance of <code>ServerContext</code>.
     * 
     * @return the current instance
     */
    public static ServerContext getCurrentInstance()
    {
        return instance.get();
    }
    
    /**
     * Gets the current session.
     * 
     * @return the current session
     */
    public static ISession getCurrentSession()
    {
        ServerContext ctx = instance.get();
        
        if (ctx == null)
        {
            return null;
        }
        
        return ctx.getSession();
    }
    
    /**
     * Sets the current context instance.
     * 
     * @param pContext the context instance
     */
    protected synchronized void setInstance(ServerContext pContext)
    {
        instance.set(pContext);
    }
    
    /**
     * Release any resources associated with this <code>ServerContext</code> instance.
     *
     * @see #isReleased
     */
    public final synchronized void release()
    {
        destroy();
    }
    
    /**
     * Gets the release state of this <code>ServerContext</code>.
     *  
     * @return <code>true</code> if there is no current instance of <code>ServerContext</code> (means
     *         that the <code>ServerContext</code> is released); otherwise <code>false</code>
     */
    public boolean isReleased()
    {
        return instance.get() == null;
    }
    
}   // ServerContext
