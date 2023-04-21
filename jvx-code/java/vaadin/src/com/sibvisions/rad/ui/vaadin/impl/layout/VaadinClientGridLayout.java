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
 * 11.05.2016 - [RZ] - creation
 */
package com.sibvisions.rad.ui.vaadin.impl.layout;

import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

import javax.rad.ui.IComponent;
import javax.rad.ui.IInsets;
import javax.rad.ui.layout.IGridLayout;

import com.sibvisions.rad.ui.vaadin.ext.ui.client.panel.helper.Margins;
import com.sibvisions.rad.ui.vaadin.ext.ui.panel.layout.GridLayout;
import com.sibvisions.rad.ui.vaadin.ext.ui.panel.layout.GridLayout.GridConstraints;
import com.sibvisions.rad.ui.vaadin.impl.VaadinInsets;
import com.sibvisions.rad.ui.vaadin.impl.VaadinResource;
import com.vaadin.ui.Component;

/**
 * The {@link VaadinClientGridLayout} is the Vaadin specific implementation of
 * the {@link IGridLayout} class.
 * <p>
 * This class wraps and provides {@link GridLayout the client-side layout}.
 * 
 * @author Robert Zenz
 */
public class VaadinClientGridLayout extends AbstractVaadinClientLayout<GridLayout, IGridLayout.IGridConstraints> 
                                    implements IGridLayout
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The constraints cache. */
	private WeakHashMap<GridConstraints, WeakReference<VaadinGridConstraints>> constraints = new WeakHashMap<GridConstraints, WeakReference<VaadinGridConstraints>>();
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link VaadinClientGridLayout}.
	 *
	 * @param pColumns the columns.
	 * @param pRows the rows.
	 */
	public VaadinClientGridLayout(int pColumns, int pRows)
	{
		super(new GridLayout(pColumns, pRows));
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public int getColumns()
	{
		return layout.getColumns();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public IGridConstraints getConstraints(IComponent pComp)
	{
		return getConstraints((GridConstraints)resource.getConstraints((Component)pComp.getResource()));
	}
	
	/**
	 * {@inheritDoc}
	 */
	public IGridConstraints getConstraints(int pColumns, int pRows)
	{
		return getConstraints(layout.getConstraints(pColumns, pRows));
	}
	
	/**
	 * {@inheritDoc}
	 */
	public IGridConstraints getConstraints(int pColumns, int pRows, int pWidth, int pHeight)
	{
		return getConstraints(layout.getConstraints(pColumns, pRows, pWidth, pHeight));
	}
	
	/**
	 * {@inheritDoc}
	 */
	public IGridConstraints getConstraints(int pColumns, int pRows, int pWidth, int pHeight, IInsets pInsets)
	{
		// TODO Insets are missing.
		return getConstraints(layout.getConstraints(pColumns, pRows, pWidth, pHeight));
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getRows()
	{
		return layout.getRows();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setColumns(int pColumns)
	{
		layout.setColumns(pColumns);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setConstraints(IComponent pComp, IGridConstraints pConstraints)
	{
		resource.setConstraints(
				(Component)pComp.getResource(),
				((VaadinGridConstraints)pConstraints).getResource());
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setRows(int pRows)
	{
		layout.setRows(pRows);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addComponent(IComponent pComponent, Object pConstraints, int pIndex)
	{
		super.addComponent(pComponent, ((VaadinGridConstraints)pConstraints).getResource(), pIndex);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the {@link VaadinGridConstraints constraints}.
	 *
	 * @param pConstraints the {@link GridConstraints grid constraints}.
	 * @return the {@link VaadinGridConstraints constraints}.
	 */
	private VaadinGridConstraints getConstraints(GridConstraints pConstraints)
	{
		if (pConstraints == null)
		{
			return null;
		}
		else
		{
			WeakReference<VaadinGridConstraints> constraintRef = constraints.get(pConstraints);
	
			if (constraintRef != null)
			{
				VaadinGridConstraints constraint = constraintRef.get();
	
				if (constraint != null)
				{
					return constraint;
				}
			}
			
			VaadinGridConstraints gridConstraints = new VaadinGridConstraints(pConstraints);
			
			constraints.put(pConstraints, new WeakReference<VaadinGridConstraints>(gridConstraints));
			
			return gridConstraints;
		}
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * The {@link VaadinGridConstraints} is the Vaadin specific implementation
	 * of {@link javax.rad.ui.layout.IGridLayout.IGridConstraints}.
	 * 
	 * @author Robert Zenz
	 */
	public static class VaadinGridConstraints extends VaadinResource<GridConstraints, GridConstraints> 
	                                          implements IGridConstraints
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of {@link VaadinGridConstraints}.
		 *
		 * @param pResource the {@link GridConstraints resource}.
		 */
		public VaadinGridConstraints(GridConstraints pResource)
		{
			super(pResource);
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Interface implementation
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * {@inheritDoc}
		 */
		public int getGridHeight()
		{
			return resource.height;
		}
		
		/**
		 * {@inheritDoc}
		 */
		public int getGridWidth()
		{
			return resource.width;
		}
		
		/**
		 * {@inheritDoc}
		 */
		public int getGridX()
		{
			return resource.x;
		}
		
		/**
		 * {@inheritDoc}
		 */
		public int getGridY()
		{
			return resource.y;
		}
		
		/**
		 * {@inheritDoc}
		 */
		public IInsets getInsets()
		{
			Margins padding = resource.padding;
			
			return new VaadinInsets(
					padding.top,
					padding.left,
					padding.bottom,
					padding.right);
		}
		
		/**
		 * {@inheritDoc}
		 */
		public void setGridHeight(int pGridHeight)
		{
			resource.setHeight(pGridHeight);
		}
		
		/**
		 * {@inheritDoc}
		 */
		public void setGridWidth(int pGridWidth)
		{
			resource.setWidth(pGridWidth);
		}
		
		/**
		 * {@inheritDoc}
		 */
		public void setGridX(int pGridX)
		{
			resource.setColumn(pGridX);
		}
		
		/**
		 * {@inheritDoc}
		 */
		public void setGridY(int pGridY)
		{
			resource.setRow(pGridY);
		}
		
		/**
		 * {@inheritDoc}
		 */
		public void setInsets(IInsets pInsets)
		{
			if (pInsets != null)
			{
				resource.setPaddingBottom(pInsets.getBottom());
				resource.setPaddingLeft(pInsets.getLeft());
				resource.setPaddingRight(pInsets.getRight());
				resource.setPaddingTop(pInsets.getTop());
			}
			else
			{
				resource.setPaddingBottom(0);
				resource.setPaddingLeft(0);
				resource.setPaddingRight(0);
				resource.setPaddingTop(0);
			}
		}
		
	}	// VaadinGridConstraints

}	// VaadinClientGridLayout
