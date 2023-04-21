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
 * 14.11.2008 - [HM] - creation
 * 24.10.2012 - [JR] - #604: added constructor
 */
package javax.rad.genui;

import javax.rad.ui.IDimension;
import javax.rad.ui.IPoint;
import javax.rad.ui.IRectangle;

/**
 * Platform and technology independent rectangle.
 * 
 * @author Martin Handsteiner
 */
public class UIRectangle extends UIResource<IRectangle> 
                         implements IRectangle
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>UIRectangle</code> with x=0, y=0, with=0 and height=0.
     *
     * @see IRectangle
     */
    public UIRectangle()
	{
		super(UIFactoryManager.getFactory().createRectangle(0, 0, 0, 0));
	}
    
    /**
     * Creates a new instance of <code>UIRectangle</code> with the given rectangle.
     *
     * @param pRectangle the rectangle
     * @see IRectangle
     */
    protected UIRectangle(IRectangle pRectangle)
	{
		super(pRectangle);
	}

    /**
     * Creates a new instance of <code>UIRectangle</code> with the given <code>IPoint</code> and 
     * <code>IDimension</code>.
     *
     * @param pPoint the point value
     * @param pDimension the dimension value
     * @see IRectangle
     * @see IPoint
     * @see IDimension
     */
    public UIRectangle(IPoint pPoint, IDimension pDimension)
	{
		super(UIFactoryManager.getFactory().createRectangle(pPoint.getX(), pPoint.getY(), pDimension.getWidth(), pDimension.getHeight()));
	}
    
    /**
     * Creates a new instance of <code>IRectangle</code> with the given x, y, width and height.
     *
     * @param pX the x value
     * @param pY the y value
     * @param pWidth the width 
     * @param pHeight the height 
     * @see IRectangle
     */
    public UIRectangle(int pX, int pY, int pWidth, int pHeight)
	{
		super(UIFactoryManager.getFactory().createRectangle(pX, pY, pWidth, pHeight));
	}
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
    public int getX()
    {
    	return uiResource.getX();
    }

	/**
	 * {@inheritDoc}
	 */
    public void setX(int pX)
    {
    	uiResource.setX(pX);
    }

	/**
	 * {@inheritDoc}
	 */
    public int getY()
    {
    	return uiResource.getY();
    }

	/**
	 * {@inheritDoc}
	 */
    public void setY(int pY)
    {
    	uiResource.setY(pY);
    }
    
	/**
	 * {@inheritDoc}
	 */
    public int getWidth()
    {
    	return uiResource.getWidth();
    }

	/**
	 * {@inheritDoc}
	 */
    public void setWidth(int pWidth)
    {
    	uiResource.setWidth(pWidth);
    }

	/**
	 * {@inheritDoc}
	 */
    public int getHeight()
    {
    	return uiResource.getHeight();
    }

	/**
	 * {@inheritDoc}
	 */
    public void setHeight(int pHeight)
    {
    	uiResource.setHeight(pHeight);
    }

    /**
     * Checks, if the given point is inside the given rectangle.
     * 
     * @param pRect the rectangle
     * @param pX the x
     * @param pY the y
     * @return true, if the given point is inside the given rectangle.
     */
    public static boolean contains(IRectangle pRect, int pX, int pY)
    {
        int x = pRect.getX();
        int y = pRect.getY();
        int w = pRect.getWidth();
        int h = pRect.getHeight();
        
        return pX >= x && pX < x + w
                && pY >= y && pY < y + h;
    }

    /**
     * Checks, if the given rectangle is inside the given rectangle.
     * 
     * @param pRect the rectangle
     * @param pX the x
     * @param pY the y
     * @param pW the width
     * @param pH the height
     * @return true, if the given rectangle is inside the given rectangle.
     */
    public static boolean contains(IRectangle pRect, int pX, int pY, int pW, int pH)
    {
        int x = pRect.getX();
        int y = pRect.getY();
        int w = pRect.getWidth();
        int h = pRect.getHeight();
        
        return pX >= x && pX + pW <= x + w
                && pY >= y && pY + pH <= y + h;
    }
    
    /**
     * Checks, if the given point is inside the given rectangle.
     * 
     * @param pRect the rectangle
     * @param pPoint the point
     * @return true, if the given point is inside the given rectangle.
     */
    public static boolean contains(IRectangle pRect, IPoint pPoint)
    {
        return contains(pRect, pPoint.getX(), pPoint.getY());
    }

    /**
     * Checks, if the given sub rectangle is inside the given rectangle.
     * 
     * @param pRect the rectangle
     * @param pSubRect the sub rectangle
     * @return true, if the given rectangle is inside the given rectangle.
     */
    public static boolean contains(IRectangle pRect, IRectangle pSubRect)
    {
        return contains(pRect, pSubRect.getX(), pSubRect.getY(), pSubRect.getWidth(), pSubRect.getHeight());
    }

    /**
     * Checks, if the two given rectangles intersects.
     * 
     * @param pRect1 the rectangle
     * @param pRect2 the rectangle
     * @return true, if the two given rectangles intersects.
     */
    public static boolean intersects(IRectangle pRect1, IRectangle pRect2)
    {
        int x1 = pRect1.getX();
        int y1 = pRect1.getY();
        int w1 = pRect1.getWidth();
        int h1 = pRect1.getHeight();
        int x2 = pRect2.getX();
        int y2 = pRect2.getY();
        int w2 = pRect2.getWidth();
        int h2 = pRect2.getHeight();
        
        return x2 + w2 > x1 && x2 < x1 + w1
                && y2 + h2 > y1 && y2 < y1 + h1;
    }
    
    /**
     * Calculates the intersection of the two given rectangles.
     * 
     * @param pRect1 the rectangle
     * @param pRect2 the rectangle
     * @return the intersection of the two given rectangles.
     */
    public static IRectangle intersection(IRectangle pRect1, IRectangle pRect2)
    {
        int x1 = pRect1.getX();
        int y1 = pRect1.getY();
        int xw1 = x1 + pRect1.getWidth();
        int yh1 = y1 + pRect1.getHeight();
        int x2 = pRect2.getX();
        int y2 = pRect2.getY();
        int xw2 = x2 + pRect2.getWidth();
        int yh2 = y2 + pRect2.getHeight();
        if (x2 > x1)
        {
            x1 = x2;
        }
        if (y2 > y1) 
        {
            y1 = y2;
        }
        if (xw2 < xw1) 
        {
            xw1 = xw2;
        }
        if (yh2 < yh1)
        {
            yh1 = yh2;
        }
        
        return new UIRectangle(x1, y1, xw1 - x1, yh1 - y1);
    }
    
    /**
     * Calculates the union of the two given rectangles.
     * 
     * @param pRect1 the rectangle
     * @param pRect2 the rectangle
     * @return the union of the two given rectangles.
     */
    public static IRectangle union(IRectangle pRect1, IRectangle pRect2)
    {
        int x1 = pRect1.getX();
        int y1 = pRect1.getY();
        int xw1 = x1 + pRect1.getWidth();
        int yh1 = y1 + pRect1.getHeight();
        int x2 = pRect2.getX();
        int y2 = pRect2.getY();
        int xw2 = x2 + pRect2.getWidth();
        int yh2 = y2 + pRect2.getHeight();
        if (x2 < x1)
        {
            x1 = x2;
        }
        if (y2 < y1) 
        {
            y1 = y2;
        }
        if (xw2 > xw1) 
        {
            xw1 = xw2;
        }
        if (yh2 > yh1)
        {
            yh1 = yh2;
        }
        
        return new UIRectangle(x1, y1, xw1 - x1, yh1 - y1);
    }
    
}	// UIRectangle
