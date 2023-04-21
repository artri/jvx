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
package com.sibvisions.rad.ui.vaadin.ext.ui.extension;

import java.util.HashMap;

import com.sibvisions.rad.ui.vaadin.ext.ui.client.UIExtensionState;
import com.vaadin.server.Resource;
import com.vaadin.server.ResourceReference;
import com.vaadin.ui.AbstractComponent;

/**
 * The <code>UIExtension</code> is an extension that connects to the UI.
 * 
 * @author René Jahn
 */
public class UIExtension extends AbstractExtension
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** the style mapping. */
    private HashMap<String, String> hmpStyles;
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Creates a new instance of <code>UIExtension</code>.
     */
    public UIExtension()
    {
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Assigns the extension with the component.
     * 
     * @param pTarget the server side component
     */
    public void extend(AbstractComponent pTarget) 
    {
        super.extend(pTarget);
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
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Adds a stylesheet.
     * 
     * @param pName the name
     * @param pStyle the resource
     */
    public void addStylesheet(String pName, Resource pStyle)
    {
        if (pStyle != null)
        {
            String sKey = pName + ":" + pStyle.hashCode();
            
            ResourceReference ref = ResourceReference.create(pStyle, this, sKey);
            
            String sURL = ref.getURL();
            
            if (hmpStyles == null)
            {
                hmpStyles = new HashMap<String, String>();
            }

            hmpStyles.put(pName, sURL);
            
            getState().resources.put(sURL, ref);

            markAsDirty();
        }
    }
    
    /**
     * Removes a stylesheet.
     * 
     * @param pName the name
     */
    public void removeStylesheet(String pName)
    {
        if (hmpStyles != null)
        {
            String sUrl = hmpStyles.remove(pName);
            
            if (sUrl != null)
            {
                getState().resources.remove(sUrl);
            
                markAsDirty();
            }
        }
    }
    
    /**
     * Clears all stylesheets.
     */
    public void clearStylesheets()
    {
        if (hmpStyles != null)
        {
            hmpStyles.clear();
            getState().resources.clear();
            
            markAsDirty();
        }
    }
    
    /**
     * Deletes an added stylesheet, by path.
     *  
     * @param pPath the path to delete
     */
    public void deleteStylesheet(String pPath)
    {
        getState().deletePath = pPath;
        
        markAsDirty();
    }
    
}   // UIExtension
