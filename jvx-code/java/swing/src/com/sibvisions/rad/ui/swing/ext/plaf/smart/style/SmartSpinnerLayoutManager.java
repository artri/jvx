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
 * 14.10.2008 - [JR] - creation
 * 17.10.2008 - [JR] - used the border insets
 */
package com.sibvisions.rad.ui.swing.ext.plaf.smart.style;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;

import javax.swing.plaf.UIResource;

import com.sibvisions.rad.ui.swing.ext.plaf.smart.SmartLookAndFeel;
import com.sibvisions.rad.ui.swing.ext.plaf.smart.SmartTheme;

/**
 * The <code>SmartSpinnerLayoutManager</code> is a simple layout manager for 
 * the editor and the next/previous buttons. It's a copy of the 
 * SynthSpinnerUI.SynthSpinnerLayout, but the buttons ignore the insets of the
 * spinner.
 * 
 * @author René Jahn
 */
public class SmartSpinnerLayoutManager implements LayoutManager, UIResource
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the next button component. */
	private Component	compNext	= null;

	/** the previous button component. */
	private Component	compPrev	= null;

	/** the editor component. */
	private Component	compEdit	= null;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public void addLayoutComponent(String pName, Component pComponent)
	{
		if ("Next".equals(pName))
		{
			compNext = pComponent;
		}
		else if ("Previous".equals(pName))
		{
			compPrev = pComponent;
		}
		else if ("Editor".equals(pName))
		{
			compEdit = pComponent;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeLayoutComponent(Component pComponent)
	{
		if (pComponent == compNext)
		{
			pComponent = null;
		}
		else if (pComponent == compPrev)
		{
			compPrev = null;
		}
		else if (pComponent == compEdit)
		{
			compEdit = null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Dimension minimumLayoutSize(Container pContainer)
	{
		return preferredLayoutSize(pContainer);
	}

	/**
	 * {@inheritDoc}
	 */
	public Dimension preferredLayoutSize(Container pContainer)
	{
		Dimension dimNext = getPreferredSize(compNext);
		Dimension dimPrev = getPreferredSize(compPrev);
		Dimension dimEdit = getPreferredSize(compEdit);

		// Die Höhe des Editors sollte immer ein vielfaches von 2 sein
		dimEdit.height = ((dimEdit.height + 1) / 2) * 2;

		//Editor-Breite/Höhe
		Dimension dimSize = new Dimension(dimEdit.width, dimEdit.height);
		
		//Button-Breite (die Höhe ergibt sich automatisch)
		dimSize.width += Math.max(dimNext.width, dimPrev.width);
		
		//Insets berücksichtigen
		Insets insets = SmartLookAndFeel.getBorderInsets(pContainer);
		
		dimSize.width += insets.left + insets.right;
		dimSize.height += insets.top + insets.bottom;
		
		return dimSize;
	}

	/**
	 * {@inheritDoc}
	 */
	public void layoutContainer(Container pContainer)
	{
		Insets insets = SmartLookAndFeel.getBorderInsets(pContainer);

		int iHeight = pContainer.getHeight();
		int iWidth  = pContainer.getWidth();
		
		int iWidthButton = SmartTheme.WIDTH_TEXT_ARROWBUTTONS;
		int iWidthEdit = iWidth - iWidthButton - insets.left - insets.right;

		int iHeightNext = iHeight / 2;
		int iHeightPrev = iHeight - iHeightNext; 

		
		/* 
		 * Orientation des Editors berücksichtigen
		 */
		int iEditorX;
		int iButtonX;
		
		if (SmartLookAndFeel.isLeftToRightOrientation(pContainer))
		{
			iEditorX = insets.left;
			iButtonX = iWidth - iWidthButton;
		}
		else
		{
			iButtonX = 0;
			iEditorX = iWidthButton + insets.left;
		}

		setBounds(compEdit, iEditorX, insets.top, iWidthEdit, iHeight - insets.top - insets.bottom);
		setBounds(compNext, iButtonX, 0, iWidthButton, iHeightNext);
		setBounds(compPrev, iButtonX, iHeightNext, iWidthButton, iHeightPrev);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the preferred size of a component.
	 * 
	 * @param pComponent the component
	 * @return the preferred size of <code>pComponent</code> or a dimension
	 *         with width and height of 0, if the <code>pComponent</code> is 
	 *         <code>null</code>
	 */
	private Dimension getPreferredSize(Component pComponent)
	{
		return (pComponent == null) ? new Dimension(0, 0) : pComponent.getPreferredSize();
	}

	/**
	 * Sets the bounds of a component, if the component is not <code>null</code>.
	 * 
	 * @param pComponent the component
	 * @param pX the x position
	 * @param pY the y position
	 * @param pWidth the width
	 * @param pHeight the height
	 */
	private void setBounds(Component pComponent, int pX, int pY, int pWidth, int pHeight)
	{
		if (pComponent != null)
		{
			pComponent.setBounds(pX, pY, pWidth, pHeight);
		}
	}
	
}	// SmartSpinnerLayoutManager
