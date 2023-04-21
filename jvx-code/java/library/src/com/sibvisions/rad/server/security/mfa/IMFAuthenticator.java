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
 * 29.03.2022 - [JR] - creation
 */
package com.sibvisions.rad.server.security.mfa;

import javax.rad.server.ISession;

import com.sibvisions.rad.remote.mfa.IMFAConstants;
import com.sibvisions.rad.server.security.UserInfo;

/**
 * The <code>IMFAuthenticator</code> handles multi-factor authentication.
 * 
 * @author René Jahn
 */
public interface IMFAuthenticator extends IMFAConstants
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Initializes multi-factor authentication.
	 * 
	 * @param pSession the starting session
	 * @param pUser the user information
	 */
	public void init(ISession pSession, UserInfo pUser);
	
	/**
	 * Validates multi-factor authentication.
	 * 
	 * @param pSession the session
	 * @return <code>true</code> if validation is successful
	 */
	public boolean validate(ISession pSession);
	
}	// IMFAuthenticator
