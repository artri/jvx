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
 * 06.04.2022 - [JR] - creation
 */
package com.sibvisions.rad.server.security.mfa.auth;

import javax.rad.server.ISession;

import com.sibvisions.rad.server.security.mfa.MFAHandler.AccessToken;

/**
 * The <code>IWaitNotificationHandler</code> is a notification interface for a "wait" MFA. 
 * 
 * @author René Jahn
 */
public interface IWaitNotificationHandler extends IPayloadNotificationHandler
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Checks if the authentication is confirmed.
	 * 
	 * @param pSession the session
	 * @param pToken the access token
	 * @param pPayload the payload
	 * @return <code>true</code> if authentication is confirmed, <code>false</code> otherwise
	 */
	public boolean isConfirmed(ISession pSession, AccessToken pToken, String pPayload);
	
}	// IWaitNotificationHandler
