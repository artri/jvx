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
 * 08.01.2013 - [SW] - creation
 * 12.10.1027 - [JR] - #1834: don't send default properties
 */
package com.sibvisions.rad.ui.vaadin.ext.ui.extension;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import com.sibvisions.rad.ui.vaadin.ext.ui.client.CssExtensionAttribute;
import com.sibvisions.rad.ui.vaadin.ext.ui.client.CssExtensionState;

import elemental.json.JsonArray;
import elemental.json.JsonObject;
import elemental.json.JsonValue;

/**
 * The <code>CssExtension</code> class is a server side CssExtension. For all instances of an AbstractComponent 
 * it is possible to set style attributes on the client component.
 * 
 * <pre>
 * com.vaadin.ui.Label label = new com.vaadin.ui.Label("That is a label");
 * 
 * CssExtension cssExtension = new CssExtension();
 * cssExtension.extend(label);
 * 
 * cssExtension.setAttribute("background", "red");
 * cssExtension.addAttribute("font-weight", "bold");
 * cssExtension.addAttribute("font-size", "18px");
 * </pre>
 * 
 * @author Stefan Wurm
 */
public class CssExtension extends AbstractComponentExtension
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~		
	
    /** the default values. */
    private static HashMap<String, String> hmpDefaults;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    static
    {
        hmpDefaults = new HashMap<String, String>();
        
        CssExtensionAttribute attDefault = new CssExtensionAttribute();
        
        try
        {
            Field[] fields = attDefault.getClass().getDeclaredFields();
            
            ArrayList<Field> auFields = new ArrayList<Field>();
            
            for (int i = 0; i < fields.length; i++)
            {
                if (!Modifier.isStatic(fields[i].getModifiers()))
                {
                    try
                    {
                        fields[i].setAccessible(true);
                        
                        hmpDefaults.put(fields[i].getName(), "" + fields[i].get(attDefault));
                    }
                    catch (Exception se)
                    {
                        //ignore
                    }
                }
            }
    
            fields = auFields.toArray(new Field[auFields.size()]);
        }
        catch (SecurityException se)
        {
        }
        
        //defaults, if reflection doesn't work
        if (hmpDefaults.isEmpty())
        {
            hmpDefaults.put("elementClassName", "" + attDefault.getElementClassName());
            hmpDefaults.put("searchDirection", "" + attDefault.getSearchDirection());
            hmpDefaults.put("styleAttributeImportant", "" + attDefault.isStyleAttributeImportant());
            hmpDefaults.put("styleAttribute", "" + attDefault.isStyleAttribute());
            hmpDefaults.put("classAttribute", "" + attDefault.isClassAttribute());
            hmpDefaults.put("exactMatch", "" + attDefault.isExactMatch());
            hmpDefaults.put("multiMatch", "" + attDefault.isMultipleMatch());
        }
    }
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CssExtensionState getState() 
	{
		return (CssExtensionState)super.getState(false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public JsonObject encodeState()
	{
	    JsonObject jo = super.encodeState();
	    
	    //don't send default values to the client -> change JSON Object and remove properties with default values
	    String[] sAttNames = new String[] {"attributes", "attributesRemoved"};
	    
	    for (int i = 0; i < sAttNames.length; i++)
	    {
    	    JsonArray ja = jo.getArray(sAttNames[i]);
    	    
    	    if (ja != null)
    	    {
        	    JsonObject joAtt;
        	    JsonValue jsv;
        	    
        	    String sValue;
        	    
        	    for (int j = 0, cnt = ja.length(); j < cnt; j++)
        	    {
        	        joAtt = ja.getObject(j);
    
        	        for (Entry<String, String> entry : hmpDefaults.entrySet())
        	        {
        	        	jsv = joAtt.get(entry.getKey());
        	        	
        	        	if (jsv != null)
        	        	{
	        	            sValue = jsv.toJson();
	        	            
	        	            if (sValue == null)
	        	            {
	        	                sValue = "null";
	        	            }
        	        	}
        	        	else
        	        	{
        	        		sValue = "null";
        	        	}
        	            
                        if (entry.getValue().equals(sValue))
                        {
                            joAtt.remove(entry.getKey());
                        }
        	        }
        	    }
    	    }
	    }
	    
	    return jo;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	

	/**
	 * Adds a attribute to the state.
	 * 
	 * @param pAttribute the cssExtensionAttribute
	 */
	public void addAttribute(CssExtensionAttribute pAttribute)
	{
		addAttribute(pAttribute, false);
	}
	
	/**
	 * Adds a attribute to the state.
	 * 
	 * @param pAttribute the cssExtensionAttribute
	 * @param force <code>true</code> if it is required to add a style explicit.
	 */
	public void addAttribute(CssExtensionAttribute pAttribute, boolean force)
	{
		if (getState().addAttribute(pAttribute, force))
		{
			markAsDirty();
		}
	}	
	
	/**
	 * Adds an attribute to the state.
	 * 
	 * @param pAttribute the attribute name
	 * @param pValue the value of the attribute
	 * @param force <code>true</code> if it is required to add a style explicit.
     * @return the attribute
	 */
	public CssExtensionAttribute addAttribute(String pAttribute, String pValue, boolean force) 
	{
		CssExtensionAttribute attribute = new CssExtensionAttribute(pAttribute, pValue);
		
		addAttribute(attribute, force);
		
		return attribute;
	}	
	
	/**
	 * Adds an attribute to the state.
	 * 
	 * @param pAttribute the attribute name
	 * @param pValue the value of the attribute
	 * @return the attribute
	 */
	public CssExtensionAttribute addAttribute(String pAttribute, String pValue) 
	{
		CssExtensionAttribute attribute = new CssExtensionAttribute(pAttribute, pValue);
		
		addAttribute(attribute);
		
		return attribute;
	}
	
	/**
	 * Adds the given list of CssExtensionAttributes to the state.
	 * 
	 * @param pAttributes the list to add 
	 */
	public void addAttributes(ArrayList<CssExtensionAttribute> pAttributes) 
	{
		addAttributes(pAttributes, false);
	}	
	
	/**
	 * Adds the given list of CssExtensionAttributes to the state.
	 * 
	 * @param pAttributes the list to add 
	 * @param force <code>true</code> if it is required to add a style explicit.
	 */
	public void addAttributes(ArrayList<CssExtensionAttribute> pAttributes, boolean force) 
	{
		boolean markAsDirty = false;
		
		for (CssExtensionAttribute attribute : pAttributes) 
		{
			if (getState().addAttribute(attribute, force))
			{
				markAsDirty = true;
			}
		}

		if (markAsDirty)
		{
			markAsDirty();
		}
	}
	
	/**
	 * Removes the given list of CssExtensionAttributes from the state.
	 * 
	 * @param pAttributes the list to remove
	 */
	public void removeAttributes(ArrayList<CssExtensionAttribute> pAttributes) 
	{
		removeAttributes(pAttributes, false);
	}		
	
	/**
	 * Removes the given list of CssExtensionAttributes from the state.
	 * 
	 * @param pAttributes the list to remove
	 * @param force true if it is required to remove a style which is not explicit added.
	 */
	public void removeAttributes(ArrayList<CssExtensionAttribute> pAttributes, boolean force) 
	{
		boolean markAsDirty = false;
		
		for (CssExtensionAttribute attribute : pAttributes) 
		{
			if (getState().removeAttribute(attribute, force))
			{
				markAsDirty = true;
			}			
		}

		if (markAsDirty)
		{
			markAsDirty();
		}
	}			
  
	/**
	 * Removes an attribute from the state.
	 * 
	 * @param pAttribute the attribute name
	 */
	public void removeAttribute(String pAttribute) 
	{
		CssExtensionAttribute attribute = new CssExtensionAttribute(pAttribute, "");
		
		removeAttribute(attribute);
	}
	
	/**
	 * Removes an attribute from the state.
	 * 
	 * @param pAttribute the attribute name
	 * @param force true if it is required to remove a style which is not explicit added.
	 */
	public void removeAttribute(String pAttribute, boolean force) 
	{
		CssExtensionAttribute attribute = new CssExtensionAttribute(pAttribute, "");
		
		removeAttribute(attribute, force);
	}	
	
	/**
	 * Removes an attribute from the state.
	 * 
	 * @param pAttribute the attribute name
	 */
	public void removeAttribute(CssExtensionAttribute pAttribute) 
	{
		removeAttribute(pAttribute, false);
	}
	
	/**
	 * Removes an attribute from the state.
	 * 
	 * @param pAttribute the attribute name
	 * @param force true if it is required to remove a style which is not explicit added.
	 */
	public void removeAttribute(CssExtensionAttribute pAttribute, boolean force) 
	{
		if (getState().removeAttribute(pAttribute, force))
		{
			markAsDirty();	
		}
	}	
	
	/**
	 * Removes all currently added attributes from the state.
	 */
	public void removeAllAttributes()
	{
	    for (CssExtensionAttribute attrib : getAttributes())
	    {
	        removeAttribute(attrib, true);
	    }
	}
	
	/**
	 * Gets all attributes.
	 * @return all attributes.
	 */
	public CssExtensionAttribute[] getAttributes()
	{
        Set<CssExtensionAttribute> set = getState().getAttributes();

        return set.toArray(new CssExtensionAttribute[set.size()]);
	}

	/**
     * Gets the specific attribute.
     * 
     * @param pAttribute the attribute.
     * @return the attribute or null, if it does not exist.
     */
    public CssExtensionAttribute getAttribute(String pAttribute)
    {
        for (CssExtensionAttribute attrib : getState().getAttributes())
        {
            if (pAttribute.equals(attrib.getAttribute()))
            {
                return attrib;
            }
        }
        
        return null;
    }

} 	// CssExtension
