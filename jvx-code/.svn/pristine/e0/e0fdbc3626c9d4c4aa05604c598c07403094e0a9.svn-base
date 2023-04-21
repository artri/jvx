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
 * 14.11.2009 - [JR] - creation
 */
package com.sibvisions.rad.server.security.validation;

import javax.rad.server.ISession;

/**
 * The <code>IPasswordValidator</code> will be used from the AbstractSecurityManager and sub classes
 * to validate if the new password during password change is strength enough.
 * 
 * @author René Jahn
 */
public interface IPasswordValidator
{
	/**
	 * Checks if a password is strength enough to be used.
	 * 
	 * @param pSession the session which wants to change the password
	 * @param pPassword the new password
	 * @throws Exception if the password is not valid because it violates the password rules
	 */
	public void checkPassword(ISession pSession, String pPassword) throws Exception;
	
}	// IPasswordValidator
