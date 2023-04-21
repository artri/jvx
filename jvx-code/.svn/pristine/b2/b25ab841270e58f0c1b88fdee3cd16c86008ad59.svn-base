/*
 * Copyright 2016 SIB Visions GmbH
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
 * 02.05.2016 - [RZ] - creation
 */
package com.sibvisions.rad.ui.vaadin.impl.layout;

import javax.rad.ui.IComponent;
import javax.rad.ui.layout.IBorderLayout;

import com.sibvisions.rad.ui.vaadin.ext.ui.panel.layout.BorderLayout;
import com.vaadin.ui.Component;

/**
 * The {@link VaadinClientBorderLayout} is the Vaadin specific implementation of
 * the {@link javax.rad.ui.layout.IFormLayout} class.
 * <p>
 * This class wraps and provides {@link BorderLayout the client-side layout}.
 * 
 * @author Robert Zenz
 */
public class VaadinClientBorderLayout extends AbstractVaadinClientLayout<BorderLayout, String> 
                                      implements IBorderLayout
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link VaadinClientBorderLayout}.
	 */
	public VaadinClientBorderLayout()
	{
		super(new BorderLayout());
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public String getConstraints(IComponent pComp)
	{
		return (String)resource.getConstraints((Component)pComp.getResource());
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setConstraints(IComponent pComp, String pConstraints)
	{
		resource.setConstraints((Component)pComp.getResource(), pConstraints);
	}
	
}	// VaadinClientBorderLayout
