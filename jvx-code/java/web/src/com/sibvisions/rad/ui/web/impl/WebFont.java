/*
 * Copyright 2009 SIB Visions GmbH
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
 * 19.11.2009 - [HM] - creation
 */
package com.sibvisions.rad.ui.web.impl;

import javax.rad.ui.IFont;

import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.type.StringUtil;

/**
 * Web server implementation of Font.
 * 
 * @author Martin Handsteiner
 */
public class WebFont extends WebResource 
                     implements IFont
{	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The font name. */
	private String name;
	/** The font style. */
	private int style;
	/** The font size. */
	private int size;
	
	/** whether the name is the default font name. */
	private boolean bDefaultName;
	/** whether the style is the default font style. */
	private boolean bDefaultStyle;
	/** whether the size is the default font size. */
	private boolean bDefaultSize;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Creates a new instance of <code>WebFont</code> from the specified name, 
     * style and point size.
     *
     * @param pName the font name. 
     * @param pStyle the style constant for the <code>WebFont</code>
     * @param pSize the point size of the <code>WebFont</code>
     */
    public WebFont(String pName, int pStyle, int pSize)
    {
    	name = pName;
    	style = pStyle;
    	size = pSize;
    	
    	applyDefault();
    }
      
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
	 * {@inheritDoc}
	 */
    @Override
    public String getAsString()
    {
    	StringBuilder sbFont = new StringBuilder();
    	
    	if (!bDefaultName)
    	{
    		sbFont.append(name);
    	}
    	
		sbFont.append(",");
		
		if (!bDefaultStyle)
		{
			sbFont.append(style);
		}
    	
		sbFont.append(",");

		if (!bDefaultSize)
		{
			sbFont.append(size);
		}
		
		return sbFont.toString();
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public String getName()
    {
    	return name;
    }

	/**
	 * {@inheritDoc}
	 */
	public String getFamily()
    {
    	return name;
    }

	/**
	 * {@inheritDoc}
	 */
	public String getFontName()
    {
    	return name;
    }

	/**
	 * {@inheritDoc}
	 */
	public int getStyle()
    {
    	return style;
    }

	/**
	 * {@inheritDoc}
	 */
	public int getSize()
    {
    	return size;
    }
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void setAsString(String pValue)
    {
    	ArrayUtil<String> values = StringUtil.separateList(pValue, ",", true);
    	
    	if (values.size() >= 3)
    	{
        	name = values.get(0);
        	style = Integer.parseInt(values.get(1));
        	size = Integer.parseInt(values.get(2));
        	
        	applyDefault();
    	}
    }
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Applies default font handling. In case of defaults, not all properties are relevant in {@link #getAsString()}.
     */
    private void applyDefault()
    {
    	if (name == null || name.equals("Default"))
    	{
    		bDefaultName = true;
    		
			bDefaultStyle = style == IFont.PLAIN;
			bDefaultSize = size == 12;
    	}
    	else
    	{
    		bDefaultName = false;
    		bDefaultStyle = false;
    		bDefaultSize = false;
    	}
    }
    
}	// WebFont
