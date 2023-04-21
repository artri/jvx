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
 * 01.10.2008 - [HM] - creation
 * 05.02.2011 - [JR] - #278: setParent now checks null parent                      
 */
package com.sibvisions.rad.ui.swing.impl.container;

import java.awt.Component;
import java.awt.Container;
import java.awt.Insets;

import javax.rad.ui.IContainer;
import javax.rad.ui.IInsets;
import javax.rad.ui.ILayout;
import javax.rad.ui.container.IToolBar;
import javax.rad.ui.container.IToolBarPanel;
import javax.swing.JToolBar;

import com.sibvisions.rad.ui.awt.impl.AwtInsets;
import com.sibvisions.rad.ui.swing.ext.JVxToolBar;
import com.sibvisions.rad.ui.swing.impl.SwingComponent;

/**
 * The <code>SwingToolBar</code> is the <code>IToolBar</code>
 * implementation for swing.
 * 
 * @author Martin Handsteiner
 * @see	javax.swing.JToolBar
 * @see IToolBar
 */
public class SwingToolBar extends SwingComponent<JVxToolBar> 
                          implements IToolBar
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the movable state of the toolbar. */
	private boolean bMovable = true;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>SwingToolBar</code>.
	 */
	public SwingToolBar()
	{
		super(new JVxToolBar());
		
		resource.setFloatable(false);
		resource.setBackground(null);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public int getOrientation()
	{
		return resource.getOrientation();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setOrientation(int pOrientation)
	{
		resource.setOrientation(pOrientation);
	}

	/**
	 * {@inheritDoc}
	 */
	public IInsets getMargins()
	{
		Insets margins = resource.getMargin();
		if (margins == null)
		{
			return null;
		}
		else
		{
			return new AwtInsets(margins);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void setMargins(IInsets pMargins)
	{
		if (pMargins == null)
		{
			resource.setMargin(null);
		}
		else
		{
			resource.setMargin((Insets)pMargins.getResource());
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
    public void setMovable(boolean pMovable)
    {
    	bMovable = pMovable;
    	
    	Container parent = resource.getParent();
    	
    	if (parent != null && parent instanceof JVxToolBar)
    	{
    		((JVxToolBar)parent).setFloatable(pMovable);
    	}
    }
    
	/**
	 * {@inheritDoc}
	 */
    public boolean isMovable()
    {
    	Container parent = resource.getParent();
    	
    	if (parent != null && parent instanceof JVxToolBar)
    	{
    		return ((JVxToolBar)parent).isFloatable();
    	}
    	else
    	{
    		return bMovable;
    	}
    }
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setLayout(ILayout pLayout)
	{
		// Do not set any layout to a tabset panel!
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setParent(IContainer pParent)
	{
		if ((pParent == null || pParent instanceof IToolBarPanel)
			&& (parent == null || parent instanceof IToolBarPanel))
		{
			IToolBarPanel tbp = (IToolBarPanel)pParent;

			if (pParent == null)
			{
				if (parent != null && ((IToolBarPanel)parent).indexOfToolBar(this) >= 0)
				{
					throw new IllegalArgumentException("Can't unset parent, because this component is still added!");
				}
			}
			else
			{
				if (tbp.indexOfToolBar(this) < 0)
				{
					throw new IllegalArgumentException("Can't set parent, because this component is not added!");
				}
			}
	  
			parent = pParent;
		}
		else
		{
			super.setParent(pParent);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void addIntern(Component pComponent, Object pConstraints, int pIndex)
	{
		if (pComponent instanceof JToolBar)
		{
			if (resource.getComponentCount() > 0)
			{
				((JToolBar)pComponent).add(new JToolBar.Separator(), null, 0);
			}
		}

		super.addIntern(pComponent, pConstraints, pIndex);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void removeIntern(Component pComponent)
	{
		super.removeIntern(pComponent);
		
		if (pComponent instanceof JToolBar)
		{
			if (((JToolBar)pComponent).getComponent(0) instanceof JToolBar.Separator)
			{
				((JToolBar)pComponent).remove(0);
			}
		}
	}
	
}	// SwingToolBar
