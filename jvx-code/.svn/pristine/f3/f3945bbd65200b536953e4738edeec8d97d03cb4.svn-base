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
 * 14.10.2010 - [JR] - #185: addUrl/removeUrl added
 */
package com.sibvisions.rad.remote.http;

import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * The <code>HttpsHostnameVerifier</code> is a {@link HostnameVerifier} without
 * restrictions.
 * 
 * @author René Jahn
 */
public class HttpsHostnameVerifier implements HostnameVerifier
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public boolean verify(String pHostname, SSLSession pSession) 
	{
		//true is not bad here, because we got the same hostname as used as for the server URL
		//if someone changes the connect hostame then he has a special JVx server
		return true;
	}
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	

	/**
	 * Adds an URL to the list of allowed connections. 
	 * 
	 * @param pUrl the connection URL
	 */
	public void addUrl(URL pUrl)
	{
	}
	
	/**
	 * Removes an URL from the list of allowed connections.
	 * 
	 * @param pUrl the connection URL
	 */
	public void removeUrl(URL pUrl)
	{
	}
	
}	// HttpsHostnameVerifier
