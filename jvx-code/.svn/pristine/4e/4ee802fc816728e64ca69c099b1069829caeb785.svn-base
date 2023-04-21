/*
 * Copyright 2009 SIB Visions GmbH
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
 * 08.05.2009 - [JR] - creation
 * 29.07.2009 - [JR] - getObjectName, getMethodName defined
 *                   - getNextContext, getPreviousContext implemented
 * 02.03.2011 - [JR] - #297: addObject, removeObject, getObject are now abstract                   
 */
package javax.rad.server;

import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import javax.rad.remote.IConnection;
import javax.rad.server.ICallBackBroker.PublishMode;
import javax.rad.server.ICallBackBroker.PublishState;
import javax.rad.server.security.IAccessChecker;

/**
 * A <code>SessionContext</code> contains all of the per-request state information related to the processing 
 * of a single server call. It is passed to, and potentially modified by, each phase of the request processing 
 * lifecycle.
 * 
 * A SessionContext instance remains active until its release() method is called. It's possible to have more
 * than one SessionContexts, but only the last SessionContext is accessible as current instance.
 * 
 * While a SessionContext instance is active, it must not be referenced from any thread other than the one 
 * upon which the server executing this application utilizes for the processing of this call.
 * 
 * @author René Jahn
 */
public abstract class SessionContext
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the list of previous SessionContext instances e.g. for recursive usage. */
	private static ThreadLocal<Vector<SessionContext>> contexts = new ThreadLocal<Vector<SessionContext>>();

	/** the current SessionContext instance. */
    private static ThreadLocal<SessionContext> instance = new ThreadLocal<SessionContext>();
    
    /** the list of injected objects. */
    private Hashtable<String, InjectObject> htObjects = null;
    
    /** the position of the context in the list of available contexts. */
    private int iElementPos = -1;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Destroyes any resources associated with this SessionContext instance.
     */
	protected abstract void destroy();
	
	/**
	 * Gets the {@link ISession} instance for this context.
	 * 
	 * @return the {@link ISession} instance
	 */
	public abstract ISession getSession();

	/**
	 * Gets the master {@link ISession} instance for this context.
	 * 
	 * @return the master {@link ISession}
	 */
	public abstract ISession getMasterSession();
	
	/**
	 * Gets the {@link IConfiguration} for the session.
	 * 
	 * @return the {@link IConfiguration}
	 */
	public abstract IConfiguration getSessionConfig();

	/**
	 * Gets the server {@link IConfiguration}.
	 * 
	 * @return the {@link IConfiguration}
	 */
	public abstract IConfiguration getServerConfig();
	
	/**
	 * Gets a connection to the server.
	 * 
	 * @return a "direct" server connection
	 */
	public abstract IConnection getServerConnection();
	
	/**
	 * Gets the name of the object from which a method will be called. A <code>SessionContext</code>
	 * will be initialized through a method call with or without object name. The method call without
	 * object name is known as action call.
	 * 
	 * @return the name of the object which will be used or <code>null</code> if an action will be called
	 * @see #getMethodName()
	 */
	public abstract String getObjectName();
	
	/**
	 * Gets the name of the method which will be called.
	 * 
	 * @return the method name
	 */
	public abstract String getMethodName();
	
	/**
	 * Puts an object to this <code>SessionContext</code>.
	 * 
	 * @param pObject the inject object
	 * @return the old object or <code>null</code> if the object was not known 
	 */
	public abstract InjectObject putObject(InjectObject pObject);

	/**
	 * Removes an inject object from this <code>SessionContext</code>.
	 * 
	 * @param pObject the inject object
	 * @return the removed object or <code>null</code> if the object was not put
	 */
	public abstract InjectObject removeObject(InjectObject pObject);
	
	/**
	 * Gets an already added inject object.
	 * 
	 * @param pName the object name
	 * @return the object or <code>null</code> if there is no object with the <code>pName</code>
	 */
	public abstract InjectObject getObject(String pName);
	
    /**
     * Gets the {@link ICallHandler} for the current context. The handler depends on the 
     * current session.
     * 
     * @return the call handler
     */
    public abstract ICallHandler getCallHandler();    
    
    /**
     * Gets the {@link ICallBackBroker}.
     * 
     * @return the callback broker
     */
    public abstract ICallBackBroker getCallBackBroker();
    
    /**
     * Creates a new sub session from the current master session. Don't forget to {@link ICloseableSession#close()}
     * the session.
     * 
     * @param pLifeCycleName the name of the life-cycle object
     * @param pProperties the additional session properties
     * @return the new sub session instance
     * @throws Throwable if sub session creation fails
     */
    public abstract ICloseableSession createSubSession(String pLifeCycleName, Map<String, Object> pProperties) throws Throwable;
    
    /**
     * Gets the access checker of the current session, if available.
     *  
     * @return the access checker or <code>null</code> if no access checker is available
     */
    public abstract IAccessChecker getAccessChecker();
    
    /**
     * Gets the class loader.
     * 
     * @return the class loader
     */
    public abstract ClassLoader getClassLoader();
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /**
     * Release any resources associated with this SessionContext instance.
     *
     * @see #isReleased
     */
	public final synchronized void release()
	{
		try
		{
			destroy();
		}
		finally
		{
			htObjects = null;
		}
	}
	
	/**
	 * Gets the current/last instance of <code>SessionContext</code>.
	 * 
	 * @return the current instance
	 */
	public static SessionContext getCurrentInstance()
	{
		return instance.get();
	}
	
	/**
	 * Gets the {@link ISession} from the current instance of <code>SessionContext</code>.
	 *  
	 * @return the current {@link ISession}
	 */
	public static ISession getCurrentSession()
	{
		SessionContext ctx = instance.get();
		
		if (ctx == null)
		{
			return null;
		}
		
		return ctx.getSession();
	}

	/**
	 * Gets the {@link IConfiguration} from the current instance of <code>SessionContext</code>.
	 *  
	 * @return the current session {@link IConfiguration}
	 */
	public static IConfiguration getCurrentSessionConfig()
	{
		SessionContext ctx = instance.get();
		
		if (ctx == null)
		{
			return null;
		}

		return ctx.getSessionConfig();
	}
	
	/**
	 * Gets the {@link IConfiguration} from the current instance of <code>SessionContext</code>.
	 *  
	 * @return the current server {@link IConfiguration}
	 */
	public static IConfiguration getCurrentServerConfig()
	{
		SessionContext ctx = instance.get();
		
		if (ctx == null)
		{
			return null;
		}

		return ctx.getServerConfig();
	}
	
    /**
     * Publishs an object as callback result. The object will be sent to the client with next call.
     * 
     * @param pInstruction the instruction identifier
     * @param pObject the object to send
     * @param pMode the publish mode
     * @return <code>true</code> if publishing was successful, <code>false</code> otherwise
     */
    public static PublishState publish(String pInstruction, Object pObject, PublishMode... pMode)
    {
        SessionContext ctx = instance.get();
        
        if (ctx == null)
        {
            return PublishState.Failed;
        }

        return ctx.getCallBackBroker().publish(pInstruction, pObject, pMode);
    }
	
    /**
     * Gets the current {@link ICallBackBroker}.
     * 
     * @return the current {@link ICallBackBroker}
     */
    public static ICallBackBroker getCurrentCallBackBroker()
    {
        SessionContext ctx = instance.get();
        
        if (ctx == null)
        {
            return null;
        }

        return ctx.getCallBackBroker();
    }
	
	/**
	 * Gets the release state of this <code>SessionContext</code>.
	 *  
	 * @return <code>true</code> if there is no current instance of <code>SessionContext</code> (means
	 *         that the <code>SessionContext</code> is released); otherwise <code>false</code>
	 */
	public boolean isReleased()
	{
		return instance.get() == null;
	}
	
	/**
	 * Sets the current SessionContext instance. If there already is an instance, the instance will
	 * kept as previous SessionContext until the current instance will be released. To unset the
	 * current instance you should pass <code>null</code> as parameter for the current instance.
	 * 
	 * @param pContext the current {@link SessionContext} or <code>null</code> to unset the current
	 *                 instance
	 */
	protected synchronized void setCurrentInstance(SessionContext pContext)
	{
		Vector<SessionContext> vContexts = contexts.get();
		
		if (vContexts == null)
		{
			vContexts = new Vector<SessionContext>();
			
			contexts.set(vContexts);
		}

		if (pContext == null)
		{
			//unsets the current instance -> check the cached instance
			
			int iContexts = vContexts.size();
			
			if (iContexts > 0)
			{
				//use the previous context as current instance
				SessionContext scPrevious = vContexts.remove(iContexts - 1);
				
				//save the position for navigation (the position is not a valid index!)
				scPrevious.iElementPos = iContexts - 1;

				instance.set(scPrevious);
			}
			else
			{
				//no more instances left
				instance.set(null);
			}
		}
		else
		{
			SessionContext scOld = instance.get();
			
			if (scOld != pContext)
			{
				//set another context while a context is specified -> cache the current instance for later use 
				if (scOld != null)
				{
					vContexts.add(scOld);
					
					//save the position for navigation (the position is a valid index!)
					scOld.iElementPos = vContexts.size() - 1;
				}
	
				//use the new context as current instance
				instance.set(pContext);
				
				//save the position for navigation (the position is not a valid index!)
				pContext.iElementPos = vContexts.size(); 
			}
		}
	}

	/**
	 * Puts an object with a specific name to this <code>SessionContext</code>.
	 * 
	 * @param pName the object name
	 * @param pObject the object or <code>null</code> to remove the object with <code>pName</code>
	 * @return the old object or <code>null</code> if the object was not put
	 */
	public InjectObject putObject(String pName, Object pObject)
	{
		if (pObject == null)
		{
			return removeObject(pName);
		}
		else
		{
			return putObject(new InjectObject(pName, pObject));
		}
	}
	
	/**
	 * Removes an inject object from this <code>SessionContext</code>.
	 * 
	 * @param pName the name of the inject object
	 * @return the removed object or <code>null</code> if the object was not put
	 */
	public InjectObject removeObject(String pName)
	{
		if (htObjects == null)
		{
			return null;
		}
		
		return htObjects.remove(pName);
	}
	
	/**
	 * Gets the previous SessionContext, if available.
	 * 
	 * @return the SessionContext which was created before this context was created or 
	 *         <code>null</code> if there is no previous created context
	 */
	public SessionContext getPreviousContext()
	{
		if (iElementPos > 0)
		{
			return contexts.get().get(iElementPos - 1);
		}
		
		return null;
	}
	
	/**
	 * Gets the next SessionContext, if available.
	 * 
	 * @return the Sessioncontext which was created after this context was created or
	 *         <code>null</code> if this context is the current context
	 */
	public SessionContext getNextContext()
	{
		Vector<SessionContext> vContext = contexts.get(); 
		
		int iSize;
		
		if (vContext != null)
		{
			iSize = vContext.size() - 1;
		}
		else
		{
			iSize = 0;
		}
		
		//the current context is not in the list!
		if (iSize == iElementPos)
		{
			return instance.get();
		}
		else if (iElementPos < iSize)
		{
			return vContext.get(iElementPos + 1);
		}
		
		return null;
	}
	
}	// SessionContext
