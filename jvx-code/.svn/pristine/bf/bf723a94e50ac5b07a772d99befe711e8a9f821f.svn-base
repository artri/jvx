/*
 * Copyright 2011 SIB Visions GmbH
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
 * 23.09.2011 - [JR] - creation
 */
package com.sibvisions.rad.server.security;

import java.util.Map;

import javax.rad.server.AbstractObjectProvider;
import javax.rad.server.ISession;

/**
 * The <code>SimpleAddressAccessController</code> enables the access to an object with the name
 * "address". All other objects or method invocations will be denied.
 *  
 * @author René Jahn
 */
public class SimpleAddressAccessController implements IObjectAccessController
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public boolean isObjectAccessAllowed(AbstractObjectProvider pProvider, ISession pSession, Map pLifeCycleObject, String pObjectName, Object pObject)
	{
		if ("address".equals(pObjectName))
		{
			return true;
		}

		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isMethodInvocationAllowed(AbstractObjectProvider pProvider, ISession pSession, String pObjectName, Object pObject, String pMethodName, Object... pParams)
	{
		if ("address".equals(pObjectName))
		{
			return true;
		}
		
		return false;
	}

}	// SimpleAddressAccessController
