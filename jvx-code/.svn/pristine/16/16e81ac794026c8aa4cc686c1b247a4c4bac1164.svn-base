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
 * 13.09.2015 - [SW] - creation
 */
package com.sibvisions.rad.ui.vaadin.ext;

import java.util.ArrayList;

import com.vaadin.server.FontIcon;

/**
 * The <code>FontResource</code> wraps a {@link FontIcon} as resource.
 * 
 * @author René Jahn
 */
public class FontResource implements FontIcon, 
                                     INamedResource
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /** the resource name. */
    private String resourceName;

    /** the font resource. */
    private FontIcon fontIcon;
    
    /** the style name. */
    private String sStyleName;
    
    /** custom style definitions. */
    private ArrayList<StyleProperty> liCustomStyles;
    
    /** the size, if set (-1 default). */
    private int iSize = -1;
    
    /** whether the font resource is a mapped resource. */
    private boolean bMapped;
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Creates a new instance of <code>FontResource</code>.
     * 
     * @param pResourceName the resource name
     * @param pIcon the font icon
     */
    public FontResource(String pResourceName, FontIcon pIcon)
    {
    	resourceName = pResourceName;
        fontIcon = pIcon;
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getMIMEType()
    {
        return fontIcon.getMIMEType();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getFontFamily()
    {
        return fontIcon.getFontFamily();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int getCodepoint()
    {
        return fontIcon.getCodepoint();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getHtml()
    {
        return fontIcon.getHtml();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getResourceName()
    {
        return resourceName;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setStyleName(String pStyleName)
    {
        sStyleName = pStyleName;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getStyleName()
    {
        return sStyleName;
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Removes all custom style properties.
     */
    public void removeAllCustomStyleProperties()
    {
        liCustomStyles = null;
    }
    
    /**
     * Adds a custom style property.
     * 
     * @param pProperty the property
     * @param pValue the value
     */
    public void addCustomStyleProperty(String pProperty, String pValue)
    {
        if (liCustomStyles == null)
        {
            liCustomStyles = new ArrayList<StyleProperty>();
        }
        
        liCustomStyles.add(new StyleProperty(pProperty, pValue));
    }
    
    /**
     * Gets all custom style properties.
     * 
     * @return the list of custom style properties or <code>null</code> if no custom style properties
     *         were set
     */
    public StyleProperty[] getCustomStyleProperties()
    {
        if (liCustomStyles == null)
        {
            return null;
        }
        
        return liCustomStyles.toArray(new StyleProperty[liCustomStyles.size()]);
    }
    
    /**
     * Sets whether th resource was a mapped resource. A mapped resource has a name
     * and a resource mapping in the factory.
     * 
     * @param pMapped <code>true</code> if font resource is mapped
     */
    public void setMapped(boolean pMapped)
    {
        bMapped = pMapped;
    }
    
    /**
     * Gets whether the font resource is a mapped resoruce.
     * 
     * @return <code>true</code> if font resource is mapped, <code>false</code> otherwise
     * @see #setMapped(boolean)
     */
    public boolean isMapped()
    {
        return bMapped;
    }
    
    /**
     * Sets the preferred size.
     * 
     * @param pSize the size
     */
    public void setSize(int pSize)
    {
    	iSize = pSize;
    }
    
    /**
     * Gets the preferred size.
     * 
     * @return the size
     */
    public int getSize()
    {
    	return iSize;
    }
    
    //****************************************************************
    // Subclass definition
    //****************************************************************
    
    /**
     * The <code>StyleProperty</code> class is a lightweight POJO for style definitions.
     * 
     * @author René Jahn
     */
    public static final class StyleProperty
    {
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Class members
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /** the property name. */
        private String name;
        /** the property value. */
        private String value;

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Initialization
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /**
         * Creates a new instance of <code>StyleProperty</code>.
         * 
         * @param pName the name
         * @param pValue the value
         */
        private StyleProperty(String pName, String pValue)
        {
            name = pName;
            value = pValue;
        }
        
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // User-defined methods
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /**
         * Gets the name of the property.
         * 
         * @return the name
         */
        public String getName()
        {
            return name;
        }
        
        /**
         * Gets the value of the property.
         * 
         * @return the value
         */
        public String getValue()
        {
            return value;
        }
        
    }   // StyleProperty
    
}   // FontResource
