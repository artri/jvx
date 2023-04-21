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
 * 01.10.2008 - [HM] - creation
 */
package com.sibvisions.rad.ui.awt.impl;

import java.awt.AWTException;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.Hashtable;

import javax.rad.ui.ICursor;
import javax.rad.ui.UIException;

import com.sibvisions.rad.ui.swing.ext.JVxUtil;

/**
 * A class to encapsulate the representation of the mouse cursor.
 * 
 * @author Martin Handsteiner
 */
public class AwtCursor extends AwtResource<Cursor> 
                       implements ICursor 
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** List of predefined cursors. */
	private static final AwtCursor[] PREDEFINEDCURSORS = new AwtCursor[] 
    {
		new AwtCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)),
		new AwtCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR)),
		new AwtCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR)),
		new AwtCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)),
		new AwtCursor(Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR)),
		new AwtCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR)),
		new AwtCursor(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR)),
		new AwtCursor(Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR)),
		new AwtCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR)),
		new AwtCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR)),
		new AwtCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR)),
		new AwtCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR)),
		new AwtCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)),
		new AwtCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR))
	};

	/** Cached system cursors (used from developers). */
	private static Hashtable<String, AwtCursor> systemCustomCursors = new Hashtable<String, AwtCursor>();
			
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates an instance of <code>AwtCursor</code> based on 
	 * a <code>java.awt.Cursor</code>.
	 * 
	 * @param pCursor java.awt.Cursor
	 * @see java.awt.Cursor
	 */
	public AwtCursor(Cursor pCursor) 
	{
		super(pCursor);
	}
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public int getType() 
    {
    	return resource.getType();
    }

	/**
	 * {@inheritDoc}
	 */
    public String getName() 
    {
    	return resource.getName();
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Returns a cursor object with the specified predefined type.
     * 
     * @param pType the type of predefined cursor
     * @return the specified predefined cursor
     */
	public static AwtCursor getPredefinedCursor(int pType) 
	{
		return PREDEFINEDCURSORS[pType];
	}

	/**
	 * Returns a system-specific custom cursor object matching the 
     * specified name.  Cursor names are, for example: "Invalid.16x16"
     * 
     * @param pCursorName name a string describing the desired system-specific custom cursor
     * @return the system specific custom cursor named
	 */
	public static AwtCursor getSystemCustomCursor(String pCursorName) 
	{
		try 
		{
			AwtCursor result = systemCustomCursors.get(pCursorName);
			
			if (result == null)
			{
				Cursor cur = Cursor.getSystemCustomCursor(pCursorName);
				
				if (cur == null)
				{
					Image image = JVxUtil.getImage("/com/sibvisions/rad/ui/awt/ext/images/cursor/" + pCursorName + ".png");
					
					if (image != null) 
					{
						//try to create a custom cursor
						cur = Toolkit.getDefaultToolkit().createCustomCursor(image, new Point(0, 0), pCursorName);
					}
				}

				if (cur == null)
				{
					throw new IllegalArgumentException("Cursor with name '" + pCursorName + "' was not found!");
				}
				result = new AwtCursor(cur);
				
				systemCustomCursors.put(pCursorName, result);
			}
			
			return result;
		}
		catch (AWTException ex)
		{
			throw new UIException(ex.getMessage(), ex);
		}
	}

}	// AwtCursor
