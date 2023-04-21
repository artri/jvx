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

import java.util.ArrayList;
import java.util.List;

import javax.rad.application.IConnectable;
import javax.rad.ui.IComponent;
import javax.rad.ui.IContainer;
import javax.rad.ui.container.IDesktopPanel;
import javax.rad.ui.container.IInternalFrame;

import com.sibvisions.rad.ui.web.impl.WebComponent;
import com.sibvisions.rad.ui.web.impl.WebContainer;
import com.sibvisions.rad.ui.web.impl.layout.WebBorderLayout;

/**
 * Web server implementation of {@link IDesktopPanel}.
 * 
 * @author Martin Handsteiner
 */
public class WebDesktopPanel extends WebContainer
                             implements IDesktopPanel
{

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Creates a new instance of <code>WebDesktopPanel</code>.
     *
     * @see IDesktopPanel
     */
	public WebDesktopPanel()
	{
		setLayout(new WebBorderLayout());
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public boolean isTabMode()
    {
    	return getProperty("tabMode", Boolean.FALSE).booleanValue();
    }
	
	/**
	 * {@inheritDoc}
	 */
	public void setTabMode(boolean pTabMode)
    {
		setProperty("tabMode", Boolean.valueOf(pTabMode));
    }
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isNavigationKeysEnabled()
	{
    	return getProperty("navigationKeysEnabled", Boolean.FALSE).booleanValue();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setNavigationKeysEnabled(boolean pNavigationKeysEnabled)
	{
		setProperty("navigationKeysEnabled", Boolean.valueOf(pNavigationKeysEnabled));
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void add(IComponent pComponent, Object pConstraints, int pIndex)
    {
		if (pComponent instanceof IInternalFrame)
		{
			addAdditional(pComponent, pIndex);
		}
		else
		{
			super.add(pComponent, pConstraints, pIndex);
		}
    }
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove(IComponent pComponent)
	{
		if (pComponent instanceof IInternalFrame)
		{
			removeAdditional(pComponent);
		}
		else
		{
			super.remove(pComponent);
		}
    }	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<WebComponent> getAdditionalComponents()
	{
		List<WebComponent> list = super.getAdditionalComponents();
		
		ArrayList<WebComponent> liResult = new ArrayList<WebComponent>();
		
		int iNotModal = 0;
		int iModal = 0;
		
		WebComponent comp;
		
		//Sorts the frames: first: non modal and connectable, modal last
		for (int i = 0, cnt = list.size(); i < cnt; i++)
		{
			comp = list.get(i);
			
			if (comp instanceof IInternalFrame)
			{
				if (((IInternalFrame)comp).isModal()
					&& !isConnectable(comp))
				{
					liResult.add(iModal, comp);
					
					iModal++;
				}
				else
				{
					liResult.add(iNotModal, comp);
					
					iNotModal++;
					iModal++;
				}
			}
		}
		
		return liResult;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets whether the given component or a sub-component is an instance of {@link IConnectable}.
	 * This method implements a recursive component search.
	 * 
	 * @param pComponent the start component
	 * @return whether the given or a sub-component is an instance of {@link IConnectable}
	 */
	private boolean isConnectable(IComponent pComponent)
	{
		if (pComponent instanceof IConnectable)
		{
			return true;
		}
		
		if (pComponent instanceof IContainer)
		{
			IContainer cont = (IContainer)pComponent;
			
			for (int i = 0, cnt = cont.getComponentCount(); i < cnt; i++)
			{
				if (isConnectable(cont.getComponent(i)))
				{
					return true;
				}
			}
		}
		
		return false;
	}	
	
}	// WebDesktopPanel
