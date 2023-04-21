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
 * 28.12.2018 - [JR] - creation
 */
package com.sibvisions.rad.server.http.rest.service;

/**
 * The <code>ICustomServiceGetDelegate</code> can be used to handle custom GET service action calls.
 * It's possible to register a delegation instance via {@link AdminService#register(String, String, ICustomServiceDelegate)}. 
 * 
 * @author René Jahn
 */
public interface ICustomServiceGetDelegate extends ICustomServiceDelegate
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Method definitions
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Handles a get call.
	 * 
	 * @param pService the service
	 * @param pApplicationName the application name
	 * @param pAction the action name
	 * @param pParameter the optional action parameter
	 * @return the result of the action call
	 * @throws Throwable if an error occurs
	 */
	public Object call(AbstractCustomService pService, String pApplicationName, String pAction, String pParameter) throws Throwable;
	
}	// ICustomServiceGetDelegate
