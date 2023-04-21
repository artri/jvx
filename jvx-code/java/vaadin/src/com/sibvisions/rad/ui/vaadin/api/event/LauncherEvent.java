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
package com.sibvisions.rad.ui.vaadin.api.event;

import javax.rad.application.ILauncher;

import com.sibvisions.rad.ui.vaadin.impl.VaadinUI;

/**
 * The <code>LauncherEvent</code> contains information about an {@link ILauncher}.
 * 
 * @author René Jahn
 */
public class LauncherEvent extends UIPhaseEvent
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /** the launcher. */
    private ILauncher launcher;
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Creates a new instance of <code>LauncherEvent</code>.
     * 
     * @param pPhase the phase identifier
     * @param pUI the UI
     * @param pLauncher the UI launcher
     */
    public LauncherEvent(int pPhase, VaadinUI pUI, ILauncher pLauncher)
    {
        super(pPhase, pUI);
        
        launcher = pLauncher;
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Gets the {@link ILauncher}.
     * 
     * @return the launcher
     */
    public ILauncher getLauncher()
    {
        return launcher;
    }
    
}   // LauncherEvent
