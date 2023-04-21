/*
 * Copyright 2013 SIB Visions GmbH
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
 * 25.01.2013 - [SW] - creation
 */
package com.sibvisions.rad.ui.vaadin.ext.ui.client;

import com.google.gwt.aria.client.ExpandedValue;
import com.google.gwt.aria.client.Roles;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.AttachEvent.Handler;
import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.UIDL;
import com.vaadin.client.ui.datefield.DateTimeFieldConnector;
import com.vaadin.shared.ui.Connect;

/**
 * The <code>MouseOverButtonConnector</code> class is the connector between the MouseOverButton on 
 * the server side and the button on the client side.
 * 
 * @author Stefan Wurm
 */
@Connect(com.sibvisions.rad.ui.vaadin.ext.ui.ExtendedPopupDateField.class)
public class ExtendedPopupDateFieldConnector extends DateTimeFieldConnector
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~    
    
	/**
	 * Creates a new instance of <code>MouseOverButtonConnector</code>.
	 */
	public ExtendedPopupDateFieldConnector()
	{
		super();	
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    @Override
    protected void init() 
    {
    	super.init();
    	
    	getWidget().popup.addAttachHandler(new Handler() 
    	{
			public void onAttachOrDetach(AttachEvent event) 
			{
				Roles.getButtonRole().setAriaExpandedState(getWidget().calendarToggle.getElement(), event.isAttached() ? ExpandedValue.TRUE : ExpandedValue.FALSE);					
			}
		});
    }
    
    @Override
    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) 
    {
    	super.updateFromUIDL(uidl,  client);

        if (uidl.hasAttribute("format")) 
        {
        	if (uidl.getStringAttribute("format").contains("yyyy"))
        	{
        		getWidget().removeStyleName("no-popup-button");
        	}
        	else
        	{
        		getWidget().addStyleName("no-popup-button");
        	}
        }
    }

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
    
	/**
	 * {@inheritDoc}
	 */
	@Override
	public VExtendedPopupDateField getWidget()
	{
		// Required so that the correct class is created and instantiated.
		return (VExtendedPopupDateField)super.getWidget();
	}
		
} 	// ExtendedPopupDateFieldConnector
