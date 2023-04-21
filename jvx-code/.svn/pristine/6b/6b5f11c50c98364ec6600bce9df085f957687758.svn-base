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
 * 15.10.2012 - [HM] - creation
 */
package com.sibvisions.rad.ui.vaadin.impl.container;

import javax.rad.ui.IInsets;
import javax.rad.ui.container.IToolBar;

import com.sibvisions.rad.ui.vaadin.ext.ui.SimplePanel;
import com.sibvisions.rad.ui.vaadin.impl.VaadinInsets;
import com.sibvisions.rad.ui.vaadin.impl.VaadinSingleComponentContainer;
import com.sibvisions.rad.ui.vaadin.impl.layout.VaadinClientFlowLayout;

/**
 * The <code>VaadinToolBar</code> class is the vaadin implementation of {@link IToolBar}.
 * 
 * @author Benedikt Cermak
 */
public class VaadinToolBar extends VaadinSingleComponentContainer<SimplePanel>
                           implements IToolBar
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the layout. */
	private VaadinClientFlowLayout layout = new VaadinClientFlowLayout();

	/** the movable flag. */
	private boolean bMovable;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>VaadinToolBar</code>.
     *
     * @see javax.rad.ui.container.IToolBar
     */
	public VaadinToolBar()
	{
		super(new SimplePanel());
		
		layout.setMargins(new VaadinInsets(1, 1, 1, 1));
		layout.setHorizontalGap(0);
		layout.setVerticalGap(0);
//		layout.setAutoWrap(true); // AutoWrap inside AutoWrap can lead in problems, as size of outer autowrap will not change.
		layout.setHorizontalAlignment(ALIGN_LEFT);
		layout.setVerticalAlignment(ALIGN_TOP);
	
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
    	
    	for (int i = 0, cnt = getComponentCount(); i < cnt; i++)
    	{
    		if (getComponent(i) instanceof VaadinToolBar)
    		{
    			((VaadinClientFlowLayout)((VaadinToolBar)getComponent(i)).getLayout()).setOrientation(pOrientation);
    		}
    	}
    }
	
	/**
	 * {@inheritDoc}
	 */
    public IInsets getMargins()
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
    	bMovable = pMovable;
    }
    
	/**
	 * {@inheritDoc}
	 */
    public boolean isMovable()
    {
    	return bMovable;
    }
    
}	// VaadinToolBar
