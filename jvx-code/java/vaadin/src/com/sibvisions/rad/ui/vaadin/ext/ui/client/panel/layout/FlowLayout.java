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
 */
package com.sibvisions.rad.ui.vaadin.ext.ui.client.panel.layout;

import com.sibvisions.rad.ui.vaadin.ext.ui.client.panel.helper.Size;
import com.vaadin.client.ComponentConnector;

/**
 * The {@link FlowLayout} allows to lay out its components in a flow.
 * 
 * @author Robert Zenz
 */
public class FlowLayout extends AbstractClientSideAlignedLayout
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** If the elements should automatically wrap. */
	private boolean autoWrap = false;

	/** The {@link HorizontalAlignment}. */
	protected HorizontalAlignment horizontalComponentAlignment = HorizontalAlignment.CENTER;
	
	/** The {@link VerticalAlignment}. */
	protected VerticalAlignment verticalComponentAlignment = VerticalAlignment.CENTER;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link FlowLayout}.
	 */
	public FlowLayout()
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
		
		LayoutInformation layoutInformation = calculateGrid();
		
		return limitPreferredSize(parent.getParent(), 
				(layoutInformation.width * layoutInformation.columns) + margins.getHorizontal() + horizontalGap * (layoutInformation.columns - 1),
				(layoutInformation.height * layoutInformation.rows) + margins.getVertical() + verticalGap * (layoutInformation.rows - 1));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void layoutComponents(boolean pFirstRun)
	{
		super.layoutComponents();
		
		LayoutInformation rectCompInfo = calculateGrid();
		
		//ignore the insets!
		Size dimPref = new Size(
				rectCompInfo.width * rectCompInfo.columns + horizontalGap * (rectCompInfo.columns - 1),
				rectCompInfo.height * rectCompInfo.rows + verticalGap * (rectCompInfo.rows - 1));
		
		Size canvasSize;
		if (pFirstRun)
		{
			canvasSize = parent.getPanelSize();
		}
		else
		{
			canvasSize = parent.getCanvasSize();
		}

        Size preferredSize = new Size(dimPref.width + margins.getHorizontal(), dimPref.height + margins.getVertical());
		if (parent.getChildren().size() == 0)
        {
		    canvasSize.width = preferredSize.width;
		    canvasSize.height = preferredSize.height;
        }
        else if ((isInsideScrollableContainer() && pFirstRun) || isScrollableContainer())
		{
			if (canvasSize.width < preferredSize.width)
			{
				canvasSize.width = preferredSize.width;
			}
			
			if (canvasSize.height < preferredSize.height)
			{
				canvasSize.height = preferredSize.height;
			}
		}
        
        parent.setCanvasSize(canvasSize);
        updatePreferredSizeCache(preferredSize.width, preferredSize.height);
		
		Size dimSize = new Size(canvasSize.width - margins.getHorizontal(), canvasSize.height - margins.getVertical());
		
		int iLeft;
		int iWidth;
		
		if (horizontalAlignment == HorizontalAlignment.STRETCH)
		{
			iLeft = margins.left;
			iWidth = dimSize.width;
		}
		else
		{
			//align the layout in the container
			iLeft = (int)((dimSize.width - dimPref.width) * getAlignmentFactor(horizontalAlignment)) + margins.left;
			iWidth = dimPref.width;
		}
		
		int iTop;
		int iHeight;
		
		if (verticalAlignment == VerticalAlignment.STRETCH)
		{
			iTop = margins.top;
			iHeight = dimSize.height;
		}
		else
		{
			//align the layout in the container
			iTop = (int)((dimSize.height - dimPref.height) * getAlignmentFactor(verticalAlignment)) + margins.top;
			iHeight = dimPref.height;
		}
		
		int fW = Math.max(1, iWidth);
		int fPW = Math.max(1, dimPref.width);
		int fH = Math.max(1, iHeight);
		int fPH = Math.max(1, dimPref.height);
		int x = 0;
		int y = 0;
		
		boolean bFirst = true;
		for (ComponentConnector connector : parent.getChildComponents())
		{
			Size size = getPreferredSize(connector);
			
			if (orientation == Orientation.HORIZONTAL)
			{
				if (!bFirst
						&& autoWrap
						&& dimSize.width > 0
						&& x + size.width > dimSize.width)
				{
					x = 0;
					y += (rectCompInfo.height + verticalGap) * fH / fPH;
				}
				else if (bFirst)
				{
					bFirst = false;
				}
				
				if (verticalComponentAlignment == VerticalAlignment.STRETCH)
				{
					resizeRelocate(
							connector,
                            iLeft + x * fW / fPW,
							iTop + y,
							size.width * fW / fPW,
							rectCompInfo.height * fH / fPH);
				}
				else
				{
					resizeRelocate(
							connector,
                            iLeft + x * fW / fPW,
							iTop + y + (int)((rectCompInfo.height - size.height) * getAlignmentFactor(verticalComponentAlignment)) * fH / fPH,
							size.width * fW / fPW,
							size.height * fH / fPH);
				}
				
				x += size.width + horizontalGap;
			}
			else
			{
				if (!bFirst
						&& autoWrap
						&& dimSize.height > 0
						&& y + size.height > dimSize.height)
				{
					y = 0;
					x += (rectCompInfo.width + horizontalGap) * fW / fPW;
				}
				else if (bFirst)
				{
					bFirst = false;
				}
				
				if (horizontalComponentAlignment == HorizontalAlignment.STRETCH)
				{
					resizeRelocate(
							connector,
                            iLeft + x,
							iTop + y * fH / fPH,
							rectCompInfo.width * fW / fPW,
							size.height * fH / fPH);
				}
				else
				{
					resizeRelocate(
							connector,
                            iLeft + x + (int)((rectCompInfo.width - size.width) * getAlignmentFactor(horizontalComponentAlignment)) * fW / fPW,
							iTop + y * fH / fPH,
							size.width * fW / fPW,
							size.height * fH / fPH);
				}
				
				y += size.height + verticalGap;
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void updateFromState()
	{
		super.updateFromState();
		
		autoWrap = Boolean.parseBoolean(parent.getLayoutData("autoWrap"));
		horizontalComponentAlignment = HorizontalAlignment.getAlignment(parent.getLayoutData("horizontalComponentAlignment"));
		verticalComponentAlignment = VerticalAlignment.getAlignment(parent.getLayoutData("verticalComponentAlignment"));
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Calculates the width, height, row and column count for the current
	 * components of a container.
	 * 
	 * @return the {@link LayoutInformation}.
	 */
	private LayoutInformation calculateGrid()
	{
		int iWidth = 0;
		int iHeight = 0;
		
		int iCalcWidth = 0;
		int iCalcHeight = 0;
		
		int iAnzRows = 1;
		int iAnzCols = 1;
		
		Size bounds = new Size(
				parent.getPanelSize().width,
				parent.getPanelSize().height);
		
		bounds.width = bounds.width - margins.getHorizontal();
		bounds.height = bounds.height - margins.getVertical();
		
		//needed because the visible state of the component will be checked!
		boolean bFirst = true;
		
		for (ComponentConnector connector : parent.getChildComponents())
		{
			Size dimPref = getPreferredSize(connector);
			
			if (orientation == Orientation.HORIZONTAL)
			{
				if (!bFirst)
				{
					iCalcWidth += horizontalGap;
				}
				
				iCalcWidth += dimPref.width;
				iHeight = Math.max(iHeight, dimPref.height);
				
				//wrapping doesn't change the height, because the height will be used
				//for all rows
				if (!bFirst
						&& autoWrap
						&& bounds.width > 0
						&& iCalcWidth > bounds.width)
				{
					iCalcWidth = dimPref.width;
					iAnzRows++;
				}
				else if (bFirst)
				{
					bFirst = false;
				}
				
				iWidth = Math.max(iWidth, iCalcWidth);
			}
			else
			{
				if (!bFirst)
				{
					iCalcHeight += verticalGap;
				}
				
				iWidth = Math.max(iWidth, dimPref.width);
				iCalcHeight += dimPref.height;
				
				//wrapping doesn't change the width, because the width will be used
				//for all columns
				if (!bFirst
						&& autoWrap
						&& bounds.height > 0
						&& iCalcHeight > bounds.height)
				{
					iCalcHeight = dimPref.height;
					iAnzCols++;
				}
				else if (bFirst)
				{
					bFirst = false;
				}
				
				iHeight = Math.max(iHeight, iCalcHeight);
			}
		}
		
		return new LayoutInformation(iAnzCols, iAnzRows, iWidth, iHeight);
	}
	
	/**
	 * Gets the factor for an alignment value. The factor will be used to align
	 * the components in the layout.
	 * 
	 * @param pHorizontalAlignment the {@link HorizontalAlignment horizontal
	 *            alignment}.
	 * @return the alignment factor.
	 */
	private float getAlignmentFactor(HorizontalAlignment pHorizontalAlignment)
	{
		if (pHorizontalAlignment == HorizontalAlignment.CENTER)
		{
			return 0.5f;
		}
		else if (pHorizontalAlignment == HorizontalAlignment.RIGHT)
		{
			return 1.0f;
		}
		else
		{
			return 0.0f;
		}
	}
	
	/**
	 * Gets the factor for an alignment value. The factor will be used to align
	 * the components in the layout.
	 * 
	 * @param pVerticalAlignment the {@link VerticalAlignment vertical
	 *            alignment}.
	 * @return the alignment factor.
	 */
	private float getAlignmentFactor(VerticalAlignment pVerticalAlignment)
	{
		if (pVerticalAlignment == VerticalAlignment.CENTER)
		{
			return 0.5f;
		}
		else if (pVerticalAlignment == VerticalAlignment.BOTTOM)
		{
			return 1.0f;
		}
		else
		{
			return 0.0f;
		}
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * Holds information about the layout.
	 * 
	 * @author Robert Zenz
	 */
	private static final class LayoutInformation
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/** The number of columns. */
		private int columns;
		
		/** The height. */
		private int height;
		
		/** The number of rows. */
		private int rows;
		
		/** The width. */
		private int width;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of {@link LayoutInformation}.
		 *
		 * @param pColumns the number of columns.
		 * @param pRows the number of rows.
		 * @param pWidth the width.
		 * @param pHeight the height.
		 */
		public LayoutInformation(int pColumns, int pRows, int pWidth, int pHeight)
		{
			columns = pColumns;
			rows = pRows;
			width = pWidth;
			height = pHeight;
		}
		
	}	// LayoutInformation
	
}	// FlowLayout
