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
 * Platform and technology independent action event definition.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 * @see    java.awt.event.ActionEvent
 */
public class UIActionEvent extends UIEvent 
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** The first number in the range of ids used for action events. */
    public static final int ACTION_FIRST		= 1001;

    /** This event id indicates that a meaningful action occured. */
    public static final int ACTION_PERFORMED	= ACTION_FIRST;

    /** The last number in the range of ids used for action events. */
    public static final int ACTION_LAST			= ACTION_PERFORMED;

    
    /**
     * The nonlocalized string that gives more details
     * of what actually caused the event.
     * This information is very specific to the component
     * that fired it.
     */
    private String actionCommand;
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>UIActionEvent</code>.
	 * 
	 * @param pSource the Source of this UIActionEvent.
	 * @param pId     the Id of this UIActionEvent.
     * @param pWhen   the time the event occurred, in milliseconds since epoch.
     * @param pModifiers represents the modifier keys and mouse buttons down while the event occurred
     * @param pActionCommand nonlocalized string that gives more details of what actually caused the event 
	 */
	public UIActionEvent(IComponent pSource, int pId, long pWhen, int pModifiers, String pActionCommand)
	{
		super(pSource, pId, pWhen, pModifiers);
		
		actionCommand = pActionCommand;
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
		if (pId < ACTION_FIRST || pId > ACTION_LAST)
		{
			super.checkId(pId);
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
     * Returns the command string associated with this action.
     * This string allows a "modal" component to specify one of several 
     * commands, depending on its state. For example, a single button might
     * toggle between "show details" and "hide details". The source object
     * and the event would be the same in each case, but the command string
     * would identify the intended action.
     * <p>
     * Note that if a <code>null</code> command string was passed
     * to the constructor for this <code>ActionEvent</code>, this
     * this method returns <code>null</code>.
     *
     * @return the string identifying the command for this event
     */
    public String getActionCommand() 
    {
        return actionCommand;
    }
    
}	// UIActionEvent
