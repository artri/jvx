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
 * 23.05.2009 - [JR] - getLifeCycleName defined    
 * 07.07.2009 - [JR] - get defined     
 * 30.07.2009 - [JR] - setProperty defined     
 * 18.11.2009 - [JR] - #33: put defined
 * 15.10.2013 - [JR] - inactive interval is now in seconds instead of minutes
 */
package javax.rad.server;

import java.util.Hashtable;

/**
 * Provides a way to identify a user and to store information about that user.
 * 
 * @author René Jahn
 */
public interface ISession
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the session identifier.
	 * 
	 * @return session id
	 */
	public Object getId();

	/**
	 * Gets the name of the life-cycle object.
	 * 
	 * @return the name of the life-cycle object
	 */
	public String getLifeCycleName();
	
	/**
	 * Gets the associated application name of the session.
	 * 
	 * @return service name
	 */
	public String getApplicationName();
	
	/**
	 * Gets the sessions user name.
	 * 
	 * @return the user name
	 */
	public String getUserName();
	
	/**
	 * Gets the sessions password.
	 * 
	 * @return the password
	 */
	public String getPassword();
	
	/**
	 * Gets the value of a property.
	 * 
	 * @param pName the property name
	 * @return the value of the property or <code>null</code> if the property is not available
	 */
	public Object getProperty(String pName);
	
	/**
	 * Gets all properties.
	 * 
	 * @return a {@link Hashtable} with property names and values
	 */
	public Hashtable<String, Object> getProperties();
	
	/**
	 * Sets the value of a property.
	 * 
	 * @param pName the property name
	 * @param pValue the value for the property or <code>null</code> to delete the property
	 */
	public void setProperty(String pName, Object pValue);
	
	/**
	 * Gets the session start/create time.
	 * 
	 * @return start time in millis
	 */
	public long getStartTime();
	
	/**
	 * Gets the time of the last session access.
	 * 
	 * @return access time in millis
	 */
	public long getLastAccessTime();
	
	/**
	 * Specifies the time, in seconds, before the session will be inactive. 
	 * A zero or negative time indicates the session should never be inactive.
	 * 
	 * @param pMaxInactiveInterval time in seconds
	 */
	public void setMaxInactiveInterval(int pMaxInactiveInterval);

	/**
	 * Returns the maximum time interval, in seconds, that this session will be active.
	 * After this interval, the session is inactive. The maximum time interval can be set with the 
	 * <code>setMaxInactiveInterval</code> method. A zero or negative time indicates the session should never be inactive.
	 * 
	 * @return time in seconds
	 * @see #setMaxInactiveInterval(int)
	 */
	public int getMaxInactiveInterval();
	
	/**
	 * Checks if the session is inactive.
	 * 
	 * @param pAccessTime current access time
	 * @return <code>true</code> if the session is inactive
	 * @see #getMaxInactiveInterval()
	 * @see #getLastAccessTime()
	 */
	public boolean isInactive(long pAccessTime);
	
	/**
	 * Gets the time of the last communication of the session.
	 * 
	 * @return the last communication/alive time
	 */
	public long getLastAliveTime();

	/**
	 * Sets the desired client-side communication interval for this session. 
	 *  
	 * @param pAliveInterval the alive interval (client-side)
	 */
	public void setAliveInterval(long pAliveInterval);

	/**
	 * Gets the desired client-side communication interval for this session.
	 * 
	 * @return the alive interval (client-side)
	 */
	public long getAliveInterval();

	/**
	 * Checks if the session is alive. That means the client sends alive
	 * messages. The session is not alive if the client doesn't send alive
	 * messages.
	 * 
	 * @param pAccessTime current access time
	 * @return <code>true</code> if the session is alive
	 * @see #getLastAliveTime()
	 * @see #getAliveInterval()
	 */
	public boolean isAlive(long pAccessTime);
	
	/**
	 * Executes a method from an object in the life-cycle object.
	 * 
	 * @param pObjectName list of already mapped server object name/alias
	 * @param pMethod method name which should be called
	 * @param pParams parameters for the method call
	 * @return result of method call or null if it's an asynchronous method call
	 * @throws Throwable if the object identified by <code>pObjectName</code> was found but can not be created
	 * @throws SecurityException if the method call is not allowed
	 */
	public Object call(String pObjectName, String pMethod, Object... pParams) throws Throwable;
	
	/**
	 * Executes an action from the life-cycle object.
	 * 
	 * @param pAction action which should be called
	 * @param pParams the parameters for the action call
	 * @return result from the action call
	 * @throws Throwable communication error, security checks, invalid action, ...
	 */
	public Object callAction(String pAction, Object... pParams) throws Throwable;
	
	/**
	 * Gets an object from the life-cycle object.
	 * 
	 * @param pObjectName the object name
	 * @return the object
	 * @throws Throwable if the object was not found or an error occured during object creation
	 */
	public Object get(String pObjectName) throws Throwable;
	
	/**
	 * Puts an object to the life-cycle object.
	 * 
	 * @param pObjectName the object name
	 * @param pObject the object
	 * @return the object if an object was already specified
	 * @throws Throwable if an unknown error occurs while putting the object
	 */
	public Object put(String pObjectName, Object pObject) throws Throwable;
	
	/**
	 * Gets the {@link IConfiguration} of the session.
	 * 
	 * @return the sessions configuration
	 */
	public IConfiguration getConfig();
	
}	// ISession
