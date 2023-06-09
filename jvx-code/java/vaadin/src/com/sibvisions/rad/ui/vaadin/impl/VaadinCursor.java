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
		"cursor-default", "cursor-crosshair", "cursor-text", "cursor-wait", 
		"cursor-sw-resize", "cursor-se-resize", "cursor-nw-resize", "cursor-ne-resize", 
		"cursor-n-resize", "cursor-s-resize", "cursor-w-resize", "cursor-e-resize", 
		"cursor-hand", "cursor-move" 
	};

    /** List of predefined cursors. */
    public static final VaadinCursor[] PREDEFINEDCURSORS = new VaadinCursor[] 
    {
        new VaadinCursor(DEFAULT_CURSOR, null),
        new VaadinCursor(CROSSHAIR_CURSOR, null),
        new VaadinCursor(TEXT_CURSOR, null),
        new VaadinCursor(WAIT_CURSOR, null),
        new VaadinCursor(SW_RESIZE_CURSOR, null),
        new VaadinCursor(SE_RESIZE_CURSOR, null),
        new VaadinCursor(NW_RESIZE_CURSOR, null),
        new VaadinCursor(NE_RESIZE_CURSOR, null),
        new VaadinCursor(N_RESIZE_CURSOR, null),
        new VaadinCursor(S_RESIZE_CURSOR, null),
        new VaadinCursor(W_RESIZE_CURSOR, null),
        new VaadinCursor(E_RESIZE_CURSOR, null),
        new VaadinCursor(HAND_CURSOR, null),
        new VaadinCursor(MOVE_CURSOR, null)
    };
    
    /** The default cursor. */
    public static final VaadinCursor DEFAULT = PREDEFINEDCURSORS[DEFAULT_CURSOR];
	
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
		return PREDEFINEDCURSORS[pType];
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
    	if (type >= 0 && type < CURSORS.length)
    	{
    	    return CURSORS[type];
    	}
    	else
    	{
    	    return "cursor-auto";
    	}
    }
    
} 	// VaadinCursor

