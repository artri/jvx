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
 * 12.10.2008 - [JR] - paintBorder: check UI (necessary when switching between LaFs)
 * 15.11.2008 - [JR] - paintborder: avoid NullPointerException when component == null 
 */
package com.sibvisions.rad.ui.swing.ext.plaf.smart.painter;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Window;

import javax.swing.JComponent;
import javax.swing.JRootPane;
import javax.swing.border.Border;
import javax.swing.plaf.RootPaneUI;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthLookAndFeel;

import com.sibvisions.rad.ui.swing.ext.plaf.smart.SmartRootPaneUI;
import com.sibvisions.rad.ui.swing.ext.plaf.smart.SmartTheme;

/**
 * The <code>SmartRootPaneBorder</code> is the border painter for dialogs and frames.
 * 
 * @author René Jahn
 */
public class SmartRootPaneBorder implements Border
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** the insets of the border. */
	private static Insets insBorder = null; 
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */	
	public void paintBorder(Component pComponent, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight)
	{
		if (pComponent != null)
		{
			RootPaneUI ui = ((JRootPane)pComponent).getUI();
		
			if (ui instanceof SmartRootPaneUI
				&& ((SmartRootPaneUI)ui).getTitlePane() != null)
			{
				Window window = (Window)pComponent.getParent(); 
				
				Color colOuterBorder;
				Color colInnerBorder;
				Color colBack;
				
				if (window.isActive())
				{
					colOuterBorder = SmartTheme.COL_INTFRAME_OUTER_BORDER_ACTIVE;
					colInnerBorder = SmartTheme.COL_INTFRAME_INNER_BORDER_ACTIVE;
					
					if (window instanceof Dialog && ((Dialog)window).isModal())
					{
						colBack = SmartTheme.COL_INTFRAME_MODAL_BACKGROUND_ACTIVE;
					}
					else
					{
						colBack = SmartTheme.COL_INTFRAME_BACKGROUND_ACTIVE;
					}
				}
				else
				{
					colOuterBorder = SmartTheme.COL_INTFRAME_OUTER_BORDER_INACTIVE;
					colInnerBorder = SmartTheme.COL_INTFRAME_INNER_BORDER_INACTIVE;
					colBack = SmartTheme.COL_INTFRAME_BACKGROUND_INACTIVE;
				}
		
				SmartPainter.paintRectangle(pGraphics, pX, pY, pWidth, pHeight, 1, -1, colBack, null, -1, colOuterBorder);
		
				Insets insWindow = getBorderInsets(pComponent);
				
				int iTitleHeight = ((SmartRootPaneUI)ui).getTitlePane().getHeight();
				
				pGraphics.setColor(colInnerBorder);
		    	pGraphics.drawRect(pX + insWindow.left - 1, 
		    			           pY + insWindow.top + iTitleHeight - 1, 
		    					   pWidth - insWindow.left - insWindow.right + 1, 
		    					   pHeight - insWindow.top - iTitleHeight - insWindow.bottom + 1);
			}
		}
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
		//get the insets of JInternalFrame definition (defined in smart.xml)
		if (insBorder == null)
		{
			insBorder = SynthLookAndFeel.getStyle((JComponent)pComponent, Region.INTERNAL_FRAME).getInsets(null, null); 
		}
		
		return insBorder;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isBorderOpaque()
	{
		return false;
	}

}	// SmartRootPaneBorder
