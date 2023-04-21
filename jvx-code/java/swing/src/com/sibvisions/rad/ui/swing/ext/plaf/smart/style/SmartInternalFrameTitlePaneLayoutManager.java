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
package com.sibvisions.rad.ui.swing.ext.plaf.smart.style;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Insets;
import java.awt.LayoutManager;

import javax.swing.AbstractButton;
import javax.swing.JInternalFrame;

import com.sibvisions.rad.ui.swing.ext.plaf.smart.SmartLookAndFeel;
import com.sibvisions.rad.ui.swing.ext.plaf.smart.SmartTheme;

/**
 * The <code>SmartInternalFrameTitlePaneLayoutManager</code> is the layout
 * manager for all title panes of internal frames with the Smart/LF.
 * The layout manager was needed because java 1.5 doesn't allow buttons higher
 * than 16 pixels. Sind java 1.6 this problem is solved. To be independent
 * of the java version it's important to provide a special layout manager.
 * 
 * @author René Jahn
 */
public class SmartInternalFrameTitlePaneLayoutManager implements LayoutManager
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the associated internal frame. */
	private JInternalFrame frame = null;
	
	/** the menu button of the title pane. */
	private AbstractButton butMenu;

	/** the iconify button of the title pane. */
	private AbstractButton butMin;

	/** the maximize button of the title pane. */
	private AbstractButton butMax;

	/** the close button of the title pane. */
	private AbstractButton butClose;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public void addLayoutComponent(String name, Component pContainer)
	{
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeLayoutComponent(Component pContainer)
	{
	}

	/**
	 * Returns the minimum layout size.
	 * 
	 * @param pContainer the component to be laid out
	 * @return the preferred size dimension
	 * @see #minimumLayoutSize(Container)
	 */
	public Dimension preferredLayoutSize(Container pContainer)
	{
		return minimumLayoutSize(pContainer);
	}

	/**
	 * {@inheritDoc}
	 */
	public Dimension minimumLayoutSize(Container pContainer)
	{
		detectMembers(pContainer);
		
		int width = 0;
		int height = 0;

		int buttonCount = 0;

		//Berechnung des Darstellungsbereiches für die Buttons
		
		if (frame.isClosable())
		{
			width += SmartTheme.SIZE_INTFRAME_BUTTONS;
			height = Math.max(SmartTheme.SIZE_INTFRAME_BUTTONS, height);
			buttonCount++;
		}
		
		if (frame.isMaximizable())
		{
			width += SmartTheme.SIZE_INTFRAME_BUTTONS;
			height = Math.max(SmartTheme.SIZE_INTFRAME_BUTTONS, height);
			buttonCount++;
		}
		
		if (frame.isIconifiable())
		{
			width += SmartTheme.SIZE_INTFRAME_BUTTONS;
			height = Math.max(SmartTheme.SIZE_INTFRAME_BUTTONS, height);
			buttonCount++;
		}
		
		if (frame.getFrameIcon() != null)
		{
			Dimension dimPref = butMenu.getPreferredSize();
			width += dimPref.width;
			height = Math.max(dimPref.height, height);
		}
		
		width += Math.max(0, (buttonCount - 1) * SmartTheme.SPACE_INTFRAME_TITLE_BUTTONS);
		
		//Berechnung des Darstellungsbereiches für den Fenstertitel
		FontMetrics fm = pContainer.getFontMetrics(pContainer.getFont());

		String sTitle = frame.getTitle();
		
		int iTitleWidth = sTitle != null ? fm.stringWidth(sTitle) : 0;
		int iTitleLength = sTitle != null ? sTitle.length() : 0;

		// Leave room for three characters in the title.
		if (iTitleLength > 3)
		{
			int iSubTitleWidth = fm.stringWidth(sTitle.substring(0, 3) + "...");
			
			width += (iTitleWidth < iSubTitleWidth) ? iTitleWidth : iSubTitleWidth;
		}
		else
		{
			width += iTitleWidth;
		}

		//Für die Höhe wird zusätzlich zu den Buttons die Font-Höhe berücksichtigt!
		height = Math.max(fm.getHeight() + 2, height);
		
		width += SmartTheme.SPACE_INTFRAME_TITLE_TEXT + SmartTheme.SPACE_INTFRAME_TITLE_TEXT;

		Insets insets = pContainer.getInsets();
		
		height += insets.top + insets.bottom;
		width += insets.left + insets.right;
		
		return new Dimension(width, height);
	}

	/**
	 * {@inheritDoc}
	 */
	public void layoutContainer(Container pContainer)
	{
		Insets insets = pContainer.getInsets();
		
		if (SmartLookAndFeel.isLeftToRightOrientation(frame))
		{
			if (frame.getFrameIcon() != null)
			{
				alignMenu(pContainer, butMenu, insets, insets.left, false);
			}
			
			int x = pContainer.getWidth() - insets.right;				

			if (frame.isClosable())
			{
				x = center(pContainer, butClose, insets, x, true);
			}
			
			if (frame.isMaximizable())
			{
				x = center(pContainer, butMax, insets, x, true);
			}
			
			if (frame.isIconifiable())
			{
				x = center(pContainer, butMin, insets, x, true);
			}
		}
		else
		{
			if (frame.getFrameIcon() != null)
			{
				alignMenu(pContainer, butMenu, insets, pContainer.getWidth() - insets.right, true);
			}
			
			int x = insets.left;
			
			if (frame.isClosable())
			{
				x = center(pContainer, butClose, insets, x, false);
			}
			
			if (frame.isMaximizable())
			{
				x = center(pContainer, butMax, insets, x, false);
			}
			
			if (frame.isIconifiable())
			{
				x = center(pContainer, butMin, insets, x, false);
			}
		}		
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Searches the title buttons and the internal frame from a given component.
	 * It's assumed that the component is instance of BasicInternalFrameTitlePane.
	 * 
	 * @param pContainer an instance of BasicInternalFrameTitlePane
	 */
	private void detectMembers(Container pContainer)
	{
		if (frame == null)
		{
			AbstractButton[] buttons = SmartLookAndFeel.getNorthPaneButtons(pContainer);
			
			butMenu  = buttons[0];
			butMin   = buttons[1];
			butMax   = buttons[2];
			butClose = buttons[3];

			frame = SmartLookAndFeel.getInternalFrame(pContainer);
		}
	}
	
	/**
	 * Centers a component at the given place.
	 * 
	 * @param pContainer the parent of the component
	 * @param pComponent the component to be laid out
	 * @param pContainerInsets the insets of the container <code>pContainer</code>
	 * @param pX the x coordinate 
	 * @param pTrailing the orientation
	 * @return the calculated x coordinate, includes the orientation offset
	 */
	private int center(Container pContainer, Component pComponent, Insets pContainerInsets, int pX, boolean pTrailing)
	{
		int iSize = SmartTheme.SIZE_INTFRAME_BUTTONS;
		
		if (pTrailing)
		{
			pX -= iSize;
		}
		
		pComponent.setBounds(pX, pContainerInsets.top + (pContainer.getHeight() - pContainerInsets.top - pContainerInsets.bottom - iSize) / 2, iSize, iSize);
		
		if (pTrailing)
		{
			return pX - SmartTheme.SPACE_INTFRAME_TITLE_BUTTONS;
		}
		
		return pX + iSize + SmartTheme.SPACE_INTFRAME_TITLE_BUTTONS;
	}
	
	/**
	 * Aligns the menu button in the container.
	 * 
	 * @param pContainer the parent of the component
	 * @param pComponent the component to be laid out
	 * @param pContainerInsets the insets of the container <code>pContainer</code>
	 * @param pX the x coordinate 
	 * @param pTrailing the orientation
	 * @return the calculated x coordinate, includes the orientation offset
	 */
	private int alignMenu(Container pContainer, Component pComponent, Insets pContainerInsets, int pX, boolean pTrailing)
	{
		Dimension pref = pComponent.getPreferredSize();
		
		if (pTrailing)
		{
			pX -= pref.width;
		}
		
		//der Menü-Button muss immer die maximal Verfügbare Höhe haben, damit das
		//Menü an der richtigen Position dargestellt werden kann (nur für jdk 1.5)
		pComponent.setBounds
		(
			pX, 
			pContainerInsets.top, 
			pref.width, 
			pContainer.getHeight() - pContainerInsets.bottom - pContainerInsets.top
		);
		
		if (pref.width > 0)
		{
			if (pTrailing)
			{
				return pX - SmartTheme.SPACE_INTFRAME_TITLE_BUTTONS;
			}
			
			return pX + pref.width + SmartTheme.SPACE_INTFRAME_TITLE_BUTTONS;
		}
		
		return pX;
	}	
	
}	// SmartInternalFrameTitlePaneLayoutManager
