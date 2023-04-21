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
package democopy;

import java.io.File;

import remote.RemoteFile;

/**
 * Session object for unit tests.
 * 
 * @author René Jahn
 */
public class Session extends Application
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>Session</code>.
	 */
	@SuppressWarnings("unused")
	public Session()
	{
		//Unit test -> Session Ersetzung testen
		Session s;
		
		addCall("Session()");
	}
	
	/**
	 * Initialization of the logfile property.
	 * 
	 * @return new <code>RemoteFile</code> instance
	 */
	@SuppressWarnings("unused")
	private RemoteFile initRemoteFile()
	{
		return new RemoteFile(new File("session.xml"));
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Returns "delayedGetData()" after 3 seconds.
	 * 
	 * @return "delayedGetData" 
	 * @throws InterruptedException if the delay was interrupted
	 */
	public String delayedGetData() throws InterruptedException
	{
		addCall("delayedGetData()");

		Thread.sleep(3000);
		
		return "delayedGetData()";
	}
	
	/**
	 * Throws an Exception after 3 seconds.
	 * 
	 * @throws Exception always after 3 seconds or if the delay was interrupted
	 */
	public void delayedException() throws Exception
	{
		addCall("delayedException()");
		
		Thread.sleep(3000);
		
		throw new Exception("DELAYED_EXCEPTION");
	}
	
	/**
	 * Gets the remote log file.
	 * 
	 * @return the remote log file
	 */
	public RemoteFile getRemoteFile()
	{
		addCall("getRemoteFile()");

		return (RemoteFile)get("remoteFile");
	}

}	// Session
