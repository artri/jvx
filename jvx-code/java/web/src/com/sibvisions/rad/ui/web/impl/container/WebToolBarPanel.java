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

import java.util.List;

import javax.rad.ui.container.IToolBar;
import javax.rad.ui.container.IToolBarPanel;

import com.sibvisions.rad.ui.web.impl.WebComponent;
import com.sibvisions.rad.ui.web.impl.WebContainer;
import com.sibvisions.rad.ui.web.impl.layout.WebBorderLayout;
import com.sibvisions.util.ArrayUtil;

/**
 * Web server implementation of {@link IToolBarPanel}.
 * 
 * @author Martin Handsteiner
 */
public class WebToolBarPanel extends WebContainer
                             implements IToolBarPanel
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The tool bar panel. */
	protected ArrayUtil<WebToolBar> liToolBars;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>WebToolBarPanel</code>.
     *
     * @see IToolBarPanel
     */
	public WebToolBarPanel()
	{
		setLayout(new WebBorderLayout());
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
		WebToolBarPanel parent = (WebToolBarPanel)pToolBar.getParent();
		
		if (parent != null)
		{
			parent.removeToolBar(pToolBar);
		}
		
		pToolBar.setOrientation(getOrientation(getToolBarArea()));
		
		if (liToolBars == null)
		{
			liToolBars = new ArrayUtil<WebToolBar>();
		}
		
		if (pIndex < 0)
		{
			liToolBars.add((WebToolBar)pToolBar);
		}
		else
		{
			liToolBars.add(pIndex, (WebToolBar)pToolBar);
		}
		
		((WebToolBar)pToolBar).setParent(this, false);
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeToolBar(int pIndex)
	{
		if (liToolBars != null)
		{
			WebToolBar tbar = liToolBars.remove(pIndex);
			
			tbar.setParent(null, false);
			
			if (liToolBars.size() == 0)
			{
				liToolBars = null;
			}
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
		return liToolBars != null ? liToolBars.size() : 0;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public WebToolBar getToolBar(int pIndex)
	{
		if (liToolBars != null)
		{
			return (WebToolBar)liToolBars.get(pIndex);
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int indexOfToolBar(IToolBar pToolBar)
	{
		return liToolBars != null ? liToolBars.indexOf(pToolBar) : -1;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setToolBarArea(int pToolBarArea)
	{
		setProperty("toolBarArea", Integer.valueOf(pToolBarArea));
		
		if (liToolBars != null)
		{
			int iOrient = getOrientation(pToolBarArea);
			
			for (int i = 0, cnt = liToolBars.size(); i < cnt; i++)
			{
				liToolBars.get(i).setOrientation(iOrient);
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getToolBarArea()
	{
		return getProperty("toolBarArea", Integer.valueOf(AREA_TOP)).intValue();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<WebComponent> getAdditionalComponents()
	{
		List<WebComponent> liComps = super.getAdditionalComponents();
		
		if (liToolBars != null)
		{
			liComps.addAll(liToolBars);
		}
		
		return liComps;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the toolbar orientation for the given toolbar area.
	 *  
	 * @param pToolBarArea the area
	 * @return the orientation
	 */
	private int getOrientation(int pToolBarArea)
	{
		switch (pToolBarArea)
		{
			case AREA_LEFT:   
			case AREA_RIGHT:  
				return WebToolBar.VERTICAL;
			default: 
				return WebToolBar.HORIZONTAL;
		}
	}
	
}	// WebToolBarPanel
