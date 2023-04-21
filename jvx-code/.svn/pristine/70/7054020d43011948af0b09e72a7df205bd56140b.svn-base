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
package com.sibvisions.rad.ui.awt.impl;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.rad.ui.IImage;

/**
 * The <code>AwtImage</code> represents graphical images.
 * 
 * @author Martin Handsteiner
 */
public class AwtImage extends AwtResource<Image> 
                      implements IImage
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
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
	 * @param pImageName image name
	 * @param pImage java.awt.Image
	 * @see java.awt.Image
	 */
	public AwtImage(String pImageName, Image pImage)
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
		return resource.getWidth(null);
	}

	/**
	 * {@inheritDoc}
	 */
	public int getHeight()
	{
		return resource.getHeight(null);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void saveAs(OutputStream pOut, ImageType pType) throws IOException
	{
		String sType;

		if (pType == null)
		{
			sType = "png";
		}
		else 
		{
			switch (pType)
			{
				case JPG:
					sType = "jpg";
					break;
				case GIF:
					sType = "gif";
					break;
				case BMP:
					sType = "bmp";
					break;
				default:
					sType = "png";
			}
		}
		
		if (resource instanceof RenderedImage)
		{
			ImageIO.write((RenderedImage)resource, sType, pOut);
		}
		else
		{
			BufferedImage image = new BufferedImage(resource.getWidth(null), resource.getHeight(null), BufferedImage.BITMASK);
			Graphics gr = image.getGraphics();
			gr.drawImage(resource, 0, 0, null);
			gr.dispose();
			
			ImageIO.write(image, sType, pOut);
		}
	}

}	// AwtImage
