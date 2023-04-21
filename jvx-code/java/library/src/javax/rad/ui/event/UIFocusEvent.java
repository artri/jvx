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
package javax.rad.ui.event;

import javax.rad.ui.IComponent;

/**
 * Platform and technology independent focus event definition.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 * @see    java.awt.event.FocusEvent
 */
public class UIFocusEvent extends UIEvent 
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** The first number in the range of ids used for focus events. */    
    public static final int FOCUS_FIRST		= 1004;

    /** This event indicates that the Component is now the focus owner. */
    public static final int FOCUS_GAINED = FOCUS_FIRST;

    /** This event indicates that the Component is no longer the focus owner. */
    public static final int FOCUS_LOST = FOCUS_FIRST + 1;

    /** The last number in the range of ids used for focus events. */
    public static final int FOCUS_LAST		= FOCUS_LOST;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>UIFocusEvent</code>.
	 * 
	 * @param pSource the Source of this UIFocusEvent.
	 * @param pId     the Id of this UIFocusEvent.
     * @param pWhen   the time the event occurred
     * @param pModifiers represents the modifier keys and mouse buttons down while the event occurred
	 */
	public UIFocusEvent(IComponent pSource, int pId, long pWhen, int pModifiers)
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
		if (pId < FOCUS_FIRST || pId > FOCUS_LAST)
		{
			super.checkId(pId);
		}
	}
	
}	// UIFocusEvent
