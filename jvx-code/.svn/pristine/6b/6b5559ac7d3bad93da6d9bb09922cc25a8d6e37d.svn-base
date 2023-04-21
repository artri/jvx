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
 * The generic implementation of {@link IKeyListener}.
 * 
 * @author René Jahn
 */
public class IKeyListenerProxy extends AbstractListenerProxy
                               implements IKeyListener
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>IKeyListenerProxy</code>.
	 * 
	 * @param pHandler the handler
	 */
	public IKeyListenerProxy(InvocationHandler pHandler)
	{
		super(pHandler);
	}
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public void keyTyped(UIKeyEvent pEvent) throws Throwable 
	{
		dispatch(method("keyTyped", UIKeyEvent.class), pEvent);
	}

	/**
	 * {@inheritDoc}
	 */
	public void keyPressed(UIKeyEvent pEvent) throws Throwable 
	{
		dispatch(method("keyPressed", UIKeyEvent.class), pEvent);
	}

	/**
	 * {@inheritDoc}
	 */
	public void keyReleased(UIKeyEvent pEvent) throws Throwable 
	{
		dispatch(method("keyReleased", UIKeyEvent.class), pEvent);
	}

}	// IKeyListenerProxy
