/*
 * Copyright 2014 SIB Visions GmbH
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
 * 28.03.2014 - [HM] - creation
 */
package com.sibvisions.rad.ui.swing.ext;

import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JRadioButton;

/**
 * The <code>JVxRadioButton</code> class is a radio button that checks the mouse position.
 * The state changes only, if the mouse position is inside the radio button rectangle.
 *  
 * @author Martin Handsteiner
 */
public class JVxRadioButton extends JRadioButton
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Creates an initially unselected radio button with no set text or icon.
     */
	public JVxRadioButton()
	{
		super();
	}
	
    /**
     * Creates a radio button whose properties are taken from the Action supplied.
     *
     * @param pAction the action of the radio button
     */
	public JVxRadioButton(Action pAction)
	{
		super(pAction);
	}
	
    /**
     * Creates an initially unselected radio button with an icon.
     *
     * @param pIcon the icon of the radio button
     */
	public JVxRadioButton(Icon pIcon)
	{
		super(pIcon);
	}
	
    /**
     * Creates an initially unselected radio button with text.
     *
     * @param pText the text of the radio button
     */
	public JVxRadioButton(String pText)
	{
		super(pText);
	}
	
    /**
     * Creates a radio button with the specified text and selection state.
     *
     * @param pText the text of the radio button 
     * @param pSelected the selected state of the radio button
     */
	public JVxRadioButton(String pText, boolean pSelected)
	{
		super(pText, pSelected);
	}
	
    /**
     * Creates an initially unselected radio button with the specified text and icon.
     *
     * @param pText the text of the radio button
     * @param pIcon the icon of the radio button
     */
	public JVxRadioButton(String pText, Icon pIcon)
	{
		super(pText, pIcon);
	}
	
    /**
     * Creates a radio button with the specified text, icon, and selection state.
     *
     * @param pText the text of the radio button
     * @param pIcon the icon of the radio button
     * @param pSelected the selected state of the radio button
     */
	public JVxRadioButton(String pText, Icon pIcon, boolean pSelected)
	{
		super(pText, pIcon, pSelected);
	}

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
    protected void processMouseEvent(MouseEvent pMouseEvent)
    {
        Rectangle bounds = getBounds();
        Dimension prefSize = getPreferredSize();
        
        int horizontalAlignment = getHorizontalAlignment();
        if (getComponentOrientation() == ComponentOrientation.RIGHT_TO_LEFT)
        {
            if (horizontalAlignment == JVxConstants.LEADING)
            {
                horizontalAlignment = JVxConstants.RIGHT;
            }
            else if (horizontalAlignment == JVxConstants.TRAILING)
            {
                horizontalAlignment = JVxConstants.LEFT;
            }
        }
        else
        {
            if (horizontalAlignment == JVxConstants.LEADING)
            {
                horizontalAlignment = JVxConstants.LEFT;
            }
            else if (horizontalAlignment == JVxConstants.TRAILING)
            {
                horizontalAlignment = JVxConstants.RIGHT;
            }
        }
        
        switch (horizontalAlignment)
        {
            case JVxConstants.LEFT:
                bounds.x = 0;
                bounds.width = prefSize.width;
                break;
            case JVxConstants.CENTER:
                bounds.x = (bounds.width - prefSize.width) / 2;
                bounds.width = prefSize.width;
                break;
            case JVxConstants.RIGHT:
                bounds.x = bounds.width - prefSize.width;
                bounds.width = prefSize.width;
                break;
            default:
                bounds.x = 0;
        }
        switch (getVerticalAlignment())
        {
            case JVxConstants.TOP:
                bounds.y = 0;
                bounds.height = prefSize.height;
                break;
            case JVxConstants.CENTER:
                bounds.y = (bounds.height - prefSize.height) / 2;
                bounds.height = prefSize.height;
                break;
            case JVxConstants.RIGHT:
                bounds.y = bounds.height - prefSize.height;
                bounds.height = prefSize.height;
                break;
            default:
                bounds.y = 0;
        }
        
        if (bounds.contains(pMouseEvent.getX(), pMouseEvent.getY()))
        {
            super.processMouseEvent(pMouseEvent);
        }
    }
    
}	// JVxRadioButton
