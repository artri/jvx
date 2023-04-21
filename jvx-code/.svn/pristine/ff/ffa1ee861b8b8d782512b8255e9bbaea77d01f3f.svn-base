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
 * 19.10.2009 - [JR] - creation
 */
package javax.rad.ui.component;

import javax.rad.ui.IImage;


/**
 * Platform and technology independent toggle action component definition.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author René Jahn
 */
public interface IToggleActionComponent extends IActionComponent
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /**
     * Checks if the component is selected.
     * 
     * @return <code>true</code> if the component is selected, otherwise <code>false</code>
     */
	public boolean isSelected();

    /**
     * Sets the selected state of the component.
     * 
     * @param pPressed <code>true</code> selects the toggle component,
     *                 <code>false</code> deselects the toggle component.
     */
    public void setSelected(boolean pPressed);
    
    /**
     * Sets the image for the button when the button is pressed.
     * 
     * @param pImage the mouse pressed image
     */
    public void setPressedImage(IImage pImage);
    
    /**
     * Gets the image for the button when the button is pressed.
     * 
     * @return the mouse pressed image
     */
    public IImage getPressedImage();
    
    
}	// IToggleActionComponent
