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
import javax.rad.ui.component.ITextField;

/**
 * Platform and technology independent text field.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 * @see	ITextField
 */
public class UITextField extends AbstractUITextField<ITextField> 
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>UITextField</code>.
     *
     * @see ITextField
     */
	public UITextField()
	{
		this(UIFactoryManager.getFactory().createTextField());
	}

    /**
     * Creates a new instance of <code>UITextField</code> with the given
     * text field.
     *
     * @param pField the text field
     * @see ITextField
     */
	protected UITextField(ITextField pField)
	{
		super(pField);
		
		setColumns(10);
	}
	
    /**
     * Creates a new instance of <code>UITextField</code>.
     *
     * @param pText the text.
     * @see ITextField
     */
	public UITextField(String pText)
	{
		this();
		
		setText(pText);
	}
	
}	// UITextField
