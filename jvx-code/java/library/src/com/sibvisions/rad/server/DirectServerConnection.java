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
 * 11.02.2009 - [JR] - creation
 * 13.02.2009 - [JR] - setProperty: check if the connection is open
 *                   - call: start callback in it's owtn thread
 * 09.04.2009 - [JR] - setAndCheckAlive: always forward to server instead of return null
 *                     (updates the alive time for the server-side session!)      
 * 10.04.2009 - [JR] - setProperty: don't allow to change client properties when connected
 * 13.05.2009 - [JR] - close: checked isOpen() [BUGFIX] 
 * 27.05.2009 - [JR] - used IDirectServer instead of IServer  
 * 11.08.2009 - [JR] - open/openSub: syncProperties [BUGFIX]
 * 04.10.2009 - [JR] - setNewPassword: old password parameter
 * 15.10.2009 - [JR] - setProperty: always set internal property and only call sync when the connection is open [BUGFIX]
 * 16.10.2009 - [JR] - static server instance [BUGFIX]   
 * 14.11.2009 - [JR] - open, openSub, call: connection-id validation [BUGFIX]   
 *                   - set/getProperty: connection info check    
 * 23.02.2010 - [JR] - #18: syncProperties: the server sends only allowed properties - removed class check
 * 06.06.2010 - [JR] - #134: remove missing properties 
 * 28.02.2013 - [JR] - #643: callback validation removed because server checks it      
 * 11.07.2013 - [JR] - #728: isCalling implemented    
 *                   - setLastCallTime calls     
 * 04.04.2014 - [RZ] - #997: implemented addPropertyChangedListener and removePropertyChangedListener
 * 03.06.2014 - [JR] - #1054: used CallBackForward  
 * 27.05.2015 - [JR] - #1397: call of beforeFirstCall and afterLastCall
 * 08.06.2016 - [JR] - #25: syncWithServer implemented
 * 23.06.2016 - [JR] - used push feature of server for callbackresults
 * 12.01.2017 - [JR] - #1744: send NOPARAMETER instead of null 
 */
package com.sibvisions.rad.server;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;

import javax.rad.remote.ConnectionInfo;
import javax.rad.remote.IConnection;
import javax.rad.remote.IConnectionConstants;
import javax.rad.remote.SessionExpiredException;
import javax.rad.remote.event.CallBackForward;
import javax.rad.remote.event.CallBackResultEvent;
import javax.rad.remote.event.ICallBackListener;
import javax.rad.remote.event.ICallBackResultListener;
import javax.rad.remote.event.IConnectionPropertyChangedListener;
import javax.rad.remote.event.PropertyEvent;
import javax.rad.server.ResultObject;
import javax.rad.server.push.IPushReceiver;
import javax.rad.server.push.PushMessage;

import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.ChangedHashtable;
import com.sibvisions.util.log.ILogger;
import com.sibvisions.util.log.ILogger.LogLevel;
import com.sibvisions.util.log.LoggerFactory;

/**
 * The <code>DirectServerConnection</code> is an {@link IConnection} implementation for 
 * a direct server communication. The calls will be sent to the server without
 * serialization.
 * 
 * @author René Jahn
 */
public class DirectServerConnection implements IConnection
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the communication server. */
	private IDirectServer server = null;
	
	/** whether a call is active. */
	private boolean bCalling = false;
	
	/** the list of {@link IConnectionPropertyChangedListener}s. */
	private ArrayUtil<IConnectionPropertyChangedListener> auPropertyChangedListeners = new ArrayUtil<IConnectionPropertyChangedListener>();
	
    /** the list of {@link ICallBackResultListener}s. */
    private ArrayUtil<ICallBackResultListener> auCallBackResultListeners;

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of {@link DirectServerConnection} with a new {@link Server} instance.
	 */
	public DirectServerConnection()
	{
		if (server == null)
		{
			server = Server.getInstance();
		}
	}

	/**
	 * Creates a new instance of {@link DirectServerConnection} for a {@link IDirectServer} implementation.
	 * 
	 * @param pServer the server
	 */
	public DirectServerConnection(IDirectServer pServer)
	{
		server = pServer;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public synchronized void open(ConnectionInfo pConnectionInfo) throws Throwable
	{
		pConnectionInfo.setLastCallTime(System.currentTimeMillis());		

		if (pConnectionInfo.getConnectionId() != null)
		{
			//Sessions can't be created more than once!
			throw new SecurityException("Session is already open!");
		}
		
		Object oId = server.createSession(pConnectionInfo.getProperties());

		pConnectionInfo.setConnectionId(oId);
		
		syncWithServer(pConnectionInfo);
		
		server.registerPushReceiver(oId, new DirectServerPushReceiver(this));
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized void openSub(ConnectionInfo pConnectionInfo, ConnectionInfo pConnectionInfoSub) throws Throwable
	{
		pConnectionInfo.setLastCallTime(System.currentTimeMillis());		

		if (pConnectionInfoSub.getConnectionId() != null)
		{
			//Sessions can't be created more than once!
			throw new SecurityException("Session is already open!");
		}

		Object oId = server.createSubSession(pConnectionInfo.getConnectionId(), pConnectionInfoSub.getProperties());
		
		pConnectionInfoSub.setConnectionId(oId);
		
		syncWithServer(pConnectionInfoSub);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isOpen(ConnectionInfo pConnectionInfo)
	{
		return pConnectionInfo != null && pConnectionInfo.getConnectionId() != null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public synchronized void close(ConnectionInfo pConnectionInfo) throws Throwable
	{
		if (isOpen(pConnectionInfo))
		{
			pConnectionInfo.setLastCallTime(System.currentTimeMillis());		
	
            Object oConId = pConnectionInfo.getConnectionId();

            try
			{
			    try
			    {
			        checkCallBackResults(pConnectionInfo);
			    }
			    catch (Throwable th)
			    {
			        //e.g. reopen
			        if (!(th instanceof SessionExpiredException))
			        {
			            LoggerFactory.getInstance(DirectServerConnection.class).error(th);
			        }
			    }
			 
				server.destroySession(oConId);
			}
			finally
			{
                server.unregisterPushReceiver(oConId);

                pConnectionInfo.setConnectionId(null);
			}
		}
		else
		{
			throw new IllegalStateException("Connection not open");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized Object[] call(ConnectionInfo pConnectionInfo,
						 			  String[] pObjectName, 
						 			  String[] pMethod, 
						 			  Object[][] pParams, 
						 			  ICallBackListener[] pCallBack) throws Throwable
    {
		try
		{
			bCalling = true;

			//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			// Validation 
			// (same checks as in AbstractSerializedConnection.call)
			//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
			if (pConnectionInfo == null)
			{
				throw new IllegalArgumentException("Invalid connection information: null");
			}
	
			if (pConnectionInfo.getConnectionId() == null)
			{
				throw new IllegalStateException("The connection is not open!");
			}
			
			//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			// Validation 
			// (same checks as in AbstractSerializedConnection.callIntern)
			//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
			if (pMethod == null)
			{
				throw new IllegalArgumentException("No remote method specified!");
			}
			
			if (pObjectName != null && pMethod.length != pObjectName.length)
			{
				throw new IllegalArgumentException("More or less objects than methods!");
			}
	
			if (pParams != null && pParams.length != pMethod.length)
			{
				throw new IllegalArgumentException("More or less params than methods!");
			}
			
			Object oConnectionId = pConnectionInfo.getConnectionId();
			
			//without connection-id: only create connection is possible
			if (oConnectionId == null)
			{
				if (pObjectName == null 
					|| !IConnection.OBJ_SESSION.equals(pObjectName[0]) 
					|| (!IConnection.MET_SESSION_CREATE.equals(pMethod[0]) 
						&& !IConnection.MET_SESSION_SUBSESSION_CREATE.equals(pMethod[0])))
				{
					throw new IOException("Connection is not open!");
				}
			}
	
			if (pCallBack != null && pCallBack.length != pMethod.length)
			{
				throw new IllegalArgumentException("More or less callbacks than methods!");
			}
			
			//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			// CALL(s)
			//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			
			Object[] oResult = new Object[pMethod.length];
			
			Object oConId = pConnectionInfo.getConnectionId();
			
			Throwable thError = null;
			
			if (oResult.length > 0)
			{
			    server.beforeFirstCall(oConId);
			}
			
			for (int i = 0, anz = oResult.length; i < anz && thError == null; i++)
			{
				try
				{
					if (pCallBack == null || pCallBack[i] == null)
					{
						oResult[i] = server.execute(oConId, 
								                    pObjectName != null ? pObjectName[i] : null, 
								                    pMethod[i], 
								                    pParams != null ? pParams[i] : NOPARAMETER);
					}
					else
					{
						server.executeCallBack(oConId,
											   new CallBackForward(this, pCallBack[i]),
								               pObjectName != null ? pObjectName[i] : null, 
								               pMethod[i], 
								               pParams != null ? pParams[i] : NOPARAMETER);
						
						oResult[i] = null;
					}
				}
				catch (Throwable th)
				{
					oResult[i] = null;
					thError = th;
				}
				finally
				{
					pConnectionInfo.setLastCallTime(System.currentTimeMillis());		
				}
			}
			
			if (oResult.length > 0)
			{
			    server.afterLastCall(oConId, thError != null);
			}

            //update the properties independent from call errors
            try
            {
                syncWithServer(pConnectionInfo);
            }
            catch (Throwable th)
            {
                //don't override previous errors!
                if (thError == null)
                {
                    thError = th;
                }
            }       
            finally
            {
                pConnectionInfo.setLastCallTime(System.currentTimeMillis());        
            }
            
            if (thError != null)
            {
                throw thError;
            }
			
			return oResult;
		}
		finally
		{
			bCalling = false;
		}
    }
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isCalling()
	{
		return bCalling;
	}	
	
	/**
	 * {@inheritDoc}
	 */
	public synchronized ConnectionInfo[] setAndCheckAlive(ConnectionInfo pConnectionInfo, 
			                                              ConnectionInfo[] pSubConnections) throws Throwable
	{
		pConnectionInfo.setLastCallTime(System.currentTimeMillis());		

		Hashtable<Object, ConnectionInfo> htMapping;
		
		Object[] oConIds;

		if (pSubConnections != null)
		{
			htMapping = new Hashtable<Object, ConnectionInfo>();
			
			oConIds = new Object[pSubConnections.length];
			
			for (int i = 0, anz = pSubConnections.length; i < anz; i++)
			{
				oConIds[i] = pSubConnections[i].getConnectionId();
				
				htMapping.put(oConIds[i], pSubConnections[i]);
			}
		}
		else
		{
			htMapping = null;
			oConIds = null;
		}
		
		Object[] oValid = server.setAndCheckAlive(pConnectionInfo.getConnectionId(), oConIds);
		
		syncWithServer(pConnectionInfo);
		
		if (oValid == null)
		{
			return null;
		}
		else
		{
			ConnectionInfo[] ciResult = new ConnectionInfo[oValid.length];
			
			for (int i = 0, anz = oValid.length; i < anz; i++)
			{
				ciResult[i] = htMapping.get(oValid[i]);
			}
			
			return ciResult;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public synchronized void setProperty(ConnectionInfo pConnectionInfo, String pName, Object pValue) throws Throwable
	{
		if (pName != null)
		{
	        //client properties can only be changed when the connection is closed!
		    if (pName.startsWith(IConnectionConstants.PREFIX_CLIENT))
		    {
    			if (isOpen(pConnectionInfo))
    			{
    				throw new SecurityException("Client properties are not accessible after the connection was opened!");
    			}
    			
    			Object oldValue = pConnectionInfo.getProperties().put(pName, pValue, false);
    			
    			firePropertyChanged(pName, oldValue, pValue, false);
		    }
	        else
	        {
	            Object oldValue = pConnectionInfo.getProperties().put(pName, pValue, false);
	            
	            firePropertyChanged(pName, oldValue, pValue, false);

	            if (isOpen(pConnectionInfo))
	            {
	                pConnectionInfo.setLastCallTime(System.currentTimeMillis());        
	        
	                server.setProperty(pConnectionInfo.getConnectionId(), pName, pValue);
	            }
	        }
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public synchronized Object getProperty(ConnectionInfo pConnectionInfo, String pName) throws Throwable
	{
		if (pConnectionInfo != null)
		{
			return pConnectionInfo.getProperties().get(pName);
		}
		
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public synchronized Hashtable<String, Object> getProperties(ConnectionInfo pConnectionInfo) throws Throwable
	{
		if (pConnectionInfo != null)
		{
			return (Hashtable<String, Object>)pConnectionInfo.getProperties().clone();
		}
		
		return null;
	}
	
    /**
     * {@inheritDoc}
     */
    public synchronized void setNewPassword(ConnectionInfo pConnectionInfo, String pOldPassword, String pNewPassword) throws Throwable
    {
        pConnectionInfo.setLastCallTime(System.currentTimeMillis());        
        
        server.setNewPassword(pConnectionInfo.getConnectionId(), pOldPassword, pNewPassword);
        
        syncWithServer(pConnectionInfo);
    }

    /**
	 * {@inheritDoc}
	 */
	public void addPropertyChangedListener(IConnectionPropertyChangedListener pListener)
	{
		if (pListener == null)
		{
			throw new IllegalArgumentException("Listener can't be null!");
		}
		
		synchronized (auPropertyChangedListeners)
		{
			if (!auPropertyChangedListeners.contains(pListener))
			{
			    auPropertyChangedListeners.add(pListener);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void removePropertyChangedListener(IConnectionPropertyChangedListener pListener)
	{
		synchronized (auPropertyChangedListeners) 
		{
			auPropertyChangedListeners.remove(pListener);
		}
	}
	
    /**
     * {@inheritDoc}
     */
    public void addCallBackResultListener(ICallBackResultListener pListener)
    {
		if (pListener == null)
		{
			throw new IllegalArgumentException("Listener can't be null!");
		}

		if (auCallBackResultListeners == null)
        {
            auCallBackResultListeners = new ArrayUtil<ICallBackResultListener>();
        }
        
        if (!auCallBackResultListeners.contains(pListener))
        {
            auCallBackResultListeners.add(pListener);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void removeCallBackResultListener(ICallBackResultListener pListener)
    {
        if (auCallBackResultListeners != null)
        {
            auCallBackResultListeners.remove(pListener);
        }
    }	

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Synchronize the client-side with the server-side.
     * 
     * @param pConnectionInfo the connection information
     * @throws Throwable communication error, security checks, invalid method, ...
     */
    private void syncWithServer(ConnectionInfo pConnectionInfo) throws Throwable
    {
        syncProperties(pConnectionInfo);
        
        checkCallBackResults(pConnectionInfo);
    }
    
	/**
	 * Synchronize the client-side properties with the server-side properties.
	 * 
	 * @param pConnectionInfo the connection information
	 * @throws Throwable communication error, security checks, invalid method, ...
	 */
	private void syncProperties(ConnectionInfo pConnectionInfo) throws Throwable
	{
		//with this call we get only accessible properties - NOT all!!!
		ChangedHashtable<String, Object> chtServerProps = server.getProperties(pConnectionInfo.getConnectionId());
		ChangedHashtable<String, Object> chtClientProps = pConnectionInfo.getProperties();

		Object oOldValue;
		
		String sKey;
		Object oValue;
		
		for (Entry<String, Object> entry : chtServerProps.entrySet())
		{ 
		    sKey = entry.getKey();
		    oValue = entry.getValue();
		    
			oOldValue = chtClientProps.put(sKey, oValue, false);

			firePropertyChanged(sKey, oOldValue, oValue, true);
		}
		
		Hashtable<String, Object> htKeys = (Hashtable<String, Object>)chtClientProps.clone();
		
		//remove missing properties
		for (String sClientKey : htKeys.keySet())
		{
			if (!chtServerProps.containsKey(sClientKey))
			{
				chtClientProps.remove(sClientKey);
			}
		}
	}

	/**
	 * Checks callback result objects and notifies callback result listeners.
	 * 
	 * @param pConnectionInfo the connection information
	 * @throws Throwable if notification of callback result listeners fails
	 */
	private void checkCallBackResults(ConnectionInfo pConnectionInfo) throws Throwable
	{
        if (auCallBackResultListeners != null)
        {
    	    List<ResultObject> liResult = server.getCallBackResults(pConnectionInfo.getConnectionId());

    	    if (liResult != null)
    	    {
    	        ResultObject resObject;
    	        
    	        for (int i = 0, cnt = liResult.size(); i < cnt; i++)
    	        {
    	            resObject = liResult.get(i);
    	            
    	            if (resObject.getType() == IConnection.TYPE_CALLBACKRESULT_RESULT)
    	            {
    	                CallBackResultEvent event = new CallBackResultEvent((String)resObject.getCallBackId(), resObject.getObject());
    	                
    	                for (int j = 0, cntj = auCallBackResultListeners.size(); j < cntj; j++)
    	                {
	                        auCallBackResultListeners.get(j).callBackResult(event);
    	                }
    	            }
    	        }
    	    }
        }    	    
	}

	/**
	 * Fires the property changed event on all registered {@link IConnectionPropertyChangedListener}s
	 * if the parameters <code>pOldValue</code> and <code>pNewValue</code> are
	 * not the same and both are not null.
	 * 
	 * @param pName the name of the property
	 * @param pOldValue the old value of the property
	 * @param pNewValue the new value of the property
	 * @param pContinueOnException whether an exception shouldn't stop notification of listeners
	 * @throws Throwable if listener notification failed
	 */
	private void firePropertyChanged(String pName, Object pOldValue, Object pNewValue, boolean pContinueOnException) throws Throwable
	{
		synchronized (auPropertyChangedListeners) 
		{
			if (!auPropertyChangedListeners.isEmpty())
			{
				if (pOldValue != null || pNewValue != null)
				{
					if (pOldValue == null || !pOldValue.equals(pNewValue))
					{
						PropertyEvent event = new PropertyEvent(pName, pOldValue, pNewValue);
						
						for (IConnectionPropertyChangedListener listener : auPropertyChangedListeners)
						{
						    try
						    {
						        listener.propertyChanged(event);
						    }
						    catch (Throwable th)
						    {
						        if (!pContinueOnException)
						        {
						            throw th;
						        }
						        
						        LoggerFactory.getInstance(DirectServerConnection.class).error(th);
						    }
						}
					}
				}
			}
		}
	}
	
	//****************************************************************
    // Subclass definition
    //****************************************************************

	/**
	 * The <code>DirctServerPushReceiver</code> is the {@link IPushReceiver} for connections,
	 * opened via {@link DirectServerConnection}s.
	 * 
	 * @author René Jahn
	 */
	private static final class DirectServerPushReceiver implements IPushReceiver
	{
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Class members
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	    /** the logger. */
	    private static ILogger log = LoggerFactory.getInstance(DirectServerConnection.class);
	    
	    /** the connection. */
	    private DirectServerConnection connection;

	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Initialization
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    
	    /**
	     * Creates a new instance of <code>DirectServerPushReceiver</code>.
	     * 
	     * @param pConnection the connection
	     */
	    private DirectServerPushReceiver(DirectServerConnection pConnection)
	    {
	        connection = pConnection;
	    }
	    
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Interface implementation
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	    /**
	     * {@inheritDoc}
	     */
        public void receivedMessage(PushMessage pMessage)
        {
            ResultObject resobj = pMessage.getObject();
            
            if (log.isEnabled(LogLevel.INFO))
            {
                log.info("Received push message: ", Byte.valueOf(resobj.getType()));
            }
        
            if (resobj.getType() == IConnection.TYPE_CALLBACKRESULT_RESULT)
            {
                if (connection.auCallBackResultListeners != null)
                {
                    CallBackResultEvent event = new CallBackResultEvent((String)resobj.getCallBackId(), resobj.getObject());
                    
                    for (int j = 0, cntj = connection.auCallBackResultListeners.size(); j < cntj; j++)
                    {
                        try
                        {
                        	ICallBackResultListener listener = connection.auCallBackResultListeners.get(j);
                        	
                        	synchronized(listener)
                        	{
                        		listener.callBackResult(event);
                        	}
                        }
                        catch (Throwable th)
                        {
                            log.error(th);                            
                        }
                    }
                }
            }
        }
	    
	}  // DirectServerPushReceiver
	
}	// DirectServerConnection
