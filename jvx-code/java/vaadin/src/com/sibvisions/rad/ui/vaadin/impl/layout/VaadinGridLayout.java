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
 * 06.03.2014 - [TL] - creation
 * 23.01.2015 - [JR] - #1232: removed references to AWT
 */
package com.sibvisions.rad.ui.vaadin.impl.layout;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.rad.ui.IComponent;
import javax.rad.ui.IInsets;
import javax.rad.ui.layout.IGridLayout;

import com.sibvisions.rad.ui.vaadin.ext.VaadinUtil;
import com.sibvisions.rad.ui.vaadin.impl.VaadinComponentBase;
import com.sibvisions.rad.ui.vaadin.impl.VaadinInsets;
import com.sibvisions.rad.ui.vaadin.impl.VaadinResourceBase;
import com.sibvisions.util.log.LoggerFactory;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.GridLayout.OverlapsException;
import com.vaadin.ui.Label;

/**
 * The <code>VaadinGridLayout</code> class is the vaadin implementation of {@link IGridLayout}.
 * 
 * @author Thomas Lehner
 */
public class VaadinGridLayout extends AbstractVaadinLayout<GridLayout, IGridLayout.IGridConstraints> 
							  implements IGridLayout
{
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /** Stores all constraints. */
    private final Map<IComponent, CellConstraints>  constraintMap;

    /** The number of rows. */
	private int										rows = 1;

	/** The number of columns. */
	private int										columns	= 1;

	/** Margin. **/
	private IInsets                                 margins = new VaadinInsets(0, 0, 0, 0);
	
	/** Horizontal Gap. **/
	private int                                     horizontalGap = 0;
	
	/** Vertical Gap. **/
	private int                                     verticalGap = 0;
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of <code>VaadinGridLayout</code> with given columns and rows.
	 * 
	 * @param pColumns the column count.
	 * @param pRows the row count.
	 * @see IGridLayout
	 */
	public VaadinGridLayout(int pColumns, int pRows)
	{
		super(new GridLayout());
		
		setRows(pRows);
		setColumns(pColumns);
		
		constraintMap = new HashMap<IComponent, CellConstraints>();
		
		resource.setSizeUndefined();
	}
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~		

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void repaintLayout(boolean pComponentsChanged)	
	{
		if (pComponentsChanged)
		{
			resource.removeAllComponents();

	        int targetColumns = columns;
	        int targetRows = rows;

	        for (Map.Entry<IComponent, CellConstraints> entry : constraintMap.entrySet())
	        {
	            IComponent component = entry.getKey();
	            
	            if (component.isVisible())
	            {
	                CellConstraints constraints = entry.getValue();
	                
	                if (columns <= 0 && constraints.gridX + constraints.gridWidth > targetColumns)
	                {
	                    targetColumns = constraints.gridX + constraints.gridWidth;
	                }
	                if (rows <= 0 && constraints.gridY + constraints.gridHeight > targetRows)
	                {
	                    targetRows = constraints.gridY + constraints.gridHeight;
	                }
	            }
	        }

			resource.setColumns(targetColumns * 2 + 1);
			resource.setRows(targetRows * 2 + 1);
			
			for (int i = 0, cnt = resource.getColumns(); i < cnt; i++)
			{
				if (i % 2 != 0)
				{
					resource.setColumnExpandRatio(i, 1f);
				}
				else if (i > 0 && i < cnt - 1)
				{
					Label label = new Label();
					label.setWidth(getHorizontalGap() + "px");
					resource.addComponent(label, i, 0);
				}
			}
			
			for (int i = 1, cnt = resource.getRows(); i < cnt; i++)
			{
				if (i % 2 != 0)
				{
					resource.setRowExpandRatio(i, 1f);
				}
				else if (i < cnt - 1)
				{
					Label label = new Label();
					label.setHeight(getVerticalGap() + "px");
					resource.addComponent(label, 0, i);
				}
			}
			
			/*********************** Set Left-Margin, Right-Margin and Horizontal-Gaps ***************************/
	
			Label marginTopLeft = new Label();
			Label marginBottomRight = new Label();
			
			marginTopLeft.setHeight(margins == null ? "0px" : margins.getTop() + "px");
			marginTopLeft.setWidth(margins == null ? "0px" : margins.getLeft() + "px");
			marginBottomRight.setHeight(margins == null ? "0px" : margins.getBottom() + "px");
			marginBottomRight.setWidth(margins == null ? "0px" : margins.getRight() + "px");
			
			resource.addComponent(marginTopLeft, 0, 0);
			resource.addComponent(marginBottomRight, resource.getColumns() - 1, resource.getRows() - 1);
			
			Set<IComponent> components = constraintMap.keySet();
			
			for (IComponent component : components)
			{
				VaadinComponentBase<?, ?> componentBase = (VaadinComponentBase<?, ?>)component;
				
				if (componentBase.isPreferredSizeSet()) // Reinitialize the size of the components.
				{
					componentBase.setSize(componentBase.getPreferredSize());
				}
				else
				{
					componentBase.setSizeUndefined();
				}
	
				applySize(componentBase, "max", componentBase.getMaximumSize(), 
						  false, 
						  false);
				applySize(componentBase, "min", componentBase.getMinimumSize(), 
						  componentBase.isMinimumSizeSet(), 
						  componentBase.isMinimumSizeSet());
				
				CellConstraints constraints = (CellConstraints) ((VaadinComponentBase<?, ?>) component).getConstraints();
	
				((com.vaadin.ui.Component) component.getResource()).setSizeFull();
				
				int gridX = constraints.getGridX() * 2 + 1;
				int gridY = constraints.getGridY() * 2 + 1;
				int gridWidth = constraints.getGridWidth() * 2 - 1;
				int gridHeight = constraints.getGridHeight() * 2 - 1;
				
				try
				{
					if (VaadinUtil.isParentNull(component))
					{
						resource.addComponent((com.vaadin.ui.Component) component.getResource(), 
											  gridX, gridY, gridX + gridWidth - 1, gridY + gridHeight - 1);				
					}
				}
				catch (OverlapsException e) // A Component at this position is already added
				{	
					Component comp = (Component)resource.getComponent(columns, rows);
					
					LoggerFactory.getInstance(getClass()).debug("A Component ", (comp != null ? comp.getClass() : "<undefined>"), 
																" is already added at position (column, row, endColumn, endRow) ",
																Integer.valueOf(constraints.getGridX()), ", ", Integer.valueOf(constraints.getGridY()));
				}
				catch (Exception e)
				{
					LoggerFactory.getInstance(getClass()).error("Exception while adding a component to the layout.", e);
				}
	
			}
		}
		
		performLayout();
	}	
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
     * {@inheritDoc}
     */
	public IGridConstraints getConstraints(IComponent pComp)
	{
		return constraintMap.get(pComp);
	}

	/**
     * {@inheritDoc}
     */
	public void setConstraints(IComponent pComp, IGridConstraints pConstraints)
	{
		checkNotNull(pComp, "The component must not be null.");
		checkNotNull(pConstraints, "The constraints must not be null.");

		constraintMap.put((VaadinComponentBase<?, ?>)pComp, (CellConstraints)((CellConstraints)pConstraints).clone());
		
		markComponentsChanged();
	}

	/**
     * {@inheritDoc}
     */
	public IInsets getMargins()
	{
		return margins;
	}

	/**
     * {@inheritDoc}
     */
	public void setMargins(IInsets pMargins)
	{
		margins = pMargins;

    	markDirty();
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
		horizontalGap = pHorizontalGap;
		
		markDirty();
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
		verticalGap = pVerticalGap;
	}

	/**
     * {@inheritDoc}
     */
	public void addComponent(IComponent pComponent, Object pConstraints, int pIndex)
	{
		checkNotNull(pConstraints, "The constraints must not be null.");
		
		if (pConstraints instanceof CellConstraints)
		{
			setConstraints(pComponent, (CellConstraints)pConstraints);
		}
		else
		{
			throw new IllegalArgumentException("Illegal constraint type " + pConstraints.getClass());
		}
		
		markComponentsChanged();
	}

	/**
     * {@inheritDoc}
     */
	public void removeComponent(IComponent pComponent)
	{
		constraintMap.remove(pComponent);
		
		markComponentsChanged();
	}

	/**
     * {@inheritDoc}
     */
	public IGridConstraints getConstraints(int pColumns, int pRows)
	{
		return new CellConstraints(pColumns, pRows);
	}

	/**
     * {@inheritDoc}
     */
	public IGridConstraints getConstraints(int pColumns, int pRows, int pWidth, int pHeight)
	{
		return new CellConstraints(pColumns, pRows, pWidth, pHeight);
	}

	/**
     * {@inheritDoc}
     */
	public IGridConstraints getConstraints(int pColumns, int pRows, int pWidth, int pHeight, IInsets insets)
	{
		return new CellConstraints(pColumns, pRows, pWidth, pHeight, (VaadinInsets)insets);
	}

	/**
     * {@inheritDoc}
     */
	public void setColumns(int pColumns)
	{
		columns = pColumns;
		
		markComponentsChanged();
	}

	/**
     * {@inheritDoc}
     */
	public int getColumns()
	{
		return columns;
	}

	/**
     * {@inheritDoc}
     */
	public void setRows(int pRows)
	{
		rows = pRows;
		
		markComponentsChanged();
	}

	/**
     * {@inheritDoc}
     */
	public int getRows()
	{
		return rows;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Throws a NullPointerException with the specified message if the given Object references null.
	 * 
	 * @param pReference the Object which shall be checked
	 * @param pMessage the specified message
	 */
	public static void checkNotNull(Object pReference, String pMessage)
	{
		if (pReference == null)
		{
			throw new NullPointerException(pMessage);
		}
	}
	
	/**
	 * Forces the layout to apply layout logic to all its child components.
	 */
	private void performLayout() 
	{		
		if (VaadinUtil.isParentWidthDefined(resource))
		{
			VaadinUtil.setComponentWidth(resource, 100f, Unit.PERCENTAGE);
		}
		else
		{
			VaadinUtil.setComponentWidth(resource, VaadinUtil.SIZE_UNDEFINED, Unit.PIXELS);
		}	
		
		if (VaadinUtil.isParentHeightDefined(resource))
		{
			VaadinUtil.setComponentHeight(resource, 100f, Unit.PERCENTAGE);
		}
		else
		{
			VaadinUtil.setComponentHeight(resource, VaadinUtil.SIZE_UNDEFINED, Unit.PIXELS);
		}
		
		Set<IComponent> components = constraintMap.keySet();
		
		for (IComponent component : components)
		{
			performLayout(component);
		}	
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * The CellConstraint stores the X and Y position,
	 * the Width and Height and the insets of the component.
	 * 
	 * @author Thomas Lehner
	 */
	public static class CellConstraints extends VaadinResourceBase  
	                                    implements Cloneable, 
	                                               IGridLayout.IGridConstraints
	{
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	    /** Empty insets. */
        private static final VaadinInsets   EMPTY_INSETS    = new VaadinInsets(0, 0, 0, 0);
        

        /** The position on the x-axis. */
		private int					gridX;

		/** The position on the y-axis. */
		private int					gridY;

		/** The width of the component. */
		private int					gridWidth;

		/** The height of the component. */
		private int					gridHeight;

		/** The specified insets of the component. */
		private VaadinInsets		insets;

		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of <code>CellConstraints</code>.
		 */
		public CellConstraints()
		{
			this(0, 0);
		}

        /**
         * Constructs a new CellConstraint with the given parameters.
         * 
         * @param pGridX the position on the x-axis
         * @param pGridY the position on the y-axis
         */
        public CellConstraints(int pGridX, int pGridY)
        {
            this(pGridX, pGridY, 1, 1, EMPTY_INSETS);
        }

        /**
		 * Creates a new instance of <code>CellConstraints</code> with given parameters.
		 * 
		 * @param pGridX the position on the x-axis
		 * @param pGridY the position on the y-axis
		 * @param pWidth the width of the component
		 * @param pHeight the height of the component
		 */
		public CellConstraints(int pGridX, int pGridY, int pWidth, int pHeight)
		{
			this(pGridX, pGridY, pWidth, pHeight, EMPTY_INSETS);
		}

		/**
		 * Constructs a new CellConstraint with the given parameters.
		 * 
		 * @param pGridX the position on the x-axis
		 * @param pGridY the position on the y-axis
		 * @param pWidth the width of the component
		 * @param pHeight the height of the component
		 * @param pInsets the specified insets
		 */
		public CellConstraints(int pGridX, int pGridY, int pWidth, int pHeight, VaadinInsets pInsets)
		{
			if (pGridX < 0)
			{
				throw new IndexOutOfBoundsException("The grid x must be a positive number.");
			}
			
			if (pGridY < 0)
			{
				throw new IndexOutOfBoundsException("The grid y must be a positive number.");
			}
			
			if (pWidth <= 0)
			{
				throw new IndexOutOfBoundsException("The grid width must be a positive number.");
			}
			
			if (pHeight <= 0)
			{
				throw new IndexOutOfBoundsException("The grid height must be a positive number.");
			}
			
            gridX = pGridX;
            gridY = pGridY;
            gridWidth = pWidth;
            gridHeight = pHeight;
            insets = pInsets;
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Interface implementation
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
		
		/**
		 * {@inheritDoc}
		 */
		public int getGridX()
		{
			return gridX;
		}

		/**
		 * {@inheritDoc}
		 */
		public int getGridY()
		{
			return gridY;
		}

		/**
		 * {@inheritDoc}
		 */
		public int getGridWidth()
		{
			return gridWidth;
		}

		/**
		 * {@inheritDoc}
		 */
		public int getGridHeight()
		{
			return gridHeight;
		}

		/**
		 * {@inheritDoc}
		 */
		public VaadinInsets getInsets()
		{
			return insets;
		}
		
		/**
		 * {@inheritDoc}
		 */
		public void setGridX(int pGridX)
		{
			gridX = pGridX;
		}

		/**
		 * {@inheritDoc}
		 */
		public void setGridY(int pGridY)
		{
			gridY = pGridY;
		}

		/**
		 * {@inheritDoc}
		 */
		public void setGridHeight(int pGridHeight)
		{
			gridHeight = pGridHeight;
		}

		/**
		 * {@inheritDoc}
		 */
		public void setGridWidth(int pGridWidth)
		{
			gridWidth = pGridWidth;
		}

		/**
		 * {@inheritDoc}
		 */
		public void setInsets(IInsets pInsets)
		{
			insets = (VaadinInsets)pInsets;
		}
		
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Overwritten methods
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~      
        
		/**
		 * {@inheritDoc}
		 */
        @Override
        public Object clone()
        {
            try
            {
                CellConstraints c = (CellConstraints)super.clone();
                c.insets = (VaadinInsets)insets.clone();
                
                return c;
            }
            catch (CloneNotSupportedException e)
            {
                // This shouldn't happen, since we are Cloneable.
                throw new InternalError();
            }
        }
		
	} 	//CellConstraints
	
}	//VaadinGridLayout
