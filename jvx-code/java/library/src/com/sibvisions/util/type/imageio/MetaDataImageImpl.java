/*
 * Copyright 2022 SIB Visions GmbH
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
 * 14.12.2012 - [JR] - creation
 */
package com.sibvisions.util.type.imageio;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

import javax.imageio.ImageIO;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.jpeg.JpegDirectory;
import com.sibvisions.util.type.ImageUtil;
import com.sibvisions.util.type.ImageUtil.ImageFormat;

/**
 * The <code>MetaDataImageImpl</code> class uses 3rd party libraries for reading image metadata. This class won't work
 * if libraries are not available.
 * 
 * @author René Jahn
 */
public final class MetaDataImageImpl extends MetaDataImage
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>MetaDataImageImpl</code> for given image data.
	 * 
	 * @param pData the image data
	 * @param pImage the image
	 */
    public MetaDataImageImpl(byte[] pData, Image pImage)
	{
		this(null, pData, pImage);
	}		
	
	/**
	 * Creates a new instance of <code>MetaDataImageImpl</code> for given image data.
	 * 
	 * @param pImageName the image name
	 * @param pData the image data
	 * @param pImage the image
	 */
	public MetaDataImageImpl(String pImageName, byte[] pData, Image pImage)
	{
	    orientation = Orientation.Undefined;
	    image = pImage;
	    
		if (pData != null)
		{
		    ImageFormat format = ImageUtil.getImageFormat(pData); 

		    if (format == ImageFormat.JPG
		    	|| format == ImageFormat.JPG2000)
		    {
		    	try
		    	{
		    		Metadata metadata = ImageMetadataReader.readMetadata(new ByteArrayInputStream(pData));
		    		
		    		if (metadata != null)
		    		{
		    		 	Directory dirExif = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
		    			
		    			if (dirExif != null)
		    			{
		    				JpegDirectory dirJpg = metadata.getFirstDirectoryOfType(JpegDirectory.class);
		    				
		    				int iOrientation = dirExif.getInt(ExifIFD0Directory.TAG_ORIENTATION);
		    				
		    				int widthDir = dirJpg.getImageWidth();
		    				int heightDir = dirJpg.getImageHeight();
		    				
		    				AffineTransform atr;

		    			    switch (iOrientation) 
		    			    {
			    			    case 1:
			    			    	orientation = Orientation.Correct;
			    			    	
			    			    	atr = null;
			    			        break;
			    			    case 2: // Flip X
			    			    	orientation = Orientation.Mirrored;
			    			    	
			    			    	atr = new AffineTransform();
			    			    	atr.scale(-1.0, 1.0);
			    			        atr.translate(-widthDir, 0);
			    			        break;
			    			    case 3: // PI rotation
			    			    	orientation = Orientation.Rotate_180;
			    			    	
                                    atr = new AffineTransform();
			    			        atr.translate(widthDir, heightDir);
			    			        atr.rotate(Math.PI);
			    			        break;
			    			    case 4: // Flip Y
			    			    	orientation = Orientation.Rotate_180_Mirrored;
			    			    	
                                    atr = new AffineTransform();
			    			        atr.scale(1.0, -1.0);
			    			        atr.translate(0, -heightDir);
			    			        break;
			    			    case 5: // - PI/2 and Flip X
			    			    	orientation = Orientation.Rotate_90;
			    			    	
                                    atr = new AffineTransform();
			    			        atr.rotate(-Math.PI / 2);
			    			        atr.scale(-1.0, 1.0);
			    			        break;
			    			    case 6: // -PI/2 and -width
			    			    	orientation = Orientation.Rotate_90_Mirrored;
			    			    	
                                    atr = new AffineTransform();
			    			        atr.translate(heightDir, 0);
			    			        atr.rotate(Math.PI / 2);
			    			        break;
			    			    case 7: // PI/2 and Flip
			    			    	orientation = Orientation.Rotate_270;
			    			    	
                                    atr = new AffineTransform();
			    			        atr.scale(-1.0, 1.0);
			    			        atr.translate(-heightDir, 0);
			    			        atr.translate(0, widthDir);
			    			        atr.rotate(3 * Math.PI / 2);
			    			        break;
			    			    case 8: // PI / 2
			    			    	orientation = Orientation.Rotate_279_Mirrored;
			    			    	
                                    atr = new AffineTransform();
			    			        atr.translate(0, widthDir);
			    			        atr.rotate(3 * Math.PI / 2);
			    			        break;
			    			    default:
			    			    	throw new IllegalArgumentException("Unsupported orientation: " + iOrientation);
		    			    }	
		    			    
		    			    if (atr != null)
		    			    {
    		    			    BufferedImage bfImage;
    		    				if (image instanceof BufferedImage)
    		    				{
                                    bfImage = (BufferedImage)image;
    		    				}
    		    				else
    		    				{
                                    bfImage = ImageIO.read(new ByteArrayInputStream(pData));                                    
    		    				}
    		    					
    		    				if (bfImage != null)
    		    				{
    		    					AffineTransformOp op = new AffineTransformOp(atr, AffineTransformOp.TYPE_BICUBIC);
    		    				    
    		    			        Rectangle r = op.getBounds2D(bfImage).getBounds();
    		    			        // If r.x (or r.y) is < 0, then we want to only create an image
    		    			        // that is in the positive range.
    		    			        // If r.x (or r.y) is > 0, then we need to create an image that
    		    			        // includes the translation.
    		    	                BufferedImage destinationImage = new BufferedImage(r.x + r.width, r.y + r.height, bfImage.getType());
    		    	                Graphics2D g = destinationImage.createGraphics();
    		    	                
    		    	                try
    		    	                {
    		    	                    //g.setBackground(Color.WHITE);
    		    	                    //g.clearRect(0, 0, width, height);
    		    	                    destinationImage = op.filter(bfImage, destinationImage);
    		    	                }
    		    	                finally
    		    	                {
    		    	                    g.dispose();
    		    	                }
    		    	                
    		    	                image = destinationImage;		    				
    		    				}
		    			    }
		    			}
		    		}
		    	}
		    	catch (Throwable th)
		    	{
		    		//ignore
		    	}
		    }
		}
		
		width = image.getWidth(null);
		height = image.getHeight(null);
	}

}	// MetaDataImageImpl
