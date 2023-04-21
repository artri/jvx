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

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.vaadin.client.MouseEventDetailsBuilder;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.Icon;
import com.vaadin.client.ui.VButton;
import com.vaadin.client.ui.button.ButtonConnector;
import com.vaadin.shared.ui.Connect;

/**
 * The <code>MouseOverButtonConnector</code> class is the connector between the MouseOverButton on 
 * the server side and the button on the client side.
 * 
 * @author Stefan Wurm
 */
@Connect(com.sibvisions.rad.ui.vaadin.ext.ui.MouseOverButton.class)
public class MouseOverButtonConnector extends ButtonConnector 
                                      implements MouseOverHandler, 
                                                 MouseOutHandler
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
	
	/** The RPC. **/
	private MouseOverServerRpc rpc = RpcProxy.create(MouseOverServerRpc.class, this);
	
	/** The mouse over handler registration. **/
    private HandlerRegistration mouseOverHandler;
    
    /** The mouse out handler registration. **/
    private HandlerRegistration mouseOutHandler;	
	
    /** whether the icon element should be before the text element. **/
    private boolean bIconBeforeText = true;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~    
    
	/**
	 * Creates a new instance of <code>MouseOverButtonConnector</code>.
	 */
	public MouseOverButtonConnector()
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
    public void onStateChanged(StateChangeEvent pStateChangeEvent) 
    {
        Icon iconOld = getIcon();
        
        super.onStateChanged(pStateChangeEvent);

        Icon iconNew = getIcon();
        
        VButton but = getWidget();

        if (getIcon() != null) 
        {
            //order will be changed in case of different icons
            if (iconNew != iconOld || bIconBeforeText != getState().isIconBeforeText())
            {
                Element elIcon = but.icon.getElement();
                Element elParent = elIcon.getParentElement();
                
                if (getState().isIconBeforeText())
                {
                    elParent.insertBefore(elIcon, but.captionElement);
                }
                else
                {
                    elParent.insertAfter(elIcon, but.captionElement);
                }
            }
        } 
        else 
        {
            if (but.icon != null) 
            {
                but.icon.getElement().removeFromParent();
                but.icon = null;
            }
        }
        
        bIconBeforeText = getState().isIconBeforeText();
        
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
    public MouseOverButtonState getState() 
    {
        return (MouseOverButtonState) super.getState();
    }

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~    
    
	/**
	 * {@inheritDoc}
	 */
	public void onMouseOut(MouseOutEvent pEvent)
	{
        rpc.mouseOut(MouseEventDetailsBuilder.buildMouseEventDetails(pEvent.getNativeEvent(), getWidget().getElement()));
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void onMouseOver(MouseOverEvent pEvent)
	{
	    rpc.mouseOver(MouseEventDetailsBuilder.buildMouseEventDetails(pEvent.getNativeEvent(), getWidget().getElement()));
	}       
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
	
	/**
	 * Registers a MouseOutHandler and a MouseOverHandler to the button.
	 */
    private void registerMouseHandler()
    {
    	if (mouseOverHandler == null)
    	{
	    	mouseOverHandler = getWidget().addMouseOverHandler(this);
   		}
    	
    	if (mouseOutHandler == null)
    	{
	    	mouseOutHandler = getWidget().addMouseOutHandler(this);
    	}
    }
    
	/**
	 * Unregisters a MouseOutHandler and a MouseOverHandler from the button.
	 */
    private void unregisterMouseHandler()
    {
    	if (mouseOutHandler != null)
    	{
    		mouseOutHandler.removeHandler();
    		mouseOutHandler = null;
    	}
    	
    	if (mouseOverHandler != null)
    	{
    		mouseOverHandler.removeHandler();
    		mouseOverHandler = null;
    	}
    }     

} 	// MouseOverButtonConnector
