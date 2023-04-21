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
 * 17.10.2008 - [JR] - used the border insets
 */
package com.sibvisions.rad.ui.swing.ext.plaf.smart.style;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;

import javax.swing.ComboBoxEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;

import com.sibvisions.rad.ui.swing.ext.plaf.smart.SmartLookAndFeel;
import com.sibvisions.rad.ui.swing.ext.plaf.smart.SmartTheme;

/**
 * The <code>SmartComboBoxLayoutManager</code> is the layout manager for combo boxes.
 * It paints the arrow button independent of the combo box insets.
 * 
 * @author René Jahn
 */
public class SmartComboBoxLayoutManager implements LayoutManager
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** the arrow button instance of the combo box. */
	private JButton arrowButton;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>SmartComboBoxLayoutManager</code> for
	 * a combo box.
	 * 
	 *  @param pComboBox the desired combo box
	 */
	public SmartComboBoxLayoutManager(JComboBox pComboBox)
	{
		arrowButton = SmartLookAndFeel.getComboBoxArrowButton(pComboBox);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public void addLayoutComponent(String pName, Component pComponent)
	{
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeLayoutComponent(Component pComponent)
	{
	}

	/**
	 * {@inheritDoc}
	 */
	public Dimension minimumLayoutSize(Container pContainer)
	{
		return pContainer.getMinimumSize();
	}

	/**
	 * {@inheritDoc}
	 */
	public Dimension preferredLayoutSize(Container pContainer)
	{
		return pContainer.getPreferredSize();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void layoutContainer(Container pContainer)
	{
        JComboBox box = (JComboBox)pContainer;
        
        int iWidth = box.getWidth();
        int iHeight = box.getHeight();
        
        //Paints the arrow button independent of the combo box insets
        if (arrowButton != null) 
        {
            int iButtonWidth = SmartTheme.WIDTH_TEXT_ARROWBUTTONS;

            if (SmartLookAndFeel.isLeftToRightOrientation(box)) 
	        {
			    arrowButton.setBounds(iWidth - iButtonWidth, 0, iButtonWidth, iHeight);
			}
			else 
			{
			    arrowButton.setBounds(0, 0, iButtonWidth, iHeight);
			}
        }
        
        ComboBoxEditor editor = box.getEditor();
        
        if (editor != null) 
        {
        	Rectangle cvb = rectangleForCurrentValue(box);
            editor.getEditorComponent().setBounds(cvb);
        }		
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
     * Returns the area that is reserved for drawing the combo box editor.
     * 
     * @param pComboBox the combo box
     * @return the area for the combo box editor
     */
    protected Rectangle rectangleForCurrentValue(JComboBox pComboBox) 
    {
        int iWidth = pComboBox.getWidth();
        int iHeight = pComboBox.getHeight();
        
        //Die Insets vom Border verwenden, damit die Insets der Komponente egal sind!
        Insets insBox = SmartLookAndFeel.getBorderInsets(pComboBox);
        
        int iButtonWidth  = 0;

        
		if (arrowButton != null) 
		{
			iButtonWidth  = SmartTheme.WIDTH_TEXT_ARROWBUTTONS;
		}
	
		if (SmartLookAndFeel.isLeftToRightOrientation(pComboBox)) 
		{
		    return new Rectangle(insBox.left, 
		    		             insBox.top, 
		    		             iWidth - (insBox.left + insBox.right + iButtonWidth),
	                             iHeight - (insBox.top + insBox.bottom));
		}
		else 
		{
		    return new Rectangle(insBox.left + iButtonWidth, 
		    		             insBox.top,
				                 iWidth - (insBox.left + insBox.right + iButtonWidth),
	                             iHeight - (insBox.top + insBox.bottom));
		}
    }

}	// SmartComboBoxLayoutManager
