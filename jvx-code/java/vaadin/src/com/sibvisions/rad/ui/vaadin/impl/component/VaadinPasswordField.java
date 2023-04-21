/*
 * Copyright 2012 SIB Visions GmbH
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
 * 10.10.2012 - [CB] - creation
 */
package com.sibvisions.rad.ui.vaadin.impl.component;

import javax.rad.ui.component.IPasswordField;

import com.vaadin.ui.PasswordField;

/**
 * The <code>VaadinPasswordField</code> class is the vaadin implementation of {@link IPasswordField}.
 *
 * @author Benedikt Cermak
 * @see com.vaadin.ui.PasswordField
 */
public class VaadinPasswordField extends AbstractVaadinTextField<PasswordField>
                                 implements IPasswordField
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>VaadinPasswordField</code>.
     *
     * @see IPasswordField
     */
	public VaadinPasswordField()
	{
		super(new PasswordField());
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface Implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
    public char getEchoChar()
    {
    	return Character.valueOf('.').charValue();
    }

	/**
	 * {@inheritDoc}
	 */
    public void setEchoChar(char pChar)
    {
    	// not supported
    }

}	// VaadinPasswordField
