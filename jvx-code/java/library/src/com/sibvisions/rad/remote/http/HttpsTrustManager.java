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
 * 14.10.2010 - [JR] - #185: addUrl, removeUrl added
 */
package com.sibvisions.rad.remote.http;

import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**
 * The <code>HttpsTrustManager</code> is a {@link X509TrustManager} without
 * restrictions.
 * 
 * @author René Jahn
 */
public class HttpsTrustManager implements X509TrustManager
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public void checkClientTrusted(X509Certificate[] pChain, String pAuthType) throws CertificateException 
	{
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void checkServerTrusted(X509Certificate[] pChain, String pAuthType) throws CertificateException 
	{
	}

	/**
	 * {@inheritDoc}
	 */
	public X509Certificate[] getAcceptedIssuers() 
	{
		return null;
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
	 * Removes an URL from te list of allowed connections.
	 * 
	 * @param pUrl the connection URL
	 */
	public void removeUrl(URL pUrl)
	{
	}	
	
}	// HttpsTrustManager
