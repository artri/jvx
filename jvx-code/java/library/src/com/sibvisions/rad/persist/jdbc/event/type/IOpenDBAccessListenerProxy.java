/*
 * Copyright 2019 SIB Visions GmbH
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
 * 20.03.2019 - [JR] - creation
 */
package com.sibvisions.rad.persist.jdbc.event.type;

import java.lang.reflect.InvocationHandler;

import javax.rad.util.event.proxy.impl.AbstractListenerProxy;

import com.sibvisions.rad.persist.jdbc.DBAccess;

/**
 * The generic implementation of {@link IOpenDBAccessListener}.
 * 
 * @author René Jahn
 */
public class IOpenDBAccessListenerProxy extends AbstractListenerProxy
                                        implements IOpenDBAccessListener
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>IOpenDBAccessListenerProxy</code>.
	 * 
	 * @param pHandler the handler
	 */
	public IOpenDBAccessListenerProxy(InvocationHandler pHandler)
	{
		super(pHandler);
	}
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public void openedDBAccess(DBAccess pDBAccess) throws Throwable 
	{
		dispatch(method("openedDBAccess", DBAccess.class), pDBAccess);
	}

}	// IOpenDBAccessListenerProxy
