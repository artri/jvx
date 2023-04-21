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
package javax.rad.application.genui.event;

import java.lang.reflect.InvocationHandler;

import javax.rad.application.genui.RemoteApplication;

/**
 * The generic implementation of {@link IRemoteApplicationListener}.
 * 
 * @author Ren� Jahn
 */
public class IRemoteApplicationListenerProxy extends IApplicationListenerProxy
                                             implements IRemoteApplicationListener
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>IRemoteApplicationListenerProxy</code>.
	 * 
	 * @param pHandler the handler
	 */
	public IRemoteApplicationListenerProxy(InvocationHandler pHandler)
	{
		super(pHandler);
	}
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public void afterLogin(RemoteApplication pApplication) throws Throwable 
	{
		dispatch(method("afterLogin", RemoteApplication.class), pApplication);
	}

	/**
	 * {@inheritDoc}
	 */
	public void afterLogout(RemoteApplication pApplication) throws Throwable 
	{
		dispatch(method("afterLogout", RemoteApplication.class), pApplication);
	}

}	// IRemoteApplicationListenerProxy
