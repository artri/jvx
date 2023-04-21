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
 * 24.10.2017 - [RZ] - Creation
 */
package com.sibvisions.rad.ui.vaadin.ext.data;

import com.vaadin.server.Setter;

/**
 * The {@link DummySetter} is a {@link Setter} implementation which does
 * nothing.
 * 
 * @author Robert Zenz
 * @param <T> the first type.
 * @param <T2> the second type.
 */
public class DummySetter<T extends Object, T2 extends Object> implements Setter<T, T2>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constants
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The single instance that can be used. */
	public static final DummySetter<Object, Object> INSTANCE = new DummySetter<>();
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link DummySetter}.
	 */
	public DummySetter()
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
	public void accept(T pBean, T2 pFieldvalue)
	{
		// Do nothing.
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Convenience method to get the single {@link #INSTANCE}.
	 * 
     * @param <T> the first type.
     * @param <T2> the second type.
	 * @return the single {@link #INSTANCE}.
	 */
	public static final <T, T2> DummySetter<T, T2> getInstance()
	{
		return (DummySetter<T, T2>)INSTANCE;
	}
	
}	// DummySetter
