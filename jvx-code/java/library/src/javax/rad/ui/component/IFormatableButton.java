/*
 * Copyright 2021 SIB Visions GmbH
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
 * 08.04.2021 - [JR] - creation
 */
package javax.rad.ui.component;

import javax.rad.ui.IInsets;

/**
 * Platform and technology independent button definition.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author René Jahn
 * @see	java.awt.Button
 * @see	javax.swing.JButton
 */
public interface IFormatableButton extends ILabeledIcon
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Sets space for margin between the button border and
     * the text. Setting to <code>null</code> will cause the component to
     * use the default margin.
     *
     * @param pMargins the space between the border and the test
     */
	public void setMargins(IInsets pMargins);

    /**
     * Returns the margin between the button border and the text.
     * 
     * @return an {@link IInsets} object specifying the margin
     *		   between the component's border and the text
     * @see #setMargins(IInsets)
     */
	public IInsets getMargins();	
	
    /**
     * Returns the amount of space between the text and the icon
     * displayed in this button.
     *
     * @return an int equal to the number of pixels between the text
     *         and the icon.
     * @see #setImageTextGap
     */
    public int getImageTextGap();

    /**
     * If both the icon and text properties are set, this property
     * defines the space between them.  
     * <p>
     * The default value of this property is 4 pixels.
     * <p>
     * This is a JavaBeans bound property.
     * 
	 * @param pImageTextGap the image gap
     * @see #getImageTextGap
     */
    public void setImageTextGap(int pImageTextGap);
	
    /**
     * Sets the vertical position of the text relative to the icon.
     * 
     * @param pVerticalPosition one of the following values:
     * <ul>
     * <li>IAlignmentConstants.CENTER
     * <li>IAlignmentConstants.TOP
     * <li>IAlignmentConstants.BOTTOM
     * </ul>
     */
	public void setVerticalTextPosition(int pVerticalPosition);
	
    /**
     * Returns the vertical position of the text relative to the icon.
     * 
     * @return one of the following values:
     * <ul>
     * <li>IAlignmentConstants.CENTER
     * <li>IAlignmentConstants.TOP
     * <li>IAlignmentConstants.BOTTOM
     * </ul>
     */
	public int getVerticalTextPosition();

    /**
     * Sets the horizontal position of the text relative to the icon.
     * 
     * @param pHorizontalPosition one of the following values:
     * <ul>
     * <li>IAlignmentConstants.CENTER
     * <li>IAlignmentConstants.LEFT
     * <li>IAlignmentConstants.RIGHT
     * </ul>
     */
	public void setHorizontalTextPosition(int pHorizontalPosition);
	
    /**
     * Returns the horizontal position of the text relative to the icon.
     * 
     * @return one of the following values:
     * <ul>
     * <li>IAlignmentConstants.CENTER
     * <li>IAlignmentConstants.LEFT
     * <li>IAlignmentConstants.RIGHT
     * </ul>
     */
	public int getHorizontalTextPosition();
	
}	// IFormatableButton
