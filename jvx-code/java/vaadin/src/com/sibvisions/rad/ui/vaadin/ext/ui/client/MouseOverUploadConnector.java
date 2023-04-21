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
 * 17.01.2023 - [HM] - creation
 */
package com.sibvisions.rad.ui.vaadin.ext.ui.client;

import java.util.Collections;
import java.util.List;

import com.google.gwt.event.shared.HandlerRegistration;
import com.sibvisions.rad.ui.vaadin.ext.ui.MouseOverUpload;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ConnectorHierarchyChangeEvent;
import com.vaadin.client.ConnectorHierarchyChangeEvent.ConnectorHierarchyChangeHandler;
import com.vaadin.client.HasComponentsConnector;
import com.vaadin.client.ui.VButton;
import com.vaadin.shared.ui.Connect;

/**
 * The <code>MouseOverUploadConnector</code> class is the connector between the MouseOverUpload on 
 * the server side and the button on the client side.
 * 
 * @author Martin Handsteiner
 */
@Connect(MouseOverUpload.class)
public class MouseOverUploadConnector extends com.vaadin.client.ui.upload.UploadConnector
                                      implements HasComponentsConnector, ConnectorHierarchyChangeHandler
{

    /** The childComponents. */
    private List<ComponentConnector> childComponents;

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~    
    
	/**
	 * Creates a new instance of <code>MouseOverUploadConnector</code>.
	 */
	public MouseOverUploadConnector()
	{
	    addConnectorHierarchyChangeHandler(this);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateCaption(ComponentConnector connector)
    {
        // not caption to update.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ComponentConnector> getChildComponents()
    {
        return childComponents == null ? Collections.emptyList() : childComponents;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setChildComponents(List<ComponentConnector> pChildren)
    {
        childComponents = pChildren;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HandlerRegistration addConnectorHierarchyChangeHandler(ConnectorHierarchyChangeHandler pHandler)
    {
        return ensureHandlerManager().addHandler(ConnectorHierarchyChangeEvent.TYPE, pHandler);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onConnectorHierarchyChange(ConnectorHierarchyChangeEvent pConnectorHierarchyChangeEvent)
    {
        List<ComponentConnector> liOldChildren = pConnectorHierarchyChangeEvent.getOldChildren();

        if (liOldChildren.size() > 0)
        {
            getWidget().setCustomSubmitButton(null);
        }
        
        List<ComponentConnector> liChildren = getChildComponents();
        
        if (liChildren.size() > 0)
        {
            getWidget().setCustomSubmitButton((VButton)liChildren.get(0).getWidget());
        }
    }
    
} 	// MouseOverButtonConnector
