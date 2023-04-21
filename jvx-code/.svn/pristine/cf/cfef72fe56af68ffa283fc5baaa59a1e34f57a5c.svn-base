/*
 * Copyright 2009 SIB Visions GmbH
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
 * 25.06.2009 - [HM] - creation
 */
package javax.rad.util;


/**
 * The <code>RuntimeEventHandler</code> is a <code>EventHandler</code> that 
 * handles Events, and informs the ExceptionHandler. 
 * 
 * @author Martin Handsteiner
 * 
 * @param <L> the Listener type
 */
public class RuntimeEventHandler<L> extends EventHandler<L>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Constructs a new EventHandler, the listener type may only have 1 method.
	 *  
	 * @param pListenerType the listener type interface.
	 * @param pParameterTypes parameter types to check additional.
	 */
	public RuntimeEventHandler(Class<L> pListenerType, Class... pParameterTypes)
	{
		super(pListenerType, null, pParameterTypes);
	}
	
	/**
	 * Constructs a new EventHandler.
	 *  
	 * @param pListenerType the listener type interface.
	 * @param pListenerMethodName the method to be called inside the interface.
	 * @param pParameterTypes parameter types to check additional.
	 */
	public RuntimeEventHandler(Class<L> pListenerType, String pListenerMethodName, Class... pParameterTypes)
	{
		super(pListenerType, pListenerMethodName, pParameterTypes);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object dispatchEvent(Object... pEventParameter)
	{
		try
		{
			return super.dispatchEvent(pEventParameter);
		}
		catch (Throwable pThrowable)
		{
			ExceptionHandler.raise(pThrowable);
			return null;
		}
	}
	
}	// RuntimeEventHandler
