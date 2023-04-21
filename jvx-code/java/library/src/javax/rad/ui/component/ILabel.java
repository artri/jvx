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

/**
 * Platform and technology independent Label definition.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 * @see	java.awt.Label
 * @see	javax.swing.JLabel
 */
public interface ILabel extends IComponent, 
                                IAlignmentConstants
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** 
	 * Returns the text string that the label displays.
	 *
	 * @return a String
	 * @see #setText
	 */
	public String getText();

	/**
	 * Defines the single line of text this component will display.  If
	 * the value of text is null or empty string, nothing is displayed.
	 * 
	 * @param pText the text
	 */
	public void setText(String pText);

}	// ILabel
