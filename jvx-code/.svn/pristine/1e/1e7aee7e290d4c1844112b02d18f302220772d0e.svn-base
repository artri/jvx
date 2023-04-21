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
 * 31.03.2009 - [JR] - creation
 * 09.04.2009 - [JR] - used InternalToolBarPanel
 */
package com.sibvisions.rad.ui.swing.impl.container;

import java.awt.Color;

import javax.rad.ui.IColor;
import javax.rad.ui.container.IToolBar;
import javax.rad.ui.container.IToolBarPanel;

import com.sibvisions.rad.ui.swing.impl.SwingComponent;

/**
 * The <code>SwingToolBarPanel</code> is {@link IToolBarPanel} implementation
 * for Swing.
 * 
 * @author René Jahn
 */
public class SwingToolBarPanel extends SwingComponent<InternalToolBarPanel> 
                               implements IToolBarPanel
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>SwingPanel</code>.
	 */
	public SwingToolBarPanel()
	{
		super(new InternalToolBarPanel());
		
		resource.setToolBarOwner(this);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public void addToolBar(IToolBar pToolBar)
	{
		addToolBar(pToolBar, -1);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void addToolBar(IToolBar pToolBar, int pIndex)
	{
		resource.addUIToolBar(pToolBar, pIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeToolBar(IToolBar pToolBar)
	{
		resource.removeUIToolBar(pToolBar);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void removeToolBar(int pIndex)
	{
		resource.removeUIToolBar(pIndex);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void removeAllToolBars()
	{
		int iSize;
		while ((iSize = resource.getUIToolBarCount()) > 0)
		{
			removeToolBar(iSize - 1);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getToolBarCount()
	{
		return resource.getUIToolBarCount();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public IToolBar getToolBar(int pIndex)
	{
		return resource.getUIToolBar(pIndex);
	}	
	
	/**
	 * {@inheritDoc}
	 */
	public int indexOfToolBar(IToolBar pToolBar)
	{
		return resource.indexOfUIToolBar(pToolBar);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setToolBarArea(int pArea)
	{
		resource.setUIArea(pArea);
	}	

	/**
	 * {@inheritDoc}
	 */
	public int getToolBarArea()
	{
		return resource.getUIArea();
	}	
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setBackground(IColor pBackground)
	{
		super.setBackground(pBackground);
		
		if (pBackground == null)
		{
			resource.getToolBar().setBackground(null);
		}
		else
		{
			resource.getToolBar().setBackground((Color)pBackground.getResource());
		}
	}	
	
}	// SwingToolBarPanel
