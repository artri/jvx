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
 * 06.10.2017 - [RZ] - Craetion
 */

package com.sibvisions.rad.ui.vaadin.ext.grid.databinding;

import java.util.stream.Stream;

import com.vaadin.data.provider.AbstractBackEndDataProvider;
import com.vaadin.data.provider.Query;

/**
 * The {@link DummyDataProvider} is an {@link AbstractBackEndDataProvider}
 * extension which provides no data. It can be used as {@code null} value for
 * the data source.
 * 
 * @author Robert Zenz
 */
public class DummyDataProvider extends AbstractBackEndDataProvider<Integer, Object>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constants
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The instance that can be reused. */
	public static final DummyDataProvider INSTANCE = new DummyDataProvider();
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link DummyDataProvider}.
	 */
	public DummyDataProvider()
	{
		super();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Stream<Integer> fetchFromBackEnd(Query<Integer, Object> pQuery)
	{
		return Stream.empty();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected int sizeInBackEnd(Query<Integer, Object> pQuery)
	{
		return 0;
	}
	
}	// DummyDataProvider
