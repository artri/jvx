/*
 * Copyright 2020 SIB Visions GmbH
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
 * 22.09.2020 - [JR] - creation
 */
package com.sibvisions.rad.application;

import javax.rad.util.RootCauseException;

/**
 * A {@link ClientException} should be used in application code, if it's necessary to
 * show a fixed exception with root cause in details.
 * 
 * @author René Jahn
 */
public class ClientException extends RootCauseException 
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>ClientException</code> with given message.
	 * 
	 * @param pMessage the message
	 */
	public ClientException(String pMessage)
	{
		super(pMessage);
	}

	/**
	 * Creates a new instance of <code>ClientException</code> with given cause.
	 * 
	 * @param pCause the wrapped cause
	 */
	public ClientException(Throwable pCause)
	{
		super(pCause);
	}
	
	/**
	 * Creates a new instance of <code>ClientException</code> with given cause and a custom message.
	 * 
	 * @param pNewMessage the new message
	 * @param pCause the wrapped cause
	 */
	public ClientException(String pNewMessage, Throwable pCause)
	{
		super(pNewMessage, pCause);
	}
	
}	// ClientException
