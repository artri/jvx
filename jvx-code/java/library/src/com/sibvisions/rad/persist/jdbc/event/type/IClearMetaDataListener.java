/*
 * Copyright 2018 SIB Visions GmbH
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
 * 21.03.2018 - [HM] - creation
 */
package com.sibvisions.rad.persist.jdbc.event.type;

import java.util.List;

/**
 * The <code>IClearMetaDataListener</code> will be used for clear metadata event of a dBAccess. 
 * 
 * @author Martin Handsteiner
 */
public interface IClearMetaDataListener
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Method definitions
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Invoked when a DBAccess is cleared.
     * 
     * @param pApplicationName the application name
     * @param pIdentifier the identifier for the given application name
     * @throws Throwable if it fails.
     */
    public void clearedMetaData(String pApplicationName, List<String> pIdentifier) throws Throwable;
    
}   // IClearMetaDataListener
