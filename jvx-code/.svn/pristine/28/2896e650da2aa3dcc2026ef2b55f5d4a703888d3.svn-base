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
 * 22.08.2013 - [JR] - creation
 */
package javax.rad.genui;

import javax.rad.ui.IResource;

/**
 * The <code>UIFactoryResource</code> holds the resource per factory. It creates a resource
 * copy if the resource was initialized as static resource.
 * 
 * @author René Jahn
 *
 * @param <UI> the resource type
 */
public abstract class UIFactoryResource<UI extends IResource> extends AbstractUIFactoryResource<UI> 
								                              implements IResource
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>UIFactoryResource</code>.
	 * 
	 * @param pResource the resource
	 */
	public UIFactoryResource(UI pResource)
	{
		super(pResource);
	}
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementations
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public final Object getResource()
	{
		UI ui = super.getUIResource();
		
		if (ui == null)
		{
			return null;
		}
		else
		{
			return ui.getResource();
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Abstract methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract boolean equals(Object pObject);
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract int hashCode();
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract String toString();

}	// UIFactoryResource
