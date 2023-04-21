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
 * 01.10.2008 - [JR] - creation
 * 08.05.2009 - [JR] - moved to javax.rad from com.sibvisions
 */
package javax.rad.server.event;

import javax.rad.server.ISession;

/**
 * The listener interface for receiving session based events.
 * 
 * @author René Jahn
 */
public interface ISessionListener
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Invoked when a session was created and is ready to use.
	 * 
	 * @param pSession the newly created session
	 */
	public void sessionCreated(ISession pSession);
	
	/**
	 * Invoked when a session was destroyed an can not be used anymore.
	 * 
	 * @param pSession the destroyed session
	 */
	public void sessionDestroyed(ISession pSession);
	
}	// ISessionListener
