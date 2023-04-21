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
package com.sibvisions.rad.ui.swing.impl.component;

import javax.rad.ui.component.IPasswordField;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * The <code>SwingPasswordField</code> is the <code>IPasswordField</code>
 * implementation for swing.
 * 
 * @author Martin Handsteiner
 * @see	java.awt.TextField
 * @see	javax.swing.JPasswordField
 */
public class SwingPasswordField extends SwingTextField<JPasswordField> 
							    implements IPasswordField
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>SwingPasswordField</code>.
	 */
	public SwingPasswordField()
	{
		super(new JPasswordField());
		
		resource.setFont(new JTextField().getFont());
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /**
	 * {@inheritDoc}
	 */
    public char getEchoChar()
    {
    	return component.getEchoChar();
    }

    /**
	 * {@inheritDoc}
	 */
    public void setEchoChar(char pChar)
    {
    	component.setEchoChar(pChar);
    }

}	// SwingPasswordField
