/*
 * Copyright 2013 SIB Visions GmbH
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
 * 24.09.2013 - [CB] - creation
 */
package com.sibvisions.rad.ui.vaadin.impl;

/**
 * The <code>VaadinResource</code> class is a {@link VaadinResourceBase} that defines the resource type (for internal usage)
 * and the component type which will be used for accessing the resource via {@link #getResource()}. This class allows 
 * type-safe resource access.
 * 
 * @author René Jahn
 *
 * @param <C> the component type returned from the resource
 * @param <R> the resource type
 */
public abstract class VaadinResource<C, R> extends VaadinResourceBase<R> 
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Creates a new instance of <code>VaadinResource</code> for the given
     * resource.
     * 
     * @param pResource the platform dependent resource
     */
    protected VaadinResource(R pResource) 
    {
        super(pResource);
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public C getResource() 
	{
		return (C)resource;
	}
	
} 	// VaadinResourceBase
