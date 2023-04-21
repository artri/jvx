/*
 * Copyright 2017 SIB Visions GmbH
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
 * 17.10.2017 - [RZ] - Creation
 */
package com.sibvisions.rad.ui.vaadin.ext.data;

import com.vaadin.data.ValueProvider;

/**
 * The {@link PassThroughValueProvider} is a {@link ValueProvider}
 * implementation which just passes on the given value.
 * 
 * @author Robert Zenz
 * @param <T> the type of the value.
 */
public class PassThroughValueProvider<T> implements ValueProvider<T, T>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constants
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The single instance that can be used. */
	public static final PassThroughValueProvider<?> INSTANCE = new PassThroughValueProvider<>();
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link PassThroughValueProvider}.
	 */
	public PassThroughValueProvider()
	{
		super();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public T apply(T pSource)
	{
		return pSource;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Convenience method to get the single {@link #INSTANCE}.
	 * 
	 * @param <T> the type of the value.
	 * @return the single {@link #INSTANCE}.
	 */
	public static final <T> PassThroughValueProvider<T> getInstance()
	{
		return (PassThroughValueProvider<T>)INSTANCE;
	}
	
}	// PassThroughValueProvider
