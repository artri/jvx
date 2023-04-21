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

import javax.rad.ui.IContainer;
import javax.rad.ui.IInsets;
import javax.rad.ui.container.IToolBar;

import com.sibvisions.rad.ui.web.impl.WebContainer;
import com.sibvisions.rad.ui.web.impl.WebInsets;
import com.sibvisions.rad.ui.web.impl.layout.WebFlowLayout;

/**
 * Web server implementation of {@link IToolBar}.
 * 
 * @author Martin Handsteiner
 */
public class WebToolBar extends WebContainer
                        implements IToolBar
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the orientation. */
	private WebFlowLayout layout = new WebFlowLayout();

	/** the movable flag. */
	private boolean movable;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>WebToolBar</code>.
     *
     * @see javax.rad.ui.container.IToolBar
     */
	public WebToolBar()
	{
		layout.setMargins(new WebInsets(1, 1, 1, 1));
		layout.setHorizontalAlignment(WebFlowLayout.ALIGN_LEFT);
		layout.setVerticalAlignment(WebFlowLayout.ALIGN_TOP);
		layout.setComponentAlignment(WebFlowLayout.ALIGN_STRETCH);
		layout.setHorizontalGap(1);
		layout.setVerticalGap(1);
		setLayout(layout);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
    public int getOrientation()
    {
    	return layout.getOrientation();
    }

	/**
	 * {@inheritDoc}
	 */
    public void setOrientation(int pOrientation)
    {
    	layout.setOrientation(pOrientation);
    }
	
	/**
	 * {@inheritDoc}
	 */
    public WebInsets getMargins()
    {
    	return layout.getMargins();
    }

	/**
	 * {@inheritDoc}
	 */
    public void setMargins(IInsets pMargins)
    {
    	layout.setMargins(pMargins);
    }
    
	/**
	 * {@inheritDoc}
	 */
    public void setMovable(boolean pMovable)
    {
    	movable = pMovable;
    }
    
	/**
	 * {@inheritDoc}
	 */
    public boolean isMovable()
    {
    	return movable;
    }
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setParent(IContainer pParent, boolean pCheckAdded)
    {
    	//overwritten to make it available in this package
    	super.setParent(pParent, pCheckAdded);
    }
    
}	// WebToolBar
