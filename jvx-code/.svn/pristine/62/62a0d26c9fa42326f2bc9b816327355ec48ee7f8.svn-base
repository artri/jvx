/*
 * Copyright 2013 SIB Visions GmbH
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
 * 31.03.2013 - [JR] - creation
 */
package com.sibvisions.rad.application.event;

import javax.rad.util.RuntimeEventHandler;

/**
 * The <code>ErrorHandler</code> is a <code>RuntimeEventHandler</code> that 
 * handles <code>IErrorListener</code>. 
 * 
 * @author René Jahn
 */
public class ErrorHandler extends RuntimeEventHandler<IErrorListener>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Constructs a new ErrorHandler.
	 *  
	 * @param pListenerMethodName the method to be called inside the interface.
	 */
	public ErrorHandler(String pListenerMethodName)
	{
		super(IErrorListener.class, pListenerMethodName);
	}

}	// ErrorHandler
