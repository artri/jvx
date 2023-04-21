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
 * Platform and technology independent mouse event definition.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 * @see    java.awt.event.MouseEvent
 */
public class UIMouseEvent extends UIEvent 
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** The first number in the range of ids used for mouse events. */
    public static final int MOUSE_FIRST 	= 500;

    /**
     * The "mouse clicked" event. This <code>MouseEvent</code>
     * occurs when a mouse button is pressed and released.
     */
    public static final int MOUSE_CLICKED 	= MOUSE_FIRST;

    /**
     * The "mouse pressed" event. This <code>MouseEvent</code>
     * occurs when a mouse button is pushed down.
     */
    public static final int MOUSE_PRESSED 	= MOUSE_FIRST + 1;

    /**
     * The "mouse released" event. This <code>MouseEvent</code>
     * occurs when a mouse button is let up.
     */
    public static final int MOUSE_RELEASED 	= MOUSE_FIRST + 2;

    /**
     * The "mouse entered" event. This <code>MouseEvent</code>
     * occurs when the mouse cursor enters the unobscured part of component's
     * geometry. 
     */
    public static final int MOUSE_ENTERED 	= MOUSE_FIRST + 3;

    /**
     * The "mouse exited" event. This <code>MouseEvent</code>
     * occurs when the mouse cursor exits the unobscured part of component's
     * geometry.
     */
    public static final int MOUSE_EXITED	= MOUSE_FIRST + 4;

    /** The last number in the range of ids used for mouse events. */
    public static final int MOUSE_LAST      = MOUSE_EXITED;
    
    /**
     * The mouse event's x coordinate.
     * The x value is relative to the component that fired the event.
     */
    private int x;

    /**
     * The mouse event's y coordinate.
     * The y value is relative to the component that fired the event.
     */
    private int y;

    /**
     * Indicates the number of quick consecutive clicks of
     * a mouse button.
     * clickCount will be valid for only three mouse events :<BR>
     * <code>MOUSE_CLICKED</code>,
     * <code>MOUSE_PRESSED</code> and
     * <code>MOUSE_RELEASED</code>.
     * For the above, the <code>clickCount</code> will be at least 1. 
     * For all other events the count will be 0.
     */
    private int clickCount;

    /**
     * A property used to indicate whether a Popup Menu
     * should appear  with a certain gestures.
     * If <code>popupTrigger</code> = <code>false</code>,
     * no popup menu should appear.  If it is <code>true</code>
     * then a popup menu should appear.
     */
    private boolean popupTrigger;
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>UIMouseEvent</code>.
	 * 
	 * @param pSource the Source of this UIMouseEvent.
	 * @param pId     the Id of this UIMouseEvent.
     * @param pWhen   the time the event occurred
     * @param pModifiers represents the modifier keys and mouse buttons down while the event occurred
     * @param pX         the horizontal x coordinate for the mouse location
     * @param pY         the vertical y coordinate for the mouse location
     * @param pClickCount   the number of mouse clicks associated with event
     * @param pPopupTrigger a boolean, true if this event is a trigger for a popup menu 
	 */
	public UIMouseEvent(IComponent pSource, int pId, long pWhen, int pModifiers, 
			            int pX, int pY, int pClickCount, boolean pPopupTrigger)
	{
		super(pSource, pId, pWhen, pModifiers);
		
		x = pX;
		y = pY;
		clickCount = pClickCount;
		popupTrigger = pPopupTrigger;
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
		if (pId < MOUSE_FIRST || pId > MOUSE_LAST)
		{
			super.checkId(pId);
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
     * Returns the horizontal x position of the event relative to the 
     * source component.
     *
     * @return an integer indicating horizontal position relative to
     *         the component
     */
    public int getX() 
    {
        return x;
    }

    /**
     * Returns the vertical y position of the event relative to the
     * source component.
     *
     * @return an integer indicating vertical position relative to
     *         the component
     */
    public int getY() 
    {
        return y;
    }

    /**
     * Returns the number of mouse clicks associated with this event.
     *
     * @return integer value for the number of clicks
     */
    public int getClickCount() 
    {
        return clickCount;
    }

    /**
     * Returns whether or not this mouse event is the popup menu
     * trigger event for the platform.
     * <p><b>Note</b>: Popup menus are triggered differently
     * on different systems. Therefore, <code>isPopupTrigger</code>
     * should be checked in both <code>mousePressed</code>
     * and <code>mouseReleased</code>
     * for proper cross-platform functionality.
     *
     * @return <code>true</code> if this event is the popup menu trigger
     *         for this platform
     */
    public boolean isPopupTrigger() 
    {
        return popupTrigger;
    }
    
}	// UIMouseEvent
