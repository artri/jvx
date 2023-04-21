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
 * 11.09.2014 - [JR] - creation
 */
package com.sibvisions.rad.ui.vaadin.ext.ui;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import com.sibvisions.rad.ui.vaadin.ext.ui.client.StyledMenuBarState;
import com.sibvisions.util.ArrayUtil;
import com.vaadin.server.AbstractClientConnector;
import com.vaadin.server.PaintException;
import com.vaadin.server.PaintTarget;

/**
 * The <code>MenuBar</code> extends the standard {@link com.vaadin.ui.MenuBar} and sends "real" ids 
 * for menuitems to the client.
 *  
 * @author René Jahn
 */
public class MenuBar extends com.vaadin.ui.MenuBar
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /** the mapping of "real" ids. */
    private HashMap<MenuItem, String> hmpItemIds = new HashMap<MenuItem, String>();
    
    /** mark as dirty forwarding connectors. */
    private ArrayUtil<WeakReference<AbstractClientConnector>> liMarkAsDirtyForward;
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void paintContent(PaintTarget pTarget) throws PaintException 
    {
        super.paintContent(pTarget);

        pTarget.startTag("itemIds");
        
        for (MenuItem item : getItems()) 
        {
            paintItemId(pTarget, item);
        }
        
        pTarget.endTag("itemIds");
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~  
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected StyledMenuBarState getState()
    {
        return (StyledMenuBarState)super.getState();
    }    
    
    /**
     * {@inheritDoc}
     */
    public void markAsDirty()
    {
        if (liMarkAsDirtyForward != null)
        {
            ArrayUtil<WeakReference<AbstractClientConnector>> liCopy = new ArrayUtil<WeakReference<AbstractClientConnector>>(liMarkAsDirtyForward);
            
            for (WeakReference<AbstractClientConnector> wrcon : liCopy)
            {
                AbstractClientConnector con = wrcon.get(); 
                
                if (con != null)
                {
                    //foward markAsDirty to all registered connectors
                    con.markAsDirty();
                }
            }
        }
        
        super.markAsDirty();
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Sets the real id for the given menuitem.
     * 
     * @param pItem the item
     * @param pId the "real" id
     */
    public void setId(MenuItem pItem, String pId)
    {
        if (pId != null)
        {
            hmpItemIds.put(pItem, pId);
        }
        else
        {
            hmpItemIds.remove(pItem);
        }
    }
    
    /**
     * Copies all item ids into the given menu bar.
     * 
     * @param pMenuBar the menu bar
     */
    public void copyIds(MenuBar pMenuBar)
    {
        pMenuBar.hmpItemIds.clear();
        pMenuBar.hmpItemIds.putAll(hmpItemIds);
    }

    /**
     * Clears all item ids.
     */
    public void clearIds()
    {
        hmpItemIds.clear();
    }
    
    /**
     * Sets the style name for the popup menu.
     * 
     * @param pName the style name
     */
    public void setPopupStyleName(String pName)
    {
        getState().popupStyle = pName;
    }

    /**
     * Paints the item id and all children item ids.
     * 
     * @param pTarget the paint target
     * @param pItem the menuitem
     * @throws PaintException if painting fails
     */
    private void paintItemId(PaintTarget pTarget, MenuItem pItem) throws PaintException 
    {
        if (!pItem.isVisible()) 
        {
            return;
        }

        pTarget.startTag("item");
        
        pTarget.addAttribute("id", "" + pItem.getId());
        
        String sId = hmpItemIds.get(pItem);
        
        if (sId != null)
        {
            pTarget.addAttribute("componentId", sId);
        }
        else
        {
            pTarget.addAttribute("componentId", "");
        }

        pTarget.endTag("item");

        if (pItem.hasChildren()) 
        {
            for (MenuItem child : pItem.getChildren()) 
            {
                paintItemId(pTarget, child);
            }
        }
    }
    
    /**
     * Adds a connector to the markAsDirty forward list.
     * 
     * @param pConnector the connector
     */
    public void addMarkAsDirtyConnector(AbstractClientConnector pConnector)
    {
        if (pConnector != null)
        {
            if (liMarkAsDirtyForward == null)
            {
                liMarkAsDirtyForward = new ArrayUtil<WeakReference<AbstractClientConnector>>();
            }
            
            if (!liMarkAsDirtyForward.contains(pConnector))
            {
                liMarkAsDirtyForward.add(new WeakReference<AbstractClientConnector>(pConnector));
            }
        }
    }
    
    /**
     * Removes a connector to the markAsDirty forward list.
     * 
     * @param pConnector the connector
     */
    public void removeMarkAsDirtyConnector(AbstractClientConnector pConnector)
    {
        if (liMarkAsDirtyForward != null && pConnector != null)
        {
            liMarkAsDirtyForward.remove(pConnector);
        }
    }
    
}   // MenuBar
