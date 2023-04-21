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
 * 03.11.2008 - [JR] - creation
 * 12.12.2008 - [JR] - added the valid time
 */
package com.sibvisions.rad.server.security.ntlm;

import jcifs.smb.NtlmPasswordAuthentication;

/**
 * The <code>NtlmAuthInfo</code> holds the ntlm relevant objects for an
 * authenticated client.
 * 
 * @author René Jahn
 */
public final class NtlmAuthInfo
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the ntlm password authentication. */
	private NtlmPasswordAuthentication ntlmAuth;
	
	/** the used domain controller. */
	private NtlmSession session;
	
	/** the creation time of this object. */
	private long lCreation = System.currentTimeMillis();
	
	/** The validity in millis. */
	private int iValidTime;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new <code>NtlmAuthInfo</code>.
	 * 
	 * @param pNtlmAuth the authentication credentials
	 * @param pSession the session used for authentication
	 * @param pValidTime the validity in millis
	 */
	NtlmAuthInfo(NtlmPasswordAuthentication pNtlmAuth, NtlmSession pSession, int pValidTime)
	{
		ntlmAuth = pNtlmAuth;
		session  = pSession;
		
		iValidTime = pValidTime;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the ntlm password authentication object.
	 * 
	 * @return the authentication object
	 */
	public final NtlmPasswordAuthentication getNtlmAuth()
	{
		return ntlmAuth;
	}
	
	/**
	 * Gets the session.
	 * 
	 * @return the session used for authentication
	 */
	public final NtlmSession getSession()
	{
		return session;
	}
	
	/**
	 * Checks if the authentication information is valid and usable.
	 * 
	 * @return <code>true</code> if the authentication information can be used, 
	 *         <code>false</code> otherwise
	 */
	public final boolean isValid()
	{
		return lCreation + iValidTime > System.currentTimeMillis();
	}

}	// NtlmAuthInfo
