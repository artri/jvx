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
 * 12.10.2008 - [JR] - creation
 * 30.10.2008 - [JR] - used ImageObserver for drawImage
 */
package com.sibvisions.rad.ui.swing.ext;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

/**
 * The <code>JVxIcon</code> is a simple component with an image. The
 * image can be aligned or stretched.
 *  
 * @author René Jahn
 */
public class JVxIcon extends JComponent 
                     implements JVxConstants
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** the image of the icon. */
	private ImageIcon image;
	
	/** the icon width. */
	private int iImageWidth = 0;
	
	/** the icon height. */
	private int iImageHeight = 0;
	
	/** the horizontal alignment of the image (default: {@link #CENTER}). */
	private int iHorizontalAlign = JVxConstants.CENTER;

	/** the vertical alignment of the background image (default: {@link #CENTER}). */
	private int iVerticalAlign = JVxConstants.CENTER;
	
	/** if the aspect ratio of the image should be preserved. */
	private boolean bPreserveAspectRatio = false;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>JVxIcon</code> without an image.
	 */
	public JVxIcon()
	{
		this((ImageIcon)null);
	}
	
	/**
	 * Creates a new instance of <code>JVxIcon</code> based on an {@link Image}.
	 * 
	 * @param pImage the image
	 */
	public JVxIcon(Image pImage)
	{
		setImage(pImage);
	}
	
	/**
	 * Creates a new instance of <code>JVxIcon</code> based on an {@link Image}.
	 * 
	 * @param pImage the image
	 */
	public JVxIcon(ImageIcon pImage)
	{
		setImageIcon(pImage);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean imageUpdate(Image img, int infoflags, int x, int y, int w, int h) 
    {
        // Stop animation thread, if component is not showing anymore.
        return isShowing() && super.imageUpdate(img, infoflags, x, y, w, h);
    }
    
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void paintComponent(Graphics pGraphics)
	{
		Insets ins = getInsets();
		Dimension size = getSize();

		if (isBackgroundSet())
		{
			pGraphics.setColor(getBackground());
			pGraphics.fillRect(0, 0, size.width, size.height);
		}
		
		if (image != null)
		{
			size.width -= ins.left + ins.right;
			size.height -= ins.top + ins.bottom;
			
			int hAlign = iHorizontalAlign;
			int vAlign = iVerticalAlign;
			if (image instanceof JVxFontAwesomeIcon)
			{
				if (hAlign == STRETCH)
				{
					hAlign = CENTER;
				}
				if (vAlign == STRETCH)
				{
					vAlign = CENTER;
				}
			}
			
			int iX;
			int iY;
			int iWidth;
			int iHeight;
			
			switch (hAlign)
			{
				case LEFT:
					iX = ins.left;
					iWidth = iImageWidth;
					break;
				case CENTER:
					iX = ins.left + (size.width - iImageWidth) / 2;
					iWidth = iImageWidth;
					break;
				case RIGHT:
					iX = ins.left + size.width - iImageWidth;
					iWidth = iImageWidth;
					break;
				case STRETCH:
				default:
					iX = ins.left;
					iWidth = size.width;
			}
			
			switch (vAlign)
			{
				case TOP:
					iY = ins.top;
					iHeight = iImageHeight;
					break;
				case CENTER:
					iY = ins.top + (size.height - iImageHeight) / 2;
					iHeight = iImageHeight;
					break;
				case BOTTOM:
					iY = ins.top + size.height - iImageHeight;
					iHeight = iImageHeight;
					break;
				case STRETCH:
				default:
					iY = ins.top;
					iHeight = size.height;
			}
			
			if (bPreserveAspectRatio)
			{
				if (iHorizontalAlign == STRETCH && iVerticalAlign != STRETCH)
				{
					int newHeight = iWidth * iImageHeight / iImageWidth;
					
					if (iVerticalAlign == CENTER)
					{
						iY = iY - (newHeight - iHeight) / 2;
					}
					else if (iVerticalAlign == BOTTOM)
					{
						iY = iY - (newHeight - iHeight);
					}
					
					iHeight = newHeight;
				}
				else if (iHorizontalAlign != STRETCH && iVerticalAlign == STRETCH)
				{
					int newWidth = iHeight * iImageWidth / iImageHeight;
					
					if (iHorizontalAlign == CENTER)
					{
						iX = iX - (newWidth - iWidth) / 2;
					}
					else if (iHorizontalAlign == RIGHT)
					{
						iX = iX - (newWidth - iWidth);
					}
					
					iWidth = newWidth;
				}
				else if (iHorizontalAlign == STRETCH && iVerticalAlign == STRETCH)
				{
					int propWidth = iHeight * iImageWidth / iImageHeight;
					int propHeight = iWidth * iImageHeight / iImageWidth;
					
					if (propWidth > iWidth)
					{
						iHeight = propHeight;
					}
					else if (propHeight > iHeight)
					{
					    iWidth = propWidth;
					}
					
					iX = ins.left + (size.width - iWidth) / 2;
					iY = ins.top + (size.height - iHeight) / 2;
				}
			}
			
			if (iImageWidth == iWidth && iImageHeight == iHeight)
			{
				image.paintIcon(this, pGraphics, iX, iY);
			}
			// Proper stretching of FontAwesome icons
			// But this is not possible in other technologies, so we will center instead of stretch
//			else if (image instanceof JVxFontAwesomeIcon)
//			{
//				Graphics2D g2 = (Graphics2D)pGraphics.create(iX, iY, iWidth, iHeight);
//
//				g2.scale((double)iWidth / (double)iImageWidth, (double)iHeight / (double)iImageHeight);
//				
//				image.paintIcon(this, g2, 0, 0);
//				
//				g2.dispose();
//			}
			else
			{
				((Graphics2D)pGraphics).setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

				pGraphics.drawImage(image.getImage(), iX, iY, iWidth, iHeight, this);
			}
		}
	}

	/**
	 * Gets the preferred size of the icon dependent of the configured
	 * alignments. If an alignment is {@link #STRETCH} then the
	 * size of the component will be used. Otherwise the size
	 * of the image will be used.
	 * 
	 * @return the preferred size of the image
	 */
	@Override
	public Dimension getPreferredSize()
	{
		if (isPreferredSizeSet())
		{
			return super.getPreferredSize();
		}
		else
		{
			Insets ins = getInsets(); 
			return new Dimension(iImageWidth + ins.left + ins.right, iImageHeight + ins.top + ins.bottom);
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the image of the icon.
	 * 
	 * @return the image.
	 */
	public ImageIcon getImageIcon()
	{
		return image;
	}
	
	/**
	 * Sets the image for the icon.
	 * 
	 * @param pImage the image
	 */
	public void setImageIcon(ImageIcon pImage)
	{
		image = pImage;
		
		int oldHeight = iImageHeight;
		int oldWidth = iImageWidth;
		if (image == null)
		{
			iImageHeight = 0;
			iImageWidth  = 0;
		}
		else
		{
			iImageHeight = pImage.getIconHeight();
			iImageWidth  = pImage.getIconWidth();
		}
		
		invalidate();
		if ((oldHeight != iImageHeight || oldWidth != iImageWidth) && !isPreferredSizeSet())
		{
			JVxUtil.revalidateAllDelayed(this);
		}
		
		repaint();
	}	
	
	/**
	 * Gets the image of the icon.
	 * 
	 * @return the image.
	 */
	public Image getImage()
	{
		if (image == null)
		{
			return null;
		}
		else
		{
			return image.getImage();
		}
	}
	
	/**
	 * Sets the image for the icon.
	 * 
	 * @param pImage the image
	 */
	public void setImage(Image pImage)
	{
		if (pImage == null)
		{
			setImageIcon(null);
		}
		else
		{
			setImageIcon(new ImageIcon(pImage));
		}
	}	
	
	/**
	 * Sets the horizontal alignment of the icon.
	 * 
	 * @param pAlignment the alignment
	 * @see JVxConstants
	 */
	public void setHorizontalAlignment(int pAlignment)
	{
		if (pAlignment == LEFT || pAlignment == CENTER || pAlignment == RIGHT || pAlignment == STRETCH)
		{
			if (pAlignment != iHorizontalAlign)
			{
				iHorizontalAlign = pAlignment;
				invalidate();
				repaint();
			}
		}
		else
		{
			throw new IllegalArgumentException("horizontalAlignment " + pAlignment);
		}
	}
	
	/**
	 * Gets the horizontal alignment of the icon.
	 * 
	 * @return the alignment
	 * @see JVxConstants
	 */
	public int getHorizontalAlignment()
	{
		return iHorizontalAlign;
	}

	/**
	 * Sets the vertical alignment of the icon.
	 * 
	 * @param pAlignment the alignment
	 * @see JVxConstants
	 */
	public void setVerticalAlignment(int pAlignment)
	{
		if (pAlignment == TOP || pAlignment == CENTER || pAlignment == BOTTOM || pAlignment == STRETCH)
		{
			if (pAlignment != iVerticalAlign)
			{
				iVerticalAlign = pAlignment;
				invalidate();
				repaint();
			}
		}
		else
		{
			throw new IllegalArgumentException("verticalAlignment " + pAlignment);
		}
	}

	/**
	 * Gets the vertical alignment of the icon.
	 * 
	 * @return the alignment
	 * @see JVxConstants
	 */
	public int getVerticalAlignment()
	{
		return iVerticalAlign;
	}

    /**
     * If the aspect ratio of the image should be preserved if it is stretched
     * in any direction.
     * 
     * @return {@code true} if the aspect ratio of the image is preserved when stretched.
     */
	public boolean isPreserveAspectRatio()
	{
		return bPreserveAspectRatio;
	}

    /**
     * Sets if the aspect ratio of the image should be preserved if it is stretched
     * in any direction.
     * 
     * @param pPreserveAspectRatio {@code true} if the aspect ratio of the image is preserved when stretched.
     */
	public void setPreserveAspectRatio(boolean pPreserveAspectRatio)
	{
		bPreserveAspectRatio = pPreserveAspectRatio;
		
		invalidate();
		repaint();
	}
	
}	// JVxIcon
