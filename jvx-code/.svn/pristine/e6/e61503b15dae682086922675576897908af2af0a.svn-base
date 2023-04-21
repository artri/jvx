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
 * 22.10.2015 - [JR] - creation
 */
package com.sibvisions.rad.ui.vaadin.ext.ui.client;

import com.google.gwt.dom.client.Style;
import com.sibvisions.rad.ui.vaadin.ext.ui.SimplePanel;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.csslayout.CssLayoutConnector;
import com.vaadin.shared.ui.Connect;

/**
 * The <code>SimplePanelConnector</code> adds support for background images to {@link com.vaadin.ui.Panel}s.
 * 
 * @author René Jahn
 */
@Connect (value = SimplePanel.class)
public class SimplePanelConnector extends CssLayoutConnector
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) 
    { 
        super.onStateChanged(stateChangeEvent);

        String sURI = getBackgroundUri();

        if (sURI != null)
        {
            Style style = getWidget().getElement().getStyle();
            
            style.setBackgroundImage("url(" + sURI + ")");
            
            SimplePanelState state = getState();
            
            //Attention: GWT needs CamelCase and not separated by minus: background-size (throws an exception with enabled assertions)
            
            if (state.backgroundSize != null)
            {
                style.setProperty("backgroundSize", state.backgroundSize);
            }
            
            if (state.backgroundRepeat != null)
            {
                style.setProperty("backgroundRepeat", state.backgroundRepeat);
            }
            else
            {
                style.setProperty("backgroundRepeat", "no-repeat");
            }
        }
        else
        {
            Style style = getWidget().getElement().getStyle();
            
            style.clearBackgroundImage();
            style.clearProperty("backgroundSize");
            style.clearProperty("backgroundRepeat");
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public SimplePanelState getState()
    {
        return (SimplePanelState)super.getState();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
	public void onUnregister()
	{
    	// TODO Suppresses an error that the widget is still attached, requires checking.
    	// This is here to suppress an error on the client side which states
    	// that the widget is still attached to the DOM tree. If I see this
    	// right this might a bug in the CssLayout, but needs more investigating. 
    	if (getWidget().isAttached())
    	{
    		getWidget().removeFromParent();
    	}
    	
		super.onUnregister();
	}
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
     * Gets the URI of the background image set for this component.
     * 
     * @return the URI of the background image, or <code>null</code> if no background imagehas been
     *         defined.
     */
    protected String getBackgroundUri()
    {
        return getResourceUrl("background-image");
    }

}   // PanelConnector
