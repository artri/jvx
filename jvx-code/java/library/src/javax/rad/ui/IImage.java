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
 * 25.01.2009 - [JR] - saveAs defined
 * 21.02.2011 - [JR] - #62: saveAs with output stream and type 
 */
package javax.rad.ui;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Platform and technology independent image definition.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF,... .
 * 
 * @author Martin Handsteiner
 * @see	java.awt.Image
 */
public interface IImage extends IResource
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Types
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the save image types. */
	public enum ImageType
	{
		/** Joint Photographic Experts Group. */
		JPG,
		/** Portable Network Graphics. */
		PNG,
		/** Graphic Interchange Format. */
		GIF,
		/** Windows Bitmap. */
		BMP
	};
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the name of the image.
	 * 
	 * @return the name of the image.
	 */
	public String getImageName();

	/**
	 * Determines the width of the image. If the width is not yet known, 
	 * this method returns <code>-1</code>.
	 * 
	 * @return the width of this image, or <code>-1</code> 
	 *         if the width is not yet known.
	 * @see java.awt.Image#getHeight
	 */
	public int getWidth();

	/**
	 * Determines the height of the image. If the height is not yet known, 
	 * this method returns <code>-1</code>.
	 * 
	 * @return the height of this image, or <code>-1</code> 
	 *         if the height is not yet known.
	 * @see java.awt.Image#getWidth
	 */
	public int getHeight();
	
	/**
	 * Saves the current image as file.
	 * 
	 * @param pOut the output stream
	 * @param pType the image type
	 * @throws IOException if the image can not be saved
	 */
	public void saveAs(OutputStream pOut, ImageType pType) throws IOException;

} 	// IImage
