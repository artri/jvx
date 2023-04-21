/*
 * Copyright 2010 SIB Visions GmbH
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
 * 28.12.2010 - [JR] - creation
 */
package javax.rad.application.genui.event;

import javax.rad.util.RuntimeEventHandler;

/**
 * The <code>RemoteApplicationHandler</code> is a <code>RuntimeEventHandler</code> that 
 * handles <code>IRemoteApplicationListener</code>. 
 * 
 * @author René Jahn
 * 
 * @param <L> the Listener type
 */
public class RemoteApplicationHandler<L> extends RuntimeEventHandler<L>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Constructs a new RemoteApplicationHandler.
	 *  
	 * @param pClass the listener class
	 */
	public RemoteApplicationHandler(Class<L> pClass)
	{
		super(pClass);
	}

}	// RemoteApplicationHandler
