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
 * 20.11.2009 - [HM] - creation
 */
package com.sibvisions.rad.ui.web.impl.container;

import javax.rad.ui.IImage;
import javax.rad.ui.container.IFrame;
import javax.rad.ui.container.IToolBarPanel;
import javax.rad.ui.menu.IMenuBar;

import com.sibvisions.rad.ui.web.impl.WebImage;
import com.sibvisions.rad.ui.web.impl.menu.WebMenuBar;

/**
 * Web server implementation of {@link IFrame}.
 * 
 * @author Martin Handsteiner
 */
public abstract class AbstractWebFrame extends AbstractWebWindow
                                       implements IFrame
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** the menu bar. */ 
	protected WebMenuBar menuBar;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>AbstractWebFrame</code>.
     *
     * @see IFrame
     */
	protected AbstractWebFrame()
	{
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public void pack()
    {
		setCommandProperty("pack", Boolean.TRUE);
    }

	/**
	 * {@inheritDoc}
	 */
	public void toFront()
    {
		setCommandProperty("toFront", Boolean.TRUE);
    }

	/**
	 * {@inheritDoc}
	 */
	public void toBack()
    {
		setCommandProperty("toBack", Boolean.TRUE);
    }

	/**
	 * {@inheritDoc}
	 */
	public String getTitle()
    {
		return getProperty("title", "");
    }

	/**
	 * {@inheritDoc}
	 */
	public void setTitle(String pTitle)
    {
		setProperty("title", pTitle);
    }

	/**
	 * {@inheritDoc}
	 */
	public WebImage getIconImage()
    {
    	return getProperty("iconImage", null);
    }

	/**
	 * {@inheritDoc}
	 */
	public void setIconImage(IImage pIconImage)
    {
		setProperty("iconImage", WebImage.createScaledImage((WebImage)pIconImage));
    }

	/**
	 * {@inheritDoc}
	 */
    public int getState()
    {
    	return getProperty("state", Integer.valueOf(IFrame.NORMAL)).intValue();
    }

	/**
	 * {@inheritDoc}
	 */
    public void setState(int pState)
    {
    	setProperty("state", Integer.valueOf(pState));
    }

	/**
	 * {@inheritDoc}
	 */
    public boolean isResizable()
    {
    	return getProperty("resizable", Boolean.TRUE).booleanValue();
    }

	/**
	 * {@inheritDoc}
	 */
    public void setResizable(boolean pResizable)
    {
    	setProperty("resizable", Boolean.valueOf(pResizable));
    }

	/**
	 * {@inheritDoc}
	 */
	public int getToolBarArea()
	{
		return getProperty("toolBarArea", Integer.valueOf(IToolBarPanel.AREA_TOP)).intValue();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setToolBarArea(int pToolBarArea)
	{
		setProperty("toolBarArea", Integer.valueOf(pToolBarArea));
	}

    /**
 	 * {@inheritDoc}
 	 */
    public WebMenuBar getMenuBar()
    {
   		return menuBar;
    }
    
    /**
	 * {@inheritDoc}
	 */
    public void setMenuBar(IMenuBar pMenuBar)
    {
    	if (menuBar != null)
    	{
    		removeAdditional(menuBar);
    	}
    	
    	menuBar = (WebMenuBar)pMenuBar;

    	if (menuBar != null)
    	{
			addAdditional(menuBar);
    	}
    	
    	setProperty("menuBar", menuBar);
    }
    
}	// AbstractWebFrame
