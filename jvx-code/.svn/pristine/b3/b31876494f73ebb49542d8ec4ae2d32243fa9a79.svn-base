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
 * 07.10.2009 - [JR] - getProperty defined
 */
package com.sibvisions.rad.server;

import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

/**
 * Defines an object to provide client request.
 * 
 * @author René Jahn
 */
public interface IRequest
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constants
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the property name for the authentication type. */
	public static final String PROP_AUTHENTICATION_TYPE = "authtype";
	
	/** the property name for the remote host address. */
	public static final String PROP_REMOTE_ADDRESS = "remoteaddr";
	
	/** the property name for the remote hostname. */
	public static final String PROP_REMOTE_HOST = "remotehost";
	
	/** the property name for the remote user. */
	public static final String PROP_REMOTE_USER = "remoteuser";
	
	/** the property name for cookies. */
	public static final String PROP_COOKIE = "cookie";

	/** the property name for request uri. */
	public static final String PROP_URI = "uri";

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Retrieves the binary data of the request as {@link InputStream}.
	 * 
	 * @return a {@link InputStream} containing the request
	 * @throws IOException if an input or output exception occurred
	 */
	public InputStream getInputStream() throws IOException;

	/**
	 * Gets the value of a request property.
	 * 
	 * @param pKey the property name
	 * @return the value or <code>null</code> if the property is not available
	 */
	public Object getProperty(String pKey);
	
	/**
	 * Returns properties from the request.
	 * 
	 * @return the access properties or null if there are no acces properties
	 */
	public Hashtable<String, Object> getProperties();
	
	/**
	 * Notifies that processing is finished.
	 */
	public void close();
	
	/**
	 * Gets whether the request was closed.
	 * 
	 * @return <code>true</code> if request was closed, <code>false</code> if still open
	 */
	public boolean isClosed();
	
}	// IRequest
