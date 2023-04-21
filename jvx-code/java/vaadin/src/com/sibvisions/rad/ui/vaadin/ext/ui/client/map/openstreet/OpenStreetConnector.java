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
package com.sibvisions.rad.ui.vaadin.ext.ui.client.map.openstreet;

import com.sibvisions.rad.ui.vaadin.ext.ui.client.map.MapConnector;
import com.sibvisions.rad.ui.vaadin.ext.ui.client.map.MapState;
import com.sibvisions.rad.ui.vaadin.ext.ui.map.openstreet.OpenStreetMapComponent;
import com.vaadin.client.annotations.OnStateChange;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.shared.ui.Connect;
import com.vaadin.shared.ui.Connect.LoadStyle;

/**
 * The <code>OpenStreetConnector</code> handles the communication between client widget and server state.
 * 
 * @author Robert Zenz
 * @author Lukas Katic
 */
@Connect(value = OpenStreetMapComponent.class, loadStyle = LoadStyle.EAGER)
public class OpenStreetConnector extends MapConnector
{
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Creates a new instance of {@link OpenStreetConnector}.
     */
    public OpenStreetConnector()
    {
        super();
        
        getWidget().setConnector(this);
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * {@inheritDoc}
     */
    @Override
    public MapState getState()
    {
        return (MapState)super.getState();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public VOpenStreetMap getWidget()
    {
        return (VOpenStreetMap)super.getWidget();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void onStateChanged(StateChangeEvent pStateChangeEvent)
    {
        super.onStateChanged(pStateChangeEvent);
        
        getWidget().update();
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Updates the maps position and zoomLevel.
     */
    @OnStateChange(value =  {"position", "zoomLevel"})
    private void onPositionChange()
    {
        getWidget().updateMapView();
    }
    
} 	// OpenStreetConnector
