/*
 * Copyright 2011 SIB Visions GmbH
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
 * 01.10.2008 - [JR] - creation
 */
package javax.rad.ui.event;

import javax.rad.ui.IComponent;

/**
 * Platform and technology independent component event definition.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author René Jahn
 * @see    java.awt.event.ComponentEvent
 */
public class UIComponentEvent extends UIEvent 
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** The first number in the range of ids used for component events. */
    public static final int COMPONENT_FIRST		= 100;
    
    /** This event indicates that the component's position changed. */
    public static final int COMPONENT_MOVED		= COMPONENT_FIRST;

    /** This event indicates that the component's size changed. */
    public static final int COMPONENT_RESIZED	= 1 + COMPONENT_FIRST;

    /** The last number in the range of ids used for component events. */
    public static final int COMPONENT_LAST      = COMPONENT_RESIZED;
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>UIComponentEvent</code>.
	 * 
	 * @param pSource the Source of this event.
	 * @param pId     the Id of this event.
     * @param pWhen   the time the event occurred
     * @param pModifiers represents the modifier keys and mouse buttons down while the event occurred
	 */
	public UIComponentEvent(IComponent pSource, int pId, long pWhen, int pModifiers)
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
		if (pId < COMPONENT_FIRST || pId > COMPONENT_LAST)
		{
			super.checkId(pId);
		}
	}
    
}	// UIComponentEvent
