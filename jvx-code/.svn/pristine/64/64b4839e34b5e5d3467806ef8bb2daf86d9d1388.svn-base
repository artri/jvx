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
 * 09.04.2009 - [JR] - used InternalToolBarPanel as toolbar container
 * 04.06.2009 - [JR] - add/removeNotify
 * 04.10.2011 - [JR] - #477: beforeAddNotify handling                     
 * 24.10.2012 - [JR] - #604: added constructor
 */
package javax.rad.genui.container;

import javax.rad.genui.UIContainer;
import javax.rad.genui.UIFactoryManager;
import javax.rad.genui.layout.UIBorderLayout;
import javax.rad.ui.IComponent;
import javax.rad.ui.container.IToolBar;
import javax.rad.ui.container.IToolBarPanel;

/**
 * Platform and technology independent toolbar panel.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF,... .
 * 
 * @author René Jahn
 */
public class UIToolBarPanel extends UIContainer<IToolBarPanel> 
                            implements IToolBarPanel
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The ToolBarPanel handler. */
	private transient InternalToolBarPanel toolBarPanel;
	
	/** the flag indicates that addNotify is active. */
	private transient boolean bAddNotify = false;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>UIToolBarPanel</code>.
     *
     * @see IToolBarPanel
     */
	public UIToolBarPanel()
	{
		this(UIFactoryManager.getFactory().createToolBarPanel());
	}

    /**
     * Creates a new instance of <code>UIToolBarPanel</code> with the given
     * toolbar panel.
     *
     * @param pPanel the toolbar panel
     * @see IToolBarPanel
     */
	protected UIToolBarPanel(IToolBarPanel pPanel)
	{
		super(pPanel);
		
		toolBarPanel = new InternalToolBarPanel(this);
		
		setLayout(new UIBorderLayout());
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
		toolBarPanel.addToolBar(pToolBar, pIndex);
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
	public void removeToolBar(int pIndex)
	{
		toolBarPanel.removeToolBar(pIndex);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void removeAllToolBars()
	{
		int iSize;
		while ((iSize = toolBarPanel.getToolBarCount()) > 0)
		{
			removeToolBar(iSize - 1);
		}
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
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateTranslation()
	{
		super.updateTranslation();
		
		//updateTranslation will be called through addNotify -> don't call it more than once!
		if (bAddNotify)
		{
			return;
		}
		
		toolBarPanel.updateTranslation();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void beforeAddNotify(IComponent pParent)
	{
		if (bAddNotify)
		{
			return;
		}
		
		bAddNotify = true;
		
		try
		{
			super.beforeAddNotify(pParent);
		}
		finally
		{
			bAddNotify = false;
		}
		
		toolBarPanel.beforeAddNotify(this);
	}	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addNotify()
	{
		if (bAddNotify)
		{
			return;
		}
		
		bAddNotify = true;
		
		try
		{
			super.addNotify();
		}
		finally
		{
			bAddNotify = false;
		}
		
		toolBarPanel.addNotify();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeNotify()
	{
		super.removeNotify();
		
		toolBarPanel.removeNotify();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Sets all toolbars movable or fixed.
	 * 
	 * @param pMovable <code>true</code> to move the toolbars, <code>false</code> otherwise
	 */
	public void setToolBarMovable(boolean pMovable)
	{
		toolBarPanel.setToolBarMovable(pMovable);
	}
	
	/**
	 * Gets whether the toolbars are movable.
	 * 
	 * @return <code>true</code> if the toolbars are movable, <code>false</code> otherwise
	 */
	public boolean isToolBarMovable()
	{
		return toolBarPanel.isToolBarMovable();
	}
	
}	// UIToolBarPanel
