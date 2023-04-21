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
 * 06.06.2010 - [JR] - creation
 * 22.09.2011 - [JR] - #475
 *                     * isAllowed(String) -> isAllowed(ISession)
 *                     * removeAccess defined
 * 23.12.2012 - [JR] - #534: changed interface because it's not needed   
 * 19.09.2014 - [JR] - #1115: introduced find   
 * 23.10.2018 - [JR] - IAccessChecker introduced             
 */
package com.sibvisions.rad.server.security;

import javax.rad.server.security.IAccessChecker;

/**
 * The <code>IAccessController</code> defines the general access to server lifecycle
 * objects. The access will be checked during session creation.
 * 
 * @author René Jahn
 */
public interface IAccessController extends IAccessChecker
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Adds access to a specific life-cycle object.
	 * 
	 * @param pLifeCycleName the name of the life-cycle object
	 */
	public void addAccess(String pLifeCycleName);
	
	/**
	 * Removes access for a specific life-cycle object.
	 *  
	 * @param pLifeCycleName the name of the life-cycle object
	 */
	public void removeAccess(String pLifeCycleName);
	
}	// IAccessController
