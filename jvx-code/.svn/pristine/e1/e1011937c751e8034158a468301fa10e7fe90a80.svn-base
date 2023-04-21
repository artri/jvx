/*
 * Copyright 2022 SIB Visions GmbH
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
 * 01.04.2022 - [JR] - creation
 */
package com.sibvisions.rad.server.security.mfa.auth;

import javax.rad.server.ISession;

import com.sibvisions.rad.server.security.UserInfo;
import com.sibvisions.rad.server.security.mfa.MFAHandler.AccessToken;

/**
 * The <code>IPayloadNotificationHandler</code> is a notification interface for text input MFA. 
 * 
 * @author René Jahn
 */
public interface IPayloadNotificationHandler 
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Sends the validation code.
	 * 
	 * @param pSession the session
	 * @param pUser the user information
	 * @param pToken the access token
	 * @param pPayload the payload
	 * @throws Exception if sending payload fails
	 */
	public void sendNotification(ISession pSession, UserInfo pUser, AccessToken pToken, String pPayload) throws Exception;
	
}	// IPayloadNotificationHandler
