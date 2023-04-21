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
 */
package com.sibvisions.rad.ui.vaadin.ext.ui.client;

import java.io.Serializable;

/**
 * The <code>CssExtensionAttribute</code> class holds the state of one css attribute.
 * 
 * @author Stefan Wurm
 */
public class CssExtensionAttribute implements Serializable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /** search the dom tree down. **/
    public static final int SEARCH_DOWN = 0;

    /** search the dom tree up. **/
	public static final int SEARCH_UP = 1;
	
	/** search the dom tree in same parent (not recursive). */
	public static final int SEARCH_IN_PARENT = 2;
	
    /** search the dom tree in parent of parent (not recursive). */
    public static final int SEARCH_IN_PARENT_PARENT = 3;

    /** the current element. */
	public static final int SELF = -1;
	
	/** The attribute. **/
	private String attribute;
	
	/** The value of the attribute. **/
	private String value;
	
	/** the class name to search for. **/
	private String elementClassName;
	
    /** the search direction. **/
    private int searchDirection = SELF;

    /** Indicates whether that the style attribute should set with important priority. */
    private boolean styleAttributeImportant = false;
    
    /** if the extension is a style attribute. it could also attribute not in the style tag. **/
    private boolean styleAttribute = true;

    /** if the extension is a class name attribute. */
    private boolean classAttribute = false; 
    
    /** whether css match should be exact or with contains. */
    private boolean exactMatch = false;
    
    /** whether more than one element is possible. */
    private boolean multiMatch = false;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of <code>CssExtensionAttribute</code>.
	 */
	public CssExtensionAttribute() 
	{
	}
	
	/**
	 * Creates a new instance of <code>CssExtensionAttribute</code> with given
	 * attribute and value.
	 * 
	 * @param pAttribute the attribute of the style
	 * @param pValue the value of the attribute
	 */
	public CssExtensionAttribute(String pAttribute, String pValue) 
	{
		attribute = pAttribute;
		value = pValue;
	}
	
	/**
	 * Creates a new instance of <code>CssExtensionAttribute</code> with given attribute and value and priority.
	 * 
	 * @param pAttribute the attribute of the style
	 * @param pValue the value of the attribute
	 * @param pImportant <code>true</code> if the style attribute priority is important, <code>false</code> otherwise.
	 */
	public CssExtensionAttribute(String pAttribute, String pValue, boolean pImportant)
	{
		attribute = pAttribute;
		value = pValue;
		styleAttributeImportant = pImportant;
	}

	/**
	 * Creates a new instance of <code>CssExtensionAttribute</code> with given 
	 * attribute, value and search options.
	 * 
	 * @param pAttribute the attribute of the style
	 * @param pValue the value of the attribute
	 * @param pElementClassName the name of the class
	 * @param pSearchDirection search up or down in dom tree
	 */
	public CssExtensionAttribute(String pAttribute, String pValue, String pElementClassName, int pSearchDirection) 
	{
		attribute = pAttribute;
		value = pValue;
		elementClassName = pElementClassName;
		searchDirection = pSearchDirection;
	}	
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attribute == null) ? 0 : attribute.hashCode());
		result = prime * result + ((elementClassName == null) ? 0 : elementClassName.hashCode());
		result = prime * result + searchDirection;
		return result;
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
		CssExtensionAttribute other = (CssExtensionAttribute)pObject;
		if (attribute == null)
		{
			if (other.attribute != null)
			{
				return false;
			}
		}
		else if (!attribute.equals(other.attribute))
		{
			return false;
		}
		if (elementClassName == null)
		{
			if (other.elementClassName != null)
			{
				return false;
			}
		}
		else if (!elementClassName.equals(other.elementClassName))
		{
			return false;
		}
		if (searchDirection != other.searchDirection)
		{
			return false;
		}
		return true;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
	
	/**
	 * Returns the attribute.
	 * 
	 * @return the attribute
	 */
	public String getAttribute()
	{
		return attribute;
	}

	/**
	 * Sets the attribute.
	 * 
	 * @param pAttribute the attribute
	 */
	public void setAttribute(String pAttribute)
	{
		attribute = pAttribute;
	}

	/**
	 * Returns the value.
	 * 
	 * @return the value
	 */
	public String getValue()
	{
		return value;
	}

	/**
	 * Sets the value.
	 * 
	 * @param pValue the value
	 */
	public void setValue(String pValue)
	{
		value = pValue;
	}
	
	/**
	 * Sets the style attribute priority to important.
	 * 
	 * @return <code>true</code> if the style attribute priority is important, <code>false</code> otherwise.
	 */
	public boolean isStyleAttributeImportant()
	{
		return styleAttributeImportant;
	}

	/**
	 * Is the style attribute priority important or not.
	 * 
	 * @param pStyleAttributeImportant <code>true</code> if the style attribute priority is important, <code>false</code> otherwise.
	 */
	public void setStyleAttributeImportant(boolean pStyleAttributeImportant)
	{
		styleAttributeImportant = pStyleAttributeImportant;
	}

	/**
	 * If the CssExtension is an attribute for the style tag of the element or not.
	 * 
	 * @return true if the attribute is a style tag.
	 */
	public boolean isStyleAttribute()
	{
		return styleAttribute;
	}

	/**
	 * If the CssExtension is a class attribute for the class tag fo the element or not.
	 * 
	 * @return true fi the attribute is a class tag.
	 */
	public boolean isClassAttribute()
	{
	    return classAttribute;
	}
	
	/**
	 * If the CssExtension is an attribute for the style tag of the element or not.
	 * 
	 * @param pStyleAttribute true if the attribute is a style tag.
	 */
	public void setStyleAttribute(boolean pStyleAttribute)
	{
	    if (pStyleAttribute)
	    {
	        classAttribute = false;
	    }
	    
		styleAttribute = pStyleAttribute;
	}
	
	/**
	 * If the CssExtension is a class attribute for the class tag of the element or not.
	 * 
	 * @param pClassAttribute true if the attribute is a class tag.
	 */
	public void setClassAttribute(boolean pClassAttribute)
	{
	    if (pClassAttribute)
	    {
	        styleAttribute = false;
	    }
	    
	    classAttribute = pClassAttribute;
	}

	/**
	 * Returns the search direction.
	 * 
	 * @return 0 for down, 1 for up
	 */
	public int getSearchDirection()
	{
		return searchDirection;
	}

	/**
	 * Sets the search direction.
	 * 
	 * @param pSearchDirection 0 for down, 1 for up
	 */
	public void setSearchDirection(int pSearchDirection)
	{
		searchDirection = pSearchDirection;
	}

	/**
	 * Returns the element class name.
	 * 
	 * @return the element class name.
	 */
	public String getElementClassName()
	{
		return elementClassName;
	}

	/**
	 * Sets the element class name.
	 * 
	 * @param pElementClassName the element class name
	 */
	public void setElementClassName(String pElementClassName)
	{
		elementClassName = pElementClassName;
	}
		
	/**
	 * Sets whether class name matching should be exact or with contains.
	 * 
	 * @param pExact <code>true</code> to use equals or <code>false</code> to use contains
	 */
	public void setExactMatch(boolean pExact)
	{
		exactMatch = pExact;
	}
	
	/**
	 * Gets whether matching should be exact or with contains.
	 * 
	 * @return <code>true</code> to use equals or <code>false</code> to use contains
	 */
	public boolean isExactMatch()
	{
		return exactMatch;
	}
	
	/**
	 * Sets whether more than one element is possible.
	 * 
	 * @param pMulti <code>true</code> to support more than one element <code>false</code> to use first found element
	 */
	public void setMultipleMatch(boolean pMulti)
	{
		multiMatch = pMulti;
	}
	
	/**
	 * Gets whether more than one element is possible.
	 * 
	 * @return <code>true</code> if more than one element is supported<code>false</code> otherwise
	 */
	public boolean isMultipleMatch()
	{
		return multiMatch;
	}
	
} 	// CssExtensionAttribute
