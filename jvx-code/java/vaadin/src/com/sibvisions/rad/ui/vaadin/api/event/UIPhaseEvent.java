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

import com.sibvisions.rad.ui.vaadin.impl.VaadinUI;

/**
 * The <code>UIPhaseEvent</code> delivers phase information to listeners.
 * 
 * @author René Jahn
 */
public abstract class UIPhaseEvent
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constants
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** the after launcher creation. */
    public static final int PHASE_CONFIGURE_LAUNCHER = 1;

    /** the after application creation. */ 
    public static final int PHASE_CONFIGURE_APPLICATION = 2;
    
    /** the before notifyVisible of application. */
    public static final int PHASE_BEFORE_NOTIFYVISIBLE = 3;

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** the phase. */
    private int iPhase;
    
    /** the UI. */
    private VaadinUI ui;

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Creates a new instance of <code>UIPhaseEvent</code>.
     * 
     * @param pPhase the phase identifier
     * @param pUI the UI
     */
    public UIPhaseEvent(int pPhase, VaadinUI pUI)
    {
        iPhase = pPhase;
        ui = pUI;
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Gets the phase identifier.
     * 
     * @return the phase identifier
     */
    public int getPhase()
    {
        return iPhase;
    }
    
    /**
     * Gets the UI.
     * 
     * @return the UI
     */
    public VaadinUI getUI()
    {
        return ui;
    }
    
}   // UIPhaseEvent
