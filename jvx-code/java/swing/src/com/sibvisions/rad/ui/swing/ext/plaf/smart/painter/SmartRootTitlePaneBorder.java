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

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.border.Border;

import com.sibvisions.rad.ui.swing.ext.plaf.smart.SmartRootTitlePane;
import com.sibvisions.rad.ui.swing.ext.plaf.smart.SmartTheme;

/**
 * The <code>SmartRootTitlePaneBorder</code> is the border painter for title panes of
 * dialogs and frames.
 * 
 * @author René Jahn
 */
public class SmartRootTitlePaneBorder implements Border
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */	
	public void paintBorder(Component pComponent, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight)
	{
		Color color;
		
		if (((SmartRootTitlePane)pComponent).getWindow().isActive())
		{
			color = SmartTheme.COL_INTFRAME_INNER_BORDER_ACTIVE;
		}
		else
		{
			color = SmartTheme.COL_INTFRAME_INNER_BORDER_INACTIVE;
		}
		
		pGraphics.setColor(color);
		pGraphics.drawLine(pX, pY + pHeight - 1, pX + pWidth, pY + pHeight - 1);
	}
	
	/**
	 * Gets the insets for the border. The insets will be defined in the "internalFrame" area
	 * of smart.xml.
	 * 
	 * @param pComponent the component which requests the insets
	 * @return the "JInternalFrame" insets
	 */
	public Insets getBorderInsets(Component pComponent)
	{
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isBorderOpaque()
	{
		return false;
	}

}	// SmartRootTitlePaneBorder
