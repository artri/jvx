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
 */
package com.sibvisions.rad.ui.vaadin.ext.ui.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.dom.client.Element;
import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.UIDL;
import com.vaadin.client.ui.VOverlay;
import com.vaadin.shared.ui.menubar.MenuBarConstants;

/**
 * The <code>VMenuBar</code> extends the standard {@link com.vaadin.client.ui.VMenuBar} and adds
 * the id property for created menu items.
 * 
 * @author René Jahn
 */
@SuppressWarnings("deprecation")
public class VMenuBar extends com.vaadin.client.ui.VMenuBar
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /** the id cache for internal menu item ids. */ 
    private HashMap<String, String> ids = new HashMap<String, String>();
    
    /** the additional popup style name. */
    private String popupStyle;
    
    /** whether to use the changed primary style name. */
    private boolean bChangedPrimaryStyleName = false;
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Creates a new instance of <code>VMenuBar</code>.
     */
    public VMenuBar()
    {
    }
    
    /**
     * Creates a new instance of <code>VMenuBar</code>.
     * 
     * @param pSubMenu whether this menubar is a sub menu
     * @param pParent the parent menu
     */    
    public VMenuBar(boolean pSubMenu, com.vaadin.client.ui.VMenuBar pParent)
    {
    	super(pSubMenu, pParent);
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    @Override
    public String buildItemHTML(UIDL item) 
    {
        String sHtml = super.buildItemHTML(item);
        
        if (!item.hasAttribute("separator")) 
        {
            String sId = ids.get(new String("" + item.getIntAttribute("id")));
            
            if (sId != null)
            {
                int iPos = sHtml.indexOf("<span class=");
                
                if (iPos >= 0)
                {
                    //add ID to first <span
                    //this ID will be removed later because the ID is one hierarchy level too deep
                    //see MenuBarConnector for details
                    StringBuilder sbHtml = new StringBuilder(sHtml);
                    
                    sbHtml.replace(iPos, iPos + 12, "<span id=\"" + sId + "\" class=");
                    
                    sHtml = sbHtml.toString();
                }
            }
        }
        
        return sHtml;
    }
    
    @Override
    public void clearItems()
    {
        super.clearItems();
    }

    @Override
    protected void showChildMenuAt(com.vaadin.client.ui.VMenuBar.CustomMenuItem item, int top, int left)
    {
        bChangedPrimaryStyleName = true;
     
        try
        {
            super.showChildMenuAt(item, top, left);
        }
        finally
        {
            bChangedPrimaryStyleName = false;
        }
    }
    
    @Override
    public String getStylePrimaryName()
    {
        if (bChangedPrimaryStyleName && popupStyle != null)
        {
            return popupStyle + " " + super.getStylePrimaryName();
        }
        
        return super.getStylePrimaryName();
    }
    
    @Override
    protected VOverlay createOverlay()
    {
        if (popupStyle != null 
            || parentMenu != null && parentMenu.getStyleName().contains("jvxmenubar"))
        {
            PopupOverlay overlay = new PopupOverlay();
            overlay.sAdditionalStyle = popupStyle;
            
            return overlay;
        }
        else
        {
            return super.createOverlay();
        }
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Clear all ids.
     */
    public void clearIds()
    {
        ids.clear();
    }
    
    /**
     * Sets the id for the given menuitem.
     * 
     * @param pItemId the "internal" item id
     * @param pId the "real" id
     */
    public void setItemId(String pItemId, String pId)
    {
        ids.put(pItemId, pId);
    }
    
    /**
     * Sets the additional name for the popup.
     * 
     * @param pName the style name
     */
    public void setPopupStyleName(String pName)
    {
        popupStyle = pName;
    }
    
    //****************************************************************
    // Subclass definition
    //****************************************************************

    /**
     * The <code>PopupOverlay</code> is a simple {@link VOverlay} but adds a default style name. 
     * 
     * @author René Jahn
     */
    public static class PopupOverlay extends VOverlay
    {
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Class members
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /** an additional style. */
        public String sAdditionalStyle;
        
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Initialization
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        
        /**
         * Creates a new instance of <code>PopupOverlay</code>.
         */
        public PopupOverlay()
        {
            super(true, false);
        }

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Overwritten methods
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * {@inheritDoc}
		 */
        @Override
        public void setStyleName(String pStyle)
        {
            super.setStyleName(pStyle);
            
            addStyleName("jvxmenubarpopup");
            
            if (sAdditionalStyle != null && !sAdditionalStyle.equals(pStyle))
            {
                addStyleName(sAdditionalStyle);
            }
        }
        
    }	// PopupOverlay 
    
    /**
     * The <code>CustomMenuItem</code> extends the default menu item and clears the style class definition.
     * 
     * @author René Jahn
     */
	public static class CustomMenuItem extends com.vaadin.client.ui.VMenuBar.CustomMenuItem
	{
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Class members
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/** the custom styles. */
		private List<String> liCustomStyles;
		
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Overwritten methods
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void updateFromUIDL(UIDL pUidl, ApplicationConnection pClient) 
		{
    	   if (pUidl.hasAttribute(MenuBarConstants.ATTRIBUTE_ITEM_STYLE)) 
    	   {
    		   //We need the style name definition from the server-side, for lager style update
    		   liCustomStyles = parseStyle(pUidl.getStringAttribute(MenuBarConstants.ATTRIBUTE_ITEM_STYLE));
            }

            super.updateFromUIDL(pUidl, pClient);
        }

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected void updateStyleNames()
		{
	    	super.updateStyleNames();
			
	    	fixStyle(this, liCustomStyles);
		}

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // User-defined methods
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Parse the style names from the given string.
		 * 
		 * @param pStyle the style names
		 * @return the list of names
		 */
		static List<String> parseStyle(String pStyle)
		{
			List<String> liStyles = new ArrayList<String>();
			
   	    	if (pStyle != null)
   	    	{
   	    		String[] styles = pStyle.split(" ");
   	    		
   	    		for (String name : styles)
   	    		{
   	    			liStyles.add(name);
   	    		}
   	    	}
   	    	
   	    	return liStyles;
		}
		
		/**
		 * Removes duplicates from the style definition and adds missing definitions.
		 * 
		 * @param pItem the item to check
		 * @param pCustomStyles the custom style names
		 */
		static void fixStyle(com.vaadin.client.ui.VMenuBar.CustomMenuItem pItem, List<String> pCustomStyles)
		{
			//clear the style names
			
	    	Element element = pItem.getElement();
	    	
	    	String style = element.getClassName();
	    	
	    	String[] styles = style.split(" ");
	    	
	    	if (styles.length > 1)
	    	{
	    		ArrayList<String> list = new ArrayList<String>();
	    		
	    		String sPrimary = pItem.getStylePrimaryName();
	    		
	    		for (int i = 0; i < styles.length; i++)
	    		{
	    			if (!list.contains(styles[i]))
	    			{
	        			list.add(styles[i]);
	        			
	        			if (!styles[i].startsWith(sPrimary))
	        			{
	        				list.add(sPrimary + "-"  + styles[i]);
	        			}
	        			else 
	        			{
	        				String sPostfix = styles[i].substring(sPrimary.length());

	        				if (sPostfix.length() > 0)
	        				{
	        					if (sPostfix.charAt(0) == '-')
	        					{
	        						sPostfix = sPostfix.substring(1);
	        					}
	        					
	        					if (sPostfix.length() > 0)
	        					{
		        					if (pCustomStyles != null && pCustomStyles.contains(sPostfix) && !list.contains(sPostfix))
		        					{
		        						list.add(sPostfix);
		        					}
	        					}
	        				}
	        			}
	    			}
	    		}
	    		
	    		StringBuilder sb = new StringBuilder();
	    		
	    		for (String name : list)
	    		{
	    			if (sb.length() > 0)
	    			{
	    				sb.append(" ");
	    			}
	    			
	    			sb.append(name);
	    		}
	    		
	    		element.setClassName(sb.toString());
		    }			
		}
		
	}	// CustomMenuItem
	
    /**
     * The <code>VMenuItem</code> extends the default menu item and clears the style class definition.
     * 
     * @author René Jahn
     */
	public static class VMenuItem extends com.vaadin.contextmenu.client.VMenuItem
	{
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Class members
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/** the custom styles. */
		private List<String> liCustomStyles;
		
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Overwritten methods
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void updateFromUIDL(UIDL pUidl, ApplicationConnection pClient) 
		{
    	   if (pUidl.hasAttribute(MenuBarConstants.ATTRIBUTE_ITEM_STYLE)) 
    	   {
    		   //We need the style name definition from the server-side, for lager style update
    		   liCustomStyles = CustomMenuItem.parseStyle(pUidl.getStringAttribute(MenuBarConstants.ATTRIBUTE_ITEM_STYLE));
            }

            super.updateFromUIDL(pUidl, pClient);
        }

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected void updateStyleNames()
		{
	    	super.updateStyleNames();
			
	    	CustomMenuItem.fixStyle(this, liCustomStyles);
		}
		
	}	// VMenuItem
    
}   // VMenuBar
