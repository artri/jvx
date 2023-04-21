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
 * 06.10.2010 - [JR] - creation
 * 19.10.2012 - [JR] - #603: call - dot notation support
 * 11.07.2013 - [JR] - set last call time
 * 11.07.2013 - [JR] - #728: isCalling implemented
 *                   - setLastCallTime called
 * 04.04.2014 - [RZ] - #997: implemented addPropertyChangedListener and removePropertyChangedListener
 * 12.01.2017 - [JR] - #1744: send NOPARAMETER instead of null 
 */
package com.sibvisions.rad.util;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;

import javax.rad.remote.ConnectionInfo;
import javax.rad.remote.IConnection;
import javax.rad.remote.IConnectionConstants;
import javax.rad.remote.event.ICallBackListener;
import javax.rad.remote.event.ICallBackResultListener;
import javax.rad.remote.event.IConnectionPropertyChangedListener;
import javax.rad.remote.event.PropertyEvent;

import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.Reflective;
import com.sibvisions.util.type.CommonUtil;
import com.sibvisions.util.type.StringUtil;

/**
 * The <code>DirectObjectConnection</code> enables direct object calls without the need of a server.
 * It's possible to use the original client implementation and pass server and lifecycle objects. But
 * it is important to specify all needed server objects by name (manual lifecycle handling).
 * 
 * @author René Jahn
 */
public class DirectObjectConnection implements IConnection
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** the known/mapped objects. */
	private Map<String, Object> mpObjects;
	
    /** the collection of {@link IConnectionPropertyChangedListeners}. */
    private ArrayUtil<IConnectionPropertyChangedListener> auPropertyChangedListeners;

    /** whether a call is active. */
	private boolean bCalling = false;
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>DirectObjectConnection</code> without any object.
	 */
	public DirectObjectConnection()
	{
	}
	
	/**
	 * Creates a new instance of <code>DirectObjectConnection</code> with predefined objects.
	 * 
	 * @param pObjects the initial objects.
	 */
	public DirectObjectConnection(Map<String, Object> pObjects)
	{
		mpObjects = new HashMap<String, Object>(pObjects);
	}
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
	
	/**
	 * Calls a list of methods from objects identified by name(s). The object is searched in the known map of objects.
	 * It's not possible to use callback listeners and it's also not possible to call actions. Only specific object
	 * calls are implemented.
	 * 
	 * @param pConnectionInfo the connection info
	 * @param pObjectName the object names
	 * @param pMethod the method names
	 * @param pParams the method params
	 * @param pCallBack the callback listeners
	 * @return the result of the method calls
	 * @throws Throwable if an exception occurs
	 */
	public synchronized Object[] call(ConnectionInfo pConnectionInfo, String[] pObjectName, String[] pMethod, Object[][] pParams, ICallBackListener[] pCallBack) throws Throwable
	{
		try
		{
			bCalling = true;

			if (pCallBack != null)
			{
				for (ICallBackListener icb : pCallBack)
				{
					if (icb != null)
					{
						throw new RuntimeException("CallBacks are not supported!");
					}
				}
			}
			
			if (pObjectName == null || pObjectName.length == 0)
			{
				throw new RuntimeException("Action calls are not supported!");
			}
	
			Object[] oResult = new Object[pObjectName.length];
			
			Object oCall = null;
			Object oMapObject;
			
			ArrayUtil<String> auNames;
			
			String sObjectName;
			
			for (int i = 0, anz = pObjectName.length; i < anz; i++)
			{
				if (mpObjects == null)
				{
					throw new RuntimeException("Unknown object '" + pObjectName[i] + "'");
				}
	
				auNames = StringUtil.separateList(pObjectName[i], ".", true);
				
				oCall = mpObjects.get(auNames.get(0));
	
				for (int j = 1, anzj = auNames.size(); j < anzj; j++)
				{
					sObjectName = auNames.get(j);
	
					try
					{
						//try to get "sub" object
						oCall = Reflective.call(oCall, StringUtil.formatMethodName("get", sObjectName));
					}
					catch (NoSuchMethodException nsme)
					{
						if (oCall instanceof Map)
						{
							oMapObject = ((Map)oCall).get(sObjectName);
							
							if (oMapObject == null && !((Map)oCall).containsKey(sObjectName))
							{
								throw new RuntimeException("Unknown object '" + sObjectName + "'");
							}
							
							//use the result!
							oCall = oMapObject;
						}
						else
						{
							throw new RuntimeException("Unknown object '" + sObjectName + "'", nsme);
						}
					}
					finally
					{
						pConnectionInfo.setLastCallTime(System.currentTimeMillis());					
					}
				}
	
				if (oCall == null)
				{
					throw new RuntimeException("Unknown object '" + pObjectName[i] + "'");
				}

				try
				{
					//throw Exceptions, if occurs, direct 
					oResult[i] = Reflective.call(oCall, pMethod[i], pParams != null ? pParams[i] : NOPARAMETER);
				}
				finally
				{
					pConnectionInfo.setLastCallTime(System.currentTimeMillis());
				}
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
	public synchronized void close(ConnectionInfo pConnectionInfo) throws Throwable
	{
		pConnectionInfo.setConnectionId(null);
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized Hashtable<String, Object> getProperties(ConnectionInfo pConnectionInfo) throws Throwable
	{
		return pConnectionInfo.getProperties();
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized Object getProperty(ConnectionInfo pConnectionInfo, String pName) throws Throwable
	{
		return pConnectionInfo.getProperties().get(pName);
	}

	/**
	 * The connection is always open.
	 * 
	 * @param pConnectionInfo the connection info
	 * @return <code>true</code>
	 */
	public boolean isOpen(ConnectionInfo pConnectionInfo)
	{
		return pConnectionInfo.getConnectionId() != null;
	}

	/**
	 * Sets a random connection ID.
	 * 
	 * @param pConnectionInfo the connection info
	 */
	public synchronized void open(ConnectionInfo pConnectionInfo) 
	{
		pConnectionInfo.setConnectionId(UUID.randomUUID().toString());
	}

	/**
	 * Sets a random connection ID for the sub connection.
	 * 
	 * @param pConnectionInfo the connection info
	 * @param pConnectionInfoSub the sub connection info
	 */
	public synchronized void openSub(ConnectionInfo pConnectionInfo, ConnectionInfo pConnectionInfoSub)
	{
		pConnectionInfoSub.setConnectionId(UUID.randomUUID().toString());
	}

	/**
	 * Not supported.
	 * 
	 * @param pConnectionInfo the connection info
	 */
	public synchronized void reopen(ConnectionInfo pConnectionInfo)
	{
	}

	/**
	 * Returns <code>null</code>.
	 * 
	 * @param pConnectionInfo the connection info
	 * @param pSubConnections the current sub connection infos
	 * @return <code>null</code>
	 */
	public synchronized ConnectionInfo[] setAndCheckAlive(ConnectionInfo pConnectionInfo, ConnectionInfo[] pSubConnections)
	{
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized void setNewPassword(ConnectionInfo pConnectionInfo, String pOldPassword, String pNewPassword) throws Throwable
	{
		pConnectionInfo.getProperties().put(IConnectionConstants.PASSWORD, pNewPassword);
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized void setProperty(ConnectionInfo pConnectionInfo, String pName, Object pValue) throws Throwable
	{
		Object oldValue = pConnectionInfo.getProperties().put(pName, pValue);
		
		if (auPropertyChangedListeners != null)
		{
		    if (!CommonUtil.equals(oldValue, pValue))
		    {
				PropertyEvent event = new PropertyEvent(pName, oldValue, pValue);
				
				for (IConnectionPropertyChangedListener listener : auPropertyChangedListeners)
				{
					listener.propertyChanged(event);
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void addPropertyChangedListener(IConnectionPropertyChangedListener pListener)
	{
		if (auPropertyChangedListeners == null)
		{
			auPropertyChangedListeners = new ArrayUtil<IConnectionPropertyChangedListener>();
		}
		
		auPropertyChangedListeners.add(pListener);
	}

	/**
	 * {@inheritDoc}
	 */
	public void removePropertyChangedListener(IConnectionPropertyChangedListener pListener)
	{
		if (auPropertyChangedListeners != null)
		{
			auPropertyChangedListeners.remove(pListener);
		}
	}
	
    /**
     * {@inheritDoc}
     */	
	public void addCallBackResultListener(ICallBackResultListener pListener)
	{
	}
	    
    /**
     * {@inheritDoc}
     */
	public void removeCallBackResultListener(ICallBackResultListener pListener)
    {
    }
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Puts an object to the list of known objects.
	 * 
	 * @param pName the object name
	 * @param pObject the object or <code>null</code> to remove the object with the specified name
	 * @return the previous object if the key is present
	 */
	public Object put(String pName, Object pObject)
	{
		if (pObject == null)
		{
			return remove(pName);
		}
		else
		{
			if (mpObjects == null)
			{
				mpObjects = new HashMap<String, Object>();
			}
			
			return mpObjects.put(pName, pObject);
		}
	}
	
	/**
	 * Gets a specific object from the list of known objects.
	 * 
	 * @param pName the object name
	 * @return the object or <code>null</code> if object wasn't found
	 */
	public Object get(String pName)
	{
	    if (mpObjects == null || pName == null)
	    {
	        return null;
	    }
	    
	    return mpObjects.get(pName);
	}
	
	/**
	 * Removes an object from the list of known objects.
	 * 
	 * @param pName the object name
	 * @return the object for the object name
	 */
	public Object remove(String pName)
	{
		if (mpObjects != null)
		{
			return mpObjects.remove(pName);
		}
		
		return null;
	}
	
}	// DirectObjectConnection
