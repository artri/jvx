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
 * 04.04.2009 - [JR] - creation
 */
package javax.rad.remote;

import com.sibvisions.util.ChangedHashtable;

/**
 * The <code>ConnectionInfo</code> holds all important connection information:
 * <ul>
 * <li>the connection identifier</li>
 * <li>the current connection properties</li>
 * <li>the last call time</li>
 * </ul>
 * <p>
 * The information will be needed to communicate with any server through an
 * <code>IConnection</code> implementation.
 * 
 * @author René Jahn
 * @see IConnection
 */
public class ConnectionInfo
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the connection identifier. */
	private Object oConnectionId;
	
	/** the current connection properties. */
	private ChangedHashtable<String, Object> chtProperties;
	
	/** the last call time. */
	private long lLastCallTime = -1;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>ConnectionInfo</code>.
	 */
	public ConnectionInfo()
	{
		chtProperties = new ChangedHashtable<String, Object>();
	}
	
	/**
	 * Creates a new instance of <code>ConnectionInfo</code> with predefined
	 * connection properties.
	 * 
	 * @param pProperties the predefined connection properties
	 */
	public ConnectionInfo(ChangedHashtable<String, Object> pProperties)
	{
		chtProperties = pProperties;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Sets the connection identifier.
	 * 
	 * @param pConnectionId the connection identifier
	 */
	public void setConnectionId(Object pConnectionId)
	{
		this.oConnectionId = pConnectionId;
	}

	/**
	 * Gets the connection identifier.
	 * 
	 * @return the connection identifier or <code>null</code> if the connection
	 *         is not established
	 */
	public Object getConnectionId()
	{
		return oConnectionId;
	}

	/**
	 * Gets the current connection properties.
	 * 
	 * @return the current connection properties
	 */
	public ChangedHashtable<String, Object> getProperties()
	{
		return chtProperties;
	}

	/**
	 * Sets the time in millis when the last call was sent to the server.
	 * 
	 * @param pTime the time in millis
	 */
	public void setLastCallTime(long pTime)
	{
		lLastCallTime = Math.max(lLastCallTime, pTime);
	}

	/**
	 * Gets the time in millis when the last call was sent to the server.
	 *
	 * @return the time in millis, or -1 if no call was sent
	 */
	public long getLastCallTime()
	{
		return lLastCallTime;
	}
	
}	// ConnectionInfo

