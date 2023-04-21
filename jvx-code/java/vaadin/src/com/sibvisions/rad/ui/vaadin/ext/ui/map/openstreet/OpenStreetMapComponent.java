/*
 * Copyright 2020 SIB Visions GmbH
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
 * 15.06.2020 - [LK] - creation
 */
package com.sibvisions.rad.ui.vaadin.ext.ui.map.openstreet;

import com.sibvisions.rad.ui.vaadin.ext.ui.map.MapComponent;

/**
 * This class only exists for Vaadin to stop complaining about having two different Connectors using one Component.
 * This class should stay empty in order to ensure the different maps are working the same way.
 * 
 * @author Lukas Katic
 */
public class OpenStreetMapComponent extends MapComponent
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Creates a new Instance of <code>OpenStreetMapComponent</code>.
     */
    public OpenStreetMapComponent()
    {
        super();
    }
    
}	// OpenStreetMapComponent
