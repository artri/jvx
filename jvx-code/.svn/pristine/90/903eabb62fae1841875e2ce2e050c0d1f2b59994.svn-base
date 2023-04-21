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
 * 16.01.2013 - [HM] - creation
 */
package com.sibvisions.rad.ui.vaadin.impl.layout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import javax.rad.ui.IAlignmentConstants;
import javax.rad.ui.IComponent;
import javax.rad.ui.IInsets;
import javax.rad.ui.layout.IFormLayout;

import com.sibvisions.rad.ui.vaadin.ext.VaadinUtil;
import com.sibvisions.rad.ui.vaadin.ext.ui.extension.CssExtension;
import com.sibvisions.rad.ui.vaadin.impl.VaadinComponentBase;
import com.sibvisions.rad.ui.vaadin.impl.VaadinInsets;
import com.sibvisions.rad.ui.vaadin.impl.VaadinResourceBase;
import com.sibvisions.util.log.LoggerFactory;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.GridLayout.OverlapsException;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.SingleComponentContainer;

/**
 * The <code>VaadinFormLayout</code> class is the vaadin implementation of
 * {@link IFormLayout}.
 * 
 * @author Stefan Wurm
 */
public class VaadinFormLayout extends AbstractVaadinLayout<GridLayout, IFormLayout.IConstraints>
		                      implements IFormLayout
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** the horizontal alignment. */
	private int horizontalAlignment = IAlignmentConstants.ALIGN_LEFT;
	
	/** the vertical alignment. */
	private int verticalAlignment = IAlignmentConstants.ALIGN_TOP;
	
	/** the new line count. */
	private int newlineCount = 2;
	
	/** The left border anchor. */
	private VaadinAnchor leftAnchor = new VaadinAnchor(this, IAnchor.HORIZONTAL, 0);
	
	/** The left border anchor. */
	private VaadinAnchor rightAnchor = new VaadinAnchor(this, IAnchor.HORIZONTAL, -1);
	
	/** The left border anchor. */
	private VaadinAnchor topAnchor = new VaadinAnchor(this, IAnchor.VERTICAL, 0);
	
	/** The left border anchor. */
	private VaadinAnchor bottomAnchor = new VaadinAnchor(this, IAnchor.VERTICAL, -1);
	
	/** The left margin border anchor. */
	private VaadinAnchor leftMarginAnchor = new VaadinAnchor(leftAnchor, 10);
	
	/** The left margin border anchor. */
	private VaadinAnchor rightMarginAnchor = new VaadinAnchor(rightAnchor, -10);
	
	/** The left margin border anchor. */
	private VaadinAnchor topMarginAnchor = new VaadinAnchor(topAnchor, 10);
	
	/** The left margin border anchor. */
	private VaadinAnchor bottomMarginAnchor = new VaadinAnchor(bottomAnchor, -10);
	
	/** All left default anchors. */
	private List<VaadinAnchor> leftDefaultAnchors = new ArrayList<VaadinAnchor>();
	
	/** All top default anchors. */
	private List<VaadinAnchor> topDefaultAnchors = new ArrayList<VaadinAnchor>();
	
	/** All right default anchors. */
	private List<VaadinAnchor> rightDefaultAnchors = new ArrayList<VaadinAnchor>();
	
	/** All bottom default anchors. */
	private List<VaadinAnchor> bottomDefaultAnchors = new ArrayList<VaadinAnchor>();
	
	/** Horizontal Gap. **/
	private int horizontalGap = 5;
	
	/** Vertical Gap. **/
	private int verticalGap = 5;
	
	/** Margin. **/
	private IInsets margins = new VaadinInsets(10, 10, 10, 10);
	
	/** The nextColumn if no constraint is set. **/
	private int nextColumn = 1;
	
	/** The gridLayout holds the components. **/
	private HorizontalLayout surroundingLayout = new HorizontalLayout();
	
	/** All Components in this FormLayout. **/
	private List<IComponent> components = new ArrayList<IComponent>();
	
	/** if the layout has a expandable column. **/
	boolean expandableColumn;
	
	/** if the layout has a expandable column component. **/
	boolean expandableColumnComponent;
	
	/** the maximal negative column from the layout. **/
	int maxNegativeColumn;
	
	/** the maximal positive column from the layout. **/
	int maxPositiveColumn;
	
	/** if the layout has a expandable row. **/
	boolean expandableRow;
	
	/** if the layout has a expandable row component. **/
	boolean expandableRowComponent;
	
	/** the maximal negative row from the layout. **/
	int maxNegativeRow;
	
	/** the maximal positive row from the layout. **/
	int maxPositiveRow;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Initialization.
	 */
	public VaadinFormLayout()
	{
		super(new GridLayout());
		
		surroundingLayout.setMargin(false);
		surroundingLayout.setSpacing(false);
		
		setMargins(margins);
		
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
			
			resource.setColumns(getColumnCount());
			resource.setRows(getRowCount());
			
			expandableColumn = false;
			expandableColumnComponent = false;
			maxNegativeColumn = -2;
			maxPositiveColumn = 1;
			expandableRow = false;
			expandableRowComponent = false;
			maxNegativeRow = -2;
			maxPositiveRow = 1;
			
			setupHorizontalMarginsAndGaps();
			setupVerticalMarginsAndGaps();
			
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
				
				VaadinConstraints constraints = (VaadinConstraints)componentBase.getConstraints();
				
				boolean setMaxWidthAllowed = setComponentWidth(
						(Component)component.getResource(),
						constraints.getLeftAnchor(),
						constraints.getRightAnchor());
						
				boolean setMaxHeightAllowed = setComponentHeight(
						(Component)component.getResource(),
						constraints.getTopAnchor(),
						constraints.getBottomAnchor());
				
				int startColumn = constraints.getColumn();
				int endColumn = constraints.getEndColumn();
				int startRow = constraints.getRow();
				int endRow = constraints.getEndRow();
				
				if (startColumn > 0 && endColumn < 0)
				{
					expandableColumn = true;
					expandableColumnComponent = true;
					horizontalAlignment = ALIGN_STRETCH;
				}
				
				if (startRow > 0 && endRow < 0)
				{
					expandableRow = true;
					expandableRowComponent = true;
					verticalAlignment = ALIGN_STRETCH;
				}
				
				applySize(componentBase, "max", componentBase.getMaximumSize(),
						componentBase.isMaximumSizeSet() && !expandableColumnComponent && setMaxWidthAllowed,
						componentBase.isMaximumSizeSet() && !expandableRowComponent && setMaxHeightAllowed);
				applySize(componentBase, "min", componentBase.getMinimumSize(),
						componentBase.isMinimumSizeSet(),
						componentBase.isMinimumSizeSet());
						
				if (endColumn < maxNegativeColumn)
				{
					maxNegativeColumn = endColumn;
				}
				
				if (startColumn > maxPositiveColumn)
				{
					maxPositiveColumn = startColumn;
				}
				
				if (endRow < maxNegativeRow)
				{
					maxNegativeRow = endRow;
				}
				
				if (startRow > maxPositiveRow)
				{
					maxPositiveRow = startRow;
				}
				
				if (endColumn < 0)
				{
					expandableColumn = true;
					
					endColumn = resource.getColumns() + endColumn;
				}
				
				if (endRow < 0)
				{
					expandableRow = true;
					
					endRow = resource.getRows() + endRow;
				}
				
				if (startColumn < 0)
				{
					startColumn = resource.getColumns() + startColumn;
				}
				
				if (startRow < 0)
				{
					startRow = resource.getRows() + startRow;
				}
				
				try
				{
					if (VaadinUtil.isParentNull(component))
					{
						resource.addComponent((Component)component.getResource(), startColumn, startRow, endColumn, endRow);
					}
				}
				catch (OverlapsException e) // A Component at this position is already added
				{
					Component comp = resource.getComponent(startColumn, startRow);
					
					if (component.isVisible() && comp != null && comp.isVisible()) // Only show message when both components are visible.
					{
						LoggerFactory.getInstance(getClass()).debug("A Component ", (comp != null ? comp.getClass() : "<undefined>"),
								" is already added at position (column, row, endColumn, endRow) ",
								Integer.valueOf(constraints.getColumn()), ", ",
								Integer.valueOf(constraints.getRow()), ", ",
								Integer.valueOf(constraints.getEndColumn()), ", ",
								Integer.valueOf(constraints.getEndRow()));
					}
					
					if (comp != null && !comp.isVisible() && component.isVisible()) // If both components are visible the first component is set in the gridlayout.
					{
						resource.replaceComponent(comp, (Component)component.getResource());
					}
				}
				catch (Exception e)
				{
					LoggerFactory.getInstance(getClass()).error("Exception while adding a component in the layout.", e);
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
	public IConstraints getConstraints(IComponent pComponent)
	{
		if (((VaadinComponentBase<?, ?>)pComponent).getConstraints() == null)
		{
			return getConstraints(0, 0, 0, 0);
		}
		else
		{
			return (IConstraints)((VaadinComponentBase<?, ?>)pComponent).getConstraints();
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setConstraints(IComponent pComponent, IConstraints pConstraints)
	{
		((VaadinComponentBase<?, ?>)pComponent).setConstraints(pConstraints);
		
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

        if (pMargins == null)
		{
			topMarginAnchor.position = 0;
			leftMarginAnchor.position = 0;
			bottomMarginAnchor.position = 0;
			rightMarginAnchor.position = 0;
		}
		else
		{
			topMarginAnchor.position = pMargins.getTop();
			leftMarginAnchor.position = pMargins.getLeft();
			bottomMarginAnchor.position = -pMargins.getBottom();
			rightMarginAnchor.position = -pMargins.getRight();
			
		}
		
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
		
		markDirty();
	}
	
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
		horizontalAlignment = pHorizontalAlignment;
		
		markDirty();
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
		verticalAlignment = pVerticalAlignment;
		
		markDirty();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getNewlineCount()
	{
		return newlineCount;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setNewlineCount(int pNewlineCount)
	{
		newlineCount = pNewlineCount;
		
		markDirty();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public IAnchor createAnchor(IAnchor pRelatedAnchor)
	{
		return new VaadinAnchor((VaadinAnchor)pRelatedAnchor);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public IAnchor createAnchor(IAnchor pRelatedAnchor, int pPosition)
	{
		return new VaadinAnchor((VaadinAnchor)pRelatedAnchor, pPosition);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public IAnchor getLeftAnchor()
	{
		return leftAnchor;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public IAnchor getRightAnchor()
	{
		return rightAnchor;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public IAnchor getTopAnchor()
	{
		return topAnchor;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public IAnchor getBottomAnchor()
	{
		return bottomAnchor;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public IAnchor getLeftMarginAnchor()
	{
		return leftMarginAnchor;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public IAnchor getRightMarginAnchor()
	{
		return rightMarginAnchor;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public IAnchor getTopMarginAnchor()
	{
		return topMarginAnchor;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public IAnchor getBottomMarginAnchor()
	{
		return bottomMarginAnchor;
	}
	
    /**
     * Fills in the given anchor and all related anchors.
     * 
     * @param pAnchors the list of anchors
     * @param pAnchor the anchor
     */
    private void fillInAnchors(LinkedHashSet<IAnchor> pAnchors, IAnchor pAnchor)
    {
        while (pAnchor != null && pAnchors.add(pAnchor))
        {
            pAnchor = pAnchor.getRelatedAnchor();
        }
    }
    
	/**
	 * {@inheritDoc}
	 */
	public IAnchor[] getHorizontalAnchors()
	{
        LinkedHashSet<IAnchor> horizontalAnchors = new LinkedHashSet<IAnchor>();
        
        for (IComponent comp : components)
        {
            VaadinConstraints constraint = (VaadinConstraints)((VaadinComponentBase<?, ?>)comp).getConstraints();
            fillInAnchors(horizontalAnchors, constraint.getLeftAnchor());
            fillInAnchors(horizontalAnchors, constraint.getRightAnchor());
        }
        
        return horizontalAnchors.toArray(new IAnchor[horizontalAnchors.size()]);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public IAnchor[] getVerticalAnchors()
	{
        LinkedHashSet<IAnchor> verticalAnchors = new LinkedHashSet<IAnchor>();
        
        for (IComponent comp : components)
        {
            VaadinConstraints constraint = (VaadinConstraints)((VaadinComponentBase<?, ?>)comp).getConstraints();
            fillInAnchors(verticalAnchors, constraint.getTopAnchor());
            fillInAnchors(verticalAnchors, constraint.getBottomAnchor());
        }
        
        return verticalAnchors.toArray(new IAnchor[verticalAnchors.size()]);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public IConstraints getConstraints(int pColumn, int pRow)
	{
		return getConstraints(pColumn, pRow, pColumn, pRow);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public IConstraints getConstraints(int pBeginColumn, int pBeginRow, int pEndColumn, int pEndRow)
	{
		VaadinAnchor[] left = createDefaultAnchors(leftDefaultAnchors, rightDefaultAnchors, leftMarginAnchor, rightMarginAnchor, pBeginColumn, getHorizontalGap());
		VaadinAnchor[] right;
		
		if (pBeginColumn == pEndColumn)
		{
			right = left;
		}
		else
		{
			right = createDefaultAnchors(leftDefaultAnchors, rightDefaultAnchors, leftMarginAnchor, rightMarginAnchor, pEndColumn, getHorizontalGap());
		}
		
		VaadinAnchor[] top = createDefaultAnchors(topDefaultAnchors, bottomDefaultAnchors, topMarginAnchor, bottomMarginAnchor, pBeginRow, getVerticalGap());
		VaadinAnchor[] bottom;
		
		if (pBeginRow == pEndRow)
		{
			bottom = top;
		}
		else
		{
			bottom = createDefaultAnchors(topDefaultAnchors, bottomDefaultAnchors, topMarginAnchor, bottomMarginAnchor, pEndRow, getVerticalGap());
		}
		
		VaadinConstraints vaadinConstraint = new VaadinConstraints(top[0], left[0], bottom[1], right[1]);
		
		return vaadinConstraint;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public IConstraints getConstraints(IAnchor pTopAnchor, IAnchor pLeftAnchor, IAnchor pBottomAnchor, IAnchor pRightAnchor)
	{
		return new VaadinConstraints((VaadinAnchor)pTopAnchor, (VaadinAnchor)pLeftAnchor, (VaadinAnchor)pBottomAnchor, (VaadinAnchor)pRightAnchor);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void addComponent(IComponent pComponent, Object pConstraints, int pIndex)
	{
		int startColumn;
		int startRow;
		int endColumn;
		int endRow;
		
		if (pConstraints == null)
		{
			int rowCount = getRowCount();
			
			startRow = rowCount - 2;
			endRow = rowCount - 2;
			
			startColumn = nextColumn;
			endColumn = nextColumn;
			
			nextColumn += 2;
			
			if (startColumn == ((newlineCount * 2) + 1))
			{
				startColumn = 1;
				startRow = rowCount;
				endColumn = 1;
				endRow = rowCount;
				
				nextColumn = 3;
			}
			
			pConstraints = getConstraints(startColumn, startRow, endColumn, endRow);
		}
		else if (pConstraints == IFormLayout.NEWLINE)
		{
			int rowCount = getRowCount();
			
			startColumn = 1;
			startRow = rowCount;
			endColumn = 1;
			endRow = rowCount;
			
			nextColumn = 3;
			
			pConstraints = getConstraints(startColumn, startRow, endColumn, endRow);
		}
		else
		{
			startRow = ((VaadinConstraints)pConstraints).getRow();
			startColumn = ((VaadinConstraints)pConstraints).getColumn();
			endRow = ((VaadinConstraints)pConstraints).getEndRow();
			endColumn = ((VaadinConstraints)pConstraints).getEndColumn();
		}
		
		((VaadinConstraints)pConstraints).setRow(startRow);
		((VaadinConstraints)pConstraints).setColumn(startColumn);
		((VaadinConstraints)pConstraints).setEndRow(endRow);
		((VaadinConstraints)pConstraints).setEndColumn(endColumn);
		
		((VaadinComponentBase<?, ?>)pComponent).setConstraints(pConstraints);
		
		components.add(pComponent);
		
		markComponentsChanged();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void removeComponent(IComponent pComponent)
	{
		components.remove(pComponent);
		
		if (components.isEmpty())
		{
			nextColumn = 1;
		}
		
		removeFromVaadin(pComponent, resource);
		
		markComponentsChanged();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates the default anchors.
	 * 
	 * @param pLeftTopDefaultAnchors the vector to store the anchors.
	 * @param pRightBottomDefaultAnchors the vector to store the anchors.
	 * @param pLeftTopAnchor the left or top margin anchor.
	 * @param pRightBottomAnchor the right or bottom margin anchor.
	 * @param pColumnOrRow the column or the row.
	 * @param pGap the horizontal or vertical gap.
	 * @return the leftTop and rightBottom Anchors.
	 */
	private VaadinAnchor[] createDefaultAnchors(List<VaadinAnchor> pLeftTopDefaultAnchors,
			List<VaadinAnchor> pRightBottomDefaultAnchors,
			VaadinAnchor pLeftTopAnchor,
			VaadinAnchor pRightBottomAnchor,
			int pColumnOrRow,
			int pGap)
	{
		List<VaadinAnchor> defaultAnchors;
		
		VaadinAnchor anchor;
		
		int gap;
		boolean rightBottom = pColumnOrRow < 0;
		
		if (rightBottom)
		{
			pColumnOrRow = (-pColumnOrRow - 1) * 2;
			defaultAnchors = pRightBottomDefaultAnchors;
			anchor = pRightBottomAnchor;
			gap = -pGap;
		}
		else
		{
			pColumnOrRow *= 2;
			defaultAnchors = pLeftTopDefaultAnchors;
			anchor = pLeftTopAnchor;
			gap = pGap;
		}
		
		int size = defaultAnchors.size();
		
		while (pColumnOrRow >= size)
		{
			if (size == 0)
			{
				defaultAnchors.add(anchor);
			}
			else
			{
				defaultAnchors.add(new VaadinAnchor(defaultAnchors.get(size - 1), gap));
			}
			defaultAnchors.add(new VaadinAnchor(defaultAnchors.get(size)));
			size = defaultAnchors.size();
		}
		
		if (rightBottom)
		{
			return new VaadinAnchor[] { defaultAnchors.get(pColumnOrRow + 1), defaultAnchors.get(pColumnOrRow) };
		}
		else
		{
			return new VaadinAnchor[] { defaultAnchors.get(pColumnOrRow), defaultAnchors.get(pColumnOrRow + 1) };
		}
	}	
	
	/**
	 * Returns true if the given component is added in the gridLayout.
	 * 
	 * @param component the component.
	 * @return true if the given component is added in the gridLayout.
	 */
	public boolean isComponentAdded(IComponent component)
	{
		if (resource != null)
		{
			Iterator<Component> iterator = resource.iterator();
			
			while (iterator.hasNext())
			{
				if (iterator.next().equals((Component)component.getResource()))
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Forces the layout to apply layout logic to all its child components.
	 */
	private void performLayout()
	{
		removeExpandRatio();
		
		if (horizontalAlignment != ALIGN_STRETCH
				&& verticalAlignment != ALIGN_STRETCH
				&& horizontalAlignment != ALIGN_LEFT
				&& verticalAlignment != ALIGN_TOP)
		{
			// SurroundingLayout is needed to align gridLayout.
			if (surroundingLayout.getComponentCount() <= 0
					&& resource.getParent() != null
					&& resource.getParent() instanceof SingleComponentContainer)
			{
				SingleComponentContainer parent = (SingleComponentContainer)resource.getParent();
				
				parent.setContent(surroundingLayout);
				
				surroundingLayout.addComponent(resource);
				
				surroundingLayout.setComponentAlignment(resource, VaadinUtil.getVaadinAlignment(horizontalAlignment, verticalAlignment));
			}
			
			if (VaadinUtil.isParentHeightDefined(surroundingLayout))
			{
				VaadinUtil.setComponentHeight(surroundingLayout, 100f, Unit.PERCENTAGE);
			}
			else
			{
				VaadinUtil.setComponentHeight(surroundingLayout, VaadinUtil.SIZE_UNDEFINED, Unit.PIXELS);
			}
			
			if (VaadinUtil.isParentWidthDefined(surroundingLayout))
			{
				VaadinUtil.setComponentWidth(surroundingLayout, 100f, Unit.PERCENTAGE);
			}
			else
			{
				VaadinUtil.setComponentWidth(surroundingLayout, VaadinUtil.SIZE_UNDEFINED, Unit.PIXELS);
			}
		}
		else
		{
			if (surroundingLayout.getComponentCount() >= 0
					&& surroundingLayout.getParent() != null
					&& surroundingLayout.getParent() instanceof SingleComponentContainer)
			{
				SingleComponentContainer parent = (SingleComponentContainer)surroundingLayout.getParent();
				
				parent.setContent(resource);
			}
		}
		
		if (expandableColumn)
		{
			if (VaadinUtil.isParentWidthDefined(resource))
			{
				VaadinUtil.setComponentWidth(resource, 100f, Unit.PERCENTAGE);
			}
			else
			{
				VaadinUtil.setComponentWidth(resource, VaadinUtil.SIZE_UNDEFINED, Unit.PIXELS);
			}
			
			int expandColumnIndex;
			
			if (expandableColumnComponent)
			{
				expandColumnIndex = ((maxPositiveColumn + (resource.getColumns() + maxNegativeColumn)) / 2);
			}
			else
			{
				expandColumnIndex = resource.getColumns() + maxNegativeColumn - 1;
				
				Component component = resource.getComponent(expandColumnIndex, 0);
				
				if (component != null)
				{
					component.setWidth(100f, Unit.PERCENTAGE);
				}
				
				if (resource.getColumns() > 4) // No Gap between max positive added component and max negative added component. See ticket #1204
				{
					Component marginOrGapLabel = addMarginOrGapLabel(expandColumnIndex, 0, getHorizontalGap(), Math.abs(topMarginAnchor.getPosition()));
					
					if (marginOrGapLabel != null && marginOrGapLabel instanceof Label)
					{
						CssExtension css = new CssExtension();
						
						css.extend((Label)marginOrGapLabel);
						
						css.addAttribute("min-width", getHorizontalGap() + "px", false);
					}
				}
			}
			
			resource.setColumnExpandRatio(expandColumnIndex, 1f);
		}
		else
		{
			if (VaadinUtil.isParentWidthDefined(resource) && horizontalAlignment == IAlignmentConstants.ALIGN_STRETCH)
			{
				if (maxNegativeColumn >= -2)
				{
					resource.setColumnExpandRatio(resource.getColumns() - 1, 1f);
				}
				else
				{
					resource.setColumnExpandRatio(0, 1f);
				}
				
				VaadinUtil.setComponentWidth(resource, 100f, Unit.PERCENTAGE);
			}
			else
			{
				VaadinUtil.setComponentWidth(resource, VaadinUtil.SIZE_UNDEFINED, Unit.PIXELS);
			}
		}
		
		if (expandableRow)
		{
			if (VaadinUtil.isParentHeightDefined(resource))
			{
				VaadinUtil.setComponentHeight(resource, 100f, Unit.PERCENTAGE);
			}
			else
			{
				VaadinUtil.setComponentHeight(resource, VaadinUtil.SIZE_UNDEFINED, Unit.PIXELS);
			}
			
			int expandRowIndex;
			
			if (expandableRowComponent)
			{
				expandRowIndex = ((maxPositiveRow + (resource.getRows() + maxNegativeRow)) / 2);
			}
			else
			{
				expandRowIndex = resource.getRows() + maxNegativeRow - 1;
				
				Component component = resource.getComponent(0, expandRowIndex);
				
				if (component != null)
				{
					component.setHeight(100f, Unit.PERCENTAGE);
				}
				
				if (resource.getRows() > 4) // No Gap between max positive added component and max negative added component. See ticket #1204 
				{
					Component marginOrGapLabel = addMarginOrGapLabel(0, expandRowIndex, Math.abs(leftMarginAnchor.getPosition()), getVerticalGap());
					
					if (marginOrGapLabel != null && marginOrGapLabel instanceof Label)
					{
						CssExtension css = new CssExtension();
						
						css.extend((Label)marginOrGapLabel);
						
						css.addAttribute("min-height", getVerticalGap() + "px", true);
					}
				}
			}
			
			resource.setRowExpandRatio(expandRowIndex, 1f);
			
		}
		else
		{
			if (VaadinUtil.isParentHeightDefined(resource) && verticalAlignment == IAlignmentConstants.ALIGN_STRETCH)
			{
				if (maxNegativeRow >= -2)
				{
					resource.setRowExpandRatio(resource.getRows() - 1, 1f);
				}
				else
				{
					resource.setRowExpandRatio(0, 1f);
				}
				VaadinUtil.setComponentHeight(resource, 100f, Unit.PERCENTAGE);
			}
			else
			{
				VaadinUtil.setComponentHeight(resource, VaadinUtil.SIZE_UNDEFINED, Unit.PIXELS);
			}
		}
		
		for (IComponent component : components)
		{
			setIgnorePerformLayout(component, true);
			
			try
			{
				VaadinComponentBase<?, ?> componentBase = (VaadinComponentBase<?, ?>)component;
				
				VaadinConstraints constraints = (VaadinConstraints)componentBase.getConstraints();
				
				int startColumn = constraints.getColumn();
				int endColumn = constraints.getEndColumn();
				int startRow = constraints.getRow();
				int endRow = constraints.getEndRow();
				
				if (VaadinUtil.isWidthUndefined((Component)component.getResource(), true) && VaadinUtil.isHeightUndefined((Component)component.getResource(), true))
				{
					componentBase.setSizeUndefined();
				}
				else if (VaadinUtil.isWidthUndefined((Component)component.getResource(), true))
				{
					componentBase.setWidthUndefined();
				}
				else if (VaadinUtil.isHeightUndefined((Component)component.getResource(), true))
				{
					componentBase.setHeightUndefined();
				}
				
				if (endColumn != startColumn)
				{
					componentBase.setWidthFull(); // Is needed to call setWidthFull also from editor.
					
					if (VaadinUtil.isWidthUndefined((Component)component.getResource(), false))
					{
						// Width can be set, also when parent (gridLayout) is undefined. GridLayout calculates width and height, when size undefined. 
						VaadinUtil.setComponentWidth((Component)component.getResource(), 100, Unit.PERCENTAGE);
					}
				}
				
				if (endRow != startRow)
				{
					componentBase.setHeightFull(); // Is needed to call setHeightFull also from editor.
					
					if (VaadinUtil.isHeightUndefined((Component)component.getResource(), false))
					{
						// Width can be set, also when parent (gridLayout) is undefined. GridLayout calculates width and height, when size undefined. 
						VaadinUtil.setComponentHeight((Component)component.getResource(), 100, Unit.PERCENTAGE);
					}
				}
			}
			finally
			{
				setIgnorePerformLayout(component, false);
			}
		}
	}
	
	/**
	 * Removes the expanded columns and rows from the gridLaoyut.
	 */
	private void removeExpandRatio()
	{
		for (int c = 0; c < resource.getColumns(); c++)
		{
			resource.setColumnExpandRatio(c, 0f);
		}
		
		for (int r = 0; r < resource.getRows(); r++)
		{
			resource.setRowExpandRatio(r, 0f);
		}
	}
	
	/**
	 * Returns the count of the columns from the gridlayout.
	 * 
	 * @return the column count.
	 */
	private int getColumnCount()
	{
		int maxPositiveColumnHelper = 0;
		int maxNegativeColumnHelper = 0;
		
		for (IComponent component : components)
		{
			VaadinConstraints constraints = (VaadinConstraints)((VaadinComponentBase<?, ?>)component).getConstraints();
			
			int startColumn = constraints.getColumn();
			int endColumn = constraints.getEndColumn();
			
			if (startColumn >= 0)
			{
				if (startColumn > maxPositiveColumnHelper)
				{
					maxPositiveColumnHelper = startColumn;
				}
			}
			else
			{
				if (startColumn < maxNegativeColumnHelper)
				{
					maxNegativeColumnHelper = startColumn;
				}
			}
			
			if (endColumn >= 0)
			{
				if (endColumn > maxPositiveColumnHelper)
				{
					maxPositiveColumnHelper = endColumn;
				}
			}
			else
			{
				if (endColumn < maxNegativeColumnHelper)
				{
					maxNegativeColumnHelper = endColumn;
				}
			}
		}
		
		return Math.max(3, (maxPositiveColumnHelper + Math.abs(maxNegativeColumnHelper) + 2));
	}
	
	/**
	 * Returns the count of the rows from the grid layout.
	 * 
	 * @return the row count.
	 */
	private int getRowCount()
	{
		int maxPositiveRowHelper = 0;
		int maxNegativeRowHelper = 0;
		
		for (IComponent component : components)
		{
			VaadinConstraints constraints = (VaadinConstraints)((VaadinComponentBase<?, ?>)component).getConstraints();
			
			int startRow = constraints.getRow();
			int endRow = constraints.getEndRow();
			
			if (startRow >= 0)
			{
				if (startRow > maxPositiveRowHelper)
				{
					maxPositiveRowHelper = startRow;
				}
			}
			else
			{
				if (startRow < maxNegativeRowHelper)
				{
					maxNegativeRowHelper = startRow;
				}
			}
			
			if (endRow >= 0)
			{
				if (endRow > maxPositiveRowHelper)
				{
					maxPositiveRowHelper = endRow;
				}
			}
			else
			{
				if (endRow < maxNegativeRowHelper)
				{
					maxNegativeRowHelper = endRow;
				}
			}
		}
		
		return Math.max(3, (maxPositiveRowHelper + Math.abs(maxNegativeRowHelper) + 2));
	}
	
	/**
	 * Adds a margin or gap label to the gridLayout.
	 * 
	 * @param row the row.
	 * @param column the column.
	 * @param height the height.
	 * @param width the width.
	 * @return the margin or gap component.
	 */
	private Component addMarginOrGapLabel(int column, int row, int width, int height)
	{
		Component marginOrGapLabel = resource.getComponent(column, row);
		
		if (marginOrGapLabel == null) // If it is not null height and width is already set
		{
			marginOrGapLabel = new Label();
			
			marginOrGapLabel.setHeight(height, Unit.PIXELS);
			marginOrGapLabel.setWidth(width, Unit.PIXELS);
			
			try
			{
				resource.addComponent(marginOrGapLabel, column, row);
			}
			catch (OverlapsException e) // A Component at this position is already added
			{
				marginOrGapLabel = resource.getComponent(column, row);
				
				LoggerFactory.getInstance(getClass()).debug(
						"A margin or gap component ",
						marginOrGapLabel.getClass(),
						" is already added at position (column, row) ",
						Integer.valueOf(column), ", ", Integer.valueOf(row));
			}
			catch (Throwable th)
			{
				LoggerFactory.getInstance(getClass()).debug("Error: Adding margin or gap component was not possible.", th);
			}
		}
		
		return marginOrGapLabel;
	}
	
	/**
	 * Sets the {@link Component} height depending on the given {@link IAnchor}s.
	 * @param pComponent the {@link Component}.
	 * @param pTopAnchor the {@link IAnchor top anchor}.
	 * @param pBottomAnchor the {@link IAnchor bottom anchor}.
	 * @return if setting the max height is allowed.
	 */
	private boolean setComponentHeight(Component pComponent, IAnchor pTopAnchor, IAnchor pBottomAnchor)
	{
		boolean setMaxHeightAllowed = true;
		
		if (pBottomAnchor.getRelatedAnchor() == pTopAnchor
				&& !pBottomAnchor.isAutoSize())
		{
			int height = Math.abs(pBottomAnchor.getPosition());
			
			VaadinUtil.setComponentHeight(pComponent, height, Unit.PIXELS);
			
			setMaxHeightAllowed = false;
		}
		
		if (pTopAnchor.getRelatedAnchor() == pBottomAnchor
				&& !pTopAnchor.isAutoSize())
		{
			int height = Math.abs(pTopAnchor.getPosition());
			
			VaadinUtil.setComponentHeight(pComponent, height, Unit.PIXELS);
			
			setMaxHeightAllowed = false;
		}
		
		return setMaxHeightAllowed;
	}

	/**
	 * Sets the {@link Component} width depending on the given {@link IAnchor}s.
	 * @param pComponent the {@link Component}.
	 * @param pLeftAnchor the {@link IAnchor left anchor}.
	 * @param pRightAnchor the {@link IAnchor right anchor}.
	 * @return if setting the max width is allowed.
	 */
	private boolean setComponentWidth(Component pComponent, IAnchor pLeftAnchor, IAnchor pRightAnchor)
	{
		boolean setMaxWidthAllowed = true;
		
		if (pRightAnchor.getRelatedAnchor() == pLeftAnchor && !pRightAnchor.isAutoSize())
		{
			int width = Math.abs(pRightAnchor.getPosition());
			
			VaadinUtil.setComponentWidth(pComponent, width, Unit.PIXELS);
			
			setMaxWidthAllowed = false;
		}
		
		if (pLeftAnchor.getRelatedAnchor() == pRightAnchor && !pLeftAnchor.isAutoSize())
		{
			int width = Math.abs(pLeftAnchor.getPosition());
			
			VaadinUtil.setComponentWidth(pComponent, width, Unit.PIXELS);
			
			setMaxWidthAllowed = false;
		}
		
		return setMaxWidthAllowed;
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * Sets up the horizontal margins and gaps.
	 */
	private void setupHorizontalMarginsAndGaps()
	{
		IAnchor[] horizontalAnchors = getHorizontalAnchors();
		
		for (IAnchor anchor : horizontalAnchors)
		{
			int column = ((VaadinAnchor)anchor).getLeftColumnOrTopRow() - 1;
			
			if (column < 0)
			{
				column = resource.getColumns() + column + 1;
			}
			
			int width = Math.abs(anchor.getPosition());
			
			if (width > 0) // only if size > 0 px.
			{
				addMarginOrGapLabel(column, 0, width, Math.abs(topMarginAnchor.getPosition()));
			}
		}
	}

	/**
	 * Sets up the vertical margins and gaps.
	 */
	private void setupVerticalMarginsAndGaps()
	{
		IAnchor[] verticalAnchors = getVerticalAnchors();
		
		for (IAnchor anchor : verticalAnchors)
		{
			int row = ((VaadinAnchor)anchor).getLeftColumnOrTopRow() - 1;
			
			if (row < 0)
			{
				row = resource.getRows() + row + 1;
			}
			
			int height = Math.abs(anchor.getPosition());
			
			if (height > 0)
			{
				addMarginOrGapLabel(0, row, Math.abs(leftMarginAnchor.getPosition()), height);
			}
		}
	}

	/**
	 * The <code>VaadinAnchor</code> gives the possible horizontal and vertical
	 * positions.
	 * 
	 * @author Stefan Wurm
	 */
	public static class VaadinAnchor extends VaadinResourceBase
			implements IAnchor
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/** The orientation of this anchor. */
		private int orientation;
		
		/** The layout. */
		private VaadinFormLayout layout;
		
		/** The related anchor to this anchor. */
		private VaadinAnchor relatedAnchor;
		
		/** true, if this anchor should be auto sized. */
		private boolean autoSize;
		
		/** The position of this anchor. */
		private int position;
		
		/** The number of the anchor. **/
		private int anchorNumber;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of <code>VaadinAnchor</code>.
		 * 
		 * @param pLayout the layout.
		 * @param pOrientation the orientation.
		 * @param pAnchorNumber the number of the anchor.
		 */
		protected VaadinAnchor(VaadinFormLayout pLayout, int pOrientation, int pAnchorNumber)
		{
			layout = pLayout;
			orientation = pOrientation;
			anchorNumber = pAnchorNumber;
			relatedAnchor = null;
			autoSize = false;
			position = 0;
		}
		
		/**
		 * Creates a new instance of <code>VaadinAnchor</code> relative to the
		 * given anchor.
		 * 
		 * @param pRelatedAnchor the related anchor for this anchor.
		 */
		public VaadinAnchor(VaadinAnchor pRelatedAnchor)
		{
			layout = pRelatedAnchor.layout;
			orientation = pRelatedAnchor.orientation;
			relatedAnchor = pRelatedAnchor;
			autoSize = true;
			position = 0;
			
			if (pRelatedAnchor.anchorNumber >= 0)
			{
				anchorNumber = pRelatedAnchor.anchorNumber + 1;
			}
			else
			{
				anchorNumber = pRelatedAnchor.anchorNumber - 1;
			}
		}
		
		/**
		 * Creates a new instance of <code>VaadinAnchor</code> relative to the
		 * given anchor with position in pixels.
		 * 
		 * @param pRelatedAnchor the related anchor for this anchor.
		 * @param pPosition the position relative to the related anchor.
		 */
		public VaadinAnchor(VaadinAnchor pRelatedAnchor, int pPosition)
		{
			layout = pRelatedAnchor.layout;
			orientation = pRelatedAnchor.orientation;
			relatedAnchor = pRelatedAnchor;
			autoSize = false;
			position = pPosition;
			
			if (pRelatedAnchor.anchorNumber >= 0)
			{
				anchorNumber = pRelatedAnchor.anchorNumber + 1;
			}
			else
			{
				anchorNumber = pRelatedAnchor.anchorNumber - 1;
			}
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Interface implementation
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~		
		
		/**
		 * {@inheritDoc}
		 */
		public IFormLayout getLayout()
		{
			return layout;
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
		public IAnchor getRelatedAnchor()
		{
			return relatedAnchor;
		}
		
		/**
		 * {@inheritDoc}
		 */
		public void setRelatedAnchor(IAnchor pAnchor)
		{
			relatedAnchor = (VaadinAnchor)pAnchor;
			
			if (relatedAnchor.anchorNumber >= 0)
			{
				anchorNumber = relatedAnchor.anchorNumber + 1;
			}
			else
			{
				anchorNumber = relatedAnchor.anchorNumber - 1;
			}
		}
		
		/**
		 * {@inheritDoc}
		 */
		public IAnchor getBorderAnchor()
		{
			VaadinAnchor borderAnchor = this;
			
			while (borderAnchor.relatedAnchor != null)
			{
				borderAnchor = borderAnchor.relatedAnchor;
			}
			
			return borderAnchor;
		}
		
		/**
		 * {@inheritDoc}
		 */
		public boolean isAutoSize()
		{
			return autoSize;
		}
		
		/**
		 * {@inheritDoc}
		 */
		public void setAutoSize(boolean pAutoSize)
		{
			autoSize = pAutoSize;
		}
		
		/**
		 * {@inheritDoc}
		 */
		public int getPosition()
		{
			return position;
		}
		
		/**
		 * {@inheritDoc}
		 */
		public void setPosition(int pPosition)
		{
			position = pPosition;
		}
		
		/**
		 * {@inheritDoc}
		 */
		public int getAbsolutePosition()
		{
			if (relatedAnchor == null)
			{
				return position;
			}
			else
			{
				return relatedAnchor.getAbsolutePosition() + position;
			}
		}
		
		/**
		 * Returns the start column or the start row.
		 * 
		 * @return the start column or the start row.
		 */
		public int getLeftColumnOrTopRow()
		{
			if (anchorNumber >= 0)
			{
				return Math.max(0, anchorNumber);
			}
			else
			{
				return Math.min(-1, anchorNumber + 1);
			}
		}
		
		/**
		 * Returns the end column or the end row.
		 * 
		 * @return the end column or the end row.
		 */
		public int getRightColumnOrBottomRow()
		{
			if (anchorNumber >= 0)
			{
				return Math.max(0, (anchorNumber - 1));
			}
			else
			{
				return anchorNumber;
			}
		}
		
		@Override
		public int hashCode()
		{
			final int prime = 31;
			
			int result = super.hashCode();
			
			result = prime * result + anchorNumber;
			result = prime * result + orientation;
			
			return result;
		}
		
		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
			{
				return true;
			}
			if (!super.equals(obj))
			{
				return false;
			}
			if (getClass() != obj.getClass())
			{
				return false;
			}
			VaadinAnchor other = (VaadinAnchor)obj;
			if (anchorNumber != other.anchorNumber)
			{
				return false;
			}
			if (orientation != other.orientation)
			{
				return false;
			}
			
			return true;
		}
		
	}	//VaadinAnchor
	
	/**
	 * The <code>VaadinConstraints</code> stores the top, left, bottom and right
	 * Anchor for layouting a component.
	 * 
	 * @author Stefan Wurm
	 */
	public static class VaadinConstraints extends VaadinResourceBase
			implements IFormLayout.IConstraints
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/** The top anchor. */
		private VaadinAnchor topAnchor;
		
		/** The left anchor. */
		private VaadinAnchor leftAnchor;
		
		/** The bottom anchor. */
		private VaadinAnchor bottomAnchor;
		
		/** The right anchor. */
		private VaadinAnchor rightAnchor;
		
		/** Start row. **/
		private int row;
		
		/** Start column. **/
		private int column;
		
		/** End column. **/
		private int endColumn;
		
		/** End row. **/
		private int endRow;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of <code>VaadinConstraints</code> with default
		 * constraints for the given anchors.
		 * 
		 * @param pTopAnchor the top anchor.
		 * @param pLeftAnchor the left anchor.
		 * @param pBottomAnchor the bottom anchor.
		 * @param pRightAnchor the right anchor.
		 */
		public VaadinConstraints(VaadinAnchor pTopAnchor, VaadinAnchor pLeftAnchor, VaadinAnchor pBottomAnchor, VaadinAnchor pRightAnchor)
		{
			if (pLeftAnchor == null && pRightAnchor != null)
			{
				pLeftAnchor = new VaadinAnchor(pRightAnchor);
			}
			else if (pRightAnchor == null && pLeftAnchor != null)
			{
				pRightAnchor = new VaadinAnchor(pLeftAnchor);
			}
			if (pTopAnchor == null && pBottomAnchor != null)
			{
				pTopAnchor = new VaadinAnchor(pBottomAnchor);
			}
			else if (pBottomAnchor == null && pTopAnchor != null)
			{
				pBottomAnchor = new VaadinAnchor(pTopAnchor);
			}
			setLeftAnchor(pLeftAnchor);
			setRightAnchor(pRightAnchor);
			setTopAnchor(pTopAnchor);
			setBottomAnchor(pBottomAnchor);
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Interface implementation
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * {@inheritDoc}
		 */
		public IAnchor getLeftAnchor()
		{
			return leftAnchor;
		}
		
		/**
		 * {@inheritDoc}
		 */
		public void setLeftAnchor(IAnchor pLeftAnchor)
		{
			if (pLeftAnchor == null && rightAnchor != null)
			{
				leftAnchor = new VaadinAnchor(rightAnchor);
			}
			else if (pLeftAnchor.getOrientation() == IAnchor.VERTICAL)
			{
				throw new IllegalArgumentException("A vertical anchor can not be used as left anchor!");
			}
			else
			{
				leftAnchor = (VaadinAnchor)pLeftAnchor;
			}
			
			column = leftAnchor.getLeftColumnOrTopRow();
			
			((VaadinFormLayout)leftAnchor.getLayout()).markDirty();
		}
		
		/**
		 * {@inheritDoc}
		 */
		public IAnchor getRightAnchor()
		{
			return rightAnchor;
		}
		
		/**
		 * {@inheritDoc}
		 */
		public void setRightAnchor(IAnchor pRightAnchor)
		{
			if (pRightAnchor == null && leftAnchor != null)
			{
				rightAnchor = new VaadinAnchor(leftAnchor);
			}
			else if (pRightAnchor.getOrientation() == IAnchor.VERTICAL)
			{
				throw new IllegalArgumentException("A vertical anchor can not be used as right anchor!");
			}
			else
			{
				rightAnchor = (VaadinAnchor)pRightAnchor;
			}
			
			endColumn = rightAnchor.getRightColumnOrBottomRow();
			
			((VaadinFormLayout)rightAnchor.getLayout()).markDirty();
		}
		
		/**
		 * {@inheritDoc}
		 */
		public IAnchor getTopAnchor()
		{
			return topAnchor;
		}
		
		/**
		 * {@inheritDoc}
		 */
		public void setTopAnchor(IAnchor pTopAnchor)
		{
			if (pTopAnchor == null && bottomAnchor != null)
			{
				topAnchor = new VaadinAnchor(bottomAnchor);
			}
			else if (pTopAnchor.getOrientation() == IAnchor.HORIZONTAL)
			{
				throw new IllegalArgumentException("A horizontal anchor can not be used as top anchor!");
			}
			else
			{
				topAnchor = (VaadinAnchor)pTopAnchor;
			}
			
			row = topAnchor.getLeftColumnOrTopRow();
			
			((VaadinFormLayout)topAnchor.getLayout()).markDirty();
		}
		
		/**
		 * {@inheritDoc}
		 */
		public IAnchor getBottomAnchor()
		{
			return bottomAnchor;
		}
		
		/**
		 * {@inheritDoc}
		 */
		public void setBottomAnchor(IAnchor pBottomAnchor)
		{
			if (pBottomAnchor == null && topAnchor != null)
			{
				bottomAnchor = new VaadinAnchor(topAnchor);
			}
			else if (pBottomAnchor.getOrientation() == IAnchor.HORIZONTAL)
			{
				throw new IllegalArgumentException("A vertical anchor can not be used as bottom anchor!");
			}
			else
			{
				bottomAnchor = (VaadinAnchor)pBottomAnchor;
			}
			
			endRow = bottomAnchor.getRightColumnOrBottomRow();
			
			((VaadinFormLayout)bottomAnchor.getLayout()).markDirty();
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// User defined methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Returns the start column.
		 * 
		 * @return the start column.
		 */
		public int getColumn()
		{
			// Because of the getHCenterConstraints
			if (column > 0 && column % 2 == 0)
			{
				return column - 1;
			}
			else if (column < -1 && column % 2 == -1)
			{
				return column + 1;
			}
			
			return column;
		}
		
		/**
		 * Returns the start row.
		 * 
		 * @return the start row.
		 */
		public int getRow()
		{
			// Because of the getVCenterConstraints
			if (row > 0 && row % 2 == 0)  // row == endRow
			{
				return row - 1;
			}
			else if (row < -1 && row % 2 == -1)
			{
				return row + 1;
			}
			
			return row;
		}
		
		/**
		 * Returns the end column.
		 * 
		 * @return the end column.
		 */
		public int getEndColumn()
		{
			if (endColumn > 0 && endColumn % 2 == 0)
			{
				return endColumn - 1;
			}
			else if (endColumn < -1 && endColumn % 2 == -1)
			{
				return endColumn + 1;
			}
			
			return endColumn;
		}
		
		/**
		 * Returns the end row.
		 * 
		 * @return the end row.
		 */
		public int getEndRow()
		{
			// Because of the getVCenterConstraints
			if (endRow > 0 && endRow % 2 == 0)  // row == endRow
			{
				return endRow - 1;
			}
			else if (endRow < -1 && endRow % 2 == -1)
			{
				return endRow + 1;
			}
			
			return endRow;
		}
		
		/**
		 * Sets the row.
		 * 
		 * @param pRow the row.
		 */
		public void setRow(int pRow)
		{
			row = pRow;
		}
		
		/**
		 * Sets the column.
		 * 
		 * @param pColumn the column.
		 */
		public void setColumn(int pColumn)
		{
			column = pColumn;
		}
		
		/**
		 * Sets end column.
		 * 
		 * @param pEndColumn the end column.
		 */
		public void setEndColumn(int pEndColumn)
		{
			endColumn = pEndColumn;
		}
		
		/**
		 * Sets end row.
		 * 
		 * @param pEndRow the end row.
		 */
		public void setEndRow(int pEndRow)
		{
			endRow = pEndRow;
		}
		
	}	// VaadinConstraints
	
}	//VaadinFormLayout
