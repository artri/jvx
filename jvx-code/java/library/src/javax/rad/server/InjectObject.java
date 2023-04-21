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
 * 11.05.2009 - [JR] - creation
 */
package javax.rad.server;

/**
 * The <code>InjectObject</code> is a POJO which holds an object with
 * a specific name. The <code>InjectObject</code> will be used to
 * add any objects to the {@link SessionContext}.
 * 
 * @author René Jahn
 * @see SessionContext
 */
public class InjectObject
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the inject name for the object. */
	private String name;
	
	/** the injectec object. */
	private Object object;
	
	/** whether the object should be handled as external object. */
	private boolean external;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>InjectObject</code> for a inject object
	 * with a specific name. The new object will be an internal object.
	 * 
	 * @param pName the injection name
	 * @param pObject the injection object
	 */
	public InjectObject(String pName, Object pObject)
	{
	    this(pName, pObject, false);
	}
	
    /**
     * Creates a new instance of <code>InjectObject</code> for a inject object
     * with a specific name.
     * 
     * @param pName the injection name
     * @param pObject the injection object
     * @param pExternal whether the object will be handled as external object
     */
    public InjectObject(String pName, Object pObject, boolean pExternal)
    {
        name = pName;
        object = pObject;
        external = pExternal;
    }
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the name of the inject object.
	 * 
	 * @return the object "mapping" name
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Gets the inject object.
	 * 
	 * @return the object
	 */
	public Object getObject()
	{
		return object;
	}
	
	/**
	 * Gets whether the object should be handled lik an external object. An external
	 * object won't be closed after a session ends.
	 * 
	 * @return <code>true</code> if object should be handled as an external object, <code>false</code> otherwise
	 */
	public boolean isExternal()
	{
	    return external;
	}
	
}	// InjectObject
