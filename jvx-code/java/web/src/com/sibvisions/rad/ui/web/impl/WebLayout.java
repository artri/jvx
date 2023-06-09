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
 * 26.11.2009 - [HM] - creation
 */
package com.sibvisions.rad.ui.web.impl;

import javax.rad.ui.IComponent;
import javax.rad.ui.IInsets;
import javax.rad.ui.ILayout;

import com.sibvisions.util.type.CommonUtil;

/**
 * Web server implementation of {@link ILayout}.
 *  
 * @param <CE> the Contraint type.
 * 
 * @author Martin Handsteiner
 */
public abstract class WebLayout<CE> extends WebResource
								    implements ILayout<CE>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the container. */
	private IWebContainer container;

	/** the margins of the layout. */
	protected WebInsets margins = null;
	
	/** the horizontal gap. */
	protected int horizontalGap = 5;
	/** the vertical gap. */
	protected int verticalGap = 5;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>WebLayout</code>.
     *
     * @see javax.rad.ui.ILayout
     */
	protected WebLayout()
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
    @Override
	public String getAsString()
    {
    	WebInsets m = getMargins();
    	
    	return getClass().getSimpleName().substring(3) + "," + 
    		m.getTop() + "," + m.getLeft() + "," + m.getBottom() + "," + m.getRight() + "," +
    		horizontalGap + "," + verticalGap; 
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public CE getConstraints(IComponent pComponent)
	{
		return (CE)((WebComponent)pComponent).getConstraints();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setConstraints(IComponent pComponent, CE pConstraints)
	{
		((WebComponent)pComponent).setConstraints(pConstraints);
	}

	/**
	 * {@inheritDoc}
	 */
    public WebInsets getMargins()
    {
    	if (margins == null)
    	{
    		margins = new WebInsets(0, 0, 0, 0);
    	}
    	
    	return margins;
    }
    
	/**
	 * {@inheritDoc}
	 */
    public void setMargins(IInsets pMargins)
    {
    	IInsets old = margins;
    	
    	margins = (WebInsets)pMargins;
    	
    	if (!CommonUtil.equals(old, margins))
    	{
    		markChanged();
    	}
    }

	/**
	 * {@inheritDoc}
	 */
    public int getHorizontalGap()
    {
    	return horizontalGap;
    }

	/**
	 * {@inheritDoc}
	 */
    public void setHorizontalGap(int pHorizontalGap)
    {
    	int old = horizontalGap;
    	
    	horizontalGap = pHorizontalGap;

    	if (old != horizontalGap)
    	{
    		markChanged();
    	}
    }

	/**
	 * {@inheritDoc}
	 */
    public int getVerticalGap()
    {
    	return verticalGap;
    }

	/**
	 * {@inheritDoc}
	 */
    public void setVerticalGap(int pVerticalGap)
    {
    	int old = verticalGap;

    	verticalGap = pVerticalGap;
    	
    	if (old != verticalGap)
    	{
    		markChanged();
    	}
    }
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the layout data for the given container.
	 * 
	 * @param pContainer the (parent)container
	 * @return the layout data or <code>null</code> if no specific data is available
	 */
	protected abstract String getData(IWebContainer pContainer);
	
	/**
	 * Gets the constraints be the given constraint data.
	 * 
	 * @param pConstraintData the constraint data
	 * @return the constraint
	 */
	public abstract CE getConstraints(String pConstraintData);
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Converts an added Constraint to the correct Constraint type CE. This method is relevant because other layouts
	 * may override it to support Object parameter instead of Constraint type CE as parameter. This method should 
	 * convert an Object to the Constraint type CE.
	 * 
	 * @param pComponent the component.
	 * @param pConstraints the constraints.
	 */
	public void setComponentConstraints(IComponent pComponent, Object pConstraints)
	{
		setConstraints(pComponent, (CE)pConstraints);
	}	
	
	/**
	 * Gets the container that is layouted by this layout.
	 * 
	 * @return the container that is layouted by this layout.
	 */
	public IWebContainer getContainer()
	{
	    return container;
	}
	
	/**
	 * Sets the container which holds this layout.
	 * 
	 * @param pContainer the container
	 */
	void setContainer(IWebContainer pContainer)
	{
		if (container != null)
		{
			container.setProperty("layout", null);
		}
		
		container = pContainer;
		
		if (container != null)
		{
			container.setProperty("layout", this);
		}
	}
	
	/**
	 * Marks this layout as changed.
	 */
	protected void markChanged()
	{
		if (container != null)
		{
			//force layout update
			container.setProperty("layout", this, true);
		}
	}
    
}	// WebLayout
