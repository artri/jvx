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
 * 03.02.2009 - [JR] - throws IOException
 */
package remote.net;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * The <code>IChannel</code> declares the interprocess communication
 * between <code>VMServer</code> and <code>VMConnection</code>.
 * 
 * @author René Jahn
 */
public interface IChannel
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Returns the output stream for the communication.
	 *  
	 * @return output stream
	 * @throws Throwable if the outputstream is not available
	 */
	public OutputStream getOutputStream() throws Throwable;
	
	/**
	 * Returns the input stream for the communication.
	 * 
	 * @return input stream
	 * @throws Throwable if the inputstream is not available
	 */
	public InputStream getInputStream() throws Throwable;
	
}	// IChannel
