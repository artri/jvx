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
package javax.rad.ui.event.type.key;

import java.lang.reflect.InvocationHandler;

import javax.rad.ui.event.UIKeyEvent;
import javax.rad.util.event.proxy.impl.AbstractListenerProxy;

/**
 * The generic implementation of {@link IKeyPressedListener}.
 * 
 * @author Ren� Jahn
 */
public class IKeyPressedListenerProxy extends AbstractListenerProxy
                                      implements IKeyPressedListener
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>IKeyPressedListenerProxy</code>.
	 * 
	 * @param pHandler the handler
	 */
	public IKeyPressedListenerProxy(InvocationHandler pHandler)
	{
		super(pHandler);
	}
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public void keyPressed(UIKeyEvent pEvent) throws Throwable 
	{
		dispatch(method("keyPressed", UIKeyEvent.class), pEvent);
    }
    
}	// IKeyPressedListenerProxy
