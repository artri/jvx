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
package javax.rad.application.genui.event.type.screen;

import java.lang.reflect.InvocationHandler;

import javax.rad.application.genui.WorkScreen;
import javax.rad.util.event.proxy.impl.AbstractListenerProxy;

/**
 * The generic implementation of {@link IParameterChangedListener}.
 * 
 * @author René Jahn
 */
public class IParameterChangedListenerProxy extends AbstractListenerProxy
                                            implements IParameterChangedListener
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>IParameterChangedListenerProxy</code>.
	 * 
	 * @param pHandler the handler
	 */
	public IParameterChangedListenerProxy(InvocationHandler pHandler)
	{
		super(pHandler);
	}
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public void parameterChanged(WorkScreen pScreen, String pParameter, Object pOldValue, Object pNewValue) throws Throwable 
	{
		dispatch(method("parameterChanged", WorkScreen.class, String.class, Object.class, Object.class), pScreen, pParameter, pOldValue, pNewValue);
	}

}	// IParameterChangedListenerProxy
