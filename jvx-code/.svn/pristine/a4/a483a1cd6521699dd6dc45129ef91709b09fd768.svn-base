/*
 * Copyright 2018 SIB Visions GmbH
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
 * 14.02.2018 - [JR] - creation
 */
package com.sibvisions.rad.ui.vaadin.ext.ui.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.HeadElement;
import com.google.gwt.dom.client.LinkElement;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.user.client.DOM;
import com.sibvisions.rad.ui.vaadin.ext.ui.extension.UIExtension;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.extensions.AbstractExtensionConnector;
import com.vaadin.shared.ApplicationConstants;
import com.vaadin.shared.ui.Connect;

/**
 * The <code>UIExtensionConnector</code> is a special extension that enables the UI to change the DOM. 
 * 
 * @author René Jahn
 */
@Connect(UIExtension.class)
public class UIExtensionConnector extends AbstractExtensionConnector
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /** the connected widget. **/
    //private Widget widget;

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Creates a new instance of <code>DownloaderConnector</code>.
     */
    public UIExtensionConnector()
    {
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Abstract methods implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    @Override
    protected void extend(ServerConnector pTarget) 
    {
        //widget = ((ComponentConnector)pTarget).getWidget();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public UIExtensionState getState()
    {
        return (UIExtensionState)super.getState();
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void onStateChanged(StateChangeEvent pStateChangeEvent) 
    {
        super.onStateChanged(pStateChangeEvent);

        configureStylesheets();
    }    
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Configures the dynamic stylesheets. All unused stylesheets will be removed and newly added
     * stylesheets will be set.
     */
    private void configureStylesheets()
    {
        UIExtensionState state = getState();

        HeadElement head = getHead();
        
        NodeList<Node> liNodes = head.getChildNodes();
        
        List<String> liStyles = new ArrayList<String>();
        
        //boolean bRemoved = false;
        
        for (int i = liNodes.getLength(); i > 0; i--)
        {
            Node node = liNodes.getItem(i);
            
            if (node instanceof LinkElement)
            {
                LinkElement link = ((LinkElement)node);
                
                String sHref = link.getHref(); 
                
                String sPath = "/" + ApplicationConstants.APP_PATH + "/cachedresource/";
                 
                if (sHref != null)
                {
                    if (sHref.indexOf(sPath) > 0)
                    {
                        //URL from server doesn't contain the real information, it is like : app://APP/cachedresources/....
                        //see: getConnection().translateVaadinUri(.head..)
                        sHref = ApplicationConstants.APP_PROTOCOL_PREFIX + sHref.substring(sHref.indexOf(sPath) + 1);
                        
                        //remove styles which not available in the resource list 
                        if (!state.resources.containsKey(sHref))
                        {
                            head.removeChild(node);
                            
                            //bRemoved = true;
                        }
                        else
                        {
                            liStyles.add(sHref);
                        }
                    }
                    else if (state.deletePath != null && sHref.indexOf(state.deletePath) >= 0)
                    {
                        head.removeChild(node);
                    }
                }
            }
        }
        
        String sLastURL = null;
        
        for (String key : state.resources.keySet())
        {
            if (liStyles.indexOf(key) < 0)
            {
                sLastURL = getState().resources.get(key).getURL();
                
                LinkElement link = LinkElement.as(DOM.createElement(LinkElement.TAG));
                link.setRel("stylesheet");
                link.setHref(sLastURL);
                link.setType("text/css");
                
                head.appendChild(link);
            }
        }

        // We have to test, if it works without forceLayout. Everything will be faster.
//        if (bRemoved || sLastURL != null)
//        {
//            Timer timer = new Timer()
//            {
//                @Override
//                public void run()
//                {
//                    try
//                    {
//                        getConnection().forceLayout();
//                    }
//                    catch (Throwable th)
//                    {
//                        //ignore
//                    }
//                }
//            };
//            timer.schedule(300);
//        }
    }
    
    /**
     * Gets the head element from the DOM.
     * 
     * @return the head element
     */
    private HeadElement getHead() 
    {
        return HeadElement.as(Document.get().getElementsByTagName(HeadElement.TAG).getItem(0));
    }

}   // UIExtensionConnector
