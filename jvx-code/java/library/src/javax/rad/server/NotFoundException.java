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
 * 08.10.2014 - [JR] - creation
 */
package javax.rad.server;

/**
 * The <code>NotFoundException</code> is a {@link SecurityException} in case that something was
 * not found.
 * 
 * @author Ren� Jahn
 */
public class NotFoundException extends SecurityException
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** 
     * Constructs a new <code>NotFoundException</code> with the specified message.
     *
     * @param pMessage the message
     */
    public NotFoundException(String pMessage)
    {
        super(pMessage);
    }
    
}   // NotFoundException
