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

import java.awt.Image;

import com.sibvisions.util.Reflective;
import com.sibvisions.util.log.LoggerFactory;

/**
 * The <code>MetaDataImage</code> is a simple wrapper class for accessing image metadata. This class
 * is required to be backwards compatible. The metadata access required 3rd party libs. If libs are
 * not available, everything should work as usual but without metadata support. This class handles
 * access to metadata via reflection.
 * 
 * @author René Jahn
 */
public class MetaDataImage
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the orientation. */
	public enum Orientation
	{
		/** no orientation available. */
		Undefined,
		/** the correct orientation, no adjustment is required. */
		Correct,
		/** image has been flipped back-to-front. */
		Mirrored,
		/** image is upside down. */
		Rotate_180,
		/** image has been flipped back-to-front and is upside down. */
		Rotate_180_Mirrored,
		/** image has been flipped back-to-front and is on its side. */
		Rotate_90,
		/** image is on its side. */
		Rotate_90_Mirrored,
		/** image has been flipped back-to-front and is on its far side. */
		Rotate_270,
		/** image is on its far side. */
		Rotate_279_Mirrored,
	}
	
	/** the image instance. */
	protected Image image;

    /** the orientation of the original image. */
	protected Orientation orientation;
    
    /** the image width. */
	protected int width;
    
    /** the image height. */
	protected int height;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>MetaDataImage</code> for given image data.
	 * 
	 * @param pData the image data
	 * @param pImage the image
	 */
	public MetaDataImage(byte[] pData, Image pImage)
	{
		this(null, pData, pImage);
	}		
	
	/**
	 * Creates a new instance of <code>MetaDataImage</code> for given image data.
	 * 
	 * @param pImageName the image name
	 * @param pData the image data
	 * @param pImage the image
	 */
	public MetaDataImage(String pImageName, byte[] pData, Image pImage)
	{
		try
		{
		    MetaDataImage impl = (MetaDataImage)Reflective.construct("com.sibvisions.util.type.imageio.MetaDataImageImpl", pImageName, pData, pImage);
		    
		    image = impl.getImage();
		    orientation = impl.getOrientation();
		    width = impl.getWidth();
		    height = impl.getHeight();
		}
		catch (Throwable th)
		{
            image = pImage;
            orientation = Orientation.Undefined;
            width = image.getWidth(null);
            height = image.getHeight(null);

            LoggerFactory.getInstance(MetaDataImage.class).error(th);
		}
	}
	
	/**
	 * For extended implementations.
	 */
	protected MetaDataImage()
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the image orientation.
	 * 
	 * @return the orientation
	 */
	public Orientation getOrientation()
	{
		return orientation;
	}
	
	/**
	 * Gets the image width.
	 * 
	 * @return the width
	 */
	public int getWidth()
	{
		return width;
	}

	/**
	 * Gets the image height.
	 * 
	 * @return the height
	 */
	public int getHeight()
	{
		return height;
	}
	
	/**
	 * Gets the image.
	 * 
	 * @return the image
	 */
	public Image getImage()
	{
		return image;
	}
	
}	// MetaDataImage
