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
 * 11.08.2009 - [JR] - creation
 * 25.07.2013 - [JR] - #732: TABSET_ACTIVATED, TABSET_DEACTIVATED defined 
 */
package javax.rad.ui.event;

import javax.rad.ui.IComponent;

/**
 * Platform and technology independent tabset event definition.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author René Jahn
 */
public class UITabsetEvent extends UIEvent 
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** The first number in the range of ids used for tabset events. */
    public static final int TABSET_FIRST		= 700;

    /** This event id indicates that a close event occured. */
    public static final int TABSET_CLOSED		= TABSET_FIRST;

    /** This event id indicates that a move event occured. */
    public static final int TABSET_MOVED		= TABSET_FIRST + 1;

    /** This event id indicates that a activated event occured. */
    public static final int TABSET_ACTIVATED	= TABSET_FIRST + 2;

    /** This event id indicates that a deactivated event occured. */
    public static final int TABSET_DEACTIVATED	= TABSET_FIRST + 3;
    
    /** The last number in the range of ids used for tabset events. */
    public static final int TABSET_LAST			= TABSET_DEACTIVATED;

    /** the tab index before the event occured. */
    private int iOldIndex;
    
    /** the current tab index. */
    private int iNewIndex;
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>UIActionEvent</code>.
	 * 
	 * @param pSource the Source of this UIActionEvent.
	 * @param pId     the Id of this UIActionEvent.
     * @param pWhen   the time the event occurred
     * @param pModifiers represents the modifier keys and mouse buttons down while the event occurred
     * @param pOldIndex the old tab index on which the event occured
     * @param pNewIndex the new tab index on which the event occured
	 */
	public UITabsetEvent(IComponent pSource, int pId, long pWhen, int pModifiers, int pOldIndex, int pNewIndex)
	{
		super(pSource, pId, pWhen, pModifiers);
		
		iOldIndex  = pOldIndex;
		iNewIndex   = pNewIndex;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void checkId(int pId)
	{
		if (pId < TABSET_FIRST || pId > TABSET_LAST)
		{
			super.checkId(pId);
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the index of the tab before the event occured.
	 * 
	 * @return the old tab index
	 */
    public int getOldIndex() 
    {
        return iOldIndex;
    }
    
    /**
     * Gets the current index of the tab.
     * 
     * @return the tab index
     */
    public int getNewIndex()
    {
    	return iNewIndex;
    }
    
}	// UITabsetEvent
