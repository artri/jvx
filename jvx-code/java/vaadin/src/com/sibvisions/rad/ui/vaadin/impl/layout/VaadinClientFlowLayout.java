/*
 * Copyright 2016 SIB Visions GmbH
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
 * 02.05.2016 - [RZ] - creation
 */
package com.sibvisions.rad.ui.vaadin.impl.layout;

import javax.rad.ui.IComponent;
import javax.rad.ui.layout.IFlowLayout;

import com.sibvisions.rad.ui.vaadin.ext.ui.panel.layout.AbstractAlignedLayout.HorizontalAlignment;
import com.sibvisions.rad.ui.vaadin.ext.ui.panel.layout.AbstractAlignedLayout.Orientation;
import com.sibvisions.rad.ui.vaadin.ext.ui.panel.layout.AbstractAlignedLayout.VerticalAlignment;
import com.sibvisions.rad.ui.vaadin.ext.ui.panel.layout.FlowLayout;
import com.vaadin.ui.Component;

/**
 * The {@link VaadinClientFlowLayout} is the Vaadin specific implementation of
 * the {@link javax.rad.ui.layout.IFormLayout} class.
 * <p>
 * This class wraps and provides {@link FlowLayout the client-side layout}.
 * 
 * @author Robert Zenz
 */
public class VaadinClientFlowLayout extends AbstractAlignedVaadinClientLayout<FlowLayout, Object> 
                                    implements IFlowLayout
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link VaadinClientFlowLayout}.
	 */
	public VaadinClientFlowLayout()
	{
		super(new FlowLayout());
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public int getComponentAlignment()
	{
		switch (layout.getHorizontalComponentAlignment())
		{
			case Center:
				return ALIGN_CENTER;
			
			case Left:
				return ALIGN_LEFT;
			
			case Right:
				return ALIGN_RIGHT;
			
			case Stretch:
				return ALIGN_STRETCH;
			
			case Default:
			default:
				return ALIGN_DEFAULT;
			
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Object getConstraints(IComponent pComp)
	{
		return resource.getConstraints((Component)pComp.getResource());
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getOrientation()
	{
		switch (layout.getOrientation())
		{
			case Vertical:
				return VERTICAL;
			
			case Horizontal:
			default:
				return HORIZONTAL;
			
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isAutoWrap()
	{
		return layout.isAutoWrap();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setAutoWrap(boolean pAutoWrap)
	{
		layout.setAutoWrap(pAutoWrap);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setComponentAlignment(int pComponentAlignment)
	{
		switch (pComponentAlignment)
		{
			case ALIGN_CENTER:
				layout.setHorizontalComponentAlignment(HorizontalAlignment.Center);
				layout.setVerticalComponentAlignment(VerticalAlignment.Center);
				break;
			
			case ALIGN_LEFT:
				layout.setHorizontalComponentAlignment(HorizontalAlignment.Left);
				layout.setVerticalComponentAlignment(VerticalAlignment.Top);
				break;
			
			case ALIGN_RIGHT:
				layout.setHorizontalComponentAlignment(HorizontalAlignment.Right);
				layout.setVerticalComponentAlignment(VerticalAlignment.Bottom);
				break;
			
			case ALIGN_STRETCH:
				layout.setHorizontalComponentAlignment(HorizontalAlignment.Stretch);
				layout.setVerticalComponentAlignment(VerticalAlignment.Stretch);
				break;
			
			case ALIGN_DEFAULT:
			default:
				layout.setHorizontalComponentAlignment(HorizontalAlignment.Default);
				layout.setVerticalComponentAlignment(VerticalAlignment.Default);
				break;
			
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setConstraints(IComponent pComp, Object pConstraints)
	{
		resource.setConstraints((Component)pComp.getResource(), pConstraints);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setOrientation(int pOrientation)
	{
		switch (pOrientation)
		{
			case VERTICAL:
				layout.setOrientation(Orientation.Vertical);
				break;
			
			case HORIZONTAL:
			default:
				layout.setOrientation(Orientation.Horizontal);
				break;
		}
	}
	
}	// VaadinClientFlowLayout
