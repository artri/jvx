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
 * 01.10.2008 - [JR] - creation
 * 03.11.2008 - [JR] - setUserName implemented (important for security managers)
 * 08.11.2008 - [JR] - executeIntern: get the object via generic bean method (if no getter is available)
 *                   - removeCallBackResults: don't set access time (see comment)
 * 08.11.2008 - [JR] - executeIntern: generic call of actions      
 * 19.11.2008 - [JR] - used ThreadHandler for callback thread       
 * 11.02.2009 - [JR] - changed properties to <String, Object> instead of <String, String>  
 * 04.04.2009 - [JR] - getChangedProperties removed and implemented getProperties
 * 06.05.2009 - [JR] - implemented ISession
 * 09.05.2009 - [JR] - made abstract
 * 25.05.2009 - [JR] - replaced constants with IConnectionConstants    
 * 27.05.2009 - [JR] - used Call for method parameters  
 * 15.07.2009 - [JR] - get: called getObject [BUGFIX]
 * 29.07.2009 - [JR] - createSessionContext: set object name and method name       
 * 17.08.2009 - [JR] - setExecuting, isExecuting implemented [BUGFIX]    
 * 30.09.2009 - [JR] - call, callIntern: executeIntern instead of executeWithSessionContext      
 *                   - CallBackWorker: executeIntern instead of executeWithSessionContext
 * 18.11.2009 - [JR] - #33: put(String, Object) implemented
 * 27.01.2010 - [JR] - delegated invoke to the ObjectProvider
 *                   - removed getObject because the "unknown object" exception will now be thrown 
 *                     from the ObjectProvier
 * 23.02.2010 - [JR] - setMaxInactiveInterval, setAliveInterval now sets the connection property [BUGFIX]
 *                   - #18: 
 *                     * setPropertyIntern: inactive interval and alive interval are now Number values
 *                     * executeWithSessionContext: removed String cast for Object value [BUGFIX]
 * 03.03.2010 - [JR] - executeWithSessionContext: used List instead of ArrayUtil
 * 17.04.2010 - [JR] - #118: setSerializer: set session property     
 * 07.06.2010 - [JR] - #49: access control support   
 * 24.10.2010 - [JR] - setPropertyIntern: support numbers as string   
 * 18.11.2010 - [JR] - #206: getApplicationZone made protected  
 * 26.02.2011 - [JR] - #171: isInjectionAllowed implemented and used in getInjectObjects
 *                   - #172: getInjectObjects now supports class
 * 02.03.2011 - [JR] - #297: putObject/removeObject/getObject implemented
 * 03.03.2011 - [JR] - addCallInfo: add call before first ObjectProvider call 
 * 31.07.2011 - [JR] - #16: prepareException used  
 * 22.09.2011 - [JR] - #475: changed isAllowed(String) to isAllowed(ISession)
 *                   - #476: change external/creation properties 
 * 14.11.2011 - [JR] - introduced getPropertyAsString
 * 13.12.2011 - [JR] - #523: setApplicationName implemented   
 * 21.11.2013 - [JR] - execute: setLastAccessTime instead of setLastAliveTime 
 * 04.12.2013 - [JR] - #885: lazy inject object initialization
 * 12.11.2014 - [JR] - set multiple properties via setProperties instead of executeWithSessionContext           
 * 18.12.2014 - [JR] - #1217: removed object id           
 * 26.01.2015 - [JR] - #1238: Exception recording     
 * 27.05.2015 - [JR] - #1397: SessionCallHandler used 
 * 28.01.2016 - [JR] - #1555: addCallInfo checks if call was already added 
 * 02.03.2016 - [JR] - #1576: catch Throwable in addCallInfo
 * 22.06.2016 - [JR] - IInjectObject introduced for injected objects
 * 21.11.2017 - [JR] - #1856: lock mechanism introduced
 * 21.12.2017 - [JR] - #1868: alive interval (timeout) introduced
 * 11.12.2019 - [JR] - support inject objects with ServiceLoader
 */
package com.sibvisions.rad.server;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ServiceLoader;
import java.util.UUID;

import javax.rad.remote.IConnectionConstants;
import javax.rad.server.AbstractObjectProvider;
import javax.rad.server.ICallBackBroker;
import javax.rad.server.ISession;
import javax.rad.server.InjectObject;
import javax.rad.server.ResultObject;
import javax.rad.server.ServerContext;
import javax.rad.server.SessionContext;
import javax.rad.util.SilentAbortException;

import com.sibvisions.rad.remote.ISerializer;
import com.sibvisions.rad.server.config.ApplicationZone;
import com.sibvisions.rad.server.config.Zone;
import com.sibvisions.rad.server.protocol.ICategoryConstants;
import com.sibvisions.rad.server.protocol.ICommandConstants;
import com.sibvisions.rad.server.protocol.ProtocolFactory;
import com.sibvisions.rad.server.protocol.Record;
import com.sibvisions.rad.server.security.AbstractSecurityManager;
import com.sibvisions.rad.server.security.IAccessController;
import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.ChangedHashtable;
import com.sibvisions.util.ThreadHandler;
import com.sibvisions.util.log.ILogger;
import com.sibvisions.util.log.LoggerFactory;
import com.sibvisions.util.type.CommonUtil;
import com.sibvisions.util.type.StringUtil;
import com.sibvisions.util.xml.XmlNode;

/**
 * A <code>Session</code> is a server side session which will be started 
 * when an <code>IConnection</code> connects to a remote server.<br>
 * The session persists for a specified time period, across more than one request 
 * from the <code>IConnection</code>.
 * 
 * @author René Jahn
 * @see ISession
 */
public abstract class AbstractSession implements ICallBackSession
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** the log instance. */
	protected ILogger log = LoggerFactory.getInstance(getClass());
	
	/** the assigned session manager for this session. */
	private DefaultSessionManager manager = null;
	
	/** the object provider of the server. */
	private AbstractObjectProvider objProvider = null;

	/** the call handler. */
	private SessionCallHandler callHandler = null;

	/** the callback broker. */
	private ICallBackBroker broker = null;
	
	/** the serializer for this session. */
	private ISerializer serializer = null;
	
	/** the call-back result-object cache. */
	private ArrayUtil<ResultObject> auCallBackResult = null;

    /** the client and server properties. */
    protected ChangedHashtable<String, Object> chtProperties = new ChangedHashtable<String, Object>();
    
    /** the properties got via constructor. */
    protected ChangedHashtable<String, Object> chtExternalProperties = null;

    /** the {@link SessionContext} implementation. */
	private Class<? extends SessionContext> clsSessionContext;

    /** session ID. */
	private Object oId;
	
	/** object for synchronized access to the callback cache. */
	private Object oSyncCallBack = new Object();
	
	/** object for synchronized lock access. */
	private Object oLock = new Object();
	
	/** the list of all available inject objects. */
	private ChangedHashtable<String, InjectObject> chtInjectObjects = null;
	
	/** the number of currently executed calls. */
	private int iExecution = 0;
	
	/** session start time (create time). */
	private long lStartTime;
	
	/** last access time in millis. */
	private long lLastAccessTime;
	
	/** last alive time in millis. */
	private long lLastAliveTime;
	
	/** specifies the time, in seconds, between the session will be expired (default: endless). */
	private int iMaxInactiveInterval = 0;
	
	/** specifies the client-side alive interval. */
	private long lAliveInterval = -1;
	
	/** the lock count. */
	private int iLockCount = 0;
	
	/** the max alive factor. */
	private int iMaxAliveFactor = -1;
	
	/** the max alive timeout. */
	private int iMaxAliveTimeout = -1;
	
	/** whether access happened during lock. */
	private boolean bLockAccess = false;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Crates a new instance of <code>Session</code> with a unique
	 * session ID and the start/create time.
	 * 
	 * @param pManager the assigned session manager
	 * @param pProperties the initial session properties
	 * @see DefaultSessionManager
	 */
	AbstractSession(DefaultSessionManager pManager, 
			        ChangedHashtable<String, Object> pProperties)
	{
	    Record record = ProtocolFactory.openRecord(ICategoryConstants.SESSION, ICommandConstants.SESSION_INIT);

	    try
	    {
    		manager = pManager;
    		objProvider = pManager.getServer().getObjectProvider();
    		chtExternalProperties = pProperties;
    		
    		oId          = UUID.randomUUID().toString();
    		lStartTime   = System.currentTimeMillis();
    		
    		setLastAccessTime(lStartTime);
    		
    		//"clone" because it's possible that we change chtExternalProperties in the loop
    		Hashtable<String, Object> htCopy = new Hashtable<String, Object>(pProperties);
    		
    		//set every property, to fill intern properties and validate the usage
    		for (Map.Entry<String, Object> entry : htCopy.entrySet())
    		{
    			setPropertyIntern(entry.getKey(), entry.getValue());
    		}
	    }
	    finally
	    {
	        CommonUtil.close(record);
	    }
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the application zone for which the session was created.
	 * 
	 * @return the application zone
	 */
	protected abstract ApplicationZone getApplicationZone();
	
	/**
	 * Gets the {@link IAccessController} for checking the access to server side objects
	 * and calls.
	 * 
	 * @return the object inspector or <code>null</code> if no inspector is available
	 */
	public abstract IAccessController getAccessController();

    /**
     * Sets the new password for the current session.
     * 
     * @param pOldPassword the old password
     * @param pNewPassword the new password
     * @throws Exception if the password can not be changed
     */
    public abstract void setNewPassword(String pOldPassword, String pNewPassword) throws Exception;
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public Object getId()
	{
		return oId;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getLifeCycleName()
	{
		return getPropertyAsString(IConnectionConstants.LIFECYCLENAME);
	}

	/**
	 * {@inheritDoc}
	 */
	public final String getApplicationName()
	{
		return getPropertyAsString(IConnectionConstants.APPLICATION);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public final String getUserName()
	{
		return getPropertyAsString(IConnectionConstants.USERNAME);
	}

	/**
	 * {@inheritDoc}
	 */
	public final String getPassword()
	{
		return getPropertyAsString(IConnectionConstants.PASSWORD);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * The access time will be updated.
	 * 
	 * @param pName {@inheritDoc}
	 */
	public Object getProperty(String pName)
	{
		setLastAccessTime(System.currentTimeMillis());
		
		return getPropertyIntern(pName);
	}

	/**
	 * {@inheritDoc}
	 */
	public long getStartTime()
	{
		return lStartTime; 
	}

	/**
	 * {@inheritDoc}
	 */
	public long getLastAccessTime()
	{
		return lLastAccessTime;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setMaxInactiveInterval(int pMaxInactiveInterval)
	{
		this.iMaxInactiveInterval = pMaxInactiveInterval;
		
		chtProperties.put(IConnectionConstants.SESSIONTIMEOUT_IN_SECONDS, Integer.valueOf(pMaxInactiveInterval));
		//backwards compatibility
		chtProperties.put(IConnectionConstants.SESSIONTIMEOUT, Integer.valueOf(pMaxInactiveInterval > 0 ? (pMaxInactiveInterval / 60) : pMaxInactiveInterval));
		
		if (chtExternalProperties != null)
		{
			chtExternalProperties.put(IConnectionConstants.SESSIONTIMEOUT_IN_SECONDS, Integer.valueOf(pMaxInactiveInterval));
			//backwards compatibility
			chtExternalProperties.put(IConnectionConstants.SESSIONTIMEOUT, Integer.valueOf(pMaxInactiveInterval > 0 ? (pMaxInactiveInterval / 60) : pMaxInactiveInterval));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public int getMaxInactiveInterval()
	{
		return iMaxInactiveInterval;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isInactive(long pAccessTime)
	{
		return iMaxInactiveInterval > 0 && !isExecuting() && !isLocked() && getLastAccessTime() + iMaxInactiveInterval * 1000L < pAccessTime;
	}

	/**
	 * {@inheritDoc}
	 */
	public long getLastAliveTime()
	{
		return lLastAliveTime;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setAliveInterval(long pAliveInterval)
	{
		this.lAliveInterval = pAliveInterval;
		
		chtProperties.put(IConnectionConstants.ALIVEINTERVAL, Long.valueOf(pAliveInterval));
		
		if (chtExternalProperties != null)
		{
			chtExternalProperties.put(IConnectionConstants.ALIVEINTERVAL, Long.valueOf(pAliveInterval));
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public long getAliveInterval()
	{
		return lAliveInterval;
	}
	
	/**
	 * Checks if the session is alive. That means the client sends alive
	 * messages. The session is not alive if the client doesn't send alive
	 * messages during 4 times of the desired alive interval.
	 * 
	 * @param pAccessTime {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	public boolean isAlive(long pAccessTime)
	{
 	    //when the session is executing a command and it tooks much time, then the session is still alive,
        //because the client is not able to send commands (syncronized IConnection calls!)
		boolean bAlive = isExecuting()
				         //locked means that the session is "in use" but not executing
				         || isLocked()
					     //the client "always" sends ALIVE. If that's not the case for 4x the usual interval, then we assume
					     //that the client is not alive!
				         || !isMaxAliveIntervalExceeded(pAccessTime);
		
		if (!bAlive)
		{
			log.debug("Executing: ", Boolean.valueOf(isExecuting()), 
					  ", Locked: ", Boolean.valueOf(isLocked()), 
					  ", Interval exceeded: ", Boolean.valueOf(!isMaxAliveIntervalExceeded(pAccessTime)));
		}
		
		return bAlive;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Object call(String pObjectName, String pMethod, Object... pParams) throws Throwable
	{
        Record record = openRecord(ICommandConstants.SESSION_CALL);
        
        try
        {
            if (record != null)
            {
                record.setParameter(getLifeCycleName(), pObjectName, pMethod, pParams);
            }
            
    		return executeIntern(new Call(null, pObjectName, pMethod, pParams));
        }
        catch (Throwable th)
        {
            if (record != null)
            {
                record.setException(th);
            }
            
            throw th;
        }
        finally
        {
            CommonUtil.close(record);
        }
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Object callAction(String pAction, Object... pParams) throws Throwable
	{
        Record record = openRecord(ICommandConstants.SESSION_CALL_ACTION);
        
        try
        {
            if (record != null)
            {
                record.setParameter(getLifeCycleName(), pAction, pParams);
            }
            
    		return executeIntern(new Call(null, null, pAction, pParams));
        }
        catch (Throwable th)
        {
            if (record != null)
            {
                record.setException(th);
            }
            
            throw th;
        }
        finally
        {
            CommonUtil.close(record);
        }
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Object get(String pObjectName) throws Throwable
	{
		//Always use a new context. If another context is available, then
		//the SessionContext handles the context chain!
		SessionContext context = createSessionContext(pObjectName, null);
		
		try
		{
			return objProvider.getObject(this, pObjectName);
		}
		catch (Throwable th)
		{
			throw addCallInfo(th, pObjectName, null);
		}
		finally
		{
			if (context != null)
			{
				context.release();
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Object put(String pObjectName, Object pObject) throws Throwable
	{
		//Always use a new context. If another context is available, then
		//the SessionContext handles the context chain!
		SessionContext context = createSessionContext(pObjectName, null);

		try
		{
			return objProvider.putObject(this, pObjectName, pObject);		
		}
		catch (Throwable th)
		{
			throw addCallInfo(th, pObjectName, null);
		}
		finally
		{
			if (context != null)
			{
				context.release();
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public ICallBackBroker getCallBackBroker()
	{
		if (broker == null)
		{
			broker = new SessionCallBackBroker(this);
		}
		
		return broker;
	}	
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Uninitializes the session. Be careful with this method. It should be used in constructor if an
	 * Exception occurs.
	 */
	final void uninit()
	{
	    if (objProvider instanceof DefaultObjectProvider)
	    {
	        ((DefaultObjectProvider)objProvider).destroySession(this);
	    }
	}
	
	/**
	 * Gets the value of a property as string. This method does not cast the value to a String, if
	 * calls toString(). This offers more flexibility, because sometimes it is possible that a property
	 * contains a non transferable Object. A string is always transferable 
	 * 
	 * @param pPropertyName the name of the property
	 * @return the string represenation of the value or <code>null</code> if the property is not set
	 */
	private final String getPropertyAsString(String pPropertyName)
	{
		//more flexible as a cast to String (we can set non transferable custom objects)
		Object oValue = chtProperties.get(pPropertyName);
		
		if (oValue != null)
		{
			return oValue.toString();
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * Sets the name of the life-cycle object for this session.
	 * 
	 * @param pName the name of the life cycle object
	 */
	final void setLifeCycleName(String pName)
	{
		chtProperties.put(IConnectionConstants.LIFECYCLENAME, pName);
	}
	
	/**
	 * Sets the application name.
	 * 
	 * @param pApplicationName the application name
	 */
	final void setApplicationName(String pApplicationName)
	{
		chtProperties.put(IConnectionConstants.APPLICATION, pApplicationName);
	}
	
	/**
	 * Sets the sessions user name.
	 * 
	 * @param pUserName the user name
	 */
	public final void setUserName(String pUserName)
	{
		chtProperties.put(IConnectionConstants.USERNAME, pUserName);
	}

	/**
	 * Sets the session password.
	 * 
	 * @param pPassword the password
	 */
	public final void setPassword(String pPassword)
	{
		chtProperties.put(IConnectionConstants.PASSWORD, pPassword);
	}
	
	/**
	 * Sets the serializer for this session.
	 * 
	 * @param pSerializer the serialier implementation
	 */
	void setSerializer(ISerializer pSerializer)
	{
		this.serializer = pSerializer;
		
		if (pSerializer != null)
		{
			setPropertyIntern(IConnectionConstants.PREFIX_SERVER + IConnectionConstants.PREFIX_SESSION + "serializer", pSerializer.getClass().getSimpleName());
		}
		else
		{
			setPropertyIntern(IConnectionConstants.PREFIX_SERVER + IConnectionConstants.PREFIX_SESSION + "serializer", "");
		}
	}
	
	/**
	 * Returns the serializer for this session.
	 * 
	 * @return the serializer implementation
	 */
	ISerializer getSerializer()
	{
		return serializer;
	}
	
	/**
	 * Sets the time of the last session access.
	 * 
	 * @param pLastAccessTime access time in millis
	 */
	public void setLastAccessTime(long pLastAccessTime)
	{
		this.lLastAccessTime = pLastAccessTime;

		//manual change of access time -> change access time in unlock
		bLockAccess = true;
		
		setLastAliveTime(pLastAccessTime);
	}
	
	/**
	 * Sets the time of the last communication of the session.
	 * 
	 * @param pLastAliveTime the last communication/alive time
	 */
	public void setLastAliveTime(long pLastAliveTime)
	{
		this.lLastAliveTime = pLastAliveTime;
	}
	
	/**
	 * Executes a remote method call.
	 * 
	 * @param pCall the call information
	 * @return result of method call or null if it's an asynchronous method call
	 * @throws Throwable if an exception occurs during call
	 * @throws SecurityException if the call is not allowed
	 */
	final Object execute(Call pCall) throws Throwable
	{
        Record record = openRecord(ICommandConstants.SESSION_CALL);
        
        try
        {
    	    chtExternalProperties = null;
    		
    		setExecuting(true);

            if (record != null)
            {
                record.setParameter(getLifeCycleName(), pCall.getObjectName(), pCall.getMethodName(), pCall.getParameters());
            }
    		
    		try
    		{
    			//save the access time for this call
    			setLastAccessTime(System.currentTimeMillis());
    
    			//executes the call within the SessionContext
    			Object oResult = executeIntern(pCall);
    			
    			return oResult;
    		}
    		finally
    		{
    			//otherwise very long calls will time out the session!
    			setLastAccessTime(System.currentTimeMillis());
    
    			setExecuting(false);
    		}
        }
        finally
        {
            CommonUtil.close(record);
        }
	}
	
	/**
	 * Creates a {@link SessionContext} and executes the call. This method is for internal use only.
	 * 
	 * @param pCall the call information
	 * @return result of method call or null if it's an asynchronous method call
	 * @throws Throwable if an exception occurs during call
	 * @throws SecurityException if the call is not allowed
	 */
	private final Object executeIntern(Call pCall) throws Throwable
	{
		SessionContext context = createSessionContext(pCall.getObjectName(), pCall.getMethodName());
		
		try
		{
			return executeWithSessionContext(context, pCall);
		}
		finally
		{
			if (context != null)
			{
				context.release();
			}
		}
	}
	
	/**
	 * Executes a remote method call. This method can be overwritten from sub classes.
	 * 
	 * @param pContext the current session context
	 * @param pCall the call information
	 * @return result of method call or null if it's an asynchronous method call
	 * @throws Throwable if an exception occurs during call
	 * @throws SecurityException if the call is not allowed
	 */
	protected Object executeWithSessionContext(SessionContext pContext, Call pCall) throws Throwable
	{
        if (callHandler != null)
        {
            callHandler.fireBeforeCall(pCall);
        }
	    
		if (pCall.isCallBack())
		{
			//asynchronuous call
			ThreadHandler.start(new CallBackWorker(pCall));

	        if (callHandler != null)
	        {
	            callHandler.fireAfterCall(pCall, null, null);
	        }
			
			return null;
		}
		else
		{
	        String sObjectName = pCall.getObjectName();
	        String sMethodName = pCall.getMethodName();
	        Object[] oParams = pCall.getParameters();

	        try
			{
				//synchronous call
				Object oResult = objProvider.invoke(this, sObjectName, sMethodName, oParams);
				
	            if (callHandler != null)
	            {
	                callHandler.fireAfterCall(pCall, oResult, null);
	            }
				
				return oResult;
			}
			catch (Throwable th)
			{
                if (callHandler != null)
                {
                    callHandler.fireAfterCall(pCall, null, th);
                }

                throw addCallInfo(th, sObjectName, sMethodName);
			}
		}
	}	
	
	/**
	 * Adds the result of an asynchronous call to the cache.
	 * 
	 * @param pResult the result of an asynchronous call
	 */
	protected void addCallBackResult(ResultObject pResult)
	{
		synchronized(oSyncCallBack)
		{
			if (auCallBackResult == null)
			{
				auCallBackResult = new ArrayUtil<ResultObject>();
			}

			auCallBackResult.add(pResult);
		}
	}
	
	/**
	 * Removes all result objects of asynchronous calls from the cache. 
	 * The access time will be updated.
	 *  
	 * @return the current object cache
	 */
	ArrayUtil<ResultObject> removeCallBackResults()
	{
		//don't change the access time -> because alive checks calls this
		//method every time...
		//the session would not time out if the access time will be set
		return removeCallBackResultsIntern();
	}
	
	/**
	 * Removes all result objects of asynchronous calls from the cache.
	 * The alive time will be updated.
	 *  
	 * @return the current object cache
	 */
	private ArrayUtil<ResultObject> removeCallBackResultsIntern()
	{
		setLastAliveTime(System.currentTimeMillis());
		
		synchronized(oSyncCallBack)
		{
			ArrayUtil<ResultObject> auReference = auCallBackResult;
			
			auCallBackResult = null;
			
			return auReference;
		}
	}
	
	/**
	 * Sets a property. The access time will be updated.
	 * 
	 * @param pName the property name
	 * @param pValue the value of the property or <code>null</code> to delete the property
	 */
	public void setProperty(String pName, Object pValue)
	{
		setLastAccessTime(System.currentTimeMillis());		
		
		setPropertyIntern(pName, pValue);
	}
	
	/**
	 * Sets multiple properties.
	 * 
	 * @param pProperties the key/value paris as list of <code>Object[]</code>
	 */
	protected void setProperties(List<Object[]> pProperties)
	{
	    setLastAccessTime(System.currentTimeMillis());
	    
	    if (pProperties != null)
	    {
    	    Object[] oPair;
            
            for (int i = 0, anz = pProperties.size(); i < anz; i++)
            {
                oPair = (Object[])pProperties.get(i);
                
                setPropertyIntern((String)oPair[0], oPair[1]);
            }
	    }
	}

	/**
	 * Sets internal members with property values. The alive time will be updated.
	 * 
	 * @param pName the property name
	 * @param pValue the property value
	 */
	protected final void setPropertyIntern(String pName, Object pValue)
	{
		setLastAliveTime(System.currentTimeMillis());

		checkPropertyAccess(pName, pValue);
		
		if (IConnectionConstants.SESSIONTIMEOUT.equals(pName))
		{
		    int iValue;
		    
			if (pValue instanceof String)
			{
			    iValue = Integer.valueOf((String)pValue).intValue();
			}
			else
			{
			    iValue = ((Number)pValue).intValue();
			}
			
            if (iValue > 0)
            {
                setMaxInactiveInterval(iValue * 60);
            }
            else
            {
                setMaxInactiveInterval(iValue);
            }
		}
		else if (IConnectionConstants.SESSIONTIMEOUT_IN_SECONDS.equals(pName))
		{
			if (pValue instanceof String)
			{
				setMaxInactiveInterval(Integer.valueOf((String)pValue).intValue());
			}
			else
			{
				setMaxInactiveInterval(((Number)pValue).intValue());
			}
		}
		else if (IConnectionConstants.ALIVEINTERVAL.equals(pName))
		{
			if (pValue instanceof String)
			{
				setAliveInterval(Long.valueOf((String)pValue).longValue());
			}
			else
			{
				setAliveInterval(((Number)pValue).longValue());
			}
		}
		else
		{
			chtProperties.put(pName, pValue);
			
			if (chtExternalProperties != null)
			{
				chtExternalProperties.put(pName, pValue);
			}
		}
	}
	
	/**
	 * Checks if it's allowed to access a property.
	 * 
	 * @param pName the property name
	 * @param pValue the property value
	 */
	private void checkPropertyAccess(String pName, Object pValue)
	{
		if (pName == null)
		{
			throw new IllegalArgumentException("Invalid property name: null");
		}
		
		if ((IConnectionConstants.SESSIONTIMEOUT.equals(pName) || IConnectionConstants.SESSIONTIMEOUT_IN_SECONDS.equals(pName)) 
		    && (pValue == null))
		{
			throw new SecurityException("It's not allowed to change the property '" + pName + "' to '" + pValue + "'");
		}
	}
	
	/**
	 * Gets the value of a property. The alive time will be updated.
	 * 
	 * @param pName the property name
	 * @return the value of the property or <code>null</code> if the property is not available
	 */
	private Object getPropertyIntern(String pName)
	{
		setLastAliveTime(System.currentTimeMillis());
		
		if (pName == null)
		{
			throw new IllegalArgumentException("Invalid property name '" + pName + "'");
		}
		
		return chtProperties.get(pName);
	}
	
	/**
	 * Gets a reference to the internal properties.
	 * 
	 * @return the session properties
	 */
	public ChangedHashtable<String, Object> getProperties()
	{
	    //don't change the access time
		return chtProperties;
	}
	
	/**
	 * Gets the session manager for this session.
	 * 
	 * @return the session manager
	 */
	protected DefaultSessionManager getSessionManager()
	{
		return manager;
	}
	
	/**
	 * Gets the object provider.
	 * 
	 * @return the object provider
	 */
	AbstractObjectProvider getObjectProvider()
	{
	    return objProvider;
	}
	
	/**
	 * Initializes the {@link SessionContext} for this session.
	 * 
	 * @param pObjectName the name of the object from the call
	 * @param pMethodName the name of the method from the call
	 * @return the context for this session
	 */
	final SessionContext createSessionContext(String pObjectName, String pMethodName)
	{
		SessionContext context;
		
		if (clsSessionContext == null)
		{			
			context = new SessionContextImpl(this);
		}
		else
		{
			try
			{
				context = (SessionContext)clsSessionContext.getConstructor(ISession.class).newInstance(this);
			}
			catch (Exception e)
			{
				context = new SessionContextImpl(this);
			}
		}
		
		if (context instanceof AbstractSessionContext)
		{
			((AbstractSessionContext)context).setObjectName(pObjectName);
			((AbstractSessionContext)context).setMethodName(pMethodName);
		}
		
		return context;
	}
	
	/**
	 * Sets the implementation of the {@link SessionContext}.
	 * 
	 * @param pClass the session context class name or <code>null</code> to unset
	 */
	final void setSessionContextImpl(Class<? extends SessionContext> pClass)
	{
		clsSessionContext = pClass;	
	}
	
	/**
	 * Gets the implementation of the {@link SessionContext}.
	 * 
	 * @return the session context class name or <code>null</code> if unset
	 */
	final Class<? extends SessionContext> getSessionContextImpl()
	{
		return clsSessionContext;
	}
	
	/**
	 * Puts an object to the cache. If an object with the same name is already added, it will be replaced.
	 * 
	 * @param pObject the object to inject
	 * @return the previous object or <code>null</code> if there was no object with the same object name added
	 */
	public InjectObject putObject(InjectObject pObject)
	{
		if (pObject == null)
		{
			return null;
		}
		else
		{
			String sName = pObject.getName();

			if (sName == null || sName.startsWith("!"))
			{
				throw new RuntimeException("The object name '" + sName + "' is not valid!");
			}
			
			try
			{
				initInjectObjects();
			}
			catch (Exception e)
			{
				//configuration problem - should not occur in production use
				throw new RuntimeException(e);
			}
			
			if (chtInjectObjects.containsKey("!" + sName))
			{
				throw new RuntimeException("It is not allowed to change the preconfigured object '" + sName + "'!");
			}
			
			return chtInjectObjects.put(sName, pObject);
		}
	}
	
	/**
	 * Removes an object from the cache.
	 * 
	 * @param pObject the object to remove
	 * @return the removed object or <code>null</code> if an object with the same object name was not found
	 */
	public InjectObject removeObject(InjectObject pObject)
	{
		if (pObject == null)
		{
			return null;
		}
		else
		{
			//It is not allowed to remove preconfigured objects
			String sName = pObject.getName();

			if (sName == null || sName.startsWith("!"))
			{
				throw new RuntimeException("The object name '" + sName + "' is not valid!");
			}
			
			try
			{
				initInjectObjects();
			}
			catch (Exception e)
			{
				//configuration problem - should not occur in production use
				throw new RuntimeException(e);
			}
			
			if (chtInjectObjects.containsKey("!" + sName))
			{
				throw new RuntimeException("It is not allowed to remove the preconfigured object '" + sName + "'!");
			}
			
			return chtInjectObjects.remove(sName);
		}
	}
	
	/**
	 * Gets an object from the cache.
	 * 
	 * @param pName the name of the object
	 * @return the object or <code>null</code> if an object with the given name was not found
	 */
	public InjectObject getObject(String pName)
	{
		try
		{
			initInjectObjects();
			
			//default case: check if the object is a preconfigured object
			InjectObject iobj = chtInjectObjects.get("!" + pName);
			
			if (iobj != null)
			{
				return iobj;
			}
			else
			{
				//special case: check if the object is a programatically added object
				return chtInjectObjects.get(pName);
			}
		}
		catch (Exception e)
		{
			//configuration problem - should not occur in production use
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Gets an {@link Enumeration} of known inject objects.
	 * 
	 * @return the known inject objects or <code>null</code> if there are no objects to inject
	 * @throws Exception if the injection configuration is invalid
	 */
	Enumeration<InjectObject> getInjectObjects() throws Exception
	{
		initInjectObjects();
		
		return chtInjectObjects.elements();
	}
	
	/**
	 * Gets only changed inject objects since the list of inject objects was created.
	 * 
	 * @return the list of changed objects
	 */
	List<Entry<String, InjectObject>> getChangedInjectObjects()
	{
		//don't initInjectObjects because we return only changed entries!
		if (chtInjectObjects == null)
		{
			return null;
		}

		return chtInjectObjects.getChanges();
	}
	
	/**
	 * Reads the configuration file and initializes the inject objects if they are not already
	 * initialized.
	 * 
	 * @throws Exception if the configuration file is invalid or contains invalid objects
	 */
	private void initInjectObjects() throws Exception
	{
		if (chtInjectObjects == null)
		{
			//init the injection objects for the first usage
				
			chtInjectObjects = new ChangedHashtable<String, InjectObject>(); 
				
			try
			{
				List<XmlNode> liInject = getApplicationZone().getNodes("/application/inject");
					
				if (liInject != null)
				{
					XmlNode ndName;
					XmlNode ndObject;

					Object obj;

					Class<?> clazz;
					
					String sName;
					String sLazyObject;

					for (XmlNode node : liInject)
					{
						obj = null;
						sLazyObject = null;
						
						ndName = node.getNode("name");
						
						if (ndName != null)
						{
							sName = ndName.getValue();
						}
						else
						{
							sName = null;
						}
						
						ndObject = node.getNode("object");
						
						//check injection by predefined objectname

						if (ndObject != null)
						{
							String sObject = ndObject.getValue();
							
							if ("monitoring".equals(sObject))
							{
								obj = ((Server)getSessionManager().getServer()).getMonitoring();
								
								if (sName == null)
								{
									sName = sObject;
								}
							}
							else
							{
								throw new SecurityException("Object '" + sObject + "' is not supported!");
							}
						}
						else
						{
							//check injection by class
							
							ndObject = node.getNode("class");
							
							if (ndObject != null)
							{
								sLazyObject = ndObject.getValue(); 
							}
							else
							{
								throw new SecurityException("Unsupported inject definition: " + node);
							}
						}
						
						if (obj != null || !StringUtil.isEmpty(sLazyObject))
						{
							if (isInjectionAllowed(node))
							{
								if (obj == null)
								{
									try
									{
										clazz = getClass(sLazyObject);
										
										obj = clazz.newInstance();
										
										if (obj instanceof IInjectObject)
										{
											try
											{
												if (sName == null)
												{
													sName = ((IInjectObject)obj).getDefaultName(); 
												}
												
												((IInjectObject)obj).init(this);
											}
											catch (SilentAbortException sae)
											{
												log.debug(sae);
											}
										}
										
										if (sName == null)
										{
											sName = clazz.getSimpleName();
										}
									}
									catch (Throwable th)
									{
										log.error(th);
										
										obj = null;
									}
								}
								
								putInjectObject(chtInjectObjects, sName, obj);
							}
						}
					}
				}
				
				Iterator<IInjectObject> itInjectObjects;
				
				ClassLoader loader = objProvider.getClassLoader(this);
				
				try
				{
		            if (loader == null)
		            {
		                itInjectObjects = ServiceLoader.load(IInjectObject.class, getClass().getClassLoader()).iterator();
		            }
		            else
		            {
		                itInjectObjects = ServiceLoader.load(IInjectObject.class, loader).iterator();
		            }
		            
		            String sName = null;
		            
		            while (itInjectObjects.hasNext()) 
		            {
		            	IInjectObject injobj = itInjectObjects.next();
		            	
						try
						{
							sName = injobj.getDefaultName();
							
							injobj.init(this);
							
							putInjectObject(chtInjectObjects, sName, injobj);
						}
						catch (SilentAbortException sae)
						{
							log.debug(sName, injobj, sae);
						}
						catch (Throwable th)
						{
							log.error(sName, injobj, th);
						}
		            }	            
				}
				catch (Throwable th)
				{
					log.error("Loading injectable objects with ServiceLoader failed!", th);
				}
			}
			catch (Exception e)
			{
				chtInjectObjects = null;
				
				throw e;
			}
		}
	}
	
	/**
	 * Puts an object to the list of automatically inject objects.
	 * 
	 * @param pObjects the cached inject objects
	 * @param pName the name
	 * @param pObject the object
	 */
	private static void putInjectObject(ChangedHashtable<String, InjectObject> pObjects, String pName, Object pObject)
	{
		if (pObject == null)
		{
			return;
		}
		
		if (pObjects.containsKey("!" + pName))
		{
			throw new IllegalArgumentException("Object name '" + pName + "' is already used!");
		}
		
		//use call sign that we know the difference between configured and programmatically added
		//objects
		pObjects.put("!" + pName, new InjectObject(pName, pObject), false);
	}
	
	/**
	 * Checks if an object injection is allowed by specific allow rules.
	 * 
	 * @param pNode the inject definition
	 * @return <code>true</code> if the injection is allowed for this session, <code>false</code> otherwise
	 */
	private boolean isInjectionAllowed(XmlNode pNode)
	{
		List<XmlNode> liAllow = pNode.getNodes("allow");
		
		//NO specific allow rules -> allow injection
		if (liAllow == null)
		{
			return true;
		}
		
		XmlNode ndInstanceOf;
		XmlNode ndLcoName;
		
		Class<?> clazz;
		
		String sLcoName = getLifeCycleName();
		String sInstanceOf;
		
		for (XmlNode node : liAllow)
		{
			ndInstanceOf = node.getNode("instanceof");
			ndLcoName = node.getNode("lifecyclename");
			
			//check by name
			if (ndLcoName != null)
			{
				if (sLcoName.equals(ndLcoName.getValue()))
				{
					return true;
				}
			}
			
			//check by instance
			if (ndInstanceOf != null)
			{
				sInstanceOf = ndInstanceOf.getValue();
				
				try
				{
					clazz = getClass(sInstanceOf);
					
					if (clazz.isAssignableFrom(getClass()))
					{
						return true;
					}
				}
				catch (Exception e)
				{
					log.error(e);
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Returns whether the session is execution at least one command.
	 * 
	 * @return <code>true</code> if the session is executing a command, otherwise <code>false</code>
	 */
	protected boolean isExecuting()
	{
		return iExecution > 0;
	}
	
	/**
	 * Gets whether the alive interval is exceeded.
	 * 
	 * @param pAccessTime the access time
	 * @return <code>true</code> if alive timeout is exceeded, <code>false</code> otherwise
	 */
	protected boolean isMaxAliveIntervalExceeded(long pAccessTime)
	{
	    if (lAliveInterval >= 0)
	    {
	        long lTimeout = -1;
	        long lTimeoutFactor = -1;

	        if (iMaxAliveTimeout > 0)
	        {
	            lTimeout = iMaxAliveTimeout * 1000;
	        }
	        
	        if (iMaxAliveFactor > 0)
	        {
	            lTimeoutFactor = lAliveInterval * iMaxAliveFactor;
	        }
	        
	        if (lTimeout > 0 && lTimeoutFactor > 0)
	        {
	            lTimeout = Math.min(lTimeout, lTimeoutFactor);
	        }
	        else if (lTimeout > 0 || lTimeoutFactor > 0)
	        {
	            lTimeout = Math.max(lTimeout, lTimeoutFactor);
	        }
	        else
	        {
	            //default
	            lTimeout = lAliveInterval * 4;
	        }
	        
	        return getLastAliveTime() + lTimeout < pAccessTime;
	    }
	    
        return false;
	}
	
	/**
	 * Sets that the session is executiong a command. The method considers that sub exections will
	 * be made. It changes a counter per method call. 
	 * 
	 * @param pExecuting <code>true</code> to increase the execution counter; <code>false</code> to
	 *                   decrease the execution counter
	 */
	protected void setExecuting(boolean pExecuting)
	{
		if (pExecuting)
		{
			iExecution++;
		}
		else
		{
			iExecution--;
		}
	}
	
	/**
	 * Checks if it is allowed to create the session.
	 */
	protected void checkAccess()
	{
	    IAccessController acc = getAccessController();
		
		if (acc != null)
		{
			if (!acc.isAllowed(getLifeCycleName()))
			{
				throw new SecurityException("Access to '" + getLifeCycleName() + "' denied!");
			}
		}
	}
	
	/**
	 * Adds additional information to an exception. The information are the object name and
	 * the method name.
	 *  
	 * @param pCause the cause
	 * @param pObjectName the object name of the call
	 * @param pMethodName the method name of the call
	 * @return the cause with additional information
	 */
	private Throwable addCallInfo(Throwable pCause, String pObjectName, String pMethodName)
	{
		Throwable thCurrent = pCause;
		
		do
		{
			StackTraceElement[] ste = thCurrent.getStackTrace();
			
			if (ste != null)
			{
				ArrayUtil<StackTraceElement> auStack = new ArrayUtil<StackTraceElement>();

				boolean bCustomAdd = false;
				
				for (int i = ste.length - 1; i >= 0; i--)
				{
					try
					{
						if (!bCustomAdd && AbstractObjectProvider.class.isAssignableFrom(getClass(ste[i].getClassName())))
						{
							bCustomAdd = true;
							
							StackTraceElement steNew = new StackTraceElement(getLifeCycleName() + ".<" + pObjectName + ">", 
                                                                             pMethodName != null ? pMethodName : "", 
                                                                             null, 
                                                                             -1);
							
							//don't add multiple times (possible with async call)
							if (i + 1 >= ste.length || !ste[i + 1].equals(steNew))
							{
	                            auStack.add(0, steNew); 
							}
						}
					}
					catch (Throwable th)
					{
						//nothing to be done
					}
					
					auStack.add(0, ste[i]);
				}

				StackTraceElement[] stackNew = new StackTraceElement[auStack.size()];
				auStack.toArray(stackNew);

				thCurrent.setStackTrace(stackNew);
			}
			
			thCurrent = thCurrent.getCause();
		}
		while (thCurrent != null);
		
		return pCause;
	}
	
    /**
     * Opens a record.
     * 
     * @param pCommand the command
     * @param pParameter additional parameter
     * @return the record or <code>null</code> if record couldn't be created
     */
    protected Record openRecord(String pCommand, Object... pParameter)
    {
        Record record = ProtocolFactory.openRecord(ICategoryConstants.SESSION, pCommand, pParameter);
        
        try
        {
            if (record != null)
            {
                record.addIdentifier(getId());
            }
        }
        catch (RuntimeException re)
        {
            //to be sure
            CommonUtil.close(record);
            
            throw re;
        }
        
        return record;
    }
    
    /**
     * Gets the current {@link SessionCallHandler}. It will be created on first access. 
     * There's only one call handler per session.
     * 
     * @return the call handler
     */
    SessionCallHandler getCallHandler()
    {
        if (callHandler == null)
        {
            callHandler = new SessionCallHandler(this);
        }
        
        return callHandler;
    }
    
    /**
     * Initializes the max inactive interval.
     * 
     * @param pProperties the session properties
     * @param pZone the zone to use
     * @param pScope the scope (master/subsession)
     * @param pDefault the default timeout if not configured
     * @throws Exception if reading configuration failed
     */
    protected void initMaxInactiveInterval(ChangedHashtable<String, Object> pProperties, Zone pZone, String pScope, int pDefault) throws Exception
    {
        //set the default timeout from the configuration, if not defined as property
        if (pProperties.get(IConnectionConstants.SESSIONTIMEOUT) == null
            && pProperties.get(IConnectionConstants.SESSIONTIMEOUT_IN_SECONDS) == null)
        {
            String sTimeoutSec = pZone.getProperty("/application/timeoutSeconds/" + pScope);
            String sTimeout = null;
            
            if (sTimeoutSec == null)
            {
                sTimeout = pZone.getProperty("/application/timeout/" + pScope);
            }
            
            if (sTimeoutSec != null || sTimeout != null)
            {
                int iSec = -1;
                int iMin = -1;
                
                if (sTimeoutSec != null)
                {
                    try
                    {
                        iSec = Integer.parseInt(sTimeoutSec);
                    }
                    catch (NumberFormatException nfe)
                    {
                        log.info("Invalid timeout (seconds)!", nfe);
                    }
                }
                
                if (sTimeout != null)
                {
                    try
                    {
                        iMin = Integer.parseInt(sTimeout) * 60;
                    }
                    catch (NumberFormatException nfe)
                    {
                        log.info("Invalid timeout (minutes)!", nfe);
                    }
                }
    
                if (iSec == -1 && iMin == -1)
                {
                    //default
                    setMaxInactiveInterval(pDefault);
                }
                else if (iSec >= 0 && iMin >= 0)
                {
                    setMaxInactiveInterval(Math.min(iSec, iMin));
                }
                else
                {
                    setMaxInactiveInterval(Math.max(iSec, iMin));
                }
            }
            else
            {
                setMaxInactiveInterval(pDefault);
            }
        }
    }

    /**
     * Initializes the max alive interval.
     * 
     * @param pProperties the session properties
     * @param pZone the zone to use
     * @throws Exception if reading configuration failed
     */
    protected void initMaxAliveInterval(ChangedHashtable<String, Object> pProperties, Zone pZone) throws Exception
    {
        //default
        iMaxAliveFactor = -1;
        iMaxAliveTimeout = -1;

        //set the default timeout from the configuration, if not defined as property
        String sTimeout = null;
        String sTimeoutSec = pZone.getProperty("/application/alive/timeoutSeconds");
        String sTimeoutFactor = pZone.getProperty("/application/alive/timeoutFactor");
            
        if (sTimeoutSec == null)
        {
            sTimeout = pZone.getProperty("/application/alive/timeout");
        }
        
        if (sTimeoutFactor != null)
        {
            try
            {
                iMaxAliveFactor = Integer.parseInt(sTimeoutFactor);
                
                pProperties.put(IConnectionConstants.ALIVEFACTOR, Integer.valueOf(iMaxAliveFactor));
            }
            catch (NumberFormatException nfe)
            {
                log.info("Invalid alive factor!", nfe);
            }
        }
        else
        {
        	//default value
        	pProperties.put(IConnectionConstants.ALIVEFACTOR, Integer.valueOf(4));
        }

        if (sTimeoutSec != null || sTimeout != null)
        {
            int iSec = -1;
            int iMin = -1;
            
            if (sTimeoutSec != null)
            {
                try
                {
                    iSec = Integer.parseInt(sTimeoutSec);
                }
                catch (NumberFormatException nfe)
                {
                    log.info("Invalid alive timeout (seconds)!", nfe);
                }
            }
            
            if (sTimeout != null)
            {
                try
                {
                    iMin = Integer.parseInt(sTimeout) * 60;
                }
                catch (NumberFormatException nfe)
                {
                    log.info("Invalid alive timeout (minutes)!", nfe);
                }
            }

            if (iSec >= 0 && iMin >= 0)
            {
                iMaxAliveTimeout = Math.min(iSec, iMin);
            }
            else if (iSec >= 0 || iMin >= 0)
            {
                iMaxAliveTimeout = Math.max(iSec, iMin);
            }
        }
        
        if (iMaxAliveTimeout > 0 && iMaxAliveFactor > 0)
        {
            log.info("Alive check timeout will use timeout AND factor!");
        }
    }
    
    /**
     * Sets the session locked. This means that the session is in use. If you lock an already locked
     * session, a lock counter will be used. A locked session must be {@link #unlock()}ed.
     * This method doesn't change the access time. 
     * 
     * @see #unlock()
     * @see #lock(boolean)
     */
    public void lock()
    {
    	lock(false);
    }
    
    /**
     * Sets the session locked. This means that the session is in use. If you lock an already locked
     * session, a lock counter will be used. A locked session must be {@link #unlock()}ed. 
     * 
     * @param pForceAccess <code>true</code> to force changing the access time
     * @see #unlock()
     */
    public void lock(boolean pForceAccess)
    {
        synchronized (oLock)
        {
            if (iLockCount == 0)
            {
            	bLockAccess = false;
            }

            iLockCount++;
            
            if (pForceAccess)
            {
            	setLastAccessTime(System.currentTimeMillis());
            }
        }
    }

    /**
     * Unlocks the session but don't force changing access time. The access
     * time will be changed only if something important happened.
     * 
     * @see #lock()
     */
    public void unlock()
    {
    	unlock(false);
    }
    
    /**
     * Unlocks the session.
     * 
     * @param pForceAccess <code>true</code> to force changing the access time
     * @see #lock()
     */
    public void unlock(boolean pForceAccess)
    {
        synchronized (oLock)
        {
            if (iLockCount > 0)
            {
                iLockCount--;
            }
            
            if (bLockAccess || pForceAccess)
            {
            	setLastAccessTime(System.currentTimeMillis());
            }
            
            if (iLockCount == 0)
            {
            	//reset
            	bLockAccess = false;
            }
        }
    }
    
    /**
     * Gets whether the session is locked.
     * 
     * @return <code>true</code> if this session is locked, <code>false</code> otherwise
     * @see #lock()
     */
    public boolean isLocked()
    {
        synchronized (oLock)
        {
            return iLockCount > 0;
        }
    }
    
    /**
     * Gets the {@link Class} for the given class name. The class loader of our internal {@link AbstractObjectProvider} will be
     * used if available, otherwise the default class loader.
     * 
     * @param pFqName the full qualified class name
     * @return the class
     * @throws ClassNotFoundException if class with given name wasn't found
     */
    protected Class<?> getClass(String pFqName) throws ClassNotFoundException
    {
    	ClassLoader loader = objProvider.getClassLoader(this);
    	
    	if (loader == null)
    	{
    		return Class.forName(pFqName);
    	}
    	else
    	{
    		return Class.forName(pFqName, false, loader);
    	}
    }
    
	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * The <code>CallBackWorker</code> executes commands asynchronous and prepares the
	 * result for sending it back to the client.
	 * 
	 * @author René Jahn
	 */
	final class CallBackWorker implements Runnable
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/** the callback information. */
		private Call call = null;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of <code>CallBackWorker</code>.
		 * 
		 * @param pCall the call information
		 */
		private CallBackWorker(Call pCall)
		{
			this.call = pCall;
		}

		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	// Interface implementation
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * {@inheritDoc}
		 */
		public void run()
		{
		    ServerContext ctxt = ((Server)getSessionManager().getServer()).createServerContext();
		    
		    try
		    {
        		if (ctxt instanceof AbstractServerContext)
        		{
        			((AbstractServerContext)ctxt).setSession(AbstractSession.this);
        		}

                SessionCallHandler handler = AbstractSession.this.getCallHandler(); 

                Record record = openRecord(ICommandConstants.SESSION_CALLBACK);
    		    
                boolean bCallError = false;
                
    		    try
    		    {
        		    if (record != null)
        		    {
        		        //needed, because we have no server-call in async case
        		        record.addIdentifier("(thread)", 0);
        		        
        		        record.setParameter(getLifeCycleName(), call.getObjectName(), call.getMethodName(), call.getParameters());
        		    }
        		    
    	            handler.fireBeforeFirstCall();
        		    
        		    try
        			{
        				//ATTENTION !!!
        				//DON'T CHANGE original call instance because we need the callback information!
        				//the new Call object has no callback information (first parameter) because this would produce an endless loop!
        				Object oReturn = executeIntern(new Call((Object)null, call.getObjectName(), call.getMethodName(), call.getParameters()));
        
                        handler.fireAfterCall(call, oReturn, null);

                        call.success(AbstractSession.this, oReturn);
        			}
        			catch (Throwable th)
        			{
                        bCallError = true;

                        if (record != null)
        			    {
        			        record.setException(th);
        			    }
        			    
    			        handler.fireAfterCall(call, null, th);

                        call.error(AbstractSession.this, addCallInfo(AbstractSecurityManager.prepareException(th), call.getObjectName(), call.getMethodName()));
        			}
    		    }
    		    finally
    		    {
                    CommonUtil.close(record);
                    
                    handler.fireAfterLastCall(bCallError);
    		    }
		    }
		    finally
	        {
		    	if (ctxt != null)
		    	{
			        ctxt.release();
		    	}
		    }
		}
		
	}	// CallBackWorker
	
}	// AbstractSession
