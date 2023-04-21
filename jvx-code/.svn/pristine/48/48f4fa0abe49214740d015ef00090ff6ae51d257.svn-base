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
 * 01.02.2009 - [JR] - createConnectionProperties: set compression property
 * 04.02.2009 - [JR] - reopen implemented
 *                   - bIgnoreCallError used
 *                   - getProperties implemented
 * 08.04.2009 - [JR] - getProperties: don't mask the password  
 * 23.05.2009 - [JR] - set/getLifeCycleName implemented     
 * 07.07.2009 - [JR] - executeWithSessionContext implemented
 *                   - get(String) implemented    
 * 23.02.2010 - [JR] - #18: setTimeout: property is now numeric and not String
 * 16.09.2011 - [JR] - #23: call/open/close events
 * 07.03.2012 - [JR] - #556
 *                     * set decimal format symbols as connection properties
 *                     * set locale information as connection properties
 *                     * set environment properties as connection properties
 * 27.02.2013 - [JR] - getTimeout implemented 
 * 28.02.2013 - [JR] - #643: 
 *                     * internal callback listener implemented for callback methods
 *                     * changed return type of callback methods to void    
 * 15.10.2013 - [JR] - used SESSIONTIMEOUT constant
 * 04.04.2014 - [RZ] - #997: IConnectionListener can now listen for property changes from IConnection
 * 03.06.2014 - [JR] - #1054: CallBackForward used
 * 13.01.2015 - [JR] - property changed listener for single properties
 * 05.02.2015 - [JR] - #1254: set defaultCharset as connection property
 * 02.06.2016 - [JR] - #644: ICallBackListener with objects and slots
 * 20.01.2020 - [JR] - #2171: getTimeout without division by 60
 */
package javax.rad.remote;

import java.nio.charset.Charset;
import java.text.DecimalFormatSymbols;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.rad.IPackageSetup;
import javax.rad.remote.event.CallBackEvent;
import javax.rad.remote.event.CallBackForward;
import javax.rad.remote.event.CallBackResultEvent;
import javax.rad.remote.event.CallBackResultForward;
import javax.rad.remote.event.CallErrorEvent;
import javax.rad.remote.event.CallEvent;
import javax.rad.remote.event.ConnectionEvent;
import javax.rad.remote.event.ICallBackListener;
import javax.rad.remote.event.ICallBackResultListener;
import javax.rad.remote.event.IConnectionListener;
import javax.rad.remote.event.IConnectionPropertyChangedListener;
import javax.rad.remote.event.PropertyEvent;
import javax.rad.util.EventHandler;
import javax.rad.util.UIInvoker;

import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.ChangedHashtable;
import com.sibvisions.util.ICloseable;
import com.sibvisions.util.KeyValueList;
import com.sibvisions.util.log.ILogger;
import com.sibvisions.util.log.ILogger.LogLevel;
import com.sibvisions.util.log.LoggerFactory;
import com.sibvisions.util.type.StringUtil;

/**
 * The <code>AbstractConnection</code> encapsulates the access to an
 * <code>IConnection</code> implementation.<br>It implements the connection
 * listener handling and an implementation of {@link ConnectionInfo}.
 * 
 * @author René Jahn
 * @see IConnection
 */
public abstract class AbstractConnection implements ICloseable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the relevant system properties. */
	private static final String[] USED_SYSPROPS = {"user.name", "os.name", "os.version", "os.arch",
        										   "java.vendor", "java.version", "java.class.version",
        										   "java.vm.name", "file.encoding", "file.separator", 
        										   "path.separator", "line.separator", "user.language",
        										   "user.timezone"};
	
	/** the internal callback listener. */
	private static final ICallBackListener CBL_INTERN = new ICallBackListener()
	{
		public void callBack(CallBackEvent pEvent)
		{
			try
			{
				log.debug(pEvent.getObject());
			}
			catch (Throwable th)
			{
				log.debug(th);
			}
		}
	};
	
    /** the internal callback result listener. */
    private static final ICallBackResultListener CBRL_INTERN = new ICallBackResultListener()
    {
        public void callBackResult(CallBackResultEvent pEvent)
        {
            log.debug(pEvent.getInstruction(), pEvent.getObject());
        }
    };

	/** the logger. */
	private static ILogger log = LoggerFactory.getInstance(AbstractConnection.class); 
	
    /** The callback provider. */
    private static EventHandler<ICallBackListener> callBackProvider = new EventHandler<ICallBackListener>(ICallBackListener.class);

    
	/** the connection implementation. */
	protected IConnection connection;
	
	/** the connection information.*/
	protected ConnectionInfo coninf;
	
	/** the callback result mapping. */
	private HashMap<ICallBackResultListener, ICallBackResultListener> hmpCallBackResult = null;
	
	/** all registered connection listeners. */
	private ArrayUtil<IConnectionListener> auConListener = null;
	
    /** the connection property changed listener delegate. */
    private PropertyChangedDelegate pcdHandler;
    
    /** the callback result listener delegate. */
    private CallBackResultDelegate cbrHandler;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>AppliationConnection</code> with an
	 * <code>IConnection</code> implementation.
	 * 
	 * @param pConnection the <code>IConnection</code> implementation
	 */
	protected AbstractConnection(IConnection pConnection)
	{
		this.connection = pConnection;
		
		coninf = new ConnectionInfo(createConnectionProperties());
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Opens the connection without event handling.
	 * 
	 * @throws Throwable if the connection can not be or is already opened
	 */
	protected abstract void openConnection() throws Throwable;
	
	/**
	 * Gets the UI invoker.
	 * 
	 * @return the UI invoker
	 * @see UIInvoker
	 */
	protected abstract UIInvoker getUIInvoker();

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void finalize() throws Throwable
	{
		//avoid fireCallError if the connection is not open
		auConListener = null;

		try
		{
		    close();
		}
		catch (Throwable th)
		{
		    //ignore
		}
		
		super.finalize();
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Sets the name of the server-side life-cycle object.
	 * 
	 * @param pName the name of the life-cycle object
	 */
	public void setLifeCycleName(String pName)
	{
		coninf.getProperties().put(IConnectionConstants.LIFECYCLENAME, pName);
	}
	
	/**
	 * Gets the name of the server-side life-cycle object.
	 * 
	 * @return the name of the life-cycle object
	 */
	public String getLifeCycleName()
	{
		return (String)coninf.getProperties().get(IConnectionConstants.LIFECYCLENAME);
	}

	/**
	 * Sets the application name which will be used from the remote server
	 * to authenticate the connection, when the connection is not open.
	 * 
	 * @param pApplicationName the application name
	 */
	public void setApplicationName(String pApplicationName)
	{
		if (!isOpen())
		{
			coninf.getProperties().put(IConnectionConstants.APPLICATION, pApplicationName);
		}
	}
	
	/**
	 * Gets the application name which will be used from the remote server
	 * to authenticate the connection.
	 * 
	 * @return the application name
	 */
	public String getApplicationName()
	{
		return (String)coninf.getProperties().get(IConnectionConstants.APPLICATION);
	}
	
	
	/**
	 * Sets the user name which will be used from the remote server
	 * to authenticate the connection, when the connection is not open.
	 * 
	 * @param pUserName the user name
	 */
	public void setUserName(String pUserName)
	{
		coninf.getProperties().put(IConnectionConstants.USERNAME, pUserName);
	}

	/**
	 * Gets the user name which will be used from the remote server
	 * to authenticate the connection.
	 * 
	 * @return the user name
	 */
	public String getUserName()
	{
		return (String)coninf.getProperties().get(IConnectionConstants.USERNAME);
	}
	
	/**
	 * Sets the password which will be used from the remote server
	 * to authenticate the connection, when the connection is not open.
	 * 
	 * @param pPassword the password
	 */
	public void setPassword(String pPassword)
	{
		if (!isOpen())
		{
			coninf.getProperties().put(IConnectionConstants.PASSWORD, pPassword);
		}
	}
	
	/**
	 * Gets the password which will be used from the remote server
	 * to authenticate the connection.
	 * 
	 * @return the password
	 */
	public String getPassword()
	{
		return (String)coninf.getProperties().get(IConnectionConstants.PASSWORD);
	}

    /**
     * Opens the connection.
     * 
     * @throws Throwable if the connection can not be or is already opened
     */
    public void open() throws Throwable
    {
        long lStart = System.currentTimeMillis();
        
        String sErrTitle = null;
        
        try
        {
            openConnection();
            
            if (log.isEnabled(LogLevel.DEBUG))
            {
                sErrTitle = "open" + StringUtil.getShortenedWords(getClass().getSimpleName(), 2);
                
                logCommunication(lStart, sErrTitle, null);
            }
            
            fireOpen();
        }
        catch (Throwable th)
        {
            if (log.isEnabled(LogLevel.DEBUG))
            {
                logCommunication(lStart, sErrTitle, th);
            }
            
            throw handleCallError(th);
        }
    }
	
	/**
	 * Checks if the connection is open.
	 * 
	 * @return <code>true</code> if the connection is open, otherwise <code>false</code>
	 */
	public boolean isOpen()
	{
		return connection.isOpen(coninf);
	}
	
	/**
	 * Close the connection.
	 * 
	 * @throws Throwable if it is not possible to close the connection
	 */
	public void close() throws Throwable
	{
	    close(true);
	}	
	
    /**
     * Close the connection, with or without remote call.
     * 
     * @param pDoCall <code>true</code> to send close to the server, <code>false</code> to close internal without
     *                remote communication (client-side close)
     * @throws Throwable if it is not possible to close the connection
     */
	protected void close(boolean pDoCall) throws Throwable
	{
        long lStart = System.currentTimeMillis();
        
        if (pDoCall)
        {
            try
            {
                connection.close(coninf);
            }
            catch (Throwable th)
            {
                logCommunication(lStart, "close", th);
                
                //doesn't matter -> close anyway
            }                
        } 
        else
        {
            coninf.setConnectionId(null);
        }

        logCommunication(lStart, "close", null);
        
        fireClose();
	}

	/**
	 * Reopens the connection.
	 * 
	 * @param pProperties additional properties before reopening the connection
	 * @throws Throwable if an error occurs while opening the connection
	 */
    public void reopen(Map<String, Object> pProperties) throws Throwable
    {
    	long lStart = System.currentTimeMillis();
    	
        try
        {
            connection.close(coninf);
        }
        catch (Throwable th)
        {
            //doesn't matter
        }
        
        setProperties(pProperties);
        
    	try
    	{
            openConnection();
    		
            logCommunication(lStart, "reopen", null);

            fireReOpen();
    	}
    	catch (Throwable th)
    	{
            logCommunication(lStart, "reopen", th);
    	    
            throw handleCallError(th);
    	}
    }
    
	/**
	 * Reopens the connection.
	 * 
	 * @throws Throwable if an error occurs while opening the connection
	 */
    public void reopen() throws Throwable
    {
    	reopen(null);
    }

	/**
	 * Returns the connection to the server.
	 * 
	 * @return the connection
	 */
	public IConnection getConnection()
	{
		return connection;
	}
	
	/**
	 * Gets the connection identifier for this connection.
	 * 
	 * @return the connection identifier
	 */
	public Object getConnectionId()
	{
		return coninf.getConnectionId();
	}

    /**
     * Calls desired methods of server objects, through the connection.
     *
     * @param pCallBackObject the callback listener objects
     * @param pCallBackMethod the callback listener methods 
     * @param pObjectName list of already mapped server object names/aliases
     * @param pMethod method names which should be called
     * @param pParams parameters for the method calls
     * @throws Throwable communication error, security checks, invalid method, ...
     * @see IConnection#call(ConnectionInfo, String[], String[], Object[][], ICallBackListener[])
     */
    public void callBack(Object[] pCallBackObject, String[] pCallBackMethod, String[] pObjectName, String[] pMethod, Object[][] pParams) throws Throwable
    {
        call(createCallBackListener(pCallBackObject, pCallBackMethod), pObjectName, pMethod, pParams);
    }	
	
	/**
	 * Calls desired methods of server objects, through the connection.
	 *
	 * @param pCallBack callback listeners for asynchronous or null for synchronous calls 
	 * @param pObjectName list of already mapped server object names/aliases
	 * @param pMethod method names which should be called
	 * @param pParams parameters for the method calls
	 * @throws Throwable communication error, security checks, invalid method, ...
	 * @see IConnection#call(ConnectionInfo, String[], String[], Object[][], ICallBackListener[])
	 */
	public void call(ICallBackListener[] pCallBack, String[] pObjectName, String[] pMethod, Object[][] pParams) throws Throwable
	{
	    long lStart = System.currentTimeMillis();
	    
		try
		{
			connection.call(coninf, pObjectName, pMethod, pParams, createCallBackListener(pMethod, pCallBack));
			
	        logCommunication(lStart, "call", null, pCallBack, pObjectName, pMethod);
			
			if (auConListener != null && pMethod != null)
			{
				for (int i = 0; i < pMethod.length; i++)
				{
					fireCall(true,
							 pObjectName != null && pObjectName.length > i ? pObjectName[i] : null,
							 pMethod[i],
							 pParams != null && pParams.length > i ? pParams[i] : null);
				}
			}
		}
		catch (Throwable th)
		{
		    logCommunication(lStart, "call", th, pCallBack, pObjectName, pMethod);
		    
            throw handleCallError(th);
		}
	}	

    /**
     * The method will call a remote method from a remote object. 
     * 
     * @param pCallBackObject the callback listener object
     * @param pCallBackMethod the callback listener method 
     * @param pObjectName an already mapped server object name/alias
     * @param pMethod method name which should be called
     * @param pParams parameters for the method call
     * @throws Throwable communication error, security checks, invalid method, ...
     */
    public void callBack(Object pCallBackObject, String pCallBackMethod, String pObjectName, String pMethod, Object... pParams) throws Throwable
    {
        call(callBackProvider.createListener(pCallBackObject, pCallBackMethod), pObjectName, pMethod, pParams);
    }
    
	/**
	 * The method will call a remote method from a remote object. 
	 * 
	 * @param pCallBack callback listener for asynchronous or null for synchronous call 
	 * @param pObjectName an already mapped server object name/alias
	 * @param pMethod method name which should be called
	 * @param pParams parameters for the method call
	 * @throws Throwable communication error, security checks, invalid method, ...
	 */
	public void call(ICallBackListener pCallBack, String pObjectName, String pMethod, Object... pParams) throws Throwable
	{
	    long lStart = System.currentTimeMillis();
	    
		try
		{
			connection.call(coninf, new String[] {pObjectName}, new String[] {pMethod}, 
					        new Object[][] {pParams}, new ICallBackListener[] {createCallBackListener(pCallBack)});
			
		    logCommunication(lStart, "call", null, pCallBack, pObjectName, pMethod);
			
			fireCall(true, pObjectName, pMethod, pParams);
		}
		catch (Throwable th)
		{
		    logCommunication(lStart, "call", th, pCallBack, pObjectName);
		    
            throw handleCallError(th);
		}
	}

    /**
     * The method will call a remote method from a remote object. 
     * 
     * @param pCallBackObject the callback listener object
     * @param pCallBackMethod the callback listener method 
     * @param pObjectName an already mapped server object name/alias
     * @param pMethod method name which should be called
     * @throws Throwable communication error, security checks, invalid method, ...
     */
    public void callBack(Object pCallBackObject, String pCallBackMethod, String pObjectName, String pMethod) throws Throwable
    {
        call(callBackProvider.createListener(pCallBackObject, pCallBackMethod), pObjectName, pMethod);
    }	
	
	/**
	 * The method will call a remote method from a remote object. 
	 * 
	 * @param pCallBack callback listener for asynchronous or null for synchronous call 
	 * @param pObjectName an already mapped server object name/alias
	 * @param pMethod method name which should be called
	 * @throws Throwable communication error, security checks, invalid method, ...
	 */
	public void call(ICallBackListener pCallBack, String pObjectName, String pMethod) throws Throwable
	{
	    long lStart = System.currentTimeMillis();
	           
		try
		{
			connection.call(coninf, new String[] {pObjectName}, new String[] {pMethod}, null, new ICallBackListener[] {createCallBackListener(pCallBack)});
			
			logCommunication(lStart, "call", null, pCallBack, pObjectName, pMethod);
			
			fireCall(true, pObjectName, pMethod);
		}
		catch (Throwable th)
		{
		    logCommunication(lStart, "call", th, pCallBack, pObjectName, pMethod);
		    
            throw handleCallError(th);
		}
	}

	/**
	 * Calls desired methods of server objects, through the connection. This call will be
	 * synchronous.
	 *
	 * @param pObjectName list of already mapped server object names/aliases
	 * @param pMethod method names which should be called
	 * @param pParams parameters for the method calls
	 * @return result list from the remote method calls
	 * @throws Throwable communication error, security checks, invalid method, ...
	 * @see IConnection#call(ConnectionInfo, String[], String[], Object[][], ICallBackListener[])
	 */
	public Object[] call(String[] pObjectName, String[] pMethod, Object[][] pParams) throws Throwable
	{
	    long lStart = System.currentTimeMillis();
	    
		try
		{
			Object[] obj = connection.call(coninf, pObjectName, pMethod, pParams, null);
		
			logCommunication(lStart, "call", null, null, pObjectName, pMethod);
			
			if (auConListener != null && obj != null)
			{
				for (int i = 0; i < obj.length; i++)
				{
					fireCall(false,
							 pObjectName != null && pObjectName.length > i ? pObjectName[i] : null,
							 pMethod[i],
							 pParams != null && pParams.length > i ? pParams[i] : null);
				}
			}
			
			return obj;
		}
		catch (Throwable th)
		{
		    logCommunication(lStart, "call", th, null, pObjectName, pMethod);
		    
            throw handleCallError(th);
		}
	}	

	/**
	 * Calls desired methods of server objects, through the connection. This call will be
	 * synchronous.
	 *
	 * @param pObjectName list of already mapped server object names/aliases
	 * @param pMethod method names which should be called
	 * @return result list from the remote method calls
	 * @throws Throwable communication error, security checks, invalid method, ...
	 * @see IConnection#call(ConnectionInfo, String[], String[], Object[][], ICallBackListener[])
	 */
	public Object[] call(String[] pObjectName, String[] pMethod) throws Throwable
	{
	    long lStart = System.currentTimeMillis();
	    
	    try
		{
			Object[] obj = connection.call(coninf, pObjectName, pMethod, null, null);
		
			logCommunication(lStart, "call", null, null, pObjectName, pMethod);			
			
			if (auConListener != null && obj != null)
			{
				for (int i = 0; i < obj.length; i++)
				{
					fireCall(false,
							 pObjectName != null && pObjectName.length > i ? pObjectName[i] : null,
							 pMethod[i]);
				}
			}
			
			return obj;
		}
		catch (Throwable th)
		{
		    logCommunication(lStart, "call", th, null, pObjectName, pMethod);		    
		    
            throw handleCallError(th);
		}
	}	

	/**
	 * The method will call a remote method from a remote object. The call will be 
	 * synchronous. 
	 * 
	 * @param pObjectName an already mapped server object name/alias
	 * @param pMethod method name which should be called
	 * @param pParams parameters for the method call
	 * @return result from the remote method call
	 * @throws Throwable communication error, security checks, invalid method, ...
	 * @see IConnection#call(ConnectionInfo, String[], String[], Object[][], ICallBackListener[])
	 */
	public Object call(String pObjectName, String pMethod, Object... pParams) throws Throwable
	{
	    long lStart = System.currentTimeMillis();
	    
		try
		{
			Object obj = connection.call(coninf, new String[] {pObjectName}, new String[] {pMethod}, new Object[][] {pParams}, null)[0];
		
			logCommunication(lStart, "call", null, null, pObjectName, pMethod);			
			
			fireCall(false, pObjectName, pMethod, pParams);
			
			return obj;
		}
		catch (Throwable th)
		{
		    logCommunication(lStart, "call", th, null, pObjectName, pMethod);
		    
            throw handleCallError(th);
		}
	}

	/**
	 * The method will call a remote method from a remote object. The call will be 
	 * synchronous.
	 * 
	 * @param pObjectName an already mapped server object name/alias
	 * @param pMethod method name which should be called
	 * @return result from the remote method call
	 * @throws Throwable communication error, security checks, invalid method, ...
	 * @see IConnection#call(ConnectionInfo, String[], String[], Object[][], ICallBackListener[])
	 */
	public Object call(String pObjectName, String pMethod) throws Throwable
	{
	    long lStart = System.currentTimeMillis();
	    
		try
		{
			Object obj = connection.call(coninf, new String[] {pObjectName}, new String[] {pMethod}, null, null)[0];
		
			logCommunication(lStart, "call", null, null, pObjectName, pMethod);
			
			fireCall(false, pObjectName, pMethod);
			
			return obj;
		}
		catch (Throwable th)
		{
		    logCommunication(lStart, "call", th, null, pObjectName, pMethod);
		    
            throw handleCallError(th);
		}
	}
	
	/**
	 * The method will call a remote action. The call will be 
	 * synchronous.
	 * 
	 * @param pAction action which should be called
	 * @return result from the remote action call
	 * @throws Throwable communication error, security checks, invalid action, ...
	 * @see IConnection#call(ConnectionInfo, String[], String[], Object[][], ICallBackListener[])
	 */
	public Object callAction(String pAction) throws Throwable
	{
	    long lStart = System.currentTimeMillis();
	    
		try
		{
			Object obj = connection.call(coninf, null, new String[] {pAction}, null, null)[0];
			
			logCommunication(lStart, "callAction", null, null, pAction);
			
			fireCallAction(false, pAction);
			
			return obj;
		}
		catch (Throwable th)
		{
		    logCommunication(lStart, "callAction", th, null, pAction);
		    
            throw handleCallError(th);
		}
	}
	
	/**
	 * The method will call a remote action. The call will be 
	 * synchronous.
	 * 
	 * @param pAction action which should be called
	 * @param pParams parameters for the action call
	 * @return result from the remote action call
	 * @throws Throwable communication error, security checks, invalid method, ...
	 * @see IConnection#call(ConnectionInfo, String[], String[], Object[][], ICallBackListener[])
	 */
	public Object callAction(String pAction, Object... pParams) throws Throwable
	{
	    long lStart = System.currentTimeMillis();
	    
		try
		{
			Object obj = connection.call(coninf, null, new String[] {pAction}, new Object[][] {pParams}, null)[0];
		
			logCommunication(lStart, "callAction", null, null, pAction);
			
			fireCallAction(false, pAction, pParams);
			
			return obj;
		}
		catch (Throwable th)
		{
		    logCommunication(lStart, "callAction", th, null, pAction);
		    
            throw handleCallError(th);
		}
	}

	/**
	 * The method will call one or more remote actions. The call will be synchronous. 
	 * 
	 * @param pAction list of actions which should be called
	 * @param pParams parameters for the action calls
	 * @return result list from the remote action calls
	 * @throws Throwable communication error, security checks, invalid method, ...
	 * @see IConnection#call(ConnectionInfo, String[], String[], Object[][], ICallBackListener[])
	 */
	public Object[] callAction(String[] pAction, Object[][] pParams) throws Throwable
	{
	    long lStart = System.currentTimeMillis();
	    
		try
		{
			Object[] obj = connection.call(coninf, null, pAction, pParams, null);
		
			logCommunication(lStart, "callAction", null, null, pAction);
			
			if (obj != null)
			{
				for (int i = 0; i < obj.length; i++)
				{
					fireCallAction(false, pAction[i], pParams != null && pParams.length > i ? pParams[i] : null);
				}
			}
			
			return obj;
		}
		catch (Throwable th)
		{
		    logCommunication(lStart, "callAction", th, null, pAction);
		    
            throw handleCallError(th);
		}
	}

	/**
	 * The method will call one or more remote actions. The call will be synchronous. 
	 * 
	 * @param pAction list of actions which should be called
	 * @return result list from the remote action calls
	 * @throws Throwable communication error, security checks, invalid method, ...
	 * @see IConnection#call(ConnectionInfo, String[], String[], Object[][], ICallBackListener[])
	 */
	public Object[] callAction(String[] pAction) throws Throwable
	{
	    long lStart = System.currentTimeMillis();
	    
		try
		{
			Object[] obj = connection.call(coninf, null, pAction, null, null);
		
			logCommunication(lStart, "callAction", null, null, pAction);
			
			if (obj != null)
			{
				for (int i = 0; i < obj.length; i++)
				{
					fireCallAction(false, pAction[i]);
				}
			}
			
			return obj;
		}
		catch (Throwable th)
		{
		    logCommunication(lStart, "callAction", th, null, pAction);
		    
            throw handleCallError(th);
		}
	}

    /**
     * The method will call a remote action.
     * 
     * @param pCallBackObject the callback listener object
     * @param pCallBackMethod the callback listener method 
     * @param pAction action which should be called
     * @throws Throwable communication error, security checks, invalid method, ...
     * @see IConnection#call(ConnectionInfo, String[], String[], Object[][], ICallBackListener[])
     */
    public void callBackAction(Object pCallBackObject, String pCallBackMethod, String pAction) throws Throwable
    {
        callAction(callBackProvider.createListener(pCallBackObject, pCallBackMethod), pAction);
    }

    /**
	 * The method will call a remote action.
	 * 
	 * @param pCallBack callback listener for asynchronous or null for synchronous call 
	 * @param pAction action which should be called
	 * @throws Throwable communication error, security checks, invalid method, ...
	 * @see IConnection#call(ConnectionInfo, String[], String[], Object[][], ICallBackListener[])
	 */
	public void callAction(ICallBackListener pCallBack, String pAction) throws Throwable
	{
	    long lStart = System.currentTimeMillis();
	    
		try
		{
			connection.call(coninf, null, new String[] {pAction}, null, new ICallBackListener[] {createCallBackListener(pCallBack)});
		
			logCommunication(lStart, "callAction", null, pCallBack, pAction);
			
			fireCallAction(true, pAction);
		}
		catch (Throwable th)
		{
		    logCommunication(lStart, "callAction", th, pCallBack, pAction);
		    
            throw handleCallError(th);
		}
	}

    /**
     * The method will call a remote action. 
     * 
     * @param pCallBackObject the callback listener object
     * @param pCallBackMethod the callback listener method 
     * @param pAction action which should be called
     * @param pParams parameters for the action call
     * @throws Throwable communication error, security checks, invalid method, ...
     * @see IConnection#call(ConnectionInfo, String[], String[], Object[][], ICallBackListener[])
     */
    public void callBackAction(Object pCallBackObject, String pCallBackMethod, String pAction, Object... pParams) throws Throwable
    {
        callAction(callBackProvider.createListener(pCallBackObject, pCallBackMethod), pAction, pParams);
    }	
	
	/**
	 * The method will call a remote action. 
	 * 
	 * @param pCallBack callback listener for asynchronous or null for synchronous call 
	 * @param pAction action which should be called
	 * @param pParams parameters for the action call
	 * @throws Throwable communication error, security checks, invalid method, ...
	 * @see IConnection#call(ConnectionInfo, String[], String[], Object[][], ICallBackListener[])
	 */
	public void callAction(ICallBackListener pCallBack, String pAction, Object... pParams) throws Throwable
	{
	    long lStart = System.currentTimeMillis();
	    
		try
		{
			connection.call(coninf, null, new String[] {pAction}, new Object[][] {pParams}, new ICallBackListener[] {createCallBackListener(pCallBack)});
		
			logCommunication(lStart, "callAction", null, pCallBack, pAction);
			
			fireCallAction(true, pAction, pParams);
		}
		catch (Throwable th)
		{
		    logCommunication(lStart, "callAction", th, pCallBack, pAction);
		    
            throw handleCallError(th);
		}
	}

    /**
     * The method will call one or more remote actions.
     * 
     * @param pCallBackObject the callback listener objects
     * @param pCallBackMethod the callback listener methods
     * @param pAction list of actions which should be called
     * @param pParams parameters for the action calls
     * @throws Throwable communication error, security checks, invalid method, ...
     * @see IConnection#call(ConnectionInfo, String[], String[], Object[][], ICallBackListener[])
     */
    public void callBackAction(Object[] pCallBackObject, String[] pCallBackMethod, String[] pAction, Object[][] pParams) throws Throwable
    {
        callAction(createCallBackListener(pCallBackObject, pCallBackMethod), pAction, pParams);
    }	
	
	/**
	 * The method will call one or more remote actions.
	 * 
	 * @param pCallBack callback listeners for asynchronous or null for synchronous calls 
	 * @param pAction list of actions which should be called
	 * @param pParams parameters for the action calls
	 * @throws Throwable communication error, security checks, invalid method, ...
	 * @see IConnection#call(ConnectionInfo, String[], String[], Object[][], ICallBackListener[])
	 */
	public void callAction(ICallBackListener[] pCallBack, String[] pAction, Object[][] pParams) throws Throwable
	{
	    long lStart = System.currentTimeMillis();
	    
		try
		{
			connection.call(coninf, null, pAction, pParams, createCallBackListener(pAction, pCallBack));
		
			logCommunication(lStart, "callAction", null, pCallBack, pAction);
			
			if (auConListener != null && pAction != null)
			{
				for (int i = 0; i < pAction.length; i++)
				{
					fireCallAction(true,
							       pAction[i],
							       pParams != null && pParams.length > i ? pParams[i] : null);
				}
			}
		}
		catch (Throwable th)
		{
		    logCommunication(lStart, "callAction", th, pCallBack, pAction);
		    
            throw handleCallError(th);
		}
	}
	
	/**
	 * Sets a connection property. It's not allowed to set/change client properties if connection
	 * is already established.
	 * 
	 * @param pName the property name
	 * @param pValue the value of the property or <code>null</code> to delete the property
	 * @throws Throwable communication error, security checks, invalid method, ...
	 */
	public void setProperty(String pName, Object pValue) throws Throwable
	{
	    long lStart = System.currentTimeMillis();
	    
		try
		{
			connection.setProperty(coninf, pName, pValue);
			
			logCommunication(lStart, "setProperty", null, pName);
		}
		catch (Throwable th)
		{
		    logCommunication(lStart, "setProperty", th, pName);
		    
            throw handleCallError(th);
		}
	}
	
	/**
	 * Sets multiple connection properties.
	 * 
	 * @param pProperties the name/value pairs
	 * @throws Throwable communication error, security checks, invalid method, ...
	 */
	public void setProperties(Map<String, Object> pProperties) throws Throwable
	{
		if (pProperties != null)
		{
			for (Entry<String, Object> entry : pProperties.entrySet())
			{
				setProperty(entry.getKey(), entry.getValue());
			}	
		}
	}
	
	/**
	 * Gets a connection property.
	 * 
	 * @param pName the property name
	 * @return the value of the property or null if the property is not available
	 * @throws Throwable communication error, security checks, invalid method, ...
	 */	
	public Object getProperty(String pName) throws Throwable
	{
	    long lStart = System.currentTimeMillis();
	    
		try
		{
			Object obj = connection.getProperty(coninf, pName);
			
			logCommunication(lStart, "getProperty", null, pName);
			
			return obj;
		}
		catch (Throwable th)
		{
		    logCommunication(lStart, "getProperty", th, pName);
		    
            throw handleCallError(th);
		}
	}
	
	/**
	 * Gets a copy of the current properties.
	 * 
	 * @return the known properties and its values
	 * @throws Throwable communication error, security checks, invalid method, ...
	 */
	public Hashtable<String, Object> getProperties() throws Throwable
	{
	    long lStart = System.currentTimeMillis();
	    
		try
		{
			Hashtable<String, Object> htResult = connection.getProperties(coninf);
			
			logCommunication(lStart, "getProperties", null);
			
			return htResult;
		}
		catch (Throwable th)
		{
		    logCommunication(lStart, "getProperties", null);
		    
            throw handleCallError(th);
		}
	}
	
	/**
	 * Sets the timeout of the connection.
	 * 
	 * @param pMinutes the timeout in minutes or -1 to disable timeout
	 * @throws Throwable communication error, security checks, invalid method, ...
	 */
	public void setTimeout(int pMinutes) throws Throwable
	{
        setProperty(IConnectionConstants.SESSIONTIMEOUT, Integer.valueOf(pMinutes));
	}

	/**
	 * Gets the timeout of the connection.
	 * 
	 * @return the timeout in minutes (-2 if timeout is unknown or not explicitely set)
	 * @throws Throwable communication error, security checks, invalid method, ...
	 */
	public int getTimeout() throws Throwable
	{
		Integer iValue = (Integer)getProperty(IConnectionConstants.SESSIONTIMEOUT);
		
		if (iValue != null)
		{
		    return iValue.intValue(); 
		}
		else
		{
			return -2;
		}
	}
	
    /**
     * Gets the properties which will be sent to the server when opening a new connection.
     * 
     * @return the properties as list with key/value pairs
     */
    protected ChangedHashtable<String, Object> createConnectionProperties()
    {
    	ChangedHashtable<String, Object> chtProperties = new ChangedHashtable<String, Object>();
		
		//-----------------------------------------------------------
		// System Properties
		//-----------------------------------------------------------

    	try
    	{
    	    Charset charset = Charset.defaultCharset();
    	    
    	    chtProperties.put(IConnectionConstants.PREFIX_CLIENT + "defaultCharset", charset.name());
    	}
    	catch (Exception e)
    	{
    	    //e.g. Security
    	}
    	
		String sValue;
		String sPrefix = IConnectionConstants.PREFIX_CLIENT + IConnectionConstants.PREFIX_SYSPROP;
		
		for (int i = 0, anz = AbstractConnection.USED_SYSPROPS.length; i < anz; i++)
		{
			try
			{
				sValue = System.getProperty(AbstractConnection.USED_SYSPROPS[i]);
				
				if (sValue != null)
				{
					chtProperties.put(sPrefix + USED_SYSPROPS[i], sValue);
				}
			}
			catch (Exception e)
			{
				//e.g. SecurityException in an Applet
			}
		}
		
		sPrefix = IConnectionConstants.PREFIX_CLIENT + IConnectionConstants.PREFIX_ENVPROP;
		
		try
		{
			Map<String, String> mapEnv = System.getenv();
			
			String sKey;
			
			for (Entry<String, String> entry : mapEnv.entrySet())
			{
				sKey = entry.getKey();
				sValue = entry.getValue();
			
				if (sKey != null && sValue != null)
				{
					chtProperties.put(sPrefix + entry.getKey(), sValue);
				}
			}
		}
		catch (Exception e)
		{
			//e.g. SecurityException
		}

		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		
		sPrefix = IConnectionConstants.PREFIX_CLIENT + "decimalFormatSymbols.";
		
		chtProperties.put(sPrefix + "currencySymbol", dfs.getCurrencySymbol());
		chtProperties.put(sPrefix + "currencyCode", dfs.getCurrency().getCurrencyCode());
		chtProperties.put(sPrefix + "internationalCurrencySymbol", dfs.getInternationalCurrencySymbol());
		chtProperties.put(sPrefix + "decimalsSparator", Character.toString(dfs.getDecimalSeparator()));
		chtProperties.put(sPrefix + "digit", Character.toString(dfs.getDigit()));
		chtProperties.put(sPrefix + "groupingSeparator", Character.toString(dfs.getGroupingSeparator()));
		chtProperties.put(sPrefix + "infinity", dfs.getInfinity());
		chtProperties.put(sPrefix + "minusSign", Character.toString(dfs.getMinusSign()));
		chtProperties.put(sPrefix + "monetaryDecimalSeparator", Character.toString(dfs.getMonetaryDecimalSeparator()));
		chtProperties.put(sPrefix + "NaN", dfs.getNaN());
		chtProperties.put(sPrefix + "patternSeparator", Character.toString(dfs.getPatternSeparator()));
		chtProperties.put(sPrefix + "percent", Character.toString(dfs.getPercent()));
		chtProperties.put(sPrefix + "perMill", Character.toString(dfs.getPerMill()));
		chtProperties.put(sPrefix + "zeroDigit", Character.toString(dfs.getZeroDigit()));
		
		//-----------------------------------------------------------
		// Client Information
		//-----------------------------------------------------------

		chtProperties.put(IConnectionConstants.PREFIX_CLIENT + "oslib_version", IPackageSetup.JVX_OS_VERSION);
		chtProperties.put(IConnectionConstants.PREFIX_CLIENT + "spec_version", IPackageSetup.SPEC_VERSION);
		
		chtProperties.put(IConnectionConstants.CREATIONTIME_CLIENT, new Date());
		
		//-----------------------------------------------------------
		// Communication Properties
		//-----------------------------------------------------------
		
		IConnection con = getConnection();
		
		if (con != null)
		{
		    chtProperties.put(IConnectionConstants.CONNECTION_CLASS, con.getClass().getName());
		}
		
		chtProperties.put(IConnectionConstants.COMPRESSION, "true");
		
		return chtProperties;
    }    
    
    /**
     * Adds an {@link IConnectionListener} to the list of registered listeners.
     * 
     * @param pListener the new connection listener
     */
    public void addConnectionListener(IConnectionListener pListener)
    {
        if (pListener != null)
        {
        	if (auConListener == null)
        	{
        		auConListener = new ArrayUtil<IConnectionListener>();
        	}
        	
        	if (auConListener.indexOf(pListener) < 0)
        	{
            	auConListener.add(pListener);
            	
            	connection.addPropertyChangedListener(pListener);
        	}
        }
    }
    
    /**
     * Removes an {@link IConnectionListener} from the list of registered listeners.
     * 
     * @param pListener the new connection listener
     */
    public void removeConnectionListener(IConnectionListener pListener)
    {
    	if (auConListener != null && pListener != null)
    	{
    		connection.removePropertyChangedListener(pListener);
    		
    		auConListener.remove(pListener);
    		
    		if (auConListener.size() == 0)
    		{
    			auConListener = null;
    		}
    	}
    }

    /**
     * Gets all registered {@link IConnectionListener}s.
     * 
     * @return an array with {@link IConnectionListener}s or null if no listener is registered
     */
    public IConnectionListener[] getConnectionListener()
    {
    	if (auConListener != null)
    	{
    		IConnectionListener[] listener = new IConnectionListener[auConListener.size()];
    		
    		return auConListener.toArray(listener);
    	}
    	
    	return null;
    }

    /**
     * Adds an {@link IConnectionPropertyChangedListener} to the list of registered listeners.
     * 
     * @param pListener the listener
     */
    public void addPropertyChangedListener(IConnectionPropertyChangedListener pListener)
    {
        addPropertyChangedListener(null, pListener);
    }
    
    /**
     * Adds an {@link IConnectionPropertyChangedListener} to the list of registered listeners,
     * for the given property.
     * 
     * @param pPropertyName the property name
     * @param pListener the listener
     */
    public void addPropertyChangedListener(String pPropertyName, IConnectionPropertyChangedListener pListener)
    {
        if (pcdHandler == null)
        {
            pcdHandler = new PropertyChangedDelegate();
            
            connection.addPropertyChangedListener(pcdHandler);
        }

        pcdHandler.add(pPropertyName, pListener);
    }
    
    /**
     * Removes all registered listeners for the given property.
     * 
     * @param pPropertyName the property name
     */
    public void removePropertyChangedListener(String pPropertyName)
    {
        removePropertyChangedListener(pPropertyName, null);
    }

    /**
     * Removes the given {@link IConnectionPropertyChangedListener} from the list of registered listeners.
     * 
     * @param pListener the listener or <code>null</code> to remove all registered listeners
     */
    public void removePropertyChangedListener(IConnectionPropertyChangedListener pListener)
    {
        if (pcdHandler != null)
        {
            pcdHandler.remove(pListener);
        }
    }
    
    /**
     * Removes an {@link IConnectionPropertyChangedListener} from the list of registered listeners,
     * for the given property.
     * 
     * @param pPropertyName the property name
     * @param pListener the listener or <code>null</code> to remove all registered listeners
     */
    public void removePropertyChangedListener(String pPropertyName, IConnectionPropertyChangedListener pListener)
    {
        if (pcdHandler != null)
        {
            pcdHandler.remove(pPropertyName, pListener);
        }
    }    

    /**
     * Adds an {@link ICallBackResultListener} to the list of registered listeners.
     * 
     * @param pListener the listener
     */
    public void addCallBackResultListener(ICallBackResultListener pListener)
    {
        addCallBackResultListener(null, pListener);
    }    
    
    /**
     * Adds an {@link ICallBackResultListener} to the list of registered listeners,
     * for the given instruction.
     * 
     * @param pInstruction the instruction identifier
     * @param pListener the listener
     */
    public void addCallBackResultListener(String pInstruction, ICallBackResultListener pListener)
    {
        if (cbrHandler == null)
        {
            cbrHandler = new CallBackResultDelegate();
            
            hmpCallBackResult = new HashMap<ICallBackResultListener, ICallBackResultListener>();
        }

        ICallBackResultListener cbrl = createCallBackResultListener(pListener);
        
        hmpCallBackResult.put(pListener, cbrl);
        
        cbrHandler.add(pInstruction, cbrl);
    }
    
    /**
     * Removes the given {@link ICallBackResultListener} from the list of registered listeners.
     * 
     * @param pListener the listener or <code>null</code> to remove all registered listeners
     */
    public void removeCallBackResultListener(ICallBackResultListener pListener)
    {
        if (cbrHandler != null)
        {
            cbrHandler.remove(hmpCallBackResult.remove(pListener));
        }
    }    
    
    /**
     * Removes all registered listeners for the given instruction.
     * 
     * @param pInstruction the instruction identifier
     */
    public void removeCallBackResultListener(String pInstruction)
    {
        removeCallBackResultListener(pInstruction, null);
    }

    /**
     * Removes an {@link ICallBackResultListener} from the list of registered listeners,
     * for the given instruction.
     * 
     * @param pInstruction the instruction identifier
     * @param pListener the listener or <code>null</code> to remove all registered listeners
     */
    public void removeCallBackResultListener(String pInstruction, ICallBackResultListener pListener)
    {
        if (cbrHandler != null)
        {
            cbrHandler.remove(pInstruction, hmpCallBackResult.remove(pListener));
        }
    }    
    
    /**
     * Fires the <code>callError</code> methods to all registered connection listeners.
     * 
     * @param pError the occured error
     */
    protected void fireCallError(Throwable pError)
    {
    	if (auConListener != null)
    	{
    		CallErrorEvent event = new CallErrorEvent(this, pError);
    		
    		for (IConnectionListener listener : auConListener.clone())
    		{
    			listener.callError(event);
    		}
    	}
    }
    
    /**
     * Fires the <code>callBackResult</code> methods to all registered callback result listeners.
     * 
     * @param pEvent the event
     * @throws Throwable if notification fails
     */
    protected void fireCallBackResult(CallBackResultEvent pEvent) throws Throwable
    {
        if (cbrHandler != null)
        {
            cbrHandler.callBackResult(pEvent);
        }
    }
    
    /**
     * Handles a call error. This methods sets the connection for every {@link CommunicationException} and
     * notifies connection listeners via {@link #fireCallError(Throwable)}.
     * 
     * @param pError the occured error
     * @return the changed <code>pError</code>
     * @see #setConnection(Throwable)
     * @see #fireCallError(Throwable)
     */
    protected Throwable handleCallError(Throwable pError)
    {
        Throwable throwable = setConnection(pError);
        
        fireCallError(throwable);
        
        return throwable;
    }

    /**
     * Sets the connection property for all {@link CommunicationException} in the hierarchy of the given
     * error.
     * 
     * @param pError the occured error
     * @return the changed <code>pError</code>
     */
    protected Throwable setConnection(Throwable pError)
    {
        //update communication exceptions because if listeners should know the connection or
        if (pError != null)
        {
            Throwable thCause = pError;
            
            while (thCause != null)
            {
                if (thCause instanceof CommunicationException)
                {
                    ((CommunicationException)thCause).setConnection(this);
                }
                
                thCause = thCause.getCause();
            }
        }
        
        return pError;
    }
    
    /**
     * Fires the <code>connectionOpened</code> methods to all registered connection listeners.
     */
    protected void fireOpen()
    {    	
    	if (auConListener != null)
    	{
    		ConnectionEvent event = new ConnectionEvent(this);
    		
    		for (IConnectionListener listener : auConListener.clone())
    		{
    			listener.connectionOpened(event);
    		}
    	}
    }
    
    /**
     * Fires the <code>connectionReOpened</code> methods to all registered connection listeners.
     */
    protected void fireReOpen()
    {
    	if (auConListener != null)
    	{
    		ConnectionEvent event = new ConnectionEvent(this);
    		
    		for (IConnectionListener listener : auConListener.clone())
    		{
    			listener.connectionReOpened(event);
    		}
    	}
    }

    /**
     * Fires the <code>connectionClosed</code> methods to all registered connection listeners.
     */
    protected void fireClose()
    {
    	if (auConListener != null)
    	{
    		ConnectionEvent event = new ConnectionEvent(this);
    		
    		for (IConnectionListener listener : auConListener.clone())
    		{
    			listener.connectionClosed(event);
    		}
    	}
    }
    
    /**
     * Fires the <code>objectCalled</code> methods to all registered connection listeners.
     * 
     * @param pCallBack whether the call was a callback call
     * @param pObjectName the name of the remote object
     * @param pMethodName the name of the method
     * @param pParams the method parameters
     */
    protected void fireCall(boolean pCallBack, String pObjectName, String pMethodName, Object... pParams)
    {
    	if (auConListener != null)
    	{
    		CallEvent event = new CallEvent(this, pObjectName, pMethodName, pParams, pCallBack);
    		
    		for (IConnectionListener listener : auConListener.clone())
    		{
    			listener.objectCalled(event);
    		}
    	}
    }

    /**
     * Fires the <code>actionCalled</code> methods to all registered connection listeners.
     * 
     * @param pCallBack whether the action call was a callback call
     * @param pAction the name of the action
     * @param pParams the method parameters
     */
    protected void fireCallAction(boolean pCallBack, String pAction, Object... pParams)
    {
    	if (auConListener != null)
    	{
    		CallEvent event = new CallEvent(this, null, pAction, pParams, pCallBack);
    		
    		for (IConnectionListener listener : auConListener.clone())
    		{
    			listener.actionCalled(event);
    		}
    	}
    }
    
    /**
     * Creates a callback listener, if necessary.
     * 
     * @param pListener the listener
     * @return the wrapped <code>pListener</code> or an internal listener if the given listener is null
     */
    private ICallBackListener createCallBackListener(ICallBackListener pListener)
    {
    	if (pListener == null)
    	{
    		return CBL_INTERN;
    	}
    	
    	return new CallBackForward(getUIInvoker(), this, pListener);
    }
    
    /**
     * Creates a callback-result listener, if necessary.
     * 
     * @param pListener the listener
     * @return the wrapped <code>pListener</code> or an internal listener if the given listener is null
     */
    private ICallBackResultListener createCallBackResultListener(ICallBackResultListener pListener)
    {
        if (pListener == null)
        {
            return CBRL_INTERN;
        }
        
        return new CallBackResultForward(getUIInvoker(), pListener);
    }

    /**
     * Creates callback listeners, if necessary.
     * 
     * @param pMethods the method(s) for listener count 
     * @param pListener the available listener(s)
     * @return the <code>pListener</code> if enough listeners were defined or a list with additional
     *         internal listeners, if <code>pListener</code> contains <code>null</code> elements
     */
    private ICallBackListener[] createCallBackListener(String[] pMethods, ICallBackListener[] pListener)
    {
    	ICallBackListener[] cbl = pListener;
    	
    	if (pListener == null)
    	{
    		if (pMethods != null)
    		{
    			cbl = new ICallBackListener[pMethods.length];
    		}
    	}
    	
    	//update listener array
    	if (cbl != null)
    	{
    		for (int i = 0; i < cbl.length; i++)
    		{
    			if (cbl[i] == null)
    			{
    				cbl[i] = CBL_INTERN;
    			}
    			else
    			{
    			    cbl[i] = new CallBackForward(getUIInvoker(), this, cbl[i]);
    			}
    		}
    	}
    	
    	return cbl;
    }

    /**
     * Creates callback listeners for given objects and methods combination.
     * 
     * @param pListener the listener objects
     * @param pMethod the listener methods
     * @return the callback listeners
     */
    private ICallBackListener[] createCallBackListener(Object[] pListener, String[] pMethod)
    {
        ICallBackListener[] listeners = null;
        
        if (pListener != null)
        {
            listeners = new ICallBackListener[pListener.length];
            
            for (int i = 0; i < pListener.length; i++)
            {
                if (pListener[i] != null)
                {
                    listeners[i] = callBackProvider.createListener(pListener[i], pMethod[i]);
                }
            }
        }

        return listeners;
    }
    
    /**
     * Loggs communication duration.
     * 
     * @param pStart the communication start
     * @param pTitle the communication "title", means the triggering method name like "open"
     * @param pError if an error occured
     * @param pInfo the log information (parameter)
     */
    protected void logCommunication(long pStart, String pTitle, Throwable pError, Object... pInfo)
    {
        if (log.isEnabled(LogLevel.DEBUG))
        {
            if (pError != null)
            {
                log.debug(pTitle, " error: " + Long.valueOf(System.currentTimeMillis() - pStart), " ms", " ", pInfo, pError);
            }
            else
            {
                log.debug(pTitle, ": ", Long.valueOf(System.currentTimeMillis() - pStart), " ms", " ", pInfo);
            }
        }
    }

    //****************************************************************
    // Subclass definition
    //****************************************************************

    /**
     * The <code>ListenerDelegate</code> is the base class for listener delegates.
     * 
     * @author René Jahn
     *
     * @param <K> the listener interface
     */
    public static class ListenerDelegate<K>
    {
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Class members
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /** the listener. */
        private KeyValueList<String, K> kvlListener = new KeyValueList<String, K>();
        
        /** the listener for ALL properties. */
        private List<K> liAllListeners = null;
        
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // User-defined methods
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        
        /**
         * Gets all available listeners for the given name.
         * 
         * @param pName the name
         * @return the listeners
         */
        public List<K> getListeners(String pName)
        {
            List<K> liCopy = new ArrayUtil<K>();
            
            //add to a copy to avoid problems with add/remove during notification
            List<K> listeners = kvlListener.get(pName);
            
            if (listeners != null)
            {
                liCopy.addAll(listeners);
            }
            
            if (liAllListeners != null)
            {
                liCopy.addAll(liAllListeners);
            }
            
            return liCopy;
        }
        
        /**
         * Adds a listener for a specific name.
         * 
         * @param pName the name
         * @param pListener the listener for the name
         */
        public void add(String pName, K pListener)
        {
            if (pName == null)
            {
                if (liAllListeners == null)
                {
                    liAllListeners = new ArrayUtil<K>();
                    liAllListeners.add(pListener);
                }
                else if (!liAllListeners.contains(pListener))
                {
                    liAllListeners.add(pListener);
                }
            }
            else
            {
                if (!kvlListener.contains(pName, pListener))
                {
                    kvlListener.put(pName, pListener);
                }
            }
        }

        /**
         * Removes a specific or all listener for a specific name.
         * 
         * @param pName the name
         * @param pListener the listener or <code>null</code> to remove all listeners
         */
        public void remove(String pName, K pListener)
        {
            if (pName == null)
            {
                if (liAllListeners != null)
                {
                    if (pListener == null)
                    {
                        liAllListeners = null;
                    }
                    else
                    {
                        liAllListeners.remove(pListener);
                    }
                }                    
            }
            else
            {
                if (pListener == null)
                {
                    kvlListener.remove(pName);
                }
                else
                {
                    kvlListener.remove(pName, pListener);
                }
            }
        }
        
        /**
         * Removes a listener from internal listener lists.
         * 
         * @param pListener the listener
         */
        public void remove(K pListener)
        {
            if (liAllListeners != null)
            {
                if (pListener == null)
                {
                    liAllListeners.clear();
                }
                else
                {
                    liAllListeners.remove(pListener);
                }
            }
            
            if (pListener == null)
            {
                kvlListener.clear();
            }
            else
            {
                kvlListener.removeValue(pListener);
            }
        }
        
    }   // ListenerDelegate
    
    /**
     * The <code>PropertyChangedDelegate</code> is an {@link IConnectionPropertyChangedListener} that forwards
     * property changed events to single property listeners. The listeners can be mapped to specific property
     * names and don't handle other property names.
     * 
     * @author René Jahn
     */
    public static class PropertyChangedDelegate extends ListenerDelegate<IConnectionPropertyChangedListener>
                                                implements IConnectionPropertyChangedListener
    {
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Interface implementation
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /**
         * {@inheritDoc}
         */
        public void propertyChanged(PropertyEvent pEvent) throws Throwable
        {
            List<IConnectionPropertyChangedListener> listeners = getListeners(pEvent.getPropertyName());
            
            for (int i = 0; i < listeners.size(); i++)
            {
                listeners.get(i).propertyChanged(pEvent);
            }
        }
        
    }   // PropertyChangedDelegate    
    
    /**
     * The <code>CallBackResultDelegate</code> is an {@link ICallBackResultListener} that forwards
     * callback result events to single listeners. The listeners can be mapped to specific or all 
     * instructions.
     * 
     * @author René Jahn
     */
    public static class CallBackResultDelegate extends ListenerDelegate<ICallBackResultListener>
                                               implements ICallBackResultListener
    {
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Interface implementation
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /**
         * {@inheritDoc}
         */
        public void callBackResult(CallBackResultEvent pEvent) throws Throwable
        {
            List<ICallBackResultListener> listener = getListeners(pEvent.getInstruction());
            
            for (int i = 0; i < listener.size(); i++)
            {
                listener.get(i).callBackResult(pEvent);
            }
        }
        
    }   // CallBackResultDelegate
    
}	// AbstractConnection
