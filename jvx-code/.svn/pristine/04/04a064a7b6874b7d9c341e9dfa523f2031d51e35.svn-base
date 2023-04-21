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
 * 05.11.2008 - [HM] - creation
 */
package javax.rad.ui.celleditor;

import javax.rad.model.ui.ICellRenderer;

/**
 * Platform and technology independent choice editor definition.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 * @param <C> Placeholder for the library dependent component type.
 */
public interface IImageViewer<C> extends IStyledCellEditor, ICellRenderer<C>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the default image that is shown if selectedIndex is -1.
	 * @return the default image.
	 */
	public String getDefaultImageName();

	/**
	 * Sets the default image name that is shown if selectedIndex is -1.
	 * 
	 * @param pDefaultImageName the default image name.
	 */
	public void setDefaultImageName(String pDefaultImageName);

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

}	// IImageViewer
