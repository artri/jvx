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
package com.sibvisions.rad.ui.web.impl.layout;

import javax.rad.ui.IAlignmentConstants;
import javax.rad.ui.layout.IFlowLayout;

import com.sibvisions.rad.ui.web.impl.IWebContainer;
import com.sibvisions.rad.ui.web.impl.WebLayout;

/**
 * Web server implementation of {@link IFlowLayout}.
 * 
 * @author Martin Handsteiner
 */
public class WebFlowLayout extends WebLayout<Object> 
						   implements IFlowLayout
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the horizontal alignment. */
	private int horizontalAlignment = IAlignmentConstants.ALIGN_CENTER;
	/** the vertical alignment. */
	private int verticalAlignment = IAlignmentConstants.ALIGN_CENTER;
	
	/** the orientation. */
	private int orientation = IFlowLayout.HORIZONTAL;

	/** the component alignment. */
	private int componentAlignment = IFlowLayout.ALIGN_CENTER;

	/** the auto wrap. */
	private boolean autoWrap = false;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>WebFlowLayout</code>.
     *
     * @see javax.rad.ui.layout.IFlowLayout
     */
	public WebFlowLayout()
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Abstract methods implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getData(IWebContainer pContainer)
	{
		return null;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
	public int getHorizontalAlignment()
	{
		return horizontalAlignment;
	}
	
    /**
     * {@inheritDoc}
     */
	public void setHorizontalAlignment(int pHorizontalAlignment)
	{
		int old = horizontalAlignment;
		
		horizontalAlignment = pHorizontalAlignment;
		
		if (old != horizontalAlignment)
		{
			markChanged();
		}
	}
	
    /**
     * {@inheritDoc}
     */
	public int getVerticalAlignment()
	{
		return verticalAlignment;
	}

    /**
     * {@inheritDoc}
     */
    public void setVerticalAlignment(int pVerticalAlignment)
    {
    	int old = verticalAlignment;
    	
    	verticalAlignment = pVerticalAlignment;
    	
		if (old != horizontalAlignment)
		{
			markChanged();
		}    	
    }
    
    /**
	 * {@inheritDoc}
	 */
	public int getOrientation()
	{
		return orientation;
	}

    /**
	 * {@inheritDoc}
	 */
	public void setOrientation(int pOrientation)
	{
		int old = orientation;
		
		orientation = pOrientation;
		
		if (old != orientation)
		{
			markChanged();
		}
	}

    /**
	 * {@inheritDoc}
	 */
	public int getComponentAlignment()
	{
		return componentAlignment;
	}

    /**
	 * {@inheritDoc}
	 */
	public void setComponentAlignment(int pComponentAlignment)
	{
		int old = componentAlignment;
		
		componentAlignment = pComponentAlignment;
		
		if (old != componentAlignment)
		{
			markChanged();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isAutoWrap()
	{
		return autoWrap;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setAutoWrap(boolean pAutoWrap)
	{
		boolean old = autoWrap;
		
		autoWrap = pAutoWrap;
		
		if (old != autoWrap)
		{
			markChanged();
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
    public String getAsString()
    {
    	return super.getAsString() + "," + orientation + "," 
    			+ horizontalAlignment + "," + verticalAlignment + "," 
    			+ componentAlignment + "," + autoWrap;
    }
	
}	// WebFlowLayout
