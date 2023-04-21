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
 * 01.10.2008 - [JR] - creation
 */
package demo;

import javax.rad.server.SessionContext;


/**
 * User work-screen for unit tests.
 * 
 * @author René Jahn
 */
public class User extends Session
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>User</code>.
	 */
	public User()
	{
	    InstanceChecker.add(User.class);
	    
		System.out.println("CALL: User.<init>");
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Checks the mailbox.
	 */
	public void checkMail()
	{
		System.out.println("CALL: User.checkMail");
	}
	
	/**
	 * Calls the {@link #callMe()} method through the {@link SessionContext}.
	 * 
	 * @return the name stack
	 * @throws Throwable if the call fails
	 */
	public String callSub() throws Throwable
	{
		String sName = SessionContext.getCurrentInstance().getObjectName() + "." + SessionContext.getCurrentInstance().getMethodName(); 
		
		sName += " -> " + SessionContext.getCurrentSession().call(null, "callMe");
		
		return sName;
	}
	
	/**
	 * Returns the object and method name from the SessionContext.
	 * 
	 * @return SessionContext.getCurrentInstance().getObjectName() + "." + SessionContext.getCurrentInstance().getMethodName()
	 */
	public String callMe()
	{
		String sPrevious = SessionContext.getCurrentInstance().getPreviousContext().getObjectName() + "." + 
		                   SessionContext.getCurrentInstance().getPreviousContext().getMethodName(); 
		
		return "[from: " + sPrevious + "] " + SessionContext.getCurrentInstance().getObjectName() + "." + SessionContext.getCurrentInstance().getMethodName();
	}
	
}	// User
