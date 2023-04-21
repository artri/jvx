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
 * 02.12.2008 - [HM] - creation
 */
package com.sibvisions.rad.ui.swing.impl.layout;

import java.awt.Insets;

import javax.rad.ui.IComponent;
import javax.rad.ui.IInsets;
import javax.rad.ui.layout.IFlowLayout;

import com.sibvisions.rad.ui.awt.impl.AwtInsets;
import com.sibvisions.rad.ui.awt.impl.AwtResource;
import com.sibvisions.rad.ui.swing.ext.layout.JVxSequenceLayout;
import com.sibvisions.rad.ui.swing.impl.SwingFactory;

/**
 * The <code>AwtFormLayout</code> is a platform independent FlowLayout impelementation.
 * 
 * @author Martin Handsteiner
 * @see	com.sibvisions.rad.ui.swing.ext.layout.JVxSequenceLayout
 */
public class AwtFlowLayout extends AwtResource<JVxSequenceLayout> 
                           implements IFlowLayout
{

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of <code>AwtSimpleFormLayout</code> based on an
	 * <code>IFlowLayout</code> implementation.
	 * 
	 * @see IFlowLayout
	 */
	public AwtFlowLayout()
	{
		super(new JVxSequenceLayout());
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
	public int getHorizontalAlignment()
	{
		return SwingFactory.getHorizontalAlignment(resource.getHorizontalAlignment());
	}
	
    /**
     * {@inheritDoc}
     */
	public void setHorizontalAlignment(int pHorizontalAlignment)
	{
		resource.setHorizontalAlignment(SwingFactory.getHorizontalSwingAlignment(pHorizontalAlignment));
	}
	
    /**
     * {@inheritDoc}
     */
	public int getVerticalAlignment()
	{
		return SwingFactory.getVerticalAlignment(resource.getVerticalAlignment());
	}

    /**
     * {@inheritDoc}
     */
    public void setVerticalAlignment(int pVerticalAlignment)
    {
    	resource.setVerticalAlignment(SwingFactory.getVerticalSwingAlignment(pVerticalAlignment));
    }
    
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
	public int getComponentAlignment()
	{
		return SwingFactory.getHorizontalAlignment(resource.getHorizontalComponentAlignment());
	}

    /**
	 * {@inheritDoc}
	 */
	public void setComponentAlignment(int pComponentAlignment)
	{
		resource.setHorizontalComponentAlignment(SwingFactory.getHorizontalSwingAlignment(pComponentAlignment));
		resource.setVerticalComponentAlignment(SwingFactory.getVerticalSwingAlignment(pComponentAlignment));
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
    public void setHorizontalGap(int pHgap)
    {
    	resource.setHorizontalGap(pHgap);
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
    public void setVerticalGap(int pVgap)
    {
    	resource.setVerticalGap(pVgap);
    }
    
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
	public boolean isAutoWrap()
	{
		return resource.isAutoWrap();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setAutoWrap(boolean pAutoWrap)
	{
		resource.setAutoWrap(pAutoWrap);
	}

    /**
     * {@inheritDoc}
     */
	public Object getConstraints(IComponent pComponent)
	{
		return null;
	}

    /**
     * {@inheritDoc}
     */
	public void setConstraints(IComponent pComponent, Object pConstraints)
	{
		throw new IllegalArgumentException("Constraints can't been set!");
	}
	
}	// AwtFlowLayout
