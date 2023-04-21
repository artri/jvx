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
 * 09.04.2009 - [JR] - creation
 * 02.07.2009 - [JR] - addUIToolBar: call removeToolBar instead of remove!
 * 13.08.2013 - [JR] - #756: changed add toolbar
 */
package com.sibvisions.rad.ui.swing.impl.container;

import java.util.ArrayList;
import java.util.List;

import javax.rad.ui.IContainer;
import javax.rad.ui.container.IToolBar;
import javax.rad.ui.container.IToolBarPanel;
import javax.swing.JToolBar;

import com.sibvisions.rad.ui.swing.ext.JVxToolBarPanel;

/**
 * The <code>InternalToolBarPanel</code> extends the {@link JVxToolBarPanel} and acts
 * as a bridge between UI and swing. The class stores the toolbars in an
 * own list. It acts like an UI Container for toolbars.
 * 
 * @author René Jahn
 */
final class InternalToolBarPanel extends JVxToolBarPanel
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** List of toolbars. */
	private List<IToolBar> toolbars = new ArrayList<IToolBar>();
	
	/** the owner of the toolbar panel. */
	private IToolBarPanel toolBarOwner;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public JToolBar getToolBar()
	{
		//made public
		return super.getToolBar();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Adds a toolbar to this panel at a specified index.
	 * 
	 * @param pToolBar the toolbar to be added
	 * @param pIndex the index for the toolbar              
	 */
	void addUIToolBar(IToolBar pToolBar, int pIndex)
	{
		if (pToolBar.getParent() != null)
		{
			((IToolBarPanel)pToolBar.getParent()).removeToolBar(pToolBar);
		}

		if (pIndex < 0)
		{
			toolbars.add(pToolBar);
		}
		else
		{
			toolbars.add(pIndex, pToolBar);
		}

		IContainer conOldParent = pToolBar.getParent();
		
		pToolBar.setParent(toolBarOwner);
		
		try
		{
			addToolBarIntern(pToolBar, pIndex);
		}
		catch (RuntimeException re)
		{
			toolbars.remove(pToolBar);

			pToolBar.setParent(conOldParent);
			
			throw re;
		}
		catch (Error e)
		{
			toolbars.remove(pToolBar);
			
			pToolBar.setParent(conOldParent);
			
			throw e;
		}
	}

	/**
	 * Removes a toolbar from this panel.
	 * 
	 * @param pIndex the index
	 */
	void removeUIToolBar(int pIndex)
	{
		removeUIToolBar(toolbars.get(pIndex));
	}
	
	/**
	 * Removes a toolbar from this panel.
	 * 
	 * @param pToolBar the toolbar
	 */
	void removeUIToolBar(IToolBar pToolBar)
	{
		removeToolBarIntern(pToolBar);

		toolbars.remove(pToolBar);
		
		pToolBar.setParent(null);
		
		repaint();
	}
	
	/**
	 * Gets the number of <code>IToolBar</code>s in this panel.
	 * 
	 * @return the number of toolbars
	 */
	int getUIToolBarCount()
	{
		return toolbars.size();
	}
	
	/**
	 * Gets the {@link IToolBar} from a specific index.
	 *  
	 * @param pIndex the index
	 * @return the toolbar at <code>pIndex</code>
	 */
	IToolBar getUIToolBar(int pIndex)
	{
		if (pIndex < 0 || pIndex >= toolbars.size())
		{
			throw new ArrayIndexOutOfBoundsException("No such child: " + pIndex);
		}
		
		return toolbars.get(pIndex);
	}	
	
	/**
	 * Gets the n<sup>th</sup> position of an <code>IToolBar</code> in this panel.
	 * 
	 * @param pToolBar the <code>IToolBar</code> to search
	 * @return the n<sup>th</sup> position of <code>pToolBar</code> in this panel or
	 *         <code>-1</code> if <code>pToolBar</code> is not added
	 */
	int indexOfUIToolBar(IToolBar pToolBar)
	{
		return toolbars.indexOf(pToolBar);
	}
	
	/**
	 * Sets the display area where the toolbars will be added.
	 * 
	 * @param pArea an area constant {@link #AREA_TOP}, {@link #AREA_LEFT}, 
	 *              {@link #AREA_BOTTOM}, {@link #AREA_RIGHT}
	 */
	public void setUIArea(int pArea)
	{
		setArea(getSwingArea(pArea));
	}	

	/**
	 * Gets the area where the toolbar(s) are added.
	 * 
	 * @return an area constant {@link #AREA_TOP}, {@link #AREA_LEFT}, 
	 *         {@link #AREA_BOTTOM}, {@link #AREA_RIGHT}
	 */
	public int getUIArea()
	{
		return getUIArea(getArea());
	}	

	/**
	 * Gets the swing area for an area constant.
	 * 
	 * @param pArea the area constant
	 * @return {@link ToolBarArea}
	 */
	public ToolBarArea getSwingArea(int pArea)
	{
		switch (pArea)
		{
			case IToolBarPanel.AREA_TOP:
				return ToolBarArea.TOP;
			case IToolBarPanel.AREA_LEFT:
				return ToolBarArea.LEFT;
			case IToolBarPanel.AREA_BOTTOM:
				return ToolBarArea.BOTTOM;
			case IToolBarPanel.AREA_RIGHT:
				return ToolBarArea.RIGHT;
			default:
				return ToolBarArea.TOP;
		}
	}

	/**
	 * Gets the area constant for a swing area.
	 * 
	 * @param pArea {@link ToolBarArea}
	 * @return the area constant
	 */
	public int getUIArea(ToolBarArea pArea)
	{
		switch (pArea)
		{
			case TOP:
				return IToolBarPanel.AREA_TOP;
			case LEFT:
				return IToolBarPanel.AREA_LEFT;
			case BOTTOM:
				return IToolBarPanel.AREA_BOTTOM;
			case RIGHT:
				return IToolBarPanel.AREA_RIGHT;
			default:
				return IToolBarPanel.AREA_TOP;
		}
	}
	
	/**
	 * Sets the owner of the toolbar for the parent chain.
	 * 
	 * @param pToolBarOwner an {@link IToolBarPanel}
	 */
	void setToolBarOwner(IToolBarPanel pToolBarOwner)
	{
		toolBarOwner = pToolBarOwner;
	}
	
	/**
	 * Adds the toolbar at a specific index.
	 * 
	 * @param pToolBar the toolbar to be added
	 * @param pIndex the position
	 */
	protected void addToolBarIntern(IToolBar pToolBar, int pIndex)
	{
		//set before adding, because the SwingToolbar asks the parent floatable state!
		getToolBar().setFloatable(pToolBar.isMovable());

		addToolBar((JToolBar)pToolBar.getResource(), pIndex);
	}
	
	/**
	 * Removes the toolbar from the panel.
	 * 
	 * @param pToolBar the toolbar to be removed
	 */
	protected void removeToolBarIntern(IToolBar pToolBar)
	{
		removeToolBar((JToolBar)pToolBar.getResource());
	}
	
}	// InternalToolBarPanel
