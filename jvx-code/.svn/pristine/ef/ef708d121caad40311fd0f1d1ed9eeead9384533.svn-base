/*
 * Copyright 2021 SIB Visions GmbH
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
 * 09.06.2021 - [JR] - creation
 */
package com.sibvisions.rad.server;

import java.util.Map;

import javax.rad.server.ISession;

/**
 * The <code>ILifeCycleObjectListener</code> can be used to get notifications about created
 * lifecycle objects.
 * 
 * @author rjahn
 */
public interface ILifeCycleObjectListener 
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Invoked when a session was created and is ready to use.
	 * 
	 * @param pSession the newly created session
	 * @param pLCO the lifecycle object
	 */
	public void applicationObjectCreated(ISession pSession, Map pLCO);

	/**
	 * Invoked when a session was created and is ready to use.
	 * 
	 * @param pSession the newly created session
	 * @param pLCO the lifecycle object
	 */
	public void sessionObjectCreated(ISession pSession, Map pLCO);
	
	/**
	 * Invoked when a session was destroyed an can not be used anymore.
	 * 
	 * @param pSession the destroyed session
	 * @param pLCO the lifecycle object
	 */
	public void objectDestroyed(ISession pSession, Map pLCO);
	
}
