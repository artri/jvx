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
 * 08.12.2008 - [JR] - default button definitions
 * 10.12.2008 - [JR] - moved accelerator methods from IMenuItem
 * 20.07.2009 - [JR] - set/getMargins defined
 * 19.10.2009 - [JR] - extends IActionComponent instead of IButton
 */
package javax.rad.ui.component;

import javax.rad.ui.IImage;

/**
 * Platform and technology independent button definition.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 * @see	java.awt.Button
 * @see	javax.swing.JButton
 */
public interface IButton extends IFormatableButton,
                                 IActionComponent
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Gets, if the border should only be shown on mouse entered.
     *
     * @return true, if the border should only be shown on mouse entered.
     */
    public boolean isBorderOnMouseEntered();

    /**
     * Sets, if the border should only be shown on mouse entered.
     *
     * @param pBorderOnMouseEntered true, if the border should only be shown on mouse entered.
     */
    public void setBorderOnMouseEntered(boolean pBorderOnMouseEntered);
    
    /**
     * Sets whether the border will be painted. If <code>true</code>, the border will be painted. 
     * The default value is that the border will be painted.
     * 
     * @param pBorderPainted <code>true</code> to paint the border, otherwise <code>false</code>
     */
    public void setBorderPainted(boolean pBorderPainted);
    
    /**
     * Gets whether the border will be painted.
     * 
     * @return <code>true</code> when the border will be painted, otherwise <code>false</code>
     */
    public boolean isBorderPainted();
    
    /**
     * Sets the image for the button when the mouse is over.
     * 
     * @param pImage the mouse over image
     */
    public void setMouseOverImage(IImage pImage);
    
    /**
     * Gets the image for the button when the mouse is over.
     * 
     * @return the mouse over image
     */
    public IImage getMouseOverImage();
    
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
    
    /**
     * Sets the <code>defaultButton</code> property.
     * The default button is the button which will be activated 
     * when a UI-defined activation event (typically the <b>Enter</b> key) 
     * occurs, regardless of whether or not the button 
     * has keyboard focus (unless there is another component within 
     * which consumes the activation event)
     * For default activation to work, the button must be enabled.
     *
     * @param pDefault <code>true</code> to set this button as default button, otherwise <code>false</code>
     * @see #isDefaultButton 
     */
    public void setDefaultButton(boolean pDefault);
    
    /**
     * Gets the value of the <code>defaultButton</code> property,
     * which if <code>true</code> means that this button is the current
     * default button.
     *
     * @return <code>true</code> if this button is defined as default button, otherwise <code>false</code>
     * @see #setDefaultButton(boolean)
     */
    public boolean isDefaultButton();
	
}	// IButton
