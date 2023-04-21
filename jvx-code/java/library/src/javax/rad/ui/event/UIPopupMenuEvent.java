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
 * 17.07.2014 - [HM] - creation
 */
package javax.rad.ui.event;

import javax.rad.ui.IComponent;

/**
 * Platform and technology independent popup menu event definition.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 * @see    javax.swing.event.PopupMenuEvent
 */
public class UIPopupMenuEvent extends UIEvent 
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** The first number in the range of ids used for foacus events. */    
    public static final int POPUPMENU_FIRST		= 2000;

    /** This event indicates that the popup menu will become visible. */
    public static final int POPUPMENU_WILLBECOMEVISIBLE = POPUPMENU_FIRST;

    /** This event indicates that the popup menu will become invisible. */
    public static final int POPUPMENU_WILLBECOMEINVISIBLE = POPUPMENU_FIRST + 1;

    /** This event indicates that the popup menu is canceled. */
    public static final int POPUPMENU_CANCELED = POPUPMENU_FIRST + 2;
    
    /** The last number in the range of ids used for popup menu events. */
    public static final int POPUPMENU_LAST		= POPUPMENU_CANCELED;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>UIPopupMenuEvent</code>.
	 * 
	 * @param pSource the Source of this UIPopupMenuEvent.
	 * @param pId     the Id of this UIPopupMenuEvent.
     * @param pWhen   the time the event occurred
     * @param pModifiers represents the modifier keys and mouse buttons down while the event occurred
	 */
	public UIPopupMenuEvent(IComponent pSource, int pId, long pWhen, int pModifiers)
	{
		super(pSource, pId, pWhen, pModifiers);
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
		if (pId < POPUPMENU_FIRST || pId > POPUPMENU_LAST)
		{
			super.checkId(pId);
		}
	}
	
}	// UIPopupMenuEvent
