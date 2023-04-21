/*
 * Copyright 2014 SIB Visions GmbH
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
 * 06.03.2014 - [LT] - erstellt
 */
package com.sibvisions.rad.ui.swing.impl.layout;

import java.awt.Component;
import java.awt.Insets;
import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

import javax.rad.ui.IComponent;
import javax.rad.ui.IInsets;
import javax.rad.ui.layout.IGridLayout;

import com.sibvisions.rad.ui.awt.impl.AwtInsets;
import com.sibvisions.rad.ui.awt.impl.AwtResource;
import com.sibvisions.rad.ui.swing.ext.layout.JVxGridLayout;
import com.sibvisions.rad.ui.swing.ext.layout.JVxGridLayout.CellConstraints;

/**
 * The <code>AwtGridLayout</code> is the {@link IGridLayout} implementation for AWT.
 * 
 * @author Thomas Lehner
 * @see	JVxGridLayout
 */
public class AwtGridLayout extends AwtResource<JVxGridLayout> 
                           implements IGridLayout
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** Hashmap to store the constraints. */
	private WeakHashMap<JVxGridLayout.CellConstraints, WeakReference<AwtGridConstraints>> constraints = new WeakHashMap
													<JVxGridLayout.CellConstraints, WeakReference<AwtGridConstraints>>();

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of <code>AwtGridLayout</code> based on an
	 * <code>IGridLayout</code> implementation.
	 * 
	 * @param pColumns the column count
	 * @param pRows the row count
	 * @see IGridLayout
	 */
	public AwtGridLayout(int pColumns, int pRows)
	{
		super(new JVxGridLayout(pColumns, pRows));
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
	public void setColumns(int pColumns)
	{
		resource.setColumns(pColumns);
	}

	/**
	 * {@inheritDoc}
	 */
	public int getColumns()
	{
		return resource.getColumns();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setRows(int pRows)
	{
		resource.setRows(pRows);
	}

	/**
	 * {@inheritDoc}
	 */
	public int getRows()
	{
		return resource.getRows();
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
	public IGridConstraints getConstraints(IComponent pComponent)
	{
		return getConstraints(resource.getConstraints((Component)pComponent.getResource()));
	}

	/**
	 * {@inheritDoc}
	 */
	public void setConstraints(IComponent pComponent, IGridConstraints pConstraints)
	{
		resource.setConstraints((Component)pComponent.getResource(), (JVxGridLayout.CellConstraints)pConstraints.getResource());
	}

	/**
	 * {@inheritDoc}
	 */
	public IGridConstraints getConstraints(int pColumns, int pRows)
	{
		return getConstraints(resource.getConstraints(pColumns, pRows));
	}

	/**
	 * {@inheritDoc}
	 */
	public IGridConstraints getConstraints(int pColumns, int pRows, int pWidth, int pHeight)
	{
		return getConstraints(resource.getConstraints(pColumns, pRows, pWidth, pHeight));
	}

	/**
	 * {@inheritDoc}
	 */
	public IGridConstraints getConstraints(int pColumns, int pRows, int pWidth, int pHeight, IInsets pInsets)
	{
		Insets insets;
		
		if (pInsets == null)
		{
			insets = null;
		}
		else
		{
			insets = (Insets)pInsets.getResource();
		}
		
		return getConstraints(resource.getConstraints(pColumns, pRows, pWidth, pHeight, insets));
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Gets the JVxGridLayout.Constraint from an CellCounstraints.
     * 
     * @param pConstraints the constraints
     * @return the JVxGridLayout.Constraint
     */
	private AwtGridConstraints getConstraints(JVxGridLayout.CellConstraints pConstraints)
	{
		if (pConstraints == null)
		{
			return null;
		}
		else
		{
			WeakReference<AwtGridConstraints> constraintRef = constraints.get(pConstraints);
			
			if (constraintRef != null)
			{
				AwtGridConstraints constraint = constraintRef.get();

				if (constraint != null)
				{
					return constraint;
				}
			}
			
			AwtGridConstraints constraint = new AwtGridConstraints(pConstraints);

			constraints.put(pConstraints, new WeakReference<AwtGridConstraints>(constraint));

			return constraint;
		}
	}

	//****************************************************************
	// Subinterface definition
	//****************************************************************
	
	/**
	 * The Constraint stores the columns and rows for layouting a component.
	 * 
	 * @author Thomas Lehner
	 */
	public static class AwtGridConstraints extends AwtResource<CellConstraints> 
	                                       implements IGridConstraints, Cloneable
	{	
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class Members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/** cached constraints. */
		private IInsets insets;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Constructs a new AwtConstraint.
		 * 
		 * @param pConstraints the JVxGridLayout.Constraint.
		 */
		protected AwtGridConstraints(JVxGridLayout.CellConstraints pConstraints)
		{
			super(pConstraints);
			
			insets = new AwtInsets(resource.getInsets());
		}

		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    // Interface implementation
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * {@inheritDoc}
		 */
		public int getGridX()
		{
			return resource.getGridX();
		}

		/**
		 * {@inheritDoc}
		 */
		public int getGridY()
		{
			return resource.getGridY();
		}

		/**
		 * {@inheritDoc}
		 */
		public int getGridWidth()
		{
			return resource.getGridWidth();
		}

		/**
		 * {@inheritDoc}
		 */
		public int getGridHeight()
		{
			return resource.getGridHeight();
		}

		/**
		 * {@inheritDoc}
		 */
		public IInsets getInsets()
		{
			return insets;
		}

		/**
		 * {@inheritDoc}
		 */
		public void setGridX(int pGridX)
		{
			resource.setGridX(pGridX);
		}

		/**
		 * {@inheritDoc}
		 */
		public void setGridY(int pGridY)
		{
			resource.setGridY(pGridY);
		}

		/**
		 * {@inheritDoc}
		 */
		public void setGridHeight(int pGridHeight)
		{
			resource.setGridHeight(pGridHeight);
		}

		/**
		 * {@inheritDoc}
		 */
		public void setGridWidth(int pGridWidth)
		{
			resource.setGridWidth(pGridWidth);
		}

		/**
		 * {@inheritDoc}
		 */
		public void setInsets(IInsets pInsets)
		{
			if (pInsets == null)
			{
				resource.setInsets(null);

				insets = new AwtInsets(resource.getInsets());
			}
			else
			{
				resource.setInsets((Insets)pInsets.getResource());
				
				insets = pInsets;
			}
		}
		
	}	// AwtGridConstraint
	
}	// AwtGridLayout
