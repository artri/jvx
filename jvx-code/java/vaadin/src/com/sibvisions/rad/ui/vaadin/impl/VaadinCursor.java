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

import javax.rad.ui.ICursor;

/**
 * The <code>VaadinCursor</code> class is the vaadin implementation of {@link ICursor}.
 * 
 * @author Stefan Wurm
 */
public class VaadinCursor extends VaadinResourceBase<ICursor>
                          implements ICursor
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class Members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
	/** CSS names corresponding to the type. */
	public static final String[] CURSORS = {
		"default", "crosshair", "text", "wait", 
		"sw-resize", "se-resize", "nw-resize", "ne-resize", 
		"n-resize", "s-resize", "w-resize", "e-resize", 
		"pointer", "move" 
	};

	/** the type. */
	private int type;
	
	/** the name. */
	private String name;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>VaadinCursor</code>.
     *
     * @param pType the type.
     * @param pName the name.
     * @see ICursor
     */
	protected VaadinCursor(int pType, String pName)
	{
		type = pType;
		name = pName;
	}

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
    public int getType()
    {
    	return type;
    }

	/**
	 * {@inheritDoc}
	 */
    public String getName()
    {
    	return name;
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Gets a <code>ICursor</code> object with the specified type.
     * 
     * @param pType the type of cursor
     * @return the <code>ICursor</code>
     * @see ICursor
     */
    public static ICursor getPredefinedCursor(int pType)
	{
		return new VaadinCursor(pType, null);
	}    
    
    /**
     * Returns a system-specific custom <code>ICursor</code> object matching the 
     * specified name.  Cursor names are, for example: "Invalid.16x16"
     * 
     * @param pCursorName a string describing the desired system-specific custom cursor 
     * @return the system specific custom cursor named
     * @see ICursor
     */
    public static ICursor getSystemCustomCursor(String pCursorName)
	{
		return new VaadinCursor(-1, pCursorName);
	}    
    
    /**
     * Gets the name of the style which is defined in the css file.
     * 
     * @return the name of the style
     */
    public String getStyleName() 
    {
    	
		switch (type) 
		{
			case ICursor.DEFAULT_CURSOR: return "cursor-default";			
			case ICursor.CROSSHAIR_CURSOR: return "cursor-crosshair";
			case ICursor.TEXT_CURSOR: return "cursor-text";
			case ICursor.WAIT_CURSOR: return "cursor-wait";
			case ICursor.SW_RESIZE_CURSOR: return "cursor-sw-resize";
			case ICursor.SE_RESIZE_CURSOR: return "cursor-se-resize";
			case ICursor.NW_RESIZE_CURSOR: return "cursor-nw-resize";
			case ICursor.NE_RESIZE_CURSOR: return "cursor-ne-resize";
			case ICursor.N_RESIZE_CURSOR: return "cursor-n-resize";
			case ICursor.S_RESIZE_CURSOR: return "cursor-s-resize";
			case ICursor.W_RESIZE_CURSOR: return "cursor-w-resize";
			case ICursor.E_RESIZE_CURSOR: return "cursor-e-resize";
			case ICursor.HAND_CURSOR: return "cursor-hand";
			case ICursor.MOVE_CURSOR: return "cursor-move";
			
			default: return "cursor-auto";
		}    
    }
    
} 	// VaadinCursor

