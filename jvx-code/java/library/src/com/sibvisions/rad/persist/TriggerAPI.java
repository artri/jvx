/*
 * Copyright 2011 SIB Visions GmbH
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
 * 09.05.2011 - [JR] - creation
 */
package com.sibvisions.rad.persist;

import java.sql.Timestamp;

import javax.rad.server.ISession;
import javax.rad.server.SessionContext;

/**
 * The <code>TriggerAPI</code> class offers methods for server-side triggers.
 * 
 * @author René Jahn
 */
public final class TriggerAPI
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Invisible constructor, because the <code>TriggerAPI</code> class is a utility class.
	 */
	private TriggerAPI()
	{
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the current timestamp.
	 * 
	 * @return the current timestamp
	 */
	public static Timestamp getCurrentTimestamp()
	{
		return new Timestamp(System.currentTimeMillis());
	}
	
	/**
	 * Gets the current username from the session through the {@link SessionContext}.
	 * 
	 * @return the username or <code>null</code> if no session is active
	 */
	public static String getCurrentUserName()
	{
		ISession session = SessionContext.getCurrentSession();
		
		if (session != null)
		{
			return session.getUserName();
		}
		
		return null;
	}
	
}	// TriggerAPI
