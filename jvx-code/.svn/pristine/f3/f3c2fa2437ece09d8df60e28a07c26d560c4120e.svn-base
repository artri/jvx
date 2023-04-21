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
 * 12.12.2011 - [JR] - creation
 */
package com.sibvisions.rad.server.http.rest;

import org.junit.Before;
import org.restlet.data.ChallengeResponse;
import org.restlet.data.ChallengeScheme;

/**
 * The <code>BaseServiceTest</code> is the base class for server resource tests.
 * 
 * @author René Jahn
 */
public abstract class BaseServiceTest
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** the application name (default: demo). */
	private String sApplicationName;

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	/**
     * Sets values before each test.
     * 
     * @throws Exception if set values fails
     */
    @Before
    public void beforeTest() throws Exception
    {
    	setApplicationName("demo");
    }
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the application name.
	 * 
	 * @return the application name
	 */
	protected String getApplicationName()
	{
		return sApplicationName;
	}
	
	/**
	 * Sets the application name.
	 * 
	 * @param pName the application name
	 */
	protected void setApplicationName(String pName)
	{
		sApplicationName = pName;
	}

	/**
	 * Gets the application base URL.
	 * 
	 * @return the base URL
	 */
	protected String getBaseURL()
	{
		// see build.xml
		String sUrl = System.getProperty("com.sibvisions.rad.remote.TestHttpConnection");
		
		if (sUrl != null)
		{
			sUrl = sUrl.replace("/services/Server", "/services/rest/" + getApplicationName());
			
			if (!sUrl.endsWith("/"))
			{
				return sUrl + "/";
			}
			else
			{
				return sUrl;
			}
		}
		
		if (System.getProperty("os.name").toLowerCase().indexOf("mac") >= 0)
		{
			return "http://localhost:8081/JVx.Server/services/rest/" +  getApplicationName() + "/";
		}
		else
		{
			return "http://localhost/JVx.Server/services/rest/" +  getApplicationName() + "/";
		}
	}
	
	/**
	 * Gets the challenge response.
	 * 
	 * @return the challenge response
	 */
	protected ChallengeResponse getChallengeResponse()
	{
		return new ChallengeResponse(ChallengeScheme.HTTP_BASIC, "rene", "rene");
	}

}	// BaseServiceTest
