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
 * 06.05.2009 - [JR] - creation
 * 27.01.2010 - [JR] - invoke defined
 */
package javax.rad.server;

/**
 * An <code>AbstractObjectProvider</code> handles the access to the life-cycle 
 * objects for all sessions. A life-cycle object holds references to
 * the server-side objects which are available for the client within a
 * specific session.
 * 
 * @author René Jahn
 */
public abstract class AbstractObjectProvider
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the associated server. */
	private IServer server;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>AbstractObjectProvider</code> for a specific
	 * {@link IServer}.
	 * 
	 * @param pServer the server
	 */
	protected AbstractObjectProvider(IServer pServer)
	{
		server = pServer;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Returns an object from the life-cycle object container for an {@link ISession}.
	 * 
	 * @param pSession the session
	 * @param pObjectName the object name (can be an EL)
	 * @return the object for the session or <code>null</code> for the life-cycle object container
	 * @throws Throwable if the life-cycle object is not available
	 */
	public abstract Object getObject(ISession pSession, String pObjectName) throws Throwable;
	
	/**
	 * Puts an object to the life-cycle object container for an {@link ISession}.
	 * 
	 * @param pSession the session
	 * @param pObjectName the object name
	 * @param pObject the object or <code>null</code> to remove the object
	 * @return <code>null</code> if there was no object under the specified name, otherwise the previous object
	 *         for the specified name
	 * @throws Throwable if the life-cycle object is not available
	 */
	public abstract Object putObject(ISession pSession, String pObjectName, Object pObject) throws Throwable;
	
	/**
	 * Invokes a method from a specific life-cycle object.
	 * 
	 * @param pSession the session
	 * @param pObjectName the object name
	 * @param pMethodName the method to invoke
	 * @param pParams the method parameters
	 * @return the return value of the invoked method
	 * @throws Throwable if the life-cycle object is not available or the method was not found 
	 */
	public abstract Object invoke(ISession pSession, String pObjectName, String pMethodName, Object... pParams) throws Throwable;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the associated {@link IServer}.
	 * 
	 * @return the server
	 */
	public IServer getServer()
	{
		return server;
	}
	
    /**
     * Gets the classloader for loading LCOs.
     * 
     * @param pSession the session
     * @return the class loader or <code>null</code> to use the default class loader
     */
    public ClassLoader getClassLoader(ISession pSession)
    {
    	return null;
    }
	
}	// AbstractObjectProvider
