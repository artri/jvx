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
 * 12.04.2019 - [DJ] - Creation
 */
package com.sibvisions.rad.ui.vaadin.ext.ui;

import java.util.Collection;

import com.sibvisions.rad.ui.vaadin.ext.ui.client.MouseDownServerRpc;
import com.sibvisions.rad.ui.vaadin.ext.ui.client.label.ClickableLabelState;
import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.shared.Registration;
import com.vaadin.v7.shared.ui.label.ContentMode;

/**
 * * The {@code ClickableLabel} class is the server-side implementation of a label that 
 * handles mouse click events.
 * 
 * @author Jozef Dorko
 */
@SuppressWarnings("deprecation")
public class ClickableLabel extends com.vaadin.v7.ui.Label
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /** The Implementation of the RPC for the MouseDown interaction. **/
    private MouseDownServerRpc rpc = new MouseDownServerRpc()
    {
        public void onMouseDown(MouseEventDetails pMouseDetails)
        {
            fireEvent(new ClickEvent(ClickableLabel.this, pMouseDetails));
        }
    };
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Creates a new instance of {@code ClickableLabel}.
     */
    public ClickableLabel()
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
    public ClickableLabelState getState() 
    {
        return (ClickableLabelState)super.getState();
    }   
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(String pValue)
    {
        super.setValue(pValue);
    }   
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setContentMode(ContentMode pContentMode)
    {
        if (pContentMode != getContentMode())
        {
            super.setContentMode(pContentMode);
        }
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Adds the label click listener.
     *
     * @see Registration
     *
     * @param pListener the Listener to be added.
     * @return a registration object for removing the listener
     */
    public Registration addClickListener(ClickListener pListener)
    {
        if (getListeners(ClickEvent.class).isEmpty())
        {
            getState().setRegisterMouseHandler(true);
            registerRpc(rpc);
        }
        return addListener(ClickEvent.class, pListener, ClickListener.clickMethod);
    }
    
    /**
     * Removes the label click listener.
     * 
     * @param pListener the Listener to be added.
     */
    public void removeClickListener(ClickListener pListener)
    {
        removeListener(ClickEvent.class, pListener, ClickListener.clickMethod);

        if (getListeners(ClickEvent.class).isEmpty())
        {
            getState().setRegisterMouseHandler(false);
        }
    }

    /**
     * Removes all click listeners.
     */
    public void removeAllClickListeners() 
    {
        for (ClickListener listener : getClickListeners())
        {
            removeClickListener(listener);
        }
    }

    /**
     * Gets all click listeners.
     * 
     * @return all click listeners.
     */
    public Collection<ClickListener> getClickListeners() 
    {
        return (Collection<ClickListener>)getListeners(ClickEvent.class);
    }

}   // ClickableLabel
