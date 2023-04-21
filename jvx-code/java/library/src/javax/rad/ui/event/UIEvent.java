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
 * 28.02.2009 - [JR] - set modifiers from constructor parameter to member [BUGFIX]
 */
package javax.rad.ui.event;

import javax.rad.ui.IComponent;

/**
 * Platform and technology independent event definition.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 * @see    java.awt.AWTEvent
 */
public abstract class UIEvent 
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * The Shift key modifier constant.
     */
    public static final int SHIFT_MASK = 1 << 0;

    /**
     * The Control key modifier constant.
     */
    public static final int CTRL_MASK = 1 << 1;

    /**
     * The Meta key modifier constant.
     */
    public static final int META_MASK = 1 << 2;

    /**
     * The Alt key modifier constant.
     */
    public static final int ALT_MASK = 1 << 3;

    /** The AltGraph key modifier constant. */
    public static final int ALT_GRAPH_MASK = 1 << 5;

    /**
     * The Mouse Button1 modifier constant.
     */
    public static final int BUTTON1_MASK = 1 << 4;

    /**
     * The Mouse Button2 modifier constant.
     * Note that BUTTON2_MASK has the same value as ALT_MASK.
     */
    public static final int BUTTON2_MASK = 1 << 3;

    /**
     * The Mouse Button3 modifier constant.
     * Note that BUTTON3_MASK has the same value as META_MASK.
     */
    public static final int BUTTON3_MASK = 1 << 2;


	/** The Source Component of this UIEvent. */
	private IComponent source;
	
	/** The event Id of this event. */
	private int id;
	
    /**
     * The input event's timestamp in milliseconds since epoch.
     * The time stamp indicates when the input event was created.
     * 
     * @see System#currentTimeMillis()
     */
	private long when;

    /** The state of the modifier mask at the time the input event was fired. */
    private int modifiers;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>UIEvent</code>.
	 * 
	 * @param pSource the Source of this UIEvent.
	 * @param pId     the Id of this UIEvent.
     * @param pWhen   the time the event occurred, in milliseconds since epoch.
     * @param pModifiers represents the modifier keys and mouse buttons down while the event occurred
	 */
	protected UIEvent(IComponent pSource, int pId, long pWhen, int pModifiers)
	{
		checkId(pId);
		
		source    = pSource;
		id        = pId;
		when   	  = pWhen;
		modifiers = pModifiers;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Checks if the current Instance of UIEvent allows the given id.
	 * 
	 * @param pId the Id of this UIEvent.
	 */
	protected void checkId(int pId)
	{
		throw new IllegalArgumentException("The given id is not allowed for a " + getClass().getName()); 
	}

	/**
     * The object on which the Event initially occurred.
     *
     * @return the object on which the Event initially occurred.
     */
    public IComponent getSource()
    {
    	return source;
    }

	/**
     * The id of this Event.
     *
     * @return the id of this Event.
     */
    public int getId()
    {
    	return id;
    }

    /**
     * Returns the timestamp of when this event occurred, in milliseconds since epoch.
     *
     * @return the timestamp of when this event occurred, in milliseconds since epoch.
     * @see System#currentTimeMillis()
     */
    public long getWhen()
    {
        return when;
    }

    /**
     * Returns the modifier mask for this event.
     *
     * @return the modifier mask for this event.
     */
    public int getModifiers() 
    {
        return modifiers;
    }
    
    /**
     * True, if the given modifiers are set.
     * @param pModifier the modifiers to test.
     * @return true, if the given modifiers are set.
     */
    public boolean isModifier(int pModifier)
    {
    	return (modifiers & pModifier) == pModifier;
    }

    /**
     * Gets true, if shift is down.
     *  
     * @return true, if shift is down.
     */
    public boolean isShiftDown()
    {
        return (modifiers & SHIFT_MASK) == SHIFT_MASK;
    }
    
    /**
     * Gets true, if ctrl is down.
     *  
     * @return true, if ctrl is down.
     */
    public boolean isCtrlDown()
    {
        return (modifiers & CTRL_MASK) == CTRL_MASK;
    }
    
    /**
     * Gets true, if meta is down.
     *  
     * @return true, if meta is down.
     */
    public boolean isMetaDown()
    {
        return (modifiers & META_MASK) == META_MASK;
    }
    
    /**
     * Gets true, if alt is down.
     *  
     * @return true, if alt is down.
     */
    public boolean isAltDown()
    {
        return (modifiers & ALT_MASK) == ALT_MASK;
    }
    
    /**
     * Gets true, if alt graph is down.
     *  
     * @return true, if alt graph is down.
     */
    public boolean isAltGraphDown()
    {
        return (modifiers & ALT_GRAPH_MASK) == ALT_GRAPH_MASK;
    }
    
    /**
     * Gets true, if button1 is down.
     *  
     * @return true, if button1 is down.
     */
    public boolean isButton1Down()
    {
        return (modifiers & BUTTON1_MASK) == BUTTON1_MASK;
    }
    
    /**
     * Gets true, if button2 is down.
     *  
     * @return true, if button2 is down.
     */
    public boolean isButton2Down()
    {
        return (modifiers & BUTTON2_MASK) == BUTTON2_MASK;
    }
    
    /**
     * Gets true, if button3 is down.
     *  
     * @return true, if button3 is down.
     */
    public boolean isButton3Down()
    {
        return (modifiers & BUTTON3_MASK) == BUTTON3_MASK;
    }
    
    /**
     * Gets true, if the modifier is active.
     *  
     * @param pModifiers the modifiers
     * @param pModifier the modifier to check
     * @return true, if active
     */
    public static boolean isModifier(int pModifiers, int pModifier)
    {
        return (pModifiers & pModifier) == pModifier;
    }

    /**
     * Gets true, if shift is down.
     *  
     * @param pModifiers the modifiers
     * @return true, if shift is down.
     */
    public static boolean isShiftDown(int pModifiers)
    {
        return (pModifiers & SHIFT_MASK) == SHIFT_MASK;
    }
    
    /**
     * Gets true, if ctrl is down.
     *  
     * @param pModifiers the modifiers
     * @return true, if ctrl is down.
     */
    public static boolean isCtrlDown(int pModifiers)
    {
        return (pModifiers & CTRL_MASK) == CTRL_MASK;
    }
    
    /**
     * Gets true, if meta is down.
     *  
     * @param pModifiers the modifiers
     * @return true, if meta is down.
     */
    public static boolean isMetaDown(int pModifiers)
    {
        return (pModifiers & META_MASK) == META_MASK;
    }
    
    /**
     * Gets true, if alt is down.
     *  
     * @param pModifiers the modifiers
     * @return true, if alt is down.
     */
    public static boolean isAltDown(int pModifiers)
    {
        return (pModifiers & ALT_MASK) == ALT_MASK;
    }
    
    /**
     * Gets true, if alt graph is down.
     *  
     * @param pModifiers the modifiers
     * @return true, if alt graph is down.
     */
    public static boolean isAltGraphDown(int pModifiers)
    {
        return (pModifiers & ALT_GRAPH_MASK) == ALT_GRAPH_MASK;
    }
    
    /**
     * Gets true, if button1 is down.
     *  
     * @param pModifiers the modifiers
     * @return true, if button1 is down.
     */
    public static boolean isButton1Down(int pModifiers)
    {
        return (pModifiers & BUTTON1_MASK) == BUTTON1_MASK;
    }
    
    /**
     * Gets true, if button2 is down.
     *  
     * @param pModifiers the modifiers
     * @return true, if button2 is down.
     */
    public static boolean isButton2Down(int pModifiers)
    {
        return (pModifiers & BUTTON2_MASK) == BUTTON2_MASK;
    }
    
    /**
     * Gets true, if button3 is down.
     *  
     * @param pModifiers the modifiers
     * @return true, if button3 is down.
     */
    public static boolean isButton3Down(int pModifiers)
    {
        return (pModifiers & BUTTON3_MASK) == BUTTON3_MASK;
    }
    
}	// UIEvent
