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
 * 17.11.2008 - [JR] - creation
 */
package com.sibvisions.rad.ui.swing.ext.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;

import javax.swing.JViewport;
import javax.swing.SwingUtilities;

import com.sibvisions.rad.ui.swing.ext.JVxConstants;
import com.sibvisions.rad.ui.swing.ext.JVxUtil;

/**
 * The <code>JVxSequenceLayout</code> can be used as {@link java.awt.FlowLayout} with
 * additional features. The additional features are:
 * <ul>
 *   <li>stretch all components to the maximum size of the greatest component</li>
 *   <li>en-/disable wrapping when the width/height changes</li>
 *   <li>margins</li>
 * </ul>
 *
 * @author René Jahn
 */
public class JVxSequenceLayout implements LayoutManager,
									      JVxConstants
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the layout margins. */
	private Insets			insMargins 				= new Insets(0, 0, 0, 0);
	
	/** the component orientation: {@link JVxConstants#HORIZONTAL} or {@link JVxConstants#VERTICAL}. */
	private int				iOrientation;

	/** the horizontal gap between components. */
	private int				iHorizontalGap;

	/** the vertical gap between components. */
	private int				iVerticalGap;

	/** the x-axis alignment (default: {@link JVxConstants#CENTER}). */
	private int				iHorizontalAlignment			= CENTER;

	/** the y-axis alignment (default: {@link JVxConstants#CENTER}). */
	private int				iVerticalAlignment				= CENTER;

	/** 
	 * the x-axis alignment for components relative to the other components, if
	 * the have another width. (default: {@link JVxConstants#CENTER}).
	 */
	private int				iHorizontalComponentAlignment	= CENTER;

	/** 
	 * the x-axis alignment for components relative to the other components, if
	 * the have another height. (default: {@link JVxConstants#CENTER}).
	 */
	private int				iVerticalComponentAlignment	= CENTER;

	/** 
	 * the mark to wrap the layout if there is not enough space to show 
	 * all components (FlowLayout mode).
	 */
	private boolean			bAutoWrap				= false;
	
	/** The last preferredSize calculation. */
	private Rectangle       lastPreferredSizeCalculation = null;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>JVxSequenceLayout</code> with {@link JVxConstants#HORIZONTAL}
	 * orientation and 5 pixels vertical and horizontal gap.
	 */
	public JVxSequenceLayout()
	{
		this(HORIZONTAL, 5, 5);
	}

	/**
	 * Creates a new instance of <code>JVxSequenceLayout</code> with the desired orientation
	 * and 5 pixels vertical and horizontal gap.
	 * 
	 * @param pOrientation the desired orientation
	 */
	public JVxSequenceLayout(int pOrientation)
	{
		this(pOrientation, 5, 5);
	}

	/**
	 * Creates a new instance of <code>JVxSequenceLayout</code> with the desired orientation
	 * and gaps.
	 * 
	 * @param pOrientation the desired orientation
	 * @param pHorizontalGap the horizontal gap
	 * @param pVerticalGap the vertical gap
	 */
	public JVxSequenceLayout(int pOrientation, int pHorizontalGap, int pVerticalGap)
	{
		iOrientation = pOrientation;
		iHorizontalGap = pHorizontalGap;
		iVerticalGap = pVerticalGap;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public void addLayoutComponent(String name, Component comp)
	{
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeLayoutComponent(Component comp)
	{
	}

	/**
	 * {@inheritDoc}
	 */
	public Dimension preferredLayoutSize(Container pContainer)
	{
		Insets insets = pContainer.getInsets();
		
		//x stores the columns
		//y stores the rows
		Rectangle rectCompInfo = calculateGrid(pContainer);
		lastPreferredSizeCalculation = rectCompInfo;

		return new Dimension(rectCompInfo.width * rectCompInfo.x + iHorizontalGap * (rectCompInfo.x - 1) + insets.left + insets.right + insMargins.left + insMargins.right, 
				             rectCompInfo.height * rectCompInfo.y + iVerticalGap * (rectCompInfo.y - 1) + insets.top + insets.bottom + insMargins.top + insMargins.bottom);
	}

	/**
	 * Gets the minimum layout size.
	 * 
	 * @param target the container
	 * @return a new dimension with a width and height of 0
	 */
	public Dimension minimumLayoutSize(Container target)
	{
		return new Dimension(0, 0);
	}

	/**
	 * {@inheritDoc}
	 */
	public void layoutContainer(final Container pContainer)
	{
		Insets insets = pContainer.getInsets();
		Dimension dimSize = pContainer.getSize();
		
		dimSize.width -= insets.left + insets.right + insMargins.left + insMargins.right;
		dimSize.height -= insets.top + insets.bottom + insMargins.top + insMargins.bottom;
		
		//x stores the columns
		//y stores the rows
		Rectangle rectCompInfo = calculateGrid(pContainer);
		if (bAutoWrap && lastPreferredSizeCalculation != null)
		{
            if ((iOrientation == HORIZONTAL && lastPreferredSizeCalculation.y != rectCompInfo.y)
                    || (iOrientation == VERTICAL && lastPreferredSizeCalculation.x != rectCompInfo.x))
            {
                SwingUtilities.invokeLater(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        pContainer.invalidate();
                        JVxUtil.revalidateAll(pContainer);
                    }
                });
            }
		}

		//ignore the insets!
		Dimension dimPref = new Dimension(rectCompInfo.width * rectCompInfo.x + iHorizontalGap * (rectCompInfo.x - 1), 
				                          rectCompInfo.height * rectCompInfo.y + iVerticalGap * (rectCompInfo.y - 1));
		
		int iLeft;
		int iWidth;
		
		if (iHorizontalAlignment == STRETCH)
		{
			iLeft = insets.left + insMargins.left;
			iWidth = dimSize.width;
		}
		else
		{
			//align the layout in the container
			iLeft = (int)((dimSize.width - dimPref.width) * getAlignmentFactor(iHorizontalAlignment)) + insets.left + insMargins.left;
			iWidth = dimPref.width;
		}
		
		int iTop;
		int iHeight;
		
		if (iVerticalAlignment == STRETCH)
		{
			iTop = insets.top + insMargins.top;
			iHeight = dimSize.height;
		}
		else
		{
			//align the layout in the container
			iTop = (int)((dimSize.height - dimPref.height) * getAlignmentFactor(iVerticalAlignment)) + insets.top + insMargins.top;
			iHeight = dimPref.height;
		}
		
		int fW = Math.max(1, iWidth);
		int fPW = Math.max(1, dimPref.width);
		int fH = Math.max(1, iHeight);
		int fPH = Math.max(1, dimPref.height);
		int x = 0;
		int y = 0;
		
		Component comp;
		
		boolean bFirst = true;
		for (int i = 0, anz = pContainer.getComponentCount(); i < anz; i++)
		{
			comp = pContainer.getComponent(i);
			
			if (comp.isVisible())
			{
				Dimension size = JVxUtil.getPreferredSize(comp);
				
				if (iOrientation == HORIZONTAL)
				{
					if (!bFirst 
						&& bAutoWrap 
						&& dimSize.width > 0 
						&& x + size.width > dimSize.width)
					{
						x = 0;
						y += (rectCompInfo.height + iVerticalGap) * fH / fPH;
					}
					else if (bFirst)
					{
						bFirst = false;
					}
					
					if (iVerticalComponentAlignment == STRETCH)
					{
						comp.setBounds(iLeft + x * fW / fPW, 
								       iTop + y, 
								       size.width * fW / fPW, 
								       rectCompInfo.height * fH / fPH);
					}
					else
					{
						comp.setBounds(iLeft + x * fW / fPW, 
								       iTop + y + (int)((rectCompInfo.height - size.height) * getAlignmentFactor(iVerticalComponentAlignment)) * fH / fPH, 
								       size.width * fW / fPW, 
								       size.height * fH / fPH);
					}
					
					x += size.width + iHorizontalGap;
				}
				else
				{
					if (!bFirst 
						&& bAutoWrap
						&& dimSize.height > 0 
						&& y + size.height > dimSize.height)
					{
						y = 0;
						x += (rectCompInfo.width + iHorizontalGap) * fW / fPW;
					}
					else if (bFirst)
					{
						bFirst = false;
					}
					
					if (iHorizontalComponentAlignment == STRETCH)
					{
						comp.setBounds(iLeft + x, 
									   iTop + y * fH / fPH, 
									   rectCompInfo.width * fW / fPW, 
									   size.height * fH / fPH);
					}
					else
					{
						comp.setBounds(iLeft + x + (int)((rectCompInfo.width - size.width) * getAlignmentFactor(iHorizontalComponentAlignment)) * fW / fPW, 
								       iTop + y * fH / fPH, 
								       size.width * fW / fPW, 
								       size.height * fH / fPH);
					}
					
					y += size.height + iVerticalGap;
				}
			}
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Sets the orientation of the components in the "layout".
	 * 
	 * @param pOrientation the orientation {@link JVxConstants#HORIZONTAL} or
	 *                     {@link JVxConstants#VERTICAL}
	 */
	public void setOrientation(int pOrientation)
	{
		iOrientation = pOrientation;
	}

	/**
	 * Gets the component orientation.
	 * 
	 * @return {@link JVxConstants#HORIZONTAL} or {@link JVxConstants#VERTICAL}
	 */
	public int getOrientation()
	{
		return iOrientation;
	}

	/**
	 * Sets horizontal gap between components.
	 *  
	 * @param pGap the gap in pixel
	 */
	public void setHorizontalGap(int pGap)
	{
		iHorizontalGap = pGap;
	}

	/**
	 * Gets the horizontal gap between components.
	 * 
	 * @return the gap in pixel
	 */
	public int getHorizontalGap()
	{
		return iHorizontalGap;
	}

	/**
	 * Sets the vertical gap between components.
	 * 
	 * @param pGap the gap in pixel
	 */
	public void setVerticalGap(int pGap)
	{
		iVerticalGap = pGap;
	}

	/**
	 * Gets the vertical gap between components.
	 * 
	 * @return the gap in pixel
	 */
	public int getVerticalGap()
	{
		return iVerticalGap;
	}

	/**
	 * Sets the automatic wrap mode. This means that the layout acts like
	 * the {@link java.awt.FlowLayout}.
	 * 
	 * @param pAutoWrap <code>true</code> to enabled the auto wrap mode; <code>false</code> otherwise
	 */
	public void setAutoWrap(boolean pAutoWrap)
	{
		bAutoWrap = pAutoWrap;
	}

	/**
	 * Gets the current state of the automatic wrap mode.
	 * 
	 * @return <code>true</code> if the automatic wrap mode is enabled; otherwise <code>false</code>
	 */
	public boolean isAutoWrap()
	{
		return bAutoWrap;
	}

	/**
	 * Sets the alignment for the x axis. 
	 * 
	 * @param pHorizontalAlignment the alignment {@link JVxConstants#LEFT}, {@link JVxConstants#CENTER},
	 *               {@link JVxConstants#RIGHT} or {@link JVxConstants#STRETCH}
	 */
	public void setHorizontalAlignment(int pHorizontalAlignment)
	{
		iHorizontalAlignment = pHorizontalAlignment;
	}

	/**
	 * Returns the alignment for the x axis.
	 * 
	 * @return the alignment {@link JVxConstants#LEFT}, {@link JVxConstants#CENTER},
	 *         {@link JVxConstants#RIGHT} or {@link JVxConstants#STRETCH} 
	 */
	public int getHorizontalAlignment()
	{
		return iHorizontalAlignment;
	}

	/**
	 * Sets the alignment for the y axis.
	 * 
	 * @param pVerticalAlignment the alignment {@link JVxConstants#TOP}, {@link JVxConstants#CENTER},
	 *               {@link JVxConstants#BOTTOM} or {@link JVxConstants#STRETCH}
	 */
	public void setVerticalAlignment(int pVerticalAlignment)
	{
		iVerticalAlignment = pVerticalAlignment;
	}

	/**
	 * Gets the alignment for the y axis.
	 * 
	 * @return the alignment {@link JVxConstants#TOP}, {@link JVxConstants#CENTER},
	 *         {@link JVxConstants#BOTTOM} or {@link JVxConstants#STRETCH}
	 */
	public int getVerticalAlignment()
	{
		return iVerticalAlignment;
	}

	/**
	 * Sets the component alignment for the x axis. The alignment will be used, if the
	 * container contains components which have different widths and the orientation
	 * of the layout is {@link JVxConstants#VERTICAL}.
	 * 
	 * @param pHorizontalComponentAlignment the alignment {@link JVxConstants#LEFT}, {@link JVxConstants#CENTER},
	 *               {@link JVxConstants#RIGHT} or {@link JVxConstants#STRETCH}
	 */
	public void setHorizontalComponentAlignment(int pHorizontalComponentAlignment)
	{
		iHorizontalComponentAlignment = pHorizontalComponentAlignment;
	}

	/**
	 * Gets the component alignment for the x axis.
	 * 
	 * @return the alignment {@link JVxConstants#LEFT}, {@link JVxConstants#CENTER},
	 *         {@link JVxConstants#RIGHT} or {@link JVxConstants#STRETCH}
	 * @see #setHorizontalComponentAlignment(int)         
	 */
	public int getHorizontalComponentAlignment()
	{
		return iHorizontalComponentAlignment;
	}

	/**
	 * Sets the component alignement for the y axis. The alignment will be used, if the
	 * container contains components which have different heights and the orientation
	 * of the layout is {@link JVxConstants#HORIZONTAL}.
	 * 
	 * @param pVerticalComponentAlignment the alignment {@link JVxConstants#TOP}, {@link JVxConstants#CENTER},
	 *        {@link JVxConstants#BOTTOM} or {@link JVxConstants#STRETCH}
	 */
	public void setVerticalComponentAlignment(int pVerticalComponentAlignment)
	{
		iVerticalComponentAlignment = pVerticalComponentAlignment;
	}

	/**
	 * Gets the component alignment for the x axis.
	 * 
	 * @return the alignment {@link JVxConstants#TOP}, {@link JVxConstants#CENTER},
	 *         {@link JVxConstants#BOTTOM} or {@link JVxConstants#STRETCH}
	 * @see #setVerticalComponentAlignment(int)         
	 */
	public int getVerticalComponentAlignment()
	{
		return iVerticalComponentAlignment;
	}

	/**
	 * Calculates the width, height, row and column count for the current components
	 * of a container.
	 * 
	 * @param pContainer the container to be layouted
	 * @return a rectangle with width, height, column count (stored in x) and row count
	 *         (stored in y)
	 */
	private Rectangle calculateGrid(Container pContainer)
	{
		int iWidth = 0;
		int iHeight = 0;

		int iCalcWidth = 0;
		int iCalcHeight = 0;
		
		int iAnzRows = 1;
		int iAnzCols = 1;
		
		Insets insets = pContainer.getInsets();
		Rectangle bounds = pContainer.getBounds();
		if (pContainer.getParent() instanceof JViewport)
		{
			Dimension dim = pContainer.getParent().getSize();
			
			if (dim.width < bounds.width)
			{
				bounds.width = dim.width;
			}
			if (dim.height < bounds.height)
			{
				bounds.height = dim.height;
			}
		}
        bounds.width -= insets.left + insets.right + insMargins.left + insMargins.right;
        bounds.height -= insets.top + insets.bottom + insMargins.top + insMargins.bottom;
		
		Component comp;

		//needed because the visible state of the component will be checked!
		boolean bFirst = true;
		
		for (int i = 0, anz = pContainer.getComponentCount(); i < anz; i++)
		{
			comp = pContainer.getComponent(i);

			if (comp.isVisible())
			{
				Dimension dimPref = JVxUtil.getPreferredSize(comp);
				
				if (iOrientation == HORIZONTAL)
				{
					if (!bFirst)
					{
						iCalcWidth += iHorizontalGap;
					}
					
					iCalcWidth += dimPref.width;
					iHeight = Math.max(iHeight, dimPref.height);
					
					//wrapping doesn't change the height, because the height will be used
					//for all rows
					if (!bFirst 
						&& bAutoWrap 
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
						iCalcHeight += iVerticalGap;
					}
					
					iWidth = Math.max(iWidth, dimPref.width);
					iCalcHeight += dimPref.height;
					
					//wrapping doesn't change the width, because the width will be used
					//for all columns
					if (!bFirst
						&& bAutoWrap 
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
		}
		
		return new Rectangle(iAnzCols, iAnzRows, iWidth, iHeight);
	}
	
	/**
	 * Gets the factor for an alignment value. The factor will be used
	 * to align the components in the layout.
	 * 
	 * @param pAlign the alignment e.g {@link JVxConstants#LEFT}, {@link JVxConstants#CENTER}, {@link JVxConstants#RIGHT} 
	 * @return the factor for the alignment e.g <code>0f</code>, <code>0.5f</code>, <code>1f</code>
	 * @throws IllegalArgumentException if the alignment is unknown or not allowed
	 */
	private float getAlignmentFactor(int pAlign)
	{
		switch (pAlign)
		{
			case JVxConstants.LEFT:
			case JVxConstants.TOP:
				return 0f;
			case JVxConstants.CENTER:
				return 0.5f;
			case JVxConstants.RIGHT:
			case JVxConstants.BOTTOM:
				return 1f;
			default:
				throw new IllegalArgumentException("Invalid alignment: " + pAlign);
		}
	}
	
	/**
	 * Sets the layout margins.
	 * 
	 * @param pMargins the margins
	 */
	public void setMargins(Insets pMargins)
	{
		insMargins = pMargins;
	}
	
	/**
	 * Gets the layout margins.
	 * 
	 * @return the margins
	 */
	public Insets getMargins()
	{
		return insMargins;
	}

}	// JVxSequenceLayout
