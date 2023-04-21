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
 */
package javax.rad.ui.component;

import javax.rad.ui.IAlignmentConstants;
import javax.rad.ui.IComponent;
import javax.rad.ui.IImage;

/**
 * Platform and technology independent Icon definition.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 */
public interface IIcon extends IComponent, 
                               IAlignmentConstants
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Method definitions
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Returns the default image.
     * 
     * @return the default image
     * @see #setImage(IImage)
     */
    public IImage getImage();
    
    /**
     * Sets the  default image. 
     *
     * @param pImage the image
     * @see #getImage()
     */
    public void setImage(IImage pImage);
    
    /**
     * If the aspect ratio of the image should be preserved if it is stretched
     * in any direction.
     * 
     * @return {@code true} if the aspect ratio of the image is preserved when stretched.
     */
    public boolean isPreserveAspectRatio();
    
    /**
     * Sets if the aspect ratio of the image should be preserved if it is stretched
     * in any direction.
     * 
     * @param pPreserveAspectRatio {@code true} if the aspect ratio of the image is preserved when stretched.
     */
    public void setPreserveAspectRatio(boolean pPreserveAspectRatio);
    
}	// IIcon
