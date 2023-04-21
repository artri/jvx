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
package com.sibvisions.rad.ui.vaadin.ext.ui.client.map.openstreet;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.TextResource;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * Provides resources for the map.
 * 
 * @author Robert Zenz
 */
interface IMapResources extends ClientBundle
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constants
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /** The instance. */
    public static final IMapResources INSTANCE = GWT.create(IMapResources.class);
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Abstract methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Gets the {@code leaflet.js} resource.
     * 
     * @return The {@code leaflet.js} resource.
     */
    @Source("leaflet.js")
    public TextResource getLeafletScript();

    /**
     * Gets the {@code leaflet.css} resource.
     * 
     * @return The {@code leaflet.css} resource.
     */
    @Source("leaflet.css")
    public TextResource getLeafletSheet();
    
    /**
     * Gets the {@code map_defaultmarker.png} resource.
     * 
     * @return The {@code map_defaultmarker.png} resource.
     */
    @Source("map_defaultmarker.png")
    public ImageResource getDefaultMarkerImage();
    
} 	// IMapRessources
