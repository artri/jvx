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
 * 05.01.2022 - [JR] - creation
 */
package com.sibvisions.rad.server.security;

import javax.rad.type.bean.Bean;

/**
 * The <code>UserInfo</code> class is a bean which contains relevant and additional information about a user.
 * This object will be used to reset the password of a user.
 * 
 * @author René Jahn
 */
public class UserInfo extends Bean 
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the property name for the username. */
	public static final String USERNAME = "USERNAME";
	
	/** the property name for the email address. */
	public static final String EMAIL = "EMAIL";
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Sets the username.
	 * 
	 * @param pName the username
	 */
	public void setUserName(String pName)
	{
		put(USERNAME, pName);
	}
	
	/**
	 * Gets the username.
	 * 
	 * @return the username
	 */
	public String getUserName()
	{
		return (String)get(USERNAME);
	}
	
	/**
	 * Sets the email address.
	 * 
	 * @param pEmail the email address
	 */
	public void setEmailAddress(String pEmail)
	{
		put(EMAIL, pEmail);
	}
	
	/**
	 * Gets the email address.
	 * 
	 * @return the email address
	 */
	public String getEmailAddress()
	{
		return (String)get(EMAIL);
	}
	
	/**
	 * Gets the unique identifier for this user info instance. 
	 * This is usually the username.
	 * 
	 * @return the unique identifier
	 */
	public String getUniqueIdentifier()
	{
		return (String)get(USERNAME);
	}
	
}	// UserInfo
