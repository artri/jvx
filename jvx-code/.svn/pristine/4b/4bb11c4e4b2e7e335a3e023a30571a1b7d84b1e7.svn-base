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
 * 07.10.2009 - [JR] - setPropety defined
 */
package com.sibvisions.rad.server;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Defines an object to assist a servre in sending a response to the client.
 * 
 * @author René Jahn
 */
public interface IResponse
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Returns an {@link OutputStream} suitable for writing binary data in the response.
	 * 
	 * @return an {@link OutputStream} for writing binary data
	 * @throws IOException if an input or output exception occurred
	 */
	public OutputStream getOutputStream() throws IOException;
	
	/**
	 * Sets a response property.
	 * 
	 * @param pKey the property name
	 * @param pValue the value
	 */
	public void setProperty(String pKey, Object pValue);
	
	/**
	 * Notifies that processing is finished.
	 */
	public void close();

    /**
     * Gets whether the response was closed.
     * 
     * @return <code>true</code> if response was closed, <code>false</code> if still open
     */
    public boolean isClosed();
	
}	// IResponse
