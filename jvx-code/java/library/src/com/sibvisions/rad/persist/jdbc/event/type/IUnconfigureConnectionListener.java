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
 * The <code>IUnconfigureConnectionListener</code> will be used for uninitializing connections of a dBAccess. 
 * 
 * @author Martin Handsteiner
 */
public interface IUnconfigureConnectionListener
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Method definitions
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Invoked, when a connection is not used for this DBAccess anymore.
     * 
     * @param pEvent the event
     * @throws Throwable if connection unconfiguration fails.
     */
    public void unconfigureConnection(ConnectionEvent pEvent) throws Throwable;
    
}   // IUnconfigureConnectionListener
