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
 */
package com.sibvisions.rad.server.security.ntlm;

import jcifs.UniAddress;

/**
 * The <code>Session</code> holds the challenge and domain controller
 * for later authentication.
 * 
 * @author René Jahn
 */
public final class NtlmSession
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** the challenge. */
	private byte[] byChallenge;
	
	/** the domain controller. */
	private UniAddress uaDomainController;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of <code>Session</code> with a domain controller
	 * and a challenge.
	 * 
	 * @param pDomainController the domain controller
	 * @param pChallenge the challenge
	 */
	NtlmSession(UniAddress pDomainController, byte[] pChallenge)
	{
		uaDomainController = pDomainController;
		byChallenge = pChallenge;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the challenge.
	 * 
	 * @return the challenge
	 */
	public final byte[] getChallenge()
	{
		return byChallenge;
	}
	
	/**
	 * Gets the domain controller.
	 * 
	 * @return the domain controller
	 */
	public final UniAddress getDomainController()
	{
		return uaDomainController;
	}
	
}	// NtlmSession
