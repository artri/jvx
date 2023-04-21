/*
 * Copyright 2021 SIB Visions GmbH
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
 * 14.06.2021 - [JR] - creation
 */
package com.sibvisions.rad.server.security.reset;

import javax.rad.server.ISession;

import com.sibvisions.rad.server.security.UserInfo;

/**
 * The <code>IOneTimePasswordHandler</code> is a notification interface for one-time-passwords. 
 * 
 * @author René Jahn
 */
public interface IOneTimePasswordHandler 
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Sends the one-time-password.
	 * 
	 * @param pSession the session
	 * @param pUser the user information
	 * @param pPassword the one-time-password
	 * @throws Exception if sending password fails
	 */
	public void sendOneTimePassword(ISession pSession, UserInfo pUser, String pPassword) throws Exception;
	
}	// IOneTimePasswordHandler
