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
 * 06.02.2015 - [JR] - creation
 */
package com.sibvisions.rad.ui.vaadin.api;

import com.sibvisions.rad.ui.vaadin.api.event.UIPhaseEvent;

/**
 * The <code>IVaadinUIPhaseController</code> is like a listener for specific UI states.
 * It should be used to get access to the launcher or application, before the application 
 * runs. 
 * 
 * @author René Jahn
 */
public interface IVaadinUIPhaseController
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Method definitions
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~  
    
    /**
     * Notifies the controller about UI phase changes.
     * 
     * @param pEvent the phase event
     */
    public void phaseChanged(UIPhaseEvent pEvent);
    
}   // IVaadinUIPhaseController
