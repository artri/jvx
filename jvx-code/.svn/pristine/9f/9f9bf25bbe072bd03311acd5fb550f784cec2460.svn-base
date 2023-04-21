/*
 * Copyright 2012 SIB Visions GmbH
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
 * 04.01.2013 - [SW] - creation
 * 07.08.2015 - [JR] - #1445: redesign
 */
package com.sibvisions.rad.ui.vaadin.ext.ui.client.radiobutton;

import java.util.logging.Logger;

import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.vaadin.client.EventHelper;
import com.vaadin.client.MouseEventDetailsBuilder;
import com.vaadin.client.VCaption;
import com.vaadin.client.VTooltip;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractFieldConnector;
import com.vaadin.client.ui.Icon;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.shared.communication.FieldRpc.FocusAndBlurServerRpc;
import com.vaadin.shared.ui.Connect;
import com.vaadin.shared.ui.checkbox.CheckBoxServerRpc;
import com.vaadin.shared.ui.checkbox.CheckBoxState;

/**
 * The <code>RadioButtonConnector</code> class handles the communication between the server-side component RadioButton
 * and the client-side widget {@link VRadioButton}.
 * 
 * It is a copy of {@link com.vaadin.client.ui.checkbox.CheckBoxConnector}. The only difference is the {@link #getWidget()}
 * method because it returns the {@link VRadioButton}
 * 
 * @author Stefan Wurm
 */
@Connect(com.sibvisions.rad.ui.vaadin.ext.ui.RadioButton.class)
public class RadioButtonConnector extends AbstractFieldConnector 
                                  implements FocusHandler, 
                                             BlurHandler, 
                                             ClickHandler
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /** the focus handler. */
    private HandlerRegistration focusHandlerRegistration;
    /** the blur handler. */
    private HandlerRegistration blurHandlerRegistration;
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean delegateCaptionHandling()
    {
        return false;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void init()
    {
        super.init();
        
        VRadioButton widget = getWidget();
        
        widget.addClickHandler(this);
        widget.client = getConnection();
        widget.id = getConnectorId();
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("deprecation")
    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent)
    {
        super.onStateChanged(stateChangeEvent);
        
        focusHandlerRegistration = EventHelper.updateFocusHandler(this, focusHandlerRegistration);
        blurHandlerRegistration = EventHelper.updateBlurHandler(this, blurHandlerRegistration);
        
        VRadioButton widget = getWidget();

        if (null != getState().errorMessage)
        {
            widget.setAriaInvalid(true);
            
            if (widget.errorIndicatorElement == null)
            {
                widget.errorIndicatorElement = DOM.createSpan();
                widget.errorIndicatorElement.setInnerHTML("&nbsp;");
                
                DOM.setElementProperty(widget.errorIndicatorElement, "className", "v-errorindicator");
                DOM.appendChild(widget.getElement(), widget.errorIndicatorElement);
                DOM.sinkEvents(widget.errorIndicatorElement, VTooltip.TOOLTIP_EVENTS | Event.ONCLICK);
            }
            else
            {
                widget.errorIndicatorElement.getStyle().clearDisplay();
            }
        }
        else if (widget.errorIndicatorElement != null)
        {
            widget.errorIndicatorElement.getStyle().setDisplay(Display.NONE);
            
            widget.setAriaInvalid(false);
        }
        
        if (isReadOnly())
        {
            widget.setEnabled(false);
        }
        
        Icon iconWidget = widget.icon;
        
        Icon icon = getIcon();
        
        if (iconWidget != null && (icon == null || icon != iconWidget))
        {
        	widget.getElement().removeChild(iconWidget.getElement());
        	widget.icon = null;
        	
        	Logger.getLogger("browser").severe("Clear icon");
        }

        if (icon != null)
        {
            widget.icon = icon;
            DOM.insertChild(widget.getElement(), icon.getElement(), 1);
            
            icon.sinkEvents(VTooltip.TOOLTIP_EVENTS);
            icon.sinkEvents(Event.ONCLICK);
        }
        
        // Set text
        VCaption.setCaptionText(widget, getState());
        
        widget.setValue(Boolean.valueOf(getState().checked));
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public CheckBoxState getState()
    {
        return (CheckBoxState)super.getState();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public VRadioButton getWidget()
    {
        return (VRadioButton)super.getWidget();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void onFocus(FocusEvent event)
    {
        // EventHelper.updateFocusHandler ensures that this is called only when
        // there is a listener on server side
        getRpcProxy(FocusAndBlurServerRpc.class).focus();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void onBlur(BlurEvent event)
    {
        // EventHelper.updateFocusHandler ensures that this is called only when
        // there is a listener on server side
        getRpcProxy(FocusAndBlurServerRpc.class).blur();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void onClick(ClickEvent event)
    {
        if (!isEnabled())
        {
            return;
        }
        
        VRadioButton widget = getWidget();
        
        boolean bValue = widget.getValue().booleanValue();
        
        // We get click events also from the label text, which do not alter the
        // actual value. The server-side is only interested in real changes to
        // the state.
        if (getState().checked != bValue)
        {
            getState().checked = bValue;
            
            // Add mouse details
            MouseEventDetails details = MouseEventDetailsBuilder.buildMouseEventDetails(event.getNativeEvent(), widget.getElement());
            getRpcProxy(CheckBoxServerRpc.class).setChecked(bValue, details);
            
            getConnection().getServerRpcQueue().flush();
        }
    } 

} 	// RadioButtonConnector

