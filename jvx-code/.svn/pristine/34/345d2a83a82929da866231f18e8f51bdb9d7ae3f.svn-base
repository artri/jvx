/*
 * Copyright 2021 SIB Visions GmbH
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
 * 30.07.2021 - [JR] - creation
 */
package com.sibvisions.rad.ui.web.impl;

import java.io.IOException;
import java.io.InputStream;

/**
 * The <code>IPushHandler</code> defines the bridge between server-side launcher
 * and client application. It enables server-to-client communication.
 * 
 * @author René Jahn
 */
public interface IPushHandler 
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Sends an update message.
	 * 
	 * @throws IOException if sending message fails
	 */
	public void pushUpdate() throws IOException;
	
	/**
	 * Sends a message to the client application.
	 * 
	 * @param pStream the stream
	 * @throws IOException if an exception occurs
	 */
	public void push(InputStream pStream) throws IOException;
	
}	// IPushHandler
