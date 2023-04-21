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

import javax.rad.ui.IAlignmentConstants;

import com.sibvisions.rad.ui.vaadin.ext.ui.panel.layout.AbstractAlignedLayout;
import com.sibvisions.rad.ui.vaadin.ext.ui.panel.layout.AbstractAlignedLayout.HorizontalAlignment;
import com.sibvisions.rad.ui.vaadin.ext.ui.panel.layout.AbstractAlignedLayout.VerticalAlignment;
import com.sibvisions.rad.ui.vaadin.ext.ui.panel.layout.AbstractGapLayout;

/**
 * The {@link AbstractAlignedVaadinClientLayout} provides the abstract base for all
 * wrappers of client-side layouts.
 * 
 * @author Martin Handsteiner
 * @param <L> the type of the {@link AbstractGapLayout}.
 * @param <CO> the type of the constraints.
 */
public abstract class AbstractAlignedVaadinClientLayout<L extends AbstractAlignedLayout, CO> extends AbstractVaadinClientLayout<L, CO>
		                                                                                     implements IAlignmentConstants
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link AbstractAlignedVaadinClientLayout}.
	 *
	 * @param pLayout the layout.
	 */
	protected AbstractAlignedVaadinClientLayout(L pLayout)
	{
		super(pLayout);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getHorizontalAlignment()
	{
		switch (layout.getHorizontalAlignment())
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
	@Override
	public void setHorizontalAlignment(int pHorizontalAlignment)
	{
		switch (pHorizontalAlignment)
		{
			case ALIGN_CENTER:
				layout.setHorizontalAlignment(HorizontalAlignment.Center);
				break;
			
			case ALIGN_LEFT:
				layout.setHorizontalAlignment(HorizontalAlignment.Left);
				break;
			
			case ALIGN_RIGHT:
				layout.setHorizontalAlignment(HorizontalAlignment.Right);
				break;
			
			case ALIGN_STRETCH:
				layout.setHorizontalAlignment(HorizontalAlignment.Stretch);
				break;
			
			case ALIGN_DEFAULT:
			default:
				layout.setHorizontalAlignment(HorizontalAlignment.Default);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getVerticalAlignment()
	{
		switch (layout.getVerticalAlignment())
		{
			case Bottom:
				return ALIGN_BOTTOM;
			
			case Center:
				return ALIGN_CENTER;
			
			case Stretch:
				return ALIGN_STRETCH;
			
			case Top:
				return ALIGN_TOP;
			
			case Default:
			default:
				return ALIGN_DEFAULT;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setVerticalAlignment(int pVerticalAlignment)
	{
		switch (pVerticalAlignment)
		{
			case ALIGN_BOTTOM:
				layout.setVerticalAlignment(VerticalAlignment.Bottom);
				break;
			
			case ALIGN_CENTER:
				layout.setVerticalAlignment(VerticalAlignment.Center);
				break;
			
			case ALIGN_STRETCH:
				layout.setVerticalAlignment(VerticalAlignment.Stretch);
				break;
			
			case ALIGN_TOP:
				layout.setVerticalAlignment(VerticalAlignment.Top);
				break;
			
			case ALIGN_DEFAULT:
			default:
				layout.setVerticalAlignment(VerticalAlignment.Default);
		}
	}
	
}	// AbstractAlignedVaadinClientLayout
