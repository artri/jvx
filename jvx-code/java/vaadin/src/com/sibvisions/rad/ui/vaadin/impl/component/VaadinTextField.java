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
 * 03.10.2012 - [CB] - creation
 */
package com.sibvisions.rad.ui.vaadin.impl.component;

import javax.rad.ui.component.ITextField;

import com.vaadin.ui.TextField;

/**
 * The <code>VaadinTextField</code> is the vaadin implementation of {@link ITextField}.
 * 
 * @author Benedikt Cermak
 */
public class VaadinTextField extends AbstractVaadinTextField<TextField>
                             implements ITextField
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			  
    /**
     * Creates a new instance of <code>VaadinTextField</code>.
     * 
     * @see	javax.rad.ui.component.ITextField
     */
	public VaadinTextField()
	{
		super(new TextField());
	}
	
}	// VaadinTextField
