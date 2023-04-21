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
 * 18.11.2015 - [JR] - creation
 */
package com.sibvisions.rad.ui.vaadin.ext.ui;

import com.sibvisions.rad.ui.vaadin.ext.ui.client.window.InternalFrameState;
import com.vaadin.ui.Window;

/**
 * The <code>InternalFrame</code> class is a {@link Window} and support setting
 * visibility of maximizable feature.
 * 
 * @author René Jahn
 */
public class InternalFrame extends Window
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~  
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected InternalFrameState getState()
    {
        return (InternalFrameState)super.getState();
    }    

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Sets whether the maximize button should be visible.
     * 
     * @param pMaximizable <code>true</code> if maximize button should be visible, <code>false</code> otherwise
     */
    public void setMaximizable(boolean pMaximizable)
    {
        getState().maximizable = pMaximizable;
    }
    
    /**
     * Gets whether the maximize button is visible.
     * 
     * @return <code>true</code> if maximize button is visible, <code>false</code> otherwise
     */
    public boolean isMaximizable()
    {
        return getState().maximizable;
    }

    /**
     * Marks this internal frame to be packed.
     */
    public void pack()
    {
        getState().pack++;
    }
    
    /**
     * Undos the pack commando due to setSize afterwards.
     */
    public void undoPack()
    {
        getState().pack = 0;
    }
    
}   // InternalFrame
