/*
 * Copyright 2018 SIB Visions GmbH
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
 * 23.10.2018 - [JR] - creation            
 */
package javax.rad.server.security;

/**
 * The <code>IAccessChecker</code> defines the general access check for lifecycle
 * objects.
 * 
 * @author René Jahn
 */
public interface IAccessChecker
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Checks the access for creating a session.
	 * 
	 * @param pLifeCycleName the name of the life-cycle object
	 * @return <code>true</code> if it's allowed to open the connection, otherwise <code>false</code>
	 */
	public boolean isAllowed(String pLifeCycleName);
	
	/**
	 * Search all available lifecycle objects for the given name.
	 * 
	 * @param pLoader the class loader or <code>null</code> to use the default class loader
	 * @param pName the object name (full qualified or not)
	 * @return the full qualified lifecycle object name or <code>null</code> if no lifecycle object
	 *         name was found for the given object name
	 */
	public String find(ClassLoader pLoader, String pName);
	
}	// IAccessChecker
