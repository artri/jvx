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
import javax.rad.ui.component.ICheckBox;

/**
 * Platform and technology independent checkbox.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 */
public class UICheckBox extends AbstractUIToggleButton<ICheckBox> 
                        implements ICheckBox 
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>UICheckBox</code>.
     *
     * @see ICheckBox
     */
	public UICheckBox()
	{
		this(UIFactoryManager.getFactory().createCheckBox());
	}

    /**
     * Creates a new instance of <code>UICheckBox</code> with the given
     * checkbox.
     *
     * @param pCheckBox the checkbox
     * @see ICheckBox
     */
	protected UICheckBox(ICheckBox pCheckBox)
	{
		super(pCheckBox);
	}
	
    /**
     * Creates a new instance of <code>UICheckBox</code>.
     *
     * @param pText the label of the button.
     * @see ICheckBox
     */
	public UICheckBox(String pText)
	{
		this();
		
		setText(pText);
	}

}	// UICheckBox
