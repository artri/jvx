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
 */
package com.sibvisions.rad.server.object;

import javax.rad.remote.ConnectionInfo;
import javax.rad.remote.IConnection;
import javax.rad.server.SessionContext;

/**
 * The <code>ExchangeSession</code> is a session bound connection. It can be used to direct 
 * call methods and actions from the bound server-side session.<br>
 * The <code>ExchangeSession</code> can be used for accessing the session from outside
 * the server, e.g. a servlet.
 * Example for servlet usage:
 * server-side action:
 * <pre>
 * public void Object generateKey()
 * {
 *   return ObjectCache.put(new ExchangeSession());
 * }
 * </pre>
 * servlet code:
 * <pre>
 * public void doGet(HttpServletRequest pRequest, HttpServletResponse pResponse) throws ServletException
 * {
 *   ObjectCache.get(pRequest.getParameter("KEY"));
 * }
 * </pre>
 * 
 * @author René Jahn
 */
public class ExchangeSession
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the connection information for the bound session. */
	private ConnectionInfo ciSession;
	
	/** the direct server connection for method and action calls. */
	private IConnection conSession;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>ExchangeSession</code>. The instance will be bound to a server-side
	 * session. All method and action calls will be made through the bound session.
	 */
	public ExchangeSession()
	{
		ciSession = new ConnectionInfo();
		ciSession.setConnectionId(SessionContext.getCurrentSession().getId());

		conSession = SessionContext.getCurrentInstance().getServerConnection();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * The method will call a method from the bound session.
	 * 
	 * @param pObjectName an already mapped server object name/alias
	 * @param pMethod method name which should be called
	 * @param pParams parameters for the method call
	 * @return result from the remote method call
	 * @throws Throwable communication error, security checks, invalid method, ...
	 */
	public Object call(String pObjectName, String pMethod, Object... pParams) throws Throwable
	{
		return conSession.call(ciSession, new String[] {pObjectName}, new String[] {pMethod}, new Object[][] {pParams}, null)[0];
	}

	/**
	 * The method will call a method from the bound session.
	 * 
	 * @param pObjectName an already mapped server object name/alias
	 * @param pMethod method name which should be called
	 * @return result from the remote method call
	 * @throws Throwable communication error, security checks, invalid method, ...
	 */
	public Object call(String pObjectName, String pMethod) throws Throwable
	{
		return conSession.call(ciSession, new String[] {pObjectName}, new String[] {pMethod}, null, null)[0];
	}
	
	/**
	 * The method will call an action from the bound session.
	 * 
	 * @param pAction action which should be called
	 * @return result from the remote action call
	 * @throws Throwable communication error, security checks, invalid action, ...
	 */
	public Object callAction(String pAction) throws Throwable
	{
		return conSession.call(ciSession, null, new String[] {pAction}, null, null)[0];
	}
	
	/**
	 * The method will call an action from the bound session.
	 *
	 * @param pAction action which should be called
	 * @param pParams parameters for the action call
	 * @return result from the remote action call
	 * @throws Throwable communication error, security checks, invalid method, ...
	 */
	public Object callAction(String pAction, Object... pParams) throws Throwable
	{
		return conSession.call(ciSession, null, new String[] {pAction}, new Object[][] {pParams}, null)[0];
	}
	
	/**
	 * Gets the value of a session property.
	 * 
	 * @param pName the property name
	 * @return the value of the property or <code>null</code> if the property is not available
	 * @throws Throwable communication error, security checks, invalid method, ...
	 */
	public Object getProperty(String pName) throws Throwable
	{
		return conSession.getProperty(ciSession, pName);
	}
		
}	// ExchangeSession
