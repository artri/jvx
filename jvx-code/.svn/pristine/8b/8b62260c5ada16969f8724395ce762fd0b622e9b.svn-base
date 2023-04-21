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
 * 05.12.2014 - [JR] - creation
 */
package javax.rad.ui;

import java.util.Arrays;
import java.util.List;

import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.type.StringUtil;

/**
 * The <code>Style</code> class is a simple container for style information. It
 * allows to define additional style names.
 * 
 * @author René Jahn
 */
public class Style implements Cloneable
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /** the style names. */
    private String[] saStyleNames;
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Creates a new instance of <code>Style</code> with given style 
     * names.
     * 
     * @param pStyleNames the style names
     */
    public Style(String... pStyleNames)
    {
        saStyleNames = pStyleNames;
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    @Override
    public Style clone()
    {
        try
        {
            Style result = (Style)super.clone();
            
            if (saStyleNames != null)
            {
                result.saStyleNames = saStyleNames.clone();
            }
            
            return result;
        }
        catch (CloneNotSupportedException cnse)
        {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError();
        }    
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
    	if (saStyleNames == null)
    	{
    		return "Style = []";
    	}
    	
    	return "Style = " + Arrays.toString(saStyleNames);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return 31 + Arrays.hashCode(saStyleNames);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object pObject)
    {
        if (this == pObject)
        {
            return true;
        }
        
        if (pObject == null)
        {
            return false;
        }
        
        if (getClass() != pObject.getClass())
        {
            return false;
        }
        
        Style stObject = (Style)pObject;
        
        if (!Arrays.equals(saStyleNames, stObject.saStyleNames))
        {
            return false;
        }
        
        return true;
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Adds a style name.
     * 
     * @param pName the name of the style
     * @return this style
     */
    private Style addStyleName(String pName)
    {
        if (pName != null 
            && pName.length() > 0
            && ArrayUtil.indexOf(saStyleNames, pName) < 0)
        {
            saStyleNames = ArrayUtil.add(saStyleNames, pName);
        }
        
        return this;
    }
    
    /**
     * Removes a style name.
     * 
     * @param pName the name of the style
     * @return this style
     */
    private Style removeStyleName(String pName)
    {
        if (pName != null)
        {
            saStyleNames = ArrayUtil.remove(saStyleNames, pName);
        }
        
        return this;
    }

    /**
     * Gets the list of already defined style names.
     * 
     * @return the list of style names or an empty list
     */
    public String[] getStyleNames()
    {
        if (saStyleNames == null)
        {
            return new String[0];
        }
        else
        {
            return saStyleNames;
        }
    }
    
    /**
     * Gets whether the current style definition contains the given style name.
     * 
     * @param pName the style name to find
     * @return <code>true</code> if given style name was found or <code>false</code> if not found
     */
    public boolean containsStyleName(String pName)
    {
        return ArrayUtil.indexOf(saStyleNames, pName) >= 0;
    }

    /**
     * Gets a <code>Style</code> with parsed values of the given style names.
     * 
     * @param pStyleNames the style names, separated with spaces
     * @return the style information
     */
    public static Style parse(String pStyleNames)
    {
        if (pStyleNames == null)
        {
            return new Style();
        }
        
        List<String> liStyles = StringUtil.separateList(pStyleNames, " ", true);
        
        Style style = new Style();
        
        String sStyleName;
        
        for (int i = 0, cnt = liStyles.size(); i < cnt; i++)
        {
            sStyleName = liStyles.get(i);

            if (sStyleName.length() > 0)
            {
                style.addStyleName(sStyleName);
            }
        }
        
        return style;
    }
    
    /**
     * Adds one or more style names to the given component.
     * 
     * @param pComponent the component
     * @param pName the style names
     * @return the current style of the component
     */
    public static Style addStyleNames(IComponent pComponent, String... pName)
    {
        if (pComponent != null)
        {
            Style style = pComponent.getStyle();
            
            if (pName != null && pName.length > 0)
            {
                for (int i = 0; i < pName.length; i++)
                {
                	if (pName[i].indexOf(' ') > 0)
                	{
                		for (String name : pName[i].split(" "))
                		{
                    		style.addStyleName(name);
                		}
                	}
                	else
                	{
                		style.addStyleName(pName[i]);
                	}
                }
    
                pComponent.setStyle(style);
            }
            
            return style;
        }
        
        return null;
    }
        
    /**
     * Removes one or more style names from the given component.
     * 
     * @param pComponent the component
     * @param pName the style names
     * @return the current style of the component
     */
    public static Style removeStyleNames(IComponent pComponent, String... pName)
    {
        if (pComponent != null)
        {
            Style style = pComponent.getStyle();
    
            if (style != null && pName != null && pName.length > 0)
            {
                for (int i = 0; i < pName.length; i++)
                {
                	if (pName[i].indexOf(' ') > 0)
                	{
                		for (String name : pName[i].split(" "))
                		{
                    		style.removeStyleName(name);
                		}
                	}
                	else
                	{
                		style.removeStyleName(pName[i]);
                	}
                }
    
                pComponent.setStyle(style);
            }
            
            return style;
        }
        
        return null;
    }
    
}   // Style
