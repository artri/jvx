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
package javax.rad.remote.event;

import java.lang.reflect.InvocationHandler;

import javax.rad.util.event.proxy.impl.AbstractListenerProxy;

/**
 * The generic implementation of {@link IConnectionListener}.
 * 
 * @author René Jahn
 */
public class IConnectionListenerProxy extends AbstractListenerProxy
                                	  implements IConnectionListener
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>IConnectionListenerProxy</code>.
	 * 
	 * @param pHandler the handler
	 */
	public IConnectionListenerProxy(InvocationHandler pHandler)
	{
		super(pHandler);
	}
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public void propertyChanged(PropertyEvent pEvent) throws Throwable 
	{
		dispatch(method("propertyChanged", PropertyEvent.class), pEvent);		
	}

	/**
	 * {@inheritDoc}
	 */
	public void callError(CallErrorEvent pEvent) 
	{
		dispatchSilent(method("callError", CallErrorEvent.class), pEvent);		
	}

	/**
	 * {@inheritDoc}
	 */
	public void connectionOpened(ConnectionEvent pEvent) 
	{
		dispatchSilent(method("callError", ConnectionEvent.class), pEvent);		
	}

	/**
	 * {@inheritDoc}
	 */
	public void connectionReOpened(ConnectionEvent pEvent) 
	{
		dispatchSilent(method("connectionReOpened", ConnectionEvent.class), pEvent);		
	}

	/**
	 * {@inheritDoc}
	 */
	public void connectionClosed(ConnectionEvent pEvent) 
	{
		dispatchSilent(method("connectionClosed", ConnectionEvent.class), pEvent);		
	}

	/**
	 * {@inheritDoc}
	 */
	public void actionCalled(CallEvent pEvent) 
	{
		dispatchSilent(method("actionCalled", CallEvent.class), pEvent);		
	}

	/**
	 * {@inheritDoc}
	 */
	public void objectCalled(CallEvent pEvent) 
	{
		dispatchSilent(method("objectCalled", CallEvent.class), pEvent);		
	}
	
}	// IConnectionListenerProxy
