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
package com.sibvisions.rad.ui.vaadin.impl.component.map.openstreet;

import javax.rad.genui.component.UIMap;

import com.sibvisions.rad.ui.vaadin.ext.ui.map.openstreet.OpenStreetMapComponent;
import com.sibvisions.rad.ui.vaadin.impl.component.map.VaadinMap;

/**
 * The {@link VaadinOpenStreetMap} is the vaadin implementation for the
 * {@linkplain UIMap} component showing a OpenStreet map.
 * 
 * @author Lukas Katic
 */
public class VaadinOpenStreetMap extends VaadinMap<OpenStreetMapComponent> 
{    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Creates a new instance of {@link VaadinOpenStreetMap}.
     */
    public VaadinOpenStreetMap()
    {
        this(new OpenStreetMapComponent());
    }
    
    /**
     * Creates a new instance of {@link VaadinOpenStreetMap} with given {@link OpenStreetMapComponent}.
     *
     * @param pComponent The {@link OpenStreetMapComponent}.
     */
    protected VaadinOpenStreetMap(OpenStreetMapComponent pComponent)
    {
        super(pComponent);
        
        pComponent.getState().tileServerAddress = "http://tile.openstreetmap.org";
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Sets address of the tile server.
     * <p>
     * The tile server is expected to behave the same as the OpenStreetMap
     * server and should be in the format:
     * {@code http://tile.openstreetmap.org}.
     * 
     * @param pTileServerAddress The tile server address.
     */
    public void setTileServerAddress(String pTileServerAddress)
    {
        resource.getState().tileServerAddress = pTileServerAddress;
    }
    
} 	// VaadinOpenStreetMap
