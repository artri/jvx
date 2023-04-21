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
 * 09.12.2008 - [HM] - creation
 * 12.03.2010 - [JR] - #87: setBackground now sets the viewports background color
 */
package com.sibvisions.rad.ui.swing.impl;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;

import javax.rad.ui.IColor;
import javax.rad.ui.ICursor;
import javax.rad.ui.IFont;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

import com.sibvisions.rad.ui.awt.impl.AwtColor;
import com.sibvisions.rad.ui.awt.impl.AwtCursor;
import com.sibvisions.rad.ui.awt.impl.AwtFont;

/**
 * The <code>SwingComponent</code> is a component with scrollbars.
 * 
 * @author Martin Handsteiner
 * @param <C> an instance of {@link JComponent}
 * @param <SC> The Sub component {@link JComponent}
 */
public class SwingScrollComponent<C extends JComponent, SC extends JComponent> extends SwingComponent<C>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class Members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The component. */
	protected SC component;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>SwingScrollComponent</code>.
	 * 
	 * @param pJComponent an instance of {@link JComponent}
	 */
	protected SwingScrollComponent(C pJComponent)
	{
		super(pJComponent);
		
		if (pJComponent instanceof JScrollPane)
		{
			component = (SC)((JScrollPane)pJComponent).getViewport().getView();
		}
		else
		{
			component = (SC)pJComponent;
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Adds a mouse listener for this component, if not already added.
	 */
	protected void addMouseListener()
	{
		if (!bMouseListener)
		{
			bMouseListener = true;
			component.addMouseListener(this);
		}
	}
	
	/**
	 * Adds a key listener for this component, if not already added.
	 */
	protected void addKeyListener()
	{
		if (!bKeyListener)
		{
			bKeyListener = true;
			component.addKeyListener(this);
		}
	}
    
	/**
	 * Adds a mouse listener for this component, if not already added.
	 */
	protected void addFocusListener()
	{
		if (!bFocusListener)
		{
			bFocusListener = true;
			component.addFocusListener(this);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setToolTipText(String pText)
	{
		component.setToolTipText(pText);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getToolTipText()
	{
		return component.getToolTipText();
	}
	/**
	 * {@inheritDoc}
	 */
	public String getName()
	{
		return component.getName();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setName(String pName)
	{
		component.setName(pName);
		if (pName == null)
		{
            resource.setName(pName);
		}
		else
		{
		    resource.setName("Scroll." + pName);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public IColor getBackground()
	{
		Color color = component.getBackground();
		if (color == null)
		{
			return null;
		}
		else
		{
			return new AwtColor(color);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void setBackground(IColor pBackground)
	{
		JViewport cViewport = null;
		
		if (resource instanceof JScrollPane)
		{
			cViewport = ((JScrollPane)resource).getViewport();
		}
		
		if (pBackground != null)
		{
			if (cViewport != null)
			{
				cViewport.setBackground((Color)pBackground.getResource());
			}
			
			resource.setBackground((Color)pBackground.getResource());
			component.setBackground((Color)pBackground.getResource());			
		}
		else
		{
			if (cViewport != null)
			{
				cViewport.setBackground(null);
			}

			component.setBackground(null);
			resource.setBackground(null);
		}
		if (cViewport != null)
		{
			cViewport.setOpaque(pBackground != null);
		}
		resource.setOpaque(pBackground != null);
		component.setOpaque(pBackground != null);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isBackgroundSet()
	{
		return component.isBackgroundSet();
	}

	/**
	 * {@inheritDoc}
	 */
	public IColor getForeground()
	{
		Color color = component.getForeground();
		if (color == null)
		{
			return null;
		}
		else
		{
			return new AwtColor(color);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void setForeground(IColor pForeground)
	{
		if (pForeground != null)
		{
			resource.setForeground((Color)pForeground.getResource());
			component.setForeground((Color)pForeground.getResource());
		}
		else
		{
			resource.setForeground(null);
			component.setForeground(null);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isForegroundSet()
	{
		return component.isForegroundSet();
	}

	/**
	 * {@inheritDoc}
	 */
	public ICursor getCursor()
	{
		return new AwtCursor(component.getCursor());
	}

	/**
	 * {@inheritDoc}
	 */
	public void setCursor(ICursor pCursor)
	{
		if (pCursor == null)
		{
//			resource.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			component.setCursor(null);
		}
		else
		{
			component.setCursor((Cursor)pCursor.getResource());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isCursorSet()
	{
		return component.isCursorSet();
	}

	/**
	 * {@inheritDoc}
	 */
	public IFont getFont()
	{
		return new AwtFont(component.getFont());
	}

	/**
	 * {@inheritDoc}
	 */
	public void setFont(IFont pFont)
	{
		if (pFont == null) 
		{
			resource.setFont(null);
			component.setFont(null);
		}
		else
		{
			resource.setFont((Font)pFont.getResource());
			component.setFont((Font)pFont.getResource());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isFontSet()
	{
		return component.isFontSet();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setFocusable(boolean pFocusable)
	{
		component.setFocusable(pFocusable);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isFocusable()
	{
		return component.isFocusable();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setTabIndex(Integer pTabIndex)
	{
		super.setTabIndex(pTabIndex);
		
		component.putClientProperty("tabIndex", pTabIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	public void requestFocus()
    {
		component.requestFocus();
    }

	/**
	 * {@inheritDoc}
	 */
	public boolean isEnabled()
	{
		return component.isEnabled();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setEnabled(boolean pEnable)
	{
		if (resource instanceof JScrollPane)
		{
			((JScrollPane)resource).setEnabled(pEnable);
		}
		
		component.setEnabled(pEnable);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the Swing Component.
	 * 
	 * @return the Swing Component.
	 */
	public SC getComponent()
	{
		return component;
	}
	
}	// SwingScrollComponent
