/*
 * Copyright 2012 SIB Visions GmbH
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
 * 21.11.2012 - [JR] - creation
 */
package com.sibvisions.rad.server.security;

import java.util.Map;

import javax.rad.server.AbstractObjectProvider;
import javax.rad.server.ISession;

/**
 * The <code>IObjectAccessController</code> defines a security controller for accessing
 * objects and methods.
 * 
 * @author René Jahn
 */
public interface IObjectAccessController
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets whether access to an object is allowed.
	 * 
	 * @param pProvider the object provider
	 * @param pSession the accessing session
	 * @param pLifeCycleObject the life-cycle instance 
	 * @param pObjectName the object name
	 * @param pObject the object
	 * @return <code>true</code> if access is granted, <code>false</code> otherwise
	 */
	public boolean isObjectAccessAllowed(AbstractObjectProvider pProvider, ISession pSession, Map pLifeCycleObject, String pObjectName, Object pObject);
	
	/**
	 * Gets whether method invocation is allowed.
	 * 
	 * @param pProvider the object provider
	 * @param pSession the accessing session
	 * @param pObjectName the object name 
	 * @param pObject the object
	 * @param pMethodName the method name
	 * @param pParams the parameters
	 * @return <code>true</code> if access is granted, <code>false</code> otherwise
	 */
	public boolean isMethodInvocationAllowed(AbstractObjectProvider pProvider, 
			                                 ISession pSession, 
			                                 String pObjectName, 
			                                 Object pObject, 
			                                 String pMethodName, 
			                                 Object... pParams);
	
}	// IObjectAccessController
