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
 */
package com.sibvisions.rad.ui.vaadin.ext.ui.map;

import com.sibvisions.rad.ui.vaadin.ext.ui.client.map.MapState;
import com.vaadin.shared.communication.ServerRpc;
import com.vaadin.ui.AbstractComponent;

/**
 * The {@link MapComponent} provides the component.
 * 
 * @author Lukas Katic
 */
public abstract class MapComponent extends AbstractComponent
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Creates a new instance of {@link MapComponent}.
     */
    public MapComponent()
    {
        super();
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
    public MapState getState(boolean pMarkAsDirty) 
    {
        return (MapState)super.getState(pMarkAsDirty);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends ServerRpc> void registerRpc(T pImplementation, Class<T> pImplementationClass)
    {
    	super.registerRpc(pImplementation, pImplementationClass);
    }
    
} 	// MapComponent
