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
 * 04.10.2008 - [JR] - setNewPassword defined
 * 05.10.2008 - [JR] - open: added pNewPassword
 * 27.10.2008 - [JR] - open/openSub: removed parameters (needed as key/value pair)
 * 04.02.2009 - [JR] - reopen defined
 * 04.10.2009 - [JR] - setNewPassword: old password as parameter
 * 11.07.2013 - [JR] - #728: isCalling defined
 * 04.04.2014 - [RZ] - #997: added addPropertyChangedListener and removePropertyChangedListener
 * 08.06.2016 - [JR] - #25: defined TYPE_CALLBACKRESULT_RESULT 
 * 12.01.2017 - [JR] - #1744: defined NOPARAMETER
 */
package javax.rad.remote;

import java.util.Hashtable;

import javax.rad.remote.event.ICallBackListener;
import javax.rad.remote.event.ICallBackResultListener;
import javax.rad.remote.event.IConnectionPropertyChangedListener;

/**
 * The <code>IConnection</code> interface defines all methods for
 * the communication between client and server.
 * 
 * @author René Jahn
 */
public interface IConnection
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constants
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the max. bytes for uncompressed communication (more bytes switches to compressed mode). */
	public static final int COMPRESSION_BYTES = 256;

	/** Constant type for uncompressed communication. */
	public static final int MODE_UNCOMPRESSED = 0x01;

	/** Constant type for compressed communication. */
	public static final int MODE_COMPRESSED = 0x02;
	
	
	/** the acknowledge flag. */
	public static final int FLAG_ACKNOWLEDGE = 'A';
	
	/** the established flag. */
	public static final int FLAG_ESTABLISHED = 'E';

	/** the broken flag. */
	public static final int FLAG_BROKEN = 'B';
	
	
	/** Constant type for successful callback remote method calls. */
	public static final byte TYPE_CALLBACK_RESULT = 0x1;
	
	/** Constant type for failed callback remote method calls. */
	public static final byte TYPE_CALLBACK_ERROR = 0x2;

    /** Constant type for return values from remote method calls. */
	public static final byte TYPE_CALL_RESULT = 0x3;
	
	/** Constant type for exception return values. */
	public static final byte TYPE_CALL_ERROR = 0x4;
	
	/** Constant type for properties from the server. */
	public static final byte TYPE_PROPERTY_RESULT = 0x5;
	
    /** Constant type for failed callback remote method calls. */
    public static final byte TYPE_CALLBACKRESULT_RESULT = 0x6;

    
	/** Constant for the internal session handler. */
	public static final String OBJ_SESSION = "Session!"; 
	
	/** Constant for the session create method. */
	public static final String MET_SESSION_CREATE = "createSession";

	/** Constant for the sub session create method. */
	public static final String MET_SESSION_SUBSESSION_CREATE = "createSubSession";

	/** Constant for the session destroy method. */
	public static final String MET_SESSION_DESTROY = "destroySession";
	
	/** Constant for the set property method. */
	public static final String MET_SESSION_SET_PROPERTY = "setProperty";
	
	/** Constant for the get property method. */
	public static final String MET_SESSION_GET_PROPERTY = "getProperty";

	/** Constant for the get properties method. */
	public static final String MET_SESSION_GET_PROPERTIES = "getProperties";

	/** Constant for the set and check of the session alive state. */
	public static final String MET_SESSION_SETCHECKALIVE = "setAndCheckAlive";
	
	/** Constant for the set new password method. */
	public static final String MET_SESSION_SET_NEW_PASSWORD = "setNewPassword";

    /** the null(no) parameter array. */
    public static final Object[] NOPARAMETER = new Object[0];
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Opens the connection to the server. The credentials have to be a key/value 
	 * mapping in the connection information. 
	 *
	 * @param pConnectionInfo the connection information 
	 * @throws Throwable if the connection can not be established
	 */
	public void open(ConnectionInfo pConnectionInfo) throws Throwable;

	/**
	 * Opens a new sub connection. The name of the sub connection has to be a key/value mapping
	 * in the sub connection information.
	 * 
	 * @param pConnectionInfo the connection information of the master connection
	 * @param pConnectionInfoSub the connection information of the sub connection
	 * @throws Throwable communication error, security checks, invalid method, ...
	 */
	public void openSub(ConnectionInfo pConnectionInfo, ConnectionInfo pConnectionInfoSub) throws Throwable;

	/**
	 * Checks if the connection to the server is opened.
	 * 
	 * @param pConnectionInfo the connection information
	 * @return true if the connection is open
	 */
	public boolean isOpen(ConnectionInfo pConnectionInfo);
	
	/**
	 * Close the connection to the server.
	 * 
	 * @param pConnectionInfo the connection information
	 * @throws Throwable if the connection can not be established
	 */
	public void close(ConnectionInfo pConnectionInfo) throws Throwable;

	/**
	 * Calls desired methods from a remote server object.
	 *
	 * @param pConnectionInfo the connection information
	 * @param pObjectName list of server object names/aliases
	 * @param pMethod method names which should be called
	 * @param pParams parameters for the method calls
	 * @param pCallBack callback listeners for asynchronous or null for synchronous calls
	 * @return result list from the remote method calls
	 * @throws Throwable communication error, security checks, invalid method, ...
	 */
	public Object[] call(ConnectionInfo pConnectionInfo,
						 String[] pObjectName, 
						 String[] pMethod, 
						 Object[][] pParams, 
						 ICallBackListener[] pCallBack) throws Throwable;
	
	/**
	 * Gets whether a call is active.
	 * 
	 * @return <code>true</code> if a call is active, <code>false</code> otherwise
	 */
	public boolean isCalling();
	
	/**
	 * Sets the alive state for a connection, on the server, and validates the alive
	 * state of subconnections.
	 * 
	 * @param pConnectionInfo the connection information
	 * @param pSubConnections the connection information of the sub connections, for the alive validation
	 * @return the invalid/expired sub connections
	 * @throws Throwable communication error, security checks, invalid method, ...
	 */
	public ConnectionInfo[] setAndCheckAlive(ConnectionInfo pConnectionInfo, ConnectionInfo[] pSubConnections) throws Throwable;
	
	/**
	 * Sets a connection property.
	 * 
	 * @param pConnectionInfo the connection information
	 * @param pName the property name
	 * @param pValue the value of the property or <code>null</code> to delete the property
	 * @throws Throwable communication error, security checks, invalid method, ...
	 * @throws SecurityException if it's not allowed to set the property
	 */
	public void setProperty(ConnectionInfo pConnectionInfo, String pName, Object pValue) throws Throwable;
	
	/**
	 * Gets a connection property.
	 * 
	 * @param pConnectionInfo the connection information
	 * @param pName the property name
	 * @return the value of the property or <code>null</code> if the property is not set
	 * @throws Throwable communication error, security checks, invalid method, ...
	 */
	public Object getProperty(ConnectionInfo pConnectionInfo, String pName) throws Throwable;
	
    /**
     * Sets a new password for the connected user. 
     * 
     * @param pConnectionInfo the connection information
     * @param pOldPassword the old password
     * @param pNewPassword the new password
     * @throws Throwable communication error, security checks, invalid method, ...
     */
    public void setNewPassword(ConnectionInfo pConnectionInfo, String pOldPassword, String pNewPassword) throws Throwable;

    /**
	 * Gets a clone of all connection properties.
	 * 
	 * @param pConnectionInfo the connection information
	 * @return a {@link Hashtable} with property names and values
	 * @throws Throwable communication error, security checks, invalid method, ...
	 */
	public Hashtable<String, Object> getProperties(ConnectionInfo pConnectionInfo) throws Throwable;
	
	/**
	 * Adds an {@link IConnectionPropertyChangedListener} to the list of registered listeners.
	 * 
	 * @param pListener the new property changed listener.
	 */
	public void addPropertyChangedListener(IConnectionPropertyChangedListener pListener);
	
	/**
	 * Removes an {@link IConnectionPropertyChangedListener} from the list of registered listeners.
	 *  
	 * @param pListener the property changed listener to remove
	 */
	public void removePropertyChangedListener(IConnectionPropertyChangedListener pListener);

    /**
     * Adds an {@link ICallBackResultListener} to the list of registered listeners.
     * 
     * @param pListener the new callback result listener.
     */
    public void addCallBackResultListener(ICallBackResultListener pListener);
    
    /**
     * Removes an {@link ICallBackResultListener} from the list of registered listeners.
     *  
     * @param pListener the callback result listener to remove
     */
    public void removeCallBackResultListener(ICallBackResultListener pListener);
	
}	// IConnection
