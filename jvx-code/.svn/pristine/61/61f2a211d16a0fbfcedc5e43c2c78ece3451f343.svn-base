/*
 * Copyright 2014 SIB Visions GmbH
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
 * 12.09.2014 - [JR] - creation
 * 07.11.2018 - [JR] - replace submenubars to support custom styling
 */
package com.sibvisions.rad.ui.vaadin.ext.ui.client;

import java.util.Iterator;
import java.util.List;

import com.google.gwt.dom.client.Element;
import com.vaadin.client.UIDL;
import com.vaadin.client.ui.VMenuBar.CustomMenuItem;
import com.vaadin.shared.ui.ComponentStateUtil;
import com.vaadin.shared.ui.Connect;

/**
 * The <code>MenuBarConnector</code> is an extension of the {@link com.vaadin.client.ui.menubar.MenuBarConnector}
 * and forwards menuitem ids to the menubar.
 * 
 * @author René Jahn
 */
@Connect(com.sibvisions.rad.ui.vaadin.ext.ui.MenuBar.class)
public class MenuBarConnector extends com.vaadin.client.ui.menubar.MenuBarConnector
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    @SuppressWarnings("deprecation")
    @Override
    public void updateFromUIDL(UIDL uidl, com.vaadin.client.ApplicationConnection client) 
    {
        if (!isRealUpdate(uidl)) 
        {
            return;
        }
        
        if (uidl.getChildCount() > 2)
        {
            VMenuBar vmb = (VMenuBar)getWidget();
            vmb.clearIds();
            
            vmb.setPopupStyleName(getState().popupStyle);

            UIDL itemIds = uidl.getChildUIDL(2);
            
            Iterator<Object> itr = itemIds.getChildIterator();
    
            while (itr.hasNext()) 
            {
                UIDL item = (UIDL)itr.next();
    
                String sId = item.getStringAttribute("componentId");
                
                if (sId != null && sId.length() > 0)
                {
                    vmb.setItemId(item.getStringAttribute("id"), sId);
                }
            }
            
            super.updateFromUIDL(uidl, client);
            
            updateItems(getWidget(), vmb.getItems(), 1);
        }
        else
        {
            super.updateFromUIDL(uidl, client);            
        }
    }    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public StyledMenuBarState getState()
    {
        return (StyledMenuBarState)super.getState();
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Removes the ID from the first child element and sets the id on the menuitem element.
     * 
     * @param pParent the parent menubar
     * @param pItems the list of items to update
     * @param pLevel the menu level
     */
    private void updateItems(com.vaadin.client.ui.VMenuBar pParent, List<CustomMenuItem> pItems, int pLevel)
    {
        if (pItems != null)
        {
            for (CustomMenuItem item : pItems)
            {
                Element el = item.getElement().getFirstChildElement();
                
                if (el != null && el.getId() != null && el.getId().length() > 0)
                {
                    String sId = el.getId();
                    
                    el.removeAttribute("id");
                    
                    item.getElement().setId(sId);
                }

                com.vaadin.client.ui.VMenuBar mbar = item.getSubMenu();
                
                if (mbar != null)
                {
                	if (!(mbar instanceof VMenuBar))
                	{
                		StyledMenuBarState state = getState();
                		
                		VMenuBar mbNew = new VMenuBar(true, pParent);
                		
                		for (CustomMenuItem mitem : mbar.getItems())
                		{
                			mbNew.addItem(mitem);
                		}
                		
                		getWidget().client.getVTooltip().connectHandlersToWidget(mbNew);
                		mbNew.setPopupStyleName(state.popupStyle != null ? "level" + pLevel + " " + state.popupStyle : null);
                		
                        if (ComponentStateUtil.hasStyles(state)) 
                        {
                            for (String style : state.styles) 
                            {
                            	mbNew.addStyleDependentName(style);
                            }
                        }
                		
                		item.setSubMenu(mbNew);
                		
                		mbar = mbNew;
                	}
                	
                    updateItems(mbar, mbar.getItems(), pLevel + 1);
                }
            }
        }
    }
    
}   // VMenuBarConnector
