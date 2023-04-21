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
 * 18.10.2012 - [TK] - creation
 */
package com.sibvisions.rad.ui.vaadin.impl;

import javax.rad.ui.IAlignmentConstants;
import javax.rad.ui.IComponent;

import com.sibvisions.util.log.ILogger.LogLevel;
import com.sibvisions.util.log.LoggerFactory;
import com.sibvisions.util.type.ExceptionUtil;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;

/**
 * The <code>VaadinContainer</code> class extends the base container and handles components size.
 * 
 * @author Thomas Krautinger
 * @param <C> an instance of {@link ComponentContainer}
 */
public abstract class VaadinContainer<C extends ComponentContainer> extends VaadinContainerBase<C, C>
                                                                    implements IAlignmentConstants
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** whether debug log is enabled. */
	private static boolean debug;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	static
	{
		debug = LoggerFactory.getInstance(VaadinContainer.class).isEnabled(LogLevel.DEBUG);
	}

	/**
     * Creates a new instance of <code>VaadinContainer</code>.
     *
     * @param pContainer a AbstractComponentContainer 
     */
	protected VaadinContainer(C pContainer)
	{
		super(pContainer);
	}
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
	/**
	 * {@inheritDoc}
	 */
	public void addToVaadin(IComponent pComponent, Object pConstraints, int pIndex)
	{
		if (debug)
		{
			LoggerFactory.getInstance(VaadinContainer.class).debug(ExceptionUtil.dump(new Exception(">> addToVaadin <<"), true));
		}
		
		resource.addComponent((Component)pComponent.getResource());
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeFromVaadin(IComponent pComponent)
	{
		resource.removeComponent((Component)pComponent.getResource());
	}

}	// VaadinContainer
