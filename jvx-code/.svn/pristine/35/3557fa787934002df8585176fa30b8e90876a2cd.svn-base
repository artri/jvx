/*
 * Copyright 2012 SIB Visions GmbH
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
 * 18.10.2012 - [CB] - creation
 * 24.01.2013 - [SW] - redesign
 */
package com.sibvisions.rad.ui.vaadin.impl.container;

import javax.rad.ui.IComponent;
import javax.rad.ui.ILayout;
import javax.rad.ui.container.IToolBar;
import javax.rad.ui.container.IToolBarPanel;

import com.sibvisions.rad.ui.vaadin.ext.ui.SimplePanel;
import com.sibvisions.rad.ui.vaadin.impl.VaadinSingleComponentContainer;
import com.sibvisions.rad.ui.vaadin.impl.layout.VaadinClientBorderLayout;

/**
 * The <code>VaadinToolBarPanel</code> class is the vaadin implementation of {@link IToolBarPanel}.
 * 
 * @author Benedikt Cermak
 * @author Stefan Wurm
 */
public class VaadinToolBarPanel extends VaadinSingleComponentContainer<SimplePanel>
                                implements IToolBarPanel
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The content panel. */
	private VaadinPanel content = new VaadinPanel();
	
	/** The tool bar panel. */
	protected VaadinToolBar toolBarPanel = new VaadinToolBar();
	
	/** the current area constraint. */
	private int toolBarArea = IToolBarPanel.AREA_TOP;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>VaadinToolBarPanel</code>.
     *
     * @see IToolBarPanel
     */
	public VaadinToolBarPanel()
	{
		super(new SimplePanel());

        setLayout(new VaadinClientBorderLayout());

        toolBarPanel.getResource().setStyleName("jvxtoolbar");
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
		pToolBar.setOrientation(toolBarPanel.getOrientation());
		toolBarPanel.add(pToolBar, pIndex);
		
		// Only add once.
		if (toolBarPanel.getResource().getParent() == null)
		{
			String sConstraint = getToolBarAreaConstraint(toolBarArea);

			toolBarPanel.getResource().addStyleName("orientation-" + sConstraint.toLowerCase());

			addToVaadin(toolBarPanel, sConstraint, -1);			
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeToolBar(int pIndex)
	{
		toolBarPanel.remove(pIndex);
		
		if (getToolBarCount() == 0)
		{
			removeFromVaadin(toolBarPanel);			
		}	
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void removeToolBar(IToolBar pToolBar)
	{
		removeToolBar(indexOfToolBar(pToolBar));
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void removeAllToolBars()
	{
		int iSize;
		while ((iSize = getToolBarCount()) > 0)
		{
			removeToolBar(iSize - 1);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getToolBarCount()
	{
		return toolBarPanel.getComponentCount();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public VaadinToolBar getToolBar(int pIndex)
	{
		return (VaadinToolBar)toolBarPanel.getComponent(pIndex);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int indexOfToolBar(IToolBar pToolBar)
	{
		return toolBarPanel.indexOf(pToolBar);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setToolBarArea(int pToolBarArea)
	{
		String sOldContstraint = getToolBarAreaConstraint(toolBarArea);
		
		toolBarArea = pToolBarArea;
		
		switch (getToolBarArea())
		{
			case AREA_LEFT:   
				toolBarPanel.setOrientation(VaadinToolBar.VERTICAL);
				break;
			case AREA_BOTTOM: 
				toolBarPanel.setOrientation(VaadinToolBar.HORIZONTAL);
				break;
			case AREA_RIGHT:  
				toolBarPanel.setOrientation(VaadinToolBar.VERTICAL);
				break;
			default: 		  
				toolBarPanel.setOrientation(VaadinToolBar.HORIZONTAL);
				break;
		}
		
		String sConstraint = getToolBarAreaConstraint(toolBarArea);

		if (toolBarPanel.getResource().getParent() == null)
		{
			toolBarPanel.getResource().addStyleName("orientation-" + sConstraint.toLowerCase());

			addToVaadin(toolBarPanel, sConstraint, -1);			
		}
		else if (!sOldContstraint.equals(sConstraint))
		{
			toolBarPanel.getResource().removeStyleName("orientation-" + sOldContstraint.toLowerCase());
			toolBarPanel.getResource().addStyleName("orientation-" + sConstraint.toLowerCase());

			removeFromVaadin(toolBarPanel);
			addToVaadin(toolBarPanel, sConstraint, -1);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public int getToolBarArea()
	{
		return toolBarArea;
	}
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Returns the layout from the content panel.
	 * 
	 * @return the layout from the content panel.
	 */
	public ILayout getLayoutForContent()
	{
		return content.getLayout();
	}

	/**
	 * Sets the layout for the content panel.
	 * 
	 * @param pLayout the layout.
	 */
	public void setLayoutForContent(ILayout pLayout)
	{
		content.setLayout(pLayout);
	}
	
	/**
	 * Adds a component to the content panel.
	 * 
	 * @param pComponent the content
	 * @param pConstraints the constraints
	 * @param pIndex the index.
	 */
	public void addComponentToContent(IComponent pComponent, Object pConstraints, int pIndex)
	{
		setContent(content);
 			
		content.addToVaadin(pComponent, pConstraints, pIndex);
	}
	
	/**
	 * Removes the component from the content panel.
	 * 
	 * @param pComponent the component.
	 */
	public void removeComponentFromContent(IComponent pComponent)
	{
		content.removeFromVaadin(pComponent);
	}	
	
	/**
	 * Returns the content from the center of the <code>VaadinBorderLayout</code>.
	 * 
	 * @return the content from the center of the <code>VaadinBorderLayout</code>.
	 */
	public VaadinPanel getContent()
	{
		return content;
	}
	
	/**
	 * Sets the given <code>VaadinPanel</code> in the center of the <code>VaadinBorderLayout</code>.
	 * 
	 * @param pComponent the <code>VaadinPanel</code>.
	 */
	public void setContent(VaadinPanel pComponent)
	{
		content = pComponent;
		
		addToVaadin(content, VaadinClientBorderLayout.CENTER, -1);
	}
	
	/**
	 * Gets the toolbar orientation constraint.
	 * 
	 * @param pOrientation the toolbar orientation
	 * @return the lowercase
	 */
	private String getToolBarAreaConstraint(int pOrientation)
	{
		switch (pOrientation)
		{
			case AREA_LEFT:   
				return VaadinClientBorderLayout.WEST;
			case AREA_BOTTOM: 
				return VaadinClientBorderLayout.SOUTH;
			case AREA_RIGHT:  
				return VaadinClientBorderLayout.EAST;
			default: 		  
				return VaadinClientBorderLayout.NORTH;
		}
	}
	
}	// VaadinToolBarPanel
