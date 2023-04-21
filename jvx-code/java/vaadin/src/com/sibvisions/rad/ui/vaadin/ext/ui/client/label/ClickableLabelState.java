/*
 * Copyright 2019 SIB Visions GmbH
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
 * 12.04.2019 - [DJ] - creation
 */
package com.sibvisions.rad.ui.vaadin.ext.ui.client.label;

import com.vaadin.v7.shared.ui.label.LabelState;

/**
 * The {@code ClickableLabelState} class is the state for {@link com.sibvisions.rad.ui.vaadin.ext.ui.ClickableLabel}.
 * 
 * @author Jozef Dorko
 */
public class ClickableLabelState extends LabelState
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /** true if the mouse handler should be registered. **/
    private boolean registerMouseHandler = false;

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Gets whether the mouse handler is registered.
     * 
     * @return {@code true} if the mouse handler is registered, {@code false} otherwise
     */
    public boolean isRegisterMouseHandler()
    {
        return registerMouseHandler;
    }

    /**
     * Sets wheter the mouse handler should be registered.
     * 
     * @param pRegisterMouseHandler {@code true} if the mouse handler should be registered.
     */
    public void setRegisterMouseHandler(boolean pRegisterMouseHandler)
    {
        registerMouseHandler = pRegisterMouseHandler;
    }
    
}   // ClickableLabelState
