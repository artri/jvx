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
 * 18.10.2012 - [CB] - creation
 * 24.09.2013 - [JR] - support null resource
 */
package com.sibvisions.rad.ui.vaadin.impl;

import javax.rad.ui.IResource;

/**
 * The <code>VaadinResourceBase</code> class is the base class for alls Vaadin UI classes. It offers
 * access to the underlaying platform dependent (original) resource.
 *
 * @author Benedikt Cermak
 * @param <R> encapsulated resource type
 */
public abstract class VaadinResourceBase<R> implements IResource 
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** Encapsulated resource. */
    protected R resource;

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Creates a new instance of <code>VaadinResourceBase</code>.
     */
    @SuppressWarnings("unchecked")
	protected VaadinResourceBase() 
    {
    	resource = (R)this;
    }

    /**
     * Creates a new instance of <code>VaadinResourceBase</code> for the given
     * resource.
     * 
     * @param pResource the platform dependent resource
     */
    protected VaadinResourceBase(R pResource) 
    {
        resource = pResource;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
	@Override
	public boolean equals(Object pObject) 
	{
		if (pObject instanceof IResource) 
		{
			Object obj = ((IResource)pObject).getResource();
			
			if (resource == obj)
			{
				return true;
			}
			
			if (resource != null)
			{
				return false;
			}
			else 
			{
				if (obj == null)
				{
					return false;
				}
			
				return resource == this ? super.equals(obj) : resource.equals(obj);
			}
		} 
		else 
		{
			return false;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() 
	{
		return resource != null ? (resource == this ? super.hashCode() : resource.hashCode()) : 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() 
	{
		if (resource == this)
		{
			return getClass().getName();
		}
		else
		{
			return getClass().getName() + "[" + String.valueOf(resource) + "]";
		}
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public Object getResource() 
	{
		//Object return type is needed because sometimes the resource is not the same type as R
		//e.g. layouts
		return resource;
	}
	
} 	// VaadinResourceBase
