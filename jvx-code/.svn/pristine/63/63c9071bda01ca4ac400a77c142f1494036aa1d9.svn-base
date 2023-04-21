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
 * 07.03.2016 - [RZ] - creation
 * 24.04.2019 - [DJ] - #2015: performance tuning, measure only once
 */
package com.sibvisions.rad.ui.vaadin.ext.ui.client.panel.layout;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sibvisions.rad.ui.vaadin.ext.ui.client.panel.helper.Size;
import com.vaadin.client.ComponentConnector;
import com.vaadin.shared.Connector;

/**
 * The {@link FormLayout} allows to layout its children in a complex form like
 * fashion.
 * 
 * @author Robert Zenz
 */
public class FormLayout extends AbstractClientSideAlignedLayout
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The anchors. */
	private Map<String, Anchor> anchorsByName = new HashMap<String, Anchor>();
	
	/** Component-Constraint Map. */
	private Map<Connector, Constraints> connectorConstraints = new HashMap<Connector, Constraints>();
	
	/**
	 * The {@link Constraints} that contain the {@link Anchor}s used for the
	 * border.
	 */
	private Constraints borderAnchors;
	
	/**
	 * The {@link Constraints} that contain the {@link Anchor}s used for the
	 * margin.
	 */
	private Constraints marginAnchors;
	
	/** top is used. */
	private boolean topBorderUsed;
	
	/** left is used. */
	private boolean leftBorderUsed;
	
	/** bottom is used. */
	private boolean bottomBorderUsed;
	
	/** right is used. */
	private boolean rightBorderUsed;
	
	/** preferred width. */
	private int preferredWidth;
	
	/** preferred height. */
	private int preferredHeight;
	
	/** minimum width. */
	private int minimumWidth;
	
	/** minimum height. */
	private int minimumHeight;
	
	/** The size in layout path, to check with postLayout. */
	private Size cachedSize;

    /** True, if it is a scrollable container. */
    private boolean isScrollableContainer;
	/** The cached min size. */
    private Size minSize;
    /** The cached max size. */
    private Size maxSize;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link FormLayout}.
	 */
	public FormLayout()
	{
		super();
		
		horizontalAlignment = HorizontalAlignment.STRETCH;
		verticalAlignment = VerticalAlignment.STRETCH;
		
		borderAnchors = new Constraints(
				new Anchor(AnchorOrientation.VERTICAL),
				new Anchor(AnchorOrientation.HORIZONTAL),
				new Anchor(AnchorOrientation.VERTICAL),
				new Anchor(AnchorOrientation.HORIZONTAL));
		
		marginAnchors = new Constraints(
				new Anchor(borderAnchors.topAnchor, 10),
				new Anchor(borderAnchors.leftAnchor, 10),
				new Anchor(borderAnchors.bottomAnchor, -10),
				new Anchor(borderAnchors.rightAnchor, -10));
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void layoutComponents(boolean pFirstRun)
	{
		super.layoutComponents();
		
		if (pFirstRun)
		{
			cachedSize = parent.getPanelSize();
		}
		else
		{
			cachedSize = parent.getCanvasSize();
		}

		isScrollableContainer = isScrollableContainer();
		
		
		calculateAnchors();
		
        if (parent.getChildren().size() == 0)
        {
            cachedSize.width = preferredWidth;
            cachedSize.height = preferredHeight;
        }
        else if ((isInsideScrollableContainer() && pFirstRun) || isScrollableContainer)
		{
			// Now we will check if we are smaller than our preferred size, if yes,
			// we will resize ourselves to our preferred size and do a second layout
			// pass afterwards to make sure that we are at least our preferred size
			// big if we can get that big.
			if (cachedSize.width < preferredWidth)
			{
				cachedSize.width = preferredWidth;
			}
			
			if (cachedSize.height < preferredHeight)
			{
				cachedSize.height = preferredHeight;
			}
		}
		
		parent.setCanvasSize(cachedSize);
		updatePreferredSizeCache(preferredWidth, preferredHeight);

		calculateTargetDependentAnchors();

		resizeAndRelocateChildren();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Size getPreferredSize()
	{
		updateFromState();
		
		cachedSize = parent.getPanelSize();
		
		isScrollableContainer = isScrollableContainer();
		
		calculateAnchors();
		
		return new Size(preferredWidth, preferredHeight);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates an anchor from anchor data.
	 * 
	 * @param pAnchorData the anchor data.
	 * @return the anchor.
	 */
	protected Anchor getAnchor(String pAnchorData)
	{
		String name = pAnchorData.substring(0, pAnchorData.indexOf(','));
		
		if (name.equals("-"))
		{
			return null;
		}
		
		Anchor anchor = anchorsByName.get(name);
		if (anchor == null)
		{
			anchor = new Anchor(pAnchorData);
			
			anchorsByName.put(name, anchor);
		}
		else
		{
			anchor.setAnchorData(pAnchorData);
		}
		
		return anchor;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void updateFromState()
	{
		super.updateFromState();
		
		anchorsByName.clear();
		
		anchorsByName.put("t", borderAnchors.topAnchor);
		anchorsByName.put("l", borderAnchors.leftAnchor);
		anchorsByName.put("b", borderAnchors.bottomAnchor);
		anchorsByName.put("r", borderAnchors.rightAnchor);
		anchorsByName.put("tm", marginAnchors.topAnchor);
		anchorsByName.put("lm", marginAnchors.leftAnchor);
		anchorsByName.put("bm", marginAnchors.bottomAnchor);
		anchorsByName.put("rm", marginAnchors.rightAnchor);
		
		connectorConstraints.clear();
		
		for (String anchorData : parent.getLayoutData("anchors").split(";"))
		{
			getAnchor(anchorData);
		}
		
		for (ComponentConnector connector : parent.getChildComponents())
		{
			String anchorData = parent.getConstraint(connector);
			
			Constraints constraints = new Constraints(this, anchorData);
			
			connectorConstraints.put(connector, constraints);
		}
		
		for (Anchor anchor : anchorsByName.values())
		{
			anchor.parseAnchorData();
		}
	}
	
	/**
	 * Calculates all {@link Anchor}s.
	 */
	private void calculateAnchors()
	{
	    // init border anchors, to get hopefully correct label sizes.
        borderAnchors.leftAnchor.position = 0;
        borderAnchors.rightAnchor.position = 0;
        borderAnchors.topAnchor.position = cachedSize.width;
        borderAnchors.bottomAnchor.position = cachedSize.height;

        // Calculate PreferredSize before resetting the 
        clearAutoSizeAnchors();
        initAutoSizeAnchors();
        calculateAutoSizeAnchors();

        // reset border anchors
		borderAnchors.leftAnchor.position = 0;
		borderAnchors.rightAnchor.position = 0;
		borderAnchors.topAnchor.position = 0;
		borderAnchors.bottomAnchor.position = 0;
		
		// reset preferred size;
		preferredWidth = 0;
		preferredHeight = 0;
		
		// reset minimum size;
		minimumWidth = 0;
		minimumHeight = 0;
		
		calculateSizes();
		
        minSize = parent.getMinimumSize(parent.getParent());
        
		if (minSize != null && !isScrollableContainer)
		{
            if (minSize.width > preferredWidth)
            {
                preferredWidth = minSize.width;
            }
            if (minSize.height > preferredHeight)
            {
                preferredHeight = minSize.height;
            }
		}
		else
		{
		    minSize = new Size(minimumWidth, minimumHeight);
		}
        maxSize = parent.getMaximumSize(parent.getParent());

        if (maxSize != null && !isScrollableContainer)
        {
            if (maxSize.width < preferredWidth)
            {
                preferredWidth = maxSize.width;
            }
            if (maxSize.height < preferredHeight)
            {
                preferredHeight = maxSize.height;
            }
        }
        else
        {
            maxSize = new Size(Integer.MAX_VALUE, Integer.MAX_VALUE);
        }
	        

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
		
		if (size == pAutoSizeCount)
		{
			int fixedSize = pRightBottomAnchor.getAbsolutePosition() - pLeftTopAnchor.getAbsolutePosition();
			for (Anchor anchor : anchors)
			{
				fixedSize += anchor.position;
			}
			
			int diffSize = (pPreferredSize - fixedSize + size - 1) / size;
			for (Anchor anchor : anchors)
			{
				if (diffSize > -anchor.position)
				{
					anchor.position = -diffSize;
				}
				anchor.firstCalculation = false;
			}
		}
		
		anchors = getAutoSizeAnchorsBetween(pRightBottomAnchor, pLeftTopAnchor);
		size = anchors.size();
		
		if (size == pAutoSizeCount)
		{
			int fixedSize = pRightBottomAnchor.getAbsolutePosition() - pLeftTopAnchor.getAbsolutePosition();
			for (Anchor anchor : anchors)
			{
				fixedSize -= anchor.position;
			}
			
			int diffSize = (pPreferredSize - fixedSize + size - 1) / size;
			for (Anchor anchor : anchors)
			{
				if (diffSize > anchor.position)
				{
					anchor.position = diffSize;
				}
				anchor.firstCalculation = false;
			}
		}
	}
	
	/**
	 * Calculates all auto-size {@link Anchor}s.
	 */
	private void calculateAutoSizeAnchors()
	{
		int autoSizeCount = 1;
		
		do
		{
			calculateAutoSizeAnchors(autoSizeCount);
			
			autoSizeCount = finishAutoSizeCalculation();
		}
		while (autoSizeCount > 0 && autoSizeCount < Integer.MAX_VALUE);
	}
	
	/**
	 * Calculates all auto-size {@link Anchor}s.
	 * 
	 * @param pAutoSizeCount the auto size count.
	 */
	private void calculateAutoSizeAnchors(int pAutoSizeCount)
	{
		for (ComponentConnector connector : parent.getChildComponents())
		{
            Constraints constraint = connectorConstraints.get(connector);
            
//		    Size preferredSize = getPreferredSize(connector, constraint.rightAnchor.getAbsolutePosition() - constraint.leftAnchor.getAbsolutePosition());
            Size preferredSize = getPreferredSize(connector);

		    calculateAutoSize(constraint.leftAnchor, constraint.rightAnchor, preferredSize.width, pAutoSizeCount);
			calculateAutoSize(constraint.topAnchor, constraint.bottomAnchor, preferredSize.height, pAutoSizeCount);
		}
	}
	
	/**
	 * Calculates all relative {@link Anchor}s.
	 */
	private void calculateRelativeAnchors()
	{
		for (ComponentConnector connector : parent.getChildComponents())
		{
			Constraints constraint = connectorConstraints.get(connector);
			Size preferredSize = getPreferredSize(connector);
			
			calculateRelativeAnchor(constraint.leftAnchor, constraint.rightAnchor, preferredSize.width);
			calculateRelativeAnchor(constraint.topAnchor, constraint.bottomAnchor, preferredSize.height);
		}
	}
	
	/**
	 * Calculates the relative {@link Anchor}.
	 * 
	 * @param pLeftTopAnchor the left/top {@link Anchor}.
	 * @param pRightBottomAnchor the right/bottom {@link Anchor}.
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
	 * Calculates the size of the layout.
	 */
	private void calculateSizes()
	{
		leftBorderUsed = false;
		rightBorderUsed = false;
		topBorderUsed = false;
		bottomBorderUsed = false;
		
		int leftWidth = 0;
		int rightWidth = 0;
		int topHeight = 0;
		int bottomHeight = 0;
		
		for (ComponentConnector connector : parent.getChildComponents())
		{
			Constraints constraint = connectorConstraints.get(connector);
			
			Size preferredSize = getPreferredSize(connector);
			Size minimumSize = getMinimumSize(connector);
			
			if (constraint.rightAnchor.getBorderAnchor() == borderAnchors.leftAnchor)
			{
				int w = constraint.rightAnchor.getAbsolutePosition();
				if (w > leftWidth)
				{
					leftWidth = w;
				}
				leftBorderUsed = true;
			}
			
			if (constraint.leftAnchor.getBorderAnchor() == borderAnchors.rightAnchor)
			{
				int w = -constraint.leftAnchor.getAbsolutePosition();
				if (w > rightWidth)
				{
					rightWidth = w;
				}
				rightBorderUsed = true;
			}
			
			if (constraint.bottomAnchor.getBorderAnchor() == borderAnchors.topAnchor)
			{
				int h = constraint.bottomAnchor.getAbsolutePosition();
				if (h > topHeight)
				{
					topHeight = h;
				}
				topBorderUsed = true;
			}
			
			if (constraint.topAnchor.getBorderAnchor() == borderAnchors.bottomAnchor)
			{
				int h = -constraint.topAnchor.getAbsolutePosition();
				if (h > bottomHeight)
				{
					bottomHeight = h;
				}
				bottomBorderUsed = true;
			}
			
			if (constraint.leftAnchor.getBorderAnchor() == borderAnchors.leftAnchor && constraint.rightAnchor.getBorderAnchor() == borderAnchors.rightAnchor)
			{
                if (!constraint.leftAnchor.autoSize || !constraint.rightAnchor.autoSize)
                {
    				int w = constraint.leftAnchor.getAbsolutePosition() - constraint.rightAnchor.getAbsolutePosition() + preferredSize.width;
    				if (w > preferredWidth)
    				{
    					preferredWidth = w;
    				}
    				w = constraint.leftAnchor.getAbsolutePosition() - constraint.rightAnchor.getAbsolutePosition() + minimumSize.width;
    				if (w > minimumWidth)
    				{
    					minimumWidth = w;
    				}
                }
				leftBorderUsed = true;
				rightBorderUsed = true;
			}
			
			if (constraint.topAnchor.getBorderAnchor() == borderAnchors.topAnchor && constraint.bottomAnchor.getBorderAnchor() == borderAnchors.bottomAnchor)
			{
                if (!constraint.topAnchor.autoSize || !constraint.bottomAnchor.autoSize)
                {
    				int h = constraint.topAnchor.getAbsolutePosition() - constraint.bottomAnchor.getAbsolutePosition() + preferredSize.height;
    				if (h > preferredHeight)
    				{
    					preferredHeight = h;
    				}
    				h = constraint.topAnchor.getAbsolutePosition() - constraint.bottomAnchor.getAbsolutePosition() + minimumSize.height;
    				if (h > minimumHeight)
    				{
    					minimumHeight = h;
    				}
                }
				topBorderUsed = true;
				bottomBorderUsed = true;
			}
			
		}
		
		if (leftWidth != 0 && rightWidth != 0)
		{
			int w = leftWidth + rightWidth + horizontalGap;
			
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
			int w = leftWidth - marginAnchors.rightAnchor.position;
			
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
			int w = rightWidth + marginAnchors.leftAnchor.position;
			
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
            int w = marginAnchors.leftAnchor.position - marginAnchors.rightAnchor.position;
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
			int h = topHeight + bottomHeight + verticalGap;
			
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
			int h = topHeight - marginAnchors.bottomAnchor.position;
			
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
			int h = bottomHeight + marginAnchors.topAnchor.position;
			
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
            int h = marginAnchors.topAnchor.position - marginAnchors.bottomAnchor.position;
            if (h > preferredHeight)
            {
                preferredHeight = h;
            }
            if (h > minimumHeight)
            {
                minimumHeight = h;
            }
        }
	}
	
	/**
	 * Calculates all target dependent {@link Anchor}s.
	 */
	private void calculateTargetDependentAnchors()
	{
		// If we don't have a size, let us assume that we should be
		// our preferred size.
		if (cachedSize.width <= 0)
		{
			cachedSize.width = preferredWidth;
		}
		if (cachedSize.height <= 0)
		{
			cachedSize.height = preferredHeight;
		}
		
		if (horizontalAlignment == HorizontalAlignment.STRETCH || (leftBorderUsed && rightBorderUsed))
		{
			if (minSize.width > cachedSize.width)
			{
				borderAnchors.leftAnchor.position = 0;
				borderAnchors.rightAnchor.position = minSize.width;
			}
			else if (maxSize.width < cachedSize.width)
			{
				switch (horizontalAlignment)
				{
					case LEFT:
						borderAnchors.leftAnchor.position = 0;
						break;
					
					case RIGHT:
						borderAnchors.leftAnchor.position = cachedSize.width - maxSize.width;
						break;
					
					default:
						borderAnchors.leftAnchor.position = (cachedSize.width - maxSize.width) / 2;
						
				}
				borderAnchors.rightAnchor.position = borderAnchors.leftAnchor.position + maxSize.width;
			}
			else
			{
				borderAnchors.leftAnchor.position = 0;
				borderAnchors.rightAnchor.position = cachedSize.width;
				//				if (rightBorderUsed)
				//				{
				//					borderAnchors.rightAnchor.position = size.getWidth();
				//				}
				//				else
				//				{
				//					borderAnchors.rightAnchor.position = preferredWidth;
				//				}
			}
		}
		else
		{
			if (preferredWidth > cachedSize.width)
			{
				borderAnchors.leftAnchor.position = 0;
			}
			else
			{
				switch (horizontalAlignment)
				{
					case LEFT:
						borderAnchors.leftAnchor.position = 0;
						break;
					
					case RIGHT:
						borderAnchors.leftAnchor.position = cachedSize.width - preferredWidth;
						break;
					
					default:
						borderAnchors.leftAnchor.position = (cachedSize.width - preferredWidth) / 2;
						
				}
			}
			borderAnchors.rightAnchor.position = borderAnchors.leftAnchor.position + preferredWidth;
		}
		if (verticalAlignment == VerticalAlignment.STRETCH || (topBorderUsed && bottomBorderUsed))
		{
			if (minSize.height > cachedSize.height)
			{
				borderAnchors.topAnchor.position = 0;
				borderAnchors.bottomAnchor.position = minSize.height;
			}
			else if (maxSize.height < cachedSize.height)
			{
				switch (verticalAlignment)
				{
					case TOP:
						borderAnchors.topAnchor.position = 0;
						break;
					
					case BOTTOM:
						borderAnchors.topAnchor.position = cachedSize.height - maxSize.height;
						break;
					
					default:
						borderAnchors.topAnchor.position = (cachedSize.height - maxSize.height) / 2;
						
				}
				borderAnchors.bottomAnchor.position = borderAnchors.topAnchor.position + maxSize.height;
			}
			else
			{
				borderAnchors.topAnchor.position = 0;
				borderAnchors.bottomAnchor.position = cachedSize.height;
			}
		}
		else
		{
			if (preferredHeight > cachedSize.height)
			{
				borderAnchors.topAnchor.position = 0;
			}
			else
			{
				switch (verticalAlignment)
				{
					case TOP:
						borderAnchors.topAnchor.position = 0;
						break;
					
					case BOTTOM:
						borderAnchors.topAnchor.position = cachedSize.height - preferredHeight;
						break;
					
					default:
						borderAnchors.topAnchor.position = (cachedSize.height - preferredHeight) / 2;
						
				}
			}
			borderAnchors.bottomAnchor.position = borderAnchors.topAnchor.position + preferredHeight;
		}
		
		calculateRelativeAnchors();
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
                    && relatedAutoSizeAnchor != marginAnchors.leftAnchor
                    && relatedAutoSizeAnchor != marginAnchors.topAnchor
                    && relatedAutoSizeAnchor != marginAnchors.rightAnchor
                    && relatedAutoSizeAnchor != marginAnchors.bottomAnchor)
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
	 * Clears all auto-size {@link Anchor}s.
	 */
	private void clearAutoSizeAnchors()
	{
	    List<Anchor> horizontalAnchors = new ArrayList<Anchor>();
        List<Anchor> verticalAnchors = new ArrayList<Anchor>();
	    
	    for (ComponentConnector connector : parent.getChildComponents())
	    {
	        Constraints constraints = connectorConstraints.get(connector);

            clearAutoSize(horizontalAnchors, constraints.leftAnchor);
            clearAutoSize(horizontalAnchors, constraints.rightAnchor);
            clearAutoSize(verticalAnchors, constraints.topAnchor);
            clearAutoSize(verticalAnchors, constraints.bottomAnchor);
	    }
        for (Anchor anchor : horizontalAnchors)
        {
            initAutoSize(anchor);
        }
        for (Anchor anchor : verticalAnchors)
        {
            initAutoSize(anchor);
        }
	}
	
	/**
	 * Finishes the calculation of the auto-size {@link Anchor}s.
	 * 
	 * @return the count of remaining auto-size {@link Anchor}s.
	 */
	private int finishAutoSizeCalculation()
	{
		int autoSizeCount = Integer.MAX_VALUE;
		
		for (ComponentConnector connector : parent.getChildComponents())
		{
			Constraints constraints = connectorConstraints.get(connector);
			
			int counthelp = finishAutoSizeCalculation(constraints.leftAnchor, constraints.rightAnchor);
			
			if (counthelp > 0 && counthelp < autoSizeCount)
			{
				autoSizeCount = counthelp;
			}
			
			counthelp = finishAutoSizeCalculation(constraints.rightAnchor, constraints.leftAnchor);
			
			if (counthelp > 0 && counthelp < autoSizeCount)
			{
				autoSizeCount = counthelp;
			}
			
			counthelp = finishAutoSizeCalculation(constraints.topAnchor, constraints.bottomAnchor);
			
			if (counthelp > 0 && counthelp < autoSizeCount)
			{
				autoSizeCount = counthelp;
			}
			
			counthelp = finishAutoSizeCalculation(constraints.bottomAnchor, constraints.topAnchor);
			
			if (counthelp > 0 && counthelp < autoSizeCount)
			{
				autoSizeCount = counthelp;
			}
		}
		
		return autoSizeCount;
	}
	
	/**
	 * Marks all touched Autosize anchors as calculated.
	 * 
	 * @param pLeftTopAnchor the left or top anchor
	 * @param pRightBottomAnchor the right or bottom anchor
	 * @return amount of autosize anchors left.
	 */
	private int finishAutoSizeCalculation(Anchor pLeftTopAnchor, Anchor pRightBottomAnchor)
	{
		List<Anchor> anchors = getAutoSizeAnchorsBetween(pLeftTopAnchor, pRightBottomAnchor);
		int count = anchors.size();
		
		for (Anchor anchor : anchors)
		{
			if (!anchor.firstCalculation)
			{
				anchor.autoSizeCalculated = true;
				count--;
			}
		}
		return count;
	}
	
	/**
	 * Gets all auto-size {@link Anchor}s between the given two {@link Anchor}s.
	 * 
	 * @param pStartAnchor the start {@link Anchor}.
	 * @param pEndAnchor the end {@link Anchor}.
	 * @return all auto-size {@link Anchor}s between the given two.
	 */
	private List<Anchor> getAutoSizeAnchorsBetween(Anchor pStartAnchor, Anchor pEndAnchor)
	{
		List<Anchor> anchorsBuffer = new ArrayList<Anchor>();
		Anchor nextRelatedAnchor = pStartAnchor;
		
		while (nextRelatedAnchor != null && nextRelatedAnchor != pEndAnchor)
		{
			if (nextRelatedAnchor.autoSize && !nextRelatedAnchor.autoSizeCalculated)
			{
				anchorsBuffer.add(nextRelatedAnchor);
			}
			nextRelatedAnchor = nextRelatedAnchor.relatedAnchor;
		}
		if (nextRelatedAnchor == null)
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
		for (Anchor anchor : getAutoSizeAnchorsBetween(pStartAnchor, pEndAnchor))
		{
			anchor.relative = false;
		}
	}
	
	/**
	 * Initializes all auto-size {@link Anchor}s.
	 */
	private void initAutoSizeAnchors()
	{
		for (ComponentConnector connector : parent.getChildComponents())
		{
			Constraints constraints = connectorConstraints.get(connector);
			
			initAutoSize(constraints.leftAnchor, constraints.rightAnchor);
			initAutoSize(constraints.rightAnchor, constraints.leftAnchor);
			initAutoSize(constraints.topAnchor, constraints.bottomAnchor);
			initAutoSize(constraints.bottomAnchor, constraints.topAnchor);
		}
	}
	
	/**
	 * Resizes and relocates the children based on the calculated {@link Anchor}
	 * s.
	 */
	private void resizeAndRelocateChildren()
	{
		for (ComponentConnector connector : parent.getChildComponents())
		{
			Constraints constraint = connectorConstraints.get(connector);
			
			int x = constraint.leftAnchor.getAbsolutePosition();
			int y = constraint.topAnchor.getAbsolutePosition();
			int width = constraint.rightAnchor.getAbsolutePosition() - x;
			int height = constraint.bottomAnchor.getAbsolutePosition() - y;
			
			resizeRelocate(connector, x, y, width, height);
		}
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * The <code>WebAnchor</code> gives the possible horizontal and vertical
	 * positions.
	 * 
	 * @author Martin Handsteiner
	 */
	private class Anchor
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/** The anchor data. */
		private String anchorData;
		
		/** The related anchor to this anchor. */
		private Anchor relatedAnchor;
		
		/** true, if this anchor should be auto sized. */
		private boolean autoSize;
		
		/** true, if this anchor is a relative anchor. */
		private boolean relative;
		
		/** The position of this anchor. */
		private int position;
		
		/** If the first calculation has occurred. */
		private boolean firstCalculation;
		
		/** The {@link AnchorOrientation} of this Anchor. */
		private AnchorOrientation orientation = AnchorOrientation.HORIZONTAL;
		
		/** If this has been automatically sized. */
		private boolean autoSizeCalculated;
        /** True, if the anchor is used by a visible component. **/
        private boolean used;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Constructs the WebAnchor with correct name.
		 * 
		 * @param pAnchorData the anchor data.
		 */
		public Anchor(String pAnchorData)
		{
			setAnchorData(pAnchorData);
		}
		
		/**
		 * Constructs the WebAnchor with correct name.
		 * 
		 * @param pRelatedAnchor the related anchor.
		 * @param pPosition the position.
		 */
		public Anchor(Anchor pRelatedAnchor, int pPosition)
		{
			relatedAnchor = pRelatedAnchor;
			autoSize = false;
			position = pPosition;
			orientation = relatedAnchor.orientation;
		}
		
		/**
		 * Creates a new instance of {@link Anchor}.
		 * 
		 * @param pOrientation the {@link AnchorOrientation orientation}.
		 */
		public Anchor(AnchorOrientation pOrientation)
		{
			orientation = pOrientation;
			relatedAnchor = null;
			autoSize = false;
			position = 0;
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// User-defined methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Constructs the WebAnchor with correct name.
		 * 
		 * @param pAnchorData the anchor data.
		 */
		public void setAnchorData(String pAnchorData)
		{
			anchorData = pAnchorData;
		}
		
		/**
		 * Parses the anchor data.
		 */
		public void parseAnchorData()
		{
			if (anchorData != null)
			{
				String[] splittedData = anchorData.split(",");
				
				String relatedAnchorName = splittedData[1];
				if (!relatedAnchorName.equals("-"))
				{
					relatedAnchor = anchorsByName.get(relatedAnchorName);
				}
				else
				{
					relatedAnchor = null;
				}
				
				autoSize = Boolean.parseBoolean(splittedData[3]);
				position = new BigDecimal(splittedData[4]).intValue();
				
				String orientationName = splittedData[6];
				if (orientationName.equals("H"))
				{
					orientation = AnchorOrientation.HORIZONTAL;
				}
				else
				{
					orientation = AnchorOrientation.VERTICAL;
				}
			}
		}
		
		/**
		 * Gets the anchors absolute position.
		 * 
		 * @return the position
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
		 * Gets the {@link Anchor} that is the border of the layout.
		 * 
		 * @return the {@link Anchor} that is the border of the layout.
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
	 * The orientation of the anchors.
	 * 
	 * @author Robert Zenz
	 */
	private enum AnchorOrientation
	{
		/** Column. */
		HORIZONTAL,
		
		/** Rows. */
		VERTICAL
		
	}	// AnchorOrientation
	
	/**
	 * The <code>Constraint</code> for the GVxFormLayout.
	 * 
	 * @author Martin Handsteiner
	 */
	private static class Constraints implements Serializable
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
		 * Constructs a new instance of <code>Constraint</code>.
		 * 
		 * @param pLayout the layout.
		 * @param pConstraintData the constraint data.
		 */
		public Constraints(FormLayout pLayout, String pConstraintData)
		{
			String[] constraint = pConstraintData.split(";");
			
			topAnchor = pLayout.anchorsByName.get(constraint[0]);
			leftAnchor = pLayout.anchorsByName.get(constraint[1]);
			bottomAnchor = pLayout.anchorsByName.get(constraint[2]);
			rightAnchor = pLayout.anchorsByName.get(constraint[3]);
		}
		
		/**
		 * Creates a new instance of {@link Constraints}.
		 *
		 * @param pTop the {@link Anchor top anchor}.
		 * @param pLeft the {@link Anchor left anchor}.
		 * @param pBottom the {@link Anchor bottom anchor}.
		 * @param pRight the {@link Anchor right anchor}.
		 */
		public Constraints(Anchor pTop, Anchor pLeft, Anchor pBottom, Anchor pRight)
		{
			topAnchor = pTop;
			leftAnchor = pLeft;
			bottomAnchor = pBottom;
			rightAnchor = pRight;
		}
		
	}	// Constraints
	
}
