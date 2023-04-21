/*
 * Copyright 2012 SIB Visions GmbH
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
 * 08.01.2013 - [SW] - creation
 */
package com.sibvisions.rad.ui.vaadin.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.rad.ui.IFont;

import com.sibvisions.rad.ui.vaadin.ext.ui.client.CssExtensionAttribute;

/**
 * The <code>VaadinFont</code> class is the vaadin implementation of {@link IFont}.
 * 
 * @author Stefan Wurm
 */
public class VaadinFont extends VaadinResourceBase<IFont> 
						implements IFont
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The font name. */
	private String sName;
	
	/** The font style. */
	private int iStyle;
	
	/** The font size. */
	private int iSize;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Creates a new instance of <code>VaadinFont</code> from the specified name, 
     * style and point size.
     *
     * @param pName the font name. 
     * @param pStyle the style constant for the <code>WebFont</code>
     * @param pSize the point size of the <code>WebFont</code>
     */
    public VaadinFont(String pName, int pStyle, int pSize)
    {
    	sName = pName;
    	iStyle = pStyle;
    	iSize = pSize;
    }	
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */    
	public String getName()
	{
		return sName;
	}
	
	/**
	 * {@inheritDoc}
	 */	
	public String getFamily()
	{
		return sName;
	}
	
	/**
	 * {@inheritDoc}
	 */	
	public String getFontName()
	{
		return sName;
	}
	
	/**
	 * {@inheritDoc}
	 */	
	public int getStyle()
	{
		return iStyle;
	}
	
	/**
	 * {@inheritDoc}
	 */	
	public int getSize()
	{
		return iSize;
	}
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Gets all defined css properties for this font.
     * 
     * @param pElementClassName the name of the class element
     * @param pSearchDirection the search direction
     * @return the CssExtensionAttributes as List
     */
    public ArrayList<CssExtensionAttribute> getStyleAttributes(String pElementClassName, int pSearchDirection) 
    {
    	return getStyleAttributes(pElementClassName, pSearchDirection, false);
    }
    
    /**
     * Gets all defined css properties for this font.
     * 
     * @param pElementClassName the name of the class element
     * @param pSearchDirection the search direction
     * @param pExactMatch whether the css matching should be exact
     * @return the CssExtensionAttributes as List
     */
    public ArrayList<CssExtensionAttribute> getStyleAttributes(String pElementClassName, int pSearchDirection, boolean pExactMatch) 
    {
    	ArrayList<CssExtensionAttribute> cssExtensionAttributes = new ArrayList<CssExtensionAttribute>();
    	
    	Map<String, String> styleAttributes = getStyleAttributes(false);
    	Iterator<String> styleAttributeNames = styleAttributes.keySet().iterator();
		
    	while (styleAttributeNames.hasNext())
    	{
    		String styleAttributeName = styleAttributeNames.next();

			CssExtensionAttribute attrib = new CssExtensionAttribute(styleAttributeName,
																	 styleAttributes.get(styleAttributeName),
																	 pElementClassName,
																	 pSearchDirection);
			attrib.setExactMatch(pExactMatch);
    			
			cssExtensionAttributes.add(attrib);
    	}
    	
		return cssExtensionAttributes;
		
    }
    
    /**
     * Gets all defined css properties for this font.
     * 
     * @param pCamelCaseFormat <code>true</code> if the camel case format should use for style names, <code>false</code> otherwise
     * @return all defined css properties as <code>Map</code>
     */
    public Map<String, String> getStyleAttributes(boolean pCamelCaseFormat)
    {
    	Map<String, String> styleAttributes = new HashMap<String, String>();
    	
    	boolean isDefaultFont = getName() == null || "Dialog".equals(getName()) || "Default".equals(getName());
    	
        if (!isDefaultFont)
        {
            styleAttributes.put(pCamelCaseFormat ? "fontFamily" : "font-family", getName());
        }
        
    	if (!isDefaultFont || getStyle() != IFont.PLAIN)
		{
			if ((getStyle() & IFont.BOLD) != 0)
			{
				styleAttributes.put(pCamelCaseFormat ? "fontWeight" : "font-weight", "bold");
			}
			else
			{
			    styleAttributes.put(pCamelCaseFormat ? "fontWeight" : "font-weight", "normal");
			}
			if ((getStyle() & IFont.ITALIC) != 0)
			{
				styleAttributes.put(pCamelCaseFormat ? "fontStyle" : "font-style", "italic");
			}
		}
		
    	if (!isDefaultFont || getSize() != 12)
    	{
    	    styleAttributes.put(pCamelCaseFormat ? "fontSize" : "font-size", getSize() + "px");
    	}
    	
    	return styleAttributes;
    }
    
}	// VaadinFont
