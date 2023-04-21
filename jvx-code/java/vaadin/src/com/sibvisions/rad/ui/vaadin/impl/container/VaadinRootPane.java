/*
 * Copyright 2013 SIB Visions GmbH
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
 * 24.01.2013 - [SW] - creation
 */
package com.sibvisions.rad.ui.vaadin.impl.container;

import javax.rad.ui.IComponent;
import javax.rad.ui.ILayout;
import javax.rad.ui.container.IToolBar;
import javax.rad.ui.container.IToolBarPanel;
import javax.rad.ui.menu.IMenuBar;

import com.sibvisions.rad.ui.vaadin.ext.ui.SimplePanel;
import com.sibvisions.rad.ui.vaadin.impl.VaadinSingleContainer;
import com.sibvisions.rad.ui.vaadin.impl.layout.VaadinClientBorderLayout;
import com.vaadin.ui.Component;


/**
 * The <code>VaadinRootPane</code> class is the root panel of a Frame. It holds the 
 * menu and toolbar.
 * 
 * @author Stefan Wurm
 */
public class VaadinRootPane extends VaadinSingleContainer<Component, SimplePanel> 
                            implements IToolBarPanel
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The menu bar. **/
	private IMenuBar menuBar;

	/** The tool bar panel. **/
	private VaadinToolBarPanel toolBarPanel = new VaadinToolBarPanel();
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * New instance of <code>VaadinRootPane</code>.
	 */
	public VaadinRootPane()
	{
		super(new SimplePanel());

		resource.setSizeUndefined();

		setLayout(new VaadinClientBorderLayout());
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	

	/**
	 * {@inheritDoc}
	 */
	public void addToolBar(IToolBar pToolBar)
	{
		toolBarPanel.addToolBar(pToolBar);
	}

	/**
	 * {@inheritDoc}
	 */
	public void addToolBar(IToolBar pToolBar, int pIndex)
	{
		toolBarPanel.addToolBar(pToolBar, pIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeToolBar(int pIndex)
	{
		toolBarPanel.removeToolBar(pIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeToolBar(IToolBar pToolBar)
	{
		toolBarPanel.removeToolBar(pToolBar);
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeAllToolBars()
	{
		toolBarPanel.removeAllToolBars();
	}

	/**
	 * {@inheritDoc}
	 */
	public int getToolBarCount()
	{
		return toolBarPanel.getToolBarCount();
	}

	/**
	 * {@inheritDoc}
	 */
	public IToolBar getToolBar(int pIndex)
	{
		return toolBarPanel.getToolBar(pIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	public int indexOfToolBar(IToolBar pToolBar)
	{
		return toolBarPanel.indexOfToolBar(pToolBar);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setToolBarArea(int pArea)
	{
		toolBarPanel.setToolBarArea(pArea);		
	}

	/**
	 * {@inheritDoc}
	 */
	public int getToolBarArea()
	{
		return toolBarPanel.getToolBarArea();
	}	
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    @Override
	public Component getResource()
	{
		return resource;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Returns the menubar.
	 * 
	 * @return the menubar.
	 */
	public IMenuBar getMenuBar()
	{
		return menuBar;
	}

	/**
	 * Sets the menubar for the rootpane.
	 * 
	 * @param pMenuBar the menubar
	 */
	public void setMenuBar(IMenuBar pMenuBar)
	{
		if (menuBar != null)
		{
			remove(menuBar);
		}
		if (pMenuBar != null)
		{
			add(pMenuBar, VaadinClientBorderLayout.NORTH);
		}

		menuBar = pMenuBar;
	}	
	
	/**
	 * Returns the layout from the content panel.
	 * 
	 * @return the layout from the content panel.
	 */
	public ILayout getLayoutForContent()
	{
		return toolBarPanel.getLayoutForContent();
	}
	
	/**
	 * Sets the layout for the content panel.
	 * 
	 * @param pLayout the layout.
	 */
	public void setLayoutForContent(ILayout pLayout)
	{
		toolBarPanel.setLayoutForContent(pLayout);
	}
	
	/**
	 * Adds a component to the content panel.
	 * 
	 * @param pComponent the component.
	 * @param pConstraints the constraint.
	 * @param pIndex the index.
	 */
	public void addComponentToContent(IComponent pComponent, Object pConstraints, int pIndex)
	{
		addToVaadin(toolBarPanel, VaadinClientBorderLayout.CENTER, -1);
		
		toolBarPanel.addComponentToContent(pComponent, pConstraints, pIndex);
	}
	
	/**
	 * Removes the component from the content panel.
	 * 
	 * @param pComponent the component.
	 */
	public void removeComponentFromContent(IComponent pComponent)
	{
		toolBarPanel.removeComponentFromContent(pComponent);
	}

	/**
	 * Sets the content in the toolBarPanel CENTER.
	 * 
	 * @param pContent the content.
	 */
	public void setContent(VaadinPanel pContent)
	{
		toolBarPanel.setContent(pContent);
	}	
	
	/**
	 * Returns the Content from the ToolBarPanel.
	 * 
	 * @return the Content from the ToolBarPanel.
	 */
	public VaadinPanel getContent()
	{
		return toolBarPanel.getContent();
	}
	
} 	// VaadinRootPane
