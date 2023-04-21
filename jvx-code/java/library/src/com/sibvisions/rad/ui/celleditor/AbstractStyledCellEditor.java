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
 * 11.11.2015 - [RZ] - creation
 */
package com.sibvisions.rad.ui.celleditor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.rad.model.ModelException;
import javax.rad.model.ui.IControl;
import javax.rad.ui.Style;
import javax.rad.ui.celleditor.IStyledCellEditor;
import javax.rad.ui.component.IPlaceholder;

/**
 * The {@link AbstractStyledCellEditor} is an abstract implementation of
 * {@link IStyledCellEditor} which provides a base implementation.
 * 
 * @author Robert Zenz
 */
public abstract class AbstractStyledCellEditor implements IStyledCellEditor
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The horizontal alignment. */
	protected transient int horizontalAlignment = ALIGN_LEFT;
	
	/** The vertical alignment. */
	protected transient int verticalAlignment = ALIGN_CENTER;
	
	/** Stores additional properties. */ 
	private transient HashMap<String, Object> properties = null;   
	
	/** the style. */
	private transient Style style;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link AbstractStyledCellEditor}.
	 */
	protected AbstractStyledCellEditor()
	{
		// Nothing to be done.
	}
	
	/**
	 * Creates a new instance of {@link AbstractStyledCellEditor}.
	 *
	 * @param pHorizontalAlignment the horizontal alignment.
	 * @param pVerticalAlignment the vertical alignment.
	 */
	public AbstractStyledCellEditor(int pHorizontalAlignment, int pVerticalAlignment)
	{
		setHorizontalAlignment(pHorizontalAlignment);
		setVerticalAlignment(pVerticalAlignment);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public int getHorizontalAlignment()
	{
		return horizontalAlignment;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getVerticalAlignment()
	{
		return verticalAlignment;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isDirectCellEditor()
	{
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setHorizontalAlignment(int pHorizontalAlignment)
	{
		horizontalAlignment = pHorizontalAlignment;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setVerticalAlignment(int pVerticalAlignment)
	{
		verticalAlignment = pVerticalAlignment;
	}
	
    /**
     * {@inheritDoc}
     */
    public void setStyle(Style pStyle)
    {
        if (pStyle != null && pStyle.getStyleNames().length > 0)
        {
            style = pStyle.clone();
        }
        else
        {
            style = null;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public Style getStyle()
    {
        if (style == null)
        {
            return new Style();
        }
        
        return style.clone();
    }
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the placeholder text, that should be displayed, or null, if no placeholder text should be displayed.
	 * 
	 * @param pPlaceholder the editor
	 * @return the placeholder text, that should be displayed
	 * @throws ModelException pException
	 */
	public String getPlaceholderText(IPlaceholder pPlaceholder) throws ModelException
	{
		String placeholder = pPlaceholder.getPlaceholder();

		if (placeholder != null)
		{
			if (pPlaceholder instanceof IControl)
			{
				return ((IControl)pPlaceholder).translate(placeholder);
			}
			else
			{
				return placeholder;
			}
		}
		
		return null;
	}
	
	/**
	 * Sets an additional property.
	 * 
	 * @param pName the property name
	 * @param pValue the value
	 */
	public void setProperty(String pName, Object pValue)
	{
		if (pValue != null)
		{
			if (properties == null)
			{
				properties = new HashMap<String, Object>();
			}
			
			properties.put(pName, pValue);
		}
		else
		{
			if (properties != null)
			{
				properties.remove(pName);
				
				if (properties.isEmpty())
				{
					properties = null;
				}
			}
		}
	}
	
	/**
	 * Gets the value of an additional property.
	 * 
	 * @param pName the property name
	 * @return the value
	 */
	public Object getProperty(String pName)
	{
		if (properties == null)
		{
			return null;
		}
		
		return properties.get(pName);
	}
	
	/**
	 * Gets all additional properties.
	 * 
	 * @return the map with property name and property value
	 */
	public Map<String, Object> getProperties()
	{
		if (properties != null)
		{
			return Collections.unmodifiableMap(properties);
		}
		else
		{
			return Collections.unmodifiableMap(Collections.EMPTY_MAP);
		}
	}
	
	/**
	 * Gets whether an additional property with given name is set.
	 * 
	 * @param pName the property name
	 * @return <code>true</code> if a property with given is available, <code>false</code> otherwise
	 */
	public boolean hasProperty(String pName)
	{
		return properties != null && properties.containsKey(pName);
	}
	
}	// AbstractStyledCellEditor
