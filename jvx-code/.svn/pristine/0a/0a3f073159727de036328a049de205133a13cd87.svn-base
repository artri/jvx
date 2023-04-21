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
 * 26.05.2021 - [JR] - creation
 */
package javax.rad.server.event;

import java.util.Hashtable;

import javax.rad.server.ISession;

import com.sibvisions.util.ChangedHashtable;

/**
 * The <code>FailedSessionEvent</code> is used for notification about session creation failures.
 * 
 * @author René Jahn
 */
public class FailedSessionEvent 
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the session class type. */
	private Class<? extends ISession> clazz;
	
	/** the cause. */
	private Throwable cause;
	
	/** the properties used for session creation. */
	private ChangedHashtable<String, Object> properties;
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>FailedSessionEvent</code>.
	 * 
	 * @param pClass the session class
	 * @param pCause the failure cause
	 * @param pProperties the properties used for session creation
	 */
	public FailedSessionEvent(Class<? extends ISession> pClass, Throwable pCause, ChangedHashtable<String, Object> pProperties)
	{
		clazz = pClass;
		cause = pCause;
		properties = pProperties;
	}

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the expected class type of session.
	 * 
	 * @return the class type
	 */
	public Class<? extends ISession> getSessionType()
	{
		return clazz;
	}
	
	/**
	 * Gets the failure cause.
	 * 
	 * @return the cause
	 */
	public Throwable getCause()
	{
		return cause;
	}
	
	/**
	 * Gets the value of a property.
	 * 
	 * @param pName the property name
	 * @return the value of the property or <code>null</code> if the property is not available
	 */
	public Object getProperty(String pName)
	{
		return properties.get(pName);
	}
	
	/**
	 * Gets all properties.
	 * 
	 * @return a {@link Hashtable} with property names and values
	 */
	public Hashtable<String, Object> getProperties()
	{
		//return a copy to avoid direct manipulation
		return new Hashtable<String, Object>(properties);
	}

}	// FailedSessionEvent
