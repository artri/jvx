/*
 * Copyright 2013 SIB Visions GmbH
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
 * 01.04.2022 - [HM] - creation
 */
package javax.rad.util;

import java.util.Collection;

/**
 * The <code>IObjectStore</code> is a standard interface for storing objects by name with getObject, putObject, getObjectNames.
 * 
 * @author Martin Handsteiner
 */
public interface IObjectStore
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Get the specific Object.
     * 
     * @param pObjectName the property name.
     * @return the property value.
     * @see #getObjectNames()
     * @see #putObject(String, Object)
     */
    public Object getObject(String pObjectName);
    
    /**
     * Gets a {@link Collection} of all object names that are currently
     * stored as property on this resource. Returns an empty {@link Collection}
     * if there are no objects put. The returned {@link Collection} is a copy
     * of the original collection of names.
     * 
     * @return the {@link Collection} of all object names. If there are no
     *         objects put, returns an empty {@link Collection}.
     * @see #getObject(String)
     * @see #putObject(String, Object)
     */
    public Collection<String> getObjectNames();
    
    /**
     * Puts the specific property.
     * 
     * @param pObjectName the property name.
     * @param pObject the property values.
     * @return the old pObject.
     * @see #getObject(String)
     * @see #getObjectNames()
     */
    public Object putObject(String pObjectName, Object pObject);

}	// IObjectStore
