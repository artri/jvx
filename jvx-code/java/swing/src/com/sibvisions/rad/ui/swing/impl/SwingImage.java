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
 * 24.01.2009 - [JR] - saveAs implemented
 * 21.02.2011 - [JR] - #62: saveAs with output stream 
 */
package com.sibvisions.rad.ui.swing.impl;

import java.io.IOException;
import java.io.OutputStream;

import javax.rad.ui.IImage;
import javax.swing.ImageIcon;

import com.sibvisions.rad.ui.awt.impl.AwtResource;
import com.sibvisions.util.type.ImageUtil;
import com.sibvisions.util.type.ImageUtil.ImageFormat;

/**
 * The <code>AwtImage</code> represents graphical images.
 * 
 * @author Martin Handsteiner
 */
public class SwingImage extends AwtResource<ImageIcon> 
                        implements IImage
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class Members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The image name. */
	private String imageName;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates an instance of <code>AwtImage</code> based on 
	 * a <code>java.awt.Image</code>.
	 * 
	 * @param pImage java.awt.Image
	 * @see java.awt.Image
	 */
	public SwingImage(ImageIcon pImage)
	{
		this(null, pImage);
	}

	/**
	 * Creates an instance of <code>AwtImage</code> based on 
	 * a <code>java.awt.Image</code>.
	 * 
	 * @param pImageName the image name
	 * @param pImage java.awt.Image
	 * @see java.awt.Image
	 */
	public SwingImage(String pImageName, ImageIcon pImage)
	{
		super(pImage);
		
		imageName = pImageName;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public String getImageName()
	{
		return imageName;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getWidth()
	{
		return resource.getIconWidth();
	}

	/**
	 * {@inheritDoc}
	 */
	public int getHeight()
	{
		return resource.getIconHeight();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void saveAs(OutputStream pOut, ImageType pType) throws IOException
	{
		ImageFormat format;

		if (pType == null)
		{
		    format = ImageFormat.PNG;
		}
		else 
		{
			switch (pType)
			{
				case JPG:
				    format = ImageFormat.JPG;
					break;
				case GIF:
				    format = ImageFormat.GIF;
					break;
				case BMP:
				    format = ImageFormat.BMP;
					break;
				default:
				    format = ImageFormat.PNG;
			}
		}
		
        ImageUtil.save(resource.getImage(), format, pOut); 
	}

}	// AwtImage
