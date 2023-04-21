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
 * The <code>SysOutWaitNotificationHandler</code> is a simple {@link IWaitNotificationHandler} and 
 * prints payload to system out.
 * 
 * @author René Jahn
 */
public class SysOutWaitNotificationHandler implements IWaitNotificationHandler
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** the confirmation counter. */
	private int iConfirmCount = 0;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>SysOutWaitNotificationHandler</code>.
	 */
	public SysOutWaitNotificationHandler()
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
		System.out.println("Payload: " + pPayload);
		
		iConfirmCount = 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isConfirmed(ISession pSession, AccessToken pToken, String pPayload) 
	{
		iConfirmCount++;
		
		System.out.println("Payload confirm check: " + pPayload + " = " + (iConfirmCount == 3));
		
		return iConfirmCount == 3;
	}
	
}	// SysOutWaitNotificationHandler
