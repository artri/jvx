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
 * 01.10.2008 - [JR] - creation
 */
package javax.rad.ui.event;

import javax.rad.ui.IComponent;

/**
 * Platform and technology independent window event definition.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author René Jahn
 * @see    java.awt.event.WindowEvent
 */
public class UIWindowEvent extends UIEvent 
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** The first number in the range of ids used for window events. */
    public static final int WINDOW_FIRST	= 200;
    
    /**
     * The window opened event.  This event is delivered only
     * the first time a window is made visible.
     */
    public static final int WINDOW_OPENED	= WINDOW_FIRST;

    /**
     * The "window is closing" event. This event is delivered when
     * the user attempts to close the window from the window's system menu.  
     * If the program does not explicitly hide or dispose the window
     * while processing this event, the window close operation will be
     * cancelled.
     */
    public static final int WINDOW_CLOSING	= 1 + WINDOW_FIRST;

    /**
     * The window closed event. This event is delivered after
     * the window has been closed as the result of a call to dispose.
     */
    public static final int WINDOW_CLOSED	= 2 + WINDOW_FIRST;

    /**
     * The window iconified event. This event is delivered when
     * the window has been changed from a normal to a minimized state.
     * For many platforms, a minimized window is displayed as
     * the icon specified in the window's iconImage property.
     * @see java.awt.Frame#setIconImage
     */
    public static final int WINDOW_ICONIFIED	= 3 + WINDOW_FIRST;

    /**
     * The window deiconified event type. This event is delivered when
     * the window has been changed from a minimized to a normal state.
     */
    public static final int WINDOW_DEICONIFIED	= 4 + WINDOW_FIRST;

    /**
     * The window-activated event type. This event is delivered when the Window
     * becomes the active Window. Only a Frame or a Dialog can be the active
     * Window. The native windowing system may denote the active Window or its
     * children with special decorations, such as a highlighted title bar. The
     * active Window is always either the focused Window, or the first Frame or
     * Dialog that is an owner of the focused Window.
     */
    public static final int WINDOW_ACTIVATED	= 5 + WINDOW_FIRST;

    /**
     * The window-deactivated event type. This event is delivered when the
     * Window is no longer the active Window. Only a Frame or a Dialog can be
     * the active Window. The native windowing system may denote the active
     * Window or its children with special decorations, such as a highlighted
     * title bar. The active Window is always either the focused Window, or the
     * first Frame or Dialog that is an owner of the focused Window.
     */
    public static final int WINDOW_DEACTIVATED	= 6 + WINDOW_FIRST;

    /** The last number in the range of ids used for window events. */
    public static final int WINDOW_LAST         = WINDOW_DEACTIVATED;
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>UIWindowEvent</code>.
	 * 
	 * @param pSource the Source of this UIWindowEvent.
	 * @param pId     the Id of this UIWindowEvent.
     * @param pWhen   the time the event occurred
     * @param pModifiers represents the modifier keys and mouse buttons down while the event occurred
	 */
	public UIWindowEvent(IComponent pSource, int pId, long pWhen, int pModifiers)
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
		if (pId < WINDOW_FIRST || pId > WINDOW_LAST)
		{
			super.checkId(pId);
		}
	}
    
}	// UIWindowEvent
