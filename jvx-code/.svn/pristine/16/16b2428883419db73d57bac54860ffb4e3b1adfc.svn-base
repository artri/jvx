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
 * 16.11.2008 - [HM] - creation
 * 24.10.2012 - [JR] - #604: added constructor
 */
package javax.rad.genui.component;

import javax.rad.genui.UIFactoryManager;
import javax.rad.ui.component.IRadioButton;

/**
 * Platform and technology independent radio button.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 */
public class UIRadioButton extends AbstractUIToggleButton<IRadioButton> 
                           implements IRadioButton
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>UIRadioButton</code>.
     *
     * @see IRadioButton
     */
	public UIRadioButton()
	{
		this(UIFactoryManager.getFactory().createRadioButton());
	}

    /**
     * Creates a new instance of <code>UIRadioButton</code> with the given
     * radio button.
     *
     * @param pButton the radio button
     * @see IRadioButton
     */
	protected UIRadioButton(IRadioButton pButton)
	{
		super(pButton);
	}
	
    /**
     * Creates a new instance of <code>UIRadioButton</code>.
     *
     * @param pText the label of the button.
     * @see IRadioButton
     */
	public UIRadioButton(String pText)
	{
		this();

		setText(pText);
	}

}	// UIRadioButton
