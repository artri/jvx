/*
 * Copyright 2011 SIB Visions GmbH
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
 * 16.09.2011 - [JR] - creation
 */
package javax.rad.remote.event;

/**
 * The <code>PropertyEvent</code> class is the event for property changes.
 * 
 * @author René Jahn
 */
public class PropertyEvent
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the property name. */
	private String sName;
	
	/** the old value. */
	private Object oOld;
	
	/** the new value. */
	private Object oNew;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>PropertyEvent</code> with the given event information.
	 * 
	 * @param pName the property name
	 * @param pOld the old property value
	 * @param pNew the new property value
	 */
	public PropertyEvent(String pName, Object pOld, Object pNew)
	{
		sName = pName;
		oOld  = pOld;
		oNew  = pNew;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the property name.
	 * 
	 * @return the property name
	 */
	public String getPropertyName()
	{
		return sName;
	}
	
	/**
	 * Gets the old value.
	 * 
	 * @return the old value
	 */
	public Object getOldValue()
	{
		return oOld;
	}
	
	/**
	 * Gets the new value.
	 * 
	 * @return the new value
	 */
	public Object getNewValue()
	{
		return oNew;
	}
	
}	// PropertyEvent
