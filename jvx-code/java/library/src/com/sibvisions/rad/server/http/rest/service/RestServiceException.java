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
 * 23.07.2018 - [JR] - creation
 */
package com.sibvisions.rad.server.http.rest.service;

import javax.rad.type.bean.IBean;

/**
 * The <code>RestServiceException</code> is the custom {@link Exception} for REST service calls.
 * 
 * @author René Jahn
 */
public class RestServiceException extends Exception 
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** the status code. */
	private int iStatus;
	
	/** the details bean. */
	private IBean details;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of <code>RestServiceException</code> for the given status code.
	 * 
	 * @param pStatus the status code
	 */
	public RestServiceException(int pStatus)
	{
		super();
		
		iStatus = pStatus;
	}
	
	/**
	 * Creates a new instance of <code>RestServiceException</code> for the given status code and details.
	 * 
	 * @param pStatus the status code
	 * @param pDetails additional error details
	 */
	public RestServiceException(int pStatus, IBean pDetails)
	{
		super();
		
		iStatus = pStatus;
		details = pDetails;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the status code.
	 * 
	 * @return the status code
	 */
	public int getStatus()
	{
		return iStatus;
	}			
	
	/**
	 * Gets the details.
	 * 
	 * @return the details
	 */
	public IBean getDetails()
	{
		return details;
	}
	
}	// RestServiceException
