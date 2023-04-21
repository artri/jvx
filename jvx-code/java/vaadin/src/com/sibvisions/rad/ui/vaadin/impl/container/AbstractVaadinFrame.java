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
 * 23.10.2012 - [CB] - creation
 */
package com.sibvisions.rad.ui.vaadin.impl.container;

import javax.rad.ui.IImage;
import javax.rad.ui.ILayout;
import javax.rad.ui.container.IFrame;
import javax.rad.ui.container.IToolBar;
import javax.rad.ui.menu.IMenuBar;

import com.sibvisions.rad.ui.vaadin.impl.VaadinImage;
import com.sibvisions.rad.ui.vaadin.impl.VaadinInsets;
import com.sibvisions.rad.ui.vaadin.impl.layout.VaadinAbsoluteLayout;
import com.vaadin.ui.SingleComponentContainer;

/**
 * The <code>AbstractVaadinFrame</code> class is the vaadin implementation of {@link IFrame}.
 * 
 * @author Benedikt Cermak
 * @param <C> an instance of SingleComponentContainer
 */
public abstract class AbstractVaadinFrame<C extends SingleComponentContainer> extends AbstractVaadinWindow<C>
														                      implements IFrame
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** the root pane for the frame. */ 
	protected VaadinRootPane rootPane = new VaadinRootPane();
	
	/** the image. */
	private VaadinImage image;
	
	/** the title. */
	private String sTitle;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /**
     * Creates a new instance of <code>AbstractVaadinFrame</code>.
     *
     * @see IFrame
     * @param pContainer an instance of {@link com.vaadin.ui.ComponentContainer}
     */
	public AbstractVaadinFrame(C pContainer)
	{
		super(pContainer);
		setLayout(createDefaultLayout());
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public String getTitle()
    {
		return sTitle;
    }

	/**
	 * {@inheritDoc}
	 */
	public void setTitle(String pTitle)
    {
		sTitle = pTitle;
		
		resource.getUI().getPage().setTitle(pTitle);
    }

	/**
	 * {@inheritDoc}
	 */
	public IImage getIconImage()
    {
		return image;
    }

	/**
	 * {@inheritDoc}
	 */
	public void setIconImage(IImage pIconImage)
    {
		image = (VaadinImage) pIconImage;
		
		if (pIconImage == null)
		{
			resource.setIcon(null);
		}
		else
		{
			resource.setIcon(image.getResource());
		}
    }

	/**
	 * {@inheritDoc}
	 */
    public int getState()
    {
    	return IFrame.NORMAL;
    }

	/**
	 * {@inheritDoc}
	 */
    public void setState(int pState)
    {
    }

	/**
	 * {@inheritDoc}
	 */
    public boolean isResizable()
    {
    	return true;
    }

	/**
	 * {@inheritDoc}
	 */
    public void setResizable(boolean pResizable)
    {
    	//Not supported.
    }
    
	/**
	 * {@inheritDoc}
	 */   
	public void addToolBar(IToolBar pToolBar)
	{
		rootPane.addToolBar(pToolBar);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void addToolBar(IToolBar pToolBar, int pIndex)
	{
		rootPane.addToolBar(pToolBar, pIndex);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void removeToolBar(int pIndex)
	{
		rootPane.removeToolBar(pIndex);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void removeToolBar(IToolBar pToolBar)
	{
		rootPane.removeToolBar(pToolBar);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void removeAllToolBars()
	{
		rootPane.removeAllToolBars();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getToolBarCount()
	{
		return rootPane.getToolBarCount();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public IToolBar getToolBar(int pIndex)
	{
		return rootPane.getToolBar(pIndex);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int indexOfToolBar(IToolBar pToolBar)
	{
		return rootPane.indexOfToolBar(pToolBar);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setToolBarArea(int pArea)
	{
		rootPane.setToolBarArea(pArea);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getToolBarArea()
	{
		return rootPane.getToolBarArea();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setMenuBar(IMenuBar pMenuBar)
	{
		rootPane.setMenuBar(pMenuBar);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public IMenuBar getMenuBar()
	{
		return rootPane.getMenuBar();
	}  
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	@Override
	public ILayout getLayout()
	{
		return rootPane.getLayoutForContent();
	}

	@Override
	public void setLayout(ILayout pLayout)
	{
		if (rootPane != null)
		{
			rootPane.setLayoutForContent(pLayout);
		}
	}	
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates the default Layout.
	 * 
	 * @return the default Layout
	 */
	private ILayout createDefaultLayout() 
	{
		VaadinAbsoluteLayout al = new VaadinAbsoluteLayout();
		al.setMargins(new VaadinInsets(0, 0, 0, 0));

		return al;
	}
	
	/**
	 * Returns the RootPane of the frame.
	 * 
	 * @return the VaadinRootPane
	 */
	public VaadinRootPane getRootPane()
	{
		return rootPane;
	}
	
	/**
	 * Sets the Content.
	 * 
	 * @param pComponent the content.
	 */
	public void setContent(VaadinPanel pComponent)
	{
		rootPane.setContent(pComponent);
	}

}	// AbstractVaadinFrame
