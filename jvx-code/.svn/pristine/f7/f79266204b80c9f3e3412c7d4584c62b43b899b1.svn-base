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

import javax.rad.ui.IComponent;
import javax.rad.ui.container.ISplitPanel;

import com.sibvisions.rad.ui.web.impl.WebComponent;
import com.sibvisions.rad.ui.web.impl.WebContainer;

/**
 * Web server implementation of {@link ISplitPanel}.
 * 
 * @author Martin Handsteiner
 */
public class WebSplitPanel extends WebContainer
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
     * Creates a new instance of <code>WebSplitPanel</code>.
     *
     * @see ISplitPanel
     */
	public WebSplitPanel()
	{
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
    public int getOrientation()
    {
    	return getProperty("orientation", Integer.valueOf(ISplitPanel.SPLIT_LEFT_RIGHT)).intValue();
    }

	/**
	 * {@inheritDoc}
	 */
    public void setOrientation(int pOrientation)
    {
    	setProperty("orientation", Integer.valueOf(pOrientation));
    }
    
	/**
	 * {@inheritDoc}
	 */
    public int getDividerPosition()
    {
    	return getProperty("dividerPosition", Integer.valueOf(0)).intValue();
    }

	/**
	 * {@inheritDoc}
	 */
    public void setDividerPosition(int pDividerPosition)
    {
    	setProperty("dividerPosition", Integer.valueOf(pDividerPosition));
    }

	/**
	 * {@inheritDoc}
	 */
    public int getDividerAlignment()
    {
    	return getProperty("dividerAlignment", Integer.valueOf(ISplitPanel.DIVIDER_RELATIVE)).intValue();
    }

	/**
	 * {@inheritDoc}
	 */
	public void setDividerAlignment(int pDividerAlignment)
	{
		setProperty("dividerAlignment", Integer.valueOf(pDividerAlignment));
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
            add(pComponent, FIRST_COMPONENT);
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
            add(pComponent, SECOND_COMPONENT);
        }
    }

	/**
	 * {@inheritDoc}
	 */
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
        	super.add(pComponent, FIRST_COMPONENT, 0);
        	firstComponent = pComponent;
        	
        }
        else if (pConstraints == SECOND_COMPONENT)
        {
        	super.add(pComponent, SECOND_COMPONENT, -1);
        	secondComponent = pComponent;
        }
        else
        {
        	throw new IllegalArgumentException("SplitPanel can only handle first and second component!");
        }
    	((WebComponent)pComponent).setConstraints(pConstraints);
    }
    
	/**
	 * {@inheritDoc}
	 */
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
	
}	// WebSplitPanel
