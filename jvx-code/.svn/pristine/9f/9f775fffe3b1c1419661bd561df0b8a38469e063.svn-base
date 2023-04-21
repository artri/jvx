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
 * The <code>SysOutPayloadNotificationHandler</code> is a simple {@link IPayloadNotificationHandler} and 
 * prints verification code to system out.
 * 
 * @author René Jahn
 */
public class SysOutPayloadNotificationHandler implements IPayloadNotificationHandler
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>SysOutPayloadNotificationHandler</code>.
	 */
	public SysOutPayloadNotificationHandler()
	{
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sendNotification(ISession pSession, UserInfo pUser, AccessToken pToken, String pPayload) throws Exception
	{
		System.out.println("Verification code: " + pPayload);
	}
	
}	// SysOutPayloadNotificationHandler
