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
 * 30.09.2021 - [DJ] - creation
 */
package javax.rad.type.bean.event;

/**
 * The <code>PropertyChangedEvent</code> contains information about changes in the 
 * {@link javax.rad.type.bean.AbstractBean}.
 * 
 * @author Jozef Dorko
 */
public class PropertyChangedEvent
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /** the property name. */
    private String sPropertyName;
    
    /** the old value. */
    private Object oOldValue;
    
    /** the new value. */
    private Object oNewValue;

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Creates a new instance of <code>PropertyChangedEvent</code>.
     * 
     * @param pPropertyName the property name
     * @param pOldValue the old value
     * @param pNewValue the new value
     */
    public PropertyChangedEvent(String pPropertyName, Object pOldValue, Object pNewValue)
    {
        sPropertyName = pPropertyName;
        oOldValue = pOldValue;
        oNewValue = pNewValue;
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
        return sPropertyName;
    }

    /**
     * Gets the old value.
     * 
     * @return the old value
     */
    public Object getOldValue()
    {
        return oOldValue;
    }

    /**
     * Gets the new value.
     * 
     * @return the new value
     */
    public Object getNewValue()
    {
        return oNewValue;
    }
    
}   // PropertyChangedEvent
