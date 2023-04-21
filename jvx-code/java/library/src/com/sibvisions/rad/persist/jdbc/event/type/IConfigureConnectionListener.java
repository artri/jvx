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
 * 01.06.2015 - [HM] - creation
 */
package com.sibvisions.rad.persist.jdbc.event.type;

import com.sibvisions.rad.persist.jdbc.event.ConnectionEvent;

/**
 * The <code>IConfigureConnectionListener</code> will be used for initializing connections of a dBAccess. 
 * 
 * @author Martin Handsteiner
 */
public interface IConfigureConnectionListener
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Method definitions
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Invoked, when a new connection is used for this DBAccess.
     * 
     * @param pEvent the event
     * @throws Throwable if connection configuration fails.
     */
    public void configureConnection(ConnectionEvent pEvent) throws Throwable;
    
}   // IConfigureConnectionListener
