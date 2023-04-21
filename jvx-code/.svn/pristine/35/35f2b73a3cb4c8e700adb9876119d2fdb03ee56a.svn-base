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
 * 05.10.2008 - [JR] - setNewPassword defined
 * 17.10.2008 - [JR] - open/openSub: removed ArrayUtil and used a ChangedHashtable instead
 * 11.02.2009 - [JR] - changed properties to <String, Object> instead of <String, String>
 * 08.05.2009 - [JR] - defined getObjectProvider
 * 25.05.2009 - [JR] - getProperty, setProperty, getCallBackResults, setAndCheckAlive now throws Throwable
 * 04.10.2009 - [JR] - setNewPassword: old password as parameter
 */
package javax.rad.server;

import java.util.List;

import com.sibvisions.util.ChangedHashtable;

/**
 * The <code>IServer</code> interface defines the methods which are necessary for
 * remote server implementations.
 * 
 * @author René Jahn
 */
public interface IServer
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the {@link AbstractObjectProvider}.
	 * 
	 * @return the object provider.
	 */
	public AbstractObjectProvider getObjectProvider();
	
	/**
	 * Gets the {@link AbstractSessionManager}.
	 * 
	 * @return the session manager.
	 */
	public AbstractSessionManager getSessionManager();
	
	/**
	 * Creates a new session for an application. The credentials are
	 * stored in the properties.
	 * 
	 * @param pProperties the initial session properties
	 * @return session identifier of newly created <code>Session</code>
	 * @throws Throwable if the session can not be created
	 */
	public Object createSession(ChangedHashtable<String, Object> pProperties) throws Throwable;
	
	/**
	 * Creates a sub session of an application. The name/alias of the sub session
	 * is stored in the properties.
	 * 
	 * @param pSessionId session identifier
	 * @param pProperties the initial session properties
	 * @return session identifier of newly created <code>SubSession</code>
	 * @throws Throwable if the session can not be created
	 */
	public Object createSubSession(Object pSessionId, 
			                       ChangedHashtable<String, Object> pProperties) throws Throwable;
	
	/**
	 * Destroyes a session with given identifier.
	 * 
	 * @param pSessionId session identifier
	 */	
	public void destroySession(Object pSessionId);
	
	/**
	 * Executes a method call.
	 *  
	 * @param pSessionId session identifier
	 * @param pObjectName server object name/alias
	 * @param pMethod method name which should be called
	 * @param pParams parameters for the method call
	 * @return result of method call
	 * @throws Throwable if an error occurs during execution
	 */
	public Object execute(Object pSessionId, String pObjectName, String pMethod, Object... pParams) throws Throwable;
	
	/**
	 * Executes an asynchronous method call.
	 *  
	 * @param pSessionId session identifier
	 * @param pCallBackId the callback identifier
	 * @param pObjectName server object name/alias
	 * @param pMethod method name which should be called
	 * @param pParams parameters for the method call
	 * @throws Throwable if an error occurs during execution
	 */
	public void executeCallBack(Object pSessionId, Object pCallBackId, String pObjectName, String pMethod, Object... pParams) throws Throwable;

	/**
	 * Executes an action call.
	 *  
	 * @param pSessionId session identifier
	 * @param pAction action which should be called
	 * @param pParams parameters for the action call
	 * @return result of action call
	 * @throws Throwable if an error occurs during execution
	 */
	public Object executeAction(Object pSessionId, String pAction, Object... pParams) throws Throwable;
	
	/**
	 * Executes an asynchronous action call.
	 *  
	 * @param pSessionId session identifier
	 * @param pCallBackId the callback identifier
	 * @param pAction action which should be called
	 * @param pParams parameters for the action call
	 * @throws Throwable if an error occurs during execution
	 */
	public void executeActionCallBack(Object pSessionId, Object pCallBackId, String pAction, Object... pParams) throws Throwable;

	/**
	 * Returns all available objects of an asynchronous execution.
	 * 
	 * @param pSessionId session identifier
	 * @return result objects or null if there are no result objects
	 * @throws Throwable if an error occurs during execution
	 */
	public List<ResultObject> getCallBackResults(Object pSessionId) throws Throwable;
	
	/**
	 * Sets a session property.
	 * 
	 * @param pSessionId the session identifier
	 * @param pName the property name
	 * @param pValue the value of the property or <code>null</code> to delete the property
	 * @throws Throwable if an error occurs during execution
	 */
	public void setProperty(Object pSessionId, String pName, Object pValue) throws Throwable;
	
	/**
	 * Gets the value of a session property.
	 * 
	 * @param pSessionId the session identifier
	 * @param pName the property name
	 * @return the value of the property or <code>null</code> if the property is not available
	 * @throws Throwable if an error occurs during execution
	 */
	public Object getProperty(Object pSessionId, String pName) throws Throwable;
	
	/**
	 * Gets all session properties.
	 * 
	 * @param pSessionId the session identifier
	 * @return a {@link ChangedHashtable} with property names and values
	 * @throws Throwable if an error occurs during execution
	 */
	public ChangedHashtable<String, Object> getProperties(Object pSessionId) throws Throwable;

	/**
	 * Sets the alive state for a session and validates the alive
	 * state of sub sessions.
	 * 
	 * @param pSessionId the session id
	 * @param pSubSessionId the sub session ids
	 * @return the invalid/expired sub session ids
	 * @throws Throwable if an error occurs during execution
	 */
	public Object[] setAndCheckAlive(Object pSessionId, Object... pSubSessionId) throws Throwable;
	
	/**
	 * Sets a new password for the user of a session.
	 * 
	 * @param pSessionId the session id
	 * @param pOldPassword the old password
	 * @param pNewPassword the new password
	 * @throws Throwable if an error occurs during execution
	 */
	public void setNewPassword(Object pSessionId, String pOldPassword, String pNewPassword) throws Throwable;
	
}	// IServer
