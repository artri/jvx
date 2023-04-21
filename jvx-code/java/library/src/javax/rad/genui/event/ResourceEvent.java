/*
 * Copyright 2015 SIB Visions GmbH
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
 * 15.01.2015 - [JR] - creation
 */
package javax.rad.genui.event;

import javax.rad.genui.UIResource;

/**
 * The <code>ResourceEvent</code> contains information about changes in the 
 * {@link UIResource}.
 * 
 * @author René Jahn
 */
public class ResourceEvent
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** the event type. */
    public enum EventType
    {
        /** Parameter event. */
        Parameter,
        /** Object event. */
        Object
    }
    
    /** the event resource. */
    private UIResource resource;
    
    /** the event type. */
    private EventType type;
    
    /** the object/attribute name. */
    private String sName;
    
    /** the old value. */
    private Object oOld;
    
    /** the new value. */
    private Object oNew;
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>ResourceEvent</code>.
	 *
	 * @param pResource the event resource
	 * @param pType the event type
	 * @param pName the attribute name
	 * @param pOld the old value
	 * @param pNew the new value
	 */
	public ResourceEvent(UIResource pResource, EventType pType, String pName, Object pOld, Object pNew)
	{
	    resource = pResource;
	    type = pType;
	    sName = pName;
	    oOld = pOld;
	    oNew = pNew;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the UI resource.
	 * 
	 * @return the resource
	 */
	public UIResource getResource()
	{
	    return resource;
	}
	
	/**
	 * Gets the event type.
	 * 
	 * @return the type
	 */
	public EventType getEventType()
	{
	    return type;
	}
	
	/**
	 * Gets the parameter/object name.
	 * 
	 * @return the name
	 */
	public String getName()
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
	
}	// ResourceEvent
