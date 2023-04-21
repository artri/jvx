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
 * 10.10.2008 - [HM] - creation
 */
package com.sibvisions.rad.ui.swing.ext;

import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.rad.ui.celleditor.IStyledCellEditor;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;

import com.sibvisions.rad.ui.swing.impl.SwingFactory;

/**
 * The <code>JVxToggleButton</code> is a <code>JToggleButton</code>
 * extension.
 * 
 * @author Martin Handsteiner
 * @see	javax.swing.JToggleButton
 */
public class JVxToggleButton extends JToggleButton
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Creates an initially unselected toggle button
     * without setting the text or image.
     */
    public JVxToggleButton() 
    {
        super(null, null, false);
        
        init();
    }

    /**
     * Creates an initially unselected toggle button
     * with the specified image but no text.
     *
     * @param pIcon  the image that the button should display
     */
    public JVxToggleButton(Icon pIcon) 
    {
        super(null, pIcon, false);
        
        init();
    }
    
    /**
     * Creates a toggle button with the specified image 
     * and selection state, but no text.
     *
     * @param pIcon  the image that the button should display
     * @param pSelected  if true, the button is initially selected;
     *                  otherwise, the button is initially unselected
     */
    public JVxToggleButton(Icon pIcon, boolean pSelected) 
    {
        super(null, pIcon, pSelected);
        
        init();
    }
    
    /**
     * Creates an unselected toggle button with the specified text.
     *
     * @param pText  the string displayed on the toggle button
     */
    public JVxToggleButton(String pText) 
    {
        super(pText, null, false);
        
        init();
    }

    /**
     * Creates a toggle button with the specified text
     * and selection state.
     *
     * @param pText  the string displayed on the toggle button
     * @param pSelected  if true, the button is initially selected;
     *                  otherwise, the button is initially unselected
     */
    public JVxToggleButton(String pText, boolean pSelected) 
    {
        super(pText, null, pSelected);
        
        init();
    }

    /**
     * Creates a toggle button where properties are taken from the 
     * Action supplied.
     * @param pAction the action.
     */
    public JVxToggleButton(Action pAction) 
    {
        super();
        
        setAction(pAction);
        
        init();
    }

    /**
     * Creates a toggle button that has the specified text and image,
     * and that is initially unselected.
     *
     * @param pText the string displayed on the button
     * @param pIcon  the image that the button should display
     */
    public JVxToggleButton(String pText, Icon pIcon) 
    {
        super(pText, pIcon, false);
        
        init();
    }

    /**
     * Creates a toggle button with the specified text, image, and
     * selection state.
     *
     * @param pText the text of the toggle button
     * @param pIcon  the image that the button should display
     * @param pSelected  if true, the button is initially selected;
     *                  otherwise, the button is initially unselected
     */
    public JVxToggleButton(String pText, Icon pIcon, boolean pSelected) 
    {
    	super(pText, pIcon, pSelected);
    	
    	init();
    }
    
    /**
     * Initializes the button.
     */
    private void init()
    {
    	if (SwingFactory.isMacLaF())
    	{
    		//otherwise a toggle button in the toolbar won't show the border
    		putClientProperty("JButton.buttonType", "toggle");
    	}
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
        IStyledCellEditor cellEditor = (IStyledCellEditor)getClientProperty("cellEditor");
        if (cellEditor != null)
        {
            Rectangle bounds = getBounds();
            Dimension prefSize = getPreferredSize();
            
            int horizontalAlignment = SwingFactory.getHorizontalSwingAlignment(cellEditor.getHorizontalAlignment());
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
            switch (SwingFactory.getHorizontalSwingAlignment(cellEditor.getVerticalAlignment()))
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
        else
        {
            super.processMouseEvent(pMouseEvent);
        }
    }
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Returns the <code>KeyStroke</code> which serves as an accelerator 
     * for the menu item.
     * @return a <code>KeyStroke</code> object identifying the
     *		accelerator key
     */
    public KeyStroke getAccelerator() 
    {
        return JVxButton.getAccelerator(this);
    }
    
    /**
     * Sets the key combination which invokes the menu item's
     * action listeners without navigating the menu hierarchy. It is the
     * UI's responsibility to install the correct action.  Note that 
     * when the keyboard accelerator is typed, it will work whether or
     * not the menu is currently displayed.
     *
     * @param pKeyStroke the <code>KeyStroke</code> which will
     *		serve as an accelerator 
     */
    public void setAccelerator(KeyStroke pKeyStroke) 
    {
    	JVxButton.setAccelerator(this, pKeyStroke);
    }

    /**
     * Gets, if the border should only be shown on mouse entered.
     *
     * @return true, if the border should only be shown on mouse entered.
     */
    public boolean isBorderOnMouseEntered()
    {
        return JVxButton.isBorderOnMouseEntered(this);
    }

    /**
     * Sets, if the border should only be shown on mouse entered.
     *
     * @param pBorderOnMouseEntered true, if the border should only be shown on mouse entered.
     */
    public void setBorderOnMouseEntered(boolean pBorderOnMouseEntered)
    {
    	JVxButton.setBorderOnMouseEntered(this, pBorderOnMouseEntered);
    }

}	// JVxToggleButton
