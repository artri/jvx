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
package com.sibvisions.rad.ui.vaadin.ext.ui.client.label;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.sibvisions.rad.ui.vaadin.ext.ui.ClickableLabel;
import com.sibvisions.rad.ui.vaadin.ext.ui.client.MouseDownServerRpc;
import com.vaadin.client.MouseEventDetailsBuilder;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.shared.ui.Connect;
import com.vaadin.v7.client.ui.label.LabelConnector;

/**
 * The {@code ClickableLabelConnector} class is the connector between the {@link ClickableLabel} on 
 * the server side and the label on the client side.
 * 
 * @author Jozef Dorko
 */
@Connect(value = ClickableLabel.class)
public class ClickableLabelConnector extends LabelConnector implements MouseDownHandler
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /** RPC. */
    private MouseDownServerRpc rpc = RpcProxy.create(MouseDownServerRpc.class, this);
    
    /** The mouse down handler registration. **/
    private HandlerRegistration mouseDownHandler;

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * {@inheritDoc}
     */
    public void onMouseDown(MouseDownEvent pEvent)
    {
        rpc.onMouseDown(MouseEventDetailsBuilder.buildMouseEventDetails(pEvent.getNativeEvent(), getWidget().getElement()));
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void init()
    {
        super.init();
        
        if (getState().isRegisterMouseHandler())
        {
            registerMouseHandler();
        }
        else
        {
            unregisterMouseHandler();
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void onStateChanged(StateChangeEvent pEvent)
    {
        super.onStateChanged(pEvent);
        
        if (getState().isRegisterMouseHandler())
        {
            registerMouseHandler();
        }
        else
        {
            unregisterMouseHandler();
        }    
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ClickableLabelState getState() 
    {
        return (ClickableLabelState)super.getState();
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Unregisters {@link MouseDownHandler} from the label.
     */
    private void unregisterMouseHandler()
    {
        if (mouseDownHandler != null)
        {
            mouseDownHandler.removeHandler();
            mouseDownHandler = null;
        }
    }

    /**
     * Registers {@link MouseDownHandler} to the label.
     */
    private void registerMouseHandler()
    {
        if (mouseDownHandler == null)
        {
            mouseDownHandler = getWidget().addMouseDownHandler(this);
        }
    }

}   // ClickableLabelConnector
