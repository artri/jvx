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

import com.sibvisions.rad.server.GenericBean;

/**
 * Application object for unit tests.
 * 
 * @author René Jahn
 */
public class Application extends GenericBean
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** method call queue. */
	private StringBuilder sbCallQueue = new StringBuilder();

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 
	/**
	 * Creates a new instance of <code>Application</code>.
	 */
	public Application()
	{
		addCall("Application()");
	} 
	
	/**
	 * Initialization of the application property.
	 * 
	 * @return new <code>RemoteFile</code> instance
	 */
	@SuppressWarnings("unused")
	private RemoteFile initApplication()
	{
		return new RemoteFile(new File("application.xml"));
	}	
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Adds a call to the queue.
	 * 
	 * @param pCall the call statement
	 */
	protected void addCall(String pCall)
	{
		if (sbCallQueue.length() > 0)
		{
			sbCallQueue.append("\n");
		}
		
		sbCallQueue.append(pCall);
	}
	
	/**
	 * Returns the current call queue.
	 * 
	 * @return the call queue
	 */
	public String getCallQueue()
	{
		return sbCallQueue.toString();
	}

	/**
	 * Gets the remote application file.
	 *
	 * @return the remote application file
	 */
	public RemoteFile getApplication()
	{
		addCall("getApplication()");
		
		return (RemoteFile)get("application");
	}
	
}	// Application
