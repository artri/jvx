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
 * 17.06.2020 - [LK] - creation
 * 
 */
package com.sibvisions.rad.ui.vaadin.ext.ui.client.map;

import com.vaadin.shared.communication.ServerRpc;

/**
 * The {@link ServerRpc} for the {@linkplain MapConnector} to enable communication between client and server.
 * 
 * @author Lukas Katic
*/
public interface IMapRpc extends ServerRpc
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Method definitions
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /** 
     * Saves the position of the selection.
     * 
     * @param pLatitude The Latitude of the selected position.
     * @param pLongitude The Longitude of the selected position.
     */
    public void savePointSelection(double pLatitude, double pLongitude);
    
} 	// IMapRpc
