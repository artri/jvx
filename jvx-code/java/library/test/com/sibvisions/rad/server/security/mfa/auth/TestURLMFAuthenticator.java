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
 * 12.04.2022 - [JR] - creation
 */
package com.sibvisions.rad.server.security.mfa.auth;

import javax.rad.server.ISession;

import com.sibvisions.rad.server.security.UserInfo;
import com.sibvisions.rad.server.security.mfa.MFAHandler.AccessToken;

/**
 * Tests multi-factor authentication with URL.
 * 
 * @author René Jahn
 * @see AbstractURLMFAuthenticator
 */
public class TestURLMFAuthenticator extends AbstractURLMFAuthenticator 
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Link createLink(AccessToken pToken, ISession pSession, UserInfo pUser) 
	{
		String sURL = "http://www.sibvisions.com";
		
		System.out.println("URL: " + sURL);
		
		pToken.put("INDEX", Integer.valueOf(0));
		
		return new Link(sURL);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean isConfirmed(AccessToken pToken, ISession pSession) 
	{
		Integer idx = (Integer)pToken.get("INDEX");
		
		idx = Integer.valueOf(idx.intValue() + 1);
		
		pToken.put("INDEX", idx);
		
		System.out.println("URL confirmation check: " + (idx.intValue() == 3));
		
		return idx.intValue() == 3;
	}

}	//TestURLMFAuthenticator 
