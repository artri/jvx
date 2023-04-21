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
 * 05.11.2009 - [JR] - creation
 * 17.12.2009 - [JR] - getScaledIcon(Icon...) implemented
 * 16.06.2011 - [JR] - getImageFormat implemented
 * 15.09.2017 - [JR] - re-use image type of scaled image, if possible
 */
package com.sibvisions.util.type;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import com.sibvisions.util.type.imageio.MetaDataImage;

/**
 * The <code>ImageUtil</code> provides useful image operation/manipulation methods.
 *  
 * @author René Jahn
 */
public final class ImageUtil
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class Members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The image formats. */
	public static enum ImageFormat
	{
		/** unknown image format. */
		UNKNOWN,
		/** jpg format. */
		JPG,
		/** jpg2000 format. */
		JPG2000,
		/** gif format. */
		GIF,
		/** png format. */
		PNG,
		/** wmf format. */
		WMF,
		/** bmp format. */
		BMP,
		/** tiff format. */
		TIFF,
		/** jbig2 format. */
		JBIG2
	};
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Invisible constructor because <code>ImageUtil</code> is a utility
	 * class.
	 */
	private ImageUtil()
	{
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets a scaled {@link ImageIcon} out of an {@link Icon}.
	 * 
	 * @param pIcon the icon
	 * @param pWidth the desired image height
	 * @param pHeight the desired image height
	 * @param pProportional <code>true</code> for proportional scaling and <code>false</code> for fixed scaling
	 * @return the scaled icon
	 */
	public static ImageIcon getScaledIcon(Icon pIcon, int pWidth, int pHeight, boolean pProportional)
	{
		if (pIcon == null)
		{
			return null;
		}
		
		if (pIcon instanceof ImageIcon)
		{
			return getScaledIcon((ImageIcon)pIcon, pWidth, pHeight, pProportional);
		}
		else
		{
		    BufferedImage biOrig = new BufferedImage(pIcon.getIconWidth(),
					                                 pIcon.getIconHeight(),
					                                 BufferedImage.TYPE_INT_ARGB);
			
			Graphics gra = biOrig.getGraphics();
			
			pIcon.paintIcon(null, gra, 0, 0);
			
			gra.dispose();
			
			return new ImageIcon(getScaledImage(biOrig, pWidth, pHeight, pProportional));
		}
	}	
	
	/**
	 * Gets a scaled {@link ImageIcon} out of an {@link ImageIcon}.
	 * 
	 * @param pIcon the image
	 * @param pWidth the desired image height
	 * @param pHeight the desired image height
	 * @param pProportional <code>true</code> for proportional scaling and <code>false</code> for fixed scaling
	 * @return the scaled icon
	 */
	public static ImageIcon getScaledIcon(ImageIcon pIcon, int pWidth, int pHeight, boolean pProportional)
	{
		if (pIcon == null)
		{
			return null;
		}
		
		if (pIcon.getIconWidth() <= pWidth && pIcon.getIconHeight() <= pHeight)
		{
			return pIcon;
		}
		else
		{
			return new ImageIcon(getScaledImage(pIcon.getImage(), pWidth, pHeight, pProportional));
		}
	}	
	
	/**
	 * Gets a scaled {@link Image} out of an {@link InputStream}.
	 * 
	 * @param pImage the image stream
	 * @param pWidth the desired image height
	 * @param pHeight the desired image height
	 * @param pProportional <code>true</code> for proportional scaling and <code>false</code> for fixed scaling
	 * @return the scaled image
	 * @throws IOException if an error occurs during image streaming
	 */
	public static Image getScaledImage(InputStream pImage, int pWidth, int pHeight, boolean pProportional) throws IOException
	{
		byte[] byData = FileUtil.getContent(pImage);
		
		ByteArrayInputStream bais = new ByteArrayInputStream(byData);
		
		try
		{
			return getScaledImage(new MetaDataImage(byData, ImageIO.read(bais)).getImage(), pWidth, pHeight, pProportional);
		}
		finally
		{
			CommonUtil.close(bais);
		}
	}
	
	/**
	 * Gets a scaled {@link Image} out of an {@link Image}.
	 * 
	 * @param pImage the image
	 * @param pWidth the desired image height
	 * @param pHeight the desired image height
	 * @param pProportional <code>true</code> for proportional scaling and <code>false</code> for fixed scaling
	 * @return the scaled image
	 */
	public static Image getScaledImage(Image pImage, int pWidth, int pHeight, boolean pProportional)
	{
        if (pImage instanceof BufferedImage)
        {
            return getScaledImage(pImage, ((BufferedImage)pImage).getType(), pWidth, pHeight, pProportional);
        }
        else
        {
            return getScaledImage(pImage, BufferedImage.TYPE_INT_ARGB, pWidth, pHeight, pProportional);
        }
	}
	
	/**
	 * Gets a scaled {@link Image} out of an {@link Image}.
	 * 
	 * @param pImage the image
	 * @param pType one of the image types of {@link BufferedImage}
	 * @param pWidth the desired image height
	 * @param pHeight the desired image height
	 * @param pProportional <code>true</code> for proportional scaling and <code>false</code> for fixed scaling
	 * @return the scaled image
	 */
	public static Image getScaledImage(Image pImage, int pType, int pWidth, int pHeight, boolean pProportional)
	{
		if (pImage == null)
		{
			return null;
		}
	
		int iWidth = pImage.getWidth(null);
		int iHeight = pImage.getHeight(null);
		
		if (iWidth <= pWidth && iHeight <= pHeight)
		{
			return pImage;
		}
		else
		{
			//check proportional option
			if (pProportional)
			{
                int propWidth = pHeight * iWidth / iHeight;
			    int propHeight = pWidth * iHeight / iWidth;
			    
                if (propWidth > pWidth)
                {
                    pHeight = propHeight;
                }
                else if (propHeight > pHeight)
                {
                    pWidth = propWidth;
                }
			}
		
			while (iWidth > pWidth || iHeight > pHeight)
			{
	            if (iWidth > pWidth) 
	            {
	                iWidth /= 2;
	                if (iWidth < pWidth) 
	                {
	                    iWidth = pWidth;
	                }
	            }
	            if (iHeight > pHeight) 
	            {
	                iHeight /= 2;
	                if (iHeight < pHeight) 
	                {
	                    iHeight = pHeight;
	                }
	            }
	            
	            BufferedImage biScaled = new BufferedImage(Math.max(1, iWidth), Math.max(1, iHeight), pType);
	            
	            Graphics2D g2 = biScaled.createGraphics();
	            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	            g2.drawImage(pImage, 0, 0, iWidth, iHeight, null);
	            g2.dispose();
	
	            pImage = biScaled;
			}
			
			return pImage;
		}
	}
	
	/**
	 * Creates a scaled image out of an {@link InputStream}.
	 * 
	 * @param pImage the image stream
	 * @param pWidth the desired image height
	 * @param pHeight the desired image height
	 * @param pProportional <code>true</code> for proportional scaling and <code>false</code> for fixed scaling
	 * @param pScaledImage the scaled image output stream
	 * @param pFormat the output format supported from image IO, e.g. png, jpg, gif
	 * @see ImageIO
	 * @throws IOException if an error occurs during image reading or writing
	 */
	public static void createScaledImage(InputStream pImage, 
			                             int pWidth, 
			                             int pHeight, 
			                             boolean pProportional, 
			                             OutputStream pScaledImage, 
			                             String pFormat) throws IOException
	{
		Image img = getScaledImage(pImage, pWidth, pHeight, pProportional);
		
		if (!(img instanceof BufferedImage))
		{
			throw new IOException("Only buffered images can be scaled!");
		}

        Iterator<ImageWriter> it = ImageIO.getImageWritersByFormatName(pFormat);
        
        if (!it.hasNext())
        {
            throw new RuntimeException("No image writer available for '" + pFormat + "'!");
        }
        else
        {
            ImageWriter writer = it.next();

            if (writer == null)
            {
                throw new IOException("Output format '" + pFormat + "' is not supported!");
            }
            
            ImageOutputStream os = null;
            
            try
            {
                ImageWriteParam param = writer.getDefaultWriteParam();
                
                if (param != null && param.canWriteCompressed())
                {
//                    param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
//                    param.setCompressionQuality(1);
                }
                
                os = ImageIO.createImageOutputStream(pScaledImage);
                
                writer.setOutput(os);
                writer.write(null, new IIOImage((BufferedImage)img, null, null), param);
            }
            finally
            {
                CommonUtil.close(os);
                
                writer.dispose();
            }
        }
	}
	
	/**
	 * Gets the image format from raw image data. 
	 * 
	 * @param pData the image data
	 * @return the detected image format
	 */
	public static ImageFormat getImageFormat(byte[] pData)
	{
		ByteArrayInputStream bais = null;
		
		try 
		{
			bais = new ByteArrayInputStream(pData);
			
			int c1 = bais.read();
			int c2 = bais.read();
			int c3 = bais.read();
			int c4 = bais.read();
			
			if (c1 == 'G' && c2 == 'I' && c3 == 'F') 
			{
				return ImageFormat.GIF;
			}
			
			if (c1 == 0xFF && c2 == 0xD8) 
			{
				return ImageFormat.JPG;
			}
			
			if (c1 == 0x00 && c2 == 0x00 && c3 == 0x00 && c4 == 0x0c) 
			{
				return ImageFormat.JPG2000;
			}

			if (c1 == 0xff && c2 == 0x4f && c3 == 0xff && c4 == 0x51) 
			{
				return ImageFormat.JPG2000;
			}
			
			if (c1 == 137 && c2 == 80 && c3 == 78 && c4 == 71) 
			{
				return ImageFormat.PNG;
			}
			
			if (c1 == 0xD7 && c2 == 0xCD) 
			{
				return ImageFormat.WMF;
			}
			if (c1 == 'B' && c2 == 'M') 
			{
				return ImageFormat.BMP;
			}
			
			if ((c1 == 'M' && c2 == 'M' && c3 == 0 && c4 == 42)
				|| (c1 == 'I' && c2 == 'I' && c3 == 42 && c4 == 0)) 
			{
				return ImageFormat.TIFF;
			}
			
			if (c1 == 0x97 && c2 == 'J' && c3 == 'B' && c4 == '2') 
			{
				int c5 = bais.read();
				int c6 = bais.read();
				int c7 = bais.read();
				int c8 = bais.read();
				
				if (c5 == '\r' && c6 == '\n' && c7 == 0x1a && c8 == '\n') 
				{
					//int iFileHeaderFlags = bais.read();
					//int iNumberOfPages = -1;
					//
					//if ((iFileHeaderFlags & 0x2) == 0x2) 
					//{
					//	int iNumberOfPages = (bais.read() << 24) | (bais.read() << 16) | (bais.read() << 8) | bais.read();
					//}
					
					// A jbig2 file with a file header. The header is the only way we know here.                                                           
					// embedded jbig2s don't have a header
					return ImageFormat.JBIG2;
				}
			}
			
			return ImageFormat.UNKNOWN;
		} 
		finally 
		{
			try
			{
				bais.close();
			}
			catch (Exception e)
			{
				//nothing to be done
			}
			
			bais = null;
		}
	}
	
	/**
	 * Saves the given image icon.
	 * 
	 * @param pImage the image icon
	 * @param pFormat the output format
	 * @param pOut the stream
	 * @throws IOException if writing failed
	 */
    public static void save(ImageIcon pImage, ImageFormat pFormat, OutputStream pOut) throws IOException
    {
        if (pImage != null)
        {
            save(pImage.getImage(), pImage.getIconWidth(), pImage.getIconHeight(), pFormat, pOut);
        }
    }

    /**
     * Saves the given image.
     * 
     * @param pImage the image icon
     * @param pFormat the output format
     * @param pOut the stream
     * @throws IOException if writing failed
     */
    public static void save(Image pImage, ImageFormat pFormat, OutputStream pOut) throws IOException
    {
        if (pImage != null)
        {
            save(pImage, pImage.getWidth(null), pImage.getHeight(null), pFormat, pOut);
        }
	}
    
    /**
     * Saves the given image.
     * 
     * @param pImage the image icon
     * @param pWidth the image width
     * @param pHeight the image height
     * @param pFormat the output format
     * @param pOut the stream
     * @throws IOException if writing failed
     */
    private static void save(Image pImage, int pWidth, int pHeight, ImageFormat pFormat, OutputStream pOut) throws IOException
    {
	    String sType;
	    
	    switch (pFormat)
	    {
	        case JPG:
            case JPG2000:
	            sType = "jpg";
	            break;
	        case BMP:
	            sType = "bmp";
	            break;
	        case GIF:
	            sType = "gif";
	            break;
	        default:
	            sType = "png";
	            break;
	    }
	    
        if (pImage instanceof RenderedImage)
        {
            ImageIO.write((RenderedImage)pImage, sType, pOut);
        }
        else
        {
            BufferedImage image = new BufferedImage(pWidth, pHeight, BufferedImage.BITMASK);
            Graphics gr = image.getGraphics();
            gr.drawImage(pImage, 0, 0, null);
            gr.dispose();
            
            ImageIO.write(image, sType, pOut);
        }
	}
	
}	// ImageUtil
