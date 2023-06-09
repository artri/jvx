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
 * 08.06.2020 - [JR] - #2292: invalidate component
 */
package com.sibvisions.rad.ui.swing.ext.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.swing.JComponent;

import com.sibvisions.rad.ui.swing.ext.JVxConstants;
import com.sibvisions.rad.ui.swing.ext.JVxUtil;

/**
 * The FormLayout is a simple to use Layout which allows complex forms.
 * 
 * @author Martin Handsteiner
 */
public class JVxFormLayout implements LayoutManager2, JVxConstants
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** Constraint for starting a new row for the given component. */
	public static final String NEWLINE = "\n";
	
    /** Gives the anchors a unique number. */
    private int anchorCounter = 0;

    /** The left border anchor. */
	private Anchor leftAnchor = new Anchor(this, Anchor.HORIZONTAL, "l");
	/** The left border anchor. */
	private Anchor rightAnchor = new Anchor(this, Anchor.HORIZONTAL, "r");
	/** The left border anchor. */
	private Anchor topAnchor = new Anchor(this, Anchor.VERTICAL, "t");
	/** The left border anchor. */
	private Anchor bottomAnchor = new Anchor(this, Anchor.VERTICAL, "b");
	
	/** The left margin border anchor. */
	private Anchor leftMarginAnchor = new Anchor(leftAnchor, 10, "lm");
	/** The left margin border anchor. */
	private Anchor rightMarginAnchor = new Anchor(rightAnchor, -10, "rm");
	/** The left margin border anchor. */
	private Anchor topMarginAnchor = new Anchor(topAnchor, 10, "tm");
	/** The left margin border anchor. */
	private Anchor bottomMarginAnchor = new Anchor(bottomAnchor, -10, "bm");
	
	/** All horizontal anchors. */
	private List<Anchor> horizontalAnchors = new ArrayList<Anchor>();
	/** All vertical anchors. */
	private List<Anchor> verticalAnchors = new ArrayList<Anchor>();
	/** All vertical anchors. */
	private List<Anchor> anchorsBuffer = new ArrayList<Anchor>();
	
	/** All left default anchors. */
	private List<Anchor> leftDefaultAnchors = new ArrayList<Anchor>();
	/** All top default anchors. */
	private List<Anchor> topDefaultAnchors = new ArrayList<Anchor>();
	/** All left default anchors. */
	private List<Anchor> rightDefaultAnchors = new ArrayList<Anchor>();
	/** All top default anchors. */
	private List<Anchor> bottomDefaultAnchors = new ArrayList<Anchor>();
	
	/** Stores all constraints. */
	private Hashtable<Component, Constraint> constraints = new Hashtable<Component, Constraint>();
	
	/** the x-axis alignment (default: {@link JVxConstants#CENTER}). */
	private int	horizontalAlignment = STRETCH;
	/** the y-axis alignment (default: {@link JVxConstants#CENTER}). */
	private int	verticalAlignment = STRETCH;
	/** The horizontal gap. */ 
	private int hgap = 5;
	/** The vertical gap. */ 
	private int vgap = 5;
	/** The new line count. */
	private int newlineCount = 2;

	/** The preferred width. */ 
	private int preferredWidth = 0;
	/** The preferred height. */ 
	private int preferredHeight = 0;
	/** The preferred width. */ 
	private int minimumWidth = 0;
	/** The preferred height. */ 
	private int minimumHeight = 0;
	/** The valid state of anchor calculation. */ 
	private boolean valid = false;
	/** True, if the target dependent anchors should be calculated again. */ 
	private boolean calculateTargetDependentAnchors = false;
	/** True, if the left border is used by another anchor. */ 
    private boolean leftBorderUsed = false;
	/** True, if the right border is used by another anchor. */ 
    private boolean rightBorderUsed = false;
	/** True, if the top border is used by another anchor. */ 
    private boolean topBorderUsed = false;
	/** True, if the bottom border is used by another anchor. */ 
    private boolean bottomBorderUsed = false;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Constructs a new FormLayout.
	 */
	public JVxFormLayout()
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    public float getLayoutAlignmentX(Container pTarget) 
    {
    	return 0.5f;
    }
	
    /**
     * {@inheritDoc}
     */
    public float getLayoutAlignmentY(Container pTarget) 
    {
    	return 0.5f;
    }

    /**
     * {@inheritDoc}
     */
    public void invalidateLayout(Container pTarget) 
    {
    	valid = false;
    }

    /**
     * {@inheritDoc}
     */
    public void addLayoutComponent(Component pComponent, Object pConstraint)
    {
    	Constraint constraint;

    	if (pConstraint instanceof Constraint)
    	{
    		constraint = (Constraint)pConstraint;
    	}
    	else
    	{
    		constraint = null;
    		
    		Container parent = pComponent.getParent();
    		int zOrder = parent.getComponentZOrder(pComponent);
		  
    		if (zOrder == parent.getComponentCount() - 1)
    		{
    			Constraint consBefore;
    			if (zOrder == 0)
    			{
    				consBefore = null;
    			}
    			else
    			{
    				consBefore = getConstraint(parent.getComponent(zOrder - 1));
    			}
				if (consBefore == null)
				{
					constraint = createConstraint(0, 0);
				}
				else
				{
					int col = leftDefaultAnchors.indexOf(consBefore.leftAnchor) / 2 + 1;
					int row = topDefaultAnchors.indexOf(consBefore.topAnchor) / 2;
					
	    			if (pConstraint == NEWLINE || (pConstraint == null && col % newlineCount == 0))
	    			{
   						constraint = createConstraint(0, row + 1);
	    			}
	    			else if (pConstraint == null)
	    			{
	    				constraint = createConstraint(col, row);
	    			}
				}
    		}
    	}
	
    	if (constraint == null)
    	{
    		throw new IllegalArgumentException("Constraint " + pConstraint + " is not allowed!");
    	}
    	else if (constraint.getLeftAnchor().getLayout() != this
    			|| constraint.getRightAnchor().getLayout() != this
    			|| constraint.getTopAnchor().getLayout() != this
    			|| constraint.getBottomAnchor().getLayout() != this)
    	{
    		throw new IllegalArgumentException("Constraint " + pConstraint + " has anchors for an other layout!");
    	}
    	else
    	{
    		constraints.put(pComponent, constraint);
    	}

    	valid = false;
    }

    /**
     * {@inheritDoc}
     */
    public void addLayoutComponent(String pName, Component pComponent) 
    {
    	// unused in LayoutManager2
    }

    /**
     * {@inheritDoc}
     */
    public void removeLayoutComponent(Component pComponent) 
    {
    	constraints.remove(pComponent);
    	
    	valid = false;
    }

    /**
     * {@inheritDoc}
     */
    public Dimension minimumLayoutSize(Container pTarget) 
    {
    	if (pTarget.isMinimumSizeSet())
    	{
        	return pTarget.getMinimumSize();
    	}
    	else
    	{
        	return new Dimension(minimumWidth, minimumHeight);
    	}
    }

    /**
     * {@inheritDoc}
     */
    public Dimension preferredLayoutSize(Container pTarget) 
    {
    	calculateAnchors(pTarget);

    	return new Dimension(preferredWidth, preferredHeight);
    }

    /**
     * {@inheritDoc}
     */
    public Dimension maximumLayoutSize(Container pTarget) 
    {
    	if (pTarget.isMaximumSizeSet())
    	{
        	return pTarget.getMaximumSize();
    	}
    	else
    	{
            return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    	}
    }

    /**
     * {@inheritDoc}
     */
    public void layoutContainer(Container pTarget) 
    {
    	calculateAnchors(pTarget);

    	calculateTargetDependentAnchors(pTarget);
    	
        // set component bounds.
        for (int i = 0; i < pTarget.getComponentCount(); i++)
        {
        	Component comp = pTarget.getComponent(i);

        	if (comp.isVisible())
        	{
            	Constraint constraint = getConstraint(comp);

            	int x = constraint.leftAnchor.getAbsolutePosition();
            	int width = constraint.rightAnchor.getAbsolutePosition() - x;
            	int y = constraint.topAnchor.getAbsolutePosition();
            	int height = constraint.bottomAnchor.getAbsolutePosition() - y;

            	comp.setBounds(x, y, width, height);
        	}
        }
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Sets the alignment for the x axis. 
	 * 
	 * @param pHorizontalAlignment the alignment {@link JVxConstants#LEFT}, {@link JVxConstants#CENTER},
	 *               {@link JVxConstants#RIGHT} or {@link JVxConstants#STRETCH}
	 */
	public void setHorizontalAlignment(int pHorizontalAlignment)
	{
		horizontalAlignment = pHorizontalAlignment;
	}

	/**
	 * Returns the alignment for the x axis.
	 * 
	 * @return the alignment {@link JVxConstants#LEFT}, {@link JVxConstants#CENTER},
	 *         {@link JVxConstants#RIGHT} or {@link JVxConstants#STRETCH} 
	 */
	public int getHorizontalAlignment()
	{
		return horizontalAlignment;
	}

	/**
	 * Sets the alignment for the y axis.
	 * 
	 * @param pVerticalAlignment the alignment {@link JVxConstants#TOP}, {@link JVxConstants#CENTER},
	 *               {@link JVxConstants#BOTTOM} or {@link JVxConstants#STRETCH}
	 */
	public void setVerticalAlignment(int pVerticalAlignment)
	{
		verticalAlignment = pVerticalAlignment;
	}

	/**
	 * Gets the alignment for the y axis.
	 * 
	 * @return the alignment {@link JVxConstants#TOP}, {@link JVxConstants#CENTER},
	 *         {@link JVxConstants#BOTTOM} or {@link JVxConstants#STRETCH}
	 */
	public int getVerticalAlignment()
	{
		return verticalAlignment;
	}

    /**
     * Gets the horizontal gap.
     * 
     * @return the horizontal gap.
     */
    public int getHorizontalGap() 
    {
        return hgap;
    }
    
    /**
     * Sets the horizontal gap.
     * 
     * @param pGap the horizontal gap.
     */
    public void setHorizontalGap(int pGap) 
    {
        hgap = pGap;
    }
    
    /**
     * Gets the vertical gap.
     * 
     * @return the vertical gap.
     */
    public int getVerticalGap() 
    {
        return vgap;
    }
    
    /**
     * Sets the vertical gap.
     * 
     * @param pGap the vertical gap.
     */
    public void setVerticalGap(int pGap) 
    {
        vgap = pGap;
    }

    /**
     * Gets the margins.
     * 
     * @return the margins.
     */
    public Insets getMargins() 
    {
        return new Insets(topMarginAnchor.position, leftMarginAnchor.position, -bottomMarginAnchor.position, -rightMarginAnchor.position);
    }
    
    /**
     * Sets the margins.
     * 
     * @param pMargins the margins.
     */
    public void setMargins(Insets pMargins) 
    {
    	if (pMargins == null)
    	{
    		topMarginAnchor.position = 0;
    		leftMarginAnchor.position = 0;
    		bottomMarginAnchor.position = 0;
    		rightMarginAnchor.position = 0;
    	}
    	else
    	{
    		topMarginAnchor.position = pMargins.top;
    		leftMarginAnchor.position = pMargins.left;
    		bottomMarginAnchor.position = -pMargins.bottom;
    		rightMarginAnchor.position = -pMargins.right;
    	}
    }

    /**
     * Gets the new line count.
     * 
     * @return the new line count.
     */
    public int getNewlineCount() 
    {
        return newlineCount;
    }
    
    /**
     * Sets the new line count.
     * 
     * @param pNewlineCount the new line count.
     */
    public void setNewlineCount(int pNewlineCount) 
    {
    	newlineCount = pNewlineCount;
    }

	/**
	 * Returns the left border anchor.
	 *
	 * @return the left border anchor.
	 */
	public Anchor getLeftAnchor()
	{
		return leftAnchor;
	}
	/**
	 * Returns the right border anchor.
	 *
	 * @return the right border anchor.
	 */
	public Anchor getRightAnchor()
	{
		return rightAnchor;
	}
	/**
	 * Returns the top border anchor.
	 *
	 * @return the top border anchor.
	 */
	public Anchor getTopAnchor()
	{
		return topAnchor;
	}
	/**
	 * Returns the bottom border anchor.
	 *
	 * @return the bottom border anchor.
	 */
	public Anchor getBottomAnchor()
	{
		return bottomAnchor;
	}
	
	/**
	 * Returns the left margin border anchor.
	 *
	 * @return the left margin border anchor.
	 */
	public Anchor getLeftMarginAnchor()
	{
		return leftMarginAnchor;
	}
	/**
	 * Returns the right margin border anchor.
	 *
	 * @return the right margin border anchor.
	 */
	public Anchor getRightMarginAnchor()
	{
		return rightMarginAnchor;
	}
	/**
	 * Returns the top margin border anchor.
	 *
	 * @return the top margin border anchor.
	 */
	public Anchor getTopMarginAnchor()
	{
		return topMarginAnchor;
	}
	/**
	 * Returns the bottom margin border anchor.
	 *
	 * @return the bottom margin border anchor.
	 */
	public Anchor getBottomMarginAnchor()
	{
		return bottomMarginAnchor;
	}
	
	/**
	 * Validates the anchors, so that they are correct.
	 */
    private void validateAnchors()
    {
        if (!valid && constraints.size() > 0)
        {
            Container container = constraints.keySet().iterator().next().getParent();
            
            if (container != null)
            {
                preferredLayoutSize(container);
            }
        }
    }
    
	/**
	 * Returns all horizontal anchors used by this layout.
	 *
	 * @return all horizontal anchors used by this layout.
	 */
	public Anchor[] getHorizontalAnchors()
	{
	    validateAnchors();
	    
		return horizontalAnchors.toArray(new Anchor[horizontalAnchors.size()]);
	}
	
	/**
	 * Returns all vertical anchors used by this layout.
	 *
	 * @return all vertical anchors used by this layout.
	 */
	public Anchor[] getVerticalAnchors()
	{
        validateAnchors();
        
		return verticalAnchors.toArray(new Anchor[verticalAnchors.size()]);
	}
	
	/**
	 * Gets the constraints for given component.
	 * 
	 * @param pComponent the component.
	 * @return the constraints for the given component.
	 */
    public Constraint getConstraint(Component pComponent)
    {
    	return constraints.get(pComponent);
    }

    /**
     * Sets the constraints for given component.
     * 
     * @param pComponent  the component.
     * @param pConstraint the constraints for the given component.
     */
    public void setConstraint(final Component pComponent, Constraint pConstraint)
    {
    	if (constraints.containsKey(pComponent))
    	{
            constraints.put(pComponent, pConstraint);

            //#2292
            pComponent.invalidate();
            if (pComponent.getParent() instanceof JComponent)
            {
                ((JComponent)pComponent.getParent()).revalidate();
            }
    	}
    	else
    	{
    		throw new IllegalArgumentException("Component " + pComponent + " has no constraints to overwrite!");
    	}
    }

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
    private Anchor[] createDefaultAnchors(List<Anchor> pLeftTopDefaultAnchors, 
    									  List<Anchor> pRightBottomDefaultAnchors, 
    		                              Anchor pLeftTopAnchor, 
    		                              Anchor pRightBottomAnchor, 
    		                              int pColumnOrRow,
    		                              int pGap)
    {
        String ltName;
        String rbName;
        if (pLeftTopAnchor == leftMarginAnchor)
        {
            ltName = "l";
            rbName = "r";
        }
        else
        {
            ltName = "t";
            rbName = "b";
        }

        List<Anchor> defaultAnchors;
    	Anchor anchor;
    	int gap;
        int size;
        int index;
    	
    	boolean rightBottom = pColumnOrRow < 0;
    	if (rightBottom)
    	{
        	pColumnOrRow = (-pColumnOrRow - 1) * 2;
        	defaultAnchors = pRightBottomDefaultAnchors;
        	anchor = pRightBottomAnchor;
        	gap = -pGap;
            size = defaultAnchors.size();
            index = -size / 2 - 1; 
    	}
    	else
    	{
        	pColumnOrRow *= 2;
        	defaultAnchors = pLeftTopDefaultAnchors;
        	anchor = pLeftTopAnchor;
        	gap = pGap;
            size = defaultAnchors.size();
            index = size / 2;
    	}

    	while (pColumnOrRow >= size)
    	{
    		if (size == 0)
    		{
    			defaultAnchors.add(anchor);
    		}
    		else
    		{
    			defaultAnchors.add(new Anchor(defaultAnchors.get(size - 1), gap, rightBottom ? rbName + index : ltName + index));
    		}
    		defaultAnchors.add(new Anchor(defaultAnchors.get(size), rightBottom ? ltName + index : rbName + index));
    		
            if (rightBottom)
            {
                index--;
            }
            else
            {
                index++;
            }
    		size = defaultAnchors.size();
    	}
    	
    	if (rightBottom)
    	{
        	return new Anchor[] {defaultAnchors.get(pColumnOrRow + 1), defaultAnchors.get(pColumnOrRow)}; 
    	}
    	else
    	{
        	return new Anchor[] {defaultAnchors.get(pColumnOrRow), defaultAnchors.get(pColumnOrRow + 1)}; 
    	}
    }
    
	/**
	 * Creates the default constraints for the given column and row.
	 * 
	 * @param pColumn the column.
	 * @param pRow the row.
	 * @return the constraints for the given component.
	 */
    public Constraint createConstraint(int pColumn, int pRow)
    {
    	return createConstraint(pColumn, pRow, pColumn, pRow);
    }

	/**
	 * Creates the default constraints for the given column and row.
	 * 
	 * @param pBeginColumn the begin column.
	 * @param pBeginRow the begin row.
	 * @param pEndColumn the end column.
	 * @param pEndRow the end row.
	 * @return the constraints for the given component.
	 */
    public Constraint createConstraint(int pBeginColumn, int pBeginRow, int pEndColumn, int pEndRow)
    {
    	Anchor[] left = createDefaultAnchors(leftDefaultAnchors, rightDefaultAnchors, leftMarginAnchor, rightMarginAnchor, pBeginColumn, hgap);
    	Anchor[] right;
    	if (pBeginColumn == pEndColumn)
    	{
    		right = left;
    	}
    	else
    	{
    		right = createDefaultAnchors(leftDefaultAnchors, rightDefaultAnchors, leftMarginAnchor, rightMarginAnchor, pEndColumn, hgap);
    	}
    	
    	Anchor[] top = createDefaultAnchors(topDefaultAnchors, bottomDefaultAnchors, topMarginAnchor, bottomMarginAnchor, pBeginRow, vgap);
    	Anchor[] bottom;
    	if (pBeginRow == pEndRow)
    	{
    		bottom = top;
    	}
    	else
    	{
    		bottom = createDefaultAnchors(topDefaultAnchors, bottomDefaultAnchors, topMarginAnchor, bottomMarginAnchor, pEndRow, vgap);
    	}
    	return new Constraint(top[0], 
    						  left[0], 
    						  bottom[1], 
    						  right[1]);
    }

    /**
     * clears auto size position of anchor.
     * 
     * @param pAnchorList the list to add.
     * @param pAnchor the left or top anchor.
     */
    private void clearAutoSize(List<Anchor> pAnchorList, Anchor pAnchor)
    {
        Anchor anchor = pAnchor;
        int pos = pAnchorList.size();
        while (anchor != null && !pAnchorList.contains(anchor))
        {
            pAnchorList.add(pos, anchor);

            anchor.relative = anchor.autoSize;
            anchor.autoSizeCalculated = false;
            anchor.firstCalculation = true;
            anchor.used = false;
            if (anchor.autoSize)
            {
                anchor.position = 0;
            }

            anchor = anchor.relatedAnchor;
        }
        pAnchor.used = true;
    }
    
    /**
     * Inits the autosize with negative gap, to ensure the gaps are, as there is no component in this row or column.
     * @param pAnchor the anchor
     */
    private void initAutoSize(Anchor pAnchor)
    {
        Anchor relatedAutoSizeAnchor = pAnchor.relatedAnchor;
        if (relatedAutoSizeAnchor != null)
        {
            if (!pAnchor.used && relatedAutoSizeAnchor.used 
                    && relatedAutoSizeAnchor != leftMarginAnchor
                    && relatedAutoSizeAnchor != topMarginAnchor
                    && relatedAutoSizeAnchor != rightMarginAnchor
                    && relatedAutoSizeAnchor != bottomMarginAnchor)
            {
                pAnchor.used = true;
            }

            if (relatedAutoSizeAnchor.autoSize && !pAnchor.autoSize && relatedAutoSizeAnchor.relatedAnchor != null && !relatedAutoSizeAnchor.relatedAnchor.autoSize)
            {
                relatedAutoSizeAnchor.position = relatedAutoSizeAnchor.used ? -relatedAutoSizeAnchor.relatedAnchor.position : -pAnchor.position;
            }
        }
    }
    
    /**
     * Gets all auto size anchors between start and end anchor.
     * @param pStartAnchor start anchor.
     * @param pEndAnchor end anchor.
     * @return all auto size anchors between start and end anchor.
     */
    private List<Anchor> getAutoSizeAnchorsBetween(Anchor pStartAnchor, Anchor pEndAnchor)
    {
    	anchorsBuffer.clear();
    	while (pStartAnchor != null && pStartAnchor != pEndAnchor)
    	{
    		if (pStartAnchor.autoSize && !pStartAnchor.autoSizeCalculated)
    		{
    			anchorsBuffer.add(pStartAnchor);
    		}
    		pStartAnchor = pStartAnchor.relatedAnchor;
    	}
    	if (pStartAnchor == null)
    	{
    		anchorsBuffer.clear();
    	}
    	return anchorsBuffer;
    }
    
    /**
     * init component auto size position of anchor.
     * 
     * @param pStartAnchor the start anchor.
     * @param pEndAnchor the end anchor.
     */
    private void initAutoSize(Anchor pStartAnchor, Anchor pEndAnchor)
    {
    	List<Anchor> anchors = getAutoSizeAnchorsBetween(pStartAnchor, pEndAnchor);
    	
		for (int i = 0; i < anchors.size(); i++)
		{
			Anchor anchor = anchors.get(i);
			anchor.relative = false;
		}
    }
    
    /**
     * Marks all touched Autosize anchors as calculated. 
     * @param pLeftTopAnchor the left or top anchor
     * @param pRightBottomAnchor the right or bottom anchor
     * @return amount of autosize anchors left.
     */
    private int finishAutoSizeCalculation(Anchor pLeftTopAnchor, Anchor pRightBottomAnchor)
    {
    	List<Anchor> anchors = getAutoSizeAnchorsBetween(pLeftTopAnchor, pRightBottomAnchor);
    	int count = anchors.size();
    	for (int i = 0, size = anchors.size(); i < size; i++)
		{
			Anchor anchor = anchors.get(i);
			if (!anchor.firstCalculation)
			{
				anchor.autoSizeCalculated = true;
				count--;
			}
		}
    	return count;
    }
    
    /**
     * Calculates the preferred size of component auto size anchors.
     * 
     * @param pLeftTopAnchor the left or top anchor.
     * @param pRightBottomAnchor the right or bottom anchor.
     * @param pPreferredSize the preferred size.
     * @param pAutoSizeCount the amount of autoSizeCount.
     */
    private void calculateAutoSize(Anchor pLeftTopAnchor, Anchor pRightBottomAnchor, int pPreferredSize, int pAutoSizeCount)
    {
    	List<Anchor> anchors = getAutoSizeAnchorsBetween(pLeftTopAnchor, pRightBottomAnchor);
    	int size = anchors.size();
    	if (size == pAutoSizeCount) // && pLeftTopAnchor.getRelatedAnchor() == pRightBottomAnchor)
    	{
    		int fixedSize = pRightBottomAnchor.getAbsolutePosition() - pLeftTopAnchor.getAbsolutePosition();
    		for (int i = 0; i < size; i++)
    		{
    			fixedSize += anchors.get(i).position;
    		}
    		
    		int diffSize = (pPreferredSize - fixedSize + size - 1) / size;
    		for (int i = 0; i < size; i++)
    		{
    			Anchor anchor = anchors.get(i);
    			if (diffSize > -anchor.position)
    			{
    				anchor.position = -diffSize;
    			}
				anchor.firstCalculation = false;
    		}
    	}
    	
    	anchors = getAutoSizeAnchorsBetween(pRightBottomAnchor, pLeftTopAnchor);
    	size = anchors.size();
    	
    	if (anchors.size() == pAutoSizeCount) // && pRightBottomAnchor.getRelatedAnchor() == pLeftTopAnchor)
    	{
    		int fixedSize = pRightBottomAnchor.getAbsolutePosition() - pLeftTopAnchor.getAbsolutePosition();
    		for (int i = 0; i < size; i++)
    		{
    			fixedSize -= anchors.get(i).position;
    		}
    		
    		int diffSize = (pPreferredSize - fixedSize + size - 1) / size;
    		for (int i = 0; i < size; i++)
    		{
    			Anchor anchor = anchors.get(i);
    			if (diffSize > anchor.position)
    			{
    				anchor.position = diffSize;
    			}
				anchor.firstCalculation = false;
    		}
    	}
    }
    
    /**
     * Calculates the preferred size of relative anchors.
     * 
     * @param pLeftTopAnchor the left or top anchor.
     * @param pRightBottomAnchor the right or bottom anchor.
     * @param pPreferredSize the preferred size.
     */
    private void calculateRelativeAnchor(Anchor pLeftTopAnchor, Anchor pRightBottomAnchor, int pPreferredSize)
    {
    	if (pLeftTopAnchor.relative)
    	{
    		Anchor rightBottom = pRightBottomAnchor.getRelativeAnchor();
    		if (rightBottom != null && rightBottom != pLeftTopAnchor)
    		{
    			int pref = rightBottom.getAbsolutePosition() - pRightBottomAnchor.getAbsolutePosition() + pPreferredSize;
    			int size = rightBottom.relatedAnchor.getAbsolutePosition() - pLeftTopAnchor.relatedAnchor.getAbsolutePosition();
    			
    			int pos = pref - size;
    			if (pos < 0)
    			{
    				pos /= 2;
    			}
    			else
    			{
    				pos -= pos / 2;
    			}
    			if (rightBottom.firstCalculation || pos > rightBottom.position)
    			{
    				rightBottom.firstCalculation = false;
    				rightBottom.position = pos;
    			}
    			pos = pref - size - pos;
    			if (pLeftTopAnchor.firstCalculation || pos > -pLeftTopAnchor.position)
    			{
    				pLeftTopAnchor.firstCalculation = false;
    				pLeftTopAnchor.position = -pos;
    			}
    		}
    	}
    	else if (pRightBottomAnchor.relative)
    	{
    		Anchor leftTop = pLeftTopAnchor.getRelativeAnchor();
    		if (leftTop != null && leftTop != pRightBottomAnchor)
    		{
    			int pref = pLeftTopAnchor.getAbsolutePosition() - leftTop.getAbsolutePosition() + pPreferredSize;
    			int size = pRightBottomAnchor.relatedAnchor.getAbsolutePosition() - leftTop.relatedAnchor.getAbsolutePosition();

    			int pos = size - pref;
    			if (pos < 0)
    			{
    				pos -= pos / 2;
    			}
    			else
    			{
    				pos /= 2;
    			}
    			if (leftTop.firstCalculation || pos < leftTop.position)
    			{
    				leftTop.firstCalculation = false;
    				leftTop.position = pos;
    			}
    			pos = pref - size - pos;
    			if (pRightBottomAnchor.firstCalculation || pos > -pRightBottomAnchor.position)
    			{
    				pRightBottomAnchor.firstCalculation = false;
    				pRightBottomAnchor.position = -pos;
    			}
    		}
    	}
    }
    
    /**
     * Calculates all auto size anchors.
     * 
     * @param pTarget the target.
     */
    private void calculateAnchors(Container pTarget)
    {
        if (!valid)
        {
            synchronized (pTarget.getTreeLock())
            {
                valid = true;
    	        // reset border anchors
    	    	leftAnchor.position = 0;
    	        rightAnchor.position = 0;
    	        topAnchor.position = 0;
    	        bottomAnchor.position = 0;
    	        // reset preferred size;
    	        preferredWidth = 0;
    	        preferredHeight = 0;
    	        // reset minimum size;
    	        minimumWidth = 0;
    	        minimumHeight = 0;
    	        // reset List of Anchors;
    	        horizontalAnchors.clear();
    	        verticalAnchors.clear();
    
    	        // clear auto size anchors.
    	        for (int i = 0; i < pTarget.getComponentCount(); i++)
    	        {
    	            Component comp = pTarget.getComponent(i);
                    Constraint constraint = getConstraint(comp);
                    
                    if (comp.isVisible())
                    {
                        clearAutoSize(horizontalAnchors, constraint.leftAnchor);
                        clearAutoSize(horizontalAnchors, constraint.rightAnchor);
                        clearAutoSize(verticalAnchors, constraint.topAnchor);
                        clearAutoSize(verticalAnchors, constraint.bottomAnchor);
                    }
    	        }
    	        for (Anchor anchor : horizontalAnchors)
    	        {
    	            initAutoSize(anchor);
    	        }
                for (Anchor anchor : verticalAnchors)
                {
                    initAutoSize(anchor);
                }
    	    	// init component auto size anchors.
    	        for (int i = 0; i < pTarget.getComponentCount(); i++)
    	        {
                    Component comp = pTarget.getComponent(i);
    	        	Constraint constraint = getConstraint(comp);
    	        	
                    if (comp.isVisible())
                    {
        	        	initAutoSize(constraint.leftAnchor, constraint.rightAnchor);
        	        	initAutoSize(constraint.rightAnchor, constraint.leftAnchor);
        	        	initAutoSize(constraint.topAnchor, constraint.bottomAnchor);
        	        	initAutoSize(constraint.bottomAnchor, constraint.topAnchor);
                    }
    	        }
    	        int autoSizeCount = 1;
    	        
    	        do
    	        {
    		        // calculate component auto size anchors.
    		        for (int i = 0; i < pTarget.getComponentCount(); i++)
    		        {
    		        	Component comp = pTarget.getComponent(i);
    		        	if (comp.isVisible())
    		        	{
    		            	Constraint constraint = getConstraint(comp);
    		            	
    		            	Dimension preferredSize = JVxUtil.getPreferredSize(comp);
    		            	
    		            	calculateAutoSize(constraint.topAnchor, constraint.bottomAnchor, preferredSize.height, autoSizeCount);
    		            	calculateAutoSize(constraint.leftAnchor, constraint.rightAnchor, preferredSize.width, autoSizeCount);
    		        	}
    		        }
    		        autoSizeCount = Integer.MAX_VALUE;
    		        for (int i = 0; i < pTarget.getComponentCount(); i++)
    		        {
    		        	Component comp = pTarget.getComponent(i);
    		        	if (comp.isVisible())
    		        	{
    		            	Constraint constraint = getConstraint(comp);
    		            	
    		            	int count = finishAutoSizeCalculation(constraint.leftAnchor, constraint.rightAnchor);
    		            	if (count > 0 && count < autoSizeCount)
    		            	{
    		            		autoSizeCount = count;
    		            	}
    		            	count = finishAutoSizeCalculation(constraint.rightAnchor, constraint.leftAnchor);
    		            	if (count > 0 && count < autoSizeCount)
    		            	{
    		            		autoSizeCount = count;
    		            	}
    		            	count = finishAutoSizeCalculation(constraint.topAnchor, constraint.bottomAnchor);
    		            	if (count > 0 && count < autoSizeCount)
    		            	{
    		            		autoSizeCount = count;
    		            	}
    		            	count = finishAutoSizeCalculation(constraint.bottomAnchor, constraint.topAnchor);
    		            	if (count > 0 && count < autoSizeCount)
    		            	{
    		            		autoSizeCount = count;
    		            	}
    		        	}
    		        }
    	        } while (autoSizeCount > 0 && autoSizeCount < Integer.MAX_VALUE);
    	        
    	        leftBorderUsed = false;
    	        rightBorderUsed = false;
    	        topBorderUsed = false;
    	        bottomBorderUsed = false;
    	        int leftWidth = 0;
    	        int rightWidth = 0;
    	        int topHeight = 0;
    	        int bottomHeight = 0;
    	        
    	        // calculate preferredSize.
    	        for (int i = 0; i < pTarget.getComponentCount(); i++)
    	        {
    	        	Component comp = pTarget.getComponent(i);
    	        	if (comp.isVisible())
    	        	{
    	            	Constraint constraint = getConstraint(comp);
    	            	
    	            	Dimension preferredSize = JVxUtil.getPreferredSize(comp);
    	            	Dimension minimumSize = JVxUtil.getMinimumSize(comp);
    	
    	            	if (constraint.rightAnchor.getBorderAnchor() == leftAnchor)
    	            	{
    	            		int w = constraint.rightAnchor.getAbsolutePosition();
    	            		if (w > leftWidth)
    	            		{
    	            			leftWidth = w;
    	            		}
    	            		leftBorderUsed = true;
    	            	}
    	            	if (constraint.leftAnchor.getBorderAnchor() == rightAnchor)
    	            	{
    	            		int w = -constraint.leftAnchor.getAbsolutePosition();
    	            		if (w > rightWidth)
    	            		{
    	            			rightWidth = w;
    	            		}
    	            		rightBorderUsed = true;
    	            	}
    	            	if (constraint.bottomAnchor.getBorderAnchor() == topAnchor)
    	            	{
    	            		int h = constraint.bottomAnchor.getAbsolutePosition();
    	            		if (h > topHeight)
    	            		{
    	            			topHeight = h;
    	            		}
    	            		topBorderUsed = true;
    	            	}
    	            	if (constraint.topAnchor.getBorderAnchor() == bottomAnchor)
    	            	{
    	            		int h = -constraint.topAnchor.getAbsolutePosition();
    	            		if (h > bottomHeight)
    	            		{
    	            			bottomHeight = h;
    	            		}
    	            		bottomBorderUsed = true;
    	            	}
    	            	if (constraint.leftAnchor.getBorderAnchor() == leftAnchor && constraint.rightAnchor.getBorderAnchor() == rightAnchor)
    	            	{
    	            	    if (!constraint.leftAnchor.isAutoSize() || !constraint.rightAnchor.isAutoSize())
    	            	    {
        	            		int w = constraint.leftAnchor.getAbsolutePosition() - constraint.rightAnchor.getAbsolutePosition() +
        	            		        preferredSize.width;
        	            		if (w > preferredWidth)
        	            		{
        	            			preferredWidth = w;
        	            		}
        	            		w = constraint.leftAnchor.getAbsolutePosition() - constraint.rightAnchor.getAbsolutePosition() +
                		        	minimumSize.width;
                	    		if (w > minimumWidth)
                	    		{
                	    			minimumWidth = w;
                	    		}
    	            	    }
    	            		leftBorderUsed = true;
    	            		rightBorderUsed = true;
    	            	}
    	            	if (constraint.topAnchor.getBorderAnchor() == topAnchor && constraint.bottomAnchor.getBorderAnchor() == bottomAnchor)
    	            	{
    	            	    if (!constraint.topAnchor.isAutoSize() || !constraint.bottomAnchor.isAutoSize())
    	            	    {
        	            		int h = constraint.topAnchor.getAbsolutePosition() - constraint.bottomAnchor.getAbsolutePosition() +
        	            		        preferredSize.height;
        	            		if (h > preferredHeight)
        	            		{
        	            			preferredHeight = h;
        	            		}
        	            		h = constraint.topAnchor.getAbsolutePosition() - constraint.bottomAnchor.getAbsolutePosition() +
                		        	minimumSize.height;
                	    		if (h > minimumHeight)
                	    		{
                	    			minimumHeight = h;
                	    		}
    	            	    }
    	            		topBorderUsed = true;
    	            		bottomBorderUsed = true;
    	            	}
    	        	}
    	        }
    	        if (leftWidth != 0 && rightWidth != 0)
    	        {
    	        	int w = leftWidth + rightWidth + hgap;
    	    		if (w > preferredWidth)
    	    		{
    	    			preferredWidth = w;
    	    		}
    	    		if (w > minimumWidth)
    	    		{
    	    			minimumWidth = w;
    	    		}
    	        }
    	        else if (leftWidth != 0)
    	        {
    	        	int w = leftWidth - rightMarginAnchor.position;
    	        	if (w > preferredWidth)
    	        	{
    	        		preferredWidth = w;
    	        	}
    	    		if (w > minimumWidth)
    	    		{
    	    			minimumWidth = w;
    	    		}
    	        }
    	        else if (rightWidth != 0)
    	        {
    	        	int w = rightWidth + leftMarginAnchor.position;
    	        	if (w > preferredWidth)
    	        	{
    	        		preferredWidth = w;
    	        	}
    	    		if (w > minimumWidth)
    	    		{
    	    			minimumWidth = w;
    	    		}
    	        }
    	        else
    	        {
                    int w = leftMarginAnchor.position - rightMarginAnchor.position;
                    if (w > preferredWidth)
                    {
                        preferredWidth = w;
                    }
                    if (w > minimumWidth)
                    {
                        minimumWidth = w;
                    }
    	        }
    	        if (topHeight != 0 && bottomHeight != 0)
    	        {
    	        	int h = topHeight + bottomHeight + vgap;
    	    		if (h > preferredHeight)
    	    		{
    	    			preferredHeight = h;
    	    		}
    	    		if (h > minimumHeight)
    	    		{
    	    			minimumHeight = h;
    	    		}
    	        }
    	        else if (topHeight != 0)
    	        {
    	        	int h = topHeight - bottomMarginAnchor.position;
    	        	if (h > preferredHeight)
    	        	{
    	        		preferredHeight = h;
    	        	}
    	    		if (h > minimumHeight)
    	    		{
    	    			minimumHeight = h;
    	    		}
    	        }
    	        else if (bottomHeight != 0)
    	        {
    	        	int h = bottomHeight + topMarginAnchor.position;
    	        	if (h > preferredHeight)
    	        	{
    	        		preferredHeight = h;
    	        	}
    	    		if (h > minimumHeight)
    	    		{
    	    			minimumHeight = h;
    	    		}
    	        }
    	        else
    	        {
                    int h = topMarginAnchor.position - bottomMarginAnchor.position;
                    if (h > preferredHeight)
                    {
                        preferredHeight = h;
                    }
                    if (h > minimumHeight)
                    {
                        minimumHeight = h;
                    }
    	        }
    	        
    		    Insets ins = pTarget.getInsets();
    		    
    	        preferredWidth += ins.left + ins.right;
    	        preferredHeight += ins.top + ins.bottom;
    	        
    	        minimumWidth += ins.left + ins.right;
    	        minimumHeight += ins.top + ins.bottom;
    	        
    	        calculateTargetDependentAnchors = true;
            }
      	}
    }
	
    /**
     * Calculates all target size dependent anchors.
     * This can only be done after the target has his correct size.
     * 
     * @param pTarget the target.
     */
    private void calculateTargetDependentAnchors(Container pTarget)
    {
    	if (calculateTargetDependentAnchors)
    	{
		    // set border anchors
		    Dimension size = pTarget.getSize();
		    Dimension minSize = minimumLayoutSize(pTarget);
		    Dimension maxSize = maximumLayoutSize(pTarget);
		    Insets ins = pTarget.getInsets();
		    size.width -= ins.left + ins.right;
		    size.height -= ins.top + ins.bottom;
		    minSize.width -= ins.left + ins.right;
		    minSize.height -= ins.top + ins.bottom;
		    maxSize.width -= ins.left + ins.right;
		    maxSize.height -= ins.top + ins.bottom;
		    
		    if (horizontalAlignment == STRETCH || (leftBorderUsed && rightBorderUsed))
		    {
		    	if (minSize.width > size.width)
		    	{
		        	leftAnchor.position = 0;
		            rightAnchor.position = minSize.width;
		    	}
		    	else if (maxSize.width < size.width)
		    	{
		    		switch (horizontalAlignment)
		    		{
		    			case LEFT: 
		    				leftAnchor.position = 0; 
		    				break;
		    			case RIGHT:
		    				leftAnchor.position = size.width - maxSize.width; 
		    				break;
		    			default:
		    				leftAnchor.position = (size.width - maxSize.width) / 2; 
		    		}
		    		rightAnchor.position = leftAnchor.position + maxSize.width;
		    	}
		    	else
		    	{
		        	leftAnchor.position = 0;
		            rightAnchor.position = size.width;
		    	}
		    }
		    else
		    {
		    	if (preferredWidth > size.width)
		    	{
		    		leftAnchor.position = 0;
		    	}
		    	else
		    	{
		    		switch (horizontalAlignment)
		    		{
		    			case LEFT: 
		    				leftAnchor.position = 0; 
		    				break;
		    			case RIGHT:
		    				leftAnchor.position = size.width - preferredWidth; 
		    				break;
		    			default:
		    				leftAnchor.position = (size.width - preferredWidth) / 2; 
		    		}
		    	}
		    	rightAnchor.position = leftAnchor.position + preferredWidth;
		    }
		    if (verticalAlignment == STRETCH || (topBorderUsed && bottomBorderUsed))
		    {
		    	if (minSize.height > size.height)
		    	{
		            topAnchor.position = 0;
		    		bottomAnchor.position = minSize.height;
		    	}
		    	else if (maxSize.height < size.height)
		    	{
		    		switch (verticalAlignment)
		    		{
		    			case TOP: 
		    				topAnchor.position = 0; 
		    				break;
		    			case BOTTOM:
		    				topAnchor.position = size.height - maxSize.height; 
		    				break;
		    			default:
		    				topAnchor.position = (size.height - maxSize.height) / 2; 
		    		}
		    		bottomAnchor.position = topAnchor.position + maxSize.height;
		    	}
		    	else
		    	{
		            topAnchor.position = 0;
		    		bottomAnchor.position = size.height;
		    	}
		    }
		    else
		    {
		    	if (preferredHeight > size.height)
		    	{
		    		topAnchor.position = 0;
		    	}
		    	else
		    	{
		    		switch (verticalAlignment)
		    		{
		    			case TOP: 
		    				topAnchor.position = 0; 
		    				break;
		    			case BOTTOM:
		    				topAnchor.position = size.height - preferredHeight; 
		    				break;
		    			default:
		    				topAnchor.position = (size.height - preferredHeight) / 2; 
		    		}
		    	}
		    	bottomAnchor.position = topAnchor.position + preferredHeight;
		    }
		    leftAnchor.position += ins.left;
		    rightAnchor.position += ins.left;
		    topAnchor.position += ins.top;
		    bottomAnchor.position += ins.top;
		
		    // calculate relative anchors.
		    for (int i = 0; i < pTarget.getComponentCount(); i++)
		    {
		    	Component comp = pTarget.getComponent(i);
		    	if (comp.isVisible())
		    	{
		        	Constraint constraint = getConstraint(comp);
		        	
		        	Dimension preferredSize = JVxUtil.getPreferredSize(comp);
		        	
		        	calculateRelativeAnchor(constraint.leftAnchor, constraint.rightAnchor, preferredSize.width);
		        	calculateRelativeAnchor(constraint.topAnchor, constraint.bottomAnchor, preferredSize.height);
		    	}
		    }
		    calculateTargetDependentAnchors = false;
    	}
    }
    
	//****************************************************************
	// Subclass definition
	//****************************************************************

    /**
	 * The Anchor gives the possible horizontal and vertical positions.
	 * 
	 * @author Martin Handsteiner 
	 */
	public static class Anchor 
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/** Constant for horizontal anchors. */
		public static final int HORIZONTAL = 0;
		/** Constant for vertical anchors. */
		public static final int VERTICAL   = 1;

		/** The layout for this anchor. */
		private JVxFormLayout layout;
		/** The anchor name. */
		private String name;
		/** The orientation of this anchor. */
		private int     orientation;
		/** The related anchor to this anchor. */
		private Anchor  relatedAnchor;
		/** true, if this anchor should be auto sized. */
		private boolean autoSize;
		/** The position of this anchor. */
		private boolean autoSizeCalculated;
		/** The position of this anchor. */
		private int     position;
		/** True, if the anchor is not calculated by components preferred size. **/
		private boolean relative;
		/** True, if the relative anchor is not calculated. **/
		private boolean firstCalculation;
        /** True, if the anchor is used by a visible component. **/
        private boolean used;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Internal for constructing border anchors.
		 * @param pLayout the layout for this anchor.
		 * @param pOrientation the orientation for this anchor.
         * @param pName the name.
		 */
		private Anchor(JVxFormLayout pLayout, int pOrientation, String pName) 
		{
			layout = pLayout;
			name = pName;
			orientation = pOrientation;
			relatedAnchor = null;
			autoSize = false;
			position = 0;
		}
		
        /**
         * Constructs an anchor relative to pRelatedAnchor auto sized.
         * @param pRelatedAnchor the related anchor for this anchor.
         */
        public Anchor(Anchor pRelatedAnchor)
        {
            this(pRelatedAnchor, null);
        }
        
		/**
		 * Constructs an anchor relative to pRelatedAnchor auto sized.
		 * @param pRelatedAnchor the related anchor for this anchor.
         * @param pName the name.
		 */
		public Anchor(Anchor pRelatedAnchor, String pName)
		{
			layout = pRelatedAnchor.layout;
			name = pName != null ? pName : orientationPrefix(pRelatedAnchor.orientation) + "" + layout.anchorCounter++ + ":" + pRelatedAnchor.name;
			
			orientation = pRelatedAnchor.orientation;
			relatedAnchor = pRelatedAnchor;
			autoSize = true;
			position = 0;
		}

        /**
         * Constructs an anchor relative to pRelatedAnchor with pPosition pixels.
         * @param pRelatedAnchor the related anchor for this anchor.
         * @param pPosition the position relative to the related anchor.
         */
        public Anchor(Anchor pRelatedAnchor, int pPosition)
        {
            this(pRelatedAnchor, pPosition, null);
        }
        
		/**
		 * Constructs an anchor relative to pRelatedAnchor with pPosition pixels.
		 * @param pRelatedAnchor the related anchor for this anchor.
		 * @param pPosition the position relative to the related anchor.
         * @param pName the name.
		 */
		public Anchor(Anchor pRelatedAnchor, int pPosition, String pName)
		{
			layout = pRelatedAnchor.layout;
			name = pName != null ? pName : orientationPrefix(pRelatedAnchor.orientation) + "" + layout.anchorCounter++ + ":" + pRelatedAnchor.name;
			
			orientation = pRelatedAnchor.orientation;
			relatedAnchor = pRelatedAnchor;
			autoSize = false;
			position = pPosition;
		}

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Overwritten methods
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        
        /**
         * {@inheritDoc}
         */
        @Override
        public String toString()
        {
            return "Anchor: " + name + "," + (relatedAnchor == null ? "-" : relatedAnchor.name)
                    + "," + "-"
                    + "," + (autoSize ? "a" : String.valueOf(position))
                    + "," + String.valueOf(position);
        }

		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// User-defined methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /**
         * Gets the prefix for an orientation.
         * 
         * @param pOrientation the orientation
         * @return the prefix
         */
        private static String orientationPrefix(int pOrientation)
        {
            if (pOrientation == HORIZONTAL)
            {
                return "h";
            }
            else
            {
                return "v";
            }
        }
        
		/**
		 * Returns the Layout to which this Anchor belongs.
		 *
		 * @return the layout.
		 */
		public JVxFormLayout getLayout()
		{
			return layout;
		}

		/**
		 * Returns whether the orientation of this Anchor is <code>HORIZONTAL</code> or <code>VERTICAL</code>.
		 *
		 * @return the orientation.
		 */
		public int getOrientation()
		{
			return orientation;
		}

		/**
		 * Returns true, if this anchor is the border anchor.
		 *
		 * @return the fixed.
		 */
		public boolean isBorderAnchor()
		{
			return relatedAnchor == null;
		}
		
		/**
		 * Returns the related Anchor.
		 *
		 * @return the relatedAnchor.
		 */
		public Anchor getRelatedAnchor()
		{
			return relatedAnchor;
		}

		/**
		 * true, if pRelatedAnchor has a cycle reference to this anchor.
		 *
		 * @param pRelatedAnchor the relatedAnchor to set.
		 * @return true, if pRelatedAnchor has a cycle reference to this anchor.
		 */
		private boolean hasCycleReference(Anchor pRelatedAnchor)
		{
			do
			{
				if (pRelatedAnchor == this)
				{
					return true;
				}
				pRelatedAnchor = pRelatedAnchor.relatedAnchor;
			}
			while (pRelatedAnchor != null);
			
			return false;
		}
		
		/**
		 * Sets the related Anchor.
		 * It is only allowed to choose an anchor with same orientation from the same layout.
		 *
		 * @param pRelatedAnchor the relatedAnchor to set.
		 */
		public void setRelatedAnchor(Anchor pRelatedAnchor)
		{
			if (layout != pRelatedAnchor.layout || orientation != pRelatedAnchor.orientation)
			{
				throw new IllegalArgumentException("The related anchor must have the same layout and the same orientation!");
			}
			else if (hasCycleReference(pRelatedAnchor))
			{
				throw new IllegalArgumentException("The related anchor has a cycle reference to this anchor!");
			}
			else
			{
				relatedAnchor = pRelatedAnchor;
			}
		}
		
		/**
		 * Returns true, if the position of this anchor is calculated automatically.
		 *
		 * @return the fixed.
		 */
		public boolean isAutoSize()
		{
			return autoSize;
		}

		/**
		 * Sets, if the position of this anchor is calculated automatically.
		 *
		 * @param pAutoSize the fixed to set.
		 */
		public void setAutoSize(boolean pAutoSize)
		{
			autoSize = pAutoSize;
		}

		/**
		 * Returns the position of this Anchor.
		 * The position is only correct if the layout is valid.
		 *
		 * @return the position.
		 */
		public int getPosition()
		{
			return position;
		}

		/**
		 * Sets the position of this Anchor.
		 * It is not allowed to set the position of a border anchor.
		 *
		 * @param pPosition the position to set
		 */
		public void setPosition(int pPosition)
		{
			if (relatedAnchor == null)
			{
				throw new IllegalArgumentException("Position of border anchor may not be set!");
			}
			else
			{
				position = pPosition;
			}
		}
		
		/**
		 * Returns the absolute position of this Anchor in this FormLayout.
		 * The position is only correct if the layout is valid.
		 *
		 * @return the absolute position.
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
		 * Gets the related border anchor to this anchor.
		 *
		 * @return the related border anchor.
		 */
		public Anchor getBorderAnchor()
		{
			Anchor borderAnchor = this;
			while (borderAnchor.relatedAnchor != null)
			{
				borderAnchor = borderAnchor.relatedAnchor;
			}
			return borderAnchor;
		}

		/**
		 * true, if the anchor is relative.
		 *
		 * @return true, if the anchor is relative.
		 */
		public boolean isRelative()
		{
			return relative;
		}
		
		/**
		 * Gets the related unused auto size anchor.
		 *
		 * @return the related unused auto size anchor.
		 */
		public Anchor getRelativeAnchor()
		{
			Anchor relativeAnchor = this;
			while (relativeAnchor != null && !relativeAnchor.relative)
			{
				relativeAnchor = relativeAnchor.relatedAnchor;
			}
			return relativeAnchor;
		}
		
	}	// Anchor
	
	/**
	 * The Constraint stores the top, left, bottom and right Anchor for layouting a component.
	 * 
	 * @author Martin Handsteiner
	 */
	public static class Constraint implements Cloneable
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/** The top anchor. */
		private Anchor topAnchor;
		/** The left anchor. */
		private Anchor leftAnchor;
		/** The bottom anchor. */
		private Anchor bottomAnchor;
		/** The right anchor. */
		private Anchor rightAnchor;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Constructs Constraints with the given anchors as bounds.
		 * @param pTopAnchor the left anchor.
		 * @param pLeftAnchor the left anchor.
		 * @param pBottomAnchor the left anchor.
		 * @param pRightAnchor the left anchor.
		 */
		public Constraint(Anchor pTopAnchor, Anchor pLeftAnchor, Anchor pBottomAnchor, Anchor pRightAnchor)
		{
			if (pLeftAnchor == null && pRightAnchor != null)
			{
				pLeftAnchor = new Anchor(pRightAnchor);
			}
			else if (pRightAnchor == null && pLeftAnchor != null)
			{
				pRightAnchor = new Anchor(pLeftAnchor);
			}
			if (pTopAnchor == null && pBottomAnchor != null)
			{
				pTopAnchor = new Anchor(pBottomAnchor);
			}
			else if (pBottomAnchor == null && pTopAnchor != null)
			{
				pBottomAnchor = new Anchor(pTopAnchor);
			}
			setLeftAnchor(pLeftAnchor);
			setRightAnchor(pRightAnchor);
			setTopAnchor(pTopAnchor);
			setBottomAnchor(pBottomAnchor);
		}

		/**
		 * Constructs Constraints with the given anchors as bounds.
		 * @param pTopAnchor the left anchor.
		 * @param pLeftAnchor the left anchor.
		 */
		public Constraint(Anchor pTopAnchor, Anchor pLeftAnchor)
		{
			this(pTopAnchor, pLeftAnchor, null, null);
		}

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Overwritten methods
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        
        /**
         * {@inheritDoc}
         */
        @Override
        public String toString()
        {
            return "Constraint: " + topAnchor.name + ";" + leftAnchor.name + ";" + bottomAnchor.name + ";" + rightAnchor.name;
        }

		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// User-defined methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Returns the left anchor.
		 *
		 * @return the left anchor.
		 */
		public Anchor getLeftAnchor()
		{
			return leftAnchor;
		}

		/**
		 * Sets the left anchor.
		 *
		 * @param pLeftAnchor left to set
		 */
		public void setLeftAnchor(Anchor pLeftAnchor)
		{
			if (pLeftAnchor == null && rightAnchor != null)
			{
				leftAnchor = new Anchor(rightAnchor);
			}
			else if (pLeftAnchor.orientation == Anchor.VERTICAL)
			{
				throw new IllegalArgumentException("A vertical anchor can not be used as left anchor!");
			}
			else
			{
				leftAnchor = pLeftAnchor;
			}
		}

		/**
		 * Returns the right anchor.
		 *
		 * @return the right anchor.
		 */
		public Anchor getRightAnchor()
		{
			return rightAnchor;
		}

		/**
		 * Sets the right anchor.
		 *
		 * @param pRightAnchor the right anchor.
		 */
		public void setRightAnchor(Anchor pRightAnchor)
		{
			if (pRightAnchor == null && leftAnchor != null)
			{
				rightAnchor = new Anchor(leftAnchor);
			}
			else if (pRightAnchor.orientation == Anchor.VERTICAL)
			{
				throw new IllegalArgumentException("A vertical anchor can not be used as right anchor!");
			}
			else
			{
				rightAnchor = pRightAnchor;
			}
		}

		/**
		 * Returns the top anchor.
		 *
		 * @return the top anchor.
		 */
		public Anchor getTopAnchor()
		{
			return topAnchor;
		}

		/**
		 * Sets the top anchor.
		 *
		 * @param pTopAnchor the top anchor
		 */
		public void setTopAnchor(Anchor pTopAnchor)
		{
			if (pTopAnchor == null && bottomAnchor != null)
			{
				topAnchor = new Anchor(bottomAnchor);
			}
			else if (pTopAnchor.orientation == Anchor.HORIZONTAL)
			{
				throw new IllegalArgumentException("A horizontal anchor can not be used as top anchor!");
			}
			else
			{
				topAnchor = pTopAnchor;
			}
		}

		/**
		 * Returns the bottom anchor.
		 *
		 * @return the bottom anchor.
		 */
		public Anchor getBottomAnchor()
		{
			return bottomAnchor;
		}

		/**
		 * Sets the bottom anchor.
		 *
		 * @param pBottomAnchor the bottom to set
		 */
		public void setBottomAnchor(Anchor pBottomAnchor)
		{
			if (pBottomAnchor == null && topAnchor != null)
			{
				bottomAnchor = new Anchor(topAnchor);
			}
			else if (pBottomAnchor.orientation == Anchor.HORIZONTAL)
			{
				throw new IllegalArgumentException("A vertical anchor can not be used as bottom anchor!");
			}
			else
			{
				bottomAnchor = pBottomAnchor;
			}
		}

	}	// Constraint

}	// JVxFormLayout
