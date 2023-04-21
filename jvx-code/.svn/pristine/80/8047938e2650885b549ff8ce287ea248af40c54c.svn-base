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
package com.sibvisions.rad.ui.vaadin.ext.ui.client.panel.layout;

import com.sibvisions.rad.ui.vaadin.ext.ui.client.panel.helper.Margins;
import com.sibvisions.rad.ui.vaadin.ext.ui.client.panel.helper.PaddedRectangle;
import com.sibvisions.rad.ui.vaadin.ext.ui.client.panel.helper.Size;
import com.vaadin.client.ComponentConnector;

/**
 * The {@link GridLayout} allows to lay out its components in a grid.
 * 
 * @author Robert Zenz
 */
public class GridLayout extends AbstractClientSideGapLayout
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The number of columns. */
	private int columns = 0;
	
	/** THe number of rows. */
	private int rows = 0;
	
	/** cache for x-coordinates. */
    private int[] xPosition = null;

    /** cache for y-coordinates. */
    private int[] yPosition = null;

    /** The target columns. */
    private int targetColumns;
    /** The target rows. */
    private int targetRows;

    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link GridLayout}.
	 */
	public GridLayout()
	{
		super();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Size getPreferredSize()
	{
		updateFromState();
		
		int maxWidth = 0;
		int maxHeight = 0;
		
		targetColumns = columns;
		targetRows = rows;

		for (ComponentConnector connector : parent.getChildComponents())
		{
			String constraintAsString = parent.getConstraint(connector);
			
			if (constraintAsString != null)
			{
				PaddedRectangle constraint = new PaddedRectangle(constraintAsString);
				
				int gridX = constraint.x;
				int gridY = constraint.y;
				int gridWidth = constraint.width;
                int gridHeight = constraint.height;
                Margins insets = constraint.padding;
				
				Size preferredSize = getPreferredSize(connector);
				
				int width = (preferredSize.width + gridWidth - 1) / gridWidth + insets.getHorizontal();
				if (width > maxWidth)
				{
				    maxWidth = width;
				}
				int height = (preferredSize.height + gridHeight - 1) / gridHeight + insets.getVertical();
				if (height > maxHeight)
				{
				    maxHeight = height;
				}
                
                if (columns <= 0 && gridX + gridWidth > targetColumns)
                {
                    targetColumns = gridX + gridWidth;
                }
                if (rows <= 0 && gridY + gridHeight > targetRows)
                {
                    targetRows = gridY + gridHeight;
                }
			}
		}
		
        return limitPreferredSize(parent.getParent(), 
                maxWidth * targetColumns + margins.getHorizontal() + horizontalGap * (targetColumns - 1), 
                maxHeight * targetRows + margins.getVertical() + verticalGap * (targetRows - 1));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void layoutComponents(boolean pFirstRun)
	{
		super.layoutComponents();
		
		Size panelSize;
		if (pFirstRun)
		{
			panelSize = parent.getPanelSize();
		}
		else
		{
			panelSize = parent.getCanvasSize();
		}
		
        Size preferredSize = getPreferredSize();
        
		// if parent is an scroll panel, we have to add preferredSize of center connector 
		if (parent.getChildren().size() == 0)
        {
		    panelSize.width = preferredSize.width;
		    panelSize.height = preferredSize.height;
        }
        else if ((isInsideScrollableContainer() && pFirstRun) || isScrollableContainer())
		{
			if (panelSize.width < preferredSize.width)
			{
			    panelSize.width = preferredSize.width;
			}
			if (panelSize.height < preferredSize.height)
			{
			    panelSize.height = preferredSize.height;
			}
		}
		
		updatePreferredSizeCache(preferredSize.width, preferredSize.height);
		
        int maxWidth = panelSize.width;
        int maxHeight = panelSize.height;

        if (targetColumns > 0 && targetRows > 0)
        {
            int leftInsets = margins.left;
            int topInsets = margins.top;
            
            int totalGapsWidth = (targetColumns - 1) * horizontalGap;
            int totalGapsHeight = (targetRows - 1) * verticalGap;
            
            int totalWidth = maxWidth - leftInsets - margins.right - totalGapsWidth;
            int totalHeight = maxHeight - topInsets - margins.bottom - totalGapsHeight;
            
            int columnSize = totalWidth / targetColumns;
            int rowSize = totalHeight / targetRows;

            int widthCalcError = totalWidth - columnSize * targetColumns;
            int heightCalcError = totalHeight - rowSize * targetRows;
            int xMiddle = 0;
            if (widthCalcError > 0)
            {
                xMiddle = (targetColumns / widthCalcError + 1) / 2;
            }
            int yMiddle = 0;
            if (heightCalcError > 0)
            {
                yMiddle = (targetRows / heightCalcError + 1) / 2;
            }
            
            if (xPosition == null || xPosition.length != targetColumns + 1)
            {
                xPosition = new int[targetColumns + 1];
            }
            xPosition[0] = leftInsets;
            int corrX = 0;
            for (int i = 0; i < targetColumns; i++)
            {
                xPosition[i + 1] = xPosition[i] + columnSize + horizontalGap;
                if (widthCalcError > 0 && corrX * targetColumns / widthCalcError + xMiddle == i) 
                {
                    xPosition[i + 1]++;
                    corrX++;
                }
            }
            if (yPosition == null || yPosition.length != targetRows + 1)
            {
                yPosition = new int[targetRows + 1];
            }
            yPosition[0] = topInsets;
            int corrY = 0;
            for (int i = 0; i < targetRows; i++)
            {
                yPosition[i + 1] = yPosition[i] + rowSize + verticalGap;
                if (heightCalcError > 0 && corrY * targetRows / heightCalcError + yMiddle == i) 
                {
                    yPosition[i + 1]++;
                    corrY++;
                }
            }

    		for (ComponentConnector connector : parent.getChildComponents())
    		{
    			String constraintAsString = parent.getConstraint(connector);
    			
    			if (constraintAsString != null)
    			{
    				PaddedRectangle constraint = new PaddedRectangle(constraintAsString);
    				
                    int gridX = constraint.x;
                    int gridY = constraint.y;
                    int gridWidth = constraint.width;
                    int gridHeight = constraint.height;
                    Margins insets = constraint.padding;

                    int x = getPosition(xPosition, gridX, columnSize, horizontalGap) + insets.left;
                    int y = getPosition(yPosition, gridY, rowSize, verticalGap) + insets.top;
                    int width = getPosition(xPosition, gridX + gridWidth, columnSize, horizontalGap) - horizontalGap - x - insets.right;
                    int height = getPosition(yPosition, gridY + gridHeight, rowSize, verticalGap) - verticalGap - y - insets.bottom;
    
    				resizeRelocate(connector, x, y, width, height);
    				
    				if (x + width > maxWidth)
    				{
    					maxWidth = x + width;
    				}
    				if (y + height > maxHeight)
    				{
    					maxHeight = y + height;
    				}
    			}
    		}
        }

        parent.setCanvasSize(new Size(maxWidth, maxHeight));
	}
	
	/**
     * Gets in any case an position inside the grid.
     * @param pPositions the stored positions.
     * @param pIndex the index
     * @param pSize the size of column or row
     * @param pGap the gap
     * @return the position
     */
    private static int getPosition(int[] pPositions, int pIndex, int pSize, int pGap)
    {
        if (pIndex < 0)
        {
            return pPositions[0] + pIndex * (pSize + pGap);
        }
        else if (pIndex >= pPositions.length)
        {
            return pPositions[pPositions.length - 1] + (pIndex - pPositions.length + 1) * (pSize + pGap);
        }
        else
        {
            return pPositions[pIndex];
        }
    }
    
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void updateFromState()
	{
		super.updateFromState();
		
		columns = Integer.parseInt(parent.getLayoutData("columns"));
		rows = Integer.parseInt(parent.getLayoutData("rows"));
	}
	
}	// GridLayout
