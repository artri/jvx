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
package javax.rad.ui.event;

import java.lang.reflect.InvocationHandler;

import javax.rad.util.event.proxy.impl.AbstractListenerProxy;

/**
 * The generic implementation of {@link ITabsetListener}.
 * 
 * @author René Jahn
 */
public class ITabsetListenerProxy extends AbstractListenerProxy
                                 implements ITabsetListener
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>ITabsetListenerProxy</code>.
	 * 
	 * @param pHandler the handler
	 */
	public ITabsetListenerProxy(InvocationHandler pHandler)
	{
		super(pHandler);
	}
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public void tabClosed(UITabsetEvent pTabsetEvent) throws Throwable 
	{
		dispatch(method("tabClosed", UITabsetEvent.class), pTabsetEvent);
	}

	/**
	 * {@inheritDoc}
	 */
	public void tabMoved(UITabsetEvent pTabsetEvent) throws Throwable 
	{
		dispatch(method("tabMoved", UITabsetEvent.class), pTabsetEvent);
	}

	/**
	 * {@inheritDoc}
	 */
	public void tabActivated(UITabsetEvent pTabsetEvent) throws Throwable 
	{
		dispatch(method("tabActivated", UITabsetEvent.class), pTabsetEvent);	
		}

	/**
	 * {@inheritDoc}
	 */
	public void tabDeactivated(UITabsetEvent pTabsetEvent) throws Throwable 
	{
		dispatch(method("tabDeactivated", UITabsetEvent.class), pTabsetEvent);	
	}

}	// ITabsetListenerProxy
