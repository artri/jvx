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
package com.sibvisions.rad.ui.swing.ext.plaf.smart.painter;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.border.Border;

import com.sibvisions.rad.ui.swing.ext.plaf.smart.SmartTheme;

/**
 * The <code>SmartComboPopupBorder</code> is the border painter for combo box
 * popup lists.
 * 
 * @author René Jahn
 */
public class SmartComboPopupBorder implements Border
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** the insets of the border. */
	private static Insets insBorder = new Insets(1, 1, 1, 1); 
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */	
	public void paintBorder(Component pComponent, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight)
	{
		SmartPainter.paintRectangle(pGraphics, pX, pY, pWidth, pHeight, 1, -1, null, null, -1, SmartTheme.COL_TEXT_BORDER);
	}
	
	/**
	 * Gets the insets for the border.
	 * 
	 * @param pComponent the component which requests the insets
	 * @return the insets with value 0
	 */
	public Insets getBorderInsets(Component pComponent)
	{
		return insBorder;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isBorderOpaque()
	{
		return false;
	}

}	// SmartComboPopupBorder
