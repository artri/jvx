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
 * The generic implementation of {@link IWindowListener}.
 * 
 * @author René Jahn
 */
public class IWindowListenerProxy extends AbstractListenerProxy
                                  implements IWindowListener
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>IWindowListenerProxy</code>.
	 * 
	 * @param pHandler the handler
	 */
	public IWindowListenerProxy(InvocationHandler pHandler)
	{
		super(pHandler);
	}
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public void windowOpened(UIWindowEvent pEvent) throws Throwable 
	{
		dispatch(method("windowOpened", UIWindowEvent.class), pEvent);		
	}

	/**
	 * {@inheritDoc}
	 */
	public void windowClosing(UIWindowEvent pEvent) throws Throwable 
	{
		dispatch(method("windowClosing", UIWindowEvent.class), pEvent);		
	}

	/**
	 * {@inheritDoc}
	 */
	public void windowClosed(UIWindowEvent pEvent) throws Throwable 
	{
		dispatch(method("windowClosed", UIWindowEvent.class), pEvent);		
	}

	/**
	 * {@inheritDoc}
	 */
	public void windowIconified(UIWindowEvent pEvent) throws Throwable 
	{
		dispatch(method("windowIconified", UIWindowEvent.class), pEvent);		
	}

	/**
	 * {@inheritDoc}
	 */
	public void windowDeiconified(UIWindowEvent pEvent) throws Throwable 
	{
		dispatch(method("windowDeiconified", UIWindowEvent.class), pEvent);		
	}

	/**
	 * {@inheritDoc}
	 */
	public void windowActivated(UIWindowEvent pEvent) throws Throwable 
	{
		dispatch(method("windowActivated", UIWindowEvent.class), pEvent);		
	}

	/**
	 * {@inheritDoc}
	 */
	public void windowDeactivated(UIWindowEvent pEvent) throws Throwable 
	{
		dispatch(method("windowDeactivated", UIWindowEvent.class), pEvent);		
	}
	
}	// IWindowListenerProxy
