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
 */
package com.sibvisions.rad.remote;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * The <code>ISerializer</code> interface defines standard methods for serialization
 * and deserialization of objects to and from streams. An implementation of <code>ISerializer</code>
 * will be needed for connections to the remote server.<p>
 * An <code>ISerializer</code> implementation can encrypt the communication between client
 * and server or can optimize the traffic.
 *  
 * @author René Jahn
 */
public interface ISerializer
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Reads in a serialized object from a stream.
	 * 
	 * @param pIn stream with serialized content
	 * @return deserialized object
	 * @throws Exception if deserialization fails
	 */
	public Object read(DataInputStream pIn) throws Exception;
	
	/**
	 * Writes a serialized object to a stream.
	 * 
	 * @param pOut output stream for the object
	 * @param pObject serializable object
	 * @throws Exception if serialization fails
	 */
	public void write(DataOutputStream pOut, Object pObject) throws Exception;
	
}	// ISerializer
