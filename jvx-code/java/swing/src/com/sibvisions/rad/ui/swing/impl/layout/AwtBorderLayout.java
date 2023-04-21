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
 */
package com.sibvisions.rad.ui.swing.impl.layout;

import java.awt.Component;
import java.awt.Insets;

import javax.rad.ui.IComponent;
import javax.rad.ui.IInsets;
import javax.rad.ui.layout.IBorderLayout;

import com.sibvisions.rad.ui.awt.impl.AwtInsets;
import com.sibvisions.rad.ui.awt.impl.AwtResource;
import com.sibvisions.rad.ui.swing.ext.layout.JVxBorderLayout;

/**
 * The <code>AwtBorderLayout</code> is a platform independent BorderLayout impelementation.
 * 
 * @author Martin Handsteiner
 * @see	java.awt.BorderLayout
 */
public class AwtBorderLayout extends AwtResource<JVxBorderLayout> 
							 implements IBorderLayout
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>AwtBorderLayout</code> based on an
	 * <code>IBorderLayout</code> implementation.
	 * 
	 * @see IBorderLayout
	 */
	public AwtBorderLayout()
	{
		super(new JVxBorderLayout());
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
    public IInsets getMargins()
    {
    	return new AwtInsets(resource.getMargins());
    }
    
	/**
	 * {@inheritDoc}
	 */
    public void setMargins(IInsets pMargins)
    {
    	if (pMargins == null)
    	{
    		resource.setMargins(null);
    	}
    	else
    	{
        	resource.setMargins((Insets)pMargins.getResource());
    	}
    }

	/**
     * {@inheritDoc}
     */
	public int getHorizontalGap()
	{
		return resource.getHorizontalGap();
	}

    /**
     * {@inheritDoc}
     */
    public void setHorizontalGap(int pHorizontalGap)
    {
    	resource.setHorizontalGap(pHorizontalGap);
    }

    /**
     * {@inheritDoc}
     */
    public int getVerticalGap()
    {
    	return resource.getVerticalGap();
    }

    /**
     * {@inheritDoc}
     */
    public void setVerticalGap(int pVerticalGap)
    {
    	resource.setVerticalGap(pVerticalGap);
    }

    /**
     * {@inheritDoc}
     */
	public String getConstraints(IComponent pComponent)
	{
		return (String)resource.getConstraints((Component)pComponent.getResource());
	}

    /**
     * {@inheritDoc}
     */
	public void setConstraints(IComponent pComponent, String pConstraints)
	{
		//throw new IllegalArgumentException("Constraints can't been set!");
	}
	
}	// AwtBorderLayout
