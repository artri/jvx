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
 * 11.12.2020 - [JR] - creation
 */
package javax.rad.util;

/**
 * The <code>RootCauseSecurityException</code> is a simple {@link SecurityException} wrapper and not more. A root cause exception
 * defines that the wrapped {@link Throwable} is the root cause. Usually, the first Exception is the
 * root cause.
 * 
 * @author René Jahn
 */
public class RootCauseSecurityException extends SecurityException 
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>RootCauseSecurityException</code> with given message.
	 * 
	 * @param pMessage the message
	 */
	public RootCauseSecurityException(String pMessage)
	{
		super(pMessage);
	}

	/**
	 * Creates a new instance of <code>RootCauseSecurityException</code> with given cause.
	 * 
	 * @param pCause the wrapped cause
	 */
	public RootCauseSecurityException(Throwable pCause)
	{
		super(pCause);
	}
	
	/**
	 * Creates a new instance of <code>RootCauseSecurityException</code> with given cause and message.
	 * 
	 * @param pMessage the message
	 * @param pCause the wrapped cause
	 */
	public RootCauseSecurityException(String pMessage, Throwable pCause)
	{
		super(pMessage, pCause);
	}
	
}	// RootCauseSecurityException
