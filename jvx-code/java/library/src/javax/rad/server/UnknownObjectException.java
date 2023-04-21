/*
 * Copyright 2014 SIB Visions GmbH
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
 * 18.08.2014 - [JR] - creation
 */
package javax.rad.server;

/**
 * The <code>UnknownObjectException</code> is a {@link RuntimeException} for 
 * undefined or not found server objects.
 * 
 * @author René Jahn
 */
public class UnknownObjectException extends RuntimeException
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** 
     * Constructs a new <code>UnknownObjectException</code> with the specified object name.
     * The cause is not initialized, and may subsequently be initialized by a call to 
     * {@link #initCause}.
     *
     * @param pObjectName the name of the object 
     */
    public UnknownObjectException(String pObjectName)
    {
        super(formatName(pObjectName));
    }
    
    /**
     * Constructs a new <code>UnknownObjectException</code> with the specified object name and
     * cause. <p>Note that the detail message associated with
     * <code>cause</code> is <i>not</i> automatically incorporated in
     * this exception's detail message.
     *
     * @param pObjectName the name of the object 
     * @param pCause the cause (which is saved for later retrieval by the
     *        {@link #getCause()} method).  (A <code>null</code> value is
     *        permitted, and indicates that the cause is nonexistent or
     *        unknown.)
     */
    public UnknownObjectException(String pObjectName, Throwable pCause)
    {
        super(formatName(pObjectName), pCause);
    }   
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Formats the given object name as message.
     * 
     * @param pObjectName the object name
     * @return the message
     */
    private static String formatName(String pObjectName)
    {
        if (pObjectName == null || pObjectName.startsWith("Unknown object '"))
        {
            return pObjectName;
        }
        
        return "Unknown object '" + pObjectName + "'";        
    }
    
}   // UnknownObjectException
