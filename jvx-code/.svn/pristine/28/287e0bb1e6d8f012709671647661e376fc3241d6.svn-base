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
 * 22.11.2008 - [JR] - add: call super instead of addIntern [BUGFIX]
 */
package com.sibvisions.rad.ui.swing.impl.container;

import javax.rad.ui.IComponent;
import javax.rad.ui.ILayout;
import javax.rad.ui.container.ISplitPanel;
import javax.swing.JSplitPane;

import com.sibvisions.rad.ui.swing.ext.JVxSplitPane;
import com.sibvisions.rad.ui.swing.impl.SwingComponent;

/**
 * The <code>SwingSplitPanel</code> is the <code>ISplitPanel</code>
 * implementation for swing.
 * 
 * @author Martin Handsteiner
 * @see	javax.swing.JSplitPane
 * @see ISplitPanel
 */
public class SwingSplitPanel extends SwingComponent<JVxSplitPane> 
                             implements ISplitPanel
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The left or top Component. */
	private IComponent	firstComponent	= null;

	/** The right or bottom Component. */
	private IComponent	secondComponent	= null;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>SwingLabel</code> with a divider size
	 * of 6, continous layout enabled and one touch expandable enabled.
	 */
	public SwingSplitPanel()
	{
		super(new JVxSplitPane(JSplitPane.HORIZONTAL_SPLIT, true));
		
		// size is odd, for centered image.
		resource.setDividerSize(7);
		resource.setOneTouchExpandable(true);
		resource.setDividerLocation(0.5f);
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
    public IComponent getFirstComponent()
    {
    	return firstComponent;
    }

	/**
	 * {@inheritDoc}
	 */
    public void setFirstComponent(IComponent pComponent)
    {
        if (pComponent == null) 
        {
            if (firstComponent != null) 
            {
                remove(firstComponent);
            }
        } 
        else
        {
            add(pComponent, FIRST_COMPONENT, 0);
        }
    }

	/**
	 * {@inheritDoc}
	 */
    public IComponent getSecondComponent()
    {
    	return secondComponent;
    }

	/**
	 * {@inheritDoc}
	 */
    public void setSecondComponent(IComponent pComponent)
    {
        if (pComponent == null) 
        {
            if (secondComponent != null) 
            {
                remove(secondComponent);
            }
        } 
        else 
        {
            add(pComponent, SECOND_COMPONENT, -1);
        }
    }
	
	/**
	 * {@inheritDoc}
	 */
    public int getDividerPosition()
    {
    	return resource.getDividerLocation();
    }

	/**
	 * {@inheritDoc}
	 */
    public void setDividerPosition(int pDividerPosition)
    {
    	resource.setDividerLocation(pDividerPosition);
    }

	/**
	 * {@inheritDoc}
	 */
    public int getDividerAlignment()
    {
    	return resource.getDividerAlignment();
    }

	/**
	 * {@inheritDoc}
	 */
	public void setDividerAlignment(int pDividerAlignment)
	{
		resource.setDividerAlignment(pDividerAlignment);
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
	public void add(IComponent pComponent, Object pConstraints, int pIndex)
    {
        if (pConstraints == FIRST_COMPONENT && firstComponent != null)
        {
        	remove(firstComponent);
        }
        else if (pConstraints == SECOND_COMPONENT && secondComponent != null)
        {
        	remove(secondComponent);
        }
        else if (pConstraints == null)
        {
        	if (firstComponent == null)
        	{
        		pConstraints = FIRST_COMPONENT;
        	}
        	else if (secondComponent == null)
        	{
        		pConstraints = SECOND_COMPONENT;
        	}
        }
        if (pConstraints == FIRST_COMPONENT)
        {
        	super.add(pComponent, JSplitPane.LEFT, 0);
        	firstComponent = pComponent;
        }
        else if (pConstraints == SECOND_COMPONENT)
        {
        	super.add(pComponent, JSplitPane.RIGHT, -1);
        	secondComponent = pComponent;
        }
        else
        {
        	throw new IllegalArgumentException("SplitPanel can only handle first and second component!");
        }
    }
    
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove(int pIndex)
	{
		IComponent comp = getComponent(pIndex);
		
		super.remove(pIndex);
		
		if (comp == firstComponent)
		{
			firstComponent = null;	
		}
		else if (comp == secondComponent)
		{
			secondComponent = null;
		}
	}
	
}	// SwingSplitPanel
