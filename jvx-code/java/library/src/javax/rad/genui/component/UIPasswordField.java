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
import javax.rad.ui.component.IPasswordField;

/**
 * Platform and technology independent password field.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 */
public class UIPasswordField extends AbstractUITextField<IPasswordField> 
                             implements IPasswordField
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>UIPasswordField</code>.
     *
     * @see IPasswordField
     */
	public UIPasswordField()
	{
		this(UIFactoryManager.getFactory().createPasswordField());
	}

    /**
     * Creates a new instance of <code>UIPasswordField</code> with the given
     * password field.
     *
     * @param pField the password field
     * @see IPasswordField
     */
	protected UIPasswordField(IPasswordField pField)
	{
		super(pField);
		
		setColumns(10);
	}
	
    /**
     * Creates a new instance of <code>UIPasswordField</code>.
     *
     * @param pText the text.
     * @see IPasswordField
     */
	public UIPasswordField(String pText)
	{
		this();
		
		setText(pText);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface Implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
    public char getEchoChar()
    {
    	return uiResource.getEchoChar();
    }

	/**
	 * {@inheritDoc}
	 */
    public void setEchoChar(char pChar)
    {
    	uiResource.setEchoChar(pChar);
    }

}	// UIPasswordField
