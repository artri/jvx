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
 * 03.12.2008 - [JR] - creation
 */
package javax.rad.util.event;

import java.io.Serializable;

/**
 * The <code>IExceptionListener</code> will be used to handle program 
 * exceptions. They can occur in any place of the program and will be
 * handled at the desired place of the program.
 * 
 * @author René Jahn
 */
public interface IExceptionListener extends Serializable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Notification for the listener to handle an exception occured in
	 * another part of the program.
	 * 
	 * @param pThrowable the occured exception
	 */
	public void handleException(Throwable pThrowable);
	
}	// IExceptionListener
