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
 * 04.04.2014 - [RZ] - #1 - added getKey(UIKeyEvent)
 */
package javax.rad.ui.event;

import com.sibvisions.util.Internalize;

/**
 * Stores Key Event Data for Accelerator.
 * 
 * @author Martin Handsteiner
 * @see    java.awt.event.KeyEvent
 */
public final class Key
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /**
     * <code>keyChar</code> is a valid unicode character
     * that is fired by a key or a key combination on
     * a keyboard.
     */
    private char keyChar;

    /**
     * The unique value assigned to each of the keys on the
     * keyboard.  There is a common set of key codes that
     * can be fired by most keyboards.
     * The symbolic name for a key code should be used rather
     * than the code value itself.
     */
    private int keyCode;

    /** The state of the modifier mask at the time the input event was fired. */
    private int modifiers;

    /** The KeyEvent that occurs. */
    private int keyEvent;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Constructs an <code>Key</code> with the specified
     * values. <code>Key</code>s should not be constructed
     * by client code. Use a variant of <code>getKey...</code>
     * instead.
     *
     * @param pKeyChar the character value for a keyboard key
     * @param pKeyCode the key code for this <code>Key</code>
     * @param pModifiers a bitwise-ored combination of any modifiers
     * @param pKeyEvent UIKeyEvent.KEY_PRESSED, UIKeyEvent.KEY_RELEASED, UIKeyEvent.KEY_TYPED
     */
	private Key(char pKeyChar, int pKeyCode, int pModifiers, int pKeyEvent)
	{
		keyChar = pKeyChar;
		keyCode = pKeyCode;
		modifiers = pModifiers;
		keyEvent = pKeyEvent;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
     * Returns true if this object is identical to the specified object.
     *
     * @param pObject the Object to compare this object to
     * @return true if the objects are identical
     */
    @Override
    public final boolean equals(Object pObject) 
    {
        if (pObject instanceof Key) 
        {
        	Key key = (Key)pObject;
		    return key.keyChar == keyChar && key.keyCode == keyCode && key.modifiers == modifiers && key.keyEvent == keyEvent;
        }
        return false;
    }
    
	/**
     * Returns a numeric value for this object that is likely to be unique,
     * making it a good choice as the index value in a hash table.
     *
     * @return an int that represents this object
     */
    public int hashCode() 
    {
        return ((int)keyChar + 1) * (keyCode + 1) * (modifiers + 1) * keyEvent;
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
     * Returns the character for this <code>Key</code>.
     *
     * @return a char value
     */
    public final char getKeyChar() 
    {
        return keyChar;
    }

    /**
     * Returns the numeric key code for this <code>Key</code>.
     *
     * @return an int containing the key code value
     */
    public final int getKeyCode() 
    {
        return keyCode;
    }

    /**
     * Returns the modifier keys for this <code>Key</code>.
     *
     * @return an int containing the modifiers
     */
    public final int getModifiers() 
    {
        return modifiers;
    }

    /**
     * Returns the type of <code>KeyEvent</code> which corresponds to
     * this <code>Key</code>.
     *
     * @return <code>KeyEvent.KEY_PRESSED</code>,
     *         <code>KeyEvent.KEY_TYPED</code>,
     *         or <code>KeyEvent.KEY_RELEASED</code>
     */
    public final int getKeyEventType() 
    {
    	return keyEvent;
    }

    /**
     * Returns a shared instance of an <code>Key</code>
     * that represents the <code>Key</code> of the given {@link UIKeyEvent}.
     * 
     * @param pEvent the {@link UIKeyEvent} for this key
     * @return an <code>Key</code> object for the given {@link UIKeyEvent}
     */
    public static Key getKey(UIKeyEvent pEvent)
    {
    	if (pEvent.getId() == UIKeyEvent.KEY_TYPED)
    	{
        	return Internalize.intern(new Key(pEvent.getKeyChar(), UIKeyEvent.VK_UNDEFINED, pEvent.getModifiers(), pEvent.getId()));
    	}
    	else
    	{
        	return Internalize.intern(new Key(UIKeyEvent.CHAR_UNDEFINED, pEvent.getKeyCode(), pEvent.getModifiers(), pEvent.getId()));
    	}
    }
    
    /**
     * Returns a shared instance of an <code>Key</code> 
     * that represents a <code>KEY_TYPED</code> event for the 
     * specified character.
     *
     * @param pKeyChar the character value for a keyboard key
     * @return an <code>Key</code> object for that key
     */
    public static Key getKey(char pKeyChar)
    {
    	return Internalize.intern(new Key(pKeyChar, UIKeyEvent.VK_UNDEFINED, 0, UIKeyEvent.KEY_TYPED));
    }

    /**
     * Returns a shared instance of an <code>Key</code>,
     * for the specified character.
     *
     * The modifiers <ul>
     * <li>javax.rad.ui.event.UIEvent.SHIFT_MASK 
     * <li>javax.rad.ui.event.UIEvent.CTRL_MASK 
     * <li>javax.rad.ui.event.UIEvent.META_MASK 
     * <li>javax.rad.ui.event.UIEvent.ALT_MASK
     * <li>javax.rad.ui.event.UIEvent.ALT_GRAPH_MASK
     * </ul> 
     *
     * Since these numbers are all different powers of two, any combination of
     * them is an integer in which each bit represents a different modifier
     * key. Use 0 to specify no modifiers.
     *
     * @param pKeyChar the Character for a keyboard character
     * @param pModifiers a bitwise-ored combination of any modifiers
     * @return an <code>Key</code> object for that key
     */
    public static Key getKey(char pKeyChar, int pModifiers) 
    {
    	return Internalize.intern(new Key(pKeyChar, UIKeyEvent.VK_UNDEFINED, pModifiers, UIKeyEvent.KEY_TYPED));
    }

    /**
     * Returns a shared instance of an <code>Key</code> 
     * that represents a <code>KEY_PRESSED</code> event for the 
     * specified key code.
     *
     * @param pKeyCode the key code value for a keyboard key
     * @return an <code>Key</code> object for that key
     */
    public static Key getKeyOnPressed(int pKeyCode) 
    {
        return Internalize.intern(new Key(UIKeyEvent.CHAR_UNDEFINED, pKeyCode, 0, UIKeyEvent.KEY_PRESSED));
    }

    /**
     * Returns a shared instance of an <code>Key</code>,
     * for the specified key code. Note
     * that the first parameter is of type Character rather than
     * char. This is to avoid inadvertent clashes with
     * calls to <code>getKey(int keyCode, int modifiers)</code>.
     *
     * The modifiers <ul>
     * <li>javax.rad.ui.event.UIEvent.SHIFT_MASK 
     * <li>javax.rad.ui.event.UIEvent.CTRL_MASK 
     * <li>javax.rad.ui.event.UIEvent.META_MASK 
     * <li>javax.rad.ui.event.UIEvent.ALT_MASK
     * <li>javax.rad.ui.event.UIEvent.ALT_GRAPH_MASK
     * </ul> 
     *
     * Since these numbers are all different powers of two, any combination of
     * them is an integer in which each bit represents a different modifier
     * key. Use 0 to specify no modifiers.
     *
     * @param pKeyCode the key code for a keyboard character
     * @param pModifiers a bitwise-ored combination of any modifiers
     * @return an <code>Key</code> object for that key
     */
    public static Key getKeyOnPressed(int pKeyCode, int pModifiers) 
    {
    	return Internalize.intern(new Key(UIKeyEvent.CHAR_UNDEFINED, pKeyCode, pModifiers, UIKeyEvent.KEY_PRESSED));
    }

    /**
     * Returns a shared instance of an <code>Key</code> 
     * that represents a <code>KEY_RELEASED</code> event for the 
     * specified key code.
     *
     * @param pKeyCode the key code value for a keyboard key
     * @return an <code>Key</code> object for that key
     */
    public static Key getKeyOnReleased(int pKeyCode) 
    {
        return Internalize.intern(new Key(UIKeyEvent.CHAR_UNDEFINED, pKeyCode, 0, UIKeyEvent.KEY_RELEASED));
    }

    /**
     * Returns a shared instance of an <code>Key</code>,
     * for the specified key code. Note
     * that the first parameter is of type Character rather than
     * char. This is to avoid inadvertent clashes with
     * calls to <code>getKey(int keyCode, int modifiers)</code>.
     *
     * The modifiers <ul>
     * <li>javax.rad.ui.event.UIEvent.SHIFT_MASK 
     * <li>javax.rad.ui.event.UIEvent.CTRL_MASK 
     * <li>javax.rad.ui.event.UIEvent.META_MASK 
     * <li>javax.rad.ui.event.UIEvent.ALT_MASK
     * <li>javax.rad.ui.event.UIEvent.ALT_GRAPH_MASK
     * </ul> 
     *
     * Since these numbers are all different powers of two, any combination of
     * them is an integer in which each bit represents a different modifier
     * key. Use 0 to specify no modifiers.
     *
     * @param pKeyCode the key code for a keyboard character
     * @param pModifiers a bitwise-ored combination of any modifiers
     * @return an <code>Key</code> object for that key
     */
    public static Key getKeyOnReleased(int pKeyCode, int pModifiers) 
    {
    	return Internalize.intern(new Key(UIKeyEvent.CHAR_UNDEFINED, pKeyCode, pModifiers, UIKeyEvent.KEY_RELEASED));
    }
    
    /**
     * Returns true if the Key matches the given KeyEvent.
     *
     * @param pEvent the <code>KeyEvent</code>
     * @return true if the Key matches the given KeyEvent.
     * @throws NullPointerException if <code>anEvent</code> is null
     */
    public boolean equals(UIKeyEvent pEvent) 
    {
    	if (pEvent.getId() == UIKeyEvent.KEY_TYPED)
    	{
        	return keyChar == pEvent.getKeyChar() && modifiers == pEvent.getModifiers();
    	}
    	else
    	{
        	return keyCode == pEvent.getKeyCode() && modifiers == pEvent.getModifiers();
    	}
    }
    
}	// Key
