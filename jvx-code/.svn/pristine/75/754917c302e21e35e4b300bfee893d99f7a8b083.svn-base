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
 * 07.11.2008 - [JR] - creation
 */
package com.sibvisions.rad.ui.swing.ext.event;

import java.util.EventObject;

/**
 * An event which indicates that a tab action occurred in a tabbed pane. 
 * 
 * @author René Jahn
 */
public class TabEvent extends EventObject
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The affected old tab index. */
	private int iOldIndex;
	
	/** The affected new tab index. */
	private int iNewIndex;

	/** The input event's Time stamp in UTC format. The time stamp indicates when the input event was created. */
	private long lWhen;

    /** The state of the modifier mask at the time the input event was fired. */
    private int iModifiers;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /**
     * Creates a new instance of <code>TabEvent</code>.
     * 
     * @param pSource the Source of this <code>TabEvent</code>
     * @param pOldIndex the old index of the triggered tab
     * @param pNewIndex the new index of the triggered tab
     * @param pWhen the time the event occurred
     * @param pModifiers represents the modifier keys and mouse buttons down while the event occurred
     */
	public TabEvent(Object pSource, int pOldIndex, int pNewIndex, long pWhen, int pModifiers)
	{
		super(pSource);
		
		iOldIndex = pOldIndex;
		iNewIndex = pNewIndex;
		lWhen     = pWhen;
		iModifiers = pModifiers;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the affected old tab index.
	 * 
	 * @return the tab index
	 */
	public int getOldTabIndex()
	{
		return iOldIndex;
	}

	/**
	 * Gets the affected new tab index.
	 * 
	 * @return the tab index
	 */
	public int getNewTabIndex()
	{
		return iNewIndex;
	}

	/**
	 * Returns the timestamp of when this event occurred.
	 * 
	 * @return the timestamp in millis
	 */
	public long getWhen()
	{
		return lWhen;
	}
	
	/**
	 * Returns the modifier mask for this event.
	 * 
	 * @return the modifier mask
	 */
	public int getModifiers()
	{
		return iModifiers;
	}
	
}	// TabEvent
